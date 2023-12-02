package com.sumadhura.employeeservice.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/*
* Status enum provides different Status codes.
* 
* @author Venkat_Koniki
* @since 04.04.2019
* @time 11:12PM
*/
public enum Status {
	
	ALL(0L,"All"),
	INCOMPLETE(1L,"incomplete"),
	COMPLETED(2L,"complete"),
	ENQUIRED(3L,"enquired"),
	INTERESTED(4L,"interested"),
	PARTIAL(5L,"partially intrested"),
	ACTIVE(6L,"completed"),
	INACTIVE(7L,"In completed"),
	CLEARED(8L,"Cleared"),
	INPROGRESS(9L,"Inprogress"),
	RESOLVED(10L,"Resolved"),
	CLOSED(11L,"Closed"),
	OPEN(12L,"Open"),
	PUBLIC(13L,"Public"),
	APPROVED(14L,"APPROVED"),
	NOTAPPROVED(15L,"NOTAPPROVED"),
	REJECTED(16L,"REJECTED"),
	ESCALATED(17L,"ESCALATED"),
	TRUE(18L,"true"),
	FALSE(19L,"false"),
	PENDING(20L,"pending"),
	YES(21L,"yes"),
	NO(22L,"no"),
	NEW(23l,"New"),
	REOPEN(24l,"ReOpen"),
	REPLIED(25l,"Replied"),
	CREATE(26L,"Create"),
	UPDATE(27L,"Update"),
	PERMENANT(28L,"permenent"),//acp
	PAID(30L,"Paid"),
	PAID_WITH_INTEREST(31L,"Paid With Interest"),
	With_Interest(32L,"With Interest"),
	With_Out_Interest(33L,"With Out Interest"),
	RAISED(34L,"Raised"),
	RECEIVED(35l,"Received"),
	CREATED(36L,"Created"),
	TRANSACTION_COMPLETED(37L,"Transaction Completed"),
	MODIFY(38L,"Modify"),
	CHANGED(39L,"Changed"),
	REMOVED(40L,"Removed"),
	ADDED(41L,"Added"),
	MODIFY_REJECT(42L,"Modify Reject"),
	MODIFY_INACTIVE(43L,"Modify Inactive"),
	REGENERATED_DEMAND_NOTE(44l,"Regenerated Demand Note"),
	TRANSACTION_DELETED(45L,"Transaction Deleted"),
	TRANSACTION_SHIFTED(46L,"Transaction Shifted"),
	TRANSACTION_EDITED(47L,"Transaction Edited"),
	SHIFT_RECORD_DETAILS(48L,"Shift Record Details"),
	UNCLEARED_CHEQUE(49L,"Uncleared Cheque"),
	UPLOADED_DATA(50l,"Uploaded Data"),
    CHEQUE_BOUNCED(51L,"Cheque Bounced"),
    EXPLICIT_GENERATED_RECORD(52L,"Explicit_Generated_Record"),
    MS_COMPLETED(53l,"Completed"),
    MS_INCOMPLETE(54l,"Incomplete"),
    CANCELLED_NOT_REFUNDED(55l,"Cancelled Not Refunded"),
	SWAP(56l, "Swap"),
	AVAILABLE(57l, "Available"),
	BLOCKED(58l, "Blocked"),
	NOT_OPEN(59l, "Not Open"),
	//Booking Status
	PRICE_UPDATE(60l, "Price Update"),
	LEGAL_CASE(61l, "Legal Case"),
	PMAY_SCHEME_ELIGIBLE(62l, "PMAY Scheme Eligible"),
	RETAINED(63l, "Retained"),
	CANCEL(64l, "Cancel"),
	//Booking Status
	UPDATED(65l, "Updated"),
	BOOKED(66l, "Booked"),
    CANCELLED(67l, "Cancelled"),
    HOLD(68l, "Hold"),
    ALLOTTED(69l, "Allotted"),
    DELETED(70l, "Deleted"),
    ACTIVE_AND_COMPLETED(71l, "Active and Completed"),
    BOOKED_AND_COMPLETED(72l, "Booked and Completed"),
    MEETING_HAPPENED(73l, "Meeting Happened"),
    MEETING_NOT_HAPPENED(74l, "Meeting Not Happened"),
    MODIFY_CREATED(75L,"Modify Crated"),
    PENDING_FOR_APPROVAL(76l, "Pending For Approval"),
    ASSIGNMENT(77l, "Assignment"),
	READ(78l,"Read"),
	UNREAD(79l,"Unread"),
	AMOUNT_DISBURSED(80l,"Amount Disbursed"),
	;
	
	public Long status;
	public String description;
	
	private static Map<Long, String> statusIdDescriptionMap = new LinkedHashMap<>();
	
	static{
		for(Status status : Status.values()) {
			statusIdDescriptionMap.put(status.status, status.description);
		}
	}

	Status(Long status,String description) {
		this.status = status;
		this.description = description;
	}

	/**
	 * @return the status
	 */
	public Long getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Long status) {
		this.status = status;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/* Getting Description by Status Id */
	public static String getDescriptionByStatus(Long status) {
		return statusIdDescriptionMap.get(status);
	}

}
