package net.mcforge.mb.events;

import net.mcforge.API.EventHandler;
import net.mcforge.API.Listener;
import net.mcforge.API.player.PlayerBlockChangeEvent;
import net.mcforge.iomodel.Player;
import net.mcforge.mb.blocks.MessageBlock;
import net.mcforge.mb.blocks.ZoneBlock;
import net.mcforge.world.Block;
import net.mcforge.world.PlaceMode;

public class Events implements Listener {
	
	@EventHandler
	public void breakblock(PlayerBlockChangeEvent event) {
		if (event.getPlaceType() == PlaceMode.PLACE) {
			Block b = event.getPlayer().getLevel().getTile(event.getX(), event.getY(), event.getZ());
			System.out.println((b instanceof ZoneBlock));
			System.out.println(b);
			try {
				System.out.println(((ZoneBlock)b).getOwners()[0]);
			}
			catch (Exception e) {
				e.printStackTrace();
				System.out.println("Welp, that didnt work");
			}
			System.out.println(b.getVisableBlock());
			System.out.println(b.getClass().getClassLoader());
			System.out.println(ZoneBlock.class.getClassLoader());
			System.out.println("-------");
			if (b instanceof ZoneBlock) {
				ZoneBlock zb = (ZoneBlock)b;
				for (String s : zb.getOwners()) {
					event.getPlayer().sendMessage(s);
				}
				
				if (zb.canBuild(event.getPlayer())) {
					ZoneBlock newb = zb.clone(event.getBlock());
					Player.GlobalBlockChange(event.getX(), event.getY(), event.getZ(), newb, event.getLevel(), event.getServer());
					event.setCancel(true);
					return;
				}
				else {
					event.getPlayer().sendMessage("Sorry, but you cant build here!");
					event.setCancel(true);
					return;
				}
			}
		}
		if (event.getPlaceType() == PlaceMode.BREAK) {
			Block b = event.getPlayer().getLevel().getTile(event.getX(), event.getY(), event.getZ());
			if (b instanceof MessageBlock) {
				MessageBlock mb = (MessageBlock)b;
				event.getPlayer().sendMessage(mb.getMessage());
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
					event.getPlayer().sendMessage("Sorry, but you cant build here!");
					event.setCancel(true);
					return;
				}
			}
		}
	}

}
