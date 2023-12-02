package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class CustomerLeadCountPojo {

		
	@Column(name="total_lead")
	private int totalLead;
	
	@Column(name="total_lead_generated")
	private int totalLeadGenerated;
	
	@Column(name="total_lead_opportunity")
	private int totalLeadOpportunity;
	
	@Column(name="total_lead_booking")
	private int totalLeadBooking;
	
	
	public int getTotalLead() {
		return totalLead;
	}

	public void setTotalLead(int totalLead) {
		this.totalLead = totalLead;
	}

	public int getTotalLeadGenerated() {
		return totalLeadGenerated;
	}

	public void setTotalLeadGenerated(int totalLeadGenerated) {
		this.totalLeadGenerated = totalLeadGenerated;
	}

	public int getTotalLeadOpportunity() {
		return totalLeadOpportunity;
	}

	public void setTotalLeadOpportunity(int totalLeadOpportunity) {
		this.totalLeadOpportunity = totalLeadOpportunity;
	}

	public int getTotalLeadBooking() {
		return totalLeadBooking;
	}

	public void setTotalLeadBooking(int totalLeadBooking) {
		this.totalLeadBooking = totalLeadBooking;
	}

	

	
}
