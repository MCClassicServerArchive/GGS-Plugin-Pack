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

@ManualLoad
public class Stop extends Command implements HelpItem {
	@Override
	public String getName() {
		return "stop";
	}
	
	@Override
	public String[] getShortcuts() {
		return new String[0];
	}

	@Override
	public boolean isOpCommandDefault() {
		return true;
	}

	@Override
	public int getDefaultPermissionLevel() {
		return 100;
	}

	@Override
	public void execute(CommandExecutor player, String[] args) {
		try {
			player.getServer().stop();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/stop - shuts the server down");
	}

    @Override
    public String getType() {
        return "mod";
    }
}

