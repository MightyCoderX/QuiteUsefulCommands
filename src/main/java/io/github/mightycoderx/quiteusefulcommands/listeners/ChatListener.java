package io.github.mightycoderx.quiteusefulcommands.listeners;

import io.github.mightycoderx.quiteusefulcommands.utils.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener
{
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e)
	{
		String message = e.getMessage();

		e.setFormat("<%s> " + ChatUtils.color(message));

		if(ChatColor.stripColor(message).trim().isBlank()) e.setCancelled(true);
	}
}
