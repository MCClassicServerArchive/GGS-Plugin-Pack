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
import net.mcforge.API.plugin.Command;
import net.mcforge.iomodel.Player;
import net.mcforge.plugin.help.HelpItem;

@ManualLoad
public class Take extends Command implements HelpItem {
	@Override
	public String[] getShortcuts() {
		return new String[0];
	}

	@Override
	public String getName() {
		return "take";
	}

	@Override
	public boolean isOpCommandDefault() {
		return true;
	}

	@Override
	public int getDefaultPermissionLevel() {
		return 80;
	}

	@Override
	public void execute(CommandExecutor executor, String[] args) {
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
		if (who.getMoney() - amount < 0) {
			executor.sendMessage("Players can't have less than 0 " + executor.getServer().CurrencyName);
			return;
		}
		who.setMoney(who.getMoney() - amount);
		who.getServer().sendGlobalMessage(who.getDisplayName() +  " has been rattled down for " + amount + " " + who.getServer().CurrencyName);
	}

	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/take <player> <amount> - takes the specified amount of " + 
	                         executor.getServer().CurrencyName + " from the specified player!");
	}

    @Override
    public String getType() {
        return "eco";
    }
}

