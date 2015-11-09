package de.ketrwu.levitate.cli;

import java.util.HashMap;
import java.util.logging.Logger;

import de.ketrwu.levitate.cli.Message.TextMode;

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
	public void onHelp(Logger logger, String command, String[] args) {
		HashMap<String, String> replaces = new HashMap<String, String>();
		logger.info(Message.DEFAULTHELPMAP_HEADER.get(TextMode.COLOR, replaces));
		int i = 0;
		for(CommandInformation info : registry.getCommands().keySet()) {
			if(info.getCommand().equalsIgnoreCase(getCommandBase(command))) {
				String syntax = info.getSyntax();
				replaces.put("%syntax%", syntax);
				String desc = info.getDescription();
				if(desc == null || desc.equals("")) desc = Message.DEFAULTHELPMAP_NO_DESCRIPTION.get(TextMode.COLOR);
				replaces.put("%description%", desc);
				logger.info(Message.DEFAULTHELPMAP_HELP_ELEMENT.get(TextMode.COLOR, replaces));
			} 
		}
		if(i == 0) {
			replaces.put("%command%", command);
			logger.info(Message.DEFAULTHELPMAP_NO_COMMANDS.get(TextMode.COLOR, replaces));
		}
	}
	
	public String getCommandBase(String command) {
		if(command.startsWith("?") || command.startsWith("/") || command.startsWith("$"))
			command = command.substring(1);
		return command;
	}
	
}
