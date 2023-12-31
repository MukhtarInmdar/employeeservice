package com.sumadhura.employeeservice.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;

import javax.xml.bind.JAXBException;

import org.docx4j.Docx4J;
import org.docx4j.Docx4jProperties;
import org.docx4j.XmlUtils;
import org.docx4j.convert.in.xhtml.DivToSdt;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.convert.out.HTMLSettings;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.AltChunkType;
import org.docx4j.openpackaging.parts.WordprocessingML.NumberingDefinitionsPart;

/**
 * This sample converts XHTML to docx content.
 * 
 * Beware that a file created with a Microsoft text editor
 * will start with a byte order mark (BOM):
 * 
 *    http://msdn.microsoft.com/en-us/library/windows/desktop/dd374101(v=vs.85).aspx
 * 
 * and if this is converted to a String, it can result in 
 * "Content not allowed in prolog" error.
 * 
 * So it is preferable to use one of the XHTMLImporter.convert
 * signatures which doesn't use a String (eg File or InputStream).
 * 
 * Here a string may be used for convenience where the XHTML is escaped 
 * (as required for OpenDoPE input), so it can be unescaped first.
 *
 * For best results, be sure to include src/main/resources on your classpath.
 *  
 */
public class ConvertInXHTMLFile {
	
//private static Logger log = LoggerFactory.getLogger(XhtmlToDocxAndBack.class);		
	

