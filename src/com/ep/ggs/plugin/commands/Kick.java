/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.plugin.commands;

import java.util.Arrays;

import com.ep.ggs.API.CommandExecutor;
import com.ep.ggs.API.ManualLoad;
import com.ep.ggs.API.help.HelpItem;
import com.ep.ggs.API.plugin.Command;
import com.ep.ggs.chat.Messages;
import com.ep.ggs.iomodel.Player;


@ManualLoad
public class Kick extends Command implements HelpItem {
	@Override
	public String[] getShortcuts() {
		return new String[] { "k" };
	}

	@Override
	public String getName() {
		return "kick";
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
		if (args.length == 0) {
			help(executor);
			return;
		}
		String kickReason = "";
		Player who = executor.getServer().findPlayer(args[0]);
		if (who == null) {
			executor.sendMessage("Player not found!");
			return;
		}
		if (who.equals(executor)) {
			executor.sendMessage("You can't kick yourself!");
			return;
		}
		if (who.getGroup().permissionlevel > executor.getGroup().permissionlevel) {
			executor.sendMessage("You can't kick higher ranked players!");
			return;
		}
		if (args.length > 1) { 
			kickReason = Messages.join(Arrays.copyOfRange(args, 1, args.length));
		}
		else {
			kickReason = "You have been kicked!";
		}
		who.kick(kickReason);
	}

	@Override
	public void help(CommandExecutor player) {
		player.sendMessage("/kick <player> [reason] - kicks the specified player with the specified reason");
	}

    @Override
    public String getType() {
        return "mod";
    }
}
