/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.irc;

import com.ep.ggs.API.CommandExecutor;
import com.ep.ggs.API.plugin.Command;

public class CmdReloadIRCControllers extends Command {

	@Override
	public String getName() {
		return "reloadirccontrollers";
	}
	@Override
	public String[] getShortcuts() {
		return new String[] { "rcontrollers" };
	}
	@Override
	public int getDefaultPermissionLevel() {
		return 50;
	}
	@Override
	public boolean isOpCommandDefault() {
		return true;
	}
	@Override
	public void execute(CommandExecutor executor, String[] args) {
		IRCUser.loadIRCControllers();
		executor.sendMessage("Reloaded IRC-Controller list!");
	}
	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/reloadirccontrollers - reloads the IRC controllers list");
		executor.sendMessage("Shortcut: /rcontrollers");
	}
}
