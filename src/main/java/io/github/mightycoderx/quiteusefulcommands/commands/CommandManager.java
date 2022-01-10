package io.github.mightycoderx.quiteusefulcommands.commands;

import io.github.mightycoderx.quiteusefulcommands.PowerTool;
import io.github.mightycoderx.quiteusefulcommands.QuiteUsefulCommands;
import io.github.mightycoderx.quiteusefulcommands.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.*;

public class CommandManager implements TabExecutor
{
	private final QuiteUsefulCommands plugin;
	private final ArrayList<Command> commands = new ArrayList<>();

	private final Set<UUID> frozenPlayers = new HashSet<>();
	private final Set<UUID> godmodePlayers = new HashSet<>();
	private final Map<UUID, List<PowerTool>> powerTools = new HashMap<>();

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
		commands.add(new FreezeCommand(this));
		commands.add(new GodModeCommand(this));
		commands.add(new SpeedCommand());
		commands.add(new PowerToolCommand(this));
	}

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args)
	{
		Command cmd = getCommand(command.getName());

		if(!cmd.perform(sender, label, args))
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

	public boolean isPlayerFrozen(Player player)
	{
		return frozenPlayers.contains(player.getUniqueId());
	}

	public void freezePlayer(Player player)
	{
		frozenPlayers.add(player.getUniqueId());
	}

	public void unFreezePlayer(Player player)
	{
		if(!isPlayerFrozen(player)) return;
		frozenPlayers.remove(player.getUniqueId());
	}

	public List<Player> getGodModePlayers()
	{
		return godmodePlayers.stream().map(Bukkit::getPlayer).toList();
	}

	public boolean isPlayerInGodMode(Player player)
	{
		return godmodePlayers.contains(player.getUniqueId());
	}

	public void addGodmodePlayer(Player player)
	{
		godmodePlayers.add(player.getUniqueId());
	}

	public void removeGodmodePlayer(Player player)
	{
		if(!isPlayerInGodMode(player)) return;
		godmodePlayers.remove(player.getUniqueId());
	}

	public List<PowerTool> getPowerTools(Player player)
	{
		return powerTools.get(player.getUniqueId());
	}

	public PowerTool getPowerTool(Player player, Material item)
	{
		return getPowerTools(player).stream()
				.filter(powerTool -> powerTool.getItem().equals(item))
				.toList().get(0);
	}

	public boolean hasAnyPowerTool(Player player)
	{
		return powerTools.containsKey(player.getUniqueId());
	}

	public boolean hasPowerTool(Player player, Material item)
	{
		return powerTools.containsKey(player.getUniqueId()) && getPowerTools(player).stream()
				.map(PowerTool::getItem)
				.anyMatch(item::equals);
	}

	public void createPowerTool(Player owner, Material item, String command)
	{
		if(powerTools.containsKey(owner.getUniqueId()))
		{
			powerTools.get(owner.getUniqueId()).add(new PowerTool(item, command));
		}
		else
		{
			powerTools.put(
				owner.getUniqueId(),
				new ArrayList<>(List.of(new PowerTool(item, command)))
			);
		}
	}

	public boolean removePowerTool(Player owner, Material item)
	{
		if(!hasPowerTool(owner, item)) return false;

		powerTools.get(owner.getUniqueId()).remove(getPowerTool(owner, item));
		return true;
	}

	public void removeAllPlayerPowerTools(Player owner)
	{
		powerTools.remove(owner.getUniqueId());
	}
}
