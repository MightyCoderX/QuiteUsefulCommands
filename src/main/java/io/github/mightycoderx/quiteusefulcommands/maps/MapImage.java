package io.github.mightycoderx.quiteusefulcommands.maps;

import java.net.URL;

public class MapImage
{
	private final URL url;
	private final Vector startPos;
	private final boolean scaleDown;

	public MapImage(URL url, Vector startPos, boolean scaleDown)
	{
		this.url = url;
		this.startPos = startPos;
		this.scaleDown = scaleDown;
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
		return scaleDown;
	}

	public static class Vector
	{
		private final int x;
		private final int y;

		public Vector(int x, int y)
		{
			this.x = x;
			this.y = y;
		}

		public Vector()
		{
			this.x = 0;
			this.y = 0;
		}

		public int getX()
		{
			return x;
		}

		public int getY()
		{
			return y;
		}
	}
}
