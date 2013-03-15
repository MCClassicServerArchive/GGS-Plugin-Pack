/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.mb.commands;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.ManualLoad;
import net.mcforge.API.action.Action;
import net.mcforge.API.action.BlockChangeAction;
import net.mcforge.API.plugin.PlayerCommand;
import net.mcforge.chat.ChatColor;
import net.mcforge.iomodel.Player;
import net.mcforge.mb.blocks.CommandBlock;
import net.mcforge.mb.blocks.MessageBlock;
import net.mcforge.world.blocks.classicmodel.ClassicBlock;

@ManualLoad
public class MB extends PlayerCommand {

	private ClassicBlock b;
	@Override
	public void execute(Player player, String[] arg1) {
		if (arg1.length == 0) { help(player); return; }
		if (arg1[0].equals("show") && arg1.length == 1) {
			show(player);
			return;
		}
		int startindex = 0;
		if (!ClassicBlock.getBlock(arg1[0]).name.equals("NULL")) {
			b = ClassicBlock.getBlock(arg1[0]);
			startindex++;
		}
		String message = "";
		for (int i = startindex; i < arg1.length; i++) {
			message += " " + arg1[i];
		}
		message = message.trim();
		player.sendMessage("Place a block where the message block will go!");
		Thread t = new Run(player, message);
		t.start();
	}

	@Override
	public int getDefaultPermissionLevel() {
		return 50;
	}

	@Override
	public String getName() {
		return "mb";
	}

	@Override
	public String[] getShortcuts() {
		return new String[] { };
	}

	@Override
	public void help(CommandExecutor arg0) {
		arg0.sendMessage("/mb <message> - Place a message in a block! When the block is hit, the message will display");
		arg0.sendMessage("/mb [block] <message> - Place a message in a [block] block! When the block is hit, the message will display");
		arg0.sendMessage("/mb show - Show/hide all message blocks in the current level your in.");
	}

	@Override
	public boolean isOpCommandDefault() {
		return false;
	}
	
	private void show(Player p) {
		boolean show = false;
		if (p.hasAttribute("showmb")) {
			show = p.getAttribute("showmb", Boolean.class).booleanValue();
		}
		show = !show;
		for (int x = 0; x < p.getLevel().getWidth(); x++) {
			for (int y = 0; y < p.getLevel().getHeight(); y++) {
				for (int z = 0; z < p.getLevel().getDepth(); z++) {
					if (p.getLevel().getTile(x, y, z) instanceof MessageBlock) {
						MessageBlock pb = (MessageBlock)p.getLevel().getTile(x, y, z);
						if (show)
							p.sendBlockChange((short)x, (short)y, (short)z, ClassicBlock.getBlock("White"));
						else
							p.sendBlockChange((short)x, (short)y, (short)z, pb);
					}
				}
			}
		}
		p.sendMessage("Now " + (show ? "showing" : "hiding") + " messageblocks");
		p.setAttribute("showmb", show);
	}
	
	private class Run extends Thread {
		String message;
		Player player;
		public Run(Player player, String message) { this.player = player; this.message = message; }
		
		@Override
		public void run() {
			Action<BlockChangeAction> action = new BlockChangeAction();
			action.setPlayer(player);
			try {
				BlockChangeAction response = action.waitForResponse();
				ClassicBlock mb;
				if (message.startsWith("/") && player.getServer().getCommandHandler().find(message.substring(1).split("\\ ")[0]) != null) {
					if (b == null)
						mb = new CommandBlock(message, ClassicBlock.getBlock((byte)36));
					else
						mb = new CommandBlock(message, b);
					player.sendMessage(ChatColor.Bright_Green + "Command Block placed!");
				}
				else {
					if (b == null)
						mb = new MessageBlock(message, ClassicBlock.getBlock((byte)36));
					else
						mb = new MessageBlock(message, b);
					player.sendMessage(ChatColor.Bright_Green + "Message Block placed!");
				}
				Player.GlobalBlockChange((short)response.getX(), (short)response.getY(), (short)response.getZ(), mb, player.getLevel(), player.getServer());
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				player.sendMessage("An error has occured..");
				return;
			} catch (InterruptedException e) {
				e.printStackTrace();
				player.sendMessage("An error has occured..");
				return;
			}
		}
	}
}

