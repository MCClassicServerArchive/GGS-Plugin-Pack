package net.mcforge.command;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.ManualLoad;
import net.mcforge.API.plugin.PlayerCommand;
import net.mcforge.iomodel.Player;

@ManualLoad
public class ExampleCommand extends PlayerCommand {

	@Override
	public void execute(Player player, String[] arg1) {
		player.sendMessage("HI!");
	}

	@Override
	public int getDefaultPermissionLevel() {
		return 0;
	}

	@Override
	public String getName() {
		return "test";
	}

	@Override
	public String[] getShortcuts() {
		return new String[] { "test1" };
	}

	@Override
	public void help(CommandExecutor sender) {
		sender.sendMessage("I say Hi!");
	}

	@Override
	public boolean isOpCommand() {
		return false;
	}

}
