/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mcforge.groupmanager.commands;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.ManualLoad;
import net.mcforge.API.plugin.Command;
import net.mcforge.groupmanager.API.GroupManagerAPI;
import net.mcforge.groupmanager.main.GroupPlugin;
import net.mcforge.iomodel.Player;

@ManualLoad
public class CmdPromote extends Command {

	@Override
	public String[] getShortcuts() {
		return new String[0];
	}

	@Override
	public String getName() {
		return "promote";
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
		if (args.length == 1) {
			Player who = executor.getServer().findPlayer(args[0]);
			if (executor.getServer().findPlayer(args[0]) == executor) {
				executor.sendMessage("You can't promote yourself!");
				return;
			}
			if (who.getGroup().permissionlevel >= executor.getGroup().permissionlevel) {
				executor.sendMessage("You can't promote players of the equal or higher rank!");
				return;
			}

			if (GroupManagerAPI.promotePlayer(args[0])) {
				executor.sendMessage("Promoted '" + Player.find(GroupPlugin.server, args[0]).username + "'");
				Player.find(GroupPlugin.server, args[0]).sendMessage("You have been promoted to " + executor.getGroup().name + "!");
			}
			else {
				executor.sendMessage("Couldn't promote '" + args[0] + "'");
			}
		}
		else {
			help(executor);
			return;
		}
	}

	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/promote <player> - promotes a player");
	}

}
