package com.ep.ggs.plugin.commands;

import com.ep.ggs.API.CommandExecutor;
import com.ep.ggs.API.ManualLoad;
import com.ep.ggs.API.help.HelpItem;
import com.ep.ggs.API.plugin.Command;
import com.ep.ggs.chat.ChatColor;
import com.ep.ggs.iomodel.Player;
import com.ep.ggs.plugin.Main.Main;
import com.ep.ggs.server.Server;


@ManualLoad
public class TColor extends Command implements HelpItem {

	@Override
	public String getName() {
		return "tcolor";
	}

	@Override
	public String[] getShortcuts() { 
		return new String[] { "titlecolor", "tcolour", "titlecolour" };
	}

	@Override
	public int getDefaultPermissionLevel() {
		return 0;
	}

	@Override
	public boolean isOpCommandDefault() {
		return false;
	}

	@Override
	public void execute(CommandExecutor executor, String[] args) {
		if (args.length == 0) {
			executor.sendMessage("Please specify a tcolor!");
			return;
		}
		Server s = executor.getServer();
		Player who;
		if (args.length == 1) {
			if (executor instanceof Player) {
				who = (Player)executor;
			}
			else {
				executor.sendMessage("You need to specify a player!");
				return;
			}
		}
		else {
			who = s.findPlayer(args[0]);
			if (who == null) {
				executor.sendMessage("Player not found!");
				return;
			}
		}
		String color = args.length == 1 ? args[0] : args[1];
		ChatColor parsedColor = ChatColor.fromName(color);
		
		if (parsedColor == null) {
			executor.sendMessage("Invalid color!");
			Main.displayValidColors(executor);
			return;
		}
		
		String prefix = who.getPrefix();
		if (prefix == null) {
			prefix = parsedColor.toString();
		}
		else {
			if (prefix.startsWith("["))
				prefix = prefix.substring(1);
			else if (prefix.startsWith("&") && prefix.charAt(2) == '[')
				prefix = prefix.substring(0, 2) + prefix.substring(3, prefix.length());
			
			if (prefix.endsWith("] "))
				prefix = prefix.substring(0, prefix.length() - 2);
			
			if (!prefix.startsWith("&") || prefix.length() == 1) {
				if (prefix.charAt(prefix.length() - 2) == '&') {
					prefix = prefix.substring(0, prefix.length() - 2);
				}
				prefix = parsedColor + "[" + prefix + parsedColor + "] ";
			}
			else {
				prefix = prefix.substring(2);
				if (prefix.charAt(prefix.length() - 2) == '&') {
					prefix = prefix.substring(0, prefix.length() - 2);
				}
				prefix = parsedColor + "[" + prefix + parsedColor + "] ";
			}
		}
		who.setRawPrefix(prefix);
		s.sendGlobalMessage(who.getDisplayName() + s.defaultColor + " got the tcolor " + parsedColor + color);
	}
	
	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/tcolor <player> <color> - changes the player's title color");
		if (executor instanceof Player)
			executor.sendMessage("/tcolor <color> - changes your title color");
		Main.displayValidColors(executor);
	}

    @Override
    public String getType() {
        return "personalization";
    }
}
