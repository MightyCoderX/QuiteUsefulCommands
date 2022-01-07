package io.github.mightycoderx.quiteusefulcommands.commands;

import io.github.mightycoderx.quiteusefulcommands.utils.ChatUtils;
import io.github.mightycoderx.quiteusefulcommands.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EnderChestCommand extends Command
{
	public EnderChestCommand()
	{
		super("enderchest", "[<player>]");
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args)
	{
		if(!(sender instanceof Player p))
		{
			ChatUtils.sendPrefixedMessage(sender, CommandMessage.ONLY_PLAYERS_COMMAND);
			return true;
		}

		if(args.length == 0)
		{
			p.openInventory(p.getEnderChest());
			ChatUtils.sendPrefixedMessage(p, "&aOpened your ender chest");
		}
		else if(args.length == 1)
		{
			Object target = CommandUtils.getOfflineOrOnlinePlayerFromArg(p, args[0]);
			if(target == null) return true;

			Player actualPlayer = ((OfflinePlayer) target).getPlayer();

			if(actualPlayer == null)
			{
				ChatUtils.sendPrefixedMessage(p, CommandMessage.PLAYER_NOT_FOUND);
				return true;
			}

			p.openInventory(actualPlayer.getEnderChest());
			ChatUtils.sendPrefixedMessage(p, "&aOpened &b" + actualPlayer.getName() + "&a's ender chest");
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
			return null;
		}

		return List.of();
	}
}
