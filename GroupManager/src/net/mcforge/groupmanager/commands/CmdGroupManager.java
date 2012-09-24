/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mcforge.groupmanager.commands;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.plugin.Command;

/**
 *
 * @author Wouter Gerarts
 */
public class CmdGroupManager extends Command {

    @Override
    public String[] getShortcuts() {
        String[] shortcuts = new String[1];
        shortcuts[0] = "gm";
        return shortcuts;
    }

    @Override
    public String getName() {
        return "groupmanager";
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
        
    }

    @Override
    public void help(CommandExecutor executor) {
        
    }
    
}
