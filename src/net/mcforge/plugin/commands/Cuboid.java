package net.mcforge.plugin.commands;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.action.Action;
import net.mcforge.API.action.BlockChangeAction;
import net.mcforge.API.plugin.PlayerCommand;
import net.mcforge.iomodel.Player;
import net.mcforge.world.Block;

public class Cuboid extends PlayerCommand {

    Block block;
    @Override
    public void execute(Player p, String[] arg1) {
        if (arg1.length != 0)
            block = Block.getBlock(arg1[0]);
        BlockChangeAction b = null;
        Action<BlockChangeAction> bb = new BlockChangeAction();
        bb.setPlayer(p);
        p.sendMessage("Place two blocks to determine the edges.");
        int x1, x2, y1, y2, z1, z2, count = 0;
        try {
            b = bb.waitForResponse();
            if (block == null || block.name.equals("NULL"))
                block = b.getHolding();
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
            
            for (int xx = Math.min(x1, x2); xx <= Math.max(x1, x2); xx++) {
                for (int yy = Math.min(y1, y2); yy <= Math.max(y1, y2); yy++) {
                    for (int zz = Math.min(z1, z2); zz <= Math.max(z1, z2); zz++) {
                        if (p.getLevel().getTile(xx, yy, zz) != block) {
                            Player.GlobalBlockChange((short)xx, (short)yy, (short)zz, block, p.getLevel(), p.getServer());
                            count++;
                        }
                    }
                }
            }
            
            p.sendMessage(count + " blocks.");
            block = null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean runInSeperateThread() {
        return true;
    }

    @Override
    public int getDefaultPermissionLevel() {
        return 50;
    }

    @Override
    public String getName() {
        return "cuboid";
    }

    @Override
    public String[] getShortcuts() {
        return new String[] { "z" };
    }

    @Override
    public void help(CommandExecutor arg0) {
        arg0.sendMessage("/z - Cuboid the block your holding");
        arg0.sendMessage("/z [block] - Cuboid the block [block]");
    }

    @Override
    public boolean isOpCommandDefault() {
        return false;
    }
}
