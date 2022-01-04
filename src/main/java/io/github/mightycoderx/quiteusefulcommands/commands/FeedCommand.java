package io.github.mightycoderx.quiteusefulcommands.commands;

import io.github.mightycoderx.quiteusefulcommands.utils.ChatUtils;
import io.github.mightycoderx.quiteusefulcommands.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class FeedCommand extends Command
{
	public FeedCommand()
	{
		super("feed", "[<player>]");
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args)
	{
		if(args.length == 0)
		{
			if(!(sender instanceof Player p))
			{
				ChatUtils.sendPrefixedMessage(sender, "&cOnly players can feed themselves!");
				return true;
			}

			p.setFoodLevel(20);
			p.setSaturation(20);
			ChatUtils.sendPrefixedMessage(p, "&aYou have been fed!");
		}
		else if(args.length == 1)
		{
			Player target = CommandUtils.getPlayerFromArg(sender, args[0]);
			if(target == null) return true;

			target.setFoodLevel(20);
			target.setSaturation(20);
			ChatUtils.sendPrefixedMessage(sender, "&aYou fed &b" + target.getDisplayName() + "!");
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
