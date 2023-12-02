package com.sumadhura.employeeservice.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HeaderFooterPageEvent2 extends PdfPageEventHelper{

	 private String headData;String footerData;
	 private String rightLogo;String leftLogo;
	 private Long headerTextHeight,footerTextHeight,fontSize;
	 private Font fontWeight;
	 private String applyPageNumberinFooter;
	 private String applyPageNumberinHeader;
	 private String filePath;
	 private Integer totalPdfPageNumber;
	 /**
	  * 
	  * @param headData Data for Header Side
	  * @param footerData Data for Footer Side
	  * @param rightLogo rightLogo for pdf
	  * @param headerTextHeight header height
	  * @param footerTextHeight footer height
	  * @param fontSize 
	  * 
	  */
	 public HeaderFooterPageEvent2(String headData,String footerData,String rightLogo,Long headerTextHeight,Long footerTextHeight,Long fontSize) {
		this.headData = headData;
		this.footerData  = footerData;
		this.rightLogo  = rightLogo;
		this.headerTextHeight  = headerTextHeight;
		this.footerTextHeight = footerTextHeight;
		this.fontSize = fontSize;
		this.fontWeight = null;//not in use
	}
	 
	 public HeaderFooterPageEvent2() {
		// TODO Auto-generated constructor stub
		// System.out.println("HeaderFooterPageEvent2 "+filePath);
	}
	@Override
	public void onStartPage(PdfWriter writer, Document document) {
		// TODO Auto-generated method stub
		super.onStartPage(writer, document);
	}
	 
	 int count = 0;
	 //How to restrict the number of characters on a single line?
	public void onEndPage(PdfWriter writer, Document document) {
		Font ffont = null;
		PdfContentByte cb = writer.getDirectContent();
		Phrase header = null;
		Phrase footer = null;
        
		int currentPageNumber = writer.getPageNumber();
		
       /* if (totalPdfPageNumber == null && filePath != null && applyPageNumberinHeader!=null && applyPageNumberinHeader.equalsIgnoreCase("true")) {
	        PdfReader reader = null;
			try {
				PdfCopy copy = new PdfCopy(document, new FileOutputStream(filePath));
				reader = new PdfReader(filePath);
				totalPdfPageNumber = reader.getNumberOfPages();
			} catch (Exception e) {
				System.out.println("HeaderFooterPageEvent2 getMessage "+e.getMessage());
			}
		}*/
        
        System.out.println("currentPageNumber "+currentPageNumber+" totalPdfPageNumber "+totalPdfPageNumber);
        
        if(footerData!=null && headData!=null) {
			ffont = new Font(Font.FontFamily.UNDEFINED, fontSize, org.apache.poi.ss.usermodel.Font.ANSI_CHARSET);
			header = new Phrase(headData, ffont);
			footer = new Phrase(footerData, ffont);
        }
		
		if(footerData!=null && (footerData.contains("SUMADHURA VASAVI") || footerData.contains("INDMAX INFRASTRUCTURE") || footerData.contains("HOMES LLP"))) {
			ffont = new Font(Font.FontFamily.UNDEFINED, fontSize, Font.BOLD);
			footer = new Phrase(footerData, ffont);
		}
			
		if(footerData!=null && footerData.contains("==")) {//to draw the line in PDF
			//adding content to header part, images or text
			addHeader(writer, document);

			ColumnText.showTextAligned(cb, Rectangle.BOTTOM, header, document.left(), document.top() - headerTextHeight,0);

			// instantiating a PdfCanvas object
			PdfContentByte canvas = writer.getDirectContent();
			// drawing horizontal line
			// starting point of the line
			canvas.moveTo(20, 60);

			// Drawing the line till the end point.
			canvas.lineTo(1800, 60);//working
			//canvas.lineTo(1800, 806);

             // close the path stroke 
           canvas.closePathStroke(); 		
		} else if(footerData!=null && headData!=null) {
			//adding header and footer text to pdf pages, at footer and header level
			ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, header,
					 document.left() , document.top() - headerTextHeight, 0);
			
			ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, footer,
					document.left(), document.bottom()-80 + footerTextHeight, 0);
		}/* else if(applyPageNumberinHeader!=null && applyPageNumberinHeader.equalsIgnoreCase("true")) {
			//assigning page number to pdf pages
	           ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT,
	                    new Phrase(String.format("Page %s of %s", currentPageNumber, totalPdfPageNumber)), 559, 806, 0);
		}*/
		/*
		 ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, header,
				(document.right() - document.left()) / 2 + document.leftMargin(), document.top() + headerTextHeight, 0);
				
		ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, footer,
				(document.right() - document.left()) / 2 + document.leftMargin(), document.bottom() - 0, 0);*/
	}

	public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
		//pass source file name, and destination file name to add page numbers
		PdfReader reader = new PdfReader(src);
        int n = reader.getNumberOfPages();
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        PdfContentByte pagecontent;
        for (int i = 0; i < n; ) {
            pagecontent = stamper.getOverContent(++i);
            ColumnText.showTextAligned(pagecontent, Element.ALIGN_RIGHT,
                    new Phrase(String.format("Page %s of %s", i, n)), 559, 806, 0);
        }
        stamper.close();
        reader.close();
    }

	
    private void addHeader(PdfWriter writer, Document document){
    	System.out.println("***** Control inside the HeaderFooterPageEvent.addHeader() *****");
        //PdfPTable header = new PdfPTable(2);
        try {
        	if(Util.isNotEmptyObject(this.rightLogo)) {
        		//Right side image
				Image rightLogo1 = Image.getInstance(this.rightLogo);
				// rightLogo1.setWidthPercentage(10);
				rightLogo1.setAlignment(Element.ALIGN_RIGHT);
	
				rightLogo1.setAbsolutePosition(480, 740);//first param moving img to right side and left side
				//rightLogo1.setAbsolutePosition(480, 745);745 for left side vasavi logo
				//seconf param moving img to up and down sode
				rightLogo1.scaleToFit(90, 90);
				writer.getDirectContent().addImage(rightLogo1);		
        	}
        	
			//Left side image
			if(Util.isNotEmptyObject(this.leftLogo)) {
				//headData field used for image 
				Image leftLogo1 = Image.getInstance(this.leftLogo);
				// rightLogo1.setWidthPercentage(10);
				leftLogo1.setAlignment(Element.ALIGN_LEFT);
	
				leftLogo1.setAbsolutePosition(20, 740);//1st param moving img to right side and 2nd left side
				//leftLogo1.setAbsolutePosition(05, 740);//1st param moving img to right side and 2nd left side
				//seconf param moving img to up and down side
				leftLogo1.scaleToFit(90, 90);
				writer.getDirectContent().addImage(leftLogo1);
			}
        } catch(DocumentException de) {
            throw new ExceptionConverter(de);
        } catch (MalformedURLException e) {
            throw new ExceptionConverter(e);
        } catch (IOException e) {
            throw new ExceptionConverter(e);
        }
    }
}
