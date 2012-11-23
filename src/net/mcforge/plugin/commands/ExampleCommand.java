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
public class ExampleCommand extends PlayerCommand {

	@Override
	public void execute(Player player, String[] arg1) {
		player.sendMessage("HI!");
	}

	@Override
	public int getDefaultPermissionLevel() {
		return 0;
	}

	@Override
	public String getName() {
		return "test";
	}

	@Override
	public String[] getShortcuts() {
		return new String[] { "test1" };
	}

	@Override
	public void help(CommandExecutor sender) {
		sender.sendMessage("I say Hi!");
	}

	@Override
	public boolean isOpCommandDefault() {
		return false;
	}

}

