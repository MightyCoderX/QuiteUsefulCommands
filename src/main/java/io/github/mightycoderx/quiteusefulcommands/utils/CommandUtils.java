package io.github.mightycoderx.quiteusefulcommands.utils;

import io.github.mightycoderx.quiteusefulcommands.commands.CommandMessage;
import net.minecraft.network.PacketListener;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandUtils
{
	public static Player getPlayerFromArg(CommandSender sender, String arg)
	{
		Player target = Bukkit.getPlayer(arg);

		if(target == null)
		{
			ChatUtils.sendPrefixedMessage(sender, CommandMessage.PLAYER_NOT_FOUND);
		}

		return target;
	}

	public static OfflinePlayer getOfflinePlayerFromArg(CommandSender sender, String arg)
	{
		OfflinePlayer target = null;

		for(OfflinePlayer p : Bukkit.getOfflinePlayers())
		{
			if(p.getName() == null) continue;

			if(p.getName().equals(arg))
			{
				target = p;
				break;
			}
		}

		if(target == null)
		{
			ChatUtils.sendPrefixedMessage(sender, CommandMessage.PLAYER_NOT_FOUND);
		}

		return target;
	}

	public static Object getOfflineOrOnlinePlayerFromArg(CommandSender sender, String arg)
	{
		Object target = Bukkit.getPlayer(arg);
		if(target == null) target = getOfflinePlayerFromArg(sender, arg);

		return target;
	}

	public static List<String> getOfflineAndOnlinePlayersList(CommandSender sender, String query)
	{
		List<OfflinePlayer> allPlayers = new ArrayList<>();
		allPlayers.addAll(Bukkit.getOnlinePlayers());
		allPlayers.addAll(Arrays.asList(Bukkit.getOfflinePlayers()));

		Player senderPlayer = sender instanceof Player ? (Player) sender : null;

		List<String> suggestions = allPlayers.stream()
				.filter(offlinePlayer -> !(sender instanceof Player) ^ !offlinePlayer.equals(senderPlayer))
				.map(OfflinePlayer::getName)
				.filter(name -> name != null && !name.isBlank() && StringUtil.startsWithIgnoreCase(name, query))
				.sorted(String.CASE_INSENSITIVE_ORDER).toList();

		if(allPlayers.isEmpty())
		{
			return Collections.singletonList(ChatUtils.color("&cNo other players online"));
		}

		return suggestions;
	}
}
