package net.mcforge.mb.blocks;

import net.mcforge.world.Block;
import net.mcforge.world.Level;

public class PortalBlock extends Block {
	private static final long serialVersionUID = 1808391182561161713L;
	private POS pos;
	private boolean isExit;
	private boolean canWalk;
	public PortalBlock(byte ID, String name) {
		super(ID, name);
	}
	public PortalBlock(int x, int y, int z, Level l, Block placed) {
		this(placed.getVisableBlock(), placed.name);
		this.pos = new POS(x, y + 32, z, l);
		this.canWalk = placed.canWalkThrough();
	}
	
	public POS getDestination () {
		return pos;
	}
	
	public boolean isExit() {
		return isExit;
	}
	
	public void setExit(boolean exit) {
		this.isExit = exit;
	}
	
	@Override
	public boolean canWalkThrough() {
		return canWalk;
	}
	
	public class POS {
		private int x;
		private int y;
		private int z;
		private Level level;
		public POS(int x, int y, int z, Level level) { this.setLevel(level); this.setX(x); this.setY(y); this.setZ(z); }
		/**
		 * @return the x
		 */
		public int getX() {
			return x;
		}
		/**
		 * @param x the x to set
		 */
		public void setX(int x) {
			this.x = x;
		}
		/**
		 * @return the y
		 */
		public int getY() {
			return y;
		}
		/**
		 * @param y the y to set
		 */
		public void setY(int y) {
			this.y = y;
		}
		/**
		 * @return the z
		 */
		public int getZ() {
			return z;
		}
		/**
		 * @param z the z to set
		 */
		public void setZ(int z) {
			this.z = z;
		}
		/**
		 * @return the level
		 */
		public Level getLevel() {
			return level;
		}
		/**
		 * @param level the level to set
		 */
		public void setLevel(Level level) {
			this.level = level;
		}
	}
}
