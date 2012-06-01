package hr.fer.zemris.java.javadz10.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Generates Excel file from the provided parameters
 * (n - number of pages, a - starting number, b - ending
 * number). Every page has two columns - first are numbers
 * from a to b, second column is number to the power of
 * current page.
 */
public class Powers extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String parameterA = req.getParameter("a");
		String parameterB = req.getParameter("b");
		String parameterN = req.getParameter("n");
		
		int a = 0;
		int b = 0;
		int n = 0;
		
		try {
			a = Integer.parseInt(parameterA);
		} catch(NumberFormatException ignorable) {}
		
		try {
			b = Integer.parseInt(parameterB);
		} catch(NumberFormatException ignorable) {}
		
		try {
			n = Integer.parseInt(parameterN);
		} catch(NumberFormatException ignorable) {}
		
		// Check for boundaries
		if((a < -100 || a > 100) || (b < -100 || b > 100) || (n < 1 || n > 5)) {
			req.setAttribute("errorMessage", "Parameters 'a', 'b' ili 'n' are invalid! They should be in these boundaries - a[-100,100], b[-100,100] i n [1,5].");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}
		
		req.setAttribute("parameterA", a);
		req.setAttribute("parameterB", b);
		req.setAttribute("parameterN", n);
		
		// Set the file name and content type (for proper download)
		resp.setContentType("application/octet-stream");
		resp.setHeader("Content-Disposition", "attachment;filename=powers.xls");
		
		generateExcelFile(n, a, b).write(resp.getOutputStream());
	}
	
	public HSSFWorkbook generateExcelFile(int noPages, int a, int b) {
		HSSFWorkbook hwb = new HSSFWorkbook();
		
		for(int i = 0; i < noPages; i++) {
			HSSFSheet page = hwb.createSheet("page " + (i+1));
			
			int rowNumber = 0;
			for(int number = a; number <= b; number++) {
				HSSFRow row = page.createRow(rowNumber++);
				row.createCell(0).setCellValue(number);
				row.createCell(1).setCellValue(Math.pow(number, i+1));
			}
		}

		return hwb;
	}
}
