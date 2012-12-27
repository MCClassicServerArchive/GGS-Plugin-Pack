package net.mcforge.plugin.commands;

import java.util.HashMap;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.EventHandler;
import net.mcforge.API.Listener;
import net.mcforge.API.ManualLoad;
import net.mcforge.API.Priority;
import net.mcforge.API.player.PlayerChatEvent;
import net.mcforge.API.plugin.Command;
import net.mcforge.iomodel.Player;
import net.mcforge.plugin.help.HelpItem;
import net.mcforge.server.Server;
@ManualLoad
public class Whisper extends Command implements Listener, HelpItem {

    private HashMap<CommandExecutor, Data> data = new HashMap<CommandExecutor, Data>();
    boolean init;
    
    @Override
    public String[] getShortcuts() {
        return new String[0];
    }

    @Override
    public String getName() {
        return "whisper";
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
    public void execute(CommandExecutor player, String[] args) {
        if (!init)
            init(player.getServer());
        if (!data.containsKey(player)) {
            Data d = new Data();
            d.ison = false;
            d.whisperto = "";
            data.put(player, d);
        }
        if (args.length == 0) {
            Data d = data.get(player);
            d.ison = !d.ison;
            if (d.ison) player.sendMessage("All messages sent will now auto-whisper");
            else player.sendMessage("Whisper chat turned off");
        }
        else {
            Data d = data.get(player);
            Player who = player.getServer().findPlayer(args[0]);
            if (who == null) {
                d.whisperto = "";
                d.ison = false;
                player.sendMessage("Could not find player.");
                return;
            }
            d.whisperto = who.getName();
            d.ison = true;
            player.sendMessage("Auto-whisper enabled. All messages will now be sent to " + who.getName() + ".");
        }
    }
    
    private void init(Server s) {
        if (!init) {
            s.getEventSystem().registerEvents(this);
            init = true;
        }
    }
    
    @EventHandler(priority = Priority.Low)
    public void onChat(PlayerChatEvent event) {
        if (data.containsKey(event.getPlayer())) {
            Data d = data.get(event.getPlayer());
            if (d.ison) {
                CommandExecutor to = null;
                if (d.whisperto.equals("") || (to = event.getPlayer().getServer().findPlayer(d.whisperto)) == null) {
                    event.getPlayer().sendMessage("The player you have auto-whisper set to is offline.");
                    event.getPlayer().sendMessage("Whisper chat turned off.");
                    d.ison = false;
                    d.whisperto = "";
                    event.setCancel(true);
                    return;
                }
                to.sendMessage(event.getOrginalMessage());
                event.setCancel(true);
                return;
            }
        }
    }

    @Override
    public void help(CommandExecutor executor) {
        executor.sendMessage("/whisper <name> - Make all messages act like whispers");
    }
    
    private class Data {
        boolean ison;
        String whisperto;
    }

    @Override
    public String getType() {
        return "chat";
    }

}
