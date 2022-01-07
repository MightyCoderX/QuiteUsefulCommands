package io.github.mightycoderx.quiteusefulcommands.commands;

import io.github.mightycoderx.quiteusefulcommands.utils.ChatUtils;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.protocol.game.PacketPlayOutOpenWindow;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.entity.player.PlayerInventory;
import net.minecraft.world.inventory.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftInventoryPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.util.List;

public class WorkbenchCommand extends Command
{
	public WorkbenchCommand()
	{
		super("workbench", "");
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args)
	{
		if(!(sender instanceof Player p))
		{
			ChatUtils.sendPrefixedMessage(sender, CommandMessage.ONLY_PLAYERS_COMMAND);
			return true;
		}

		if(args.length == 0)
		{
			String openedName = null;
			switch (label)
			{
				case "craft", "wbench" ->
					openedName = p.openWorkbench(null, true).getType().toString();

				case "enchanting", "etable" ->
					openedName = openInventory(p, InventoryType.ENCHANTING);

				case "loom" ->
					openedName = openInventory(p, InventoryType.LOOM);

				case "cartography", "cartographytable", "carttable" ->
					openedName = openInventory(p, InventoryType.CARTOGRAPHY);

				case "stonecutter" ->
					openedName = openInventory(p, InventoryType.STONECUTTER);

				case "grindstone" ->
					openedName = openInventory(p, InventoryType.GRINDSTONE);

				case "smithingtable", "smithtable" ->
					openedName = openInventory(p, InventoryType.SMITHING);

				case "anvil", "rename" ->
					openedName = openInventory(p, InventoryType.ANVIL);

			}

			ChatUtils.sendPrefixedMessage(p, "&aOpened &7" + openedName);
		}
		else
		{
			return false;
		}

		return true;
	}

	private String openInventory(Player player, InventoryType inventoryType)
	{
		EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();

		Container container = null;

		int c = entityPlayer.nextContainerCounter();
		PlayerInventory playerInventory = ((CraftInventoryPlayer) player.getInventory()).getInventory();

		switch (inventoryType)
		{
			case LOOM -> container = new ContainerLoom(c, playerInventory);
			case CARTOGRAPHY -> container = new ContainerCartography(c, playerInventory);
			case STONECUTTER -> container = new ContainerStonecutter(c, playerInventory);
			case GRINDSTONE -> container = new ContainerGrindstone(c, playerInventory);
			case SMITHING -> container = new ContainerSmithing(c, playerInventory);
			case ANVIL -> container = new ContainerAnvil(c, playerInventory);
			case ENCHANTING -> container = new EnchantContainer(c, playerInventory);
		}

		entityPlayer.b.a(new PacketPlayOutOpenWindow(c, container.a(), new ChatComponentText(inventoryType.getDefaultTitle())));

		entityPlayer.bW = container;
		entityPlayer.bW.checkReachable = false;


		return inventoryType.toString();
	}

	private static class EnchantContainer extends ContainerEnchantTable
	{
		public EnchantContainer(int i, PlayerInventory playerinventory)
		{
			super(i, playerinventory);
		}

		@Override
		public boolean a(EntityHuman entityhuman)
		{
			return true;
		}
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args)
	{
		return List.of();
	}
}
