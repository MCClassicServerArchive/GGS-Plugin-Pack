package net.mcforge.plugin.commands;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.ManualLoad;
import net.mcforge.API.plugin.Command;
import net.mcforge.chat.ChatColor;
import net.mcforge.iomodel.Player;
import net.mcforge.plugin.help.HelpItem;
@ManualLoad
public class Nick extends Command implements HelpItem {

    @Override
    public void execute(CommandExecutor ce, String[] args) {
        Player who = null;
        String nick = "";
        if (args.length == 0) { help(ce); return; }
        if (!(ce instanceof Player)) {
            if ((who = ce.getServer().findPlayer(args[0])) == null) {
                ce.sendMessage("Player not found..");
                return;
            }
            if (args[1].equals("reset")) {
                who.resetCustomNick();
                ce.sendMessage(who.getName() + " nick was reset.");
                return;
            }
            for (int i = 0; i < args.length; i++) {
                nick += args[i] + " ";
            }
            nick = nick.trim();
            who.setCustomNick(nick);
            ce.getServer().sendGlobalMessage(ce.getName() + ChatColor.White + " set " + who.getName() + "'s nick to " + who.getCustomName());
        }
        else {
            Player p = (Player)ce;
            who = p;
            int startindex = 0;
            if (p.getServer().findPlayer(args[0]) != null) {
                who = p.getServer().findPlayer(args[0]);
                startindex++;
            }
            if (who == p && args[0].equals("reset")) {
                p.resetCustomNick();
                p.sendMessage("Your nick was reset!");
                return;
            }
            if (who != p && args[1].equals("reset")) {
                who.resetCustomNick();
                p.sendMessage(who.getName() + "'s name was reset.");
                return;
            }
            for (int i = startindex; i < args.length; i++) {
                nick += args[i] + " ";
            }
            nick = nick.trim();
            who.setCustomNick(nick);
            if (who != p)
                p.getServer().sendGlobalMessage(p.getDisplayName() + ChatColor.White + " set " + who.getName() + "'s nick to " + who.getCustomName());
            else
                p.getServer().sendGlobalMessage(p.getName() + " set their nick to " + p.getCustomName());
        }
    }

    @Override
    public int getDefaultPermissionLevel() {
        return 50;
    }

    @Override
    public String getName() {
        return "nick";
    }

    @Override
    public String[] getShortcuts() {
        return new String[0];
    }

    @Override
    public void help(CommandExecutor ce) {
        if (ce instanceof Player) {
            ce.sendMessage("/nick <nick> - Give yourself a new name! This name will replace your normal username.");
            ce.sendMessage("/nick reset - Reset your nick.");
        }
        ce.sendMessage("/nick [player] <nick> - Give [player] a new name! This name will replace [player]'s normal username");
        ce.sendMessage("/nick [player] reset - Reset [player]'s name.");
    }

    @Override
    public boolean isOpCommandDefault() {
        return false;
    }

    @Override
    public String getType() {
        return "personalization";
    }

}
