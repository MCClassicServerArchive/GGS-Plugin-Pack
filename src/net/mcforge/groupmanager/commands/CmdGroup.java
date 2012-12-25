/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.groupmanager.commands;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.ManualLoad;
import net.mcforge.API.plugin.Command;
import net.mcforge.chat.ChatColor;
import net.mcforge.groupmanager.API.GroupManagerAPI;
import net.mcforge.groups.Group;
import net.mcforge.plugin.Main.Main;

@ManualLoad
public class CmdGroup extends Command {

	@Override
	public String[] getShortcuts() {
		return new String[0];
	}

	@Override
	public String getName() {
		return "group";
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
			if ("list".equals(args[0])) {
				String[] groups = GroupManagerAPI.listGroups();
				player.sendMessage("Group list:");
				for (int i = 0; i < groups.length; i++) {
					player.sendMessage(groups[i]);
				}
			}
		}
		else if (args.length == 2) {
			if ("info".equals(args[0])) {
				Group g = Group.find(args[1]);
				if (g == null) {
					player.sendMessage("Group not found!");
					return;
				}
				player.sendMessage("Group info:");
				player.sendMessage("name: " + g.name);
				player.sendMessage("permission: " + Integer.toString(g.permissionlevel));
				String opstr = String.valueOf(g.isOP);
				player.sendMessage("operatorgroup: " + opstr);
				if (Group.getDefault().equals(g)) {
					player.sendMessage("default group: true");
				}
			}
			if ("del".equals(args[0])) {
				if (GroupManagerAPI.deleteGroup(args[1])) {
					player.sendMessage("Successfully deleted group!");
				}
				else {
					player.sendMessage("Couldn't delete group!");
				}
			}
		}
		else if (args.length == 3) {
			if ("set".equals(args[0])) {
				if (args[2].startsWith("name:")) {
					if ("".equals(args[2].substring(5))) {
						help(player);
						return;
					}
					if (GroupManagerAPI.editGroupName(args[1], args[2].substring(5))) {
						player.sendMessage("Successfully changed group name!");
					}
					else {
						player.sendMessage("Failed to change group name!");
					}
				}
				else if (args[2].startsWith("permission:")) {
					if ("".equals(args[2].substring(11))) {
						help(player);
						return;
					}
					if (GroupManagerAPI.editGroupPermission(args[1], Integer.parseInt(args[2].substring(11)))) {
						player.sendMessage("Successfully changed permission level of group!");
					}
					else {
						player.sendMessage("Failed to set permission level of group!");
					}
				}
				else if (args[2].startsWith("color:")) {
					if (args[2].length() == 6) {
						help(player);
						return;
					}
					ChatColor color = ChatColor.fromName(args[2].substring(6));
					if (color == null) {
						player.sendMessage("Invalid group color specified!");
						Main.displayValidColors(player);
						return;
					}
					if (GroupManagerAPI.editGroupColor(args[1], color)) {
						player.sendMessage("Successfully changed the group's color!");
					}
					else {
						player.sendMessage("Failed to set color of group!");
					}
					
				}
				else if (args[2].startsWith("+op") || args[2].startsWith("-op")) {
					if (GroupManagerAPI.editGroupIsOp(args[1], args[2].startsWith("+op"))) {
						player.sendMessage("Successfully changed group operator status!");
					}
					else {
						player.sendMessage("Failed to set group to operator!");
					}
				}
				else {
					help(player);
				}
			}
			else {
				help(player);
			}
		}
		
		else if (args.length == 4) {
			if ("add".equals(args[0])) {
				if (GroupManagerAPI.createGroup(args[1], ChatColor.parse(args[3]), Integer.parseInt(args[2]))) {
					player.sendMessage("Successfully created new group!");
				}
				else {
					player.sendMessage("Failed to create group!");
				}
			}
			else
				help(player);
		}
		
		else if (args.length == 5) {
			if ("add".equals(args[0])) {
				if (GroupManagerAPI.createGroup(args[1], Integer.parseInt(args[2]), ChatColor.parse(args[3]), args[4].equals("+op"))) {
					player.sendMessage("Successfully created a new group!");
				}
				else {
					player.sendMessage("Failed to create group!");
				}
			}
			else
				help(player);
		}
		
		else {
			help(player);
		}
	}

	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/group add <name> <permission> <color> [+op/-op] - creates a new group");
		executor.sendMessage("/group del <name> - deletes a group");
		executor.sendMessage("/group set <groupname> <name:newname> - gives group a new name");
		executor.sendMessage("/group set <groupname> <permission:number> - sets permission level for group");
		executor.sendMessage("/group set <groupname> <+op/-op> - sets if group is an operator group or not");
		executor.sendMessage("/group set <groupname> <color:newcolor> - sets the group's default color");
		executor.sendMessage("/group info <name> - shows group information");
		executor.sendMessage("/group list - get group list");
	}
}
