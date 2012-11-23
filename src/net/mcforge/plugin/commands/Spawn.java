/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.plugin.commands;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.ManualLoad;
import net.mcforge.API.plugin.PlayerCommand;
import net.mcforge.iomodel.Player;

@ManualLoad
public class Spawn extends PlayerCommand
{
	@Override
	public String[] getShortcuts()
	{
		return new String[0];
	}

	@Override
	public String getName()
	{
		return "spawn";
	}

	@Override
	public boolean isOpCommandDefault()
	{
		return false;
	}

	@Override
	public int getDefaultPermissionLevel()
	{
		return 0;
	}

	@Override
	public void execute(Player player, String[] args)
	{
		player.setPos((short)((0.5 + player.getLevel().spawnx) * 32), (short)((1 + player.getLevel().spawny) * 32), (short)((0.5 + player.getLevel().spawnz) * 32));
	}

	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/spawn - Sends you to the spawn of the current map.");
	}
}

