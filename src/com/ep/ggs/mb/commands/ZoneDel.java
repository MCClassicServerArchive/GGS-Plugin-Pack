/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.mb.commands;

import com.ep.ggs.API.CommandExecutor;
import com.ep.ggs.API.action.Action;
import com.ep.ggs.API.action.BlockChangeAction;
import com.ep.ggs.API.plugin.PlayerCommand;
import com.ep.ggs.chat.ChatColor;
import com.ep.ggs.iomodel.Player;
import com.ep.ggs.mb.MessageBlockPlugin;
import com.ep.ggs.mb.blocks.ZoneBlock;
import com.ep.ggs.server.Server;
import com.ep.ggs.world.Level;
import com.ep.ggs.world.blocks.Block;
import com.ep.ggs.world.blocks.classicmodel.ClassicBlock;


public class ZoneDel extends PlayerCommand {
	@Override
	public void execute(Player player, String[] arg1) {
		if (arg1.length != 0 && arg1[0].equalsIgnoreCase("all")) {
			player.sendMessage(ChatColor.Yellow + "Deleting all zones...");
			Thread t = new RunAll(player.getLevel(), player);
			t.start();
			return;
		}
		player.sendMessage("Place a block inside the zone you want to remove.");
		Thread t = new Run(player);
		t.start();
	}

	@Override
	public int getDefaultPermissionLevel() {
		return 50;
	}

	@Override
	public String getName() {
		return "zonedel";
	}

	@Override
	public String[] getShortcuts() {
		return new String[] { "zdel" };
	}

	@Override
	public void help(CommandExecutor arg0) {
		arg0.sendMessage("/zonedel - Delete a zone.");
		arg0.sendMessage("/zonedel [all] - Delete all zones in this level.");
	}

	@Override
	public boolean isOpCommandDefault() {
		return true;
	}
	
	private void remove(int x, int y, int z, Level l, ZoneBlock zb, Server server) {
		Player.GlobalBlockChange((short)x, (short)y, (short)z, ClassicBlock.getBlock(zb.getVisibleBlock()), l, server);
	}
	private void checkandremove(int x, int y, int z, Level l, Server server, ZoneBlock zb) {
		if (l.getTile(x + 1, y, z) instanceof ZoneBlock && ((ZoneBlock)l.getTile(x + 1, y, z)).getOwners() == zb.getOwners()) {
			remove(x + 1, y, z, l, (ZoneBlock)l.getTile(x + 1, y, z), server);
			checkandremove(x + 1, y, z, l, server, zb);
		}
		if (l.getTile(x - 1, y, z) instanceof ZoneBlock && ((ZoneBlock)l.getTile(x - 1, y, z)).getOwners() == zb.getOwners()) {
			remove(x - 1, y, z, l, (ZoneBlock)l.getTile(x - 1, y, z), server);
			checkandremove(x - 1, y, z, l, server, zb);
		}
		if (l.getTile(x, y + 1, z) instanceof ZoneBlock && ((ZoneBlock)l.getTile(x, y + 1, z)).getOwners() == zb.getOwners()) {
			remove(x, y + 1, z, l, (ZoneBlock)l.getTile(x, y + 1, z), server);
			checkandremove(x, y + 1, z, l, server, zb);
		}
		if (l.getTile(x, y - 1, z) instanceof ZoneBlock && ((ZoneBlock)l.getTile(x, y - 1, z)).getOwners() == zb.getOwners()) {
			remove(x, y - 1, z, l, (ZoneBlock)l.getTile(x, y - 1, z), server);
			checkandremove(x, y - 1, z, l, server, zb);
		}
		if (l.getTile(x, y, z + 1) instanceof ZoneBlock && ((ZoneBlock)l.getTile(x, y, z + 1)).getOwners() == zb.getOwners()) {
			remove(x, y, z + 1, l, (ZoneBlock)l.getTile(x, y, z + 1), server);
			checkandremove(x, y , z + 1, l, server, zb);
		}
		if (l.getTile(x, y, z - 1) instanceof ZoneBlock && ((ZoneBlock)l.getTile(x, y, z - 1)).getOwners() == zb.getOwners()) {
			remove(x, y, z - 1, l, (ZoneBlock)l.getTile(x, y, z - 1), server);
			checkandremove(x, y , z - 1, l, server, zb);
		}

	}
	
	private class RunAll extends Thread {
		Level l;
		Player p;
		public RunAll(Level l, Player p) { this.l = l; this.p = p; }
		
		@Override
		public void run() {
			Block b;
			for (int x = 0; x < l.getWidth(); x++) {
				for (int y = 0; y < l.getHeight(); y++) {
					for (int z = 0; z < l.getDepth(); z++) {
						b = l.getTile(x, y, z);
						if (b instanceof ZoneBlock)
							remove(x, y, z, l, (ZoneBlock)b, p.getServer());
					}
				}
			}
			p.sendMessage(ChatColor.Bright_Green + "All zone's removed!");
		}
	}
	
	private class Run extends Thread {
		Player player;
		public Run(Player player) { this.player = player; }
		
		@Override
		public void run() {
			int x1, y1, z1;
			MessageBlockPlugin.INSTANCE.deleters.add(player);
			try {
				ZoneBlock zb = null;
				while (zb == null) {
					Action<BlockChangeAction> action = new BlockChangeAction();
					action.setPlayer(player);
					BlockChangeAction response = action.waitForResponse();
					x1 = response.getX();
					y1 = response.getY();
					z1 = response.getZ();
					if (!(player.getLevel().getTile(x1, y1, z1) instanceof ZoneBlock)) {
						player.sendMessage(ChatColor.Red + "This is not a zone, please place a block in the zone to remove.");
						continue;
					}
					zb = (ZoneBlock)player.getLevel().getTile(x1, y1, z1);
					player.sendMessage(ChatColor.Yellow + "Removing zone...");
					remove(x1, y1, z1, player.getLevel(), zb, player.getServer());
					checkandremove(x1, y1, z1, player.getLevel(), player.getServer(), zb);
					player.sendMessage(ChatColor.Bright_Green + "Zone removed!");
					MessageBlockPlugin.INSTANCE.deleters.remove(player);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

