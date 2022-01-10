package io.github.mightycoderx.quiteusefulcommands.listeners;

import io.github.mightycoderx.quiteusefulcommands.commands.CommandManager;
import io.github.mightycoderx.quiteusefulcommands.commands.GodModeCommand;
import io.github.mightycoderx.quiteusefulcommands.utils.ChatUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class GodModeListener implements Listener
{
	private final CommandManager commandManager;

	public GodModeListener(CommandManager commandManager)
	{
		this.commandManager = commandManager;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		if(!commandManager.isPlayerInGodMode(e.getPlayer())) return;

		GodModeCommand godModeCommand = (GodModeCommand) commandManager.getCommand("godmode");
		godModeCommand.setGodMode(e.getPlayer(), true);
	}

	@EventHandler
	public void onPlayerDamaged(EntityDamageEvent e)
	{
		if(!(e.getEntity() instanceof Player player)) return;
		if(e.getCause().equals(EntityDamageEvent.DamageCause.VOID)) return;

		if(commandManager.isPlayerInGodMode(player))
		{
			e.setCancelled(true);
			e.getEntity().setFireTicks(0);
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e)
	{
		if(e.getEntity().getPlayer() == null) return;

		Player player = e.getEntity().getPlayer();

		if(commandManager.isPlayerInGodMode(player))
		{
			e.setKeepLevel(true);
			e.setKeepInventory(true);
			e.setDeathMessage(ChatUtils.color("&eThe god &b&l" + player.getDisplayName() + "&e died!"));
			player.getWorld().strikeLightning(player.getLocation());
		}
		commandManager.removeGodmodePlayer(player);
	}
}
