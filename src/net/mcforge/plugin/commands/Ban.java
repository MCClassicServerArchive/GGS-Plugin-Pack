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
import net.mcforge.iomodel.Player;
import net.mcforge.API.help.HelpItem;
import net.mcforge.banhandler.BanHandler;

@ManualLoad
public class Ban extends Command implements HelpItem {

    @Override
    public String[] getShortcuts() {
        return new String[0];
    }

    @Override
    public String getName() {
        return "ban";
    }

    @Override
    public boolean isOpCommandDefault() {
        return true;
    }

    @Override
    public int getDefaultPermissionLevel() {
        return 100;
    }

    @Override
    public void execute(CommandExecutor player, String[] args) {
        if (args.length != 0) {
            if (BanHandler.banHandler.isBanned(args[0])) {
                player.sendMessage(args[0] + " is already banned");
                return;
            }
            if (!args[0].equalsIgnoreCase(player.getName())) {
                Player who = player.getServer().findPlayer(args[0]);
                if (who != null) {
                    BanHandler.banHandler.ban(who.username);
                    player.getServer().getMessages().serverBroadcast(who.username + " has been banned by " + player.getName());
                }
                else {
                    BanHandler.banHandler.ban(args[0]);
                    player.getServer().getMessages().serverBroadcast(args[0] + " has been banned by " + player.getName());
                }
            }
            else {
                player.sendMessage("You cannot ban yourself.");
            }
        }
        else {
            help(player);
        }
        // TODO Add expire date
    }

    @Override
    public void help(CommandExecutor executor) {
        executor.sendMessage("/ban <player> - bans the specified player!");
    }

    @Override
    public String getType() {
        return "mod";
    }

}

