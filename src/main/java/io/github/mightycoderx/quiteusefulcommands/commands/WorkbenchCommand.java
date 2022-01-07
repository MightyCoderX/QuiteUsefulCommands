package io.github.mightycoderx.quiteusefulcommands.commands;

import io.github.mightycoderx.quiteusefulcommands.utils.ChatUtils;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftInventoryPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;

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
				{
					InventoryView crafting = p.openWorkbench(null, true);
					if(crafting == null) return true;
					openedName = crafting.getType().toString();
				}

//				case "enchanting", "etable" ->
//					openedName = openInventory(p, InventoryType.ENCHANTING);
//
//				case "loom" ->
//					openedName = openInventory(p, InventoryType.LOOM);
//
//				case "cartography", "cartographytable", "carttable" ->
//					openedName = openInventory(p, InventoryType.CARTOGRAPHY);
//
//				case "stonecutter" ->
//					openedName = openInventory(p, InventoryType.STONECUTTER);
//
//				case "grindstone" ->
//					openedName = openInventory(p, InventoryType.GRINDSTONE);
//
//				case "smithingtable", "smithtable" ->
//					openedName = openInventory(p, InventoryType.SMITHING);
//
//				case "anvil", "rename" ->
//					openedName = openInventory(p, InventoryType.ANVIL);

			}

			ChatUtils.sendPrefixedMessage(p, "&aOpened &b" + openedName);
		}
		else
		{
			return false;
		}

		return true;
	}

	private String openInventory(Player player, InventoryType inventoryType)
	{
		ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();

		AbstractContainerMenu container = null;

		int c = serverPlayer.nextContainerCounter();
		Inventory playerInventory = ((CraftInventoryPlayer) player.getInventory()).getInventory();

		switch (inventoryType)
		{
			case LOOM -> container = new LoomMenu(c, playerInventory);
			case CARTOGRAPHY -> container = new CartographyTableMenu(c, playerInventory);
			case STONECUTTER -> container = new StonecutterMenu(c, playerInventory);
			case GRINDSTONE -> container = new GrindstoneMenu(c, playerInventory);
			case SMITHING -> container = new SmithingMenu(c, playerInventory);
			case ANVIL -> container = new AnvilMenu(c, playerInventory);
			case ENCHANTING -> container = new EnchantContainer(c, playerInventory);
		}
		serverPlayer.containerMenu = container;
		serverPlayer.containerMenu.checkReachable = false;
		serverPlayer.initMenu(container);

		serverPlayer.connection.send(new ClientboundOpenScreenPacket(container.containerId, container.getType(), new TextComponent(inventoryType.getDefaultTitle())));

		return inventoryType.toString();
	}

	private static class EnchantContainer extends EnchantmentMenu
	{
		public EnchantContainer(int i, Inventory inventory)
		{
			super(i, inventory);
		}

		@Override
		public boolean stillValid(net.minecraft.world.entity.player.Player player)
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
