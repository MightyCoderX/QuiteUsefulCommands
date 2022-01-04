package io.github.mightycoderx.quiteusefulcommands.maps;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class ColorMapRenderer extends MapRenderer
{
	private final byte color;

	public ColorMapRenderer(byte color)
	{
		this.color = color;
	}

	@Override
	public void render(MapView map, MapCanvas canvas, Player player)
	{
		for(int x = 0; x < 128; x++)
		{
			for(int y = 0; y < 128; y++)
			{
				canvas.setPixel(x, y, color);
			}
		}
	}
}
