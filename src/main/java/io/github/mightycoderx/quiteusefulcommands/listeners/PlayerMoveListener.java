package io.github.mightycoderx.quiteusefulcommands.listeners;

import io.github.mightycoderx.quiteusefulcommands.commands.CommandManager;
import io.github.mightycoderx.quiteusefulcommands.utils.ChatUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener
{
	private final CommandManager commandManager;

	public PlayerMoveListener(CommandManager commandManager)
	{
		this.commandManager = commandManager;
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e)
	{
		if(commandManager.isPlayerFrozen(e.getPlayer()))
		{
			e.setCancelled(true);
			ChatUtils.sendMessage(e.getPlayer(), "&cYou have been frozen!");
		}
	}
}
