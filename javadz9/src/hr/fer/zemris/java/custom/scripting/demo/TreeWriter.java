package hr.fer.zemris.java.custom.scripting.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

/**
 *	Program which opens SmartScript document (.smscr extension),
 *	parses it into a tree and reproduces its (aproximate)
 *	original form onto standard output.
 */
public class TreeWriter {

	public static void main(String[] args) {
		if(args.length != 1)
			throw new IllegalArgumentException("TreeWriter accepts single argument - SmartScript file (.smscr).");
		
		String fileName = args[0];
		if(!fileName.contains(".smscr"))
			throw new IllegalArgumentException("TreeWriter accepts only SmartScript files (extension is .smscr).");
		
		String docBody = "";
		
		try {
			Scanner inputScanner = new Scanner(new FileInputStream(new File(fileName)), "UTF-8");
			while(inputScanner.hasNextLine()) {
				docBody += inputScanner.nextLine();
			}
			inputScanner.close();
		} catch (FileNotFoundException e1) {
			System.out.println("Unable to find '" + fileName + "' file.");
			System.exit(-1);
		}
		
		SmartScriptParser parser = null;
		try {
			parser = new SmartScriptParser(docBody);
		} catch(SmartScriptParserException e) {
			System.out.println("Unable to parse document!");
			System.exit(-1);
		}
		
		WriteVisitor visitor = new WriteVisitor();
		parser.getDocumentNode().accept(visitor);
	}
	
	/**
	 *	Visitor which is responsible for writing out the output
	 *	of the document {@link Node}s.
	 */
	public static class WriteVisitor implements INodeVisitor {
		
		/**
		 * Recursively print all the nodes.
		 * 
		 * @param node 	Any node of the document or {@link DocumentNode} itself.
		 * @return 		Textual representation as it would like in original document.
		 */
		private static String recreateOriginalScript(Node node) {
			String body = node.asText();
			for(int i = 0; i < node.numberOfChildren(); i++) {
				body += recreateOriginalScript(node.getChild(i));
			}
			
			// Add END tag at the end if it's non-empty tag (ForLoopNode)
			if(node instanceof ForLoopNode)
				body += "[$END$]";
			
			return body;
		}

		@Override
		public void visitTextNode(TextNode node) {
			System.out.println(recreateOriginalScript(node));
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			System.out.println(recreateOriginalScript(node));
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			System.out.println(recreateOriginalScript(node));
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			System.out.println(recreateOriginalScript(node));
		}
		
	}
	
}
