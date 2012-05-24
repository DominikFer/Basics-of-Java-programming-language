package hr.fer.zemris.java.custom.scripting.exec;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.tokens.Token;
import hr.fer.zemris.java.custom.scripting.tokens.TokenConstantDouble;
import hr.fer.zemris.java.custom.scripting.tokens.TokenConstantInteger;
import hr.fer.zemris.java.custom.scripting.tokens.TokenConstantString;
import hr.fer.zemris.java.custom.scripting.tokens.TokenFunction;
import hr.fer.zemris.java.custom.scripting.tokens.TokenOperator;
import hr.fer.zemris.java.custom.scripting.tokens.TokenVariable;
import hr.fer.zemris.java.webserver.RequestContext;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Stack;

public class SmartScriptEngine {

	private DocumentNode documentNode;
	private RequestContext requestContext;
	private ObjectMultistack multistack = new ObjectMultistack();
	
	private INodeVisitor visitor = new INodeVisitor() {
		
		@Override
		public void visitTextNode(TextNode node) {
			try {
				requestContext.write(node.getValue());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void visitForLoopNode(ForLoopNode node) {
			ValueWrapper finalValue = new ValueWrapper(node.getEndExpression().asText());
			ValueWrapper stepValue = new ValueWrapper(node.getStepExpression().asText());
			ValueWrapper currentValue = null;
			
			Token initialValue = node.getStartExpression();
			multistack.push(node.getVariable().getName(), new ValueWrapper(initialValue.asText()));
			
			do {
				for(int i = 0; i < node.numberOfChildren(); i++) {
					node.getChild(i).accept(visitor);
				}
				
				currentValue = multistack.pop(node.getVariable().getName());
				currentValue.increment(stepValue.getValue());
				multistack.push(node.getVariable().getName(), currentValue);
			} while(currentValue.numCompare(finalValue.getValue()) != 1);
			
			multistack.pop(node.getVariable().getName());
		}
		
		@Override
		public void visitEchoNode(EchoNode node) {
			Token[] tokens = node.getTokens();
			Stack<Object> stack = new Stack<Object>();
			
			for(int i = 0; i < tokens.length; i++) {
				Token currentToken = tokens[i];
				if(currentToken instanceof TokenConstantDouble) {
					stack.push(((TokenConstantDouble) currentToken).getValue());
				} else if(currentToken instanceof TokenConstantInteger) {
					stack.push(((TokenConstantInteger) currentToken).getValue());
				} else if(currentToken instanceof TokenConstantString) {
					stack.push(((TokenConstantString) currentToken).getValue());
				} else if(currentToken instanceof TokenVariable) {
					String variableName = ((TokenVariable) currentToken).getName();
					stack.push(multistack.peek(variableName).getValue());
				} else if(currentToken instanceof TokenOperator) {
					String symbol = ((TokenOperator) currentToken).getSymbol();
					
					ValueWrapper valueOne = new ValueWrapper(stack.pop());
					ValueWrapper valueTwo = new ValueWrapper(stack.pop());
					
					if(symbol.equals("+")) {
						valueOne.increment(valueTwo.getValue());
					} else if(symbol.equals("-")) {
						valueOne.decrement(valueTwo.getValue());
					} else if(symbol.equals("*")) {
						valueOne.multiply(valueTwo.getValue());
					} else if(symbol.equals("/")) {
						valueOne.divide(valueTwo.getValue());
					}
					
					stack.push(valueOne.getValue());
				} else if(currentToken instanceof TokenFunction) {
					String function = ((TokenFunction) currentToken).getName();
					if(function.startsWith("sin")) {
						ValueWrapper x = new ValueWrapper(stack.pop());
						double result = Math.sin(Double.parseDouble(x.getValue().toString())/360.0*Math.PI*2);
						stack.push(result);
					} else if(function.startsWith("decfmt")) {
						DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.UK);
						otherSymbols.setDecimalSeparator('.');
						otherSymbols.setGroupingSeparator(','); 
						
						String decimalFormat = (String) stack.pop();
						DecimalFormat f = new DecimalFormat(decimalFormat, otherSymbols);
						ValueWrapper x = new ValueWrapper(stack.pop());
						String r = f.format(x.getValue());
						stack.push(r);
					} else if(function.startsWith("dup")) {
						ValueWrapper x = new ValueWrapper(stack.pop());
						stack.push(x.getValue());
						stack.push(x.getValue());
					} else if(function.startsWith("setMimeType")) {
						String x = (String) stack.pop();
						requestContext.setMimeType(x);
					} else if(function.startsWith("paramGet")) {
						Object defaultValue = stack.pop();
						String name = (String) stack.pop();
						
						Object value = requestContext.getParameter(name);
						
						stack.push(value != null ? value : defaultValue);
					} else if(function.startsWith("pparamGet")) {
						Object defaultValue = stack.pop();
						String name = (String) stack.pop();
						
						Object value = requestContext.getPersistentParameter(name);
						
						stack.push(value != null ? value : defaultValue);
					} else if(function.startsWith("pparamSet")) {
						String name = (String) stack.pop();
						Object value = stack.pop();
						
						requestContext.setPersistentParameter(name, value.toString());
					}
				}
			}
			
			for(Object object : stack) {
				try {
					requestContext.write(object.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		@Override
		public void visitDocumentNode(DocumentNode node) {
			for(int i = 0; i < node.numberOfChildren(); i++) {
				node.getChild(i).accept(visitor);
			}
		}
	};
	
	public SmartScriptEngine(DocumentNode documentNode, RequestContext requestContext) {
		this.documentNode = documentNode;
		this.requestContext = requestContext;
	}
	
	public void execute() {
		documentNode.accept(visitor);
	}
	
}
