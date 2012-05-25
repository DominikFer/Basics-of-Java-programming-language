package hr.fer.zemris.java.webserver;

/**
 *	Interface which describes the web workers process
 *	request method.
 */
public interface IWebWorker {

	public void processRequest(RequestContext context);
	
}
