package hr.fer.zemris.java.javadz10.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Calculates application uptime and formats it so it can
 * be displayed on the page.
 */
public class AppInfo extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("Start time is " + req.getServletContext().getAttribute("startTime"));
		
		long currentTime = System.currentTimeMillis();
		long startTime = Long.parseLong(req.getServletContext().getAttribute("startTime").toString());
		long difference = currentTime - startTime;
		
		long days  = difference/1000/86400;
		long hours = (difference/1000 - 86400*days) / 3600;
		long minutes  = (difference/1000 - 86400*days - 3600*hours) / 60;
		long seconds  = (difference/1000 - 86400*days - 3600*hours - 60*minutes);
		long milliseconds  = (difference - 86400*days*1000 - 3600*hours*1000 - 60*minutes*1000 - seconds*1000);
		
		String uptime = days + " days, " + hours + " hours, " + minutes + " minutes, " + seconds + " seconds and " + milliseconds + " milliseconds";
		req.setAttribute("uptime", uptime);
		
		req.getRequestDispatcher("/WEB-INF/pages/appinfo.jsp").forward(req, resp);
	}
	
}
