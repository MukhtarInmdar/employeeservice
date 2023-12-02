package com.sumadhura.employeeservice.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hwpf.HWPFDocumentCore;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.converter.WordToHtmlUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
/*import org.docx4j.model.datastorage.migration.VariablePrepare;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;

import de.phip1611.Docx4JSRUtil;*/
import org.apache.xmlbeans.XmlCursor;
import org.w3c.dom.Document;

/**
 * 
 * @author @NIKET CH@V@N
 * class provides functionality to read the word file and replace the content and convert to pdf file
 *
 */
//https://www.pdftohtml.net/
//https://wordhtml.com/
public class WordDocumentHelper {
	//new WordDocumentHelper().loadWordFileAndReplaceContent(map);
	 private static final String PATH_TO_EXE = "H:\\ACP\\Soft\\OfficeToPDF.exe";//download OfficeToPDF.exe
	 //private static final String PATH_TO_EXE = "C:\\Program Files (x86)\\Microsoft Office\\root\\Office16\\WINWORD.EXE";
	 private static final String FILE_NAME = "AgreementDraft.docx";
	 private static final String PATH_TO_TEMPLATE = "H:\\ACP\\Soft\\"+FILE_NAME;
	 private static final String PATH_TO_OUTPUT = "H:\\ACP\\Soft\\AgreementDraft.pdf";
	
	//required dependency
	/*		<dependency>
			    <groupId>de.phip1611</groupId>
			    <artifactId>docx4j-search-and-replace-util</artifactId>
			    <version>1.0.3</version>
			</dependency> 
	*/		
	public void usingDocx4j(Map<String, Map<String, String>> listOfData) throws Exception {/*
		long start = System.currentTimeMillis();
		System.out.println("***** Control inside the WordDocumentHelper.usingDocx4j() *****");
		String fileLocationPath = PATH_TO_TEMPLATE;
		File file = new File(fileLocationPath);
		File destination = new File("H:\\ACP\\Soft\\sample\\");
		FileUtils.copyFileToDirectory(file, destination);// copying actual file, so actual file should not any effect

		file = new File("H:\\ACP\\Soft\\sample\\" + FILE_NAME);
		fileLocationPath = file.getAbsolutePath();

		WordprocessingMLPackage template = WordprocessingMLPackage
				.load(new FileInputStream(new File(fileLocationPath)));
		;

		// that's it; you can now save `template`, export it as PDF or whatever you want
		// to do
		//Docx4JSRUtil.searchAndReplace(template, listOfData.get("documentData"));
		
		MainDocumentPart documentPart = template.getMainDocumentPart();
		VariablePrepare.prepare(template);
		documentPart.variableReplace(listOfData.get("documentData"));

		OutputStream os = new FileOutputStream(new File(fileLocationPath));
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		template.save(outputStream);
		outputStream.writeTo(os);
		os.close();
		outputStream.close();

		Process process;
		process = new ProcessBuilder(PATH_TO_EXE, fileLocationPath, PATH_TO_OUTPUT).start();
		process.waitFor();

		System.out.println("Result of Office processing: " + process.exitValue());

		file = new File(PATH_TO_OUTPUT);
		byte[] fileContent = Files.readAllBytes(file.toPath());
		 long end = System.currentTimeMillis();
	      //finding the time difference and converting it into seconds
	      float sec = (end - start) / 1000F; System.out.println("seconds "+sec + " seconds");
		System.out.println(fileContent.length);
	*/}
	 
