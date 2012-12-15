/*******************************************************************************
* Copyright (c) 2012 MCForge.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the GNU Public License v3.0
* which accompanies this distribution, and is available at
* http://www.gnu.org/licenses/gpl.html
******************************************************************************/
package net.mcforge.plugin.commands;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.ManualLoad;
import net.mcforge.API.plugin.Command;
import net.mcforge.API.plugin.Plugin;
import net.mcforge.chat.Messages;


@ManualLoad
public class Plugins extends Command  {
	@Override
	public String[] getShortcuts() {
		return new String[0];
	}

	@Override
	public String getName() {
		return "plugins";
	}

	@Override
	public boolean isOpCommandDefault() {
		return false;
	}

	@Override
	public int getDefaultPermissionLevel() {
		return 0;
	}

	@Override
	public void execute(CommandExecutor executor, String[] args) {
		List<Plugin> plugins = executor.getServer().getPluginHandler().getLoadedPlugins();
		if (plugins.size() == 0) {
			executor.sendMessage("The server doesn't have any plugins loaded!");
			return;
		}
		executor.sendMessage(join("getName", ", ", (Object[])plugins.toArray(new Plugin[plugins.size()])));
	}

	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/pay <player> <amount> - pays the specified amount of " + 
	                         executor.getServer().CurrencyName + " to the specified player!");
	}
	/**
	 * Joins the specified Object array into a string array
	 * by calling the specified method to convert the objects to string
	 * using the specified separator <br>
	 * 
	 * For the following example assume you have a Server object by the name server <br>
	 * The example will show how to get a joined list of the server's players by their display names <br> <br>
	 * <code>
	 * Player[] players = server.players.toArray(new Player[server.players.size()]); <br>
	 * String namesJoined = join("getDisplayName", ", ", players);
	 * </code>
	 * 
	 * @param methodToConvert - The name of the method to use to convert the objects to string
	 * @param separator - The separator to use to separate the joined list
	 * @param objects - The array of objects to join
	 */
	private static String join(String methodToConvert, String separator, Object[] objects) {
		Method converterMethod;
		List<String> toJoin = new ArrayList<String>();
		try {
			Object obj = objects[0];
			converterMethod = obj.getClass().getMethod(methodToConvert, (Class<?>[])null);
		}
		catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			return null;
		}
		for (int i = 0; i < objects.length; i++) {
			Object returned;
			try {
				returned = converterMethod.invoke(objects[i], (Object[])null);
			}
			catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
				return null;
			}
			if (returned instanceof String)
				toJoin.add((String)returned);
		}
		return Messages.join(toJoin.toArray(new String[toJoin.size()]), separator);
	}
}

