/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.irc;

import java.io.IOException;
import java.net.Socket;
import java.util.Formatter;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

import net.mcforge.chat.ChatColor;

/**
* This is the main class for the IRC bot.
* Every needed IRC feature is handled in this class, using helper methods from {@link IRCHandler}
*/
public class IRCBot implements Runnable { // TODO: add opchat chan support
	protected IRCHandler handler;
	private IRCPlugin plugin;

	private Scanner reader;
	protected Formatter writer;
	private Socket socket;

	protected String userName;
	private String password;
	protected final String REALNAME = "MCForge Server Bot";
	private boolean identify;
	private boolean visible;

	protected String server;
	protected String channel;
	protected int port;

	private Thread botThread;
	protected volatile boolean isRunning;
	protected volatile boolean connected;

	public IRCBot(IRCPlugin plugin, String userName, String password, boolean identify, boolean visible) {
		this.userName = userName;
		this.password = password;
		this.identify = identify;
		this.visible = visible;
		this.plugin = plugin;
		handler = new IRCHandler(this);
	}
	protected IRCPlugin getPlugin() {
		return plugin;
	}
	
	/**
	 * Sets the bot's connection details
	 * @param server - The server to connect to
	 * @param port - The port of the specified server
	 * @param channel - The channel to connect to
	 */
	public void setConnectionDetails(String server, int port, String channel) {
		this.server = server;
		this.channel = channel;
		this.port = port;
	}

	/**
	 * Initializes the IRC bot and starts it,
	 * connecting to the server and joining the channel.
	 * Use the {@link #setConnectionDetails} method before this to set the bot's connection details!
	 * @throws IOException If there's an error initializing the bot
	 */
	public void startBot() throws IOException {
		socket = new Socket(server, port);
		writer = new Formatter(socket.getOutputStream());
		reader = new Scanner(socket.getInputStream());
		handler.setNick(userName);
		handler.sendUserMsg(userName, visible);
		IRCUser.loadIRCControllers();
		isRunning = true;
		botThread = new Thread(this);
		botThread.start();
	}

	@Override
	public void run() {
		String line;
		while ((line = reader.nextLine()) != null && isRunning) {
			if (line.split(" ")[1].equals("004")) {
				handler.joinChannel(channel);
				plugin.getConsole().sendMessage("Bot joined the channel " + channel);
				if (identify)
					handler.identify(password);
				break;
			}
			else if (line.split(" ")[1].equals("433")) {
				plugin.getConsole().sendMessage("Your nickname("+ userName + ") is already in use! Assigning a random nickname...");
				userName = "Forge" + new Random(System.currentTimeMillis()).nextInt(1000000000);
				plugin.getConsole().sendMessage("New IRC nickname: " + userName);
				handler.setNick(userName);
			}
			else if (line.startsWith("PING ")) {
				handler.pong(line);
			}
		}
		connected = true;

		while ((line = reader.nextLine()) != null && isRunning) { 
			if (line.startsWith("PING ")) {
				handler.pong(line);
			}
			else if (line.toLowerCase(Locale.ENGLISH).contains("privmsg " + channel.toLowerCase(Locale.ENGLISH)) && 
					!line.split(" ")[1].equals("005")) {
				String toSend = ChatColor.Purple + "[IRC]" +  handler.getSender(line) + ": " +  handler.getMessage(line);
				plugin.getServer().getMessages().serverBroadcast(toSend);
				getPlugin().getServer().Log("[IRC]" +  handler.getSender(line) + ": " +  handler.getMessage(line));
			}
			else if (line.contains("PRIVMSG " + userName)) {
				IRCUser u = new IRCUser(handler.getSender(line), this);
				u.executeCommand(line);
			}
			else if (line.split(" ")[1].equals("474")) {
				String providedReason = handler.getMessage(line);
				String banReason = providedReason.equals("Cannot join channel (+b)") ? "You're banned" : providedReason;
				plugin.getConsole().sendMessage("You're banned from the channel " + channel + ". Reason: " + banReason);
				plugin.getConsole().sendMessage("IRC shutting down...");
				disposeBot();
				return;
			}
			else if (line.split(" ")[1].equals("486")) {
				plugin.getConsole().sendMessage("A user tried to run an IRC command, but your bot is unable to private message!");
				plugin.getConsole().sendMessage("Please register and identify your bot for IRC commands to function properly!");
			}
		}
	}

	public void disposeBot() {
	    try {
	        handler.sendPart("");
	        handler.sendQuit("");
	        if (reader != null)
	            reader.close();
	        if (writer != null)
	            writer.close();
	        if (socket != null)
	            socket.close();
	        isRunning = false;
	        connected = false;
	    }
	    catch(Exception ex) {
	    }
	}
}
