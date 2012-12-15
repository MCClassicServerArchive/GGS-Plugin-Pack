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
package net.mcforge.groupmanager.API;

import net.mcforge.chat.ChatColor;
import net.mcforge.groupmanager.main.GroupActions;
import net.mcforge.groups.Group;

/**
*
* @author Wouter Gerarts
*/
public class GroupManagerAPI {
	/**
	* Demote a player
	* @param playername name of the player to demote
	* @return if successful
	*/
	public static boolean demotePlayer(String playername) {
		return GroupActions.demote(playername);
	}
	/**
	* Promote a player
	* @param playername name of the player to promote
	* @return if successful
	*/
	public static boolean promotePlayer(String playername) {
		return GroupActions.promote(playername);
	}
	/**
	* Set player's group
	* @param playername the player's name
	* @param groupname the group name
	* @return if successful
	*/
	public static boolean setPlayerGroup(String playername, String groupname) {
		return GroupActions.setGroup(playername, groupname);
	}
	/**
	* Creates a new group
	* @param name The name of the group
	* @param permission The 
	* permission level of the group
	* @return if successful
	*/
	public static boolean createGroup(String name, ChatColor color, int permission) {
		return createGroup(name, permission, color, false);
	}
	/**
	* Creates a new group
	* @param name The name of the group
	* @param permission The permission level of the group
	* @param color The color of the group
	* @param isop If the group is an operator group
	* @return If successful
	*/
	public static boolean createGroup(String name, int permission, ChatColor color, boolean isop) {
		return GroupActions.createGroup(name, color, permission, isop);
	}
	/**
	* Deletes a group
	* @param name the name of the group to remove
	* @return if successful
	*/
	public static boolean deleteGroup(String name) {
		return GroupActions.deleteGroup(name);
	}
	/**
	* Change a group's name
	* @param groupname the original name
	* @param newname the new group name
	* @return if successful
	*/
	public static boolean editGroupName(String groupname, String newname) {
		return Group.find(groupname).SetName(newname);
	}
	/**
	* Change a group's permission level
	* @param groupname the group name
	* @param permissionlevel the new permission level
	* @return if successful
	*/
	public static boolean editGroupPermission(String groupname, int permissionlevel) {
		return Group.find(groupname).SetPermission(permissionlevel);
	}
	/**
	* Change if a group is an operator group or not
	* @param groupname the group name
	* @param isop if the group should be operator
	* @return if successful
	*/
	public static boolean editGroupIsOp(String groupname, boolean isop) {
		return Group.find(groupname).SetIsOp(isop);
	}
	/**
	* Get all groups as list
	* @return a String array of group names
	*/
	public static String[] listGroups() {
		String[] names = new String[Group.getGroupList().size()];
		for (int i=0;i<names.length;i++)
		{
			names[i] = Group.getGroupList().toArray(new Group[Group.getGroupList().size()])[i].name;
		}
		return names;
	}
}

