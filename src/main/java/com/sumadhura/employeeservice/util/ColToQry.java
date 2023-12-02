package com.sumadhura.employeeservice.util;

public class ColToQry {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String tabName = "REFERENCE_MASTER";
		 String str = "@Column(name=\"REFERENCE_ID\")_____private long referenceId_____@Column(name=\"REFERENCE_TYPE\")_____private String referenceType_____@Column(name=\"STATUS_ID\")_____private long statusId_____@Column(name=\"CREATED_DATE\")_____private Timestamp createdDate_____@Column(name=\"MODIFIED_DATE\")_____private Timestamp modifiedDate_____";
		 String[] strLinesArr = str.split("_____");//(\r\n|\r|\n)");
		 String[] colNames = new String[strLinesArr.length];
		 for(int i=0;i<strLinesArr.length;i++)
		 {
		     if(strLinesArr[i].trim().startsWith("@Column"))
		         colNames[i] = strLinesArr[i].trim().replace("@Column","").trim().replace("(name=\"","").replace("\")","");
		 }
		 String qryStr = "public static final String QRY_TO_GET_"+tabName+" = new StringBuilder(\"SELECT \")\r\n";
		 
		 for(int j=0;j<colNames.length;j++)
		 {
		     if(colNames[j]!=null)
		         qryStr = qryStr + ".append(\"" + colNames[j] + " AS " + colNames[j] + ",\")" + "\r\n";
		 }
		 qryStr = qryStr + ".append(\"FROM "+tabName+"\")"+"\r\n";
		 qryStr = qryStr + ".toString();";
		 System.out.println(qryStr);
		
	}

}
