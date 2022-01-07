package io.github.mightycoderx.quiteusefulcommands.maps;

import io.github.mightycoderx.quiteusefulcommands.QuiteUsefulCommands;
import io.github.mightycoderx.quiteusefulcommands.utils.HTTPUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapPalette;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class CustomMapManager
{
	private final QuiteUsefulCommands plugin;
	private final Map<Integer, Object> maps = new HashMap<>();
	private final MapsConfiguration mapsConfiguration;

	public CustomMapManager(QuiteUsefulCommands plugin)
	{
		this.plugin = plugin;
		this.mapsConfiguration = new MapsConfiguration(this);
		mapsConfiguration.loadMaps();
	}

	public void addMap(int mapId, Object obj)
	{
		maps.put(mapId, obj);
		mapsConfiguration.saveMaps();
	}

	public void renderAllMaps()
	{
		for(int id : maps.keySet())
		{
			for(Player player : Bukkit.getOnlinePlayers())
			{
				for(ItemStack item : player.getInventory().all(Material.FILLED_MAP).values())
				{
					if(((MapMeta) item.getItemMeta()).getMapView().getId() != id) continue;

					MapMeta mapMeta = (MapMeta) item.getItemMeta();

					mapMeta.getMapView().getRenderers().clear();

					Object obj = maps.get(id);

					if(obj instanceof MapImage mapImage)
					{
						HTTPUtils httpUtils = new HTTPUtils(plugin);

						AtomicReference<Image> image = new AtomicReference<>();

						httpUtils.getImageFromUrl(mapImage.getUrl(), image::set);

						mapMeta.getMapView().addRenderer(
							new ImageMapRenderer(
								image.get(),
								mapImage.getStartPos(),
								mapImage.shouldScaleDown()
							)
						);
					}
					else if(obj instanceof Byte color)
					{
						mapMeta.getMapView().addRenderer(new ColorMapRenderer(color));
					}

					item.setItemMeta(mapMeta);
				}
			}
		}
	}

	public QuiteUsefulCommands getPlugin()
	{
		return plugin;
	}

	public Map<Integer, Object> getMaps()
	{
		return maps;
	}

	public MapsConfiguration getMapsConfiguration()
	{
		return mapsConfiguration;
	}
}
