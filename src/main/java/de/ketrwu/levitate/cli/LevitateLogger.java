package de.ketrwu.levitate.cli;

import java.util.logging.Level;

public interface LevitateLogger {
	
	public void log(Level level, String message);
	
	public void log(String message);
	
}
