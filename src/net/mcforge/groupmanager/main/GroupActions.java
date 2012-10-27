/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mcforge.groupmanager.main;

import net.mcforge.groups.Group;
import net.mcforge.iomodel.Player;

public class GroupActions {
    public static boolean setGroup(String playername, String groupname)
    {
        Player player = GroupPlugin.Find(playername);
        Group group = Group.find(groupname);
        if (player != null && group != null)
        {
            player.setGroup(group);
            return true;
        }
        return false;
    }
    public static Group getGroup(String playername)
    {
        return GroupPlugin.Find(playername).getGroup();
    }
    public static boolean Promote(String username)
    {
        Group startgroup = getGroup(username);
        if (startgroup == null) { return false; }
        //int perms = startgroup.permissionlevel;
        Group lowestabove = null;
        for (Group g : Group.getGroupList()) {
            if (g.permissionlevel > startgroup.permissionlevel) {
                if (lowestabove == null) { lowestabove = g; }
                if (lowestabove.permissionlevel > g.permissionlevel) { lowestabove = g; }
            }
        }
        Player p = GroupPlugin.Find(username);
        if (p == null || lowestabove == null) { return false; }
        p.setGroup(lowestabove);
        return true;
    }
    public static boolean Demote(String username)
    {
        Group startgroup = getGroup(username);
        if (startgroup == null) { return false; }
        //int perms = startgroup.permissionlevel;
        Group highestbelow = null;
        for (Group g : Group.getGroupList()) {
            if (g.permissionlevel < startgroup.permissionlevel) {
                if (highestbelow == null) { highestbelow = g; }
                if (highestbelow.permissionlevel < g.permissionlevel) { highestbelow = g; }
            }
        }
        Player p = GroupPlugin.Find(username);
        if (p == null || highestbelow == null) { return false; }
        p.setGroup(highestbelow);
        return true;
    }
    
    // group commands
    
    public static boolean createGroup(String name, int permission, boolean isop) {
        Group g = new Group(name, permission, isop, GroupPlugin.server);
        return Group.Add(g);
    }
    
    public static boolean deleteGroup(String name)
    {
        return Group.find(name).Delete();
    }
}