package hr.fer.zemris.java.tecaj.hw5;

import java.io.BufferedReader;
import java.io.BufferedWriter;

/**
 * Exits the console (TERMINATES).
 */
public class ExitShellCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(BufferedReader in, BufferedWriter out, String[] arguments) {
		return ShellStatus.TERMINATE;
	}

}
