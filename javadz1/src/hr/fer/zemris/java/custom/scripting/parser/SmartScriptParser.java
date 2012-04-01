package hr.fer.zemris.java.custom.scripting.parser;

import hr.fer.zemris.java.custom.collections.ArrayBackedIndexedCollection;
import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.tokens.Token;
import hr.fer.zemris.java.custom.scripting.tokens.TokenConstantDouble;
import hr.fer.zemris.java.custom.scripting.tokens.TokenConstantInteger;
import hr.fer.zemris.java.custom.scripting.tokens.TokenFunction;
import hr.fer.zemris.java.custom.scripting.tokens.TokenOperator;
import hr.fer.zemris.java.custom.scripting.tokens.TokenString;
import hr.fer.zemris.java.custom.scripting.tokens.TokenVariable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Class used for document parsing and creating document structure. Supports empty tags, for loops and text. 
 * 
 * @author Sven Kapuðija
 * 
 */
public class SmartScriptParser {
	
	// Tag constants used for parsing.
	/** Initial tag, expect text (or escaping) or opening tag */
	private static final int INITIAL = 0;
	
	/** TextNode */
	private static final int NODE_TEXT = 1;
	/** Escape in TextNode */
	private static final int ESCAPED_TEXT = 2;

	/** Some Node should be next */
	private static final int NODE_COMMAND = 12;
	
	/** EchoNode/emptyTag (=) */
	private static final int NODE_ECHO = 3;
	/** Function in EchoNode */
	private static final int ECHO_FUNCTION = 4;
	/** Number in EchoNode */
	private static final int ECHO_NUMBER_OR_OPERATOR = 5;
	/** String in EchoNode */
	private static final int ECHO_STRING = 6;
	/** Escape in String in EchoNode */
	private static final int ESCAPED_STRING = 17;
	/** Variable in EchoNode */
	private static final int ECHO_VARIABLE = 7;
	
	/** ForLoopNode */
	private static final int NODE_FOR = 8;
	/** StartExpression in ForLoopNode */
	private static final int FOR_START = 9;
	/** EndExpression in ForLoopNode */
	private static final int FOR_END = 10;
	/** StepExpression in ForLoopNode */
	private static final int FOR_STEP = 11;
	
	/** END tag (for closing non-empty tags like ForLoopNode) */
	private static final int NODE_END = 13;
	
	/** Opening tag, got first '[' letter, '$' should be next */
	private static final int TAG_OPEN_1 = 14;
	/** Opening tag, some command should be next (=,END,FOR) */
	private static final int TAG_OPEN = 15;
	
	/** Opening tag, got first '$' letter, ']' should be next */
	private static final int TAG_CLOSE_1 = 16;
	
	/** Document body in String representation */
	private String documentBody;
	/** Document structure */
	private DocumentNode documentNode;
	
	private ObjectStack stack;
	private StringBuilder builder;
	
	private ArrayBackedIndexedCollection echoNodeTokens;
	
	/**
	 * Creates new document structure and automatically parses the document.
	 * 
	 * @param documentBody Document you want to parse.
	 * @throws SmartScriptParserException If parsing could not succeed.
	 */
	public SmartScriptParser(String documentBody) throws SmartScriptParserException {
		this.documentBody = documentBody;
		this.stack = new ObjectStack();
		
		this.parse();
	}
	
	/**
	 * Creates and add's TextNode to the stack.
	 * 
	 * @throws SmartScriptParserException If the stack is empty - there is too much <code>END</code> tags.
	 */
	private void addTextNode() throws SmartScriptParserException {
		if(stack.isEmpty()) {
			throw new SmartScriptParserException("Too much 'END's.");
		} else {
			((Node) stack.peek()).addChildNode(new TextNode(builder.toString()));
		}
	}
	
