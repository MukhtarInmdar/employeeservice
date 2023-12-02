/**
 * 
 */
package com.sumadhura.employeeservice.service.dto;

/**
 * @author VENKAT
 *
 */
public class StateInfo {

	private Long stateId;
	private String stateName;
	/**
	 * @return the stateId
	 */
	public Long getStateId() {
		return stateId;
	}
	/**
	 * @param stateId the stateId to set
	 */
	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}
	/**
	 * @return the stateName
	 */
	public String getStateName() {
		return stateName;
	}
	/**
	 * @param stateName the stateName to set
	 */
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StateDTO [stateId=" + stateId + ", stateName=" + stateName + "]";
	}
	
	
}
