package io.github.mightycoderx.quiteusefulcommands.tasks;

import io.github.mightycoderx.quiteusefulcommands.commands.CommandManager;
import io.github.mightycoderx.quiteusefulcommands.utils.EntityUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GodModeTask extends BukkitRunnable
{
	private final CommandManager commandManager;

	public GodModeTask(CommandManager commandManager)
	{
		this.commandManager = commandManager;
	}

	@Override
	public void run()
	{
		for(Player p : commandManager.getGodModePlayers())
		{
			if(EntityUtils.getDistanceFromGround(p.getLocation(), 0.1) <= 1)
			{
				p.setFlying(true);
			}
		}
	}
}
