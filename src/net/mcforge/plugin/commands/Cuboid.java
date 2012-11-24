package net.mcforge.plugin.commands;

import java.util.ArrayList;
import java.util.Random;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.action.Action;
import net.mcforge.API.action.BlockChangeAction;
import net.mcforge.API.plugin.PlayerCommand;
import net.mcforge.iomodel.Player;
import net.mcforge.world.Block;
import net.mcforge.world.BlockUpdate;

public class Cuboid extends PlayerCommand {

    Block block;
    @Override
    public void execute(Player p, String[] arg1) {
        CuboidType ct = CuboidType.Normal;
        ArrayList<BlockUpdate> bu = new ArrayList<BlockUpdate>();
        if (arg1.length == 2) {
            block = Block.getBlock(arg1[0]);
            ct = CuboidType.parse(arg1[1]);
        }
        else if (arg1.length == 1) {
            if (Block.getBlock(arg1[0]).name.equals("NULL"))
                ct = CuboidType.parse(arg1[0]);
            else
                block = Block.getBlock(arg1[0]);
        }
        p.sendMessage("Using cuboid type: \"" + ct.getType() + "\"");
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
            
            if (ct == CuboidType.Normal) {
                for (int xx = Math.min(x1, x2); xx <= Math.max(x1, x2); xx++) {
                    for (int yy = Math.min(y1, y2); yy <= Math.max(y1, y2); yy++) {
                        for (int zz = Math.min(z1, z2); zz <= Math.max(z1, z2); zz++) {
                            if (p.getLevel().getTile(xx, yy, zz) != block) {
                                bu.add(new BlockUpdate(block, xx, yy, zz));
                                count++;
                            }
                        }
                    }
                }
            }
            
            else if (ct == CuboidType.Hollow) {
                for (int yy = Math.min(y1, y2); yy <= Math.max(y1, y2); yy++) {
                    for (int zz = Math.min(z1, z2); zz <= Math.max(z1, z2); zz++) {
                        if (p.getLevel().getTile(x1, yy, zz) != block) {
                            bu.add(new BlockUpdate(block, x1, yy, zz));
                            count++;
                        }
                        if (p.getLevel().getTile(x2, yy, zz) != block && x1 != x2) {
                            bu.add(new BlockUpdate(block, x2, yy, zz));
                            count++;
                        }
                    }
                }
                if (Math.abs(x1 - x2) >= 2) {
                    for (int xx = Math.min(x1, x2); xx <= Math.max(x1, x2); xx++) {
                        for (int zz = Math.min(z1, z2); zz <= Math.max(z1, z2); zz++) {
                            if (p.getLevel().getTile(xx, y1, zz) != block) {
                                bu.add(new BlockUpdate(block, xx, y1, zz));
                                count++;
                            }
                            if (p.getLevel().getTile(xx, y2, zz) != block && y1 != y2) {
                                bu.add(new BlockUpdate(block, xx, y2, zz));
                                count++;
                            }
                        }
                    }
                    if (Math.abs(y1 - y2) >= 2) {
                        for (int xx = Math.min(x1, x2); xx <= Math.max(x1, x2); xx++) {
                            for (int yy = Math.min(y1, y2); yy <= Math.max(y1, y2); yy++) {
                                if (p.getLevel().getTile(xx, yy, z1) != block) {
                                    bu.add(new BlockUpdate(block, xx, yy, z1));
                                    count++;
                                }
                                if (p.getLevel().getTile(xx, yy, z2) != block && z1 != z2) {
                                    bu.add(new BlockUpdate(block, xx, yy, z2));
                                    count++;
                                } 
                            }
                        }
                    }
                }
            }
            
            else if (ct == CuboidType.Walls) {
                for (int yy = Math.min(y1, y2); yy <= Math.max(y1, y2); yy++) {
                    for (int zz = Math.min(z1, z2); zz <= Math.max(z1, z2); zz++) {
                        if (p.getLevel().getTile(x1, yy, zz) != block) {
                            bu.add(new BlockUpdate(block, x1, yy, zz));
                            count++;
                        }
                        if (p.getLevel().getTile(x2, yy, zz) != block && x1 != x2) {
                            bu.add(new BlockUpdate(block, x2, yy, zz));
                            count++;
                        }
                    }
                }
                if (Math.abs(x1 - x2) >= 2 && Math.abs(z1 - z2) >= 2) {
                    for (int xx = Math.min(x1, x2); xx <= Math.max(x1, x2); xx++) {
                        for (int yy = Math.min(y1, y2); yy <= Math.max(y1, y2); yy++) {
                            if (p.getLevel().getTile(xx, yy, z1) != block) {
                                bu.add(new BlockUpdate(block, xx, yy, z1));
                                count++;
                            }
                            if (p.getLevel().getTile(xx, yy, z2) != block && z1 != z2) {
                                bu.add(new BlockUpdate(block, xx, yy, z2));
                                count++;
                            } 
                        }
                    }
                }
            }
            
            else if (ct == CuboidType.Holes) {
                boolean checked = true, startZ, startY;
                for (int xx = Math.min(x1, x2); xx <= Math.max(x1, x2); xx++) {
                    startY = checked;
                    for (int yy = Math.min(y1, y2); yy <= Math.max(y1, y2); yy++) {
                        startZ = checked;
                        for (int zz = Math.min(z1, z2); zz <= Math.max(z1, z2); zz++) {
                            checked = !checked;
                            if (checked && p.getLevel().getTile(xx, yy, zz) != block) {
                                bu.add(new BlockUpdate(block, xx, yy, zz));
                                count++;
                            }
                        }
                        checked = !startZ;
                    }
                    checked = !startY;
                }
            }
            
            else if (ct == CuboidType.Wire) {
                for (int xx = Math.min(x1, x2); xx <= Math.max(x1, x2); xx++) {
                    bu.add(new BlockUpdate(block, xx, y1, z1));
                    bu.add(new BlockUpdate(block, xx, y1, z2));
                    bu.add(new BlockUpdate(block, xx, y2, z1));
                    bu.add(new BlockUpdate(block, xx, y2, z2));
                }
                for (int yy = Math.min(y1, y2); yy <= Math.max(y1, y2); yy++) {
                    bu.add(new BlockUpdate(block, x2, yy, z1));
                    bu.add(new BlockUpdate(block, x2, yy, z2));
                    bu.add(new BlockUpdate(block, x1, yy, z1));
                    bu.add(new BlockUpdate(block, x1, yy, z2));
                }
                for (int zz = Math.min(z1, z2); zz <= Math.max(z1, z2); zz++) {
                    bu.add(new BlockUpdate(block, x2, y1, zz));
                    bu.add(new BlockUpdate(block, x1, y1, zz));
                    bu.add(new BlockUpdate(block, x2, y2, zz));
                    bu.add(new BlockUpdate(block, x1, y2, zz));
                }
            }
            else if (ct == CuboidType.Random) {
                final Random rand = new Random();
                for (int xx = Math.min(x1, x2); xx <= Math.max(x1, x2); xx++) {
                    for (int yy = Math.min(y1, y2); yy <= Math.max(y1, y2); yy++) {
                        for (int zz = Math.min(z1, z2); zz <= Math.max(z1, z2); zz++) {
                            if (p.getLevel().getTile(xx, yy, zz) != block && rand.nextBoolean()) {
                                bu.add(new BlockUpdate(block, xx, yy, zz));
                                count++;
                            }
                        }
                    }
                }
            }
            Player.GlobalBlockChange(bu.toArray(new BlockUpdate[bu.size()]), p.getLevel(), p.getServer());
            bu.clear();
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
        arg0.sendMessage("/z [type: solid/hollow/walls/holes/wire/random] - Cuboid the block your holding using type [type]");
        arg0.sendMessage("/z [type: solid/hollow/walls/holes/wire/random] [block] - Cuboid the block [block] using type [type]");
    }

    @Override
    public boolean isOpCommandDefault() {
        return false;
    }
    
    public enum CuboidType {
        Normal("solid"),
        Hollow("hollow"),
        Walls("walls"),
        Holes("holes"),
        Wire("wire"),
        Random("random");
        
        String type;
        CuboidType(String type) { this.type = type; }
        
        public String getType() {
            return type;
        }
        
        public static CuboidType parse(String type) {
            type = type.toLowerCase();
            if (type.equals("solid"))
                return Normal;
            else if (type.equals("hollow"))
                return Hollow;
            else if (type.equals("walls"))
                return Walls;
            else if (type.equals("holes"))
                return Holes;
            else if (type.equals("wire"))
                return Wire;
            else if (type.equals("random"))
                return Random;
            return Normal;
        }
    }
}
