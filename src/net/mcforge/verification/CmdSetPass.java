package net.mcforge.verification;

import java.io.IOException;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.ManualLoad;
import net.mcforge.API.plugin.PlayerCommand;
import net.mcforge.iomodel.Player;
import net.mcforge.util.FileUtils;

@ManualLoad
public class CmdSetPass extends PlayerCommand {
	@Override
	public String getName() {
		return "setpass";
	}

	@Override
	public String[] getShortcuts() {
		return new String[] { "setpassword" };
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
		if (args.length == 0) {
			player.sendMessage("Please specify a parameter!");
			help(player);
			return;
		}
		
		if (args.length == 1) {
			if (Verifier.getPassword(player) != null) {
				player.sendMessage("You have already set a password!");
				player.sendMessage("&eUse &b/setpass <current password> <new password> &eto change your password!");
				return;
			}
			try {
				FileUtils.writeText("properties/passwords.config", player.username + ":" + Verifier.getHash(args[0]));
			}
			catch (IOException e) {
				player.sendMessage("An error occured...");
				e.printStackTrace();
				return;
			}
			
			player.sendMessage("Your password has been set to: &b" + args[0]);
			player.sendMessage("&eYou can now use &b/pass <password> &e to identify yourself!");
		}
		else if (args.length == 2) {
			String password = Verifier.getPassword(player);
			if (password == null) {
				player.sendMessage("Please set a password before changing it!");
				player.sendMessage("&eUse &b/setpass <password> &eto set your password!");
				return;
			}
			
			if (Verifier.isCorrect(args[0], password)) {
				Verifier.changePassword(player, password, Verifier.getHash(args[1]));
				Verifier.setVerified(player, false);
				player.sendMessage("Your password has been set to: &b" + args[1]);
				player.sendMessage("&eYou can now use &b/pass <password> &e to identify yourself!");
			}
		}
		else {
			player.sendMessage("Please specify valid parameters!");
			help(player);
		}
	}

	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/setpass <password> - sets your identification password");
		executor.sendMessage("/setpass <current password> <new password> - changes your password");
	}
}