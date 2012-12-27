/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.plugin.Main;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.EventHandler;
import net.mcforge.API.Listener;
import net.mcforge.API.player.PlayerBanRequestEvent;
import net.mcforge.API.plugin.Command;
import net.mcforge.API.plugin.CommandLoadEvent;
import net.mcforge.API.plugin.Plugin;
import net.mcforge.banhandler.BanHandler;
import net.mcforge.chat.ChatColor;
import net.mcforge.globalchat.GlobalChatPlugin;
import net.mcforge.groupmanager.main.GroupPlugin;
import net.mcforge.irc.IRCPlugin;
import net.mcforge.mb.MessageBlockPlugin;
import net.mcforge.plugin.commands.Afk;
import net.mcforge.plugin.commands.Ban;
import net.mcforge.plugin.commands.Color;
import net.mcforge.plugin.commands.Cuboid;
import net.mcforge.plugin.commands.Devs;
import net.mcforge.plugin.commands.Give;
import net.mcforge.plugin.commands.Goto;
import net.mcforge.plugin.commands.Help;
import net.mcforge.plugin.commands.Kick;
import net.mcforge.plugin.commands.Load;
import net.mcforge.plugin.commands.Loaded;
import net.mcforge.plugin.commands.Maps;
import net.mcforge.plugin.commands.Me;
import net.mcforge.plugin.commands.Money;
import net.mcforge.plugin.commands.Mute;
import net.mcforge.plugin.commands.Muted;
import net.mcforge.plugin.commands.Newlvl;
import net.mcforge.plugin.commands.Nick;
import net.mcforge.plugin.commands.Pay;
import net.mcforge.plugin.commands.Place;
import net.mcforge.plugin.commands.Players;
import net.mcforge.plugin.commands.Replace;
import net.mcforge.plugin.commands.Replaceall;
import net.mcforge.plugin.commands.Rules;
import net.mcforge.plugin.commands.Save;
import net.mcforge.plugin.commands.Spawn;
import net.mcforge.plugin.commands.Stop;
import net.mcforge.plugin.commands.Summon;
import net.mcforge.plugin.commands.TColor;
import net.mcforge.plugin.commands.TP;
import net.mcforge.plugin.commands.Take;
import net.mcforge.plugin.commands.Title;
import net.mcforge.plugin.commands.Unban;
import net.mcforge.plugin.commands.Whisper;
import net.mcforge.plugin.help.HelpItemManager;
import net.mcforge.server.Server;
import net.mcforge.system.updater.Updatable;
import net.mcforge.system.updater.UpdateType;

public class Main extends Plugin implements Updatable, Listener {
    private static final String VERSION = "1.2.0";
    private static final String CONFIG_VERSION = "#VERSION.4";
    public static final HelpItemManager helpmanager = new HelpItemManager();
    private ArrayList<String> load = new ArrayList<String>();
    private static final Command[] COMMANDS = new Command[] {
        new Afk(),
        new Ban(),
        new Color(),
        new Cuboid(),
        new Devs(),
        new Give(),
        new Goto(),
        new Help(),
        new Kick(),
        new Load(),
        new Loaded(),
        new Maps(),
        new Me(),
        new Money(),
        new Mute(),
        new Muted(),
        new Newlvl(),
        new Nick(),
        new Pay(),
        new Place(),
        new Players(),
        new Rules(),
        new Replace(),
        new Replaceall(),
        new Save(),
        new Spawn(),
        new Stop(),
        new Summon(),
        new Take(),
        new TColor(),
        new Title(),
        new TP(),
        new Unban(),
        new Whisper()
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
        strLine = br.readLine();
        if (strLine.equals(CONFIG_VERSION)) {
            while ((strLine = br.readLine()) != null)   {
                if (strLine.startsWith("#"))
                    continue;
                load.add(strLine);
            }
            in.close();
            return true;
        }
        else {
            getServer().Log("Adding new commands to config..");
            ArrayList<String> write = new ArrayList<String>();
            while ((strLine = br.readLine()) != null) {
                write.add(strLine);
                if (!strLine.startsWith("#"))
                    load.add(strLine);
            }
            in.close();
            for (Command c : COMMANDS) {
                if (!write.contains(c.getName()))
                    write.add(c.getName());
            }
            updateConfig(write.toArray(new String[write.size()]));
            write.clear();
            getServer().Log("Done!");
            return true;
        }
    }

