package com.ep.ggs.verification;

import com.ep.ggs.API.CommandExecutor;
import com.ep.ggs.API.ManualLoad;
import com.ep.ggs.API.plugin.PlayerCommand;
import com.ep.ggs.iomodel.Player;

@ManualLoad
public class CmdVerification extends PlayerCommand {
	@Override
	public String getName() {
		return "verification";
	}

	@Override
	public String[] getShortcuts() {
		return new String[0];
	}

	@Override
	public int getDefaultPermissionLevel() {
		return AdminVerification.identificationPerm;
	}

	@Override
	public boolean isOpCommandDefault() {
		return true;
	}
	
	@Override
	public void execute(Player player, String[] args) {
		if (args.length < 1) {
			player.sendMessage("Please specify a parameter!");
			help(player);
			return;
		}

		if (args[0].equalsIgnoreCase("on")) {
			if (Verifier.verifies(player)) {
				player.sendMessage("&eYou are already verifying!");
				return;
			}
			
			Verifier.setVerifies(player, true);
			player.sendMessage("&eYou now have to verify!");

		}
		else if (args[0].equalsIgnoreCase("off")) {
			if (!Verifier.verifies(player)) {
				player.sendMessage("&eYou have already turned verification off!");
				return;
			}
			
			Verifier.setVerifies(player, false);
			player.sendMessage("&eYou no longer have to verify!");
			player.sendMessage("&cPlease note that this is a major security risk to your server!");
		}
		else if (args[0].equalsIgnoreCase("status")) {
			if (Verifier.verifies(player)) {
				player.sendMessage("&aYou have to verify!");
			}
			else { 
				player.sendMessage("&cYou don't have to verify!");
				player.sendMessage("&cPlease note that this is a major security risk to your server!");
			}
		}
		else {
			player.sendMessage("Please specify a valid parameter!");
			help(player);
			return;
		}
	}

	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/verification on/off - whether you'll be asked to identify on login");
		executor.sendMessage("/verification status - shows your verification status");
	}
}
