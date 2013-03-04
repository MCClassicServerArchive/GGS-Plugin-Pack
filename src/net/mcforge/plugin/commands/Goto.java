/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.plugin.commands;

import java.io.File;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.ManualLoad;
import net.mcforge.API.plugin.PlayerCommand;
import net.mcforge.iomodel.Player;
import net.mcforge.API.help.HelpItem;
import net.mcforge.server.Server;
import net.mcforge.world.Level;
import net.mcforge.world.LevelHandler;

@ManualLoad
public class Goto extends PlayerCommand implements HelpItem {
	@Override
	public String[] getShortcuts() {
		return new String[] { "g" };
	}

	@Override
	public String getName() {
		return "goto";
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
	public void execute(Player player, String[] args) {
		if (args.length == 1) {
			Server s = player.getServer();
			LevelHandler handler = s.getClassicLevelHandler();
			Level level = handler.findLevel(args[0]);

			if (level != null) {
				player.changeLevel(level);
				s.sendGlobalMessage(player.getDisplayName() + s.defaultColor + " went to &b" + level.getName());
			}
			else {
				if (player.getServer().loadOnGoto) {
					String[] files = new File("levels").list();
					for (int i = 0; i < files.length; i++) {
						File fi = new File(files[i]);
						if (fi.getName().equalsIgnoreCase(args[0] + ".ggs")) {
							Level found = handler.loadLevel(new File("levels/" + fi.getName()));
							if (found == null) {
								player.sendMessage("Level doesn't exist...");
								return;
							}
							player.changeLevel(found);
							s.sendGlobalMessage(player.getDisplayName() + s.defaultColor + " went to &b" + found.getName());
							return;
						}
					}
				}
				else {
					player.sendMessage("Level doesn't exist or it isn't loaded...");
				}
			}
		}
		else {
			player.sendMessage("Correct format: /goto <level name>");
		}
	}

	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/goto <level name> - sends you to the specified level");
		executor.sendMessage("Shortcut: /g");
		if (executor.getServer().loadOnGoto)
			executor.sendMessage("Server will automatically load the level if it isn't already loaded!");
	}

    @Override
    public String getType() {
        return "other";
    }
}

