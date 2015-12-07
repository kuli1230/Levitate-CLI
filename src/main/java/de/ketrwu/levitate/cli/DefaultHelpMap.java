package de.ketrwu.levitate.cli;

import java.util.HashMap;

/**
 * Default Levitate-HelpMap
 * @author Kenneth Wussmann
 */
public class DefaultHelpMap implements HelpMap {
	
	CommandRegistry registry = null;
	
	public DefaultHelpMap(CommandRegistry commandRegistry) {
		registry = commandRegistry;
	}

	@Override
	public void onHelp(LevitateLogger logger, String command, String[] args) {
		HashMap<String, String> replaces = new HashMap<String, String>();
		logger.log(Message.DEFAULTHELPMAP_HEADER.get(replaces));
		int i = 0;
		for(CommandInformation info : registry.getCommands().keySet()) {
			if(info.getCommand().equalsIgnoreCase(getCommandBase(command))) {
				String syntax = info.getSyntax();
				replaces.put("%syntax%", syntax);
				String desc = info.getDescription();
				if(desc == null || desc.equals("")) desc = Message.DEFAULTHELPMAP_NO_DESCRIPTION.get();
				replaces.put("%description%", desc);
				logger.log(Message.DEFAULTHELPMAP_HELP_ELEMENT.get(replaces));
			} 
		}
		if(i == 0) {
			replaces.put("%command%", command);
			logger.log(Message.DEFAULTHELPMAP_NO_COMMANDS.get(replaces));
		}
	}
	
	public String getCommandBase(String command) {
		if(command.startsWith("?") || command.startsWith("/") || command.startsWith("$"))
			command = command.substring(1);
		return command;
	}
	
}
