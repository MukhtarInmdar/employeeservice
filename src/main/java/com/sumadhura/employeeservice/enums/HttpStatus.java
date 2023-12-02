/**
 * 
 */
package com.sumadhura.employeeservice.enums;

import java.util.ArrayList;
import java.util.List;

/*
* HttpStatus enum provides HttpStatus codes.
* 
* @author Venkat_Koniki
* @since 04.04.2019
* @time 11:12PM
*/
public enum HttpStatus {
	
 authenticationError(){
    	/**
    	 * @return the responceCode
    	 */
    	public Integer getResponceCode() {
    		return 535;
    	}
    	/**
    	 * @return the description
    	 */
    	public String getDescription() {
    		return "Invalid Credentials,Please verify them and retry.";
    	}
    	/**
    	 * @return the errorMsgs
    	 */
    	public List<String> getErrorMsgs() {
    		List<String> errorMasgList = new ArrayList<String>();
    		errorMasgList.add("Invalid Credentials,Please verify them and retry.");
    		return errorMasgList;
    	}
   
    },
 
    NoExtendedEscalationApprovalLevel(){
    	/**
    	 * @return the responceCode
    	 */
    	public Integer getResponceCode() {
    		return 601;
    	}
    	/**
    	 * @return the description
    	 */
    	public String getDescription() {
    		return "Dear Employee, This Ticket has No Extended Escalation.";
    	}
    	/**
    	 * @return the errorMsgs
    	 */
    	public List<String> getErrorMsgs() {
    		List<String> errorMasgList = new ArrayList<String>();
    		errorMasgList.add("Dear Employee, This Ticket has No Extended Escalation.");
    		return errorMasgList;
    	}
    },
   
   PendingExtendedEscalationApprovalLevel(){
    	/**
    	 * @return the responceCode
    	 */
    	public Integer getResponceCode() {
    		return 602;
    	}
    	/**
    	 * @return the description
    	 */
    	public String getDescription() {
    		return "Dear Employee, This Ticket is already pending for Extended Escalation Approval.";
    	}
    	/**
    	 * @return the errorMsgs
    	 */
    	public List<String> getErrorMsgs() {
    		List<String> errorMasgList = new ArrayList<String>();
    		errorMasgList.add("Dear Employee, This Ticket is already pending for Extended Escalation Approval.");
    		return errorMasgList;
    	}
    },
   
   EligibleExtendedEscalationApprovalLevel(){
    	/**
    	 * @return the responceCode
    	 */
    	public Integer getResponceCode() {
    		return 603;
    	}
    	/**
    	 * @return the description
    	 */
    	public String getDescription() {
    		return "Dear Employee, This Ticket is Eligible  for Extended Escalation Approval.";
    	}
    	/**
    	 * @return the errorMsgs
    	 */
    	public List<String> getErrorMsgs() {
    		List<String> errorMasgList = new ArrayList<String>();
    		errorMasgList.add("Dear Employee, This Ticket is Eligible  for Extended Escalation Approval.");
    		return errorMasgList;
    	}
    },
   
   ExaustedExtendedEscalationApprovalLevel(){
    	/**
    	 * @return the responceCode
    	 */
    	public Integer getResponceCode() {
    		return 604;
    	}
    	/**
    	 * @return the description
    	 */
    	public String getDescription() {
    		return "Dear Employee, This Ticket has reached maximum Extended Escalation Approval.";
    	}
    	/**
    	 * @return the errorMsgs
    	 */
    	public List<String> getErrorMsgs() {
    		List<String> errorMasgList = new ArrayList<String>();
    		errorMasgList.add("Dear Employee, This Ticket has reached maximum Extended Escalation Approval.");
    		return errorMasgList;
    	}
    },
   ExaustedchangeTicketTypeApprovalLevel(){
    	/**
    	 * @return the responceCode
    	 */
    	public Integer getResponceCode() {
    		return 605;
    	}
    	/**
    	 * @return the description
    	 */
    	public String getDescription() {
    		return "Dear Employee, Unable to request! You have reached your maximum limit!.";
    	}
    	/**
    	 * @return the errorMsgs
    	 */
    	public List<String> getErrorMsgs() {
    		List<String> errorMasgList = new ArrayList<String>();
    		errorMasgList.add("Dear Employee, Unable to request! You have reached your maximum limit!.");
    		return errorMasgList;
    	}
    },
  
    insufficientInput(){
    	
    	/**
    	 * @return the responceCode
    	 */
    	public Integer getResponceCode() {
    		return 600;
    	}
    	/**
    	 * @return the description
    	 */
    	public String getDescription() {
    		return "Insufficient Input is given.";
    	}
    	/**
    	 * @return the errorMsgs
    	 */
    	public List<String> getErrorMsgs() {
    		List<String> errorMasgList = new ArrayList<String>();
    		errorMasgList.add("Insufficient Input is given.");
    		return errorMasgList;
    	}
    	
    },
    
