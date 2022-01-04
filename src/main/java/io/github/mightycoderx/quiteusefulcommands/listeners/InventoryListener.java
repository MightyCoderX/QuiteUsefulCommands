package io.github.mightycoderx.quiteusefulcommands.listeners;

import io.github.mightycoderx.quiteusefulcommands.inventories.FullPlayerInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryListener implements Listener
{
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e)
	{
		if(e.getClickedInventory() == null) return;
		if(!(e.getInventory() instanceof FullPlayerInventory holder)) return;

		if(e.getClickedInventory().equals(holder.getPlayer().getInventory()))
		{
			if(e.getSlot() < 36)
			{
				holder.getInventory().setItem(e.getSlot(), e.getCurrentItem());
			}
			switch (e.getSlot())
			{
				//Helmet
				case 39 -> holder.getInventory().setItem(36, e.getCurrentItem());
				//ChestPlate
				case 38 -> holder.getInventory().setItem(37, e.getCurrentItem());
				//Leggings
				case 37 -> holder.getInventory().setItem(38, e.getCurrentItem());
				//Boots
				case 36 -> holder.getInventory().setItem(39, e.getCurrentItem());
			}
		}

		if(e.getClickedInventory().equals(holder.getInventory()))
		{
			if(e.getSlot() < 36)
			{
				holder.getPlayer().getInventory().setItem(e.getSlot(), e.getCurrentItem());
			}
			else
			{
				switch (e.getSlot())
				{
					case 36 ->
						holder.getPlayer().getInventory().setHelmet(e.getCurrentItem());

					case 37 ->
						holder.getPlayer().getInventory().setChestplate(e.getCurrentItem());

					case 38 ->
							holder.getPlayer().getInventory().setLeggings(e.getCurrentItem());

					case 39 ->
							holder.getPlayer().getInventory().setBoots(e.getCurrentItem());

					case 44 ->
							holder.getPlayer().getInventory().setItemInOffHand(e.getCurrentItem());
				}
			}

			holder.getPlayer().updateInventory();
		}
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e)
	{
		if(e.getInventory().getHolder() instanceof FullPlayerInventory inventory)
		{
			inventory.saveInventory();
		}
	}
}
