/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.plugin.commands;


import java.io.File;

import com.ep.ggs.API.CommandExecutor;
import com.ep.ggs.API.ManualLoad;
import com.ep.ggs.API.help.HelpItem;
import com.ep.ggs.API.plugin.Command;
import com.ep.ggs.chat.ChatColor;
import com.ep.ggs.world.LevelHandler;

@ManualLoad
public class Load extends Command implements HelpItem {
	@Override
	public String[] getShortcuts() {
		return new String[0];
	}

	@Override
	public String getName() {
		return "load";
	}

	@Override
	public boolean isOpCommandDefault() {
		return true;
	}
	
	@Override
    public boolean runInSeperateThread() {
        return true;
    }

	@Override
	public int getDefaultPermissionLevel() {
		return 100;
	}

	@Override
	public void execute(CommandExecutor player, String[] args) {
		if (args.length == 1) {
			LevelHandler handler = player.getServer().getClassicLevelHandler();
			File levelFile = new File("levels/" + args[0] + ".ggs");

			if (levelFile.exists()) {
				handler.loadLevel(levelFile);
				player.sendMessage(ChatColor.Bright_Green + "+ " + ChatColor.White + args[0] + " loaded!");
			}
			else {
				player.sendMessage("Level does not exist.");
			}
		}
		else {
			help(player);
		}
	}

	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/load - Loads an existing level");
	}

    @Override
    public String getType() {
        return "mod";
    }
}

