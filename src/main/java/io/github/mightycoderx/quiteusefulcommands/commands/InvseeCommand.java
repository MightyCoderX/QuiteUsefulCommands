package io.github.mightycoderx.quiteusefulcommands.commands;

import io.github.mightycoderx.quiteusefulcommands.inventories.FullPlayerInventory;
import io.github.mightycoderx.quiteusefulcommands.utils.ChatUtils;
import io.github.mightycoderx.quiteusefulcommands.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class InvseeCommand extends Command
{
	public InvseeCommand()
	{
		super("invsee", "[<player>]");
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
			openInventory(p, p);
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

			openInventory(p, actualPlayer);
		}
		else
		{
			return false;
		}

		return true;
	}

	public void openInventory(Player sender, Player target)
	{
//		sender.openInventory(new FullPlayerInventory(target).getInventory());
		sender.openInventory(target.getInventory());

		if(target.equals(sender))
		{
			ChatUtils.sendPrefixedMessage(sender, "&aOpened your inventory. \nBruh...");
		}
		else
		{
			ChatUtils.sendPrefixedMessage(sender, "&aOpened &b" + target.getName() + "&a's inventory");
		}
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
