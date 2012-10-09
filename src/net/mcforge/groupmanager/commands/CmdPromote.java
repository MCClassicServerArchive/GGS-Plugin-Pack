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
import net.mcforge.iomodel.Player;

/**
 *
 * @author Wouter Gerarts
 */
public class CmdPromote extends Command implements ManualLoad {

    @Override
    public String[] getShortcuts() {
        return new String[0];
    }

    @Override
    public String getName() {
        return "promote";
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
            if (GroupManagerAPI.PromotePlayer(args[0]))
            {
                player.sendMessage("Promoted '" + Player.find(GroupPlugin.server, args[0]).username + "'");
                Player.find(GroupPlugin.server, args[0]).sendMessage("You have been promoted!");
            }
            else
            {
                player.sendMessage("Couldn't promote '" + args[0] + "'");
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
        executor.sendMessage("/promote <player> - promotes a player");
    }
    
}
