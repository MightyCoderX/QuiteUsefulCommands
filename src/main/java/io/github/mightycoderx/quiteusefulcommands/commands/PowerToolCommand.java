package io.github.mightycoderx.quiteusefulcommands.commands;

import io.github.mightycoderx.quiteusefulcommands.PowerTool;
import io.github.mightycoderx.quiteusefulcommands.utils.ChatUtils;
import io.github.mightycoderx.quiteusefulcommands.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class PowerToolCommand extends Command
{
	private final CommandManager commandManager;

	public PowerToolCommand(CommandManager commandManager)
	{
		super("powertool", "<list | add [c:]<command> | remove | clear [player]>");
		this.commandManager = commandManager;
	}

	@Override
	public boolean perform(CommandSender sender, String label, String[] args)
	{
		if(!(sender instanceof Player player))
		{
			ChatUtils.sendPrefixedMessage(sender, CommandMessage.ONLY_PLAYERS_COMMAND);
			return true;
		}

		if(args.length == 1)
		{
			switch(args[0])
			{
				case "list" ->
				{
					if(!commandManager.hasAnyPowerTool(player))
					{
						ChatUtils.sendPrefixedMessage(player, "&cYou don't have any powertool");
						return true;
					}

					ChatUtils.sendMessage(player, "&3---------&7«&2Your PowerTools&7»&3---------&r");
					for(PowerTool powerTool : commandManager.getPowerTools(player))
					{
						String command = powerTool.getCommand();
						command = command.startsWith("c:") ? command : "/" + command;
						ChatUtils.sendMessage(player, "&a" + powerTool.getItem() + "&7 - &9" + command);
					}
				}
				case "remove" ->
				{
					Material itemInMainHand = player.getInventory().getItemInMainHand().getType();
					if(commandManager.removePowerTool(player, itemInMainHand))
					{
						ChatUtils.sendPrefixedMessage(player, "&aRemoved powertool from &b" + itemInMainHand);
					}
					else
					{
						ChatUtils.sendPrefixedMessage(player, "&cNo powertool bound to &b" + itemInMainHand);
					}
				}
				case "clear" ->
				{
					commandManager.removeAllPlayerPowerTools(player);
					ChatUtils.sendPrefixedMessage(player, "&aCleared all your powertools");
				}
				default ->
				{
					return false;
				}
			}
		}
		else if(args.length == 2 && args[0].equals("clear"))
		{
			Player target = CommandUtils.getPlayerFromArg(player, args[1]);
			if(target == null) return true;

			commandManager.removeAllPlayerPowerTools(target);
			ChatUtils.sendPrefixedMessage(player, "&aCleared all &b" + target.getName() + "&a's power-tools");
		}
		else if(args.length > 1)
		{
			String command = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
			Material itemInMainHand = player.getInventory().getItemInMainHand().getType();

			if (args[0].equals("add"))
			{
				if(commandManager.hasPowerTool(player, itemInMainHand))
				{
					commandManager.removePowerTool(player, itemInMainHand);
				}

				commandManager.createPowerTool(
					player,
					itemInMainHand,
					command
				);
				ChatUtils.sendPrefixedMessage(player, "&aAdded powertool to &b" + itemInMainHand);
			}
			else
			{
				return false;
			}

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
			return CommandUtils.getSortedArgs(args[0], "add", "remove", "clear", "list");
		}
		else if(args.length == 2 && args[0].equals("clear"))
		{
			return null;
		}
		else if(args.length > 1 && args[0].equals("add"))
		{
			String command = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
			Stream<String> helpTopics = Bukkit.getServer().getHelpMap().getHelpTopics().stream()
					.map(helpTopic -> helpTopic.getName().replace("/", ""))
					.filter(topic -> !Character.isUpperCase(topic.charAt(0)));

			List<String> argList = new ArrayList<>(helpTopics.toList());
			argList.add("c:");

			helpTopics = argList.stream();

			if(!command.isBlank())
			{
				return helpTopics.filter(s -> s.startsWith(command))
						.toList();
			}

			return argList;
		}

		return List.of();
	}
}
