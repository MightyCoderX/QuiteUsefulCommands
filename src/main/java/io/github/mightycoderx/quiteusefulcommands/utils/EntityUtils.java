package io.github.mightycoderx.quiteusefulcommands.utils;

import org.bukkit.Location;

public class EntityUtils
{
	public static double getDistanceFromGround(Location entityLoc, double accuracy)
	{
		Location loc = entityLoc.clone();
		double distance = 0;
		for (double i = loc.getY(); i >= 0; i -= accuracy)
		{
			loc.setY(i);
			distance += accuracy;
			if (loc.getBlock().getType().isSolid()) // Makes a little more sense than checking if it's air
				break;
		}
		return distance;
	}
}
