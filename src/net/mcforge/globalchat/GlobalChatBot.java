package net.mcforge.globalchat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import net.mcforge.iomodel.Player;
import net.mcforge.server.Server;
import net.mcforge.util.WebUtils;

public class GlobalChatBot implements Runnable {
    protected final IRCHandler ircHandler;
    protected final RankHandler rankHandler;
    protected Server s;

    protected Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    protected final String REALNAME = "MCForge GC Bot";
    protected final String CONSTNAME = "MCFServer";
    protected String username;	
    protected String consoleNick = "Console";

    private String server = "irc.mcforge.net";
    private String channel = "#GlobalChat";
    private String quitMessage = "Server shutting down...";
    protected final int port = 6667;

    protected volatile boolean isRunning;
    protected volatile boolean connected;
    private Thread botThread;

    protected String line;
    protected String[] colonSplit;
    protected String[] spaceSplit;

    protected final static String outgoing = "&6<[Global]";
    protected final static String incoming = "&6>[Global]";


    public GlobalChatBot(Server server, String username, String quitMessage) {
        this.username = username;
        this.quitMessage = quitMessage;
        s = server;
        ircHandler = new IRCHandler(this);
        rankHandler = new RankHandler();
        URL url;
        String gcData;
        try {
            url = new URL("http://server.mcforge.net/gcdata");
            gcData = WebUtils.readContentsToArray(url)[0];
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
        String[] info = gcData.split("&");
        this.server = info[0];
        channel = info[1].toLowerCase(Locale.ENGLISH);
    }

    public void startBot() throws IOException {
        socket = new Socket(server, port);

        if (username.startsWith("ForgeBot")) {
            s.Log("You're currently using the default Global Chat bot nickname!");
            s.Log("Consider changing your bot's nickname in the server properties!");
        }

        botThread = new Thread(this);
        botThread.start();
    }
    @Override
    public void run() {	
        try  {
            isRunning = true;
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            ircHandler.setNick(username);
            ircHandler.sendUserMsg(true);

            while (isRunning) {
                line = reader.readLine();
                if (line == null) {
                    continue;
                }

                colonSplit = line.split(":");
                spaceSplit = line.split(" ");

                if (ircHandler.hasCode("004")) {
                    ircHandler.joinChannel(channel);
                    s.Log("Bot joined the Global Chat!");
                    break;
                }
                else if (ircHandler.hasCode("433")) {
                    s.Log("Nickname already in use! Randomizing..");
                    username = "ForgeBot" + new Random(System.currentTimeMillis()).nextInt(1000000000);
                    ircHandler.setNick(username);
                    s.Log("New Global Chat nickname: " + username);
                }
                else if (line.startsWith("PING ")) {
                    ircHandler.pong(line);
                }
            }
            connected = true;

            while (isRunning) {
                line = reader.readLine();
                if (line == null) {
                    continue;
                }

                colonSplit = line.split(":");
                spaceSplit = line.split(" ");

                if (line.startsWith("PING ")) {
                    ircHandler.pong(line);
                }
                else if (line.toLowerCase(Locale.ENGLISH).contains("privmsg " + channel) && !ircHandler.hasCode("005")) {
                    String message = ircHandler.getMessage();
                    if (message.startsWith("\u0001") && message.endsWith("\u0001")) {
                        continue;
                    }
                    if (message.startsWith("^")) {
                        if (message.startsWith("^UGCS"))
                            GCCPBanService.updateBanList();
                        else if (message.startsWith("^GETINFO ")) {
                            if (message.split(" ")[1].equals(username)) {
                                ircHandler.sendMessage("^Name: " + s.Name);
                                ircHandler.sendMessage("^Description: " + s.description);
                                ircHandler.sendMessage("^MoTD: " + s.MOTD);
                                ircHandler.sendMessage("^Version: MCForge " + Server.CORE_VERSION);
                                ircHandler.sendMessage("^URL: http://minecraft.net/classic/play/" + s.hash);
                                ircHandler.sendMessage("^Players: " + s.getClassicPlayers().size() + "/" + s.MaxPlayers);
                            }
                        }
                        else if (message.startsWith("^SENDRULES ")) {
                            Player who = s.findPlayer(message.split(" ")[1]);

                            if (who != null) {
                                String sender = ircHandler.getSender();
                                if (rankHandler.canSendRules(sender)) {
                                    s.getCommandHandler().execute(who, "gcrules", "");
                                    ircHandler.sendMessage("^" + sender + ": Sent rules to " + who.getName() + "!");
                                }
                                else {
                                    ircHandler.sendMessage("^" + sender + ": You don't have the required permission to send the GC rules!");
                                }
                            }
                        }
                        else if (message.startsWith("^GETIP ") || message.startsWith("^IPGET ")) {
                            List<Player> players = s.getClassicPlayers();
                            for (int i = 0; i < players.size(); i++) {
                                Player p = players.get(i);
                                if (p.username.equals(message.split(" ")[1])) {
                                    ircHandler.sendMessage("^Username: " + p.username + " IP: " + p.getIP());
                                }
                            }
                            players = null;
                        }
                        else if (message.startsWith("^ISONLINE")) {
                            List<Player> players = s.getClassicPlayers();
                            for (int i = 0; i < players.size(); i++) {
                                Player p = players.get(i);
                                if (p.username.equals(message.split(" ")[1])) {
                                    ircHandler.sendMessage("^" + p.username + " is online on " + s.Name);
                                }
                            }
                            players = null;
                        }
                        else if (message.startsWith("^ISSERVER ")) {
                            if (message.split(" ")[1].equals(username))
                                ircHandler.sendMessage("^IMASERVER");
                        }
                        continue;
                    }
                    String toSend = incoming + ircHandler.getSender() + ": &f" +  message;
                    ircHandler.messagePlayers(toSend);
                }
                else if (colonSplit[1].contains("PRIVMSG " + username)) {
                    if (ircHandler.getMessage().equals("\u0001" + "VERSION" + "\u0001")) {
                        ircHandler.sendNotice(ircHandler.getSender(), "\u0001" + "VERSION MCForge " + Server.CORE_VERSION + " : " + System.getProperty("os.name") + "\u0001");
                    }
                }


                else if (ircHandler.hasCode("353")) {
                    rankHandler.clear();
                    String userListRaw = colonSplit[2];
                    String[] userList = userListRaw.split(" ");
                    for (int i = 0; i < userList.length; i++) {
                        String user = userList[i];
                        rankHandler.addUser(IRCRank.parseUser(user), IRCRank.parseRank(user));
                    }
                }

                else if (colonSplit[1].contains(" NICK ")) {
                    String sender = ircHandler.getSender();
                    IRCRank rank = rankHandler.getRank(sender);

                    rankHandler.removeUser(sender);

                    rankHandler.addUser(colonSplit[2], rank);
                }

                else if (colonSplit[1].contains(" PART ") || colonSplit[1].contains(" QUIT ")) {
                    rankHandler.removeUser(ircHandler.getSender());
                }

                else if (colonSplit[1].contains(" MODE ")) {
                    if (spaceSplit.length >= 5) {
                        String user = spaceSplit[4];
                        String rawMode = spaceSplit[3];
                        if (rawMode.equals("+v")) {
                            rankHandler.removeUser(user);
                            rankHandler.addUser(user, IRCRank.Voiced);
                        }
                        else if (rawMode.equals("+h")) {
                            rankHandler.removeUser(user);
                            rankHandler.addUser(user, IRCRank.HalfOp);
                        }
                        else if (rawMode.equals("+o")) {
                            rankHandler.removeUser(user);
                            rankHandler.addUser(user, IRCRank.Op);
                        }
                        else if (rawMode.equals("+ao")) {
                            rankHandler.removeUser(user);
                            rankHandler.addUser(user, IRCRank.Admin);
                        }
                        else if (rawMode.equals("+a")) {
                            rankHandler.removeUser(user);
                            rankHandler.addUser(user, IRCRank.Owner);
                        }
                        else {
                            rankHandler.removeUser(user);
                            rankHandler.addUser(user, IRCRank.Other);
                        }
                    }					
                }


                else if (ircHandler.hasCode("474")) {
                    String providedReason = ircHandler.getMessage();
                    String banReason = providedReason.equals("Cannot join channel (+b)") ? "You're banned" : providedReason;
                    s.Log("You're banned from the Global Chat! Reason: " + banReason);
                    disposeBot();
                    return;
                }
            }
        }
        catch(Exception ex) {
        }
    }
    public String getChannel() {
        return channel;
    }

    public BufferedReader getReader() {
        return reader;
    }
    public BufferedWriter getWriter() {
        return writer;
    }

    public void disposeBot() {
        ircHandler.sendPart(quitMessage);
        ircHandler.sendQuit(quitMessage);

        isRunning = false;
        connected = false;

        try {
            if (reader != null)
                reader.close();
            if (writer != null)
                writer.close();

            if (socket != null)
                socket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
