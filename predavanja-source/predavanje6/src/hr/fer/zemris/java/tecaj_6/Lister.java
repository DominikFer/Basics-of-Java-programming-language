package hr.fer.zemris.java.tecaj_6;

import java.io.File;

public class Lister {
	
	public static void main(String[] args) {
		if(args.length != 1) throw new RuntimeException();
		
		File rootDirectory = new File(args[0]);
		int size = listFiles(rootDirectory, 0, 0);
		
		System.out.println("Veličina svih datoteka u KB: " + size/8);
	}
	
	private static int listFiles(File file, int indentCount, int size) {
		for(int i = 0; i < indentCount; i++) System.out.print(" ");
		System.out.println(file.getName());
		
		if(file.isDirectory()) {
			File[] children = file.listFiles();
			for(File child : children) {
				listFiles(child, indentCount+1, size += child.length());
			}
		}
		
		return size;
	}
}
