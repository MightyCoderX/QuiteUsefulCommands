package io.github.mightycoderx.quiteusefulcommands.commands;

import io.github.mightycoderx.quiteusefulcommands.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class HealCommand extends Command
{
	public HealCommand()
	{
		super("heal", "[<player>]");
	}

	@Override
	public boolean perform(CommandSender sender, String label, String[] args)
	{
		if(args.length == 0)
		{
			if(!(sender instanceof Player p))
			{
				ChatUtils.sendPrefixedMessage(sender, "&cOnly players can heal themselves!");
				return true;
			}

			healPlayer(p);
			ChatUtils.sendPrefixedMessage(sender, "&aYou have been healed!");
		}
		else if(args.length == 1)
		{
			Player target = Bukkit.getPlayer(args[0]);

			if(target == null)
			{
				ChatUtils.sendPrefixedMessage(sender, "&cPlayer not found");
				return true;
			}

			healPlayer(target);
			ChatUtils.sendPrefixedMessage(sender, "&aHealed &b" + target.getDisplayName() + "!");
		}

		return true;
	}

	public void healPlayer(Player p)
	{
		p.getActivePotionEffects().clear();
		p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
		p.setFoodLevel(20);
		p.setSaturation(20);
		p.setFireTicks(0);
		p.setFreezeTicks(0);
		p.setExhaustion(0);
		p.setRemainingAir(p.getMaximumAir());
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
