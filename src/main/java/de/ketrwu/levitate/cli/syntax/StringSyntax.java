package de.ketrwu.levitate.cli.syntax;

import java.util.HashMap;
import java.util.List;

import de.ketrwu.levitate.cli.Message;
import de.ketrwu.levitate.cli.SyntaxHandler;
import de.ketrwu.levitate.cli.exception.SyntaxResponseException;

/**
 * Checks if user-input is a string
 * @author Kenneth Wussmann
 */
public class StringSyntax implements SyntaxHandler {

	@Override
	public void check(String parameter, String passed) throws SyntaxResponseException {
		HashMap<String, String> replaces = new HashMap<String, String>();
		replaces.put("%arg%", passed);
		if(parameter.equals("") || parameter.equals("aA")) {
			if(isInt(passed)) throw new SyntaxResponseException(Message.STRINGSYNTAX_CANNOT_BE_INT.get(replaces));
			return;
		}
		if(parameter.equals("a")) {
			if(!isLowerCase(passed)) throw new SyntaxResponseException(Message.STRINGSYNTAX_ONLY_LOWERCASE.get(replaces));
		}
		if(parameter.equals("A")) {
			if(!isUpperCase(passed)) throw new SyntaxResponseException(Message.STRINGSYNTAX_ONLY_UPPERCASE.get(replaces));
		}
		return;
	}

	public boolean isInt(String val) {
		try {
			Integer.parseInt(val);
			return true;
		} catch (Exception e) { }
		return false;
	}

	public boolean isLowerCase(String input) {
		for(char ch : input.toCharArray()) {
			String c = String.valueOf(ch);
			if(!c.equals(c.toLowerCase())) return false;
		}
		return true;
	}

	public boolean isUpperCase(String input) {
		for(char ch : input.toCharArray()) {
			String c = String.valueOf(ch);
			if(!c.equals(c.toUpperCase())) return false;
		}
		return true;
	}

	@Override
	public List<String> getTabComplete(String parameter, String passed) {
		return null;
	}
	
}
