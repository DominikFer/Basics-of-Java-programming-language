package hr.fer.zemris.java.custom.scripting.demo;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;
import hr.fer.zemris.java.webserver.RequestContext;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *	Class to test {@link SmartScriptEngine} class.
 */
public class DemoSmartScriptEngine {

	public static void main(String[] args) {
		
		//osnovni();
		//zbrajanje();
		brojPoziva();
		
	}
	
	private static void osnovni() {
		String documentBody = readFromDisk("osnovni.smscr");
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		
		try {
			new SmartScriptEngine(
					new SmartScriptParser(documentBody).getDocumentNode(),
					new RequestContext(
							System.out,
							parameters,
							persistentParameters,
							cookies
					)
			).execute();
		} catch (SmartScriptParserException e) {
			System.out.println("Could not parse document body.");
		}
	}
	
	private static void zbrajanje() {
		String documentBody = readFromDisk("zbrajanje.smscr");
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		parameters.put("a", "4");
		parameters.put("b", "2");
		
		try {
			new SmartScriptEngine(
					new SmartScriptParser(documentBody).getDocumentNode(),
					new RequestContext(
							System.out,
							parameters,
							persistentParameters,
							cookies
					)
			).execute();
		} catch (SmartScriptParserException e) {
			System.out.println("Could not parse document body.");
		}
	}
	
	private static void brojPoziva() {
		String documentBody = readFromDisk("brojPoziva.smscr");
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		persistentParameters.put("brojPoziva", "3");
		
		RequestContext rc = new RequestContext(
				System.out,
				parameters,
				persistentParameters,
				cookies
		);
		
		try {
			new SmartScriptEngine(new SmartScriptParser(documentBody).getDocumentNode(), rc).execute();
		} catch (SmartScriptParserException e) {
			System.out.println("Could not parse document body.");
		}
		
		System.out.println("Vrijednost u mapi: " + rc.getPersistentParameter("brojPoziva"));
	}
	
	/**
	 * Read the file content from the disk.
	 * 
	 * @param fileName		File name of the content you want to read.
	 * @return				File content.
	 */
	private static String readFromDisk(String fileName) {
		File file = new File(fileName);
	    StringBuilder fileBody = new StringBuilder((int)file.length());
	    Scanner scanner = null;
		try {
			scanner = new Scanner(file, "UTF-8");
		} catch (FileNotFoundException e) {
			System.out.println("Could not open file '" + fileName + "'.");
		}
		
	    String lineSeparator = System.getProperty("line.separator");
	    while(scanner.hasNextLine()) {        
            fileBody.append(scanner.nextLine() + lineSeparator);
        }
	    
	    scanner.close();
	    
	    return fileBody.toString();
	}
}
