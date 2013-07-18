/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.plugin.Main;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.ep.ggs.API.CommandExecutor;
import com.ep.ggs.API.EventHandler;
import com.ep.ggs.API.Listener;
import com.ep.ggs.API.player.PlayerBanRequestEvent;
import com.ep.ggs.API.plugin.Command;
import com.ep.ggs.API.plugin.CommandLoadEvent;
import com.ep.ggs.API.plugin.Plugin;
import com.ep.ggs.banhandler.BanHandler;
import com.ep.ggs.chat.ChatColor;
import com.ep.ggs.copy.Copy;
import com.ep.ggs.copy.Paste;
import com.ep.ggs.globalchat.GlobalChatPlugin;
import com.ep.ggs.groupmanager.main.GroupPlugin;
import com.ep.ggs.irc.IRCPlugin;
import com.ep.ggs.mb.MessageBlockPlugin;
import com.ep.ggs.plugin.commands.Afk;
import com.ep.ggs.plugin.commands.Ban;
import com.ep.ggs.plugin.commands.Color;
import com.ep.ggs.plugin.commands.Cuboid;
import com.ep.ggs.plugin.commands.Give;
import com.ep.ggs.plugin.commands.Goto;
import com.ep.ggs.plugin.commands.Kick;
import com.ep.ggs.plugin.commands.Load;
import com.ep.ggs.plugin.commands.Loaded;
import com.ep.ggs.plugin.commands.Maps;
import com.ep.ggs.plugin.commands.Me;
import com.ep.ggs.plugin.commands.Money;
import com.ep.ggs.plugin.commands.Mute;
import com.ep.ggs.plugin.commands.Muted;
import com.ep.ggs.plugin.commands.Newlvl;
import com.ep.ggs.plugin.commands.Nick;
import com.ep.ggs.plugin.commands.Pay;
import com.ep.ggs.plugin.commands.Place;
import com.ep.ggs.plugin.commands.Players;
import com.ep.ggs.plugin.commands.Replace;
import com.ep.ggs.plugin.commands.Replaceall;
import com.ep.ggs.plugin.commands.Rules;
import com.ep.ggs.plugin.commands.Save;
import com.ep.ggs.plugin.commands.Spawn;
import com.ep.ggs.plugin.commands.Staff;
import com.ep.ggs.plugin.commands.Stop;
import com.ep.ggs.plugin.commands.Summon;
import com.ep.ggs.plugin.commands.TColor;
import com.ep.ggs.plugin.commands.TP;
import com.ep.ggs.plugin.commands.Take;
import com.ep.ggs.plugin.commands.Title;
import com.ep.ggs.plugin.commands.Unban;
import com.ep.ggs.plugin.commands.Undo;
import com.ep.ggs.plugin.commands.Whisper;
import com.ep.ggs.server.Server;
import com.ep.ggs.system.updater.Updatable;
import com.ep.ggs.system.updater.UpdateType;
import com.ep.ggs.verification.AdminVerification;


public class Main extends Plugin implements Updatable, Listener {
    private static final String VERSION = "1.0.0";
    private static final String CONFIG_VERSION = "#VERSION.4";
    private ArrayList<String> load = new ArrayList<String>();
    private static final Command[] COMMANDS = new Command[] {
        new Afk(),
        new Ban(),
        new Color(),
        new Copy(),
        new Cuboid(),
        new Give(),
        new Goto(),
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
        new Paste(),
        new Pay(),
        new Place(),
        new Players(),
        new Rules(),
        new Replace(),
        new Replaceall(),
        new Save(),
        new Spawn(),
        new Staff(),
        new Stop(),
        new Summon(),
        new Take(),
        new TColor(),
        new Title(),
        new TP(),
        new Unban(),
        new Undo(),
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

    @Override
    public void onLoad(String[] arg0) {
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
        p = new AdminVerification(getServer());
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
    public String getCurrentVersion() {
        return VERSION;
    }

    @Override
    public String getDownloadPath() {
        return "plugins/Defaults.jar";
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

    @Override
    public String getInfoURL() {
        return "http://update.mcforge.net/VERSION_2/defaults/updatej";
    }

    @Override
    public String getWebsite() {
        return "http://www.mcforge.net";
    }
}

