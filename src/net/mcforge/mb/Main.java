package net.mcforge.mb;

import net.mcforge.API.player.PlayerBlockChangeEvent;
import net.mcforge.API.plugin.Plugin;
import net.mcforge.mb.commands.MB;
import net.mcforge.mb.commands.Zone;
import net.mcforge.mb.events.Events;
import net.mcforge.server.Server;

public class Main extends Plugin {

	private final Events events = new Events();
	private final MB command = new MB();
	private final Zone command2 = new Zone();
	public Main(Server server) {
		super(server);
	}

	@Override
	public void onLoad(String[] arg0) {
		getServer().getEventSystem().registerEvents(events);
		getServer().getCommandHandler().addCommand(command);
		getServer().getCommandHandler().addCommand(command2);
	}

	@Override
	public void onUnload() {
		PlayerBlockChangeEvent.getEventList().unregister(events);
		getServer().getCommandHandler().removeCommand(command);
		getServer().getCommandHandler().removeCommand(command2);
	}

}
