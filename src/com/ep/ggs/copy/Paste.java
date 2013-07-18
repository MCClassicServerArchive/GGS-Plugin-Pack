package com.ep.ggs.copy;

import com.ep.ggs.API.CommandExecutor;
import com.ep.ggs.API.action.Action;
import com.ep.ggs.API.action.BlockChangeAction;
import com.ep.ggs.API.plugin.PlayerCommand;
import com.ep.ggs.chat.ChatColor;
import com.ep.ggs.iomodel.Player;
import com.ep.ggs.world.blocks.BlockUpdate;

public class Paste extends PlayerCommand {

    @Override
    public void execute(Player player, String[] args) {
        BlockUpdate[] data = CopyPasteService.getData(player);
        if (data == null) {
            player.sendMessage("No copy data stored..");
            return;
        }
        player.sendMessage("Place a block in the corner of where you want to paste.");
        BlockChangeAction b = null;
        Action<BlockChangeAction> bb = new BlockChangeAction();
        bb.setPlayer(player); 
        int x, y, z;
        try {
            b = bb.waitForResponse();
            x = b.getX();
            y = b.getY();
            z = b.getZ();
            b = null;
            
            player.sendMessage(ChatColor.Yellow + "Please Wait..");
            for (BlockUpdate update : data) {
                int nx = update.getX() + x;
                int ny = update.getY() + y;
                int nz = update.getZ() + z;
                update.setX(nx);
                update.setY(ny);
                update.setZ(nz);
            }
            Player.GlobalBlockChange(data, player.getLevel(), player.getServer());
            player.sendMessage("Pasted " + data.length + " blocks.");
        } catch (Exception e) { 
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean runInSeperateThread() {
        return true;
    }

    @Override
    public String[] getShortcuts() {
        return new String[] { "v" };
    }

    @Override
    public String getName() {
        return "paste";
    }

    @Override
    public boolean isOpCommandDefault() {
        return false;
    }

    @Override
    public int getDefaultPermissionLevel() {
        return 50;
    }

    @Override
    public void help(CommandExecutor executor) {
        executor.sendMessage("/paste - Pastes the stored copy.");
        executor.sendMessage("&4BEWARE: " + executor.getServer().defaultColor + "The blocks will always be pasted in a set direction");
    }

}
