package com.sumadhura.employeeservice.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.IOException;
import java.net.MalformedURLException;

public class PdfHeader extends PdfPageEventHelper {

    
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {
            Rectangle pageSize = document.getPageSize();
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(String.format("%s", String.valueOf(writer.getCurrentPageNumber()))),
                    pageSize.getRight(30), pageSize.getBottom(30), 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

}
