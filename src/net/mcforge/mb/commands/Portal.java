/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.mb.commands;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.action.Action;
import net.mcforge.API.action.BlockChangeAction;
import net.mcforge.API.plugin.PlayerCommand;
import net.mcforge.chat.ChatColor;
import net.mcforge.iomodel.Player;
import net.mcforge.mb.blocks.PortalBlock;
import net.mcforge.world.blocks.Block;
import net.mcforge.world.Level;

public class Portal extends PlayerCommand {

	Block b;
	@Override
	public void execute(Player player, String[] arg1) {
		if (arg1.length != 0) {
			if (arg1[0].equalsIgnoreCase("show")) {
				show(player);
				return;
			}
			int startindex = 0;
			if (!Block.getBlock(arg1[0]).name.equals("NULL")) {
				b = Block.getBlock(arg1[0]);
				startindex++;
			}
			String message = "";
			for (int i = startindex; i < arg1.length; i++) {
				message += arg1[i];
			}
			message = message.trim();
		}
		player.sendMessage("Place an &aEntry block" + ChatColor.White + " for the portal.");
		Thread t = new Run(player);
		t.start();
	}

	@Override
	public int getDefaultPermissionLevel() {
		return 50;
	}

	@Override
	public String getName() {
		return "portal";
	}

	@Override
	public String[] getShortcuts() {
		return new String[0];
	}

	@Override
	public void help(CommandExecutor arg0) {
		 arg0.sendMessage("/portal [block] [multi] - Activates Portal mode.");
         arg0.sendMessage("/portal [type] multi - Place Entry blocks until exit is wanted.");
         arg0.sendMessage("/portal show - Shows portals, green = in, red = out.");
	}

	@Override
	public boolean isOpCommandDefault() {
		return false;
	}
	
	public void show(Player p) {
		boolean show = false;
		if (p.hasAttribute("showportal")) {
			show = ((Boolean)(p.getAttribute("showportal"))).booleanValue();
		}
		show = !show;
		for (int x = 0; x < p.getLevel().getWidth(); x++) {
			for (int y = 0; y < p.getLevel().getHeight(); y++) {
				for (int z = 0; z < p.getLevel().getDepth(); z++) {
					if (p.getLevel().getTile(x, y, z) instanceof PortalBlock) {
						PortalBlock pb = (PortalBlock)p.getLevel().getTile(x, y, z);
						if (show)
							p.sendBlockChange((short)x, (short)y, (short)z, (pb.isExit() ? Block.getBlock("Red") : Block.getBlock("Green")));
						else
							p.sendBlockChange((short)x, (short)y, (short)z, pb);
					}
				}
			}
		}
		p.sendMessage("Now " + (show ? "showing" : "hiding") + " portals");
		p.setAttribute("showportal", show);
	}

	public class Run extends Thread {
		private Player player;
		public Run(Player player) {this.player = player; }
		@Override
		public void run() {
			Action<BlockChangeAction> action = new BlockChangeAction();
			action.setPlayer(player);
			try {
				BlockChangeAction response = action.waitForResponse();
				int startx, starty, startz, endx, endy, endz;
				startx = response.getX() * 32;
				starty = response.getY() * 32;
				startz = response.getZ() * 32;
				final Level startl = player.getLevel();
				player.sendMessage("&aEntry block placed.");
				
				action = new BlockChangeAction();
				action.setPlayer(player);
				response = action.waitForResponse();
				endx = response.getX() * 32;
				endy = response.getY() * 32;
				endz = response.getZ() * 32;
				final Level endl = player.getLevel();
				
				
				PortalBlock pb1 = new PortalBlock(endx, endy, endz, endl, (b == null ? Block.getBlock("Blue") : b));
				Player.GlobalBlockChange((short)(startx / 32), (short)(starty / 32), (short)(startz / 32), pb1, startl, player.getServer());
				PortalBlock pb2 = new PortalBlock(startx, starty, startz, startl, Block.getBlock("Air"));
				pb2.setExit(true);
				Player.GlobalBlockChange((short)(endx / 32), (short)(endy / 32), (short)(endz / 32), pb2, endl, player.getServer());
				player.sendMessage("&3Exit" + ChatColor.White + " block placed.");
				b = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
