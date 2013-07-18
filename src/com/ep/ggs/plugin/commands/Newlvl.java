/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.plugin.commands;

import java.util.List;

import com.ep.ggs.API.CommandExecutor;
import com.ep.ggs.API.ManualLoad;
import com.ep.ggs.API.help.HelpItem;
import com.ep.ggs.API.plugin.Command;
import com.ep.ggs.chat.ChatColor;
import com.ep.ggs.world.LevelHandler;
import com.ep.ggs.world.generator.Generator;
import com.ep.ggs.world.generator.classicmodel.ClassicGenerator;
import com.ep.ggs.world.generator.classicmodel.FlatGrass;


@ManualLoad
public class Newlvl extends Command implements HelpItem {
	@Override
	public String[] getShortcuts() {
		return new String[0];
	}

	@Override
	public String getName() {
		return "newlvl";
	}

	@Override
	public boolean isOpCommandDefault() {
		return true;
	}

	@Override
	public int getDefaultPermissionLevel() {
		return 0;
	}
	
	@Override
	public boolean runInSeperateThread() {
	    return true;
	}

	@Override
	public void execute(CommandExecutor player, String[] args) {
		Generator<?> gen = null;
		if (args.length == 1) {
			short sh = 64;
			gen = new FlatGrass(player.getServer());
			createLevel(player, args[0], sh, sh, sh, gen);
			return;
		}
		else if (args.length == 4) {
			gen = new FlatGrass(player.getServer());
		}
		else if (args.length == 5) {
		    gen = player.getServer().getGeneratorHandler().findGenerator(args[4]);
			if (gen == null) {
				gen = new FlatGrass(player.getServer());
				player.sendMessage("The type " + args[4] + " could not be found..");
				help(player);
				return;
			}
			if (!(gen instanceof ClassicGenerator)) {
			    player.sendMessage("The type " + args[4] + " is not a Classic level generator!");
			    help(player);
			    return;
			}
		}
		else {
			help(player);
			return;
		}
		createLevel(player, args[0], Short.valueOf(args[1]), Short.valueOf(args[2]), Short.valueOf(args[3]), gen);
	}

	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/newlvl <name> <w> <h> <l> [type] - creates a new level with the specified dimensions");
		executor.sendMessage("/newlvl <name> - creates a new 64x64x64 level");
		String typeList = "";
		List<Generator<?>> gens = executor.getServer().getGeneratorHandler().getGenerators();
		for (int i = 0; i < gens.size(); i++) {
			if (gens.get(i) instanceof ClassicGenerator) {
				typeList += gens.get(i).getName() + ", ";
			}
		}
		typeList = typeList.substring(0, typeList.length() - 2);
		executor.sendMessage("Types: " + typeList);
	}

	private void createLevel(CommandExecutor player, String name, short w, short h, short l, Generator<?> gen) {
		LevelHandler handler = player.getServer().getClassicLevelHandler();

		if (handler.findLevel(name) == null) {
			player.sendMessage(ChatColor.Yellow + "Creating " + ChatColor.White + name + "," + ChatColor.Yellow + " please wait...");
			handler.generateLevel(name, gen, w, h, l);
			player.sendMessage(ChatColor.Bright_Green + "Created new level: \"" + name + "\"!");
		}
		else {
			player.sendMessage(ChatColor.Dark_Red + "Level \"" + name + "\" already exists!");
		}
	}

    @Override
    public String getType() {
        return "build";
    }
}
