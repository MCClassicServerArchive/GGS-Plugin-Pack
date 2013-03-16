/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.globalchat;

public enum IRCRank {
	Other("", -1), Voiced("+", 0), HalfOp("%", 1), Op("@", 2), Admin("&", 3), Owner("~", 4);
	
	private String symbol;
	private int perm;

	IRCRank(String symbol, int perm) {
		this.symbol = symbol;
		this.perm = perm;
	}
	
	public static IRCRank parseRank(String rawUser) {
		if (rawUser.length() < 1) { throw new IllegalArgumentException("String cannot be empty!"); }
		
		return getBySymbol(String.valueOf(rawUser.charAt(0)));
	}
	
	public static String parseUser(String rawUser) {
		if (rawUser.length() < 1) { throw new IllegalArgumentException("String cannot be empty!"); }
		
		if (parseRank(rawUser) == Other) {
			return rawUser;
		}
		
		return rawUser.substring(1);
	}
	
	public static IRCRank getBySymbol(String symbol) {
		if (symbol.equals("+"))
			return Voiced;
		else if (symbol.equals("%"))
			return HalfOp;
		else if (symbol.equals("@"))
			return Op;
		else if (symbol.equals("&"))
			return Admin;
		else if (symbol.equals("~"))
			return Owner;
		else return Other;
	}
	
	public String getSymbol() {
		return symbol;
	}
	
	public int getPerm() {
		return perm;
	}
}
