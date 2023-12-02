package com.sumadhura.employeeservice.util;

import java.io.File;

import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

public class TestDoc {
	
	 public static void main(String[] args) throws Exception {
	        
	    	String xhtml = "<ul class=\"UnorderedList\">"+
	    	"  <li> Outer 1 </li>"+
	    	 " <li> Outer 2 </li>"+
	    	 " <li> Outer 3 </li>"+
	    	"</ul>";
	    	
			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(new File("E:\\CUSTOMERAPP_CUG\\images\\sumadhura_projects_images\\documents\\Preview\\124\\894\\ListStyle.docx"));

	        XHTMLImporterImpl XHTMLImporter = new XHTMLImporterImpl(wordMLPackage);
			
			wordMLPackage.getMainDocumentPart().getContent().addAll( 
					XHTMLImporter.convert( xhtml, null) );
		
		wordMLPackage.save(new java.io.File("E:\\CUSTOMERAPP_CUG\\images\\sumadhura_projects_images\\documents\\Preview\\124\\894\\OUT_from_XHTML.docx") );
		
	  }


}
