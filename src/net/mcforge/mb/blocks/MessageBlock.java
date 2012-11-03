package net.mcforge.mb.blocks;

import net.mcforge.world.Block;

public class MessageBlock extends Block{
	
	private static final long serialVersionUID = 1808391182561161713L;
	private String message;
	private boolean canWalk;
	public MessageBlock(byte ID, String name) {
		super(ID, name);
	}
	public MessageBlock(String message, Block placed) {
		this(placed.getVisableBlock(), placed.name);
		this.message = message;
		this.canWalk = placed.canWalkThrough();
	}
	
	public String getMessage() {
		return message;
	}
	
	@Override
	public boolean canWalkThrough() {
		return canWalk;
	}

}

