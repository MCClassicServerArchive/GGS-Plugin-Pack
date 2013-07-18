package com.ep.ggs.plugin.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ep.ggs.API.CommandExecutor;
import com.ep.ggs.API.ManualLoad;
import com.ep.ggs.API.help.HelpItem;
import com.ep.ggs.API.plugin.PlayerCommand;
import com.ep.ggs.chat.ChatColor;
import com.ep.ggs.iomodel.Player;
import com.ep.ggs.world.blocks.BlockUpdate;
import com.ep.ggs.world.blocks.classicmodel.ClassicBlock;

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
        ArrayList<ClassicBlock> oldTypes = new ArrayList<ClassicBlock>();
        ClassicBlock block;
        for (String blockname : temp) {
            if ((block = ClassicBlock.getBlock(blockname)) == null) {
                p.sendMessage("Invalid block " + blockname);
                continue;
            }
            if (!oldTypes.contains(block))
                oldTypes.add(block);
            block = null;
        }

        ClassicBlock newblock = ClassicBlock.getBlock(args[1]);
        if (newblock.name.equals("NULL")) {
            p.sendMessage(ChatColor.Dark_Red + "Block not found: " + args[1]);
            return;
        }
        ArrayList<BlockUpdate> bu = new ArrayList<BlockUpdate>();
        p.sendMessage(ChatColor.Yellow + "Please wait..");
        for (int xx = 0; xx <= p.getLevel().getWidth(); xx++) {
            for (int yy = 0; yy <= p.getLevel().getHeight(); yy++) {
                for (int zz = 0; zz <= p.getLevel().getDepth(); zz++) {
                    if (hasType(oldTypes, (ClassicBlock)p.getLevel().getTile(xx, yy, zz))) bu.add(new BlockUpdate(newblock, xx, yy, zz));
                }
            }
        }
        p.sendMessage(bu.size() + " blocks.");
        Player.GlobalBlockChange(bu.toArray(new BlockUpdate[bu.size()]), p.getLevel(), p.getServer());
        bu.clear();
    }
    
    private boolean hasType(ArrayList<ClassicBlock> array, ClassicBlock block) {
        for (ClassicBlock b : array) {
            if (block.ID == b.ID && block.name.equals(b.name))
                return true;
        }
        return false;
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
