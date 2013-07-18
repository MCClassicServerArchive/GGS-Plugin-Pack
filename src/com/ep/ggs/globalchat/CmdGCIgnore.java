/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.globalchat;

import com.ep.ggs.API.CommandExecutor;
import com.ep.ggs.API.plugin.PlayerCommand;
import com.ep.ggs.iomodel.Player;

public class CmdGCIgnore extends PlayerCommand {

	@Override
	public String getName() {
		return "globalchatignore";
	}

	@Override
	public String[] getShortcuts() {
		return new String[] { "gcignore", "ignoregc" };
	}

	@Override
	public int getDefaultPermissionLevel() {
		return 0;
	}

	@Override
	public boolean isOpCommandDefault() {
		return false;
	}

	@Override
	public void execute(Player executor, String[] args) {
		boolean ignoring = DataHandler.ignoringGC(executor);
		DataHandler.setValue(executor, DataHandler.ignoring, !ignoring, true);

		executor.sendMessage(ignoring ? "You are no longer ignoring the Global Chat!"
				: "You are now ignoring the Global Chat!");
	}

	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/gcignore - ignore the Global Chat");
	}
}
