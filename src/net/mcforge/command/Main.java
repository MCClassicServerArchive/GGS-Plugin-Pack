package net.mcforge.command;

import net.mcforge.API.plugin.Command;
import net.mcforge.API.plugin.Plugin;
import net.mcforge.server.Server;
import net.mcforge.system.updater.Updatable;
import net.mcforge.system.updater.UpdateType;

public class Main extends Plugin implements Updatable {

	private static final Command[] COMMANDS = new Command[] {
		new Afk(),
		new Ban(),
		new Devs(),
		new Goto(),
		new Help(),
		new Kick(),
		new Load(),
		new Loaded(),
		new Maps(),
		new Me(),
		new Newlvl(),
		new Players(),
		new Save(),
		new Spawn(),
		new Stop(),
		new TP(),
		new Unban()
	};
	public Main(Server server) {
		super(server);
	}

	@Override
	public void onLoad(String[] arg0) {
		loadCommands(COMMANDS);
	}

	@Override
	public void onUnload() {
		unloadCommands(COMMANDS);
	}
	
	private void loadCommands(Command[] commands) {
		for (Command c : commands) {
			getServer().getCommandHandler().addCommand(c);
		}
	}
	
	private void unloadCommands(Command[] commands) {
		for (Command c : commands) {
			getServer().getCommandHandler().removeCommand(c);
		}
	}
	
	@Override
	public String getName() {
		return "MCForge Commands";
	}

	@Override
	public String getCheckURL() {
		return null;
	}

	@Override
	public String getCurrentVersion() {
		return null;
	}

	@Override
	public String getDownloadPath() {
		return null;
	}

	@Override
	public String getDownloadURL() {
		return null;
	}

	@Override
	public UpdateType getUpdateType() {
		return UpdateType.Auto_Silent;
	}

	@Override
	public void unload() {
		getServer().getPluginHandler().unload(this);
	}

}
