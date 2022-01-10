package io.github.mightycoderx.quiteusefulcommands.commands;

import io.github.mightycoderx.quiteusefulcommands.utils.ChatUtils;
import io.github.mightycoderx.quiteusefulcommands.utils.CommandUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class GodModeCommand extends Command
{
	private final CommandManager commandManager;

	public GodModeCommand(CommandManager commandManager)
	{
		super("godmode", "[<player>]");
		this.commandManager = commandManager;
	}

	@Override
	public boolean perform(CommandSender sender, String label, String[] args)
	{
		if(args.length == 0)
		{
			if(!(sender instanceof Player player))
			{
				ChatUtils.sendPrefixedMessage(sender, "&cOnly players can toggle godmode for themselves");
				return true;
			}

			if(!commandManager.isPlayerInGodMode(player))
			{
				setGodMode(player, true);
				commandManager.addGodmodePlayer(player);
				ChatUtils.sendPrefixedMessage(sender, "&aYou are now in &bGodMode");
			}
			else
			{
				setGodMode(player, false);
				commandManager.removeGodmodePlayer(player);
				ChatUtils.sendPrefixedMessage(sender, "&aYou are no longer in &bGodMode");
			}
		}
		else if(args.length == 1)
		{
			Player target = CommandUtils.getPlayerFromArg(sender, args[0]);
			if(target == null) return true;

			if(!commandManager.isPlayerInGodMode(target))
			{
				setGodMode(target, true);
				commandManager.addGodmodePlayer(target);
				ChatUtils.sendPrefixedMessage(sender, "&b" + target.getName() + "&a is now in &bGodMode");
			}
			else
			{
				setGodMode(target, false);
				commandManager.removeGodmodePlayer(target);
				ChatUtils.sendPrefixedMessage(sender, "&b" + target.getName() + "&a is no longer in &bGodMode");
			}
		}
		else
		{
			return false;
		}

		return true;
	}

	public void setGodMode(Player player, boolean godmode)
	{
		if(godmode)
		{
			player.setAllowFlight(true);
			player.setFlying(true);
			player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, (int) 10e100, 50, true, false));
			player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, (int) 10e100, 50, true, false));
		}
		else
		{
			player.setAllowFlight(false);
			player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
			player.removePotionEffect(PotionEffectType.FAST_DIGGING);
		}
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args)
	{
		if(args.length == 1)
		{
			return null;
		}

		return List.of();
	}
}
