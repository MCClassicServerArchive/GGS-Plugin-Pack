/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.globalchat;

import java.io.IOException;
import java.util.List;

import net.mcforge.iomodel.Player;

/**
* This class handles communication with the IRC, and implements the needed
* features of it's protocol for the bot to use.
*/
public class IRCHandler {
	private GlobalChatBot bot;
	
	public IRCHandler(GlobalChatBot bot) {
		this.bot = bot;
	}
	
	/**
	 * Sends a message to all the players that aren't ignoring the Global Chat
	 */
	public void messagePlayers(String message) {
		List<Player> players = bot.s.players;
		for (int i = 0; i < players.size(); i++) {
			if (!DataHandler.ignoringGC(players.get(i))) {
				players.get(i).sendMessage(message);
			}
			else
				players.get(i).sendMessage("You are a faggot!");
		}
	}
	
	/**
	 * Sends a raw message to the IRC.
	 * Note that this isn't a method to send messages to the channel
	 * @param message - the message to send
	 */
	protected void sendRaw(String message) {
		try {
			bot.writer.out().append(message + "\r\n");
		}
		catch (IOException e) {
		}
		bot.writer.flush();
	}

	/**
	 * Sets or changes the bot's nickname to the specified nickname.
	 * @param nick - The nickname to change to
	 */
	public void setNick(String nick) {
		sendRaw("NICK " + nick);
	}
	
	/**
	 * Sends the user message to the IRC
	 * @param nick - The nickname to use
	 * @param visible - Whether the bot will be marked as visible
	 */
	public void sendUserMsg(String nick, boolean visible) {
		sendRaw(String.format("USER %s %d * :%s", nick, visible ? 0 : 8, bot.REALNAME));
	}

	/**
	 * Joins the specified IRC channel
	 * @param channel - The channel to join
	 */
	public void joinChannel(String channel) {
		sendRaw("JOIN " + channel);
	}

	/**
	 * Identifies to the IRC with the specified nickname and password
	 * @param nick - The nickname to use
	 * @param password - The password to use
	 */
	public void identify(String password) {
		sendRaw("NS IDENTIFY " + password);
	}
	
	/**
	 * Responds to the server's pings.
	 * @param reply - The ping reply that was sent
	 */
	public void pong(String reply) {
		sendRaw("PONG " + reply.toLowerCase().substring(5));
	}

	/**
	 * Sends a message to the IRC channel the bot is connected to
	 * @param message - The message to send
	 */
	public void sendMessage(String message) {
		if (bot.connected)
			sendRaw("PRIVMSG " + bot.getChannel() + " :" + message);
	}
	
	/**
	 * Sends a private message to the specified user
	 * @param user - The user to send to
	 * @param message - The message to send
	 */
	public void privateMessage(String user, String message) {
		if (bot.connected)
			sendRaw("PRIVMSG " + user + " :" + message);
	}
	
	/**
	 * Makes the bot quit from the network
	 * 
	 * @param quitMessage - The quit message to send
	 */
	public void sendQuit(String quitMessage) {
		sendRaw("QUIT :" + quitMessage);
	}
	
	/**
	 * Makes the bot leave the channel
	 * 
	 * @param partMessage - The part message to send
	 */
	public void sendPart(String partMessage) {
		sendRaw("PART " + bot.getChannel() + " :" + partMessage);
	}
	
	/**
	 * Gets the user who said the specified line
	 * @param line - The line to check
	 */
	public String getSender(String line)
	{
		return line.split("!")[0].split(":")[1];
	}
	/**
	 * Gets the message of the specified line
	 * @param line - The line to check
	 */
	public String getMessage(String line) {
		String tmp = "";
		int loop = 0;
		if (line.split(":").length >= 2) {
			for (String str: line.split(":")) {
				if (str.contains("PRIVMSG")) loop = 1;
				else if (loop == 2) tmp = str;
				else if (loop > 2) tmp = tmp + ":" + str;
				loop++;
			}
			return tmp;
		}
		return "";
	}
	/**
	 * Checks if the specified line has the specified reply code
	 * @param line - The line to check
	 * @param code - The code to look for
	 * 
	 * @return A boolean indicating whether the specified line
	 * contains the specified reply code 
	 */
	public boolean hasCode(String line, String code) {
		return line.split(" ")[1].equals(code);
	}
}

