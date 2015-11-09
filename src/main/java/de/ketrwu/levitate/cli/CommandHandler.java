package de.ketrwu.levitate.cli;


/**
 * Handle a Levitate-Command
 * @author Kenneth Wussmann
 */
public interface CommandHandler {
	
	/**
	 * Handles the registered command.
	 * This method only get's called when the user-input matches the syntax and he has permission.
	 * @param args A set of arguments. This is used to get vaild parsed values of the user-input
	 */
	public void execute(String command, ParameterSet args);
	
}
