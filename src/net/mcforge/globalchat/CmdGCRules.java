/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.globalchat;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.plugin.Command;
import net.mcforge.iomodel.Player;
import net.mcforge.util.WebUtils;

public class CmdGCRules extends Command {

	@Override
	public String getName() {
		return "globalchatrules";
	}
	@Override
	public String[] getShortcuts() {
		return new String[] { "gcrules", "gcr" };
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
			u = new URL("http://server.mcforge.net/gcrules.txt");
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
			if (!DataHandler.agreedToRules(p)) {
				executor.sendMessage("You can use /gcagree to agree to the Global Chat rules!");
				executor.sendMessage("By agreeing to the rules you take full consequences of your actions" +
						             " and may be banned without a prior warning for breaking a rule.");
			} 
			
			DataHandler.setValue(p, DataHandler.readRules, true, true);
		}
	}
	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/gcrules - read the Global Chat rules");
	}
}
