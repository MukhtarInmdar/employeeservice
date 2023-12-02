/**
 * 
 */
package com.sumadhura.employeeservice.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ByteBuffer;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.sumadhura.employeeservice.dto.Email;
import com.sumadhura.employeeservice.dto.FileInfo;
import com.sumadhura.employeeservice.enums.MetadataId;
import com.sumadhura.employeeservice.exception.EmployeeFinancialServiceException;
import com.sumadhura.employeeservice.service.MailService;
import com.sumadhura.employeeservice.service.dto.CarParkingAllotmentPdfDetailInfo;
import com.sumadhura.employeeservice.service.dto.DemandNoteGeneratorInfo;
import com.sumadhura.employeeservice.service.dto.EmployeeFinancialServiceInfo;
import com.sumadhura.employeeservice.service.dto.EmployeeFinancialTransactionServiceInfo;
import com.sumadhura.employeeservice.service.dto.FinancialConsolidatedReceiptPdfInfo;
import com.sumadhura.employeeservice.service.dto.FinancialUploadDataInfo;
import com.sumadhura.employeeservice.service.helpers.EmployeeFinancialHelper;


/**
 * PdfHelper class provides Support for to generate Pdf.
 * 
 * @author Venkat_Koniki
 * @since 25.01.2020
 * @time 10:37AM
 */

@Component("PdfHelper")
public class PdfHelper {
	//XMLParser
	//https://stackoverflow.com/questions/1632005/ordered-list-html-lower-alpha-with-right-parentheses
	//https://wordhtml.com/
	//https://community.jamasoftware.com/blogs/robin-calhoun/2020/07/13/quick-start-html-templates-for-velocity-pdf-report
	//https://www.codegrepper.com/code-examples/javascript/get+all+input+fields+jquery+with+id+property+in+jquery
	private final static Logger log = Logger.getLogger(PdfHelper.class);
	
	@Autowired(required = true)
	@Qualifier("mailService")
	private MailService mailServiceImpl;
	
	@SuppressWarnings("unused")
	private  String USER_PASS;
	@SuppressWarnings("unused")
	private  String OWNER_PASS;
	
	@Autowired
	private ResponceCodesUtil responceCodesUtil;
	