	/**
	 * Creates and add's EchoNode to the stack with all the tokens.
	 * 
	 * @throws SmartScriptParserException If the stack is empty - there is too much <code>END</code> tags.
	 */
	private void addEchoNode() throws SmartScriptParserException {
		Token[] tokens = new Token[echoNodeTokens.size()];
		for(int i = 0; i < echoNodeTokens.size(); i++) {
			tokens[i] = (Token) echoNodeTokens.get(i);
		}
		echoNodeTokens.clear();
		
		EchoNode node = new EchoNode(tokens);
		
		if(stack.isEmpty()) {
			throw new SmartScriptParserException("Too much 'END's.");
		} else {
			((Node) stack.peek()).addChildNode(node);
		}
	}
	
	/**
	 * Add's EchoNodeToken into private collection so it could be latter on added to EchoNode.
	 * 
	 * @throws SmartScriptParserException If the token isn't properly typed.
	 */
	private void addEchoNodeToken() throws SmartScriptParserException {
		if(echoNodeTokens == null)
			echoNodeTokens = new ArrayBackedIndexedCollection();
		
		echoNodeTokens.add(getToken(builder.toString()));
	}
	
	/**
	 * Creates Token suclass based on textual representation of the token (ex. -1.2 becomes TokenConstantDouble).
	 * 
	 * @param token Textual representation of the token.
	 * @return Token subclass based on textual representation of the token.
	 * @throws SmartScriptParserException If the token isn't properly typed.
	 */
	private Token getToken(String token) throws SmartScriptParserException {
		if(token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/")) {
			return new TokenOperator(token);
		} else if(token.startsWith("@")) {
			return new TokenFunction(token.substring(1, token.length()));
		} else if(isInteger(token)) {
			return new TokenConstantInteger(Integer.parseInt(token));
		} else if(isDouble(token)) {
			return new TokenConstantDouble(Double.parseDouble(token));
		} else if(token.startsWith("\"") && token.endsWith("\"")) {
			return new TokenString(token.replace("\"", ""));
		} else if(Character.isLetter(token.charAt(0))){
			return new TokenVariable(token);
		} else {
			throw new SmartScriptParserException("Invalid Token parameter type.");
		}
	}
	
	/**
	 * Creates and add's ForLoopNode to the stack with all 4 parameters (last can be <code>null</code>).
	 * 
	 * @return Created ForLoopNode.
	 * @throws SmartScriptParserException If the stack is empty - there is too much <code>END</code> tags.
	 */
	private ForLoopNode addForLoopNode() throws SmartScriptParserException {
		String[] tokensString = builder.toString().split(" ");
		Token[] tokens = new Token[4];
		
		for(int i = 0; i < tokensString.length; i++) {
			tokens[i] = getToken(tokensString[i]);
		}

		ForLoopNode node = new ForLoopNode((TokenVariable) tokens[0], tokens[1], tokens[2], tokens[3]);
		
		if(stack.isEmpty()) {
			throw new SmartScriptParserException("Too much 'END's.");
		} else {
			((Node) stack.peek()).addChildNode(node);
		}
		
		return node;
	}
	
