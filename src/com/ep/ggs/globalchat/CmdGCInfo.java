/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.globalchat;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.ep.ggs.API.CommandExecutor;
import com.ep.ggs.API.plugin.Command;
import com.ep.ggs.iomodel.Player;
import com.ep.ggs.util.WebUtils;


public class CmdGCInfo extends Command {

	@Override
	public String getName() {
		return "globalchatinfo";
	}
	@Override
	public String[] getShortcuts() {
		return new String[] { "gcinfo", "gcinformation" };
	}
	@Override
	public int getDefaultPermissionLevel() {
		return 0;
	}
	@Override
	public boolean isOpCommandDefault() {
		return false;
	}
	@Override
	public void execute(CommandExecutor executor, String[] args) {
		URL u;
		String[] lines;
		
		try {
			u = new URL("http://server.mcforge.net/gcinfo.txt");
			lines = WebUtils.readContentsToArray(u);
		}
		catch (MalformedURLException e) {
			executor.sendMessage("An error occured!");
			e.printStackTrace();
			return;
		}
		catch (IOException e) {
			executor.sendMessage("An error occured!");
			e.printStackTrace();
			return;
		}
		for (int i = 0; i < lines.length; i++) {
			executor.sendMessage(lines[i]);
		}
		if (executor instanceof Player) {
			Player p = (Player)executor;
			if (DataHandler.agreedToRules(p))
				p.sendMessage("&2You have agreed to the Global Chat rules!");
			else
				p.sendMessage("&4You haven't agreed to the Global Chat rules!");
			
			if (DataHandler.ignoringGC(p))
				p.sendMessage("&4You're ignoring the Global Chat!");
		}
	}
	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/gcinfo - displays the information about the Global Chat");
	}
}
