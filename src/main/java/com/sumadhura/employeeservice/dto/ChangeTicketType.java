/**
 * 
 */
package com.sumadhura.employeeservice.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ChangeTicketTypeResponce bean class provides ChangeTicketType Response specific properties.
 * 
 * @author Venkat_Koniki
 * @since 04.06.2020
 * @time 04:19PM
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@Getter
@Setter
@ToString
public class ChangeTicketType extends TicketResponse{
private static final long serialVersionUID = 7995567562925300966L;
   private String to;
   private String cc;
   private String subject;
   private String messageBody;
   private String employeeName;
   private String raisedUnderCategory;
   private String categoryToBeChanged;
   private Long employeeId;
   private Long changedTicketTypeId;
   private Long noOfTimesRequested;
   private Long changeTicketTypeStatus;
   private String changeTicketTypeAction;
}
