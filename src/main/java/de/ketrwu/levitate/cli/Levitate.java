package de.ketrwu.levitate.cli;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;

import de.ketrwu.levitate.cli.exception.CommandSyntaxException;
import de.ketrwu.levitate.cli.exception.SyntaxResponseException;

/**
 * Bundles everything you need to use Levitate.
 * Read <a href="https://github.com/KennethWussmann/Levitate/wiki/2.-First-command">this page</a> to create your first Levitate-Command.
 * @author Kenneth Wussmann
 */

public class Levitate {
	
	private CommandRegistry registry;
	private LevitateLogger logger;
	private boolean readInput;
	
	/**
	 * Bundles everything you need to use Levitate.
	 * Read <a href="https://github.com/KennethWussmann/Levitate/wiki/2.-First-command">this page</a> to create your first Levitate-Command.
	 * @param plugin Your plugin instance
	 */
	public Levitate() {
		registerDefaultLogger();
		SyntaxValidations.registerDefaultSyntax();
		registry = new CommandRegistry(logger);
		registry.registerDefaultHelpMap();
	}

	/**
	 * Bundles everything you need to use Levitate.
	 * Read <a href="https://github.com/KennethWussmann/Levitate/wiki/2.-First-command">this page</a> to create your first Levitate-Command.
	 */
	public Levitate(LevitateLogger logger) {
		registerLogger(logger);
		SyntaxValidations.registerDefaultSyntax();
		registry = new CommandRegistry(logger);
		registry.registerDefaultHelpMap();
	}

	/**
	 * Bundles everything you need to use Levitate. 
	 * Pass <b>true</b> to create a <i>messages.yml</i>.
	 * Read more about the <i>messages.yml</i> <a href="https://github.com/KennethWussmann/Levitate/wiki/5.-Modify-messages">here</a>.
	 * Read <a href="https://github.com/KennethWussmann/Levitate/wiki/2.-First-command">this page</a> to create your first Levitate-Command.
	 * @param plugin
	 * @param createYAML
	 */
	public Levitate(LevitateLogger logger, boolean createYAML) {
		registerLogger(logger);
		SyntaxValidations.registerDefaultSyntax();
		registry = new CommandRegistry(logger);
		registry.registerDefaultHelpMap();
		if(createYAML) {
			File config = new File("messages.yml");
			if(!config.exists()) {
				InputStream jarURL = Levitate.class.getResourceAsStream("/messages.yml");
				try {
					if(jarURL == null) {
						config.createNewFile();
					} else {
						copyFile(jarURL, config);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			Message.loadConfig(config);
		}
	}
	
	public void readInput() {
		readInput = true;
		Scanner scanner = new Scanner(System.in);
		while(readInput) {
			String command = scanner.nextLine();
			List<String> args = new ArrayList<String>();
			String[] argsArr = new String[]{};
			if(command.contains(" ")) {
				String[] splitted = command.split(" ");
				for (int i = 0; i < splitted.length; i++) {
					if(i == 0) {
						command = splitted[i];
						continue;
					}
					args.add(splitted[i]);
				}
				argsArr = args.toArray(new String[args.size()]);
			}
			try {
				getCommandRegistry().userPassCommand(command, argsArr);
			} catch (CommandSyntaxException e) {
				e.printStackTrace();
			} catch (SyntaxResponseException e) {
				logger.log(Level.WARNING, e.getMessage());
			}
		}
	}
	
	public void stopReading() {
		readInput = false;
	}
	
	public void registerLogger(LevitateLogger logger) {
		this.logger = logger;
	}
	
	public LevitateLogger getLogger() {
		return logger;
	}
	
	private void registerDefaultLogger() {
		registerLogger(new LevitateLogger() {
			
			@Override
			public void log(String message) {
				System.out.println(message);
			}
			
			@Override
			public void log(Level level, String message) {
				System.out.println("[" + level.toString() + "] " + message);
			}
		});
	}
	
	/**
	 * Get instance of CommandRegistry which holds your Levitate-Commands
	 * @return
	 */
	public CommandRegistry getCommandRegistry() {
		return registry;
	}
	
	/**
	 * Register all commands with annotations in given class
	 * @param obj Class with commands
	 */
	public void registerCommands(Object obj) {
		getCommandRegistry().registerCommands(obj);
	}

	/**
	 * Register new command with aliases
	 * @param info CommandInformation with syntax, permission etc
	 * @param aliases Array with aliases
	 * @param handler The CommandHandler which handels the execution of the command
	 */
	public void register(CommandInformation info, String[] aliases, CommandHandler handler) {
		getCommandRegistry().register(info, aliases, handler);
	}
	
	/**
	 * Register new command
	 * @param info CommandInformation with syntax, permission etc
	 * @param handler The CommandHandler which handels the execution of the command
	 */
	public void register(CommandInformation info, CommandHandler handler) {
		getCommandRegistry().register(info, handler);
	}
	
	/**
	 * Register your own syntax.
	 * @param method The base command of your syntax
	 * @param handler The handler to check values against your syntax
	 */
	public static void registerSyntax(String method, SyntaxHandler handler) {
		SyntaxValidations.getSyntaxes().put(method, handler);
	}
		
	/**
	 * Register default HelpMap
	 */
	public void registerDefaultHelpMap() {
		getCommandRegistry().registerDefaultHelpMap();
	}
	
	/**
	 * Register own HelpMap
	 * @param helpMap Handles the help-message
	 */
	public void registerHelpMap(HelpMap helpMap) {
		getCommandRegistry().registerHelpMap(helpMap);
	}
	
	/**
	 * Copy InputStream to output file
	 * @param in
	 * @param out
	 * @throws Exception
	 */
	private void copyFile(InputStream in, File out) throws Exception {
        InputStream fis = in;
        FileOutputStream fos = new FileOutputStream(out);
        try {
            byte[] buf = new byte[1024];
            int i = 0;
            while ((i = fis.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }
}
