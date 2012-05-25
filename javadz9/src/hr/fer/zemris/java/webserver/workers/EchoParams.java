package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

/**
 *	Demo class used to write out the provided URL parameters
 *	into simple HTML table-
 */
public class EchoParams implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) {
		
		context.setMimeType("text/html");
		
		Map<String, String> parameters = context.getParameters();
		try {
			context.write("<html><body>");
			context.write("<table border=\"1\">");
			
			context.write("<tr><td>Ime parametra</td><td>Vrijednost</td></tr>");
			
			for(Entry<String, String> parameter : parameters.entrySet()) {
				context.write("<tr><td>" + parameter.getKey() + "</td><td>" + parameter.getValue() + "</td></tr>");
			}
			
			context.write("</table>");
			context.write("</body></html>");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
	}

}
