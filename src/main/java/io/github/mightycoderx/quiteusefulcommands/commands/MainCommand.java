package io.github.mightycoderx.quiteusefulcommands.commands;

import io.github.mightycoderx.quiteusefulcommands.utils.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

import java.util.List;

public class MainCommand extends Command
{
	private final CommandManager commandManager;

	public MainCommand(CommandManager commandManager)
	{
		super("quiteusefulcommands", "[help | reload]");

		this.commandManager = commandManager;
	}

	@Override
	public boolean perform(CommandSender sender, String label, String[] args)
	{
		if(args.length == 0)
		{
			ChatUtils.sendMessage(sender, ChatUtils.pluginHeader);
			for(Command cmd : commandManager.getCommands())
			{
				ChatUtils.sendCommandUsage(sender, cmd);
			}
			ChatUtils.sendMessage(sender, "");
		}
		else if(args.length == 1)
		{
			if(args[0].equals("help"))
			{
				ChatUtils.sendMessage(sender, ChatUtils.pluginHeader);
				for(Command cmd : commandManager.getCommands())
				{
					ChatUtils.sendCommandUsage(sender, cmd);
				}
				ChatUtils.sendMessage(sender, "");
			}
			else if(args[0].equals("reload"))
			{
				commandManager.getPlugin().reloadConfig();
				commandManager.getPlugin().getCustomMapManager().getMapsConfiguration().reload();
				ChatUtils.sendPrefixedMessage(sender, "&aAll configs reloaded!");
			}
			else
			{
				return false;
			}
		}
		else if(args.length == 2)
		{
			if(args[0].equals("help"))
			{
				Command cmd = commandManager.getCommand(args[1]);
				PluginCommand pluginCommand = commandManager.getPlugin().getCommand(args[1]);

				if(cmd == null || pluginCommand == null)
				{
					ChatUtils.sendPrefixedMessage(sender, "&cCommand " + ChatColor.stripColor(args[1]) + " doesn't exist!");
					return true;
				}

				String description = pluginCommand.getDescription();
				ChatUtils.sendMessage(sender, ChatUtils.pluginHeader);
				ChatUtils.sendMessage(sender, "&3/" + cmd.getName() + " " + cmd.getUsage() + " &b" + description);
				ChatUtils.sendMessage(sender, "");
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}

		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args)
	{
		if(args.length == 1)
		{
			return List.of("help","reload");
		}
		else if(args.length == 2)
		{
			if(args[0].equals("help"))
			{
				return commandManager.getCommandNames();
			}
		}

		return List.of();
	}

}
