/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package net.mcforge.tutorialplugin.events;

import net.mcforge.API.EventHandler;
import net.mcforge.API.Listener;
import net.mcforge.API.player.PlayerBlockChangeEvent;

/**
*
* @author Wouter Gerarts
*/
public class EventListener implements Listener {
	
	@EventHandler
	public void onPlayerBlockChange(PlayerBlockChangeEvent event)
	{
		event.getPlayer().sendWoMMessage(
				"x: " + event.getX() + " " +
				"y: " + event.getY() + " " +
				"z: " + event.getZ());
	}
}

