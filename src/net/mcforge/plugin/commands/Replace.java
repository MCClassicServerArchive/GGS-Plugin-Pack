package net.mcforge.plugin.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.action.Action;
import net.mcforge.API.action.BlockChangeAction;
import net.mcforge.API.plugin.PlayerCommand;
import net.mcforge.chat.ChatColor;
import net.mcforge.iomodel.Player;
import net.mcforge.world.Block;
import net.mcforge.world.BlockUpdate;

public class Replace extends PlayerCommand {

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
        
        BlockChangeAction b = null;
        Action<BlockChangeAction> bb = new BlockChangeAction();
        bb.setPlayer(p);
        p.sendMessage("Place two blocks to determine the edges.");
        int x1, x2, y1, y2, z1, z2;
        try {
            b = bb.waitForResponse();
            if (newblock == null || newblock.name.equals("NULL"))
                newblock = b.getHolding();
            x1 = b.getX();
            y1 = b.getY();
            z1 = b.getZ();
            
            b = null;
            bb = new BlockChangeAction();
            bb.setPlayer(p);
            b = bb.waitForResponse();
            x2 = b.getX();
            y2 = b.getY();
            z2 = b.getZ();
            ArrayList<BlockUpdate> bu = new ArrayList<BlockUpdate>();
            p.sendMessage(ChatColor.Yellow + "Please wait..");
            for (int xx = Math.min(x1, x2); xx <= Math.max(x1, x2); xx++) {
                for (int yy = Math.min(y1, y2); yy <= Math.max(y1, y2); yy++) {
                    for (int zz = Math.min(z1, z2); zz <= Math.max(z1, z2); zz++) {
                        if (oldTypes.contains(p.getLevel().getTile(xx, yy, zz))) bu.add(new BlockUpdate(newblock, xx, yy, zz));
                    }
                }
            }
            p.sendMessage(bu.size() + " blocks.");
            Player.GlobalBlockChange(bu.toArray(new BlockUpdate[bu.size()]), p.getLevel(), p.getServer());
            bu.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getDefaultPermissionLevel() {
        return 50;
    }

    @Override
    public String getName() {
        return "replace";
    }

    @Override
    public String[] getShortcuts() {
        return new String[] { "r" };
    }

    @Override
    public void help(CommandExecutor arg0) {
        arg0.sendMessage("/replace [block,block2,...] [new] - replace block with new inside a selected cuboid");
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

}
