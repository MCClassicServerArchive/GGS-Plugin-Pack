/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.plugin.commands;

import com.ep.ggs.API.CommandExecutor;
import com.ep.ggs.API.ManualLoad;
import com.ep.ggs.API.help.HelpItem;
import com.ep.ggs.API.plugin.PlayerCommand;
import com.ep.ggs.iomodel.Player;

@ManualLoad
public class Spawn extends PlayerCommand implements HelpItem
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
		player.setPos((short)((0.5 + player.getLevel().getSpawnX()) * 32), (short)((1 + player.getLevel().getSpawnY()) * 32), (short)((0.5 + player.getLevel().getSpawnZ()) * 32));
	}

	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/spawn - Sends you to the spawn of the current map.");
	}

    @Override
    public String getType() {
        return "mod";
    }
}