    forbiddenRequest(){
    	/**
    	 * @return the responceCode
    	 */
    	public Integer getResponceCode() {
    		return 403;
    	}
    	/**
    	 * @return the description
    	 */
    	public String getDescription() {
    		return "Does not have access rights to the content";
    	}
    	/**
    	 * @return the errorMsgs
    	 */
    	public List<String> getErrorMsgs() {
    		List<String> errorMasgList = new ArrayList<String>();
    		errorMasgList.add("Does not have access rights to the content");
    		return errorMasgList;
    	}
    },

    approvalFailed(){
    	/**
    	 * @return the responceCode
    	 */
    	public Integer getResponceCode() {
    		return 406;
    	}
    	/**
    	 * @return the description
    	 */
    	public String getDescription() {
    		return "This Transaction is already taken care either by Approving/Rejecting";
    	}
    	/**
    	 * @return the errorMsgs
    	 */
    	public List<String> getErrorMsgs() {
    		List<String> errorMasgList = new ArrayList<String>();
    		errorMasgList.add("This Transaction is already taken care either by Approving/Rejecting");
    		return errorMasgList;
    	}
    },

    DataAccessException(){
    	/**
    	 * @return the responceCode
    	 */
    	public Integer getResponceCode() {
    		return 900;
    	}
    	/**
    	 * @return the description
    	 */
    	public String getDescription() {
    		return "The  Database API  error.";
    	}
    	/**
    	 * @return the errorMsgs
    	 */
    	public List<String> getErrorMsgs() {
    		List<String> errorMasgList = new ArrayList<String>();
    		errorMasgList.add("The error occurring while Accessing the Database API.");
    		return errorMasgList;
    	}
    },
    SQLInsertionException(){
    	/**
    	 * @return the responceCode
    	 */
    	public Integer getResponceCode() {
    		return 800;
    	}
    	/**
    	 * @return the description
    	 */
    	public String getDescription() {
    		return "Record is not inserted Properly into database.";
    	}
    	/**
    	 * @return the errorMsgs
    	 */
    	public List<String> getErrorMsgs() {
    		List<String> errorMasgList = new ArrayList<String>();
    		errorMasgList.add("Record is not inserted Properly into database.");
    		return errorMasgList;
    	}
    },
    exceptionRaisedInFlow(){
    	/**
    	 * @return the responceCode
    	 */
    	public Integer getResponceCode() {
    		return 700;
    	}
    	/**
    	 * @return the description
    	 */
    	public String getDescription() {
    		return "Internal Application Error Please try again Later.";
    	}
    	/**
    	 * @return the errorMsgs
    	 */
    	public List<String> getErrorMsgs() {
    		List<String> errorMasgList = new ArrayList<String>();
    		errorMasgList.add("Internal Application Error Please try again Later.");
    		return errorMasgList;
    	}
    	
    },
    success(){
    	/**
    	 * @return the responceCode
    	 */
    	public Integer getResponceCode() {
    		return 200;
    	}
    	/**
    	 * @return the description
    	 */
    	public String getDescription() {
    		return "success";
    	}
    },  
    failure(){
		@Override
		public Integer getResponceCode() {
			return 800;
		}
		@Override
		public String getDescription() {
			return "failure";
		}
	},
    
    invalidStatusCode(){
    	
    	/**
    	 * @return the responceCode
    	 */
    	public Integer getResponceCode() {
    		return 1000;
    	}
    	/**
    	 * @return the description
    	 */
    	public String getDescription() {
    		return " Invalid status code.";
    	}
    	/**
    	 * @return the errorMsgs
    	 */
    	public List<String> getErrorMsgs() {
    		List<String> errorMasgList = new ArrayList<String>();
    		errorMasgList.add("The error occurring while Accessing the Ticket with invalid Ticket status.");
    		return errorMasgList;
    	}
    	
    },
    
    ticketAssignFailed(){
    	/**
    	 * @return the responceCode
    	 */
    	public Integer getResponceCode() {
    		return 1001;
    	}
    	/**
    	 * @return the description
    	 */
    	public String getDescription() {
    		return " TicketAssigng to Employee is Failed ";
    	}
    	/**
    	 * @return the errorMsgs
    	 */
    	public List<String> getErrorMsgs() {
    		List<String> errorMasgList = new ArrayList<String>();
    		errorMasgList.add("The error occurring while assigning Ticket to another Employee (or) Department.");
    		return errorMasgList;
    	}
    },
    
    twoOrMoreDepartments(){
    	/**
    	 * @return the responceCode
    	 */
    	public Integer getResponceCode() {
    		return 1002;
    	}
    	/**
    	 * @return the description
    	 */
    	public String getDescription() {
    		return " Choose the respective department to login.";
    	}
    	/**
    	 * @return the errorMsgs
    	 */
    	public List<String> getErrorMsgs() {
    		List<String> errorMasgList = new ArrayList<String>();
    		errorMasgList.add("Choose the respective department to login.");
    		return errorMasgList;
    	}
    },
    
