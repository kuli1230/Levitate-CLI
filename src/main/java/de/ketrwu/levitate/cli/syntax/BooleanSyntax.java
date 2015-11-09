package de.ketrwu.levitate.cli.syntax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.ketrwu.levitate.cli.Message;
import de.ketrwu.levitate.cli.SyntaxHandler;
import de.ketrwu.levitate.cli.Message.TextMode;
import de.ketrwu.levitate.cli.exception.SyntaxResponseException;

/**
 * Checks if user-input is something like a boolean
 * @author Kenneth Wussmann
 */
public class BooleanSyntax implements SyntaxHandler {
	
	private List<String> values = new ArrayList<String>();
	
	public BooleanSyntax() {
		loadValues();
	}
	
	private void loadValues() {
		values.add("true");
		values.add("false");
		values.add("0");
		values.add("1");
		values.add("on");
		values.add("off");
		values.add("an");
		values.add("aus");
		values.add("ja");
		values.add("nein");
		values.add("yes");
		values.add("no");
		values.add("enable");
		values.add("disable");
	}
	
	@Override
	public void check(String parameter, final String passed) throws SyntaxResponseException {
		if(values.contains(passed.toLowerCase())) return;
		throw new SyntaxResponseException(Message.BOOLEANSYNTAX_HAS_TO_BE_BOOLEAN.get(TextMode.COLOR, new HashMap<String, String>(){{put("%arg%", passed);}}));
	}

	@Override
	public List<String> getTabComplete(String parameter, String passed) {
		return new ArrayList<String>(values);
	}

}
