package net.mcforge.mb;

import java.io.IOException;
import java.util.ArrayList;

import net.mcforge.API.ManualLoad;
import net.mcforge.API.player.PlayerBlockChangeEvent;
import net.mcforge.API.plugin.Plugin;
import net.mcforge.groups.Group;
import net.mcforge.iomodel.Player;
import net.mcforge.mb.commands.MB;
import net.mcforge.mb.commands.Zone;
import net.mcforge.mb.commands.ZoneDel;
import net.mcforge.mb.events.Events;
import net.mcforge.server.Server;

@ManualLoad
public class MessageBlockPlugin extends Plugin {

	private final Events events = new Events();
	private final MB command = new MB();
	private final Zone command2 = new Zone();
	private final ZoneDel command3 = new ZoneDel();
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
			getServer().getSystemProperties().addComment("messageblock-repeat-message", "Weather or not to repeat messageblock messages if they are the same message.");
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
		INSTANCE = this;
	}

	@Override
	public void onUnload() {
		PlayerBlockChangeEvent.getEventList().unregister(events);
		getServer().getCommandHandler().removeCommand(command);
		getServer().getCommandHandler().removeCommand(command2);
		getServer().getCommandHandler().removeCommand(command3);
		INSTANCE = null;
	}

}
