package hr.fer.zemris.java.javadz10.filters;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

@WebFilter("/stories/*")
public class StoryFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		// Show the story only if the minute time is even number
		boolean evenMinutes = true;
		Calendar calendar = Calendar.getInstance();
		if(calendar.get(Calendar.MINUTE) % 2 != 0) {
			evenMinutes = false;
		}
		
		if(evenMinutes) {
			System.out.println("Minute is even! Show the story.");
			chain.doFilter(req, resp);
		} else {
			System.out.println("Minute is odd! Show an error.");
			req.setAttribute("errorMessage", "You can see the story only if minute time is even.");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
		}
	}

	@Override
	public void destroy() {}

}
