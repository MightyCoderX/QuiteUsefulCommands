package io.github.mightycoderx.quiteusefulcommands.inventories;

import io.github.mightycoderx.quiteusefulcommands.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class FullPlayerInventory implements Listener, InventoryHolder
{
	private final Inventory inventory;
	private final Player player;

	public FullPlayerInventory(Player player)
	{
		this.player = player;
		this.inventory = Bukkit.createInventory(this, 45,
				ChatUtils.color("&9" + player.getName()));

		loadInventory();
	}

	public void loadInventory()
	{
		inventory.setContents(player.getInventory().getContents());

		inventory.setItem(36, player.getInventory().getHelmet());
		inventory.setItem(37, player.getInventory().getChestplate());
		inventory.setItem(38, player.getInventory().getLeggings());
		inventory.setItem(39, player.getInventory().getBoots());

		inventory.setItem(44, player.getInventory().getItemInOffHand());
	}

	public void saveInventory()
	{
		ItemStack[] inventoryContents = inventory.getContents();
		for(int i = 0; i < 36; i++)
		{
			player.getInventory().setItem(i, inventoryContents[i]);
		}

		player.getInventory().setHelmet(inventoryContents[36]);
		player.getInventory().setChestplate(inventoryContents[37]);
		player.getInventory().setLeggings(inventoryContents[38]);
		player.getInventory().setBoots(inventoryContents[39]);

		player.getInventory().setItemInOffHand(inventoryContents[44]);
	}

	@Override
	public Inventory getInventory()
	{
		return inventory;
	}

	public Player getPlayer()
	{
		return player;
	}
}
