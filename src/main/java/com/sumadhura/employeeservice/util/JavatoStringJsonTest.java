package com.sumadhura.employeeservice.util;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sumadhura.employeeservice.service.dto.CustomerBookingFormInfo;

public class JavatoStringJsonTest {

	public static void main(String[] a) 
    { 
  
        // Creating object of Organisation 
        CustomerBookingFormInfo org = new CustomerBookingFormInfo(); 
  
        // Insert the data into the object 
       // org = getObjectData(org); 
  
        // Creating Object of ObjectMapper define in Jakson Api 
       // ObjectMapper Obj = new ObjectMapper().writer().withDefaultPrettyPrinter(); 
  
        try { 
  
            // get Oraganisation object as a json string 
           // String jsonStr = Obj.writeValueAsString(org); 
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(org);
            // Displaying JSON String 
            System.out.println(json); 
        } 
  
        catch (IOException e) { 
            e.printStackTrace(); 
        } 
    } 

}
