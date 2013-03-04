package net.mcforge.verification;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.ManualLoad;
import net.mcforge.API.plugin.PlayerCommand;
import net.mcforge.iomodel.Player;

@ManualLoad
public class CmdPass extends PlayerCommand {
	@Override
	public String getName() {
		return "pass";
	}

	@Override
	public String[] getShortcuts() {
		return new String[] { "identify", "password", "verify" };
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
		if (Verifier.isVerified(player)) {
			player.sendMessage("&eYou have already identified yourself!");
			return;
		}
		
		if (args.length < 1) {
			player.sendMessage("You need to specify a password!");
			help(player);
			return;
		}
		
		String password = Verifier.getPassword(player);
		if (password == null) {
			player.sendMessage("&eYou haven't set a verification password!");
			player.sendMessage("&eSet your password using &b/setpass <password>");
			return;
		}
		
		if (Verifier.isCorrect(args[0], password)) {
			player.sendMessage("&aYou are now identified!");
			player.setAttribute("mcf_verified", true);
		}
		else {
			Verifier.setTries(player, Verifier.getTries(player) + 1);
			if (Verifier.getTries(player) >= 3) {
				player.kick("Too many ident tries!");
				return;
			}
			
			player.sendMessage("&cIncorrect password! &eYou have &b" + (3 - Verifier.getTries(player)) + " &etries left!");
			player.sendMessage("&eIf you have forgotten your password, contact the &bserver owner &eto reset it!");
		}
	}

	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/pass <password> - identifies you with the specified password!");
	}
}