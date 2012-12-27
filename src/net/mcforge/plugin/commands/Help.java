/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.plugin.commands;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.ManualLoad;
import net.mcforge.API.plugin.Command;
import net.mcforge.chat.ChatColor;
import net.mcforge.groups.Group;
import net.mcforge.plugin.Main.Main;
import net.mcforge.plugin.help.HelpItem;

@ManualLoad
public class Help extends Command implements HelpItem {
	@Override
	public String[] getShortcuts() {
		return new String[0];
	}

	@Override
	public String getName() {
		return "help";
	}

	@Override
	public boolean isOpCommandDefault() {
		return false;
	}

	@Override
	public int getDefaultPermissionLevel() {
		return 0;
	}

	@Override
	public void execute(CommandExecutor player, String[] args) {
		if (args.length == 0) {
			for (String s : Main.helpmanager.getHelpTypes()) {
			    player.sendMessage("Use &b/help " + s);
			}
			return;
		}
		if (!Main.helpmanager.helpTopicExists(args[0])) {
		    Command cmd = player.getServer().getCommandHandler().find(args[0]);
		    if (cmd == null) {
		        player.sendMessage("The specified command wasn't found!");
		        return;
		    }
		    cmd.help(player);
		}
		else {
		    Command[] cmds = Main.helpmanager.getCommands(args[0], player.getGroup());
		    if (cmds.length == 0) {
		        player.sendMessage("No commands of this type are available to you.");
		        return;
		    }
		    String message = "";
		    for (int i = 0; i < cmds.length; i++) {
		        Command cmd = cmds[i];
		        if (i + 1 < cmds.length)
		            message += getColor(cmd) + cmd.getName() + ", ";
		        else
		            message += getColor(cmd) + cmd.getName();
		    }
		    player.sendMessage(args[0] + " commands you may use:");
		    player.sendMessage(message + ".");
		}
	}

	private ChatColor getColor(Command cmd) {
        for (Group g : Group.getGroupList()) {
            if (g.canExecute(cmd))
                return g.color;
        }
        return ChatColor.Black;
    }

    @Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/help <commandname> - shows help for the specified command");
	}

    @Override
    public String getType() {
        return "information";
    }
}
