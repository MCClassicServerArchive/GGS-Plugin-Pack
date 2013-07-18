/*******************************************************************************
* Copyright (c) 2012 MCForge.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the GNU Public License v3.0
* which accompanies this distribution, and is available at
* http://www.gnu.org/licenses/gpl.html
******************************************************************************/
package com.ep.ggs.plugin.commands;

import java.util.List;

import com.ep.ggs.API.CommandExecutor;
import com.ep.ggs.API.ManualLoad;
import com.ep.ggs.API.help.HelpItem;
import com.ep.ggs.API.plugin.PlayerCommand;
import com.ep.ggs.iomodel.Player;


@ManualLoad
public class Summon extends PlayerCommand implements HelpItem {
	@Override
	public String[] getShortcuts() {
		return new String[] { "s" };
	}

	@Override
	public String getName() {
		return "summon";
	}

	@Override
	public boolean isOpCommandDefault() {
		return false;
	}

	@Override
	public int getDefaultPermissionLevel() {
		return 80;
	}

	@Override
	public void execute(Player executor, String[] args) {
		if (args.length < 1) {
			executor.sendMessage("Please specify a player!");
			help(executor);
			return;
		}
		if (args[0].equalsIgnoreCase("all")) {
			List<Player> players = executor.getServer().getClassicPlayers();
			for (int i = 0; i < players.size(); i++) {
				Player pl = players.get(i);
				if ((pl.getLevel() == executor.getLevel()) && 
				    (executor.getGroup().permissionlevel >= pl.getGroup().permissionlevel)) {
					pl.setPos(executor.getX(), executor.getY(), executor.getZ(), executor.yaw, executor.pitch);
					pl.sendMessage("You have been summoned by " + executor.getDisplayName());
				}
			}
		}
		else {
			Player who = executor.getServer().findPlayer(args[0]);
			if (who == null) {
				executor.sendMessage("Player not found");
				return;
			}
			if (who == executor) {
				executor.sendMessage("Cannot summon yourself!");
				return;
			}
			if (who.getGroup().permissionlevel > executor.getGroup().permissionlevel) {
				executor.sendMessage("Cannot summon players of same or equal ranks!");
				return;
			}
			
			if (who.getLevel() != executor.getLevel())
					who.changeLevel(executor.getLevel());
			who.setPos(executor.getX(), executor.getY(), executor.getZ(), executor.yaw, executor.pitch);
			who.sendMessage("You have been summoned by " + executor.getDisplayName());
		}
	}

	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/summon <player> - summons the specified player to you");
		executor.sendMessage("/summon all - summons every player on the same level to you");
	}

    @Override
    public String getType() {
        return "other";
    }
}

