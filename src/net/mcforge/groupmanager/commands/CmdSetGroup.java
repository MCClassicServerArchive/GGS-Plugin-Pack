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
import net.mcforge.groups.Group;
import net.mcforge.iomodel.Player;
import net.mcforge.util.Utils;

@ManualLoad
public class CmdSetGroup extends Command {

	@Override
	public String[] getShortcuts() {
		return new String[] { "setrank", "rank" };
	}

	@Override
	public String getName() {
		return "setgroup";
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
		if (args.length == 2)
		{
			Player who = executor.getServer().findPlayer(args[0]);
			if (who != null) {
				if (executor.getServer().findPlayer(args[0]) == executor) {
					executor.sendMessage("You can't change your own rank!");
					return;
				}
				if (who.getGroup().permissionlevel >= executor.getGroup().permissionlevel) {
					executor.sendMessage("You can't rank players of the equal or higher rank!");
					return;
				}
				
				Group toRank = Group.find(args[1]);
				if (toRank != null) {
					if (toRank.permissionlevel >= executor.getGroup().permissionlevel) {
						executor.sendMessage("You can't rank players to a rank higher than or equal to yours!");
						return;
					}
				}
				
				if (GroupManagerAPI.setPlayerGroup(args[0], args[1])) {
					Player pl = executor.getServer().findPlayer(args[0]);
					executor.getServer().sendGlobalMessage(pl.getDisplayColor() + Utils.getPossessiveForm(pl.username) + 
                            							   executor.getServer().defaultColor + 
                            							   " rank was set to " + toRank.color + toRank.name);
				}
				else {
					executor.sendMessage("Failed to set player's rank!");
				}
			}
			else {
				Group ranked = Group.getGroup(args[0]);
				if (ranked == null) {
					Group.getDefault().addMember(args[0]);
					ranked = Group.getDefault();
				}
				
				if (ranked.permissionlevel >= executor.getGroup().permissionlevel) {
					executor.sendMessage("You can't rank players of the equal or higher rank!");
					return;
				}
				
				Group toRank = Group.find(args[1]);
				if (toRank != null) {
					if (toRank.permissionlevel >= executor.getGroup().permissionlevel) {
						executor.sendMessage("You can't rank players to a rank higher than or equal to yours!");
						return;
					}
				}
				
				if (GroupManagerAPI.setPlayerGroup(args[0], args[1])) {
					Group g = Group.find(args[1]);
					executor.getServer().sendGlobalMessage("&f(Offline)" + Utils.getPossessiveForm(args[0]) +
														   executor.getServer().defaultColor + 
														   " rank was set to " + g.color + g.name);
				
				}
				else {
					executor.sendMessage("Failed to set player's rank!");
				}
			}
		}
		else {
			help(executor);
			return;
		}
	}

	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/setgroup <player> <group> - sets player group");
	}
	
}
