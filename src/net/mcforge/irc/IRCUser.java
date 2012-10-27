/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.irc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.plugin.Command;
import net.mcforge.groups.Group;
import net.mcforge.server.Server;
import net.mcforge.util.FileUtils;

/**
 * This class mostly handles the IRC commands.
 */
public class IRCUser implements CommandExecutor {
	private static List<String> IRCControllers = new ArrayList<String>();
	private String nickname;
	private IRCBot bot;
	public IRCUser(String nickname, IRCBot bot) {
		this.nickname = nickname;
		this.bot = bot;
	}

	@Override
	public Group getGroup() {
		return Group.getGroupList().get(Group.getGroupList().size() - 1);
	}
	@Override
	public String getName() {
		return nickname;
	}
	@Override
	public Server getServer() {
		return IRCPlugin.getBot().getPlugin().getServer();
	}
	@Override
	public void sendMessage(String message) {
		bot.handler.privateMessage(nickname, message);
	}

	/**
	 * Makes the IRC user execute a command
	 * @param rawLine - The raw cmd line
	 */
	public void executeCommand(String rawLine) {
		String message = rawLine.split(":")[2];
		String cmdName = message.split(" ")[0];
		Command cmd = bot.getPlugin().getServer().getCommandHandler().find(cmdName);
		if (cmd == null) {
			sendMessage("Command not found!");
			return;
		}
		if (!IRCControllers.contains(nickname)) {
			sendMessage("You aren't an IRC Controller!");
			return;
		}
		String cmdArgs = message.substring(cmdName.length()).trim();
		cmd.execute(this, cmdArgs.split(" "));
	}

	/**
	 * Loads the IRC controller list
	 */
	public static void loadIRCControllers() {
		IRCControllers.clear();
		try {
			IRCControllers = FileUtils.readToList(FileUtils.IRCCONTROLLERS_FILE);
		}
		catch (IOException e) {
		} 
	}
}
