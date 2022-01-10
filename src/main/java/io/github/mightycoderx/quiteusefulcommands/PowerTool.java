package io.github.mightycoderx.quiteusefulcommands;

import org.bukkit.Material;

public class PowerTool
{
	private final Material item;
	private final String command;

	public PowerTool(Material item, String command)
	{
		this.item = item;
		this.command = command;
	}

	public Material getItem()
	{
		return item;
	}

	public String getCommand()
	{
		return command;
	}
}
