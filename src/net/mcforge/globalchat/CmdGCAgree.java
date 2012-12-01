/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.globalchat;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.plugin.PlayerCommand;
import net.mcforge.iomodel.Player;

public class CmdGCAgree extends PlayerCommand {

	@Override
	public String getName() {
		return "globalchatagree";
	}
	@Override
	public String[] getShortcuts() {
		return new String[] { "gcagree", "gca" };
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
	public void execute(Player executor, String[] args) {
		if (DataHandler.agreedToRules(executor)) {
			executor.sendMessage("You have already agreed to the Global Chat rules!");
			return;
		}
		if (!DataHandler.readGCRules(executor)) {
			executor.sendMessage("You must read the Global Chat rules first!");
			executor.sendMessage("By agreeing to the rules you take full consequences of your actions" +
		             " and may be banned without a prior warning for breaking a rule.");
			return;
		}
		executor.sendMessage("By agreeing to the rules you took full consequences of your actions" +
	             " and may be banned without a prior warning for breaking a rule.");
		executor.sendMessage("You can now use the Global Chat!");
		
		DataHandler.setValue(executor, DataHandler.agreed, true, true);
	}
	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/gcagree - agree to the Global Chat rules");
		if (executor instanceof Player) {
			Player p = (Player)executor;
			if (!DataHandler.agreedToRules(p))
				executor.sendMessage("By agreeing to the rules you take full consequences of your actions" +
			             " and may be banned without a prior warning for breaking a rule.");
			if (!DataHandler.readGCRules(p))
				executor.sendMessage("You must read the Global Chat rules first!");
		}
	}
}
