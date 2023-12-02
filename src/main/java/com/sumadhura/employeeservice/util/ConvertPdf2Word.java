package com.sumadhura.employeeservice.util;

import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

public class ConvertPdf2Word {
	public static void main(String[] args) throws IOException {
		System.out.println("Document converted started");
		XWPFDocument doc = new XWPFDocument();
		String pdf = "E:\\CUSTOMERAPP_CUG\\images\\sumadhura_projects_images\\documents\\Preview\\124\\894\\Agreement Draft - Sumadhura Sushantham Phase 1 - A803_1.pdf";
		PdfReader reader = new PdfReader(pdf);
		PdfReaderContentParser parser = new PdfReaderContentParser(reader);
		for (int i = 1; i <= reader.getNumberOfPages(); i++) {
			TextExtractionStrategy strategy = parser.processContent(i,
					new SimpleTextExtractionStrategy());
			String text = strategy.getResultantText();
			XWPFParagraph p = doc.createParagraph();
			XWPFRun run = p.createRun();
			run.setText(text);
			run.addBreak(BreakType.PAGE);
		}
		FileOutputStream out = new FileOutputStream("E:\\CUSTOMERAPP_CUG\\images\\sumadhura_projects_images\\documents\\Preview\\124\\894\\javadomain.docx");
		doc.write(out);
		out.close();
		reader.close();
		System.out.println("Document converted successfully");
	}
}