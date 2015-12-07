package de.ketrwu.levitate.cli;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import de.ketrwu.levitate.cli.exception.CommandSyntaxException;
import de.ketrwu.levitate.cli.exception.SyntaxResponseException;

/**
 * Holds syntax of Levitate-Command
 * @author Kenneth Wussmann
 */
public class CommandInformation {

	private String syntax;
	private String command;
	private String description = "";
	private List<Argument> args = new ArrayList<Argument>();
	
	/**
	 * Create a CommandInformation for a new command
	 * @param syntax Your syntax
	 */
	public CommandInformation(String syntax) {
		this.syntax = syntax;
		try {
			processSyntax();
		} catch (CommandSyntaxException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Create a CommandInformation with permission and description for a new command
	 * @param syntax Your syntax
	 * @param description Your description of your command
	 */
	public CommandInformation(String syntax, String description) {
		this.syntax = syntax;
		this.description = description;
		try {
			processSyntax();
		} catch (CommandSyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private void processSyntax() throws CommandSyntaxException {
		
		if(!syntax.contains(" ")) {
			processCommandBase(syntax);
			return;
		}
		Iterator<MatchResult> matches = SyntaxValidations.allMatches(Pattern.compile("<([^>]*)>|(\\w+)"), syntax).iterator();
		int index = 0;
		while(matches.hasNext()) {
			String arg = matches.next().group();
			if(index == 0) {
				processCommandBase(arg);
				index++;
				continue;
			}
			index++;
			HashMap<String,String> replaces = new HashMap<String, String>();
			replaces.put("%arg%", arg);
			
			if(!arg.startsWith("<")) throw new CommandSyntaxException(Message.CI_ARG_HAS_TO_START_WITH_CHAR.get(replaces));
			if(!arg.endsWith(">")) throw new CommandSyntaxException(Message.CI_ARG_HAS_TO_END_WITH_CHAR.get(replaces));
			arg = arg.substring(1, arg.length()-1);
			String method = parseArgument(arg).get(0);
			boolean unlimited = false;
			if(method.endsWith("...")) {
				if(matches.hasNext()) throw new CommandSyntaxException(Message.CI_ARG_CANNOT_BE_UNLIMITED.get(replaces));
				unlimited = true;
				method = method.substring(0, method.length()-3);
			}
			if(!SyntaxValidations.existHandler(method)) {
				replaces.clear();
				replaces.put("%method%", method);
				throw new CommandSyntaxException(Message.CI_NO_SYNTAX.get(replaces));
			}
			this.args.add(new Argument(method, parseArgument(arg).get(1), SyntaxValidations.getSyntaxes().get(method), unlimited));
		}
	}
	
	public boolean matchArgument(String input, String syntaxArg) throws CommandSyntaxException {
		List<String> i = parseArgument(syntaxArg);
		for(SyntaxHandler h : SyntaxValidations.getSyntaxes().values()) {
			try {
				h.check(i.get(1), input);
				return true;
			} catch (Exception e) { }
		}
		return false;
	}
			
	private List<String> parseArgument(String arg) throws CommandSyntaxException {
		List<String> i = new ArrayList<String>();
		String method = "";
		String parameters = "";
		if(arg.contains("[") && arg.contains("]")) {
			boolean start = false;
			boolean end = false;
			int dots = 0;
			for(char c : arg.toCharArray()) {
				String ch = String.valueOf(c);	
				HashMap<String,String> replaces = new HashMap<String, String>();
				replaces.put("%char%", "[");
				replaces.put("%arg%", arg);
				if(ch.equals("[")) {
					if(start) throw new CommandSyntaxException(Message.CI_ERROR_AT_CHAR_IN_ARG.get(replaces));
					if(end) throw new CommandSyntaxException(Message.CI_ERROR_AT_CHAR_IN_ARG.get(replaces));
					start = true;
					continue;
				}
				if(ch.equals("]")) {
					replaces.put("%char%", "]");
					if(!start) throw new CommandSyntaxException(Message.CI_ERROR_AT_CHAR_IN_ARG.get(replaces));
					if(end) throw new CommandSyntaxException(Message.CI_ERROR_AT_CHAR_IN_ARG.get(replaces));
					end = true;
					continue;
				}
				if(!start && !end) {
					method += ch;
				} else if(start == true && end == false) {
					parameters += ch;
				} else {
					if(ch.equalsIgnoreCase(".")) {
						dots++;
						if(dots <= 3) continue;
					}
					replaces.clear();
					replaces.put("%char%", ch);
					throw new CommandSyntaxException(Message.CI_ERROR_AT_CHAR.get(replaces));
				}
			}
		} else {
			method = arg;
		}
		i.add(method);
		i.add(parameters);
		return i;
	}
	
	private void processCommandBase(String base) throws CommandSyntaxException {
		if(base.startsWith("<")) throw new CommandSyntaxException(Message.CI_CMD_CANNOT_START_WITH.get());
		this.command = base;
	}
	
	
	public boolean matches(String command, String[] args) throws CommandSyntaxException, SyntaxResponseException {
		if(!this.command.equalsIgnoreCase(command)) return false;
				
		if(args == null || args.length == 0) {
			if(this.args.size() != args.length) return false;
		}
		
		int i = 0; 
		Argument unlimitedArg = null;
		while(i < args.length) {
			Argument exArg = null;
			try {
				exArg = this.args.get(i);
			} catch (IndexOutOfBoundsException e) {
				if(unlimitedArg != null) {
					exArg = unlimitedArg;
				}
			}
			if(exArg == null) return false;
			if(exArg.isUnlimited()) {
				unlimitedArg = (Argument) exArg.clone();
			}
			String arg = args[i];
			try {
				exArg.getHandler().check(exArg.getParameter(), arg);
			} catch (SyntaxResponseException e) {
				throw e;
			}
			i++;
		}
		if(unlimitedArg != null) return true;
		if(this.args.size() == i) return true;
		return false;
	}

	public String getSyntax() {
		return syntax;
	}

	public void setSyntax(String syntax) {
		this.syntax = syntax;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Argument> getArgs() {
		return args;
	}

	public void setArgs(List<Argument> args) {
		this.args = args;
	}
	
	
}
