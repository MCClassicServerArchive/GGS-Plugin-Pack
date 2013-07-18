/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.irc;

import java.io.IOException;
import java.util.Random;

import com.ep.ggs.API.ManualLoad;
import com.ep.ggs.API.player.PlayerChatEvent;
import com.ep.ggs.API.plugin.Plugin;
import com.ep.ggs.server.Server;
import com.ep.ggs.system.Console;
import com.ep.ggs.util.properties.Properties;


@ManualLoad
public class IRCPlugin extends Plugin {
	private CmdIRCInfo cmdircinfo = new CmdIRCInfo();
	private CmdReloadIRCControllers cmdreload = new CmdReloadIRCControllers();
	private EventListener listener = new EventListener();

	private static IRCBot ircBot;
	@Override	
	public String getName() {
		return "MCForge IRC Plugin";
	}
	@Override
	public String getAuthor() {
		return "Arrem";
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
		getServer().getCommandHandler().addCommand(cmdreload);
		getServer().getEventSystem().registerEvents(listener);
		
		Properties p = getServer().getSystemProperties();
	    String name = "ForgeBot" + new Random(System.currentTimeMillis()).nextInt(1000000000);
	    String pass = "";
	    boolean identify = false;
	    boolean visible = true;
	    
	    
	    String serv = "irc.geekshed.net", chan = "#changeme";
	    int port = 6667;
	    
	    if (!p.hasValue("IRC-Enabled") || !p.getBool("IRC-Enabled")) {
	    	if (!p.hasValue("IRC-Enabled")) {
	    		p.addSetting("IRC-Enabled", false);
	    		p.addComment("IRC-Enabled", "Determines if the IRC will be used or not");
	    		saveConfig();
	    	}
	    	kill();
	    	return;
	    }
	    
	    if (!p.hasValue("IRC-Nickname")) {
	    	p.addSetting("IRC-Nickname", name);
	    	p.addComment("IRC-Nickname", "The nickname for the IRC Bot");
	    	saveConfig();
	    }
	    else {
	    	name = p.getValue("IRC-Nickname");
	    }
	    
	    if (!p.hasValue("IRC-Identify")) {
	    	p.addSetting("IRC-Identify", false);
	    	saveConfig();
	    }
	    else {
	    	identify = p.getBool("IRC-Identify");
	    }
	    	
		if (!p.hasValue("IRC-Password")) {
		    p.addSetting("IRC-Password", "");
		    identify = false;
		    saveConfig();
		}
		else {
		    pass = p.getValue("IRC-Password");
		}
	    if (!p.hasValue("IRC-Visible")) {
	    	p.addSetting("IRC-Visible", false);
	    	saveConfig();
	    }
	    else {
	    	visible = p.getBool("IRC-Visible");
	    }
	    
	    if (!p.hasValue("IRC-Server")) {
	    	p.addSetting("IRC-Server", serv);
	    	saveConfig();
	    }
	    else {
	    	serv = p.getValue("IRC-Server");
	    }
	    
	    if (!p.hasValue("IRC-Port")) {
	    	p.addSetting("IRC-Port", port);
	    	saveConfig();
	    }
	    else {
	    	port = p.getInt("IRC-Port");
	    }
	    
	    if (!p.hasValue("IRC-Channel")) {
	    	p.addSetting("IRC-Channel", chan);
	    	saveConfig();
	    }
	    else {
	    	chan = p.getValue("IRC-Channel");
	    }
	    
	    if (chan.equals("#changeme")) {
	    	getConsole().sendMessage("Please configure your IRC plugin! Shutting down...");
	    	return;
	    }
	    if (chan.equalsIgnoreCase("#MCForgeGC")) {
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
			kill();
			return;
		}
	}
	public static IRCBot getBot() {
		return ircBot;
	}
	@Override
	public void onUnload() {
		kill();
	}
	public void kill() {
		PlayerChatEvent.getEventList().unregister(listener);
		getServer().getCommandHandler().removeCommand(cmdircinfo);
		getServer().getCommandHandler().removeCommand(cmdreload);
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

