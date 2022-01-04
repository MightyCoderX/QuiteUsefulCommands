package io.github.mightycoderx.quiteusefulcommands.commands;

import io.github.mightycoderx.quiteusefulcommands.QuiteUsefulCommands;
import io.github.mightycoderx.quiteusefulcommands.utils.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class CommandManager implements TabExecutor
{
	private final QuiteUsefulCommands plugin;
	private final ArrayList<Command> commands = new ArrayList<>();

	public CommandManager(QuiteUsefulCommands plugin)
	{
		this.plugin = plugin;
		init();
	}

	public void init()
	{
		commands.add(new MainCommand(this));
		commands.add(new HealCommand());
		commands.add(new FeedCommand());
		commands.add(new GamemodeCommand());
		commands.add(new FlyCommand());
		commands.add(new SudoCommand());
		commands.add(new EnderChestCommand());
		commands.add(new WorkbenchCommand());
		commands.add(new InvseeCommand());
		commands.add(new RideCommand());
		commands.add(new SpawnMobCommand());
		commands.add(new CustomMapCommand(plugin));

	}

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args)
	{
		Command cmd = getCommand(command.getName());

		if(!cmd.execute(sender, label, args))
		{
			ChatUtils.sendCommandUsage(sender, cmd);
		}

		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args)
	{
		return getCommand(command.getName()).tabComplete(sender, alias, args);
	}

	public ArrayList<Command> getCommands()
	{
		return commands;
	}

	public Command getCommand(String name)
	{
		return commands.stream().filter(command -> command.getName().equals(name)).toList().get(0);
	}

	public List<String> getCommandNames()
	{
		return commands.stream().map(Command::getName).toList();
	}

	public QuiteUsefulCommands getPlugin()
	{
		return plugin;
	}
}
