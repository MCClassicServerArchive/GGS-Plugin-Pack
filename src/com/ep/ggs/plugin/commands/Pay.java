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
import com.ep.ggs.API.plugin.PlayerCommand;
import com.ep.ggs.iomodel.Player;

@ManualLoad
public class Pay extends PlayerCommand implements HelpItem {
	@Override
	public String[] getShortcuts() {
		return new String[0];
	}

	@Override
	public String getName() {
		return "pay";
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
	public void execute(Player executor, String[] args) {
		if (args.length < 2) {
			executor.sendMessage("Please specify a player and the amount to take!");
			help(executor);
			return;
		}
		Player who = executor.getServer().findPlayer(args[0]);
		int amount = 0;
		if (who == null) {
			executor.sendMessage("Player not found!");
			return;
		}
		try {
			amount = Integer.parseInt(args[1]);
		}
		catch(NumberFormatException ex) {
			executor.sendMessage("Please specify a valid integer!");
			return;
		}
		if (amount < 0) {
			executor.sendMessage("Please specify a positive integer!");
			return;
		}
		if (executor.getMoney() - amount < 0) {
			executor.sendMessage("You don't have that much " + executor.getServer().CurrencyName);
			return;
		}
		if (who.getMoney() + amount > Integer.MAX_VALUE) {
			executor.sendMessage(who.getDisplayName() + " can't have that much " + executor.getServer().CurrencyName);
			return;
		}
		executor.setMoney(executor.getMoney() - amount);
		who.setMoney(who.getMoney() + amount);
		who.getServer().sendGlobalMessage(executor.getDisplayName() + " paid " + who.getDisplayName() + " " + amount + " " + who.getServer().CurrencyName);
	}

	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/pay <player> <amount> - pays the specified amount of " + 
	                         executor.getServer().CurrencyName + " to the specified player!");
	}

    @Override
    public String getType() {
        return "eco";
    }
}

