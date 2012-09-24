/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mcforge.groupmanager.main;

import java.util.Properties;
import net.mcforge.API.plugin.Plugin;
import net.mcforge.groupmanager.commands.CmdGroupManager;
import net.mcforge.iomodel.Player;
import net.mcforge.server.Server;

/**
 *
 * @author Wouter Gerarts
 */
public class MainPlugin extends Plugin {

    public static Server server;
    public MainPlugin(Server server)
    {
        super(server);
    }
    public MainPlugin(Server server, Properties properties)
    {
        super(server, properties);
    }
    @Override
    public void onLoad(String[] args) {
        server = getServer();
        getServer().getCommandHandler().addCommand(new CmdGroupManager());
    }

    @Override
    public void onUnload() {
        getServer().getCommandHandler().removeCommand("groupmanager");
    }
    @Override
    public String getName() {
        return "Group Manager";
    }
    @Override
    public String getVersion() {
        return "1.0.0";
    }
    @Override
    public String getAuthor() {
        return "Wouto1997";
    }
    
    public static Player Find(String username)
    {
        return server.findPlayer(username);
    }
}
