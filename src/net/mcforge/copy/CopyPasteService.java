package net.mcforge.copy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import net.mcforge.chat.ChatColor;
import net.mcforge.iomodel.Player;
import net.mcforge.system.Serializer;
import net.mcforge.system.Serializer.SaveType;
import net.mcforge.world.blocks.BlockUpdate;

public class CopyPasteService {
    
    private static HashMap<Player, BlockUpdate[]> copy_cache = new HashMap<Player, BlockUpdate[]>();
    private static final Serializer<BlockUpdate[]> SAVER = new Serializer<BlockUpdate[]>(SaveType.GZIP_JAVA);
    
    public static void addCopyData(Player p, BlockUpdate[] data) {
        if (copy_cache.containsKey(p))
            copy_cache.remove(p);
        copy_cache.put(p, data);
    }
    
    public static BlockUpdate[] getData(Player p) {
        if (!copy_cache.containsKey(p))
            return null;
        return copy_cache.get(p);
    }
    
    public static boolean hasData(Player p) {
        return copy_cache.containsKey(p);
    }
    
    public static void saveData(Player p, String filename) {
        BlockUpdate[] u = getData(p);
        if (u == null)
            return;
        if (!new File("system/copy_data").exists())
            new File("system/copy_data").mkdir();
        try {
            FileOutputStream fos = new FileOutputStream("system/copy_data/" + filename + ".copy");
            SAVER.saveObject(u, fos);
            fos.close();
            p.sendMessage(ChatColor.Bright_Green + "+ " + p.getServer().defaultColor + "Clipboard saved to " + filename + "!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            p.sendMessage(ChatColor.Dark_Red + "Error saving clipboard!");
        } catch (IOException e) {
            e.printStackTrace();
            p.sendMessage(ChatColor.Dark_Red + "Error saving clipboard!");
        }
    }
    
    public static void loadData(Player p, String filename) {
        if (!new File("system/copy_data").exists()) {
            new File("system/copy_data").mkdir();
            p.sendMessage(ChatColor.Dark_Red + "File does not exist!");
            return;
        }
        if (!new File("system/copy_data/" + filename + ".copy").exists()) {
            p.sendMessage(ChatColor.Dark_Red + "File does not exist!");
            return;
        }
        try {
            FileInputStream in = new FileInputStream("system/copy_data/" + filename + ".copy");
            BlockUpdate[] data = SAVER.getObject(in);
            addCopyData(p, data);
            in.close();
            p.sendMessage(ChatColor.Bright_Green + "+ " + p.getServer().defaultColor + "Clipboard loaded from " + filename + "!");
        } catch (IOException e) {
            e.printStackTrace();
            p.sendMessage(ChatColor.Dark_Red + "Error saving clipboard!");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            p.sendMessage(ChatColor.Dark_Red + "Error saving clipboard!");
        }
    }

}
