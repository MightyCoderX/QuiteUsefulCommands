package io.github.mightycoderx.quiteusefulcommands.commands;

import io.github.mightycoderx.quiteusefulcommands.utils.ChatUtils;
import io.github.mightycoderx.quiteusefulcommands.utils.CommandUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SpeedCommand extends Command
{
	public SpeedCommand()
	{
		super("speed", "<fly | walk> <speed> [<player>]");
	}

	@Override
	public boolean perform(CommandSender sender, String label, String[] args)
	{
		if(args.length < 1)
		{
			return false;
		}

		switch(label)
		{
			case "speed" ->
			{
				if(args.length == 1)
				{
					if(!(sender instanceof Player player))
					{
						ChatUtils.sendPrefixedMessage(sender, "&cOnly players can set their own speed");
						return true;
					}

					if(args[0].equals("fly"))
					{
						ChatUtils.sendPrefixedMessage(player, "&aYour fly speed is &b" + player.getFlySpeed()*10);
						return true;
					}
					else if(args[0].equals("walk"))
					{
						ChatUtils.sendPrefixedMessage(player, "&aYour walk speed is &b" + player.getWalkSpeed()*10);
						return true;
					}

					String type = player.isFlying() ? "fly" : "walk";

					float speed = setSpeed(type, args[0], player);
					if(speed == 11) return true;
					else if(speed == -11) return false;

					ChatUtils.sendPrefixedMessage(player, "&aYour " + type + " speed is now &b" + speed);
				}
				else if(args.length == 2)
				{
					if(!(sender instanceof Player player))
					{
						ChatUtils.sendPrefixedMessage(sender, "&cOnly players can set their own speed");
						return true;
					}

					float speed = setSpeed(args[0], args[1], player);
					if(speed == 11) return true;
					else if(speed == -11) return false;

					ChatUtils.sendPrefixedMessage(player, "&aYour " + args[0] + " speed is now &b" + speed);
				}
				else if(args.length == 3)
				{
					Player target = CommandUtils.getPlayerFromArg(sender, args[2]);
					if(target == null) return true;

					float speed = setSpeed(args[0], args[1], target);
					if(speed == 11) return true;
					else if(speed == -11) return false;

					ChatUtils.sendPrefixedMessage(target, "&b" + target.getName() + "'s&a " + args[0] + " speed is now &b" + speed);
				}
				else
					return false;
			}
			case "flyspeed" ->
			{
				if(args.length == 1)
				{
					if(!(sender instanceof Player player))
					{
						ChatUtils.sendPrefixedMessage(sender, "&cOnly players can set their own fly speed");
						return true;
					}

					float speed = setSpeed("fly", args[0], player);
					if(speed == 11) return true;
					else if(speed == -11) return false;

					ChatUtils.sendPrefixedMessage(player, "&aYour fly speed is now &b" + speed);
				}
				else if(args.length == 2)
				{
					Player target = CommandUtils.getPlayerFromArg(sender, args[1]);
					if(target == null) return true;

					float speed = setSpeed("fly", args[0], target);
					if(speed == 11) return true;
					else if(speed == -11) return false;

					ChatUtils.sendPrefixedMessage(target, "&b" + target.getName() + "'s&a fly speed is now &b" + speed);
				}
				else
					return false;
			}
			case "walkspeed" ->
			{
				if(args.length == 1)
				{
					if(!(sender instanceof Player player))
					{
						ChatUtils.sendPrefixedMessage(sender, "&cOnly players can set their own walk speed");
						return true;
					}

					float speed = setSpeed("walk", args[0], player);
					if(speed == 11) return true;
					else if(speed == -11) return false;

					ChatUtils.sendPrefixedMessage(player, "&aYour walk speed is now &b" + speed);
				}
				else if(args.length == 2)
				{
					Player target = CommandUtils.getPlayerFromArg(sender, args[1]);
					if(target == null) return true;

					float speed = setSpeed("walk", args[0], target);
					if(speed == 11) return true;
					else if(speed == -11) return false;

					ChatUtils.sendPrefixedMessage(target, "&b" + target.getName() + "'s&a walk speed is now &b" + speed);
				}
				else
					return false;
			}
		}

		return true;
	}

	public float setSpeed(String typeArg, String speedStr, Player player)
	{
		float speed;
		try
		{
			speed = Float.parseFloat(speedStr);

			if(Math.abs(speed) > 10)
			{
				ChatUtils.sendPrefixedMessage(player, "&cInvalid value:&7 valid values are between -10 and 10");
				return 11;
			}

			speed = speed/10;
		}
		catch (NumberFormatException ex)
		{
			ChatUtils.sendPrefixedMessage(player, "&cInvalid number &6" + speedStr);
			return 0;
		}

		if(typeArg.equals("fly"))
		{
			player.setFlySpeed(speed);
		}
		else if(typeArg.equals("walk"))
		{
			player.setWalkSpeed(speed);
		}
		else
		{
			return -11;
		}

		return speed*10;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args)
	{
		if(alias.equals("speed"))
		{
			switch(args.length)
			{
				case 1 ->
				{
					return CommandUtils.getSortedArgs(args[0], "fly", "walk");
				}
				case 2 ->
				{
					return List.of();
				}
				case 3 ->
				{
					return null;
				}
			}
		}
		else if((alias.equals("flyspeed") || alias.equals("walkspeed")) && args.length == 2)
		{
			return null;
		}

		return List.of();
	}
}
