package io.github.mightycoderx.quiteusefulcommands.listeners;

import io.github.mightycoderx.quiteusefulcommands.PowerTool;
import io.github.mightycoderx.quiteusefulcommands.commands.CommandManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.PlayerInventory;

public class PowerToolListener implements Listener
{
	private final CommandManager commandManager;

	public PowerToolListener(CommandManager commandManager)
	{
		this.commandManager = commandManager;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e)
	{
		if(e.getAction() == Action.LEFT_CLICK_AIR ||
			e.getAction() == Action.LEFT_CLICK_BLOCK ||
			e.getAction().equals(Action.PHYSICAL)) return;

		PlayerInventory playerInventory = e.getPlayer().getInventory();

		Material itemTypeInHand = null;

		boolean offHandEmpty = playerInventory.getItemInOffHand().getType() == Material.AIR;
		boolean handEmpty = playerInventory.getItemInMainHand().getType() == Material.AIR;

		if(handEmpty)
		{
			if(e.getHand() == EquipmentSlot.HAND) return;
			itemTypeInHand = playerInventory.getItemInOffHand().getType();
		}
		else if(offHandEmpty)
		{
			if(e.getHand() == EquipmentSlot.OFF_HAND) return;
			itemTypeInHand = playerInventory.getItemInMainHand().getType();
		}

		if(!commandManager.hasPowerTool(e.getPlayer(), itemTypeInHand)) return;

		e.setCancelled(true);

		PowerTool powerTool = commandManager.getPowerTool(e.getPlayer(), itemTypeInHand);

		String powerToolCommand = powerTool.getCommand()
				.replaceAll("\\{PLAYERNAME}", e.getPlayer().getName());

		if(powerToolCommand.startsWith("c:"))
		{
			e.getPlayer().chat(powerToolCommand.substring(2));
			return;
		}

		powerToolCommand = powerToolCommand.replace("/", "");

		e.getPlayer().performCommand(powerToolCommand);
	}
}
