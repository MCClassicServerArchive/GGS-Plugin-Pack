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
public class Color extends Command implements HelpItem {

	@Override
	public String getName() {
		return "color";
	}

	@Override
	public String[] getShortcuts() {
		return new String[0];
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
			executor.sendMessage("Please specify a color!");
			Main.displayValidColors(executor);
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
		who.setDisplayColor(parsedColor);
		s.sendGlobalMessage(who.getDisplayName() + s.defaultColor + " got the color " + parsedColor + color);
	}
	
	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/color <player> <color> - changes the player's color");
		if (executor instanceof Player)
			executor.sendMessage("/color <color> - changes your color");
		Main.displayValidColors(executor);
	}

    @Override
    public String getType() {
        return "personalization";
    }
}
