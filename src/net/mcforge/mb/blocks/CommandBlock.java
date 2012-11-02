package net.mcforge.mb.blocks;

import net.mcforge.API.plugin.Command;
import net.mcforge.server.Server;
import net.mcforge.world.Block;

public class CommandBlock extends Block {
	
	private static final long serialVersionUID = 1808391182561161713L;
	private String message;
	public CommandBlock(byte ID, String name) {
		super(ID, name);
	}
	public CommandBlock(String message, Block placed) {
		this(placed.getVisableBlock(), placed.name);
		this.message = message;
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

}

