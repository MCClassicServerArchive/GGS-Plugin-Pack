/*******************************************************************************
* Copyright (c) 2012 MCForge.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the GNU Public License v3.0
* which accompanies this distribution, and is available at
* http://www.gnu.org/licenses/gpl.html
******************************************************************************/
package net.mcforge.irc;

import java.io.IOException;
import java.util.Random;

import net.mcforge.API.ManualLoad;
import net.mcforge.API.player.PlayerChatEvent;
import net.mcforge.API.plugin.Plugin;
import net.mcforge.server.Server;
import net.mcforge.system.Console;
import net.mcforge.util.properties.Properties;

@ManualLoad
public class IRCPlugin extends Plugin {
	private CmdIRCInfo cmdircinfo = new CmdIRCInfo();
	private EventListener listener = new EventListener();

	private static IRCBot ircBot;
	@Override	
	public String getName() {
		return "MCForge IRC Plugin";
	}
	@Override
	public String getVersion() {
		return "1.0.0";
	}
	public IRCPlugin(Server server) {
		super(server);
	}

	@Override
	public void onLoad(String[] args) {
		getServer().getCommandHandler().addCommand(cmdircinfo);
		getServer().getEventSystem().registerEvents(listener);
		
		Properties p = getServer().getSystemProperties();
	    String name = "ForgeBot" + new Random(System.currentTimeMillis()).nextInt(1000000000);
	    String pass = "";
	    boolean identify = false;
	    boolean visible = true;
	    
	    
	    String serv = "irc.geekshed.net", chan = "#changeme";
	    int port = 6667;
	    
	    if (!p.hasValue("IRC-Enable") || !p.getBool("IRC-Enable")) {
	    	if (!p.hasValue("IRC-Enable")) {
	    		p.addSetting("IRC-Enable", false);
	    		saveConfig();
	    	}
	    	return;
	    }
	    
	    if (!p.hasValue("IRC-nickname")) {
	    	System.out.println("NICKPRINT: " + p.getValue("IRC-nickname"));
	    	p.addSetting("IRC-nickname", name);
	    	saveConfig();
	    }
	    else {
	    	name = p.getValue("IRC-nickname");
	    }
	    
	    if (!p.hasValue("IRC-identify")) {
	    	p.addSetting("IRC-identify", false);
	    	saveConfig();
	    }
	    else {
	    	identify = p.getBool("IRC-identify");
	    }
	    	
		if (!p.hasValue("IRC-password")) {
		    p.addSetting("IRC-password", "");
		    identify = false;
		    saveConfig();
		}
		else {
		    pass = p.getValue("IRC-password");
		}
	    if (!p.hasValue("IRC-visible")) {
	    	p.addSetting("IRC-visible", false);
	    	saveConfig();
	    }
	    else {
	    	visible = p.getBool("IRC-visible");
	    }
	    
	    if (!p.hasValue("IRC-server")) {
	    	p.addSetting("IRC-server", serv);
	    	saveConfig();
	    }
	    else {
	    	serv = p.getValue("IRC-server");
	    }
	    
	    if (!p.hasValue("IRC-port")) {
	    	p.addSetting("IRC-port", port);
	    	saveConfig();
	    }
	    else {
	    	port = p.getInt("IRC-port");
	    }
	    
	    if (!p.hasValue("IRC-channel")) {
	    	p.addSetting("IRC-channel", chan);
	    	saveConfig();
	    }
	    else {
	    	chan = p.getValue("IRC-channel");
	    }
	    
	    if (chan.equals("#changeme")) {
	    	getConsole().sendMessage("Please configure your IRC plugin! Shutting down... k k k?");
	    	getConsole().sendMessage("Chan=" + chan);
	    	return;
	    }
	    if (chan.equalsIgnoreCase("#MCForge")) {
	    	getConsole().sendMessage("Please configure your IRC plugin! You can't use the global chat channel as a server channel! Shutting down...");
	    	return;
	    }
		ircBot = new IRCBot(this, name, pass, identify, visible);
		ircBot.setConnectionDetails(serv, port, chan);
		try {
			ircBot.startBot();
		}
		catch (IOException e) {
			getConsole().sendMessage("Error while starting IRC Bot! IRC disabled!");
			e.printStackTrace();
			kill(false);
			return;
		}
	}
	public static IRCBot getBot() {
		return ircBot;
	}
	@Override
	public void onUnload() {
		kill(true);
	}
	public void kill(boolean unload) {
		if (unload)
			getServer().getPluginHandler().unload(this);
		PlayerChatEvent.getEventList().unregister(listener);
		if (ircBot != null)
			ircBot.disposeBot();
	}
	private void saveConfig() {
		try {
			getServer().getSystemProperties().save("system.config");		
		}
		catch (IOException ex) {
		}
	}
	protected Console getConsole() {
		return getServer().getConsole();
	}
}

