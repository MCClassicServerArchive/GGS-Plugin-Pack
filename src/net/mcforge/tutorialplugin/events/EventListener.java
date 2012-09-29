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
