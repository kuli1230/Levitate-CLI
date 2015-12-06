package de.ketrwu.levitate.cli.test;

import java.util.logging.Logger;

import de.ketrwu.levitate.cli.CommandHandler;
import de.ketrwu.levitate.cli.CommandInformation;
import de.ketrwu.levitate.cli.Levitate;
import de.ketrwu.levitate.cli.ParameterSet;
import de.ketrwu.levitate.cli.exception.CommandSyntaxException;
import de.ketrwu.levitate.cli.exception.SyntaxResponseException;

public class Main {
	
	public static void main(String[] args) {
		Levitate levitate = new Levitate(Logger.getAnonymousLogger());
		levitate.register(new CommandInformation("/test"), new CommandHandler() {
			
			@Override
			public void execute(String command, ParameterSet args) {
				System.out.println("Hello!");
			}
		});
		try {
			levitate.getCommandRegistry().userPassCommand("test", new String[] {});
		} catch (CommandSyntaxException | SyntaxResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
