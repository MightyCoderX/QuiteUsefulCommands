package io.github.mightycoderx.quiteusefulcommands.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class Command
{
	private final String name;
	private final String usage;

	public Command(String name, String usage)
	{
		this.name = name;
		this.usage = usage;
	}

	public abstract boolean perform(CommandSender sender, String label, String[] args);

	public List<String> tabComplete(CommandSender sender, String alias, String[] args)
	{
		if(sender instanceof Player)
		{
			return tabComplete((Player) sender, alias, args);
		}

		return List.of();
	}

	public List<String> tabComplete(Player player, String alias, String[] args) { return List.of(); }

	public String getName()
	{
		return name;
	}

	public String getUsage()
	{
		return usage;
	}
}