	/*<dependency>
    <groupId>org.apache.poi</groupId>
   <artifactId>poi-scratchpad</artifactId>
   <version>3.12</version>
</dependency>*/
	/**
	 * @param listOfData
	 * @throws Exception
	 */
	public void loadWordFileAndReplaceContent(Map<String, Map<String, String>> listOfData) throws Exception {
		System.out.println("***** Control inside the WordDocumentHelper.loadWordFileAndReplaceContent() *****");
		String fileLocationPath = PATH_TO_TEMPLATE;
		/*StopWatch watch = new StopWatch();
		 watch.start();*/
		long start = System.currentTimeMillis();
		File file = new File(fileLocationPath);
		File destination = new File("H:\\ACP\\Soft\\sample\\");
		FileUtils.copyFileToDirectory(file, destination);// copying actual file, so actula file should not any effect

		file = new File("H:\\ACP\\Soft\\sample\\" + FILE_NAME);
		fileLocationPath = file.getAbsolutePath();

		FileInputStream fis = new FileInputStream(fileLocationPath);
		OPCPackage opcPackage = OPCPackage.open(fis);
		XWPFDocument doc = new XWPFDocument(opcPackage);

		//Iterator<XWPFParagraph> paragraphs = doc.getParagraphs().iterator();
		
		for (int i = 0; i < doc.getParagraphs().size(); i++) {
			XWPFParagraph xwpfParagraph = (XWPFParagraph) doc.getParagraphs().get(i);
	
			System.out.println(" "+xwpfParagraph.getText());
			List<XWPFRun> runs = xwpfParagraph.getRuns();
			if (runs != null) {
				//for (XWPFRun r : runs) {
					for (int ind = 0; ind < runs.size(); ind++) {
						XWPFRun r = runs.get(ind);
						String text = r.getText(0);
					if (text != null && (text.contains("${"))) {
						
						if(text.contains("${firstApplicantAddress}")) {

							r.addBreak();
							r.setText("2. Ms. DIVYAVANI S d");
							r.setBold(true);
							/*IRunBody iRUn= r.getParent();
							CTR ctr = r.getCTR();*/
							
							XWPFRun run = xwpfParagraph.createRun();
							run.setBold(false);
							run.setText(", Wommo W/o. Mr. E Jagadish, (Aadhar No.779955669727) aged about 33 years, (PAN No. DQJPS9234Q),");
							xwpfParagraph.addRun(run);
							
						}
						text = text.replace("${customerName1}", "1.	Smt. SIDDAMBIKE");
						text = text.replace("${customerName2}", "2.	Sri. NATESH. H.G");
						text = text.replace("${customerName3}", "3.	Sri. N. NAGARAJA");
						text = text.replace("${customerName4}", "4.	Mr.NEETISH PURUSHOTTAMA.B.N");
						text = text.replace("${customerName5}", "5.	Sri.M.CHANDRASHEKAR");
						
						text = text.replace("${customerAddres5}","aged about 57 years, S/o. Late.Sri.M.Muniyappa.\r\n" + 
								"Residing at No.310, Kaggadasapura, C.V.Raman Nagar, Bengaluru\r\n" + 
								"");
						text = text.replace("${firstApplicantName}", "1. Mr. E JAGADISH");
						text = text.replace("${firstApplicantAddress}", "Mr. E Jagadish, (Aadhar No.779955669727)");
						
						r.setText(text, 0);
					}
				}
			}
		}
		
		//addTable(doc);
		//int tableIndexNumber = 1;
		//addRowsOnTable(doc,tableIndexNumber,listOfData.get("firstTableData"));
		//long countTables = 0l; 
		
		
		for (XWPFTable tbl : doc.getTables()) {
			for (XWPFTableRow row : tbl.getRows()) {
				for (XWPFTableCell cell : row.getTableCells()) {
					for (XWPFParagraph p : cell.getParagraphs()) {
						for (XWPFRun r : p.getRuns()) {
							String text = r.getText(0);
							if (text != null && text.contains("${milestoneName}")) {
								text = text.replace("${milestoneName}", "On Booking");
								r.setText(text, 0);
							}
						}
					}
				}
			}
		}

		//XWPFParagraph paragraph = paragraphs.get(paragraphs.size() - 1);
		//System.out.println(paragraph.getText());
		// XWPFRun runText = paragraph.createRun();

		// if you want to add text
		// runText.setText("appending here\n");

		// if you want to add image
		// runText.addPicture(java.io.InputStream pictureData, int pictureType,
		// java.lang.String filename, int width, int height)

		try (FileOutputStream out = new FileOutputStream(fileLocationPath)) {
			doc.write(out);
			opcPackage.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Process process;
		process = new ProcessBuilder(PATH_TO_EXE, fileLocationPath, PATH_TO_OUTPUT).start();
		process.waitFor();

		System.out.println("Result of Office processing: " + process.exitValue());

		file = new File(PATH_TO_OUTPUT);
		byte[] fileContent1 = Files.readAllBytes(file.toPath());
		 // finding the time after the operation is executed
	      long end = System.currentTimeMillis();
	      //finding the time difference and converting it into seconds
	      float sec = (end - start) / 1000F; System.out.println("seconds "+sec + " seconds");
		System.out.println(fileContent1.length);
	}
	 
	@SuppressWarnings("unused")
	private void addRowsOnTable(XWPFDocument doc, int tableIndexNumber, Map<String, String> tableData) {
		XWPFTable trnTable = doc.getTables().get(1);

		XWPFTableRow tableRowTwo = trnTable.createRow();// adding rows to tables
		// Attributes of second row
		tableRowTwo.getCell(0).setText("SBI110");
		tableRowTwo.getCell(1).setText("12,500.00");
		tableRowTwo.getCell(2).setText("CB Bank");
		tableRowTwo.getCell(3).setText("15-05-2021");

		
		for (XWPFTableRow row : trnTable.getRows()) {
			for (XWPFTableCell cell : row.getTableCells()) {
				for (XWPFParagraph p : cell.getParagraphs()) {
					for (XWPFRun r : p.getRuns()) {
						String text = r.getText(0);
						if (text != null && text.contains("${checkNumber}")) {
							text = text.replace("${checkNumber}", "HDFC1102");
							r.setText(text, 0);
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("unused")
	private void addTable(XWPFDocument doc) {   // Creating a table
        XWPFTable table = doc.createTable();
 
        // Create first row in a table
        XWPFTableRow tableRowOne = table.getRow(0);
 
        // Attributes added to the first table
        tableRowOne.getCell(0).setText("Geeks (0,0)");
        tableRowOne.addNewTableCell().setText("For (0,1)");
        tableRowOne.addNewTableCell().setText(
            "Geeks (0,2)");
 
        // Creating a second row
        XWPFTableRow tableRowTwo = table.createRow();
 
        // Attributes of second row
        tableRowTwo.getCell(0).setText("Geeks (1,0)");
        tableRowTwo.getCell(1).setText("For (1,1)");
        tableRowTwo.getCell(2).setText("Geeks (1,2)");
 
        // Creating a third row
        XWPFTableRow tableRowThree = table.createRow();
 
        // Attibutes of row
        tableRowThree.getCell(0).setText("Geeks (2,0)");
        tableRowThree.getCell(1).setText("For (2,1)");
        tableRowThree.getCell(2).setText("Geeks (2,2)");
        
	}
	
	@SuppressWarnings("unused")
	private void createParagraphs(XWPFParagraph p, String[] paragraphs) {
	    if (p != null) {
	        XWPFDocument doc = p.getDocument();
	        XmlCursor cursor = p.getCTP().newCursor();
	        for (int i = 0; i < paragraphs.length; i++) {
	            XWPFParagraph newP = doc.createParagraph();
	            newP.getCTP().setPPr(p.getCTP().getPPr());
	            XWPFRun newR = newP.createRun();
	            newR.getCTR().setRPr(p.getRuns().get(0).getCTR().getRPr());
	            newR.setText(paragraphs[i]);
	            XmlCursor c2 = newP.getCTP().newCursor();
	            c2.moveXml(cursor);
	            c2.dispose();
	        }
	        cursor.removeXml(); // Removes replacement text paragraph
	        cursor.dispose();
	    }
	}
	
	@SuppressWarnings("unused")
	private void generateHTMLFromPDF(String filename) throws Exception {
		System.out.println("WordDocumentHelper.generateHTMLFromPDF()");

		/*
		 * <dependency> <groupId>net.sf.cssbox</groupId>
		 * <artifactId>pdf2dom</artifactId> <version>1.8</version> </dependency>
		 */
		/*String dataDir = "H:\\ACP\\Docs\\Add-ons\\COST BreakUp templates\\Sushantham\\";
		PDDocument pdf = PDDocument.load(new File(dataDir + "push.pdf"));
		Writer output = new PrintWriter(dataDir + "pdf.html", "utf-8");
		new PDFDomTree().writeText(pdf, output);

		output.close();
		System.out.println("WordDocumentHelper.generateHTMLFromPDF()1");*/
		/*
		 * Process process; process = new ProcessBuilder(PATH_TO_EXE, fileLocationPath,
		 * PATH_TO_OUTPUT).start(); process.waitFor();
		 * 
		 * System.out.println("Result of Office processing: " + process.exitValue());
		 * 
		 * file = new File(PATH_TO_OUTPUT); byte[] fileContent1 =
		 * Files.readAllBytes(file.toPath());
		 */
	}

	public void convertWordDocumentToHtml() throws Exception{
		String dataDir = "H:\\ACP\\Docs\\Add-ons\\COST BreakUp templates\\Sushantham\\";
		/*System.out.println("WordDocumentHelper.convertWordDocumentToHtml()");
		String dataDir = "H:\\ACP\\Docs\\Add-ons\\COST BreakUp templates\\Sushantham\\";
		// Load the document from disk.
		Document doc = new Document(dataDir + "push.pdf");
		// Save the document into HTML.
		doc.save(dataDir + "Document_out.html", SaveFormat.HTML);*/
		
		HWPFDocumentCore wordDocument = WordToHtmlUtils.loadDoc(new FileInputStream(dataDir));

	    WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
	            DocumentBuilderFactory.newInstance().newDocumentBuilder()
	                    .newDocument());
	    wordToHtmlConverter.processDocument(wordDocument);
	    Document htmlDocument = wordToHtmlConverter.getDocument();
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    DOMSource domSource = new DOMSource(htmlDocument);
	    StreamResult streamResult = new StreamResult(out);

	    TransformerFactory tf = TransformerFactory.newInstance();
	    Transformer serializer = tf.newTransformer();
	    serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	    serializer.setOutputProperty(OutputKeys.INDENT, "yes");
	    serializer.setOutputProperty(OutputKeys.METHOD, "html");
	    serializer.transform(domSource, streamResult);
	    out.close();

	    String result = new String(out.toByteArray());
	    System.out.println(result);
	}
	
	public static void main(String[] args) throws Exception {
		//new WordDocumentHelper().
		
		Map<String,Map<String, String>> listOfData =  new HashMap<>();
		Map<String, String> map = new HashMap<>();
		
		map.put("${customerName1}", "1.	Smt. SIDDAMBIKE");
		map.put("${customerName2}", "2.	Sri. NATESH. H.G");
		map.put("${customerName3}", "3.	Sri. N. NAGARAJA");
		map.put("${customerName4}", "4.	Mr.NEETISH PURUSHOTTAMA.B.N");
		map.put("${customerName5}", "5.	Sri.M.CHANDRASHEKAR");
		map.put("${customerAddres5}","aged about 57 years, S/o. Late.Sri.M.Muniyappa.\r\n" + 
				"Residing at No.310, Kaggadasapura, C.V.Raman Nagar, Bengaluru\r\n" + 
				"");
		
		map.put("${firstApplicantName}", "Aniket Chavan");
		listOfData.put("documentData", map);
		
		Map<String, String> listOfTransactionData = new HashMap<>();
		map.put("${chequeNumber}", "haystack");
		map.put("${chequeAmount}", "15,000,00");
		map.put("${bankName}", "CB Bank");
		map.put("${date}", "15-05-2021");
		
		listOfData.put("firstTableData", listOfTransactionData);
		
		//new WordDocumentHelper().usingDocx4j(listOfData);
		new WordDocumentHelper().loadWordFileAndReplaceContent(listOfData);
	}
	
}
