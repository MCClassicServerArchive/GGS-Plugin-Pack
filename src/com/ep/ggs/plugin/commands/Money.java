/*******************************************************************************
* Copyright (c) 2012 MCForge.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the GNU Public License v3.0
* which accompanies this distribution, and is available at
* http://www.gnu.org/licenses/gpl.html
******************************************************************************/
package com.ep.ggs.plugin.commands;

import com.ep.ggs.API.CommandExecutor;
import com.ep.ggs.API.ManualLoad;
import com.ep.ggs.API.help.HelpItem;
import com.ep.ggs.API.plugin.Command;
import com.ep.ggs.iomodel.Player;

@ManualLoad
public class Money extends Command implements HelpItem {
	@Override
	public String[] getShortcuts() {
		return new String[0];
	}

	@Override
	public String getName() {
		return "money";
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
	public void execute(CommandExecutor executor, String[] args) {
		Player who;
		if (args.length > 0) {
			who = executor.getServer().findPlayer(args[0]);
			if (who == null) {
				executor.sendMessage("Player not found!");
				return;
			}
		}
		else {
			if (executor instanceof Player) {
				who = (Player)executor;
			}
			else {
				executor.sendMessage("Please specify a player!");
				help(executor);
				return;
			}
		}
		
		executor.sendMessage(who.getName() + " has &6" + who.getAttribute("mcf_money", Integer.class) + " " + executor.getServer().CurrencyName);
	}

	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/money <player> - shows the player's money");
		if (executor instanceof Player)
			executor.sendMessage("/money - shows your money");
	}

    @Override
    public String getType() {
        return "eco";
    }
}
