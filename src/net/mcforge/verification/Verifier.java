package net.mcforge.verification;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import net.mcforge.iomodel.Player;
import net.mcforge.util.FileUtils;

public abstract class Verifier {
	private static MessageDigest digest;
	static {
		try {
			digest = MessageDigest.getInstance("SHA-256");
		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	protected static boolean verifies(Player p) {
		return p.getAttribute("mcf_verifies");
	}
	
	protected static void setVerifies(Player p, boolean verifies) {
		p.setAttribute("mcf_verifies", verifies);
	}
	
	protected static boolean isVerified(Player p) {
		return p.getAttribute("mcf_verified");
	}
	
	protected static void setVerified(Player p, boolean verified) {
		p.setAttribute("mcf_verified", verified);
	}
	
	protected static int getTries(Player p) {
		return p.getAttribute("mcf_identtries");
	}
	
	protected static void setTries(Player p, int tries) {
		p.setAttribute("mcf_identtries", tries);
	}

	protected static String getHash(String password) {
		digest.update(password.getBytes(), 0, password.length());
		return new BigInteger(1, digest.digest()).toString(16);
	}

	protected static boolean isCorrect(String input, String hash) {
		return getHash(input).equals(hash);
	}
	
	protected static String getPassword(Player p) {
		String[] lines;
		try {
			lines = FileUtils.readAllLines("properties/passwords.config");
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		String[] split;
		for (int i = 0; i < lines.length; i++) {
			split = lines[i].split(":"); 
			if (split[0].equals(p.username)) {
				return split[1];
			} 
		}
		
		lines = null;
		return null;
	}
	
	protected static String getPassword(String player) {
		String[] lines;
		try {
			lines = FileUtils.readAllLines("properties/passwords.config");
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		String[] split;
		for (int i = 0; i < lines.length; i++) {
			split = lines[i].split(":"); 
			if (split[0].equals(player)) {
				return split[1];
			} 
		}
		
		lines = null;
		return null;
	}
	
	protected static void changePassword(Player p, String currentHash, String newHash) {
		String[] lines;
		try {
			lines = FileUtils.readAllLines("properties/passwords.config");
		}
		catch (IOException e) {
			e.printStackTrace();
			return;
		}

		for (int i = 0; i < lines.length; i++) {
			if (lines[i].equals(p.username + ":" + currentHash)) {
				lines[i] = p.username + ":" + newHash;
			}
		}
		FileUtils.deleteIfExist("properties/passwords.config");
		try {
			FileUtils.writeLines("properties/passwords.config", lines);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		lines = null;
	}
	
	protected static void deletePassword(String player) {
		List<String> lines;
		try {
			lines = FileUtils.readToList("properties/passwords.config");
		}
		catch (IOException e) {
			e.printStackTrace();
			return;
		}

		for (int i = 0; i < lines.size(); i++) {
			if (lines.get(i).split(":")[0].equals(player)) {
				lines.remove(i);
			} 
		}
		FileUtils.deleteIfExist("properties/passwords.config");
		try {
			FileUtils.writeLines("properties/passwords.config", lines.toArray(new String[lines.size()]));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		lines = null;
	}
}
