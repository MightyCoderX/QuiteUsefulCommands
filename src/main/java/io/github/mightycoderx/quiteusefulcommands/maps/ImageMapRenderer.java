package io.github.mightycoderx.quiteusefulcommands.maps;

import org.bukkit.entity.Player;
import org.bukkit.map.*;

import java.awt.*;

public class ImageMapRenderer extends MapRenderer
{
	private final Image image;
	private final MapImage.Vector startPos;

	public ImageMapRenderer(Image image, MapImage.Vector startPos, boolean scaleDown)
	{
		this.image = scaleDown ? MapPalette.resizeImage(image) : image;
		this.startPos = startPos;
	}

	@Override
	public void render(MapView map, MapCanvas canvas, Player player)
	{
		if(image == null) return;

		canvas.drawImage(-startPos.getX(), -startPos.getY(), image);
	}
}
