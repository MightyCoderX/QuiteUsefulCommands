package io.github.mightycoderx.quiteusefulcommands.commands;

import io.github.mightycoderx.quiteusefulcommands.utils.ChatUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class SpawnMobCommand extends Command
{
	public SpawnMobCommand()
	{
		super("spawnmob", "<mob> [<count>]");
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args)
	{
		if(!(sender instanceof Player p))
		{
			ChatUtils.sendPrefixedMessage(sender, CommandMessage.ONLY_PLAYERS_COMMAND);
			return true;
		}

		if(args.length == 0 || args.length > 4) return false;

		String entityTypeArg = args[0].toUpperCase();

		if(Arrays.stream(EntityType.values()).noneMatch(type -> type.toString().equals(entityTypeArg)))
		{
			ChatUtils.sendPrefixedMessage(p, "&cInvalid entity");
			return true;
		}

		EntityType entityType = Arrays.stream(EntityType.values())
				.filter(type -> type.toString().equals(entityTypeArg))
				.toList().get(0);

		if(entityType == null)
		{
			ChatUtils.sendPrefixedMessage(p, "&cInvalid entity");
			return true;
		}

		int count = 1;
		if(args.length >= 2)
		{
			try
			{
				count = Integer.parseInt(args[1]);
			}
			catch (NumberFormatException ex)
			{
				ChatUtils.sendPrefixedMessage(p, "&cInvalid count number");
				return true;
			}
		}

		Location loc = p.getLocation();

		if(args.length >= 3)
		{
			switch(args[2])
			{
				case "target" ->
				{
					Block targetBlock = p.getTargetBlockExact(256);
					loc = targetBlock != null ? targetBlock.getLocation() : loc;
				}

				case "launch" ->
				{
					AtomicBoolean error = new AtomicBoolean(false);

					spawnMobs(p.getEyeLocation(), entityType, count, entity ->
					{
						double multiplier = 2;

						if(args.length == 4)
						{
							try
							{
								multiplier = Double.parseDouble(args[3]);
							}
							catch (NumberFormatException ex)
							{
								ChatUtils.sendPrefixedMessage(p, "&cInvalid count number");
								error.set(true);
								return;
							}
						}

						Vector newVelocity = p.getEyeLocation().getDirection().multiply(multiplier);

						if (entity instanceof Fireball fireball)
						{
							fireball.setDirection(newVelocity);
						}
						else
						{
							entity.setVelocity(newVelocity);
						}
					});

					if(error.get())
					{
						return true;
					}

					ChatUtils.sendPrefixedMessage(p, "&aLaunched &6" + count + "&2 " + entityType);
					return true;
				}

				default ->
				{
					ChatUtils.sendPrefixedMessage(p, "&cInvalid option &6" + args[2]);
					return true;
				}
			}
		}

		ChatUtils.sendPrefixedMessage(p, "&aSpawned &6" + count + "&2 " + entityType);
		spawnMobs(loc, entityType, count, null);

		return true;
	}

	private void spawnMobs(Location loc, EntityType entityType, int count, Consumer<Entity> modifier)
	{
		if(loc.getWorld() == null) return;

		for(int i = 0; i < count; i++)
		{
			Entity entity = loc.getWorld().spawnEntity(loc, entityType, true);
			if(modifier != null)
			{
				modifier.accept(entity);
			}
		}
	}

	@Override
	public List<String> tabComplete(Player player, String alias, String[] args)
	{
		if(args.length == 1)
		{
			return Arrays.stream(EntityType.values())
					.map(entityType -> entityType.toString().toLowerCase())
					.filter(arg -> arg.startsWith(args[0].toLowerCase()))
					.toList();
		}
		else if(args.length == 3)
		{
			return Stream.of("target", "launch")
					.filter(arg -> arg.startsWith(args[2])).toList();
		}

		return List.of();
	}
}
