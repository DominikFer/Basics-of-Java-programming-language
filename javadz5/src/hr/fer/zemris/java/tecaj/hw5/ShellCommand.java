package hr.fer.zemris.java.tecaj.hw5;

import java.io.BufferedReader;
import java.io.BufferedWriter;

public interface ShellCommand {

	public ShellStatus executeCommand(BufferedReader in, BufferedWriter out, String[] arguments);
}