    OTP_SEND_SUCCESS(){
    	/**
    	 * @return the responceCode
    	 */
    	public Integer getResponceCode() {
    		return 534;
    	}
    	/**
    	 * @return the description
    	 */
    	public String getDescription() {
    		return "OTP sent successfully.";
    	}
    	/**
    	 * @return the errorMsgs
    	 */
    	public List<String> getErrorMsgs() {
    		List<String> errorMasgList = new ArrayList<String>();
    		errorMasgList.add("OTP sent successfully.");
    		return errorMasgList;
    	}	
    },
    
    ONE_MOBILE_NUM_MULTI_EMP(){
    	/**
    	 * @return the responceCode
    	 */
    	public Integer getResponceCode() {
    		return 501;
    	}
    	/**
    	 * @return the description
    	 */
    	public String getDescription() {
    		return " On this Mobile Number multiple Employees are registered.";
    	}
    	/**
    	 * @return the errorMsgs
    	 */
    	public List<String> getErrorMsgs() {
    		List<String> errorMasgList = new ArrayList<String>();
    		errorMasgList.add("On this Mobile Number multiple Employees are registered.");
    		return errorMasgList;
    	}
    },
    
    MOBILE_NUMBER_NOT_REGISTERED(){
    	/**
    	 * @return the responceCode
    	 */
    	public Integer getResponceCode() {
    		return 502;
    	}
    	/**
    	 * @return the description
    	 */
    	public String getDescription() {
    		return "On this Mobile number we haven't found any employee.";
    	}
    	/**
    	 * @return the errorMsgs
    	 */
    	public List<String> getErrorMsgs() {
    		List<String> errorMasgList = new ArrayList<String>();
    		errorMasgList.add("On this Mobile number we haven't found any employee.");
    		return errorMasgList;
    	}
    },
    
    INCORRECT_PASSWORD(){
    	/**
    	 * @return the responceCode
    	 */
    	public Integer getResponceCode() {
    		return 503;
    	}
    	/**
    	 * @return the description
    	 */
    	public String getDescription() {
    		return "Your old password is incorrect. Please enter valid old password.";
    	}
    	/**
    	 * @return the errorMsgs
    	 */
    	public List<String> getErrorMsgs() {
    		List<String> errorMasgList = new ArrayList<String>();
    		errorMasgList.add("Your old password is incorrect. Please enter valid old password.");
    		return errorMasgList;
    	}
    },
    CHANGE_TICKETTYPE_REMINDER(){
    	/**
    	 * @return the responceCode
    	 */
    	public Integer getResponceCode() {
    		return 504;
    	}
    	/**
    	 * @return the description
    	 */
    	public String getDescription() {
    		return "Request sent successfully!.";
    	}
    	/**
    	 * @return the errorMsgs
    	 */
    	public List<String> getErrorMsgs() {
    		List<String> errorMasgList = new ArrayList<String>();
    		errorMasgList.add("Request sent successfully!.");
    		return errorMasgList;
    	}	
    },
    CHANGE_TICKETTYPE_APPROVED(){
    	/**
    	 * @return the responceCode
    	 */
    	public Integer getResponceCode() {
    		return 505;
    	}
    	/**
    	 * @return the description
    	 */
    	public String getDescription() {
    		return "Ticket Type Changed Successfully!.";
    	}
    	/**
    	 * @return the errorMsgs
    	 */
    	public List<String> getErrorMsgs() {
    		List<String> errorMasgList = new ArrayList<String>();
    		errorMasgList.add("Ticket Type Changed Successfully!.");
    		return errorMasgList;
    	}	
    },
    CHANGE_TICKETTYPE_REJECTED(){
    	/**
    	 * @return the responceCode
    	 */
    	public Integer getResponceCode() {
    		return 506;
    	}
    	/**
    	 * @return the description
    	 */
    	public String getDescription() {
    		return "Ticket Type Rejected Successfully!.";
    	}
    	/**
    	 * @return the errorMsgs
    	 */
    	public List<String> getErrorMsgs() {
    		List<String> errorMasgList = new ArrayList<String>();
    		errorMasgList.add("Reminder sent Successfully!.");
    		return errorMasgList;
    	}	
    },
    CHANGE_TICKETTYPE_UNAVIALABLE(){
    	/**
    	 * @return the responceCode
    	 */
    	public Integer getResponceCode() {
    		return 507;
    	}
    	/**
    	 * @return the description
    	 */
    	public String getDescription() {
    		return "Currently we don't have employee on this selected ticket type. Please select valid Ticket Type!.";
    	}
    	/**
    	 * @return the errorMsgs
    	 */
    	public List<String> getErrorMsgs() {
    		List<String> errorMasgList = new ArrayList<String>();
    		errorMasgList.add("Currently we don't have employee on this selected ticket type. Please select valid Ticket Type!.");
    		return errorMasgList;
    	}	
    },
    
 ;
	
	private Integer responceCode;
	private String description;
	private List<String> errorMsgs;
	
	/**
	 * @return the responceCode
	 */
	public Integer getResponceCode() {
		return responceCode;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @return the errorMsgs
	 */
	public List<String> getErrorMsgs() {
		return errorMsgs;
	}

}
