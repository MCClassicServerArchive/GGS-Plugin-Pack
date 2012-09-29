package net.mcforge.mb.events;

import net.mcforge.API.EventHandler;
import net.mcforge.API.Listener;
import net.mcforge.API.player.PlayerBlockChangeEvent;
import net.mcforge.mb.blocks.MessageBlock;
import net.mcforge.world.Block;
import net.mcforge.world.PlaceMode;

public class Events implements Listener {
	
	@EventHandler
	public void breakblock(PlayerBlockChangeEvent event) {
		if (event.getPlaceType() == PlaceMode.BREAK) {
			Block b = event.getPlayer().getLevel().getTile(event.getX(), event.getY(), event.getZ());
			if (b instanceof MessageBlock) {
				MessageBlock mb = (MessageBlock)b;
				event.getPlayer().sendMessage(mb.getMessage());
				event.setCancel(true);
			}
		}
	}

}
