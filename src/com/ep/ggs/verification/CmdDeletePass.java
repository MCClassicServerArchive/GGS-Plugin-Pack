package com.ep.ggs.verification;

import com.ep.ggs.API.CommandExecutor;
import com.ep.ggs.API.ManualLoad;
import com.ep.ggs.API.plugin.Command;
import com.ep.ggs.iomodel.Player;
import com.ep.ggs.util.Utils;

@ManualLoad
public class CmdDeletePass extends Command {
	@Override
	public String getName() {
		return "deletepass";
	}

	@Override
	public String[] getShortcuts() {
		return new String[] { "deletepassword", "delpass" };
	}

	@Override
	public int getDefaultPermissionLevel() {
		return 120;
	}

	@Override
	public boolean isOpCommandDefault() {
		return true;
	}
	
	@Override
	public void execute(CommandExecutor executor, String[] args) {
		if (args.length == 0) {
			executor.sendMessage("Please specify a player!");
			help(executor);
			return;
		}
		
		if (Verifier.getPassword(args[0]) == null) {
			executor.sendMessage("The specified player doesn't have a password!");
			return;
		}
		
		Verifier.deletePassword(args[0]);
		executor.sendMessage(Utils.getPossessiveForm(args[0]) + " password has been deleted!");
		
		Player who;
		if ((who = executor.getServer().findPlayer(args[0])) != null) {
			who.sendMessage("Your password was deleted by " + executor.getName());
			who.sendMessage("&eSet your password using &b/setpass <password>");
			Verifier.setVerified(who, false);
		}
	}

	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/deletepass <player> - deletes the specified player's password");
	}
}