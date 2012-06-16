package hr.fer.zemris.java.tecaj_13;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.Unos;
import hr.fer.zemris.java.tecaj_13.pdf.GeneratePdf;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.DocumentException;

/**
 * Generates PDF of all records in the database.
 */
@WebServlet("/servleti/pdfExport")
public class PDFExport extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Unos> unosi = DAOProvider.getDao().dohvatiDetaljanPopisUnosa();
		try {
			new GeneratePdf(unosi, resp.getOutputStream());
		} catch (DocumentException e) {
			// Error
			req.setAttribute("errorMessage", "PDF export details could not be generated.");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}
	}
}
