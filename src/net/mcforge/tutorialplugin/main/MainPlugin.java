/*
 * Plugin shows last edited block if using wom
 */
package net.mcforge.tutorialplugin.main;

import java.util.Properties;

import net.mcforge.API.ManualLoad;
import net.mcforge.API.plugin.Plugin;
import net.mcforge.server.Server;

/**
 *
 * @author Wouter Gerarts
 */
public class MainPlugin extends Plugin implements ManualLoad {

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
    }

    @Override
    public void onUnload() {
    }
    
    @Override
    public String getName()
    {
        return "XYZ-Inator";
    }
    
    @Override
    public String getVersion()
    {
        return "1.0.0";
    }
    
}