	/**
	 * Tests if provided <code>String</code> is <code>int</code>.
	 * 
	 * @param s String you want to parse into <code>int</code.
	 * @return <code>true</code> if string is <code>int</code>, <code>false</code> otherwise
	 */
	private static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    return true;
	}
	
	/**
	 * Tests if provided <code>String</code> is <code>double</code>.
	 * 
	 * @param s String you want to parse into <code>double</code.
	 * @return <code>true</code> if string is <code>double</code>, <code>false</code> otherwise
	 */
	private static boolean isDouble(String s) {
	    try { 
	        Double.parseDouble(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    return true;
	}
	
	/**
	 * Clears the builder elements.
	 */
	private void clearBuilder() {
		builder.setLength(0);
	}
	
	/**
	 * Tests if the last character in builder is space or it doesn't exist (there are no characters in builder).
	 * 
	 * @return <code>true</code> if last character is space or the builder is empty, <code>false</code> otherwise
	 */
	private boolean lastCharIsSpaceOrEmpty() {
		return builder.length() == 0 || builder.charAt(builder.length()-1) == ' ';
	}
	
	/**
	 * Method doing actual parsing. Converts document body string into Nodes with all the Tokens.
	 * 
	 * @throws SmartScriptParserException If something is wrong with document body.
	 */
	private void parse() throws SmartScriptParserException {
		int state = INITIAL;
		
		InputStream inputStream = new ByteArrayInputStream(this.documentBody.getBytes());
		Reader reader = new InputStreamReader(inputStream);
		
		documentNode = new DocumentNode();
		stack.push(documentNode);
		builder = new StringBuilder();
		
		try {
			int data = reader.read();
			while(data != -1){ // If there is more data to read
			    char c = (char) data;
			    switch(state) {
			    	case INITIAL:
			    		if(c == '\\') {
			    			state = ESCAPED_TEXT;
			    		} else if(c == '[') {
			    			state = TAG_OPEN_1;
			    		} else {
			    			builder.append(c);
			    			state = NODE_TEXT;
			    		}
			    	break;
			    	case ESCAPED_TEXT:
			    		if(c == '\\' || c == '[') {
			    			builder.append(c);
			    		} else {
			    			builder.append('\\');
			    			builder.append(c);
			    		}
			    		state = NODE_TEXT;
			    	break;
			    	case NODE_TEXT:
			    		if(c == '[') {
			    			this.addTextNode();
			    			this.clearBuilder();
			    			
			    			state = TAG_OPEN_1;
			    		} else if(c == '\\') {
			    			state = ESCAPED_TEXT;
			    		} else {
			    			builder.append(c);
			    		}
			    	break;
			    	case TAG_OPEN_1:
			    		if(c == '$') {
			    			state = TAG_OPEN;
			    		} else {
			    			throw new SmartScriptParserException("Opening tag exception. '$' must come immediately after '['.");
			    		}
			    	break;
			    	case TAG_OPEN:
			    		if(c == ' ') {
			    			// Do nothing
			    		} else if(Character.isLetter(c)) { // FOR, END
			    			builder.append(c);
			    			state = NODE_COMMAND;
			    		} else if(c == '=') { // empty tag
			    			this.clearBuilder();
			    			state = NODE_ECHO;
			    		} else {
			    			throw new SmartScriptParserException("Command exception. Got '" + c + "' instead of space or letter.");
			    		}
			    	break;
			    	case NODE_ECHO:
			    		if(c == ' ') {
			    			// Do nothing
			    		} else if (c == '@') {
			    			builder.append(c);
			    			state = ECHO_FUNCTION;
			    		} else if (Character.isDigit(c) || c == '-' || c == '+' || c == '*' || c == '/') { // Number
			    			builder.append(c);
			    			state = ECHO_NUMBER_OR_OPERATOR;
			    		} else if (c == '"') {
			    			builder.append(c);
			    			state = ECHO_STRING;
			    		} else if (Character.isLetter(c)) {
			    			builder.append(c);
			    			state = ECHO_VARIABLE;
			    		} else if (c == '$') {
			    			this.addEchoNode();
			    			this.clearBuilder();
			    			state = TAG_CLOSE_1;
			    		} else {
			    			throw new SmartScriptParserException("EchoNode can accept @, number, letter or +, -, * and /.");
			    		}
			    	break;
			    	case ECHO_VARIABLE:
			    		if(Character.isLetter(c) || Character.isDigit(c) || c == '_') {
		    				builder.append(c);
		    			} else if (c == ' ') {
		    				addEchoNodeToken();
			    			this.clearBuilder();
			    			
		    				state = NODE_ECHO;
		    			} else if (c == '$') {
		    				addEchoNodeToken();
			    			this.clearBuilder();
			    			
			    			this.addEchoNode();
			    			this.clearBuilder();
			    			state = TAG_CLOSE_1;
			    		} else {
		    				throw new SmartScriptParserException("Variable name can contain letters, numbers or underscores.");
		    			}
			    	break;
			    	case ECHO_STRING:
			    		if(c == '\\') {
			    			state = ESCAPED_STRING;
			    		} else if (c == '"') {
			    			builder.append(c);
			    			
			    			addEchoNodeToken();
			    			this.clearBuilder();
			    			
		    				state = NODE_ECHO;
			    		} else if (c == '$') {
			    			addEchoNodeToken();
			    			this.clearBuilder();
			    			
			    			this.addEchoNode();
			    			this.clearBuilder();
			    			state = TAG_CLOSE_1;
			    		} else {
			    			builder.append(c);
			    		}
			    	break;
			    	case ESCAPED_STRING:
			    		if(c == '\\' || c == '"') {
			    			builder.append(c);
			    		} else if (c == 'n') {
			    			builder.append('\n');
			    		} else if (c == 'r') {
			    			builder.append('\r');
			    		} else if (c == 't') {
			    			builder.append('\t');
			    		} else {
			    			builder.append('\\');
			    			builder.append(c);
			    		}
			    		state = ECHO_STRING;
			    	break;
			    	case ECHO_NUMBER_OR_OPERATOR:
			    		if(Character.isDigit(c) || c == '.') {
			    			builder.append(c);
			    		} else if (c == ' ') {
			    			addEchoNodeToken();
			    			this.clearBuilder();
			    			
		    				state = NODE_ECHO;
			    		} else if (c == '$') {
			    			addEchoNodeToken();
			    			this.clearBuilder();
			    			
			    			this.addEchoNode();
			    			this.clearBuilder();
			    			state = TAG_CLOSE_1;
			    		} else {
			    			throw new SmartScriptParserException("Number can contain minus sign, decimal dot or digits. Operators must be separated with spaces.");
			    		}
			    	break;
			    	case ECHO_FUNCTION:
			    		if(builder.length() == 0) {
			    			if(Character.isLetter(c)) {
			    				builder.append(c);
			    			} else {
			    				throw new SmartScriptParserException("Function must start with letter only.");
			    			}
			    		} else if (builder.length() != 0) {
			    			if(Character.isLetter(c) || Character.isDigit(c) || c == '_') {
			    				builder.append(c);
			    			} else if (c == ' ') {
			    				addEchoNodeToken();
				    			this.clearBuilder();
				    			
			    				state = NODE_ECHO;
			    			} else if (c == '$') {
			    				addEchoNodeToken();
				    			this.clearBuilder();
				    			
				    			this.addEchoNode();
				    			this.clearBuilder();
				    			state = TAG_CLOSE_1;
				    		} else {
			    				throw new SmartScriptParserException("Function can contain letters, numbers or underscores.");
			    			}
			    		}
			    	break;
			    	case TAG_CLOSE_1:
			    		if(c == ']') {
			    			this.clearBuilder();
			    			state = INITIAL;
			    		} else {
			    			throw new SmartScriptParserException("Closing tag exception. ']' must come immediately after '$'.");
			    		}
			    	break;
			    	case NODE_COMMAND:
			    		if (c == ' ') {
			    			// Check if command exists
			    			if(builder.toString().equals("FOR")) {
			    				this.clearBuilder();
			    				state = NODE_FOR;
			    			} else if(builder.toString().equals("END")) {
			    				this.clearBuilder();
			    				state = NODE_END;
			    			} else {
			    				throw new SmartScriptParserException("Unknown command exception.");
			    			}
			    		} else if(Character.isLetter(c)) {
			    			builder.append(c);
			    		} else if(c == '$') {
			    			this.clearBuilder();
			    			
			    			if(stack.isEmpty()) {
			    				throw new SmartScriptParserException("Too much 'END's.");
			    			} else {
			    				this.stack.pop();
				    			state = TAG_CLOSE_1;
			    			}
			    		} else {
			    			throw new SmartScriptParserException("Command exception. Command can contain only letters.");
			    		}
			    	break;
			    	case NODE_END:
			    		if (c == ' ') {
			    			// Do nothing
			    		} else if (c == '$') {
			    			if(stack.isEmpty()) {
			    				throw new SmartScriptParserException("Too much 'END's.");
			    			} else {
			    				this.stack.pop();
				    			state = TAG_CLOSE_1;
			    			}
			    		} else {
			    			throw new SmartScriptParserException("Command exception. 'END' doesn't have any parameters.");
			    		}
			    	break;
			    	case NODE_FOR:
			    		if (c == ' ' && this.lastCharIsSpaceOrEmpty()) {
			    			// Do nothing
			    		} else if (c == ' ' && !this.lastCharIsSpaceOrEmpty()) {
			    			builder.append(c);
			    			state = FOR_START;
			    		} else if (Character.isLetter(c)) {
			    			builder.append(c);
			    		} else if (c == '_' || Character.isDigit(c) && builder.length() > 0) {
			    			builder.append(c);
			    		} else {
			    			throw new SmartScriptParserException("First parameter of the forLoop must be TokenVariable. Can't accept " + c + ".");
			    		}
			    	break;
			    	case FOR_START:
			    		if (c == ' ' && this.lastCharIsSpaceOrEmpty()) {
			    			// Do nothing
			    		} else if (c == ' ' && !this.lastCharIsSpaceOrEmpty()) {
			    			builder.append(c);
			    			state = FOR_END;
			    		} else {
			    			builder.append(c);
			    		}
			    	break;
			    	case FOR_END:
			    		if (c == ' ' && this.lastCharIsSpaceOrEmpty()) {
			    			// Do nothing
			    		} else if (c == ' ' && !this.lastCharIsSpaceOrEmpty()) {
			    			builder.append(c);
			    			state = FOR_STEP;
			    		} else if (c == '$' && !this.lastCharIsSpaceOrEmpty()) {
			    			ForLoopNode node = this.addForLoopNode();
			    			stack.push(node);
			    			this.clearBuilder();
			    			
			    			state = TAG_CLOSE_1;
			    		} else {
			    			builder.append(c);
			    		}
			    	break;
			    	case FOR_STEP:
			    		if (c == ' ' && this.lastCharIsSpaceOrEmpty()) {
			    			// Do nothing
			    		} else if (c == ' ' && !this.lastCharIsSpaceOrEmpty()) {
			    			// Waiting for closing tag
			    			ForLoopNode node = this.addForLoopNode();
			    			stack.push(node);
			    			this.clearBuilder();
			    		} else if (c == '$') {
			    			if(builder.length() > 0) { // If you didn't already added forLoop in case of space after last parameter (see upper if)
			    				ForLoopNode node = this.addForLoopNode();
				    			stack.push(node);
				    			this.clearBuilder();
			    			}
			    			
			    			state = TAG_CLOSE_1;
			    		} else if(c == '-' || c == '.' || Character.isDigit(c) || Character.isLetter(c)) {
			    			if(builder.length() == 0) { // I have all 4 parameters already set-up
			    				throw new SmartScriptParserException("For-loop exception. FOR loop cannot have more than 4 parameters.");
			    			} else {
			    				builder.append(c);
			    			}
			    		} else {
			    			builder.append(c);
			    		}
			    	break;
			    }
			    
			    data = reader.read();
			}
			reader.close();
			
			// In case you stopped at text at the end of reading
			if(state == NODE_TEXT) {
				this.addTextNode();
			}
			
			// Stack must contain single DocumentNode
			if(stack.isEmpty()) {
				throw new SmartScriptParserException("Too much 'END's.");
			} else {
				// Pop DocumentNode
				stack.pop();
			}
			
			// Now the stack should be empty, otherwise there are some missing END's
			if(!stack.isEmpty()) {
				throw new SmartScriptParserException("Missing " + stack.size() + " 'END's.");
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new SmartScriptParserException("Error reading a file/string.");
		}
	}

	/**
	 * @return Main node which contains complete parsed document.
	 */
	public DocumentNode getDocumentNode() {
		return documentNode;
	}
}