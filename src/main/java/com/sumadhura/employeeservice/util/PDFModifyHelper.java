package com.sumadhura.employeeservice.util;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;

import org.springframework.stereotype.Component;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.sumadhura.employeeservice.exception.EmployeeFinancialServiceException;

/**
 * 
 * @author Aniket Chavan
 * @since 10-09-2020
 * @time 6:30 PM
 * @description this class will help u to modify the PDF content
 */
@Component("PdfModifyHelper")
public class PDFModifyHelper {

	public static void main(String args[]) throws  IOException {
		System.out.println("PDFModifyHelper.main()");
		
		/*PDDocument document = null;
        document = PDDocument.load(new File("H:\\ACP\\Project\\1 Live Project\\PDF EDT\\A1001-Sumadhura Eden Garden-On Booking.pdf"));
        document = replaceText(document,"HDFC Bank", "State Bank of India1");
        document.save("H:\\ACP\\Project\\1 Live Project\\PDF EDT\\ModifiedTestFile.pdf");
        document.close();*/
		
        editPDFFile();
        System.out.println("PDFModifyHelper.main()");				
	}
	
/*    @SuppressWarnings("unused")
	private static PDDocument replaceText(PDDocument document, String searchString, String replacement) throws IOException {
    	
    	<dependency>
        <groupId>org.apache.pdfbox</groupId>
        <artifactId>pdfbox</artifactId>
        <version>2.0.11</version>
    </dependency>
    	
        if (StringUtils.isEmpty(searchString) || StringUtils.isEmpty(replacement)) {
            return document;
        }

        for (PDPage page : document.getPages()) {
            PDFStreamParser parser = new PDFStreamParser(page);
            parser.parse();
            List<?> tokens = parser.getTokens();

            for (int j = 0; j < tokens.size(); j++) {
                Object next = tokens.get(j);
                if (next instanceof Operator) {
                    Operator op = (Operator) next;

                    String pstring = "";
                    int prej = 0;

                    if (op.getName().equals("Tj")) {
                        COSString previous = (COSString) tokens.get(j - 1);
                        String string = previous.getString();
                        string = string.replaceFirst(searchString, replacement);
                        previous.setValue(string.getBytes());
                    } else if (op.getName().equals("TJ")) {
                        COSArray previous = (COSArray) tokens.get(j - 1);
                        for (int k = 0; k < previous.size(); k++) {
                            Object arrElement = previous.getObject(k);
                            if (arrElement instanceof COSString) {
                                COSString cosString = (COSString) arrElement;
                                String string = cosString.getString();

                                if (j == prej) {
                                    pstring += string;
                                } else {
                                    prej = j;
                                    pstring = string;
                                }
                            }
                        }

                        if (searchString.equals(pstring.trim())) {
                            COSString cosString2 = (COSString) previous.getObject(0);
                            cosString2.setValue(replacement.getBytes());

                            int total = previous.size() - 1;
                            for (int k = total; k > 0; k--) {
                                previous.remove(k);
                            }
                        }
                    }
                }
            }
            PDStream updatedStream = new PDStream(document);
            OutputStream out = updatedStream.createOutputStream(COSName.FLATE_DECODE);
            ContentStreamWriter tokenWriter = new ContentStreamWriter(out);
            tokenWriter.writeTokens(tokens);
            out.close();
            page.setContents(updatedStream);
        }

        return document;
    }
*/	
    @SuppressWarnings("unused")
	private static void editPDFFile() {
		try {
			// Create PdfReader instance. input file
			PdfReader pdfReader = new PdfReader("D:\\CustomerApp_CUG\\3529\\26400\\J1305-Sumadhura Eden Garden-Within 30 days from the day of booking.pdf");

			// Create PdfStamper instance. output file
			PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream("D:\\CustomerApp_CUG\\modified\\26400\\J1305-Sumadhura Eden Garden-Within 30 days from the day of booking.pdf"));

			// Create BaseFont instance.
			//BaseFont baseFont = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

			// Get the number of pages in pdf.
			int pages = pdfReader.getNumberOfPages();
			PRStream stream;
			//final double res = 72; // PDF units are at 72 DPI
		    
			// Iterate the pdf through pages.
			for (int i = 1; i <= pages; i++) {
				// Contain the pdf data.
/*				PdfContentByte pageContentByte = pdfStamper.getOverContent(i);

				pageContentByte.beginText();
				// Set text font and size.
				pageContentByte.setFontAndSize(baseFont, 14);

				pageContentByte.setTextMatrix(50, 740);

				// Write text
				pageContentByte.showText("State Bank of India");
				pageContentByte.endText();*/
				
				PdfDictionary dict = pdfReader.getPageN(i);
				PdfObject object = dict.getDirectObject(PdfName.CONTENTS);
				//PdfObject tableObject = pdfReader.getPageN(i).get(PdfName.TABLE);
				//PdfObject tableObjectq = dict.getDirectObject(PdfName.TABLE);
				int msFound = 0;
				StringBuffer buffer = new StringBuffer("");
				if (object instanceof PRStream) {
					stream = (PRStream) object;
					byte[] data = PdfReader.getStreamBytes(stream);
					String dd = new String(data);
					BufferedReader reader = new BufferedReader(new StringReader(dd));
					//String[] lines = dd.split(System.getProperty("line.separator"));
					String line=null;
					while((line=reader.readLine()) != null) {
//						
//						if(line.contains("On Foundation") && !line.contains("\"On Foundation") && !line.contains("(On Foundation")) {
//							System.out.println(" line "+line);
//							msFound=1;
//						} else if(line.contains("%")  && msFound == 1) {
//								msFound = 2;
//						} else if(line.contains("07-05")  && msFound == 2) {
//							msFound = 3;
//						} else if(line.contains("22-05") && msFound == 3 ) {
//							line = line.replaceAll("22-05-2021","25-05-2021");
//							msFound = 4;
//						}
//						line = line.replaceAll("On Foundation of Respective Block", "Axis Bank");
//						line = line.replaceAll("HDFC Bank", "Axis Bank");
						if(line.contains("Mr")) {
							System.out.println(" line "+line);
							line = line.replaceAll("Mr","MS");
						}
						System.out.println("PDFModifyHelper.main() "+line);
						buffer.append(line);
						buffer.append(System.getProperty("line.separator"));
					}
//					if(msFound != 4) {
//						throw new EmployeeFinancialServiceException("Hello");
//					}
					/*dd = dd.replaceAll("On Foundation of Respective Block", "Axis Bank");
					dd = dd.replaceAll("HDFC Bank", "State Bank of India1");*/
					stream.setData(buffer.toString().getBytes());
					//System.out.println("PDFModifyHelper.main() "+dd);
				}
			}

			// Close the pdfStamper.
			pdfStamper.close();
			pdfReader.close();
			System.out.println("PDF modified successfully.");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void processPDF(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        int pNumbers = reader.getNumberOfPages();
        PRStream stream;
        for (int index= 1 ; index <= pNumbers;index++){
            PdfDictionary  dict = reader.getPageN(index);
            PdfObject  object = dict.getDirectObject(PdfName.CONTENTS);
            if (object instanceof PRStream) {
                  stream = (PRStream) object;
                byte[] data = PdfReader.getStreamBytes(stream);
                String dd = new String(data);
                dd = dd.replaceAll("old_text", "old_text");
                stream.setData(dd.getBytes());
            }
        }

        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        stamper.close();
        reader.close();
    }
	
	@SuppressWarnings("deprecation")
	public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
	//not using
		PdfReader reader = new PdfReader(src);
	    PdfDictionary dict = reader.getPageN(1);
	    //PdfObject object = dict.getDirectObject(PdfName.CONTENTS);
	    PdfArray refs = null;
	    if (dict.get(PdfName.CONTENTS).isArray()) {
	        refs = dict.getAsArray(PdfName.CONTENTS);
	    } else if (dict.get(PdfName.CONTENTS).isIndirect()) {
	        refs = new PdfArray(dict.get(PdfName.CONTENTS));
	    }
	    for (int i = 0; i < refs.getArrayList().size(); i++) {
	        PRStream stream = (PRStream) refs.getDirectObject(i);
	        byte[] data = PdfReader.getStreamBytes(stream);
	        stream.setData(new String(data).replace("NULA", "Nulo").getBytes());
	    }
	    PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
	    stamper.close();
	    reader.close();
	}
	
	
	 private static void editPDFFile1() {
			try {
				// Create PdfReader instance.
				PdfReader pdfReader = new PdfReader("D:\\CustomerApp_CUG\\images\\NOC_D206_Bescom.pdf");

				// Create PdfStamper instance.
				PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream("D:\\CustomerApp_CUG\\NOC_D206_Bescom.pdf"));

				// Create BaseFont instance.
				//BaseFont baseFont = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

				// Get the number of pages in pdf.
				int pages = pdfReader.getNumberOfPages();
				PRStream stream;
				//final double res = 72; // PDF units are at 72 DPI
			    
				// Iterate the pdf through pages.
				for (int i = 1; i <= pages; i++) {
					// Contain the pdf data.
	/*				PdfContentByte pageContentByte = pdfStamper.getOverContent(i);

					pageContentByte.beginText();
					// Set text font and size.
					pageContentByte.setFontAndSize(baseFont, 14);

					pageContentByte.setTextMatrix(50, 740);

					// Write text
					pageContentByte.showText("State Bank of India");
					pageContentByte.endText();*/
					
					PdfDictionary dict = pdfReader.getPageN(i);
					PdfObject object = dict.getDirectObject(PdfName.CONTENTS);
					//PdfObject tableObject = pdfReader.getPageN(i).get(PdfName.TABLE);
					//PdfObject tableObjectq = dict.getDirectObject(PdfName.TABLE);
					int msFound = 0;
					StringBuffer buffer = new StringBuffer("");
					if (object instanceof PRStream) {
						stream = (PRStream) object;
						byte[] data = PdfReader.getStreamBytes(stream);
						String dd = new String(data);
						BufferedReader reader = new BufferedReader(new StringReader(dd));
						//String[] lines = dd.split(System.getProperty("line.separator"));
						String line=null;
						while((line=reader.readLine()) != null) {
							
							if(line.contains("On Foundation") && !line.contains("\"On Foundation") && !line.contains("(On Foundation")) {
								System.out.println(" line "+line);
								msFound=1;
							} else if(line.contains("%")  && msFound == 1) {
									msFound = 2;
							} else if(line.contains("07-05")  && msFound == 2) {
								msFound = 3;
							} else if(line.contains("22-05") && msFound == 3 ) {
								line = line.replaceAll("22-05-2021","25-05-2021");
								msFound = 4;
							}
							line = line.replaceAll("24/11/2021", "25/11/2021");
							System.out.println("PDFModifyHelper.main() "+line);
							buffer.append(line);
							buffer.append(System.getProperty("line.separator"));
						}
						if(msFound != 4) {
							throw new EmployeeFinancialServiceException("Hello");
						}
						/*dd = dd.replaceAll("On Foundation of Respective Block", "Axis Bank");
						dd = dd.replaceAll("HDFC Bank", "State Bank of India1");*/
						stream.setData(buffer.toString().getBytes());
						//System.out.println("PDFModifyHelper.main() "+dd);
					}
				}

				// Close the pdfStamper.
				pdfStamper.close();
				pdfReader.close();
				System.out.println("PDF modified successfully.");
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	 
}
