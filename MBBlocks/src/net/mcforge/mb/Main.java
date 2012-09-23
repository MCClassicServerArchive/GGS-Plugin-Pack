package net.mcforge.mb;

import net.mcforge.API.player.PlayerBlockChangeEvent;
import net.mcforge.API.plugin.Plugin;
import net.mcforge.mb.commands.MB;
import net.mcforge.mb.events.Events;
import net.mcforge.server.Server;

public class Main extends Plugin {

	private final Events events = new Events();
	private final MB command = new MB();
	public Main(Server server) {
		super(server);
	}

	@Override
	public void onLoad(String[] arg0) {
		getServer().getEventSystem().registerEvents(events);
		getServer().getCommandHandler().addCommand(command);
	}

	@Override
	public void onUnload() {
		PlayerBlockChangeEvent.getEventList().unregister(events);
		getServer().getCommandHandler().removeCommand(command);
	}

}
