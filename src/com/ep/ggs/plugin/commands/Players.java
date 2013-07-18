/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.plugin.commands;

import java.util.ArrayList;
import java.util.List;

import com.ep.ggs.API.CommandExecutor;
import com.ep.ggs.API.ManualLoad;
import com.ep.ggs.API.help.HelpItem;
import com.ep.ggs.API.plugin.Command;
import com.ep.ggs.groups.Group;
import com.ep.ggs.iomodel.Player;
import com.ep.ggs.server.Server;


@ManualLoad
public class Players extends Command implements HelpItem {
	@Override
	public String[] getShortcuts() {
		return new String[] { "online", "who" };
	}

	@Override
	public String getName() {
		return "players";
	}

	@Override
	public boolean isOpCommandDefault() {
		return false;
	}

	@Override
	public int getDefaultPermissionLevel() {
		return 0;
	}

	@Override
	public void execute(CommandExecutor player, String[] args) {
		if (player.getServer().getClassicPlayers().size() == 0) {
			player.sendMessage("There are no online players!");
			return;
		}
		
		List<String> devs = player.getServer().getPrivilegesHandler().getDevs();
		List<Player> online = getOnlineStaff(player.getServer(), devs);
		if (online.size() != 0) {
			player.sendMessage("&9Developers&e: " + joinPlayerList(online, "&f,&e ", false));
		}
		
		online.clear();
		List<String> mods = player.getServer().getPrivilegesHandler().getMods();
		online = getOnlineStaff(player.getServer(), mods);
		if (online.size() != 0) {
			player.sendMessage("&9Moderators&e: " + joinPlayerList(online, "&f,&e ", false));
		}
		
		online.clear();
		List<String> gc = player.getServer().getPrivilegesHandler().getGCStaff();
		online = getOnlineStaff(player.getServer(), gc);
		if (online.size() != 0) {
			player.sendMessage("&9GC Staff&e: " + joinPlayerList(online, "&f,&e ", false));
		}
		
		List<Group> groups = Group.getGroupList();
		for (int i = 0; i < groups.size(); i++) {
			Group group = groups.get(i);
			List<Player> players = group.getOnlinePlayers(player.getServer());
			player.sendMessage(group.color + group.name + "&f: " + joinPlayerList(players, "&f, ", true));			
		}
	}
	private List<Player> getOnlineStaff(Server s, List<String> rawList) {
		List<Player> online = new ArrayList<Player>();
		for (int i = 0; i < rawList.size(); i++) {
			Player p = s.findPlayer(rawList.get(i));
			if (p != null) {
				online.add(p);
			}
		}
		return online;
	}
	private String joinPlayerList(List<Player> list, String separator, boolean color) {
		Player[] array = list.toArray(new Player[list.size()]);
		if (array.length == 0) {
			return "";
		}
		String ret = "";
		
		for (int i = 0; i < array.length; i++) {
			if (color) {
				ret += array[i].getDisplayColor();
			}
			ret += array[i].username + separator;
		}
		
		return ret.substring(0, ret.length() - separator.length());
	}
	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/players - shows the list of online players");
		executor.sendMessage("Shortcuts: /online, /who");
	}

    @Override
    public String getType() {
        return "information";
    }
}
