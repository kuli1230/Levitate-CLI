package de.ketrwu.levitate.cli;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds parameters and casts them to the right data type or class
 * @author Kenneth Wussmann
 */
public class ParameterSet {
	
	private List<String> parameter = new ArrayList<String>();
	
	/**
	 * The collection of parameters
	 * @param args
	 */
	public ParameterSet(String[] args) {
		for(String arg : args)
			parameter.add(arg);
	}
	
	/**
	 * Get argument as String
	 * @param i The index in your command, starts at 0
	 * @return
	 */
	public String getString(int i) {
		return parameter.get(i);
	}

	/**
	 * Get argument as Integer
	 * @param i The index in your command, starts at 0
	 * @return
	 */
	public int getInt(int i) {
		return Integer.parseInt(parameter.get(i));
	}
	
	/**
	 * Get argument as Double
	 * @param i The index in your command, starts at 0
	 * @return
	 */
	public double getDouble(int i) {
		return Double.parseDouble(parameter.get(i));
	}

	/**
	 * Get argument as Boolean
	 * @param i The index in your command, starts at 0
	 * @return
	 */
	public boolean getBoolean(int i) {
		String arg = parameter.get(i);
		if(arg.equalsIgnoreCase("true")) return true;
		if(arg.equalsIgnoreCase("false")) return false;
		if(arg.equalsIgnoreCase("0")) return false;
		if(arg.equalsIgnoreCase("1")) return true;
		if(arg.equalsIgnoreCase("on")) return true;
		if(arg.equalsIgnoreCase("off")) return false;
		if(arg.equalsIgnoreCase("an")) return true;
		if(arg.equalsIgnoreCase("aus")) return false;
		if(arg.equalsIgnoreCase("ja")) return true;
		if(arg.equalsIgnoreCase("nein")) return false;
		if(arg.equalsIgnoreCase("yes")) return true;
		if(arg.equalsIgnoreCase("no")) return false;
		if(arg.equalsIgnoreCase("enable")) return true;
		if(arg.equalsIgnoreCase("disable")) return false;
		return false;
	}
	
	/**
	 * Get arguments from a to b as String delimited by a space
	 * @param start Start index 
	 * @param end End index
	 * @return
	 */
	public String getString(int start, int end) {
		return getString(start, end, " ");
	}
	
	/**
	 * Get arguments from a to b as String delimited by entered String
	 * @param start Start index 
	 * @param end End index
	 * @param delimiter Combine Strings with delimiter
	 * @return
	 */
	public String getString(int start, int end, String delimiter) {
		try {
			String str = "";
			for(String s : parameter.subList(start, end)) {
				str += s + delimiter;
			}
			if(str.endsWith(delimiter)) str.substring(0, str.length()-2);
			return str;
		} catch (IndexOutOfBoundsException e) { }
		return null;
	}
	
	/**
	 * Get arguments from a to the end of all arguments delimited by space
	 * @param start Start index 
	 * @return
	 */
	public String getMessage(int start) {
		return getString(start, parameter.size());
	}
	
	/**
	 * Get argument as URL
	 * @param i The index in your command, starts at 0
	 * @return
	 */
	public URL getURL(int i) {
		try {
			return new URL(parameter.get(i));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Get argument as Object
	 * @param i The index in your command, starts at 0
	 * @return
	 */
	public Object get(int i) {
		return (Object) parameter.get(i);
	}
	
	/**
	 * Checks if string is an integer
	 * @param val 
	 * @return
	 */
	private boolean isInt(String val) {
		try {
			Integer.parseInt(val);
			return true;
		} catch (Exception e) { }
		return false;
	}
}
