/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.plugin.commands;

import java.util.ArrayList;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.EventHandler;
import net.mcforge.API.Listener;
import net.mcforge.API.player.PlayerChatEvent;
import net.mcforge.API.plugin.Command;
import net.mcforge.chat.ChatColor;
import net.mcforge.iomodel.Player;
import net.mcforge.server.Server;

public class Mute extends Command implements Listener {

	boolean init;
	ArrayList<Player> muted = new ArrayList<Player>();
	@Override
	public void execute(CommandExecutor arg0, String[] arg1) {
		init(arg0.getServer());
		
		if (arg1.length == 0) { help(arg0); return; }
		
		Player who = arg0.getServer().findPlayer(arg1[0]);
		
		if (who == null) {
			arg0.sendMessage("The player entered does not exist!");
			return;
		}
		if (who == arg0) {
			if (muted.contains(arg0)) {
				muted.remove(arg0);
				arg0.sendMessage("You &bun-muted" + ChatColor.White + " yourself!");
			}
			else
				arg0.sendMessage("You cannot mute yourself!");
			return;
		}
		
		if (muted.contains(who)) {
			muted.remove(who);
			who.getChat().serverBroadcast(who.getDisplayName() + ChatColor.White + " has been &un-muted");
		}
		else {
			if (arg0 instanceof Player) {
				Player p = (Player)arg0;
				if (who != p && who.getGroup().permissionlevel >= p.getGroup().permissionlevel) {
					p.sendMessage("Cannot mute someone of a higher or equal rank.");
					return;
				}
			}
			muted.add(who);
			who.getChat().serverBroadcast(who.getDisplayName() + ChatColor.White + " has been &8muted");
		}
	}
	
	public void init(Server s) {
		if (!init) {
			s.getEventSystem().registerEvents(this);
			init = true;
		}
	}

	@Override
	public int getDefaultPermissionLevel() {
		return 100;
	}

	@Override
	public String getName() {
		return "mute";
	}

	@Override
	public String[] getShortcuts() {
		return new String[0];
	}

	@Override
	public void help(CommandExecutor arg0) {
		arg0.sendMessage("/mute <player> - Mutes or unmutes the player.");
	}

	@Override
	public boolean isOpCommandDefault() {
		return true;
	}
	
	@EventHandler
	public void onChat(PlayerChatEvent event) {
		if (muted.contains(event.getPlayer())) {
			event.getPlayer().sendMessage(ChatColor.Dark_Red + "You are muted!");
			event.setCancel(true);
		}
	}

}
