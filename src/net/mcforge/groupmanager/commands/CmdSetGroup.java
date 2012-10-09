/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mcforge.groupmanager.commands;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.ManualLoad;
import net.mcforge.API.plugin.Command;
import net.mcforge.groupmanager.API.GroupManagerAPI;
import net.mcforge.groupmanager.main.GroupPlugin;
import net.mcforge.groups.Group;
import net.mcforge.iomodel.Player;

/**
 *
 * @author Wouter Gerarts
 */
public class CmdSetGroup extends Command implements ManualLoad {

    @Override
    public String[] getShortcuts() {
        return new String[0];
    }

    @Override
    public String getName() {
        return "setgroup";
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
        if (args.length == 2)
        {
            if (GroupManagerAPI.SetPlayerGroup(args[0], args[1]))
            {
                player.sendMessage("Successfully changed rank!");
                Player.find(GroupPlugin.server, args[0]).sendMessage("Your rank was changed to " + Group.find(args[1]));
            }
            else
            {
                player.sendMessage("Failed to set player's rank!");
            }
        }
        else
        {
            help(player);
            return;
        }
    }

    @Override
    public void help(CommandExecutor executor) {
        executor.sendMessage("/setgroup <player> <group> - sets player group");
    }
    
}
