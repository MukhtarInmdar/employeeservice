/**
 * 
 */
package com.sumadhura.employeeservice.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sumadhura.employeeservice.persistence.dto.FeedBackDTO;

/**
 * @author VENKAT
 * DATE 05-FEB-2019
 * TIME 02.57 PM
 */

@JsonIgnoreProperties(ignoreUnknown=true)
public class FeedBackWrapper extends Result  {
	
	private static final long serialVersionUID = -3131531107889329005L;
	private List<FeedBackDTO> feedbackList;

	/**
	 * @return the feedbackList
	 */
	public List<FeedBackDTO> getFeedbackList() {
		return feedbackList;
	}

	/**
	 * @param feedbackList the feedbackList to set
	 */
	public void setFeedbackList(List<FeedBackDTO> feedbackList) {
		this.feedbackList = feedbackList;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FeedBackwrapper [feedbackList=" + feedbackList + "]";
	}
	
}
