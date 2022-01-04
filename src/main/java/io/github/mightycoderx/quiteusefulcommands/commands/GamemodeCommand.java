package io.github.mightycoderx.quiteusefulcommands.commands;

import io.github.mightycoderx.quiteusefulcommands.utils.ChatUtils;
import io.github.mightycoderx.quiteusefulcommands.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GamemodeCommand extends Command
{
	public GamemodeCommand()
	{
		super("gm", "[gamemode] [<player>]");
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args)
	{
		System.out.println(getName() + " " + label);
		if(label.equals("gm"))
		{
			if(args.length == 0)
			{
				if(!(sender instanceof Player p))
				{
					ChatUtils.sendPrefixedMessage(sender, "&cOnly players can look at their own gamemode!");
					return true;
				}

				ChatUtils.sendPrefixedMessage(p, "&bYour gamemode is &6" + p.getGameMode());
			}
			else if(args.length == 1)
			{
				if(!(sender instanceof Player p))
				{
					ChatUtils.sendPrefixedMessage(sender, "&cOnly players can update their own gamemode!");
					return true;
				}

				String gamemodeArg = ChatColor.stripColor(args[0].toUpperCase());

				if(Arrays.stream(GameMode.values()).noneMatch(gm -> gm.toString().startsWith(gamemodeArg)))
				{
					ChatUtils.sendPrefixedMessage(p, "&cInvalid gamemode &6" + gamemodeArg);
					return true;
				}

				GameMode gamemode = Arrays.stream(GameMode.values())
						.filter(gm -> gm.toString().startsWith(gamemodeArg))
						.toList().get(0);

				p.setGameMode(gamemode);
				ChatUtils.sendPrefixedMessage(p, "&aUpdated your gamemode to &6" + gamemode);
			}
			else if(args.length == 2)
			{
				String gamemodeArg = ChatColor.stripColor(args[0].toUpperCase());

				if(Arrays.stream(GameMode.values()).noneMatch(gm -> gm.toString().startsWith(gamemodeArg)))
				{
					ChatUtils.sendPrefixedMessage(sender, "&cInvalid gamemode &6" + gamemodeArg);
					return true;
				}

				GameMode gamemode = Arrays.stream(GameMode.values())
						.filter(gm -> gm.toString().startsWith(gamemodeArg))
						.toList().get(0);

				Player target = CommandUtils.getPlayerFromArg(sender, args[0]);
				if(target == null) return true;

				target.setGameMode(gamemode);
				ChatUtils.sendPrefixedMessage(sender, "&aUpdated " + target.getName() + "'s gamemode to &6" + gamemode);
			}
			else
			{
				return false;
			}
		}
		else if(label.length() >= 3 && label.startsWith("gm"))
		{
			if(args.length == 0 && !(sender instanceof Player))
			{
				ChatUtils.sendPrefixedMessage(sender, "&cOnly players can update their own gamemode!");
				return true;
			}

			String gamemodeStr = label.substring(2).toUpperCase();

			GameMode gamemode = Arrays.stream(GameMode.values())
					.filter(gm -> gm.toString().startsWith(gamemodeStr))
					.toList().get(0);

			Player target = (Player) sender;

			if(args.length == 1)
			{
				target = CommandUtils.getPlayerFromArg(sender, args[0]);
				if(target == null) return true;
			}

			Player p = (Player) sender;
			target.setGameMode(gamemode);

			if(target.equals(p))
			{
				ChatUtils.sendPrefixedMessage(p, "&aUpdated your gamemode to &6" + gamemode);
			}
			else
			{
				ChatUtils.sendPrefixedMessage(sender, "&aUpdated " + target.getName() + "'s gamemode to &6" + gamemode);
			}
		}

		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args)
	{
		if(alias.equals("gm"))
		{
			if(args.length == 1)
			{
				List<String> gamemodes = new ArrayList<>(Arrays.stream(GameMode.values())
						.map(gameMode -> gameMode.toString().toLowerCase()).toList());

				//Add gamemode initials to the list
				gamemodes.addAll(gamemodes.stream().map(s -> s.substring(0, 1)).toList());

				return gamemodes;
			}
			else if(args.length == 2)
			{
				return null;
			}
		}
		else if(alias.length() >= 3 && alias.startsWith("gm"))
		{
			if(args.length == 1)
			{
				return null;
			}
		}

		return List.of();
	}
}
