package de.ketrwu.levitate.cli.syntax;

import java.util.HashMap;
import java.util.List;

import de.ketrwu.levitate.cli.Message;
import de.ketrwu.levitate.cli.SyntaxHandler;
import de.ketrwu.levitate.cli.exception.CommandSyntaxException;
import de.ketrwu.levitate.cli.exception.SyntaxResponseException;

/**
 * Checks if user-input is a double
 * @author Kenneth Wussmann
 */
public class DoubleSyntax implements SyntaxHandler {

	@Override
	public void check(String parameter, String passed) throws CommandSyntaxException, SyntaxResponseException {
		HashMap<String, String> replaces = new HashMap<String, String>();
		replaces.put("%arg%", passed);
		if(!isDouble(passed)) throw new SyntaxResponseException(Message.DOUBLESYNTAX_HAS_NO_DOUBLE.get(replaces));
		double parsedDouble = Double.parseDouble(passed);
		if(parameter.equals("+")) {
			if(parsedDouble < 0 && parsedDouble != 0) throw new SyntaxResponseException(Message.DOUBLESYNTAX_HAS_TO_BE_POSITIVE_ZERO.get(replaces));
		} else if(parameter.equals("-")) {
			if(parsedDouble > 0 && parsedDouble != 0) throw new SyntaxResponseException(Message.DOUBLESYNTAX_HAS_TO_BE_NEGATIVE_ZERO.get(replaces));
		} else if(parameter.contains("-")) {
			replaces.put("%parameter%", parameter);
			if(!parameter.matches("(-?[0-9]+)-(-?[0-9]+)")) throw new CommandSyntaxException(Message.DOUBLESYNTAX_PARAMETER_MALFORMED.get(replaces));
			if(parameter.startsWith("-")) {
				double a = Double.parseDouble("-"+parameter.split("-", 3)[1]);
				double b = Double.parseDouble(parameter.split("-", 3)[2]);
				replaces.put("%min%", String.valueOf(a));
				replaces.put("%max%", String.valueOf(b));
				
				if(a > b) throw new SyntaxResponseException(Message.DOUBLESYNTAX_PARAMETER_MALFORMED_2.get(replaces));
				if(parsedDouble < a || parsedDouble > b) throw new SyntaxResponseException(Message.DOUBLESYNTAX_HAS_TO_BE_INBETWEEN.get(replaces));
				return;
			} else {
				double a = Double.parseDouble(parameter.split("-", 2)[0]);
				double b = Double.parseDouble(parameter.split("-", 2)[1]);
				replaces.put("%min%", String.valueOf(a));
				replaces.put("%max%", String.valueOf(b));
				if(a > b) throw new SyntaxResponseException(Message.DOUBLESYNTAX_PARAMETER_MALFORMED_2.get(replaces));
				if(parsedDouble < a || parsedDouble > b) throw new SyntaxResponseException(Message.DOUBLESYNTAX_HAS_TO_BE_INBETWEEN.get(replaces));
			}		
		}
	}
	
	public boolean isDouble(String val) {
		try {
			Double.parseDouble(val);
			return true;
		} catch (Exception e) { }
		return false;
	}
	
	public int countOccurrences(String input, String search) {
		int i = 0;
		int lastIndex = 0;

	    while(lastIndex != -1){
	    	lastIndex = input.indexOf(search,lastIndex);
			if( lastIndex != -1) {
				i++;
			}
			lastIndex+=search.length();
	    }
		return i;
	}

	@Override
	public List<String> getTabComplete(String parameter, String passed) {
		return null;
	}

}
