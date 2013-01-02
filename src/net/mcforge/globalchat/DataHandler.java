package net.mcforge.globalchat;

import java.io.IOException;
import java.io.NotSerializableException;
import java.sql.SQLException;

import net.mcforge.iomodel.Player;

public abstract class DataHandler {
	protected final static String readRules = "gc_readrules";
	protected final static String agreed = "gc_agreed";
	protected final static String ignoring = "gc_ignoring";
	
	public static void preparePlayer(Player p) {
		if (!p.hasAttribute(readRules))
			p.setAttribute(readRules, false);
		if (!p.hasAttribute(agreed))
			p.setAttribute(agreed, false);
		if (!p.hasAttribute(ignoring))
			p.setAttribute(ignoring, false);
	}
	
	public static boolean readGCRules(Player p) {
		if (!p.hasAttribute(readRules))
			p.setAttribute(readRules, false);
		return p.getAttribute(readRules);
	}
	
	public static boolean agreedToRules(Player p) {
		if (!p.hasAttribute(agreed))
			p.setAttribute(agreed, false);
		
		return p.getAttribute(agreed);
	}
	
	public static boolean ignoringGC(Player p) {
		if (!p.hasAttribute(ignoring))
			p.setAttribute(ignoring, false);
		
		return p.getAttribute(ignoring);
	}
	
	public static synchronized void setValue(Player p, String identKey, Object Attribute, boolean save) {
		p.setAttribute(identKey, Attribute); //i like making my saving synced, dun judge :<
		if (save)
			saveValue(p, identKey);
	}
	
	public static synchronized void setValue(Player p, String identKey, Object Attribute) {
		setValue(p, identKey, Attribute, false);
	}
	public static void saveValue(Player p, String identKey) {
		if (!p.hasAttribute(identKey))
			return;
		try {
			p.saveAttribute(identKey);
		}
		catch (NotSerializableException e) {
			e.printStackTrace();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
