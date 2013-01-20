/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.mb.blocks;

import net.mcforge.API.plugin.Command;
import net.mcforge.server.Server;
import net.mcforge.world.blocks.Block;

public class CommandBlock extends Block {
	
	private static final long serialVersionUID = 1808391182561161713L;
	private String message;
	private boolean canWalk;
	public CommandBlock() { super(); }
	
	public CommandBlock(byte ID, String name) {
		super(ID, name);
	}
	
	public CommandBlock(String message, Block placed) {
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

