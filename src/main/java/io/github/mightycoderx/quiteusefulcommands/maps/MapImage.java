package io.github.mightycoderx.quiteusefulcommands.maps;

import java.net.URL;

public class MapImage
{
	private final URL url;
	private final boolean shouldScaleDown;

	public MapImage(URL url, boolean shouldScaleDown)
	{
		this.url = url;
		this.shouldScaleDown = shouldScaleDown;
	}

	public URL getUrl()
	{
		return url;
	}

	public boolean shouldScaleDown()
	{
		return shouldScaleDown;
	}
}
