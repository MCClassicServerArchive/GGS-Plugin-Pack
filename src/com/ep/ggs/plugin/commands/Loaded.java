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
import com.ep.ggs.API.plugin.Command;
import com.ep.ggs.world.Level;
import com.ep.ggs.world.LevelHandler;

@ManualLoad
public class Loaded extends Command implements HelpItem {
	@Override
	public String[] getShortcuts() {
		return new String[] { "levels" };
	}

	@Override
	public String getName() {
		return "loaded";
	}

	@Override
	public boolean isOpCommandDefault() {
		return false;
	}

	@Override
	public int getDefaultPermissionLevel() {
		return 0;
	}

	@Override
	public void execute(CommandExecutor player, String[] args) {
		LevelHandler handler = player.getServer().getClassicLevelHandler();
		StringBuilder finalStr = new StringBuilder();

		for (Level l : handler.getLevelList()) {
			finalStr.append(l.getName());
			finalStr.append(", ");
		}

		player.sendMessage(finalStr.toString());
	}

	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/loaded - shows the currently loaded levels");
	}

    @Override
    public String getType() {
        return "information";
    }
}

