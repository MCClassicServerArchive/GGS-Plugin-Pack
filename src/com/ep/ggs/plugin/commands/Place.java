/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.plugin.commands;

import com.ep.ggs.API.CommandExecutor;
import com.ep.ggs.API.ManualLoad;
import com.ep.ggs.API.help.HelpItem;
import com.ep.ggs.API.plugin.PlayerCommand;
import com.ep.ggs.iomodel.Player;
import com.ep.ggs.world.Level;
import com.ep.ggs.world.blocks.classicmodel.ClassicBlock;

@ManualLoad
public class Place extends PlayerCommand implements HelpItem {
	@Override
	public String[] getShortcuts() {
		return new String[] { "pl" };
	}

	@Override
	public String getName() {
		return "place";
	}

	@Override
	public boolean isOpCommandDefault() {
		return false;
	}

	@Override
	public int getDefaultPermissionLevel() {
		return 50;
	}

	@Override
	public void execute(Player executor, String[] args) {
	    ClassicBlock b = ClassicBlock.getBlock("stone");
		short x = (short) (executor.getX() / 32), 
			  y = (short) ((executor.getY() / 32) - 1), 
			  z = (short) (executor.getZ() / 32);

		switch (args.length) {
		case 0:
			break;
		case 1:
			b = ClassicBlock.getBlock(args[0]);
			if (b.name.equals("NULL")) {
				executor.sendMessage("Invalid block type!");
				return;
			}
			System.out.println(b.name);
			break;
		case 3:
			try {
				x = Short.parseShort(args[0]);
				y = Short.parseShort(args[1]);
				z = Short.parseShort(args[2]);
			} catch (NumberFormatException ex) {
				executor.sendMessage("Please specify valid numbers!");
				help(executor);
				return;
			}
			break;
		case 4:
			try {
				x = Short.parseShort(args[0]);
				y = Short.parseShort(args[1]);
				z = Short.parseShort(args[2]);
			} catch (NumberFormatException ex) {
				executor.sendMessage("Please specify valid numbers!");
				help(executor);
				return;
			}

			b = ClassicBlock.getBlock(args[3]);
			if (b.name.equals("NULL")) {
				executor.sendMessage("Invalid block type!");
				return;
			}
			break;
		default:
			executor.sendMessage("Invalid arguments!");
			help(executor);
			return;
		}
		//TODO: check for permissions
		Level level = executor.getLevel();
		if (y >= level.getHeight())
			y = (short)(level.getHeight() - 1);
		
		Player.GlobalBlockChange(x, y, z, b, level, executor.getServer());
	}

	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/place - places a stone block at your location");
		executor.sendMessage("/place <block> - places the specified block at your location");
		executor.sendMessage("/place <x> <y> <z> - places a stone block at the specified location");
		executor.sendMessage("/place <x> <y> <z> <block> - places the specified block at the specified location");
	}

    @Override
    public String getType() {
        return "build";
    }
}
