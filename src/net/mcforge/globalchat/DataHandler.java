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
		if (!p.hasValue(readRules))
			p.setValue(readRules, false);
		if (!p.hasValue(agreed))
			p.setValue(agreed, false);
		if (!p.hasValue(ignoring))
			p.setValue(ignoring, false);
	}
	
	public static boolean readGCRules(Player p) {
		if (!p.hasValue(readRules))
			p.setValue(readRules, false);
		return p.getValue(readRules);
	}
	
	public static boolean agreedToRules(Player p) {
		if (!p.hasValue(agreed))
			p.setValue(agreed, false);
		
		return p.getValue(agreed);
	}
	
	public static boolean ignoringGC(Player p) {
		if (!p.hasValue(ignoring))
			p.setValue(ignoring, false);
		
		return p.getValue(ignoring);
	}
	
	public static synchronized void setValue(Player p, String identKey, Object value, boolean save) {
		p.setValue(identKey, value); //i like making my saving synced, dun judge :<
		if (save)
			saveValue(p, identKey);
	}
	
	public static synchronized void setValue(Player p, String identKey, Object value) {
		setValue(p, identKey, value, false);
	}
	public static void saveValue(Player p, String identKey) {
		if (!p.hasValue(identKey))
			return;
		try {
			p.saveValue(identKey);
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
