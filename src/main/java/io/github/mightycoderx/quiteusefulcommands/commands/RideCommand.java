package io.github.mightycoderx.quiteusefulcommands.commands;

import io.github.mightycoderx.quiteusefulcommands.utils.ChatUtils;
import io.github.mightycoderx.quiteusefulcommands.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.List;

public class RideCommand extends Command
{
	public RideCommand()
	{
		super("ride", "<player>");
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args)
	{
		if(!(sender instanceof Player p))
		{
			ChatUtils.sendPrefixedMessage(sender, CommandMessage.ONLY_PLAYERS_COMMAND);
			return true;
		}

		if(args.length == 1)
		{
			Player target = CommandUtils.getPlayerFromArg(p, args[0]);
			if(target == null) return true;

			if(target.equals(p))
			{
				ChatUtils.sendPrefixedMessage(p, "&cYou can't ride yourself, duh...");
				return true;
			}

			target.addPassenger(p);
			ChatUtils.sendPrefixedMessage(p, "&aYou are now riding &b" + target.getName());
		}
		else
		{
			return false;
		}

		return true;
	}

	@Override
	public List<String> tabComplete(Player player, String alias, String[] args)
	{
		if(args.length == 1)
		{
			return Bukkit.getOnlinePlayers().stream()
					.filter(p -> !p.equals(player))
					.map(Player::getName)
					.filter(name -> StringUtil.startsWithIgnoreCase(name, args[0]))
					.sorted(String.CASE_INSENSITIVE_ORDER).toList();
		}

		return List.of();
	}
}
