package hr.fer.zemris.java.tecaj.hw5;

import java.io.BufferedReader;
import java.io.BufferedWriter;

/**
 * All shell commands should implement this interface
 * so that shell can work with them. 
 */
public interface ShellCommand {

	/**
	 * Executes the command (user input) from the console.
	 * 
	 * @param in			Wrapped <code>stdin</code> which will
	 * 						be used for reading in additional parameters.
	 * @param out			Wrapped <code>stout</code> which will
	 * 						be used for writing actual result(s).
	 * @param arguments		User arguments (commands).
	 * @return				<code>ShellStatus.CONTINUE</code> if the shell should
	 * 						continue working, <code>ShellStatus.TERMINATE</code> otherwise.
	 */
	public ShellStatus executeCommand(BufferedReader in, BufferedWriter out, String[] arguments);
}
