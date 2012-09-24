/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mcforge.groupmanager.main;

import java.util.ArrayList;
import net.mcforge.groups.Group;
import net.mcforge.iomodel.Player;

/**
 *
 * @author Wouter Gerarts
 */
public class GroupActions {
    public static boolean setGroup(String playername, String groupname)
    {
        Player player = MainPlugin.Find(playername);
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
        return MainPlugin.Find(playername).getGroup();
    }
    public static boolean Promote(String username)
    {
        Group startgroup = getGroup(username);
        if (startgroup == null) { return false; }
        int perms = startgroup.permissionlevel;
        Group lowestabove = null;
        for (Group g : Group.getGroupList()) {
            if (g.permissionlevel > startgroup.permissionlevel) {
                if (lowestabove == null) { lowestabove = g; }
                if (lowestabove.permissionlevel > g.permissionlevel) { lowestabove = g; }
            }
        }
        Player p = MainPlugin.Find(username);
        if (p == null || lowestabove == null) { return false; }
        p.setGroup(lowestabove);
        return true;
    }
    public static boolean Demote(String username)
    {
        Group startgroup = getGroup(username);
        if (startgroup == null) { return false; }
        int perms = startgroup.permissionlevel;
        Group highestbelow = null;
        for (Group g : Group.getGroupList()) {
            if (g.permissionlevel < startgroup.permissionlevel) {
                if (highestbelow == null) { highestbelow = g; }
                if (highestbelow.permissionlevel < g.permissionlevel) { highestbelow = g; }
            }
        }
        Player p = MainPlugin.Find(username);
        if (p == null || highestbelow == null) { return false; }
        p.setGroup(highestbelow);
        return true;
    }
    
    // group commands
    
    public boolean createGroup(String name, int permission, boolean isop) {
        Group g = new Group(name, permission, isop, MainPlugin.server);
        return Group.Add(g);
    }
}