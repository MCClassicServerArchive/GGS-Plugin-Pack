/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mcforge.tutorialplugin.events;

import net.mcforge.API.Listener;
import net.mcforge.API.player.PlayerBlockChangeEvent;
import net.mcforge.API.player.PlayerConnectEvent;

/**
 *
 * @author Wouter Gerarts
 */
public class EventListener implements Listener {
    public void onPlayerBlockChange(PlayerBlockChangeEvent event)
    {
        event.getPlayer().sendWoMMesssage(
                "x: " + event.getX() + " " +
                "y: " + event.getY() + " " +
                "z: " + event.getZ());
    }
}
