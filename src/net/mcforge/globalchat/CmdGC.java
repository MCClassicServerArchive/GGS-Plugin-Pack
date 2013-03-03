/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.globalchat;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.plugin.Command;
import net.mcforge.chat.Messages;
import net.mcforge.globalchat.GCCPBanService.GCCPBan;
import net.mcforge.iomodel.Player;
import net.mcforge.system.Console;

public class CmdGC extends Command {

	@Override
	public String getName() {
		return "globalchat";
	}
	@Override
	public String[] getShortcuts() {
		return new String[] { "gc" };
	}
	@Override
	public int getDefaultPermissionLevel() {
		return 0;
	}
	@Override
	public boolean isOpCommandDefault() {
		return false;
	}
	@Override
	public void execute(CommandExecutor executor, String[] args) {
		if (executor instanceof Player) {
			Player p = (Player)executor;
			if (GCCPBanService.isPlayerBanned(p)) {
			    GCCPBan ban = GCCPBanService.getPlayerBan(p);
			    p.sendMessage("You are &cBANNED" + p.getServer().defaultColor + " from Global Chat by '" + ban.getBannedBy() + "' because: &d'" + ban.getBanReason() + "'");
			    return;
			}
			if (DataHandler.ignoringGC(p)) {
				executor.sendMessage("You can't use the Global Chat while you're ignoring it");
				executor.sendMessage("If you want to unignore the Global Chat, type /gcignore");
				return;
			}
			if (!DataHandler.agreedToRules(p)) {				
				executor.sendMessage("You need to read and agree to the Global Chat rules first! Use &9/gcrules");
				return;
			}
		}
		if (args.length == 0) {
			executor.sendMessage("You have to specify a message!");
			return;
		}
		
		if (args[0].equalsIgnoreCase("ignore")) {
			executor.sendMessage("&cThe command to ignore the Global Chat is &b/gcignore");
		}
		else if (args[0].equalsIgnoreCase("info")) {
			executor.sendMessage("&cThe command to view the Global Chat info is &b/gcinfo");
		}
		else if (args[0].equalsIgnoreCase("rules")) {
			executor.sendMessage("&cThe command to view the Global Chat rules is &b/gcrules");
		}
		else if (args[0].equalsIgnoreCase("agree")) {
			executor.sendMessage("&cThe command to agree to the Global Chat rules is &b/gcagree");
		}
		
		String joined = Messages.join(args);
		if (executor instanceof Console) {
			String nick = GlobalChatPlugin.gcBot.consoleNick;
			nick = nick.equalsIgnoreCase("Console") ? nick : "<C>" + nick;
			GlobalChatPlugin.gcBot.handler.sendMessage(nick + ": " + joined);
			GlobalChatPlugin.gcBot.handler.messagePlayers(GlobalChatBot.outgoing + nick + ":&f " + joined);
		}
		else {
			GlobalChatPlugin.gcBot.handler.sendMessage(executor.getName() + ": " + joined);
			GlobalChatPlugin.gcBot.handler.messagePlayers(GlobalChatBot.outgoing + executor.getName() + ":&f " + joined);
		}
	}
	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/gc <message> - use the global chat");
	}
}
