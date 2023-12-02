package com.sumadhura.employeeservice.util;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.docx4j.Docx4J;
import org.docx4j.Docx4jProperties;
import org.docx4j.XmlUtils;
import org.docx4j.convert.in.xhtml.DivToSdt;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.convert.out.HTMLSettings;
import org.docx4j.convert.out.html.SdtTagHandler;
import org.docx4j.convert.out.html.SdtWriter;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.SdtPr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.SAXException;

/**
 * Round-trip XHTML to docx and back to XHTML.
 */
public class XhtmlToDocxAndBack {
	
	private static Logger log = LoggerFactory.getLogger(XhtmlToDocxAndBack.class);		
	

    public static void main(String[] args) throws Exception {
        

    	String xhtml= 
    			"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\r\n" + 
    			"<root>" + 
    			"<head>" + 
    			"    <style>\r\n" + 
    			"        body {\r\n" + 
    			"            background: #f1f1f1;\r\n" + 
    			"            width: 100%;\r\n" + 
    			"            height: 100%;\r\n" + 
    			"            margin: 0;\r\n" + 
    			"            padding: 0;\r\n" + 
    			"        }\r\n" + 
    			"\r\n" + 
    			"        .invoice {\r\n" + 
    			"            font-family: inherit;\r\n" + 
    			"            font-weight: 100;\r\n" + 
    			"            width: 95%;\r\n" + 
    			"            max-width: 1000px;\r\n" + 
    			"            margin: 2% auto;\r\n" + 
    			"            box-sizing: border-box;\r\n" + 
    			"            padding: 70px;\r\n" + 
    			"            border-radius: 5px;\r\n" + 
    			"            background: #fff;\r\n" + 
    			"            min-height: auto;\r\n" + 
    			"            font-size: 13;\r\n" + 
    			"        }\r\n" + 
    			"\r\n" + 
    			"        .header {\r\n" + 
    			"            display: flex;\r\n" + 
    			"            width: 100%;\r\n" + 
    			"\r\n" + 
    			"            align-items: center;\r\n" + 
    			"        }\r\n" + 
    			"\r\n" + 
    			"        .header--invoice {\r\n" + 
    			"            order: 2;\r\n" + 
    			"            text-align: right;\r\n" + 
    			"            width: 40%;\r\n" + 
    			"            margin: 0;\r\n" + 
    			"            padding: 0;\r\n" + 
    			"        }\r\n" + 
    			"\r\n" + 
    			"        .invoice--date,\r\n" + 
    			"        .invoice--number {\r\n" + 
    			"            font-size: 13px;\r\n" + 
    			"            color: #494949;\r\n" + 
    			"        }\r\n" + 
    			"\r\n" + 
    			"        .invoice--recipient {\r\n" + 
    			"            margin-top: 25px;\r\n" + 
    			"            margin-bottom: 4px;\r\n" + 
    			"        }\r\n" + 
    			"\r\n" + 
    			"        .header--logo {\r\n" + 
    			"            order: 1;\r\n" + 
    			"            font-size: 30px;\r\n" + 
    			"            width: 60%;\r\n" + 
    			"            font-weight: 900;\r\n" + 
    			"        }\r\n" + 
    			"\r\n" + 
    			"        .logo--address {\r\n" + 
    			"            font-size: 13px;\r\n" + 
    			"            padding: 4px;\r\n" + 
    			"\r\n" + 
    			"            width: 50%;\r\n" + 
    			"        }\r\n" + 
    			"\r\n" + 
    			"        .logo--address1 {\r\n" + 
    			"            font-size: 13px;\r\n" + 
    			"            font-family: lato;\r\n" + 
    			"            text-transform: capitalize;\r\n" + 
    			"        }\r\n" + 
    			"\r\n" + 
    			"        .description {\r\n" + 
    			"            margin: auto;\r\n" + 
    			"            text-align: justify;\r\n" + 
    			"        }\r\n" + 
    			"\r\n" + 
    			"        .sessiontitle {\r\n" + 
    			"            text-align: center;\r\n" + 
    			"            font-family: initial;\r\n" + 
    			"            padding: 1px;\r\n" + 
    			"            width: 100%;\r\n" + 
    			"            margin-top: 10px;\r\n" + 
    			"            text-transform: capitalize;\r\n" + 
    			"            font-size: 13px;\r\n" + 
    			"        }\r\n" + 
    			"\r\n" + 
    			"        .spansubtitle {\r\n" + 
    			"            font-size: 13px;\r\n" + 
    			"            font-weight: 700;\r\n" + 
    			"        }\r\n" + 
    			"\r\n" + 
    			"        .bottomcls {\r\n" + 
    			"            position: relative;\r\n" + 
    			"            top: -18px;\r\n" + 
    			"            font-size: 11px;\r\n" + 
    			"            font-family: serif;\r\n" + 
    			"            color: #795548;\r\n" + 
    			"        }\r\n" + 
    			"    </style>\r\n" + 
    			"</head>\r\n" + 
    			"<article class=\"invoice\" style=\"font-family: Times New Roman, Times, sans-serif;\">\r\n" + 
    			"    <p contenteditable=\"true\" style=\"text-align: justify;\"><b> Date:30-12-2022 </b></p>\r\n" + 
    			"\r\n" + 
    			"    <p contenteditable=\"true\" style=\"text-align: justify;\">\r\n" + 
    			"        To, <br />\r\n" + 
    			"        <b> M/S. BAJAJ HOUSING FINANCE LTD.</b><br />\r\n" + 
    			"        4th Floor, Bajaj Finserv Corporate Office,<br />\r\n" + 
    			"        Off Pune-Ahmednagar Road, Viman Nagar, Pune-411 014.\r\n" + 
    			"    </p>\r\n" + 
    			"\r\n" + 
    			"\r\n" + 
    			"    <p contenteditable=\"true\">\r\n" + 
    			"       <b> Subject:</b> Issue of NOC-cum-Release letter of <b> Flat NO. A801 </b> in <b>'Willow'</b> of Building known as <b>'Folium by Sumadhura Phase 1' </b> situated at Sy No:47/1,\r\n" + 
    			"        47/2A, 47/2B, 47/3, 48/3 &amp; 48/4 Whitefield Village, K.R.Puram Hobli, Bangalore East Taluk, Bangalore-560 066.\r\n" + 
    			"    </p>\r\n" + 
    			"    <p contenteditable=\"true\" style=\"text-align: justify;\">\r\n" + 
    			"        <b> Ref. No.: 1. Bajaj Housing Finance Sanction Letter dated 22-Sept-2022 </b>\r\n" + 
    			"    </p>\r\n" + 
    			"    <p contenteditable=\"true\" style=\"text-align: justify;\">\r\n" + 
    			"        Respected Sir / Madam,\r\n" + 
    			"    </p>\r\n" + 
    			"    <p contenteditable=\"true\" style=\"text-align: justify;\">\r\n" + 
    			"        &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; We have sold the following Flat and you are requested to give the <u>NOC-cum-Release letter</u> for the said unit at the earliest.\r\n" + 
    			"    </p>\r\n" + 
    			"    <p contenteditable=\"true\" style=\"text-align: justify;\">\r\n" + 
    			"        &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; The Purchaser/s has taken loan from HDFC Bank and their disbursement is pending. Hence you are requested to issue the <u>NOC-cum-release letter</u> at the earliest\r\n" + 
    			"    </p>\r\n" + 
    			"\r\n" + 
    			"    <table style=\"width: 100%; border: 1px solid black; border-collapse: collapse; font-size: 13px; margin-top: 20px;\">\r\n" + 
    			"         <tr style=\"border: 1px solid black;\">\r\n" + 
    			"          <th colspan=\"2\" style=\"border: 1px solid black; text-align: left; padding: 8px;background-color:#87CEEB;text-align: center;\"> Details of Purchaser</th>\r\n" + 
    			"            \r\n" + 
    			"        </tr>\r\n" + 
    			"        \r\n" + 
    			"        <tr style=\"border: 1px solid black;\">\r\n" + 
    			"            <td style=\"border: 1px solid black; text-align: left; padding: 8px;\">\r\n" + 
    			"                Purchaser's full Name\r\n" + 
    			"            </td>\r\n" + 
    			"            <td style=\"border: 1px solid black; text-align: left; padding: 8px;\">\r\n" + 
    			"               <b>  Mr Diwas Kumar  and &nbsp;Mrs. Aparajita Shankar \r\n" + 
    			" </b>\r\n" + 
    			"            </td>\r\n" + 
    			"        </tr>\r\n" + 
    			"\r\n" + 
    			"        <tr style=\"border: 1px solid black;\">\r\n" + 
    			"            <td style=\"border: 1px solid black; text-align: left; padding: 8px;\">\r\n" + 
    			"                Purchase's Mobile No.\r\n" + 
    			"            </td>\r\n" + 
    			"            <td style=\"border: 1px solid black; text-align: left; padding: 8px;\">\r\n" + 
    			"                8801117909\r\n" + 
    			"            </td>\r\n" + 
    			"        </tr>\r\n" + 
    			"        <tr style=\"border: 1px solid black;\">\r\n" + 
    			"            <td style=\"border: 1px solid black; text-align: left; padding: 8px;\">\r\n" + 
    			"                Flat no\r\n" + 
    			"            </td>\r\n" + 
    			"            <td style=\"border: 1px solid black; text-align: left; padding: 8px;\">\r\n" + 
    			"                <b> A801 </b>\r\n" + 
    			"            </td>\r\n" + 
    			"        </tr>\r\n" + 
    			"        <tr style=\"border: 1px solid black;\">\r\n" + 
    			"            <td style=\"border: 1px solid black; text-align: left; padding: 8px;\">\r\n" + 
    			"                Wing\r\n" + 
    			"            </td>\r\n" + 
    			"            <td style=\"border: 1px solid black; text-align: left; padding: 8px;\">\r\n" + 
    			"                Willow\r\n" + 
    			"            </td>\r\n" + 
    			"        </tr>\r\n" + 
    			"        <tr style=\"border: 1px solid black;\">\r\n" + 
    			"            <td style=\"border: 1px solid black; text-align: left; padding: 8px;\">\r\n" + 
    			"                Floor\r\n" + 
    			"            </td>\r\n" + 
    			"            <td style=\"border: 1px solid black; text-align: left; padding: 8px;\">\r\n" + 
    			"                <b> Floor 8 </b>\r\n" + 
    			"            </td>\r\n" + 
    			"        </tr>\r\n" + 
    			"        <tr style=\"border: 1px solid black;\">\r\n" + 
    			"            <td style=\"border: 1px solid black; text-align: left; padding: 8px;\">\r\n" + 
    			"                Area (in Sqft)\r\n" + 
    			"            </td>\r\n" + 
    			"            <td style=\"border: 1px solid black; text-align: left; padding: 8px;\">\r\n" + 
    			"                1563\r\n" + 
    			"            </td>\r\n" + 
    			"        </tr>\r\n" + 
    			"        <tr style=\"border: 1px solid black;\">\r\n" + 
    			"            <td style=\"border: 1px solid black; text-align: left; padding: 8px; width: 50%;\">\r\n" + 
    			"                Project Name\r\n" + 
    			"            </td>\r\n" + 
    			"            <td style=\"border: 1px solid black; text-align: left; padding: 8px; width: 50%;\">\r\n" + 
    			"                Folium by Sumadhura Phase 1\r\n" + 
    			"            </td>\r\n" + 
    			"        </tr>\r\n" + 
    			"\r\n" + 
    			"        <tr style=\"border: 1px solid black;\">\r\n" + 
    			"            <td style=\"border: 1px solid black; text-align: left; padding: 8px;\">\r\n" + 
    			"                Project Location/ Address\r\n" + 
    			"            </td>\r\n" + 
    			"            <td style=\"border: 1px solid black; text-align: left; padding: 8px;\">\r\n" + 
    			"                Whitefield Village, K.R.Puram Hobli, Bangalore\r\n" + 
    			"            </td>\r\n" + 
    			"        </tr>\r\n" + 
    			"\r\n" + 
    			"        <tr style=\"border: 1px solid black;\">\r\n" + 
    			"            <td style=\"border: 1px solid black; text-align: left; padding: 8px;\">\r\n" + 
    			"                Consideration/ Agreement value (in Rs.)\r\n" + 
    			"            </td>\r\n" + 
    			"            <td style=\"border: 1px solid black; text-align: left; padding: 8px;\">\r\n" + 
    			"                1,28,00,000.00/-\r\n" + 
    			"            </td>\r\n" + 
    			"        </tr>\r\n" + 
    			"\r\n" + 
    			"        <tr style=\"border: 1px solid black;\">\r\n" + 
    			"            <td style=\"border: 1px solid black; text-align: left; padding: 8px;\">\r\n" + 
    			"                Loan Amount (in Rs.)\r\n" + 
    			"            </td>\r\n" + 
    			"            <td style=\"border: 1px solid black; text-align: left; padding: 8px;\"></td>\r\n" + 
    			"        </tr>\r\n" + 
    			"        <tr style=\"border: 1px solid black;\">\r\n" + 
    			"            <td style=\"border: 1px solid black; text-align: left; padding: 8px;\">\r\n" + 
    			"                <b> Lender?s Name </b>\r\n" + 
    			"            </td>\r\n" + 
    			"            <td style=\"border: 1px solid black; text-align: left; padding: 8px;\">\r\n" + 
    			"                <b>HDFC Bank</b>\r\n" + 
    			"            </td>\r\n" + 
    			"        </tr>\r\n" + 
    			"        <tr style=\"border: 1px solid black;\">\r\n" + 
    			"            <td style=\"border: 1px solid black; text-align: left; padding: 8px;\">\r\n" + 
    			"                Own contribution given to us (in Rs.)\r\n" + 
    			"            </td>\r\n" + 
    			"            <td style=\"border: 1px solid black; text-align: left; padding: 8px;\">\r\n" + 
    			"                1,28,10,000.00/-\r\n" + 
    			"            </td>\r\n" + 
    			"        </tr>\r\n" + 
    			"        <tr style=\"border: 1px solid black;\">\r\n" + 
    			"            <td style=\"border: 1px solid black; text-align: left; padding: 8px;\">\r\n" + 
    			"               Booking date\r\n" + 
    			"            </td>\r\n" + 
    			"            <td style=\"border: 1px solid black; text-align: left; padding: 8px;\">\r\n" + 
    			"                15/07/2022\r\n" + 
    			"            </td>\r\n" + 
    			"        </tr>\r\n" + 
    			"        <tr style=\"border: 1px solid black;\">\r\n" + 
    			"            <td style=\"border: 1px solid black; text-align: left; padding: 8px;\">\r\n" + 
    			"                Agreement for sale date\r\n" + 
    			"            </td>\r\n" + 
    			"            <td style=\"border: 1px solid black; text-align: left; padding: 8px;\">\r\n" + 
    			"             24/12/2022\r\n" + 
    			"            </td>\r\n" + 
    			"        </tr>\r\n" + 
    			"    </table>\r\n" + 
    			"    <br />\r\n" + 
    			"    <p contenteditable=\"true\" style=\"margin-top: 12px;\">Kindly do the needful at the earliest.</p>\r\n" + 
    			"\r\n" + 
    			"    <p contenteditable=\"true\" style=\"text-align: justify;\">\r\n" + 
    			"        Thanking you in anticipation.<br />\r\n" + 
    			"        Yours faithfully,\r\n" + 
    			"    </p>\r\n" + 
    			"    <p contenteditable=\"true\" style=\"text-align: justify;\"><b> FOR SUMADHURA INFRACON PVT LTD</b></p>\r\n" + 
    			"</article>\r\n" + 
    			"</root>";
    	// To docx, with content controls
		WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
		
        XHTMLImporterImpl XHTMLImporter = new XHTMLImporterImpl(wordMLPackage);
        //XHTMLImporter.setDivHandler(new DivToSdt());
		
		wordMLPackage.getMainDocumentPart().getContent().addAll( 
				XHTMLImporter.convert( xhtml, null) );

		System.out.println(XmlUtils.marshaltoString(wordMLPackage
				.getMainDocumentPart().getJaxbElement(), true, true));

		wordMLPackage.save(new java.io.File("E:\\\\CUSTOMERAPP_CUG\\\\images\\\\sumadhura_projects_images\\\\documents\\\\Preview\\\\124\\\\894\\\\BankerNOCConvXhtmLFile.docx"));

		// Back to XHTML

		/*HTMLSettings htmlSettings = Docx4J.createHTMLSettings();
		htmlSettings.setWmlPackage(wordMLPackage);

	
		// output to an OutputStream.
		OutputStream os = new ByteArrayOutputStream();

		// If you want XHTML output
		Docx4jProperties.setProperty("docx4j.Convert.Out.HTML.OutputMethodXML",
				true);
		Docx4J.toHTML(htmlSettings, os, Docx4J.FLAG_EXPORT_PREFER_XSL);

		System.out.println(((ByteArrayOutputStream) os).toString());	*/
	
  }
    
    
	
}