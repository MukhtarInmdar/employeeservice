package com.sumadhura.employeeservice.service.dto;

/**
 * @author VENKAT
 *
 */
public class CityInfo {
	
	private Long Id;
	private String cityName;
	private String cityIcon;
	public Long getId() {
		return Id;
	}
	public void setId(Long id) {
		Id = id;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getCityIcon() {
		return cityIcon;
	}
	public void setCityIcon(String cityIcon) {
		this.cityIcon = cityIcon;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Id == null) ? 0 : Id.hashCode());
		result = prime * result + ((cityIcon == null) ? 0 : cityIcon.hashCode());
		result = prime * result + ((cityName == null) ? 0 : cityName.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CityInfo other = (CityInfo) obj;
		if (Id == null) {
			if (other.Id != null)
				return false;
		} else if (!Id.equals(other.Id))
			return false;
		if (cityIcon == null) {
			if (other.cityIcon != null)
				return false;
		} else if (!cityIcon.equals(other.cityIcon))
			return false;
		if (cityName == null) {
			if (other.cityName != null)
				return false;
		} else if (!cityName.equals(other.cityName))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "CityDTO [Id=" + Id + ", cityName=" + cityName + ", cityIcon=" + cityIcon + "]";
	}
	

}
