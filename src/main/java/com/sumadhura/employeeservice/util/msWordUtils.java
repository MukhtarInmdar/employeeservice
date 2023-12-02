package com.sumadhura.employeeservice.util;
import com.spire.pdf.FileFormat;
import com.spire.pdf.PdfDocument;



public class msWordUtils {
	
	public static void main(String[] args) {
		/*try {
			FileOutputStream fileOutputStream = new FileOutputStream("D:\\CUSTOMERAPP_CUG\\images\\sumadhura_projects_images\\documents\\Preview\\124\\894\\Agreement.docx");
			XWPFDocument xwpfDocument = new XWPFDocument();
			XWPFParagraph xwpfParagraph = xwpfDocument.createParagraph();
			xwpfParagraph.setAlignment(ParagraphAlignment.CENTER);
			XWPFRun xwpfRun = xwpfParagraph.createRun();
			xwpfRun.setBold(true);
			xwpfRun.setFontSize(20);
			xwpfRun.setText("Welcome to Apache POI");
			xwpfDocument.write(fileOutputStream);
			fileOutputStream.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		*/
		/*try {
			XWPFDocument doc = new XWPFDocument();
			String pdf = "E:\\AMS\\PDFTODOC\\acknowledgementSlip.pdf";
			PdfReader reader = new PdfReader(pdf);
			PdfReaderContentParser parser = new PdfReaderContentParser(reader);
			
			for (int i = 1; i <= reader.getNumberOfPages(); i++) {
			    TextExtractionStrategy strategy =
			      parser.processContent(i, new SimpleTextExtractionStrategy());
			    String text = strategy.getResultantText();
			    XWPFParagraph p = doc.createParagraph();
			    XWPFRun run = p.createRun();
			    run.setText(text);
			    run.addBreak(BreakType.PAGE);
			}
			FileOutputStream out = new FileOutputStream("E:\\\\AMS\\\\PDFTODOC\\\\pdf.docx");
			doc.write(out);
		} catch (Exception e) {
			// TODO: handle exception
		}*/
		
		 //Load a sample PDF document
        PdfDocument pdf = new PdfDocument("E:\\\\AMS\\\\PDFTODOC\\\\acknowledgementSlip.pdf");
 
        //Save the PDF document as Word DOCX format
        pdf.saveToFile("E:\\\\\\\\AMS\\\\\\\\PDFTODOC\\\\\\\\ToWord.docx", FileFormat.DOCX);
	}
	
	
}