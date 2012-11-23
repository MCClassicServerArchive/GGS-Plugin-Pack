/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.plugin.commands;

import java.util.List;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.ManualLoad;
import net.mcforge.API.plugin.Command;
import net.mcforge.iomodel.Player;

@ManualLoad
public class Players extends Command {
	@Override
	public String[] getShortcuts() {
		return new String[] { "online", "who" };
	}

	@Override
	public String getName() {
		return "players";
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
		List<Player> players = player.getServer().players;
		int size = players.size();
		if (size == 0) {
			player.sendMessage("There are no online players!");
			return;
		}
		String list = "";
		for (int i = 0; i < size; i++) {
			list += players.get(i).username + ", ";
		}
		list = list.substring(0, list.length() - 2);
		player.sendMessage(String.format("There %s %d player%s online: %s",
				size == 1 ? "is" : "are", size, size == 1 ? "" : "s", list));
	}

	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/players - shows the list of online players");
		executor.sendMessage("Shortcuts: /online, /who");
	}
}
