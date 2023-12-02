package com.sumadhura.employeeservice.enums;

public enum WorkFunction {
	SOFTWARE(1l,"Software"),
	SALES(2l,"Sales and Marketing"),
	HRA(3l,"HR/ Administration"),
	FIN(4l,"Finance"),
	PROD(5l,"Production"),
	LEGAL(6l,"Legal"),
	OPS(7l,"Operations"),
	;
	
	private WorkFunction(Long id, String name) {
		this.id = id;
		this.name = name;
		}
		private Long id;
		private String name;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
}
