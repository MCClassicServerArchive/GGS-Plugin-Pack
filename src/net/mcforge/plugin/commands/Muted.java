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
import net.mcforge.plugin.help.HelpItem;

@ManualLoad
public class Muted extends Command implements HelpItem {
	@Override
	public String[] getShortcuts() {
		return new String[0];
	}

	@Override
	public String getName() {
		return "muted";
	}

	@Override
	public boolean isOpCommandDefault() {
		return true;
	}

	@Override
	public int getDefaultPermissionLevel() {
		return 100;
	}

	@Override
	public void execute(CommandExecutor executor, String[] args) {
		List<Player> muted = Mute.muted;
		if (muted.size() == 0) {
			executor.sendMessage("There are no muted players");
			return;
		}
		String ret = "";
		for (int i = 0; i < muted.size(); i++) {
			ret += muted.get(i).username + ", ";
		}
		ret = ret.substring(0, ret.length() - 2);
		executor.sendMessage("There are " + muted.size() + " muted players: " + ret);
	}
 /*
  */
	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/muted - Shows who is muted on the server");
	}

    @Override
    public String getType() {
        return "moderation";
    }
}

