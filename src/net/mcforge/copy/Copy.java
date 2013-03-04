package net.mcforge.copy;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.action.Action;
import net.mcforge.API.action.BlockChangeAction;
import net.mcforge.API.plugin.PlayerCommand;
import net.mcforge.chat.ChatColor;
import net.mcforge.iomodel.Player;
import net.mcforge.world.blocks.BlockUpdate;
import net.mcforge.world.blocks.classicmodel.ClassicBlock;

public class Copy extends PlayerCommand {

    @Override
    public void execute(Player player, String[] args) {
        boolean cut = false;
        boolean includeair = false;
        List<String> ignore_list = null;
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("save") && args.length > 1) {
                CopyPasteService.saveData(player, args[1]);
                return;
            }
            else if (args[0].equalsIgnoreCase("load") && args.length > 1) {
                CopyPasteService.loadData(player, args[1]);
                return;
            }
            else if (args[0].equalsIgnoreCase("delete") && args.length > 1) {
                String file = args[1];
                if (!new File("system/copy_data/" + file + ".copy").exists()) {
                    player.sendMessage(ChatColor.Dark_Red + "File does not exist!");
                    return;
                }
                new File("system/copy_data/" + file + ".copy").delete();
                player.sendMessage(ChatColor.Red + "- " + player.getServer().defaultColor + "File deleted.");
                return;
            }
            else if (args[0].equalsIgnoreCase("list")) {
                File copyFolder = new File("system/copy_data/");
                File[] copyFiles = copyFolder.listFiles();
                for (File f : copyFiles) {
                    player.sendMessage(f.getName().split("\\.")[0]);
                }
                return;
            }
            else if (args[0].equalsIgnoreCase("clear")) {
                CopyPasteService.clearData(player);
                player.sendMessage(ChatColor.Red + "- " + player.getServer().defaultColor + "Clipboard cleared.");
                return;
            }
            for (int i = 0; i < args.length; i++) {
                String s = args[i];
                if (s.equalsIgnoreCase("cut"))
                    cut = true;
                else if (s.equalsIgnoreCase("air"))
                    includeair = true;
                else if (s.equalsIgnoreCase("ignore")) {
                    if (i + 1 < args.length) {
                        String blocks = args[i + 1];
                        ignore_list = Arrays.asList(blocks.split("\\,"));
                    }
                }
            }
        }
        
        ArrayList<ClassicBlock> ignore = new ArrayList<ClassicBlock>();
        if (ignore_list != null) {
            for (String b : ignore_list) {
                if (ClassicBlock.getBlock(b) != null)
                    ignore.add(ClassicBlock.getBlock(b));
            }
        }
        
        //Copy
        BlockChangeAction b = null;
        Action<BlockChangeAction> bb = new BlockChangeAction();
        bb.setPlayer(player);
        player.sendMessage("Place two blocks to determine the edges.");
        int x1, x2, y1, y2, z1, z2;
        try {
            b = bb.waitForResponse();
            x1 = b.getX();
            y1 = b.getY();
            z1 = b.getZ();
            
            b = null;
            bb = new BlockChangeAction();
            bb.setPlayer(player);
            b = bb.waitForResponse();
            x2 = b.getX();
            y2 = b.getY();
            z2 = b.getZ();
            b = null;
            ArrayList<BlockUpdate> bu = new ArrayList<BlockUpdate>();
            player.sendMessage(ChatColor.Yellow + "Please wait..");
            for (int xx = Math.min(x1, x2); xx <= Math.max(x1, x2); xx++) {
                for (int yy = Math.min(y1, y2); yy <= Math.max(y1, y2); yy++) {
                    for (int zz = Math.min(z1, z2); zz <= Math.max(z1, z2); zz++) {
                        if (player.getLevel().getTile(xx, yy, zz).getVisibleBlock() == 0 && !includeair)
                            continue;
                        if (!hasType(ignore, (ClassicBlock)player.getLevel().getTile(xx, yy, zz))) { 
                            bu.add(new BlockUpdate((ClassicBlock)player.getLevel().getTile(xx, yy, zz), (xx - x1), (yy - y1), (zz - z1)));
                            if (cut)
                                Player.GlobalBlockChange((short)xx, (short)yy, (short)zz, ClassicBlock.getBlock("air"), player.getLevel(), player.getServer());
                        }
                    }
                }
            }
            player.sendMessage(bu.size() + " blocks.");
            CopyPasteService.addCopyData(player, bu.toArray(new BlockUpdate[bu.size()]));
            bu.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private boolean hasType(ArrayList<ClassicBlock> array, ClassicBlock block) {
        for (ClassicBlock b : array) {
            if (block.ID == b.ID && block.name.equals(b.name))
                return true;
        }
        return false;
    }
    
    @Override
    public boolean runInSeperateThread() {
        return true;
    }

    @Override
    public String[] getShortcuts() {
        return new String[] { "c" };
    }

    @Override
    public String getName() {
        return "copy";
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
    public void help(CommandExecutor p) {
        p.sendMessage("/copy - Copies the blocks in an area.");
        p.sendMessage("/copy save <save_name> - Saves what you have copied.");
        p.sendMessage("/copy load <load_name> - Loads what you have saved.");
        p.sendMessage("/copy delete <delete_name> - Deletes the specified copy.");
        p.sendMessage("/copy list - Lists all you have copied.");
        p.sendMessage("/copy cut - Copies the blocks in an area, then removes them.");
        p.sendMessage("/copy air - Copies the blocks in an area, including air.");
        p.sendMessage("/copy ignore <block1>,<block2>.. - Ignores <blocks> when copying");
        //p.sendMessage("/copy @ - @ toggle for all the above, gives you a third click after copying that determines where to paste from"); //TODO Add this feature somehow
    }

}
