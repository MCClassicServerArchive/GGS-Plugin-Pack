/*******************************************************************************
* Copyright (c) 2012 MCForge.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the GNU Public License v3.0
* which accompanies this distribution, and is available at
* http://www.gnu.org/licenses/gpl.html
******************************************************************************/
package net.mcforge.irc;

import net.mcforge.API.EventHandler;
import net.mcforge.API.Listener;
import net.mcforge.API.player.PlayerChatEvent;
import net.mcforge.API.server.ServerChatEvent;

public class EventListener implements Listener {
	@EventHandler
	public void onPlayerChat(PlayerChatEvent e) {
		IRCPlugin.getBot().handler.sendMessage(e.getPlayer().getName() + ": " + e.getMessage());
	}
	@EventHandler
	public void onServerChat(ServerChatEvent e) {
		IRCPlugin.getBot().handler.sendMessage(e.getConsole().getName() + ": " + e.getMessage());
	}
}

