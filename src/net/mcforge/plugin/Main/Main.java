package net.mcforge.plugin.Main;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import net.mcforge.API.plugin.Command;
import net.mcforge.API.plugin.Plugin;
import net.mcforge.plugin.commands.Afk;
import net.mcforge.plugin.commands.Ban;
import net.mcforge.plugin.commands.Devs;
import net.mcforge.plugin.commands.Goto;
import net.mcforge.plugin.commands.Help;
import net.mcforge.plugin.commands.Kick;
import net.mcforge.plugin.commands.Load;
import net.mcforge.plugin.commands.Loaded;
import net.mcforge.plugin.commands.Maps;
import net.mcforge.plugin.commands.Me;
import net.mcforge.plugin.commands.Newlvl;
import net.mcforge.plugin.commands.Players;
import net.mcforge.plugin.commands.Save;
import net.mcforge.plugin.commands.Spawn;
import net.mcforge.plugin.commands.Stop;
import net.mcforge.plugin.commands.TP;
import net.mcforge.plugin.commands.Unban;
import net.mcforge.groupmanager.main.GroupPlugin;
import net.mcforge.irc.IRCPlugin;
import net.mcforge.mb.MessageBlockPlugin;
import net.mcforge.server.Server;
import net.mcforge.system.updater.Updatable;
import net.mcforge.system.updater.UpdateType;
import net.mcforge.util.properties.Properties;

public class Main extends Plugin implements Updatable {

	private ArrayList<String> load = new ArrayList<String>();
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
	
	public boolean loadOptions() throws IOException {
		if (!new File("properties/mcforge_plugin.config").exists())
			return false;
		FileInputStream fstream = new FileInputStream("properties/mcforge_plugin.config");
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		while ((strLine = br.readLine()) != null)   {
			if (strLine.startsWith("#"))
				continue;
			load.add(strLine);
		}
		in.close();
		return true;
	}

	@Override
	public void onLoad(String[] arg0) {
		boolean savedefaults = true;
		try {
			savedefaults = !loadOptions();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		loadCommands(COMMANDS, savedefaults);
		
		//--Load plugins--
		Plugin p = new MessageBlockPlugin(getServer());
		addPlugin(p, savedefaults);
		p = new GroupPlugin(getServer());
		addPlugin(p, savedefaults);
		p = new IRCPlugin(getServer());
		addPlugin(p, savedefaults);
		//--Load plugins--
		
		getServer().Log("MCForge Defaults loaded!");
		if (savedefaults) {
			try {
				saveDefaults();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void saveDefaults() throws IOException {
		if (new File("properties/mcforge_plugin.config").exists())
			new File("properties/mcforge_plugin.config").delete();
		new File("properties/mcforge_plugin.config").createNewFile();
		PrintWriter out = new PrintWriter("properties/mcforge_plugin.config");
		out.println("#Here you can enable and disable plugins that are loaded");
		out.println("#By the mcforge plugin.");
		out.println("#To disable a plugin/command, simply put a # infront of it.");
		for (String s : load) {
			out.println(s);
		}
		out.close();
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
	
	private void loadCommands(Command[] commands, boolean add) {
		for (Command c : commands) {
			if (add)
				load.add(c.getName());
			if ((!add && load.contains(c.getName())) || add)
				getServer().getCommandHandler().addCommand(c);
		}
	}
	
	private void unloadCommands(Command[] commands) {
		for (Command c : commands) {
			getServer().getCommandHandler().removeCommand(c);
		}
	}
	
	private void addPlugin(Plugin p, boolean add) {
		if (add || (!add && load.contains(p.getName()))) {
			plugins.add(p);
			getServer().getPluginHandler().loadPlugin(p, getServer());
			if (add)
				load.add(p.getName());
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
		return UpdateType.Auto_Silent;
	}

	@Override
	public void unload() {
		getServer().getPluginHandler().unload(this);
	}

}
