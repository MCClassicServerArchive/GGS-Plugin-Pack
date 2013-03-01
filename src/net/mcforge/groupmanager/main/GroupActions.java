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

import java.io.IOException;
import java.io.NotSerializableException;
import java.sql.SQLException;

import net.mcforge.chat.ChatColor;
import net.mcforge.groups.Group;
import net.mcforge.iomodel.Player;

public class GroupActions {
	public static boolean setGroup(String playername, String groupname) throws NotSerializableException, SQLException, IOException {
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
		
		Group old = Group.getGroup(playername);
		old.removeMember(playername);
		group.addMember(playername);

		Player.setAttribute("mcf_color", group.color, playername, GroupPlugin.server);
		return true;
	}

	public static boolean promote(String username) throws NotSerializableException, SQLException, IOException {
		Player p = GroupPlugin.server.findPlayer(username);
		Group startgroup = null;
		
		if (p != null) {
			startgroup = Group.getGroup(p);
		}
		if (startgroup == null) {
			startgroup = Group.getGroup(username);
		}
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
		if (p == null) {
			startgroup.removeMember(username);
			lowestabove.addMember(username);
			Player.setAttribute("mcf_color", lowestabove.color, username, GroupPlugin.server);
		}
		else {
			p.setGroup(lowestabove);
		}
		return true;
	}

	public static boolean demote(String username) throws NotSerializableException, SQLException, IOException {
		Player p = GroupPlugin.server.findPlayer(username);
		Group startgroup = null;
		
		if (p != null) {
			startgroup = Group.getGroup(p);
		}
		if (startgroup == null) {
			startgroup = Group.getGroup(username);
		}
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
		if (p == null) {
			startgroup.removeMember(username);
			highestbelow.addMember(username);
			Player.setAttribute("mcf_color", highestbelow.color, username, GroupPlugin.server);
		}
		else {
			p.setGroup(highestbelow);
		}
		return true;
	}

	public static Group getNextRank(String username) {
		Player p = GroupPlugin.server.findPlayer(username);
		Group startgroup = null;
		
		if (p != null) {
			startgroup = Group.getGroup(p);
		}
		if (startgroup == null) {
			startgroup = Group.getGroup(username);
		}
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
		if (lowestabove == null) {System.out.println("NOLOWAB"); return null; }
		return lowestabove;
	}

	public static Group getPreviousRank(String username) {
		Group startgroup = Group.getGroup(GroupPlugin.server.findPlayer(username));
		if (startgroup == null) {
			startgroup = Group.getGroup(username);
		}
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
