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

import java.io.File;

@ManualLoad
public class Maps extends Command implements HelpItem {
	@Override
	public String[] getShortcuts() {
		return new String[0];
	}

	@Override
	public String getName() {
		return "maps";
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
		File levelsFolder = new File("levels");
		File[] levelFiles = levelsFolder.listFiles();
		StringBuilder finalStr = new StringBuilder();

		for (File f : levelFiles) {
			if (f.getName().equals("properties"))
				continue;
			if (f.getName().split("\\.")[1].equals("ggs")) {
				finalStr.append(f.getName().split("\\.")[0]);
				finalStr.append(", ");
			}
		}

		player.sendMessage(finalStr.toString());
	}

	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/maps - shows all the maps the server has");
	}

    @Override
    public String getType() {
        return "information";
    }
}

