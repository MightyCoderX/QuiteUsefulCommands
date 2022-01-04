package io.github.mightycoderx.quiteusefulcommands;

import io.github.mightycoderx.quiteusefulcommands.commands.CommandManager;
import io.github.mightycoderx.quiteusefulcommands.commands.Command;
import io.github.mightycoderx.quiteusefulcommands.listeners.InventoryListener;
import io.github.mightycoderx.quiteusefulcommands.maps.CustomMapManager;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;


public class QuiteUsefulCommands extends JavaPlugin
{
	private CustomMapManager customMapManager;

	@Override
	public void onEnable()
	{
		customMapManager = new CustomMapManager(this);

		CommandManager commandManager = new CommandManager(this);

		for(Command command : commandManager.getCommands())
		{
			PluginCommand cmd = getCommand(command.getName());
			if(cmd == null) continue;
			cmd.setExecutor(commandManager);
		}

		getServer().getPluginManager().registerEvents(new InventoryListener(), this);
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
