package io.github.mightycoderx.quiteusefulcommands.commands;

import io.github.mightycoderx.quiteusefulcommands.utils.ChatUtils;
import io.github.mightycoderx.quiteusefulcommands.utils.CommandUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class FlyCommand extends Command
{
	public FlyCommand()
	{
		super("fly", "[<player>]");
	}

	@Override
	public boolean perform(CommandSender sender, String label, String[] args)
	{
		if(args.length == 0)
		{
			if(!(sender instanceof Player p))
			{
				ChatUtils.sendPrefixedMessage(sender, "&cOnly players can toggle their own fly");
				return true;
			}

			if(!p.getAllowFlight())
			{
				p.setAllowFlight(true);
				ChatUtils.sendPrefixedMessage(p, "&aFly enabled");
			}
			else
			{
				p.setAllowFlight(false);
				ChatUtils.sendPrefixedMessage(p, "&aFly disabled");
			}
		}
		else if(args.length == 1)
		{
			Player target = CommandUtils.getPlayerFromArg(sender, args[0]);
			if(target == null) return true;

			if(!target.getAllowFlight())
			{
				target.setAllowFlight(true);
				ChatUtils.sendPrefixedMessage(sender, "&aFly enabled for " + target.getName());
			}
			else
			{
				target.setAllowFlight(false);
				ChatUtils.sendPrefixedMessage(sender, "&aFly disabled for " + target.getName());
			}
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
