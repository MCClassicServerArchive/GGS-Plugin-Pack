package net.mcforge.commands;

import net.mcforge.API.plugin.Command;
import net.mcforge.API.plugin.CommandHandler;
import net.mcforge.API.plugin.Plugin;
import net.mcforge.command.*;
import net.mcforge.server.Server;

public class CommandPlugin extends Plugin {

	protected static CommandHandler handler;
	
	@Override
	public String getName() {
		return getProperties().getProperty("name");
	}
	@Override
	public String getVersion() {
		return getProperties().getProperty("version");
	}
	public CommandPlugin(Server server) {
		super(server);
		handler = server.getCommandHandler();
	}

	@Override
	public void onLoad(String[] arg0) { //Keep alphabetically sorted if possible :>
		addCommands(new ActionExample(),
				    new Ban(),
				    new Devs(),
				    new ExampleCommand(),
				    new Goto(),
				    new Help(),
				    new Kick(),
				    new Load(),
				    new Loaded(),
				    new Maps(),
				    new Me(),
				    new Newlvl(),
				    new Players(),
				    new Spawn(),
				    new Stop(),
				    new TP(),
				    new Unban()
				);
	}

	@Override
	public void onUnload() {
	}

	private void addCommands(Command... commands) {
		for (int i = 0; i < commands.length; i++) {
			handler.addCommand(commands[i]);
		}
	}

}
