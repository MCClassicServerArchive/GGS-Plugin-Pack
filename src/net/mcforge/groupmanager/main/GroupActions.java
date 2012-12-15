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
	public static boolean setGroup(String playername, String groupname)
	{
		Player player = GroupPlugin.find(playername);
		Group group = Group.find(groupname);
		if (player != null && group != null)
		{
			player.setGroup(group);
			if (player.getDisplayColor() == ChatColor.White){
				player.setDisplayColor(group.color);
			} 
			return true;
		}
		return false;
	}
	public static Group getGroup(String playername)
	{
		return GroupPlugin.find(playername).getGroup();
	}
	public static boolean promote(String username)
	{
		Group startgroup = getGroup(username);
		if (startgroup == null) { return false; }
		Group lowestabove = null;
		for (Group g : Group.getGroupList()) {
			if (g.permissionlevel > startgroup.permissionlevel) {
				if (lowestabove == null) { lowestabove = g; }
				if (lowestabove.permissionlevel > g.permissionlevel) { lowestabove = g; }
			}
		}
		Player p = GroupPlugin.find(username);
		if (p == null || lowestabove == null) { return false; }
		p.setGroup(lowestabove);
		return true;
	}
	public static boolean demote(String username)
	{
		Group startgroup = getGroup(username);
		if (startgroup == null) { return false; }
		Group highestbelow = null;
		for (Group g : Group.getGroupList()) {
			if (g.permissionlevel < startgroup.permissionlevel) {
				if (highestbelow == null) { highestbelow = g; }
				if (highestbelow.permissionlevel < g.permissionlevel) { highestbelow = g; }
			}
		}
		Player p = GroupPlugin.find(username);
		if (p == null || highestbelow == null) { return false; }
		p.setGroup(highestbelow);
		return true;
	}
	
	// group commands
	
	public static boolean createGroup(String name, ChatColor color, int permission, boolean isop) {
		Group g = new Group(name, permission, isop, color, null, GroupPlugin.server);
		return Group.add(g);
	}
	
	public static boolean deleteGroup(String name)
	{
		return Group.find(name).Delete();
	}
}
