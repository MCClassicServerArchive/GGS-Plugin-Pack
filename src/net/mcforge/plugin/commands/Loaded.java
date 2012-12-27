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
import net.mcforge.API.plugin.Command;
import net.mcforge.plugin.help.HelpItem;
import net.mcforge.world.Level;
import net.mcforge.world.LevelHandler;

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
		LevelHandler handler = player.getServer().getLevelHandler();
		StringBuilder finalStr = new StringBuilder();

		for (Level l : handler.getLevelList()) {
			finalStr.append(l.name);
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

