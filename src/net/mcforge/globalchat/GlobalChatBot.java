package net.mcforge.globalchat;

import java.io.IOException;
import java.net.Socket;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

import net.mcforge.iomodel.Player;
import net.mcforge.server.Server;


public class GlobalChatBot implements Runnable {
	private final IRCHandler handler;
	private Server s;

	protected Formatter writer;
	private Scanner reader;
	private Socket socket;

	protected String username;
	protected final String REALNAME = "MCForge GC Bot";
	
	protected final String server = "irc.geekshed.net";
	protected final String channel = "#MCForgeGC";
	protected final int port = 6667;

	protected volatile boolean isRunning;
	protected volatile boolean connected;
	private Thread botThread;
	
	protected final String outgoing = "&6<[Global]";
	private final String incoming = "&6>[Global]";
	
	
	public GlobalChatBot(GlobalChatPlugin plugin, Server server, String username) {
		this.username = username;
		s = server;
		handler = new IRCHandler(this);
	}
	
	public void startBot() throws IOException {
		socket = new Socket(server, port);
		writer = new Formatter(socket.getOutputStream());
		reader = new Scanner(socket.getInputStream());
		handler.setNick(username);
		handler.sendUserMsg(username, true);
		isRunning = true;
		botThread = new Thread(this);
		botThread.start();
	}
	@Override
	public void run() {
		String line;
		while ((line = reader.nextLine()) != null && isRunning) {
			if (handler.hasCode(line, "004")) {
				handler.joinChannel(channel);
				s.Log("Bot joined the Global Chat!");
				break;
			}
			else if (handler.hasCode(line, "433")) {
				s.Log("Nickname already in use! Randomizing..");
				username += new Random(System.currentTimeMillis()).nextInt(1000000000);
				handler.setNick(username);
				s.Log("New Global Chat nickname: " + username);
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
					!handler.hasCode(line, "005")) {
				String message = handler.getMessage(line);
				if (message.startsWith("^")) {
					if (message.startsWith("^UGCS")) {
						//TODO: bodhi will handle this
					}
					else if (message.startsWith("^GETINFO")) {
						handler.sendMessage("^Name: " + s.Name);
						handler.sendMessage("^Description: " + s.description);
						handler.sendMessage("^MoTD: " + s.MOTD);
						handler.sendMessage("^Version: MCForge " + s.VERSION);
						handler.sendMessage("^URL: http://minecraft.net/classic/play/" + s.hashCode());
						handler.sendMessage("Players: " + s.players.size() + "/" + s.MaxPlayers);						
					}
					else if (message.startsWith("^GETIP ") || message.startsWith("^IPGET ")) {
						List<Player> players = s.players;
						for (int i = 0; i < players.size(); i++) {
							Player p = players.get(i);
							if (p.username.equals(message.split(" ")[1])) {
								handler.sendMessage("^Username: " + p.username + " IP: " + p.getIP());
							}
						}
						players = null;
					}
					else if (message.startsWith("^ISONLINE")) {
						List<Player> players = s.players;
						for (int i = 0; i < players.size(); i++) {
							Player p = players.get(i);
							if (p.username.equals(message.split(" ")[1])) {
								handler.sendMessage("^" + p.username + " is online on " + s.Name);
							}
						}
						players = null;
					}
					continue;
				}
				String toSend = incoming + handler.getSender(line) + ": &f" +  message;
				s.sendGlobalMessage(toSend);
				s.Log(toSend);
			}
			else if (handler.hasCode(line, "474")) {
				String providedReason = handler.getMessage(line);
				String banReason = providedReason.equals("Cannot join channel (+b)") ? "You're banned" : providedReason;
				s.Log("You're banned from the Global Chat! Reason: " + banReason);
				disposeBot();
				return;
			}
		}
	}
	public void disposeBot() {
		reader.close();
		writer.close(); 
		try {
			socket.close();
		}
		catch (IOException e) {
		}
		isRunning = false;
		connected = false;
	}
}
