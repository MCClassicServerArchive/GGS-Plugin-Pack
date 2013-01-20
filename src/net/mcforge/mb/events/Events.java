/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.mb.events;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import net.mcforge.API.EventHandler;
import net.mcforge.API.Listener;
import net.mcforge.API.player.PlayerBlockChangeEvent;
import net.mcforge.API.player.PlayerMoveEvent;
import net.mcforge.API.plugin.Command;
import net.mcforge.chat.ChatColor;
import net.mcforge.iomodel.Player;
import net.mcforge.mb.MessageBlockPlugin;
import net.mcforge.mb.blocks.CommandBlock;
import net.mcforge.mb.blocks.PortalBlock;
import net.mcforge.mb.blocks.MessageBlock;
import net.mcforge.mb.blocks.ZoneBlock;
import net.mcforge.world.blocks.Block;
import net.mcforge.world.PlaceMode;
import net.mcforge.API.server.ServerStartedEvent;;

public class Events implements Listener {
	
	private HashMap<Player, String> antirepeat = new HashMap<Player, String>();
	
	@EventHandler
	public void serverStarted(ServerStartedEvent eventargs) {
		if (new File("properties/mbconvert.config").exists()) {
			eventargs.getServer().Log("Convert file found!");
			eventargs.getServer().Log("Converting...");
			try {
				MessageBlockPlugin.INSTANCE.convert();
			} catch (IOException e) {
				e.printStackTrace();
			}
			eventargs.getServer().Log("Done!");
			new File("properties/mbconvert.config").delete();
		}
	}
	
	@EventHandler
	public void playermove(PlayerMoveEvent event) {
		Check(event, 1);
		Check(event, 0);
	}
	