    @Override
    public void onLoad(String[] arg0) {
        helpmanager.init(getServer());
        boolean savedefaults = true;
        try {
            savedefaults = !loadOptions();
        } catch (IOException e) {
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
        p = new GlobalChatPlugin(getServer());
        addPlugin(p, savedefaults);
        //--Load plugins--
        getServer().Log("MCForge Defaults loaded!");
        if (savedefaults) {
            try {
                saveDefaults();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveDefaults() throws IOException {
        if (new File("properties/mcforge_plugin.config").exists())
            new File("properties/mcforge_plugin.config").delete();
        new File("properties/mcforge_plugin.config").createNewFile();
        PrintWriter out = new PrintWriter("properties/mcforge_plugin.config");
        out.println(CONFIG_VERSION);
        out.println("#Here you can enable and disable plugins that are loaded");
        out.println("#By the mcforge plugin.");
        out.println("#To disable a plugin/command, simply put a # infront of it.");
        out.println("#You can also remove the line, but then you might forget");
        out.println("#The command/plugin name if you ever want to enable it again :P");
        for (String s : load) {
            out.println(s);
        }
        out.close();
    }
    
    private void updateConfig(String[] data) throws IOException {
        if (new File("properties/mcforge_plugin.config").exists())
            new File("properties/mcforge_plugin.config").delete();
        new File("properties/mcforge_plugin.config").createNewFile();
        PrintWriter out = new PrintWriter("properties/mcforge_plugin.config");
        out.println(CONFIG_VERSION);
        for (String s : data) {
            out.println(s);
        }
        out.close();
    }

    @Override
    public void onUnload() {
        helpmanager.deinit();
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
            if ((!add && load.contains(c.getName())) || add) {
                getServer().getCommandHandler().addCommand(c);
                try {
                    if (getServer().VERSION_NUMBER < 600.7) {
                        CommandLoadEvent cle = new CommandLoadEvent(c, getServer());
                        getServer().getEventSystem().callEvent(cle);
                    }
                } catch (Exception e) {
                    CommandLoadEvent cle = new CommandLoadEvent(c, getServer());
                    getServer().getEventSystem().callEvent(cle);
                }
            }
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
            getServer().getPluginHandler().loadPlugin(p);
            if (add)
                load.add(p.getName());
        }
    }
    
    //used for color and title
	public static void displayValidColors(CommandExecutor executor) {
		executor.sendMessage("Valid colors are:");
		executor.sendMessage("&0Black &f| &1Navy &f| &2Green &f| &3Teal");
		executor.sendMessage("&4Maroon &f| &5Purple &f| &6Gold &f| &7Silver");
		executor.sendMessage("&8Gray &f| &9Blue &f| &aLime &f| &bAqua");
		executor.sendMessage("&cRed &f| &dPink &f| &eYellow &f| White");
	}

    @Override
    public String getName() {
        return "MCForge Defaults";
    }
    @Override
    public String getAuthor() {
    	return "MCForge Development Team";
    }
    @Override
    public String getVersion() {
    	return VERSION;
    }
    @Override
    public String getCheckURL() {
        return "http://update.mcforge.net/VERSION_2/defaults/current.txt";
    }

    @Override
    public String getCurrentVersion() {
        return VERSION;
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

    @EventHandler
    public void onBanRequest(PlayerBanRequestEvent event) {
        final String name = event.getPlayer().getName();
        if (BanHandler.banHandler.isBanned(name)) {
            event.getBanner().sendMessage(name + " is already banned!");
            return;
        }
        getServer().sendGlobalMessage(event.getBanner().getName() + " banned " + event.getPlayer().getDisplayName() + ChatColor.White + " for " + ChatColor.Dark_Red + event.getReason());
        BanHandler.banHandler.ban(name);
    }
}

