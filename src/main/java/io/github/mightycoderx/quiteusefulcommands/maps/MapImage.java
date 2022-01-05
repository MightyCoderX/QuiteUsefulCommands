package io.github.mightycoderx.quiteusefulcommands.maps;

import java.net.URL;

public class MapImage
{
	private final URL url;
	private final boolean shouldScaleDown;
	private final Vector startPos;

	public MapImage(URL url, Vector startPos, boolean shouldScaleDown)
	{
		this.url = url;
		this.startPos = startPos;
		this.shouldScaleDown = shouldScaleDown;
	}

	public URL getUrl()
	{
		return url;
	}

	public Vector getStartPos()
	{
		return startPos;
	}

	public boolean shouldScaleDown()
	{
		return shouldScaleDown;
	}

	public static class Vector
	{
		public int x;
		public int y;

		public Vector(int x, int y)
		{
			this.x = x;
			this.y = y;
		}
	}
}
