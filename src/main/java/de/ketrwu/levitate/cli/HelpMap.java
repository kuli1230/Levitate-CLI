package de.ketrwu.levitate.cli;

import java.util.logging.Logger;


/**
 * Handles command execution when command doesn't exist
 * @author Kenneth Wussmann
 */
public interface HelpMap {
	
	/**
	 * Called when player passes invalid command
	 * @param command Entered base of the command
	 * @param args Arguments of the command
	 */
	public void onHelp(Logger logger, String command, String[] args);
	
}
