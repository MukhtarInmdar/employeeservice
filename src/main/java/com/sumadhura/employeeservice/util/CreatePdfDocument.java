package com.sumadhura.employeeservice.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class CreatePdfDocument {

	
	public static void main(String[] args) {
		try {
			  // create document
	        Document document = new Document(PageSize.A4, 36, 36, 90, 36);
	        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("E:\\CUSTOMERAPP_CUG\\images\\sumadhura_projects_images\\documents\\Preview\\124\\894HeaderFooter.pdf"));

	        // add header and footer
	        PdfHeader event = new PdfHeader();
	        writer.setPageEvent(event);

	        // write to document
	        document.open();
	        document.add(new Paragraph("Adding a header to PDF Document using iText."));
	        document.newPage();
	        document.add(new Paragraph("Adding a footer to PDF Document using iText."));
	        document.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
   
}
