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

@ManualLoad
public class Give extends Command  {
	@Override
	public String[] getShortcuts() {
		return new String[0];
	}

	@Override
	public String getName() {
		return "give";
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
			executor.sendMessage("Please specify a player and the amount to give!");
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
		if (who.getMoney() + amount > Integer.MAX_VALUE) {
			executor.sendMessage("Players can't have more than 2147483647 " + executor.getServer().CurrencyName);
			return;
		}
		who.setMoney(who.getMoney() + amount);
		who.getServer().sendGlobalMessage(who.getDisplayName() +  " has been given " + amount + " " + who.getServer().CurrencyName);
	}

	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/give <player> <amount> - gives the specified amount of " + 
	                         executor.getServer().CurrencyName + " to the specified player!");
	}
}

