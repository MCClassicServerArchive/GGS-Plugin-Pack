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
package net.mcforge.groupmanager.main;

import net.mcforge.chat.ChatColor;
import net.mcforge.groups.Group;
import net.mcforge.iomodel.Player;

public class GroupActions {
	public static boolean setGroup(String playername, String groupname) {
		Player player = GroupPlugin.find(playername);
		Group group = Group.find(groupname);
		if (group == null) { return false; }
		if (player != null) {
			if (player.getDisplayColor() == player.getGroup().color) {
				player.setDisplayColor(group.color);
			}
			player.setGroup(group);
			return true;
		}
		else {
			Group old = Group.getGroup(playername);
			old.removeMember(playername);
			group.addMember(playername);
			//TODO: change player's color
			return true;
		}
	}

	public static boolean promote(String username) {
		Group startgroup = Group.getGroup(username);
		if (startgroup == null) { return false; }
		Group lowestabove = null;
		for (Group g : Group.getGroupList()) {
			if (g.permissionlevel > startgroup.permissionlevel) {
				if (lowestabove == null) {
					lowestabove = g;
				}
				if (lowestabove.permissionlevel > g.permissionlevel) {
					lowestabove = g;
				}
			}
		}
		if (lowestabove == null) { return false; }
		Player p = GroupPlugin.find(username);
		if (p == null) {
			startgroup.removeMember(username);
			lowestabove.addMember(username);
			//TODO: colors
		}
		else {
			p.setGroup(lowestabove);
		}
		return true;
	}

	public static boolean demote(String username) {
		Group startgroup = Group.getGroup(username);
		if (startgroup == null) { return false; }
		Group highestbelow = null;
		for (Group g : Group.getGroupList()) {
			if (g.permissionlevel < startgroup.permissionlevel) {
				if (highestbelow == null) {
					highestbelow = g;
				}
				if (highestbelow.permissionlevel < g.permissionlevel) {
					highestbelow = g;
				}
			}
		}
		if (highestbelow == null) { return false; }
		Player p = GroupPlugin.find(username);
		if (p == null) {
			startgroup.removeMember(username);
			highestbelow.addMember(username);
			//TODO: colors
		}
		else {
			p.setGroup(highestbelow);
		}
		return true;
	}

	public static Group getNextRank(String username) {
		Group startgroup = Group.getGroup(username);
		if (startgroup == null) { return null; }
		Group lowestabove = null;
		for (Group g : Group.getGroupList()) {
			if (g.permissionlevel > startgroup.permissionlevel) {
				if (lowestabove == null) {
					lowestabove = g;
				}
				if (lowestabove.permissionlevel > g.permissionlevel) {
					lowestabove = g;
				}
			}
		}
		if (lowestabove == null) { return null; }
		return lowestabove;
	}

	public static Group getPreviousRank(String username) {
		Group startgroup = Group.getGroup(username);
		if (startgroup == null) { return null; }
		Group highestbelow = null;
		for (Group g : Group.getGroupList()) {
			if (g.permissionlevel < startgroup.permissionlevel) {
				if (highestbelow == null) {
					highestbelow = g;
				}
				if (highestbelow.permissionlevel < g.permissionlevel) {
					highestbelow = g;
				}
			}
		}
		if (highestbelow == null) { return null; }
		return highestbelow;
	}

	public static boolean createGroup(String name, ChatColor color, int permission, boolean isop) {
		Group g = new Group(name, permission, isop, color, null);
		return Group.add(g);
	}

	public static boolean deleteGroup(String name) {
		return Group.find(name).delete();
	}
}
