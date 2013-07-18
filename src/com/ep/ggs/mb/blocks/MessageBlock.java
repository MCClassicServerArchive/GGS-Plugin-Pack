/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.mb.blocks;

import com.ep.ggs.world.blocks.classicmodel.ClassicBlock;

public class MessageBlock extends ClassicBlock {
	
	private static final long serialVersionUID = 1808391182561161713L;
	private String message;
	private boolean canWalk;
	public MessageBlock() { super(); }
	
	public MessageBlock(byte ID, String name) {
		super(ID, name);
	}
	
	public MessageBlock(String message, ClassicBlock placed) {
		this(placed.getVisibleBlock(), placed.name);
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

