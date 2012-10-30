/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.plugin.commands;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.ManualLoad;
import net.mcforge.API.plugin.Command;
import net.mcforge.chat.ChatColor;
import net.mcforge.server.Server;
import net.mcforge.world.Generator;
import net.mcforge.world.LevelHandler;
import net.mcforge.world.generator.FlatGrass;
import net.mcforge.world.generator.Island;

@ManualLoad
public class Newlvl extends Command {
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
	public void execute(CommandExecutor player, String[] args) {
		Generator gen = null;
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
			try {
				Class<?> _class = Class.forName("net.mcforge.world.generator." + args[4]);
				if (Generator.class.isAssignableFrom(_class)) {
					Constructor<?> construct = _class.getConstructor(Server.class);
					gen = (Generator)construct.newInstance(player.getServer());
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			if (gen == null)
				gen = new FlatGrass(player.getServer());
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
		executor.sendMessage("Types: ");
		executor.sendMessage("Flat");
		executor.sendMessage("Island");
	}

	private void createLevel(CommandExecutor player, String name, short w, short h, short l, Generator gen) {
		LevelHandler handler = player.getServer().getLevelHandler();
		//handler.loadLevels();
		if (handler.findLevel(name) == null) {
			player.sendMessage(ChatColor.Yellow + "Creating " + ChatColor.White + name + "," + ChatColor.Yellow + " please wait...");
			handler.newLevel(name, w, h, l, gen);
			player.sendMessage(ChatColor.Bright_Green + "Created new level: \"" + name + "\"!");
		}
		else {
			player.sendMessage(ChatColor.Dark_Red + "Level \"" + name + "\" already exists!");
		}
	}
}
