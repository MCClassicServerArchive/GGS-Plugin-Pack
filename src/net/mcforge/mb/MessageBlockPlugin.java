package net.mcforge.mb;

import java.util.ArrayList;

import net.mcforge.API.ManualLoad;
import net.mcforge.API.player.PlayerBlockChangeEvent;
import net.mcforge.API.plugin.Plugin;
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
	public ArrayList<Player> deleters = new ArrayList<Player>();
	public static MessageBlockPlugin INSTANCE;
	public MessageBlockPlugin(Server server) {
		super(server);
	}

	@Override
	public void onLoad(String[] arg0) {
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
