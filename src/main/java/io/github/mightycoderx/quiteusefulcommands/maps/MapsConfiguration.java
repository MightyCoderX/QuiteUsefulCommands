package io.github.mightycoderx.quiteusefulcommands.maps;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class MapsConfiguration
{
	private final CustomMapManager mapManager;

	private final File mapsFile;
	private FileConfiguration mapsFileConfig;

	public MapsConfiguration(CustomMapManager mapManager)
	{
		this.mapManager = mapManager;

		if(!mapManager.getPlugin().getDataFolder().exists()) mapManager.getPlugin().getDataFolder().mkdir();

		mapsFile = new File(mapManager.getPlugin().getDataFolder(), "maps.yml");

		try
		{
			if(!mapsFile.exists())
			{
				mapsFile.createNewFile();
			}

			mapsFileConfig = new YamlConfiguration();
			mapsFileConfig.load(mapsFile);
		}
		catch (InvalidConfigurationException | IOException ex)
		{
			ex.printStackTrace();
		}
	}

	public void saveMaps()
	{
		Map<Integer, Object> maps = mapManager.getMaps();
		for(int id : maps.keySet())
		{
			writeMap(id, maps.get(id));
		}

		try
		{
			mapsFileConfig.save(mapsFile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void writeMap(int id, Object obj)
	{
		if(obj instanceof MapImage mapImage)
		{
			mapsFileConfig.set("maps." + id + ".url", mapImage.getUrl().toString());
			mapsFileConfig.set("maps." + id + ".shouldScaleDown", mapImage.shouldScaleDown());
			mapsFileConfig.set("maps." + id + ".startPos.x", mapImage.getStartPos().x);
			mapsFileConfig.set("maps." + id + ".startPos.y", mapImage.getStartPos().y);
		}
		else if(obj instanceof Byte color)
		{
			mapsFileConfig.set("maps." + id, color);
		}
		else
		{
			Bukkit.getLogger().severe("Cannot save an invalid map!");
		}
	}

	public void loadMaps()
	{
		ConfigurationSection mapsSection = mapsFileConfig.getConfigurationSection("maps");
		if(mapsSection == null)
		{
			return;
		}

		for(String id : mapsSection.getKeys(false))
		{
			readMap(mapsSection, id);
		}

		mapManager.renderAllMaps();
	}


	public void readMap(ConfigurationSection mapsSection, String id)
	{
		try
		{
			Byte obj = Byte.parseByte(mapsSection.getString(id));
			mapManager.getMaps().put(Integer.parseInt(id), obj);
		}
		catch (NumberFormatException ex)
		{
			try
			{
				URL url = new URL(mapsSection.getString(id + ".url"));
				MapImage.Vector startPos = readImageMapStartPos(
						mapsSection.getConfigurationSection(id + ".startPos")
				);

				mapManager.getMaps().put(Integer.parseInt(id),
					new MapImage(
						url,
						startPos,
						mapsSection.getBoolean(id + ".shouldScaleDown")
					)
				);
			}
			catch (MalformedURLException | NullPointerException ex1)
			{
				Bukkit.getServer().getLogger().severe("Invalid value in maps.yml at id " + id);
			}
		}
	}

	public MapImage.Vector readImageMapStartPos(ConfigurationSection startPosSection)
	{
		return new MapImage.Vector(
			startPosSection.getInt("x"),
			startPosSection.getInt("y")
		);
	}
}
