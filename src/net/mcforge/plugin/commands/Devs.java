/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.plugin.commands;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.ManualLoad;
import net.mcforge.API.plugin.PlayerCommand;
import net.mcforge.chat.Messages;
import net.mcforge.iomodel.Player;
import net.mcforge.server.Server;

@ManualLoad
public class Devs extends PlayerCommand  {
	@Override
	public String[] getShortcuts() {
		return new String[] { "devs" };
	}

	@Override
	public String getName() {
		return "developers";
	}

	@Override
	public boolean isOpCommand() {
		return false;
	}

	@Override
	public int getDefaultPermissionLevel() {
		return 0;
	}

	@Override
	public void execute(Player player, String[] args) {
		String[] devArray = Server.devs.toArray(new String[Server.devs.size()]);
		String devs = Messages.join(devArray, "&f, &e");
		player.sendMessage("&9MCForge developers: &e" + devs);
	}

	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/developers - shows the MCForge developer list");
		executor.sendMessage("Shortcuts: /devs");
	}
}
