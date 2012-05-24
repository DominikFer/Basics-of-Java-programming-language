package hr.fer.zemris.java.custom.scripting.demo;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

public class TreeWriter {

	public static void main(String[] args) {
		String docBody = "This is sample text.\r\n[$ FOR i -1.10 10 1 $]\r\nThis is [$= i $]-th time this message is generated.\r\n[$END$]\r\n[$FOR i 0 10 2 $]\r\nsin([$=i$]^2) = [$= i i * @sin \"0.000\" @decfmt $]\r\n[$END$]";
		
		SmartScriptParser parser = null;
		try {
			parser = new SmartScriptParser(docBody);
		} catch(SmartScriptParserException e) {
			System.out.println("Unable to parse document!");
			System.exit(-1);
		} catch(Exception e) {
			System.out.println("If this line ever executes, you have failed this class!");
			System.exit(-1);
		}
		
		WriteVisitor visitor = new WriteVisitor();
		parser.getDocumentNode().accept(visitor);
	}
	
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
