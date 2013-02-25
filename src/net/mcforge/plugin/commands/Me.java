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
import net.mcforge.API.plugin.PlayerCommand;
import net.mcforge.chat.Messages;
import net.mcforge.iomodel.Player;
import net.mcforge.API.help.HelpItem;

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
