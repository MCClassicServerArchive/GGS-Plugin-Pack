/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package net.mcforge.groupmanager.commands;

/**
*
* @author Wouter Gerarts
*/
public class CommandHelper {
	/**
	* gives back a specific part of the input, if it's too long to show in chat
	* @param input the input lines
	* @param page the page to show (starting at 1)
	* @return the page to show
	*/
	public static String[] getPage(String[] input, int page) {
		--page;
		page*=7;
		int itemc = 0;
		if (input.length - page > 0) { itemc = input.length - page; }
		if (itemc > 7) { itemc = 7; }
		int ci = 0;
		String[] items = new String[7];
		for (int i=0;i<7;i++)
		{
			items[i] = input[page + ci];
			ci++;
		}
		return items;
	}
}

