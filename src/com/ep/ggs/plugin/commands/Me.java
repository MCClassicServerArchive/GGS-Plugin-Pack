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
import com.ep.ggs.chat.Messages;
import com.ep.ggs.iomodel.Player;

@ManualLoad
public class Me extends PlayerCommand implements HelpItem {
	@Override
	public String[] getShortcuts() {
		return new String[0];
	}

	@Override
	public String getName() {
		return "me";
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
	public void execute(Player player, String[] args) {
		if (args.length == 0) {
			player.sendMessage("You!");
			return;
		}
		String message = Messages.join(args, " ");
		player.getChat().serverBroadcast("&6*" + player.getDisplayColor() + player.username + " " + message);
	}

	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("What do you need help with, m'boy? Are you stuck down a well?");
	}

    @Override
    public String getType() {
        return "other";
    }
}
