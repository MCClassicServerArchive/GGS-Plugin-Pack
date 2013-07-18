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
import com.ep.ggs.banhandler.BanHandler;


@ManualLoad
public class Unban extends Command implements HelpItem {

	@Override
	public String[] getShortcuts() {
		return new String[0];
	}

	@Override
	public String getName() {
		return "unban";
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
	public void execute(CommandExecutor player, String[] args) {
		if (args.length == 1) {
			if (BanHandler.banHandler.isBanned(args[0])) {
				BanHandler.banHandler.unban(args[0]);
				player.sendMessage("You unbanned " + args[0]);
			}
			else {
				player.sendMessage(args[0] + " is not banned.");
			}
		}
		else {
			help(player);
		}
	}

	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/unban <player> - unbans a banned player");
	}

    @Override
    public String getType() {
        return "mod";
    }
}

