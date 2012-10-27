package net.mcforge.mb.commands;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.action.Action;
import net.mcforge.API.action.BlockChangeAction;
import net.mcforge.API.plugin.PlayerCommand;
import net.mcforge.chat.ChatColor;
import net.mcforge.iomodel.Player;
import net.mcforge.mb.MessageBlockPlugin;
import net.mcforge.mb.blocks.ZoneBlock;
import net.mcforge.server.Server;
import net.mcforge.world.Block;
import net.mcforge.world.Level;

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
		Player.GlobalBlockChange((short)x, (short)y, (short)z, Block.getBlock(zb.getVisableBlock()), l, server);
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
			for (int x = 0; x < l.width; x++) {
				for (int y = 0; y < l.height; y++) {
					for (int z = 0; z < l.depth; z++) {
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
			Action<BlockChangeAction> action = new BlockChangeAction();
			action.setPlayer(player);
			MessageBlockPlugin.INSTANCE.deleters.add(player);
			try {
				ZoneBlock zb = null;
				while (zb == null) {
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
