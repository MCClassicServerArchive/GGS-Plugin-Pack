/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.plugin;
import net.mcforge.API.ManualLoad;
import net.mcforge.API.plugin.Plugin;
import net.mcforge.server.Server;

@ManualLoad
public class ExamplePlugin extends Plugin {

	public ExamplePlugin(Server server) {
		super(server);
	}

	@Override
	public void onLoad(String[] arg0) {
		
	}

	@Override
	public void onUnload() {
		
	}

}