	private void Check(PlayerMoveEvent event, int yoffset) {
		final int x = event.getPlayer().getBlockX();
		final int y = event.getPlayer().getBlockY() - yoffset;
		final int z = event.getPlayer().getBlockZ();
		final Block b = event.getPlayer().getLevel().getTile(x, y, z);
		if (b.canWalkThrough() && b instanceof MessageBlock) {
			MessageBlock mb = (MessageBlock)b;
			if (!MessageBlockPlugin.INSTANCE.repeat) {
				if (!antirepeat.containsKey(event.getPlayer())) {
					antirepeat.put(event.getPlayer(), mb.getMessage());
				}
				else {
					if (antirepeat.get(event.getPlayer()).equals(mb.getMessage()))
						return;
					else {
						antirepeat.remove(event.getPlayer());
						antirepeat.put(event.getPlayer(), mb.getMessage());
					}
				}
			}
			event.getPlayer().sendMessage(mb.getMessage());
			return;
		}
		if (b.canWalkThrough() && b instanceof CommandBlock) {
			CommandBlock cb = (CommandBlock)b;
			if (!MessageBlockPlugin.INSTANCE.repeat) {
				if (!antirepeat.containsKey(event.getPlayer())) {
					antirepeat.put(event.getPlayer(), cb.getMessage());
				}
				else {
					if (antirepeat.get(event.getPlayer()).equals(cb.getMessage()))
						return;
					else {
						antirepeat.remove(event.getPlayer());
						antirepeat.put(event.getPlayer(), cb.getMessage());
					}
				}
			}
			final Command c = cb.getCommand(event.getPlayer().getServer());
			String[] args = new String[cb.getMessage().split("\\ ").length - 1]; //remove command
			for (int i = 0; i < args.length; i++) {
				args[i] = cb.getMessage().split("\\ ")[i + 1];
			}
			event.getPlayer().getServer().getCommandHandler().execute(event.getPlayer(), c.getName(), args);
			return;
		}
		else if (b.canWalkThrough() && b instanceof PortalBlock) {
			PortalBlock pb = (PortalBlock)b;
			if (!pb.isExit()) {
				if (pb.getDestination().getLevel() != event.getPlayer().getLevel())
					event.getPlayer().changeLevel(pb.getDestination().getLevel(), false);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) { }
				event.getPlayer().setPos((short)pb.getDestination().getX(), (short)pb.getDestination().getY(), (short)pb.getDestination().getZ());
				return;
			}
		}
	}
	
	@EventHandler
	public void breakblock(PlayerBlockChangeEvent event) {
		if (MessageBlockPlugin.INSTANCE.deleters.contains(event.getPlayer()))
			return;
		if (event.getPlaceType() == PlaceMode.PLACE) {
			Block b = event.getPlayer().getLevel().getTile(event.getX(), event.getY(), event.getZ());
			if (b instanceof ZoneBlock) {
				ZoneBlock zb = (ZoneBlock)b;
				if (zb.canBuild(event.getPlayer()) || MessageBlockPlugin.INSTANCE.permissionoverride <= event.getPlayer().getGroup().permissionlevel) {
					ZoneBlock newb = zb.clone(event.getBlock());
					Player.GlobalBlockChange(event.getX(), event.getY(), event.getZ(), newb, event.getLevel(), event.getServer());
					event.setCancel(true);
					return;
				}
				else {
					event.getPlayer().sendMessage(ChatColor.Red + "Sorry, but you cant build here!");
					event.getPlayer().sendMessage("This zone is owned by " + zb.getOwnersString());
					event.setCancel(true);
					return;
				}
			}
		}
		if (event.getPlaceType() == PlaceMode.BREAK) {
			Block b = event.getPlayer().getLevel().getTile(event.getX(), event.getY(), event.getZ());
			if (b instanceof MessageBlock) {
				MessageBlock mb = (MessageBlock)b;
				if (!MessageBlockPlugin.INSTANCE.repeat) {
					if (!antirepeat.containsKey(event.getPlayer())) {
						antirepeat.put(event.getPlayer(), mb.getMessage());
					}
					else {
						if (antirepeat.get(event.getPlayer()).equals(mb.getMessage())) {
							event.setCancel(true);
							return;
						}
						else {
							antirepeat.remove(event.getPlayer());
							antirepeat.put(event.getPlayer(), mb.getMessage());
						}
					}
				}
				event.getPlayer().sendMessage(mb.getMessage());
				event.setCancel(true);
				return;
			}
			if (b instanceof PortalBlock) {
				PortalBlock pb = (PortalBlock)b;
				if (!pb.isExit()) {
					if (pb.getDestination().getLevel() != event.getPlayer().getLevel())
						event.getPlayer().changeLevel(pb.getDestination().getLevel(), false);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) { }
					event.getPlayer().setPos((short)pb.getDestination().getX(), (short)pb.getDestination().getY(), (short)pb.getDestination().getZ());
				}
				event.setCancel(true);
				return;
			}
			if (b instanceof CommandBlock) {
				CommandBlock cb = (CommandBlock)b;
				final Command c = cb.getCommand(event.getServer());
				String[] args = new String[cb.getMessage().split("\\ ").length - 1]; //remove command
				for (int i = 0; i < args.length; i++) {
					args[i] = cb.getMessage().split("\\ ")[i + 1];
				}
				event.getServer().getCommandHandler().execute(event.getPlayer(), c.getName(), args);
				event.setCancel(true);
				return;
			}
			if (b instanceof ZoneBlock) {
				ZoneBlock zb = (ZoneBlock)b;
				for (String s : zb.getOwners()) {
					event.getPlayer().sendMessage(s);
				}
				if (zb.canBuild(event.getPlayer())) {
					ZoneBlock newb = zb.clone(Block.getBlock("Air"));
					Player.GlobalBlockChange(event.getX(), event.getY(), event.getZ(), newb, event.getLevel(), event.getServer());
					event.setCancel(true);
					return;
				}
				else {
					event.getPlayer().sendMessage(ChatColor.Red + "Sorry, but you cant build here!");
					event.getPlayer().sendMessage("This zone is owned by " + zb.getOwnersString());
					return;
				}
			}
		}
	}

}