	public FileInfo XMLWorkerHelper(Email email, String requestFrom, EmployeeFinancialServiceInfo employeeFinancialServiceInfo) throws FileNotFoundException, IOException, DocumentException, Exception {
		log.info(" ***** control is inside the XMLWorkerHelper in EmployeeFinancialServiceImpl ***** = "+requestFrom);
		//format are regular and 5% gst formats, old format first disbrustment, milestone completion, final demand note
		FileInfo fileInfo = new FileInfo();
		String demandNotePdfFilePath = ""; 
		String demandNotePdfFileUrl = "";
		boolean isGenerateDemandNote = requestFrom.equalsIgnoreCase("GenerateDemandNote");
		DemandNoteGeneratorInfo demandNoteGeneratorInfo = email.getDemandNoteGeneratorInfoList().get(0);
		long portNumber = employeeFinancialServiceInfo.getPortNumber();
		/* Saving Interest Letter Files in Seperate Folder */
		if("Interest_Letter".equalsIgnoreCase(employeeFinancialServiceInfo.getActionUrl())) {
			if (portNumber == Long.valueOf(MetadataId.LOCAL_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("UAT_INTEREST_LETTER_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("UAT_INTEREST_LETTER_PDF_URL");
			}else if (portNumber == Long.valueOf(MetadataId.UAT_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("UAT_INTEREST_LETTER_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("UAT_INTEREST_LETTER_PDF_URL");
			} else if (portNumber == Long.valueOf(MetadataId.CUG_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("CUG_INTEREST_LETTER_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("CUG_INTEREST_LETTER_PDF_URL");
			}else if (portNumber == Long.valueOf(MetadataId.HTTPS_CUG_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("CUG_INTEREST_LETTER_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("CUG_INTEREST_LETTER_PDF_URL");
			} else if (portNumber == Long.valueOf(MetadataId.LIVE_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("LIVE_INTEREST_LETTER_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("LIVE_INTEREST_LETTER_PDF_URL");
			} else if (portNumber == Long.valueOf(MetadataId.HTTPS_LIVE_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("LIVE_INTEREST_LETTER_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("LIVE_INTEREST_LETTER_PDF_URL");
			} else {
				log.error("Path missing to save the File. Please contact to Support Team");
				throw new EmployeeFinancialServiceException("Interest letter drive path missing to save the File, Please contact to Support Team.");
			}
		}else {
			if (portNumber == Long.valueOf(MetadataId.LOCAL_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("UAT_DEMAND_NOTE_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("UAT_DEMAND_NOTE_PDF_URL");
			}else if (portNumber == Long.valueOf(MetadataId.UAT_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("UAT_DEMAND_NOTE_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("UAT_DEMAND_NOTE_PDF_URL");
			} else if (portNumber == Long.valueOf(MetadataId.CUG_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("CUG_DEMAND_NOTE_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("CUG_DEMAND_NOTE_PDF_URL");
			}else if (portNumber == Long.valueOf(MetadataId.HTTPS_CUG_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("CUG_DEMAND_NOTE_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("CUG_DEMAND_NOTE_PDF_URL");
			} else if (portNumber == Long.valueOf(MetadataId.LIVE_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("LIVE_DEMAND_NOTE_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("LIVE_DEMAND_NOTE_PDF_URL");
			} else if (portNumber == Long.valueOf(MetadataId.HTTPS_LIVE_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("LIVE_DEMAND_NOTE_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("LIVE_DEMAND_NOTE_PDF_URL");
			} else {
				log.error("Path missing to save the File. Please contact to Support Team");
				throw new EmployeeFinancialServiceException("Demand note drive path missing to save the File, Please contact to Support Team.");
			}
		}
		
		StringBuilder filePath = new StringBuilder(demandNotePdfFilePath)
				.append("/").append(demandNoteGeneratorInfo.getSiteId())
				.append(!isGenerateDemandNote ? "/DemandNotePreviewFiles" : "")
				.append(!isGenerateDemandNote ? "" : "/"+demandNoteGeneratorInfo.getFlatBookingId())
				.append(!isGenerateDemandNote ? "" : "/"+demandNoteGeneratorInfo.getFinBokFomDmdNoteId());
		
		StringBuilder fileLocation = new StringBuilder(demandNotePdfFileUrl)
				.append("/").append(demandNoteGeneratorInfo.getSiteId())
				.append(!isGenerateDemandNote ? "/DemandNotePreviewFiles" : "")
				.append(!isGenerateDemandNote ? "" : "/"+demandNoteGeneratorInfo.getFlatBookingId())
				.append(!isGenerateDemandNote ? "" : "/"+demandNoteGeneratorInfo.getFinBokFomDmdNoteId());
		
		if(!isGenerateDemandNote){
			DeleteDemandNotePreviewFiles(new File(filePath.toString()),3,"DemandNotePdfFiles");
		}
		/* For Interest Letter We need to Change the PDF File Name and Date */
		if("Interest_Letter".equalsIgnoreCase(employeeFinancialServiceInfo.getActionUrl())) {
			getInterestLetterFileName(demandNoteGeneratorInfo);
		}
		
		String fileName = getFileName(filePath.toString(), demandNoteGeneratorInfo.getDemandNotePdfFileName());
		StringBuilder pdfFileName = new StringBuilder(filePath).append("/").append(fileName);
		//final Document document = new Document(PageSize.LEGAL);
		final Document document = new Document(PageSize.A4, 25, 25, 100, 80);//this is  for footer and header
		final PdfWriter pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(pdfFileName.toString()));
		
		USER_PASS = demandNoteGeneratorInfo.getPancard();
		OWNER_PASS = demandNoteGeneratorInfo.getPancard();

		/* for output Response */
		fileInfo.setPassword(demandNoteGeneratorInfo.getPancard());
		fileInfo.setName(fileName);
		fileInfo.setUrl(fileLocation.append("/").append(fileName).toString());
		fileInfo.setFilePath(pdfFileName.toString());
		if(isGenerateDemandNote) {
			//System.out.println("PdfHelper.XMLWorkerHelper() demand note booking fom id = "+demandNoteGeneratorInfo.getFinBokFomDmdNoteId().toString());
			fileInfo.setId(Integer.valueOf(demandNoteGeneratorInfo.getFinBokFomDmdNoteId().toString()));
		}
		
		//adding password to the pdf.
		//pdfWriter.setEncryption(USER_PASS.getBytes(), OWNER_PASS.getBytes(), PdfWriter.ALLOW_PRINTING,PdfWriter.ENCRYPTION_AES_128);
		document.open();
		document.addAuthor("Amaravadhis software services private limited");
		document.addCreator("venkat koniki");
		document.addSubject(" Demand Note ");
		document.addTitle(" Demand Note ");
		
		document.addCreationDate();

		String headData = "";
		String footerData = "===";//using these drawing the line in PDF file, can check HeaderFooterPageEvent2
		String rightLogo = Util.isNotEmptyObject(demandNoteGeneratorInfo.getRightSidelogoFilePath())? demandNoteGeneratorInfo.getRightSidelogoFilePath() : demandNoteGeneratorInfo.getRightSidelogoForPdf();
		String leftLogo = Util.isNotEmptyObject(demandNoteGeneratorInfo.getLeftSidelogoFilePath())? demandNoteGeneratorInfo.getLeftSidelogoFilePath() : demandNoteGeneratorInfo.getLeftSidelogoForPdf();
		Long headerTextHeight = 5l;
		Long footerTextHeight = 60l;
		Long fontSize = 7l;

		HeaderFooterPageEvent2 logo = new HeaderFooterPageEvent2();
		logo.setHeadData(headData);
		logo.setFooterData(footerData);
		logo.setRightLogo(rightLogo);
		logo.setLeftLogo(leftLogo);
		logo.setHeaderTextHeight(headerTextHeight);
		logo.setFooterTextHeight(footerTextHeight);
		logo.setFontSize(fontSize);
		
		final com.itextpdf.tool.xml.XMLWorkerHelper worker = com.itextpdf.tool.xml.XMLWorkerHelper.getInstance();
		if(employeeFinancialServiceInfo.isThisUplaodedData()) {
			//comment this if condition if u want to use latest demand note pdf format for upload data
			email.setTemplateName("/demandnotes/demandnote.vm");	
			if(demandNoteGeneratorInfo.getFinSchemeType()!=null && demandNoteGeneratorInfo.getFinSchemeType().trim().equalsIgnoreCase("Special Offer")) {
				email.setTemplateName("/demandnotes/demandnoteSpecialOffer5%GST.vm");	
			}
			
			if("First_Disbursement_Demandnote".equalsIgnoreCase(employeeFinancialServiceInfo.getActionUrl())) {
				email.setTemplateName("/demandnotes/FirstDisbursementDemandnote.vm");
				if(demandNoteGeneratorInfo.getFinSchemeType()!=null && demandNoteGeneratorInfo.getFinSchemeType().trim().equalsIgnoreCase("Special Offer")) {
					email.setTemplateName("/demandnotes/FirstDisbursementDemandnoteSpecialOffer%GST.vm");	
				}
			} else if("Milestone_Completion_Demandnote".equalsIgnoreCase(employeeFinancialServiceInfo.getActionUrl())) {
				email.setTemplateName("/demandnotes/MilestoneCompletionDemandnote.vm");
				if(demandNoteGeneratorInfo.getFinSchemeType()!=null && demandNoteGeneratorInfo.getFinSchemeType().trim().equalsIgnoreCase("Special Offer")) {
					email.setTemplateName("/demandnotes/MilestoneCompletionDemandnoteSpecialOffer%GST.vm");
				}
			} else if("Final_Demand_Note".equalsIgnoreCase(employeeFinancialServiceInfo.getActionUrl())) {
				//email.setTemplateName("/demandnotes/demandnote.vm");
				email.setTemplateName("/demandnotes/FinalDemandNote.vm");
				if(demandNoteGeneratorInfo.getFinSchemeType()!=null && demandNoteGeneratorInfo.getFinSchemeType().trim().equalsIgnoreCase("Special Offer")) {
					email.setTemplateName("/demandnotes/FinalDemandNoteSpecialOffer%GST.vm");	
				}
			}
	        /*StringBuffer footerAdderss = null;
	        if(demandNoteGeneratorInfo.getCompanyCin() != "-"){
	        	footerAdderss = new StringBuffer("Tel: "+demandNoteGeneratorInfo.getCompanyTelephoneNo()+" CIN: "+demandNoteGeneratorInfo.getCompanyCin()+" GSTIN: "+demandNoteGeneratorInfo.getCompanyGstin()+" E-mail: "+demandNoteGeneratorInfo.getCompanyEmail()+" Website: "+demandNoteGeneratorInfo.getCompanyWebsite());
	        } else {
	        	footerAdderss = new StringBuffer("Tel: "+demandNoteGeneratorInfo.getCompanyTelephoneNo()+" LLPIN: "+demandNoteGeneratorInfo.getCompanyLlpin()+" GSTIN: "+demandNoteGeneratorInfo.getCompanyGstin()+" E-mail: "+demandNoteGeneratorInfo.getCompanyEmail()+" Website: "+demandNoteGeneratorInfo.getCompanyWebsite());
	        }*/
	        
	        //getFooterAddress(demandNoteGeneratorInfo);
	        
	        //for header third parameter "abc"
	        //for footer fourth parameter
	        pdfWriter.setPageEvent(logo);
			//pdfWriter.setPageEvent(new HeaderFooterPageEvent2("","===",demandNoteGeneratorInfo.getRightSidelogoForPdf(),5l,60l,7l));
			pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",demandNoteGeneratorInfo.getCompanyName(),"",10l,50l,8l));
			pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",demandNoteGeneratorInfo.getCompanyBillingAddress(),"",20l,40l,7l));
			pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",getFooterAddress(demandNoteGeneratorInfo),"",30l,30l,7l));
			
		} else if("Final_Demand_Note".equalsIgnoreCase(employeeFinancialServiceInfo.getActionUrl())) {
				//email.setTemplateName("/demandnotes/demandnote.vm");
				email.setTemplateName("/demandnotes/FinalDemandNote.vm");
				if(demandNoteGeneratorInfo.getFinSchemeType()!=null && demandNoteGeneratorInfo.getFinSchemeType().trim().equalsIgnoreCase("Special Offer")) {
					email.setTemplateName("/demandnotes/FinalDemandNoteSpecialOffer%GST.vm");	
				}
				
		       /* StringBuffer footerAdderss = null;
		        
		        if(demandNoteGeneratorInfo.getCompanyCin() != "-"){
		        	footerAdderss = new StringBuffer(" GSTIN: "+demandNoteGeneratorInfo.getCompanyGstin()+" CIN: "+demandNoteGeneratorInfo.getCompanyCin()+" Tel: "+demandNoteGeneratorInfo.getCompanyTelephoneNo()+" E-mail: "+demandNoteGeneratorInfo.getCompanyEmail()+" Website: "+demandNoteGeneratorInfo.getCompanyWebsite());
		        } else {
		        	footerAdderss = new StringBuffer(" GSTIN: "+demandNoteGeneratorInfo.getCompanyGstin()+" LLPIN: "+demandNoteGeneratorInfo.getCompanyLlpin()+" Tel: "+demandNoteGeneratorInfo.getCompanyTelephoneNo()+" E-mail: "+demandNoteGeneratorInfo.getCompanyEmail()+" Website: "+demandNoteGeneratorInfo.getCompanyWebsite());
		        }*/
		        
		        //for header third parameter "abc"
		        //for footer fourth parameter
		        pdfWriter.setPageEvent(logo);
		       // pdfWriter.setPageEvent(new HeaderFooterPageEvent2("","===",demandNoteGeneratorInfo.getRightSidelogoForPdf(),5l,60l,7l));
				pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",demandNoteGeneratorInfo.getCompanyName(),"",10l,50l,8l));
				pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",demandNoteGeneratorInfo.getCompanyBillingAddress(),"",20l,40l,7l));
				pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",getFooterAddress(demandNoteGeneratorInfo),"",30l,30l,7l));
			}	
		else if("First_Disbursement_Demandnote".equalsIgnoreCase(employeeFinancialServiceInfo.getActionUrl())) {
			//email.setTemplateName("/demandnotes/demandnote.vm");
			email.setTemplateName("/demandnotes/FirstDisbursementDemandnote.vm");
			if(demandNoteGeneratorInfo.getFinSchemeType()!=null && demandNoteGeneratorInfo.getFinSchemeType().trim().equalsIgnoreCase("Special Offer")) {
				email.setTemplateName("/demandnotes/FirstDisbursementDemandnoteSpecialOffer%GST.vm");	
			}
			
	       /* String footerAdderss = "";
	        
	        if(demandNoteGeneratorInfo.getCompanyCin() != "-"){
	        	footerAdderss = "Tel: "+demandNoteGeneratorInfo.getCompanyTelephoneNo()+" CIN: "+demandNoteGeneratorInfo.getCompanyCin()+" GSTIN: "+demandNoteGeneratorInfo.getCompanyGstin()+" E-mail: "+demandNoteGeneratorInfo.getCompanyEmail()+" Website: "+demandNoteGeneratorInfo.getCompanyWebsite();
	        } else {
	        	footerAdderss = "Tel: "+demandNoteGeneratorInfo.getCompanyTelephoneNo()+" LLPIN: "+demandNoteGeneratorInfo.getCompanyLlpin()+" GSTIN: "+demandNoteGeneratorInfo.getCompanyGstin()+" E-mail: "+demandNoteGeneratorInfo.getCompanyEmail()+" Website: "+demandNoteGeneratorInfo.getCompanyWebsite();
	        }*/
	        
	        //for header third parameter "abc"
	        //for footer fourth parameter
	        pdfWriter.setPageEvent(logo);
	       // pdfWriter.setPageEvent(new HeaderFooterPageEvent2("","===",demandNoteGeneratorInfo.getRightSidelogoForPdf(),5l,60l,7l));
			pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",demandNoteGeneratorInfo.getCompanyName(),"",10l,50l,8l));
			pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",demandNoteGeneratorInfo.getCompanyBillingAddress(),"",20l,40l,7l));
			pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",getFooterAddress(demandNoteGeneratorInfo),"",30l,30l,7l));
		} else if("Interest_Letter".equalsIgnoreCase(employeeFinancialServiceInfo.getActionUrl())) {
			email.setTemplateName("/demandnotes/InterestLetter.vm");
			document.addSubject(" Interest Letter ");
			document.addTitle(" Interest Letter ");
			
	        /*String footerAdderss = "";
	        
	        if(demandNoteGeneratorInfo.getCompanyCin() != "-"){
	        	footerAdderss = "Tel: "+demandNoteGeneratorInfo.getCompanyTelephoneNo()+" CIN: "+demandNoteGeneratorInfo.getCompanyCin()+" GSTIN: "+demandNoteGeneratorInfo.getCompanyGstin()+" E-mail: "+demandNoteGeneratorInfo.getCompanyEmail()+" Website: "+demandNoteGeneratorInfo.getCompanyWebsite();
	        } else {
	        	footerAdderss = "Tel: "+demandNoteGeneratorInfo.getCompanyTelephoneNo()+" LLPIN: "+demandNoteGeneratorInfo.getCompanyLlpin()+" GSTIN: "+demandNoteGeneratorInfo.getCompanyGstin()+" E-mail: "+demandNoteGeneratorInfo.getCompanyEmail()+" Website: "+demandNoteGeneratorInfo.getCompanyWebsite();
	        }*/
	        
	        //for header third parameter "abc"
	        //for footer fourth parameter
	        pdfWriter.setPageEvent(logo);
	        //pdfWriter.setPageEvent(new HeaderFooterPageEvent2("","===",demandNoteGeneratorInfo.getRightSidelogoForPdf(),5l,60l,7l));
			pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",demandNoteGeneratorInfo.getCompanyName(),"",10l,50l,8l));
			pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",demandNoteGeneratorInfo.getCompanyBillingAddress(),"",20l,40l,7l));
			pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",getFooterAddress(demandNoteGeneratorInfo),"",30l,30l,7l));
		}else {
			//email.setTemplateName("/demandnotes/demandnote.vm");
			email.setTemplateName("/demandnotes/MilestoneCompletionDemandnote.vm");
			if(demandNoteGeneratorInfo.getFinSchemeType()!=null && demandNoteGeneratorInfo.getFinSchemeType().trim().equalsIgnoreCase("Special Offer")) {
				email.setTemplateName("/demandnotes/MilestoneCompletionDemandnoteSpecialOffer%GST.vm");
			}
	        /*String footerAdderss = "";
	        
	        if(demandNoteGeneratorInfo.getCompanyCin() != "-"){
	        	footerAdderss = "Tel: "+demandNoteGeneratorInfo.getCompanyTelephoneNo()+" CIN: "+demandNoteGeneratorInfo.getCompanyCin()+" GSTIN: "+demandNoteGeneratorInfo.getCompanyGstin()+" E-mail: "+demandNoteGeneratorInfo.getCompanyEmail()+" Website: "+demandNoteGeneratorInfo.getCompanyWebsite();
	        } else {
	        	footerAdderss = "Tel: "+demandNoteGeneratorInfo.getCompanyTelephoneNo()+" LLPIN: "+demandNoteGeneratorInfo.getCompanyLlpin()+" GSTIN: "+demandNoteGeneratorInfo.getCompanyGstin()+" E-mail: "+demandNoteGeneratorInfo.getCompanyEmail()+" Website: "+demandNoteGeneratorInfo.getCompanyWebsite();
	        }*/
	        
	        //for header third parameter "abc"
	        //for footer fourth parameter
	        pdfWriter.setPageEvent(logo);
	        //pdfWriter.setPageEvent(new HeaderFooterPageEvent2("","===",demandNoteGeneratorInfo.getRightSidelogoForPdf(),5l,60l,7l));
			pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",demandNoteGeneratorInfo.getCompanyName(),"",10l,50l,8l));
			pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",demandNoteGeneratorInfo.getCompanyBillingAddress(),"",20l,40l,7l));
			pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",getFooterAddress(demandNoteGeneratorInfo),"",30l,30l,7l));
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
        model.put("email", email);
        final String data = mailServiceImpl.geVelocityTemplateContent(model);
		//long startTime = System.currentTimeMillis();
		log.info(rightLogo +" rightLogo *********************** \n"+fileName+"\n"+pdfFileName);
		if (isGenerateDemandNote) {
			ExecutorService executorService = Executors.newFixedThreadPool(10);
			try {
				executorService.execute(new Runnable() {
					public void run() {
						try {
							worker.parseXHtml(pdfWriter, document, new StringReader(data));
							document.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
				executorService.shutdown();
			} catch (Exception e) {
				e.printStackTrace();
				executorService.shutdown();
				log.error("Failed to store file in drive, Please contact to Support Team.");
				throw new EmployeeFinancialServiceException("Failed to store file in drive, Please contact to Support Team.");
			}

		} else {
			worker.parseXHtml(pdfWriter, document, new StringReader(data));
			document.close();
			/*try {	
				worker.parseXHtml(pdfWriter, document, new StringReader(data));
				document.close();
			}catch(Exception ex){
				System.out.println(ex.getMessage());
				System.out.println(ex.getStackTrace());
			} */
		}
		/*long endTime = System.currentTimeMillis();
		long duration = (endTime - startTime); // Total execution time in milli seconds
		log.info(" ********************************************************************* " + duration);*/
		return fileInfo;
	}
	
	private String getFooterAddress(DemandNoteGeneratorInfo demandNoteGeneratorInfo) {
		StringBuffer footerAdderss = new StringBuffer("");
		if (!demandNoteGeneratorInfo.getCompanyTelephoneNo().equals("-") && !demandNoteGeneratorInfo.getCompanyTelephoneNo().equals("N/A")) {
			footerAdderss.append("Tel: " + demandNoteGeneratorInfo.getCompanyTelephoneNo());
		}
		
		if (!demandNoteGeneratorInfo.getCompanyCin().equals("-")) {
			footerAdderss.append(" CIN: " + demandNoteGeneratorInfo.getCompanyCin());	
		} else {
			footerAdderss.append(" LLPIN: " + demandNoteGeneratorInfo.getCompanyLlpin());
		}
		
		footerAdderss.append(" GSTIN: " + demandNoteGeneratorInfo.getCompanyGstin());

		if (!demandNoteGeneratorInfo.getCompanyEmail().equals("-") && !demandNoteGeneratorInfo.getCompanyEmail().equals("N/A")) {
			footerAdderss.append(" E-mail: " + demandNoteGeneratorInfo.getCompanyEmail());
		}

		if (!demandNoteGeneratorInfo.getCompanyWebsite().equals("-") && !demandNoteGeneratorInfo.getCompanyWebsite().equals("N/A")) {
			footerAdderss.append(" Website: " + demandNoteGeneratorInfo.getCompanyWebsite());
		}
		return footerAdderss.toString().intern();
	}

	public void DeleteDemandNotePreviewFiles(File file,int days,String folderToDelete) {
	        System.out.println("Now will search folders and delete files,");
	        if (file.isDirectory()) {
	           //System.out.println("Date Modified : " + file.lastModified());
	            for (File f : file.listFiles()) {
	            	//DeleteDemandNotePreviewFiles(f);
	            	if(f.isFile()) {
	            		if((f.getName().contains(folderToDelete) || f.getName().contains("TaxInvoiceUploadedFiles")) 
	            				&& f.getName().endsWith(".zip") && !f.getName().endsWith(".pdf")) {
	            			long diff = new Date().getTime() - f.lastModified();
	            			
	            			//delete this file if 15 days older
	            			if (diff > days * 24 * 60 * 60 * 1000) {
	            			   boolean flag =  f.delete();
	            			   System.out.println("Now will search folders and delete files,"+flag);
	            			}
	            		}
	            	} else if(f.isDirectory() && f.getName().contains(folderToDelete)){
	            	   boolean flag =  f.delete();
         			   System.out.println("folders and files,"+flag);
	            	}
	            }
	        } else {
	            //file.delete();
	        }
	    }
	// function to delete subdirectories and files
	   public void folderDeleteAndSubFolder(File file,int days,String folderToDelete) {
	        System.out.println("Now will search folders and delete files,");
	        if (file.isDirectory()) {
	        	// store all the paths of files and folders present inside directory
	            for (File f : file.listFiles()) {
	            	if(f.isDirectory()) {
	            		folderDeleteAndSubFolder(f,0,folderToDelete);
	            	}else if(f.isFile()) {
	            			long diff = new Date().getTime() - f.lastModified();
	            			//delete this file if 15 days older
	            			if (diff > days * 24 * 60 * 60 * 1000) {
	            			   boolean flag =  f.delete();
	            			   System.out.println("Now will search folders and delete files,"+flag);
	            			}
	            	} else if(f.isDirectory() && f.getName().contains(folderToDelete)){
	            	   boolean flag =  f.delete();
        			   System.out.println("folders and files,"+flag);
        			   if(!flag) {
        				   folderDeleteAndSubFolder(f,0,folderToDelete);
        			   }
	            	}
	            }
	        } else {
	            //file.delete();
	        }
	    }
	   
	   
	public FileInfo XMLExcelWorkerHelperForInterestAmt(Email email, String requestFrom, EmployeeFinancialServiceInfo employeeFinancialServiceInfo) throws FileNotFoundException, IOException, DocumentException, Exception {
		log.info(" ***** control is inside the XMLWorkerHelper in EmployeeFinancialServiceImpl ***** = "+requestFrom);
		FileInfo fileInfo = new FileInfo();
		String demandNotePdfFilePath = ""; 
		String demandNotePdfFileUrl = "";
		boolean isGenerateDemandNote = requestFrom.equalsIgnoreCase("GenerateDemandNote");
		DemandNoteGeneratorInfo demandNoteGeneratorInfo = email.getDemandNoteGeneratorInfoList().get(0);
		long portNumber = employeeFinancialServiceInfo.getPortNumber();

		if (portNumber == Long.valueOf(MetadataId.LOCAL_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("UAT_DEMAND_NOTE_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("UAT_DEMAND_NOTE_PDF_URL");
		}else if (portNumber == Long.valueOf(MetadataId.UAT_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("UAT_DEMAND_NOTE_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("UAT_DEMAND_NOTE_PDF_URL");
		} else if (portNumber == Long.valueOf(MetadataId.CUG_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("CUG_DEMAND_NOTE_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("CUG_DEMAND_NOTE_PDF_URL");
		}else if (portNumber == Long.valueOf(MetadataId.HTTPS_CUG_PORT_NUMBER.getName())) {
			demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("CUG_DEMAND_NOTE_PDF_PATH");
			demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("CUG_DEMAND_NOTE_PDF_URL");
		} else if (portNumber == Long.valueOf(MetadataId.LIVE_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("LIVE_DEMAND_NOTE_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("LIVE_DEMAND_NOTE_PDF_URL");
		} else if (portNumber == Long.valueOf(MetadataId.HTTPS_LIVE_PORT_NUMBER.getName())) {
			demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("LIVE_DEMAND_NOTE_PDF_PATH");
			demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("LIVE_DEMAND_NOTE_PDF_URL");
		} else {
			log.error("Path missing to save the File. Please contact to Support Team");
			throw new EmployeeFinancialServiceException("Path missing to save the File, Please contact to Support Team.");
		}
		
		StringBuilder filePath = new StringBuilder(demandNotePdfFilePath)
				.append("/").append(demandNoteGeneratorInfo.getSiteId())
				.append(!isGenerateDemandNote?"":"/"+demandNoteGeneratorInfo.getFlatBookingId())
				.append(!isGenerateDemandNote?"":"/"+demandNoteGeneratorInfo.getFinBokFomDmdNoteId());
		
		StringBuilder fileLocation = new StringBuilder(demandNotePdfFileUrl)
				.append("/").append(demandNoteGeneratorInfo.getSiteId())
				.append(!isGenerateDemandNote?"":"/"+demandNoteGeneratorInfo.getFlatBookingId())
				.append(!isGenerateDemandNote?"":"/"+demandNoteGeneratorInfo.getFinBokFomDmdNoteId());
		
		
		String fileName = getFileName(filePath.toString(), demandNoteGeneratorInfo.getDemandNotePdfFileName());
		StringBuilder pdfFileName = new StringBuilder(filePath).append("/").append(fileName);
		final Document document = new Document(PageSize.A4, 25, 25, 30, 30);
		
		final PdfWriter pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(pdfFileName.toString()));
		
		/* for output Response */
		fileInfo.setName(fileName);
		fileInfo.setUrl(fileLocation.append("/").append(fileName).toString());
		fileInfo.setFilePath(pdfFileName.toString());
		
		document.open();
		document.addAuthor("Amaravadhis software services private limited");
		document.addCreator("Aniket Chavan");
		document.addSubject(" Customer financial details ");
		document.addCreationDate();
		document.addTitle(" Customer financial details ");
		final com.itextpdf.tool.xml.XMLWorkerHelper worker = com.itextpdf.tool.xml.XMLWorkerHelper.getInstance();
		
		email.setTemplateName("/demandnotes/financialCustomerDetails.vm");
		
		Map<String, Object> model = new HashMap<String, Object>();
		email.setRequestUrl(employeeFinancialServiceInfo.getRequestUrl());
		model.put("email", email);
        model.put("requestUrl", employeeFinancialServiceInfo.getRequestUrl());
        final String data = mailServiceImpl.geVelocityTemplateContent(model);
        
       /* String Footer = "";
        org.jsoup.nodes.Document htmlData = Jsoup.parse(data);
        
        for (org.jsoup.nodes.Element table : htmlData.select("[class=foooter]")) {
				Footer = table.html();table.toString();table.text();table.data();table.val();
				table.remove();
				table.append("Hii");
        }    
        System.out.println(Footer);

    	StringBuffer sb = new StringBuffer(); // constructs a string buffer with no characters
*/		
		/*try {
			File file = new File(pdfFileName.toString()); // creates a new file instance
			FileReader fr = new FileReader(file); // reads the file
			BufferedReader br = new BufferedReader(new StringReader(Footer)); // creates a buffering character input stream
		
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line); // appends line to string buffer
				sb.append("\n"); // line feed
			}
			fr.close(); // closes the stream and release the resources
			System.out.println("Contents of File: ");
			System.out.println(sb.toString()); // returns a string that textually represents the object
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		//String str = "Tel: "+demandNoteGeneratorInfo.getCompanyTelephoneNo()+" CIN: "+demandNoteGeneratorInfo.getCompanyCin()+" GSTIN: "+demandNoteGeneratorInfo.getCompanyGstin()+" E-mail: "+demandNoteGeneratorInfo.getCompanyEmail()+" Website: "+demandNoteGeneratorInfo.getCompanyWebsite();
			/*pdfWriter.setPageEvent(new HeaderFooterPageEvent2("Header1",demandNoteGeneratorInfo.getCompanyName().toString(),10l));
			pdfWriter.setPageEvent(new HeaderFooterPageEvent2("Header2",demandNoteGeneratorInfo.getCompanyBillingAddress(),20l));*/
			//pdfWriter.setPageEvent(new HeaderFooterPageEvent());
			
	        //for header third parameter
	        //for footer fourth parameter
	        /*pdfWriter.setPageEvent(new HeaderFooterPageEvent2("======================================================================================================================================================"
	        		,"======================================================================================================================================================","",5l,35l,8l));
			pdfWriter.setPageEvent(new HeaderFooterPageEvent2("Sumadhura infracon pvt.Ltd","Sumadhura infracon pvt.Ltd","",10l,50l,8l));
			pdfWriter.setPageEvent(new HeaderFooterPageEvent2("MIS","MIS","",20l,40l,7l));
			pdfWriter.setPageEvent(new HeaderFooterPageEvent2(str,str,"",30l,30l,7l));*/
						
			worker.parseXHtml(pdfWriter, document, new StringReader(data));
	        
		document.close();
		log.info("file  generated \n"+fileInfo.getFilePath());
		return fileInfo;
	}
		
	public FileInfo CSVWorkerHelper(Email email, String requestFrom, EmployeeFinancialServiceInfo employeeFinancialServiceInfo) throws FileNotFoundException, IOException, DocumentException, Exception {
		log.info(" ***** control is inside the XMLWorkerHelper in EmployeeFinancialServiceImpl ***** = "+requestFrom);
		FileInfo fileInfo = new FileInfo();
		String demandNotePdfFilePath = ""; 
		String demandNotePdfFileUrl = "";
		boolean isGenerateDemandNote = requestFrom.equalsIgnoreCase("GenerateDemandNote");
		DemandNoteGeneratorInfo demandNoteGeneratorInfo = email.getDemandNoteGeneratorInfoList().get(0);
		long portNumber = employeeFinancialServiceInfo.getPortNumber();

		if (portNumber == Long.valueOf(MetadataId.LOCAL_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("UAT_DEMAND_NOTE_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("UAT_DEMAND_NOTE_PDF_URL");
		}else if (portNumber == Long.valueOf(MetadataId.UAT_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("UAT_DEMAND_NOTE_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("UAT_DEMAND_NOTE_PDF_URL");
		} else if (portNumber == Long.valueOf(MetadataId.CUG_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("CUG_DEMAND_NOTE_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("CUG_DEMAND_NOTE_PDF_URL");
		}else if (portNumber == Long.valueOf(MetadataId.HTTPS_CUG_PORT_NUMBER.getName())) {
			demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("CUG_DEMAND_NOTE_PDF_PATH");
			demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("CUG_DEMAND_NOTE_PDF_URL");
		} else if (portNumber == Long.valueOf(MetadataId.LIVE_PORT_NUMBER.getName())) {
				demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("LIVE_DEMAND_NOTE_PDF_PATH");
				demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("LIVE_DEMAND_NOTE_PDF_URL");
		} else if (portNumber == Long.valueOf(MetadataId.HTTPS_LIVE_PORT_NUMBER.getName())) {
			demandNotePdfFilePath = responceCodesUtil.getApplicationNamePropeties("LIVE_DEMAND_NOTE_PDF_PATH");
			demandNotePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("LIVE_DEMAND_NOTE_PDF_URL");
		} else {
			log.error("Path missing to save the File. Please contact to Support Team");
			throw new EmployeeFinancialServiceException("Path missing to save the File, Please contact to Support Team.");
		}
		
		StringBuilder filePath = new StringBuilder(demandNotePdfFilePath)
				.append("/").append(demandNoteGeneratorInfo.getSiteId())
				.append(!isGenerateDemandNote?"":"/"+demandNoteGeneratorInfo.getFlatBookingId())
				.append(!isGenerateDemandNote?"":"/"+demandNoteGeneratorInfo.getFinBokFomDmdNoteId());
		
		StringBuilder fileLocation = new StringBuilder(demandNotePdfFileUrl)
				.append("/").append(demandNoteGeneratorInfo.getSiteId())
				.append(!isGenerateDemandNote?"":"/"+demandNoteGeneratorInfo.getFlatBookingId())
				.append(!isGenerateDemandNote?"":"/"+demandNoteGeneratorInfo.getFinBokFomDmdNoteId());
		
		
		String fileName = getFileName(filePath.toString(), demandNoteGeneratorInfo.getDemandNotePdfFileName());
		StringBuilder pdfFileName = new StringBuilder(filePath).append("/").append(fileName);
		final Document document = new Document(PageSize.LEGAL);
		
		final PdfWriter pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(pdfFileName.toString()));
		
		fileInfo.setName(fileName);
		fileInfo.setUrl(fileLocation.append("/").append(fileName).toString());
		fileInfo.setFilePath(pdfFileName.toString());
		
		document.open();
		document.addAuthor("Amaravadhis software services private limited");
		document.addCreator("Aniket Chavan");
		document.addSubject(" Customer financial details ");
		document.addCreationDate();
		document.addTitle(" Customer financial details ");
		final com.itextpdf.tool.xml.XMLWorkerHelper worker = com.itextpdf.tool.xml.XMLWorkerHelper.getInstance();
		
		email.setTemplateName("/demandnotes/financialCustomerDetailsCSV.vm");
		
		Map<String, Object> model = new HashMap<String, Object>();
        model.put("email", email);
        model.put("DELIMETER", ",");
        model.put("NEWLINE", "\n");
        model.put("requestUrl", employeeFinancialServiceInfo.getRequestUrl());
        /*FileWriter writer = new FileWriter(new File("feed.csv"));
        VelocityContext context = new VelocityContext();
        
        // inject the objects to be used in template
        context.put("email", email);
        context.put("DELIMETER", ",");
        context.put("NEWLINE", "\n");
        Velocity.init();
        Template template = Velocity.getTemplate("H:\\ACP\\Project\\1 Live Project\\employeeservice\\src\\main\\resources\\demandnotes\\financialCustomerDetailsCSV.vm");
        template.merge(context, writer);
        writer.flush();
        writer.close();*/
        
        
        final String data = mailServiceImpl.geVelocityTemplateContent(model);
		
		worker.parseXHtml(pdfWriter, document, new StringReader(data));
		document.close();
		return fileInfo;
	
	}
	
	private void getInterestLetterFileName(DemandNoteGeneratorInfo demandNoteGeneratorInfo) {
		log.info(" ***** Control inside getInterestLetterFileName in PdfHelper ***** ");
		String currentDate = TimeUtil.getTimeInDD_MM_YYYY(new Date());
		String pdfFileName = demandNoteGeneratorInfo.getFlatName()+"_"+demandNoteGeneratorInfo.getSiteName()+"_Interest Letter"+"_"+currentDate+".pdf";
		demandNoteGeneratorInfo.setDemandNotePdfFileName(pdfFileName);
		demandNoteGeneratorInfo.setDemandNoteGeneratedDate(currentDate);
	}

	public Map<String, String> getThePath(long portNumber,String filePath,String fileUrl) throws FileNotFoundException, IOException{
		Map<String, String> map = new HashMap<>();
		String path = "";
		String url = "";
		
		/*if (portNumber == Long.valueOf(MetadataId.LOCAL_PORT_NUMBER.getName())) {
			path = responceCodesUtil.getApplicationNamePropeties(filePath);
			url = responceCodesUtil.getApplicationNamePropeties(fileUrl);
		}else if (portNumber == Long.valueOf(MetadataId.UAT_PORT_NUMBER.getName())) {
			path = responceCodesUtil.getApplicationNamePropeties(filePath);
			url = responceCodesUtil.getApplicationNamePropeties(fileUrl);
		} else if (portNumber == Long.valueOf(MetadataId.CUG_PORT_NUMBER.getName())) {
			path = responceCodesUtil.getApplicationNamePropeties(filePath);
			url = responceCodesUtil.getApplicationNamePropeties(fileUrl);
		} else if (portNumber == Long.valueOf(MetadataId.LIVE_PORT_NUMBER.getName())) {
			path = responceCodesUtil.getApplicationNamePropeties(filePath);
			url = responceCodesUtil.getApplicationNamePropeties(fileUrl);
		}*/
		
		map.put("path", path);
		map.put("url", url);
		return map;
	}
	
	/**
	 * Note this method is used for all types of receipt if any changes happened in code need to test all the reciep
	 */
	public void XMLWorkerHelperForReceipt(Email email, FileInfo fileInfo, EmployeeFinancialTransactionServiceInfo transactionServiceInfo, String metaDataName) 
			throws FileNotFoundException, IOException, DocumentException, Exception {
		log.info(" ***** Control inside PdfHelper.XMLWorkerHelperForReceipt() ***** ");
		String transactionReceiptPdfFilePath = "";
		String transactionGetReceiptFileURL = "";
		long bookingFormId = 0l;
		boolean isThisUploadedData = transactionServiceInfo.isThisUplaodedData();
		//try {
		long portNumber = transactionServiceInfo.getPortNumber();
		DemandNoteGeneratorInfo demandNoteInfoObject = email.getDemandNoteGeneratorInfoList().get(0);
		bookingFormId = demandNoteInfoObject.getBookingFormId();
		log.info(" ***** PdfHelper.XMLWorkerHelperForReceipt() ***** PORT number : "+portNumber+"\t Booking Form Id :"+bookingFormId);
		//EmployeeFinancialTransactionServiceInfo serviceInfo = new EmployeeFinancialTransactionServiceInfo();
			
			if (portNumber == Long.valueOf(MetadataId.LOCAL_PORT_NUMBER.getName())) {
				transactionReceiptPdfFilePath =responceCodesUtil.getApplicationNamePropeties("UAT_TRANSACTION_RECEIPT_PDF_PATH");
				transactionGetReceiptFileURL = responceCodesUtil.getApplicationNamePropeties("UAT_TRANSACTION_RECEIPT_PDF_URL");
			}else if (portNumber == Long.valueOf(MetadataId.UAT_PORT_NUMBER.getName())) {
				transactionReceiptPdfFilePath =responceCodesUtil.getApplicationNamePropeties("UAT_TRANSACTION_RECEIPT_PDF_PATH");
				transactionGetReceiptFileURL = responceCodesUtil.getApplicationNamePropeties("UAT_TRANSACTION_RECEIPT_PDF_URL");
			} else if (portNumber == Long.valueOf(MetadataId.CUG_PORT_NUMBER.getName())) {
				transactionReceiptPdfFilePath =responceCodesUtil.getApplicationNamePropeties("CUG_TRANSACTION_RECEIPT_PDF_PATH");
				transactionGetReceiptFileURL = responceCodesUtil.getApplicationNamePropeties("CUG_TRANSACTION_RECEIPT_PDF_URL");
			}else if (portNumber == Long.valueOf(MetadataId.HTTPS_CUG_PORT_NUMBER.getName())) {
				transactionReceiptPdfFilePath =responceCodesUtil.getApplicationNamePropeties("CUG_TRANSACTION_RECEIPT_PDF_PATH");
				transactionGetReceiptFileURL = responceCodesUtil.getApplicationNamePropeties("CUG_TRANSACTION_RECEIPT_PDF_URL");
			} else if (portNumber == Long.valueOf(MetadataId.LIVE_PORT_NUMBER.getName())) {
				transactionReceiptPdfFilePath =responceCodesUtil.getApplicationNamePropeties("LIVE_TRANSACTION_RECEIPT_PDF_PATH");
				transactionGetReceiptFileURL = responceCodesUtil.getApplicationNamePropeties("LIVE_TRANSACTION_RECEIPT_PDF_URL");
			}  else if (portNumber == Long.valueOf(MetadataId.HTTPS_LIVE_PORT_NUMBER.getName())) {
				transactionReceiptPdfFilePath =responceCodesUtil.getApplicationNamePropeties("LIVE_TRANSACTION_RECEIPT_PDF_PATH");
				transactionGetReceiptFileURL = responceCodesUtil.getApplicationNamePropeties("LIVE_TRANSACTION_RECEIPT_PDF_URL");
			}else {
				log.error("Path missing to save the File. Please contact to Support Team");
				throw new EmployeeFinancialServiceException("Receipt drive path missing to save the File, Please contact to Support Team.");
			}
			
			//final Document document = new Document(PageSize.A4);
			//final Document document = new Document(PageSize.A4, 25, 25, 30, 30);
			final Document document = new Document(PageSize.A4, 25, 25, 100, 80);//this is  for footer and header
			String fileName = getFileName(transactionReceiptPdfFilePath+transactionServiceInfo.getSiteId()+"/"+bookingFormId+"/"+demandNoteInfoObject.getTransactionEntryId()+"/", email.getDemandNoteGeneratorInfoList().get(0).getDemandNotePdfFileName());
			StringBuilder pdfFileName = new StringBuilder(transactionReceiptPdfFilePath)
					.append(transactionServiceInfo.getSiteId())
					.append("/").append(bookingFormId)
					.append("/").append(demandNoteInfoObject.getTransactionEntryId())
					.append("/").append(fileName);
			
			StringBuilder urlPath = new StringBuilder(transactionGetReceiptFileURL)
					.append(transactionServiceInfo.getSiteId())
					.append("/").append(bookingFormId)
					.append("/").append(demandNoteInfoObject.getTransactionEntryId())
					.append("/").append(fileName);
			
			fileInfo.setName(fileName);
			fileInfo.setUrl(urlPath.toString());
			fileInfo.setFilePath(pdfFileName.toString());
			final PdfWriter pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(pdfFileName.toString()));

			USER_PASS = email.getDemandNoteGeneratorInfoList().get(0).getPancard();
			OWNER_PASS = email.getDemandNoteGeneratorInfoList().get(0).getPancard();

			//adding password to the pdf.
			//pdfWriter.setEncryption(USER_PASS.getBytes(), OWNER_PASS.getBytes(), PdfWriter.ALLOW_PRINTING,PdfWriter.ENCRYPTION_AES_128);
			
			document.open();
			document.addAuthor("Amaravadhis software services private limited");
			document.addCreator("Aniket Chavan");
			document.addSubject(" Receipt ");
			document.addCreationDate();
			document.addTitle(" Receipt ");
			
			String headData = "";
			String footerData = "===";
			String rightLogo = Util.isNotEmptyObject(demandNoteInfoObject.getRightSidelogoFilePath())? demandNoteInfoObject.getRightSidelogoFilePath() : demandNoteInfoObject.getRightSidelogoForPdf();
			String leftLogo = Util.isNotEmptyObject(demandNoteInfoObject.getLeftSidelogoFilePath())? demandNoteInfoObject.getLeftSidelogoFilePath() : demandNoteInfoObject.getLeftSidelogoForPdf();

			Long headerTextHeight = 5l;
			Long footerTextHeight = 60l;
			Long fontSize = 7l;

			HeaderFooterPageEvent2 logo = new HeaderFooterPageEvent2();
			logo.setHeadData(headData);
			logo.setFooterData(footerData);
			logo.setRightLogo(rightLogo);
			logo.setLeftLogo(leftLogo);
			logo.setHeaderTextHeight(headerTextHeight);
			logo.setFooterTextHeight(footerTextHeight);
			logo.setFontSize(fontSize);
			
	        /*String footerAdderss = "";
	        
	        if(demandNoteInfoObject.getCompanyCin() != "-"){
	        	footerAdderss = "Tel: "+demandNoteInfoObject.getCompanyTelephoneNo()+" CIN: "+demandNoteInfoObject.getCompanyCin()+" GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" E-mail: "+demandNoteInfoObject.getCompanyEmail()+" Website: "+demandNoteInfoObject.getCompanyWebsite();
	        } else {
	        	footerAdderss = "Tel: "+demandNoteInfoObject.getCompanyTelephoneNo()+" LLPIN: "+demandNoteInfoObject.getCompanyLlpin()+" GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" E-mail: "+demandNoteInfoObject.getCompanyEmail()+" Website: "+demandNoteInfoObject.getCompanyWebsite();
	        }*/
	        
	        //for header third parameter "abc"
	        //for footer fourth parameter
	        pdfWriter.setPageEvent(logo);
	        //pdfWriter.setPageEvent(new HeaderFooterPageEvent2("","===",demandNoteInfoObject.getRightSidelogoForPdf(),5l,60l,7l));
			pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",demandNoteInfoObject.getCompanyName(),"",10l,50l,8l));
			pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",demandNoteInfoObject.getCompanyBillingAddress(),"",20l,40l,7l));
			pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",getFooterAddress(demandNoteInfoObject),"",30l,30l,7l));
			log.info("\n"+fileInfo.getFilePath()+"\t"+fileInfo.getUrl()+"\n rightLogo : "+rightLogo);

			final com.itextpdf.tool.xml.XMLWorkerHelper worker = com.itextpdf.tool.xml.XMLWorkerHelper.getInstance();
			
			if(transactionServiceInfo.isThisUplaodedData()) {
				email.setTemplateName("/demandnotes/receipt.vm");
				if(demandNoteInfoObject.getFinSchemeType()!=null && demandNoteInfoObject.getFinSchemeType().trim().equalsIgnoreCase("Special Offer")) {
					email.setTemplateName("/demandnotes/receiptSpecialOffer%GST.vm");
				}				

			} else if(metaDataName.equalsIgnoreCase(MetadataId.FIN_BOOKING_FORM_MILESTONES.getName())) {
				//IMP code for new template
				/*//email.setTemplateName("/demandnotes/receipt.vm");old format
				//email.setTemplateName("/demandnotes/PaymentReceipt.vm");
				email.setTemplateName("/demandnotes/PaymentReceiptNew3.0.vm");//new format
				if(demandNoteInfoObject.getFinSchemeType()!=null && demandNoteInfoObject.getFinSchemeType().trim().equalsIgnoreCase("Special Offer")) {
					email.setTemplateName("/demandnotes/PaymentReceiptSpecialOffer%GST.vm");
				}*/

				
				email.setTemplateName("/demandnotes/receipt.vm");
				//email.setTemplateName("/demandnotes/PaymentReceipt.vm");//not in use
				//email.setTemplateName("/demandnotes/PaymentReceiptNew3.0.vm");//new format
				if(demandNoteInfoObject.getFinSchemeType()!=null && demandNoteInfoObject.getFinSchemeType().trim().equalsIgnoreCase("Special Offer")) {
					email.setTemplateName("/demandnotes/receiptSpecialOffer%GST.vm");//old format
					//email.setTemplateName("/demandnotes/PaymentReceiptSpecialOffer%GST.vm");//new 
				}
				
/*		        String footerAdderss = "";
		        
		        if(demandNoteInfoObject.getCompanyCin() != "-"){
		        	footerAdderss = "Tel: "+demandNoteInfoObject.getCompanyTelephoneNo()+" CIN: "+demandNoteInfoObject.getCompanyCin()+" GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" E-mail: "+demandNoteInfoObject.getCompanyEmail()+" Website: "+demandNoteInfoObject.getCompanyWebsite();
		        } else {
		        	footerAdderss = "Tel: "+demandNoteInfoObject.getCompanyTelephoneNo()+" LLPIN: "+demandNoteInfoObject.getCompanyLlpin()+" GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" E-mail: "+demandNoteInfoObject.getCompanyEmail()+" Website: "+demandNoteInfoObject.getCompanyWebsite();
		        }
		        
		        //for header third parameter "abc"
		        //for footer fourth parameter
		        pdfWriter.setPageEvent(logo);
		        //pdfWriter.setPageEvent(new HeaderFooterPageEvent2("","===",demandNoteInfoObject.getRightSidelogoForPdf(),5l,60l,7l));
				pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",demandNoteInfoObject.getCompanyName(),"",10l,50l,8l));
				pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",demandNoteInfoObject.getCompanyBillingAddress(),"",20l,40l,7l));
				pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",footerAdderss,"",30l,30l,7l));
*/
				
			} else if(metaDataName.equalsIgnoreCase(MetadataId.MODIFICATION_COST.getName())) {
				email.setTemplateName("/demandnotes/Modification_Charges_Receipt.vm");
			} else if(metaDataName.equalsIgnoreCase(MetadataId.LEGAL_COST.getName())) {
				email.setTemplateName("/demandnotes/Legal_Charges_Receipt.vm");
			
	     	} else if(metaDataName.equalsIgnoreCase(MetadataId.FIN_PENALTY.getName())) {
			/* Malladi Changes */
				email.setTemplateName("/demandnotes/Interest_Charges_Receipt.vm");
	     	} else if(metaDataName.equalsIgnoreCase(MetadataId.MAINTENANCE_CHARGE.getName())) {
	     		email.setTemplateName("/demandnotes/Maintenance_Charges_Receipt.vm");
	     	} else if(metaDataName.equalsIgnoreCase(MetadataId.INDIVIDUAL_FLAT_KHATA_BIFURCATION_AND_OTHER_CHARGES.getName())) {
	     		email.setTemplateName("/demandnotes/Maintenance_Charges_Receipt.vm");
	     	} else if(metaDataName.equalsIgnoreCase(MetadataId.CORPUS_FUND.getName())) {
	     		email.setTemplateName("/demandnotes/Maintenance_Charges_Receipt.vm");
	     	}//using same template for maintenance, corpus and flat khata
			
			Map<String, Object> model = new HashMap<String, Object>();
	        model.put("email", email);
	        final String data = mailServiceImpl.geVelocityTemplateContent(model);
	        
	        if(isThisUploadedData) {
				ExecutorService executorService = Executors.newFixedThreadPool(5);
				try { 
					executorService.execute(new Runnable() {
						public void run() {
							try {
								worker.parseXHtml(pdfWriter, document, new StringReader(data));
					        	document.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					});
					executorService.shutdown();
				} catch (Exception e) {
					e.printStackTrace();
					executorService.shutdown();
					log.error("Failed to store file in drive, Please contact to Support Team.");
					throw new EmployeeFinancialServiceException("Failed to store file in drive, Please contact to Support Team.");
				}
	        } else {
	        	worker.parseXHtml(pdfWriter, document, new StringReader(data));
	        	document.close();
	        }
	        
	        log.info(" Control end of the file and file receipt generated \n"+fileInfo.getFilePath());

	}
	
	 public static void main(String[] args) throws Exception {
		 System.out.println("***** Control inside the PdfHelper.main() *****");
		
		new PdfHelper().checkTemplates(new Email(), new FileInfo());
		 
		 Email email = new Email();
		
		
		FileInfo fileInfo = new FileInfo();
		EmployeeFinancialServiceInfo transactionServiceInfo = new EmployeeFinancialServiceInfo();
		String metaDataName = "";
		transactionServiceInfo.setPortNumber(9999l);
		transactionServiceInfo.setBookingFormId(84l);
		transactionServiceInfo.setSiteId(111l);
		FinancialConsolidatedReceiptPdfInfo financialConsolidatedReceiptPdfInfo = new FinancialConsolidatedReceiptPdfInfo();
		financialConsolidatedReceiptPdfInfo.setConsolidatedPdfFileName("Statement of Account.pdf");
		financialConsolidatedReceiptPdfInfo.setBookingFormId(84l);
		financialConsolidatedReceiptPdfInfo.setCompanyBillingAddress("fassfs fasf safdasdf safd sadf asf sad fdasf as fsad fdsa fd sdaf dasfd");
		financialConsolidatedReceiptPdfInfo.setCompanyName("KJKLJ fdsaf asfas fas fas fads fsad ");
		financialConsolidatedReceiptPdfInfo.setCompanyCin("DFAS");
		financialConsolidatedReceiptPdfInfo.setCompanyEmail("SADSDDDAS");
		financialConsolidatedReceiptPdfInfo.setCompanyGstin("FDASASFADFADF");
		financialConsolidatedReceiptPdfInfo.setCompanyWebsite("FSSdsADD");
		email.setConsolidatedReceiptPdfInfos(Arrays.asList(financialConsolidatedReceiptPdfInfo));
		new PdfHelper().XMLWorkerHelperForConsolidatedReceipt(email, fileInfo, transactionServiceInfo, metaDataName);
	}
	
	public void XMLWorkerHelperForConsolidatedReceipt(Email email, FileInfo fileInfo, EmployeeFinancialServiceInfo transactionServiceInfo
			, String metaDataName) throws FileNotFoundException, IOException, DocumentException, Exception {
		log.info("***** Control inside the PdfHelper.XMLWorkerHelperForConsolidatedReceipt() *****");
		String transactionReceiptPdfFilePath = "";
		String transactionGetReceiptFileURL = "";
		long bookingFormId = 0l;
		boolean isThisUploadedData =  transactionServiceInfo.isThisUplaodedData();
		long portNumber = transactionServiceInfo.getPortNumber() == null ?9999l:transactionServiceInfo.getPortNumber();
		FinancialConsolidatedReceiptPdfInfo consolidatedReceiptPdfInfo = email.getConsolidatedReceiptPdfInfos().get(0);
		String consolidatedPdfFileName = consolidatedReceiptPdfInfo.getConsolidatedPdfFileName();
		bookingFormId = consolidatedReceiptPdfInfo.getBookingFormId();
		log.info(" ***** PdfHelper.XMLWorkerHelperForReceipt() ***** PORT number : "+portNumber+"\t Booking Form Id :"+bookingFormId);
			
			if (portNumber == Long.valueOf(MetadataId.LOCAL_PORT_NUMBER.getName())) {
				transactionReceiptPdfFilePath =responceCodesUtil.getApplicationNamePropeties("UAT_CONSOLIDATED_RECEIPT_PDF_PATH");
				transactionGetReceiptFileURL = responceCodesUtil.getApplicationNamePropeties("UAT_CONSOLIDATED_RECEIPT_PDF_URL");
			}else if (portNumber == Long.valueOf(MetadataId.UAT_PORT_NUMBER.getName())) {
				transactionReceiptPdfFilePath =responceCodesUtil.getApplicationNamePropeties("UAT_CONSOLIDATED_RECEIPT_PDF_PATH");
				transactionGetReceiptFileURL = responceCodesUtil.getApplicationNamePropeties("UAT_CONSOLIDATED_RECEIPT_PDF_URL");
			} else if (portNumber == Long.valueOf(MetadataId.CUG_PORT_NUMBER.getName())) {
				transactionReceiptPdfFilePath =responceCodesUtil.getApplicationNamePropeties("CUG_CONSOLIDATED_RECEIPT_PDF_PATH");
				transactionGetReceiptFileURL = responceCodesUtil.getApplicationNamePropeties("CUG_CONSOLIDATED_RECEIPT_PDF_URL");
			}else if (portNumber == Long.valueOf(MetadataId.HTTPS_CUG_PORT_NUMBER.getName())) {
				transactionReceiptPdfFilePath =responceCodesUtil.getApplicationNamePropeties("CUG_CONSOLIDATED_RECEIPT_PDF_PATH");
				transactionGetReceiptFileURL = responceCodesUtil.getApplicationNamePropeties("CUG_CONSOLIDATED_RECEIPT_PDF_URL");
			} else if (portNumber == Long.valueOf(MetadataId.LIVE_PORT_NUMBER.getName())) {
				transactionReceiptPdfFilePath =responceCodesUtil.getApplicationNamePropeties("LIVE_CONSOLIDATED_RECEIPT_PDF_PATH");
				transactionGetReceiptFileURL = responceCodesUtil.getApplicationNamePropeties("LIVE_CONSOLIDATED_RECEIPT_PDF_URL");
			} else if (portNumber == Long.valueOf(MetadataId.HTTPS_LIVE_PORT_NUMBER.getName())) {
				transactionReceiptPdfFilePath =responceCodesUtil.getApplicationNamePropeties("LIVE_CONSOLIDATED_RECEIPT_PDF_PATH");
				transactionGetReceiptFileURL = responceCodesUtil.getApplicationNamePropeties("LIVE_CONSOLIDATED_RECEIPT_PDF_URL");
			} else {
				log.error("Path missing to save the File. Please contact to Support Team");
				throw new EmployeeFinancialServiceException("Path missing to save the File, Please contact to Support Team.");
			}
			
/*			transactionReceiptPdfFilePath = "D:\\CUSTOMERAPP_UAT\\images\\sumadhura_projects_images\\Financial_Transaction\\Consolidated_Receipt\\";
			transactionGetReceiptFileURL = "";
*/			//final Document document = new Document(PageSize.A4, 25, 25, 30, 80);//this only for footer
			final Document document = new Document(PageSize.A4, 25, 25, 100, 80);//this is  for footer and header
			
			String path = transactionReceiptPdfFilePath+transactionServiceInfo.getSiteId()+"/"+bookingFormId+"/";
			File f = new File(path);
			if(f.exists()) {
				boolean delete = f.delete();
				System.out.println(delete);
				deleteFolder(f);
			}
			String fileName = getFileName(path, consolidatedPdfFileName);
			
			StringBuilder pdfFileName = new StringBuilder(transactionReceiptPdfFilePath)
					.append(transactionServiceInfo.getSiteId())
					.append("/").append(bookingFormId)
					//.append("/").append(demandNoteInfoObject.getTransactionEntryId())
					.append("/").append(fileName);
			
			StringBuilder urlPath = new StringBuilder(transactionGetReceiptFileURL)
					.append(transactionServiceInfo.getSiteId())
					.append("/").append(bookingFormId)
					//.append("/").append(demandNoteInfoObject.getTransactionEntryId())
					.append("/").append(fileName);
			
			fileInfo.setName(fileName);
			fileInfo.setUrl(urlPath.toString());
			fileInfo.setFilePath(pdfFileName.toString());
			log.info("\n"+pdfFileName+"\t"+urlPath);
			final PdfWriter pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(pdfFileName.toString()));

			document.open();
			document.addAuthor("Amaravadhis software services private limited");
			document.addCreator("Aniket Chavan");
			document.addSubject(" Consolidated Receipt ");
			document.addCreationDate();
			document.addTitle(" Consolidated Receipt ");
			
			
			String headData = "";
			String footerData = "===";
/*			String rightLogo = consolidatedReceiptPdfInfo.getRightSidelogoForPdf();
			String leftLogo =  consolidatedReceiptPdfInfo.getLeftSidelogoForPdf();
*/			
			String rightLogo = Util.isNotEmptyObject(consolidatedReceiptPdfInfo.getRightSidelogoFilePath())? consolidatedReceiptPdfInfo.getRightSidelogoFilePath() : consolidatedReceiptPdfInfo.getRightSidelogoForPdf();
			String leftLogo = Util.isNotEmptyObject(consolidatedReceiptPdfInfo.getLeftSidelogoFilePath())? consolidatedReceiptPdfInfo.getLeftSidelogoFilePath() : consolidatedReceiptPdfInfo.getLeftSidelogoForPdf();
			
			Long headerTextHeight = 5l;
			Long footerTextHeight = 60l;
			Long fontSize = 7l;

			HeaderFooterPageEvent2 logo = new HeaderFooterPageEvent2();
			logo.setHeadData(headData);
			logo.setFooterData(footerData);
			logo.setRightLogo(rightLogo);
			logo.setLeftLogo(leftLogo);
			logo.setHeaderTextHeight(headerTextHeight);
			logo.setFooterTextHeight(footerTextHeight);
			logo.setFontSize(fontSize);
			
			final com.itextpdf.tool.xml.XMLWorkerHelper worker = com.itextpdf.tool.xml.XMLWorkerHelper.getInstance();

			email.setTemplateName("/demandnotes/consolidated.vm");
			
			Map<String, Object> model = new HashMap<String, Object>();
	        model.put("email", email);
	        final String data = mailServiceImpl.geVelocityTemplateContent(model);
	        //final String data = "<html><body>Hii<br/>How are you?<br/>Fine.<br/> ********************************************************************* \\n</body></html>";
	        log.info(" ********************************************************************* \n"+data);
	        
 	        Rectangle headerBox = new Rectangle(36, 54, 559, 788);
	        pdfWriter.setBoxSize("headerBox", headerBox);
		    
	        String footerAdderss = "";
	        
	        if(consolidatedReceiptPdfInfo.getCompanyCin() != "-"){
	        	footerAdderss = "Tel: "+consolidatedReceiptPdfInfo.getCompanyTelephoneNo()+" CIN: "+consolidatedReceiptPdfInfo.getCompanyCin()+" GSTIN: "+consolidatedReceiptPdfInfo.getCompanyGstin()+" E-mail: "+consolidatedReceiptPdfInfo.getCompanyEmail()+" Website: "+consolidatedReceiptPdfInfo.getCompanyWebsite();
	        }else {
	        	footerAdderss = "Tel: "+consolidatedReceiptPdfInfo.getCompanyTelephoneNo()+" LLPIN: "+consolidatedReceiptPdfInfo.getCompanyLlpin()+" GSTIN: "+consolidatedReceiptPdfInfo.getCompanyGstin()+" E-mail: "+consolidatedReceiptPdfInfo.getCompanyEmail()+" Website: "+consolidatedReceiptPdfInfo.getCompanyWebsite();
	        }
	         
	        //for header third parameter "abc"
	        //for footer fourth parameter
	        //pdfWriter.setPageEvent(new HeaderFooterPageEvent2("","===",consolidatedReceiptPdfInfo.getLeftSidelogoForPdf(),5l,60l,7l));
	        pdfWriter.setPageEvent(logo);
			pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",consolidatedReceiptPdfInfo.getCompanyName(),"",10l,50l,8l));
			pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",consolidatedReceiptPdfInfo.getCompanyBillingAddress(),"",20l,40l,7l));
			pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",footerAdderss,"",30l,30l,7l));
			        
			
	        if(isThisUploadedData) {
				ExecutorService executorService = Executors.newFixedThreadPool(5);
				try {
					executorService.execute(new Runnable() {
						public void run() {
							try {
								worker.parseXHtml(pdfWriter, document, new StringReader(data));
					        	document.close();
					        	pdfWriter.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					});
					executorService.shutdown();
				} catch (Exception e) {
					e.printStackTrace();
					executorService.shutdown();
					log.error("Failed to store file in drive, Please contact to Support Team.");
					throw new EmployeeFinancialServiceException("Failed to store file in drive, Please contact to Support Team.");
				
				}
	        }else {
	        	worker.parseXHtml(pdfWriter, document, new StringReader(data));
	        	document.close();
	        	pdfWriter.close();
	        }
	        log.info("PDF Generated "+pdfFileName);
	}

	public void XMLWorkerHelperForAllotmentLetter(Email email, FileInfo fileInfo) throws Exception {
		log.info("***** Control inside the PdfHelper.XMLWorkerHelperForAllotmentLetter() *****");
		DemandNoteGeneratorInfo demandNoteInfoObject = email.getDemandNoteGeneratorInfo();
		String allotmentLetterPdfFileName = demandNoteInfoObject.getDemandNotePdfFileName();
		String transactionReceiptPdfFilePath = "";
		String transactionGetReceiptFileURL = "";
		long bookingFormId = demandNoteInfoObject.getBookingFormId();
		long portNumber = email.getPortNumber();
		log.info(" ***** PdfHelper.XMLWorkerHelperForAllotmentLetter() ***** PORT number : "+portNumber+"\t Booking Form Id :"+bookingFormId);
		//Properties prop= responceCodesUtil.getApplicationProperties();
		transactionReceiptPdfFilePath = demandNoteInfoObject.getFolderFilePath();
		transactionGetReceiptFileURL = demandNoteInfoObject.getFolderFileUrl();
/*		if (portNumber == Long.valueOf(MetadataId.LOCAL_PORT_NUMBER.getName())) {
			//transactionReceiptPdfFilePath = prop.getProperty("UAT_ALLOTMENT_LETTER_PDF_PATH");
			//transactionGetReceiptFileURL = prop.getProperty("UAT_ALLOTMENT_LETTER_PDF_URL");
			transactionReceiptPdfFilePath = prop.getProperty("DOCUMENTS_FILE_DIRECTORY_PATH");
			transactionGetReceiptFileURL = prop.getProperty("DOCUMENTS_FILE_URL_PATH");
		} else if (portNumber == Long.valueOf(MetadataId.UAT_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("DOCUMENTS_FILE_DIRECTORY_PATH");
			transactionGetReceiptFileURL = prop.getProperty("DOCUMENTS_FILE_URL_PATH");
		} else if (portNumber == Long.valueOf(MetadataId.CUG_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("CUG_ALLOTMENT_LETTER_PDF_PATH");
			transactionGetReceiptFileURL = prop.getProperty("CUG_ALLOTMENT_LETTER_PDF_URL");
		} else if (portNumber == Long.valueOf(MetadataId.LIVE_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("LIVE_ALLOTMENT_LETTER_PDF_PATH");
			transactionGetReceiptFileURL = prop.getProperty("LIVE_ALLOTMENT_LETTER_PDF_URL");
		} else {
			log.error("Path missing to save the File. Please contact to Support Team");
			throw new EmployeeFinancialServiceException("Path missing to save the File, Please contact to Support Team.");
		}
		if(transactionReceiptPdfFilePath==null || transactionReceiptPdfFilePath.isEmpty()) {
			throw new EmployeeFinancialServiceException("File path found empty for storing pdf's.");
		}*/
		final Document document;
		if (Util.isNotEmptyObject(demandNoteInfoObject.getSiteId()) && demandNoteInfoObject.getSiteId().equals("130")) {
			document = new Document(PageSize.A4, 25, 25, 100, 65);// this is for footer and header
		} else {
			document = new Document(PageSize.A4, 25, 25, 100, 80);// this is for footer and header
		}
	//	final Document document = new Document(PageSize.A4, 25, 25, 100, 80);// this is for footer and header

		String path = transactionReceiptPdfFilePath + demandNoteInfoObject.getSiteId() + "/" + bookingFormId + "/";
		String fileName = getFileName(path, allotmentLetterPdfFileName);
			
		StringBuilder pdfFileName = new StringBuilder(transactionReceiptPdfFilePath)
				.append(demandNoteInfoObject.getSiteId()).append("/").append(bookingFormId).append("/")
				.append(fileName);

		StringBuilder urlPath = new StringBuilder(transactionGetReceiptFileURL).append(demandNoteInfoObject.getSiteId())
				.append("/").append(bookingFormId).append("/").append(fileName);

		fileInfo.setName(fileName);
		fileInfo.setUrl(urlPath.toString());
		fileInfo.setFilePath(pdfFileName.toString());
		fileInfo.setExtension(pdfFileName.substring(pdfFileName.lastIndexOf(".") + 1));
		log.info(pdfFileName + "\t" + urlPath);
		final PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFileName.toString()));

		document.open();
		document.addAuthor("Amaravadhis software services private limited");
		document.addCreator("Aniket Chavan");
		document.addSubject(" Allotment Letter ");
		document.addCreationDate();
		document.addTitle(" Allotment Letter ");
		final com.itextpdf.tool.xml.XMLWorkerHelper worker = com.itextpdf.tool.xml.XMLWorkerHelper.getInstance();

		if (Util.isNotEmptyObject(demandNoteInfoObject.getSiteId()) && demandNoteInfoObject.getSiteId().equals("126")) {
			email.setTemplateName("/vmtemplates/ALLOTMENT_LETTER_Folium.vm");
		} else if(Util.isNotEmptyObject(demandNoteInfoObject.getSiteId()) && demandNoteInfoObject.getSiteId().equals("114")) {
			email.setTemplateName("/vmtemplates/ALLOTMENT_LETTER_HORIZON.vm");
		} else if(Util.isNotEmptyObject(demandNoteInfoObject.getSiteId()) && demandNoteInfoObject.getSiteId().equals("133")) {
			email.setTemplateName("/vmtemplates/ALLOTMENT_LETTER_HORIZON.vm");
		}else if(Util.isNotEmptyObject(demandNoteInfoObject.getSiteId()) && demandNoteInfoObject.getSiteId().equals("130")) {
			email.setTemplateName("/vmtemplates/ALLOTMENT_LETTER_SUSH_PHASE_2.vm");
		}else {
			email.setTemplateName("/vmtemplates/ALLOTMENT_LETTER.vm");
		}

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("email", email);
		final String data = mailServiceImpl.geVelocityTemplateContent(model);
		log.info(" ********************************************************************* \n" + data);

		Rectangle headerBox = new Rectangle(36, 54, 559, 788);
		pdfWriter.setBoxSize("headerBox", headerBox);

		String footerAdderss = "";
		if (Util.isNotEmptyObject(demandNoteInfoObject.getSiteId()) && demandNoteInfoObject.getSiteId().equals("126")) {
		    if (demandNoteInfoObject.getCompanyCin() != "-") {
		    	footerAdderss = " GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" CIN: "+demandNoteInfoObject.getCompanyCin()+" Website: "+demandNoteInfoObject.getCompanyWebsite();
	        } else {
	        	footerAdderss = " LLPIN: "+demandNoteInfoObject.getCompanyLlpin()+" GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" Website: "+demandNoteInfoObject.getCompanyWebsite();
	        }
		} else if (Util.isNotEmptyObject(demandNoteInfoObject.getSiteId()) && demandNoteInfoObject.getSiteId().equals("114")) {
		    if (demandNoteInfoObject.getCompanyCin() != "-") {
		    	footerAdderss = "GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" CIN: "+demandNoteInfoObject.getCompanyCin()+" URL: "+demandNoteInfoObject.getCompanyWebsite();
	        } else {
	        	footerAdderss = "LLPIN: "+demandNoteInfoObject.getCompanyLlpin()+" GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" URL: "+demandNoteInfoObject.getCompanyWebsite();
	        }
		} else if (Util.isNotEmptyObject(demandNoteInfoObject.getSiteId()) && demandNoteInfoObject.getSiteId().equals("130")) {
		    if (demandNoteInfoObject.getCompanyCin() != "-") {
		    	footerAdderss = "GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" CIN: "+demandNoteInfoObject.getCompanyCin()+" URL: "+demandNoteInfoObject.getCompanyWebsite();
	        } else {
	        	footerAdderss = "LLPIN: "+demandNoteInfoObject.getCompanyLlpin()+" GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" URL: "+demandNoteInfoObject.getCompanyWebsite();
	        }
		}
		else {
			 if (demandNoteInfoObject.getCompanyCin() != "-") {
			       	//footerAdderss = "Tel: "+demandNoteInfoObject.getCompanyTelephoneNo()+" CIN: "+demandNoteInfoObject.getCompanyCin()+" GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" E-mail: "+demandNoteInfoObject.getCompanyEmail()+" Website: "+demandNoteInfoObject.getCompanyWebsite();
				 	footerAdderss = getFooterAddress(demandNoteInfoObject);
			 } else {
			       	//footerAdderss = "Tel: "+demandNoteInfoObject.getCompanyTelephoneNo()+" LLPIN: "+demandNoteInfoObject.getCompanyLlpin()+" GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" E-mail: "+demandNoteInfoObject.getCompanyEmail()+" Website: "+demandNoteInfoObject.getCompanyWebsite();
				 	footerAdderss = getFooterAddress(demandNoteInfoObject);
			 }
		}
	        
		String headData = "";
		String footerData = "===";

		String rightLogo = Util.isNotEmptyObject(demandNoteInfoObject.getRightSidelogoFilePath())? demandNoteInfoObject.getRightSidelogoFilePath() : demandNoteInfoObject.getRightSidelogoForPdf();
		String leftLogo = Util.isNotEmptyObject(demandNoteInfoObject.getLeftSidelogoFilePath())? demandNoteInfoObject.getLeftSidelogoFilePath() : demandNoteInfoObject.getLeftSidelogoForPdf();

		Long headerTextHeight = 5l;
		Long footerTextHeight = 60l;
		Long fontSize = 7l;

		HeaderFooterPageEvent2 logo = new HeaderFooterPageEvent2();
		logo.setHeadData(headData);
		logo.setFooterData(footerData);
		logo.setRightLogo(rightLogo);
		logo.setLeftLogo(leftLogo);
		logo.setHeaderTextHeight(headerTextHeight);
		logo.setFooterTextHeight(footerTextHeight);
		logo.setFontSize(fontSize);

        pdfWriter.setPageEvent(logo);
        //pdfWriter.setPageEvent(new HeaderFooterPageEvent2("","===",demandNoteInfoObject.getRightSidelogoForPdf(),5l,60l,7l));
    	if (Util.isNotEmptyObject(demandNoteInfoObject.getSiteId()) && demandNoteInfoObject.getSiteId().equals("126")) {
			pdfWriter.setPageEvent(new HeaderFooterPageEvent2("","                                                                           "+demandNoteInfoObject.getCompanyName(),"",10l,50l,8l));
	        pdfWriter.setPageEvent(new HeaderFooterPageEvent2("   ","                   "+demandNoteInfoObject.getCompanyBillingAddress(),"",20l,40l,7l));
			pdfWriter.setPageEvent(new HeaderFooterPageEvent2("  ","                             "+footerAdderss,"",30l,30l,7l));	    	
    	} else if (Util.isNotEmptyObject(demandNoteInfoObject.getSiteId()) && demandNoteInfoObject.getSiteId().equals("130")) {
			pdfWriter.setPageEvent(new HeaderFooterPageEvent2("","                                                                           "+demandNoteInfoObject.getCompanyName(),"",10l,60l,8l));
	        pdfWriter.setPageEvent(new HeaderFooterPageEvent2("   ","                   "+demandNoteInfoObject.getCompanyBillingAddress(),"",20l,50l,7l));
			pdfWriter.setPageEvent(new HeaderFooterPageEvent2("  ","                             "+footerAdderss,"",30l,40l,7l));	    	
    	}
    	else {
    		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",demandNoteInfoObject.getCompanyName(),"",10l,50l,8l));
            pdfWriter.setPageEvent(new HeaderFooterPageEvent2("   ",demandNoteInfoObject.getCompanyBillingAddress(),"",20l,40l,7l));
    		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("  ",footerAdderss,"",30l,30l,7l));
    	}
			        
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		try {
			executorService.execute(new Runnable() {
				public void run() {
					try {
						worker.parseXHtml(pdfWriter, document, new StringReader(data));
						document.close();
						pdfWriter.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			executorService.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
			executorService.shutdown();
			log.error("Failed to store file in drive, Please contact to Support Team.");
			throw new EmployeeFinancialServiceException("Failed to store file in drive, Please contact to Support Team.");
		
		}

		log.info("*** The Pdf File " + pdfFileName + " is created successfully ***");
	}
	
	public void XMLWorkerHelperForCostBreakUpLetter(Email email, FileInfo fileInfo) throws Exception {
		log.info("***** Control inside the PdfHelper.XMLWorkerHelperForCostBreakUpLetter() *****");
		DemandNoteGeneratorInfo demandNoteInfoObject = email.getDemandNoteGeneratorInfo();
		String allotmentLetterPdfFileName = demandNoteInfoObject.getDemandNotePdfFileName();
		String transactionReceiptPdfFilePath = "";
		String transactionGetReceiptFileURL = "";
		String appovals=null;
		long bookingFormId = demandNoteInfoObject.getBookingFormId();
		long portNumber = email.getPortNumber();
		log.info(" ***** PdfHelper.XMLWorkerHelperForCostBreakUpLetter() ***** PORT number : "+portNumber+"\t Booking Form Id :"+bookingFormId);
		//Properties prop= responceCodesUtil.getApplicationProperties();
		transactionReceiptPdfFilePath = demandNoteInfoObject.getFolderFilePath();
		transactionGetReceiptFileURL = demandNoteInfoObject.getFolderFileUrl();
/*		if (portNumber == Long.valueOf(MetadataId.LOCAL_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("DOCUMENTS_FILE_DIRECTORY_PATH");
			transactionGetReceiptFileURL = prop.getProperty("DOCUMENTS_FILE_URL_PATH");
		} else if (portNumber == Long.valueOf(MetadataId.UAT_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("DOCUMENTS_FILE_DIRECTORY_PATH");
			transactionGetReceiptFileURL = prop.getProperty("DOCUMENTS_FILE_URL_PATH");
		} else if (portNumber == Long.valueOf(MetadataId.CUG_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("CUG_ALLOTMENT_LETTER_PDF_PATH");
			transactionGetReceiptFileURL = prop.getProperty("CUG_ALLOTMENT_LETTER_PDF_URL");
		} else if (portNumber == Long.valueOf(MetadataId.LIVE_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("LIVE_ALLOTMENT_LETTER_PDF_PATH");
			transactionGetReceiptFileURL = prop.getProperty("LIVE_ALLOTMENT_LETTER_PDF_URL");
		} else {
			log.error("Path missing to save the File. Please contact to Support Team");
			throw new EmployeeFinancialServiceException("Path missing to save the File, Please contact to Support Team.");
		}
		if(transactionReceiptPdfFilePath==null || transactionReceiptPdfFilePath.isEmpty()) {
			throw new EmployeeFinancialServiceException("File path found empty for storing pdf's.");
		}	
*/		//final Document document = new Document(PageSize.A4, 25, 25, 105, 80);// this is for footer and header
		final Document document = new Document(PageSize.A4, 25, 25, 105, 110);// this is for footer and header
		String path = transactionReceiptPdfFilePath + demandNoteInfoObject.getSiteId() + "/" + bookingFormId + "/";
		String fileName = getFileName(path, allotmentLetterPdfFileName);
			
		StringBuilder pdfFileName = new StringBuilder(transactionReceiptPdfFilePath)
				.append(demandNoteInfoObject.getSiteId()).append("/").append(bookingFormId).append("/")
				.append(fileName);

		StringBuilder urlPath = new StringBuilder(transactionGetReceiptFileURL).append(demandNoteInfoObject.getSiteId())
				.append("/").append(bookingFormId).append("/").append(fileName);

		fileInfo.setName(fileName);
		fileInfo.setUrl(urlPath.toString());
		fileInfo.setFilePath(pdfFileName.toString());
		fileInfo.setExtension(pdfFileName.substring(pdfFileName.lastIndexOf(".") + 1));
		log.info(pdfFileName + "\t" + urlPath);
		final PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFileName.toString()));

		document.open();
		document.addAuthor("Amaravadhis software services private limited");
		document.addCreator("Aniket Chavan");
		document.addSubject(" Cost Break Up Letter ");
		document.addCreationDate();
		document.addTitle(" Cost Break Up Letter ");
		final com.itextpdf.tool.xml.XMLWorkerHelper worker = com.itextpdf.tool.xml.XMLWorkerHelper.getInstance();

		//email.setTemplateName("/vmtemplates/COST_BREAK_UP_AND_PAYMENT_SCHEDULE.vm");
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("email", email);
		final String data = mailServiceImpl.geVelocityTemplateContent(model);
		log.info(" ********************************************************************* \n" + data);

		Rectangle headerBox = new Rectangle(36, 54, 559, 788);
		pdfWriter.setBoxSize("headerBox", headerBox);

		//String footerAdderss = "";

		/*if (demandNoteInfoObject.getCompanyCin() != "-") {
	       	footerAdderss = "Tel: "+demandNoteInfoObject.getCompanyTelephoneNo()+" CIN: "+demandNoteInfoObject.getCompanyCin()+" GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" E-mail: "+demandNoteInfoObject.getCompanyEmail()+" Website: "+demandNoteInfoObject.getCompanyWebsite();
	    } else {
	       	footerAdderss = "Tel: "+demandNoteInfoObject.getCompanyTelephoneNo()+" LLPIN: "+demandNoteInfoObject.getCompanyLlpin()+" GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" E-mail: "+demandNoteInfoObject.getCompanyEmail()+" Website: "+demandNoteInfoObject.getCompanyWebsite();
	    }
	    getFooterAddress(demandNoteInfoObject);*/
	    
		String headData = "";
		String footerData = "===";
		String rightLogo = Util.isNotEmptyObject(demandNoteInfoObject.getRightSidelogoFilePath())? demandNoteInfoObject.getRightSidelogoFilePath() : demandNoteInfoObject.getRightSidelogoForPdf();
		String leftLogo = Util.isNotEmptyObject(demandNoteInfoObject.getLeftSidelogoFilePath())? demandNoteInfoObject.getLeftSidelogoFilePath() : demandNoteInfoObject.getLeftSidelogoForPdf();
		Long headerTextHeight = 5l;
		Long footerTextHeight = 60l;
		Long fontSize = 7l;

		HeaderFooterPageEvent2 logo = new HeaderFooterPageEvent2();
		logo.setHeadData(headData);
		logo.setFooterData(footerData);
		logo.setRightLogo(rightLogo);
		logo.setLeftLogo(leftLogo);
		logo.setHeaderTextHeight(headerTextHeight);
		logo.setFooterTextHeight(footerTextHeight);
		logo.setFontSize(fontSize);

        pdfWriter.setPageEvent(logo);
        
        /* Malladi Changes */
        if(!("131").equals(demandNoteInfoObject.getSiteId()) && !("139").equals(demandNoteInfoObject.getSiteId())  && !("133").equals(demandNoteInfoObject.getSiteId()) ) {
        	appovals="    Prepared By                           Authorised By                           Approved By                           Customer/s Signature";
        	pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",appovals,"",10l,35l,10l));
        }
        
        //pdfWriter.setPageEvent(new HeaderFooterPageEvent2("","===",demandNoteInfoObject.getRightSidelogoForPdf(),5l,60l,7l));
		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",demandNoteInfoObject.getCompanyName(),"",10l,20l,8l));
		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",demandNoteInfoObject.getCompanyBillingAddress(),"",20l,10l,7l));
		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",getFooterAddress(demandNoteInfoObject),"",30l,0l,7l));
			        
		/*ExecutorService executorService = Executors.newFixedThreadPool(5);
		try {
			executorService.execute(new Runnable() {
				public void run() {
					try {*/
						worker.parseXHtml(pdfWriter, document, new StringReader(data));
						document.close();
						pdfWriter.close();
					/*} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			executorService.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
			executorService.shutdown();
		}*/

		log.info("*** The Pdf File " + pdfFileName + " is created successfully ***");
	}

	public void XMLWorkerHelperForAgreementDraftLetter(Email email, FileInfo fileInfo) throws Exception {
		log.info("***** Control inside the PdfHelper.XMLWorkerHelperForAgreementDraftLetter() *****");
		DemandNoteGeneratorInfo demandNoteInfoObject = email.getDemandNoteGeneratorInfo();
		String allotmentLetterPdfFileName = demandNoteInfoObject.getDemandNotePdfFileName();
		String PdfFilePath = "";
		String PdfFileURL = "";
		long bookingFormId = demandNoteInfoObject.getBookingFormId();
		long portNumber = email.getPortNumber();
		log.info(" ***** PdfHelper.XMLWorkerHelperForAgreementDraftLetter() ***** PORT number : "+portNumber+"\t Booking Form Id :"+bookingFormId);
		//Properties prop= responceCodesUtil.getApplicationProperties();
		PdfFilePath = demandNoteInfoObject.getFolderFilePath();
		PdfFileURL = demandNoteInfoObject.getFolderFileUrl();
		/*if (portNumber == Long.valueOf(MetadataId.LOCAL_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("DOCUMENTS_FILE_DIRECTORY_PATH");
			transactionGetReceiptFileURL = prop.getProperty("DOCUMENTS_FILE_URL_PATH");
		} else if (portNumber == Long.valueOf(MetadataId.UAT_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("DOCUMENTS_FILE_DIRECTORY_PATH");
			transactionGetReceiptFileURL = prop.getProperty("DOCUMENTS_FILE_URL_PATH");
		} else if (portNumber == Long.valueOf(MetadataId.CUG_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("CUG_ALLOTMENT_LETTER_PDF_PATH");
			transactionGetReceiptFileURL = prop.getProperty("CUG_ALLOTMENT_LETTER_PDF_URL");
		} else if (portNumber == Long.valueOf(MetadataId.LIVE_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("LIVE_ALLOTMENT_LETTER_PDF_PATH");
			transactionGetReceiptFileURL = prop.getProperty("LIVE_ALLOTMENT_LETTER_PDF_URL");
		} else {
			log.error("Path missing to save the File. Please contact to Support Team");
			throw new EmployeeFinancialServiceException("Path missing to save the File, Please contact to Support Team.");
		}
		if(transactionReceiptPdfFilePath==null || transactionReceiptPdfFilePath.isEmpty()) {
			throw new EmployeeFinancialServiceException("File path found empty for storing pdf's.");
		}*/	
		//final Document document = new Document(PageSize.A4, 20, 20, 80, 80);// this is for footer and header
		final Document document = new Document(PageSize.A4, 25, 25, 50, 50);//this is  for footer and header
		String path = "";
		
		path = PdfFilePath + demandNoteInfoObject.getSiteId() + "/" + bookingFormId + "/";
		
		String fileName = getFileName(path, allotmentLetterPdfFileName);
			
		StringBuilder pdfFileName = new StringBuilder(PdfFilePath)
				.append(demandNoteInfoObject.getSiteId()).append("/").append(bookingFormId).append("/")
				.append(fileName);

		StringBuilder urlPath = new StringBuilder(PdfFileURL).append(demandNoteInfoObject.getSiteId())
				.append("/").append(bookingFormId).append("/").append(fileName);

		fileInfo.setName(fileName);
		fileInfo.setUrl(urlPath.toString());
		fileInfo.setFilePath(pdfFileName.toString());
		fileInfo.setExtension(pdfFileName.substring(pdfFileName.lastIndexOf(".") + 1));

		final PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFileName.toString()));
		//Developer: Inamdar
		//Start
		PdfHeader event = new PdfHeader();
		pdfWriter.setPageEvent(event);
		//end
		document.open();
		document.addAuthor("Amaravadhis software services private limited");
		document.addCreator("Aniket Chavan");
		document.addSubject("Agreement Draft");
		document.addTitle("Agreement Draft");
		document.addCreationDate();
		
		
		final com.itextpdf.tool.xml.XMLWorkerHelper worker = com.itextpdf.tool.xml.XMLWorkerHelper.getInstance();
		
		//email.setTemplateName("/vmtemplates/AgreementDraft.vm");
		/*File file = new File("D:\\CUSTOMERAPP_UAT\\images\\sumadhura_projects_images\\Agreement_draft_letter\\AgreementDraft.vm");
		if(file.exists()) {
			System.out.println("***** Control inside the PdfHelper.XMLWorkerHelperForAgreementDraftLetter() *****");
		}
		email.setTemplateName("AgreementDraft.vm");*/
		
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("email", email);
		model.put("StringUtils", new Util());
		model.put("DateUtils", new TimeUtil());
		model.put("CurrencyUtil", new CurrencyUtil());
		model.put("EmployeeFinancialHelper", new EmployeeFinancialHelper());
		
		final String data = mailServiceImpl.geVelocityTemplateContent(model);
		log.info(pdfFileName+" ********************************************************************* \n" + data);
		log.info(pdfFileName + "\t" + urlPath);
		Rectangle headerBox = new Rectangle(36, 54, 559, 788);
		pdfWriter.setBoxSize("headerBox", headerBox);
		

		/*String footerAdderss = "";
		if (demandNoteInfoObject.getCompanyCin() != "-") {
	       	footerAdderss = "Tel: "+demandNoteInfoObject.getCompanyTelephoneNo()+" CIN: "+demandNoteInfoObject.getCompanyCin()+" GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" E-mail: "+demandNoteInfoObject.getCompanyEmail()+" Website: "+demandNoteInfoObject.getCompanyWebsite();
	    } else {
	       	footerAdderss = "Tel: "+demandNoteInfoObject.getCompanyTelephoneNo()+" LLPIN: "+demandNoteInfoObject.getCompanyLlpin()+" GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" E-mail: "+demandNoteInfoObject.getCompanyEmail()+" Website: "+demandNoteInfoObject.getCompanyWebsite();
	    }*/
	        
		String headData = "";
		String footerData = "===";
		String rightLogo = demandNoteInfoObject.getRightSidelogoForPdf();
		String leftLogo =  demandNoteInfoObject.getLeftSidelogoForPdf();
		Long headerTextHeight = 5l;
		Long footerTextHeight = 60l;
		Long fontSize = 7l;

		HeaderFooterPageEvent2 logo = new HeaderFooterPageEvent2();
		logo.setHeadData(headData);
		logo.setFooterData(footerData);
		//logo.setApplyPageNumberinFooter(String.format("Page %d of", pdfWriter.getPageNumber()));
		logo.setRightLogo(rightLogo);
		logo.setLeftLogo(leftLogo);
		logo.setHeaderTextHeight(headerTextHeight);
		logo.setFooterTextHeight(footerTextHeight);
		logo.setFontSize(fontSize);
		

        /*pdfWriter.setPageEvent(logo);
        //pdfWriter.setPageEvent(new HeaderFooterPageEvent2("","===",demandNoteInfoObject.getRightSidelogoForPdf(),5l,60l,7l));
		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",demandNoteInfoObject.getCompanyName(),"",10l,50l,8l));
		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",demandNoteInfoObject.getCompanyBillingAddress(),"",20l,40l,7l));
		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",footerAdderss,"",30l,30l,7l));*/
			        
		/*ExecutorService executorService = Executors.newFixedThreadPool(5);
		try {
			executorService.execute(new Runnable() {
				public void run() {
					try {*/
		
						/*
						java.nio.ByteBuffer bytebuffer = java.nio.ByteBuffer.wrap(css.getBytes());  
						CharBuffer charbuffer = cs.decode(bytebuffer);  */
		
					//Developer: Inamdar
					//Start
						String css = "div,span, p, li {\r\n" + 
								"  text-align: justify;\r\n" + 
								"  text-justify: inter-word;\r\n" + 
								"}";
		
						InputStream inputStream = new ByteArrayInputStream(css.getBytes());
						ByteArrayInputStream bis = new ByteArrayInputStream(data.toString().getBytes());
						
						worker.parseXHtml(pdfWriter, document, bis, inputStream);
						//End
						document.close();
						pdfWriter.close();
						
						/*Load a sample PDF document
				        PdfDocument pdf = new PdfDocument(pdfFileName.toString());
				 
				        Save the PDF document as Word DOCX format
				        pdf.saveToFile("E:\\CUSTOMERAPP_CUG\\images\\sumadhura_projects_images\\documents\\Preview\\124\\894\\ToWord.docx", FileFormat.DOCX);
				        pdf.close();*/
						
						//com.aspose.pdf.Document doc = new com.aspose.pdf.Document(pdfFileName.toString());
					//	doc.save("E:\\\\CUSTOMERAPP_CUG\\\\images\\\\sumadhura_projects_images\\\\documents\\\\Preview\\\\124\\\\894\\\\output.doc", SaveFormat.Doc);
					/*} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			executorService.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
			executorService.shutdown();
		}*/

		log.info("*** The Pdf File " + pdfFileName + " is created successfully ***");
	}

	
	public void checkTemplates(Email email, FileInfo fileInfo) throws Exception {
		log.info("***** Control inside the PdfHelper.XMLWorkerHelperForCostBreakUpLetter() *****"); 
		DemandNoteGeneratorInfo demandNoteInfoObject = new DemandNoteGeneratorInfo();
		demandNoteInfoObject.setSiteId("111");
		demandNoteInfoObject.setSiteName("Nandanam");
		String allotmentLetterPdfFileName = "Testing.pdf";
		String transactionReceiptPdfFilePath = "";
		String transactionGetReceiptFileURL = "";
		long bookingFormId = 1004;
		long portNumber = 8888;
		log.info(" ***** PdfHelper.XMLWorkerHelperForReceipt() ***** PORT number : "+portNumber+"\t Booking Form Id :"+bookingFormId);
		Properties prop = null;
		if(Util.isNotEmptyObject(responceCodesUtil)) {
			 prop = responceCodesUtil.getApplicationProperties();
		} else {
			prop = new ResponceCodesUtil().getApplicationProperties();
		}

		 
		if (portNumber == Long.valueOf(MetadataId.LOCAL_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("DOCUMENTS_FILE_DIRECTORY_PATH");
			transactionGetReceiptFileURL = prop.getProperty("DOCUMENTS_FILE_URL_PATH");
		} else if (portNumber == Long.valueOf(MetadataId.UAT_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("DOCUMENTS_FILE_DIRECTORY_PATH");
			transactionGetReceiptFileURL = prop.getProperty("DOCUMENTS_FILE_URL_PATH");
		} else if (portNumber == Long.valueOf(MetadataId.CUG_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("CUG_ALLOTMENT_LETTER_PDF_PATH");
			transactionGetReceiptFileURL = prop.getProperty("CUG_ALLOTMENT_LETTER_PDF_URL");
		}else if (portNumber == Long.valueOf(MetadataId.HTTPS_CUG_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("CUG_ALLOTMENT_LETTER_PDF_PATH");
			transactionGetReceiptFileURL = prop.getProperty("CUG_ALLOTMENT_LETTER_PDF_URL");
		} else if (portNumber == Long.valueOf(MetadataId.LIVE_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("LIVE_ALLOTMENT_LETTER_PDF_PATH");
			transactionGetReceiptFileURL = prop.getProperty("LIVE_ALLOTMENT_LETTER_PDF_URL");
		} else if (portNumber == Long.valueOf(MetadataId.HTTPS_LIVE_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("LIVE_ALLOTMENT_LETTER_PDF_PATH");
			transactionGetReceiptFileURL = prop.getProperty("LIVE_ALLOTMENT_LETTER_PDF_URL");
		} else {
			log.error("Path missing to save the File. Please contact to Support Team");
			throw new EmployeeFinancialServiceException("Path missing to save the File, Please contact to Support Team.");
		}
		if(transactionReceiptPdfFilePath==null || transactionReceiptPdfFilePath.isEmpty()) {
			throw new EmployeeFinancialServiceException("File path found empty for storing pdf's.");
		}	
		//final Document document = new Document(PageSize.A4, 25, 25, 50, 50);//this is  for footer and header
		final Document document = new Document(PageSize.A4, 25, 25, 105, 110);//cost breakip

		String path = transactionReceiptPdfFilePath + demandNoteInfoObject.getSiteId() + "/" + bookingFormId + "/";
		String fileName = getFileName(path, allotmentLetterPdfFileName);
			
		StringBuilder pdfFileName = new StringBuilder(transactionReceiptPdfFilePath)
				.append(demandNoteInfoObject.getSiteId()).append("/").append(bookingFormId).append("/")
				.append(fileName);

		StringBuilder urlPath = new StringBuilder(transactionGetReceiptFileURL).append(demandNoteInfoObject.getSiteId())
				.append("/").append(bookingFormId).append("/").append(fileName);

		fileInfo.setName(fileName);
		fileInfo.setUrl(urlPath.toString());
		fileInfo.setFilePath(pdfFileName.toString());
		fileInfo.setExtension(pdfFileName.substring(pdfFileName.lastIndexOf(".") + 1));
		log.info("\n"+pdfFileName + "\t" + urlPath);
		final PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFileName.toString()));

		document.open();
		document.addAuthor("Amaravadhis software services private limited");
		document.addCreator("Aniket Chavan");
		document.addSubject("Sample");
		document.addCreationDate();
		document.addTitle("Sample");
		//final com.itextpdf.tool.xml.XMLWorkerHelper worker = com.itextpdf.tool.xml.XMLWorkerHelper.getInstance();
		final com.itextpdf.tool.xml.XMLWorkerHelper worker = com.itextpdf.tool.xml.XMLWorkerHelper.getInstance();

		//email.setTemplateName("/vmtemplates/AgreementDraft.vm");
		email.setTemplateName("/vmtemplates/LOAN_NOC_LETTER.vm");
		//email.setTemplateName("/vmtemplates/NOC_Nandanam.vm");
		//email.setTemplateName("/vmtemplates/Agreement_134_VASAVI_Draft.vm");
		//email.setTemplateName("/vmtemplates/COST_BREAK_UP_OLYMPUS.vm");
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("email", email);
		final String data = mailServiceImpl.geVelocityTemplateContent(model);
		log.info(email.getTemplateName()+" ********************************************************************* \n" + data);

		Rectangle headerBox = new Rectangle(36, 54, 559, 788);
		pdfWriter.setBoxSize("headerBox", headerBox);
		
		/*List romanlist = new List(true, 20);
		romanlist.setIndentationLeft(30f);
		romanlist.setListSymbol("&#42;");
		romanlist.add("One");
		romanlist.add("Two");
		romanlist.add("Three");
		romanlist.add("Four");
		romanlist.add("Five");
		document.add(romanlist);*/
		
		/* HTMLtoPDF(data,new FileOutputStream(new StringBuilder(transactionReceiptPdfFilePath)
					.append(demandNoteInfoObject.getSiteId()).append("/").append(bookingFormId).append("/")+"string-to-pdf.pdf"));*/
		  
        System.out.println( "PDF Created!" );
		
		HeaderFooterPageEvent2 logo = new HeaderFooterPageEvent2();
		logo.setHeadData("");
		logo.setFooterData("===");
		logo.setRightLogo(responceCodesUtil.getApplicationProperties().getProperty("SumadhuraVasaviLLP_LOGO1"));
		logo.setLeftLogo(responceCodesUtil.getApplicationProperties().getProperty("SUMADHURA_LOGO1"));
		logo.setHeaderTextHeight(5l);
		logo.setFooterTextHeight(60l);
		logo.setFontSize(7l);
		
		HeaderFooterPageEvent2 applyPageNumberinHeader = new HeaderFooterPageEvent2();
		applyPageNumberinHeader.setApplyPageNumberinHeader("true");
		applyPageNumberinHeader.setFilePath(pdfFileName.toString());
		/*String headData,
		String footerData,
		String rightLogo,
		Long headerTextHeight,5l
		Long footerTextHeight, 60
		Long fontSize 7*/
		
		//no need this is in live, here for testing
/*		pdfWriter.setPageEvent(logo);
		//pdfWriter.setPageEvent(new HeaderFooterPageEvent2(responceCodesUtil.getApplicationProperties().getProperty("SUMADHURA_LOGO1"),"===",responceCodesUtil.getApplicationProperties().getProperty("SUMADHURA_LOGO1"),5l,60l,7l));
		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("Company name is self one","Company name is self one","",05l,50l,8l));
		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("Billing office? come to the office","Billing office? come to the office","",20l,40l,7l));
		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("Here address is","Here address is iteself which u don't know","",30l,30l,7l));
*/
		
		//pdfWriter.setPageEvent(applyPageNumberinHeader);
		//pdfWriter.setPageEvent(logo);
		//pdfWriter.setPageEvent(new HeaderFooterPageEvent2(" ","Company name is self one","",05l,50l,8l));
		//pdfWriter.setPageEvent(new HeaderFooterPageEvent2(" ","Billing office? come to the office","",20l,40l,7l));
		//pdfWriter.setPageEvent(new HeaderFooterPageEvent2(" ","Here address is iteself which u don't know","",30l,30l,7l));
	
/*        pdfWriter.setPageEvent(logo);
        String appovals="   Prepared By                           Authorised By                           Approved By                           Customer/s Signature";
    	pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",appovals,"",10l,35l,10l));
        //pdfWriter.setPageEvent(new HeaderFooterPageEvent2("","===",demandNoteInfoObject.getRightSidelogoForPdf(),5l,60l,7l));
		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("","Sumadhura Infracon pvt ltd","",10l,20l,8l));
		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("","1ST FLOOR NORTH END, 8-2-120/86/9/A/1, 12 and 2/13, ANILATH MAJA HOUSING SOCIETY, ROAD NO.2, BANJARA HILLS, HYDERABAD, TELANGANA, 500034.","",20l,10l,7l));
		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("","This is system generated document and does not require signature.","",30l,0l,7l));
*/
		
		/*ExecutorService executorService = Executors.newFixedThreadPool(5);
		try {
			executorService.execute(new Runnable() {
				public void run() {
					try {*/
				ByteArrayInputStream bis = new ByteArrayInputStream(data.toString().getBytes());
						worker.parseXHtml(pdfWriter, document, bis);//new StringReader(data)
						 /*for (Element e : XMLWorkerHelper.parseToElementList(data, css)) {
					            cell.addElement(e);
					        }*/
						document.close();
						pdfWriter.close();
					/*} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			executorService.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
			executorService.shutdown();
		}*/

		String destinationFile = new StringBuilder(transactionReceiptPdfFilePath)
						.append(demandNoteInfoObject.getSiteId()).append("/").append(bookingFormId).append("/").append("string-to-pdf.pdf").toString();
		manipulatePdf(pdfFileName.toString(), destinationFile);
		log.info("*** The Pdf File " + pdfFileName + " is created successfully ***");
	}
	
	public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
        //adding page number to pdf
		PdfReader reader = new PdfReader(src);
        int totalPages = reader.getNumberOfPages();
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        PdfContentByte pagecontent;
        Font ffont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
        for (int currentPage = 0; currentPage < totalPages; ) {
            pagecontent = stamper.getOverContent(++currentPage);
            ColumnText.showTextAligned(pagecontent, Element.ALIGN_RIGHT,
                    new Phrase(String.format("Page %s of %s", currentPage, totalPages),ffont), 559, 806, 0);
        }
        stamper.close();
        reader.close();
    
        //delete source file, as in destination file source file data copied and some data has appended 
        File sourceDelete = new File(src);
        if(sourceDelete.isFile() && sourceDelete.exists()) {
        	sourceDelete.delete();	
        }
        
        //renaming dest file name to source file name
        File destDelete = new File(dest);
        destDelete.renameTo(sourceDelete);
        /*if(destDelete.isFile() && destDelete.exists()) {
        	destDelete.delete();	
        }*/
        
    }

	@SuppressWarnings("unused")
	private void HTMLtoPDF(String data, FileOutputStream fileOutputStream) throws IOException {
		/*
		 * <groupId>com.itextpdf</groupId>
		    <artifactId>html2pdf</artifactId>
		    <version>2.1.1</version>
		 */
		/*ConverterProperties converterProperties = new ConverterProperties();
		converterProperties.setTagWorkerFactory(
		        new DefaultTagWorkerFactory() {
		            @Override
		            public ITagWorker getCustomTagWorker(IElementNode tag, ProcessorContext context) {
		                    if ("a".equalsIgnoreCase(tag.name()) ) {
		                        return new SpanTagWorker(tag, context);
		                    }
		                    if ("ol".equalsIgnoreCase(tag.name()) ) {
		                        return new SpanTagWorker(tag, context);
		                    }
		                    return null;
		                }
		            } );
		  HtmlConverter.convertToPdf(data, fileOutputStream);*/
	}

	public Map<String, Object> XMLWorkerHelperForWelcomeLetter(Email email, FileInfo fileInfo) throws Exception {
		Map<String,Object> welcomeLetterData = new HashMap<>();
		log.info("***** Control inside the PdfHelper.XMLWorkerHelperForCostBreakUpLetter() *****"); 
		DemandNoteGeneratorInfo demandNoteInfoObject = email.getDemandNoteGeneratorInfo();
		String allotmentLetterPdfFileName = demandNoteInfoObject.getDemandNotePdfFileName();
		String transactionReceiptPdfFilePath = "";
		String transactionGetReceiptFileURL = "";
		long bookingFormId = demandNoteInfoObject.getBookingFormId();
		long portNumber = email.getPortNumber();
		log.info(" ***** PdfHelper.XMLWorkerHelperForReceipt() ***** PORT number : "+portNumber+"\t Booking Form Id :"+bookingFormId);
		Properties prop= responceCodesUtil.getApplicationProperties();
		
		if (portNumber == Long.valueOf(MetadataId.LOCAL_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("UAT_ALLOTMENT_LETTER_PDF_PATH");
			transactionGetReceiptFileURL = prop.getProperty("DOCUMENTS_FILE_URL_PATH");
		} else if (portNumber == Long.valueOf(MetadataId.UAT_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("UAT_ALLOTMENT_LETTER_PDF_PATH");
			transactionGetReceiptFileURL = prop.getProperty("DOCUMENTS_FILE_URL_PATH");
		} else if (portNumber == Long.valueOf(MetadataId.CUG_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("CUG_ALLOTMENT_LETTER_PDF_PATH");
			transactionGetReceiptFileURL = prop.getProperty("CUG_ALLOTMENT_LETTER_PDF_URL");
		} else if (portNumber == Long.valueOf(MetadataId.LIVE_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("LIVE_ALLOTMENT_LETTER_PDF_PATH");
			transactionGetReceiptFileURL = prop.getProperty("LIVE_ALLOTMENT_LETTER_PDF_URL");
		}else if (portNumber == Long.valueOf(MetadataId.HTTPS_CUG_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("CUG_ALLOTMENT_LETTER_PDF_PATH");
			transactionGetReceiptFileURL = prop.getProperty("CUG_ALLOTMENT_LETTER_PDF_URL");
		} else if (portNumber == Long.valueOf(MetadataId.HTTPS_LIVE_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("LIVE_ALLOTMENT_LETTER_PDF_PATH");
			transactionGetReceiptFileURL = prop.getProperty("LIVE_ALLOTMENT_LETTER_PDF_URL");
		} else {
			log.error("Path missing to save the File. Please contact to Support Team");
			throw new EmployeeFinancialServiceException("Path missing to save the File, Please contact to Support Team.");
		}
		if(transactionReceiptPdfFilePath==null || transactionReceiptPdfFilePath.isEmpty()) {
			throw new EmployeeFinancialServiceException("File path found empty for storing pdf's.");
		}	
		//final Document document = new Document(PageSize.A4, 25, 25, 110, 80);// this is for footer and header

		String path = transactionReceiptPdfFilePath + demandNoteInfoObject.getSiteId() + "/" + bookingFormId + "/";
		String fileName = getFileName(path, allotmentLetterPdfFileName);
			
		StringBuilder pdfFileName = new StringBuilder(transactionReceiptPdfFilePath)
				.append(demandNoteInfoObject.getSiteId()).append("/").append(bookingFormId).append("/")
				.append(fileName);

		StringBuilder urlPath = new StringBuilder(transactionGetReceiptFileURL).append(demandNoteInfoObject.getSiteId())
				.append("/").append(bookingFormId).append("/").append(fileName);

		fileInfo.setName(fileName);
		fileInfo.setUrl(urlPath.toString());
		fileInfo.setFilePath(pdfFileName.toString());
		log.info(pdfFileName + "\t" + urlPath);
		//final PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFileName.toString()));

		/*document.open();
		document.addAuthor("Amaravadhis software services private limited");
		document.addCreator("Aniket Chavan");
		document.addSubject(" Welcome Letter ");
		document.addCreationDate();
		document.addTitle(" Welcome Letter ");*/
		//final com.itextpdf.tool.xml.XMLWorkerHelper worker = com.itextpdf.tool.xml.XMLWorkerHelper.getInstance();

		//email.setTemplateName("/vmtemplates/welcome_letter.vm");
		
		if(Util.isNotEmptyObject(email.getSiteIds())&&Util.isNotEmptyObject(email.getSiteIds().get(0))&&(email.getSiteIds().get(0).equals(102l)||email.getSiteIds().get(0).equals(114l)||email.getSiteIds().get(0).equals(133l)||email.getSiteIds().get(0).equals(134l))) {
			email.setTemplateName("/vmtemplates/welcome_letter_HYD.vm");
		}else {
		email.setTemplateName("/vmtemplates/welcome_letter_2.vm");
		}
		//email.setTemplateName("/vmtemplates/welcome_letter.vm");
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("email", email);
		final String data = mailServiceImpl.geVelocityTemplateContent(model);
		log.info(" ********************************************************************* \n" + data);
		welcomeLetterData.put("welcomeLetterEmailContent",data);
		
		//Rectangle headerBox = new Rectangle(36, 54, 559, 788);
		//pdfWriter.setBoxSize("headerBox", headerBox);

		/*String footerAdderss = "";
		if (demandNoteInfoObject.getCompanyCin() != "-") {
	       	footerAdderss = "Tel: "+demandNoteInfoObject.getCompanyTelephoneNo()+" CIN: "+demandNoteInfoObject.getCompanyCin()+" GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" E-mail: "+demandNoteInfoObject.getCompanyEmail()+" Website: "+demandNoteInfoObject.getCompanyWebsite();
	    } else {
	       	footerAdderss = "Tel: "+demandNoteInfoObject.getCompanyTelephoneNo()+" LLPIN: "+demandNoteInfoObject.getCompanyLlpin()+" GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" E-mail: "+demandNoteInfoObject.getCompanyEmail()+" Website: "+demandNoteInfoObject.getCompanyWebsite();
	    }*/
	        
		// for header third parameter "abc"
		// for footer fourth parameter
		/*pdfWriter.setPageEvent(new HeaderFooterPageEvent2("", "===", demandNoteInfoObject.getRightSidelogoForPdf(), 5l, 60l, 7l));
		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("", demandNoteInfoObject.getCompanyName(), "", 10l, 50l, 8l));
		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("", demandNoteInfoObject.getCompanyBillingAddress(), "", 20l, 40l, 7l));
		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("", footerAdderss, "", 30l, 30l, 7l));*/
			        
		/*ExecutorService executorService = Executors.newFixedThreadPool(5);
		try {
			executorService.execute(new Runnable() {
				public void run() {
					try {*/
						//worker.parseXHtml(pdfWriter, document, new StringReader(data));
						//document.close();
						//pdfWriter.close();
					/*} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			executorService.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
			executorService.shutdown();
		}*/

		//log.info("*** The Pdf File " + pdfFileName + " is created successfully ***");
		return welcomeLetterData;
	}

	
	public String getFileName(String path, String currentFileName) {
		/* Creating Folders */
		File filePath = new File(path);
		if(!filePath.exists()) {
			filePath.mkdirs();
			
		}
		/* Creating File Name */		
		File file = new File(path, currentFileName);
		if (!file.exists()) {
			return currentFileName;
		} else {
			int count = 1;
			String newFileName;
			do {
				String fileName = currentFileName.substring(0, currentFileName.lastIndexOf('.'));
				String extension = currentFileName.substring(currentFileName.lastIndexOf('.'));
				newFileName=fileName + "_" + count + extension;
				file = new File(path, newFileName);
				count++;
			} while (file.exists());
			return newFileName;
		}
		
	}
	
	public Boolean deleteFileIfExists(String path, String currentFileName) {
		log.info("*** Control inside of the deleteFileIfExists in PdfHelper ***");
		/* Creating Folders */
		File filePath = new File(path);
		if(!filePath.exists()) {
			filePath.mkdirs();
			return true;
		}
		
		/* Deleting old File */		
		File file = new File(path, currentFileName);
		if (file.exists()) {
			return file.delete();
		}
		return true;
	}
	
	public String generateFileName(String fileName) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH.mm.ss");  
	    Date date = new Date();  
	    return formatter.format(date)+" "+fileName; 
	}

	
	public FileInfo XMLWorkerHelperForLegalInvoice(Email email) throws IOException, DocumentException, Exception {
		log.info("***** Control inside the PdfHelper.XMLWorkerHelperForLegalInvoice() *****");
		FileInfo fileInfo = new FileInfo();
		Document document = new Document(PageSize.LEGAL);

		String legalInvoicePdfFilePath = "";
		String legalInvoicePdfFileUrl = "";
		long portNumber = email.getPortNumber();
		
/*		legalInvoicePdfFilePath = responceCodesUtil.getApplicationNamePropeties("UAT_LEGAL_INVOICE_PDF_PATH");
		legalInvoicePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("UAT_LEGAL_INVOICE_PDF_URL");*/
		
		if (portNumber == Long.valueOf(MetadataId.LOCAL_PORT_NUMBER.getName())) {
			legalInvoicePdfFilePath = responceCodesUtil.getApplicationNamePropeties("UAT_LEGAL_INVOICE_PDF_PATH");
			legalInvoicePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("UAT_LEGAL_INVOICE_PDF_URL");
		}else if (portNumber == Long.valueOf(MetadataId.UAT_PORT_NUMBER.getName())) {
			legalInvoicePdfFilePath = responceCodesUtil.getApplicationNamePropeties("UAT_LEGAL_INVOICE_PDF_PATH");
			legalInvoicePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("UAT_LEGAL_INVOICE_PDF_URL");
		} else if (portNumber == Long.valueOf(MetadataId.CUG_PORT_NUMBER.getName())) {
			legalInvoicePdfFilePath = responceCodesUtil.getApplicationNamePropeties("CUG_LEGAL_INVOICE_PDF_PATH");
			legalInvoicePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("CUG_LEGAL_INVOICE_PDF_URL");
		}else if (portNumber == Long.valueOf(MetadataId.HTTPS_CUG_PORT_NUMBER.getName())) {
			legalInvoicePdfFilePath = responceCodesUtil.getApplicationNamePropeties("CUG_LEGAL_INVOICE_PDF_PATH");
			legalInvoicePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("CUG_LEGAL_INVOICE_PDF_URL");
		} else if (portNumber == Long.valueOf(MetadataId.LIVE_PORT_NUMBER.getName())) {
			legalInvoicePdfFilePath = responceCodesUtil.getApplicationNamePropeties("LIVE_LEGAL_INVOICE_PDF_PATH");
			legalInvoicePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("LIVE_LEGAL_INVOICE_PDF_URL");
		} else if (portNumber == Long.valueOf(MetadataId.HTTPS_LIVE_PORT_NUMBER.getName())) {
			legalInvoicePdfFilePath = responceCodesUtil.getApplicationNamePropeties("LIVE_LEGAL_INVOICE_PDF_PATH");
			legalInvoicePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("LIVE_LEGAL_INVOICE_PDF_URL");
		} else {
			log.error("Path missing to save the File. Please contact to Support Team");
			throw new EmployeeFinancialServiceException("Path missing to save the File, Please contact to Support Team.");
		}

		
		StringBuilder filePath = new StringBuilder(legalInvoicePdfFilePath)
				.append("/").append(email.getDemandNoteGeneratorInfo().getSiteId())
				.append("/").append(email.getDemandNoteGeneratorInfo().getFlatBookingId())
				.append("/").append(email.getDemandNoteGeneratorInfo().getFolderId());
		StringBuilder fileLocation = new StringBuilder(legalInvoicePdfFileUrl)
				.append("/").append(email.getDemandNoteGeneratorInfo().getSiteId())
				.append("/").append(email.getDemandNoteGeneratorInfo().getFlatBookingId())
				.append("/").append(email.getDemandNoteGeneratorInfo().getFolderId());
		String fileName = getFileName(filePath.toString(), email.getDemandNoteGeneratorInfo().getDemandNotePdfFileName());
		StringBuilder pdfFileName = new StringBuilder(filePath).append("/").append(fileName);
		PdfWriter pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(pdfFileName.toString()));

		USER_PASS = email.getDemandNoteGeneratorInfo().getPancard();
		OWNER_PASS = email.getDemandNoteGeneratorInfo().getPancard();

		/* for output Response */
		fileInfo.setPassword(email.getDemandNoteGeneratorInfo().getPancard());
		fileInfo.setName(fileName);
		fileInfo.setUrl(fileLocation.append("/").append(fileName).toString());
		fileInfo.setFilePath(pdfFileName.toString());
		
		//emailObj.setDemandNoteGeneratedDate(emailObj.getDemandNoteGeneratorInfo().getDemandNoteGeneratedDate());
		
		//adding password to the pdf.
		//pdfWriter.setEncryption(USER_PASS.getBytes(), OWNER_PASS.getBytes(), PdfWriter.ALLOW_PRINTING,PdfWriter.ENCRYPTION_AES_128);
		
		document.open();
		document.addAuthor("Amaravadhis software services private limited");
		document.addCreator("Aniket Chavan");
		document.addSubject(" Invoice ");
		document.addCreationDate();
		document.addTitle(" Invoice ");
		com.itextpdf.tool.xml.XMLWorkerHelper worker = com.itextpdf.tool.xml.XMLWorkerHelper.getInstance();
		
		email.setTemplateName("/demandnotes/legalChargeInvoice.vm");
		Map<String, Object> model = new HashMap<String, Object>();
        model.put("email", email);
        String data = mailServiceImpl.geVelocityTemplateContent(model);
		worker.parseXHtml(pdfWriter, document, new StringReader(data));
		document.close();

		return fileInfo;
	}

	public FileInfo XMLWorkerHelperForModificationInvoice(Email email) throws DocumentException,IOException, Exception {
		log.info(" ***** control is inside the XMLWorkerHelperForModificationInvoice in EmployeeFinancialServiceImpl ***** "+email.getPortNumber());
		String modificationInvoicePdfFilePath = "";
		String modificationInvoicePdfFileUrl = "";
		FileInfo fileInfo = new FileInfo();
		long portNumber = email.getPortNumber();
		
		if (portNumber == Long.valueOf(MetadataId.LOCAL_PORT_NUMBER.getName())) {
			modificationInvoicePdfFilePath = responceCodesUtil.getApplicationNamePropeties("UAT_MODIFICATION_INVOICE_PDF_PATH");
			modificationInvoicePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("UAT_MODIFICATION_INVOICE_PDF_URL");
		}else if (portNumber == Long.valueOf(MetadataId.UAT_PORT_NUMBER.getName())) {
			modificationInvoicePdfFilePath = responceCodesUtil.getApplicationNamePropeties("UAT_MODIFICATION_INVOICE_PDF_PATH");
			modificationInvoicePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("UAT_MODIFICATION_INVOICE_PDF_URL");
		} else if (portNumber == Long.valueOf(MetadataId.CUG_PORT_NUMBER.getName())) {
			modificationInvoicePdfFilePath = responceCodesUtil.getApplicationNamePropeties("CUG_MODIFICATION_INVOICE_PDF_PATH");
			modificationInvoicePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("CUG_MODIFICATION_INVOICE_PDF_URL");
		}else if (portNumber == Long.valueOf(MetadataId.HTTPS_CUG_PORT_NUMBER.getName())) {
			modificationInvoicePdfFilePath = responceCodesUtil.getApplicationNamePropeties("CUG_MODIFICATION_INVOICE_PDF_PATH");
			modificationInvoicePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("CUG_MODIFICATION_INVOICE_PDF_URL");
		}  else if (portNumber == Long.valueOf(MetadataId.LIVE_PORT_NUMBER.getName())) {
			modificationInvoicePdfFilePath = responceCodesUtil.getApplicationNamePropeties("LIVE_MODIFICATION_INVOICE_PDF_PATH");
			modificationInvoicePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("LIVE_MODIFICATION_INVOICE_PDF_URL");
		}else if (portNumber == Long.valueOf(MetadataId.HTTPS_LIVE_PORT_NUMBER.getName())) {
			modificationInvoicePdfFilePath = responceCodesUtil.getApplicationNamePropeties("LIVE_MODIFICATION_INVOICE_PDF_PATH");
			modificationInvoicePdfFileUrl = responceCodesUtil.getApplicationNamePropeties("LIVE_MODIFICATION_INVOICE_PDF_URL");
		} else {
			log.error("Path missing to save the File. Please contact to Support Team");
			throw new EmployeeFinancialServiceException("Path missing to save the File, Please contact to Support Team.");
		}

		Document document = new Document(PageSize.LEGAL);
		StringBuilder filePath = new StringBuilder(modificationInvoicePdfFilePath)
				.append("/").append(email.getDemandNoteGeneratorInfo().getSiteId())
				.append("/").append(email.getDemandNoteGeneratorInfo().getFlatBookingId())
				.append("/").append(email.getDemandNoteGeneratorInfo().getFolderId());
		StringBuilder fileLocation = new StringBuilder(modificationInvoicePdfFileUrl)
				.append("/").append(email.getDemandNoteGeneratorInfo().getSiteId())
				.append("/").append(email.getDemandNoteGeneratorInfo().getFlatBookingId())
				.append("/").append(email.getDemandNoteGeneratorInfo().getFolderId());
		String fileName = getFileName(filePath.toString(), email.getDemandNoteGeneratorInfo().getDemandNotePdfFileName());
		StringBuilder pdfFileName = new StringBuilder(filePath).append("/").append(fileName);
		PdfWriter pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(pdfFileName.toString()));

		USER_PASS = email.getDemandNoteGeneratorInfo().getPancard();
		OWNER_PASS = email.getDemandNoteGeneratorInfo().getPancard();

		//adding password to the pdf.
		//pdfWriter.setEncryption(USER_PASS.getBytes(), OWNER_PASS.getBytes(), PdfWriter.ALLOW_PRINTING,PdfWriter.ENCRYPTION_AES_128);
		document.open();
		document.addAuthor("Amaravadhis software services private limited");
		document.addCreator("Aniket Chavan");
		document.addSubject(" Invoice ");
		document.addCreationDate();
		document.addTitle(" Invoice ");
		com.itextpdf.tool.xml.XMLWorkerHelper worker = com.itextpdf.tool.xml.XMLWorkerHelper.getInstance();
		email.setTemplateName("/demandnotes/modificationChargesInvoice.vm");
		Map<String, Object> model = new HashMap<String, Object>();
        model.put("email", email);
        String data = mailServiceImpl.geVelocityTemplateContent(model);
		worker.parseXHtml(pdfWriter, document, new StringReader(data));
		document.close();
		/* for output Response */
		fileInfo.setPassword(email.getDemandNoteGeneratorInfo().getPancard());
		fileInfo.setName(fileName);
		fileInfo.setUrl(fileLocation.append("/").append(fileName).toString());
		fileInfo.setFilePath(pdfFileName.toString());
		log.info(fileInfo.getFilePath());
		return fileInfo;
	}
	
	  static void deleteFolder(File file){
		  if(file.listFiles()!=null)
	      for (File subFile : file.listFiles()) {
	         if(subFile.isDirectory()) {
	            deleteFolder(subFile);
	         } else {
	            subFile.delete();
	         }
	      }
	      file.delete();
	   }

	public FileInfo financialReceiptGeneratHelper(Email email, FileInfo taxInvfileInfo, EmployeeFinancialServiceInfo employeeFinancialServiceInfo) 
			throws Exception {
		//log.info("***** Control inside the PdfHelper.financialReceiptGeneratHelper() *****");
		/*String transactionReceiptPdfFilePath = "";
		String transactionGetReceiptFileURL = "";*/
		//String deleteOldFiles = "";
		//boolean isThisUploadedData = employeeFinancialServiceInfo.isThisUplaodedData();

		//long portNumber = employeeFinancialServiceInfo.getPortNumber();
		final DemandNoteGeneratorInfo receiptGeneratorDnObject = email.getDemandNoteGeneratorInfoList().get(0);
		FinancialUploadDataInfo financialUploadDataInfo = email.getFinancialUploadDataRequests().get(0);
		//log.info(" ***** PdfHelper.financialReceiptGeneratHelper() ***** PORT number : "+portNumber);
		//Properties prop= responceCodesUtil.getApplicationProperties();
		
		/*transactionReceiptPdfFilePath = employeeFinancialServiceInfo.getFilePath(); 
		transactionGetReceiptFileURL = employeeFinancialServiceInfo.getFileUrl();*/ 
		
		/*if (portNumber == Long.valueOf(MetadataId.LOCAL_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath =responceCodesUtil.getApplicationNamePropeties("UAT_TRANSACTION_RECEIPT_PDF_PATH");
			transactionGetReceiptFileURL = responceCodesUtil.getApplicationNamePropeties("UAT_TRANSACTION_RECEIPT_PDF_URL");
		}else if (portNumber == Long.valueOf(MetadataId.UAT_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath =responceCodesUtil.getApplicationNamePropeties("UAT_TRANSACTION_RECEIPT_PDF_PATH");
			transactionGetReceiptFileURL = responceCodesUtil.getApplicationNamePropeties("UAT_TRANSACTION_RECEIPT_PDF_URL");
		} else if (portNumber == Long.valueOf(MetadataId.CUG_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath =responceCodesUtil.getApplicationNamePropeties("CUG_TRANSACTION_RECEIPT_PDF_PATH");
			transactionGetReceiptFileURL = responceCodesUtil.getApplicationNamePropeties("CUG_TRANSACTION_RECEIPT_PDF_URL");
		} else if (portNumber == Long.valueOf(MetadataId.LIVE_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath =responceCodesUtil.getApplicationNamePropeties("LIVE_TRANSACTION_RECEIPT_PDF_PATH");
			transactionGetReceiptFileURL = responceCodesUtil.getApplicationNamePropeties("LIVE_TRANSACTION_RECEIPT_PDF_URL");
		} else {
			log.error("Path missing to save the File. Please contact to Support Team");
			throw new EmployeeFinancialServiceException("Path missing to save the File, Please contact to Support Team.");
		}*/
		//deleteOldFiles =prop.getProperty("DELETE_GENERATED_TAX_INVOICE");
		//System.out.println(transactionReceiptPdfFilePath.equals(employeeFinancialServiceInfo.getFilePath()));
		//System.out.println(transactionGetReceiptFileURL.equals(employeeFinancialServiceInfo.getFileUrl()));
		final Document document = new Document(PageSize.A4, 25, 25, 100, 80);//this is  for footer and header

		
		final PdfWriter pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(taxInvfileInfo.getFilePath()));

		//USER_PASS = email.getDemandNoteGeneratorInfoList().get(0).getPancard();
		//OWNER_PASS = email.getDemandNoteGeneratorInfoList().get(0).getPancard();
		//adding password to the pdf.
		//pdfWriter.setEncryption(USER_PASS.getBytes(), OWNER_PASS.getBytes(), PdfWriter.ALLOW_PRINTING,PdfWriter.ENCRYPTION_AES_128);
			
		document.open();
		document.addAuthor("Amaravadhis software services private limited");
		document.addCreator("Aniket Chavan");
		document.addSubject(financialUploadDataInfo.getPdfTitle());
		document.addCreationDate();
		document.addTitle(financialUploadDataInfo.getPdfTitle());

		String headData = "";
		String footerData = "===";
		String rightLogo = Util.isNotEmptyObject(receiptGeneratorDnObject.getRightSidelogoFilePath())? receiptGeneratorDnObject.getRightSidelogoFilePath() : receiptGeneratorDnObject.getRightSidelogoForPdf();
		String leftLogo = Util.isNotEmptyObject(receiptGeneratorDnObject.getLeftSidelogoFilePath())? receiptGeneratorDnObject.getLeftSidelogoFilePath() : receiptGeneratorDnObject.getLeftSidelogoForPdf();
		Long headerTextHeight = 5l;
		Long footerTextHeight = 60l;
		Long fontSize = 7l;
		log.info("\n"+taxInvfileInfo.getFilePath()+"\t"+taxInvfileInfo.getUrl()+"\n rightLogo : "+rightLogo);
		final HeaderFooterPageEvent2 logo = new HeaderFooterPageEvent2();
		logo.setHeadData(headData);
		logo.setFooterData(footerData);
		logo.setRightLogo(rightLogo);
		logo.setLeftLogo(leftLogo);
		logo.setHeaderTextHeight(headerTextHeight);
		logo.setFooterTextHeight(footerTextHeight);
		logo.setFontSize(fontSize);
			
		/*final StringBuffer footerAdderss = new StringBuffer("");
	        
		if (receiptGeneratorDnObject.getCompanyCin()!=null && receiptGeneratorDnObject.getCompanyCin() != "-") {
			footerAdderss.append("Tel: ").append(receiptGeneratorDnObject.getCompanyTelephoneNo()).append(" CIN: "+receiptGeneratorDnObject.getCompanyCin()).append(" GSTIN: ").append(receiptGeneratorDnObject.getCompanyGstin()).append(" E-mail: "+receiptGeneratorDnObject.getCompanyEmail()).append(" Website: "+receiptGeneratorDnObject.getCompanyWebsite());
		} else {
			footerAdderss.append("Tel: ").append(receiptGeneratorDnObject.getCompanyTelephoneNo()).append(" LLPIN: "+receiptGeneratorDnObject.getCompanyLlpin()).append(" GSTIN: "+receiptGeneratorDnObject.getCompanyGstin()).append(" E-mail: "+receiptGeneratorDnObject.getCompanyEmail()).append(" Website: "+receiptGeneratorDnObject.getCompanyWebsite());
		}*/
	        
		pdfWriter.setPageEvent(logo);//.toUpperCase()
		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("", receiptGeneratorDnObject.getCompanyName(), "", 10l, 50l, 8l));
		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("", receiptGeneratorDnObject.getCompanyBillingAddress(), "", 20l, 40l, 7l));
		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("", getFooterAddress(receiptGeneratorDnObject), "", 30l, 30l, 7l));

		final com.itextpdf.tool.xml.XMLWorkerHelper worker = com.itextpdf.tool.xml.XMLWorkerHelper.getInstance();
			
		if(employeeFinancialServiceInfo.isThisUplaodedData()) {
				email.setTemplateName("/demandnotes/receiptTaxInvoiceUpload.vm");
		}
			
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("email", email);
		final String data = mailServiceImpl.geVelocityTemplateContent(model);
		//log.info(" ********************************************************************* ");

		worker.parseXHtml(pdfWriter, document, new StringReader(data));
		document.close();
		/*ExecutorService executorService = Executors.newFixedThreadPool(2);
		Future<Object> future = executorService.submit(new Callable<Object>(){
		    public Object call() throws Exception {
				worker.parseXHtml(pdfWriter, document, new StringReader(data));
				document.close();
		        return "Callable Result";
		    }
		});
		System.out.println("future.get() = " + future.get());*/
		
		//if (isThisUploadedData && "abc".equals("")) {
     		//try {	
				/*ExecutorService executorService = Executors.newFixedThreadPool(2);
				executorService.execute(new Runnable() {
					public void run() {
						try {
							worker.parseXHtml(pdfWriter, document, new StringReader(data));
							document.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
				executorService.shutdown();*/
			/*} catch (Exception e) {
				e.printStackTrace();
				executorService.shutdown();
				log.error("Failed to store file in drive, Please contact to Support Team.");
				throw new EmployeeFinancialServiceException("Failed to store file in drive, Please contact to Support Team.");
			}*/
		/*} else {
			worker.parseXHtml(pdfWriter, document, new StringReader(data));
			document.close();
		}*/
		//log.info(" ********************************************************************* ");
		return taxInvfileInfo;
	}

	  
	public void xMLWorkerHelperForCarParkingAllotmentLetter(Email email) throws IOException, DocumentException {
		log.info("***** Control is inside of the xMLWorkerHelperForCarParkingAllotmentLetter in PdfHelper***");
		CarParkingAllotmentPdfDetailInfo carParkingAllotmentPdfDetailInfo = email.getCarParkingAllotmentPdfDetailInfo();
        //final Document document = new Document(PageSize.A4, 25, 25, 30, 80);//this only for footer
		final Document document = new Document(PageSize.A4, 25, 25, 100, 80);//this is for footer and header
		final PdfWriter pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(carParkingAllotmentPdfDetailInfo.getAllotmentLetterFilePath()));
		
		document.open();
		document.addAuthor("Amaravadhis software services private limited");
		document.addCreator("Aniket Chavan");
		document.addSubject("Car Parking Allotment Letter");
		document.addCreationDate();
		document.addTitle("Car Parking Allotment Letter");
		final com.itextpdf.tool.xml.XMLWorkerHelper worker = com.itextpdf.tool.xml.XMLWorkerHelper.getInstance();
		
		email.setTemplateName("/vmtemplates/Car_Parking_Allotment_Letter.vm");
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("email", email);
		final String data = mailServiceImpl.geVelocityTemplateContent(model);
		log.info(" ********************************************************************* \n"+data);
		
		Rectangle headerBox = new Rectangle(36, 54, 559, 788);
		pdfWriter.setBoxSize("headerBox", headerBox);
		
		/* Header Logo Details */
		HeaderFooterPageEvent2 logo = new HeaderFooterPageEvent2();
		logo.setHeadData("");
		logo.setFooterData("===");
		logo.setRightLogo(carParkingAllotmentPdfDetailInfo.getRightSidelogoForPdf());
		logo.setLeftLogo(carParkingAllotmentPdfDetailInfo.getLeftSidelogoForPdf());
		logo.setHeaderTextHeight(5l);
		logo.setFooterTextHeight(60l);
		logo.setFontSize(7l);
		
		/* Footer Address Details */
		String footerAdderss = "";
		
		if(carParkingAllotmentPdfDetailInfo.getCompanyCin() != "-"){
			footerAdderss = "Tel: "+carParkingAllotmentPdfDetailInfo.getCompanyTelephoneNo()+" CIN: "+carParkingAllotmentPdfDetailInfo.getCompanyCin()+" GSTIN: "+carParkingAllotmentPdfDetailInfo.getCompanyGstin()+" E-mail: "+carParkingAllotmentPdfDetailInfo.getCompanyEmail()+" Website: "+carParkingAllotmentPdfDetailInfo.getCompanyWebsite();
		}else {
			footerAdderss = "Tel: "+carParkingAllotmentPdfDetailInfo.getCompanyTelephoneNo()+" LLPIN: "+carParkingAllotmentPdfDetailInfo.getCompanyLlpin()+" GSTIN: "+carParkingAllotmentPdfDetailInfo.getCompanyGstin()+" E-mail: "+carParkingAllotmentPdfDetailInfo.getCompanyEmail()+" Website: "+carParkingAllotmentPdfDetailInfo.getCompanyWebsite();
		}
		
		//for header third parameter "abc"
		//for footer fourth parameter
		//pdfWriter.setPageEvent(new HeaderFooterPageEvent2("","===",carParkingAllotmentPdfDetailInfo.getLogoForPdf(),5l,60l,7l));
		pdfWriter.setPageEvent(logo);
		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",carParkingAllotmentPdfDetailInfo.getCompanyName(),"",10l,50l,8l));
		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",carParkingAllotmentPdfDetailInfo.getCompanyBillingAddress(),"",20l,40l,7l));
		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",footerAdderss,"",30l,30l,7l));
		        
		worker.parseXHtml(pdfWriter, document, new StringReader(data));
		document.close();
		pdfWriter.close();
		log.info("*** The Pdf File "+carParkingAllotmentPdfDetailInfo.getAllotmentLetterFilePath()+" is created successfully ***");
	}
	
	public void XMLWorkerHelperForNOCLetter_Handing_Over_documents(Email email, FileInfo fileInfo) throws Exception {
		log.info("***** Control inside the PdfHelper.XMLWorkerHelperForNOCLetter() *****");
		DemandNoteGeneratorInfo demandNoteInfoObject = email.getDemandNoteGeneratorInfo();
		String NOCPdfFileName = demandNoteInfoObject.getDemandNotePdfFileName();
		NOCPdfFileName=NOCPdfFileName+"_Handing_Over_documents.pdf";
		String transactionReceiptPdfFilePath = "";
		String transactionGetReceiptFileURL = "";
		long bookingFormId = demandNoteInfoObject.getBookingFormId();
		long portNumber = email.getPortNumber();
		log.info(" ***** PdfHelper.XMLWorkerHelperForNOCLetter() ***** PORT number : "+portNumber+"\t Booking Form Id :"+bookingFormId);
		
		transactionReceiptPdfFilePath = demandNoteInfoObject.getFolderFilePath();
		transactionGetReceiptFileURL = demandNoteInfoObject.getFolderFileUrl();
		//final Document document = new Document(PageSize.A4, 25, 25, 105, 80);// this is for footer and header
		//final Document document = new Document(PageSize.A4, 25, 25, 105, 110);// this is for footer and header
		//final Document document = new Document(PageSize.A4);
		final Document document = new Document(PageSize.A4, 25, 25, 85, 25);// this is for footer and header
		String path = transactionReceiptPdfFilePath + demandNoteInfoObject.getSiteId() + "/" + bookingFormId + "/";
		String fileName = getFileName(path, NOCPdfFileName);
			
		StringBuilder pdfFileName = new StringBuilder(transactionReceiptPdfFilePath)
				.append(demandNoteInfoObject.getSiteId()).append("/").append(bookingFormId).append("/")
				.append(fileName);

		StringBuilder urlPath = new StringBuilder(transactionGetReceiptFileURL).append(demandNoteInfoObject.getSiteId())
				.append("/").append(bookingFormId).append("/").append(fileName);

		fileInfo.setName(fileName);
		fileInfo.setUrl(urlPath.toString());
		fileInfo.setFilePath(pdfFileName.toString());
		fileInfo.setExtension(pdfFileName.substring(pdfFileName.lastIndexOf(".") + 1));
		log.info(pdfFileName + "\t" + urlPath);
		final PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFileName.toString()));

		document.open();
		document.addAuthor("Amaravadhis software services private limited");
		document.addCreator("Aniket Chavan");
		document.addSubject(" NOC Letter ");
		document.addCreationDate();
		document.addTitle(" NOC Letter ");
		final com.itextpdf.tool.xml.XMLWorkerHelper worker = com.itextpdf.tool.xml.XMLWorkerHelper.getInstance();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("email", email);
		final String data = mailServiceImpl.geVelocityTemplateContent(model);
		log.info(" ********************************************************************* \n" + data);

		Rectangle headerBox = new Rectangle(36, 54, 559, 788);
		pdfWriter.setBoxSize("headerBox", headerBox);
	 
		worker.parseXHtml(pdfWriter, document, new StringReader(data));
		document.close();
		pdfWriter.close();

		log.info("*** The Pdf File " + pdfFileName + " is created successfully ***");
	}

	public void XMLWorkerHelperForNOCLetter_Taken_Over_Documents(Email email, FileInfo fileInfo) throws Exception {
		log.info("***** Control inside the PdfHelper.XMLWorkerHelperForNOCLetter() *****");
		DemandNoteGeneratorInfo demandNoteInfoObject = email.getDemandNoteGeneratorInfo();
		String NOCPdfFileName = demandNoteInfoObject.getDemandNotePdfFileName();
		NOCPdfFileName=NOCPdfFileName+"_Taken_Over_Documents.pdf";
		String transactionReceiptPdfFilePath = "";
		String transactionGetReceiptFileURL = "";
		long bookingFormId = demandNoteInfoObject.getBookingFormId();
		long portNumber = email.getPortNumber();
		log.info(" ***** PdfHelper.XMLWorkerHelperForNOCLetter() ***** PORT number : "+portNumber+"\t Booking Form Id :"+bookingFormId);
		
		transactionReceiptPdfFilePath = demandNoteInfoObject.getFolderFilePath();
		transactionGetReceiptFileURL = demandNoteInfoObject.getFolderFileUrl();
		//final Document document = new Document(PageSize.A4, 25, 25, 105, 80);// this is for footer and header
		//final Document document = new Document(PageSize.A4, 25, 25, 105, 110);// this is for footer and header
		//final Document document = new Document(PageSize.A4);
		final Document document = new Document(PageSize.A4, 25, 25, 70, 25);// this is for footer and header
		String path = transactionReceiptPdfFilePath + demandNoteInfoObject.getSiteId() + "/" + bookingFormId + "/";
		String fileName = getFileName(path, NOCPdfFileName);
			
		StringBuilder pdfFileName = new StringBuilder(transactionReceiptPdfFilePath)
				.append(demandNoteInfoObject.getSiteId()).append("/").append(bookingFormId).append("/")
				.append(fileName);

		StringBuilder urlPath = new StringBuilder(transactionGetReceiptFileURL).append(demandNoteInfoObject.getSiteId())
				.append("/").append(bookingFormId).append("/").append(fileName);

		fileInfo.setName(fileName);
		fileInfo.setUrl(urlPath.toString());
		fileInfo.setFilePath(pdfFileName.toString());
		fileInfo.setExtension(pdfFileName.substring(pdfFileName.lastIndexOf(".") + 1));
		log.info(pdfFileName + "\t" + urlPath);
		final PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFileName.toString()));

		document.open();
		document.addAuthor("Amaravadhis software services private limited");
		document.addCreator("Aniket Chavan");
		document.addSubject(" NOC Letter ");
		document.addCreationDate();
		document.addTitle(" NOC Letter ");
		final com.itextpdf.tool.xml.XMLWorkerHelper worker = com.itextpdf.tool.xml.XMLWorkerHelper.getInstance();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("email", email);
		final String data = mailServiceImpl.geVelocityTemplateContent(model);
		log.info(" ********************************************************************* \n" + data);

		Rectangle headerBox = new Rectangle(36, 54, 559, 788);
		pdfWriter.setBoxSize("headerBox", headerBox);
	 
		worker.parseXHtml(pdfWriter, document, new StringReader(data));
		document.close();
		pdfWriter.close();

		log.info("*** The Pdf File " + pdfFileName + " is created successfully ***");
	}
	public void XMLWorkerHelperForNOCLetter_Bescom(Email email, FileInfo fileInfo) throws Exception {
		log.info("***** Control inside the PdfHelper.XMLWorkerHelperForNOCLetter() *****");
		DemandNoteGeneratorInfo demandNoteInfoObject = email.getDemandNoteGeneratorInfo();
		String NOCPdfFileName = demandNoteInfoObject.getDemandNotePdfFileName();
		NOCPdfFileName=NOCPdfFileName+"_Bescom.pdf";
		String transactionReceiptPdfFilePath = "";
		String transactionGetReceiptFileURL = "";
		long bookingFormId = demandNoteInfoObject.getBookingFormId();
		long portNumber = email.getPortNumber();
		log.info(" ***** PdfHelper.XMLWorkerHelperForNOCLetter() ***** PORT number : "+portNumber+"\t Booking Form Id :"+bookingFormId);
		
		transactionReceiptPdfFilePath = demandNoteInfoObject.getFolderFilePath();
		transactionGetReceiptFileURL = demandNoteInfoObject.getFolderFileUrl();
		//final Document document = new Document(PageSize.A4, 25, 25, 105, 80);// this is for footer and header
		//final Document document = new Document(PageSize.A4, 25, 25, 105, 110);// this is for footer and header
		//final Document document = new Document(PageSize.A4);
		final Document document = new Document(PageSize.A4, 25, 25, 85, 25);// this is for footer and header
		String path = transactionReceiptPdfFilePath + demandNoteInfoObject.getSiteId() + "/" + bookingFormId + "/";
		String fileName = getFileName(path, NOCPdfFileName);
			
		StringBuilder pdfFileName = new StringBuilder(transactionReceiptPdfFilePath)
				.append(demandNoteInfoObject.getSiteId()).append("/").append(bookingFormId).append("/")
				.append(fileName);

		StringBuilder urlPath = new StringBuilder(transactionGetReceiptFileURL).append(demandNoteInfoObject.getSiteId())
				.append("/").append(bookingFormId).append("/").append(fileName);

		fileInfo.setName(fileName);
		fileInfo.setUrl(urlPath.toString());
		fileInfo.setFilePath(pdfFileName.toString());
		fileInfo.setExtension(pdfFileName.substring(pdfFileName.lastIndexOf(".") + 1));
		log.info(pdfFileName + "\t" + urlPath);
		final PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFileName.toString()));

		document.open();
		document.addAuthor("Amaravadhis software services private limited");
		document.addCreator("Aniket Chavan");
		document.addSubject(" NOC Letter ");
		document.addCreationDate();
		document.addTitle(" NOC Letter ");
		final com.itextpdf.tool.xml.XMLWorkerHelper worker = com.itextpdf.tool.xml.XMLWorkerHelper.getInstance();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("email", email);
		final String data = mailServiceImpl.geVelocityTemplateContent(model);
		log.info(" ********************************************************************* \n" + data);

		Rectangle headerBox = new Rectangle(36, 54, 559, 788);
		pdfWriter.setBoxSize("headerBox", headerBox);
	 
		worker.parseXHtml(pdfWriter, document, new StringReader(data));
		document.close();
		pdfWriter.close();

		log.info("*** The Pdf File " + pdfFileName + " is created successfully ***");
	}
	
	public void XMLWorkerHelperForCostBreakUpLetterFolium(Email email, FileInfo fileInfo) throws Exception {
		log.info("***** Control inside the PdfHelper.XMLWorkerHelperForCostBreakUpLetter() *****");
		DemandNoteGeneratorInfo demandNoteInfoObject = email.getDemandNoteGeneratorInfo();
		String allotmentLetterPdfFileName = demandNoteInfoObject.getDemandNotePdfFileName();
		String transactionReceiptPdfFilePath = "";
		String transactionGetReceiptFileURL = "";
		//String appovals=null;
		long bookingFormId = demandNoteInfoObject.getBookingFormId();
		long portNumber = email.getPortNumber();
		log.info(" ***** PdfHelper.XMLWorkerHelperForCostBreakUpLetter() ***** PORT number : "+portNumber+"\t Booking Form Id :"+bookingFormId);
		//Properties prop= responceCodesUtil.getApplicationProperties();
		transactionReceiptPdfFilePath = demandNoteInfoObject.getFolderFilePath();
		transactionGetReceiptFileURL = demandNoteInfoObject.getFolderFileUrl();
/*		if (portNumber == Long.valueOf(MetadataId.LOCAL_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("DOCUMENTS_FILE_DIRECTORY_PATH");
			transactionGetReceiptFileURL = prop.getProperty("DOCUMENTS_FILE_URL_PATH");
		} else if (portNumber == Long.valueOf(MetadataId.UAT_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("DOCUMENTS_FILE_DIRECTORY_PATH");
			transactionGetReceiptFileURL = prop.getProperty("DOCUMENTS_FILE_URL_PATH");
		} else if (portNumber == Long.valueOf(MetadataId.CUG_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("CUG_ALLOTMENT_LETTER_PDF_PATH");
			transactionGetReceiptFileURL = prop.getProperty("CUG_ALLOTMENT_LETTER_PDF_URL");
		} else if (portNumber == Long.valueOf(MetadataId.LIVE_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("LIVE_ALLOTMENT_LETTER_PDF_PATH");
			transactionGetReceiptFileURL = prop.getProperty("LIVE_ALLOTMENT_LETTER_PDF_URL");
		} else {
			log.error("Path missing to save the File. Please contact to Support Team");
			throw new EmployeeFinancialServiceException("Path missing to save the File, Please contact to Support Team.");
		}
		if(transactionReceiptPdfFilePath==null || transactionReceiptPdfFilePath.isEmpty()) {
			throw new EmployeeFinancialServiceException("File path found empty for storing pdf's.");
		}	
*/		//final Document document = new Document(PageSize.A4, 25, 25, 105, 80);// this is for footer and header
		final Document document = new Document(PageSize.A4, 25, 25, 105, 110);// this is for footer and header
		String path = transactionReceiptPdfFilePath + demandNoteInfoObject.getSiteId() + "/" + bookingFormId + "/";
		String fileName = getFileName(path, allotmentLetterPdfFileName);
			
		StringBuilder pdfFileName = new StringBuilder(transactionReceiptPdfFilePath)
				.append(demandNoteInfoObject.getSiteId()).append("/").append(bookingFormId).append("/")
				.append(fileName);

		StringBuilder urlPath = new StringBuilder(transactionGetReceiptFileURL).append(demandNoteInfoObject.getSiteId())
				.append("/").append(bookingFormId).append("/").append(fileName);

		fileInfo.setName(fileName);
		fileInfo.setUrl(urlPath.toString());
		fileInfo.setFilePath(pdfFileName.toString());
		fileInfo.setExtension(pdfFileName.substring(pdfFileName.lastIndexOf(".") + 1));
		log.info(pdfFileName + "\t" + urlPath);
		final PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFileName.toString()));

		document.open();
		document.addAuthor("Amaravadhis software services private limited");
		document.addCreator("Aniket Chavan");
		document.addSubject(" Cost Break Up Letter ");
		document.addCreationDate();
		document.addTitle(" Cost Break Up Letter ");
		final com.itextpdf.tool.xml.XMLWorkerHelper worker = com.itextpdf.tool.xml.XMLWorkerHelper.getInstance();

		//email.setTemplateName("/vmtemplates/COST_BREAK_UP_AND_PAYMENT_SCHEDULE.vm");
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("email", email);
		final String data = mailServiceImpl.geVelocityTemplateContent(model);
		log.info(" ********************************************************************* \n" + data);

		Rectangle headerBox = new Rectangle(36, 54, 559, 788);
		pdfWriter.setBoxSize("headerBox", headerBox);

		String footerAdderss = "";
		
		
		if (Util.isNotEmptyObject(demandNoteInfoObject.getSiteId()) && demandNoteInfoObject.getSiteId().equals("126")) {
		    if (demandNoteInfoObject.getCompanyCin() != "-") {
	       	footerAdderss = " GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" CIN: "+demandNoteInfoObject.getCompanyCin()+" Website: "+demandNoteInfoObject.getCompanyWebsite();
	        } else {
	       	footerAdderss = " LLPIN: "+demandNoteInfoObject.getCompanyLlpin()+" GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" Website: "+demandNoteInfoObject.getCompanyWebsite();
	        }
		}else if (Util.isNotEmptyObject(demandNoteInfoObject.getSiteId()) && demandNoteInfoObject.getSiteId().equals("114")) {
		    if (demandNoteInfoObject.getCompanyCin() != "-") {
		       	footerAdderss = "GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" CIN: "+demandNoteInfoObject.getCompanyCin()+" URL: "+demandNoteInfoObject.getCompanyWebsite();
		        } else {
		       	footerAdderss = "LLPIN: "+demandNoteInfoObject.getCompanyLlpin()+" GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" URL: "+demandNoteInfoObject.getCompanyWebsite();
		        }
		} else {
			 if (demandNoteInfoObject.getCompanyCin() != "-") {
			       	footerAdderss = "Tel: "+demandNoteInfoObject.getCompanyTelephoneNo()+" CIN: "+demandNoteInfoObject.getCompanyCin()+" GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" E-mail: "+demandNoteInfoObject.getCompanyEmail()+" Website: "+demandNoteInfoObject.getCompanyWebsite();
			        } else {
			       	footerAdderss = "Tel: "+demandNoteInfoObject.getCompanyTelephoneNo()+" LLPIN: "+demandNoteInfoObject.getCompanyLlpin()+" GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" E-mail: "+demandNoteInfoObject.getCompanyEmail()+" Website: "+demandNoteInfoObject.getCompanyWebsite();
			        }
		}
	        
		String headData = "";
		String footerData = "===";
		String rightLogo = Util.isNotEmptyObject(demandNoteInfoObject.getRightSidelogoFilePath())? demandNoteInfoObject.getRightSidelogoFilePath() : demandNoteInfoObject.getRightSidelogoForPdf();
		String leftLogo = Util.isNotEmptyObject(demandNoteInfoObject.getLeftSidelogoFilePath())? demandNoteInfoObject.getLeftSidelogoFilePath() : demandNoteInfoObject.getLeftSidelogoForPdf();
		Long headerTextHeight = 5l;
		Long footerTextHeight = 60l;
		Long fontSize = 7l;

		HeaderFooterPageEvent2 logo = new HeaderFooterPageEvent2();
		logo.setHeadData(headData);
		logo.setFooterData(footerData);
		logo.setRightLogo(rightLogo);
		logo.setLeftLogo(leftLogo);
		logo.setHeaderTextHeight(headerTextHeight);
		logo.setFooterTextHeight(footerTextHeight);
		logo.setFontSize(fontSize);

        pdfWriter.setPageEvent(logo);
        
        /* Malladi Changes */
//        if(!("131").equals(demandNoteInfoObject.getSiteId())) {
//        	appovals="    Prepared By                           Authorised By                           Approved By                           Customer/s Signature";
//        	pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",appovals,"",10l,35l,10l));
//        }
        
        
        
    	if (Util.isNotEmptyObject(demandNoteInfoObject.getSiteId()) && demandNoteInfoObject.getSiteId().equals("126")) {
    		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("","                                                                           "+demandNoteInfoObject.getCompanyName(),"",10l,20l,8l));
            pdfWriter.setPageEvent(new HeaderFooterPageEvent2("   ","                   "+demandNoteInfoObject.getCompanyBillingAddress(),"",20l,10l,7l));
    		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("  ","                             "+footerAdderss,"",30l,0l,7l));
    	}else {
        	pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",demandNoteInfoObject.getCompanyName(),"",10l,20l,8l));
    	    pdfWriter.setPageEvent(new HeaderFooterPageEvent2("  ",""+demandNoteInfoObject.getCompanyBillingAddress(),"  ",15l,10l,6l));
    	    pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",footerAdderss,"",30l,0l,6l));
        }
    	
        //pdfWriter.setPageEvent(new HeaderFooterPageEvent2("","===",demandNoteInfoObject.getRightSidelogoForPdf(),5l,60l,7l));
		
			        
		/*ExecutorService executorService = Executors.newFixedThreadPool(5);
		try {
			executorService.execute(new Runnable() {
				public void run() {
					try {*/
						worker.parseXHtml(pdfWriter, document, new StringReader(data));
						document.close();
						pdfWriter.close();
					/*} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			executorService.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
			executorService.shutdown();
		}*/

		log.info("*** The Pdf File " + pdfFileName + " is created successfully ***");
	}
	public void XMLWorkerHelperForAgreementDraftLetterFolium(Email email, FileInfo fileInfo) throws Exception {
		log.info("***** Control inside the PdfHelper.XMLWorkerHelperForAgreementDraftLetter() *****");
		DemandNoteGeneratorInfo demandNoteInfoObject = email.getDemandNoteGeneratorInfo();
		//String allotmentLetterPdfFileName = demandNoteInfoObject.getDemandNotePdfFileName();
		String PdfFilePath = "";
		String PdfFileURL = "";
		long bookingFormId = demandNoteInfoObject.getBookingFormId();
		long portNumber = email.getPortNumber();
		log.info(" ***** PdfHelper.XMLWorkerHelperForAgreementDraftLetter() ***** PORT number : "+portNumber+"\t Booking Form Id :"+bookingFormId);
		//Properties prop= responceCodesUtil.getApplicationProperties();
		PdfFilePath = demandNoteInfoObject.getFolderFilePath();
		PdfFileURL = demandNoteInfoObject.getFolderFileUrl();
		/*if (portNumber == Long.valueOf(MetadataId.LOCAL_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("DOCUMENTS_FILE_DIRECTORY_PATH");
			transactionGetReceiptFileURL = prop.getProperty("DOCUMENTS_FILE_URL_PATH");
		} else if (portNumber == Long.valueOf(MetadataId.UAT_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("DOCUMENTS_FILE_DIRECTORY_PATH");
			transactionGetReceiptFileURL = prop.getProperty("DOCUMENTS_FILE_URL_PATH");
		} else if (portNumber == Long.valueOf(MetadataId.CUG_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("CUG_ALLOTMENT_LETTER_PDF_PATH");
			transactionGetReceiptFileURL = prop.getProperty("CUG_ALLOTMENT_LETTER_PDF_URL");
		} else if (portNumber == Long.valueOf(MetadataId.LIVE_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("LIVE_ALLOTMENT_LETTER_PDF_PATH");
			transactionGetReceiptFileURL = prop.getProperty("LIVE_ALLOTMENT_LETTER_PDF_URL");
		} else {
			log.error("Path missing to save the File. Please contact to Support Team");
			throw new EmployeeFinancialServiceException("Path missing to save the File, Please contact to Support Team.");
		}
		if(transactionReceiptPdfFilePath==null || transactionReceiptPdfFilePath.isEmpty()) {
			throw new EmployeeFinancialServiceException("File path found empty for storing pdf's.");
		}*/	
		//final Document document = new Document(PageSize.A4, 20, 20, 80, 80);// this is for footer and header
		//final Document document = new Document(PageSize.A4, 25, 25, 50, 50);//this is  for footer and header
		String path = "";
		
		path = PdfFilePath + demandNoteInfoObject.getSiteId() + "/" + bookingFormId + "/";
		
		//String fileName = getFileName(path, allotmentLetterPdfFileName);
		String fileName = "Folium by Sumadhura Phase I  Agreement.pdf";
			
		StringBuilder pdfFileName = new StringBuilder(PdfFilePath)
			.append(demandNoteInfoObject.getSiteId()).append("/").append(bookingFormId).append("/")
				.append(fileName);

		StringBuilder urlPath = new StringBuilder(PdfFileURL).append(demandNoteInfoObject.getSiteId())
				.append("/").append(bookingFormId).append("/").append(fileName);

		fileInfo.setName(fileName);
		fileInfo.setUrl(urlPath.toString());
		fileInfo.setFilePath(pdfFileName.toString());
		fileInfo.setExtension(pdfFileName.substring(pdfFileName.lastIndexOf(".") + 1));
		
		
		StringBuilder pdfFileName1 = new StringBuilder(PdfFilePath)
				.append(demandNoteInfoObject.getSiteId()).append("/").append(bookingFormId).append("/");
		File source = new File("D:\\CustomerApp_CUG\\images\\sumadhura_projects_images\\aggrementLetter");
		File dest = new File(pdfFileName1.toString());
		try {
		    FileUtils.copyDirectory(source, dest);
		} catch (IOException e) {
		    e.printStackTrace();
		}

//		final PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFileName.toString()));
//
//		document.open();
//		document.addAuthor("Amaravadhis software services private limited");
//		document.addCreator("Aniket Chavan");
//		document.addSubject("Agreement Draft");
//		document.addTitle("Agreement Draft");
//		document.addCreationDate();
//		
//		final com.itextpdf.tool.xml.XMLWorkerHelper worker = com.itextpdf.tool.xml.XMLWorkerHelper.getInstance();

		//email.setTemplateName("/vmtemplates/AgreementDraft.vm");
		/*File file = new File("D:\\CUSTOMERAPP_UAT\\images\\sumadhura_projects_images\\Agreement_draft_letter\\AgreementDraft.vm");
		if(file.exists()) {
			System.out.println("***** Control inside the PdfHelper.XMLWorkerHelperForAgreementDraftLetter() *****");
		}
//		email.setTemplateName("AgreementDraft.vm");*/
//		Map<String, Object> model = new HashMap<String, Object>();
//		model.put("email", email);
//		model.put("StringUtils", new Util());
//		model.put("DateUtils", new TimeUtil());
//		model.put("CurrencyUtil", new CurrencyUtil());
//		model.put("EmployeeFinancialHelper", new EmployeeFinancialHelper());
//		
//		final String data = mailServiceImpl.geVelocityTemplateContent(model);
//		log.info(pdfFileName+" ********************************************************************* \n" + data);
//		log.info(pdfFileName + "\t" + urlPath);
//		Rectangle headerBox = new Rectangle(36, 54, 559, 788);
//		pdfWriter.setBoxSize("headerBox", headerBox);

		/*String footerAdderss = "";
		if (demandNoteInfoObject.getCompanyCin() != "-") {
	       	footerAdderss = "Tel: "+demandNoteInfoObject.getCompanyTelephoneNo()+" CIN: "+demandNoteInfoObject.getCompanyCin()+" GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" E-mail: "+demandNoteInfoObject.getCompanyEmail()+" Website: "+demandNoteInfoObject.getCompanyWebsite();
	    } else {
	       	footerAdderss = "Tel: "+demandNoteInfoObject.getCompanyTelephoneNo()+" LLPIN: "+demandNoteInfoObject.getCompanyLlpin()+" GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" E-mail: "+demandNoteInfoObject.getCompanyEmail()+" Website: "+demandNoteInfoObject.getCompanyWebsite();
	    }*/
//	        
//		String headData = "";
//		String footerData = "===";
//		String rightLogo = demandNoteInfoObject.getRightSidelogoForPdf();
//		String leftLogo =  demandNoteInfoObject.getLeftSidelogoForPdf();
//		Long headerTextHeight = 5l;
//		Long footerTextHeight = 60l;
//		Long fontSize = 7l;
//
//		HeaderFooterPageEvent2 logo = new HeaderFooterPageEvent2();
//		logo.setHeadData(headData);
//		logo.setFooterData(footerData);
//		logo.setRightLogo(rightLogo);
//		logo.setLeftLogo(leftLogo);
//		logo.setHeaderTextHeight(headerTextHeight);
//		logo.setFooterTextHeight(footerTextHeight);
//		logo.setFontSize(fontSize);

        /*pdfWriter.setPageEvent(logo);
        //pdfWriter.setPageEvent(new HeaderFooterPageEvent2("","===",demandNoteInfoObject.getRightSidelogoForPdf(),5l,60l,7l));
		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",demandNoteInfoObject.getCompanyName(),"",10l,50l,8l));
		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",demandNoteInfoObject.getCompanyBillingAddress(),"",20l,40l,7l));
		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",footerAdderss,"",30l,30l,7l));*/
			        
		/*ExecutorService executorService = Executors.newFixedThreadPool(5);
		try {
			executorService.execute(new Runnable() {
				public void run() {
					try {*/
//						worker.parseXHtml(pdfWriter, document, new StringReader(data));
//						document.close();
//						pdfWriter.close();
					/*} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			executorService.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
			executorService.shutdown();
		}*/

		log.info("*** The Pdf File " + pdfFileName + " is created successfully ***");
	}

	
	public void XMLWorkerHelperForAgreementDraftLetterAmber(Email email, FileInfo fileInfo) throws Exception {
		log.info("***** Control inside the PdfHelper.XMLWorkerHelperForAgreementDraftLetter() *****");
		DemandNoteGeneratorInfo demandNoteInfoObject = email.getDemandNoteGeneratorInfo();
		//String allotmentLetterPdfFileName = demandNoteInfoObject.getDemandNotePdfFileName();
		String PdfFilePath = "";
		String PdfFileURL = "";
		long bookingFormId = demandNoteInfoObject.getBookingFormId();
		long portNumber = email.getPortNumber();
		log.info(" ***** PdfHelper.XMLWorkerHelperForAgreementDraftLetter() ***** PORT number : "+portNumber+"\t Booking Form Id :"+bookingFormId);
		//Properties prop= responceCodesUtil.getApplicationProperties();
		PdfFilePath = demandNoteInfoObject.getFolderFilePath();
		PdfFileURL = demandNoteInfoObject.getFolderFileUrl();
		/*if (portNumber == Long.valueOf(MetadataId.LOCAL_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("DOCUMENTS_FILE_DIRECTORY_PATH");
			transactionGetReceiptFileURL = prop.getProperty("DOCUMENTS_FILE_URL_PATH");
		} else if (portNumber == Long.valueOf(MetadataId.UAT_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("DOCUMENTS_FILE_DIRECTORY_PATH");
			transactionGetReceiptFileURL = prop.getProperty("DOCUMENTS_FILE_URL_PATH");
		} else if (portNumber == Long.valueOf(MetadataId.CUG_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("CUG_ALLOTMENT_LETTER_PDF_PATH");
			transactionGetReceiptFileURL = prop.getProperty("CUG_ALLOTMENT_LETTER_PDF_URL");
		} else if (portNumber == Long.valueOf(MetadataId.LIVE_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("LIVE_ALLOTMENT_LETTER_PDF_PATH");
			transactionGetReceiptFileURL = prop.getProperty("LIVE_ALLOTMENT_LETTER_PDF_URL");
		} else {
			log.error("Path missing to save the File. Please contact to Support Team");
			throw new EmployeeFinancialServiceException("Path missing to save the File, Please contact to Support Team.");
		}
		if(transactionReceiptPdfFilePath==null || transactionReceiptPdfFilePath.isEmpty()) {
			throw new EmployeeFinancialServiceException("File path found empty for storing pdf's.");
		}*/	
		//final Document document = new Document(PageSize.A4, 20, 20, 80, 80);// this is for footer and header
		final Document document = new Document(PageSize.A4, 25, 25, 50, 50);//this is  for footer and header
		String path = "";
		
		path = PdfFilePath + demandNoteInfoObject.getSiteId() + "/" + bookingFormId + "/";
		
		//String fileName = getFileName(path, allotmentLetterPdfFileName);
		String fileName = "Aspire Amber Agreement.pdf";
			
		StringBuilder pdfFileName = new StringBuilder(PdfFilePath)
			.append(demandNoteInfoObject.getSiteId()).append("/").append(bookingFormId).append("/")
				.append(fileName);

		StringBuilder urlPath = new StringBuilder(PdfFileURL).append(demandNoteInfoObject.getSiteId())
				.append("/").append(bookingFormId).append("/").append(fileName);

		fileInfo.setName(fileName);
		fileInfo.setUrl(urlPath.toString());
		fileInfo.setFilePath(pdfFileName.toString());
		fileInfo.setExtension(pdfFileName.substring(pdfFileName.lastIndexOf(".") + 1));
		
		
		StringBuilder pdfFileName1 = new StringBuilder(PdfFilePath)
				.append(demandNoteInfoObject.getSiteId()).append("/").append(bookingFormId).append("/");
		File source = new File("D:\\CustomerApp_CUG\\images\\sumadhura_projects_images\\AgreementDraft\\AmberAggrementLetter");
		File dest = new File(pdfFileName1.toString());
		try {
		    FileUtils.copyDirectory(source, dest);
		} catch (IOException e) {
		    e.printStackTrace();
		}

//		final PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFileName.toString()));
//
//		document.open();
//		document.addAuthor("Amaravadhis software services private limited");
//		document.addCreator("Aniket Chavan");
//		document.addSubject("Agreement Draft");
//		document.addTitle("Agreement Draft");
//		document.addCreationDate();
//		
//		final com.itextpdf.tool.xml.XMLWorkerHelper worker = com.itextpdf.tool.xml.XMLWorkerHelper.getInstance();

		//email.setTemplateName("/vmtemplates/AgreementDraft.vm");
		/*File file = new File("D:\\CUSTOMERAPP_UAT\\images\\sumadhura_projects_images\\Agreement_draft_letter\\AgreementDraft.vm");
		if(file.exists()) {
			System.out.println("***** Control inside the PdfHelper.XMLWorkerHelperForAgreementDraftLetter() *****");
		}
//		email.setTemplateName("AgreementDraft.vm");*/
//		Map<String, Object> model = new HashMap<String, Object>();
//		model.put("email", email);
//		model.put("StringUtils", new Util());
//		model.put("DateUtils", new TimeUtil());
//		model.put("CurrencyUtil", new CurrencyUtil());
//		model.put("EmployeeFinancialHelper", new EmployeeFinancialHelper());
//		
//		final String data = mailServiceImpl.geVelocityTemplateContent(model);
//		log.info(pdfFileName+" ********************************************************************* \n" + data);
//		log.info(pdfFileName + "\t" + urlPath);
//		Rectangle headerBox = new Rectangle(36, 54, 559, 788);
//		pdfWriter.setBoxSize("headerBox", headerBox);

		/*String footerAdderss = "";
		if (demandNoteInfoObject.getCompanyCin() != "-") {
	       	footerAdderss = "Tel: "+demandNoteInfoObject.getCompanyTelephoneNo()+" CIN: "+demandNoteInfoObject.getCompanyCin()+" GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" E-mail: "+demandNoteInfoObject.getCompanyEmail()+" Website: "+demandNoteInfoObject.getCompanyWebsite();
	    } else {
	       	footerAdderss = "Tel: "+demandNoteInfoObject.getCompanyTelephoneNo()+" LLPIN: "+demandNoteInfoObject.getCompanyLlpin()+" GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" E-mail: "+demandNoteInfoObject.getCompanyEmail()+" Website: "+demandNoteInfoObject.getCompanyWebsite();
	    }*/
//	        
//		String headData = "";
//		String footerData = "===";
//		String rightLogo = demandNoteInfoObject.getRightSidelogoForPdf();
//		String leftLogo =  demandNoteInfoObject.getLeftSidelogoForPdf();
//		Long headerTextHeight = 5l;
//		Long footerTextHeight = 60l;
//		Long fontSize = 7l;
//
//		HeaderFooterPageEvent2 logo = new HeaderFooterPageEvent2();
//		logo.setHeadData(headData);
//		logo.setFooterData(footerData);
//		logo.setRightLogo(rightLogo);
//		logo.setLeftLogo(leftLogo);
//		logo.setHeaderTextHeight(headerTextHeight);
//		logo.setFooterTextHeight(footerTextHeight);
//		logo.setFontSize(fontSize);

        /*pdfWriter.setPageEvent(logo);
        //pdfWriter.setPageEvent(new HeaderFooterPageEvent2("","===",demandNoteInfoObject.getRightSidelogoForPdf(),5l,60l,7l));
		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",demandNoteInfoObject.getCompanyName(),"",10l,50l,8l));
		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",demandNoteInfoObject.getCompanyBillingAddress(),"",20l,40l,7l));
		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",footerAdderss,"",30l,30l,7l));*/
			        
		/*ExecutorService executorService = Executors.newFixedThreadPool(5);
		try {
			executorService.execute(new Runnable() {
				public void run() {
					try {*/
//						worker.parseXHtml(pdfWriter, document, new StringReader(data));
//						document.close();
//						pdfWriter.close();
					/*} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			executorService.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
			executorService.shutdown();
		}*/

		log.info("*** The Pdf File " + pdfFileName + " is created successfully ***");
	}

	public void XMLWorkerHelperForAgreementDraftLetterOlympus(Email email, FileInfo fileInfo) throws Exception {
		log.info("***** Control inside the PdfHelper.XMLWorkerHelperForAgreementDraftLetter() *****");
		DemandNoteGeneratorInfo demandNoteInfoObject = email.getDemandNoteGeneratorInfo();
		//String allotmentLetterPdfFileName = demandNoteInfoObject.getDemandNotePdfFileName();
		String PdfFilePath = "";
		String PdfFileURL = "";
		long bookingFormId = demandNoteInfoObject.getBookingFormId();
		long portNumber = email.getPortNumber();
		log.info(" ***** PdfHelper.XMLWorkerHelperForAgreementDraftLetter() ***** PORT number : "+portNumber+"\t Booking Form Id :"+bookingFormId);
		//Properties prop= responceCodesUtil.getApplicationProperties();
		PdfFilePath = demandNoteInfoObject.getFolderFilePath();
		PdfFileURL = demandNoteInfoObject.getFolderFileUrl();
		/*if (portNumber == Long.valueOf(MetadataId.LOCAL_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("DOCUMENTS_FILE_DIRECTORY_PATH");
			transactionGetReceiptFileURL = prop.getProperty("DOCUMENTS_FILE_URL_PATH");
		} else if (portNumber == Long.valueOf(MetadataId.UAT_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("DOCUMENTS_FILE_DIRECTORY_PATH");
			transactionGetReceiptFileURL = prop.getProperty("DOCUMENTS_FILE_URL_PATH");
		} else if (portNumber == Long.valueOf(MetadataId.CUG_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("CUG_ALLOTMENT_LETTER_PDF_PATH");
			transactionGetReceiptFileURL = prop.getProperty("CUG_ALLOTMENT_LETTER_PDF_URL");
		} else if (portNumber == Long.valueOf(MetadataId.LIVE_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("LIVE_ALLOTMENT_LETTER_PDF_PATH");
			transactionGetReceiptFileURL = prop.getProperty("LIVE_ALLOTMENT_LETTER_PDF_URL");
		} else {
			log.error("Path missing to save the File. Please contact to Support Team");
			throw new EmployeeFinancialServiceException("Path missing to save the File, Please contact to Support Team.");
		}
		if(transactionReceiptPdfFilePath==null || transactionReceiptPdfFilePath.isEmpty()) {
			throw new EmployeeFinancialServiceException("File path found empty for storing pdf's.");
		}*/	
		//final Document document = new Document(PageSize.A4, 20, 20, 80, 80);// this is for footer and header
		final Document document = new Document(PageSize.A4, 25, 25, 50, 50);//this is  for footer and header
		String path = "";
		
		path = PdfFilePath + demandNoteInfoObject.getSiteId() + "/" + bookingFormId + "/";
		
		//String fileName = getFileName(path, allotmentLetterPdfFileName);
		String fileName = "The Olympus Agreement.pdf";
			
		StringBuilder pdfFileName = new StringBuilder(PdfFilePath)
			.append(demandNoteInfoObject.getSiteId()).append("/").append(bookingFormId).append("/")
				.append(fileName);

		StringBuilder urlPath = new StringBuilder(PdfFileURL).append(demandNoteInfoObject.getSiteId())
				.append("/").append(bookingFormId).append("/").append(fileName);

		fileInfo.setName(fileName);
		fileInfo.setUrl(urlPath.toString());
		fileInfo.setFilePath(pdfFileName.toString());
		fileInfo.setExtension(pdfFileName.substring(pdfFileName.lastIndexOf(".") + 1));
		
		
		StringBuilder pdfFileName1 = new StringBuilder(PdfFilePath)
				.append(demandNoteInfoObject.getSiteId()).append("/").append(bookingFormId).append("/");
		File source = new File("D:\\CustomerApp_CUG\\images\\sumadhura_projects_images\\AgreementDraft\\134_vasavi_draft");
		File dest = new File(pdfFileName1.toString());
		try {
		    FileUtils.copyDirectory(source, dest);
		} catch (IOException e) {
		    e.printStackTrace();
		}

//		final PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFileName.toString()));
//
//		document.open();
//		document.addAuthor("Amaravadhis software services private limited");
//		document.addCreator("Aniket Chavan");
//		document.addSubject("Agreement Draft");
//		document.addTitle("Agreement Draft");
//		document.addCreationDate();
//		
//		final com.itextpdf.tool.xml.XMLWorkerHelper worker = com.itextpdf.tool.xml.XMLWorkerHelper.getInstance();

		//email.setTemplateName("/vmtemplates/AgreementDraft.vm");
		/*File file = new File("D:\\CUSTOMERAPP_UAT\\images\\sumadhura_projects_images\\Agreement_draft_letter\\AgreementDraft.vm");
		if(file.exists()) {
			System.out.println("***** Control inside the PdfHelper.XMLWorkerHelperForAgreementDraftLetter() *****");
		}
//		email.setTemplateName("AgreementDraft.vm");*/
//		Map<String, Object> model = new HashMap<String, Object>();
//		model.put("email", email);
//		model.put("StringUtils", new Util());
//		model.put("DateUtils", new TimeUtil());
//		model.put("CurrencyUtil", new CurrencyUtil());
//		model.put("EmployeeFinancialHelper", new EmployeeFinancialHelper());
//		
//		final String data = mailServiceImpl.geVelocityTemplateContent(model);
//		log.info(pdfFileName+" ********************************************************************* \n" + data);
//		log.info(pdfFileName + "\t" + urlPath);
//		Rectangle headerBox = new Rectangle(36, 54, 559, 788);
//		pdfWriter.setBoxSize("headerBox", headerBox);

		/*String footerAdderss = "";
		if (demandNoteInfoObject.getCompanyCin() != "-") {
	       	footerAdderss = "Tel: "+demandNoteInfoObject.getCompanyTelephoneNo()+" CIN: "+demandNoteInfoObject.getCompanyCin()+" GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" E-mail: "+demandNoteInfoObject.getCompanyEmail()+" Website: "+demandNoteInfoObject.getCompanyWebsite();
	    } else {
	       	footerAdderss = "Tel: "+demandNoteInfoObject.getCompanyTelephoneNo()+" LLPIN: "+demandNoteInfoObject.getCompanyLlpin()+" GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" E-mail: "+demandNoteInfoObject.getCompanyEmail()+" Website: "+demandNoteInfoObject.getCompanyWebsite();
	    }*/
//	        
//		String headData = "";
//		String footerData = "===";
//		String rightLogo = demandNoteInfoObject.getRightSidelogoForPdf();
//		String leftLogo =  demandNoteInfoObject.getLeftSidelogoForPdf();
//		Long headerTextHeight = 5l;
//		Long footerTextHeight = 60l;
//		Long fontSize = 7l;
//
//		HeaderFooterPageEvent2 logo = new HeaderFooterPageEvent2();
//		logo.setHeadData(headData);
//		logo.setFooterData(footerData);
//		logo.setRightLogo(rightLogo);
//		logo.setLeftLogo(leftLogo);
//		logo.setHeaderTextHeight(headerTextHeight);
//		logo.setFooterTextHeight(footerTextHeight);
//		logo.setFontSize(fontSize);

        /*pdfWriter.setPageEvent(logo);
        //pdfWriter.setPageEvent(new HeaderFooterPageEvent2("","===",demandNoteInfoObject.getRightSidelogoForPdf(),5l,60l,7l));
		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",demandNoteInfoObject.getCompanyName(),"",10l,50l,8l));
		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",demandNoteInfoObject.getCompanyBillingAddress(),"",20l,40l,7l));
		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",footerAdderss,"",30l,30l,7l));*/
			        
		/*ExecutorService executorService = Executors.newFixedThreadPool(5);
		try {
			executorService.execute(new Runnable() {
				public void run() {
					try {*/
//						worker.parseXHtml(pdfWriter, document, new StringReader(data));
//						document.close();
//						pdfWriter.close();
					/*} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			executorService.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
			executorService.shutdown();
		}*/

		log.info("*** The Pdf File " + pdfFileName + " is created successfully ***");
	}



	public void XMLWorkerHelperForAgreementDraftLetter_HORIZON(Email email, FileInfo fileInfo) throws Exception {
		log.info("***** Control inside the PdfHelper.XMLWorkerHelperForAgreementDraftLetter() *****");
		DemandNoteGeneratorInfo demandNoteInfoObject = email.getDemandNoteGeneratorInfo();
		//String allotmentLetterPdfFileName = demandNoteInfoObject.getDemandNotePdfFileName();
		String PdfFilePath = "";
		String PdfFileURL = "";
		long bookingFormId = demandNoteInfoObject.getBookingFormId();
		long portNumber = email.getPortNumber();
		log.info(" ***** PdfHelper.XMLWorkerHelperForAgreementDraftLetter() ***** PORT number : "+portNumber+"\t Booking Form Id :"+bookingFormId);
		//Properties prop= responceCodesUtil.getApplicationProperties();
		PdfFilePath = demandNoteInfoObject.getFolderFilePath();
		PdfFileURL = demandNoteInfoObject.getFolderFileUrl();
		/*if (portNumber == Long.valueOf(MetadataId.LOCAL_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("DOCUMENTS_FILE_DIRECTORY_PATH");
			transactionGetReceiptFileURL = prop.getProperty("DOCUMENTS_FILE_URL_PATH");
		} else if (portNumber == Long.valueOf(MetadataId.UAT_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("DOCUMENTS_FILE_DIRECTORY_PATH");
			transactionGetReceiptFileURL = prop.getProperty("DOCUMENTS_FILE_URL_PATH");
		} else if (portNumber == Long.valueOf(MetadataId.CUG_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("CUG_ALLOTMENT_LETTER_PDF_PATH");
			transactionGetReceiptFileURL = prop.getProperty("CUG_ALLOTMENT_LETTER_PDF_URL");
		} else if (portNumber == Long.valueOf(MetadataId.LIVE_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("LIVE_ALLOTMENT_LETTER_PDF_PATH");
			transactionGetReceiptFileURL = prop.getProperty("LIVE_ALLOTMENT_LETTER_PDF_URL");
		} else {
			log.error("Path missing to save the File. Please contact to Support Team");
			throw new EmployeeFinancialServiceException("Path missing to save the File, Please contact to Support Team.");
		}
		if(transactionReceiptPdfFilePath==null || transactionReceiptPdfFilePath.isEmpty()) {
			throw new EmployeeFinancialServiceException("File path found empty for storing pdf's.");
		}*/	
		//final Document document = new Document(PageSize.A4, 20, 20, 80, 80);// this is for footer and header
		final Document document = new Document(PageSize.A4, 25, 25, 50, 50);//this is  for footer and header
		String path = "";
		
		path = PdfFilePath + demandNoteInfoObject.getSiteId() + "/" + bookingFormId + "/";
		
		//String fileName = getFileName(path, allotmentLetterPdfFileName);
		String fileName = "Horizon Agreement Draft.pdf";
			
		StringBuilder pdfFileName = new StringBuilder(PdfFilePath)
			.append(demandNoteInfoObject.getSiteId()).append("/").append(bookingFormId).append("/")
				.append(fileName);

		StringBuilder urlPath = new StringBuilder(PdfFileURL).append(demandNoteInfoObject.getSiteId())
				.append("/").append(bookingFormId).append("/").append(fileName);

		fileInfo.setName(fileName);
		fileInfo.setUrl(urlPath.toString());
		fileInfo.setFilePath(pdfFileName.toString());
		fileInfo.setExtension(pdfFileName.substring(pdfFileName.lastIndexOf(".") + 1));
		
		
		StringBuilder pdfFileName1 = new StringBuilder(PdfFilePath)
				.append(demandNoteInfoObject.getSiteId()).append("/").append(bookingFormId).append("/");
		File source = new File("D:\\CustomerApp_CUG\\images\\sumadhura_projects_images\\AgreementDraft\\\\114AggreementDraft");
		File dest = new File(pdfFileName1.toString());
		try {
		    FileUtils.copyDirectory(source, dest);
		} catch (IOException e) {
		    e.printStackTrace();
		}

//		final PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFileName.toString()));
//
//		document.open();
//		document.addAuthor("Amaravadhis software services private limited");
//		document.addCreator("Aniket Chavan");
//		document.addSubject("Agreement Draft");
//		document.addTitle("Agreement Draft");
//		document.addCreationDate();
//		
//		final com.itextpdf.tool.xml.XMLWorkerHelper worker = com.itextpdf.tool.xml.XMLWorkerHelper.getInstance();

		//email.setTemplateName("/vmtemplates/AgreementDraft.vm");
		/*File file = new File("D:\\CUSTOMERAPP_UAT\\images\\sumadhura_projects_images\\Agreement_draft_letter\\AgreementDraft.vm");
		if(file.exists()) {
			System.out.println("***** Control inside the PdfHelper.XMLWorkerHelperForAgreementDraftLetter() *****");
		}
//		email.setTemplateName("AgreementDraft.vm");*/
//		Map<String, Object> model = new HashMap<String, Object>();
//		model.put("email", email);
//		model.put("StringUtils", new Util());
//		model.put("DateUtils", new TimeUtil());
//		model.put("CurrencyUtil", new CurrencyUtil());
//		model.put("EmployeeFinancialHelper", new EmployeeFinancialHelper());
//		
//		final String data = mailServiceImpl.geVelocityTemplateContent(model);
//		log.info(pdfFileName+" ********************************************************************* \n" + data);
//		log.info(pdfFileName + "\t" + urlPath);
//		Rectangle headerBox = new Rectangle(36, 54, 559, 788);
//		pdfWriter.setBoxSize("headerBox", headerBox);

		/*String footerAdderss = "";
		if (demandNoteInfoObject.getCompanyCin() != "-") {
	       	footerAdderss = "Tel: "+demandNoteInfoObject.getCompanyTelephoneNo()+" CIN: "+demandNoteInfoObject.getCompanyCin()+" GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" E-mail: "+demandNoteInfoObject.getCompanyEmail()+" Website: "+demandNoteInfoObject.getCompanyWebsite();
	    } else {
	       	footerAdderss = "Tel: "+demandNoteInfoObject.getCompanyTelephoneNo()+" LLPIN: "+demandNoteInfoObject.getCompanyLlpin()+" GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" E-mail: "+demandNoteInfoObject.getCompanyEmail()+" Website: "+demandNoteInfoObject.getCompanyWebsite();
	    }*/
//	        
//		String headData = "";
//		String footerData = "===";
//		String rightLogo = demandNoteInfoObject.getRightSidelogoForPdf();
//		String leftLogo =  demandNoteInfoObject.getLeftSidelogoForPdf();
//		Long headerTextHeight = 5l;
//		Long footerTextHeight = 60l;
//		Long fontSize = 7l;
//
//		HeaderFooterPageEvent2 logo = new HeaderFooterPageEvent2();
//		logo.setHeadData(headData);
//		logo.setFooterData(footerData);
//		logo.setRightLogo(rightLogo);
//		logo.setLeftLogo(leftLogo);
//		logo.setHeaderTextHeight(headerTextHeight);
//		logo.setFooterTextHeight(footerTextHeight);
//		logo.setFontSize(fontSize);

        /*pdfWriter.setPageEvent(logo);
        //pdfWriter.setPageEvent(new HeaderFooterPageEvent2("","===",demandNoteInfoObject.getRightSidelogoForPdf(),5l,60l,7l));
		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",demandNoteInfoObject.getCompanyName(),"",10l,50l,8l));
		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",demandNoteInfoObject.getCompanyBillingAddress(),"",20l,40l,7l));
		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",footerAdderss,"",30l,30l,7l));*/
			        
		/*ExecutorService executorService = Executors.newFixedThreadPool(5);
		try {
			executorService.execute(new Runnable() {
				public void run() {
					try {*/
//						worker.parseXHtml(pdfWriter, document, new StringReader(data));
//						document.close();
//						pdfWriter.close();
					/*} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			executorService.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
			executorService.shutdown();
		}*/

		log.info("*** The Pdf File " + pdfFileName + " is created successfully ***");
	}

	public void XMLWorkerHelperForCostBreakUpLetterSST2(Email email, FileInfo fileInfo) throws Exception {
		log.info("***** Control inside the PdfHelper.XMLWorkerHelperForCostBreakUpLetter() *****");
		DemandNoteGeneratorInfo demandNoteInfoObject = email.getDemandNoteGeneratorInfo();
		String allotmentLetterPdfFileName = demandNoteInfoObject.getDemandNotePdfFileName();
		String transactionReceiptPdfFilePath = "";
		String transactionGetReceiptFileURL = "";
		//String appovals=null;
		long bookingFormId = demandNoteInfoObject.getBookingFormId();
		long portNumber = email.getPortNumber();
		log.info(" ***** PdfHelper.XMLWorkerHelperForCostBreakUpLetter() ***** PORT number : "+portNumber+"\t Booking Form Id :"+bookingFormId);
		//Properties prop= responceCodesUtil.getApplicationProperties();
		transactionReceiptPdfFilePath = demandNoteInfoObject.getFolderFilePath();
		transactionGetReceiptFileURL = demandNoteInfoObject.getFolderFileUrl();
/*		if (portNumber == Long.valueOf(MetadataId.LOCAL_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("DOCUMENTS_FILE_DIRECTORY_PATH");
			transactionGetReceiptFileURL = prop.getProperty("DOCUMENTS_FILE_URL_PATH");
		} else if (portNumber == Long.valueOf(MetadataId.UAT_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("DOCUMENTS_FILE_DIRECTORY_PATH");
			transactionGetReceiptFileURL = prop.getProperty("DOCUMENTS_FILE_URL_PATH");
		} else if (portNumber == Long.valueOf(MetadataId.CUG_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("CUG_ALLOTMENT_LETTER_PDF_PATH");
			transactionGetReceiptFileURL = prop.getProperty("CUG_ALLOTMENT_LETTER_PDF_URL");
		} else if (portNumber == Long.valueOf(MetadataId.LIVE_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("LIVE_ALLOTMENT_LETTER_PDF_PATH");
			transactionGetReceiptFileURL = prop.getProperty("LIVE_ALLOTMENT_LETTER_PDF_URL");
		} else {
			log.error("Path missing to save the File. Please contact to Support Team");
			throw new EmployeeFinancialServiceException("Path missing to save the File, Please contact to Support Team.");
		}
		if(transactionReceiptPdfFilePath==null || transactionReceiptPdfFilePath.isEmpty()) {
			throw new EmployeeFinancialServiceException("File path found empty for storing pdf's.");
		}	
*/		//final Document document = new Document(PageSize.A4, 25, 25, 105, 80);// this is for footer and header
		final Document document = new Document(PageSize.A4, 25, 25, 105, 110);// this is for footer and header
		String path = transactionReceiptPdfFilePath + demandNoteInfoObject.getSiteId() + "/" + bookingFormId + "/";
		String fileName = getFileName(path, allotmentLetterPdfFileName);
			
		StringBuilder pdfFileName = new StringBuilder(transactionReceiptPdfFilePath)
				.append(demandNoteInfoObject.getSiteId()).append("/").append(bookingFormId).append("/")
				.append(fileName);

		StringBuilder urlPath = new StringBuilder(transactionGetReceiptFileURL).append(demandNoteInfoObject.getSiteId())
				.append("/").append(bookingFormId).append("/").append(fileName);

		fileInfo.setName(fileName);
		fileInfo.setUrl(urlPath.toString());
		fileInfo.setFilePath(pdfFileName.toString());
		fileInfo.setExtension(pdfFileName.substring(pdfFileName.lastIndexOf(".") + 1));
		log.info(pdfFileName + "\t" + urlPath);
		final PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFileName.toString()));

		document.open();
		document.addAuthor("Amaravadhis software services private limited");
		document.addCreator("Aniket Chavan");
		document.addSubject(" Cost Break Up Letter ");
		document.addCreationDate();
		document.addTitle(" Cost Break Up Letter ");
		final com.itextpdf.tool.xml.XMLWorkerHelper worker = com.itextpdf.tool.xml.XMLWorkerHelper.getInstance();

		//email.setTemplateName("/vmtemplates/COST_BREAK_UP_AND_PAYMENT_SCHEDULE.vm");
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("email", email);
		final String data = mailServiceImpl.geVelocityTemplateContent(model);
		log.info(" ********************************************************************* \n" + data);

		Rectangle headerBox = new Rectangle(36, 54, 559, 788);
		pdfWriter.setBoxSize("headerBox", headerBox);

		String footerAdderss = "";
		
		
		if (Util.isNotEmptyObject(demandNoteInfoObject.getSiteId()) && demandNoteInfoObject.getSiteId().equals("126")) {
		    if (demandNoteInfoObject.getCompanyCin() != "-") {
	       	footerAdderss = " GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" CIN: "+demandNoteInfoObject.getCompanyCin()+" Website: "+demandNoteInfoObject.getCompanyWebsite();
	        } else {
	       	footerAdderss = " LLPIN: "+demandNoteInfoObject.getCompanyLlpin()+" GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" Website: "+demandNoteInfoObject.getCompanyWebsite();
	        }
		}else if (Util.isNotEmptyObject(demandNoteInfoObject.getSiteId()) && demandNoteInfoObject.getSiteId().equals("130")) {
		    if (demandNoteInfoObject.getCompanyCin() != "-") {
		       	footerAdderss = "GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" CIN: "+demandNoteInfoObject.getCompanyCin()+" URL: "+demandNoteInfoObject.getCompanyWebsite();
		        } else {
		       	footerAdderss = "LLPIN: "+demandNoteInfoObject.getCompanyLlpin()+" GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" URL: "+demandNoteInfoObject.getCompanyWebsite();
		        }
		} else {
			 if (demandNoteInfoObject.getCompanyCin() != "-") {
			       	footerAdderss = "Tel: "+demandNoteInfoObject.getCompanyTelephoneNo()+" CIN: "+demandNoteInfoObject.getCompanyCin()+" GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" E-mail: "+demandNoteInfoObject.getCompanyEmail()+" Website: "+demandNoteInfoObject.getCompanyWebsite();
			        } else {
			       	footerAdderss = "Tel: "+demandNoteInfoObject.getCompanyTelephoneNo()+" LLPIN: "+demandNoteInfoObject.getCompanyLlpin()+" GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" E-mail: "+demandNoteInfoObject.getCompanyEmail()+" Website: "+demandNoteInfoObject.getCompanyWebsite();
			        }
		}
	        
		String headData = "";
		String footerData = "===";
		String rightLogo = Util.isNotEmptyObject(demandNoteInfoObject.getRightSidelogoFilePath())? demandNoteInfoObject.getRightSidelogoFilePath() : demandNoteInfoObject.getRightSidelogoForPdf();
		String leftLogo = Util.isNotEmptyObject(demandNoteInfoObject.getLeftSidelogoFilePath())? demandNoteInfoObject.getLeftSidelogoFilePath() : demandNoteInfoObject.getLeftSidelogoForPdf();
		Long headerTextHeight = 5l;
		Long footerTextHeight = 60l;
		Long fontSize = 7l;

		HeaderFooterPageEvent2 logo = new HeaderFooterPageEvent2();
		logo.setHeadData(headData);
		logo.setFooterData(footerData);
		logo.setRightLogo(rightLogo);
		logo.setLeftLogo(leftLogo);
		logo.setHeaderTextHeight(headerTextHeight);
		logo.setFooterTextHeight(footerTextHeight);
		logo.setFontSize(fontSize);

        pdfWriter.setPageEvent(logo);
        
        /* Malladi Changes */
//        if(!("131").equals(demandNoteInfoObject.getSiteId())) {
//        	appovals="    Prepared By                           Authorised By                           Approved By                           Customer/s Signature";
//        	pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",appovals,"",10l,35l,10l));
//        }
        
        
        
    	if (Util.isNotEmptyObject(demandNoteInfoObject.getSiteId()) && demandNoteInfoObject.getSiteId().equals("130")) {
    		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("","                                                                           "+demandNoteInfoObject.getCompanyName(),"",10l,20l,8l));
            pdfWriter.setPageEvent(new HeaderFooterPageEvent2("   ","                   "+demandNoteInfoObject.getCompanyBillingAddress(),"",20l,10l,7l));
    		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("  ","                             "+footerAdderss,"",30l,0l,7l));
    	}else {
        	pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",demandNoteInfoObject.getCompanyName(),"",10l,20l,8l));
    	    pdfWriter.setPageEvent(new HeaderFooterPageEvent2("  ",""+demandNoteInfoObject.getCompanyBillingAddress(),"  ",15l,10l,6l));
    	    pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",footerAdderss,"",30l,0l,6l));
        }
    	
        //pdfWriter.setPageEvent(new HeaderFooterPageEvent2("","===",demandNoteInfoObject.getRightSidelogoForPdf(),5l,60l,7l));
		
			        
		/*ExecutorService executorService = Executors.newFixedThreadPool(5);
		try {
			executorService.execute(new Runnable() {
				public void run() {
					try {*/
						worker.parseXHtml(pdfWriter, document, new StringReader(data));
						document.close();
						pdfWriter.close();
					/*} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			executorService.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
			executorService.shutdown();
		}*/

		log.info("*** The Pdf File " + pdfFileName + " is created successfully ***");
	}
	
	public void XMLWorkerHelperForAgreementDraftLetterSST2(Email email, FileInfo fileInfo) throws Exception {
		log.info("***** Control inside the PdfHelper.XMLWorkerHelperForAgreementDraftLetter() *****");
		DemandNoteGeneratorInfo demandNoteInfoObject = email.getDemandNoteGeneratorInfo();
		//String allotmentLetterPdfFileName = demandNoteInfoObject.getDemandNotePdfFileName();
		String PdfFilePath = "";
		String PdfFileURL = "";
		long bookingFormId = demandNoteInfoObject.getBookingFormId();
		long portNumber = email.getPortNumber();
		log.info(" ***** PdfHelper.XMLWorkerHelperForAgreementDraftLetter() ***** PORT number : "+portNumber+"\t Booking Form Id :"+bookingFormId);
		//Properties prop= responceCodesUtil.getApplicationProperties();
		PdfFilePath = demandNoteInfoObject.getFolderFilePath();
		PdfFileURL = demandNoteInfoObject.getFolderFileUrl();
		/*if (portNumber == Long.valueOf(MetadataId.LOCAL_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("DOCUMENTS_FILE_DIRECTORY_PATH");
			transactionGetReceiptFileURL = prop.getProperty("DOCUMENTS_FILE_URL_PATH");
		} else if (portNumber == Long.valueOf(MetadataId.UAT_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("DOCUMENTS_FILE_DIRECTORY_PATH");
			transactionGetReceiptFileURL = prop.getProperty("DOCUMENTS_FILE_URL_PATH");
		} else if (portNumber == Long.valueOf(MetadataId.CUG_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("CUG_ALLOTMENT_LETTER_PDF_PATH");
			transactionGetReceiptFileURL = prop.getProperty("CUG_ALLOTMENT_LETTER_PDF_URL");
		} else if (portNumber == Long.valueOf(MetadataId.LIVE_PORT_NUMBER.getName())) {
			transactionReceiptPdfFilePath = prop.getProperty("LIVE_ALLOTMENT_LETTER_PDF_PATH");
			transactionGetReceiptFileURL = prop.getProperty("LIVE_ALLOTMENT_LETTER_PDF_URL");
		} else {
			log.error("Path missing to save the File. Please contact to Support Team");
			throw new EmployeeFinancialServiceException("Path missing to save the File, Please contact to Support Team.");
		}
		if(transactionReceiptPdfFilePath==null || transactionReceiptPdfFilePath.isEmpty()) {
			throw new EmployeeFinancialServiceException("File path found empty for storing pdf's.");
		}*/	
		//final Document document = new Document(PageSize.A4, 20, 20, 80, 80);// this is for footer and header
		final Document document = new Document(PageSize.A4, 25, 25, 50, 50);//this is  for footer and header
		String path = "";
		
		path = PdfFilePath + demandNoteInfoObject.getSiteId() + "/" + bookingFormId + "/";
		
		//String fileName = getFileName(path, allotmentLetterPdfFileName);
		String fileName = "Sushantham Phase 2 Agreement Draft.pdf";
			
		StringBuilder pdfFileName = new StringBuilder(PdfFilePath)
			.append(demandNoteInfoObject.getSiteId()).append("/").append(bookingFormId).append("/")
				.append(fileName);

		StringBuilder urlPath = new StringBuilder(PdfFileURL).append(demandNoteInfoObject.getSiteId())
				.append("/").append(bookingFormId).append("/").append(fileName);

		fileInfo.setName(fileName);
		fileInfo.setUrl(urlPath.toString());
		fileInfo.setFilePath(pdfFileName.toString());
		fileInfo.setExtension(pdfFileName.substring(pdfFileName.lastIndexOf(".") + 1));
		
		
		StringBuilder pdfFileName1 = new StringBuilder(PdfFilePath)
				.append(demandNoteInfoObject.getSiteId()).append("/").append(bookingFormId).append("/");
	//	File source = new File("D:\\CustomerApp_CUG\\images\\sumadhura_projects_images\\AgreementDraft\\SST2AggrementLetter");
		File source = new File("E:\\CUSTOMERAPP_LIVE\\images\\sumadhura_projects_images\\AgreementDraft\\SST2AggrementLetter");
		File dest = new File(pdfFileName1.toString());
		try {
		    FileUtils.copyDirectory(source, dest);
		} catch (IOException e) {
		    e.printStackTrace();
		}

//		final PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFileName.toString()));
//
//		document.open();
//		document.addAuthor("Amaravadhis software services private limited");
//		document.addCreator("Aniket Chavan");
//		document.addSubject("Agreement Draft");
//		document.addTitle("Agreement Draft");
//		document.addCreationDate();
//		
//		final com.itextpdf.tool.xml.XMLWorkerHelper worker = com.itextpdf.tool.xml.XMLWorkerHelper.getInstance();

		//email.setTemplateName("/vmtemplates/AgreementDraft.vm");
		/*File file = new File("D:\\CUSTOMERAPP_UAT\\images\\sumadhura_projects_images\\Agreement_draft_letter\\AgreementDraft.vm");
		if(file.exists()) {
			System.out.println("***** Control inside the PdfHelper.XMLWorkerHelperForAgreementDraftLetter() *****");
		}
//		email.setTemplateName("AgreementDraft.vm");*/
//		Map<String, Object> model = new HashMap<String, Object>();
//		model.put("email", email);
//		model.put("StringUtils", new Util());
//		model.put("DateUtils", new TimeUtil());
//		model.put("CurrencyUtil", new CurrencyUtil());
//		model.put("EmployeeFinancialHelper", new EmployeeFinancialHelper());
//		
//		final String data = mailServiceImpl.geVelocityTemplateContent(model);
//		log.info(pdfFileName+" ********************************************************************* \n" + data);
//		log.info(pdfFileName + "\t" + urlPath);
//		Rectangle headerBox = new Rectangle(36, 54, 559, 788);
//		pdfWriter.setBoxSize("headerBox", headerBox);

		/*String footerAdderss = "";
		if (demandNoteInfoObject.getCompanyCin() != "-") {
	       	footerAdderss = "Tel: "+demandNoteInfoObject.getCompanyTelephoneNo()+" CIN: "+demandNoteInfoObject.getCompanyCin()+" GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" E-mail: "+demandNoteInfoObject.getCompanyEmail()+" Website: "+demandNoteInfoObject.getCompanyWebsite();
	    } else {
	       	footerAdderss = "Tel: "+demandNoteInfoObject.getCompanyTelephoneNo()+" LLPIN: "+demandNoteInfoObject.getCompanyLlpin()+" GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" E-mail: "+demandNoteInfoObject.getCompanyEmail()+" Website: "+demandNoteInfoObject.getCompanyWebsite();
	    }*/
//	        
//		String headData = "";
//		String footerData = "===";
//		String rightLogo = demandNoteInfoObject.getRightSidelogoForPdf();
//		String leftLogo =  demandNoteInfoObject.getLeftSidelogoForPdf();
//		Long headerTextHeight = 5l;
//		Long footerTextHeight = 60l;
//		Long fontSize = 7l;
//
//		HeaderFooterPageEvent2 logo = new HeaderFooterPageEvent2();
//		logo.setHeadData(headData);
//		logo.setFooterData(footerData);
//		logo.setRightLogo(rightLogo);
//		logo.setLeftLogo(leftLogo);
//		logo.setHeaderTextHeight(headerTextHeight);
//		logo.setFooterTextHeight(footerTextHeight);
//		logo.setFontSize(fontSize);

        /*pdfWriter.setPageEvent(logo);
        //pdfWriter.setPageEvent(new HeaderFooterPageEvent2("","===",demandNoteInfoObject.getRightSidelogoForPdf(),5l,60l,7l));
		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",demandNoteInfoObject.getCompanyName(),"",10l,50l,8l));
		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",demandNoteInfoObject.getCompanyBillingAddress(),"",20l,40l,7l));
		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",footerAdderss,"",30l,30l,7l));*/
			        
		/*ExecutorService executorService = Executors.newFixedThreadPool(5);
		try {
			executorService.execute(new Runnable() {
				public void run() {
					try {*/
//						worker.parseXHtml(pdfWriter, document, new StringReader(data));
//						document.close();
//						pdfWriter.close();
					/*} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			executorService.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
			executorService.shutdown();
		}*/

		log.info("*** The Pdf File " + pdfFileName + " is created successfully ***");
	}
	
	
	
	public void XMLWorkerHelperForLoanNocLetter(Email email, FileInfo fileInfo) throws Exception {
		log.info("***** Control inside the PdfHelper.XMLWorkerHelperForLoanNocLetter() *****");
		DemandNoteGeneratorInfo demandNoteInfoObject = email.getDemandNoteGeneratorInfo();
		String NOCPdfFileName = demandNoteInfoObject.getDemandNotePdfFileName();
		NOCPdfFileName=NOCPdfFileName+".pdf";
		String transactionReceiptPdfFilePath = "";
		String transactionGetReceiptFileURL = "";
		long bookingFormId = demandNoteInfoObject.getBookingFormId();
		long portNumber = email.getPortNumber();
		log.info(" ***** PdfHelper.XMLWorkerHelperForNOCLetter() ***** PORT number : "+portNumber+"\t Booking Form Id :"+bookingFormId);
		
		transactionReceiptPdfFilePath = demandNoteInfoObject.getFolderFilePath();
		transactionGetReceiptFileURL = demandNoteInfoObject.getFolderFileUrl();
		final Document document = new Document(PageSize.A4, 25, 25, 85, 105);// this is for footer and header
		String path = transactionReceiptPdfFilePath + demandNoteInfoObject.getSiteId() + "/" + bookingFormId + "/";
		String fileName = getFileName(path, NOCPdfFileName);
			
		StringBuilder pdfFileName = new StringBuilder(transactionReceiptPdfFilePath)
				.append(demandNoteInfoObject.getSiteId()).append("/").append(bookingFormId).append("/")
				.append(fileName);

		StringBuilder urlPath = new StringBuilder(transactionGetReceiptFileURL).append(demandNoteInfoObject.getSiteId())
				.append("/").append(bookingFormId).append("/").append(fileName);

		fileInfo.setName(fileName);
		fileInfo.setUrl(urlPath.toString());
		fileInfo.setFilePath(pdfFileName.toString());
		fileInfo.setExtension(pdfFileName.substring(pdfFileName.lastIndexOf(".") + 1));
		log.info(pdfFileName + "\t" + urlPath);
		final PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFileName.toString()));

		document.open();
		document.addAuthor("Amaravadhis software services private limited");
		document.addCreator("Aniket Chavan");
		document.addSubject(" Cost Break Up Letter ");
		document.addCreationDate();
		document.addTitle(" Cost Break Up Letter ");
		final com.itextpdf.tool.xml.XMLWorkerHelper worker = com.itextpdf.tool.xml.XMLWorkerHelper.getInstance();

		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("email", email);
		final String data = mailServiceImpl.geVelocityTemplateContent(model);
		log.info(" ********************************************************************* \n" + data);

		Rectangle headerBox = new Rectangle(36, 54, 559, 788);
		pdfWriter.setBoxSize("headerBox", headerBox);

		String footerAdderss = "";
		
		
		if (Util.isNotEmptyObject(demandNoteInfoObject.getSiteId()) && demandNoteInfoObject.getSiteId().equals("126")|| demandNoteInfoObject.getSiteId().equals("130") ||demandNoteInfoObject.getSiteId().equals("124") ||demandNoteInfoObject.getSiteId().equals("111")) {
		    if (demandNoteInfoObject.getCompanyCin() != "-") {
	       	footerAdderss = " GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" CIN: "+demandNoteInfoObject.getCompanyCin()+" Website: "+demandNoteInfoObject.getCompanyWebsite();
	        } else {
	       	footerAdderss = " LLPIN: "+demandNoteInfoObject.getCompanyLlpin()+" GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" Website: "+demandNoteInfoObject.getCompanyWebsite();
	        }
		}else if (Util.isNotEmptyObject(demandNoteInfoObject.getSiteId()) && demandNoteInfoObject.getSiteId().equals("114")) {
		    if (demandNoteInfoObject.getCompanyCin() != "-") {
		       	footerAdderss = "GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" CIN: "+demandNoteInfoObject.getCompanyCin()+" URL: "+demandNoteInfoObject.getCompanyWebsite();
		        } else {
		       	footerAdderss = "LLPIN: "+demandNoteInfoObject.getCompanyLlpin()+" GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" URL: "+demandNoteInfoObject.getCompanyWebsite();
		        }
		} else {
			 if (demandNoteInfoObject.getCompanyCin() != "-") {
			       	footerAdderss = "Tel: "+demandNoteInfoObject.getCompanyTelephoneNo()+" CIN: "+demandNoteInfoObject.getCompanyCin()+" GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" E-mail: "+demandNoteInfoObject.getCompanyEmail()+" Website: "+demandNoteInfoObject.getCompanyWebsite();
			        } else {
			       	footerAdderss = "Tel: "+demandNoteInfoObject.getCompanyTelephoneNo()+" LLPIN: "+demandNoteInfoObject.getCompanyLlpin()+" GSTIN: "+demandNoteInfoObject.getCompanyGstin()+" E-mail: "+demandNoteInfoObject.getCompanyEmail()+" Website: "+demandNoteInfoObject.getCompanyWebsite();
			        }
		}
	        
		String headData = "";
		String footerData = "===";
		String rightLogo = Util.isNotEmptyObject(demandNoteInfoObject.getRightSidelogoFilePath())? demandNoteInfoObject.getRightSidelogoFilePath() : demandNoteInfoObject.getRightSidelogoForPdf();
		String leftLogo = Util.isNotEmptyObject(demandNoteInfoObject.getLeftSidelogoFilePath())? demandNoteInfoObject.getLeftSidelogoFilePath() : demandNoteInfoObject.getLeftSidelogoForPdf();
		Long headerTextHeight = 5l;
		Long footerTextHeight = 60l;
		Long fontSize = 7l;

		HeaderFooterPageEvent2 logo = new HeaderFooterPageEvent2();
		logo.setHeadData(headData);
		logo.setFooterData(footerData);
		logo.setRightLogo(rightLogo);
		logo.setLeftLogo(leftLogo);
		logo.setHeaderTextHeight(headerTextHeight);
		logo.setFooterTextHeight(footerTextHeight);
		logo.setFontSize(fontSize);

        pdfWriter.setPageEvent(logo);
       
    	if (Util.isNotEmptyObject(demandNoteInfoObject.getSiteId()) && demandNoteInfoObject.getSiteId().equals("126") || demandNoteInfoObject.getSiteId().equals("130")||demandNoteInfoObject.getSiteId().equals("124") ||demandNoteInfoObject.getSiteId().equals("111")) {
    		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("","                                                                           "+demandNoteInfoObject.getCompanyName(),"",10l,20l,8l));
            pdfWriter.setPageEvent(new HeaderFooterPageEvent2("   ","                   "+demandNoteInfoObject.getCompanyBillingAddress(),"",20l,10l,7l));
    		pdfWriter.setPageEvent(new HeaderFooterPageEvent2("  ","                             "+footerAdderss,"",30l,0l,7l));
    	}else {
        	pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",demandNoteInfoObject.getCompanyName(),"",10l,20l,8l));
    	    pdfWriter.setPageEvent(new HeaderFooterPageEvent2("  ",""+demandNoteInfoObject.getCompanyBillingAddress(),"  ",15l,10l,6l));
    	    pdfWriter.setPageEvent(new HeaderFooterPageEvent2("",footerAdderss,"",30l,0l,6l));
        }
    
		worker.parseXHtml(pdfWriter, document, new StringReader(data));
		document.close();
		pdfWriter.close();
				

		log.info("*** The Pdf File " + pdfFileName + " is created successfully ***");
	}
	
	
		


}
