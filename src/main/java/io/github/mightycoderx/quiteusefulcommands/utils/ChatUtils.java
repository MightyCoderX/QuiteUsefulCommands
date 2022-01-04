package io.github.mightycoderx.quiteusefulcommands.utils;

import io.github.mightycoderx.quiteusefulcommands.QuiteUsefulCommands;
import io.github.mightycoderx.quiteusefulcommands.commands.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

public class ChatUtils
{
	public static String pluginName = ChatUtils.color("&bQuite&3Useful&5Commands&r");
	public static String pluginHeader = ChatUtils.color("&3--------------&b«" + pluginName + "&b»&3--------------&r");
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
		sendMessage(to, "&3/" + cmd.getName() + " " + cmd.getUsage() + "&8 - &b" + description);
	}

	public static String color(String message)
	{
		return ChatColor.translateAlternateColorCodes('&', message);
	}
}
