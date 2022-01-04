package io.github.mightycoderx.quiteusefulcommands.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.function.Consumer;

public class HTTPUtils
{
	private final JavaPlugin plugin;

	public HTTPUtils(JavaPlugin plugin)
	{
		this.plugin = plugin;
	}

	public void getImageFromUrl(URL url, Consumer<Image> imageCallback)
	{
		try
		{
			imageCallback.accept(ImageIO.read(url));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
