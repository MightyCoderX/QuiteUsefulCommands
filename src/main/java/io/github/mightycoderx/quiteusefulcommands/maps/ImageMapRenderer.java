package io.github.mightycoderx.quiteusefulcommands.maps;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.*;

public class ImageMapRenderer extends MapRenderer
{
	private final Image image;
	private final boolean shouldScaleDown;
	private final MapImage.Vector startPos;

	public ImageMapRenderer(Image image, MapImage.Vector startPos, boolean shouldScaleDown)
	{
		this.image = image;
		this.startPos = startPos;
		this.shouldScaleDown = shouldScaleDown;
	}

	@Override
	public void render(MapView map, MapCanvas canvas, Player player)
	{
		if(image == null) return;

		Image img = image;
		if(shouldScaleDown)
		{
			img = MapPalette.resizeImage(image);
		}
		canvas.drawImage(-startPos.x, -startPos.y, img);
	}
}
