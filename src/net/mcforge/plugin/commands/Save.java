package net.mcforge.plugin.commands;

import java.io.IOException;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.ManualLoad;
import net.mcforge.API.plugin.Command;
import net.mcforge.world.Level;

@ManualLoad
public class Save extends Command {

	@Override
	public void execute(CommandExecutor arg0, String[] arg1) {
		if (arg1.length == 0) {
			final int size = arg0.getServer().getLevelHandler().getLevelList().size();
			for (int i = 0; i < size; i++) {
				try {
					arg0.getServer().getLevelHandler().getLevelList().get(i).save();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			arg0.sendMessage("Saved " + size + " " + (size == 1 ? "map" : "maps") + "!");
		}
		else {
			Level l = arg0.getServer().getLevelHandler().findLevel(arg1[0]);
			if (l == null)
				arg0.sendMessage("Level not found!");
			else {
				try {
					l.save();
				} catch (IOException e) {
					e.printStackTrace();
					arg0.sendMessage("There was an error saving " + l.name + "!");
					return;
				}
				arg0.sendMessage(l.name + " saved!");
			}
		}
	}

	@Override
	public int getDefaultPermissionLevel() {
		return 100;
	}

	@Override
	public String getName() {
		return "save";
	}

	@Override
	public String[] getShortcuts() {
		return new String[] { };
	}

	@Override
	public void help(CommandExecutor arg0) {
		arg0.sendMessage("/save - Save all the levels loaded!");
		arg0.sendMessage("/save [level] - Save [level]!");
	}

	@Override
	public boolean isOpCommandDefault() {
		return true;
	}

}

