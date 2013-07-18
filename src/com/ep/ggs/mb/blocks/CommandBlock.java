/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.mb.blocks;

import com.ep.ggs.API.plugin.Command;
import com.ep.ggs.server.Server;
import com.ep.ggs.world.blocks.classicmodel.ClassicBlock;

public class CommandBlock extends ClassicBlock {
	
	private static final long serialVersionUID = 1808391182561161713L;
	private String message;
	private boolean canWalk;
	public CommandBlock() { super(); }
	
	public CommandBlock(byte ID, String name) {
		super(ID, name);
	}
	
	public CommandBlock(String message, ClassicBlock placed) {
		this(placed.getVisibleBlock(), placed.name);
		this.message = message;
		this.canWalk = placed.canWalkThrough();
	}
	
	/**
	 * Get the full message without the / at the beginning
	 * @return
	 */
	public String getMessage() {
		return message.substring(1);
	}
	
	/**
	 * Get the command this commandblock holds
	 * @param server
	 *              The server with the commandhandler
	 * @return
	 *        The command
	 */
	public Command getCommand(Server server) {
		return server.getCommandHandler().find(getMessage().split("\\ ")[0]);
	}
	
	@Override
	public boolean canWalkThrough() {
		return canWalk;
	}

}

