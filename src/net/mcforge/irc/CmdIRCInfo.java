/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.irc;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.plugin.Command;

public class CmdIRCInfo extends Command {

	@Override
	public String getName() {
		return "ircinfo";
	}
	@Override
	public String[] getShortcuts() {
		return new String[0];
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
		IRCBot b = IRCPlugin.getBot();
		executor.sendMessage("IRC server: " + b.server + ":" + b.port);
		executor.sendMessage("IRC channel: " + b.channel);
		executor.sendMessage("IRC bot nick: " + b.userName);
		executor.sendMessage(b.connected ? "&aThe bot is connected to the IRC" : "&cThe bot isn't connected to the IRC");
		executor.sendMessage("IRC controllers: " + IRCUser.IRCControllers);
	}
	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/ircinfo - shows the server's irc info");
	}
}