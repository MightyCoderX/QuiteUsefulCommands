package io.github.mightycoderx.quiteusefulcommands;

import io.github.mightycoderx.quiteusefulcommands.commands.CommandManager;
import io.github.mightycoderx.quiteusefulcommands.commands.Command;
import io.github.mightycoderx.quiteusefulcommands.listeners.*;
import io.github.mightycoderx.quiteusefulcommands.maps.CustomMapManager;
import io.github.mightycoderx.quiteusefulcommands.tasks.GodModeTask;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;


public class QuiteUsefulCommands extends JavaPlugin
{
	private CustomMapManager customMapManager;

	@Override
	public void onLoad()
	{
		customMapManager = new CustomMapManager(this);
	}

	@Override
	public void onEnable()
	{
		CommandManager commandManager = new CommandManager(this);

		for(Command command : commandManager.getCommands())
		{
			PluginCommand cmd = getCommand(command.getName());
			if(cmd == null) continue;
			cmd.setExecutor(commandManager);
		}

		getServer().getPluginManager().registerEvents(new InventoryListener(), this);
		getServer().getPluginManager().registerEvents(new ChatListener(), this);

		getServer().getPluginManager().registerEvents(new PlayerMoveListener(commandManager), this);
		getServer().getPluginManager().registerEvents(new GodModeListener(commandManager), this);
		getServer().getPluginManager().registerEvents(new PowerToolListener(commandManager), this);


		new GodModeTask(commandManager).runTaskTimer(this, 0, 1);
	}

	@Override
	public void onDisable()
	{
		customMapManager.getMapsConfiguration().saveMaps();
	}

	public CustomMapManager getCustomMapManager()
	{
		return customMapManager;
	}
}
