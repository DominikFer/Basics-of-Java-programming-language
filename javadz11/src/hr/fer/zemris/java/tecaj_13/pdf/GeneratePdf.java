package hr.fer.zemris.java.tecaj_13.pdf;

import hr.fer.zemris.java.tecaj_13.model.Unos;

import java.util.List;

import javax.servlet.ServletOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Generates a new PDF from all the records in the database.
 */
public class GeneratePdf {
	private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
	private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
	
	public GeneratePdf(List<Unos> unosi, ServletOutputStream outputStream) throws DocumentException {
		Document document = new Document();
		PdfWriter.getInstance(document, outputStream);
		document.open();
		document.addTitle("Lista unosa");
		document.addCreator("Sven Kapuđija");
		
		for(Unos unos : unosi) {
			dodajUnos(document, unos);
		}
		
		document.close();
	}

	/**
	 * Adds a new single record/page.
	 * 	
	 * @param document				
	 * @param unos					
	 * @throws DocumentException
	 */
	private static void dodajUnos(Document document, Unos unos) throws DocumentException {
		Paragraph preface = new Paragraph();
		addEmptyLine(preface, 1);
		
		preface.add(new Paragraph("Unos " + unos.getId(), catFont));
		addEmptyLine(preface, 1);
		preface.add(new Paragraph("Title: " + unos.getTitle(), smallBold));
		preface.add(new Paragraph("Message: " + unos.getMessage(), smallBold));
		preface.add(new Paragraph("User EMail: " + unos.getUserEMail(), smallBold));
		preface.add(new Paragraph("Created on: " + unos.getCreatedOn(), smallBold));

		document.add(preface);
		document.newPage();
	}

	private static void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}
}
