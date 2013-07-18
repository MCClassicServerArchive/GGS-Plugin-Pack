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
import com.ep.ggs.API.plugin.Command;
import com.ep.ggs.chat.Messages;


@ManualLoad
public class Staff extends Command implements HelpItem {
	@Override
	public String[] getShortcuts() {
		return new String[0];
	}

	@Override
	public String getName() {
		return "staff";
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
		List<String> devList = player.getServer().getPrivilegesHandler().getDevs();
		List<String> modList = player.getServer().getPrivilegesHandler().getMods();
		List<String> gcList = player.getServer().getPrivilegesHandler().getGCStaff();
		player.sendMessage("&9MCForge developers: &e" + Messages.join(devList.toArray(new String[devList.size()]), "&f, &e"));
		player.sendMessage("&9MCForge moderators: &e" + Messages.join(modList.toArray(new String[modList.size()]), "&f, &e"));
		player.sendMessage("&9MCForge GC staff: &e" + Messages.join(gcList.toArray(new String[gcList.size()]), "&f, &e"));
	}

	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/staff - shows the MCForge staff list");
	}

    @Override
    public String getType() {
        return "information";
    }
}

