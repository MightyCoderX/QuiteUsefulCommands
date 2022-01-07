package io.github.mightycoderx.quiteusefulcommands.commands;

import com.mojang.datafixers.util.Pair;
import io.github.mightycoderx.quiteusefulcommands.QuiteUsefulCommands;
import io.github.mightycoderx.quiteusefulcommands.maps.ColorMapRenderer;
import io.github.mightycoderx.quiteusefulcommands.maps.ImageMapRenderer;
import io.github.mightycoderx.quiteusefulcommands.maps.MapImage;
import io.github.mightycoderx.quiteusefulcommands.utils.ChatUtils;
import io.github.mightycoderx.quiteusefulcommands.utils.HTTPUtils;
import io.github.mightycoderx.quiteusefulcommands.utils.MapUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class CustomMapCommand extends Command
{
	private final QuiteUsefulCommands plugin;

	public CustomMapCommand(QuiteUsefulCommands plugin)
	{
		super("custommap", "<color <color> | image <url> [<startPosX> <startPosY>] [<scaleDown>] | big_image <url>>");
		this.plugin = plugin;
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args)
	{
		if(!(sender instanceof Player player))
		{
			ChatUtils.sendPrefixedMessage(sender, CommandMessage.ONLY_PLAYERS_COMMAND);
			return true;
		}

		if(args.length == 2)
		{
			if(args[0].equals("image"))
			{
				giveImageCustomMap(player, args[1], new MapImage.Vector(), false);
			}
			else if(args[0].equals("color"))
			{
				String rawColor = args[1].toUpperCase();
				Color color = null;

				try
				{
					color = Color.decode(rawColor);
				}
				catch (NumberFormatException ex)
				{
					ChatUtils.sendPrefixedMessage(player, "&cInvalid color integer");
					return true;
				}

				byte finalColor = MapPalette.matchColor(color);

				ItemStack customMap = createCustomMap(player.getWorld(), new ColorMapRenderer(finalColor));

				plugin.getCustomMapManager().addMap(
					((MapMeta) customMap.getItemMeta()).getMapView().getId(),
					Byte.valueOf(finalColor)
				);

				player.getInventory().addItem(customMap);

				ChatColor chatColor = MapUtils.mapColorToChatColor(finalColor);
				ChatUtils.sendPrefixedMessage(player, "&aGenerated " + chatColor +
						chatColor.name() + "&a map!");
			}
			else if(args[0].equals("big_image"))
			{
				giveBigImageCustomMaps(player, args[1]);
			}
			else
			{
				return false;
			}
		}
		else if(args.length == 3)
		{
			if(args[0].equals("image"))
			{
				boolean scaleDown = Boolean.parseBoolean(args[2]);
				giveImageCustomMap(player, args[1], new MapImage.Vector(), scaleDown);
			}
			else
			{
				return false;
			}
		}
		else if(args.length == 4)
		{
			if(args[0].equals("image"))
			{
				MapImage.Vector startPos = new MapImage.Vector(
					Integer.parseInt(args[2]),
					Integer.parseInt(args[3])
				);

				giveImageCustomMap(player, args[1], startPos, false);
			}
			else
			{
				return false;
			}
		}
		else if(args.length == 5)
		{
			if(args[0].equals("image"))
			{
				boolean scaleDown = Boolean.parseBoolean(args[4]);

				MapImage.Vector startPos = null;

				try
				{
					startPos = new MapImage.Vector(
							Integer.parseInt(args[2]),
							Integer.parseInt(args[3])
					);
				}
				catch (NumberFormatException ex)
				{
					ChatUtils.sendPrefixedMessage(player, "&cInvalid starPos coordinates!");
					return true;
				}


				giveImageCustomMap(player, args[1], startPos, scaleDown);
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}

		return true;
	}

	private ItemStack createCustomMap(World world, MapRenderer renderer)
	{
		ItemStack customMap = new ItemStack(Material.FILLED_MAP);
		MapMeta mapMeta = (MapMeta) customMap.getItemMeta();

		MapView customMapView = Bukkit.createMap(world);

		customMapView.getRenderers().clear();
		customMapView.addRenderer(renderer);

		mapMeta.setMapView(customMapView);
		customMap.setItemMeta(mapMeta);
		return customMap;
	}

	private void giveBigImageCustomMaps(Player player, String rawUrl)
	{
		Pair<Image, URL> imageURLPair = tryGetImageFromRawUrl(player, rawUrl);
		BufferedImage bigImage = (BufferedImage) imageURLPair.getFirst();

		if(bigImage == null)
		{
			ChatUtils.sendPrefixedMessage(player, "&cCould not load image from url: " + rawUrl);
			Bukkit.getServer().getLogger().severe(ChatUtils.color("&cCould not load image from url: ") + rawUrl);
			return;
		}

		if(bigImage.getWidth() <= 128 && bigImage.getHeight() <= 128)
		{
			giveImageCustomMap(player, rawUrl, new MapImage.Vector(), false);
			return;
		}

		int w = bigImage.getWidth();
		int h = bigImage.getHeight();

		for(int y = 0; y < h; y++)
		{
			if(y % 128 != 0) continue;

			for(int x = 0; x < w; x++)
			{
				if(x % 128 != 0) continue;

				MapImage.Vector startPos = new MapImage.Vector(x, y);

				ItemStack customMap = createCustomMap(player.getWorld(),
						new ImageMapRenderer(bigImage, startPos, false));

				Map<Integer, ItemStack> itemsLeft = player.getInventory().addItem(customMap);

				itemsLeft.values().forEach(i -> player.getWorld().dropItem(player.getEyeLocation(), i));

				plugin.getCustomMapManager().addMap(
						((MapMeta) customMap.getItemMeta()).getMapView().getId(),
						new MapImage(imageURLPair.getSecond(), startPos, false)
				);
			}
		}

		ChatUtils.sendPrefixedMessage(player, "&aGenerated all the needed maps!");
	}

	private Pair<Image, URL> tryGetImageFromRawUrl(Player player, String rawUrl)
	{
		URL url = null;

		try
		{
			url = new URL(rawUrl);

		}
		catch (MalformedURLException ex)
		{
			ChatUtils.sendPrefixedMessage(player, "&cInvalid url");
			return null;
		}

		HTTPUtils httpUtils = new HTTPUtils(plugin);

		AtomicReference<Image> image = new AtomicReference<>();

		httpUtils.getImageFromUrl(url, image::set);

		return new Pair<>(image.get(), url);
	}

	private void giveImageCustomMap(Player player, String rawUrl, MapImage.Vector startPos, boolean scaleDown)
	{
		Pair<Image, URL> imageURLPair = tryGetImageFromRawUrl(player, rawUrl);
		Image image = imageURLPair.getFirst();

		if(image == null)
		{
			ChatUtils.sendPrefixedMessage(player, "&cCould not load image from url: " + rawUrl);
			Bukkit.getServer().getLogger().severe(ChatUtils.color("&cCould not load image from url: ") + rawUrl);
			return;
		}

		ItemStack customMap = createCustomMap(player.getWorld(), new ImageMapRenderer(image, startPos, scaleDown));

		player.getInventory().addItem(customMap);

		plugin.getCustomMapManager().addMap(
			((MapMeta) customMap.getItemMeta()).getMapView().getId(),
			new MapImage(imageURLPair.getSecond(), startPos, scaleDown)
		);

		ChatUtils.sendPrefixedMessage(player, "&aGenerated map from image " + imageURLPair.getSecond());
	}

	@Override
	public List<String> tabComplete(Player player, String alias, String[] args)
	{
		if(args.length == 1)
		{
			return Stream.of("color", "image", "big_image").filter(arg -> arg.startsWith(args[0])).toList();
		}
		else if(args.length == 2 && args[0].endsWith("image"))
		{
			return Stream.of("http://", "https://").filter(arg -> arg.startsWith(args[1])).toList();
		}
		else if(args.length == 3 && args[0].equals("image"))
		{
			return Stream.of("true", "false").filter(arg -> arg.startsWith(args[2])).toList();
		}
		else if(args.length == 5 && args[0].equals("image"))
		{
			return Stream.of("true", "false").filter(arg -> arg.startsWith(args[4])).toList();
		}

		return List.of();
	}
}