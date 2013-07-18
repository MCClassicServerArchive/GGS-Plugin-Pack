/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.plugin.commands;

import java.io.IOException;

import com.ep.ggs.API.CommandExecutor;
import com.ep.ggs.API.ManualLoad;
import com.ep.ggs.API.help.HelpItem;
import com.ep.ggs.API.plugin.Command;
import com.ep.ggs.world.Level;


@ManualLoad
public class Save extends Command implements HelpItem {

	@Override
	public void execute(CommandExecutor arg0, String[] arg1) {
		if (arg1.length == 0) {
			final int size = arg0.getServer().getClassicLevelHandler().getLevelList().size();
			for (int i = 0; i < size; i++) {
				try {
					arg0.getServer().getClassicLevelHandler().getLevelList().get(i).save();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			arg0.sendMessage("Saved " + size + " " + (size == 1 ? "map" : "maps") + "!");
		}
		else {
			Level l = arg0.getServer().getClassicLevelHandler().findLevel(arg1[0]);
			if (l == null)
				arg0.sendMessage("Level not found!");
			else {
				try {
					l.save();
				} catch (IOException e) {
					e.printStackTrace();
					arg0.sendMessage("There was an error saving " + l.getName() + "!");
					return;
				}
				arg0.sendMessage(l.getName() + " saved!");
			}
		}
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
	public String getName() {
		return "save";
	}

	@Override
	public String[] getShortcuts() {
		return new String[] { };
	}

	@Override
	public void help(CommandExecutor arg0) {
		arg0.sendMessage("/save - Save all the levels loaded!");
		arg0.sendMessage("/save [level] - Save [level]!");
	}

	@Override
	public boolean isOpCommandDefault() {
		return true;
	}

    @Override
    public String getType() {
        return "mod";
    }

}

