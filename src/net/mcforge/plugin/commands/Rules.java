/*******************************************************************************
* Copyright (c) 2012 MCForge.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the GNU Public License v3.0
* which accompanies this distribution, and is available at
* http://www.gnu.org/licenses/gpl.html
******************************************************************************/
package net.mcforge.plugin.commands;

import java.io.IOException;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.ManualLoad;
import net.mcforge.API.plugin.PlayerCommand;
import net.mcforge.iomodel.Player;
import net.mcforge.util.FileUtils;

@ManualLoad
public class Rules extends PlayerCommand  {
	@Override
	public String[] getShortcuts() {
		return new String[0];
	}

	@Override
	public String getName() {
		return "rules";
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
		try {
			FileUtils.createIfNotExist("properties", "rules.txt", "No rules added yet!");
		} catch (IOException e) {
			e.printStackTrace();
			executor.sendMessage("Error while displaying rules!");
			return;
		}
		String[] rules;
		try {
			rules = FileUtils.readAllLines("properties/rules.txt");
		} catch (IOException e) {
			e.printStackTrace();
			executor.sendMessage("Error while displaying rules!");
			return;
		}
		for (int i = 0; i < rules.length; i++) {
			executor.sendMessage(rules[i]);
		}
	}

	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/rules - shows the server rules");
	}
}
