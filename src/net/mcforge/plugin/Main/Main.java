package net.mcforge.plugin.Main;

import java.util.ArrayList;

import net.mcforge.API.plugin.Command;
import net.mcforge.API.plugin.Plugin;
import net.mcforge.command.Afk;
import net.mcforge.command.Ban;
import net.mcforge.command.Devs;
import net.mcforge.command.Goto;
import net.mcforge.command.Help;
import net.mcforge.command.Kick;
import net.mcforge.command.Load;
import net.mcforge.command.Loaded;
import net.mcforge.command.Maps;
import net.mcforge.command.Me;
import net.mcforge.command.Newlvl;
import net.mcforge.command.Players;
import net.mcforge.command.Save;
import net.mcforge.command.Spawn;
import net.mcforge.command.Stop;
import net.mcforge.command.TP;
import net.mcforge.command.Unban;
import net.mcforge.groupmanager.main.GroupPlugin;
import net.mcforge.mb.MessageBlockPlugin;
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
	private static final ArrayList<Plugin> plugins = new ArrayList<Plugin>();
	public Main(Server server) {
		super(server);
	}

	@Override
	public void onLoad(String[] arg0) {
		getServer().getUpdateService().getUpdateManager().add(this);
		
		loadCommands(COMMANDS);
		
		//--Load plugins--
		Plugin p = new MessageBlockPlugin(getServer());
		getServer().getPluginHandler().loadPlugin(p, getServer());
		plugins.add(p);
		p = new GroupPlugin(getServer());
		getServer().getPluginHandler().loadPlugin(p, getServer());
		plugins.add(p);
		//--Load plugins--
	}

	@Override
	public void onUnload() {
		unloadCommands(COMMANDS);
		
		//--Unload plugins--
		for (Plugin p : plugins) {
			getServer().getPluginHandler().unload(p);
		}
		//--Unload plugins--
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
		return "MCForge Defaults";
	}

	@Override
	public String getCheckURL() {
		return "http://update.mcforge.net/VERSION_2/defaults/current.txt";
	}

	@Override
	public String getCurrentVersion() {
		return "1.0.0";
	}

	@Override
	public String getDownloadPath() {
		return "plugins/Defaults.jar";
	}

	@Override
	public String getDownloadURL() {
		return "http://update.mcforge.net/VERSION_2/defaults/Defaults.jar";
	}

	@Override
	public UpdateType getUpdateType() {
		return UpdateType.Auto_Notify;
	}

	@Override
	public void unload() {
		getServer().getPluginHandler().unload(this);
	}

}
