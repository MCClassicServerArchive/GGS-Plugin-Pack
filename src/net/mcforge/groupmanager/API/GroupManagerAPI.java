/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package net.mcforge.groupmanager.API;

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
	public static boolean DemotePlayer(String playername) {
		return GroupActions.Demote(playername);
	}
	/**
	* Promote a player
	* @param playername name of the player to promote
	* @return if successful
	*/
	public static boolean PromotePlayer(String playername) {
		return GroupActions.Promote(playername);
	}
	/**
	* Set player's group
	* @param playername the player's name
	* @param groupname the group name
	* @return if successful
	*/
	public static boolean SetPlayerGroup(String playername, String groupname) {
		return GroupActions.setGroup(playername, groupname);
	}
	/**
	* Creates a new group
	* @param name The name of the group
	* @param permission The permission level of the group
	* @return if successful
	*/
	public static boolean CreateGroup(String name, int permission) {
		return CreateGroup(name, permission, false);
	}
	/**
	* Creates a new group
	* @param name The name of the group
	* @param permission The permission level of the group
	* @param isop if the group is an operator group
	* @return if successful
	*/
	public static boolean CreateGroup(String name, int permission, boolean isop) {
		return GroupActions.createGroup(name, permission, isop);
	}
	/**
	* Deletes a group
	* @param name the name of the group to remove
	* @return if successful
	*/
	public static boolean DeleteGroup(String name) {
		return GroupActions.deleteGroup(name);
	}
	/**
	* Change a group's name
	* @param groupname the original name
	* @param newname the new group name
	* @return if successful
	*/
	public static boolean EditGroupName(String groupname, String newname) {
		return Group.find(groupname).SetName(newname);
	}
	/**
	* Change a group's permission level
	* @param groupname the group name
	* @param permissionlevel the new permission level
	* @return if successful
	*/
	public static boolean EditGroupPermission(String groupname, int permissionlevel) {
		return Group.find(groupname).SetPermission(permissionlevel);
	}
	/**
	* Change if a group is an operator group or not
	* @param groupname the group name
	* @param isop if the group should be operator
	* @return if successful
	*/
	public static boolean EditGroupIsOp(String groupname, boolean isop) {
		return Group.find(groupname).SetIsOp(isop);
	}
	/**
	* Get all groups as list
	* @return a String array of group names
	*/
	public static String[] ListGroups() {
		String[] names = new String[Group.getGroupList().size()];
		for (int i=0;i<names.length;i++)
		{
			names[i] = Group.getGroupList().toArray(new Group[Group.getGroupList().size()])[i].name;
		}
		return names;
	}
}

