package io.github.mightycoderx.quiteusefulcommands.commands;

import io.github.mightycoderx.quiteusefulcommands.utils.ChatUtils;
import io.github.mightycoderx.quiteusefulcommands.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class FreezeCommand extends Command
{
	private final CommandManager commandManager;

	public FreezeCommand(CommandManager commandManager)
	{
		super("freeze", "<player>");
		this.commandManager = commandManager;
	}

	@Override
	public boolean perform(CommandSender sender, String label, String[] args)
	{
		if(args.length == 1)
		{
			Player target = CommandUtils.getPlayerFromArg(sender, args[0]);
			if(target == null) return true;

			if(!commandManager.isPlayerFrozen(target))
			{
				commandManager.freezePlayer(target);
				ChatUtils.sendPrefixedMessage(sender, "&aFrozen player &b" + target.getName());
			}
			else
			{
				commandManager.unFreezePlayer(target);
				ChatUtils.sendPrefixedMessage(sender, "&aUnfrozen player &b" + target.getName());
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
			return null;
		}

		return List.of();
	}
}
