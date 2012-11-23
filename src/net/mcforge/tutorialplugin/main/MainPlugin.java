/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
/*
* Plugin shows last edited block if using wom
*/
package net.mcforge.tutorialplugin.main;

import java.util.Properties;

import net.mcforge.API.ManualLoad;
import net.mcforge.API.plugin.Plugin;
import net.mcforge.server.Server;

@ManualLoad
public class MainPlugin extends Plugin {

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

