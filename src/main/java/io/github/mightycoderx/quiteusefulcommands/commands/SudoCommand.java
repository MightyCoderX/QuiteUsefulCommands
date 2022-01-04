package io.github.mightycoderx.quiteusefulcommands.commands;

import io.github.mightycoderx.quiteusefulcommands.utils.ChatUtils;
import io.github.mightycoderx.quiteusefulcommands.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.help.HelpTopic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class SudoCommand extends Command
{
	public SudoCommand()
	{
		super("sudo", "<player> <command>");
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args)
	{
		if(args.length >= 2)
		{
			Player p = CommandUtils.getPlayerFromArg(sender, args[0]);
			if(p == null) return true;

			String command = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

			if(command.startsWith("c:"))
			{
				p.chat(command.substring(2));
				ChatUtils.sendPrefixedMessage(sender, "&aForced &6" + p.getName() + "&a to chat &9" + command);
				return true;
			}
			else if(command.startsWith("/"))
			{
				command = command.replace("/", "");
			}

			p.performCommand(command);
			ChatUtils.sendPrefixedMessage(sender, "&aForced &6" + p.getName() + "&a to execute command &9/" + command);
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
		else if(args.length >= 2)
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
