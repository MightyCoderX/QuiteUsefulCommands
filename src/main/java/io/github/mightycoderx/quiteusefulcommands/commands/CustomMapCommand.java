package io.github.mightycoderx.quiteusefulcommands.commands;

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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class CustomMapCommand extends Command
{
	private final QuiteUsefulCommands plugin;

	public CustomMapCommand(QuiteUsefulCommands plugin)
	{
		super("custommap", "<image <url> [<startPosX>] [<startPosY>] [<shouldScaleDown>] | color <color>>");
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
				giveImageCustomMap(player, args[1], new MapImage.Vector(0, 0), false);
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
			else
			{
				return false;
			}
		}
		else if(args.length == 3)
		{
			if(args[0].equals("image"))
			{
				boolean shouldScaleDown = Boolean.parseBoolean(args[2]);
				giveImageCustomMap(player, args[1], new MapImage.Vector(0, 0), shouldScaleDown);
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
				boolean shouldScaleDown = Boolean.parseBoolean(args[4]);

				MapImage.Vector startPos = new MapImage.Vector(
						Integer.parseInt(args[2]),
						Integer.parseInt(args[3])
				);

				giveImageCustomMap(player, args[1], startPos, shouldScaleDown);
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

	private void giveImageCustomMap(Player player, String rawUrl, MapImage.Vector startPos, boolean shouldScaleDown)
	{
		URL url = null;

		try
		{
			url = new URL(rawUrl);

		}
		catch (MalformedURLException ex)
		{
			ChatUtils.sendPrefixedMessage(player, "&cInvalid url");
			return;
		}

		HTTPUtils httpUtils = new HTTPUtils(plugin);

		AtomicReference<Image> image = new AtomicReference<>();

		httpUtils.getImageFromUrl(url, image::set);

		if(image.get() == null)
		{
			ChatUtils.sendPrefixedMessage(player, "&cCould not load image from url: " + rawUrl);
			Bukkit.getServer().getLogger().severe(ChatUtils.color("&cCould not load image from url: ") + rawUrl);
			return;
		}

		ItemStack customMap = createCustomMap(player.getWorld(), new ImageMapRenderer(image.get(), startPos, shouldScaleDown));

		player.getInventory().addItem(customMap);

		plugin.getCustomMapManager().addMap(
			((MapMeta) customMap.getItemMeta()).getMapView().getId(),
			new MapImage(url, startPos, shouldScaleDown)
		);

		ChatUtils.sendPrefixedMessage(player, "&aGenerated map from image " + url);
	}

	@Override
	public List<String> tabComplete(Player player, String alias, String[] args)
	{
		if(args.length == 1)
		{
			return Stream.of("color","image").filter(arg -> args[0].startsWith(arg)).toList();
		}
		else if(args.length == 3)
		{
			return Stream.of("true", "false").filter(arg -> args[2].startsWith(arg)).toList();
		}
		else if(args.length == 5)
		{
			return Stream.of("true", "false").filter(arg -> args[4].startsWith(arg)).toList();
		}

		return List.of();
	}
}