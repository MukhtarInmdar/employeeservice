/**
 * 
 */
package com.sumadhura.employeeservice.service.dto;

import lombok.Data;

@Data
public class MileStoneInfo {
	private String projectMilestoneId;
	private Long milStoneNo;
	private String milestoneName;
	private String due;
	private String demandNoteDate;
	private String demandNoteDueDate;
	private String totalFlatCostIncludeGST;
	private String amountRecivedIncludeGST;
	private String dueAmountExcludeGST;
	private boolean thisLastMsRecord;
	private String CGST;
	private String SGST;
	private String gstAmount;
	private String totalDueAmount;
	private String intrestIncludeGST;
	private String SAC;
	private String daysDelayed;
}
