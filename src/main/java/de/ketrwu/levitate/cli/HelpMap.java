package de.ketrwu.levitate.cli;



/**
 * Handles command execution when command doesn't exist
 * @author Kenneth Wussmann
 */
public interface HelpMap {
	
	/**
	 * Called when user passes invalid command
	 * @param command Entered base of the command
	 * @param args Arguments of the command
	 */
	public void onHelp(LevitateLogger logger, String command, String[] args);
	
}
