/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.mb;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import net.mcforge.API.ManualLoad;
import net.mcforge.API.player.PlayerBlockChangeEvent;
import net.mcforge.API.plugin.Plugin;
import net.mcforge.groups.Group;
import net.mcforge.iomodel.Player;
import net.mcforge.mb.blocks.MessageBlock;
import net.mcforge.mb.blocks.PortalBlock;
import net.mcforge.mb.blocks.ZoneBlock;
import net.mcforge.mb.commands.MB;
import net.mcforge.mb.commands.Portal;
import net.mcforge.mb.commands.Zone;
import net.mcforge.mb.commands.ZoneDel;
import net.mcforge.mb.events.Events;
import net.mcforge.server.Server;
import net.mcforge.world.Block;
import net.mcforge.world.Level;

@ManualLoad
public class MessageBlockPlugin extends Plugin {

	@Override
	public String getName() {
		return "MessageBlock Plugin";
	}
	@Override
	public String getAuthor() {
		return "hypereddie10";
	}
	@Override
	public String getVersion() {
		return "1.0.0";
	}
	private final Events events = new Events();
	private final MB command = new MB();
	private final Zone command2 = new Zone();
	private final ZoneDel command3 = new ZoneDel();
	private final Portal command4 = new Portal();
	public int permissionoverride;
	public boolean repeat;
	public ArrayList<Player> deleters = new ArrayList<Player>();
	public static MessageBlockPlugin INSTANCE;
	public MessageBlockPlugin(Server server) {
		super(server);
	}
	
	private int getHighest() {
		for (Group g : Group.getGroupList()) {
			if (g.isOP)
				return g.permissionlevel;
		}
		return 0;
	}

