package de.ketrwu.levitate.cli;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.ketrwu.levitate.cli.exception.CommandAnnotationException;
import de.ketrwu.levitate.cli.exception.CommandSyntaxException;
import de.ketrwu.levitate.cli.exception.SyntaxResponseException;

/**
 * Handles Levitate-Commands
 * @author Kenneth Wussmann
 */
public class CommandRegistry {
	
	private HashMap<CommandInformation, CommandHandler> commands = new HashMap<CommandInformation, CommandHandler>();
	private List<String> aliases = new ArrayList<String>();
	private List<Object> commandClasses = new ArrayList<Object>();
	private LevitateLogger logger;
	private HelpMap helpMap = null;
	
	
	/**
	 * This class registers your commands and handels them.
	 */
	public CommandRegistry(LevitateLogger logger) { 
		this.setLogger(logger);
	}
	
	/**
	 * Register all commands with annotations in given class
	 * @param obj Class with commands
	 */
	public void registerCommands(final Object obj) {
		if(commandClasses.contains(obj)) return;
		commandClasses.add(obj);
		HashMap<String, String> replaces = new HashMap<String, String>();
		try {
			for(final Method m : obj.getClass().getDeclaredMethods()) {
				replaces.put("%method%", obj.getClass().getName() + ": " + m.getName() + "()");
				CommandInformation cmd = null;
				String[] aliases = null;
				if(m.isAnnotationPresent(de.ketrwu.levitate.cli.Command.class)) {
					if(m.getParameterTypes().length != 2) throw new CommandAnnotationException(Message.CR_PARAMETERCOUNT_INVALID.get(replaces));
					if(m.getParameterTypes()[0] != String.class) {
						replaces.put("%index%", "0");
						replaces.put("%class%", "String");
						throw new CommandAnnotationException(Message.CR_PARAMETER_INVALID.get(replaces));
					}
					if(m.getParameterTypes()[1] != ParameterSet.class) {
						replaces.put("%index%", "1");
						replaces.put("%class%", "ParameterSet");
						throw new CommandAnnotationException(Message.CR_PARAMETER_INVALID.get(replaces));
					}
					
					Command commandAnnotation = m.getAnnotation(Command.class);
					cmd = new CommandInformation(commandAnnotation.syntax());
					
					if(!commandAnnotation.description().equals("")) cmd.setDescription(commandAnnotation.description());
					if(commandAnnotation.aliases().length > 0) aliases = commandAnnotation.aliases();
					
					if(aliases == null) {
						register(cmd, new CommandHandler() {
							
							@Override
							public void execute(String command, ParameterSet args) {
								try {
									m.invoke(obj, command, args);
								} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
									e.printStackTrace();
								}
							}
						});
					} else {;
						register(cmd, aliases, new CommandHandler() {
							
							@Override
							public void execute(String command, ParameterSet args) {
								try {
									m.invoke(obj, command, args);
								} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
									e.printStackTrace();
								}
							}
						});
					}
				}
			}
		} catch (CommandAnnotationException e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * Register new command
	 * @param info CommandInformation with syntax, permission etc
	 * @param handler The CommandHandler which handels the execution of the command
	 */
	public void register(CommandInformation info, CommandHandler handler) {
		commands.put(info, handler);
	}
	
	/**
	 * Register alias, only used internal
	 * @param alias Alias as string
	 */
	public void registerAlias(String alias) {
		if(aliases.contains(alias.toLowerCase())) return;
		aliases.add(alias.toLowerCase());
	}
	
	/**
	 * Register new command with aliases
	 * @param info CommandInformation with syntax, permission etc
	 * @param aliases Array with aliases
	 * @param handler The CommandHandler which handels the execution of the command
	 */
	public void register(CommandInformation info, String[] aliases, CommandHandler handler) {
		
		for(String alias : aliases) {
			String ns = "";
			String syntax = info.getSyntax();
			ns += alias + " ";
			if(syntax.contains(" ")) {
				String[] split = syntax.split(" ");
				for (int i = 0; i < split.length; i++) {
					if(i != 0) {
						ns += split[i] + " ";
					}
				}
			}
			if(ns.endsWith(" ")) ns = ns.substring(0, ns.length()-1);
			CommandInformation cinfo = new CommandInformation(ns);
			cinfo.setDescription(info.getDescription());
			registerAlias(alias);
			commands.put(cinfo, handler);
		}
		commands.put(info, handler);
	}
	
	/**
	 * Register default HelpMap
	 */
	public void registerDefaultHelpMap() {
		this.helpMap = new DefaultHelpMap(this);
	}
	
	/**
	 * Register own HelpMaoo
	 * @param helpMap Handles the help-message
	 */
	public void registerHelpMap(HelpMap helpMap) {
		this.helpMap = helpMap;
	}
		
	/**
	 * Executes a command.
	 * You don't need to call it.
	 * @param command The base-command
	 * @param args The arguments
	 * @return boolean
	 * @throws CommandSyntaxException
	 * @throws SyntaxResponseException
	 * @throws ExecutorIncompatibleException 
	 */
	public boolean userPassCommand(String command, String[]args) throws CommandSyntaxException, SyntaxResponseException  {
		boolean found = false;
		SyntaxResponseException exception = null;
		
		for(CommandInformation i : commands.keySet()) {
			if(found == true) continue;
			try {
				if(i.matches(command, args)) {
					commands.get(i).execute(command, new ParameterSet(args));
					found = true;
				}
			} catch (SyntaxResponseException e) {
				exception = (SyntaxResponseException) e;
			}
		}
		if(exception != null && found == false) {
			throw exception;
		} else if(helpMap != null && found == false) {
			helpMap.onHelp(getLogger(), command, args);
		}
		return found;
	}
	
	public static boolean existClass(String clazz) {
		try {
			Class.forName(clazz);
			return true;
		} catch (Exception e) { }
		return false;
	}

	public HashMap<CommandInformation, CommandHandler> getCommands() {
		return commands;
	}

	public void setCommands(HashMap<CommandInformation, CommandHandler> commands) {
		this.commands = commands;
	}
	
	public HelpMap getHelpMap() {
		return helpMap;
	}

	public void setHelpMap(HelpMap helpMap) {
		this.helpMap = helpMap;
	}

	public LevitateLogger getLogger() {
		return logger;
	}

	public void setLogger(LevitateLogger logger) {
		this.logger = logger;
	}
	
	
	
}
