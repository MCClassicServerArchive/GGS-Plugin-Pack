/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.irc;

import com.ep.ggs.API.EventHandler;
import com.ep.ggs.API.Listener;
import com.ep.ggs.API.player.PlayerChatEvent;
import com.ep.ggs.API.server.ServerChatEvent;
import com.ep.ggs.plugin.commands.Mute;


public class EventListener implements Listener {
	@EventHandler
	public void onPlayerChat(PlayerChatEvent e) {
		if (!Mute.muted.contains(e.getPlayer()) && IRCPlugin.getBot() != null)
			IRCPlugin.getBot().handler.sendMessage(e.getPlayer().getName() + ": " + e.getMessage());
	}
	@EventHandler
	public void onServerChat(ServerChatEvent e) {
		if (IRCPlugin.getBot() != null)
			IRCPlugin.getBot().handler.sendMessage(e.getConsole().getName() + ": " + e.getMessage());
	}
}

