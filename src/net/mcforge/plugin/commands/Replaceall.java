package net.mcforge.plugin.commands;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.plugin.PlayerCommand;
import net.mcforge.iomodel.Player;
import net.mcforge.world.Block;

public class Replaceall extends PlayerCommand {

    @Override
    public void execute(Player p, String[] args) {
        if (args.length != 2) {
            help(p);
            return;
        }
        int count = 0;
        Block replace = Block.getBlock(args[0]);
        Block replacement = Block.getBlock(args[1]);
        for (int i = 0; i < p.getLevel().getLength(); i++) {
            if (p.getLevel().blocks[i].equals(replace)) {
                p.getLevel().setTile(replacement, i, p.getServer(), true);
                count++;
            }
        }
        
    }

    @Override
    public int getDefaultPermissionLevel() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String[] getShortcuts() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void help(CommandExecutor arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isOpCommandDefault() {
        // TODO Auto-generated method stub
        return false;
    }

}