	@Override
	public void onLoad(String[] arg0) {
		if (!getServer().getSystemProperties().hasValue("zone-admin-permission")) {
			permissionoverride = getHighest();
			getServer().getSystemProperties().addSetting("zone-admin-permission", permissionoverride);
			getServer().getSystemProperties().addComment("zone-admin-permission", "What permission level can override zone ownership.");
			try {
				getServer().getSystemProperties().save("system.config");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
			permissionoverride = getServer().getSystemProperties().getInt("zone-admin-permission");
		if (!getServer().getSystemProperties().hasValue("messageblock-repeat-message")) {
			repeat = true;
			getServer().getSystemProperties().addSetting("messageblock-repeat-message", repeat);
			getServer().getSystemProperties().addComment("messageblock-repeat-message", "Whether or not to repeat messageblock messages if they are the same message.");
			try {
				getServer().getSystemProperties().save("system.config");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
			repeat = getServer().getSystemProperties().getBool("messageblock-repeat-message");
		getServer().getEventSystem().registerEvents(events);
		getServer().getCommandHandler().addCommand(command);
		getServer().getCommandHandler().addCommand(command2);
		getServer().getCommandHandler().addCommand(command3);
		getServer().getCommandHandler().addCommand(command4);
		INSTANCE = this;
	}

	@Override
	public void onUnload() {
		PlayerBlockChangeEvent.getEventList().unregister(events);
		getServer().getCommandHandler().removeCommand(command);
		getServer().getCommandHandler().removeCommand(command2);
		getServer().getCommandHandler().removeCommand(command3);
		getServer().getCommandHandler().removeCommand(command4);
		INSTANCE = null;
	}
	
	public void convert() throws IOException {
		FileInputStream fstream = new FileInputStream("properties/mbconvert.config");
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		while ((strLine = br.readLine()) != null)   {
			final String[] args = strLine.split("\\:");
			String type = args[0];
			int x1, x2, y1, y2, z1, z2;
			byte b = 0;
			boolean usetile = false;
			String Level;
			if (type.equals("MB")) {
				x1 = Integer.parseInt(args[1]);
				//x2 = Integer.parseInt(args[2]);
				y1 = Integer.parseInt(args[2]);
				//y2 = Integer.parseInt(args[4]);
				z1 = Integer.parseInt(args[3]);
				//z2 = Integer.parseInt(args[6]);
				if (args[4].equals("TILE"))
					usetile = true;
				else
					b = Byte.parseByte(args[4]);
				Level = args[5];
				String message = "";
				for (int i = 6; i < args.length; i++) {
					message += args[i];
				}
				Level l;
				boolean tried = false;
				while ((l = getServer().getLevelHandler().findLevel(Level)) == null && !tried) {
					getServer().getLevelHandler().loadClassicLevel("levels/" + Level + ".ggs");
					tried = true;
				}
				if (l == null) {
					getServer().Log("Could not find " + Level + "!");
					continue;
				}
				MessageBlock mb;
				if (!usetile)
					mb = new MessageBlock(message, Block.getBlock(b));
				else
					mb = new MessageBlock(message, l.getTile(x1, y1, z1));
				Player.GlobalBlockChange((short)x1, (short)y1, (short)z1, mb, l, getServer());
			}
			else if (type.equals("PORTAL")) {
				x1 = Integer.parseInt(args[1]);
				x2 = Integer.parseInt(args[2]);
				y1 = Integer.parseInt(args[3]);
				y2 = Integer.parseInt(args[4]);
				z1 = Integer.parseInt(args[5]);
				z2 = Integer.parseInt(args[6]);
				String Level2 = args[7]; //Start Level
				String Level3 = args[8]; //End Level
				Level l, l2;
				boolean tried = false;
				while ((l = getServer().getLevelHandler().findLevel(Level2)) == null && !tried) {
					getServer().getLevelHandler().loadClassicLevel("levels/" + Level2 + ".ggs");
					tried = true;
				}
				if (l == null) {
					getServer().Log("Could not find " + Level2 + "!");
					continue;
				}
				while ((l2 = getServer().getLevelHandler().findLevel(Level3)) == null && !tried) {
					getServer().getLevelHandler().loadClassicLevel("levels/" + Level3 + ".ggs");
					tried = true;
				}
				if (l2 == null) {
					getServer().Log("Could not find " + Level3 + "!");
					continue;
				}
				PortalBlock pb1 = new PortalBlock(x2, y2, z2, l2, l.getTile(x1, y1, z1));
				Player.GlobalBlockChange((short)(x1 / 32), (short)(y1 / 32), (short)(z1 / 32), pb1, l, getServer());
				PortalBlock pb2 = new PortalBlock(x1, y1, z1, l, Block.getBlock("Air"));
				pb2.setExit(true);
				Player.GlobalBlockChange((short)(x2 / 32), (short)(y2 / 32), (short)(z2 / 32), pb2, l2, getServer());
			}
			else if (type.equals("ZONE")) {
				x1 = Integer.parseInt(args[1]);
				x2 = Integer.parseInt(args[2]);
				y1 = Integer.parseInt(args[3]);
				y2 = Integer.parseInt(args[4]);
				z1 = Integer.parseInt(args[5]);
				z2 = Integer.parseInt(args[6]);
				String owner = args[7];
				if (owner.startsWith("grp"))
					owner = owner.substring(3);
				String Level1 = args[8];
				Level l;
				boolean tried = false;
				while ((l = getServer().getLevelHandler().findLevel(Level1)) == null && !tried) {
					getServer().getLevelHandler().loadClassicLevel("levels/" + Level1 + ".ggs");
					tried = true;
				}
				if (l == null) {
					getServer().Log("Could not find " + Level1 + "!");
					continue;
				}
				for (int xx = Math.min(x1, x2); xx <= Math.max(x1, x2); ++xx) {
					for (int yy = Math.min(y1, y2); yy <= Math.max(y1, y2); ++yy) {
						for (int zz = Math.min(z1, z2); zz <= Math.max(z1, z2); ++zz) {
							ZoneBlock zb = new ZoneBlock(new String[] { owner }, l.getTile(xx, yy, zz));
							Player.GlobalBlockChange((short)xx, (short)yy, (short)zz, zb, l, getServer());
						}
					}
				}
			}
		}
		in.close();
	}
	
	public class TZone {
		public int x1;
		public int y1;
		public int z1;
		public int x2;
		public int y2;
		public int z2;
		public String owner;
		
		@Override
		public boolean equals(Object owner) {
			if (owner instanceof TZone) {
				TZone tz = (TZone)owner;
				return tz.x1 == x1 && tz.x2 == x2 && tz.y1 == y1 && tz.y2 == y2 && tz.z1 == z1 && tz.z2 == z2;
			}
			return false;
		}
	}

}

