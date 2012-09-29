/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mcforge.groupmanager.commands;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.plugin.Command;
import net.mcforge.groupmanager.API.GroupManagerAPI;
import net.mcforge.groups.Group;

/**
 *
 * @author Wouter Gerarts
 */
public class CmdGroupManager extends Command {

    @Override
    public String[] getShortcuts() {
        return new String[0];
    }

    @Override
    public String getName() {
        return "group";
    }

    @Override
    public boolean isOpCommand() {
        return true;
    }

    @Override
    public int getDefaultPermissionLevel() {
        return 100;
    }

    @Override
    public void execute(CommandExecutor player, String[] args) {
        if (args.length == 1)
        {
            if ("list".equals(args[0]))
            {
                String[] groups = GroupManagerAPI.ListGroups();
                player.sendMessage("Group list:");
                for (int i=0;i<groups.length;i++)
                {
                    player.sendMessage(groups[i]);
                }
                return;
            }
        }
        if (args.length == 2)
        {
            if ("info".equals(args[0]))
            {
                Group g = Group.find(args[1]);
                if (g == null)
                {
                    player.sendMessage("Group not found!");
                    return;
                }
                player.sendMessage("Group info:");
                player.sendMessage("name: " + g.name);
                player.sendMessage("permission: " + Integer.toString(g.permissionlevel));
                String opstr = g.isOP ? "true" : "false";
                player.sendMessage("operatorgroup: " + opstr);
                if (Group.getDefault().equals(g))
                {
                    player.sendMessage("default group: true");
                }
                return;
            }
            if ("del".equals(args[0]))
            {
                if (GroupManagerAPI.DeleteGroup(args[1]))
                {
                    player.sendMessage("Successfully deleted group!");
                }
                else 
                {
                    player.sendMessage("Couldn't delete group!");
                }
                return;
            }
        }
        if (args.length == 3)
        {
            if ("add".equals(args[0]))
            {
                if (GroupManagerAPI.CreateGroup(args[1], Integer.parseInt(args[2])))
                {
                    player.sendMessage("Successfully created new group!");
                }
                else
                {
                    player.sendMessage("Failed to create group!");
                }
                return;
            }
            if ("set".equals(args[0]))
            {
                if (args[2].startsWith("name:"))
                {
                    if ("".equals(args[2].substring(5))) { help(player); return; }
                    if (GroupManagerAPI.EditGroupName(args[1], args[2].substring(5)))
                    {
                        player.sendMessage("Successfully changed group name!");
                        return;
                    }
                    else
                    {
                        player.sendMessage("Failed to change group name!");
                        return;
                    }
                }
                else if (args[2].startsWith("permission:"))
                {
                    if ("".equals(args[2].substring(11))) { help(player); return; }
                    if (GroupManagerAPI.EditGroupPermission(args[1], Integer.parseInt(args[2].substring(11))))
                    {
                        player.sendMessage("Successfully changed permission level of group!");
                        return;
                    }
                    else
                    {
                        player.sendMessage("Failed to set permission level of group!");
                        return;
                    }
                }
                else if (args[2].startsWith("+op"))
                {
                    if (GroupManagerAPI.EditGroupIsOp(args[1], true))
                    {
                        player.sendMessage("Successfully changed group to operator group!");
                        return;
                    }
                    else
                    {
                        player.sendMessage("Failed to set group to operator!");
                        return;
                    }
                }
                else if (args[2].startsWith("-op"))
                {
                    if (GroupManagerAPI.EditGroupIsOp(args[1], false))
                    {
                        player.sendMessage("Successfully changed group to operator group!");
                        return;
                    }
                    else
                    {
                        player.sendMessage("Failed to set group to operator!");
                        return;
                    }
                }      
                else
                {
                    help(player);
                    return;
                }
            }
        }
        if (args.length == 4)
        {
            if ("add".equals(args[0]))
            {
                if ("+op".equals(args[3]))
                {
                    if (GroupManagerAPI.CreateGroup(args[1], Integer.parseInt(args[2]), true))
                    {
                        player.sendMessage("Successfully created a new group!");
                        return;
                    }
                    else
                    {
                        player.sendMessage("failed to create group!");
                        return;
                    }
                }
                if ("-op".equals(args[3]))
                {
                    if (GroupManagerAPI.CreateGroup(args[1], Integer.parseInt(args[2]), false))
                    {
                        player.sendMessage("Successfully created a new group!");
                        return;
                    }
                    else
                    {
                        player.sendMessage("failed to create group!");
                        return;
                    }
                }
            }
        }
        help (player);
    }

    @Override
    public void help(CommandExecutor executor) {
        executor.sendMessage("/group add <name> <permission> [+op/-op] - creates a new group");
        executor.sendMessage("/group del <name> - deletes a group");
        executor.sendMessage("/group set <groupname> <name:newname> - gives group a new name");
        executor.sendMessage("/group set <groupname> <permission:number> - sets permission level for group");
        executor.sendMessage("/group set <groupname> <+op/-op> - sets if group is an operator group or not");
        executor.sendMessage("/group info <name> - shows group information");
        executor.sendMessage("/group list - get group list");
    }
    
}
