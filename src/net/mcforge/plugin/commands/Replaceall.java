package net.mcforge.plugin.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.ManualLoad;
import net.mcforge.API.plugin.PlayerCommand;
import net.mcforge.chat.ChatColor;
import net.mcforge.iomodel.Player;
import net.mcforge.plugin.help.HelpItem;
import net.mcforge.world.blocks.Block;
import net.mcforge.world.blocks.BlockUpdate;
@ManualLoad
public class Replaceall extends PlayerCommand implements HelpItem {

    @Override
    public void execute(Player p, String[] args) {
        if (args.length != 2) {
            p.sendMessage("Invalid number of arguments!");
            help(p);
            return;
        }
        List<String> temp = Arrays.asList(args[0].split("\\,"));
        ArrayList<Block> oldTypes = new ArrayList<Block>();
        Block block;
        for (String blockname : temp) {
            if ((block = Block.getBlock(blockname)) == null) {
                p.sendMessage("Invalid block " + blockname);
                continue;
            }
            if (!oldTypes.contains(block))
                oldTypes.add(block);
            block = null;
        }

        Block newblock = Block.getBlock(args[1]);
        if (newblock.name.equals("NULL")) {
            p.sendMessage(ChatColor.Dark_Red + "Block not found: " + args[1]);
            return;
        }
        ArrayList<BlockUpdate> bu = new ArrayList<BlockUpdate>();
        p.sendMessage(ChatColor.Yellow + "Please wait..");
        for (int xx = 0; xx <= p.getLevel().getWidth(); xx++) {
            for (int yy = 0; yy <= p.getLevel().getHeight(); yy++) {
                for (int zz = 0; zz <= p.getLevel().getDepth(); zz++) {
                    if (oldTypes.contains(p.getLevel().getTile(xx, yy, zz))) bu.add(new BlockUpdate(newblock, xx, yy, zz));
                }
            }
        }
        p.sendMessage(bu.size() + " blocks.");
        Player.GlobalBlockChange(bu.toArray(new BlockUpdate[bu.size()]), p.getLevel(), p.getServer());
        bu.clear();
    }

    @Override
    public int getDefaultPermissionLevel() {
        return 50;
    }

    @Override
    public String getName() {
        return "replaceall";
    }

    @Override
    public String[] getShortcuts() {
        return new String[] { "ra" };
    }

    @Override
    public void help(CommandExecutor arg0) {
        arg0.sendMessage("/replaceall [block,block2,...] [new] - Replaces all of [block] with [new] in a map");
        arg0.sendMessage("If more than one block is specified, they will all be replaced.");
    }

    @Override
    public boolean isOpCommandDefault() {
        return false;
    }


    @Override
    public boolean runInSeperateThread() {
        return true;
    }

    @Override
    public String getType() {
        return "build";
    }

}
