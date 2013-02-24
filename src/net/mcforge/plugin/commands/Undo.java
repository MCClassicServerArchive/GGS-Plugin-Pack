package net.mcforge.plugin.commands;

import java.util.List;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.plugin.Command;
import net.mcforge.groups.Group;
import net.mcforge.groups.exceptions.GroupAttributeNotFoundException;
import net.mcforge.iomodel.Player;
import net.mcforge.system.Console;
import net.mcforge.world.Level;
import net.mcforge.world.blocks.tracking.BlockData;

public class Undo extends Command {

    @Override
    public String[] getShortcuts() {
        return new String[] { "u" };
    }

    @Override
    public String getName() {
        return "undo";
    }

    @Override
    public boolean isOpCommandDefault() {
        return false;
    }

    @Override
    public int getDefaultPermissionLevel() {
        return 0;
    }

    //Format
    // /undo <player> <time>
    // /undo <player> all
    // /undo <player>
    // /undo <time>
    // /undo all
    // /undo
    @Override
    public void execute(CommandExecutor player, String[] args) {
        int lowestperm = 0;
        if (!player.getGroup().hasAttribute("maxUndo")) {
            player.getGroup().setAttribute("maxUndo", "30");
            Group.saveGroups();
        }
        else {
            try {
                Integer.parseInt(player.getGroup().getAttribute("maxUndo"));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (GroupAttributeNotFoundException e) {
                player.getGroup().setAttribute("maxUndo", "30");
                Group.saveGroups();
            }
        }
        Player p;
        Player who = null;
        String name = "";
        int seconds = 30;
        if (args.length == 0 && player instanceof Player) {
            who = (Player)player;
            name = who.getName();
        }
        else if (args.length == 0 && !(player instanceof Player)) {
            player.sendMessage("Console has no undo data!");
            return;
        }
        else if (args.length > 0) {
            if (args.length == 1) {
                try {
                    seconds = Integer.parseInt(args[0]);
                    if (player instanceof Player)
                        who = (Player)player;
                    else {
                        player.sendMessage("Console has no undo data!");
                        return;
                    }
                    name = who.getName();
                } catch (Exception e) {
                    name = args[0];
                    if (name.equals("all")) { // /undo all
                        seconds = Integer.MAX_VALUE - 1;
                        if (player instanceof Player)
                            who = (Player)player;
                        else {
                            player.sendMessage("Console has no undo data!");
                            return;
                        }
                        name = who.getName();
                    }
                    else {
                        who = player.getServer().findPlayer(name);
                        seconds = 30;
                    }
                }
            }
            else {
                name = args[0];
                try {
                    seconds = Integer.parseInt(args[1]);
                } catch (Exception e) {
                    if (args[1].equals("all"))
                        seconds = Integer.MAX_VALUE - 1;
                    else
                        player.sendMessage("Invalid value, using 30 seconds.");
                }
                who = player.getServer().findPlayer(name);
            }
        }
        
        if (who != null) {
            if (player instanceof Player) {
                p = (Player)player;
                if (who.getGroup().permissionlevel > p.getGroup().permissionlevel && who != p) { p.sendMessage("Cannot undo a user of higher or equal rank"); return; }
                if (who != p && p.getGroup().permissionlevel < lowestperm) { p.sendMessage("You are not a high enough rank to undo other peoples action!"); return; }
            }
            player.sendMessage("Undoing " + seconds + " seconds");
            List<BlockData> data = who.getServer().getBlockTracker().getHistory(who, seconds);
            player.sendMessage("Which is " + data.size() + " block changes.");
            Level foundLevel;
            for (BlockData d : data) {
                if ((foundLevel = who.getServer().getLevelHandler().findLevel(d.getLevel())) != null) {
                    Player.GlobalBlockChange((short)d.getX(), (short)d.getY(), (short)d.getZ(), d.getBlock(), foundLevel, who.getServer(), true);
                }
            }
        }
        else {
            if (player instanceof Player) {
                p = (Player)player;
                if (p.getGroup().permissionlevel < lowestperm) { p.sendMessage("You are not a high enough rank to undo other peoples action!"); return; }
            }
            
            player.sendMessage("Undoing " + seconds + " seconds for " + name + ".");
            List<BlockData> data = player.getServer().getBlockTracker().getOfflineHistory(name, seconds);
            player.sendMessage("Which is " + data.size() + " block changes.");
            Level foundLevel;
            for (BlockData d : data) {
                if ((foundLevel = player.getServer().getLevelHandler().findLevel(d.getLevel())) != null) {
                    Player.GlobalBlockChange((short)d.getX(), (short)d.getY(), (short)d.getZ(), d.getBlock(), foundLevel, player.getServer(), true);
                }
            }
        }
    }

    @Override
    public void help(CommandExecutor executor) {
        int maxundo = 30;
        if (!executor.getGroup().hasAttribute("maxUndo")) {
            executor.getGroup().setAttribute("maxUndo", "30");
            Group.saveGroups();
        }
        else {
            try {
                maxundo = Integer.parseInt(executor.getGroup().getAttribute("maxUndo"));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (GroupAttributeNotFoundException e) {
                executor.getGroup().setAttribute("maxUndo", "30");
                Group.saveGroups();
            }
        }
        
        
        executor.sendMessage("/undo [player] [seconds] - Undoes the blockchanges made by [player] in the previous [seconds].");
        if (executor instanceof Console || (maxundo <= 500000 || maxundo == 0))
            executor.sendMessage("/undo [player] all - &cWill undo 68 years, 18 days, 15 hours, 28 minutes, 31 seconds for [player]");
        if (executor instanceof Console || (maxundo <= 1800 || maxundo == 0))
            executor.sendMessage("/undo [player] 0 - &cWill undo 30 minutes");
    }
}