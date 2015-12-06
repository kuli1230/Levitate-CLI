package de.ketrwu.levitate.cli.syntax;

import java.util.HashMap;
import java.util.List;

import de.ketrwu.levitate.cli.Message;
import de.ketrwu.levitate.cli.SyntaxHandler;
import de.ketrwu.levitate.cli.exception.SyntaxResponseException;

/**
 * Checks if user-input is not string. Case-sensitive.
 * @author Kenneth Wussmann
 */
public class NotEqualsSyntax implements SyntaxHandler {

	@Override
	public void check(String parameter, String passed) throws SyntaxResponseException {
		HashMap<String, String> replaces = new HashMap<String, String>();
		replaces.put("%arg%", passed);
		replaces.put("%value%", parameter);
		if(parameter.equals(passed)) throw new SyntaxResponseException(Message.NOTEQUALSSYNTAX_CANNOT_EQUAL.get(replaces));
	}

	@Override
	public List<String> getTabComplete(String parameter, String passed) {
		return null;
	}
}
