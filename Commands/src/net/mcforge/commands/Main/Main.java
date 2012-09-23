package net.mcforge.commands.Main;

import net.mcforge.API.plugin.Plugin;
import net.mcforge.command.ExampleCommand;
import net.mcforge.server.Server;

public class Main extends Plugin {

	public Main(Server server) {
		super(server);
	}

	@Override
	public void onLoad(String[] arg0) {
		//TODO Add all commands
		getServer().getCommandHandler().addCommand(new ExampleCommand());
	}

	@Override
	public void onUnload() {
	}

}