    public static void main(String[] args) throws Exception {

    	String xhtml= 
    			"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\r\n" + 
    			"<html>" + 
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
    			"</html>";
               /* "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
                + "<html>\r\n" + 
                "<head>\r\n" + 
                "      <meta charset=\"utf-8\" />\r\n" + 
                "      <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>\r\n" + 
                "      <title>Agreement Draft</title>\r\n" + 
                "      <style>\r\n" + 
                "         th, td {\r\n" + 
                "  		    font-size: 12px;\r\n" + 
                "         	padding: 5px;\r\n" + 
                "         	border: 2px solid #666;\r\n" + 
                "         	\r\n" + 
                "         }\r\n" + 
                "         #tablePhotoId, th, td {\r\n" + 
                "  			border: 1px solid white;\r\n" + 
                "  			border-collapse: collapse;\r\n" + 
                "		}\r\n" + 
                "		\r\n" + 
                "		.sessiontitle {\r\n" + 
                "	      text-align: center;\r\n" + 
                "	      font-family: initial;\r\n" + 
                "	      text-decoration:underline;\r\n" + 
                "	      padding: 1px;\r\n" + 
                "	      width: 100%;\r\n" + 
                "	      margin-top: 35px;\r\n" + 
                "	      text-transform: capitalize;\r\n" + 
                "      }\r\n" + 
                "		/*li:before {\r\n" + 
                "		    display: inline-block;\r\n" + 
                "		    width: 2em;\r\n" + 
                "		    text-align: right;\r\n" + 
                "		    margin-right: 1em;\r\n" + 
                "		    counter-increment: section;\r\n" + 
                "		    content: counter(section) \"1. \";\r\n" + 
                "		}\r\n" + 
                "		\r\n" + 
                "		ol.lower_roman {list-style-type: lower-roman;}\r\n" + 
                "		\r\n" + 
                "		li.special:before {\r\n" + 
                "		    counter-increment: none;\r\n" + 
                "		    content: counter(\"\") \"a. \";\r\n" + 
                "		}\r\n" + 
                "		.moveRightSideEighteenthpx{\r\n" + 
                "			  margin-left: 30px;\r\n" + 
                "			  font-weight: 100;\r\n" + 
                "		}\r\n" + 
                "		\r\n" + 
                "		.moveRightSideThirtyFivepx{\r\n" + 
                "			  margin-left: 40px;\r\n" + 
                "			  font-weight: 100;\r\n" + 
                "		}\r\n" + 
                "		\r\n" + 
                "		.moveRightSideFourtypx{\r\n" + 
                "			margin-left:45px;\r\n" + 
                "			font-weight: 100;\r\n" + 
                "		}\r\n" + 
                "		\r\n" + 
                "		.startingDiv {\r\n" + 
                "		   font-weight:normal;\r\n" + 
                "		   padding-top:12px;\r\n" + 
                "		}\r\n" + 
                "		\r\n" + 
                "		.startingDivEightPixelPadd {\r\n" + 
                "		   font-weight:normal;\r\n" + 
                "		   padding-top:8px;\r\n" + 
                "		}\r\n" + 
                "		\r\n" + 
                "		.paragraphTwoPixelPadd {\r\n" + 
                "		   padding-top:-4px;\r\n" + 
                "		}\r\n" + 
                "		\r\n" + 
                "		div {\r\n" + 
                "  text-align: justify;\r\n" + 
                "  text-justify: inter-word;\r\n" + 
                "}\r\n" + 
                "		\r\n" + 
                "      </style>\r\n" + 
                "   </head>\r\n" + 
                "<body>\r\n" + 
                "\r\n" + 
                "\r\n" + 
                " \r\n" + 
                " \r\n" + 
                "  \r\n" + 
                "  \r\n" + 
                "    \r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 400; text-align: center;\"><strong><u>AGREEMENT TO SELL</u></strong></p>\r\n" + 
                "<p style=\"font-weight: 80;\">&nbsp;</p>\r\n" + 
                "<p style=\"font-weight: 80;\">This Agreement for Sale (\"Agreement\") is executed on this Seventh day of December,,</p>\r\n" + 
                "<p>By and Between</p>\r\n" + 
                "<p class=\"moveRightSideEighteenthpx\"><strong>1. Smt.MUNIYAMMA,</strong>aged about 77 years,\r\n" + 
                "<br/>Wife of Late.Sri.<strong>&nbsp;</strong>M.Nanjappa,</p>\r\n" + 
                "<p class=\"moveRightSideEighteenthpx\"><strong>2. Sri.N.PRAKASH,</strong>aged about 45 years,<br/>\r\n" + 
                " Son of Late.Sri.M.Nanjappa and Smt.Muniyamma,\r\n" + 
                "</p>\r\n" + 
                "<p class=\"moveRightSideEighteenthpx\"><strong>2a. Smt.PANKAJA,&nbsp;</strong>aged about 37 years,<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Wife of&nbsp;Sri.N.Prakash,</p>\r\n" + 
                "<p class=\"moveRightSideEighteenthpx\"><strong>2b. Mr. CHANDAN,&nbsp;</strong>aged about 21 years,<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Son of&nbsp;Sri.<strong>&nbsp;</strong>N.Prakash,</p>\r\n" + 
                "<p class=\"moveRightSideEighteenthpx\"><strong>2c Kumari. VANDANA,&nbsp;</strong>aged about 17 years,<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Daughter of&nbsp;Sri.<strong>&nbsp;</strong>N.Prakash,</p>\r\n" + 
                "<p class=\"moveRightSideEighteenthpx\"><strong>2d Master. SHREYAS,&nbsp;</strong>aged about 11 years,<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Son of&nbsp;Sri.<strong>&nbsp;</strong>N.Prakash,</p>\r\n" + 
                "\r\n" + 
                "\r\n" + 
                "<p class=\"moveRightSideEighteenthpx\">Party at Sl.Nos.2(c &amp; d) are being minors represented by their Natural Guardian and Father&nbsp;<strong>Sri.N.PRAKASH</strong>&nbsp;Party at Sl.No.2.</p>\r\n" + 
                "\r\n" + 
                "<p class=\"moveRightSideEighteenthpx\"><strong>Sri.N.RAVI,&nbsp;</strong>aged about 42 years,<br/>\r\n" + 
                "	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Son of Late.Sri.M.Nanjappa and Smt.Muniyamma,</p>\r\n" + 
                "\r\n" + 
                "<p class=\"moveRightSideEighteenthpx\"><strong>3a. Smt.</strong>&nbsp;<strong>LAKSHMAMMA,&nbsp;</strong>aged about 35 years,<br/>\r\n" + 
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Wife of&nbsp;Sri.N.Ravi,</p>\r\n" + 
                "\r\n" + 
                "<p class=\"moveRightSideEighteenthpx\"><strong>3b. Master.VIVEK,&nbsp;</strong>aged about 17 years,<br/>\r\n" + 
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Son of&nbsp;Sri.N.Ravi,</p>\r\n" + 
                "\r\n" + 
                "<p class=\"moveRightSideEighteenthpx\"><strong>3c. Master.SANDEEP,&nbsp;</strong>aged about 16 years,<br/>\r\n" + 
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Son of&nbsp;Sri.N.Ravi,</p>\r\n" + 
                "\r\n" + 
                "<p class=\"moveRightSideEighteenthpx\">Party at Sl.Nos.3(b &amp; c) are being minors represented by their Natural Guardian and Father&nbsp;<strong>Sri.N.Ravi&nbsp;</strong>Party at Sl.No.3.</p>\r\n" + 
                "\r\n" + 
                "\r\n" + 
                "<p class=\"moveRightSideEighteenthpx\"><strong>4. Smt. SAROJAMMA,&nbsp;</strong>aged about 47 years,<br/>\r\n" + 
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Daughter of Late.Sri.M.Nanjappa and Smt.Muniyamma,</p>\r\n" + 
                "\r\n" + 
                "<p class=\"moveRightSideEighteenthpx\"><strong>5. Smt. VIJAYAMMA,&nbsp;</strong>aged about 45 years,<br/>\r\n" + 
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Daughter of Late.Sri.M.Nanjappa and Smt.Muniyamma,,</p>\r\n" + 
                "\r\n" + 
                "<p class=\"moveRightSideEighteenthpx\">Parties at Sl.No.1 to 5 are Residing at Thindlu Village,<br/>\r\n" + 
                "Vidyaranyapura&nbsp;&nbsp;Post, Bangalore &ndash; 560 097.</p>\r\n" + 
                "\r\n" + 
                "\r\n" + 
                "<p class=\"moveRightSideEighteenthpx\"><strong>6. Sri.RAJAGOPAL.P.K,</strong>aged about 48 years,<br/>\r\n" + 
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Son of Late.Sri.P.N.Krishnappa,</p>\r\n" + 
                "\r\n" + 
                "<p class=\"moveRightSideEighteenthpx\">Residing at:Parvathamma Building, Thindlu Village,<br/> \r\n" + 
                "Vidyaranyapura&nbsp;&nbsp;Post, Bangalore &ndash; 560 097.</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;All are represented by their GPA Holder,</p>\r\n" + 
                "<p class=\"moveRightSideFourtypx\"><strong>M/s. SUMADHURA INFRACON PRIVATE LIMITED.,</strong><br/>\r\n" + 
                "A company incorporated under the provisions of Indian Companies Act 1956, having its registered office at: Sy.No.108/2, Millenia Building, 1<sup>st</sup>&nbsp;Main MSR Layout, Munnekollala Village, Marathahalli, Outer Ring Road, Bangalore &ndash; 560 037.<br/>\r\n" + 
                "Represented by its Authorised signatory,<br/> \r\n" + 
                "<strong>Mr. K. RAHUL&nbsp;</strong>Aged<strong>&nbsp;</strong>about 35 years,<br/>\r\n" + 
                " S/o. late Sri. K. Bhaskar</p>\r\n" + 
                "\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>1. Mr. Murali Nelanti,</strong>S/o.Mr.Prabhakar Nelanti,&nbsp;(Aadhar No.287527438225) aged about 0 years, (PAN No. AEVPN3213P) ,</p>\r\n" + 
                " \r\n" + 
                "<p style=\"font-weight: 80;\">\r\n" + 
                "<strong>\r\n" + 
                "	      	Residing at :  \r\n" + 
                "     </strong>\r\n" + 
                "\r\n" + 
                "&nbsp; 5-31, Ghattuppala, Chandur Mandal, Ghattuppala, Nalgonda, Telangana, 508253.</p>\r\n" + 
                "\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">Hereinafter collectively referred to as the&nbsp;<strong>OWNERS/VENDORS</strong>&nbsp;by their Power of Attorney&nbsp;(which expression wherever it so requires shall mean and include their&nbsp;successors,&nbsp;legal heirs, legal representatives, administrators and executors)</p>\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>AND</strong></p>\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>M/s. SUMADHURA INFRACON PRIVATE LIMITED.,&nbsp;</strong><strong>(CINNo.U45200KA2012PTC062071),</strong>&nbsp;A company incorporated under the provisions of Indian Companies Act 1956, having its registered office at:&nbsp;&nbsp;Sy.No.108/2, Millenia Building, 1<sup>st</sup>&nbsp;Main MSR Layout, Munnekollala Village, Marathahalli, Outer Ring Road, Bangalore &ndash; 560 037&nbsp;&nbsp;<strong>(PAN:AAQCS9641A),</strong>&nbsp;Represented by its Authorised Signatory&nbsp;<strong>Mr.K.RAHUL, S/o. Late.Sri.K.Bhaskar&nbsp;</strong><strong>(Aadhar&nbsp;&nbsp;No.9383 6839 5558).</strong></p>\r\n" + 
                "<p style=\"font-weight: 80;\">(Hereinafter referred to as the \"PROMOTER\" (which expression shall unless repugnant to the context or meaning thereof be deemed to mean and include its successor-in-interest, and permitted assigns).</p>\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>AND</strong></p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">hereinafter called the \" Allottee/s\" (which expression shall wherever&nbsp;&nbsp;the&nbsp;&nbsp;context so requires or admits, mean and include all his/her/their legal heirs, representatives, executors administrators and permitted assigns )<br/>\r\n" + 
                "The Promoter and Allottee/s shall hereinafter collectively be referred to as the \"Parties\" and individually as a \"Party\".</p>\r\n" + 
                "<p style=\"font-weight: 80;\"><strong><u>DEFINITIONS:</u></strong></p>\r\n" + 
                "<p style=\"font-weight: 80;\">For the purpose of this Agreement for Sale, unless the context otherwise requires, -</p>\r\n" + 
                "\r\n" + 
                "  <div class=\"startingDiv\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span style=\"font-weight:bold;\">(a)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;font-weight-bold;\"> <span>\r\n" + 
                "    	\"Act\" means the Real Estate (Regulation and Development) Act, 2016 (16 of 2016);\r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "\r\n" + 
                "  <div class=\"startingDiv\" style=\"width:100%;float:left\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span style=\"font-weight:bold;\">(b)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left\"> <span>\r\n" + 
                "    	\"appropriate Government\" means the State Government of Karnataka;\r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDiv\" style=\"width:100%;float:left\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span style=\"font-weight:bold;\">(c)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left\"> <span>\r\n" + 
                "    	 \"Rules\" means the Karnataka Real Estate (Regulation and Development) Rules, 2017\r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "\r\n" + 
                "  <div class=\"startingDiv\" style=\"width:100%;float:left\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span style=\"font-weight:bold;\">(d)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left\"> <span>\r\n" + 
                "	\"Regulations\" means the Regulations made under the Real Estate (Regulation and Development) Act, 2016;\r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "\r\n" + 
                "  <div class=\"startingDiv\" style=\"width:100%;float:left\">\r\n" + 
                "  	    <div style=\"width:4%;float:left;\">  <span style=\"font-weight:bold;\">(e)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left\"> <span>\r\n" + 
                "	\"section\" means a section of the Act.\r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "\r\n" + 
                "  <div class=\"startingDiv\" style=\"width:100%;float:left\">\r\n" + 
                "  	    <div style=\"width:4%;float:left;\">  <span style=\"font-weight:bold;\">(f)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left\"> <span  style=\"font-weight:bold;\">\r\n" + 
                "	Interpretation:\r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">Unless the context otherwise requires in this Agreement,</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\" class=\"paragraphTwoPixelPadd\">i. In this Agreement, any reference to any statute or statutory provision shall include all the current statues state or central, their amendment, modification, re-enactment or consolidation:</p>\r\n" + 
                "<p style=\"font-weight: 80;\" class=\"paragraphTwoPixelPadd\">ii. Any reference to the singular shall include the plural and vice-versa;</p>\r\n" + 
                "<p style=\"font-weight: 80;\" class=\"paragraphTwoPixelPadd\">iii. Any references to the masculine, the feminine and the neutral gender shall also include the other;</p>\r\n" + 
                "<p style=\"font-weight: 80;\" class=\"paragraphTwoPixelPadd\">iv. The recital, annexures and schedules annexed herein forms part of this Agreement and shall have the same force and effect as if expressly set out in the body of this Agreement, and any reference to this Agreement shall include any recitals, annexures and schedules to it.</p>\r\n" + 
                "<p style=\"font-weight: 80;\" class=\"paragraphTwoPixelPadd\">v. Harmonious Interpretation and Ambiguities within the Agreement:</p>\r\n" + 
                "<p style=\"font-weight: 80;\" class=\"paragraphTwoPixelPadd\">vi. Incaseof ambiguities or discrepancies within the Agreement, the following shall apply:</p>\r\n" + 
                "\r\n" + 
                "  <div style=\"width:100%;float:left\">\r\n" + 
                "  	<div style=\"width:5%;float:left;\">  <span style=\"margin-left:10px;\"> &nbsp;</span>  </div>\r\n" + 
                "    <div style=\"width:4%;float:left;\">  <span>a.</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left\"> <span>\r\n" + 
                "			Between two Articles of this Agreement, the provisions of the specific Article relevant to the issue under consideration shall prevail over general provisions in the other Articles.\r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left\">\r\n" + 
                "  	<div style=\"width:5%;float:left;\">  <span style=\"margin-left:10px;\"> &nbsp;</span>  </div>\r\n" + 
                "    <div style=\"width:4%;float:left;\">  <span >b.</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left\"> <span>\r\n" + 
                "			Between the provisions of this Agreement and the Appendices, the Agreement shall prevail, save and except as expressly provided in the Agreement or the Appendices.\r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left\">\r\n" + 
                "  	<div style=\"width:5%;float:left;\">  <span style=\"margin-left:10px;\"> &nbsp;</span>  </div>\r\n" + 
                "    <div style=\"width:4%;float:left;\">  <span >c.</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left\"> <span>\r\n" + 
                "			Between any value written in numerical or percentage and in words, the words shall prevail.\r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;padding-top:5px;\">vii. reference to this Agreement or any other deed, agreement or other instrument or document shall be construed as a reference to this Agreement or such deed, other agreement, or other instrument or document as the same may from time to time be amended, varied supplemented or novated;</p>\r\n" + 
                "<p style=\"font-weight: 80;\" class=\"paragraphTwoPixelPadd\">viii. Each of the representations and warranties provided in this Agreement are independent of other unless the contrary is expressly stated,</p>\r\n" + 
                "<p style=\"font-weight: 80;\" class=\"paragraphTwoPixelPadd\">ix. No Section in this Agreement limits the extent or application of another Section;</p>\r\n" + 
                "<p style=\"font-weight: 80;\" class=\"paragraphTwoPixelPadd\">x. Headings to clauses, parts and paragraphs of this Agreement, Annexures and Schedules are for convenience only and do not affect the interpretation of this Agreement;</p>\r\n" + 
                "<p style=\"font-weight: 80;\" class=\"paragraphTwoPixelPadd\">xi. the words \"include\", \"including\" and \"in particular\" shall be construed as being by way of illustration or emphasis only and shall not be construed as, nor shall they take effect as, limiting the generality of any preceding words;</p>\r\n" + 
                "<p style=\"font-weight: 80;\" class=\"paragraphTwoPixelPadd\">xii. <strong>\"</strong>Carpet area\" means the net useable area floor area of an apartment, excluding the area covered by the external wall, areas under service shafts, exclusive balcony or verandah area and exclusive open terrace area, but includes the area covered by the internal partition walls of the apartment</p>\r\n" + 
                "<p style=\"font-weight: 80;\"><strong><u>WHEREAS:</u></strong></p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>A.</strong>&nbsp;Whereas one late M.Nanjappa, S/o. Late. Motappa&nbsp;&nbsp;was the absolute and lawful owner of all that piece and parcel of residentially converted Land bearing Sy.No. 3/10, measuring about&nbsp;<strong>28 Guntas</strong>, situated at Thindlu Village, Yelahanka Hobli, Bangalore North Taluk<strong>.&nbsp;</strong>The&nbsp;<strong>&nbsp;</strong>originally the Sy No. 3/10 measuring 30 Guntas, had acquired by&nbsp;&nbsp;&nbsp;&nbsp;Sri. Govindappa S/o Late. Motappa&nbsp;&nbsp;under registered Sale Deed dated 13/05/1974, registered vide Document No.790/1974-75, Volum No.2913, 189 to 191, Book I, registered before the office of the Sub-Registrar, Peenya Bangalore, later the said M.Govindappa and M.Nanjappa both are son of late Motappa have entered oral partition for their entire joint family property, the said land measuring 28 Guntas in Sy.No.3/10, along with other property allotted to the share of the M Nanjappa,&nbsp;accordingly M.Nanjappa S/o. late Motappa's name mutated into the revenue records vide MR No.10/1987-88. After the demise of the said M Nanjappa, S/o. Late. Motappa&nbsp;&nbsp;his legal heirs&nbsp;&nbsp;(\"Owner at Sl.No.1 to 5\")&nbsp;&nbsp;became the absolute owners of the above said property.</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>A1.</strong>&nbsp;Whereas Sri.N.PRAKASH, S/o. late M Nanjappa (\"Owner at Sl.No.2\") is the absolute and lawful owner of all that piece and parcel of residentially converted Land bearing&nbsp;<strong>Sy.No.3/9,&nbsp;</strong>measuring about&nbsp;<strong>36&nbsp;</strong>Guntas, situated at Thindlu Village, Yelahanka Hobli, Bangalore North Taluk. The<strong>&nbsp;</strong>Owner at Sl.No.2 had acquired the said property under the two different sale deeds viz.,&nbsp;&nbsp;</p>\r\n" + 
                "<ol>\r\n" + 
                "<li style=\"font-weight: 80;\">Sale deed dated: 07/10/1999, registered number Allotted on 16/10/2004, registered as document No.14353/2004-05, stored in CD No.YAND87, Book I in the office of the Sub-Registrar Yelahanka, Bangalore.</li>\r\n" + 
                "<li style=\"font-weight: 80;\">Sale deed dated 07/10/1999, registered number Allotted on 15/09/2003, registered as document No.8228/2003-04, stored in CD No.YNK54, Book I in the office of the Sub-Registrar Yelahanka, Bangalore.</li>\r\n" + 
                "</ol>\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>A2. WHEREAS,</strong>&nbsp;all the aforesaid Properties i.e, Sy No.3/9 and Sy.No.3/10,&nbsp;&nbsp;are situated adjacent to each other and totally measures 1 Acre 24 Guntas, { converted vide Conversion Order of the Deputy Commissioner Bangalore, dated 05/11/2015, bearing No.ALN.(NY)SR:236/2014-15},&nbsp;<strong>&nbsp;</strong>and Presently the said property bears a common BBMP Khata No:&nbsp;1582/Sy.No.3/9,3/10, situated at<strong>&nbsp;</strong>Thindlu Village, Yelahanka Hobli, Bangalore North Taluk,, presently within the jurisdiction of Bruhath Bangalore Mahanagara Palike, which property is more fully described in&nbsp;<strong>Item No. I</strong>&nbsp;of the Schedule hereunder and hereinafter referred to as the&nbsp;<strong>\"SCHEDULE F PROPERTY\".</strong></p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>A3.</strong>&nbsp;Whereas&nbsp;one late M Nanjappa S/o. Late. Motappa&nbsp;&nbsp;was the absolute and lawful owner of all that piece and parcel of residentially converted Land bearing Sy.No. 3/7, measuring about&nbsp;<strong>2 Acres out of 2 Acres 29 Guntas,&nbsp;</strong>situated at Thindlu Village, Yelahanka Hobli, Bangalore North Taluk, he had acquired the same under registered Sale Deed dated 06/01/1959, registered vide Document No.6727/1958-59, Volume No.1755, Pages&nbsp;&nbsp;200 to 2003, Book I, registered before the office of the Sub-Registrar, Peenya Bangalore,&nbsp;After the demise of the said M Nanjappa S/o. Late. Motappa&nbsp;&nbsp;his legal heirs&nbsp;&nbsp;(\"Owner at Sl.No.1 to 5\")&nbsp;&nbsp;became the absolute owner of the above said property.</p>\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>A4.</strong>&nbsp;Whereas&nbsp;one late M Nanjappa S/o. Late. Motappa&nbsp;&nbsp;was&nbsp;&nbsp;also the absolute and lawful owner of all that piece and parcel of residentially converted Land bearing Sy.No. 3/8C, (Old Sy No.3/8)&nbsp;&nbsp;measuring about&nbsp;<strong>8 Guntas,&nbsp;</strong>situated at Thindlu Village, Yelahanka Hobli, Bangalore North Taluk, he had acquired the same under registered Sale Deed dated 31/05/1971, registered vide Document No.799/1971-72, Volume No.2781, Pages 58 to 59, Book I, registered before the office of the Sub-Registrar, Peenya Bangalore,&nbsp;After the demise of the said M Nanjappa S/o. Late. Motappa&nbsp;&nbsp;his legal heirs&nbsp;&nbsp;(\"Owner at Sl.No.1 to 5\")&nbsp;&nbsp;became the absolute owner of the above said property.</p>\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>A5.</strong>&nbsp;Whereas&nbsp;Sri.N.Prakash&nbsp;S/o. late M Nanjappa (\"Owner at Sl.No.2\") is the absolute and lawful owner of all that piece and parcel of residentially converted Land bearing&nbsp;<strong>Sy.No.3/8B,&nbsp;</strong>( Old Sy No. 3/8)<strong>&nbsp;</strong>measuring about&nbsp;<strong>10&nbsp;</strong>Guntas, situated at Thindlu Village, Yelahanka Hobli, Bangalore North Taluk. Originally the Sy No. 3/8B old Sy No. 3/8, measuring 10 Guntas, had acquired by Smt. Channamma, first wife of Late. M Nanjappa,&nbsp;&nbsp;under registered Sale Deed dated 06/09/1967, registered vide Document No.1678/1967-68, Volum No.2631, pages 227 to 229, Book I, registered before the office of the Sub-Registrar, Peenya Bangalore, later the said Smt. Channamma and others have entered oral partition for their entire joint family property, the said land measuring 10 Guntas in Sy.No.3/8B old Sy No.3/8, allotted to the share of the&nbsp;Sri.N.Prakash,&nbsp;&nbsp;&nbsp;accordingly Sri.N.Prakash S/o. late M Nanjappa's name mutated into the revenue records vide MR No. 24/2001-02. Thus the&nbsp;(\"Owner at Sl.No.2)&nbsp;&nbsp;&nbsp;became the absolute owner of the above said property.</p>\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>A6.WHEREAS&nbsp;</strong>Sri.P K Rajagopal&nbsp;S/o. Sri. P N Krishnappa (\"Owner at Sl.No.6\") is the absolute and lawful owner of all that piece and parcel of residentially converted Land bearing&nbsp;<strong>Sy.No.3/8A,&nbsp;</strong>( Old Sy No. 3/8)<strong>&nbsp;</strong>measuring about&nbsp;<strong>11&nbsp;</strong>Guntas, situated at Thindlu Village, Yelahanka Hobli, Bangalore North Taluk. he had acquired the same under registered Sale Deed dated 06/01/2005, registered vide Document No.20114/2004-05, stored in CD No.YAND103, Book I, registered before the office of the Sub-Registrar, Yelahanka Bangalore.</p>\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>A7. WHEREAS,</strong>&nbsp;All the aforesaid Properties i.e, Sy No.3/7, Sy No.3/8A, Sy No.3/8B and Sy.No.3/8C, are situated adjacent to each other and totally measures&nbsp;<strong>2 Acre 29 Guntas</strong>, { converted vide Conversion Order of the Deputy Commissioner Bangalore, dated 05/11/2015,bearing No.ALN.(NY)SR:236/2014-15},and Presently the said property bears a common BBMP Khata No:&nbsp;1583/Sy.No.3/7,3/8A,3/8B ,3/8C,situated at<strong>&nbsp;</strong>Thindlu Village,Yelahanka Hobli, Bangalore North Taluk, presently within the jurisdiction of Bruhath Bangalore Mahanagara Palike, which property is more fully described in the&nbsp;<strong>Item No. II</strong>&nbsp;of the Schedule hereunder and hereinafter referred to as the&nbsp;<strong>\"SCHEDULE F PROPERTY\".</strong></p>\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>Item No. I and Item No. II of the Schedule property collectively referred to as \"Schedule F\" Property.&nbsp;</strong></p>\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>A8. WHEREAS&nbsp;</strong>the Owners at Sl.No.1 to 6&nbsp;have executed Joint Development Agreement&nbsp;dated 27/11/2014, registered as Document No.3970/2014-15, stored in CD No. HBBD163, Book-I, registered before the Office of the Sub-Registrar, Gandhinagar(Hebbal), Bangalore&nbsp;and Rectification Deed for Joint Development Agreement dated 13/09/2019,&nbsp;registered as Document No.2850/2019-20, stored in CD No.KCHD427, Book-I, registered before the Office of the Sub-Registrar, Gandhinagar(Kacharakanahalli), Bangalore, with respect of&nbsp;&nbsp;converted land bearing&nbsp;&nbsp;Sy.No.3/10 Measuring 28 Guntas, land bearing Sy.No.3/9, measuring about&nbsp;&nbsp;38 Guntas, land bearing Sy.No. 3/7, measuring about 2 Acres out of 2 Acres 29 Guntas, land bearing Sy.No. 3/8C, (Old Sy No.3/8)&nbsp;&nbsp;measuring about 8 Guntas,<strong>&nbsp;</strong>land bearing&nbsp;Sy.No.3/8B,<strong>&nbsp;</strong>( Old Sy No. 3/8)<strong>&nbsp;</strong>measuring about 10<strong>&nbsp;</strong>Guntas, land bearing&nbsp;Sy.No.3/8A, ( Old Sy No. 3/8)<strong>&nbsp;</strong>measuring about 11<strong>&nbsp;</strong>Guntas, all the&nbsp;&nbsp;land totally Measuring 4 Acre 13 Guntas of Thindlu village in favour of PROMOTER herein,&nbsp;in furtherance to the above said registered Joint Development Agreement&nbsp;&nbsp;and the Rectification Deed the Owners at Sl.No.1 &amp; 6&nbsp;have also executed General Power of Attorney dated 27/11/2014, registered as Document No.305/2014-15, stored in CD No.HBBD163, Book-IV, registered before the Office of the Sub-Registrar, Gandhinagar (Hebbal), Bangalore and&nbsp;Rectification Deed for&nbsp;General Power of Attorney&nbsp;dated 13/09/2019,&nbsp;registered as Document No.115/2019-20, stored in CD No.KCHD427, Book-IV, registered before the Office of the Sub-Registrar, Gandhinagar(Kacharakanahalli), Bangalore, thereby authorizing the Promoter to deal with the Promoter undivided share of land and built up area and empowering the Promoter to sell&nbsp;&nbsp;the Promoter's share.</p>\r\n" + 
                "<p style=\"font-weight: 80;\">All the Properties belonging to the Owners&nbsp;at Sl No.<strong>1&nbsp;</strong>to&nbsp;<strong>6</strong>&nbsp;herein situated adjacent to each other and Promoter having developing the same jointly as one project.</p>\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>B. WHEREAS</strong> The Item No. I of the Said Land is earmarked for the purpose of building a Multistoried Residential Apartment project, comprising One<strong> Wing, known as Wing 'A'&nbsp;</strong>consists of<strong>&nbsp;</strong>two basements for parking and Ground plus Fourteen Upper Floors for residential apartments<strong>&nbsp;</strong>and hereinafter referred&nbsp;&nbsp;as&nbsp;<strong>\"</strong><strong>SUMADHURA SUSHANTHAM PHASE I\"</strong>&nbsp;and Item No. II of the Said Land is earmarked for the purpose of building a Multistoried Residential Apartment project, comprising&nbsp;<strong>Three</strong>&nbsp;<strong>Wings, known as Wing B, C and D,&nbsp;&nbsp;</strong>consists of<strong>&nbsp;</strong>two basements for parking and Ground plus Fourteen Upper Floors for residential apartments and Club house,hereinafter referred as&nbsp;<strong>\"</strong><strong>SUMADHURA SUSHANTHAM PHASE II\".</strong>The Phase-I and Phase-II are collectively will be referred as&nbsp;<strong>\" Project\"</strong>.</p>\r\n" + 
                "<p style=\"font-weight: 80;\">C. The Promoter is fully competent to enter into this Agreement and all the legal formalities with respect to the right, title and interest of the Promoter regarding the Said Land on which Project is to be constructed have been completed.</p>\r\n" + 
                "<p style=\"font-weight: 80;\">D. The BBMP has granted the commencement certificate to develop the Phase I of the Project vide approval dated 01/06/2020 bearing registration No.BBMP/Addl.Dir/JD NORTH/LP/0069/2018-19;</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>E</strong>. The BBMP has granted the approval to develop the Phase-I of the&nbsp;Project vide&nbsp;No:&nbsp;BBMP/ ADDL.DIR / JDNORTH /0069/2018-19,&nbsp;dated 14/11/2019,&nbsp;consisting of One (1)&nbsp;<strong>Wing known as Wing A, i.e., Sumadhura Sushantham Phase I,&nbsp;&nbsp;</strong>consists of<strong>&nbsp;</strong>two basements for parking, and Ground plus Fourteen Upper Floors for residential apartment, the above said&nbsp;<strong>Wing A&nbsp;</strong>consists of&nbsp;<strong>140&nbsp;</strong>residential apartment units. Copy of the said Sanctioned Plan has been made available to the Allottee/s and the same has been also displayed at the Schedule Property by the Promoter.</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>E1</strong>. The BBMP has granted the approval to develop the Project vide&nbsp;No:&nbsp;BBMP/ ADDL.DIR / JDNORTH /0068/2018-19,&nbsp;dated&nbsp;&shy;&shy;&shy;&shy;&shy;18/01/2020,&nbsp;consisting of Three (03)&nbsp;<strong>Wings known as B, C &amp; D</strong>,&nbsp;<strong>i.e., Sumadhura Sushantham Phase II,&nbsp;&nbsp;</strong>&nbsp;consists of<strong>&nbsp;</strong>two basements for parking, and Ground plus Fourteen Upper Floors for residential apartments, and all above said&nbsp;<strong>B, C &amp; D&nbsp;</strong>Wings consists of&nbsp;<strong>266&nbsp;</strong>residential apartment units. Copy of the said Sanctioned Plan has been made available to the Allottee/s and the same has been also displayed at the Schedule Property by the Promoter. The Promoter has also obtained all other Approvals/Nocs for development of the Schedule Property at it's cost.</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>F</strong>. The Promoter has registered the Project under the provisions of the Act with the Karnataka Real Estate Regulatory Authority at Bengaluru details as below;</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">(i) Sumadhura Sushantham Phase- I, Registration No:&nbsp;PRM/KA/RERA/1251/309/ PR /200103/ 003118 dated&nbsp;03/01/2020.<br/>\r\n" + 
                "And,<br/>\r\n" + 
                "(ii) Sumadhura Sushantham Phase II - Registration No:&nbsp;PRM/KA/RERA/1251/309/PR /200205/003253 dated&nbsp;05/02/2020.</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>G</strong>. The Allottee/s had/have applied for an apartment in the Project vide application No.<strong> -</strong>, dated&nbsp;<strong>28-09-2020&nbsp;</strong>\r\n" + 
                "and has been allotted Apartment&nbsp;<strong>No A803, Type  Bedroom</strong>,\r\n" + 
                "on&nbsp;<strong> Floor 8 </strong>&nbsp;in&nbsp;<strong>Wing A</strong>,\r\n" + 
                " \r\n" + 
                "having carpet area of&nbsp;<strong> 115.01</strong><strong>Sq.Mtrs or&nbsp;</strong><strong> 1238.0</strong><strong>Sq.ft,</strong>,\r\n" + 
                "and super built up area of&nbsp;<strong> 161.18 Sq.Mtrs or 1735 Sq.ft,</strong>\r\n" + 
                " \r\n" + 
                "\r\n" + 
                "\r\n" + 
                " together with&nbsp;<strong> 57.41Sq.Mtrs or 618.0 Sq.ft</strong>&nbsp;\r\n" + 
                " of undivided share in the land comprised in schedule F property with one covered&nbsp;&nbsp;parking space admeasuring&nbsp;<strong>148</strong>&nbsp;square feet, which Apartment is more fully described in schedule 'A' hereunder in&nbsp;<strong>\"</strong><strong>SUMADHURA SUSHANTHAM&nbsp;\"</strong>&nbsp;as permissible under the applicable law and of proportionate share in the common areas (\" Common Areas\") as defined under clause (n) of Section 2 of the Act (hereinafter referred to as the \" Apartment\" more particularly described in Schedule A and the floor plan of the apartment is annexed hereto as Schedule B);&nbsp;</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">H. The Parties have gone through all the terms and conditions set out in this Agreement and understood the mutual rights and obligations detailed herein;</p>\r\n" + 
                "<p style=\"font-weight: 80;\">I.&nbsp;&nbsp;DISCLOSURES:</p>\r\n" + 
                "The Allottee/s acknowledges and confirms that the Promoter have made following disclosures to the Allottee/s and the Allottee/s has reviewed all of them and after having understood the implication thereof has entered into this Agreement and the Allottee/s has agreed to all of the Disclosures and the Allottee/s, expressly grants its consent and no objection to the Promoter to undertake every action as per Disclosures.\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">i) that, the undivided share of land extent of Schedule 'A' Apartmentto be conveyed to the Allottee/s on execution of the Sale Deed is based on the overall super built up area of the Project and this is arrived after considering the deduction of the area relinquished towards road widening, park and open spaces and may change if any&nbsp;&nbsp;further relinquishment required by BDA/BBMP or any concern government authorities as per zoning regulations and building bye laws. Any such reduction will not alter the Sale Consideration for the Schedule 'A' Apartment.</p>\r\n" + 
                "<p style=\"font-weight: 80;\">ii) that, the Common Amenities and Facilities and the Common Areas in the Project will be have to be maintained by the Association of Allottees of the Project in common irrespective of the location of such Common Area and the Common Amenities and Facilities in the Project. The Clubhouse and Common Amenities are built part in Phase-I and part in Phase-II, but the Allottee/s of both the Phases will have rights on all such Clubhouse facilities and Common Amenities. The Phase-I and Phase-II have separate road access but the Allottee/s of both the Phases of the project will have the right to use both the road access. The clubhouse and all the common amenities are the part of the common area of all the Apartment units.</p>\r\n" + 
                "<p style=\"font-weight: 80;\">iii) that, the Schedule 'A' Apartment can be used in terms of the rules and regulations formulated by the Promoter and/or the Association of Apartment Owners as well as the terms set out in the Bye Laws of the Association. The Apartment Owners Association will be common for all the residents of Sumadhura Sushantham Phase I and II, which are to be constructed on composite Schedule 'F' Property.</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">iv) that, the Promoter will execute one Deed of Declaration by which an Association of Apartment Owners for the management and maintenance of Common Area and the Common Amenities and Facilities will be formed, under the provisions of the Karnataka Apartment Ownership Act, 1972 pertaining to the Project.</p>\r\n" + 
                "<p style=\"font-weight: 80;\">v) that the Schedule 'A' Apartment can be used only for residential purposes and cannot used for any other purposes.</p>\r\n" + 
                "<p style=\"font-weight: 80;\">vi)that the Promoter will be granting exclusive rights to use and right to deal with Limited Common Area to other buyers of the apartments in the Project. The Allottee/s confirms that before execution of this Agreement, the Allottee/s has been provided with details of the Limited Common Area of the Project and is fully aware of the exclusive right of user of such Limited Common Areas in the said Project. The Allottee/s has also been informed that the Deed of Declaration shall also provide the details of such Limited Common Area and its use and exclusivity.</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">vii)&nbsp;The total cost of the Apartment below mentioned is exclusive of Stamp Duty, registration charges and BWSSB deposits. The GBWASP charges paid by the Developer to BWSSB during plan approval/Noc has to be refunded by the Purchaser/s to the Developer.</p>\r\n" + 
                "<p style=\"font-weight: 80;\">viii)&nbsp;The Sumadhura Sushantham Phase-I and Phase-II of the Project have different timelines for the completion and handing over the possession. As the Clubhouse and Common Amenities are built in Phase-II, same will be available to the Phase-I Allottee/s as per the Phase II completion timelines i.e, 31/03/2023.</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">ix) The Promoter has no control on the developments that may exist or take place in future whatsoever, in adjoining and surrounding properties of the project.</p>\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>J.</strong> The Parties hereby confirm that they are signing this Agreement with full knowledge of all the laws, rules, regulations, notifications, etc., applicable to the Project;</p>\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>K.</strong>The Parties, relying on the confirmations, representations and assurances of each other to faithfully abide by all the terms, conditions and stipulations contained in this Agreement and all applicable laws, are now willing to enter into this Agreement on the terms and conditions appearing hereinafter;</p>\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>L.</strong>In accordance with the terms and conditions set out in this Agreement and as mutually agreed upon by and between the Parties, the Promoter hereby agrees to sell and the Allottee hereby agrees to purchase the Apartment and the covered parking as specified in para G;</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>NOW THEREFORE,</strong>in consideration of the mutual representations, covenants, assurances, promises and agreements contained herein and other good and valuable consideration, the parties agree as follows:</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>1. TERMS:</strong></p>\r\n" + 
                "\r\n" + 
                "<p class=\"moveRightSideEighteenthpx\">1.1 Subject to the terms and conditions as detailed in this Agreement, the Promoter agrees to sell to the Allottee and the Allottee&nbsp;&nbsp;hereby agrees to purchase, the Apartment as specified in para G.</p>\r\n" + 
                "<p class=\"moveRightSideEighteenthpx\">1.2 The Total Price for the Apartment based on the carpet area is :<strong> Rs. 1,05,85,986.60/-( Rupees And  Paisa Only)</strong></p>\r\n" + 
                " \r\n" + 
                "<table  style=\"width:100%; border: 1px solid black;border-collapse:collapse;\">\r\n" + 
                "   <tbody>\r\n" + 
                "      <tr style=\"border: 1px solid black;border-collapse:collapse;\">\r\n" + 
                "         <td style=\"width:49%;border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "            <p><strong>Block / Building / Tower/Wing No.</strong> <strong>Wing A</strong></p>\r\n" + 
                "            <p>&nbsp;<strong>Apartment No. A803</strong></p>\r\n" + 
                "            <p><strong>Type:  Bedroom </strong></p>\r\n" + 
                "            <p><strong>Floor:</strong> <strong>Floor 8</strong></p>\r\n" + 
                "         </td>\r\n" + 
                "         <td style=\"width:49%;border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "            <p>Rate of Apartment per square feet per carpet/SBUA</p>\r\n" + 
                "            <p>1238.0/1735</p>\r\n" + 
                "         </td>\r\n" + 
                "      </tr>\r\n" + 
                "      <tr style=\"border: 1px solid black;border-collapse:collapse;\">\r\n" + 
                "         <td style=\"width:49%;border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "            <p>Cost of Apartment (in rupees)</p>\r\n" + 
                "         </td>\r\n" + 
                "         <td style=\"width:49%;border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "            <p><strong>Rs. 1,05,85,986.60/-( Rupees And  Paisa Only)</strong></p>\r\n" + 
                "         </td>\r\n" + 
                "      </tr>\r\n" + 
                "   </tbody>\r\n" + 
                "</table>\r\n" + 
                "\r\n" + 
                "<table style=\"width:100%; border: 1px solid black;border-collapse:collapse;\">\r\n" + 
                "	<tbody>\r\n" + 
                "		<tr style=\"border: 1px solid black;border-collapse:collapse;\">\r\n" + 
                "			<td style=\"width:49%;border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "				<p><strong>Particulars</strong></p>\r\n" + 
                "			</td>\r\n" + 
                "			<td style=\"width:49%;border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "				<p><strong>Basic Cost</strong></p>\r\n" + 
                "			</td>\r\n" + 
                "			<td style=\"width:49%;border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "				<p><strong>GST%</strong></p>\r\n" + 
                "			</td>\r\n" + 
                "			<td style=\"width:49%;border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "				<p><strong>GST AMOUNT</strong></p>\r\n" + 
                "			</td>\r\n" + 
                "			<td style=\"width:49%;border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "				<p><strong>TOTAL AMOUNT</strong></p>\r\n" + 
                "			</td>\r\n" + 
                "		</tr>\r\n" + 
                "		\r\n" + 
                "					<tr style=\"border: 1px solid black;border-collapse:collapse;\">\r\n" + 
                "				<td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "					<p>Cost of the Apartment :</p>\r\n" + 
                "				</td>\r\n" + 
                "				<td  style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "					<p> 91,53,000.00</p>\r\n" + 
                "				</td>\r\n" + 
                "				<td  style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "					<p>5.0%</p>\r\n" + 
                "				</td>\r\n" + 
                "				<td  style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "					<p> 4,57,650.00</p>\r\n" + 
                "				</td>\r\n" + 
                "				<td  style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "					<p> 96,10,650.00</p>\r\n" + 
                "				</td>\r\n" + 
                "			</tr>\r\n" + 
                "					<tr style=\"border: 1px solid black;border-collapse:collapse;\">\r\n" + 
                "				<td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "					<p>COST OF COMMON AREAS :</p>\r\n" + 
                "				</td>\r\n" + 
                "				<td  style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "					<p> 5,47,000.00</p>\r\n" + 
                "				</td>\r\n" + 
                "				<td  style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "					<p>5.0%</p>\r\n" + 
                "				</td>\r\n" + 
                "				<td  style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "					<p> 27,350.00</p>\r\n" + 
                "				</td>\r\n" + 
                "				<td  style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "					<p> 5,74,350.00</p>\r\n" + 
                "				</td>\r\n" + 
                "			</tr>\r\n" + 
                "					<tr style=\"border: 1px solid black;border-collapse:collapse;\">\r\n" + 
                "				<td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "					<p>CAR PARKING :</p>\r\n" + 
                "				</td>\r\n" + 
                "				<td  style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "					<p> 3,00,000.00</p>\r\n" + 
                "				</td>\r\n" + 
                "				<td  style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "					<p>5.0%</p>\r\n" + 
                "				</td>\r\n" + 
                "				<td  style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "					<p> 15,000.00</p>\r\n" + 
                "				</td>\r\n" + 
                "				<td  style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "					<p> 3,15,000.00</p>\r\n" + 
                "				</td>\r\n" + 
                "			</tr>\r\n" + 
                "					<tr style=\"border: 1px solid black;border-collapse:collapse;\">\r\n" + 
                "				<td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "					<p>Maintenance for 12 Months :</p>\r\n" + 
                "				</td>\r\n" + 
                "				<td  style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "					<p> 72,870.00</p>\r\n" + 
                "				</td>\r\n" + 
                "				<td  style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "					<p>18.0%</p>\r\n" + 
                "				</td>\r\n" + 
                "				<td  style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "					<p> 13,116.60</p>\r\n" + 
                "				</td>\r\n" + 
                "				<td  style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "					<p> 85,986.60</p>\r\n" + 
                "				</td>\r\n" + 
                "			</tr>\r\n" + 
                "					<tr style=\"border: 1px solid black;border-collapse:collapse;\">\r\n" + 
                "				<td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "					<p>Total :</p>\r\n" + 
                "				</td>\r\n" + 
                "				<td  style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "					<p> 1,00,72,870.00</p>\r\n" + 
                "				</td>\r\n" + 
                "				<td  style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "					<p>%</p>\r\n" + 
                "				</td>\r\n" + 
                "				<td  style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "					<p> 5,13,116.60</p>\r\n" + 
                "				</td>\r\n" + 
                "				<td  style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "					<p> 1,05,85,986.60</p>\r\n" + 
                "				</td>\r\n" + 
                "			</tr>\r\n" + 
                "				\r\n" + 
                "	</tbody>\r\n" + 
                "</table>\r\n" + 
                "\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">Total Price of the Apartment is :<strong>&nbsp;&nbsp;Rs. 1,05,85,986.60/-( Rupees And  Paisa Only)</strong></p>\r\n" + 
                "<p style=\"font-weight: 80;\">Explanation:</p>\r\n" + 
                "<p style=\"font-weight: 80;\">i) The Total Price above includes the booking amount paid by the allottee to the Promoter towards the Apartment. The total price is payable by the Allottee/s as per the Payment Plan mentioned in Schedule\"C\".</p>\r\n" + 
                "<p style=\"font-weight: 80;\">ii)The Total Price above includes Goods and Service Tax (GST), or any other similar taxes which may be levied, in connection with the construction of the Project payable by the Promoter), Provided that in case there is any change / modification in the taxes, the subsequent amount payable by the allottee to the promoter shall be increased/reduced based on such change /modification:&nbsp;</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">iii) The Promoter shall periodically intimate in writing to the Allottee, the amount payable as stated in (i)&nbsp;above&nbsp;and the Allottee shall make payment demanded by the Promoter within the time and in the manners specified therein.&nbsp;The Promoter already provided to the Allottee the details of the taxes payable as per the&nbsp;Central Tax&nbsp;&nbsp;Rate Notification No.11/2017 Dated :28/06/2017, and Amended Notification No.3/2019&nbsp;&nbsp;dated 29/03/2019,&nbsp;&nbsp;as per which such taxes/levies etc. have been imposed or became effective;</p>\r\n" + 
                "<p style=\"font-weight: 80;\">iv) The Total Price of Apartment includes recovery of price of land, construction of [not only the Apartment but also] the Common Areas, internal development charges, external development charges, taxes, cost of providing electric wiring, electrical connectivity to the apartment, lift, internal water lines and plumbing, finishing with paint, tiles, doors, windows, fire detection and firefighting equipment in the common areas as per applicability,maintenance charges as per para 11,and includes cost for providing all other facilities, amenities and specifications to be provided within the Apartment and the Project.</p>\r\n" + 
                "<p style=\"font-weight: 80;\">1.3 The Total Price is escalation-free,&nbsp;save and except increases which the Allottee hereby agrees to pay, due to increase on account of development charges payable to the competent authority and/or any other increase in charges which may be levied or imposed by the competent authority from time to time. The Promoter undertakes and agrees that while raising&nbsp;&nbsp;a demand on the Allottee for increase in development charges, cost/charges imposed by the competent authorities, the Promoter shall enclose the said notification/ order/rule/regulation to that effect along with the demand letter being issued to the Allottee, which shall only be applicable on subsequent payments. Provided that if there is any new imposition or increase of any development charges after the expiry of the scheduled date of completion of the project as per registration with the Authority, which shall include the&nbsp;&nbsp;extension of registration, if any, granted to the said project by the Authority, as per the Act, the same shall not be charged from the allottee.</p>\r\n" + 
                "<p style=\"font-weight: 80;\">1.4 The Allottee(s) shall make the payment as per the payment plan set out in&nbsp;Schedule C&nbsp;(\"Payment Plan\").</p>\r\n" + 
                "<p style=\"font-weight: 80;\">1.5 The Promoter may allow, in its sole discretion, a rebate for early payments of installments payable by the Allottee by&nbsp;&nbsp;discounting such early payments @ 9% per annum for the&nbsp;&nbsp;period by which the respective installment has been preponed. The provision for allowing rebate and such rate of rebate shall not be subject to any revision/withdrawal, once granted to an Allottee by the Promoter.</p>\r\n" + 
                "<p style=\"font-weight: 80;\">1.6 It is agreed that the Promoter shall not make any additions and alterations in the sanctioned plans, layout plans and specifications and the nature of fixtures, fittings and amenities described herein at&nbsp;Schedule 'D' and Schedule 'E'&nbsp;(which shall be in conformity with the advertisement, prospectus etc., on the basis of which sale is effected) in respect of the apartment, without the previous written consent of the Allottee as per the provisions of the Act. Provided that the Promoter may make such minor additions or alterations as may be required&nbsp;by the Allottee, or such minor changes or alterations as per the provisions of the Act.</p>\r\n" + 
                "<p style=\"font-weight: 80;\">1.7 The Promoter shall confirm to the final carpet area that has been allotted to the Allottee after the construction of the Building is complete and the occupancy certificate is granted by the competent authority, by furnishing details of the changes, if any, in the carpet area. The total price payable for the carpet area shall be recalculated upon confirmation by the Promoter. If there is any reduction in the carpet area then the Promoter shall refund the excess money paid by Allottee within sixty days with annual interest at the rate prescribed in the Rules, from the date when such an excess amount was paid by the Allottee.&nbsp;&nbsp;If there is any increase in the carpet area, which is not more than three percent of the carpet area of the apartment, allotted to Allottee, the Promoter may demand that from the Allottee as per the next milestone of the Payment Plan as provided in&nbsp;<strong>Schedule-C</strong>. All these monetary adjustments shall be made at the same rate per square feet as agreed in para 1.2 of this Agreement.</p>\r\n" + 
                "<p style=\"font-weight: 80;\">1.8 Subject to para 9.3 the Promoter agrees and acknowledges, the Allottee shall have the right to the Apartment as mentioned below:</p>\r\n" + 
                "\r\n" + 
                "<p class=\"moveRightSideEighteenthpx\" style=\"font-weight: 80;\">(i)&nbsp;The Allottee shall have exclusive ownership of the Apartment;</p>\r\n" + 
                "<p class=\"moveRightSideEighteenthpx\" style=\"font-weight: 80;\">(ii)&nbsp;The Allottee shall also have undivided proportionate share in the Common Areas. Since the share / interest of Allottee in the Common Areas is undivided and cannot be divided or separated, the Allottee shall use the Common Areas along with other occupants, maintenance staff etc., without causing any inconvenience or hindrance to them. It is clarified that the promoter shall hand over the common areas to the association of Allot&nbsp;&nbsp;&nbsp;&nbsp;tees after duly obtaining the completion certificate from the competent authority as provided in the Act;</p>\r\n" + 
                "<p class=\"moveRightSideEighteenthpx\" style=\"font-weight: 80;\">(iii)&nbsp;That the computation of the price of the Apartment includes recovery of price of land, construction of [not only the Apartment but also] the Common Areas, internal development charges, external development charges, taxes, cost of providing electric wiring, electrical connectivity to the apartment, lift,&nbsp;internal&nbsp;water line and plumbing, finishing with paint, tiles, doors, windows, fire detection and firefighting equipment in the common areas,&nbsp;maintenance charges as per para 11&nbsp;and includes cost for providing all other facilities, amenities and specifications to be provided within the Apartment and the Project;</p>\r\n" + 
                "<p class=\"moveRightSideEighteenthpx\" style=\"font-weight: 80;\">(iv)&nbsp;The Allottee has the right to visit the project site to assess the extent of development of the project and his apartment with prior appointment from the representative of the Promoter.</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">1.9 It is made clear by the Promoter and the Allottee/s agrees that the Apartment along with _covered parking shall be treated as a single indivisible unit for all purposes. It is agreed that the Project is an independent, self-contained Project covering the said Land and is not a part of any other project or zone and shall not form a part of and/or/ linked/combined with any other project in its vicinity or otherwise except for the purpose of integration of infrastructure for the benefit of the Allottee. It is clarified that Project's facilities and amenities shall be available only for use and enjoyment of the Allottees of the Project.</p>\r\n" + 
                "<p style=\"font-weight: 80;\">1.10 The Promoter agrees to pay all outgoings before transferring the physical possession of the apartment to the Allottees, which it has collected from the Allottees, for the payment of outgoings (including land cost, ground rent, municipal or other local taxes,&nbsp;charges for electricity, maintenance charges,&nbsp;including mortgage loan and interest on mortgages or other encumbrances and such other liabilities payable to competent authorities, banks and financial institutions, which are related to the project). If the Promoter fails to pay all or any of the outgoings collected by it from the Allottees or any liability, mortgage loan and interest thereon before transferring the apartment to the Allottees, the Promoter agrees to be liable, even after the transfer of the property, to pay such outgoings and penal charges, if any, to the authority or person to whom they are payable and be liable for the cost of any legal proceedings which may be taken therefor by such authority or person.</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">1.11 The Allottee has paid a sum of&nbsp;<strong>Rs. 10,50,000.00/-( Rupees Only)&nbsp;&nbsp;&nbsp;</strong>as booking&nbsp;amount&nbsp;being part payment towards the Total Price of the Apartment&nbsp;at the time of application&nbsp;to the PROMOTER in the following manner:</p>\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>&nbsp;</strong></p>\r\n" + 
                "\r\n" + 
                "<ol>\r\n" + 
                "   	<li>A sum of <strong> 10,00,000.00/-( Rupees Only)</strong> towards Booking amount Vide <strong>Cheque</strong> Bearing No.<strong>25420456</strong>\r\n" + 
                "	dated <strong>09-02-2022 drawn on Indian Bank </strong>to the promoter</li>\r\n" + 
                "       	<li>A sum of <strong>? 50,000.00/-( Rupees Only)</strong> towards Booking amount Vide <strong>Cheque</strong> Bearing No.<strong>24555550</strong>\r\n" + 
                "	dated <strong>30-11-2022 drawn on Allahabad Bank </strong>to the promoter</li>\r\n" + 
                "    </ol>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">the receipt of which the Promoter hereby acknowledges and the Allottee/s hereby agrees to pay the remaining price of the Apartment as prescribed in the Payment Plan [Schedule C] and other amounts mentioned in Payment Plan [Schedule C] in terms stated therin,<strong>&nbsp;</strong>as may be demanded by the Promoter within the time and in the manner specified therein:</p>\r\n" + 
                "<p style=\"font-weight: 80;\">Provided that if the allottee/s delays in payment towards any amount for which is payable; he shall be liable to pay interest at the rate specified in the Rules</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>2. MODE OF PAYMENT:</strong></p>\r\n" + 
                "\r\n" + 
                "Subject to the terms of the Agreement and the Promoter abiding by the construction milestones, the Allottee shall make all payments, on written demand by the Promoter, within the&nbsp;stipulated time as mentioned in the Payment Plan [Schedule C] through A/c Payee cheque/demand draft/bankers cheque or online payment (as applicable) in favour of&nbsp;M/s. SUMADHURA INFRACON PRIVATE LIMITED'&nbsp;payable at Bengaluru.&nbsp;\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>3 </strong><strong>COMPLIANCE OF LAWS RELATING TO REMITTANCES:</strong></p>\r\n" + 
                "\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span>3.1</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "    	The Allottee, if resident outside India, shall be solely responsible for complying with the necessary formalities as laid down in Foreign Exchange Management Act, 1999, Reserve Bank of India Act, 1934 and the Rules and Regulations made thereunder or any statutory amendment(s) modification(s) made thereof and all other applicable laws including that of remittance of payment acquisition/sale/transfer of immovable properties in India etc. and provide the Promoter with such permission, approvals which would enable the Promoter to fulfill its obligations under this Agreement. Any refund, transfer of security, if provided in terms of the Agreement shall be made in accordance with the provisions of Foreign Exchange Management Act, 1999 or the statutory enactments or amendments thereof and the Rules and Regulations of the Reserve Bank of India or any other applicable law. The Allottee understands and agrees that in the event of any failure on his/her part to comply with the applicable guidelines issued by the Reserve Bank of India, he/she may be liable for any action under the Foreign Exchange Management Act, 1999 or other laws as applicable, as amended from time to time.\r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span>3.3</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "    	The Promoter accepts no responsibility in regard to matters specified in para 3.1 above. The Allottee shall keep the Promoter fully indemnified and harmless in this regard. Whenever there is any change in the residential status of the Allottee subsequent to the signing of this Agreement, it shall be the sole responsibility of the Allottee to intimate the same in writing to the Promoter immediately and comply with necessary formalities if any under the applicable laws. The Promoter shall not be responsible towards any third party making payment/remittances on behalf of any Allottee and such third party shall not have any right in the application/allotment of the said apartment applied for herein in any way and the Promoter shall be issuing the payment receipts in favour of the Allottee only.\r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span>3.3</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "    	The Allottee/s shall be solely responsible to deduct taxes at source at such rate, presently one percent on the Total sale consideration, as required under section 194IA of the income tax Act,1961 for each of the payments made towards Total Sale Consideration and comply with provisions of the IT Act. The Allottee/s also undertake/s to issue a certificate of deduction of tax in Form 16B to the Promoter before 10th day of the subsequent month of deduction.\r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "\r\n" + 
                "\r\n" + 
                "\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">4 &nbsp;<strong>ADJUSTMENT/APPROPRIATION OF PAYMENTS:</strong></p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">&nbsp;The Allottee authorizes the Promoter to adjust/appropriate all payments made by&nbsp;&nbsp;&nbsp;him/her under any head(s) of dues against lawful outstanding of the allottee against the Apartment, if any, in his/her name and the Allottee undertakes not to object/demand/direct the Promoter to adjust his payments in any manner.&nbsp;&nbsp;&nbsp;&nbsp;</p>\r\n" + 
                "<p style=\"font-weight: 80;\">5 &nbsp;<strong>TIME IS ESSENCE:</strong></p>\r\n" + 
                "<p style=\"font-weight: 80;\">The Promoter shall abide by the time schedule for completing the project as disclosed at the time of registration of the project with the Authority and towards handing over the Apartment to the Allottee and the common areas to the association of the allottees or the competent authority, as the case may be.&nbsp;Similarly, the Allotee/s shall make timely payments of the installment and other dues payable by him/her and meeting the other obligations under the agreement subject to the simultaneous completion of construction by the Promoter as provided in Payment Plan (SCHEDULE C).</p>\r\n" + 
                "<p style=\"font-weight: 80;\">&nbsp;&nbsp;&nbsp;&nbsp;The Allottee/s has been made aware and the Allottee/s is fully aware that there are other Allottee/s who would be joining the Scheme and would rely upon the assurance given by the Allottee/s herein for the payment of the instalments set out in the Payment Plan and the Statutory Payments within time and without any delay or default.</p>\r\n" + 
                "<p>The Promoter has further informed the Allottee/s and the Allottee/s is fully aware that the default in payments of the instalments set out in the Payment Plan would affect the entire Project development and there would be sufferance to the Promoter.</p>\r\n" + 
                "<p style=\"font-weight: 80;\">Provided that if the Allottee/s delays in payment towards any amount which is payable, he shall be liable to pay interest at the rate prescribed in the Rules.</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">6&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>CONSTRUCTION OF THE PROJECT/APARTMENT:</strong></p>\r\n" + 
                "<p style=\"font-weight: 80;\">The Allottee has seen the proposed layout plan, specifications, amenities and facilities of Apartment and accepted the floor plan, payment plan and the specifications, amenities and facilities [annexed along with this Agreement] which has been approved by the competent authority, as represented by the Promoter. The Promoter shall develop the Project in accordance with the said layout plans, floor plans and specifications, amenities and facilities. Subject to the terms in this Agreement, the Promoter undertakes to strictly abide by such plans approved by the competent Authorities and shall also strictly abide by the bye-laws, FAR and density norms and provisions prescribed by the BDA/BBMP and shall not have an option to make any variation/alteration/modification in such plans, other than in the manner provided under the Act, and breach of this term by the Promoter shall constitute a material breach of the Agreement.</p>\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>7 &nbsp;</strong><strong>POSSESSION OF THE APARTMENT/PLOT:</strong></p>\r\n" + 
                "\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:-5px;\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span>7.1</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "    	Schedule for possession of the said Apartment- The Promoter agrees and understands that timely<br/> delivery of possession of the Apartment to the allottee and the common areas to the association of allottees or the competent authority, as the case may be, is the essence of the Agreement. The Promoter assures to hand over possession of the Apartment along with ready and complete common areas with all specifications, amenities and facilities of the project in place on or before <strong>30/09/2022 (Thirtieth, September, Two Thousand and Twenty Two)</strong>  \r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:-5px;\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span>&nbsp;</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "    	unless there is delay or failure due to war, flood, drought, fire, cyclone,  earthquake or any other calamity caused by nature affecting the regular development of the real estate project (\"Force Majeure\"). If, however, the completion of the Project is delayed due to the Force Majeure conditions then the Allottee agrees that the Promoter shall be entitled to the extension of time for delivery of possession of the Apartment, provided that such Force Majeure conditions are not of a nature which make it impossible for the contract to be implemented. The Allottee agrees and confirms that, in the event it becomes impossible for the Promoter to implement the project due to Force Majeure conditions, then this allotment shall stand terminated and the Promoter shall refund to the Allottee the entire amount received by the Promoter from the allotment within 60  days from that date. The Promoter shall intimate the allottee about such termination at least thirty days prior to such termination. After refund of the money paid by the Allottee, the Allottee agrees that he/she shall not have any rights, claims etc. against the Promoter and that the Promoter shall be released and discharged from all its obligations and liabilities under this Agreement.\r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span>7.2</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "    	Procedure for taking possession - The Promoter, upon obtaining  the occupancy certificate from the competent authority shall offer in writing the possession of the Apartment, to the Allottee in terms of this Agreement to be taken within two months from the date of issue of occupancy certificate. The Promoter agrees and undertakes to indemnify the allottee in case of failure of fulfillment of any of the provisions, formalities, documentation on part of the promoter. The Allottee, after taking the possession, agree(s) to pay the maintenance charges as determined by the Promoter/association of allottees, as the case may be after the issuance of the completion certificate for the project. The promoter shall hand over the occupancy certificate of the apartment/plot, as the case may be, to the allottee at the time of conveyance of the same time.\r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span>7.3</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "    	Failure of Allottee to take Possession of Apartment- Upon receiving a  written intimation from the Promoter as per para 7.2, the Allottee shall take possession of the Apartment from the Promoter by making all the payments due, executing necessary indemnities, undertakings and such other documentation as prescribed in this Agreement, and the Promoter shall give possession of the Apartment to the allottee and convey the title of schedule 'B' Apartment by executing the sale deed in favor of Allottee/s. In case the Allottee fails to take possession within the time provided in para 7.2, such Allottee shall continue to be liable to pay, maintenance charges as specified in para 7.2.\r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span>7.4</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "    	Possession by the Allottee - After obtaining the occupancy certificate and handing over physical possession of the Apartment to the Allottees, it shall be the responsibility of the Promoter to hand over the necessary documents and plans, including common areas, to the association of Allottees as and when the Association will ready to accept the same.\r\n" + 
                "     <br/> The Allottee/s is fully aware that the Promoter will be developing the Project and constructing/completing the Wings, Common Amenities and Facilities of the Project from time to time. The Allottee/s has assured and agreed that Allottee/s shall have no objection to the Promoter completing the other Wings even if the Allottee/s has taken possession of the Schedule \"A\" Apartment in the Wing which is completed and the Promoter has secured Occupancy Certificate for that Wing.\r\n" + 
                "    	The Allottee/s shall bear the Statutory Payments, Khata transfer fees or any other charges that are necessary for securing separate assessment for the Schedule \"A\" Apartment. It is clarified that the Allottee/s shall pay all municipal and property taxes in respect of the Schedule \"A\" Apartment from the date its assessed separately to property taxes.\r\n" + 
                "\r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span>7.5</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "	Cancellation by Allottee - The Allottee shall have the right to cancel/withdraw his allotment in the Project as provided in the Act:\r\n" + 
                "       <br/>Provided that where the allottee proposes to cancel/withdraw from the project without any fault of the promoter, the promoter herein is entitled to forfeit the booking amount paid for the allotment. The balance amount of money paid by the allottee shall be returned by the promoter to the allottee against the execution of duly stamped Deed of cancellation of Agreement of sale, within 60 days of such cancellation. \r\n" + 
                "	<br/>If the Allottee/s has availed loan from any financial institution or the bank, then in that event based on the terms of such loan, all amounts to be refunded to the Allottee/s in terms of clause 7.5 above shall be paid over to the financial institution or the bank against issuance of \"no claim certificate\" in favour of the Promoter and the Promoter shall also be entitled to receive the original of this Agreement of sale that may have been deposited by the Allottee/s with the bank or any financial institution in addition to Allottee/s executing  the duly stamped Deed of cancellation of Agreement of sale.\r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  \r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span>7.6</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "Compensation - The Promoter as may be the case shall compensate the Allottee in case of any loss caused to him due to defective title of the land, on which the project is being developed or has been developed, in the manner as provided under the Act and the claim for interest and compensation under this provision shall not be barred by limitation provided under any law for the time being in force. \r\n" + 
                "<br/>Except for occurrence of a Force Majeure event, if the promoter fails to complete or is unable to give possession of the Apartment (i) in accordance with the terms of this Agreement, duly completed by the date specified in para 7.1; or (ii) due to discontinuance of his business as a developer on account of suspension or revocation of the registration under the Act; or for any other reason; the Promoter shall be liable, on demand to the allottees, in case the Allottee wishes to withdraw from the Project, without prejudice to any other remedy available, to return the total amount received by him in respect of the Apartment, with interest at the rate prescribed in the Rules including compensation in the manner as provided under the Act within  Sixty (60) days of it becoming due. Provided that where if the Allottee does not intend to withdraw from the Project, the Promoter shall pay the Allottee interest at the rate prescribed in the Rules for every month of delay, till the handing over of the possession of the Apartment, which shall be paid by the promoter to the allottee within Sixty (60) days of it becoming due.\r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">8 &nbsp;<strong>REPRESENTATIONS AND WARRANTIES OF THE PROMOTER:</strong></p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">&nbsp;&nbsp;&nbsp;The Promoter hereby represents and warrants to the Allottee as follows:</p>\r\n" + 
                "\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	<div style=\"width:5%;float:left; \">   <span style=\"margin-left:10px;\"> &nbsp;</span>  </div>\r\n" + 
                "    <div style=\"width:4%;float:left; \">   <span>(i)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "    	The Promoter/Land&nbsp;Owner&nbsp;has absolute, clear and marketable title with respect to the said Land; the requisite rights to carry out development upon the said Land and absolute, actual, physical and legal possession of the said Land for the Project\r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	<div style=\"width:5%;float:left; \">   <span style=\"margin-left:10px;\"> &nbsp;</span>  </div>\r\n" + 
                "    <div style=\"width:4%;float:left; \">   <span>(ii)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "    	The Promoter has lawful rights and requisite approvals from the&nbsp;&nbsp;competent Authorities to carry out development of the Project;\r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	<div style=\"width:5%;float:left; \">   <span style=\"margin-left:10px;\"> &nbsp;</span>  </div>\r\n" + 
                "    <div style=\"width:4%;float:left; \">   <span>(iii)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "    	There are no encumbrances upon the said Land or the Project;\r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	<div style=\"width:5%;float:left; \">   <span style=\"margin-left:10px;\"> &nbsp;</span>  </div>\r\n" + 
                "    <div style=\"width:4%;float:left; \">   <span>(iv)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "    	There are no litigations pending before any Court of law or Authority with respect to the said Land, Project or the Apartment;\r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	<div style=\"width:5%;float:left; \">   <span style=\"margin-left:10px;\"> &nbsp;</span>  </div>\r\n" + 
                "    <div style=\"width:4%;float:left; \">   <span>(v)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "    	All approvals, licenses and permits issued by the competent&nbsp;&nbsp;authorities with respect to the Project, said Land and Apartment are valid and subsisting and have been obtained by following due process of law. Further, the Promoter has been and shall, at all times, remain to be in compliance with all applicable laws in relation to the Project, said Land, Building and Apartment and common areas;\r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	<div style=\"width:5%;float:left; \">   <span style=\"margin-left:10px;\"> &nbsp;</span>  </div>\r\n" + 
                "    <div style=\"width:4%;float:left; \">   <span>(vi)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "    	The Promoter has the right to enter into this Agreement and has not committed or omitted to perform any act or thing, whereby the right, title and interest of the Allottee created herein, may prejudicially be affected;\r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	<div style=\"width:5%;float:left; \">   <span style=\"margin-left:10px;\"> &nbsp;</span>  </div>\r\n" + 
                "    <div style=\"width:4%;float:left; \">   <span>(vii)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "    	The Promoter confirms that the Promoter is not restricted in any&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;manner whatsoever from selling the said Apartment to the Allottee in the manner contemplated in this Agreement;\r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	<div style=\"width:5%;float:left; \">   <span style=\"margin-left:10px;\"> &nbsp;</span>  </div>\r\n" + 
                "    <div style=\"width:5%;float:left; \">   <span>(viii)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "    	The Promoter confirms that the Promoter is not restricted in any manner whatsoever from selling the said Apartment to the Allottee in the manner contemplated in this Agreement;\r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	<div style=\"width:5%;float:left; \">   <span style=\"margin-left:10px;\"> &nbsp;</span>  </div>\r\n" + 
                "    <div style=\"width:4%;float:left; \">   <span>(ix)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "    	At the time of execution of the conveyance deed the Promoter shall handover lawful, vacant, peaceful, physical possession of the Apartment to the Allottee and the common areas to the Association of the allottees.\r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	<div style=\"width:5%;float:left; \">   <span style=\"margin-left:10px;\"> &nbsp;</span>  </div>\r\n" + 
                "    <div style=\"width:4%;float:left; \">   <span>(x)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "    	The Schedule Property is not the subject matter of any HUF and that no part thereof is owned by any minor and/or no minor has any right, title and claim over the Schedule Property;\r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	<div style=\"width:5%;float:left; \">   <span style=\"margin-left:10px;\"> &nbsp;</span>  </div>\r\n" + 
                "    <div style=\"width:4%;float:left; \">   <span>(xi)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "    	The Promoter has duly paid and shall continue to pay and discharge all governmental dues, rates, charges and taxes and other monies, levies, impositions, premiums, damages and/or penalties and other outgoings, whatsoever, payable with respect to the said project to the competent Authorities till the occupancy certificate has been issued and possession of apartment,&nbsp;,&nbsp;along with common areas (equipped with all the specifications, amenities and facilities) has been handed over to the allottee&nbsp;and the association of allottees;\r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	<div style=\"width:5%;float:left; \">   <span style=\"margin-left:10px;\"> &nbsp;</span>  </div>\r\n" + 
                "    <div style=\"width:4%;float:left; \">   <span>(xii)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "		No notice from the Government or any other local body or authority&nbsp;&nbsp;&nbsp;or any legislative enactment, government ordinance, order, notification (including any notice for acquisition or requisition of the said property) has been received by or served upon the Promoter in respect of the said Land and/or the Project.\r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>9 &nbsp;</strong><strong>EVENTS OF DEFAULTS AND CONSEQUENCES:</strong></p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">9.1&nbsp;&nbsp;&nbsp;Subject to the Force Majeure clause, the Promoter shall be considered under a condition of Default, in the following events:</p>\r\n" + 
                "<p class=\"moveRightSideEighteenthpx\" style=\"font-weight: 80;\">(i)&nbsp;Promoter fails&nbsp;&nbsp;to provide ready to move in&nbsp;possession of the Apartment to the Allottee within the time period specified in para&nbsp;7.1&nbsp;or fails to complete the project within the stipulated time disclosed at the time of registration of the project with the Authority. For the purpose of this para, 'ready to move in possession' shall mean that the apartment shall be in a habitable condition which is complete in all respect including the provision of all specifications, amenities and facilities, as agreed to between the parties, and for which occupation certificate, as the case may be, has been issued by the competent authority;</p>\r\n" + 
                "<p class=\"moveRightSideEighteenthpx\" style=\"font-weight: 80;\">(ii)&nbsp;Discontinuance of the Promoter's business as a developer on account of suspension or revocation of his registration under the provisions of the Act or the rules or regulations made thereunder.</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">9.2&nbsp;&nbsp;&nbsp;In case of Default by Promoter under the conditions listed above, Allottee is entitled to the following:</p>\r\n" + 
                "<p class=\"moveRightSideEighteenthpx\" style=\"font-weight: 80;\">(i)&nbsp;Stop making further payments to Promoter as demanded by the Promoter. If the Allottee stops making payments, the Promoter shall correct the situation by completing the construction milestones and only thereafter the Allottee be required to make the next payment without any interest; or</p>\r\n" + 
                "<p class=\"moveRightSideEighteenthpx\" style=\"font-weight: 80;\">(ii)&nbsp;The Allottee shall have the option of terminating the Agreement in which case the Promoter shall be liable to refund the entire money paid by the Allottee under any head whatsoever towards the purchase of the apartment, along with interest at the rate prescribed in the Rules within&nbsp;Sixty&nbsp;days of receiving the termination notice:</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">Provided that where an Allottee does not intend to withdraw from the project or terminate the Agreement, he shall be paid, by the promoter, interest at the rate prescribed in the Rules, for every month of delay till the handing over of the possession of the Apartment, which shall be paid by the promoter to the allottee within&nbsp;Sixty&nbsp;days of it becoming due.</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">9.3&nbsp;The Allottee shall be considered under a condition of Default, on the occurrence of the following events:</p>\r\n" + 
                "<p class=\"moveRightSideEighteenthpx\" style=\"font-weight: 80;\">(i)&nbsp;In case the Allottee fails to make payments for Two consecutive demands made by the Promoter as per the Payment Plan annexed hereto, despite having been issued notice in that regard the allottee shall be liable to pay interest to the promoter on the unpaid amount&nbsp;from the due date till the date of payment at the rate prescribed in the Rules</p>\r\n" + 
                "<p class=\"moveRightSideEighteenthpx\" style=\"font-weight: 80;\">(ii)&nbsp;In case of Default by Allottee under the condition listed above continues for a period beyond Two consecutive months after notice from the Promoter in this regard, the Promoter may cancel the allotment of the Apartment, in favour of the Allottee and refund the money paid to him by the allottee by deducting the booking amount (which is 10% of Total sale consideration) and statutory expenses like taxes paid to the Government authorities and the interest liabilities and this Agreement shall thereupon stand terminated. Provided that the promoter shall intimate the allottee about such termination at least thirty days prior to such termination.&nbsp;The balance amount of money payable to&nbsp;&nbsp;the Allottee/s shall be returned by the Promoter against the execution of duly stamped Deed of cancellation of Agreement of sale, to the Allottee within 60 days of such cancellation.</p>\r\n" + 
                "<p class=\"moveRightSideEighteenthpx\" style=\"font-weight: 80;\">(iii)&nbsp;If the Allottee/s has availed loan from any financial institution or the bank, then in that event based on the terms of such loan, all amounts to be refunded to the Allottee/s in terms of clause 9.3 (ii) above shall be paid over to the financial institution or the bank against issuance \"no claim certificate\" in favour of the Promoter and the Promoter shall also be entitled to receive the original of this Agreement that may have been deposited by the Allottee/s with the bank or any financial institution.</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>10. CONVEYANCE OF THE SAID APARTMENT:&nbsp;</strong></p>\r\n" + 
                "<p style=\"font-weight: 80;\">The Promoter, on receipt of Total Price of the Apartment as per 1.2 under the Agreement&nbsp;and other amounts mentioned in&nbsp;(SCHEDULE C) Payment Plan from the Allottee, shall execute a conveyance deed and convey the title of the Apartment together with proportionate indivisible share in the Common Areas within&nbsp;3&nbsp;(Three) months from the date of issuance of the occupancy certificate,&nbsp;to the allottee.&nbsp;However, in case the Allottee fails to&nbsp;pay all the dues under this agreement, the applicable&nbsp;deposit the stamp duty and / or registration charges within the period mentioned in the notice, the allottee authorized the promoter to withhold registration of the conveyance deed in his/her favour till such payment of stamp duty and registration charges to the promoter is made by the allottee.</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>11. MAINTENANCE OF THE SAID BUILDING / APARTMENT / PROJECT:</strong></p>\r\n" + 
                "<p style=\"font-weight: 80;\">The Promoter shall be responsible to provide and maintain essential services in the Project for Twelve months from the date of issuance of Occupancy certificate and at the end of Twelve months will handover&nbsp;&nbsp;over of the maintenance of the project to the association of the allottees. The cost of such maintenance has been included in the Total Price of the Apartment.</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>12. DEFECT LIABILITY:</strong></p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">It is agreed that in case&nbsp;&nbsp;any structural defect or any other defect in workmanship, quality or provision of services or any other obligations of the Promoter as per the agreement for sale relating to such development is brought to the notice of the Promoter&nbsp;within a period of 5 (five) years by the Allottee from the date of handing over possession, it shall be the duty of the Promoter to rectify such defects without further charge, within 30 (thirty) days, and in the event of Promoter's failure to rectify such defects within such time, the aggrieved Allottees shall be entitled to receive appropriate compensation in the manner as provided under the Act.</p>\r\n" + 
                "<p style=\"font-weight: 80;\">The Promoter shall not be liable for any manufacturing or other defects of any branded inputs or fixtures or services of any third party mentioned in the 'Schedule D' to this Agreement, unless it results in structural defect.</p>\r\n" + 
                "<p style=\"font-weight: 80;\">The Allottee/s and the Association of Apartment Owners shall take the responsibility for proper safety, maintenance (including continuance of annual maintenance/insurance contracts/agreements) and upkeep of all the fixtures, equipment and machinery provided by the Promoter, for which the Promoter shall not be liable after handing over. Further, the Allottee/s (and the association of Apartment Owners / Allottee/s) is expected to periodically maintain / upkeep the various facilities / components of the Project as per the handover manual provided by the Promoter and the various vendors of the Project. The Promoter is fully indemnified by the Allottee/s / Association of Apartment Owners, if any defect arising out of non-compliance of the terms of this Handover Manual.</p>\r\n" + 
                "<p style=\"font-weight: 80;\">Notwithstanding anything contained in the above clause the following exclusions are made:</p>\r\n" + 
                "\r\n" + 
                "\r\n" + 
                "<p class=\"moveRightSideEighteenthpx\" style=\"font-weight: 80;\">a. Equipment (lifts, generator, motors, STP, transformers, gym equipment etc.,) which carry manufacturer's guarantees for a limited period. Thereafter the association shall take annual maintenance contract with the suppliers. The Promoter shall transfer manufacturer's guarantees/warrantees to the Allottee/s or association of Apartment Owners, as the case may be.</p>\r\n" + 
                "<p class=\"moveRightSideEighteenthpx\" style=\"font-weight: 80;\">b. Fittings related to plumbing, sanitary, electrical, hardware, etc., having natural wear and tear.</p>\r\n" + 
                "<p class=\"moveRightSideEighteenthpx\" style=\"font-weight: 80;\">c. Allowable structural and other deformations including expansion&nbsp;&nbsp;quotient.</p>\r\n" + 
                "<p class=\"moveRightSideEighteenthpx\" style=\"font-weight: 80;\">d. The terms of work like painting etc., which are subject to wear and tear.</p>\r\n" + 
                "<p class=\"moveRightSideEighteenthpx\" style=\"font-weight: 80;\">e. The Promoter shall not be responsible for routine/non-structural cracks&nbsp;resulting from differential co-efficient of thermal expansion, non-monolithic joints,&nbsp;seasoning effects, sweating of walls, etc. and such other defects caused due to&nbsp;normal wear and tear, abuse and improper usage.</p>\r\n" + 
                "<p class=\"moveRightSideEighteenthpx\" style=\"font-weight: 80;\">f. The&nbsp;Promoter&nbsp;shall not be responsible for issues such as difference in shades&nbsp;of tiles, Tolerances as per IS and building codes, Air Pockets beneath tiles,&nbsp;Separation cracks / gaps between non homogeneous building components, slopes&nbsp;considered for water drainage, reduction in carpet area due to plaster thickness&nbsp;and skirting.&nbsp;Defects arising from&nbsp;natural&nbsp;wear and tear/forced/intentional/accidental damages do not come under the scope of maintenance&nbsp;under defect liability. Any defects or damages caused to glass, ceramic, vitrified,&nbsp;porcelain materials shall not come under the defect liability after accepting&nbsp;possession of the apartment</p>\r\n" + 
                "\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">The Allottee/s shall maintain the apartments in good tenantable conditions and carry out the internal repairs for the upkeep of the apartments. The association of the Apartment Owners or its assigns shall maintain the services and amenities in good condition and covered with proper AMC from vendor nominated service providers and insurance. The obligation of the Promoter shall be subject to proper maintenance and upkeep of the apartments/services and amenities by the Allottee/s or the association of apartment owners, as the case may be.</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>13. RIGHT TO ENTER THE APARTMENT FOR REPAIRS:</strong></p>\r\n" + 
                "<p style=\"font-weight: 80;\">The Promoter /maintenance agency /association of allottees shall have rights of unrestricted access of all Common Areas, garages/covered parking and parking spaces for providing necessary maintenance services and the Allottee agrees to permit the association of Allottees and/or maintenance agency to enter into the Apartment or any part thereof, after due notice and during the normal working hours, unless the circumstances warrant otherwise, with a view to set right any defect.</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>14. USAGE:</strong></p>\r\n" + 
                "<p style=\"font-weight: 80;\">Use of Basement and Service Areas: The basement(s) and service areas, if any, as located within the&nbsp;<strong>\"</strong><strong>SUMADHURA SUSHANTHAM\"</strong>,&nbsp;shall be earmarked for purposes such as parking spaces and services including but not limited to electric sub-station, transformer, DG set rooms, underground water tanks, pump rooms, maintenance and service rooms, fire fighting pumps and equipment's etc. and other permitted uses as per sanctioned plans. The Allottee shall not be permitted to use the services areas and the basements in any manner whatsoever, other than those earmarked as parking spaces, and the same shall be reserved for use by the association of Allottees formed by the Allottees for rendering maintenance services.</p>\r\n" + 
                "\r\n" + 
                "<ol><li style=\"font-weight: 80;\"><strong>15. GENERAL COMPLIANCE WITH RESPECT TO THE APARTMENT:</strong></li>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">15.1 Subject to Para 12 above, the Allottee shall, after taking possession, be solely responsible to maintain the Apartment at his/her own cost, in good repair and condition and shall not do or suffer to be done&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;anything in or to the Building, or the Apartment or the staircases, lifts, common passages, corridors, circulation areas, atrium or the compound which may be in violation of any laws or rules of any authority or change or alter or make additions to the Apartment and keep the Apartment, its walls and partitions, sewers, drains, pipe and appurtenances thereto or belonging thereto, in good and tenantable repair and maintain the same in a fit and proper condition and ensure that the support, shelter etc. of the Building is not in any way damaged or jeopardized.</p>\r\n" + 
                "<p style=\"font-weight: 80;\">15.2 The Allottee further undertakes, assures and guarantees that&nbsp;&nbsp;he/she would not put any sign-board / name-plate, neon light, publicity material or advertisement material etc. on the face / facade of the Building or anywhere on the exterior of the Project, buildings therein or Common Areas.The Allottees shall also not change the colour scheme of the outer walls or painting of the exterior side of the windows or carry out any change in the exterior elevation or design. Further the Allottee shall not store any&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;hazardous or combustible goods in the Apartment or place any heavy material in the common passages or staircase of the Building. The Allottee shall also not remove any wall, including the outer and load bearing wall of the Apartment.</p>\r\n" + 
                "<p style=\"font-weight: 80;\">15.3 The Allottee shall plan and distribute its electrical load in conformity with the electrical systems installed by the Promoter and thereafter the association of Allottees and/or maintenance agency appointed by association of Allottees. The Allottee shall be responsible for any loss or damages arising out of breach of any of the aforesaid conditions.</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>16. COMPLIANCE OF LAWS, NOTIFICATIONS ETC. BY PARTIES:</strong></p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">The parties are entering into this Agreement for the allotment of Apartment with the full knowledge of all laws, rules, regulations, notifications applicable to the Project.</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>17. ADDITIONAL CONSTRUCTIONS:</strong></p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">The Promoter undertakes that it has no right to make additions or to put up additional structure(s) anywhere in the Project after the building plan, layout plan, sanction plan and specifications, amenities and facilities has been approved by the competent authority(ies) and disclosed, except for as provided in the Act.</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>18. PROMOTER SHALL NOT MORTGAGE OR CREATE A CHARGE:</strong><strong>:</strong></p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">After the Promoter executes this Agreement he shall not mortgage or create a charge on the Apartment/Building and if any such mortgage or charge is made or created then notwithstanding anything contained in any other law for the time being in force, such mortgage or charge shall not affect the right and interest of the Allottee who has taken or agreed to take such Apartment/Building.</p>\r\n" + 
                "<p style=\"font-weight: 80;\">Notwithstanding any other term of this Agreement, the Allottee hereby authorizes and permits the Promoter to raise finance/loan from any institution / company / bank by any mode or manner by way of charge/mortgage/securitization of the Apartment / Project / Building or the land underneath or the receivables, subject to the condition that the Apartment/plot shall be made free from all encumbrances at the time of execution of the Sale Deed in favour of the Allottee(s). The Allottee shall be informed about the same at the time of agreement.&nbsp;&nbsp;</p>\r\n" + 
                "\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>19. THE KARNATAKA APARTMENT OWNERSHIP ACT, 1972 and THE KARNATAKA OWNERSHIP FLATS (REGULATION OF THE PROMOTION OF THE CONSTRUCTION, SALE, MANAGEMENT AND TRANSFER) ACT, 1972:</strong></p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">The Promoter has assured the Allottees that the project in its entirety is in accordance with the provisions of the Karnataka Apartment of Ownership Act, 1972 (Karnataka Act 17 of 1973) and the Karnataka Ownership Flats (Regulation of the Promotion of the Construction, Sale, Management and Transfer) Act, 1972. The Promoter showing compliance of various laws/regulations as applicable in the State of Karnataka and its revision thereafter from time to time.</p>\r\n" + 
                "\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>20. BINDING EFFECT:</strong></p>\r\n" + 
                "\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">Forwarding this Agreement to the Allottee/s by the Promoter does not create a binding obligation on the part of the Promoter or the Allottee/s until, firstly, the Allottee/s signs and delivers this Agreement with all the schedules along with the payments due as stipulated in the Payment Plan within&nbsp;<strong>30 (thirty)</strong>&nbsp;days from the date of receipt of the draft of this Agreement to Sell by the Allottee/s. If the Allottee/s fails to execute and deliver to the Promoter this Agreement within 30 (thirty) days from the date of its receipt by the Allottee/s, then the Promoter shall serve a notice to the Allottee/s for rectifying the default, which if not rectified within&nbsp;<strong>30 (Thirty)</strong>&nbsp;days from the date of its receipt by the Allottee/s, application of the Allottee/s shall be treated as cancelled and all sums deposited by the Allottee/s in connection therewith including the booking amount shall be returned to the Allottee/s without any interest or compensation whatsoever.&nbsp;</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>21. ENTIRE AGREEMENT:</strong></p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">This Agreement, along with its schedules, constitutes the entire Agreement between the Parties with respect to the subject matter hereof and supersedes any and all understandings, any other agreements, allotment letter, correspondences, arrangements whether written or oral, if any, between the Parties&nbsp;in regard to the said apartment/plot/building, as the case may be.</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>22. RIGHT TO AMEND:</strong></p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">This Agreement may only be amended through written consent of the Parties.&nbsp;</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>23. PROVISIONS OF THIS AGREEMENT APPLICABLE ON ALLOTTEE/S / SUBSEQUENT ALLOTTEE/S:</strong></p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">It is clearly understood and so agreed by and between the Parties hereto that all the provisions contained herein and the obligations arising here under in respect of the Apartment and the project shall equally be applicable to and enforceable against and by any subsequent Allottees of the Apartment, in case of a transfer, as the said obligations go along with the Apartment for all intents and purposes.</p>\r\n" + 
                "\r\n" + 
                "<li style=\"font-weight: 80;\"><strong>24. WAIVER NOT A LIMITATION TO ENFORCE:</strong></li>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">24.1 The Promoter may, at its sole option and discretion, without prejudice to its rights as set out in this Agreement, waive the breach by the Allottee in not making payments as per the Payment Plan [Schedule C] including waiving the payment of interest for delayed payment. It is made clear and so agreed by the Allottee that exercise of discretion by the Promoter in the case of one Allottee shall not be construed to be a precedent and /or binding on the Promoter to exercise such discretion in the case of other Allottees.</p>\r\n" + 
                "<p style=\"font-weight: 80;\">24.2 Failure on the part of the Promoter to enforce at any time or for any period of time the provisions hereof shall not be construed to be a waiver of any provisions or of the right thereafter to enforce each and every provision.</p>\r\n" + 
                "\r\n" + 
                "<li style=\"font-weight: 80;\"><strong>25. SEVERABILITY:</strong></li>\r\n" + 
                "<p style=\"font-weight: 80;\">If any provision of this Agreement shall be determined to be void or unenforceable under the Act or the Rules and Regulations made thereunder or under other applicable laws, such provisions of the Agreement shall be deemed amended or deleted in so far as reasonably inconsistent with the purpose of this Agreement and to the extent necessary to conform to Act or the Rules and Regulations made thereunder or the applicable law, as the case may be, and the remaining provisions of this Agreement shall remain valid and enforceable as applicable at the time of execution of this Agreement.</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>26. METHOD OF CALCULATION OF PROPORTIONATE SHARE WHEREVER REFERRED TO IN THE AGREEMENT:</strong></p>\r\n" + 
                "<p style=\"font-weight: 80;\">Wherever in this Agreement it is stipulated that the Allottee has to make any payment, in common with other Allottee(s) in Project, the same shall be the proportion which the Super Builtup area of the Apartment bears to the total Super Builtup area of all the Apartments in the Project.</p>\r\n" + 
                "\r\n" + 
                "<li style=\"font-weight: 80;\"><strong>27. FURTHER ASSURANCES:</strong></li>\r\n" + 
                "<p style=\"font-weight: 80;\">Both Parties agree that they shall execute, acknowledge and deliver to the other such instruments and take such other actions, in additions to the instruments and actions specifically provided for herein, as may be reasonably required in order to effectuate the provisions of this Agreement or of any transaction contemplated herein or to confirm or perfect any right to be created or transferred here under or pursuant to any such transaction.</p>\r\n" + 
                "\r\n" + 
                "<li style=\"font-weight: 80;\"><strong>28. PLACE OF EXECUTION:</strong></li>\r\n" + 
                "<p style=\"font-weight: 80;\">The execution of this Agreement shall be complete only upon its execution by the Promoter through its authorized signatory at the Promoter's Office, or at some other place, which may be decided by the Promoter.</p>\r\n" + 
                "\r\n" + 
                "<li style=\"font-weight: 80;\"><strong>29. NOTICES:</strong></li></ol>\r\n" + 
                "<p style=\"font-weight: 80;\">That all notices to be served on the Allottee and the Promoter as contemplated by this Agreement shall be deemed to have been duly served if sent to the Allottee or the Promoter by Registered Post at their respective addresses specified below:</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>1.Mr. Murali Nelanti</strong></p>\r\n" + 
                "\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">\r\n" + 
                "<strong>\r\n" + 
                "	      	Residing at :  \r\n" + 
                "     </strong>\r\n" + 
                "\r\n" + 
                "&nbsp; 5-31, Ghattuppala, Chandur Mandal, Ghattuppala, Nalgonda, Telangana, 508253.</p>\r\n" + 
                "<p><strong>E-mail: user@gmail.com</strong></p>\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>And,</strong></p>\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>M/s. SUMADHURA INFRACON PVT.LTD</strong>&nbsp;A company incorporated under the provisions of Indian Companies Act 1956, having its registered office at:&nbsp;&nbsp;Sy.No.108/2, Millenia Building, 1<sup>st</sup>&nbsp;Main MSR Layout, Munnekollala Village, Marathahalli, Outer Ring Road, Bangalore &ndash; 560 037.</p>\r\n" + 
                "<p style=\"font-weight: 80;\">It shall be the duty of the Allottee and the promoter to inform each other of any change in address subsequent to the execution of this Agreement in the above address by Registered Post failing which all communications and letters posted at the above address shall be deemed to have been received by the promoter or the Allottee, as the case may be.</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>30. JOINT ALLOTTEE/S:</strong></p>\r\n" + 
                "<p style=\"font-weight: 80;\">That in case there are Joint Allottees all communications shall be sent by the Promoter to the Allottee whose name appears first and at the address given by him/her which shall for all intents and purposes to consider as properly served on all the Allottees.</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>31. SAVINGS:</strong></p>\r\n" + 
                "<p style=\"font-weight: 80;\">Any application letter, allotment letter, agreement, or any other document signed by the allottee, in respect of the apartment, plot or building, as the case may be, prior to the execution&nbsp;and registration&nbsp;of this Agreement for Sale for such apartment, plot or building, as the case may be, shall not be constructed to limit the rights and interest of the allottee under the Agreement of Sale or under the Act or the rules or the regulations made thereunder.</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>32. GOVERNING LAW:</strong></p>\r\n" + 
                "<p style=\"font-weight: 80;\">That the rights and obligations of the parties under or arising out of this Agreement shall be construed and enforced in accordance with the Act and the Rules and Regulations made thereunder including other applicable laws of India for the time being in force.</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>33. DISPUTE RESOLUTION:</strong></p>\r\n" + 
                "<p style=\"font-weight: 80;\">All or any disputes arising out or touching upon or in relation to the terms and conditions of this Agreement, including the interpretation and validity of the terms thereof and the respective rights and obligations of the Parties, shall be settled amicably by mutual discussion, failing which the same shall be settled through the adjudicating officer appointed under the Act.</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>34. THE ALLOTTEE/S (by himself / themselves with intention to bring all persons into whomsoever hands the Apartment may come) ASSURES, UNDERTAKES AND COVENANTS WITH THE PROMOTER AS FOLLOWS:</strong></p>\r\n" + 
                "<p style=\"font-weight: 80;\">a. The Allottee/s and along with other owners of the apartments in the Project undertake to join the Association to be formed by the Promoter. The Allottee/s along with the Sale Deed will also execute Form B as provided in the Karnataka Apartment Ownership Act,1972.</p>\r\n" + 
                "<p style=\"font-weight: 80;\">b. That after the Project is handed over to the Owners Association as per this Agreement, the Promoter shall not be responsible for any consequence or liability on account of failure, negligence, act or omission, obstruction, alteration, modification, restraint or improper use by any or all the owners, service providers or their agents with regards to the Common Amenities and Facilities as well as the fire safety equipment, fire protection systems, their supporting equipment, pollution control and other general safety equipment, related facilities and services. The Allottee/s with the other owners shall ensure that periodical inspections of all such Common Amenities and Facilities are done so as to ensure proper functioning thereof.</p>\r\n" + 
                "<p style=\"font-weight: 80;\">c. After the maintenance of the building is handed over to the Association, the Promoter shall not be responsible for any consequence or liability on account of failure, negligence, act or omission, obstruction, alteration, modification, restraint or improper use by any or all the owners, service providers or their agents with regard to the Common Amenities and Facilities of the Project.</p>\r\n" + 
                "<p style=\"font-weight: 80;\">d. The Allottee/s covenants that the Allottee/s shall be bound and liable to comply with the obligations set out inClause 40and will have the rights set out in&nbsp;Clause 39,&nbsp;in the enjoyment of the Schedule \"A\" Apartment and the Common Areas and the Common Amenities and Facilities of the Project on the Schedule \"A\" Apartment being complete and handover.</p>\r\n" + 
                "<p style=\"font-weight: 80;\">e. That the Allottee/s shall not have any right, at any time whatsoever, to obstruct or hinder the progress of the construction of the Project on the Schedule \"F\" Property.</p>\r\n" + 
                "<p style=\"font-weight: 80;\">f. It is an essential term of the sale under this Agreement that the Allottee/s shall not be entitled to change/alter the name of the Project,<strong>\"SUMADHURA SUSHANTHAM\"</strong>to any other name. However, the Promoter shall be entitled to make the change in the name at any time before completion of the Project and before executing any sale deed in favour of any buyers of apartments in the said development.</p>\r\n" + 
                "<p style=\"font-weight: 80;\">g. All interior related works by the Allottee/s can be taken up only after handing over possession of the Schedule \"A\" Apartment to the Allottee/s. The Promoter have no responsibility for any breakages, damages caused to any of the finishing works or to the structure already handed over to the Allottee/s. The Promoter are not responsible for any thefts during the course of the interior works.</p>\r\n" + 
                "<p style=\"font-weight: 80;\">h. The Allottee/s covenants to comply with and adhere to all the rules and regulations pertaining to Common Areas, Common Amenities and Facilities of the Project and shall not obstruct the usage of the any Common Areas and Common Amenities by placing any objects, vehicles and other articles.</p>\r\n" + 
                "<p style=\"font-weight: 80;\">i. The Allottee/s, upon handover, shall be solely responsible to maintain and upkeep the Schedule \"A\" Apartment at his/her/their own cost including all the walls, drains, pipes and other fittings and in particular which supports other the parts of the building and to carry out any internal works or repairs as may be required by the Association;</p>\r\n" + 
                "<p style=\"font-weight: 80;\">j. The Allottee/s shall not to store in the Apartment any goods which are of hazardous, combustible or dangerous nature or are so heavy as to damage the construction or structure of the building in which the Apartment is situated or storing of which goods is objected to by the concerned local or other authority and shall take care while carrying heavy packages which may damage or likely to damage the staircases, common passages or any other structure of the building in which the Apartment is situated, including entrances of the building in which the Apartment is situated and in case any damage is caused to the building in which the Apartment is situated or the Apartment on account of negligence or default of the Allottee/s in this behalf, the Allottee/s shall be liable for the consequences of the breach.</p>\r\n" + 
                "<p style=\"font-weight: 80;\">k. The Allottee/s shall carry out at his/her/their own cost all internal repairs to the said Apartment and maintain the Apartment in the same condition, state and order in which it was delivered by the Promoter to the Allottee/s and shall not do or suffer to be done anything in or to the building in which the Apartment is situated or the Apartment which may be contrary to the rules and regulations and bye-laws of the concerned local authority or other public authority. In the event of the Allottee/s committing any act in contravention of the above provision, the Allottee/s shall be responsible and liable for the consequences thereof to the concerned local authority and/or other public authority.</p>\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>35. ASSIGNMENT:</strong></p>\r\n" + 
                "\r\n" + 
                "<p class=\"moveRightSideEighteenthpx\">a. &nbsp;&nbsp;&nbsp;The Allottee/s hereby agrees and confirms that this Agreement is not transferable / assignable to any other third party or entity except as provided in clauses&nbsp;below in 35(b) &amp; (c).</p>\r\n" + 
                "<p class=\"moveRightSideEighteenthpx\">b. &nbsp;&nbsp;&nbsp;That the Allottee/s shall not be entitled to transfer / assign the rights under this Agreement for a period of Two (2) years from the date of this Agreement.</p>\r\n" + 
                "<p class=\"moveRightSideEighteenthpx\">c. &nbsp;&nbsp;&nbsp;Any assignment shall be, subject to clause 35(b), shall be permitted only by way of written agreement between the Promoter and the Allottee/s and the transferee/assignee. The transferee/assignee shall undertake to be bound by the terms of this Agreement including payment of the transfer fees of Rs.200/- (Rupees Two Hundred Only) per square foot on Super built up area of the Schedule 'A' Apartment. The transfer fee under this clause shall not apply in case of transfer made to and between the immediate family members i.e., father, mother, brother, sister, son, daughter, husband and wife.</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>36. ASSOCIATION OF OWNERS:</strong></p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">The Promoter shall form an Association of Apartment Owners, within three months from the date of issuance of occupation certificate.<br/>\r\n" + 
                "The occupation and use&nbsp;by the Allottee/s of the Schedule \"A\" Apartment and all amenities in the Project shall be governed&nbsp;<em>inter alia</em>&nbsp;by the rules and regulations, Bye laws of Association contained in Deed of Declaration. The Allottee/s shall not at any time question or challenge the validity or the binding nature of such rules and regulations and shall at all times comply with the same. All the present or future owners, tenants, guests and licensees or their employees, or any other Person who is lawfully entitled to occupy and use the Schedule \"A\" Apartment, in any manner whatsoever, shall be subject to such rules and regulations.<br/>\r\n" + 
                "Notwithstanding any other rule, even after common areas and maintenance is handed over to the Association of Apartment Allottee/s herein, the Promoter shall continue to have the rights and entitlement to advertise, market, book, sell or offer to sell or allot to person to purchase any apartment or building or plot which is still not sold or allotted and shall be deemed to have been allowed to do so by the Association of Apartment Owners without any restriction or entry of the building and development of common areas.<br/>\r\n" + 
                "The Allottee/s shall also observe and abide by all the Bye-laws, Rules and Regulations prescribed by the Municipality or State/Central Government Authority, in regard to ownership or enjoyment of Schedule \"A\" Apartment apart from the rules and regulations of the Association.</p>\r\n" + 
                "\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>37. CAR PARKING ALLOTMET:</strong></p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">The default car park will be allotted at appropriate time as per the Agreement seniority on a first &ndash; come &ndash; first served basis by developer. Allotment of additional car parks other than the default car parking is subject to availability and will be done on a first &ndash; come &ndash; first served basis as per the price fixed at the time of allotment.</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>38. CORPUS FUND</strong>- The Promoter decided to float a Corpus Fund for the entire Complex which is payable by the ultimate Allottee/s of the residential apartments/flats including the Allottee/s herein and such Corpus Fund is\r\n" + 
                "\r\n" + 
                "Rs.  50.00/-( Rupees Only)\r\n" + 
                " per Sq.Ft of SBUA of Schedule 'A' Apartment and the Allottee/s herein hereby agrees and undertakes to pay the said amount of Corpus Fund to the Promoter of Second party herein at the time of handing over&nbsp;&nbsp;possession of the Schedule&nbsp;&nbsp;'A' Residential Flat/ Apartment and such fund will be governed and held initially by the /Promoter herein and subsequently by the Association. The Promoter shall transfer the Corpus Fund to the account of Association at the time of Handing over of common areas and maintenance of the Project to the association of allottees and the Association shall keep the said Corpus Fund in a fixed deposit with any nationalized bank and the interest earned on such fixed deposit from time to time shall be utilized to meet the long term maintenance expenses and capital expenses to be incurred for repairs and replacement of the major items relating to the common amenities such as generators, motors, water pumps, common lawns, gates, laying of roads/driveways, pipelines etc. and if at any point of time interest generated/earned&nbsp;&nbsp;on the Corpus Fund is not sufficient to meet such expenditure, the residue/deficit required shall be contributed by all the owners/occupants in the Complex in the same proportion in which they contribute the monthly maintenance charges.</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>39. RIGHTS OF THE ALLOTTEE /S</strong></p>\r\n" + 
                "<p style=\"font-weight: 80;\">The Allottee/s shall, in the course of ownership of Schedule 'A' Apartment have the following rights:</p>\r\n" + 
                "\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span>i)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "    	The right to get constructed and own the Apartment described in the Schedule 'A' above for residential purposes subject to the terms of this Agreement.\r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span>ii)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "    	The right and liberty to the Allottee/s and all persons entitled, authorized or permitted by the Allottee/s&nbsp;&nbsp;(in common with all other persons entitled, permitted or authorized to a similar right) at all times, and for all purposes, to use the staircases, passages and common areas in the Building for ingress and egress and use in common;\r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span>iii)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "    	The right to subjacent lateral, vertical and horizontal support for the Schedule 'A' Apartment from the other parts of the Building;\r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span>iv)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "    	The right to free and uninterrupted passage of water, electricity, sewerage, etc., from and to the Schedule 'A' Apartment through the pipes, wires, sewer lines, drain and water courses, cables, pipes and wires which are or may at any time hereafter be, in, under or passing through the Building or any part thereof by the Schedule 'F' Property;\r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span>v)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "    	Right to lay cables or wires for Television, Telephone, Data, Cable and such other installations, in any part of the Building, however, recognizing and reciprocating such rights of the other owners;\r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span>vi)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "    	Right of entry and passage for the Allottee/s with/without workmen to other parts of the Building at all reasonable times after notice to enter into and upon other parts of the Building for the purpose of repairs to or maintenance of the Schedule 'A' Apartment or for repairing, cleaning, maintaining or removing the water tanks, sewer, drains and water courses, cables, pipes and wires causing as little disturbance as possible to the other owners and making good any damage caused.\r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span>vii)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "    	Right to use along with all other owners all common facilities and amenities provided therein on payment of such sums as may be prescribed from time to time by the Promoter or the owners' association or the agency looking after the maintenance of common areas and amenities.\r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:5%;float:left; \">   <span>viii)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "    	Right to use and enjoy the Common Areas &amp; Amenities in the Project\"&nbsp;in accordance with the purpose for which they are installed without endangering or encroaching the lawful rights of other owners/users.\r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span>ix)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "    	Right to make use of all the common roads, driveways and passages provided in Schedule `F' Property and the adjoining lands to reach the Schedule 'A' Apartment without causing any obstruction for free movement therein.\r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span>x)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "    	Right to use the Clubhouse and Common amenities built in both Phase-I and Phase-II of the Project. The clubhouse and all the common amenities are part of the common area of all the Apartment units in the Project.\r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span>xi)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "    	Right to use the road access of both Phase-I and Phase-II in the Project.\r\n" + 
                "    </span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>40. OBLIGATIONS ON THE ALLOTTEE/S</strong></p>\r\n" + 
                "\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span>i)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "		Not to raise any construction in addition to that mentioned in Schedule 'A' below.\r\n" + 
                " 	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span>ii)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                " 		Not to use or permit the use of the Schedule `A' Apartment in such manner which would diminish the value of the utility in the property described in the Schedule `F' below.\r\n" + 
                " 	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span>iii)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "   	Not to use the space in the land described in Schedule `F' Property for parking any vehicles or to use the same in any manner which might cause hindrances to or obstruct the movement of vehicles parked in the parking spaces or for users of adjoining properties.\r\n" + 
                " 	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span>iv)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                " 		Not to default in payment of any taxes or government levies to be shared by all the owners of the property described in the Schedule `F' Property.\r\n" + 
                " 	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span>v)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                " 		Not to decorate the exterior part of the Schedule `A' Apartment to be constructed otherwise than in the manner agreed to by at least two third majority of the owners of Apartments in the Project\".\r\n" + 
                " 	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span>vi)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                " 		Not to make any arrangements for maintenance of the building and for ensuring common amenities herein for the benefit of all concerned other than that agreed to by two third majority of all apartment owners.\r\n" + 
                " 	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span>vii)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                " 		The Allottee/s shall have no objection whatsoever to the Promoter managing the building in Schedule 'F' Property by themselves or through a nominated agency and even after handing over the ownership of the common areas and the facilities to the association as soon as it is formed and pending formation of the same, the Promoter shall retain the same and the Allottee/s have given specific consent to this undertaking.\r\n" + 
                " 	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span>viii)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                " 		The Allottee/s shall become a Member of the Owners Association to be formed amongst the owners of apartments in the \"Project\"&nbsp;and agree to observe and perform the terms and conditions and bye-laws and rules and regulations of the Association that may be formed and pay the admission fee and other fees that may be agreed. The&nbsp;maintenance of all the common areas and facilities in the Project shall be done by Promoter/Promoter Appointed Agency for an initial period of 12 months as stated clause 1.2 above&nbsp;and after the expiry of the 12 months, the Allottee/s shall pay all common expenses and other expenses, maintenance charges, taxes and outgoings in to the Promoter or the Promoter appointed agencies, on Cost + 10 % towards overheads and Profit, if it's maintained by the Promoter.&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\r\n" + 
                " 	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span>ix)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "		It is hereby clarified and agreed that all expenses relating to maintenance of common areas, amenities, open spaces, parks, gardens and facilities shall be borne by the owners of Apartments in the \"Project\"&nbsp; No Allottee/s of an apartment in the Project is exempted from payment of common area maintenance expenses by waiver of the use or enjoyment of all or any common areas and facilities or by non-occupation of the apartment.\r\n" + 
                " 	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span>x)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                " 		The Allottee/s shall use the apartment only for residential purposes and the car-parking space for parking a light motor vehicle/s and no other purposes. The Allottee/s shall not use the Schedule 'A' Apartment as serviced apartment or transit accommodation or let it out on temporary basis.\r\n" + 
                " 	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span>xi)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "		The Allottee/s shall not alter the elevations of the apartment building.\r\n" + 
                " 	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span>xii)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                " 		The Allottee/s shall from the date of possession, maintain the apartment at his cost in a good and habitable condition and shall not do or suffer to be done anything in or to the said apartment and/or common passages, or the compound which may be against the rules and bye-laws of the Bangalore Development Authority or any other Authority.&nbsp;&nbsp;The Allottee/s shall keep the apartment, walls, floor, roof, drains, pipes and appurtenances thereto belonging in good condition so as to support, shelter and protect the parts of the entire development and shall not do any work which jeopardizes the soundness or safety of the building or the property or reduce the value thereof or impair any assessment and shall not add any structure or excavate any basement or cellar.&nbsp;&nbsp;The Allottee/s shall promptly report to the Promoter/Maintenance Company/Association of Apartment Owners, as the case may be, of any leakage/seepage of water/sewerage and the like through the roof/floor/wall of the said apartment and especially with regard to the external and common walls shared by the owners.\r\n" + 
                " 	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span>xiii)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "		 &nbsp;&nbsp;It is specific term and condition of this Agreement and of the rights to be created in favor of the prospective buyers of the apartments in&nbsp;\"<strong>the Project</strong>\"&nbsp;that:\r\n" + 
                " 	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	<div style=\"width:5%;float:left; \">   <span style=\"margin-left:10px;\"> &nbsp;</span>  </div>\r\n" + 
                "    <div style=\"width:4%;float:left; \">   <span>a)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "		 the name of the owner and/or apartment number shall be put in standardized letters and coloring only at the spaces designated by the Promoter in the entrance lobby and at the entrance door of the particular apartment but at no other place in the building and the number allotted to any apartment shall not be altered.\r\n" + 
                " 	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	<div style=\"width:5%;float:left; \">   <span style=\"margin-left:10px;\"> &nbsp;</span>  </div>\r\n" + 
                "    <div style=\"width:4%;float:left; \">   <span>b)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "		 no sign board, hoarding or any other logo or sign shall be put up by the buyers on the exterior of the building or on the other wall/s of the apartment.\r\n" + 
                " 	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	<div style=\"width:5%;float:left; \">   <span style=\"margin-left:10px;\"> &nbsp;</span>  </div>\r\n" + 
                "    <div style=\"width:4%;float:left; \">   <span>c)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "		 the Allottee/s shall not alter the color scheme of the exterior of the building or of the exterior lobby wall of the said apartment though the Allottee/s shall be entitled to select and carry out any decoration/painting of the interior of the said apartment.\r\n" + 
                " 	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	<div style=\"width:5%;float:left; \">   <span style=\"margin-left:10px;\"> &nbsp;</span>  </div>\r\n" + 
                "    <div style=\"width:4%;float:left; \">   <span>d)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "		 the Allottee/s shall not do anything that may adversely affect the aesthetic appearance/beauty of the building, nor do anything within the compound of&nbsp;\"the Project\"&nbsp;which may cause any nuisance or obstruction or hindrance to the other owners.\r\n" + 
                " 	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	<div style=\"width:5%;float:left; \">   <span style=\"margin-left:10px;\"> &nbsp;</span>  </div>\r\n" + 
                "    <div style=\"width:4%;float:left; \">   <span>e)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "		 any further or other construction that may be permitted hereafter over and above the construction already sanctioned as aforesaid such construction may be carried out only by the Promoter. The Allottee/s shall not be entitled to object to the same or to cause any obstruction or hindrance thereof, nor to ask for any discount and/or debate and/or abatement in the above mentioned consideration.\r\n" + 
                " 	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "\r\n" + 
                " <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span>xiv)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "    The Allottee/s shall, from time to time, do and execute all further acts, deeds, matters and things as may be reasonably required by the Promoter for duly implementing the terms and intent of this Agreement and for the formation of Owners Association.\r\n" + 
                "	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span>xv)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "		As the Allottee/s will be one of the owners of the apartments in the \"Project\"&nbsp;the Allottee/s shall be entitled to use in common with the all the other buyers/ owners in the \"Project\",&nbsp;the common areas and facilities listed below:\r\n" + 
                "	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "<p class=\"moveRightSideThirtyFivepx\" style=\"font-weight: 80;\">a) driveways, roads, passages, entry and exits;</p>\r\n" + 
                "<p class=\"moveRightSideThirtyFivepx\" style=\"font-weight: 80;\">b) entrance lobby, staircase and corridors in apartment towers;</p>\r\n" + 
                "<p class=\"moveRightSideThirtyFivepx\" style=\"font-weight: 80;\">c) elevators, pumps, generators;</p>\r\n" + 
                "<p class=\"moveRightSideThirtyFivepx\" style=\"font-weight: 80;\">d) open Spaces, common gardens, parks;</p>\r\n" + 
                "<p class=\"moveRightSideThirtyFivepx\" style=\"font-weight: 80;\">e) facilities in club house including the swimming pool/s;</p>\r\n" + 
                "<p class=\"moveRightSideThirtyFivepx\" style=\"font-weight: 80;\">f) any/all other Common Areas and Amenities in the Project\";</p>\r\n" + 
                "\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span>xvi)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "		The Allottee/s shall not at any time cause any annoyance, inconvenience or disturbance or injury to the occupiers of other apartments in the development by:\r\n" + 
                "	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  <div style=\"width:5%;float:left; \">   <span style=\"margin-left:10px;\"> &nbsp;</span>  </div>\r\n" + 
                "    <div style=\"width:4%;float:left; \">   <span>a.</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                " making any alterations in the elevation or both faces of external doors and windows of the apartment/parking space to be acquired by Allottee/s which in the opinion of the Promoter or the Owners Association or Seller differ from the scheme of the building.\r\n" + 
                "	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	<div style=\"width:5%;float:left; \">   <span style=\"margin-left:10px;\"> &nbsp;</span>  </div>\r\n" + 
                "    <div style=\"width:4%;float:left; \">   <span>b.</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                " closing the lobbies, stairways, passages and parking spaces and other common areas;\r\n" + 
                "	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	<div style=\"width:5%;float:left; \">   <span style=\"margin-left:10px;\"> &nbsp;</span>  </div>\r\n" + 
                "    <div style=\"width:4%;float:left; \">   <span>c.</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                " making any structural alterations inside the apartment or making any fresh openings;\r\n" + 
                "	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	<div style=\"width:5%;float:left; \">   <span style=\"margin-left:10px;\"> &nbsp;</span>  </div>\r\n" + 
                "    <div style=\"width:4%;float:left; \">   <span>d.</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                " defaulting in payment of any taxes or levies to be shared commonly by all the owners or common expenses for maintenance of the Project.\r\n" + 
                "	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	<div style=\"width:5%;float:left; \">   <span style=\"margin-left:10px;\"> &nbsp;</span>  </div>\r\n" + 
                "    <div style=\"width:4%;float:left; \">   <span>e.</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                " creating nuisance or annoyance or damages to other occupants and owners by allowing pounding, running machinery and causing similar disturbances and noises.\r\n" + 
                "	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	<div style=\"width:5%;float:left; \">   <span style=\"margin-left:10px;\"> &nbsp;</span>  </div>\r\n" + 
                "    <div style=\"width:4%;float:left; \">   <span>f.</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                " installing machinery, store/keep explosives, inflammable/ prohibited articles which are hazardous, dangerous or combustible in nature.\r\n" + 
                "	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	<div style=\"width:5%;float:left; \">   <span style=\"margin-left:10px;\"> &nbsp;</span>  </div>\r\n" + 
                "    <div style=\"width:4%;float:left; \">   <span>g.</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                " using the common corridors, stair cases, lift lobbies and other common areas either for storage or for use by servants at any time.\r\n" + 
                "	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	<div style=\"width:5%;float:left; \">   <span style=\"margin-left:10px;\"> &nbsp;</span>  </div>\r\n" + 
                "    <div style=\"width:4%;float:left; \">   <span>h.</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                " bringing inside or park in the Schedule 'F' Property any lorry or any heavy vehicles without the prior approval of the Promoter/Maintenance Company/Association of Owners.\r\n" + 
                "	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	<div style=\"width:5%;float:left; \">   <span style=\"margin-left:10px;\"> &nbsp;</span>  </div>\r\n" + 
                "    <div style=\"width:4%;float:left; \">   <span>i.</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                " using the apartment or portion thereof for purpose other than for residential purposes and not to use for any illegal or immoral purposes.\r\n" + 
                "	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	<div style=\"width:5%;float:left; \">   <span style=\"margin-left:10px;\"> &nbsp;</span>  </div>\r\n" + 
                "    <div style=\"width:4%;float:left; \">   <span>j.</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                " draping clothes in the balconies and other places of building;\r\n" + 
                "	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	<div style=\"width:5%;float:left; \">   <span style=\"margin-left:10px;\"> &nbsp;</span>  </div>\r\n" + 
                "    <div style=\"width:4%;float:left; \">   <span>k.</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                " entering or trespassing into the parking areas, garden areas and terrace not earmarked for general common use.\r\n" + 
                "	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	<div style=\"width:5%;float:left; \">   <span style=\"margin-left:10px;\"> &nbsp;</span>  </div>\r\n" + 
                "    <div style=\"width:4%;float:left; \">   <span>l.</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                " throwing any rubbish or garbage other than in the dustbin/s provided in the property.\r\n" + 
                "	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	<div style=\"width:5%;float:left; \">   <span style=\"margin-left:10px;\"> &nbsp;</span>  </div>\r\n" + 
                "    <div style=\"width:4%;float:left; \">   <span>m.</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                " undertaking any interior decoration work or additions, alterations inside the apartment involving structural changes without prior consent in writing of the Promoter/Maintenance Company/ Owner Association.\r\n" + 
                "	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	<div style=\"width:5%;float:left; \">   <span style=\"margin-left:10px;\"> &nbsp;</span>  </div>\r\n" + 
                "    <div style=\"width:4%;float:left; \">   <span>n.</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "	 creating any nuisance or disturbance or misbehave in the matter of enjoying the common facilities provided to all the owners in the Project\".\r\n" + 
                "	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	<div style=\"width:5%;float:left; \">   <span style=\"margin-left:10px;\"> &nbsp;</span>  </div>\r\n" + 
                "    <div style=\"width:4%;float:left; \">   <span>o.</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "		 refusing to pay the common maintenance expenses or user charges or such sums as are demanded for use and enjoyment of Common Amenities in the Project.\r\n" + 
                "	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	<div style=\"width:5%;float:left; \">   <span style=\"margin-left:10px;\"> &nbsp;</span>  </div>\r\n" + 
                "    <div style=\"width:4%;float:left; \">   <span>p.</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "		 trespass into other residential buildings in the Project or misuse the facilities provided for common use.&nbsp;&nbsp;\r\n" + 
                "	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:5%;float:left; \">   <span>xvii)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "		The use of the club house, swimming pool and other facilities by the Allottee/s during tenure of membership shall be without causing any disturbance or annoyance to the other users and without committing any act of waste or nuisance which will affect the peace of the place and shall not default/refuse/avoid paying the subscription and other charges for the use of the facilities therein.\r\n" + 
                "	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:5%;float:left; \">   <span>xviii)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "		The Allottee/s shall park vehicles only in the parking space/area specifically acquired by the Allottee/s and earmarked for the exclusive use of the Allottee/s.\r\n" + 
                "	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:5%;float:left; \">   <span>xix)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "		The Allottee/s shall not throw garbage/used articles/rubbish in the common areas, parks and open spaces, roads and open spaces left&nbsp;&nbsp;open&nbsp;&nbsp;in&nbsp;&nbsp;the&nbsp;&nbsp;Schedule `F' Property.&nbsp;&nbsp;The Allottee/s shall strictly follow rules and regulations for garbage disposal as may be prescribed by the Promoter or Agency maintaining the Common Areas &amp; Amenities in the Project or by the Owners Association.\r\n" + 
                "	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span>xx)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "		The Allottee/s shall keep all the pets confined within the Schedule 'A' Apartment and shall ensure that the pets do not create any nuisance/disturbance to the other owners/occupants in the building.\r\n" + 
                "	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span>xxi)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "		The Allottee/s shall use all sewers, drains and water lines now in or&nbsp;&nbsp;upon or hereafter to be erected and installed in Schedule `F' Property and in the Apartment Building in common with the other Apartment Owners&nbsp;&nbsp;and&nbsp;&nbsp;to permit&nbsp;&nbsp;free&nbsp;&nbsp;passage of water, sanitary, electricity&nbsp;&nbsp;and electrical lines, through and along the same or any of them and to share with the other Apartment Owners the cost of maintaining and repairing all common amenities such as common accesses, staircases, lifts, generator, etc., and to use the&nbsp;&nbsp;same as aforesaid and/or in&nbsp;&nbsp;accordance&nbsp;&nbsp;with&nbsp;&nbsp;the Rules, Regulations, Bye-Laws and terms of&nbsp;&nbsp;the Association to&nbsp;&nbsp;&nbsp;be formed by or among the Apartment Owner in the Building.\r\n" + 
                "	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:5%;float:left; \">   <span>xxii)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "		The Allottee/s shall permit the Promoter and/or Maintenance Company and/or Owners' Association and/or their agents with or without workmen at all reasonable times to enter into and upon the Apartment/Parking Space or any part thereof for the purpose of repairing, maintaining, re-building, cleaning and keeping in order and condition&nbsp;&nbsp;all services, drains, structures or other conveniences belonging to or servicing or used for the said apartment and also for the&nbsp;&nbsp;purpose of laying, maintaining, repairing&nbsp;&nbsp;and testing drainage, water pipes and electric wires and for similar purposes and also for the purpose of cutting off the supply of water and electricity etc., to the Apartment/Parking space&nbsp;&nbsp;or&nbsp;&nbsp;other&nbsp;&nbsp;common areas of the&nbsp;&nbsp;building or to the occupiers of such Apartment/Parking space as the case may be who have defaulted in paying the share of the water, electricity and other charges.&nbsp;\r\n" + 
                "	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:5%;float:left; \">   <span>xxiii)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "		The Allottee/s in the event of leasing the Schedule 'A' Apartment shall keep informed the Maintenance Company/Owners Association about the tenancy of the Schedule 'A' Apartment and giving all the details of the tenants.&nbsp;&nbsp;Upon leasing, only the tenant/lessee shall be entitled to make use of the club facilities and the Allottee/s shall not be entitled to make use of the common facilities. Notwithstanding the leasing, the primary responsibility to adhere to all the rights and obligations of the Allottee/s contained herein shall be that of the Allottee/s and it shall be the responsibility of the Allottee/s&nbsp;&nbsp;to ensure that the tenant/lessee follows all the rules and regulations that may be prescribed for the occupants of the Project.\r\n" + 
                "	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	    <div style=\"width:4%;float:left; \">   <span>xiv)</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "		The Allottee/s shall be liable to pay to Promoter/Maintenance Company/Owners' Association, as the case may be, the following expenses proportionately:\r\n" + 
                "	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "\r\n" + 
                " <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	<div style=\"width:5%;float:left; \">   <span style=\"margin-left:10px;\"> &nbsp;</span>  </div>\r\n" + 
                "    <div style=\"width:3%;float:left; \">   <span>a.</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "		 expenses incurred for maintenance of all the landscaping, gardens, and open spaces, white-washing and painting the exteriors and the common areas, the repair and maintenance of lifts, pumps, generators and other machinery, water, sanitary and electrical lines, electricity and water charges of the common areas, including the cost of AMC's for these equipment, replacement of fittings and provision of consumables of all common areas and places;\r\n" + 
                "	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	<div style=\"width:5%;float:left; \">   <span style=\"margin-left:10px;\"> &nbsp;</span>  </div>\r\n" + 
                "    <div style=\"width:3%;float:left; \">   <span>b.</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "		 All taxes payable, service charges and all other incidental expenses in general.\r\n" + 
                "	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	<div style=\"width:5%;float:left; \">   <span style=\"margin-left:10px;\"> &nbsp;</span>  </div>\r\n" + 
                "    <div style=\"width:3%;float:left; \">   <span>c.</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "			 salaries, wages paid/payable to property manager, security, lift operators, plumbers, electricians, gardeners and other technicians etc.;\r\n" + 
                "	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "  <div class=\"startingDivEightPixelPadd\" style=\"width:100%;float:left;padding-top:5px;\">\r\n" + 
                "  	<div style=\"width:5%;float:left; \">   <span style=\"margin-left:10px;\"> &nbsp;</span>  </div>\r\n" + 
                "    <div style=\"width:3%;float:left; \">   <span>d.</span>  </div>\r\n" + 
                "    <div style=\"margin-left:1%; float:left;\"> <span>\r\n" + 
                "			 all other expenses incurred for proper upkeep and maintenance of common areas and facilities within the development including expenses/costs incurred for replacement of worn out equipment, machinery such as generators, elevators, pumps, motors etc.\r\n" + 
                "	</span>\r\n" + 
                "    </div>\r\n" + 
                "   </div>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;text-align: center;\"><strong><u>SCHEDULE 'A' APARTMENT</u></strong></p>\r\n" + 
                "<p style=\"font-weight: 80;\">Residential Apartment bearing&nbsp;No.<strong>A803,&nbsp;&nbsp;</strong>in&nbsp;<strong>Fourth Floor&nbsp;</strong>\r\n" + 
                "&nbsp;,&nbsp;<strong>Type  Bedroom,</strong>&nbsp;in the&nbsp;<strong>\"Wing A\"</strong>&nbsp;<strong>Phase \"I</strong>\"\r\n" + 
                " having carpet area of&nbsp;&nbsp;<strong> 115.01 Sq.Mtrs or </strong><strong>1238.0 Sq.ft,</strong>,\r\n" + 
                "  and super built up area of&nbsp;<strong> 161.18 Sq.Mtrs or 1735 Sq.ft,</strong>&nbsp;&nbsp;\r\n" + 
                "  together with&nbsp;<strong> 57.41 Sq.Mtrs or  618.0 Sq.ft</strong>\r\n" + 
                "  &nbsp;&nbsp;of undivided share in the land comprised in Schedule 'F' Property with one covered&nbsp;&nbsp;\r\n" + 
                "  parking space admeasuring&nbsp;<strong>13.75 Sq. Mtrs or 148 Sq.Ft.,</strong>&nbsp;including proportionate share in common areas such as passages,\r\n" + 
                "   lifts, lobbies, staircase, etc., of&nbsp;&nbsp;multistoried residential building known as&nbsp;<strong>\"</strong><strong>SUMADHURA SUSHANTHAM\"</strong>\r\n" + 
                "   &nbsp;constructed over Schedule \"F\" Property.</p>\r\n" + 
                "<p style=\"font-weight: 80;text-align: center;\"><strong>SCHEDULE B UNIT PLAN</strong></p>\r\n" + 
                "<p style=\"font-weight: 80;text-align: center;\"><b>Attached to this Agreement</b></p>\r\n" + 
                "<p style=\"font-weight: 80;text-align: center;\"><strong>SCHEDULE C</strong></p>\r\n" + 
                "<p style=\"font-weight: 80;text-align: center;\"><strong>[PAYMENT PLAN FOR SALE CONSIDERATION OF SCHEDULE 'A'&nbsp;APARTMENT]</strong></p>\r\n" + 
                "\r\n" + 
                "<table style=\"width:100%; border: 1px solid black;border-collapse:collapse;\">\r\n" + 
                "   <tbody>\r\n" + 
                "      <tr style=\"border: 1px solid black;padding:2px;\">\r\n" + 
                "         <td  style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "            <p><strong>&nbsp;</strong></p>\r\n" + 
                "         </td>\r\n" + 
                "         <td colspan=\"3\"  style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "            <p><strong>PAYMENT PLAN FOR RESPECTIVE WINGS/FLAT</strong></p>\r\n" + 
                "         </td>\r\n" + 
                "      </tr>\r\n" + 
                "      <tr style=\"border: 1px solid black;padding:2px;\">\r\n" + 
                "         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "            <p>Serial Number</p>\r\n" + 
                "         </td>\r\n" + 
                "         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "            <p><strong>INSTALMENT DUE STAGE</strong></p>\r\n" + 
                "         </td>\r\n" + 
                "         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "            <p><strong>Percentage Due</strong></p>\r\n" + 
                "         </td>\r\n" + 
                "         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "            <p><strong>Due Amount</strong></p>\r\n" + 
                "         </td>\r\n" + 
                "      </tr>\r\n" + 
                "      \r\n" + 
                "      	      <tr style=\"border: 1px solid black;padding:2px;\">\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p>1</p>\r\n" + 
                "	         </td>\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p>On Booking</p>\r\n" + 
                "	         </td>\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p>10%</p>\r\n" + 
                "	         </td>\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p> 10,50,000.00</p>\r\n" + 
                "	         </td>\r\n" + 
                "	      </tr>\r\n" + 
                "      	      <tr style=\"border: 1px solid black;padding:2px;\">\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p>2</p>\r\n" + 
                "	         </td>\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p>Within 15 days from the date of booking</p>\r\n" + 
                "	         </td>\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p>10%</p>\r\n" + 
                "	         </td>\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p> 10,50,000.00</p>\r\n" + 
                "	         </td>\r\n" + 
                "	      </tr>\r\n" + 
                "      	      <tr style=\"border: 1px solid black;padding:2px;\">\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p>3</p>\r\n" + 
                "	         </td>\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p>On completion of Foundation or on Apr'2020 (whichever is later)</p>\r\n" + 
                "	         </td>\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p>10%</p>\r\n" + 
                "	         </td>\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p> 10,50,000.00</p>\r\n" + 
                "	         </td>\r\n" + 
                "	      </tr>\r\n" + 
                "      	      <tr style=\"border: 1px solid black;padding:2px;\">\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p>4</p>\r\n" + 
                "	         </td>\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p>On Completion of 2nd Floor Roof Slab or Jun'2020 (whichever is later)</p>\r\n" + 
                "	         </td>\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p>10%</p>\r\n" + 
                "	         </td>\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p> 10,50,000.00</p>\r\n" + 
                "	         </td>\r\n" + 
                "	      </tr>\r\n" + 
                "      	      <tr style=\"border: 1px solid black;padding:2px;\">\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p>5</p>\r\n" + 
                "	         </td>\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p>On Completion of 4th Floor Roof Slab or Aug'2020 (whichever is later)</p>\r\n" + 
                "	         </td>\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p>10%</p>\r\n" + 
                "	         </td>\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p> 10,50,000.00</p>\r\n" + 
                "	         </td>\r\n" + 
                "	      </tr>\r\n" + 
                "      	      <tr style=\"border: 1px solid black;padding:2px;\">\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p>6</p>\r\n" + 
                "	         </td>\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p>On Completion of 6th Floor Roof Slab or Oct'2020 (whichever is later)</p>\r\n" + 
                "	         </td>\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p>10%</p>\r\n" + 
                "	         </td>\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p> 10,50,000.00</p>\r\n" + 
                "	         </td>\r\n" + 
                "	      </tr>\r\n" + 
                "      	      <tr style=\"border: 1px solid black;padding:2px;\">\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p>7</p>\r\n" + 
                "	         </td>\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p>On Completion of 9th Floor Roof Slab or Dec'2020 (whichever is later)</p>\r\n" + 
                "	         </td>\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p>10%</p>\r\n" + 
                "	         </td>\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p> 10,50,000.00</p>\r\n" + 
                "	         </td>\r\n" + 
                "	      </tr>\r\n" + 
                "      	      <tr style=\"border: 1px solid black;padding:2px;\">\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p>8</p>\r\n" + 
                "	         </td>\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p>On Completion of 11th Floor Roof Slab or Feb'2021 (whichever is later)</p>\r\n" + 
                "	         </td>\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p>10%</p>\r\n" + 
                "	         </td>\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p> 10,50,000.00</p>\r\n" + 
                "	         </td>\r\n" + 
                "	      </tr>\r\n" + 
                "      	      <tr style=\"border: 1px solid black;padding:2px;\">\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p>9</p>\r\n" + 
                "	         </td>\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p>On Completion of 14th Floor Roof Slab or Apr'2021 (whichever is later)</p>\r\n" + 
                "	         </td>\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p>10%</p>\r\n" + 
                "	         </td>\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p> 10,50,000.00</p>\r\n" + 
                "	         </td>\r\n" + 
                "	      </tr>\r\n" + 
                "      	      <tr style=\"border: 1px solid black;padding:2px;\">\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p>10</p>\r\n" + 
                "	         </td>\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p>On Completion of Flooring Work of resp. flat or Sep'2021 (whichever is later)</p>\r\n" + 
                "	         </td>\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p>5%</p>\r\n" + 
                "	         </td>\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p> 5,25,000.00</p>\r\n" + 
                "	         </td>\r\n" + 
                "	      </tr>\r\n" + 
                "      	      <tr style=\"border: 1px solid black;padding:2px;\">\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p>11</p>\r\n" + 
                "	         </td>\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p>Against Registration</p>\r\n" + 
                "	         </td>\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p>5%</p>\r\n" + 
                "	         </td>\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p> 5,25,000.00</p>\r\n" + 
                "	         </td>\r\n" + 
                "	      </tr>\r\n" + 
                "      	      <tr style=\"border: 1px solid black;padding:2px;\">\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p>12</p>\r\n" + 
                "	         </td>\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p>Maintenance Charges payable on final payment request</p>\r\n" + 
                "	         </td>\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p>-</p>\r\n" + 
                "	         </td>\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p> 85,986.60</p>\r\n" + 
                "	         </td>\r\n" + 
                "	      </tr>\r\n" + 
                "      	      <tr style=\"border: 1px solid black;padding:2px;\">\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p>13</p>\r\n" + 
                "	         </td>\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p>Total Amount</p>\r\n" + 
                "	         </td>\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p></p>\r\n" + 
                "	         </td>\r\n" + 
                "	         <td style=\"border: 1px solid black;padding:2px;\" align=\"left\">\r\n" + 
                "	            <p> 1,05,85,986.60</p>\r\n" + 
                "	         </td>\r\n" + 
                "	      </tr>\r\n" + 
                "      \r\n" + 
                "   </tbody>\r\n" + 
                "</table>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>STATUTORY AND OTHER CHARGES:</strong></p>\r\n" + 
                "<p style=\"font-weight: 80;\">Apart from the above total sale consideration, the ALLOTTEE/S shall also be liable to pay the following amounts&nbsp;to be paid as per demand along with the respective installments other than sale consideration of the apartment</p>\r\n" + 
                "\r\n" + 
                " <table style=\"width:100%; border: 1px solid black;border-collapse:collapse;\">\r\n" + 
                "   <tbody>\r\n" + 
                "      <tr style=\"border: 1px solid black;padding:2px;\">\r\n" + 
                "         <td width=\"6%\" style=\"border: 1px solid black;padding:2px;\">\r\n" + 
                "            <p>1)</p>\r\n" + 
                "         </td>\r\n" + 
                "         <td width=\"39%\" style=\"border: 1px solid black;padding:2px;\">\r\n" + 
                "            <p>Stamp duty for Agreements</p>\r\n" + 
                "         </td>\r\n" + 
                "         <td width=\"54%\" style=\"border: 1px solid black;padding:2px;\">\r\n" + 
                "            <p>At Actuals to be borne by the Allottee/s</p>\r\n" + 
                "         </td>\r\n" + 
                "      </tr>\r\n" + 
                "      \r\n" + 
                "      <tr style=\"border: 1px solid black;padding:2px;\">\r\n" + 
                "         <td width=\"6%\" style=\"border: 1px solid black;padding:2px;\">\r\n" + 
                "            <p>2)</p>\r\n" + 
                "         </td>\r\n" + 
                "         <td width=\"39%\" style=\"border: 1px solid black;padding:2px;\">\r\n" + 
                "            <p>Registration charges and Stamp Duty charges as applicable on the day of registration</p>\r\n" + 
                "         </td>\r\n" + 
                "         <td width=\"54%\" style=\"border: 1px solid black;padding:2px;\">\r\n" + 
                "            <p>At Actuals (to be paid before Registration)</p>\r\n" + 
                "         </td>\r\n" + 
                "      </tr>\r\n" + 
                "      \r\n" + 
                "      <tr style=\"border: 1px solid black;padding:2px;\">\r\n" + 
                "         <td width=\"6%\" style=\"border: 1px solid black;padding:2px;\">\r\n" + 
                "            <p>3)</p>\r\n" + 
                "         </td>\r\n" + 
                "         <td width=\"39%\" style=\"border: 1px solid black;padding:2px;\">\r\n" + 
                "            <p>Expenses towards BWSSB Water Supply &amp; Sanitary connection</p>\r\n" + 
                "         </td>\r\n" + 
                "         <td width=\"54%\" style=\"border: 1px solid black;padding:2px;\">\r\n" + 
                "            <p>As and when the BWSSB demands the necessary charges, deposits towards&nbsp;&nbsp;water supply line cost, sanitary point charges, UGD line cost, GBWASP charges/Beneficiary capital contribution charges, Prorata charges, cost of bulk flow meter, water meter cost and any other deposits/charges for providing water supply&amp;sanitary connection to the project has to be paid by the Allottee/s in proportion of the Super built-up area of their Apartment. The GBWASP charges paid by the Developer to BWSSB during plan approval has to be refunded by the Purchaser/s to the Developer.</p>\r\n" + 
                "         </td>\r\n" + 
                "      </tr>\r\n" + 
                "      \r\n" + 
                "       <tr style=\"border: 1px solid black;padding:2px;\">\r\n" + 
                "         <td width=\"6%\" style=\"border: 1px solid black;padding:2px;\">\r\n" + 
                "            <p>4)</p>\r\n" + 
                "         </td>\r\n" + 
                "         <td width=\"39%\" style=\"border: 1px solid black;padding:2px;\">\r\n" + 
                "            <p>Corpus Fund</p>\r\n" + 
                "         </td>\r\n" + 
                "         <td width=\"54%\" style=\"border: 1px solid black;padding:2px;\">\r\n" + 
                "            <p>Rs. 50.00/-( Rupees Only)&nbsp;per Square Feet on Super Built-Up Area of the Apartment to be paid at the time of registration of Schedule 'A' Property.</p>\r\n" + 
                "                      </td>\r\n" + 
                "      </tr>\r\n" + 
                "        <tr style=\"border: 1px solid black;padding:2px;\">\r\n" + 
                "         <td width=\"6%\" style=\"border: 1px solid black;padding:2px;\">\r\n" + 
                "            <p>5)</p>\r\n" + 
                "         </td>\r\n" + 
                "         <td width=\"39%\" style=\"border: 1px solid black;padding:2px;\">\r\n" + 
                "            <p>Flat legal and documentation charges</p>\r\n" + 
                "         </td>\r\n" + 
                "         <td width=\"54%\" style=\"border: 1px solid black;padding:2px;\">\r\n" + 
                "            <p>Rs. 20,000.00/-( Rupees Only)&nbsp;Plus applicable Taxes</p>\r\n" + 
                "                  </td>\r\n" + 
                "         \r\n" + 
                "      </tr>\r\n" + 
                "        <tr style=\"border: 1px solid black;padding:2px;\">\r\n" + 
                "         <td width=\"6%\" style=\"border: 1px solid black;padding:2px;\">\r\n" + 
                "            <p>6)</p>\r\n" + 
                "         </td>\r\n" + 
                "         <td width=\"39%\" style=\"border: 1px solid black;padding:2px;\">\r\n" + 
                "            <p>Individual Flat Khata bifurcation and other charges</p>\r\n" + 
                "         </td>\r\n" + 
                "         <td width=\"54%\" style=\"border: 1px solid black;padding:2px;\">\r\n" + 
                "            <p>Rs. 25,000.00/-( Rupees Only)&nbsp;&nbsp;&nbsp;Plus applicable Taxes</p>\r\n" + 
                "                   </td>\r\n" + 
                "         \r\n" + 
                "      </tr>\r\n" + 
                "   </tbody>\r\n" + 
                "</table>\r\n" + 
                "\r\n" + 
                "\r\n" + 
                "<p class=\"sessiontitle\" style=\"font-weight: 80;\"><strong>SCHEDULE 'D'&nbsp;&nbsp;(SPECIFICATIONS)</strong></p>\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>Structure:</strong></p>\r\n" + 
                "<ul>\r\n" + 
                "<li style=\"font-weight: 80;\">R.C.C. structure to withstand wind &amp; seismic loads as per IS code with RCC walls (Shear wall technology).</li>\r\n" + 
                "</ul>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>Doors:</strong></p>\r\n" + 
                "<ul>\r\n" + 
                "<li style=\"font-weight: 80;\">Main doors:&nbsp;Engineered hard wood frame with designer shutters of 40 mm thickness with melamine polish finished on both sides.</li>\r\n" + 
                "<li style=\"font-weight: 80;\">Internal doors:&nbsp;Engineered hard wood frame with designer shutters of 40 mm thickness with enamel paint finished on both sides.</li>\r\n" + 
                "<li style=\"font-weight: 80;\">Toilet doors:&nbsp;Engineered hard wood frame with designer shutters of 40 mm thickness with enamel paint finished on both sides.</li>\r\n" + 
                "<li style=\"font-weight: 80;\">French doors:&nbsp;UPVC door systems with sliding shutters with provision for mosquito mesh.</li>\r\n" + 
                "<li style=\"font-weight: 80;\">Windows:&nbsp;UPVC window systems with safety grill (M.S) and provision for mosquito mesh. All Hardware of reputed make.</li>\r\n" + 
                "</ul>\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>Paintings:</strong></p>\r\n" + 
                "<ul>\r\n" + 
                "<li style=\"font-weight: 80;\">External:&nbsp;Textured/smooth finish &amp; two coats of exterior emulsion paint.</li>\r\n" + 
                "<li style=\"font-weight: 80;\">Internal: Smooth putty finish &amp; two coats of premium emulsion paint for walls and Acrylic emulsion paint for ceiling over one coat of primer.</li>\r\n" + 
                "</ul>\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>Flooring:</strong></p>\r\n" + 
                "<ul>\r\n" + 
                "<li style=\"font-weight: 80;\">Ground floor lobby &amp; Club house lounge area: Granite flooring. Typical floor corridor&ndash; Vitrified tile flooring.</li>\r\n" + 
                "<li style=\"font-weight: 80;\">Staircases &amp; corridor:&nbsp;Vitrified tile flooring.</li>\r\n" + 
                "<li style=\"font-weight: 80;\">Living, Dining, All Bedrooms, Kitchen:&nbsp;600 X 600 mm size Double charged vitrified tiles.</li>\r\n" + 
                "<li style=\"font-weight: 80;\">Toilets: Satin finish ceramic tile flooring.</li>\r\n" + 
                "<li style=\"font-weight: 80;\">All Balconies/Utilities:&nbsp;Rustic finish ceramic tile flooring.</li>\r\n" + 
                "<li style=\"font-weight: 80;\">Dadoing in Kitchen:&nbsp;Ceramic tiles dado of to 2' height above kitchen platform level.</li>\r\n" + 
                "<li style=\"font-weight: 80;\">Toilets: ceramic tile dado up to False-Ceiling level.</li>\r\n" + 
                "<li style=\"font-weight: 80;\">Basement: cement concrete flooring with power troweled smooth finish.</li>\r\n" + 
                "</ul>\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>Hand Railing:</strong></p>\r\n" + 
                "<ul>\r\n" + 
                "<li style=\"font-weight: 80;\">Balcony: M.S railing with enamel paint finish.</li>\r\n" + 
                "<li style=\"font-weight: 80;\">Staircase (common area): M.S railing.</li>\r\n" + 
                "</ul>\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>Kitchen/Utility:</strong></p>\r\n" + 
                "<ul>\r\n" + 
                "<li style=\"font-weight: 80;\">Provision for softened water inlet in kitchen.</li>\r\n" + 
                "<li style=\"font-weight: 80;\">Sleeve provision for chimney.</li>\r\n" + 
                "<li style=\"font-weight: 80;\">Water inlet/outlet, power provision in utility area for washing machine.</li>\r\n" + 
                "</ul>\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>Toilets:</strong></p>\r\n" + 
                "<ul>\r\n" + 
                "<li style=\"font-weight: 80;\">Granite counter for wash basin with basin mixer.</li>\r\n" + 
                "<li style=\"font-weight: 80;\">Wall mounted EWC with concealed flush tank.</li>\r\n" + 
                "<li style=\"font-weight: 80;\">Single lever diverter cum shower.</li>\r\n" + 
                "<li style=\"font-weight: 80;\">Sanitary: TOTO or CERA or Equivalent.</li>\r\n" + 
                "<li style=\"font-weight: 80;\">C.P Fittings:&nbsp; Grohe or Equivalent.</li>\r\n" + 
                "</ul>\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>Electrical:</strong></p>\r\n" + 
                "<ul>\r\n" + 
                "<li style=\"font-weight: 80;\">Concealed copper wiring of Havells/V-Guard or Equivalent.</li>\r\n" + 
                "<li style=\"font-weight: 80;\">Modular switches: Norysis or Schneider make.</li>\r\n" + 
                "<li style=\"font-weight: 80;\">Power outlets for air conditioners in all Bedrooms.</li>\r\n" + 
                "<li style=\"font-weight: 80;\">USB port for mobile phone charging in master bedroom.</li>\r\n" + 
                "<li style=\"font-weight: 80;\">Power outlets in kitchen for cooking range, Hob, chimney, refrigerator, microwave oven, mixer, aquagaurd and for washing machine in utility area.</li>\r\n" + 
                "<li style=\"font-weight: 80;\">Power outlets for geysers and Exhaust Fans in all bathrooms.</li>\r\n" + 
                "<li style=\"font-weight: 80;\">Power supply for 3.5Bhk/3Bhk - 4KW and 2.5Bhk/2Bhk - 3KW.</li>\r\n" + 
                "<li style=\"font-weight: 80;\">DG backup for 3.5Bhk/3Bhk -1.5KW and for 2.5Bhk/2Bhk -1KW.</li>\r\n" + 
                "<li style=\"font-weight: 80;\">100 % DG backup power for Lifts, Pumps &amp; lighting in common areas.</li>\r\n" + 
                "</ul>\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>Plumbing:</strong></p>\r\n" + 
                "<ul>\r\n" + 
                "<li style=\"font-weight: 80;\">Drainage/sewage: PVC pipes &amp; fittings</li>\r\n" + 
                "<li style=\"font-weight: 80;\">Water supply (Internal &amp; External): Cpvc or Upvc pipes &amp; fittings.</li>\r\n" + 
                "</ul>\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>Telecom / Internet/ Cable TV:</strong></p>\r\n" + 
                "<ul>\r\n" + 
                "<li style=\"font-weight: 80;\">Provision for internet, DTH, telephone &amp; intercom.</li>\r\n" + 
                "</ul>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 140; text-align: center;\"><strong>SCHEDULE 'E'</strong></p>\r\n" + 
                "<p style=\"font-weight: 140; text-align: center;\"><strong>COMMON AMENITIES AND FACILITIES OF THE PROJECT</strong></p>\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>Security:</strong></p>\r\n" + 
                "<ul>\r\n" + 
                "<li style=\"font-weight: 80;\">Round-the-clock security system.</li>\r\n" + 
                "<li style=\"font-weight: 80;\">Intercom facility to all apartments connecting to security room.</li>\r\n" + 
                "<li style=\"font-weight: 80;\">Panic button and intercom is provided in the lifts.</li>\r\n" + 
                "<li style=\"font-weight: 80;\">Solar power fencing around the compound.</li>\r\n" + 
                "<li style=\"font-weight: 80;\">Surveillance cameras at the main security and entrance of each block.</li>\r\n" + 
                "</ul>\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>Lifts:</strong></p>\r\n" + 
                "<ul>\r\n" + 
                "<li style=\"font-weight: 80;\">Three Eight passenger &amp; one thirteen passenger Lift for Each Wing with auto rescue device with V3F for energy efficiency. (Schindler or Equivalent make)</li>\r\n" + 
                "</ul>\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>WTP &amp; STP:</strong></p>\r\n" + 
                "<ul>\r\n" + 
                "<li style=\"font-weight: 80;\">Softened water made available through an exclusive water treatment plant (in case of bore water).</li>\r\n" + 
                "<li style=\"font-weight: 80;\">Sewage treatment plant of adequate capacity as per norms will be provided inside the project, treated sewage water will be used for the landscaping / flushing purpose.</li>\r\n" + 
                "</ul>\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>Club house Amenities:</strong></p>\r\n" + 
                "<ul>\r\n" + 
                "<li style=\"font-weight: 80;\">Cr&egrave;che, swimming pool (adult &amp; kids) with deck, Multipurpose hall, Pantry, store &amp; wash area, Men's &amp; women's health club, Gym, Yoga/Aerobics, Indoor games, Table games, Badminton court, Guest room &amp; terrace garden.</li>\r\n" + 
                "</ul>\r\n" + 
                "\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>Open Area Amenities:</strong></p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">Unique tree court, Sculpture court, Music garden, Pet park, Trampoline park, Floral garden, Children's theme park (10+ years), Multipurpose court &ndash; Basketball/Tennis (12+ years), Cycle track, Cycle stand, Aromatic garden, pavilion with seating, OAT with feature wall, interactive fountain, swing park with tree court, orchard groove, youth corner, cricket practice pitch, children's play area (5-10 years), sand pit with climbing wall (5-8 years), Ludo, skating rink, chess, snake &amp; ladder (Tot- lot (2-5 years)), Lily pond, Reflexology path, yoga lawn &amp; lawn with gym equipment.</p>\r\n" + 
                "\r\n" + 
                "<h4 style=\"font-weight: 80;text-align: center;font-weight:bold;\"><u>SCHEDULE \"F\" PROPERTY</u></h4>\r\n" + 
                "<h4 style=\"font-weight: 80;text-align: center;font-weight:bold;\"><u>ITEM NO. I OF THE SCHEDULE 'F' &amp; PROPERTY</u></h4>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">All that piece and parcel of Converted Land bearing Sy.No. 3/10, measuring about 28<strong>&nbsp;</strong>Guntas,<strong>&nbsp;</strong>converted Land bearing Sy.No. 3/9, measuring about 36 Guntas out of 38<strong>&nbsp;</strong>Guntas, { converted vide Conversion Order of the Deputy Commissioner Bangalore, dated 05/11/2015, bearing No.ALN.(NY)SR:236/2014-15},&nbsp;<strong>&nbsp;</strong>both the properties are<strong>&nbsp;</strong>referred to above are situated adjacent to each other bears a common BBMP Khata No:<strong>&nbsp;</strong>1582/Sy.No.3/9,3/10, situated at<strong>&nbsp;</strong>Thindlu Village, Yelahanka Hobli, Bangalore North Taluk, and totally&nbsp;&nbsp;measuring 1 Acre 24 Guntas,<strong>&nbsp;&nbsp;</strong>and bounded on the</p>\r\n" + 
                "<p style=\"font-weight: 80;\">East by&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;Land in Sy.No.3/11, 3/12;</p>\r\n" + 
                "<p style=\"font-weight: 80;\">West by&nbsp;&nbsp;:&nbsp;&nbsp;Land in Sy.No.3/1, Sy.No.3/2 and Sy.No.3/13,;</p>\r\n" + 
                "<p style=\"font-weight: 80;\">North by :&nbsp;&nbsp;Road;</p>\r\n" + 
                "<p style=\"font-weight: 80;\">South by : Land in Sy.No.3/8 ;</p>\r\n" + 
                "<h4 style=\"font-weight: 80;text-align: center;font-weight:bold;\"><u>ITEM NO. II OF THE SCHEDULE 'F' PROPERTY</u></h4>\r\n" + 
                "<p style=\"font-weight: 80;\">All that piece and parcel of Converted Land bearing Sy.No. 3/7, measuring about 2 Acers,<strong>&nbsp;</strong>converted Land bearing Sy.No. 3/8 A old Sy.No.3/8, measuring about 11 Guntas, converted Land bearing Sy.No. 3/8 B old Sy.No.3/8, measuring about 10 Guntas and converted Land bearing Sy.No. 3/8 C old Sy.No.3/8, measuring about 8 Guntas,&nbsp;&nbsp;{ converted vide Conversion Order of the Deputy Commissioner Bangalore, dated 05/11/2015, bearing No.ALN.(NY)SR:236/2014-15},&nbsp;<strong>&nbsp;</strong>both the properties are<strong>&nbsp;</strong>referred to above are situated adjacent to each other bears a common BBMP Khata No:<strong>&nbsp;</strong>1583/Sy.No.3/7,3/8A,3/8B ,3/8C, situated at<strong>&nbsp;</strong>Thindlu Village, Yelahanka Hobli, Bangalore North Taluk, and totally&nbsp;&nbsp;measuring 2 Acre 29 Guntas,<strong>&nbsp;&nbsp;</strong>and bounded on the</p>\r\n" + 
                "<p style=\"font-weight: 80;\">East by&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;Land in Sy.No.81;</p>\r\n" + 
                "<p style=\"font-weight: 80;\">West by&nbsp;&nbsp;:&nbsp;&nbsp;Remaining portion of Sy.No.3/7, 3/4;</p>\r\n" + 
                "<p style=\"font-weight: 80;\">North by :&nbsp;&nbsp;Land in Sy.No.3/9;</p>\r\n" + 
                "<p style=\"font-weight: 80;\">South by : Road;</p>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;text-align: center;font-weight:bold;\"><u>COMPOSITE SCHEDULE \"F\" PROPERTY</u></p>\r\n" + 
                "<p style=\"font-weight: 80;\">All that piece and parcel of Land bearing&nbsp;<strong>Sy.No.3/7,&nbsp;</strong>measuring 2 Acres Out of 2 acres 29 Guntas,&nbsp;<strong>Sy.No.3/8C, Old.Sy.No.3/8,&nbsp;</strong>measuring 8 Guntas,&nbsp;<strong>Sy.No.3/10,&nbsp;</strong>measuring 28 Guntas,&nbsp;<strong>Sy.No.3/8B, Old.Sy.No.3/8&nbsp;</strong>measuring 10 Guntas,<strong>&nbsp;Sy.No.3/9,&nbsp;</strong>measuring 36 Guntas,&nbsp;<strong>Sy.No.3/8A, Old.Sy.No.3/8&nbsp;</strong>measuring 11 Guntas, situated at Thindlu Village, Yelahanka Hobli, Bangalore North Taluk, presently under the jurisdiction of&nbsp;<strong>BBMP</strong>, in all totally measuring 4 Acre 13 Guntas bounded on the:</p>\r\n" + 
                "<p style=\"font-weight: 80;\">East by&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;Land in Sy.No.3/11, 3/12 and Sy.No.81;</p>\r\n" + 
                "<p style=\"font-weight: 80;\">West by&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Remaining portion of Sy.No.3/7, 3/4, 3/13, 3/2 &amp; 3/1;</p>\r\n" + 
                "<p style=\"font-weight: 80;\">North by :&nbsp;&nbsp;Road;</p>\r\n" + 
                "<p style=\"font-weight: 80;\">South by : Road;</p>\r\n" + 
                "<p class=\"sessiontitle\" style=\"font-weight: 80;\"><strong>SCHEDULE 'G'</strong></p>\r\n" + 
                //padding:1px;
                "<p style=\"text-align: center;font-family: initial;text-decoration:underline; width: 100%;margin-top: 35px;text-transform: capitalize;font-weight: 80;margin-top:2px;\"><strong>DETAILS OF THE COMMON AREA</strong></p>\r\n" + 
                "<ul>\r\n" + 
                "<li style=\"font-weight: 80;\">The staircases, lifts, staircase and lift lobbies, fire escape, and common entrances &amp; exits of the building.</li>\r\n" + 
                "<li style=\"font-weight: 80;\">The common basement, terraces, parks such as unique tree court, sculpture court, floral garden, aromatic garden, pavilion with seating, orchard groove, OAT with feature wall, Music garden, pet park, trampoline park, children's theme park, multipurpose court- tennis/basketball, swing park with tree court, youth corner, cricket practice pitch, children's play area, sand pit with climbing wall, ludo, skating rink, chess, snake &amp; ladder, lawn with gym equipment, open parking areas, cycle track &amp; cycle stand, interactive fountain, lilly pond &amp; reflexology path.</li>\r\n" + 
                "<li style=\"font-weight: 80;\">&nbsp;Electrical rooms (metering panels), D.G yard, transformer yard, communication room, BMS room, fire command room, organic waste converter, Sewage treatment plant, servant toilet &amp; Gas bank,</li>\r\n" + 
                "<li style=\"font-weight: 80;\">Domestic &amp; treated water tanks, pump rooms, rain water sump, ducts.</li>\r\n" + 
                "<li style=\"font-weight: 80;\">Cr&egrave;che, swimming pool (adult &amp; kids) with deck, Change rooms, Multipurpose hall, Pantry, store &amp; wash area, Men's &amp; women's health club, Gym, Yoga/Aerobics, Indoor games, Table games, Badminton court, Guest room &amp; terrace garden..</li>\r\n" + 
                "</ul>\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>&nbsp;</strong></p>\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>&nbsp;</strong></p>\r\n" + 
                "<p style=\"font-weight: 80;\">&nbsp;&nbsp;&nbsp;&nbsp;<strong>SIGNED AND DELIVERED BY THE WITHIN NAMED ALLOTTEE</strong></p>\r\n" + 
                "<p style=\"font-weight: 80;\"><strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong></p>\r\n" + 
                "\r\n" + 
                "\r\n" + 
                "\r\n" + 
                "<table border = \"0\" style=\"width: 100%;border: 0px solid white;border-collapse: collapse;\" id = \"tablePhotoId\">\r\n" + 
                "	<tbody>\r\n" + 
                "		<tr style=\"width: 50%; border: none;\">\r\n" + 
                "			<td style=\"width: 10%; height: 153px;\" >&nbsp;</td>\r\n" + 
                "			<td style=\"width: 60%; height: 153px;\"  colspan=\"2\">\r\n" + 
                "				<p>Signature_________________________</p>\r\n" + 
                "				<p><strong>Name:</strong> <strong>Mr. Murali Nelanti</strong></p>\r\n" + 
                "				<p><strong>Residing at :</strong> 5-31, Ghattuppala, Chandur Mandal, Ghattuppala, Nalgonda, Telangana, 508253.</p>\r\n" + 
                "			</td>\r\n" + 
                "			\r\n" + 
                "			<td style=\"width: 30%; height: 153px;\">\r\n" + 
                "				<table  border=\"0\" style=\"width: 50%;border: 0px solid white;border-collapse: collapse;\">\r\n" + 
                "					<tbody>\r\n" + 
                "						<tr style=\"width: 50%; border: none;\">\r\n" + 
                "							<td style=\"width: 50px; border: 1px solid black; height: 140.6px;\">\r\n" + 
                "								<p  align=\"center\">Please affix photograph and sign across the photograph</p>\r\n" + 
                "							</td>\r\n" + 
                "						</tr>\r\n" + 
                "					</tbody>\r\n" + 
                "				</table>\r\n" + 
                "			</td>\r\n" + 
                "		</tr>\r\n" + 
                "		<tr><td>&nbsp;</td></tr>\r\n" + 
                "	</tbody>\r\n" + 
                "</table>\r\n" + 
                "\r\n" + 
                "<p style=\"font-weight: 80;\">&nbsp;&nbsp;&nbsp;&nbsp;<strong>SIGNED AND DELIVERED BY THE WITHIN NAMED:</strong><br/>\r\n" + 
                "&nbsp;&nbsp;&nbsp;&nbsp;<strong>PROMOTER</strong> (Authorized signatory)</p>\r\n" + 
                "\r\n" + 
                "<table  border = \"0\" style=\"width: 100%;border: 0px solid white;border-collapse: collapse;\" id = \"tablePhotoId\">\r\n" + 
                "<tbody>\r\n" + 
                "		<tr><td>&nbsp;</td><td>&nbsp;</td></tr>\r\n" + 
                "		\r\n" + 
                "		<tr style=\"width: 50%; border: none;\">\r\n" + 
                "			<td  style=\"width: 10%; height: 109px;\" >&nbsp;</td>\r\n" + 
                "			<td style=\"width: 60%; height: 151px;\"   colspan=\"2\">&nbsp;\r\n" + 
                "				<p>Signature <u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </u></p>\r\n" + 
                "				<p><strong>Name:</strong> <strong>Mr.K.RAHUL</strong></p>\r\n" + 
                "				<p><strong>Address: </strong>Sy.No.108/2, Millenia Building, 1<sup>st</sup> Main,</p>\r\n" + 
                "				<p>MSR Layout, Munnekollala Village, Marathahalli,</p>\r\n" + 
                "				<p>Outer Ring Road, Bangalore &ndash; 560 037</p>\r\n" + 
                "			</td>\r\n" + 
                "			\r\n" + 
                "			<td style=\"width: 30%; height: 151px;\">&nbsp;\r\n" + 
                "				<table  border=\"0\" style=\"width: 50%;border: 0px solid white;border-collapse: collapse;\">\r\n" + 
                "					<tbody>\r\n" + 
                "						<tr style=\"width: 50%; border: none;\">\r\n" + 
                "							<td style=\"width: 50px; border: 1px solid black; height: 140.6px;\">\r\n" + 
                "								<p  align=\"center\">Please affix photograph and sign across the photograph</p>\r\n" + 
                "							</td>\r\n" + 
                "						</tr>\r\n" + 
                "					</tbody>\r\n" + 
                "				</table>\r\n" + 
                "			</td>\r\n" + 
                "		</tr>\r\n" + 
                "		<tr><td>&nbsp;  </td></tr>\r\n" + 
                "		<tr style=\"width: 50%; border: none;\">\r\n" + 
                "		<td  style=\"width: 10%; height: 109px;\" >&nbsp;</td>\r\n" + 
                "			<td style=\"width: 60%; height: 151.8px;\"   colspan=\"2\">\r\n" + 
                "				<p>At Bengaluru, on&nbsp; 07-12-2022, in the presence of WITNESSES:</p>\r\n" + 
                "				<p>1. Signature <u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </u></p>\r\n" + 
                "				<p>Name<u> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </u>_&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</p>\r\n" + 
                "				<p>Address <u>&nbsp; &nbsp; &nbsp; ________________________</u></p>\r\n" + 
                "					</td>\r\n" + 
                "			\r\n" + 
                "			<td style=\"width: 20%; height: 151.8px;\">&nbsp;</td>\r\n" + 
                "		</tr>\r\n" + 
                "	</tbody>\r\n" + 
                "</table>\r\n" + 
                "</body>\r\n" + 
                "</html>";       
   */ 		
    	// To docx, with content controls
		WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
		
        XHTMLImporterImpl XHTMLImporter = new XHTMLImporterImpl(wordMLPackage);
        XHTMLImporter.setDivHandler(new DivToSdt());
		
      //  XHTMLImporter.setParagraphFormatting(FormattingOption.CLASS_PLUS_OTHER);
       // XHTMLImporter.setRunFormatting(FormattingOption.CLASS_PLUS_OTHER);
        
		wordMLPackage.getMainDocumentPart().getContent().addAll( 
				XHTMLImporter.convert( xhtml, null) );
		
	    //wordMLPackage.getMainDocumentPart().addStyledParagraphOfText("Title", "Testing Title");
        //wordMLPackage.getMainDocumentPart().addStyledParagraphOfText("Subtitle", "Testing Subtitle");

		System.out.println(XmlUtils.marshaltoString(wordMLPackage
				.getMainDocumentPart().getJaxbElement(), true, true));

		wordMLPackage.save(new java.io.File("E:\\CUSTOMERAPP_CUG\\images\\sumadhura_projects_images\\documents\\Preview\\124\\894\\BankerNOCConvXhtmLFile.docx"));

		// Back to XHTML

		HTMLSettings htmlSettings = Docx4J.createHTMLSettings();
		htmlSettings.setWmlPackage(wordMLPackage);

	
		// output to an OutputStream.
		OutputStream os = new ByteArrayOutputStream();

		// If you want XHTML output
//		Docx4jProperties.setProperty("docx4j.Convert.Out.HTML.OutputMethodXML",
//				true);
		Docx4J.toHTML(htmlSettings, os, Docx4J.FLAG_EXPORT_PREFER_XSL);

		System.out.println(((ByteArrayOutputStream) os).toString());	

    	    	
  }
  /*  
	public static void main(String[] args) throws Exception {
		
		String xhtml= 
    			"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\r\n" + 
    			"<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" lang=\"en\">\r\n" + 
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
    			"</html>";
		
		documentGenerator(xhtml,new java.io.File("E:\\CUSTOMERAPP_CUG\\images\\sumadhura_projects_images\\documents\\Preview\\124\\894\\BankerNOC.docx"));
	}
    
    private static void documentGenerator(String html, File file) throws Docx4JException, JAXBException {
        //Word Processing Package
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
        NumberingDefinitionsPart ndp = new NumberingDefinitionsPart();
        wordMLPackage.getMainDocumentPart().addTargetPart(ndp);
        ndp.unmarshalDefaultNumbering();
        // Convert the XHTML, and add it into the empty docx we made
        XHTMLImporterImpl XHTMLImporter = new XHTMLImporterImpl(wordMLPackage);
        XHTMLImporter.setHyperlinkStyle("Hyperlink");
        String baseurl = file.getPath();
        baseurl = baseurl.substring(0, baseurl.lastIndexOf("\\"));
        wordMLPackage.getMainDocumentPart().getContent().addAll(XHTMLImporter.convert(html, baseurl));
       //Saving the Document
      //  wordMLPackage.getMainDocumentPart().addAltChunk(AltChunkType.Xhtml, html.getBytes());
        wordMLPackage.save(file);
      }  
	*/
}