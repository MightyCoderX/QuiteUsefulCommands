package io.github.mightycoderx.quiteusefulcommands.utils;

import io.github.mightycoderx.quiteusefulcommands.QuiteUsefulCommands;
import io.github.mightycoderx.quiteusefulcommands.commands.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

public class ChatUtils
{
	public static String pluginName = ChatUtils.color("&bQuite&3Useful&5Commands&r");
	public static String pluginHeader = ChatUtils.color("&3--------------&8«" + pluginName + "&8»&3--------------&r");
	public static String pluginPrefix = ChatUtils.color("&bQ&3U&5C&r");

	public static void sendPrefixedMessage(CommandSender to, String message)
	{
		to.sendMessage(pluginPrefix + color("&7\u00BB " + message + "&r"));
	}

	public static void sendMessage(CommandSender to, String message)
	{
		to.sendMessage(color(message));
	}

	public static void sendCommandUsage(CommandSender to, Command cmd)
	{
		PluginCommand pluginCommand = QuiteUsefulCommands.getPlugin(QuiteUsefulCommands.class).getCommand(cmd.getName());
		if(pluginCommand == null) return;

		String description = pluginCommand.getDescription();
		sendMessage(to, commandUsage(cmd.getName(), cmd.getUsage(), description));

		for(String alias : pluginCommand.getAliases())
		{
			sendMessage(to, commandUsage(alias, cmd.getUsage(), description));
		}
	}

	public static String commandUsage(String name, String usage, String description)
	{
		return "&3/" + name + " &5" + usage + "&8 - &b" + description;
	}

	public static String color(String message)
	{
		return ChatColor.translateAlternateColorCodes('&', message);
	}
}
