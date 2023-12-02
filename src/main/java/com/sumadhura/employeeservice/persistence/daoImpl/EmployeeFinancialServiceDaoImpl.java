package com.sumadhura.employeeservice.persistence.daoImpl;
import static com.sumadhura.employeeservice.util.Util.isNotEmptyObject;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.sumadhura.employeeservice.dto.EmployeeFinancialRequest;
import com.sumadhura.employeeservice.dto.EmployeeFinancialTransactionRequest;
import com.sumadhura.employeeservice.dto.FileInfo;
import com.sumadhura.employeeservice.dto.FinancialProjectMileStoneRequest;
import com.sumadhura.employeeservice.dto.Site;
import com.sumadhura.employeeservice.enums.Department;
import com.sumadhura.employeeservice.enums.FinEnum;
import com.sumadhura.employeeservice.enums.FinTransactionMode;
import com.sumadhura.employeeservice.enums.FinTransactionType;
import com.sumadhura.employeeservice.enums.FinancialQuerys;
import com.sumadhura.employeeservice.enums.MetadataId;
import com.sumadhura.employeeservice.enums.SqlQuery;
import com.sumadhura.employeeservice.enums.SqlQueryTwo;
import com.sumadhura.employeeservice.enums.Status;
import com.sumadhura.employeeservice.exception.EmployeeFinancialServiceException;
import com.sumadhura.employeeservice.exception.InSufficeientInputException;
import com.sumadhura.employeeservice.exception.ResultSetMappingException;
import com.sumadhura.employeeservice.persistence.dao.EmployeeFinancialServiceDao;
import com.sumadhura.employeeservice.persistence.dto.AddressPojo;
import com.sumadhura.employeeservice.persistence.dto.CoApplicantPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerPropertyDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.DropDownPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.FinAnnyApproveStatPojo;
import com.sumadhura.employeeservice.persistence.dto.FinAnnyEntryCommentsPojo;
import com.sumadhura.employeeservice.persistence.dto.FinAnnyEntryDocPojo;
import com.sumadhura.employeeservice.persistence.dto.FinAnonymousEntryMapPojo;
import com.sumadhura.employeeservice.persistence.dto.FinAnonymousEntryPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBankPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBokFrmDemNteSchTaxMapPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBokFrmFlatKhataTaxPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBokFrmLglCostTaxPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBokFrmMaintenanceTaxPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormAccountPaymentDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormAccountPaymentPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormAccountSummaryPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormAccountsPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormAccountsStatementPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormCorpusFundPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormDemandNoteDocPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormDemandNotePojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormExcessAmountPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormExcessAmountUsagePojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormFlatKhataDtlsPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormFlatKhataPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormLegalCostPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormLglCostDtlsPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormMaintenanceDtlsPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormMaintenancePojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormMilestoneTaxPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormMilestonesPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormModiCostDtlsPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormModiCostPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormModiCostTaxPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormReceiptsPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormRefundableAdvancePojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormTdsDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.FinClearedUncleareTXPojo;
import com.sumadhura.employeeservice.persistence.dto.FinClosingBalanceReportPojo;
import com.sumadhura.employeeservice.persistence.dto.FinInterestRatesPojo;
import com.sumadhura.employeeservice.persistence.dto.FinMSChangedDtlsPojo;
import com.sumadhura.employeeservice.persistence.dto.FinMilDemNoteMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.FinModificationInvoicePojo;
import com.sumadhura.employeeservice.persistence.dto.FinPenaltyPojo;
import com.sumadhura.employeeservice.persistence.dto.FinPenaltyStatisticsPojo;
import com.sumadhura.employeeservice.persistence.dto.FinPenaltyTaxMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.FinPenaltyTaxPojo;
import com.sumadhura.employeeservice.persistence.dto.FinProjDemNoteStatisticsPojo;
import com.sumadhura.employeeservice.persistence.dto.FinProjectAccountPojo;
import com.sumadhura.employeeservice.persistence.dto.FinSchemePojo;
import com.sumadhura.employeeservice.persistence.dto.FinSchemeTaxMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.FinTaxPojo;
import com.sumadhura.employeeservice.persistence.dto.FinTransactionApprRejectStatPojo;
import com.sumadhura.employeeservice.persistence.dto.FinTransactionApprStatPojo;
import com.sumadhura.employeeservice.persistence.dto.FinTransactionApprovalDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.FinTransactionChangedDtlsPojo;
import com.sumadhura.employeeservice.persistence.dto.FinTransactionChequePojo;
import com.sumadhura.employeeservice.persistence.dto.FinTransactionCommentsPojo;
import com.sumadhura.employeeservice.persistence.dto.FinTransactionEntryDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.FinTransactionEntryDocPojo;
import com.sumadhura.employeeservice.persistence.dto.FinTransactionEntryPojo;
import com.sumadhura.employeeservice.persistence.dto.FinTransactionForPojo;
import com.sumadhura.employeeservice.persistence.dto.FinTransactionModePojo;
import com.sumadhura.employeeservice.persistence.dto.FinTransactionOnlinePojo;
import com.sumadhura.employeeservice.persistence.dto.FinTransactionPmtSetOffAccMapPojo;
import com.sumadhura.employeeservice.persistence.dto.FinTransactionSetOffAccMapPojo;
import com.sumadhura.employeeservice.persistence.dto.FinTransactionSetOffApprovalPojo;
import com.sumadhura.employeeservice.persistence.dto.FinTransactionSetOffEntryPojo;
import com.sumadhura.employeeservice.persistence.dto.FinTransactionSetOffPojo;
import com.sumadhura.employeeservice.persistence.dto.FinTransactionTypePojo;
import com.sumadhura.employeeservice.persistence.dto.FinTransactionWaivedOffPojo;
import com.sumadhura.employeeservice.persistence.dto.FinTransferModePojo;
import com.sumadhura.employeeservice.persistence.dto.FinancialDemandNoteMS_TRN_Pojo;
import com.sumadhura.employeeservice.persistence.dto.FinancialDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.FinancialMileStoneClassifideMappingBlocksPojo;
import com.sumadhura.employeeservice.persistence.dto.FinancialMileStoneClassifidesPojo;
import com.sumadhura.employeeservice.persistence.dto.FinancialProjectDemandNotePojo;
import com.sumadhura.employeeservice.persistence.dto.FinancialProjectMileStonePojo;
import com.sumadhura.employeeservice.persistence.dto.FlatBookingPojo;
import com.sumadhura.employeeservice.persistence.dto.FlatBookingSchemeMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.FlatCancellationCostPojo;
import com.sumadhura.employeeservice.persistence.dto.FlatCostPojo;
import com.sumadhura.employeeservice.persistence.dto.FlatSaleOwnersByAccount;
import com.sumadhura.employeeservice.persistence.dto.InvoiceDocumentLocationPojo;
import com.sumadhura.employeeservice.persistence.dto.ModicationInvoiceAppRej;
import com.sumadhura.employeeservice.persistence.dto.OfficeDtlsPojo;
import com.sumadhura.employeeservice.persistence.dto.PercentagesPojo;
import com.sumadhura.employeeservice.persistence.dto.SiteDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.SitePojo;
import com.sumadhura.employeeservice.persistence.dto.StatePojo;
import com.sumadhura.employeeservice.persistence.dto.StatusPojo;
import com.sumadhura.employeeservice.service.dto.BookingFormRequest;
import com.sumadhura.employeeservice.service.dto.CustomerInfo;
import com.sumadhura.employeeservice.service.dto.CustomerPropertyDetailsInfo;
import com.sumadhura.employeeservice.service.dto.EmployeeFinTranPaymentSetOffInfo;
import com.sumadhura.employeeservice.service.dto.EmployeeFinancialServiceInfo;
import com.sumadhura.employeeservice.service.dto.EmployeeFinancialTransactionServiceInfo;
import com.sumadhura.employeeservice.service.dto.FinAnonymousEntryInfo;
import com.sumadhura.employeeservice.service.dto.FinBookingFormAccountsInfo;
import com.sumadhura.employeeservice.service.dto.FinBookingFormMstSchTaxMapInfo;
import com.sumadhura.employeeservice.service.dto.FinPenalityInfo;
import com.sumadhura.employeeservice.service.dto.FinTransactionEntryDetailsInfo;
import com.sumadhura.employeeservice.service.dto.FinancialDemandNoteMS_TRN_Info;
import com.sumadhura.employeeservice.service.dto.FinancialGstDetailsInfo;
import com.sumadhura.employeeservice.service.dto.FinancialProjectMileStoneInfo;
import com.sumadhura.employeeservice.service.dto.FinancialSchemeInfo;
import com.sumadhura.employeeservice.service.mappers.EmployeeFinancialMapper;
import com.sumadhura.employeeservice.service.mappers.ExtractDataFromResultSet;
import com.sumadhura.employeeservice.service.mappers.ResultSetMapper;
import com.sumadhura.employeeservice.serviceImpl.EmployeeFinancialServiceImpl;
import com.sumadhura.employeeservice.util.DateToWord;
import com.sumadhura.employeeservice.util.TimeUtil;
import com.sumadhura.employeeservice.util.Util;
import com.sun.istack.NotNull;

import lombok.NonNull;
/**
 * @author @NIKET CH@V@N
 * @since 13-01-2020
 * @time 11:30 PM
 */
@Repository("EmployeeFinancialServiceDao")
public class EmployeeFinancialServiceDaoImpl implements EmployeeFinancialServiceDao{

	@Autowired(required = true)
	@Qualifier("nmdPJdbcTemplate")
	private NamedParameterJdbcTemplate nmdPJdbcTemplate;

	private static final Logger log = Logger.getLogger(EmployeeFinancialServiceDaoImpl.class);
	
	public static final List<Long> ALL_TYPES_OF_PAYMENTS = Arrays.asList(MetadataId.FIN_BOOKING_FORM_MILESTONES.getId(),MetadataId.PRINCIPAL_AMOUNT.getId()
			, MetadataId.TDS.getId(),MetadataId.MODIFICATION_COST.getId(), MetadataId.LEGAL_COST.getId(), MetadataId.FIN_PENALTY.getId()
			,MetadataId.REFUNDABLE_ADVANCE.getId(),MetadataId.FLAT_CANCELLATION.getId()
			,MetadataId.MAINTENANCE_CHARGE.getId(),MetadataId.INDIVIDUAL_FLAT_KHATA_BIFURCATION_AND_OTHER_CHARGES.getId(),MetadataId.CORPUS_FUND.getId()
			);
	
	public static final List<String> TESTING_FLAT = Arrays.asList("EG-TEST","AR-TEST","SU-TEST","NA-TEST","HO-TEST","GBB-TEST");
	/*
	 * GST Calculation Formula
		For calculating GST, a taxpayer can use the below mentioned formula :
		
		In order to add GST to base amount,
		
		Add GST
		GST Amount = ( Original Cost * GST% ) / 100
		Net Price = Original Cost + GST Amount
		
		In order to remove GST from base amount,
		
		Remove GST
		GST Amount = Original Cost – (Original Cost * (100 / (100 + GST% ) ) )
		Net Price = Original Cost – GST Amount
	 * 
	 * */
	
	//FinancialQuerys.QRY_TO_EXPORTS_TRANSACTION_DATA;
	
	//query to check the approvals of the transaction's		
	/*		SELECT E.FIN_TRN_SET_OFF_APPR_LVL_ID, E.FIN_TRAN_SET_OFF_LEVEL_ID, E.NEXT_TRN_SET_OFF_APPR_LVL_ID,  E.STATUS_ID,
			  D.STATUS_ID,  D.FIN_TRAN_SET_OFF_LEVEL_ID, D.FIN_TRANSACTION_TYPE_ID,  D.FIN_TRANSACTION_MODE_ID,  D.SITE_ID,
			  D.FIN_LEVEL_ID, D.AGREEMENT_COMPLETED, G.FIN_TRN_EMP_SET_OFF_LVL_MAP_ID, G.EMP_ID, G.FIN_TRAN_SET_OFF_LEVEL_ID,
			  G.STATUS_ID, F.FIN_LEVEL_ID, F.LEVEL_NAME FROM FIN_TRANSACTION_SET_OFF_APPROVAL_LEVEL E,
			  FIN_TRANSACTION_SET_OFF_LEVEL D, FIN_TRANSACTION_LEVEL F, FIN_TRN_EMP_SET_OFF_LEVEL_MAPPING G
			WHERE D.FIN_TRAN_SET_OFF_LEVEL_ID = E.FIN_TRAN_SET_OFF_LEVEL_ID AND F.FIN_LEVEL_ID                = D.FIN_LEVEL_ID
			AND D.FIN_TRAN_SET_OFF_LEVEL_ID   = G.FIN_TRAN_SET_OFF_LEVEL_ID and D.status_id = 6
			and D.FIN_TRANSACTION_TYPE_ID = 1 and D.FIN_TRANSACTION_MODE_ID = 1;
			
			  SELECT  --D.STATUS_ID,  D.FIN_TRAN_SET_OFF_LEVEL_ID,FTSOAL.NEXT_TRN_SET_OFF_APPR_LVL_ID, D.FIN_TRANSACTION_TYPE_ID, D.FIN_TRANSACTION_MODE_ID,
			  (SELECT LISTAGG(SET_OFF_TYPE, ',')   WITHIN GROUP (ORDER BY  FIN_TRAN_SET_OFF_LEVEL_ID)  AS EMP_NAME FROM FIN_TRN_SET_OFF_APPR_TYPE F1 WHERE D.FIN_TRAN_SET_OFF_LEVEL_ID = F1.FIN_TRAN_SET_OFF_LEVEL_ID AND STATUS_ID = 6) AS SETOF,
			  D.SITE_ID, D.FIN_LEVEL_ID, D.AGREEMENT_COMPLETED,	 G.FIN_TRAN_SET_OFF_LEVEL_ID,
			  G.EMP_ID, EL.USERNAME,E.FIRST_NAME,E.EMAIL,  G.FIN_TRAN_SET_OFF_LEVEL_ID,  G.STATUS_ID, F.FIN_LEVEL_ID, F.LEVEL_NAME
			  FROM    FIN_TRANSACTION_SET_OFF_APPROVAL_LEVEL FTSOAL,
			  FIN_TRANSACTION_SET_OFF_LEVEL D, FIN_TRANSACTION_LEVEL F,
			  FIN_TRN_EMP_SET_OFF_LEVEL_MAPPING G, EMPLOYEE_LOGIN EL
	      	  INNER JOIN EMPLOYEE E ON E.EMP_ID = EL.EMPLOYEE_ID
			  WHERE F.FIN_LEVEL_ID = D.FIN_LEVEL_ID		AND D.FIN_TRAN_SET_OFF_LEVEL_ID   = G.FIN_TRAN_SET_OFF_LEVEL_ID
              and D.FIN_TRAN_SET_OFF_LEVEL_ID = FTSOAL.FIN_TRAN_SET_OFF_LEVEL_ID
	          AND EL.EMPLOYEE_ID= G.EMP_ID AND D.STATUS_ID = 6		AND D.FIN_TRANSACTION_TYPE_ID = 2
			  AND D.FIN_TRANSACTION_MODE_ID = 1    AND D.SITE_ID = 111   AND G.STATUS_ID = 6   
	    	  ORDER BY  D.FIN_TRAN_SET_OFF_LEVEL_ID,F.FIN_LEVEL_ID;

SELECT FIN_TRN_EMP_SET_OFF_LVL_MAP_ID,EMP_ID,FIN_TRAN_SET_OFF_LEVEL_ID FROM FIN_TRN_EMP_SET_OFF_LEVEL_MAPPING WHERE FIN_TRAN_SET_OFF_LEVEL_ID IN (33,34,35) ORDER BY FIN_TRAN_SET_OFF_LEVEL_ID;
SELECT FIN_TRN_SET_OFF_APPR_LVL_ID,NEXT_TRN_SET_OFF_APPR_LVL_ID,FIN_TRAN_SET_OFF_LEVEL_ID
FROM FIN_TRANSACTION_SET_OFF_APPROVAL_LEVEL WHERE FIN_TRAN_SET_OFF_LEVEL_ID IN (33,34,35);
	    
	    SELECT (select SET_OFF_TYPE from FIN_TRN_SET_OFF_APPR_TYPE where D.FIN_TRAN_SET_OFF_LEVEL_ID = FIN_TRAN_SET_OFF_LEVEL_ID fetch first row only) as setoff
    ,  D.FIN_TRAN_SET_OFF_LEVEL_ID,FTSOAL.NEXT_TRN_SET_OFF_APPR_LVL_ID,
	NEXT_APPROVAL_MODULE,		  D.SITE_ID, D.FIN_LEVEL_ID, D.AGREEMENT_COMPLETED,
	 F.FIN_LEVEL_ID, F.LEVEL_NAME
			FROM    FIN_TRANSACTION_SET_OFF_APPROVAL_LEVEL FTSOAL,
			  FIN_TRANSACTION_SET_OFF_LEVEL D, FIN_TRANSACTION_LEVEL F
			  WHERE F.FIN_LEVEL_ID = D.FIN_LEVEL_ID		
              and D.FIN_TRAN_SET_OFF_LEVEL_ID = FTSOAL.FIN_TRAN_SET_OFF_LEVEL_ID
	         AND D.STATUS_ID = 6		AND D.FIN_TRANSACTION_TYPE_ID = 1
			  AND D.FIN_TRANSACTION_MODE_ID = 1    AND D.SITE_ID = 124    
	    	  ORDER BY  D.FIN_TRAN_SET_OFF_LEVEL_ID,F.FIN_LEVEL_ID;
          
          update FIN_TRANSACTION_SET_OFF_LEVEL set NEXT_APPROVAL1_MODULE = 44 where FIN_TRAN_SET_OFF_LEVEL_ID = 134;
    
	    
	    //for getting department roll id
 SELECT EL.ID,  EL.EMPLOYEE_ID,  EL.USERNAME,  EL.PASSWORD,DEPT_ID,
 DRM.ROLE_ID,ER.NAME AS ROLL,EL.STATUS, -- EDM.EMPLOYEE_DEPARTMENT_MAPING_ID,
 EDM.DEPARTMENT_ROLE_MAPPING_ID,  EDM.STATUS
 FROM EMPLOYEE_LOGIN EL INNER JOIN  
 EMPLOYEE_DEPARTMENT_MAPING EDM ON   EL.EMPLOYEE_ID  = EDM.EMPLOYEE_ID
 INNER JOIN DEPARTMENT_ROLE_MAPPING DRM ON EDM.DEPARTMENT_ROLE_MAPPING_ID = DRM.DEPARTMENT_ROLE_MAPPING_ID
 INNER JOIN EMPLOYEE_ROLES ER ON DRM.ROLE_ID = ER.ROLE_ID
 WHERE UPPER(EL.USERNAME) LIKE UPPER('%PURNIMA%')  AND EL.STATUS = 6;
   
	  
	  SELECT EL.ID,DP.DEPT_ID,DP.NAME dept,ER.ROLE_ID,ER.NAME roll,  EL.EMPLOYEE_ID,  EL.USERNAME,  EL.PASSWORD,
	  EL.STATUS, -- EDM.EMPLOYEE_DEPARTMENT_MAPING_ID,
	  EDM.DEPARTMENT_ROLE_MAPPING_ID,  EDM.STATUS
	FROM EMPLOYEE_LOGIN EL INNER JOIN  
	  EMPLOYEE_DEPARTMENT_MAPING EDM ON   EL.EMPLOYEE_ID  = EDM.EMPLOYEE_ID
    join DEPARTMENT_ROLE_MAPPING DRM on  DRM.DEPARTMENT_ROLE_MAPPING_ID=  EDM.DEPARTMENT_ROLE_MAPPING_ID
    join EMPLOYEE_ROLES ER on ER.ROLE_ID = DRM.ROLE_ID
    join DEPARTMENT DP on DRM.DEPT_ID = DP.DEPT_ID
	  WHERE --UPPER(EL.USERNAME) like UPPER('%bhanu%')
     UPPER(DP.NAME) like UPPER('%CRM FINANCE%')
    AND EL.STATUS = 6;
	  
	  select * from EMPLOYEE_SUBMENU_MODULE 
WHERE UPPER(SUB_MODULE_NAME) like UPPER('%rate of%')  AND STATUS_id = 6;


		  //for getting sub module accss department roll id wise
		    SELECT  B.DEPARTMENT_ROLE_MAPPING_ID,C.SITE_ID, SBM.SUB_MODULE_NAME,
	  B.MENU_MAPPING_ID,  C.SUBMENU_SITE_MAPPING_ID,  C.MENU_SUB_MENU_MAPPING_ID,
	   A.MODULE_ID,  A.SUBMODULE_ID
	FROM EMPLOYEE_ROLE_MENU_GROUPING B,  EMPLOYEE_MENU_SUBMENU_MAPPING A,
	  EMPLOYEE_SUBMENU_SITE_MAPPING C,EMPLOYEE_SUBMENU_MODULE SBM
	WHERE A.MENU_SUB_MENU_MAPPING_ID = B.MENU_MAPPING_ID
	AND A.MENU_SUB_MENU_MAPPING_ID   = C.MENU_SUB_MENU_MAPPING_ID
  AND SBM.SUB_MODULE_ID =  A.SUBMODULE_ID
  --and a.status_id = 6 
	   and b.status_id = 6 and a.MODULE_ID = 12 and  A.SUBMODULE_ID in ( 26)  and B.DEPARTMENT_ROLE_MAPPING_ID = 64;

	//get DEPARTMENT_ROLE_MAPPING_ID for menu mapping using department name
			SELECT DP.DEPT_ID,  DP.NAME AS DP_NAME,ESSM.site_id, el.EMP_ID,el.FIRST_NAME, DRM.DEPARTMENT_ROLE_MAPPING_ID,
		ER.NAME AS Roll_NAME,DRM.ROLE_ID
		FROM DEPARTMENT_ROLE_MAPPING DRM,  DEPARTMENT DP,  EMPLOYEE_ROLES ER
		,EMPLOYEE_DEPARTMENT_MAPING edm,EMPLOYEE el,EMPLOYEE_DETAILS ed
		,EMPLOYEE_ROLE_MENU_GROUPING ERMG,EMPLOYEE_SUBMENU_SITE_MAPPING ESSM
		WHERE DP.DEPT_ID = DRM.DEPT_ID AND ER.ROLE_ID   = DRM.ROLE_ID
		and edm.DEPARTMENT_ROLE_MAPPING_ID = DRM.DEPARTMENT_ROLE_MAPPING_ID
		and ed.EMPLOYEE_ID = el.EMP_ID
		AND ERMG.DEPARTMENT_ROLE_MAPPING_ID = DRM.DEPARTMENT_ROLE_MAPPING_ID
		and ERMG.MENU_MAPPING_ID = ESSM.MENU_SUB_MENU_MAPPING_ID
		and edm.EMPLOYEE_ID = el.EMP_ID and edm.status = 6
		AND  UPPER(DP.NAME)  = UPPER('TECH CRM ')
		GROUP BY  DP.DEPT_ID,  DP.NAME,ESSM.site_id, el.EMP_ID,el.FIRST_NAME, DRM.DEPARTMENT_ROLE_MAPPING_ID,
		ER.NAME,DRM.ROLE_ID
		order by ESSM.site_id, el.EMP_ID;
		
		
			SELECT SBM.SUB_MODULE_NAME,DP.DEPT_ID,  DP.NAME AS DP_NAME,ESSM.site_id, el.EMP_ID,el.FIRST_NAME, DRM.DEPARTMENT_ROLE_MAPPING_ID,
		ER.NAME AS Roll_NAME,DRM.ROLE_ID
		FROM DEPARTMENT_ROLE_MAPPING DRM,  DEPARTMENT DP,  EMPLOYEE_ROLES ER
		,EMPLOYEE_DEPARTMENT_MAPING edm,EMPLOYEE el,EMPLOYEE_DETAILS ed
		,EMPLOYEE_ROLE_MENU_GROUPING ERMG,EMPLOYEE_SUBMENU_SITE_MAPPING ESSM,
    EMPLOYEE_SUBMENU_MODULE SBM,EMPLOYEE_MENU_SUBMENU_MAPPING EMSM
		WHERE DP.DEPT_ID = DRM.DEPT_ID AND ER.ROLE_ID   = DRM.ROLE_ID
		and edm.DEPARTMENT_ROLE_MAPPING_ID = DRM.DEPARTMENT_ROLE_MAPPING_ID
    AND EMSM.MENU_SUB_MENU_MAPPING_ID = ERMG.MENU_MAPPING_ID
    AND SBM.SUB_MODULE_ID = EMSM.SUBMODULE_ID
		and ed.EMPLOYEE_ID = el.EMP_ID
		AND ERMG.DEPARTMENT_ROLE_MAPPING_ID = DRM.DEPARTMENT_ROLE_MAPPING_ID
		and ERMG.MENU_MAPPING_ID = ESSM.MENU_SUB_MENU_MAPPING_ID
		and edm.EMPLOYEE_ID = el.EMP_ID and edm.status = 6
		AND  UPPER(DP.NAME)  = UPPER('TECH CRM ')
    and  EMSM.SUBMODULE_ID in (30)
		GROUP BY  SBM.SUB_MODULE_NAME,DP.DEPT_ID,  DP.NAME,ESSM.site_id, el.EMP_ID,el.FIRST_NAME, DRM.DEPARTMENT_ROLE_MAPPING_ID,
		ER.NAME,DRM.ROLE_ID
		order by ESSM.site_id, el.EMP_ID;
    
		
		
//get DEPARTMENT_ROLE_MAPPING_ID for menu mapping using department name		
		SELECT DP.DEPT_ID,  DP.NAME AS DP_NAME,ESSM.site_id, el.EMP_ID,el.FIRST_NAME, DRM.DEPARTMENT_ROLE_MAPPING_ID,
		ER.NAME AS Roll_NAME,DRM.ROLE_ID
		FROM DEPARTMENT_ROLE_MAPPING DRM,  DEPARTMENT DP,  EMPLOYEE_ROLES ER
		,EMPLOYEE_DEPARTMENT_MAPING edm,EMPLOYEE el,EMPLOYEE_DETAILS ed
		,EMPLOYEE_ROLE_MENU_GROUPING ERMG,EMPLOYEE_SUBMENU_SITE_MAPPING ESSM
		,EMPLOYEE_MENU_SUBMENU_MAPPING EMSM INNER JOIN EMPLOYEE_SUBMENU_MODULE SBM ON SBM.SUB_MODULE_ID =EMSM. SUBMODULE_ID AND SBM.SUB_MODULE_ID = 30
		WHERE DP.DEPT_ID = DRM.DEPT_ID AND ER.ROLE_ID   = DRM.ROLE_ID
		and edm.DEPARTMENT_ROLE_MAPPING_ID = DRM.DEPARTMENT_ROLE_MAPPING_ID
		and ed.EMPLOYEE_ID = el.EMP_ID
		AND ERMG.DEPARTMENT_ROLE_MAPPING_ID = DRM.DEPARTMENT_ROLE_MAPPING_ID
		and ERMG.MENU_MAPPING_ID = ESSM.MENU_SUB_MENU_MAPPING_ID
		and edm.EMPLOYEE_ID = el.EMP_ID and edm.status = 6
		--and DRM.DEPARTMENT_ROLE_MAPPING_ID not in (6,16,17,28,33,42)
		AND  UPPER(DP.NAME)  = UPPER('TECH CRM ')
		and EMSM.MENU_SUB_MENU_MAPPING_ID = ERMG.MENU_MAPPING_ID
		GROUP BY  DP.DEPT_ID,  DP.NAME,ESSM.site_id, el.EMP_ID,el.FIRST_NAME, DRM.DEPARTMENT_ROLE_MAPPING_ID,
		ER.NAME,DRM.ROLE_ID
		order by  el.EMP_ID,ESSM.site_id;

		//get the customer involved scheme
		
		SELECT FSTM.SITE_ID,PER.PERCENTAGE,FS.NAME AS SCHEME_NAME,FBSM.FLAT_BOOK_ID,
		(SELECT F.FLAT_NO FROM FLAT F ,FLAT_BOOKING FB WHERE FB.FLAT_ID = F.FLAT_ID AND FB.FLAT_BOOK_ID =FBSM.FLAT_BOOK_ID  AND FB.STATUS_ID = 6) AS FLAT_NO
		FROM FLAT_BOK_SCHM_MAPPING FBSM,FLAT_BOOKING FB,Flat flt,
		  FIN_SCHEME_TAX_MAPPING FSTM INNER JOIN PERCENTAGES PER ON PER.PERCENTAGE_ID = FSTM.PERCENTAGE_ID ,FIN_SCHEME FS
		WHERE FSTM.FIN_SCHE_TAX_MAP_ID = FBSM.FIN_SCHE_TAX_MAP_ID
		AND FS.FIN_SCHEME_ID = FSTM.FIN_SCHEME_ID
    AND FB.FLAT_BOOK_ID = FBSM.FLAT_BOOK_ID
    AND FBSM.status_id = 6
    and flt.FLAT_ID = FB.FLAT_ID and flt.status_id = 6
		AND FSTM.SITE_ID  = 107  and FSTM.status_id = 6 and FB.status_id = 6
    	--and FSTM.status_id = 6
		--and FBSM.FLAT_BOOK_ID = (SELECT FB.FLAT_BOOK_ID FROM FLAT F ,FLAT_BOOKING FB WHERE FB.FLAT_ID = F.FLAT_ID AND FB.FLAT_BOOK_ID =FBSM.FLAT_BOOK_ID  AND FB.STATUS_ID = 6 and f.flat_no = 'A501');
		;
		
		//GET THE E
		SELECT EDSM.EMP_DEP_SITE_MAP_ID,  EDSM.SITE_ID,  EDSM.BLOCK_ID,  EDSM.DEPT_ID,
  EDSM.ROLE_ID, EL.EMP_ID,  EL.FIRST_NAME,  EL.LAST_NAME,  EL.EMAIL
FROM EMPLOYEE_DEPT_SITE_MAPPING EDSM,
  EMPLOYEE EL INNER JOIN 
  EMPLOYEE_DEPARTMENT_MAPING EDM  ON EL.EMP_ID = EDM.EMPLOYEE_ID
  INNER JOIN   DEPARTMENT_ROLE_MAPPING  DRM ON EDM.DEPARTMENT_ROLE_MAPPING_ID = DRM.DEPARTMENT_ROLE_MAPPING_ID  
  INNER JOIN EMPLOYEE_ROLES ER ON DRM.ROLE_ID = eR.ROLE_ID
WHERE EL.EMP_ID = EDSM.EMPLOYEE_ID;
		
		//get the customer involved scheme Site
		SELECT    B.SITE_ID, A.FIN_SCHEME_ID,  A.NAME,per.PERCENTAGE,   B.PERCENTAGE_ID,  B.START_DATE,  B.END_DATE 
		FROM FIN_SCHEME_TAX_MAPPING B,
		  FIN_SCHEME A,percentages per
		WHERE A.FIN_SCHEME_ID = B.FIN_SCHEME_ID and  B.PERCENTAGE_ID =  per.PERCENTAGE_ID and b.site_id = 107;


			//get the flat available detail
							select
    distinct BD.BLOCK_ID,BL.NAME,fl.name,FD.FLOOR_ID,f.flat_no
    --f.flat_no,BD.BLOCK_ID,BL.NAME,fl.name
    FROM FLAT F
			INNER JOIN FLOOR_DETAILS FD
			ON F.FLOOR_DET_ID = FD.FLOOR_DET_ID
			INNER JOIN FLOOR FL
			ON FL.FLOOR_ID = FD.FLOOR_ID
			INNER JOIN BLOCK_DETAILS BD
			ON FD.BLOCK_DET_ID = BD.BLOCK_DET_ID
			INNER JOIN BLOCK BL
			ON BL.BLOCK_ID = BD.BLOCK_ID
			INNER JOIN SITE S
			ON BD.SITE_ID = S.SITE_ID where s.site_id = 111 AND BL.STATUS_ID = 6 AND FD.STATUS_ID = 6
			AND F.STATUS_ID = 6
			AND f.flat_no = 'B904'
			and  BD.BLOCK_ID = 160		;

	//find completed milestone data 
SELECT fbfm.BOOKING_FORM_ID,fbfm.FIN_BOK_FOM_DMD_NOTE_ID,  A.FIN_MILESTONE_CLASSIFIDES_ID,
  A.SITE_ID,  B.MILESTONE_NAME,  B.MILE_STONE_NO,  B.STATUS_ID,  B.PROJECT_MS_STATUS_ID,
  B.MASTER_DMD_NOTE_DATE,
  B.MASTER_DMD_DUE_DATE
FROM FIN_PROJECT_MILESTONES B,
  FIN_MILESTONE_CLASSIFIDES A,
  FIN_BOOKING_FORM_MILESTONES FBFM
WHERE A.FIN_MILESTONE_CLASSIFIDES_ID = B.FIN_MILESTONE_CLASSIFIDES_ID
AND FBFM.PROJECT_MILESTONE_ID = B.PROJECT_MILESTONE_ID
AND A.SITE_ID = 111 AND B.PROJECT_MS_STATUS_ID = 53 AND FBFM.MS_STATUS_ID = 52 and fbfm.status_id =6;

//get transaction wise set off details
 
-SELECT  A.FIN_TRANSACTION_ENTRY_ID,FIN_TRANSACTION_TYPE_ID, FIN_TRANSACTION_MODE_ID,
 RECEIVED_DATE,PAYMENT_DATE ,C.FIN_TRN_SET_OFF_ENT_ID,  A.SITE_ID,  A.BOOKING_FORM_ID,  A.AMOUNT,  C.SET_OFF_TYPE,  C.PAYABLE_AMOUNT,
  C.SET_OFF_AMOUNT,  C.FIN_BOK_ACC_INVOICE_NO,  C.STATUS_ID
  FROM FIN_TRANSACTION_SET_OFF_ENTRY B,  FIN_TRANSACTION_ENTRY A,  FIN_TRANSACTION_SET_OFF C
WHERE A.FIN_TRANSACTION_ENTRY_ID = B.FIN_TRANSACTION_ENTRY_ID
AND B.FIN_TRN_SET_OFF_ENT_ID     = C.FIN_TRN_SET_OFF_ENT_ID
and   A.BOOKING_FORM_ID = 84 and a.status_id = 6 and TRANSACTION_STATUS_ID = 37
order by RECEIVED_DATE;


SELECT FIN_SET_OFF_APPR_LVL_ID, EMP.EMP_ID,el.username,el.password  , (EMP.FIRST_NAME || ' ' || EMP.LAST_NAME) AS EMP_NAME ,EMP.EMAIL AS EMP_EMAIL,FSOL.FIN_LEVEL_ID 
	,EMP.EMP_ID 
FROM FIN_SET_OFF_APPROVAL_LEVEL FSOAL 
INNER JOIN FIN_EMP_SET_OFF_LEVEL_MAPPING FESOLM ON (FESOLM.FIN_SET_OFF_LEVEL_ID=FSOAL.FIN_SET_OFF_LEVEL_ID) 
INNER JOIN EMPLOYEE EMP ON FESOLM.EMP_ID = EMP.EMP_ID
inner join EMPLOYEE_LOGIN el on el.EMPLOYEE_ID = EMP.EMP_ID
INNER JOIN FIN_SET_OFF_LEVEL FSOL ON (FESOLM.FIN_SET_OFF_LEVEL_ID=FSOL.FIN_SET_OFF_LEVEL_ID) 
WHERE FESOLM.STATUS_ID = 6 AND FSOL.STATUS_ID =6
AND FSOL.site_id = 111 AND  FSOL.TYPE = 34 order by FSOL.FIN_SET_OFF_LEVEL_ID;


select * from  TICKET   where BOOKING_ID = :oldfaltBookId;
select *  from MESSENGER   where SEND_TYPE = 4 and SEND_TO = :oldfaltBookId;
select * from MESSENGER_CONVERSATION    where CREATED_BY_TYPE =  4 and CREATED_BY_ID =  :oldfaltBookId;
SELECT * from  MESSENGER_CONVERS_VIEW_STATUS   where RECIPIENT_TYPE = 4 and RECIPIENT_ID = :oldfaltBookId ;
select * from APP_REGISTRATION where cust_id = :cust_id;
select REGISTER_DATE from APP_REGISTRATION where cust_id = :cust_id and APP_REG_ID in (select min(APP_REG_ID) from APP_REGISTRATION where cust_id = :cust_id);


update TICKET set STATUS = 6,BOOKING_ID = :newFaltBookId where BOOKING_ID = :oldfaltBookId;
update MESSENGER set SEND_TO = :newFaltBookId where SEND_TYPE = 4 and SEND_TO = :oldfaltBookId;
update MESSENGER_CONVERSATION set  CREATED_BY_ID   =:newFaltBookId  where CREATED_BY_TYPE =  4 and CREATED_BY_ID =  :oldfaltBookId;
update MESSENGER_CONVERS_VIEW_STATUS set RECIPIENT_ID = :newFaltBookId  where RECIPIENT_TYPE = 4 and RECIPIENT_ID = :oldfaltBookId ;
update APP_REGISTRATION set  REGISTER_DATE  = (select REGISTER_DATE from APP_REGISTRATION where cust_id = 946 and APP_REG_ID in (select min(APP_REG_ID) from APP_REGISTRATION where cust_id = 946))
WHERE  cust_id = 946 AND STATUS_ID = 6;

UPDATE APP_REGISTRATION 
SET UPDATED_DATE=SYSDATE,STATUS_ID=6
WHERE CUST_ID = :CUST_ID 
AND APP_REG_ID =(SELECT MIN(APP_REG_ID) FROM APP_REGISTRATION WHERE CUST_ID = :CUST_ID);
use this query for updating app registration table records. Pavan said...


STATUS_UPDATED_BY - cust id 
STATUS_UPDATE_TYPE - 7
  //SELECT DISTINCT EMSMM.MENU_SUB_MENU_MAPPING_ID, concat('INSERT INTO EMPLOYEE_SUBMENU_SITE_MAPPING(SUBMENU_SITE_MAPPING_ID, MENU_SUB_MENU_MAPPING_ID, SITE_ID, CREATED_DATE, STATUS_ID) VALUES (',''), '1497', concat(','||EMSMM.MENU_SUB_MENU_MAPPING_ID,', 126,SYSTIMESTAMP, 6);'),  EMSMM.MODULE_ID, EMSMM.SUBMODULE_ID 
  //SELECT DISTINCT EMSMM.MENU_SUB_MENU_MAPPING_ID, concat('INSERT INTO EMPLOYEE_SUBMENU_SITE_MAPPING(SUBMENU_SITE_MAPPING_ID, MENU_SUB_MENU_MAPPING_ID, SITE_ID, CREATED_DATE, STATUS_ID) VALUES', '(1497,'||EMSMM.MENU_SUB_MENU_MAPPING_ID||', 126,SYSTIMESTAMP, 6);'),  EMSMM.MODULE_ID, EMSMM.SUBMODULE_ID 
  SELECT DISTINCT EMSMM.MENU_SUB_MENU_MAPPING_ID, EMSMM.MODULE_ID, EMSMM.SUBMODULE_ID FROM EMPLOYEE_MENU_SUBMENU_MAPPING EMSMM
JOIN EMPLOYEE_ROLE_MENU_GROUPING ERMG 
ON(ERMG.MENU_MAPPING_ID=EMSMM.MENU_SUB_MENU_MAPPING_ID)
JOIN EMPLOYEE_SUBMENU_SITE_MAPPING ESMSM
ON(ESMSM.MENU_SUB_MENU_MAPPING_ID=EMSMM.MENU_SUB_MENU_MAPPING_ID)
JOIN EMPLOYEE_DEPARTMENT_MAPING EDM
ON(EDM.DEPARTMENT_ROLE_MAPPING_ID=ERMG.DEPARTMENT_ROLE_MAPPING_ID)
JOIN EMPLOYEE EMP ON(EMP.EMP_ID=EDM.EMPLOYEE_ID)
JOIN DEPARTMENT_ROLE_MAPPING DRM 
ON(DRM.DEPARTMENT_ROLE_MAPPING_ID=EDM.DEPARTMENT_ROLE_MAPPING_ID)
JOIN DEPARTMENT DPT ON(DPT.DEPT_ID=DRM.DEPT_ID)
JOIN EMPLOYEE_ROLES ERL ON(ERL.ROLE_ID=DRM.ROLE_ID)
JOIN EMPLOYEE_LOGIN ELG ON(ELG.EMPLOYEE_ID=EMP.EMP_ID)
WHERE EMSMM.STATUS_ID=:STATUS_ID AND ERMG.STATUS_ID=:STATUS_ID
AND ESMSM.STATUS_ID=:STATUS_ID AND EMP.EMP_ID=:EMP_ID
AND EDM.STATUS=:STATUS_ID ORDER BY MENU_SUB_MENU_MAPPING_ID,MODULE_ID, SUBMODULE_ID
				   
*/
	
	/**
	 * @author @NIKET CH@V@N
	 * @date 18-02-2020
	 * @time 6:30PM
	 * @param query Pass SQL query
	 * @param pojo pass your pojo Object which contains the query parameter values
	 * @param pK_ID pass primary key column name to get generated primary key
	 * @return primary key of inserted record
	 */
 	private Long commonMethodToInsertData(String query, Object pojo, String pK_ID) {
 		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
 		nmdPJdbcTemplate.update(query,new BeanPropertySqlParameterSource(pojo), keyHolder,new String[] {pK_ID});
		//log.info("**** The primarykey generated for this record is *****" + keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	}
	
 	/**
 	 * @author @NIKET CH@V@N
 	 * @date 18-02-2020
 	 * @time 6:30PM
	 * @param query Pass SQL query
	 * @param namedParameters pass namedParameters object
	 * @param pK_ID  pass primary key column name to get generated primary key
	 * @return primary key of inserted record
	 */
	private Long commonMethodToInsertData(String query, MapSqlParameterSource namedParameters, String pK_ID) {
 		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
 		nmdPJdbcTemplate.update(query,namedParameters, keyHolder,new String[] {pK_ID});
		//log.info("**** The primarykey generated for this record is *****" + keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	}
	
	private int commonMethodToUpdateData(String query, Object pojo ) {
		int result = nmdPJdbcTemplate.update(query,new BeanPropertySqlParameterSource(pojo));
		//log.info("EmployeeFinancialServiceDaoImpl.commonMethodToUpdateData(query,pojo) query result "+result);
 		return result;
	}
	
	private int commonMethodToUpdateData(String query, MapSqlParameterSource namedParameters ) {
		int result = nmdPJdbcTemplate.update(query,namedParameters);
		//log.info("EmployeeFinancialServiceDaoImpl.commonMethodToUpdateData(query,namedParameters) query result "+result+" "+namedParameters.getValues());
 		return result;
	}
	
	/**
	 * @param query Pass SQL query
	 * @param namedParameters pass namedParameters object
	 * @param clazz pass your class to set the data
	 * @return list of collection object contains u requested class object
	 */
	private <T> List<T> getData(String query, MapSqlParameterSource namedParameters, Class<T> clazz) {
		log.info(namedParameters.getValues());
		List<List<T>> list = nmdPJdbcTemplate.query(query.toString(), namedParameters,new ExtractDataFromResultSet<T>(clazz));
		if (list.isEmpty()) {
			list.add(new ArrayList<T>());
		}
		log.info(" getData namedParameters Size of list "+list.get(0).size());
		return list.get(0);
	}
	
	private <T> List<T> getData(String query, Object pojo, Class<T> clazz) {
		List<List<T>> list = nmdPJdbcTemplate.query(query.toString(),new BeanPropertySqlParameterSource(pojo),new ExtractDataFromResultSet<T>(clazz));

		if (list.isEmpty()) {
			list.add(new ArrayList<T>());
		}
		log.info(" getData POJO Size of list "+list.get(0).size());
		return list.get(0);
	}
 	
	/**
	 * @description this method will return all milestone alias names using site id
	 * mile stone is related to site, so getting all details using SITE_ID
	 */
	@Override
	public List<FinancialMileStoneClassifidesPojo> getAllMileStoneAliasNameAssociatedWithSite(EmployeeFinancialServiceInfo employeeFinancialInfo, Status status, String condition) {
		//log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.getAllMileStoneAliasNameAssociatedWithSite() ***** ");
		String query = new StringBuffer(SqlQuery.QRY_TO_GET_MILESTONE_ALIAS_NAMES1)
				.append(condition.equalsIgnoreCase("LoadById")?" AND FMC.FIN_MILESTONE_CLASSIFIDES_ID = :FIN_MILESTONE_CLASSIFIDES_ID ":"")
				.toString();
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("SITE_ID", employeeFinancialInfo.getSiteId(),Types.BIGINT);
		namedParameters.addValue("STATUS_ID", status.getStatus(),Types.BIGINT);
		//if condition is load by id
		if(condition.equalsIgnoreCase("LoadById")) {
			namedParameters.addValue("FIN_MILESTONE_CLASSIFIDES_ID", employeeFinancialInfo.getFinMilestoneClassifidesId(),Types.BIGINT);
		}
		
		
		List<FinancialMileStoneClassifidesPojo> listOfFinancialServicePojo =  (List<FinancialMileStoneClassifidesPojo>) getData(query, namedParameters, FinancialMileStoneClassifidesPojo.class);
		// here we have nested list object so we want only index zero object, as all data coming in first index of list object
		return listOfFinancialServicePojo;
	}
	 
	@Override//not i
	public void getMileStoneDetailsForTDS(EmployeeFinancialServiceInfo employeeFinancialInfo, Status active) {
		//log.info(" ***** EmployeeFinancialServiceDaoImpl.getMileStoneDetailsForTDS() ***** ");
		
	}
	
	/**
	 *Method used in multiple service impl class
	 */
	@Override
	public List<FinancialProjectMileStonePojo> getLastMilestoneDetails(EmployeeFinancialServiceInfo employeeFinancialInfo, Status active,
			String condition) {
		StringBuilder query = null;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		if(condition.equals("generateDemandNote")) {//taking MS data using alias name record id
				query = new StringBuilder("SELECT ").append(SqlQuery.QRY_TO_GET_MILESTONE_DETAILS)
						.append(", INITCAP(ST.STATUS) AS STATUS_NAME")
						.append(" FROM FIN_PROJECT_MILESTONES FPM LEFT OUTER JOIN STATUS ST ON ST.STATUS_ID = FPM.PROJECT_MS_STATUS_ID,PERCENTAGES PERCENT WHERE PERCENT.PERCENTAGE_ID=FPM.PERCENTAGE_ID AND FPM.FIN_MILESTONE_CLASSIFIDES_ID = :FIN_MILESTONE_CLASSIFIDES_ID ")
						.append(" AND FPM.STATUS_ID IN (:STATUS_ID) ")
						.append(" AND FPM.MILE_STONE_NO  IN (SELECT MAX(MILE_STONE_NO) FROM FIN_PROJECT_MILESTONES FPM_IN WHERE FPM.FIN_MILESTONE_CLASSIFIDES_ID = FPM_IN.FIN_MILESTONE_CLASSIFIDES_ID AND FPM_IN.STATUS_ID IN (:STATUS_ID))");
				
				namedParameters.addValue("FIN_MILESTONE_CLASSIFIDES_ID",employeeFinancialInfo.getFinMilestoneClassifidesId(), Types.BIGINT);
				namedParameters.addValue("STATUS_ID",Arrays.asList(new Long[] { Status.ACTIVE.getStatus(), Status.RAISED.getStatus() }), Types.BIGINT);
		} else if(condition.equals("saveFinancialTransaction")) {//taking MS data using block id and site id
			query = new StringBuilder("SELECT ")
					.append(SqlQuery.QRY_TO_GET_MILESTONE_DETAILS)
					.append(" FROM FIN_PROJECT_MILESTONES FPM, ")
					.append(" PERCENTAGES PERCENT, ")
					.append(" FIN_MILESTONE_CLASSIFIDES FMC ,")
					.append(" FIN_MIL_CLS_MAPPING_BLOCKS FMCMB, BLOCK BLOK ");
					query.append(" WHERE PERCENT.PERCENTAGE_ID=FPM.PERCENTAGE_ID ")
					.append(" AND FMCMB.FIN_MILESTONE_CLASSIFIDES_ID = FPM.FIN_MILESTONE_CLASSIFIDES_ID ")
					.append(" AND FMC.FIN_MILESTONE_CLASSIFIDES_ID = FPM.FIN_MILESTONE_CLASSIFIDES_ID ")
					.append(" AND BLOK.BLOCK_ID=FMCMB.BLOCK_ID AND BLOK.BLOCK_ID IN (:BLOCK_ID) ")
					.append(" AND FPM.PROJECT_MS_STATUS_ID = :PROJECT_MS_STATUS_ID ")
					.append(" AND FMC.SITE_ID = :SITE_ID").append(" AND FMC.STATUS_ID IN (:STATUS_ID) ").append(" AND FPM.STATUS_ID IN (:STATUS_ID) ")
					.append(" AND FPM.MILE_STONE_NO  IN (SELECT MAX(MILE_STONE_NO) FROM FIN_PROJECT_MILESTONES FPM_IN WHERE FPM_IN.PROJECT_MS_STATUS_ID = :PROJECT_MS_STATUS_ID AND FPM.FIN_MILESTONE_CLASSIFIDES_ID = FPM_IN.FIN_MILESTONE_CLASSIFIDES_ID AND FPM_IN.STATUS_ID IN (:STATUS_ID))")
					.append(" ORDER BY FPM.PROJECT_MILESTONE_ID");
					//
					
			namedParameters.addValue("PROJECT_MS_STATUS_ID", Status.MS_COMPLETED.getStatus(), Types.BIGINT);
			namedParameters.addValue("BLOCK_ID",employeeFinancialInfo.getBlockIds(), Types.BIGINT);
			namedParameters.addValue("SITE_ID",employeeFinancialInfo.getSiteId(), Types.BIGINT);
			namedParameters.addValue("STATUS_ID",Arrays.asList(new Long[] { Status.ACTIVE.getStatus(), Status.RAISED.getStatus() }), Types.BIGINT);
		} else {
			throw new RuntimeException("Error while loading last milestone of project...!");
		}
		List<FinancialProjectMileStonePojo> listOfFinancialServicePojo = getData(query.toString(),namedParameters,FinancialProjectMileStonePojo.class);
		return listOfFinancialServicePojo;
	}
	
	/**
	 * @description this method will return all mile stone names associated with mile stone alias name id
	 */
	@Override
	public List<FinancialProjectMileStonePojo> getMileStoneNameAssociatedWithMilestoneClassifidesIdForDemandNote(EmployeeFinancialServiceInfo employeeFinancialInfo, Status status, String condition) {
		//log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.getMileStoneNameAssociatedWithMilestoneClassifidesIdForDemandNote() ***** ");
		StringBuilder query = null;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		if(Util.isNotEmptyObject(employeeFinancialInfo.getCondition()) &&  (employeeFinancialInfo.getCondition().equalsIgnoreCase("VIEW_DEMAND_NOTES") || employeeFinancialInfo.getCondition().equalsIgnoreCase("loadAllMileStone"))){
			query = new StringBuilder(SqlQuery.QRY_TO_GET_MILESTONE_NAMES_BY_SITE)
					.append(" WHERE FMC.SITE_ID=:SITE_ID ")
					.append(" AND FPM.STATUS_ID IN (:STATUS_ID) ");
			if(Util.isNotEmptyObject(employeeFinancialInfo.getBlockIds()))
			{
					query.append(" AND FMCMB.BLOCK_ID = :BLOCK_ID");
					namedParameters.addValue("BLOCK_ID", employeeFinancialInfo.getBlockIds().get(0));
			}
					query.append(" ORDER BY FPM.PROJECT_MILESTONE_ID ");
			
			namedParameters.addValue("SITE_ID", employeeFinancialInfo.getSiteIds().get(0), Types.BIGINT);
			namedParameters.addValue("STATUS_ID",Arrays.asList(new Long[] {Status.ACTIVE.getStatus(),Status.RAISED.getStatus()}),Types.BIGINT);
		} else {
			query = new StringBuilder("SELECT ").append(SqlQuery.QRY_TO_GET_MILESTONE_DETAILS).append(", INITCAP(ST.STATUS) AS STATUS_NAME").
					append(" FROM FIN_PROJECT_MILESTONES FPM LEFT OUTER JOIN STATUS ST ON ST.STATUS_ID = FPM.PROJECT_MS_STATUS_ID,PERCENTAGES PERCENT WHERE PERCENT.PERCENTAGE_ID=FPM.PERCENTAGE_ID AND FPM.FIN_MILESTONE_CLASSIFIDES_ID = :FIN_MILESTONE_CLASSIFIDES_ID ")
					// ON ST.STATUS_ID = FPM.PROJECT_MS_STATUS_ID
					.append(" AND FPM.STATUS_ID IN (:STATUS_ID) ");
			if(employeeFinancialInfo.isThisUplaodedData()) {
				if(employeeFinancialInfo.getDataUploadCondition()!=null && employeeFinancialInfo.getDataUploadCondition().contains("LoadALL_MS")) {
					query.append(Util.isNotEmptyObject(employeeFinancialInfo.getProjectMileStoneIds())?" AND FPM.PROJECT_MILESTONE_ID < :PROJECT_MILESTONE_ID ":"");	
				} else if(employeeFinancialInfo.getDataUploadCondition()!=null && employeeFinancialInfo.getDataUploadCondition().contains("LoadCurrentMSDetails")) {
					query.append(Util.isNotEmptyObject(employeeFinancialInfo.getProjectMileStoneIds())?" AND FPM.PROJECT_MILESTONE_ID = :PROJECT_MILESTONE_ID ":"");	
				} else {
					//required this condition to get all the previous ms details, so we can take some of milestone completed percentage
					query.append(Util.isNotEmptyObject(employeeFinancialInfo.getProjectMileStoneIds())?" AND FPM.PROJECT_MILESTONE_ID < :PROJECT_MILESTONE_ID ":"");
				}
			} else {
				query.append(Util.isNotEmptyObject(employeeFinancialInfo.getProjectMileStoneIds())?" AND FPM.PROJECT_MILESTONE_ID < :PROJECT_MILESTONE_ID ":"");
			}
			
			query.append(condition.equalsIgnoreCase("LoadById") ? "" : "").append(" ORDER BY FPM.MILE_STONE_NO ").toString();
			if (Util.isNotEmptyObject(employeeFinancialInfo.getProjectMileStoneIds())) {
				namedParameters.addValue("PROJECT_MILESTONE_ID", employeeFinancialInfo.getProjectMileStoneIds().get(0),	Types.BIGINT);
			}
			namedParameters.addValue("FIN_MILESTONE_CLASSIFIDES_ID",employeeFinancialInfo.getFinMilestoneClassifidesId(), Types.BIGINT);
			namedParameters.addValue("STATUS_ID",Arrays.asList(new Long[] { Status.ACTIVE.getStatus(), Status.RAISED.getStatus() }), Types.BIGINT);
		}
		
		List<FinancialProjectMileStonePojo>  listOfFinancialServicePojo = getData(query.toString(),namedParameters,FinancialProjectMileStonePojo.class);
		return listOfFinancialServicePojo;
	}
	
	@Override
	public List<FinancialProjectMileStonePojo> getMilestoneDetails(EmployeeFinancialServiceInfo employeeFinancialInfo) throws Exception {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.getMilestoneDetails() *****");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder query = new StringBuilder("SELECT ")
				.append(SqlQuery.QRY_TO_GET_MILESTONE_DETAILS)
				.append(" FROM FIN_PROJECT_MILESTONES FPM, ")
				.append(" PERCENTAGES PERCENT, ")
				.append(" FIN_MILESTONE_CLASSIFIDES FMC ,")
				.append(" FIN_MIL_CLS_MAPPING_BLOCKS FMCMB, BLOCK BLOK ");
				query.append(" WHERE PERCENT.PERCENTAGE_ID=FPM.PERCENTAGE_ID ")
				.append(" AND FMCMB.FIN_MILESTONE_CLASSIFIDES_ID = FPM.FIN_MILESTONE_CLASSIFIDES_ID ")
				.append(" AND FMC.FIN_MILESTONE_CLASSIFIDES_ID = FPM.FIN_MILESTONE_CLASSIFIDES_ID ")
				.append(" AND BLOK.BLOCK_ID=FMCMB.BLOCK_ID AND BLOK.BLOCK_ID IN (:BLOCK_ID) ");
				if(employeeFinancialInfo.getCondition().equals(FinEnum.LOAD_BY_MILESTONE_NAME.getName())) {
					query.append(" AND FPM.MILESTONE_NAME = :MILESTONE_NAME ");
				} else if(employeeFinancialInfo.getCondition().equals(FinEnum.LOAD_BY_MILESTONE.getName()) && Util.isNotEmptyObject(employeeFinancialInfo.getFinancialProjectMileStoneRequests())){
					query.append(" AND FPM.MILE_STONE_NO = :MILE_STONE_NO ");
					namedParameters.addValue("MILE_STONE_NO", employeeFinancialInfo.getFinancialProjectMileStoneRequests().get(0).getMileStoneNo(),Types.BIGINT);
				} else {
					
				}
				query.append(" AND FMC.SITE_ID IN (:SITE_ID)");
				query.append(" AND FPM.STATUS_ID IN (:STATUS_ID)");
				query.append(" AND FMC.STATUS_ID IN (:STATUS_ID)");
		if(Util.isEmptyObject(employeeFinancialInfo.getSiteId())) {
			throw new EmployeeFinancialServiceException("Site id missing while inserting Transaction");
		}
		namedParameters.addValue("SITE_ID",employeeFinancialInfo.getSiteId(),Types.BIGINT);
		namedParameters.addValue("STATUS_ID",Arrays.asList(new Long[] {Status.ACTIVE.getStatus(),Status.RAISED.getStatus()}),Types.BIGINT);
		namedParameters.addValue("BLOCK_ID", employeeFinancialInfo.getBlockIds().get(0),Types.BIGINT);
		namedParameters.addValue("MILESTONE_NAME", employeeFinancialInfo.getFinancialProjectMileStoneRequests().get(0).getMilestoneName(),Types.VARCHAR);
		
		List<FinancialProjectMileStonePojo>  listOfFinancialServicePojo = (List<FinancialProjectMileStonePojo>) getData(query.toString(),namedParameters,FinancialProjectMileStonePojo.class);
		return listOfFinancialServicePojo;
	}
	
	@Override
	public List<FinancialProjectMileStonePojo> getGeneratedMilestoneDetailsOfFlat(EmployeeFinancialServiceInfo employeeFinancialInfo, Long condition) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.getGeneratedMilestoneDetailsOfFlat() *****");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder query = new StringBuilder("SELECT ")
				.append(SqlQuery.QRY_TO_GET_DATA_FROM_FIN_BOOKING_FORM_MILESTONES).append(",FBFM.STATUS_ID,")
				.append(SqlQuery.QRY_TO_GET_FIN_PROJECT_MILESTONES)
				.append(" ,PER.PERCENTAGE AS MILE_STONE_PERCENTAGE ")
				.append(" FROM FIN_BOOKING_FORM_MILESTONES FBFM ")
				.append(" JOIN FIN_PROJECT_MILESTONES FPM ON FPM.PROJECT_MILESTONE_ID = FBFM.PROJECT_MILESTONE_ID")
				.append(" LEFT OUTER JOIN PERCENTAGES PER ON FPM.PERCENTAGE_ID =  PER.PERCENTAGE_ID")
				.append(" WHERE FBFM.BOOKING_FORM_ID = :BOOKING_FORM_ID ")
				.append("AND FBFM.STATUS_ID = :STATUS_ID ")
				.append("ORDER BY FPM.PROJECT_MILESTONE_ID,FBFM.FIN_BOOKING_FORM_MILESTONES_ID ");
		
		namedParameters.addValue("STATUS_ID",Arrays.asList(new Long[] {Status.ACTIVE.getStatus()}),Types.BIGINT);
		namedParameters.addValue("BOOKING_FORM_ID",employeeFinancialInfo.getBookingFormId() ,Types.BIGINT);
		
		List<FinancialProjectMileStonePojo>  financialProjectMileStonePojoList = (List<FinancialProjectMileStonePojo>) getData(query.toString(),namedParameters,FinancialProjectMileStonePojo.class);
		return financialProjectMileStonePojoList;
	}
	
	@Override
	public List<FinancialProjectMileStonePojo> getNextMilestoneDetails(FinancialProjectMileStonePojo financialProjectMileStonePojo) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.getNextMilestoneDetails() *****");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder query = new StringBuilder("SELECT ")
				.append(SqlQuery.QRY_TO_GET_FIN_PROJECT_MILESTONES)
				.append(",PERCENT.PERCENTAGE as MILE_STONE_PERCENTAGE")
				.append(" FROM FIN_PROJECT_MILESTONES FPM")
				.append(" JOIN PERCENTAGES PERCENT ON PERCENT.PERCENTAGE_ID = FPM.PERCENTAGE_ID ")
				.append(" WHERE FPM.FIN_MILESTONE_CLASSIFIDES_ID = :FIN_MILESTONE_CLASSIFIDES_ID ")
				.append("AND FPM.MILE_STONE_NO >= :MILE_STONE_NO ")
				.append("AND FPM.STATUS_ID IN (:STATUS_ID) ")
				.append("ORDER BY FPM.PROJECT_MILESTONE_ID ");
		
		namedParameters.addValue("STATUS_ID",Arrays.asList(new Long[] {Status.ACTIVE.getStatus(),Status.RAISED.getStatus()}),Types.BIGINT);
		namedParameters.addValue("FIN_MILESTONE_CLASSIFIDES_ID",financialProjectMileStonePojo.getFinMilestoneClassifidesId() ,Types.BIGINT);
		namedParameters.addValue("MILE_STONE_NO",(financialProjectMileStonePojo.getMileStoneNo()+1) ,Types.BIGINT);
		
		
		List<FinancialProjectMileStonePojo>  financialProjectMileStonePojoList = (List<FinancialProjectMileStonePojo>) getData(query.toString(),namedParameters,FinancialProjectMileStonePojo.class);
		return financialProjectMileStonePojoList;
	}
	
	@Override
	public List<FinancialMileStoneClassifideMappingBlocksPojo> getDemandNoteBlockDetails(EmployeeFinancialServiceInfo employeeFinancialDemandNoteInfo, Status active) {
		//log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.getDemandNoteBlockDetails() ***** ");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_MILESTONE_DETAILS_BLOCK_DTLS).append(" AND BLOK.BLOCK_ID=FMCMB.BLOCK_ID ORDER BY BLOK.BLOCK_ID").toString();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FIN_MILESTONE_CLASSIFIDES_ID", employeeFinancialDemandNoteInfo.getFinMilestoneClassifidesId(),Types.BIGINT);

		
		List<FinancialMileStoneClassifideMappingBlocksPojo> listOfFinancialServicePojo = (List<FinancialMileStoneClassifideMappingBlocksPojo>) getData(query, namedParameters, FinancialMileStoneClassifideMappingBlocksPojo.class);
		// here we have nested list object so we want only index zero object, as all data coming in first index of list object
		return listOfFinancialServicePojo;
	}
	
	@Override//no use
	public List<CustomerPropertyDetailsPojo> getFlatDetailsByBlockId(EmployeeFinancialServiceInfo mappingBlocksPojo,String condition,Set<Long> blockIds) {
		//log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.getFlatDetailsByBlockId() ***** ");
		//StringBuilder query1 = new StringBuilder(NotificationsSqlQuery.GET_ACTIVE_FLAT_WITH_BLOCK_ID);
		StringBuilder query = new StringBuilder(SqlQuery.QRY_TO_GET_CUSTOMER_PROPERTY_DETAILS)
				.append(" INNER JOIN FIN_MIL_CLS_MAPPING_BLOCKS FMCMB ON FMCMB.BLOCK_ID = BD.BLOCK_ID WHERE FMCMB.FIN_MILESTONE_CLASSIFIDES_ID = :FIN_MILESTONE_CLASSIFIDES_ID AND BD.SITE_ID = :SITE_ID");
				query.append(" AND FB.STATUS_ID=:STATUS_ID ");
		if (condition.equalsIgnoreCase("LoadById")) {
			query.append(" AND FMCMB.BLOCK_ID IN (:BLOCK_ID)");
		}

		query.append(" ORDER BY BD.BLOCK_ID,FL.FLOOR_ID,FB.FLAT_ID").toString();
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FIN_MILESTONE_CLASSIFIDES_ID", mappingBlocksPojo.getFinMilestoneClassifidesId(),Types.BIGINT);
		namedParameters.addValue("SITE_ID", mappingBlocksPojo.getSiteId(),Types.BIGINT);
		namedParameters.addValue("BLOCK_ID",blockIds);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("AR_STATUS_ID",Status.ACTIVE.getStatus(), Types.BIGINT);
		
		List<CustomerPropertyDetailsPojo> CustomerPropertyDetailsPojoLists = (List<CustomerPropertyDetailsPojo>) getData(query.toString(), namedParameters, CustomerPropertyDetailsPojo.class);
		return CustomerPropertyDetailsPojoLists ;
	}
	
	@Override
	public Long insertDataIntoFinProjDemandNote(FinancialProjectDemandNotePojo finProjDemandNotePojo) {
		//log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.insertDataIntoFinProjDemandNote() ***** ");
		String query = new StringBuilder(SqlQuery.QRY_TO_INSERT_FIN_PROJECT_DEMAND_NOTE).toString();
		Long pk = commonMethodToInsertData(query, finProjDemandNotePojo,"FIN_PRO_DEM_NOTE_ID" ); 
		return pk;
/*		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		nmdPJdbcTemplate.update(query,new BeanPropertySqlParameterSource(finProjDemandNotePojo), keyHolder,new String[] { "FIN_PRO_DEM_NOTE_ID" });
		//log.info("**** The primarykey generated for this record is *****" + keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
*/	}
	
	@Override
	public Long insertDataIntoFinMilDemandNoteMapping(FinMilDemNoteMappingPojo finMilDemNoteMappingPojo) {
		//log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.insertDataIntoFinMilDemandNoteMapping() ***** ");
		//GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		String query=SqlQuery.QRY_TO_INSERT_FIN_MIL_DEM_NOTE_MAPPING;
		Long pk = commonMethodToInsertData(query, finMilDemNoteMappingPojo,"FIN_MIL_DEM_DRF_MAP_ID" ); 
		return pk;
/*		nmdPJdbcTemplate.update(query,new BeanPropertySqlParameterSource(finMilDemNoteMappingPojo), keyHolder,new String[] { "FIN_MIL_DEM_DRF_MAP_ID" });
		//log.info("**** The primarykey generated for this record is *****" + keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
*/	}
	
	@Override
	public Long insertDataIntofinProjectDemandNoteStatistics(FinProjDemNoteStatisticsPojo destFinProjDemandNotePojo) {
		//log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.insertDataIntofinProjectDemandNoteStatistics() **** ");
		//GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		String query = SqlQuery.QRY_TO_INSERT_FIN_PROJ_DEM_NOTE_STATISTICS;
		Long pk = commonMethodToInsertData(query, destFinProjDemandNotePojo,"FIN_PROJ_DEM_DRAF_STAT_ID" ); 
		return pk;
/*		nmdPJdbcTemplate.update(query,new BeanPropertySqlParameterSource(destFinProjDemandNotePojo), keyHolder,new String[] { "FIN_PROJ_DEM_DRAF_STAT_ID" });
		//log.info("**** The primarykey generated for this record is *****" + keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
*/	}
	
	@Override 
	public Long getNoOfCompletedMileStone(CustomerPropertyDetailsInfo customerPropertyDetailsInfo) {
		//log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.getNoOfCompletedMileStone() ***** ");
		String query = SqlQuery.QRY_TO_GET_PK_FIN_BOOKING_FORM_DEMAND_NOTE;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		//namedParameters.addValue("BOOKING_FORM_ID", customerPropertyDetailsInfo.getFlatBookingId(),Types.BIGINT);
		return nmdPJdbcTemplate.queryForObject(query, namedParameters, Long.class);
	}
	
	@Override
	public Long getNoOfCompletedMileStoneForBooking(CustomerPropertyDetailsInfo customerPropertyDetailsInfo) {
		String query = SqlQuery.QRY_TO_GET_IN_BOOKING_FORM_DEMAND_NOTE;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("BOOKING_FORM_ID", customerPropertyDetailsInfo.getFlatBookingId(),Types.BIGINT);
		namedParameters.addValue("STATUS_ID",Status.ACTIVE.getStatus(),Types.BIGINT);
		namedParameters.addValue("DEMAND_NOTE_STATUS_ID", Status.REGENERATED_DEMAND_NOTE.getStatus(),Types.BIGINT);
		return nmdPJdbcTemplate.queryForObject(query, namedParameters, Long.class);
	}
	
	@Override
	public Long insertDataIntoFinBookingFormDemandNote(FinBookingFormDemandNotePojo pojo) {
		//GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		String query = SqlQuery.QRY_TO_INSERT_FIN_BOOKING_FORM_DEMAND_NOTE;
		Long pk = commonMethodToInsertData(query, pojo,"FIN_BOK_FOM_DMD_NOTE_ID" ); 
		return pk;
	}
	
	@Override
	public Long saveFinBookinFormDemandNoteDoc(FinBookingFormDemandNoteDocPojo pojo) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.saveFinBookinFormDemandNoteDoc() *****"+pojo);
		String query = SqlQuery.QRY_TO_INSERT_FIN_BOOKING_FORM_DEMAND_NOTE_DOC;
	 	Long pk = commonMethodToInsertData(query, pojo,"FIN_BOK_FRM_DMD_NTE_DOC_ID" ); 
		return pk;
	}

	@Override
	public Long getCountOfDemandNoteDoc(FinBookingFormDemandNoteDocPojo demandNoteDocPojo) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.getCountOfDemandNoteDoc() *****"+demandNoteDocPojo);
		long docCount = 0;
		String query = SqlQuery.QRY_TO_COUNT_FIN_BOOKING_FORM_DEMAND_NOTE_DOC;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FIN_BOK_FOM_DMD_NOTE_ID", demandNoteDocPojo.getFinBookingFormDemandNoteId(),Types.BIGINT);
		namedParameters.addValue("STATUS_ID",Status.ACTIVE.getStatus(),Types.BIGINT);
		try {
			docCount = nmdPJdbcTemplate.queryForObject(query, namedParameters, Long.class);
		}catch(Exception e) {
			//log.info(e.getMessage());
		}
		return docCount;
	}

	@Override
	public List<FinBookingFormDemandNoteDocPojo> loadDemandNotePDFFile(EmployeeFinancialServiceInfo serviceInfo) {
		String query = SqlQuery.QRY_TO_LOAD_FIN_BOOKING_FORM_DEMAND_NOTE_DOC;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FIN_BOK_FOM_DMD_NOTE_ID", serviceInfo.getFinBookingFormDemandNoteId(),Types.BIGINT);
		//namedParameters.addValue("STATUS_ID", "");
		
		List<FinBookingFormDemandNoteDocPojo> demandNotedocDetails = (List<FinBookingFormDemandNoteDocPojo>) getData(query.toString(), namedParameters, FinBookingFormDemandNoteDocPojo.class);
		return demandNotedocDetails ;
	}
	
	@Override
	public int updateDataIntoFinBookingFormDemandNote(FinBookingFormDemandNotePojo pojo) {
		log.info("***** The Control is inside the updateDataIntoFinBookingFormDemandNote in EmployeeFinancialServiceDaoImpl ***** ");
		String query = "";
		if("Interest_Letter".equalsIgnoreCase(pojo.getActionUrl())) {
			query = SqlQuery.QRY_TO_UPDATE_FIN_BOOKING_FORM_DEMAND_NOTE_FOR_INTEREST_LETTER;
		}else {
			query = SqlQuery.QRY_TO_UPDATE_FIN_BOOKING_FORM_DEMAND_NOTE;
		}
		return nmdPJdbcTemplate.update(query,new BeanPropertySqlParameterSource(pojo) );
	}
	
	@Override
	public int updateMilestoneStatus(FinancialProjectMileStoneRequest mileStoneRequest) {
		log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.updateMilestoneStatus() *****");
		String query = FinancialQuerys.QRY_TO_UPDATE_FIN_PROJECT_MILESTONES_STATUS;
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("PROJECT_MS_STATUS_ID", mileStoneRequest.getStatusId(),Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(),Types.BIGINT);
		namedParameters.addValue("MODIFIED_BY", mileStoneRequest.getCreatedBy(),Types.BIGINT);
		namedParameters.addValue("PROJECT_MILESTONE_ID", mileStoneRequest.getProjectMilestoneId(),Types.BIGINT);
 		System.out.println(namedParameters.getValues());
		int result = commonMethodToUpdateData(query, namedParameters);
		return result;
	}
	
	@Override
	public int updateProjectMilestoneStatus(FinancialProjectMileStoneInfo finProjDemandNoteInfo, Long status) {
		//log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.updateProjectMilestoneStatus()");
		String query = SqlQuery.QRY_TO_UPDATE_FIN_PROJECT_MILESTONES;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("PROJECT_MS_STATUS_ID", status,Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(),Types.BIGINT);
		namedParameters.addValue("MILESTONE_DATE", finProjDemandNoteInfo.getMilestoneDate(),Types.TIMESTAMP);
		
		namedParameters.addValue("MASTER_DMD_NOTE_DATE", finProjDemandNoteInfo.getDemandNoteDate(),Types.TIMESTAMP);
		namedParameters.addValue("MASTER_DMD_DUE_DATE", finProjDemandNoteInfo.getMileStoneDueDate(),Types.TIMESTAMP);
		
		namedParameters.addValue("MODIFIED_BY", finProjDemandNoteInfo.getCreatedBy(),Types.BIGINT);
		namedParameters.addValue("PROJECT_MILESTONE_ID", finProjDemandNoteInfo.getProjectMilestoneId(),Types.BIGINT);
 		int result = commonMethodToUpdateData(query, namedParameters);
		return result;
	}
	
	@Override
	public Long insertDataIntoFinBookingFormMilestones(FinBookingFormMilestonesPojo pojo) {
		//log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.insertDataIntoFinBookingFormMilestones() ***** ");
		//GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		String query = SqlQuery.QRY_TO_INSERT_FIN_BOOKING_FORM_MILESTONES;
		Long pk = commonMethodToInsertData(query, pojo,"FIN_BOOKING_FORM_MILESTONES_ID" ); 
		return pk;
	}
	
	@Override
	public int updateDataIntoFinBookingFormMilestones(FinBookingFormMilestonesPojo pojo) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.updateDataIntoFinBookingFormMilestones() *****");
		String query = SqlQuery.QRY_TO_UPDATE_FIN_BOOKING_FORM_MILESTONES;
		int result = nmdPJdbcTemplate.update(query,new BeanPropertySqlParameterSource(pojo) );
		return result;
	}
	
	@Override
	public Long saveDataIntoBookingFormMilestoneTax(FinBookingFormMilestoneTaxPojo bookingFormMilestoneTaxPojo) {
		String query = SqlQuery.QRY_TO_INSERT_FIN_BOOKING_FORM_MILESTONE_TAX;
		Long pk = commonMethodToInsertData(query, bookingFormMilestoneTaxPojo,"FIN_BOK_FRM_MST_TAX_ID" ); 
		return pk;
	}
		
	@Override
	public List<FinSchemeTaxMappingPojo> getFlatBookDetailsSchemeTaxDetails(CustomerPropertyDetailsInfo customerPropertyDetailsInfo, 
			FinancialProjectMileStoneInfo finProjDemandNoteInfo) throws Exception {
		boolean flag = (isNotEmptyObject(customerPropertyDetailsInfo.getCondition())
				&& customerPropertyDetailsInfo.getCondition().equalsIgnoreCase("GetGSTPercentage")) ? true : false;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuffer query = new StringBuffer("SELECT " + (flag ? " DISTINCT FBFMS.FIN_BOK_FRM_MST_SCH_TAX_MAP_ID, " : "FBCM.FLT_BOK_SCHM_MAP_ID,"))
				.append(SqlQuery.QRY_TO_GET_SCHEME_TAX_MAPPING_ID)
				.append(" ")
				.append(" FROM FIN_SCHEME_TAX_MAPPING FSTM,")
				.append("FIN_SCHEME FS,")
				.append("FIN_TAX FT,")
				.append("FLAT_BOK_SCHM_MAPPING FBCM,")
				.append("FLAT_BOOKING FB,")
				.append("PERCENTAGES PERCENTAGE");
		if (flag) {
			query.append(" ,FIN_BOOKING_FORM_MILESTONE_TAX FBFMT LEFT OUTER JOIN FIN_BOK_FRM_MST_SCH_TAX_MAP FBFMS");
			query.append(" ON FBFMT.FIN_BOK_FRM_MST_TAX_ID = FBFMS.FIN_BOK_FRM_MST_TAX_ID");
		}

		query.append(" WHERE FS.FIN_SCHEME_ID = FSTM.FIN_SCHEME_ID")
				.append(" AND PERCENTAGE.PERCENTAGE_ID = FSTM.PERCENTAGE_ID")
				.append(" AND FT.FIN_TAX_ID = FSTM.FIN_TAX_ID").append(" AND FB.FLAT_BOOK_ID = FBCM.FLAT_BOOK_ID")
				.append(" AND FSTM.FIN_SCHE_TAX_MAP_ID = FBCM.FIN_SCHE_TAX_MAP_ID")
				.append(" AND PERCENTAGE.PERCENTAGE_ID = FSTM.PERCENTAGE_ID")
				.append(" AND FBCM.FLAT_BOOK_ID=:FLAT_BOOK_ID")
				.append(" AND FSTM.STATUS_ID=:STATUS_ID  AND FS.STATUS_ID=:STATUS_ID");
		
		if (!flag) {
			query.append(" AND FBCM.STATUS_ID = :STATUS_ID");
			query.append(" AND TRUNC(:MILE_STONE_DATE) BETWEEN TRUNC(FSTM.START_DATE) AND TRUNC(FSTM.END_DATE)");
			if(Util.isNotEmptyObject(finProjDemandNoteInfo.getMilestoneDate())) {
				namedParameters.addValue("MILE_STONE_DATE", TimeUtil.removeTimePartFromTimeStamp1(finProjDemandNoteInfo.getMilestoneDate()),Types.DATE);	
			} else {
				finProjDemandNoteInfo.setMilestoneDate(new Timestamp(new Date().getTime()));
				namedParameters.addValue("MILE_STONE_DATE", TimeUtil.removeTimePartFromTimeStamp1(new Timestamp(new Date().getTime())),Types.DATE);
			}
			
		}

		if (flag) {
			query.append(" AND FBFMS.FIN_SCHE_TAX_MAP_ID = FBCM.FIN_SCHE_TAX_MAP_ID ");
			query.append(" AND FT.ALIAS_NAME = :ALIASNAME");
			query.append(" AND FBFMT.FIN_BOOKING_FORM_MILESTONES_ID = :FIN_BOOKING_FORM_MILESTONES_ID ");
			namedParameters.addValue("ALIASNAME", customerPropertyDetailsInfo.getType(), Types.BIGINT);
			namedParameters.addValue("FIN_BOOKING_FORM_MILESTONES_ID", customerPropertyDetailsInfo.getTypeId(),Types.BIGINT);
		}
			
		namedParameters.addValue("FLAT_BOOK_ID", customerPropertyDetailsInfo.getFlatBookingId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", customerPropertyDetailsInfo.getStatusId(), Types.BIGINT);
		List<FinSchemeTaxMappingPojo> demandNoteSchemeTaxDetailsList = null;
		try {		
			demandNoteSchemeTaxDetailsList = (List<FinSchemeTaxMappingPojo>) getData(query.toString(), namedParameters, FinSchemeTaxMappingPojo.class);
		}catch(Exception e ) {
			e.printStackTrace(); demandNoteSchemeTaxDetailsList = new ArrayList<>();
		}
		return demandNoteSchemeTaxDetailsList ;
	}
	
	@Override
	public List<FinBokFrmDemNteSchTaxMapPojo> getMileStoneTaxDetails1(CustomerPropertyDetailsInfo customerPropertyDetailsInfo) {
		//log.info("***** The Control is inside the  EmployeeFinancialServiceDaoImpl.getFlatBookDetailsScheme() ***** ");
		//boolean flag = (isNotEmptyObject(customerPropertyDetailsInfo.getCondition()) && customerPropertyDetailsInfo.getCondition().equalsIgnoreCase("GetGSTPercentage")) ? true : false;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuffer query = new StringBuffer("SELECT DISTINCT FBFMS.FIN_BOK_FRM_MST_SCH_TAX_MAP_ID")
				.append(" ,FBFMS.FIN_BOK_FRM_MST_TAX_ID,FBFMS.FIN_SCHE_TAX_MAP_ID,FBFMS.AMOUNT")
				.append(" ,FT.ALIAS_NAME AS TAX_TYPE_ID,").append("FSTM.SCHEME_TYPE").append(",FS.NAME AS FIN_SCHEME_NAME").append(",FS.FIN_SCHEME_ID")
				.append(" ,(SELECT NAME FROM METADATA_TYPES WHERE METADATA_TYPES_ID = FT.ALIAS_NAME) AS TAX_TYPE ")
				.append(" ,FSTM.PERCENTAGE_ID, PERCENTAGE.PERCENTAGE AS PERCENTAGE_VALUE ")
				.append(" FROM FIN_SCHEME_TAX_MAPPING FSTM, FIN_SCHEME FS,")
				.append(" FIN_TAX FT, FLAT_BOK_SCHM_MAPPING FBCM,")
				.append(" FLAT_BOOKING FB, PERCENTAGES PERCENTAGE,")
				.append(" FIN_BOOKING_FORM_MILESTONE_TAX FBFMT LEFT OUTER JOIN FIN_BOK_FRM_MST_SCH_TAX_MAP FBFMS")
				.append(" ON FBFMT.FIN_BOK_FRM_MST_TAX_ID = FBFMS.FIN_BOK_FRM_MST_TAX_ID")
				.append(" WHERE FS.FIN_SCHEME_ID = FSTM.FIN_SCHEME_ID")
				.append(" AND PERCENTAGE.PERCENTAGE_ID = FSTM.PERCENTAGE_ID")
				.append(" AND FT.FIN_TAX_ID = FSTM.FIN_TAX_ID  AND FB.FLAT_BOOK_ID = FBCM.FLAT_BOOK_ID")
				.append(" AND FSTM.FIN_SCHE_TAX_MAP_ID = FBCM.FIN_SCHE_TAX_MAP_ID")
				.append(" AND PERCENTAGE.PERCENTAGE_ID = FSTM.PERCENTAGE_ID")
				.append(" AND FBCM.FLAT_BOOK_ID=:FLAT_BOOK_ID")
				.append(" AND FSTM.STATUS_ID IN (:STATUS_ID) AND FS.STATUS_ID=:STATUS_ID");
			//query.append(" AND FBCM.STATUS_ID = :STATUS_ID");
			//query.append(" AND FBCM.STATUS_ID = :STATUS_ID");
			query.append(" AND FBFMS.FIN_SCHE_TAX_MAP_ID = FBCM.FIN_SCHE_TAX_MAP_ID ");
			//query.append(" AND FT.ALIAS_NAME = :ALIASNAME");
			query.append(" AND FBFMT.FIN_BOOKING_FORM_MILESTONES_ID = :FIN_BOOKING_FORM_MILESTONES_ID ");
			
		//namedParameters.addValue("ALIASNAME", customerPropertyDetailsInfo.getType(), Types.BIGINT);
		namedParameters.addValue("FIN_BOOKING_FORM_MILESTONES_ID", customerPropertyDetailsInfo.getTypeId(),Types.BIGINT);
		namedParameters.addValue("FLAT_BOOK_ID", customerPropertyDetailsInfo.getFlatBookingId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", customerPropertyDetailsInfo.getStatusId(), Types.BIGINT);
		//namedParameters.addValue("SCHEME_STATUS_ID", Arrays.asList(Status.ACTIVE.getStatus(),Status.INACTIVE.getStatus()), Types.BIGINT);
		
		List<FinBokFrmDemNteSchTaxMapPojo> demandNoteSchemeTaxDetailsList = (List<FinBokFrmDemNteSchTaxMapPojo>) getData(query.toString(), namedParameters, FinBokFrmDemNteSchTaxMapPojo.class);
		return demandNoteSchemeTaxDetailsList ;
	}
	
	@Override
	public List<FinBokFrmDemNteSchTaxMapPojo> getSumOfMileStoneTaxDetails(FinSchemeTaxMappingPojo schemeDetails, FinancialProjectMileStonePojo mileStonePojo) {
		//log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.getSumOfMileStoneTaxDetails() ***** ");
		StringBuilder query = new StringBuilder("").append(SqlQuery.QRY_TO_GET_SUM_BOK_FRM_MST_SCH_TAX_MAP)
				  .append(" AND FBFM.FIN_BOOKING_FORM_MILESTONES_ID = :FIN_BOOKING_FORM_MILESTONES_ID")
				  .append(" AND FBFMS.FIN_BOK_FRM_MST_SCH_TAX_MAP_ID IN (:FIN_BOK_FRM_MST_SCH_TAX_MAP_ID)");
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FIN_BOOKING_FORM_MILESTONES_ID", mileStonePojo.getTypeId(),Types.BIGINT);
		namedParameters.addValue("FIN_BOK_FRM_MST_SCH_TAX_MAP_ID", schemeDetails.getFinBokFrmMstSchTaxMapId(),Types.BIGINT);
		
		
		List<FinBokFrmDemNteSchTaxMapPojo> demandNoteSchemeTaxDetailsList = (List<FinBokFrmDemNteSchTaxMapPojo>) getData(query.toString(), namedParameters, FinBokFrmDemNteSchTaxMapPojo.class);
		return demandNoteSchemeTaxDetailsList ;
	}
	
	@Override
	public List<FinSchemePojo> getFinSchemeDetails(FinancialSchemeInfo financialSchemeInfo) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.getFinSchemeDetails() *****");
		StringBuilder query = new StringBuilder(SqlQuery.QRY_TO_GET_FIN_SCHEME)
				.append("AND  FC.NAME = :NAME ");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(),Types.BIGINT);
		namedParameters.addValue("NAME",financialSchemeInfo.getSchemeName(),Types.VARCHAR);
		
		
		List<FinSchemePojo> SchemeTaxList = (List<FinSchemePojo>) getData(query.toString(), namedParameters, FinSchemePojo.class);
		return SchemeTaxList;
	} 
	
	@Override
	public int insertFinSchemeDetails(FinSchemePojo finSchemePojo) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.insertFinSchemeDetails() *****"+finSchemePojo);
		String query = SqlQuery.QRY_TO_INSERT_FIN_SCHEME;
		Long pk = commonMethodToInsertData(query, finSchemePojo, "FIN_SCHEME_ID");
		finSchemePojo.setFinSchemeId(pk);
		return pk.intValue();
	}
	
	@Override
	public int insertFinTaxDetails(FinTaxPojo finTaxPojo) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.insertFinTaxDetails() *****"+finTaxPojo);
		String query = SqlQuery.QRY_TO_INSERT_FIN_TAX;
		Long pk = commonMethodToInsertData(query, finTaxPojo, "FIN_TAX_ID");
		finTaxPojo.setFinTaxId(pk);
		return pk.intValue();
	}
	
	@Override
	public int insertFinSchemeTaxMappingDetails(FinSchemeTaxMappingPojo finSchemePojo) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.insertFinSchemeTaxMappingDetails() *****");
		String query = SqlQuery.QRY_TO_INSERT_FIN_SCHEME_TAX_MAPPING;
		Long pk = commonMethodToInsertData(query, finSchemePojo, "FIN_SCHE_TAX_MAP_ID");
		finSchemePojo.setFinSchemeTaxMappingId(pk);
		return pk.intValue();
	}
	
	@Override
	public int insertFlatBookingSchemeMappingDetails(FlatBookingSchemeMappingPojo flatBookingSchemeMappingPojo) {
		//log.info( "***** Control inside the EmployeeFinancialServiceDaoImpl.insertFlatBookingSchemeMappingDetails() *****");	
		String query = SqlQuery.QRY_TO_INSERT_FLAT_BOK_SCHM_MAPPING;
		Long pk = commonMethodToInsertData(query, flatBookingSchemeMappingPojo, "FLT_BOK_SCHM_MAP_ID");
		flatBookingSchemeMappingPojo.setFltBookingSchemeMappingId(pk);
		return pk.intValue();
	}
	
	@Override
	public List<FlatBookingSchemeMappingPojo> getFlatBookingSchemeMappingDetails(FlatBookingSchemeMappingPojo flatBookingSchemeMappingPojo) {
		//log.info( "***** Control inside the EmployeeFinancialServiceDaoImpl.getFlatBookingSchemeMappingDetails() *****");
		String query = SqlQuery.QRY_TO_GET_FLAT_BOK_SCHM_MAPPING_DETAILS;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(),Types.BIGINT); 
		namedParameters.addValue("BOOKING_FORM_ID", flatBookingSchemeMappingPojo.getBookingFormId(),Types.BIGINT); 
		namedParameters.addValue("FIN_SCHE_TAX_MAP_ID", flatBookingSchemeMappingPojo.getFinSchemeTaxMappingId(),Types.BIGINT); 
		
		List<FlatBookingSchemeMappingPojo> flatSchemeDetailsList = (List<FlatBookingSchemeMappingPojo>) getData(query.toString(), namedParameters, FlatBookingSchemeMappingPojo.class);
		return flatSchemeDetailsList;
	}
	
	@Override
	public List<FinSchemeTaxMappingPojo> checkSchemeDetails(FinancialSchemeInfo schemeDetails) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.checkSchemeDetails() *****");
		StringBuilder query = new StringBuilder("SELECT")
				.append(" FSTM.FIN_SCHE_TAX_MAP_ID,FSTM.FIN_SCHEME_ID,FSTM.FIN_TAX_ID,FSTM.PERCENTAGE_ID,FSTM.SITE_ID ")
				.append(" FROM FIN_SCHEME_TAX_MAPPING FSTM  ")
				.append(" JOIN PERCENTAGES PERCENTAGE ON PERCENTAGE.PERCENTAGE_ID = FSTM.PERCENTAGE_ID ")
				.append(" WHERE FSTM.SITE_ID = :SITE_ID AND FSTM.STATUS_ID = :STATUS_ID ")
				.append(" AND PERCENTAGE.PERCENTAGE = :PERCENTAGE ")
				.append(" AND FSTM.FIN_SCHEME_ID = :FIN_SCHEME_ID ")
				.append(" AND FSTM.FIN_TAX_ID = :FIN_TAX_ID ")
				.append(" AND TRUNC(FSTM.START_DATE) = TRUNC(:START_DATE) ")
				.append(" AND TRUNC(FSTM.END_DATE) = TRUNC(:END_DATE) ");
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(),Types.BIGINT);
		namedParameters.addValue("SITE_ID",schemeDetails.getSiteId(),Types.BIGINT);
		namedParameters.addValue("START_DATE",schemeDetails.getStartDate(),Types.TIMESTAMP);
		namedParameters.addValue("END_DATE",schemeDetails.getEndDate(),Types.TIMESTAMP);
		namedParameters.addValue("PERCENTAGE",schemeDetails.getPercentageValue(),Types.DOUBLE);
		namedParameters.addValue("FIN_SCHEME_ID",schemeDetails.getFinSchemeId(),Types.DOUBLE);
		namedParameters.addValue("FIN_TAX_ID",schemeDetails.getFinTaxId(),Types.DOUBLE);
		
		
		List<FinSchemeTaxMappingPojo> siteSchemeDetailsList = (List<FinSchemeTaxMappingPojo>) getData(query.toString(), namedParameters, FinSchemeTaxMappingPojo.class);
		return siteSchemeDetailsList;
	}
	
	@Override
	public List<FinTaxPojo> getFinTaxDetails(FinancialSchemeInfo financialSchemeInfo) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.getFinTaxDetails() *****");
		StringBuilder query = new StringBuilder(SqlQuery.QRY_TO_GET_FIN_TAX)
				.append(" WHERE FT.NAME = :NAME")
				.append(" AND FT.STATUS_ID = :STATUS_ID");
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(),Types.BIGINT);
		namedParameters.addValue("NAME",financialSchemeInfo.getTaxName(),Types.VARCHAR);
		
		List<FinTaxPojo> taxDetailsList = (List<FinTaxPojo>) getData(query.toString(), namedParameters, FinTaxPojo.class);
		return taxDetailsList;
	}

	/**
	 * used this method for noc valiation, welcoeme letter, demand note, legal, modification , receipt's to get the gst percentage
	 */
	@Override
	public List<FinPenaltyTaxPojo> getTaxOnInterestAmountData(FinancialProjectMileStoneInfo finProjDemandNoteInfo, List<Long> metaData) {
	 	//log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.getTaxOnInterestAmountData() ***** ");
	 	MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	 	String condition = finProjDemandNoteInfo.getCondition() == null?"":finProjDemandNoteInfo.getCondition();
	 	StringBuffer query = new StringBuffer( SqlQuery.QRY_TO_GET_FIN_PENALTY_TAX_PERCENTAGE)
	 			.append(" WHERE PERCENTAGE.PERCENTAGE_ID = FTM.PERCENTAGE_ID ")
	 			.append(" AND FTM.TAX_TYPE IN (:TAX_TYPEs) ");
	 	
		if (finProjDemandNoteInfo != null && finProjDemandNoteInfo.getSiteId() != null) {
			query.append(" AND FTM.SITE_ID = :SITE_ID ");
			namedParameters.addValue("SITE_ID", finProjDemandNoteInfo.getSiteId(), Types.BIGINT);
		}
	 	
	 	/*query.append(" AND TRUNC(:MILE_STONE_DATE) BETWEEN TRUNC(FTM.START_DATE) AND TRUNC(FTM.END_DATE)")
				.append(" AND FTM.STATUS_ID=:STATUS_ID ")
				.append(" ORDER BY FTM.FIN_TAX_MAPING_ID");*/
		if(condition.equals("ReceiptEntry") || condition.equals("NOC_Validation")) {
			namedParameters.addValue("MILE_STONE_DATE", finProjDemandNoteInfo.getMilestoneDate(), Types.TIMESTAMP);
			
		 	query.append(" AND TRUNC(:MILE_STONE_DATE) BETWEEN TRUNC(FTM.START_DATE) AND TRUNC(FTM.END_DATE)")
				 .append(" AND FTM.STATUS_ID = :STATUS_ID ")
				 .append(" ORDER BY FTM.FIN_TAX_MAPING_ID");
		} else {
			if (metaData.get(0).equals(MetadataId.FIN_PENALTY.getId())) {
				//namedParameters.addValue("MILE_STONE_DATE", finProjDemandNoteInfo.getMilestoneDate(), Types.TIMESTAMP);
				namedParameters.addValue("START_DATE", finProjDemandNoteInfo.getMileStoneDueDate(), Types.TIMESTAMP);
				namedParameters.addValue("END_DATE", finProjDemandNoteInfo.getMileStonePaidDate(), Types.TIMESTAMP);
			 	
			 	query.append(" AND FTM.FIN_TAX_MAPING_ID ");
	 			query.append("BETWEEN (SELECT FIN_TAX_MAPING_ID FROM FIN_TAX_MAPPING WHERE SITE_ID IN (:SITE_ID) AND TAX_TYPE IN (:TAX_TYPEs) AND STATUS_ID=:STATUS_ID AND TRUNC(:START_DATE) BETWEEN  TRUNC(START_DATE) AND TRUNC(END_DATE))")
	 				 .append(" AND (SELECT FIN_TAX_MAPING_ID FROM FIN_TAX_MAPPING WHERE SITE_ID IN (:SITE_ID) AND TAX_TYPE IN (:TAX_TYPEs) AND STATUS_ID=:STATUS_ID AND TRUNC(:END_DATE) BETWEEN  TRUNC(START_DATE) AND TRUNC(END_DATE))")
					 .append(" AND FTM.STATUS_ID=:STATUS_ID ")
					 .append(" ORDER BY FTM.FIN_TAX_MAPING_ID");
			} else {
				namedParameters.addValue("MILE_STONE_DATE", new Timestamp(new Date().getTime()), Types.TIMESTAMP);
				
			 	query.append(" AND TRUNC(:MILE_STONE_DATE) BETWEEN TRUNC(FTM.START_DATE) AND TRUNC(FTM.END_DATE)")
					 .append(" AND FTM.STATUS_ID=:STATUS_ID ")
					 .append(" ORDER BY FTM.FIN_TAX_MAPING_ID");
			}
		}
		
		//namedParameters.addValue("MILE_STONE_DATE", finProjDemandNoteInfo.getMilestoneDate(),Types.TIMESTAMP);
		namedParameters.addValue("STATUS_ID",Status.ACTIVE.getStatus(),Types.BIGINT);
		namedParameters.addValue("TAX_TYPEs", metaData,Types.BIGINT);
		
		List<FinPenaltyTaxPojo> finPenaltyTaxDetailsList = null ;
		try {
			finPenaltyTaxDetailsList = (List<FinPenaltyTaxPojo>) getData(query.toString(), namedParameters, FinPenaltyTaxPojo.class);
		}catch(Exception e) {
			finPenaltyTaxDetailsList = new ArrayList<>();
			log.info(e.getMessage(), e);
		}
		return finPenaltyTaxDetailsList;
	}
	
	@Override
	public List<FinInterestRatesPojo> getTaxOnInterestData(EmployeeFinancialServiceInfo serviceInfo,FinancialGstDetailsInfo gstDetailsInfo) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.getTaxOnInterestData() *****");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuffer query = new StringBuffer(SqlQuery.QRY_TO_GET_FIN_INTEREST_RATES)
				.append(" AND FIR.STATUS_ID=:STATUS_ID ");
		if (Util.isNotEmptyObject(serviceInfo.getCondition()) && serviceInfo.getCondition().equals(FinEnum.UPDATE_INTEREST_DATA.getName())) {
			query.append(" AND  FIR.FIN_INTEREST_RATES_ID BETWEEN (SELECT FIN_INTEREST_RATES_ID FROM FIN_INTEREST_RATES WHERE  STATUS_ID=:STATUS_ID AND  :START_DATE BETWEEN TRUNC(START_DATE) AND TRUNC(END_DATE))");
			query.append(" AND (SELECT FIN_INTEREST_RATES_ID FROM FIN_INTEREST_RATES WHERE  STATUS_ID=:STATUS_ID AND  :END_DATE BETWEEN TRUNC(START_DATE) AND TRUNC(END_DATE))");
			query.append(" AND FIR.SITE_ID IN (:SITE_IDs) ");
		} else {
			query.append(" AND TRUNC(START_DATE) = TRUNC(:START_DATE) AND TRUNC(END_DATE) = TRUNC(:END_DATE) ");
			query.append(" AND FIR.SITE_ID IN (:SITE_IDs) ");
		}
		
		query.append(" ORDER BY FIR.FIN_INTEREST_RATES_ID");

		if (serviceInfo.getActionUrl().equals(FinEnum.LOAD_INTEREST_DATA.getName())) {
			query = new StringBuffer(SqlQuery.QRY_TO_GET_GROUP_FIN_INTEREST_RATES);
		}

		if (gstDetailsInfo != null) {
			//String rowStatus = gstDetailsInfo.getRowStatus();
			namedParameters.addValue("START_DATE", gstDetailsInfo.getActualStartDate(), Types.TIMESTAMP);
			namedParameters.addValue("END_DATE", gstDetailsInfo.getActualEndDate(), Types.TIMESTAMP);
		}
		namedParameters.addValue("SITE_IDs", serviceInfo.getSiteIds(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);

		
		List<FinInterestRatesPojo> finTaxDetailsList = (List<FinInterestRatesPojo>) getData(query.toString(), namedParameters, FinInterestRatesPojo.class);
		return finTaxDetailsList;
	}

	@Override
	public List<String> validateInterestRatesDates(EmployeeFinancialServiceInfo serviceInfo, FinancialGstDetailsInfo gstDetailsInfo, 
			FinInterestRatesPojo finInterestRatesPojo) {
		log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.validateInterestRatesDates() *****");
		List<String> msg = new ArrayList<>();
		StringBuffer errorMsg = null;
		try {
			String rowStatus = gstDetailsInfo.getRowStatus();
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			StringBuffer query = new StringBuffer(SqlQuery.QRY_TO_GET_FIN_INTEREST_RATES)
					.append(" AND FIR.STATUS_ID=:STATUS_ID ");
			query.append(" AND  FIR.FIN_INTEREST_RATES_ID ");
			query.append(" BETWEEN (SELECT FIN_INTEREST_RATES_ID FROM FIN_INTEREST_RATES WHERE SITE_ID IN (:SITE_IDs) AND STATUS_ID=:STATUS_ID AND TRUNC(:START_DATE) BETWEEN TRUNC(START_DATE) AND TRUNC(END_DATE)) ");
			query.append(" AND     (SELECT FIN_INTEREST_RATES_ID FROM FIN_INTEREST_RATES WHERE SITE_ID IN (:SITE_IDs) AND STATUS_ID=:STATUS_ID AND TRUNC(:END_DATE) BETWEEN TRUNC(START_DATE) AND TRUNC(END_DATE)) ");
			query.append(" AND FIR.SITE_ID IN (:SITE_IDs) ");

			namedParameters.addValue("START_DATE", gstDetailsInfo.getStartDate(), Types.TIMESTAMP);
			namedParameters.addValue("END_DATE", gstDetailsInfo.getEndDate(), Types.TIMESTAMP);
			namedParameters.addValue("SITE_IDs", finInterestRatesPojo.getSiteId(), Types.BIGINT);
			namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
			errorMsg = new StringBuffer("Interest Rates dates are found duplicate Start Date is : "+TimeUtil.timestampToDD_MM_YYYY(gstDetailsInfo.getStartDate()) +", End Date is "+TimeUtil.timestampToDD_MM_YYYY(gstDetailsInfo.getEndDate())+", Please change the Dates.");
			
			if (rowStatus != null && rowStatus.equalsIgnoreCase(Status.ADDED.getDescription())) {
				List<Map<String, Object>> list = nmdPJdbcTemplate.queryForList(query.toString(), namedParameters);
				if(list.isEmpty()) {
					query = new StringBuffer(SqlQuery.QRY_TO_GET_FIN_INTEREST_RATES)
							.append(" AND FIR.STATUS_ID=:STATUS_ID ");
					query.append(" AND  FIR.FIN_INTEREST_RATES_ID ");
					query.append(" IN (SELECT FIN_INTEREST_RATES_ID FROM FIN_INTEREST_RATES WHERE SITE_ID IN (:SITE_IDs) AND STATUS_ID=:STATUS_ID AND TRUNC(:START_DATE) BETWEEN TRUNC(START_DATE) AND TRUNC(END_DATE)) ");
					query.append(" AND FIR.SITE_ID IN (:SITE_IDs) ");
					list = nmdPJdbcTemplate.queryForList(query.toString(), namedParameters);
					if (list.size() != 0) {
						msg.add(errorMsg.toString());
						return msg;
					}
					if(list.isEmpty()) {
						query = new StringBuffer(SqlQuery.QRY_TO_GET_FIN_INTEREST_RATES)
								.append(" AND FIR.STATUS_ID=:STATUS_ID ");
						query.append(" AND  FIR.FIN_INTEREST_RATES_ID ");
						query.append(" IN (SELECT FIN_INTEREST_RATES_ID FROM FIN_INTEREST_RATES WHERE SITE_ID IN (:SITE_IDs) AND STATUS_ID=:STATUS_ID AND TRUNC(:END_DATE) BETWEEN TRUNC(START_DATE) AND TRUNC(END_DATE)) ");
						query.append(" AND FIR.SITE_ID IN (:SITE_IDs) ");
						list = nmdPJdbcTemplate.queryForList(query.toString(), namedParameters);
						if (list.size() != 0) {
							msg.add(errorMsg.toString());
							return msg;
						}
					}
				} else if (list.size() != 0) {
					//log.debug(list);
					msg.add(errorMsg.toString());
					return msg;
				}
			} else if (rowStatus != null && rowStatus.equalsIgnoreCase(Status.MODIFY.getDescription())) {
				namedParameters.addValue("ACTUAL_START_DATE", gstDetailsInfo.getActualStartDate(), Types.TIMESTAMP);
				namedParameters.addValue("ACTUAL_END_DATE", gstDetailsInfo.getActualEndDate(), Types.TIMESTAMP);
				query = new StringBuffer(SqlQuery.QRY_TO_GET_FIN_INTEREST_RATES)
						.append(" AND FIR.STATUS_ID=:STATUS_ID ");
				query.append(" AND  FIR.FIN_INTEREST_RATES_ID ");
				query.append(" IN (SELECT FIN_INTEREST_RATES_ID FROM FIN_INTEREST_RATES WHERE SITE_ID IN (:SITE_IDs) AND STATUS_ID=:STATUS_ID ");
				query.append(" AND TRUNC(:ACTUAL_START_DATE) NOT BETWEEN TRUNC(START_DATE) AND TRUNC(END_DATE)");
				query.append(" AND TRUNC(:START_DATE) BETWEEN TRUNC(START_DATE) AND TRUNC(END_DATE))");
				query.append(" AND FIR.SITE_ID IN (:SITE_IDs) ");
				
				List<Map<String, Object>> list = nmdPJdbcTemplate.queryForList(query.toString(), namedParameters);
				if (list.size() != 0) {
					//log.debug(list);
					msg.add(errorMsg.toString());
					return msg;
				}
				if(list.isEmpty()) {
					query = new StringBuffer(SqlQuery.QRY_TO_GET_FIN_INTEREST_RATES)
							.append(" AND FIR.STATUS_ID=:STATUS_ID ");
					query.append(" AND FIR.FIN_INTEREST_RATES_ID ");
					query.append(" IN (SELECT FIN_INTEREST_RATES_ID FROM FIN_INTEREST_RATES WHERE SITE_ID IN (:SITE_IDs) AND STATUS_ID=:STATUS_ID ");
					query.append(" AND TRUNC(:ACTUAL_END_DATE) NOT BETWEEN TRUNC(START_DATE) AND TRUNC(END_DATE) ");
					query.append(" AND TRUNC(:END_DATE) BETWEEN TRUNC(START_DATE) AND TRUNC(END_DATE))");
					query.append(" AND FIR.SITE_ID IN (:SITE_IDs) ");
					list = nmdPJdbcTemplate.queryForList(query.toString(), namedParameters);
					if (list.size() != 0) {
						//log.debug(list);
						msg.add(errorMsg.toString());
						return msg;
					}
				}
			}
		} catch (Exception e) {
			log.info("***** Exception raised EmployeeFinancialServiceDaoImpl.validateInterestRatesDates() *****"+ e.getMessage());
			msg.add("Error occured while loading records from the database plz try again once !");
			return msg;
		}
		return msg;
	}
	
	@Override
	public int isPercentageExists(FinancialGstDetailsInfo gstDetailsInfo) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.isPercentageExists() *****");
		int percentageId = 0;
		String query = SqlQuery.QRY_TO_CHECK_PERCENTAGE_EXISTS_OR_NOT;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("PERCENTAGE", gstDetailsInfo.getPercentageValue(), Types.DOUBLE);
		try {
			percentageId = nmdPJdbcTemplate.queryForObject(query, namedParameters, Integer.class);
			gstDetailsInfo.setPercentageId((long)percentageId);
		} catch (Exception e) {
			log.info("***** Exception raised EmployeeFinancialServiceDaoImpl.isPercentageExists() *****"
					+ e.getMessage());
		}
		return percentageId;
	}
	
	@Override
	public long savePercentage(FinancialGstDetailsInfo gstDetailsInfo) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.savePercentage() *****");
		String query = SqlQuery.QRY_TO_INSERT_PERCENTAGE;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("PERCENTAGE", gstDetailsInfo.getPercentageValue());
		namedParameters.addValue("DESCRIPTION", "Interest Percentage");
		long percentageId = commonMethodToInsertData(query, namedParameters, "PERCENTAGE_ID");
		gstDetailsInfo.setPercentageId(percentageId);
		return percentageId;
	}
	
	@Override
	public Long insertInterestDetails(FinInterestRatesPojo interestRatesPojo) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.insertInterestDetails() *****");
		String query = SqlQuery.QRY_TO_INTEREST_FIN_INTEREST_RATES;
		long  pk = commonMethodToInsertData(query, interestRatesPojo, "FIN_INTEREST_RATES_ID");
		interestRatesPojo.setFinInterestRatesId(pk);
		return pk;
	}
	
	@Override
	public int inActiveInterestRatesDetails(EmployeeFinancialServiceInfo serviceInfo, FinancialGstDetailsInfo gstDetailsInfo, FinInterestRatesPojo finInterestRatesPojo) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.updateTaxOnInterestDetails() *****");
		String query = SqlQuery.QRY_TO_GET_UPDATE_FIN_INTEREST_RATES;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("ACTUAL_START_DATE", gstDetailsInfo.getActualStartDate(), Types.TIMESTAMP);
		namedParameters.addValue("ACTUAL_END_DATE", gstDetailsInfo.getActualEndDate(), Types.TIMESTAMP);
		namedParameters.addValue("SITE_ID", finInterestRatesPojo.getSiteId(), Types.BIGINT);
		namedParameters.addValue("MODIFIED_BY", serviceInfo.getEmpId(), Types.BIGINT);

		/*namedParameters.addValue("START_DATE", gstDetailsInfo.getStartDate(), Types.TIMESTAMP);
		namedParameters.addValue("END_DATE", gstDetailsInfo.getEndDate(), Types.TIMESTAMP);
		namedParameters.addValue("PERCENTAGE_ID", gstDetailsInfo.getPercentageId(), Types.BIGINT);*/
		
		namedParameters.addValue("STATUS_ID", Status.INACTIVE.getStatus(),Types.BIGINT);
		namedParameters.addValue("ACTIVE_STATUS_ID", Status.ACTIVE.getStatus(),Types.BIGINT);
		
		int result = nmdPJdbcTemplate.update(query, namedParameters);
		//log.info("***** EmployeeFinancialServiceDaoImpl.updateTaxOnInterestDetails() method execution completed ***** "+result+" "+namedParameters.getValues());
		return result;
	}
	
	@Override
	public List<FinancialProjectMileStonePojo> isMileStoneInitiatedForThisFlatBookingFormId(FinancialProjectMileStoneInfo finProjDemandNoteInfo,
			CustomerPropertyDetailsInfo info, FinProjDemNoteStatisticsPojo pojoNotInUse) {
		//log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.isMileStoneInitiatedForThisFlatBookingFormId() ***** ");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		boolean isThisUploadedData =  false;
		if (finProjDemandNoteInfo != null) {
			isThisUploadedData = finProjDemandNoteInfo.isThisUplaodedData();
			namedParameters.addValue("PROJECT_MILESTONE_ID", finProjDemandNoteInfo.getProjectMilestoneId(), Types.BIGINT);
		}
		
		StringBuilder query = new StringBuilder("SELECT ")
				.append(SqlQuery.QRY_TO_GET_DATA_FROM_FIN_BOOKING_FORM_MILESTONES).append(",")
				.append(SqlQuery.QRY_TO_GET_FIN_BOOKING_FORM_ACCOUNTS_DATA).append(",")
				.append(SqlQuery.QRY_TO_GET_FIN_PROJECT_MILESTONES)
				.append(" FROM FIN_BOOKING_FORM_MILESTONES FBFM ")
				.append(" INNER JOIN FIN_BOOKING_FORM_ACCOUNTS FBFA ON FBFM.FIN_BOOKING_FORM_MILESTONES_ID = FBFA.TYPE_ID")
				.append(" INNER JOIN FIN_PROJECT_MILESTONES FPM ON FPM.PROJECT_MILESTONE_ID = FBFM.PROJECT_MILESTONE_ID")
				.append(" WHERE FBFM.BOOKING_FORM_ID = :BOOKING_FORM_ID ")
				.append(isThisUploadedData?" AND FBFM.PROJECT_MILESTONE_ID = :PROJECT_MILESTONE_ID":"")
				.append(" AND FBFM.STATUS_ID = :STATUS_ID ")
				.append(" AND FBFA.STATUS_ID = :STATUS_ID ")
				.append(" AND FBFA.TYPE = :TYPE")
				.append(" ORDER BY FBFM.FIN_BOOKING_FORM_MILESTONES_ID ");
		
		namedParameters.addValue("STATUS_ID",Arrays.asList(new Long[] {Status.ACTIVE.getStatus()}),Types.BIGINT);
		namedParameters.addValue("BOOKING_FORM_ID",info.getFlatBookingId() ,Types.BIGINT);
		namedParameters.addValue("TYPE",MetadataId.FIN_BOOKING_FORM_MILESTONES.getId() ,Types.BIGINT);
		
		List<FinancialProjectMileStonePojo>  financialProjectMileStonePojoList =  getData(query.toString(),namedParameters,FinancialProjectMileStonePojo.class);
		return financialProjectMileStonePojoList;
		
/*		String query = new StringBuffer(SqlQuery.QRY_TO_GET_CHECK_MILESTONE_EXISTS_FOR_BOOKING_FORM_ID).toString();
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("PROJECT_MILESTONE_ID", finProjDemandNoteInfo.getProjectMilestoneId(), Types.BIGINT);
		namedParameters.addValue("BOOKING_FORM_ID", info.getFlatBookingId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		flag = nmdPJdbcTemplate.queryForObject(query, namedParameters, Boolean.class);
		//log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.isMileStoneInitiatedForThisFlatBookingFormId() = = "+flag);
		return flag;*/
	}
	
	@Override
	public List<FinBookingFormDemandNotePojo> getDemandNoteId(FinBookingFormDemandNotePojo demandNoteInfo ) {
		//log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.getDemandNoteId() *****");
		String query = SqlQuery.QRY_TO_GET_FIN_BOOKING_FORM_DEMAND_NOTE_ID;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID", demandNoteInfo.getStatusId() ,Types.BIGINT);
		namedParameters.addValue("BOOKING_FORM_ID", demandNoteInfo.getBookingFormId(),Types.BIGINT);
		
		
		List<FinBookingFormDemandNotePojo> demandNoteDetails = (List<FinBookingFormDemandNotePojo>) getData(query, namedParameters, FinBookingFormDemandNotePojo.class);
		return demandNoteDetails;
	}
	
	@Override
	public List<FinBookingFormDemandNotePojo> getMilestoneDemandNoteId(FinancialProjectMileStoneInfo demandNoteInfo) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.getMilestoneDemandNoteId() *****");
		String query = SqlQuery.QRY_TO_GET_FIN_BOOKING_FORM_DEMAND_NOTE_DETAILS;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID", demandNoteInfo.getStatusId() ,Types.BIGINT);
		namedParameters.addValue("BOOKING_FORM_ID", demandNoteInfo.getBookingFormId(),Types.BIGINT);
		namedParameters.addValue("PROJECT_MILESTONE_ID", demandNoteInfo.getProjectMilestoneId(),Types.BIGINT);
		namedParameters.addValue("TYPE_OF_INTEREST", demandNoteInfo.getInterestSelectionType(),Types.BIGINT);
	
		
		List<FinBookingFormDemandNotePojo> demandNoteDetails = (List<FinBookingFormDemandNotePojo>) getData(query, namedParameters, FinBookingFormDemandNotePojo.class);
		return demandNoteDetails;
	}
	
	@Override
	public int updateDemandNoteDetails(FinBookingFormDemandNotePojo finBookingFormDemandNotePojo, FinancialProjectMileStoneInfo demandNoteInfo) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.updateDemandNoteDetails() *****");
		String query = SqlQuery.QRY_TO_UPDATE_FIN_BOOKING_FORM_DEMAND_NOTE_INTEREST;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("statusId", Status.ACTIVE.getStatus() ,Types.BIGINT);
		namedParameters.addValue("interestSelectionType", demandNoteInfo.getInterestSelectionType() ,Types.BIGINT);
		namedParameters.addValue("finBookingFormDemandNoteId", finBookingFormDemandNotePojo.getFinBookingFormDemandNoteId(),Types.BIGINT);
		int result = nmdPJdbcTemplate.update(query, namedParameters);
		return result;
	}
	
	@Override
	public Timestamp getFlatAgreementCompletedDate(FinancialProjectMileStoneInfo finProjDemandNoteInfo103,
			CustomerPropertyDetailsInfo customerPropertyDetailsInfo) {
		//log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.getFlatAgreementCompletedDate() ***** ");
		Timestamp date = null;
		String query = "";
		if(finProjDemandNoteInfo103 == null) {
			query = new StringBuffer( SqlQuery.QRY_TO_CHECK_FLAT_AGREEMENT_COMPLETED_OR_NOT).toString();
		}else {
			query = new StringBuffer( SqlQuery.QRY_TO_CHECK_FLAT_BOOKING_COMPLETED_OR_NOT).toString();	
		}
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		if (Util.isNotEmptyObject(customerPropertyDetailsInfo.getStatusIds())) {
			namedParameters.addValue("STATUS_ID", customerPropertyDetailsInfo.getStatusIds(), Types.BIGINT);
		} else {
			namedParameters.addValue("STATUS_ID", Arrays.asList(Status.ACTIVE.getStatus()), Types.BIGINT);
		}
		namedParameters.addValue("FLAT_BOOK_ID", customerPropertyDetailsInfo.getFlatBookingId(), Types.BIGINT);
		try {
			date = nmdPJdbcTemplate.queryForObject(query, namedParameters, Timestamp.class);
		}catch(Exception e ) {
			log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.getFlatAgreementCompletedDate() *****");
		}
		return date;
		
	}
	
	@Override
	public List<FinancialMileStoneClassifidesPojo> loadCustomerInvolvedAlianNameDetails(EmployeeFinancialServiceInfo employeeFinancialDemandNoteInfo,
			CustomerPropertyDetailsInfo customerPropertyDetailsInfo) {
		//log.info("***** EmployeeFinancialServiceDaoImpl.isHisAlianNameIsChanged() *****");
		if(customerPropertyDetailsInfo.getFlatBookingId().equals(229l)) {
			//System.out.println("EmployeeFinancialServiceDaoImpl.loadCustomerInvolvedAlianNameDetails()");
		}
		String query = SqlQuery.LOAD_CUSTOMER_INVOLVED_ALIAN_NAME_DETAILS.toString();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		//namedParameters.addValue("SITE_ID", employeeFinancialDemandNoteInfo.getSiteId(),Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(),Types.BIGINT);
		namedParameters.addValue("BOOKING_FORM_ID", customerPropertyDetailsInfo.getFlatBookingId(),Types.BIGINT);
		
		
		List<FinancialMileStoneClassifidesPojo> listOfFinancialServicePojo =  (List<FinancialMileStoneClassifidesPojo>) getData(query, namedParameters, FinancialMileStoneClassifidesPojo.class);
		return listOfFinancialServicePojo;
	}
	
	@Override
	public Long saveTransactionEntryDetails(FinTransactionEntryPojo pojo) {
		log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.saveTransactionEntryDetails() ***** ");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		Long pk = nmdPJdbcTemplate.queryForObject("SELECT FIN_TRANSACTION_ENTRY_SEQ.NEXTVAL FROM DUAL",namedParameters, Long.class);
		pojo.setTransactionEntryId(pk);
		String finBokAccInvoiceNo = String.format("%04d",pk);
		pojo.setFinTransactionNo("TR"+finBokAccInvoiceNo);
		String query = SqlQuery.QRY_TO_INSERT_TRANSACTION_ENTRY_DETAILS;
		pk = commonMethodToInsertData(query, pojo, "FIN_TRANSACTION_ENTRY_ID");
		return pk;
	}
	
	@Override
	public long saveTransactionChangedDetails(List<FinTransactionChangedDtlsPojo> changedDetailsList) {
		//System.out.println("***** The Control is inside EmployeeFinancialServiceDaoImpl.saveTransactionChangedDetails()");
		SqlParameterSource[] sqlParameterSource = new SqlParameterSource[changedDetailsList.size()];
		int sqlParamIndex=0;
		String query = SqlQuery.QRY_TO_INSERT_TRANSACTION_CHANGED_DETAILS;
		
		for (FinTransactionChangedDtlsPojo changedDtlsPojo : changedDetailsList) {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("transactionApproveStatId", changedDtlsPojo.getTransactionApproveStatId(), Types.BIGINT);
			namedParameters.addValue("anonymousEntryId", changedDtlsPojo.getAnonymousEntryId(), Types.BIGINT);
			namedParameters.addValue("actualValue", changedDtlsPojo.getActualValue(), Types.VARCHAR);
			namedParameters.addValue("changedValue", changedDtlsPojo.getChangedValue(), Types.VARCHAR);
			namedParameters.addValue("empId", changedDtlsPojo.getEmpId(), Types.BIGINT);
			namedParameters.addValue("remarks", changedDtlsPojo.getRemarks(), Types.VARCHAR);
			namedParameters.addValue("columnId", changedDtlsPojo.getColumnId(), Types.BIGINT);
			namedParameters.addValue("actionType", changedDtlsPojo.getActionType(), Types.BIGINT);
			//log.info("EmployeeFinancialServiceDaoImpl.saveTransactionChangedDetails() : "+namedParameters.getValues());
			sqlParameterSource[sqlParamIndex++] = namedParameters;
		}
		
		int result[] = nmdPJdbcTemplate.batchUpdate(query, sqlParameterSource);
		return result.length;
	}
	
	@Override
	public List<FinTransactionChangedDtlsPojo> getTransactionChnagedDetails(FinTransactionEntryDetailsInfo finTransactionEntryDetailsInfo) {
		log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.getTransactionChnagedDetails()");
		String query = SqlQuery.QRY_TO_GET_TRANSACTION_CHANGED_DETAILS;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FIN_TRANSACTION_ENTRY_ID", finTransactionEntryDetailsInfo.getTransactionEntryId(), Types.BIGINT);
		
		List<FinTransactionChangedDtlsPojo> changedDetailsList = (List<FinTransactionChangedDtlsPojo>) getData(query, namedParameters, FinTransactionChangedDtlsPojo.class);
		return changedDetailsList;
	}
	
	@Override
	public int updateTransactionDetails(EmployeeFinancialTransactionServiceInfo transactionServiceInfo) {
		//log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.updateTransactionDetails() *****");
		int result = 0;
		String query = SqlQuery.QRY_TO_UPDATE_TRANSACTION_ENTRY_RECEIPT_NO;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FIN_TRANSACTION_ENTRY_ID", transactionServiceInfo.getTransactionEntryId(), Types.BIGINT);
		namedParameters.addValue("TRANSACTION_RECEIPT_NO", transactionServiceInfo.getTransactionReceiptNo(), Types.VARCHAR);
		namedParameters.addValue("SOURCE_OF_FUNDS", transactionServiceInfo.getSourceOfFunds(), Types.VARCHAR);
		result = commonMethodToUpdateData(query, namedParameters);
		return result;
	}
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	private static Timestamp dateForValidation = null;
	@Override
	public int updateTransactionStatus(EmployeeFinancialTransactionServiceInfo transactionServiceInfo, Long statusId) {
		log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.updateTransactionStatus() ***** ");
		@SuppressWarnings("unused")
		boolean isDeleteTransaction = (transactionServiceInfo.getCondition()!=null && transactionServiceInfo.getCondition().equals("DeleteTransaction"));
		String query = SqlQuery.QRY_TO_UPDATE_TRANSACTION_ENTRY_STATUS;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("TRANSACTION_STATUS_ID", statusId, Types.BIGINT);
		Timestamp transactionClosedDate = null;
		if (transactionServiceInfo.isThisUplaodedData()) {
			try {
				if(dateForValidation==null) {
					dateForValidation = TimeUtil.removeTimePartFromTimeStamp1(new Timestamp(dateFormat.parse("15-12-2012").getTime()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			transactionClosedDate = transactionServiceInfo.getTransactionClosedDate()==null?transactionServiceInfo.getChequeClearanceDate():transactionServiceInfo.getTransactionClosedDate();
			if (transactionServiceInfo.getTransactionClosedDate()!=null && dateForValidation!=null && transactionServiceInfo.getTransactionClosedDate().before(dateForValidation)){
				//checking closed date is valid or not, if not then take receive date
				transactionClosedDate = transactionServiceInfo.getTransactionReceiveDate();
			}
			if (transactionClosedDate == null) {
				transactionClosedDate = transactionServiceInfo.getTransactionReceiveDate();
			}
		} else {
			transactionClosedDate = new Timestamp(new java.util.Date().getTime());
		}
		if(statusId.equals(Status.TRANSACTION_COMPLETED.getStatus()) || statusId.equals(Status.CHEQUE_BOUNCED.getStatus())) {
			namedParameters.addValue("TRANSACTION_CLOSED_DATE",transactionClosedDate, Types.TIMESTAMP);
		} else {
			//if(!isDeleteTransaction) {
				namedParameters.addValue("TRANSACTION_CLOSED_DATE", null);
			//}
		}
		
		if(transactionServiceInfo.isThisIsModifyTransaction() || transactionServiceInfo.isThisEditTransaction() 
				|| transactionServiceInfo.isThisDeleteTransaction() || transactionServiceInfo.isThisShiftTransaction()) {
			namedParameters.addValue("TRANSACTION_CLOSED_DATE",transactionServiceInfo.getTransactionClosedDate(), Types.TIMESTAMP);
		}
		
		namedParameters.addValue("FIN_TRANSACTION_ENTRY_ID", transactionServiceInfo.getTransactionEntryId(), Types.BIGINT);
		int result = commonMethodToUpdateData(query, namedParameters);
		return result;
	}

	@Override
	public int updateModificationTransactionStatus(EmployeeFinancialTransactionServiceInfo transactionServiceInfo, Long statusId) {
		log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.updateTransactionStatus() ***** ");
		String query = SqlQuery.QRY_TO_UPDATE_MODIFICATION_ENTRY_STATUS;
		Timestamp invoiceClosedDate = new Timestamp(new java.util.Date().getTime());
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FIN_SET_OFF_APPR_LVL_ID", transactionServiceInfo.getFinsetOffAppLevelId(), Types.BIGINT);
		namedParameters.addValue("MODIFICATION_STATUS_ID", statusId, Types.BIGINT);
		namedParameters.addValue("INVOICE_CLOSED_DATE",invoiceClosedDate, Types.TIMESTAMP);
		namedParameters.addValue("FIN_BOK_FRM_MODI_COST_ID", transactionServiceInfo.getFinBookingFormModiCostId(), Types.BIGINT);
		int result = commonMethodToUpdateData(query, namedParameters);
		return result;
	}
	
	@Override
	public int updateTransactionEntryDetails(FinTransactionEntryPojo entryPojo) {
		log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.updateTransactionEntryDetails() *****");
		String query = SqlQuery.QRY_TO_UPDATE_TRANSACTION_ENTRY_DETAILS;
		return commonMethodToUpdateData(query, entryPojo);
	}
	
	@Override
	public int updateTransactionChequeDetials(FinTransactionChequePojo pojo) {
		log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.updateTransactionChequeDetials() *****");
		String query = SqlQuery.QRY_TO_UPDATE_TRANSACTION_CHEQUE_DETAILS;
		return  commonMethodToUpdateData(query, pojo);
	}
	@Override
	public int updateTransactionWaivedOffDetials(FinTransactionWaivedOffPojo pojo) {
		log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.updateTransactionWaivedOffDetials() *****");
		String query = FinancialQuerys.QRY_TO_UPDATE_TRANSACTION_WAIVED_OFF;
		return  commonMethodToUpdateData(query, pojo);
	}

	@Override
	public Long updateChequeBounceDetails(FinTransactionChequePojo chequePojo) {
		log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.updateChequeBounceDetails() *****");
		String query = SqlQuery.QRY_TO_UPDATE_TRANSACTION_CHEQUE_BOUNCE_DETAILS;
		return  (long) commonMethodToUpdateData(query, chequePojo);
	}

	/**
	 * Used this method in multiple places, if any code changes need to check it's dependency
	 */
	@Override
	public List<FinTransactionEntryPojo> isAnyTransactionInApproval(EmployeeFinancialTransactionServiceInfo transactionServiceInfo, Status transactionStage) {
		//log.info("EmployeeFinancialServiceDaoImpl.isAnyTransactionInApproval()");
		boolean isThisApprovalRequest = transactionStage.getStatus().equals(Status.APPROVED.getStatus());
		String query = new StringBuilder(SqlQuery.QRY_TO_CHECK_PENDING_TRANSACTION_ENTRY_DETAILS)
				.append(" AND FTE.TRANSACTION_STATUS_ID IN (:TRANSACTION_STATUS_ID)")
				.append(isThisApprovalRequest?" AND FTE.FIN_TRANSACTION_ENTRY_ID < :FIN_TRANSACTION_ENTRY_ID ":"")
				.toString();
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID",Status.ACTIVE.getStatus(),Types.BIGINT);
		namedParameters.addValue("TRANSACTION_STATUS_ID",Arrays.asList(new Long[] {Status.CREATED.getStatus(),Status.NEW.getStatus(),Status.PENDING.getStatus(),Status.APPROVED.getStatus(),Status.MODIFY.getStatus(),Status.UNCLEARED_CHEQUE.getStatus()}),Types.BIGINT);
		namedParameters.addValue("BOOKING_FORM_ID",transactionServiceInfo.getBookingFormId(),Types.BIGINT);
		if(isThisApprovalRequest) {
			namedParameters.addValue("FIN_TRANSACTION_ENTRY_ID",transactionServiceInfo.getTransactionEntryId(),Types.BIGINT);	
		} else {
			
		}
		List<FinTransactionEntryPojo> transactionEntryPojos = getData(query, namedParameters, FinTransactionEntryPojo.class);
		return transactionEntryPojos;
	}
	
	@Override
	public List<FinTransactionEntryPojo> isAnyInterestWaiverInApproval(EmployeeFinancialTransactionServiceInfo transactionServiceInfo, Status transactionStage) {
		//log.info("EmployeeFinancialServiceDaoImpl.isAnyInterestWaiverInApproval()");
		//boolean isThisApprovalRequest = transactionStage.getStatus().equals(Status.APPROVED.getStatus());
		String query = new StringBuilder(SqlQuery.QRY_TO_CHECK_PENDING_TRANSACTION_ENTRY_DETAILS)
				.append(" AND FTE.TRANSACTION_STATUS_ID IN (:TRANSACTION_STATUS_ID)")
				.append(" AND FTE.FIN_TRANSACTION_MODE_ID = :INTEREST_WAIVER_TYPE_ID ")
				.append(" AND FTE.FIN_TRANSACTION_TYPE_ID = :INTEREST_WAIVER_TYPE_ID ")
				//.append(isThisApprovalRequest?" AND FTE.FIN_TRANSACTION_ENTRY_ID < :FIN_TRANSACTION_ENTRY_ID ":"")
				.toString();
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID",Status.ACTIVE.getStatus(),Types.BIGINT);
		namedParameters.addValue("INTEREST_WAIVER_TYPE_ID",FinTransactionType.INTEREST_WAIVER.getId(), Types.BIGINT);
		namedParameters.addValue("TRANSACTION_STATUS_ID",Arrays.asList(new Long[] {Status.CREATED.getStatus(),Status.NEW.getStatus(),Status.PENDING.getStatus(),Status.APPROVED.getStatus(),Status.MODIFY.getStatus(),Status.UNCLEARED_CHEQUE.getStatus()}),Types.BIGINT);
		namedParameters.addValue("BOOKING_FORM_ID",transactionServiceInfo.getBookingFormId(),Types.BIGINT);
		/*if(isThisApprovalRequest) {
			namedParameters.addValue("FIN_TRANSACTION_ENTRY_ID",transactionServiceInfo.getTransactionEntryId(),Types.BIGINT);	
		} else {
			
		}*/
		List<FinTransactionEntryPojo> transactionEntryPojos = getData(query, namedParameters, FinTransactionEntryPojo.class);
		return transactionEntryPojos;
	}
	
	@Override
	public List<FinTransactionEntryPojo> getTransactionDataOnReceiveDate(EmployeeFinancialTransactionServiceInfo transactionServiceInfo,
			Status transactionStage,MetadataId transactionType) {
		boolean isThisApprovalRequest = transactionStage.getStatus().equals(Status.APPROVED.getStatus());
		String query = null;
		
		if(transactionType.getId().equals(MetadataId.RECEIPT.getId())) {
			query = new StringBuilder(SqlQuery.QRY_TO_CHECK_READJUST_TRANSACTION_ENTRY_DETAILS)
					.append(" AND FTE.TRANSACTION_STATUS_ID IN (:TRANSACTION_STATUS_ID)")
					.append(" AND TRUNC(FTE.RECEIVED_DATE) > :transactionReceiveDate ")
					//.append(isThisApprovalRequest?" AND FTE.FIN_TRANSACTION_ENTRY_ID < :FIN_TRANSACTION_ENTRY_ID ":"")
					.append(" ORDER BY FTE.RECEIVED_DATE,FTE.FIN_TRANSACTION_ENTRY_ID")
					.toString();
		}else {
			query = new StringBuilder(SqlQuery.QRY_TO_CHECK_READJUST_TRANSACTION_ENTRY_DETAILS)
					.append(" AND FTE.TRANSACTION_STATUS_ID IN (:TRANSACTION_STATUS_ID)")
					.append(" AND TRUNC(FTE.PAYMENT_DATE) > :transactionDate ")
					//.append(isThisApprovalRequest?" AND FTE.FIN_TRANSACTION_ENTRY_ID < :FIN_TRANSACTION_ENTRY_ID ":"")
					.append(" ORDER BY FTE.PAYMENT_DATE,FTE.FIN_TRANSACTION_ENTRY_ID")
					.toString();
		}
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID",Status.ACTIVE.getStatus(),Types.BIGINT);
		namedParameters.addValue("TRANSACTION_STATUS_ID",Arrays.asList(new Long[] {Status.TRANSACTION_COMPLETED.getStatus(),Status.CREATED.getStatus(),Status.NEW.getStatus(),Status.PENDING.getStatus(),Status.APPROVED.getStatus(),Status.MODIFY.getStatus(),Status.UNCLEARED_CHEQUE.getStatus()}),Types.BIGINT);
		namedParameters.addValue("BOOKING_FORM_ID",transactionServiceInfo.getBookingFormId(),Types.BIGINT);
		if(isThisApprovalRequest) {
			namedParameters.addValue("FIN_TRANSACTION_ENTRY_ID",transactionServiceInfo.getTransactionEntryId(),Types.BIGINT);	
		}
		List<FinTransactionEntryPojo> transactionEntryPojos = getData(query, namedParameters, FinTransactionEntryPojo.class);
		return transactionEntryPojos;
	}
	
	/**
	 * 	Note : if code changed need to check dependency code
	 */
	@Override
	public Long checkDemandNoteIsGeneratedOrNotForTransaction(EmployeeFinancialTransactionServiceInfo transactionServiceInfo) {
		//log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.checkDemandNoteIsGeneratedOrNotForTransaction() *****");
		Long count = 0l;
		String query = SqlQuery.QRY_TO_CHECK_DEMAND_NOTE_ENTRY_DETAILS;
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
			namedParameters.addValue("BOOKING_FORM_ID", transactionServiceInfo.getBookingFormId(), Types.BIGINT);
			
			count = nmdPJdbcTemplate.queryForObject(query, namedParameters, Long.class);
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return count;
	}
	
	@Override
	public Long checkDemandNoteAccountDetails(EmployeeFinancialTransactionServiceInfo transactionServiceInfo) {
		//log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.checkDemandNoteIsGeneratedOrNotForTransaction() *****");
		Long count = 0l;
		String query = FinancialQuerys.QRY_TO_CHECK_ACCOUNT_DETAILS.toString();
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
			namedParameters.addValue("ACCOUNT_TYPE", MetadataId.DEMAND_NOTE_ACCOUNT_NUMBER.getId() , Types.BIGINT);
			namedParameters.addValue("SITE_ID", transactionServiceInfo.getSiteId() , Types.BIGINT);
			namedParameters.addValue("FLATS_SALE_OWNERS_ID", transactionServiceInfo.getFlatSaleOwnerId() , Types.BIGINT);
			
			count = nmdPJdbcTemplate.queryForObject(query, namedParameters, Long.class);
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return count;
	}
	
	@Override
	public Long checkSchemeDetailsInserted(EmployeeFinancialTransactionServiceInfo transactionServiceInfo) {
		//log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.checkDemandNoteIsGeneratedOrNotForTransaction() *****");
		Long count = 0l;
		String query = FinancialQuerys.QRY_TO_CHECK_SCHEME_ENTRY_DETAILS.toString();
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
			namedParameters.addValue("BOOKING_FORM_ID", transactionServiceInfo.getBookingFormId(), Types.BIGINT);
			
			count = nmdPJdbcTemplate.queryForObject(query, namedParameters, Long.class);
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return count;
	}
	
	@Override
	public int isAnyTransactionCreated(EmployeeFinancialTransactionServiceInfo transactionServiceInfo) {
		Long count = 0l;
		String query = FinancialQuerys.SQL_QRY_FOR_TRANSACTION_COUNT.toString();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("BOOKING_FORM_ID", transactionServiceInfo.getBookingFormId(), Types.BIGINT);
		try {
			count = nmdPJdbcTemplate.queryForObject(query, namedParameters, Long.class);
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return count.intValue();
	}
	
	@Override
	public List<FinTransactionApprovalDetailsPojo> getNextLevelEmployeeApprovalDetails(EmployeeFinancialTransactionServiceInfo transactionServiceInfo,
			 FinTransactionApprovalDetailsPojo finTransactionApprovalDetailsPojo,Status status) {
		//log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.getNextLevelEmployeeApprovalDetails() ***** ");
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder query = new StringBuilder(SqlQuery.QRY_TO_GET_TRANSACTION_APPROVAL_DETAILS)
				.append(" FROM FIN_TRANSACTION_SET_OFF_APPROVAL_LEVEL FTSOAL")
				.append(" ,FIN_TRANSACTION_SET_OFF_LEVEL FTSOL");
				if(status!=null && !status.getDescription().equalsIgnoreCase("APPROVED")) {//if this is other than approve request
					query.append(" LEFT OUTER JOIN FIN_TRN_SET_OFF_APPR_TYPE FTSOAT ON FTSOL.FIN_TRAN_SET_OFF_LEVEL_ID = FTSOAT.FIN_TRAN_SET_OFF_LEVEL_ID ");
				} else if (Status.MODIFY.getStatus().equals(transactionServiceInfo.getTransactionStatusId()) && status.getDescription().equalsIgnoreCase("APPROVED")) {
					//if this is modify transaction request, and this transaxction is approving then based on Payment set off type checking employee has access or not for payment set off
					query.append(" LEFT OUTER JOIN FIN_TRN_SET_OFF_APPR_TYPE FTSOAT ON  FTSOL.FIN_TRAN_SET_OFF_LEVEL_ID = FTSOAT.FIN_TRAN_SET_OFF_LEVEL_ID ");
				}
				query.append(" ,FIN_TRANSACTION_LEVEL FTL");
				if(Status.NEW.getStatus().equals(transactionServiceInfo.getTransactionStatusId()) && status.getDescription().equalsIgnoreCase(Status.CREATED.getDescription())) {
					//for approval these table joining is not required, while creating transaction only we are checking employee wise access
					query.append(" ,FIN_TRN_EMP_SET_OFF_LEVEL_MAPPING FTESOLM");
				}
				query.append(" WHERE FTSOL.FIN_TRAN_SET_OFF_LEVEL_ID = FTSOAL.FIN_TRAN_SET_OFF_LEVEL_ID");
				query.append(" AND FTL.FIN_LEVEL_ID   = FTSOL.FIN_LEVEL_ID");
				if(Status.NEW.getStatus().equals(transactionServiceInfo.getTransactionStatusId()) && status.getDescription().equalsIgnoreCase(Status.CREATED.getDescription())) {
					query.append(" AND FTSOL.FIN_TRAN_SET_OFF_LEVEL_ID =  FTESOLM.FIN_TRAN_SET_OFF_LEVEL_ID");
				}
				query.append(" AND FTSOL.FIN_TRANSACTION_TYPE_ID = :FIN_TRANSACTION_TYPE_ID");
				query.append(" AND FTSOL.FIN_TRANSACTION_MODE_ID = :FIN_TRANSACTION_MODE_ID");
				if(Status.NEW.getStatus().equals(transactionServiceInfo.getTransactionStatusId()) && status.getDescription().equalsIgnoreCase(Status.CREATED.getDescription())) { 
					query.append(" AND FTESOLM.EMP_ID = :EMP_ID");
				}
				query.append(" AND FTSOAL.STATUS_ID = :STATUS_ID");
				query.append(" AND FTSOL.STATUS_ID = :STATUS_ID");
				query.append(" AND FTSOL.SITE_ID = :SITE_ID");
		
		//don't move this code to live		
		/*if (status.getDescription().equalsIgnoreCase(Status.CREATED.getDescription())) {//this condition is for emp wise approval system 
			 query = new StringBuilder(SqlQuery.QRY_TO_GET_TRANSACTION_APPROVAL_DETAILS)
						.append(" FROM FIN_TRANSACTION_SET_OFF_APPROVAL_LEVEL FTSOAL")
						.append(" ,FIN_TRANSACTION_SET_OFF_LEVEL FTSOL LEFT OUTER JOIN FIN_TRN_SET_OFF_APPR_TYPE FTSOAT ON  FTSOL.FIN_TRAN_SET_OFF_LEVEL_ID = FTSOAT.FIN_TRAN_SET_OFF_LEVEL_ID ")
						.append(" ,FIN_TRANSACTION_LEVEL FTL")
						.append(" ,FIN_TRN_EMP_SET_OFF_LEVEL_MAPPING FTESOLM")
						.append(" WHERE FTSOL.FIN_TRAN_SET_OFF_LEVEL_ID = FTSOAL.FIN_TRAN_SET_OFF_LEVEL_ID")
						.append(" AND FTL.FIN_LEVEL_ID   = FTSOL.FIN_LEVEL_ID")
						.append(" AND FTSOL.FIN_TRAN_SET_OFF_LEVEL_ID = FTESOLM.FIN_TRAN_SET_OFF_LEVEL_ID")
						.append(" AND FTSOL.FIN_TRANSACTION_TYPE_ID = :FIN_TRANSACTION_TYPE_ID")
						.append(" AND FTSOL.FIN_TRANSACTION_MODE_ID = :FIN_TRANSACTION_MODE_ID")
						.append(" AND FTESOLM.EMP_ID = :EMP_ID");
						query.append(" AND FTSOAL.STATUS_ID = :STATUS_ID");
						query.append(" AND FTSOL.STATUS_ID = :STATUS_ID");
						query.append(" AND FTSOL.SITE_ID = :SITE_ID");
		}*/

		boolean isThisPaymentTransaction = transactionServiceInfo.isThisPaymentTransaction();
		if (isThisPaymentTransaction && !status.getDescription().equalsIgnoreCase("APPROVED")) {
			// checking agreement completed or not if not completed we have to take another
			// flow of approval
			query.append(" AND FTSOL.AGREEMENT_COMPLETED = :AGREEMENT_COMPLETED");
			namedParameters.addValue("AGREEMENT_COMPLETED", MetadataId.FLAT_AGREEMENT_COMPLETED.getId());
			/*if (transactionServiceInfo.isFlatAgreementCompleted()) {
				namedParameters.addValue("AGREEMENT_COMPLETED", MetadataId.FLAT_AGREEMENT_COMPLETED.getId());
			} else {
				namedParameters.addValue("AGREEMENT_COMPLETED", MetadataId.FLAT_AGREEMENT_NOT_COMPLETED.getId());
			}*/
		}

		if (status.getDescription().equalsIgnoreCase(Status.CREATED.getDescription())) {
			//query.append(" AND UPPER(FTL.LEVEL_NAME) = :LEVEL_NAME ");
			query.append(" AND FTSOAT.SET_OFF_TYPE IN (:SET_OFF_TYPE) AND FTSOAT.STATUS_ID = :STATUS_ID ");
			//namedParameters.addValue("LEVEL_NAME", "LEVEL1");
		} else if (Status.MODIFY.getStatus().equals(transactionServiceInfo.getTransactionStatusId()) && status.getDescription().equalsIgnoreCase("APPROVED")) {
			//query.append(" AND UPPER(FTL.LEVEL_NAME) = :LEVEL_NAME ");
			query.append(" AND FTSOAT.SET_OFF_TYPE IN (:SET_OFF_TYPE) AND FTSOAT.STATUS_ID = :STATUS_ID ");
			//namedParameters.addValue("LEVEL_NAME", "LEVEL1");
		} else if (status.getDescription().equalsIgnoreCase("APPROVED")) {
			
		}
		query.append(isNotEmptyObject(finTransactionApprovalDetailsPojo)?" AND FTSOAL.FIN_TRN_SET_OFF_APPR_LVL_ID = :FIN_TRN_SET_OFF_APPR_LVL_ID ":"");
		//.append(" ORDER BY FTESOLM.FIN_TRAN_SET_OFF_LEVEL_ID ")
		query.append(" ORDER BY FTSOL.FIN_LEVEL_ID ").toString();
		
		namedParameters.addValue("FIN_TRANSACTION_TYPE_ID", transactionServiceInfo.getTransactionTypeId(),Types.BIGINT);
		namedParameters.addValue("FIN_TRANSACTION_MODE_ID", transactionServiceInfo.getTransactionModeId(),Types.BIGINT);
		namedParameters.addValue("EMP_ID", transactionServiceInfo.getEmployeeId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID",Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("SITE_ID", transactionServiceInfo.getSiteId(), Types.BIGINT);
		if(transactionServiceInfo.getPaymentSetOffTypes()!=null && transactionServiceInfo.getPaymentSetOffTypes().size()>1) {
			namedParameters.addValue("SET_OFF_TYPE",MetadataId.PRINCIPAL_AMOUNT.getId(), Types.BIGINT);
		}else {
			namedParameters.addValue("SET_OFF_TYPE", transactionServiceInfo.getPaymentSetOffTypes(), Types.BIGINT);
		}
		namedParameters.addValue("FIN_TRN_SET_OFF_APPR_LVL_ID", isNotEmptyObject(finTransactionApprovalDetailsPojo)?finTransactionApprovalDetailsPojo.getTransactionSetOffApprovalLevelId():"0");
		log.info("*****  getNextLevelEmployeeApprovalDetails() ***** Query : \n"+query+"\n"+namedParameters.getValues());
		List<FinTransactionApprovalDetailsPojo> list = (List<FinTransactionApprovalDetailsPojo>) getData(query.toString(), namedParameters, FinTransactionApprovalDetailsPojo.class);
		return list;
	}
	
	@Override
	public List<Map<String, Object>> getNextLevelEmployeeForStatusModule(EmployeeFinancialTransactionRequest empTransReq) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuffer sqlQuery = new StringBuffer("")
		.append(" SELECT DISTINCT EMP.EMP_ID, ")//FTSOL.FIN_TRAN_SET_OFF_LEVEL_ID,
		.append(" NVL((EMP.FIRST_NAME||''||EMP.LAST_NAME),'N/A') AS EMP_NAME,EMP.EMAIL AS EMP_EMAIL ")
		.append(" FROM FIN_TRANSACTION_SET_OFF_APPROVAL_LEVEL FTSOAL ")
		.append(" ,FIN_TRANSACTION_SET_OFF_LEVEL FTSOL ")
		.append(" ,FIN_TRN_EMP_SET_OFF_LEVEL_MAPPING FTESOLM ")
		.append(" ,EMPLOYEE EMP  ")
		.append(" WHERE FTSOL.FIN_TRAN_SET_OFF_LEVEL_ID = FTSOAL.FIN_TRAN_SET_OFF_LEVEL_ID ")
		.append(" AND FTSOL.FIN_TRAN_SET_OFF_LEVEL_ID =  FTESOLM.FIN_TRAN_SET_OFF_LEVEL_ID ")
		.append(" AND EMP.EMP_ID = FTESOLM.EMP_ID ")
		.append(" AND FTSOL.FIN_LEVEL_ID != 1 ")//level other than zero
		//.append(" AND FTSOL.FIN_TRANSACTION_TYPE_ID = :FIN_TRANSACTION_TYPE_ID ")
		//.append(" AND FTSOL.FIN_TRANSACTION_MODE_ID = :FIN_TRANSACTION_MODE_ID ")
	//	.append(" AND FTSOL.SITE_ID = :SITE_ID ")
		.append(" AND FTESOLM.STATUS_ID = :STATUS_ID ")
		.append(" AND FTSOL.STATUS_ID = :STATUS_ID ");
		if(Util.isNotEmptyObject(empTransReq.getSiteId())) {
			sqlQuery.append(" AND FTSOL.SITE_ID IN (:SITE_ID)");
			namedParameters.addValue("SITE_ID",empTransReq.getSiteId(), Types.BIGINT);
		}
		if(Util.isNotEmptyObject(empTransReq.getSiteIds())) {
			sqlQuery.append(" AND FTSOL.SITE_ID IN (:SITE_IDS)");
			namedParameters.addValue("SITE_IDS",empTransReq.getSiteIds(), Types.BIGINT);
		}
		namedParameters.addValue("FIN_TRANSACTION_TYPE_ID", empTransReq.getTransactionTypeId(),Types.BIGINT);
		namedParameters.addValue("FIN_TRANSACTION_MODE_ID", empTransReq.getTransactionModeId(),Types.BIGINT);
		//namedParameters.addValue("EMP_ID", empTransReq.getEmpId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID",Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("SITE_ID", empTransReq.getSiteId(), Types.BIGINT);
		
		List<Map<String, Object>> listOfEmployeeDetails = nmdPJdbcTemplate.queryForList(sqlQuery.toString(), namedParameters);
		
		return listOfEmployeeDetails;
	}
	
	@Override
	public List<FinTransactionApprovalDetailsPojo> getModifyLevelDetails(EmployeeFinancialTransactionServiceInfo transactionServiceInfo,
			FinTransactionApprovalDetailsPojo approvalDetailsPojo) {
	
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder query = new StringBuilder(SqlQuery.QRY_TO_GET_TRANSACTION_APPROVAL_DETAILS)
				.append(" FROM FIN_TRANSACTION_SET_OFF_APPROVAL_LEVEL FTSOAL")
				.append(" ,FIN_TRANSACTION_SET_OFF_LEVEL FTSOL")// LEFT OUTER JOIN FIN_TRN_SET_OFF_APPR_TYPE FTSOAT ON  FTSOL.FIN_TRAN_SET_OFF_LEVEL_ID = FTSOAT.FIN_TRAN_SET_OFF_LEVEL_ID 
				.append(" ,FIN_TRANSACTION_LEVEL FTL")
				// .append(" ,FIN_TRN_EMP_SET_OFF_LEVEL_MAPPING FTESOLM")
				.append(" WHERE FTSOL.FIN_TRAN_SET_OFF_LEVEL_ID = FTSOAL.FIN_TRAN_SET_OFF_LEVEL_ID")
				.append(" AND FTL.FIN_LEVEL_ID   = FTSOL.FIN_LEVEL_ID")
				//.append(" AND FTSOL.FIN_TRAN_SET_OFF_LEVEL_ID =
				//FTESOLM.FIN_TRAN_SET_OFF_LEVEL_ID")
				.append(" AND FTSOL.FIN_TRANSACTION_TYPE_ID = :FIN_TRANSACTION_TYPE_ID")
				.append(" AND FTSOL.FIN_TRANSACTION_MODE_ID = :FIN_TRANSACTION_MODE_ID");
				//.append(" AND FTESOLM.EMP_ID = :EMP_ID")
				query.append(" AND FTSOAL.STATUS_ID = :STATUS_ID");
				query.append(" AND FTSOL.STATUS_ID = :STATUS_ID");
				query.append(" AND FTSOL.SITE_ID = :SITE_ID");
				query.append(" AND FTSOL.FIN_TRAN_SET_OFF_LEVEL_ID = :FIN_TRAN_SET_OFF_LEVEL_ID ");
				
		namedParameters.addValue("FIN_TRANSACTION_TYPE_ID", transactionServiceInfo.getTransactionTypeId(),Types.BIGINT);
		namedParameters.addValue("FIN_TRANSACTION_MODE_ID", transactionServiceInfo.getTransactionModeId(),Types.BIGINT);
		namedParameters.addValue("SITE_ID", transactionServiceInfo.getSiteId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID",Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("FIN_TRAN_SET_OFF_LEVEL_ID",approvalDetailsPojo.getModifyTranSetOffLevelId(), Types.BIGINT);
				
		List<FinTransactionApprovalDetailsPojo> list = (List<FinTransactionApprovalDetailsPojo>) getData(query.toString(), namedParameters, FinTransactionApprovalDetailsPojo.class);
		return list;
	}
	
	@Override
	public List<FinTransactionApprovalDetailsPojo> getTheNextLevelEmplaoyeeDetails(EmployeeFinancialTransactionServiceInfo transactionServiceInfo) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.getTheNextLevelEmplaoyeeDetails() *****");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder query = new StringBuilder(SqlQuery.QRY_TO_GET_TRANSACTION_APPROVAL_EMP_EMAIL_DETAILS);
		// .append(" AND FTESOLM.EMP_ID = :EMP_ID")
		query.append(" AND FTSOAL.STATUS_ID = :STATUS_ID");
		query.append(" AND FTSOL.STATUS_ID = :STATUS_ID").append(" AND FTESOLM.STATUS_ID = :STATUS_ID ");
		
		query.append(" AND FTSOL.SITE_ID = :SITE_ID")
		.append(" AND FTSOAL.FIN_TRN_SET_OFF_APPR_LVL_ID = :FIN_TRN_SET_OFF_APPR_LVL_ID");

		namedParameters.addValue("FIN_TRANSACTION_TYPE_ID", transactionServiceInfo.getTransactionTypeId(),Types.BIGINT);
		namedParameters.addValue("FIN_TRANSACTION_MODE_ID", transactionServiceInfo.getTransactionModeId(),Types.BIGINT);
		namedParameters.addValue("EMP_ID", transactionServiceInfo.getEmployeeId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("SITE_ID", transactionServiceInfo.getSiteId(), Types.BIGINT);
		namedParameters.addValue("FIN_TRN_SET_OFF_APPR_LVL_ID", isNotEmptyObject(transactionServiceInfo.getTransactionSetOffApprovalLevelId()) ? transactionServiceInfo.getTransactionSetOffApprovalLevelId(): "0");

		List<FinTransactionApprovalDetailsPojo> listOfNextApprovalEmaEmails = getData(query.toString(), namedParameters,
				FinTransactionApprovalDetailsPojo.class);
		return listOfNextApprovalEmaEmails;
	}
	
	@Override
	public List<FinTransactionApprovalDetailsPojo> isValidEmployeeToApproveTransaction(EmployeeFinancialTransactionServiceInfo transactionServiceInfo, Status status) {
		//log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.isValidEmployeeToApproveTransaction() ***** ");
		String query = SqlQuery.QRY_TO_GET_TRANSACTION_SET_OFF_APPROVAL_DETAILS;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		if(transactionServiceInfo.getRequestUrl()!=null && transactionServiceInfo.getEmpIdFromUrl()!=null && "approveFromMail".equals(transactionServiceInfo.getRequestUrl())) {
			//if this approval request from email 
			 query = SqlQuery.QRY_TO_GET_TRANSACTION_SET_OFF_APPROVAL_DETAILS_BY_EMPID;
			 namedParameters.addValue("EMP_ID", transactionServiceInfo.getEmpIdFromUrl(),Types.BIGINT);
			 namedParameters.addValue("TRANSACTION_STATUS_ID", new ArrayList<Long>(Arrays.asList(Status.UNCLEARED_CHEQUE.getStatus(),Status.NEW.getStatus(),Status.MODIFY.getStatus(),Status.APPROVED.getStatus(),Status.PENDING.getStatus())));
		} else {
			//if this approval request from Employee portal
			 query = SqlQuery.QRY_TO_GET_TRANSACTION_SET_OFF_APPROVAL_DETAILS;
			 namedParameters.addValue("EMP_ID", transactionServiceInfo.getEmployeeId(),Types.BIGINT);
		}
		
		namedParameters.addValue("FIN_TRN_SET_OFF_ENT_ID", transactionServiceInfo.getTransactionSetOffEntryId(),Types.BIGINT);
		
		if(status!=null && status.getDescription().equalsIgnoreCase(Status.CREATED.getDescription())) {
			//if this is modify transaction or edit transaction, taking prev transaction id
			namedParameters.addValue("FIN_TRANSACTION_ENTRY_ID", transactionServiceInfo.getPrevTransactionEntryId(),Types.BIGINT);	
		} else {
			namedParameters.addValue("FIN_TRANSACTION_ENTRY_ID", transactionServiceInfo.getTransactionEntryId(),Types.BIGINT);
		}
		List<FinTransactionApprovalDetailsPojo> list = (List<FinTransactionApprovalDetailsPojo>) getData(query, namedParameters, FinTransactionApprovalDetailsPojo.class);
		return list;
	}
	
	@Override
	public boolean isValidModificationInvoiceToApprove(EmployeeFinancialTransactionServiceInfo transactionServiceInfo,
			Status approved) {
	
		return false;
	}
	
	@Override
	public Long saveNextLeveTransactionApprovalDetails(FinTransactionSetOffApprovalPojo pojo) {
		//log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.saveNextLeveTransactionApprovalDetails() ***** ");
		String query = SqlQuery.QRY_TO_INSERT_TRANSACTION_APPROVAL_DETAILS;
		Long pk = commonMethodToInsertData(query, pojo, "FIN_TRN_SET_OFF_APPR_ID");
		pojo.setTransactionSetOffApprovalId(pk);
		return pk;
	}
	
	@Override
	public Long saveApproveRejectStatistics(FinTransactionApprRejectStatPojo pojo) {
		//log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.saveApproveRejectStatistics() ***** ");
		pojo.setCreatedDate(new Timestamp(new Date().getTime()));
		String query = SqlQuery.QRY_TO_INSERT_TRANSACTION_APPROVAL_REJECT_DETAILS;
		Long pk = commonMethodToInsertData(query, pojo, "FIN_TRN_APPR_STAT_ID");
		return pk;
	}
	
	@Override
	public int updateNextLevelTransactionApprovalDetails(FinTransactionSetOffApprovalPojo pojo) {
		//log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.updateNextLevelTransactionApprovalDetails() ***** ");
		String query = SqlQuery.QRY_TO_UPDATE_TRANSACTION_APPROVAL_DETAILS;
		return commonMethodToUpdateData(query, pojo);
	}
	
	@Override
	public List<FinTransactionEntryDetailsPojo> getFinancialTransactionPaymentSetOffData(EmployeeFinancialTransactionServiceInfo transactionServiceInfo) {
		log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.getFinancialTransactionPaymentSetOffData() ***** ");
		StringBuilder query = new StringBuilder(SqlQuery.QRY_TO_GET_FIN_TRANSACTION_SET_OFF_BY_ENTRY_ID)
				.append(" WHERE FTSOE.FIN_TRANSACTION_ENTRY_ID=:FIN_TRANSACTION_ENTRY_ID ")
				.append(" AND FTSO.STATUS_ID = :STATUS_ID")
				.append(" ORDER BY FTSOAM.FIN_TRN_SET_OFF_ACC_MAP_ID ");
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID",Status.ACTIVE.getStatus(),Types.BIGINT);
		namedParameters.addValue("FIN_TRANSACTION_ENTRY_ID",transactionServiceInfo.getTransactionEntryId(),Types.BIGINT);
		
		
		List<FinTransactionEntryDetailsPojo> list = (List<FinTransactionEntryDetailsPojo>) getData(query.toString(), namedParameters, FinTransactionEntryDetailsPojo.class);
		return list;
	}
	
	@Override
	public List<FinTransactionEntryDetailsPojo> getFinancialTransactionPaymentRefundSetOffData(EmployeeFinancialTransactionServiceInfo transactionServiceInfo) {
		//log.info(" ***** Control inside EmployeeFinancialServiceDaoImpl.getFinancialTransactionPaymentRefundSetOffData() ***** ");
		StringBuilder query = new StringBuilder(SqlQuery.QRY_TO_GET_FIN_TRANSACTION_SET_OFF_PAYMENT_BY_ENTRY_ID)
				.append(" WHERE FTSOE.FIN_TRANSACTION_ENTRY_ID=:FIN_TRANSACTION_ENTRY_ID ")
				.append(" AND FTPSOAM.STATUS_ID = :STATUS_ID")
				.append(" ORDER BY FTPSOAM.FIN_TRN_PMT_SET_OFF_ACC_MAP_ID ");

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("FIN_TRANSACTION_ENTRY_ID", transactionServiceInfo.getTransactionEntryId(), Types.BIGINT);

		
		List<FinTransactionEntryDetailsPojo> list = (List<FinTransactionEntryDetailsPojo>) getData(query.toString(), namedParameters, FinTransactionEntryDetailsPojo.class);
		return list;
	}

	@Override
	public Long saveTransactionSetOffEntryDetails(FinTransactionSetOffEntryPojo pojo) {
		log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.saveTransactionSetOffEntryDetails() ***** ");
		String query = SqlQuery.QRY_TO_INSERT_TRANSACTION_SET_OFF_ENTRY_DETAILS;
	 	Long pk = commonMethodToInsertData(query, pojo,"FIN_TRN_SET_OFF_ENT_ID" ); 
		return pk;
	}
	
	
	
//	@Override
//	public List<FinModificationTaxPojo> getModificationChargesTaxDetails() {
//		log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.getModificationChargesTaxDetails() ***** ");
// 
//	 	String query = new StringBuffer( SqlQuery.QRY_TO_GET_FIN_MODIFICATION_TAX_PERCENTAGE)
//	 			.append(" WHERE PERCENTAGE.PERCENTAGE_ID = FPT.PERCENTAGE_ID ")
//				//.append(" AND TRUNC(:MILE_STONE_DATE) BETWEEN TRUNC(FPT.START_DATE) AND TRUNC(FPT.END_DATE)")
//				.append(" AND FMT.STATUS_ID=:STATUS_ID ")
//				.append(" ORDER BY FMT.FIN_MODI_TAX_ID")
//				.toString();
//	 	
//		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
//		namedParameters.addValue("STATUS_ID",Status.ACTIVE.getStatus(),Types.BIGINT);
//		
//		
//		List<FinModificationTaxPojo> list = (List<FinModificationTaxPojo>) getData(query, namedParameters, FinModificationTaxPojo.class);
//		return list;
//	}
	
	@Override
	public Long saveBookingFormModificationCost(FinBookingFormModiCostPojo pojo) {
		log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.saveBookingFormModificationCost() ***** ");
		String query = SqlQuery.QRY_TO_INSERT_FIN_BOOKING_FORM_MODI_COST;
	 	Long pk = commonMethodToInsertData(query, pojo,"FIN_BOK_FRM_MODI_COST_ID" ); 
	 	pojo.setFinBookingFormModiCostId(pk);
		return pk;
	}
	
	@Override
	public Long saveBookingFormModiCostTax(FinBookingFormModiCostTaxPojo pojo) {
		log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.saveBookingFormModiCostTax() ***** ");
		String query = SqlQuery.QRY_TO_INSERT_FIN_BOOKING_FORM_MODI_COST_TAX;
	 	Long pk = commonMethodToInsertData(query, pojo,"FIN_BOK_FRM_MODI_COST_TAX_ID" ); 
		return pk; 
	}
	
	@Override
	public Long saveBookingFormModificationCostDetails(FinBookingFormModiCostDtlsPojo pojo) {
		log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.saveBookingFormModificationCostDetails() *****");
		String query = SqlQuery.QRY_TO_INSERT_FIN_BOOKING_FORM_MODI_COST_DTLS;
	 	Long pk = commonMethodToInsertData(query, pojo,"FIN_BOK_MODI_COST_DTLS_ID" ); 
		return pk;
	}
	
	@Override
	public Long saveTransactionChequeDetials(FinTransactionChequePojo pojo) {
		log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.saveTransactionChequeDetials() *****");
		String query = SqlQuery.QRY_TO_INSERT_TRANSACTION_CHEQUE_DETAILS;
		Long pk = commonMethodToInsertData(query, pojo,  "FIN_TRANSACTION_CHEQUE_ID" ); 
		return pk;
	}
	@Override
	public Long saveTransactionWaivedOffDetials(FinTransactionWaivedOffPojo pojo) {
		log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.saveTransactionWaivedOffDetials() *****");
		String query = FinancialQuerys.QRY_TO_INSERT_TRANSACTION_WAIVED_OFF;
		Long pk = commonMethodToInsertData(query, pojo,  "FIN_TRANSACTION_WAIVED_OFF_ID" ); 
		return pk;
	}
	@Override
	public Long saveTransactionOnlineDetails(FinTransactionOnlinePojo pojo) {
		log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.saveTransactionOnlineDetails() *****");
		String query = SqlQuery.QRY_TO_INSERT_TRANSACTION_ONLINE_DETAILS;
	 	Long pk = commonMethodToInsertData(query, pojo,  "FIN_TRANSACTION_ONLINE_ID" ); 
		return pk;
	}
	
	@Override
	public int updateTransactionOnlineDetails(FinTransactionOnlinePojo pojo) {
		log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.updateTransactionOnlineDetails() *****");
		String query = SqlQuery.QRY_TO_UPDATE_TRANSACTION_ONLINE_DETAILS;
		return commonMethodToUpdateData(query, pojo);
	}
	
	@Override
	public Long saveTransactionAnonymousEntryMapping(FinAnonymousEntryMapPojo pojo) {
		log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.saveTransactionAnonymousEntryMapping() ***** ");
		String query = SqlQuery.QRY_TO_INSERT_ANONYMOUS_ENTRY_MAP_DETAILS;
	 	Long pk = commonMethodToInsertData(query, pojo,  "FIN_TRAN_ANMS_ENTRY_MAP_ID" ); 
		return pk;
	}
	
	@Override
	public List<FinAnonymousEntryPojo> getOnlinePaymentRererenceNumber(EmployeeFinancialTransactionServiceInfo transactionServiceInfo) throws Exception {
		//log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.isOnlinePaymentRererenceNumber() ***** ");
		String query = SqlQuery.QRY_TO_CHECK_TRANSACTION_ONLINE_DETAILS;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("REFERENCE_NUMBER", transactionServiceInfo.getReferenceNo(),Types.VARCHAR);
		namedParameters.addValue("SITE_ID", transactionServiceInfo.getSiteId(),Types.BIGINT);
		if(transactionServiceInfo.isThisIsModifyTransaction() || transactionServiceInfo.isThisEditTransaction() 
				|| transactionServiceInfo.isThisDeleteTransaction() || transactionServiceInfo.isThisShiftTransaction()) {
			namedParameters.addValue("STATUS_ID", Status.TRANSACTION_COMPLETED.getStatus(),Types.BIGINT);
		} else {
			namedParameters.addValue("STATUS_ID", transactionServiceInfo.getStatusId(),Types.BIGINT);
		}
		if(Util.isEmptyObject(transactionServiceInfo.getSiteId())) {
			throw new EmployeeFinancialServiceException("Site id missing while inserting Transaction");
		}
		List<FinAnonymousEntryPojo> list = (List<FinAnonymousEntryPojo>) getData(query, namedParameters,FinAnonymousEntryPojo.class); 
		return list;
	}
	
	
	@Override
	public boolean isReferenceNoIsExist(EmployeeFinancialTransactionServiceInfo transactionServiceInfo) throws EmployeeFinancialServiceException {
		//log.info("EmployeeFinancialServiceDaoImpl.isReferenceNoIsExist()");
		boolean flag = false;
		String query = SqlQuery.QRY_TO_COUNT_ANONYMOUS_ENTRY_DETAILS;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		if(transactionServiceInfo.isThisUplaodedData()) {
			namedParameters.addValue("STATUS_ID",Arrays.asList(new Long[] {Status.ACTIVE.getStatus()}),Types.BIGINT);
		} else {
			namedParameters.addValue("STATUS_ID",Arrays.asList(new Long[] {Status.ACTIVE.getStatus(),Status.TRANSACTION_COMPLETED.getStatus()}),Types.BIGINT);
		}
		namedParameters.addValue("SITE_ID",transactionServiceInfo.getSiteId(),Types.BIGINT);
		namedParameters.addValue("REFERENCE_NUMBER",transactionServiceInfo.getReferenceNo(),Types.VARCHAR);
		if(Util.isEmptyObject(transactionServiceInfo.getSiteId())) {
			throw new EmployeeFinancialServiceException("Site id missing while inserting Transaction");
		}
		try {
			flag = nmdPJdbcTemplate.queryForObject(query, namedParameters, Boolean.class);
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return flag;
	}
	
	@Override
	public boolean isReferenceNoIsExistById(EmployeeFinancialTransactionServiceInfo transactionServiceInfo) throws Exception {
		//log.info("EmployeeFinancialServiceDaoImpl.isReferenceNoIsExistById()");
		boolean flag = false;
		String query = SqlQuery.QRY_TO_COUNT_ANONYMOUS_ENTRY_DETAILS_BY_ID;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		
		namedParameters.addValue("STATUS_ID",Arrays.asList(new Long[] {Status.MODIFY.getStatus(),Status.ACTIVE.getStatus(),Status.TRANSACTION_COMPLETED.getStatus()}),Types.BIGINT);
		namedParameters.addValue("SITE_ID",transactionServiceInfo.getSiteId(),Types.BIGINT);
		namedParameters.addValue("FIN_ANMS_ENTRY_ID",transactionServiceInfo.getAnonymousEntryId(),Types.BIGINT);
		namedParameters.addValue("REFERENCE_NUMBER",transactionServiceInfo.getReferenceNo(),Types.VARCHAR);
		if(Util.isEmptyObject(transactionServiceInfo.getSiteId())) {
			throw new EmployeeFinancialServiceException("Site id missing while inserting Transaction");
		}
		try {
			flag = nmdPJdbcTemplate.queryForObject(query, namedParameters, Boolean.class);
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return flag;
	}
	
	@Override
	public List<Map<String, Object>> isReferenceNoIsExistForUploadData(List<String> holdOnlineReferenceNumber, Long siteId) throws EmployeeFinancialServiceException {

		String query = SqlQuery.QRY_TO_COUNT_ANONYMOUS_ENTRY_DETAILS_FOR_UPLOAD_DATA;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("SITE_ID",siteId,Types.BIGINT);
		namedParameters.addValue("STATUS_ID",Arrays.asList(new Long[] {Status.ACTIVE.getStatus(),Status.TRANSACTION_COMPLETED.getStatus()}),Types.BIGINT);
		namedParameters.addValue("REFERENCE_NUMBER",holdOnlineReferenceNumber,Types.VARCHAR);
		if(Util.isEmptyObject(siteId)) {
			throw new EmployeeFinancialServiceException("Site id missing while inserting Transaction");
		}
		List<Map<String, Object>> list = nmdPJdbcTemplate.queryForList(query, namedParameters);
		return list;
	}
	
	@Override
	public List<FinAnonymousEntryPojo> getCompletedTrnOnlinePaymentRererenceNumber(
			EmployeeFinancialTransactionServiceInfo transactionServiceInfo) {

		//log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.isOnlinePaymentRererenceNumber() ***** ");
		String query = SqlQuery.QRY_TO_CHECK_TRANSACTION_ONLINE_DETAILS1;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("REFERENCE_NUMBER", transactionServiceInfo.getReferenceNo(),Types.VARCHAR);
		namedParameters.addValue("SITE_ID", transactionServiceInfo.getSiteId(),Types.BIGINT);
		namedParameters.addValue("FIN_TRANSACTION_ENTRY_ID", transactionServiceInfo.getTransactionEntryId(),Types.BIGINT);
		if(transactionServiceInfo.isThisIsModifyTransaction() || transactionServiceInfo.isThisEditTransaction() || transactionServiceInfo.isThisDeleteTransaction() || transactionServiceInfo.isThisShiftTransaction()) {
			namedParameters.addValue("STATUS_ID", Status.TRANSACTION_COMPLETED.getStatus(),Types.BIGINT);
		}else {
			namedParameters.addValue("STATUS_ID", transactionServiceInfo.getStatusId(),Types.BIGINT);
		}
		
		List<FinAnonymousEntryPojo> list = (List<FinAnonymousEntryPojo>) getData(query, namedParameters,FinAnonymousEntryPojo.class); 
		return list;
	}
	
	@Override
	public int updateAnonymousEntryStatus(FinAnonymousEntryPojo finAnonymousEntryPojo) {
		log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.updateAnonymousEntryStatus() ***** ");
		String query = SqlQuery.QRY_TO_ANONYMOUS_TRANSACTION_ONLINE_DETAILS;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		/*namedParameters.addValue("statusId", Status.TRANSACTION_COMPLETED.getStatus(), Types.BIGINT);
		namedParameters.addValue("activeStatusId", Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("anonymousEntryId", finAnonymousEntryPojo.getAnonymousEntryId(), Types.BIGINT);
		*/
		namedParameters.addValue("statusId",  finAnonymousEntryPojo.getStatusId(), Types.BIGINT);
		namedParameters.addValue("activeStatusId", finAnonymousEntryPojo.getActiveStatusId(), Types.BIGINT);
		namedParameters.addValue("anonymousEntryId", finAnonymousEntryPojo.getAnonymousEntryId(), Types.BIGINT);
		int result = commonMethodToUpdateData(query, namedParameters);
		return result;
	}
	
	@Override
	public Long saveTransactionSetOffDetails(FinTransactionSetOffPojo pojo) {
		log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.saveTransactionSetOffDetails() ***** ");
		String query = SqlQuery.QRY_TO_INSERT_TRANSACTION_PAYMENT_SET_OFF_DETAILS;
	 	Long pk = commonMethodToInsertData(query, pojo, "FIN_TRN_SET_OFF_ID" ); 
		return pk;
	}
	
	@Override
	public int updateTransactionSetOffDetails(FinTransactionSetOffPojo pojo,Long setOffRecordStatus) {
		log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.updateTransactionSetOffDetails() ***** ");
		String query = SqlQuery.QRY_TO_UPDATE_TRANSACTION_PAYMENT_SET_OFF_DETAILS;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("finTransactionSetOffId",pojo.getFinTransactionSetOffId(),Types.BIGINT);
		namedParameters.addValue("setOffRecordStatus",setOffRecordStatus,Types.BIGINT);
		namedParameters.addValue("finBokAccInvoiceNo",pojo.getFinBokAccInvoiceNo(),Types.VARCHAR);
		namedParameters.addValue("transactionSetOffEntryId", pojo.getTransactionSetOffEntryId(),Types.BIGINT);
		namedParameters.addValue("statusId",Status.ACTIVE.getStatus(),Types.BIGINT);
		namedParameters.addValue("paidById",pojo.getPaidById(),Types.BIGINT);
		namedParameters.addValue("setOffAmount", pojo.getSetOffAmount(),Types.DOUBLE);
		//log.info(namedParameters.getValues());
		int result = commonMethodToUpdateData(query, namedParameters);
		return result;
	}
	
	@Override
	public int updateTransactionSetOffGSTDetails(FinTransactionSetOffPojo setOffPojo) {
		String query = SqlQuery.QRY_TO_UPDATE_TRANSACTION_PAYMENT_SET_OFF_GST_DETAILS;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("transactionSetOffEntryId", setOffPojo.getTransactionSetOffEntryId(), Types.BIGINT);
		namedParameters.addValue("setOffType", setOffPojo.getSetOffType(), Types.BIGINT);
		namedParameters.addValue("statusId", Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("setOffGstAmount", setOffPojo.getSetOffGstAmount(), Types.DOUBLE);
		int result = nmdPJdbcTemplate.update(query, namedParameters);
		return result;
	}
	
	@Override
	public Long saveTranSetOffAccountDetails(FinTransactionSetOffAccMapPojo pojo) throws EmployeeFinancialServiceException {
		log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.saveTranSetOffAccountDetails() ***** ");
		String query = "";
		if(Util.isNotEmptyObject(pojo.getFinBookingFormAccountsId())) {
		 query = SqlQuery.QRY_TO_GET_TRANSACTION_SET_OFF_ACC_DETAILS;
		 	
		 	List<FinTransactionSetOffAccMapPojo> setOffPojos = (List<FinTransactionSetOffAccMapPojo>) getData(query,pojo, FinTransactionSetOffAccMapPojo.class);
			if (!setOffPojos.isEmpty()) {
				FinTransactionSetOffAccMapPojo setOffAccPojo = setOffPojos.get(0);
				setOffAccPojo.setMsWiseAmountToSetOff(pojo.getMsWiseAmountToSetOff());
				setOffAccPojo.setActiveStatusId(Status.ACTIVE.getStatus());
				//updating amount if any changes happened
				nmdPJdbcTemplate.update(SqlQuery.QRY_TO_UPDATE_TRANSACTION_SET_OFF_ACC_DETAILS_AMT, new BeanPropertySqlParameterSource(setOffAccPojo));
				return setOffAccPojo.getFinTransactionSetOffAccMapId();
			}
		}else {
			throw new  EmployeeFinancialServiceException("Transaction set off payment data missing.");
		}
		query = SqlQuery.QRY_TO_INSERT_TRANSACTION_SET_OFF_ACC_DETAILS;
		return commonMethodToInsertData(query,pojo,"FIN_TRN_SET_OFF_ACC_MAP_ID");
	}

	@Override
	public int updateTranSetOffAccountDetails(List<Long> listOfTransactionSetOffAccMap,EmployeeFinTranPaymentSetOffInfo paymentSetOffDetails) {
		log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.updateTranSetOffAccountDetails()");
		String query = SqlQuery.QRY_TO_UPDATE_TRANSACTION_SET_OFF_ACC_DETAILS_INACTIVE;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("statusId", Status.INACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("finTransactionSetOffAccMapId", listOfTransactionSetOffAccMap);
		namedParameters.addValue("finTransactionSetOffId", paymentSetOffDetails.getFinTransactionSetOffId(), Types.BIGINT);
		namedParameters.addValue("activeStatusId", Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("modifiedBy", paymentSetOffDetails.getCreatedBy(), Types.BIGINT);
		
		int result = commonMethodToUpdateData(query, namedParameters);
		return result;
	}
	
	@Override
	public Long saveTranPaymentSetOffAccountDetails(FinTransactionPmtSetOffAccMapPojo pojo) throws EmployeeFinancialServiceException {
		log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.saveTranPaymentSetOffAccountDetails() ***** ");
		if(Util.isNotEmptyObject(pojo.getTypeId())) {//checking is already these type id(means milestone id or corspus, maintenance id etc... already exists or not)
			String query = SqlQuery.QRY_TO_COUNT_TRANSACTION_PAYMENT_SET_OFF_ACC_DETAILS;
			
			List<FinTransactionPmtSetOffAccMapPojo> setOffPojos = (List<FinTransactionPmtSetOffAccMapPojo>) getData(query,pojo, FinTransactionPmtSetOffAccMapPojo.class);
			if(!setOffPojos.isEmpty()) {//if found record then return primary key
				return setOffPojos.get(0).getFinTrnPaymentSetOffAccMapId();
			}
		} else {
			throw new  EmployeeFinancialServiceException("Transaction set off payment data missing.");
		}
		String query = SqlQuery.QRY_TO_INSERT_TRANSACTION_PAYMENT_SET_OFF_ACC_DETAILS;
		return commonMethodToInsertData(query,pojo,"FIN_TRN_PMT_SET_OFF_ACC_MAP_ID");
	}

	@Override
	public int updateTranPaymentSetOffAccountDetails(List<Long> listOfTransactionPaymentSetOffMap,
			EmployeeFinTranPaymentSetOffInfo paymentSetOffDetails) {
		log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.updateTranPaymentSetOffAccountDetails()");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = SqlQuery.QRY_TO_UPDATE_TRANSACTION_PAYMENT_SET_OFF_ACC_DETAILS_INACTIVE;
		namedParameters.addValue("statusId", Status.INACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("finTrnPaymentSetOffAccMapId", listOfTransactionPaymentSetOffMap);
		namedParameters.addValue("finTransactionSetOffId", paymentSetOffDetails.getFinTransactionSetOffId(), Types.BIGINT);
		namedParameters.addValue("activeStatusId", Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("modifiedBy", paymentSetOffDetails.getCreatedBy(), Types.BIGINT);
		int result = commonMethodToUpdateData(query, namedParameters);
		return result;
	}
	
	@Override
	public Long saveBookingFormAccountPayments(FinBookingFormAccountPaymentPojo pojo) {
		log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.saveBookingFormAccountPayments()");
		String query = SqlQuery.QRY_TO_INSERT_BOOKING_FORM_ACCOUNT_PAYMENT;
		Long pk = commonMethodToInsertData(query, pojo, "FIN_BOK_FRM_ACC_PMT_ID" ); 
		pojo.setBookingFormAccountPaymentId(pk);
		return pk;
	}

	@Override
	public Long saveBookingFormAccountPaymentsDetails(FinBookingFormAccountPaymentPojo pojo) {
		log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.saveBookingFormAccountPaymentsDetails()");
		String query = SqlQuery.QRY_TO_INSERT_BOOKING_FORM_ACCOUNT_PAYMENT_DETAILS;
		Long pk = commonMethodToInsertData(query, pojo, "FIN_BOK_FRM_ACC_PMT_DTLS_ID"); 
		return pk;
	}
	
	@Override
	public int updateBookingFormAccountPayments(FinBookingFormAccountPaymentPojo pojo) {
		//System.out.println("***** The Control is inside the EmployeeFinancialServiceDaoImpl.updateBookingFormAccountPayments()");
		String query = SqlQuery.QRY_TO_UPDATE_BOOKING_FORM_ACCOUNT_PAYMENT;
		int result = commonMethodToUpdateData(query, pojo);
		return result;
	}
	
	@Override
	public Long saveTransactionEntryDocuments(FinTransactionEntryDocPojo pojo) {
		log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.saveTransactionEntryDocuments() ***** ");
		Long pk = null;
		if(pojo.getSaveFiles().equals(false))
		{
			return pk;
		}
		String query = SqlQuery.QRY_TO_INSERT_TRANSACTION_DOCS_DETAILS;
	 	pk = commonMethodToInsertData(query, pojo, "FIN_TRANSACTION_ENTRY_DOC_ID" );
		return pk;
	}
	
	@Override
	public Long saveAnonymousEntryDocuments(FinAnnyEntryDocPojo pojo) {
		log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.saveAnonymousEntryDocuments()");
		String query = SqlQuery.QRY_TO_INSERT_FIN_ANNY_ENTRY_DOC;
		Long pk = commonMethodToInsertData(query, pojo, "FIN_ANNY_ENTRY_DOC_ID" );
		return pk;
	}
	
	@Override
	public int deleteAnonymousEntryDocuments(FileInfo fileInfo) {
		String query = SqlQuery.QRY_TO_INACTIVE_FIN_ANNY_ENTRY_DOC;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FIN_ANNY_ENTRY_DOC_ID", fileInfo.getId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.INACTIVE.getStatus(), Types.BIGINT);
		int result = nmdPJdbcTemplate.update(query, namedParameters);
		return result;
	}
	
	@Override
	public Long saveAnonymousApproveStatistics(FinAnnyApproveStatPojo pojo) {
		log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.saveAnonymousApproveStatistics()  ***** ");
		String query = SqlQuery.QRY_TO_INSERT_FIN_ANNY_APPROVE_STAT;
		Long pk = commonMethodToInsertData(query, pojo, "FIN_ANNY_APPR_STAT_ID" );
		return pk;
	}
	
	@Override
	public Long saveTransactionEntryComments(FinTransactionCommentsPojo pojo) {
		log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.saveTransactionEntryComments() ***** ");
		String query = SqlQuery.QRY_TO_INSERT_TRANSACTION_COMMENT_DETAILS;
		Long pk = commonMethodToInsertData(query, pojo, "FIN_TRANSACTION_COMMENT_ID" );
		return pk;
	}
	
	@Override
	public Long saveAnonymousEntryComments(FinAnnyEntryCommentsPojo pojo) {
		log.info("***** The Control is inside the EmployeeinancialServiceDaoImpl.saveAnonymousEntryComments()");
		String query = SqlQuery.QRY_TO_INSERT_FIN_ANNY_ENTRY_COMMENTS;
		Long pk = commonMethodToInsertData(query, pojo, "FIN_ANNY_ENTRY_COMMENTS_ID" );
		return pk;	
	}
	
	@Override
	public List<FinPenaltyPojo> getLastInterestDateToCompare(FinancialProjectMileStonePojo finProjMileStonePojo) {
		//log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.getLastInterestDateToCompare() ***** ");
		String query = new StringBuffer(SqlQuery.QRY_TO_GET_PENALTY_DETAILS).toString();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FIN_BOOKING_FORM_ACCOUNTS_ID", finProjMileStonePojo.getFinBookingFormAccountsId(),Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(),Types.BIGINT);
		
		
		List<FinPenaltyPojo> penaltyList = (List<FinPenaltyPojo>) getData(query, namedParameters, FinPenaltyPojo.class);
		return penaltyList;
	}
	
	@Override
	public List<FinPenaltyPojo> getLastInterestDateToFromMasterTB(FinancialProjectMileStoneInfo mileStoneInfo) {
		String query = new StringBuffer(SqlQuery.QRY_TO_GET_MAX_FIN_INTEREST_RATES).toString();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("SITE_ID", mileStoneInfo.getSiteId(),Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(),Types.BIGINT);
		List<FinPenaltyPojo> penaltyMaxEndDateList = getData(query, namedParameters, FinPenaltyPojo.class);
		return penaltyMaxEndDateList;
	}

	@Override
	public List<FinInterestRatesPojo> getInterestDetailsOnDue(FinancialProjectMileStoneInfo finProjDemandNoteInfo) {
	//log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.getInterestDetailsOnDue() ***** ");
	String query = new StringBuffer( SqlQuery.QRY_TO_GET_FIN_INTEREST_RATES)
 			//.append(" AND FIR.START_DATE  BETWEEN :START_DATE AND  :END_DATE")
 			.append(" AND FIR.STATUS_ID=:STATUS_ID")
 			.append(" AND  FIR.FIN_INTEREST_RATES_ID ")
 			.append("BETWEEN (SELECT FIN_INTEREST_RATES_ID FROM FIN_INTEREST_RATES WHERE SITE_ID IN (:SITE_ID) AND STATUS_ID=:STATUS_ID AND TRUNC(:START_DATE) BETWEEN  TRUNC(START_DATE) AND TRUNC(END_DATE))")
 			.append(" AND    (SELECT FIN_INTEREST_RATES_ID FROM FIN_INTEREST_RATES WHERE SITE_ID IN (:SITE_ID) AND STATUS_ID=:STATUS_ID AND TRUNC(:END_DATE)   BETWEEN  TRUNC(START_DATE) AND TRUNC(END_DATE))")
 			.append(" AND FIR.SITE_ID IN (:SITE_ID) ")
 			.append(" ORDER BY FIR.FIN_INTEREST_RATES_ID")
 			.toString();
 		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("START_DATE", finProjDemandNoteInfo.getMileStoneDueDate(), Types.TIMESTAMP);
		namedParameters.addValue("END_DATE", finProjDemandNoteInfo.getMileStonePaidDate(), Types.TIMESTAMP);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("SITE_ID", finProjDemandNoteInfo.getSiteId(), Types.BIGINT);

		List<FinInterestRatesPojo> finTaxDetailsList = getData(query, namedParameters, FinInterestRatesPojo.class);
		
		return finTaxDetailsList;
	}
	
	@Override
	public List<Map<String, Object>> getSiteIdsOfRateOfInterest() {
		String query = new StringBuffer("SELECT DISTINCT FIR.SITE_ID,SI.NAME AS SITE_NAME FROM FIN_INTEREST_RATES FIR,SITE SI WHERE FIR.SITE_ID = SI.SITE_ID AND  FIR.STATUS_ID=:STATUS_ID ").toString();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		List<Map<String, Object>> listOfSiteIds = nmdPJdbcTemplate.queryForList(query, namedParameters);
		return listOfSiteIds;
	}
	
	@Override
	public List<FinInterestRatesPojo> getAvailbleMonthInterestDetailsOnDue(FinancialProjectMileStoneInfo finProjDemandNoteInfo) {
		String query = new StringBuffer( SqlQuery.QRY_TO_GET_FIN_INTEREST_RATES)
	 			//.append(" AND FIR.START_DATE  BETWEEN :START_DATE AND  :END_DATE")
	 			.append(" AND FIR.STATUS_ID=:STATUS_ID")
	 			.append(" AND FIR.FIN_INTEREST_RATES_ID ")
	 			.append(" IN (SELECT MAX(FIN_INTEREST_RATES_ID) FROM FIN_INTEREST_RATES WHERE SITE_ID IN (:SITE_ID) AND STATUS_ID=:STATUS_ID )")
	 			.append(" AND FIR.SITE_ID IN (:SITE_ID) ")
	 			.append(" ORDER BY FIR.FIN_INTEREST_RATES_ID")
	 			.toString();
	 			 	
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			//namedParameters.addValue("START_DATE", finProjDemandNoteInfo.getMileStoneDueDate(), Types.TIMESTAMP);
			//namedParameters.addValue("END_DATE", finProjDemandNoteInfo.getMileStonePaidDate(), Types.TIMESTAMP);
			namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
			namedParameters.addValue("SITE_ID", finProjDemandNoteInfo.getSiteId(), Types.BIGINT);

			List<FinInterestRatesPojo> finTaxDetailsList = getData(query, namedParameters, FinInterestRatesPojo.class);
			
			return finTaxDetailsList;
	}
	
	@Override
	public boolean checkTheDateBetweenTheDates(FinancialProjectMileStoneInfo mileStoneInfo,
			FinInterestRatesPojo finInterestRatesPojo) {
		boolean flag = false;
		String query = new StringBuffer(SqlQuery.QRY_TO_GET_CHECK_DATE_BETWEEN_FIN_INTEREST_RATES)
				//.append(" SELECT 1 FROM DUAL WHERE :DATE_TO_CHECK BETWEEN :START_DATE AND :END_DATE")
				.toString();
	 	MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("START_DATE", finInterestRatesPojo.getStartDate(),Types.DATE);
		namedParameters.addValue("END_DATE", finInterestRatesPojo.getEndDate(),Types.DATE);
		namedParameters.addValue("DATE_TO_CHECK", mileStoneInfo.getMileStoneDueDate(),Types.DATE);
		try {
			flag  = nmdPJdbcTemplate.queryForObject(query, namedParameters, Boolean.class);	
		} catch (Exception e) {
			log.info(e.getMessage());
		 
		}
		return flag;
	}
	
	@Override
	public String isThisPenaltyDataExist(FinPenalityInfo finPenalityInfo) {
		//log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.isThisPenaltyDataExist() ***** ");
		//SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		//String startDate = dateFormat.format(finPenalityInfo.getStartDate());
		//String endDate1 = dateFormat.format(finPenalityInfo.getEndDate());

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("START_DATE",finPenalityInfo.getStartDate(),Types.TIMESTAMP);
		namedParameters.addValue("END_DATE",finPenalityInfo.getEndDate(),Types.TIMESTAMP);
		namedParameters.addValue("FIN_BOOKING_FORM_ACCOUNTS_ID", finPenalityInfo.getFinBookingFormAccountsId(),Types.BIGINT);
		namedParameters.addValue("STATUS_ID",Status.ACTIVE.getStatus(),Types.BIGINT);
		String query = FinancialQuerys.QRY_TO_CHECK_FIN_PENALTY_STATISTICS_DATA;
		String endDate = "";
		try {
			endDate = nmdPJdbcTemplate.queryForObject(query,namedParameters,String.class);
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return endDate;
	}
	
	@Override
	public Long savePenaltyTaxMappingDetails(FinPenaltyTaxMappingPojo pojo) {
		log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.savePenaltyTaxMappingDetails() ***** ");
		//GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		String query = SqlQuery.QRY_TO_INSERT_FIN_PENALTY_TAX_MAPPING;
		Long pk = commonMethodToInsertData(query, pojo,  "FIN_PEN_TAX_MAP_ID" ); 
		return pk;
	}
	
	@Override
	public Double getInitiatedInterestWaiverDetails(CustomerPropertyDetailsInfo flatInfo,String requestUrl) {
		//this method will return the total interest waiver amount, pending to approve
		StringBuffer query = null;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		Double amount = 0.0;
		if (requestUrl.equals("InterestWaiverRequest")) {//loading the data based on date and completed transaction
			query = new StringBuffer("SELECT NVL(SUM(AMOUNT),0) as SET_OFF_AMOUNT FROM FIN_TRANSACTION_ENTRY FTR WHERE FTR.BOOKING_FORM_ID = :BOOKING_FORM_ID AND FTR.FIN_TRANSACTION_TYPE_ID = :FIN_TRANSACTION_TYPE_ID AND FTR.TRANSACTION_STATUS_ID IN (:TRANSACTION_STATUS_ID) AND FTR.STATUS_ID = :STATUS_ID AND PAYMENT_DATE < :TRANSACTION_ENTRY_DATE ");
			namedParameters.addValue("TRANSACTION_STATUS_ID", Arrays.asList(Status.TRANSACTION_COMPLETED.getStatus()));
			namedParameters.addValue("TRANSACTION_ENTRY_DATE", flatInfo.getEndDate(), Types.TIMESTAMP);
		} else if (requestUrl.equals("GiveInterestWaiverReport")) {//taking the sum of interest waiver amount which pending for approval
			query = new StringBuffer("SELECT NVL(SUM(AMOUNT),0) as SET_OFF_AMOUNT FROM FIN_TRANSACTION_ENTRY FTR WHERE FTR.BOOKING_FORM_ID = :BOOKING_FORM_ID AND FTR.FIN_TRANSACTION_TYPE_ID = :FIN_TRANSACTION_TYPE_ID AND FTR.TRANSACTION_STATUS_ID NOT IN(16,37) AND FTR.STATUS_ID = :STATUS_ID ");
		} else if (requestUrl.equals("saveInterestWaiver") || requestUrl.equals("approveInterestWaiver")) {
			query = new StringBuffer(SqlQuery.QRY_TO_GET_FIN_TRANSACTION_ENTRY_PAYMENT_SET_OFF)
					.append(" AND FTR.TRANSACTION_STATUS_ID IN (:TRANSACTION_STATUS_ID) AND FTR.STATUS_ID = :STATUS_ID")
					.append(" AND FTR.FIN_TRANSACTION_TYPE_ID = :FIN_TRANSACTION_TYPE_ID")
					.append(" AND FTSO.FIN_BOK_ACC_INVOICE_NO = :FIN_BOK_ACC_INVOICE_NO ")
					.append(" AND FTSO.SET_OFF_TYPE = :SET_OFF_TYPE").append(" AND FTR.BOOKING_FORM_ID = :BOOKING_FORM_ID ");;
				 if (requestUrl.equals("approveInterestWaiver")) {
					 query.append(" AND FTSOE.FIN_TRN_SET_OFF_ENT_ID NOT IN (:noT_FIN_TRN_SET_OFF_ENT_ID)");
					 namedParameters.addValue("noT_FIN_TRN_SET_OFF_ENT_ID",flatInfo.getTransactionSetOffEntryId(), Types.BIGINT);
				 }
			namedParameters.addValue("FIN_BOK_ACC_INVOICE_NO", flatInfo.getFinBokAccInvoiceNo(),Types.VARCHAR);
			namedParameters.addValue("FIN_TRANSACTION_TYPE_ID",FinTransactionType.INTEREST_WAIVER.getId(), Types.BIGINT);
			namedParameters.addValue("SET_OFF_TYPE",MetadataId.INTEREST_WAIVER.getId(), Types.BIGINT);
			namedParameters.addValue("TRANSACTION_STATUS_ID", Arrays.asList(Status.NEW.getStatus(),Status.APPROVED.getStatus(),Status.PENDING.getStatus()));	
		}
		namedParameters.addValue("FIN_TRANSACTION_TYPE_ID",FinTransactionType.INTEREST_WAIVER.getId(), Types.BIGINT);
		namedParameters.addValue("BOOKING_FORM_ID", flatInfo.getFlatBookingId(),Types.BIGINT);
		namedParameters.addValue("STATUS_ID",Status.ACTIVE.getStatus(),Types.BIGINT);
		try {
			if (requestUrl.equals("InterestWaiverRequest")) {
				amount = nmdPJdbcTemplate.queryForObject(query.toString(),namedParameters,Double.class);
			} else if (requestUrl.equals("GiveInterestWaiverReport")) {
				amount = nmdPJdbcTemplate.queryForObject(query.toString(),namedParameters,Double.class);
			} else {
				List<FinTransactionSetOffPojo> transactionEntryPojoPaymentSetOffLists = getData(query.toString(), namedParameters, FinTransactionSetOffPojo.class);
				if(Util.isNotEmptyObject(transactionEntryPojoPaymentSetOffLists)) {
					for (FinTransactionSetOffPojo pojo : transactionEntryPojoPaymentSetOffLists) {
						amount += pojo.getSetOffAmount() == null ? 0d : pojo.getSetOffAmount();
					}
					return amount;
				}
			}			
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return amount;
	}
	
	@Override
	public Double getLastApprovedInterestWaiverDetails(CustomerPropertyDetailsInfo flatInfo, String requestUrl) {
		StringBuffer query = null;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		Double amount = 0.0;

		query = new StringBuffer("")
				.append(" SELECT FTR.AMOUNT AS SET_OFF_AMOUNT ")
				.append(" FROM FIN_TRANSACTION_ENTRY FTR  ")
				.append(" WHERE FTR.TRANSACTION_STATUS_ID  IN (:TRANSACTION_STATUS_ID) AND FTR.STATUS_ID = :STATUS_ID ")
				.append(" AND FTR.FIN_TRANSACTION_TYPE_ID = :FIN_TRANSACTION_TYPE_ID AND FTR.BOOKING_FORM_ID = :BOOKING_FORM_ID ")
				.append(" AND TRUNC(FTR.TRANSACTION_CLOSED_DATE) = to_date('08-02-2022','dd-MM-yyyy') ");

		namedParameters.addValue("FIN_BOK_ACC_INVOICE_NO", flatInfo.getFinBokAccInvoiceNo(),Types.VARCHAR);
		namedParameters.addValue("FIN_TRANSACTION_TYPE_ID",FinTransactionType.INTEREST_WAIVER.getId(), Types.BIGINT);
		namedParameters.addValue("SET_OFF_TYPE",MetadataId.INTEREST_WAIVER.getId(), Types.BIGINT);
		namedParameters.addValue("FIN_TRANSACTION_TYPE_ID",FinTransactionType.INTEREST_WAIVER.getId(), Types.BIGINT);
		namedParameters.addValue("BOOKING_FORM_ID", flatInfo.getFlatBookingId(),Types.BIGINT);
		namedParameters.addValue("STATUS_ID",Status.ACTIVE.getStatus(),Types.BIGINT);

		namedParameters.addValue("TRANSACTION_STATUS_ID", Arrays.asList(Status.TRANSACTION_COMPLETED.getStatus()));	
		List<FinTransactionSetOffPojo> transactionEntryPojoPaymentSetOffLists = getData(query.toString(), namedParameters, FinTransactionSetOffPojo.class);
		if(Util.isNotEmptyObject(transactionEntryPojoPaymentSetOffLists)) {
			for (FinTransactionSetOffPojo pojo : transactionEntryPojoPaymentSetOffLists) {
				amount += pojo.getSetOffAmount() == null ? 0d : pojo.getSetOffAmount();
			}
			return amount;
		}
		return 0.0;
	}
	//if adding any joins need to check the dependencies of this methods
	@Override
	public List<FinancialProjectMileStonePojo> getPreviousNonPaidMileStoneDetails(@NotNull FinancialProjectMileStoneInfo finProjDemandNoteInfo,
			CustomerPropertyDetailsInfo customerPropertyDetailsInfo,FinEnum loadType) throws Exception {
		//if adding any joins need to check the dependencies of this methods
		log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.getPreviousNonPaidMileStoneDetails() ***** ");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		List<FinBookingFormModiCostDtlsPojo> bookingFormModiCostDtlsPojos = null;
		List<FinBookingFormLglCostDtlsPojo> bookingFormLegalCostDtlsPojos = null;
		List<FinBokFrmDemNteSchTaxMapPojo> demNteSchTaxMapPojos = null;
		List<FinPenaltyStatisticsPojo> penaltyStatisticsPojos = null;
		List<FinBookingFormMaintenanceDtlsPojo> maintenanceChargesList = null;
		List<FinBookingFormFlatKhataDtlsPojo> flatKhataChargesList = null;
		CustomerPropertyDetailsInfo customerPropertyDetailsInfo1 = null;
		String condition = finProjDemandNoteInfo.getCondition() == null ?"":finProjDemandNoteInfo.getCondition();
		//boolean flagForSetOffPayment = false;
		StringBuilder query = new StringBuilder("SELECT ").append(SqlQuery.QRY_TO_GET_FIN_BOK_FOM_DMD_NOTE_ID_DATA).append(", ")
				.append(SqlQuery.QRY_TO_GET_DATA_FROM_FIN_BOOKING_FORM_MILESTONES).append(",")
				.append(SqlQuery.QRY_TO_GET_FIN_BOOKING_FORM_ACCOUNTS_DATA).append(",")
				.append(" FBFDND.LOCATION,FBFDND.DOC_NAME,")
				.append(" NVL(FP.TOTAL_AMOUNT,0.0) AS TOTAL_PENALITY_AMOUNT,")
				.append(" NVL((SELECT PAY_AMOUNT - PAID_AMOUNT  FROM  FIN_BOOKING_FORM_ACCOUNTS FBFA1 WHERE FBFA1.TYPE = :PENALTY_TYPE_ID  AND FBFA1.TYPE_ID = FP.FIN_PENALTY_ID AND FBFA1.BOOKING_FORM_ID=:BOOKING_FORM_ID  AND FBFA1.STATUS_ID=:STATUS_ID),0) AS TOTAL_PENDING_PENALITY_AMOUNT,")
				.append(" NVL((SELECT PAID_AMOUNT  FROM  FIN_BOOKING_FORM_ACCOUNTS FBFA1 WHERE FBFA1.TYPE = :PENALTY_TYPE_ID  AND FBFA1.TYPE_ID = FP.FIN_PENALTY_ID AND FBFA1.BOOKING_FORM_ID=:BOOKING_FORM_ID  AND FBFA1.STATUS_ID=:STATUS_ID),0) AS TOTAL_PENALITY_PAID_AMOUNT,")
				.append(" NVL((SELECT INTEREST_WAIVER_AMT  FROM  FIN_BOOKING_FORM_ACCOUNTS FBFA1 WHERE FBFA1.TYPE = :PENALTY_TYPE_ID  AND FBFA1.TYPE_ID = FP.FIN_PENALTY_ID AND FBFA1.BOOKING_FORM_ID=:BOOKING_FORM_ID  AND FBFA1.STATUS_ID=:STATUS_ID),0) AS INTEREST_WAIVER_AMT,")//this INTEREST_WAIVER_AMT used to show the waiver amount
				.append(" (SELECT PAYMENT_STATUS FROM  FIN_BOOKING_FORM_ACCOUNTS FBFA1 WHERE FBFA1.TYPE = :PENALTY_TYPE_ID AND FBFA1.TYPE_ID = FP.FIN_PENALTY_ID AND FBFA1.BOOKING_FORM_ID=:BOOKING_FORM_ID  AND FBFA1.STATUS_ID=:STATUS_ID) AS PENALITY_STATUS,")
				.append(" FBFA.PAY_AMOUNT-FBFA.PAID_AMOUNT AS TOTAL_DUE_AMOUNT, ").append(" NVL(FBFA.INTEREST_WAIVER_AMT,0) AS INTEREST_WAIVER_PAID_AMT, ")//this is used INTEREST_WAIVER_PAID_AMT in paid amount
				/* Total No of Days Delayed to Pay Amount is used in Interest Letter */
				.append(" NVL((SELECT SUM(DAYS_DIFF) FROM FIN_PENALTY_STATISTICS FPS WHERE FPS.FIN_PENALTY_ID=FP.FIN_PENALTY_ID),0) AS DAYS_DIFF, ")
				//.append(" FBFA.PAY_AMOUNT AS TOTAL_DUE_AMOUNT, ")
				.append(" (SELECT FBFA1.PAYMENT_STATUS  FROM FIN_BOOKING_FORM_ACCOUNTS FBFA1 WHERE FBFA1.TYPE = :PENALTY_TYPE_ID AND FBFA1.TYPE_ID = FP.FIN_PENALTY_ID AND FBFA1.BOOKING_FORM_ID=:BOOKING_FORM_ID AND FBFA1.STATUS_ID=:STATUS_ID) AS PENALTY_STATUS ,")
				.append(SqlQuery.QRY_TO_GET_FIN_PROJECT_MILESTONES)
				.append(" ,PERCENT.PERCENTAGE AS MILE_STONE_PERCENTAGE ")
				.append(" FROM FIN_BOOKING_FORM_DEMAND_NOTE FBFDN")
				.append(" LEFT OUTER JOIN FIN_BOOKING_FORM_DEMAND_NOTE_DOC FBFDND ON (FBFDN.FIN_BOK_FOM_DMD_NOTE_ID = FBFDND.FIN_BOK_FOM_DMD_NOTE_ID AND FBFDN.STATUS_ID = :STATUS_ID)")
				.append(" RIGHT OUTER JOIN FIN_BOOKING_FORM_MILESTONES FBFM ON (FBFDN.FIN_BOK_FOM_DMD_NOTE_ID = FBFM.FIN_BOK_FOM_DMD_NOTE_ID AND FBFM.STATUS_ID = :STATUS_ID)")
		        .append(" RIGHT OUTER JOIN FIN_BOOKING_FORM_ACCOUNTS FBFA ON FBFM.FIN_BOOKING_FORM_MILESTONES_ID = FBFA.TYPE_ID")
				.append(" LEFT OUTER JOIN FIN_PENALTY FP ON FP.FIN_BOOKING_FORM_ACCOUNTS_ID = FBFA.FIN_BOOKING_FORM_ACCOUNTS_ID AND FP.STATUS_ID = :STATUS_ID")
				.append(" LEFT OUTER JOIN FIN_PROJECT_MILESTONES FPM ON FPM.PROJECT_MILESTONE_ID = FBFM.PROJECT_MILESTONE_ID")
				.append(" LEFT OUTER JOIN PERCENTAGES PERCENT ON PERCENT.PERCENTAGE_ID = FPM.PERCENTAGE_ID ")
				.append(" WHERE FBFA.TYPE IN (:TYPE )")
				.append(" AND FBFA.BOOKING_FORM_ID=:BOOKING_FORM_ID AND FBFA.STATUS_ID=:STATUS_ID")
				.append(Util.isNotEmptyObject(finProjDemandNoteInfo.getFinBokAccInvoiceNo())?" AND FBFA.FIN_BOK_ACC_INVOICE_NO = :FIN_BOK_ACC_INVOICE_NO ":"");
				
		if(condition.equalsIgnoreCase("PREVIEW_DEMAND_NOTES") || condition.equalsIgnoreCase("CalculateCustomerInterestBreakUpData") || condition.equalsIgnoreCase("GiveInterestWaiverReport")) {
			if(finProjDemandNoteInfo.getInterestSelectionType()!=null &&
					finProjDemandNoteInfo.getInterestSelectionType().equals(Status.With_Interest.getStatus())) {
				  query.append(" AND (FBFA.PAYMENT_STATUS IN (:PAYMENT_STATUS) ")
					.append(" OR IS_INTEREST_APPLICABLE = ")
					.append(" (CASE WHEN (SELECT FBFA1.PAYMENT_STATUS FROM FIN_BOOKING_FORM_ACCOUNTS FBFA1 WHERE FBFA1.TYPE = :PENALTY_TYPE_ID AND FBFA1.TYPE_ID = FP.FIN_PENALTY_ID ) IN (:PAYMENT_STATUS) THEN '21 ' ELSE NULL END))");
			} else {
				  query.append(" AND FBFA.PAYMENT_STATUS IN (:PAYMENT_STATUS)");
			}
		}/* else if(condition.equalsIgnoreCase("ProcessPaymentSetOff")) {
			query.append(" AND FBFA.PAYMENT_STATUS IN (:PAYMENT_STATUS)");
		}*/ else {
			query.append(" AND (FBFA.PAYMENT_STATUS IN (:PAYMENT_STATUS) ")
				 .append(" OR IS_INTEREST_APPLICABLE = ")
					.append(" (CASE WHEN (SELECT FBFA1.PAYMENT_STATUS FROM FIN_BOOKING_FORM_ACCOUNTS FBFA1 WHERE FBFA1.TYPE = :PENALTY_TYPE_ID AND FBFA1.TYPE_ID = FP.FIN_PENALTY_ID ) IN (:PAYMENT_STATUS) THEN '21 ' ELSE NULL END))");
		}
		   
		//if (Util.isNotEmptyObject(condition)) {
			if (condition.equalsIgnoreCase("SetOffPayment")) {
				namedParameters.addValue("FIN_BOOKING_FORM_ACCOUNTS_ID",finProjDemandNoteInfo.getFinBookingFormAccountsId(), Types.BIGINT);
				query.append(" AND FBFA.FIN_BOOKING_FORM_ACCOUNTS_ID = :FIN_BOOKING_FORM_ACCOUNTS_ID ");
				//flagForSetOffPayment = true;
			} else if (condition.equalsIgnoreCase("ReGenerateDemandNote")) {
				query.append(" AND FBFM.PROJECT_MILESTONE_ID = :PROJECT_MILESTONE_ID");
				namedParameters.addValue("PROJECT_MILESTONE_ID",finProjDemandNoteInfo.getProjectMilestoneId(), Types.BIGINT);
			} else if(condition.equalsIgnoreCase("GenerateDemandNote") && Util.isNotEmptyObject(finProjDemandNoteInfo.getProjectMilestoneId())) {
				query.append(" AND FBFM.PROJECT_MILESTONE_ID <= :PROJECT_MILESTONE_ID");
				namedParameters.addValue("PROJECT_MILESTONE_ID",finProjDemandNoteInfo.getProjectMilestoneId(), Types.BIGINT);
			} else if(condition.equalsIgnoreCase("PREVIEW_DEMAND_NOTES")) {
				query.append(" AND FBFM.PROJECT_MILESTONE_ID <= :PROJECT_MILESTONE_ID");
				namedParameters.addValue("PROJECT_MILESTONE_ID",finProjDemandNoteInfo.getProjectMilestoneId(), Types.BIGINT);
				/*if( Util.isNotEmptyObject(finProjDemandNoteInfo.getCurrentprojectMileStoneIds())) {
					query.append(" AND FBFM.PROJECT_MILESTONE_ID IN (:PROJECT_MILESTONE_ID)");//don't move this line
					namedParameters.addValue("PROJECT_MILESTONE_ID",finProjDemandNoteInfo.getCurrentprojectMileStoneIds(), Types.BIGINT);
				} else {query.append(" AND FBFM.PROJEC1T_MILESTONE_ID IN (:PROJECT_MILESTONE_ID)");}*/
			}  else if(condition.equalsIgnoreCase("Interest_Letter")) {
				if( Util.isNotEmptyObject(finProjDemandNoteInfo.getCurrentprojectMileStoneIds())) {
					query.append(" AND FBFM.PROJECT_MILESTONE_ID IN (:PROJECT_MILESTONE_ID)"); 
					namedParameters.addValue("PROJECT_MILESTONE_ID",finProjDemandNoteInfo.getCurrentprojectMileStoneIds(), Types.BIGINT);
				} else {query.append(" AND FBFM.PROJEC1T_MILESTONE_ID IN (:PROJECT_MILESTONE_ID)");}
			} else if(condition.equalsIgnoreCase("EditDemandNote")) {
				query.append(" AND FBFM.PROJECT_MILESTONE_ID = :PROJECT_MILESTONE_ID");
				query.append(" AND FBFM.MS_STATUS_ID = :STATUS_ID");
				query.append(" AND FBFM.STATUS_ID = :STATUS_ID");
				namedParameters.addValue("PROJECT_MILESTONE_ID",finProjDemandNoteInfo.getProjectMilestoneId(), Types.BIGINT);
			} else if(condition.equalsIgnoreCase("CalculateCustomerInterestBreakUpData") || condition.equalsIgnoreCase("milestoneWiseInterestDetails")) {
				query.append(" AND FBFM.PROJECT_MILESTONE_ID IN (:PROJECT_MILESTONE_ID)");
				namedParameters.addValue("PROJECT_MILESTONE_ID",finProjDemandNoteInfo.getCurrentprojectMileStoneIds(), Types.BIGINT);
			} else if(condition.equalsIgnoreCase("GiveInterestWaiverReport")) {
				//Thi line can be uncomment if want all customer details if interest waiver raised or not
				//query.append(" and NVL((SELECT INTEREST_WAIVER_AMT  FROM  FIN_BOOKING_FORM_ACCOUNTS FBFA1 WHERE FBFA1.TYPE = :PENALTY_TYPE_ID  AND FBFA1.TYPE_ID = FP.FIN_PENALTY_ID AND FBFA1.BOOKING_FORM_ID=:BOOKING_FORM_ID  AND FBFA1.STATUS_ID=:STATUS_ID),0) !=0");
				//query.append(" AND FBFM.PROJECT_MILESTONE_ID <= :PROJECT_MILESTONE_ID");
				//namedParameters.addValue("PROJECT_MILESTONE_ID",finProjDemandNoteInfo.getCurrentprojectMileStoneIds(), Types.BIGINT);
			} else if(condition.equalsIgnoreCase("LoadMilestoneDetails")) {
				query.append(" AND  FBFA.FIN_BOOKING_FORM_ACCOUNTS_ID IN (:FIN_BOOKING_FORM_ACCOUNTS_ID)");
				namedParameters.addValue("FIN_BOOKING_FORM_ACCOUNTS_ID",finProjDemandNoteInfo.getCurrentprojectMileStoneIds(), Types.BIGINT);
			}
		//}
		
		if( Util.isNotEmptyObject(finProjDemandNoteInfo.isReGenerateDemandNote()) && finProjDemandNoteInfo.isReGenerateDemandNote()) {
			if(Util.isNotEmptyObject(finProjDemandNoteInfo.getProjectMilestoneId())) {
				//query.append(" AND FBFM.PROJECT_MILESTONE_ID <= :PROJECT_MILESTONE_ID");
				//namedParameters.addValue("PROJECT_MILESTONE_ID",finProjDemandNoteInfo.getProjectMilestoneId(), Types.BIGINT);
			}
		}
		Long type = finProjDemandNoteInfo.getType().get(0);
		if(condition.equalsIgnoreCase("ProcessPaymentSetOff")) {
			if(type.equals(MetadataId.FIN_BOOKING_FORM_MILESTONES.getId())) {
				query.append(" ORDER BY FPM.PROJECT_MILESTONE_ID").append(isNotEmptyObject(finProjDemandNoteInfo.getDataSelectionOrder()) ? " DESC" : "");
			} else {
				query.append(" ORDER BY FBFA.FIN_BOOKING_FORM_ACCOUNTS_ID").append(isNotEmptyObject(finProjDemandNoteInfo.getDataSelectionOrder()) ? " DESC" : "");
			}
		}else {
			if(isNotEmptyObject(finProjDemandNoteInfo.getDataSelectionOrder())) {
				query.append(" ORDER BY FBFA.FIN_BOOKING_FORM_ACCOUNTS_ID DESC ");
				//.append(isNotEmptyObject(finProjDemandNoteInfo.getDataSelectionOrder()) ? " DESC" : "").toString();
			} else {
				query.append(" ORDER BY FPM.PROJECT_MILESTONE_ID,FBFA.FIN_BOOKING_FORM_ACCOUNTS_ID");
				//.append(isNotEmptyObject(finProjDemandNoteInfo.getDataSelectionOrder()) ? " DESC" : "").toString();
			}
		}
		namedParameters.addValue("TYPE", finProjDemandNoteInfo.getType(), Types.BIGINT);
		namedParameters.addValue("STATUS_YES", Status.YES.getStatus(), Types.BIGINT);
		namedParameters.addValue("BOOKING_FORM_ID", customerPropertyDetailsInfo.getFlatBookingId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", customerPropertyDetailsInfo.getStatusId(), Types.BIGINT);
		namedParameters.addValue("PAYMENT_STATUS", finProjDemandNoteInfo.getPaymentStatus(), Types.BIGINT);
		namedParameters.addValue("FIN_BOK_ACC_INVOICE_NO", finProjDemandNoteInfo.getFinBokAccInvoiceNo(),Types.VARCHAR);
		namedParameters.addValue("PENALTY_TYPE_ID",  MetadataId.FIN_PENALTY.getId(),Types.BIGINT);
		
		List<FinancialProjectMileStonePojo> pendingMilestoneList = (List<FinancialProjectMileStonePojo>) getData(
				query.toString(), namedParameters, FinancialProjectMileStonePojo.class);
		//log.info("*****EmployeeFinancialServiceDaoImpl.getPreviousNonPaidMileStoneDetails()1 ***** \n"+pendingMilestoneList);
		for (FinancialProjectMileStonePojo financialProjectMileStonePojo : pendingMilestoneList) {
			if(condition.equalsIgnoreCase("CalculateCustomerInterestBreakUpData")) {//this is View interest breakup data
				financialProjectMileStonePojo.setTotalPendingPenaltyAmount_Db(0d);
				financialProjectMileStonePojo.setTotalPendingPenaltyAmount(0d);
				//financialProjectMileStonePojo.setDaysDelayed(Util.isEmptyObject(financialProjectMileStonePojo.getDaysDelayed())?0l:financialProjectMileStonePojo.getDaysDelayed());
				financialProjectMileStonePojo.setTotalDelayedDays_Db(0l);
			} else {
				financialProjectMileStonePojo.setTotalPendingPenaltyAmount_Db(Util.isEmptyObject(financialProjectMileStonePojo.getTotalPendingPenaltyAmount())?0d:financialProjectMileStonePojo.getTotalPendingPenaltyAmount());
				//financialProjectMileStonePojo.setDaysDelayed(Util.isEmptyObject(financialProjectMileStonePojo.getDaysDelayed())?0l:financialProjectMileStonePojo.getDaysDelayed());
				financialProjectMileStonePojo.setTotalDelayedDays_Db(Util.isEmptyObject(financialProjectMileStonePojo.getDaysDelayed())?0l:financialProjectMileStonePojo.getDaysDelayed());
			}
			if(condition.equalsIgnoreCase("ProcessPaymentSetOff") || condition.equalsIgnoreCase("SetOffPayment")) {
				if(type.equals(MetadataId.FIN_PENALTY.getId())) {// is this request from save and approve transaction, then adding interest waiver amount in interest paid amount, if customer paying interest
					financialProjectMileStonePojo.setPaidAmount(financialProjectMileStonePojo.getPaidAmount()+financialProjectMileStonePojo.getInterestWaiverPaidAmount());
					//total amt = 1000, paid amt = 500, waived amt = 100, pending : 500 ;
				}
			}
			customerPropertyDetailsInfo1 = (CustomerPropertyDetailsInfo) customerPropertyDetailsInfo.clone();
			customerPropertyDetailsInfo1.setTypeId(financialProjectMileStonePojo.getTypeId());
			if (loadType.getId().equals(FinEnum.DO_NOT_lOAD_DATA.getId())) {
				continue;
			}
			if(financialProjectMileStonePojo.getType().equals(MetadataId.MODIFICATION_COST.getId())) {//loading modification details, like amount and gst percentage and pdf path
				bookingFormModiCostDtlsPojos = getModificationCostDetails(financialProjectMileStonePojo);
				financialProjectMileStonePojo.setBookingFormModiCostDtlsPojos(bookingFormModiCostDtlsPojos);
			} else if(financialProjectMileStonePojo.getType().equals(MetadataId.LEGAL_COST.getId())) {//loading legal details, like amount and gst percentage and pdf path
				bookingFormLegalCostDtlsPojos = getLegalCostDetails(financialProjectMileStonePojo);
				financialProjectMileStonePojo.setBookingFormLglCostDtlsPojos(bookingFormLegalCostDtlsPojos);
			} else if(financialProjectMileStonePojo.getType().equals(MetadataId.FIN_BOOKING_FORM_MILESTONES.getId())) {//loading milestone details, like amount and gst percentage
				demNteSchTaxMapPojos = getMileStoneTaxDetails1(customerPropertyDetailsInfo1);
				List<FinBookingFormMstSchTaxMapInfo> bookingFormMstSchTaxMapInfos = new EmployeeFinancialMapper().copyFinBokFrmDemNteSchTaxMapPojoTOFinBookingFormMstSchTaxMapInfo(demNteSchTaxMapPojos);
				financialProjectMileStonePojo.setBokFrmDemNteSchTaxMapInfos(bookingFormMstSchTaxMapInfos);
			} else if(financialProjectMileStonePojo.getType().equals(MetadataId.FIN_PENALTY.getId())) {
				penaltyStatisticsPojos = loadPenaltyStatisticsData(financialProjectMileStonePojo);
				financialProjectMileStonePojo.setPenaltyStatisticsPojos(penaltyStatisticsPojos);
			} else if(financialProjectMileStonePojo.getType().equals(MetadataId.MAINTENANCE_CHARGE.getId())) {
				maintenanceChargesList = loadMaintenance_ChargesDetails(financialProjectMileStonePojo);
				financialProjectMileStonePojo.setMaintenanceChargesDtlsPojos(maintenanceChargesList);
			} else if(financialProjectMileStonePojo.getType().equals(MetadataId.INDIVIDUAL_FLAT_KHATA_BIFURCATION_AND_OTHER_CHARGES.getId())) {
				flatKhataChargesList = loadFlatKhataDetails(financialProjectMileStonePojo);
				financialProjectMileStonePojo.setFlatKhataChargesDtlsPojos(flatKhataChargesList);
			}
		}
		return pendingMilestoneList;
	}
	
	@Override
	public List<FinTransactionEntryDetailsPojo> getTransactionDetails(FinTransactionEntryDetailsInfo transactionEntryDetailsInfo,
			CustomerPropertyDetailsInfo customerPropertyDetailsInfoNotINUse) throws Exception {
		//this method used for Customer Ledger, and Delete Transaction, And Edit Transaction 
		log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.getTransactionDetails() *****");
		
		boolean isThisDeleteTransaction = (transactionEntryDetailsInfo.getCondition()!=null && transactionEntryDetailsInfo.getCondition().equals(FinEnum.DELETE_TRANSACTION.getName()));
		boolean isThisShiftTransaction = (transactionEntryDetailsInfo.getCondition()!=null && transactionEntryDetailsInfo.getCondition().equals(FinEnum.SHIFT_TRANSACTION.getName()));
		boolean isThisEditTransaction = (transactionEntryDetailsInfo.getCondition()!=null && transactionEntryDetailsInfo.getCondition().equals(FinEnum.EDIT_TRANSACTION.getName()));
		boolean isThisLoadPrevTransaction = (transactionEntryDetailsInfo.getCondition()!=null && transactionEntryDetailsInfo.getCondition().equals("LoadPreviousTransaction"));
		boolean isCustomerLedger = (transactionEntryDetailsInfo.getCondition()!=null && transactionEntryDetailsInfo.getCondition().equals(MetadataId.CUSTOMER_LEDGER.getName()));
		boolean customerLedgerPayment =  (transactionEntryDetailsInfo.getCondition()!=null && transactionEntryDetailsInfo.getCondition().equals("CustomerLedgerPayment"));
		boolean paymentExcessAmount =  (transactionEntryDetailsInfo.getCondition()!=null && transactionEntryDetailsInfo.getCondition().equals("PaymentExcessAmount"));
		StringBuffer query = null;
		
		if (isThisDeleteTransaction || isThisEditTransaction) {
			query = new StringBuffer(SqlQuery.QRY_TO_GET_RECEIPT_DETAILS_ADJUSTED_AMOUNT);
			query.append(" AND FTE.FIN_TRANSACTION_ENTRY_ID = :FIN_TRANSACTION_ENTRY_ID");
			query.append(" AND FTE.FIN_TRANSACTION_TYPE_ID IN (:FIN_TRANSACTION_TYPE_ID_FOR_PREVs) ");
		} else if (isThisShiftTransaction) {
			query = new StringBuffer(SqlQuery.QRY_TO_GET_RECEIPT_DETAILS_ADJUSTED_AMOUNT);
			//query.append(" AND FTE.FIN_TRANSACTION_ENTRY_ID > :FIN_TRANSACTION_ENTRY_ID");
			query.append(" AND  FTE.RECEIVED_DATE > :TRANSACTION_ENTRY_RECEIVE_DATE");
			query.append(" AND FTE.FIN_TRANSACTION_ENTRY_ID != :FIN_TRANSACTION_ENTRY_ID");
			query.append(" AND FTE.FIN_TRANSACTION_TYPE_ID IN (:FIN_TRANSACTION_TYPE_ID_FOR_PREVs) ");
			if(Util.isEmptyObject(transactionEntryDetailsInfo.getActualTransactionReceiveDate())) {
				log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.getTransactionDetails() Date found empty *****");
				throw new EmployeeFinancialServiceException("Date found empty");
			}
			//TRANSACTION_ENTRY_RECEIVE_DATE
		}else if (isThisLoadPrevTransaction) {
			query = new StringBuffer(SqlQuery.QRY_TO_GET_RECEIPT_DETAILS_ADJUSTED_AMOUNT);
			query.append(" AND FTE.FIN_TRANSACTION_ENTRY_ID < :FIN_TRANSACTION_ENTRY_ID");
			query.append(" AND FTE.FIN_TRANSACTION_TYPE_ID IN (:FIN_TRANSACTION_TYPE_ID_FOR_PREVs) ");
		} else if (isCustomerLedger) {
			query = new StringBuffer(SqlQuery.QRY_TO_GET_RECEIPT_DETAILS);
			query.append(" AND FTE.FIN_TRANSACTION_TYPE_ID IN (:FIN_TRANSACTION_TYPE_ID_FOR_PREVs) ");
		}else if(customerLedgerPayment) {
			query = new StringBuffer(SqlQuery.QRY_TO_GET_PAYMENT_DETAILS_FOR_CUSTOMER_LEDGER);
			query.append(" AND FTE.FIN_TRANSACTION_TYPE_ID IN (:FIN_TRANSACTION_TYPE_ID_FOR_PREVs) ");
		}else if(paymentExcessAmount) {
			 query = new StringBuffer(SqlQuery.QRY_TO_GET_PAYMENT_DETAILS);
			 query.append(" AND FTE.FIN_TRANSACTION_ENTRY_ID = :FIN_TRANSACTION_ENTRY_ID");
			 query.append(" AND FTE.FIN_TRANSACTION_TYPE_ID IN (:FIN_TRANSACTION_TYPE_ID_FOR_PREVs) ");
		}
		
		if(customerLedgerPayment) {
			query.append(" AND FBA.TYPE IN (:TYPE) ");
			query.append(" ORDER BY FTE.FIN_TRANSACTION_ENTRY_ID,FBFAPD.FIN_BOK_FRM_ACC_PMT_DTLS_ID ");
		}else if (paymentExcessAmount) {
			query.append(" AND FBFAPD.TYPE IN (:TYPE) ");
			query.append(" ORDER BY FTE.FIN_TRANSACTION_ENTRY_ID,FBFAPD.FIN_BOK_FRM_ACC_PMT_DTLS_ID ");
		} else if (isThisShiftTransaction) {
			query.append(" AND FBA.TYPE IN (:TYPE) ");
			query.append(" ORDER BY FTE.RECEIVED_DATE,FTE.FIN_TRANSACTION_ENTRY_ID,FBAS.FIN_BOK_FRM_ACC_SMT_ID ");
		} else {
			query.append(" AND FBA.TYPE IN (:TYPE) ");
			query.append(" ORDER BY FTE.FIN_TRANSACTION_ENTRY_ID,FBAS.FIN_BOK_FRM_ACC_SMT_ID ");
		}

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("TYPE", Arrays.asList(MetadataId.FIN_BOOKING_FORM_MILESTONES.getId(),MetadataId.FIN_PENALTY.getId()), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("DOC_TYPE", MetadataId.GENERATED.getId(), Types.BIGINT);
		namedParameters.addValue("RECEIPT_TYPE", MetadataId.FIN_BOOKING_FORM_MILESTONES.getId(), Types.BIGINT);
		namedParameters.addValue("BOOKING_FORM_ID", transactionEntryDetailsInfo.getBookingFormId(), Types.BIGINT);
		namedParameters.addValue("TRANSACTION_STATUS_ID", Status.TRANSACTION_COMPLETED.getStatus(), Types.BIGINT);
		namedParameters.addValue("FIN_TRANSACTION_ENTRY_ID", transactionEntryDetailsInfo.getTransactionEntryId(), Types.BIGINT);
		namedParameters.addValue("TRANSACTION_ENTRY_RECEIVE_DATE", transactionEntryDetailsInfo.getActualTransactionReceiveDate(), Types.TIMESTAMP);
		namedParameters.addValue("FIN_TRANSACTION_TYPE_ID_FOR_PREVs", Arrays.asList(FinTransactionType.RECEIPT.getId(),FinTransactionType.PAYMENT.getId()), Types.BIGINT);
		if (isThisDeleteTransaction || isThisEditTransaction ) {
			//selecting records using payment type, for deleteing and editing the transaction
			/*namedParameters.addValue("TYPE", Arrays.asList(MetadataId.FIN_BOOKING_FORM_MILESTONES.getId(),
					MetadataId.MODIFICATION_COST.getId(),
					MetadataId.LEGAL_COST.getId(),
					MetadataId.REFUNDABLE_ADVANCE.getId(),
					MetadataId.FIN_PENALTY.getId()
					,MetadataId.TDS.getId()
					,MetadataId.MAINTENANCE_CHARGE.getId(),MetadataId.INDIVIDUAL_FLAT_KHATA_BIFURCATION_AND_OTHER_CHARGES.getId(),MetadataId.CORPUS_FUND.getId()
					), Types.BIGINT);*/
			namedParameters.addValue("TYPE",ALL_TYPES_OF_PAYMENTS, Types.BIGINT);
		}
		
		if(paymentExcessAmount) {
			namedParameters.addValue("TYPE", MetadataId.FIN_BOOKING_FORM_EXCESS_AMOUNT.getId(), Types.BIGINT);
		}
		
		List<FinTransactionEntryDetailsPojo> receiptDetails = getData(query.toString(), namedParameters, FinTransactionEntryDetailsPojo.class);
		return receiptDetails;
	}

	@Override
	public List<FinBookingFormAccountPaymentDetailsPojo> getTransactionPayemntDetails(FinTransactionEntryDetailsInfo transactionEntryDetailsInfo) throws Exception {
		log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.getTransactionPayemntDetails() *****");
		boolean isThisDeleteTransaction = (transactionEntryDetailsInfo.getCondition()!=null && transactionEntryDetailsInfo.getCondition().equals(FinEnum.DELETE_TRANSACTION.getName()));
		boolean isThisShiftTransaction = (transactionEntryDetailsInfo.getCondition()!=null && transactionEntryDetailsInfo.getCondition().equals(FinEnum.SHIFT_TRANSACTION.getName()));
		boolean isThisEditTransaction = (transactionEntryDetailsInfo.getCondition()!=null && transactionEntryDetailsInfo.getCondition().equals(FinEnum.EDIT_TRANSACTION.getName()));
		
		StringBuilder query = new StringBuilder(SqlQuery.QRY_TO_GET_PAYMENT_DETAILS);
		if (isThisDeleteTransaction || isThisEditTransaction) {
			query.append(" AND FTE.FIN_TRANSACTION_ENTRY_ID = :FIN_TRANSACTION_ENTRY_ID");
			query.append(" AND FTE.FIN_TRANSACTION_TYPE_ID IN (:FIN_TRANSACTION_TYPE_ID_FOR_PREVs) ");
			query.append(" ORDER BY FTE.PAYMENT_DATE,FTE.FIN_TRANSACTION_ENTRY_ID ");
		} else if (isThisShiftTransaction) {
			//query.append(" AND FTE.FIN_TRANSACTION_ENTRY_ID > :FIN_TRANSACTION_ENTRY_ID");
			query.append(" AND  FTE.PAYMENT_DATE > :TRANSACTION_ENTRY_PAYMENT_DATE");
			query.append(" AND FTE.FIN_TRANSACTION_TYPE_ID IN (:FIN_TRANSACTION_TYPE_ID_FOR_PREVs) ");
			query.append(" AND FTE.FIN_TRANSACTION_ENTRY_ID != :FIN_TRANSACTION_ENTRY_ID");
			query.append(" ORDER BY FTE.PAYMENT_DATE,FTE.FIN_TRANSACTION_ENTRY_ID ");
			
			if(Util.isEmptyObject(transactionEntryDetailsInfo.getActualTransactionDate())) {
				log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.getTransactionPayemntDetails() Date found empty *****");
				throw new EmployeeFinancialServiceException("Date found empty");
			}
		}
		//selecting all the payment types which is in, Payment type transaction refunded
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("BOOKING_FORM_ID", transactionEntryDetailsInfo.getBookingFormId(), Types.BIGINT);
		namedParameters.addValue("TRANSACTION_STATUS_ID", Status.TRANSACTION_COMPLETED.getStatus(), Types.BIGINT);
		namedParameters.addValue("FIN_TRANSACTION_ENTRY_ID", transactionEntryDetailsInfo.getTransactionEntryId(), Types.BIGINT);
		namedParameters.addValue("TRANSACTION_ENTRY_PAYMENT_DATE", transactionEntryDetailsInfo.getActualTransactionDate(), Types.TIMESTAMP);
		namedParameters.addValue("FIN_TRANSACTION_TYPE_ID_FOR_PREVs", Arrays.asList(FinTransactionType.RECEIPT.getId(),FinTransactionType.PAYMENT.getId()), Types.BIGINT);
		log.info("*****  *****\n"+query+"\n"+namedParameters.getValues());
		List<FinBookingFormAccountPaymentDetailsPojo> paymentList = getData(query.toString(), namedParameters, FinBookingFormAccountPaymentDetailsPojo.class);
		return paymentList;
	}
	
	@Override
	public int isThisTransactionHavingOnlyExces(FinTransactionEntryDetailsInfo transactionEntryDetailsInfo,
			CustomerPropertyDetailsInfo customerPropertyDetailsInfo) {
		String sqlQuery = FinancialQuerys.QRY_TO_GET_FIN_BOOKING_FORM_RECEIPTS_ENTRY_COUNT;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("BOOKING_FORM_ID", transactionEntryDetailsInfo.getBookingFormId(), Types.BIGINT);
		namedParameters.addValue("FIN_TRANSACTION_ENTRY_ID", transactionEntryDetailsInfo.getTransactionEntryId(), Types.BIGINT);
		List<Map<String, Object>> transactionList = nmdPJdbcTemplate.queryForList(sqlQuery, namedParameters);
		sqlQuery = "";
		if(Util.isNotEmptyObject(transactionList)) {//if we found transaction
			EmployeeFinancialServiceImpl emp = new EmployeeFinancialServiceImpl();
			double totalExcessBalanceAmount = 0.0;
			double transactionAmount = transactionList.get(0).get("TRANSACTION_AMOUNT") ==null?0d:Double.valueOf(transactionList.get(0).get("TRANSACTION_AMOUNT").toString());
			if(transactionAmount==0) {
				return 0;
			}
			//checking is this transaction is adjusted to any fin booking form accounts table 
			//loading statment list
			StringBuffer sqlQuery1 = new StringBuffer(SqlQuery.QRY_TO_GET_BOOKING_FORM_STATEMENT_AND_RECEIPT_DATA);
			sqlQuery1.append(" AND FBFR.FIN_BOK_FRM_RCPT_ID = :FIN_BOK_FRM_RCPT_ID ");
			namedParameters.addValue("FIN_BOK_FRM_RCPT_ID", transactionList.get(0).get("FIN_BOK_FRM_RCPT_ID"), Types.BIGINT);
			List<Map<String, Object>> statementObjectList = nmdPJdbcTemplate.queryForList(sqlQuery1.toString(), namedParameters);
			sqlQuery1 = null;
			if(Util.isEmptyObject(statementObjectList)) {//if statement object list is empty means , the amount is not adjusted to any accounts record
				 sqlQuery1 = new StringBuffer(SqlQuery.QRY_TO_GET_BOOKING_FORM_EXCESS_AMOUNT_DETAILS_BY_ID);
				 sqlQuery1.append(" AND FBFR.FIN_BOK_FRM_RCPT_ID = :FIN_BOK_FRM_RCPT_ID ");
				 List<Map<String, Object>> excessAmtObjectList = nmdPJdbcTemplate.queryForList(sqlQuery1.toString(), namedParameters);
				 if(Util.isNotEmptyObject(excessAmtObjectList)) {//if we found excess amount list, means all the amount of transaction went to excess amount
					 for (Map<String, Object> excessObj : excessAmtObjectList) {
						 totalExcessBalanceAmount = totalExcessBalanceAmount + emp.roundOffAmount(excessObj.get("BALANCE_AMOUNT") ==null ? 0d : Double.valueOf(excessObj.get("BALANCE_AMOUNT").toString()));
					}
				 }
			}
			if (totalExcessBalanceAmount == transactionAmount) {
				//total excess balance amount should match with transaction amount, if not matching means 
				//some excess amount is refunded, so that refund amount payment transaction should deleted first
				return 1;
			}
		}
		
		return 0;
	}
	
	@Override
	public List<FinBookingFormReceiptsPojo> getPreviousTransactionPaidDate(FinTransactionEntryDetailsInfo transactionEntryDetailsInfo) {
		log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.getPreviousTransactionPaidDate() *****");
		boolean isDeleteTransaction = (transactionEntryDetailsInfo.getCondition()!=null && transactionEntryDetailsInfo.getCondition().equals(FinEnum.DELETE_TRANSACTION.getName()));
		
		StringBuffer query = new StringBuffer("SELECT ")
				.append(SqlQuery.QRY_TO_GET_FIN_BOOKING_FORM_RECEIPTS)
				.append(" FROM FIN_BOOKING_FORM_RECEIPTS FBFR")
				.append(" WHERE FBFR.BOOKING_FORM_ID = :BOOKING_FORM_ID")
				.append(" AND FBFR.STATUS_ID = :STATUS_ID");
		if (isDeleteTransaction) {
			query.append(" AND FBFR.FIN_BOK_FRM_RCPT_ID  = (SELECT MAX(FIN_BOK_FRM_RCPT_ID) FROM FIN_BOOKING_FORM_RECEIPTS ");
			query.append(" WHERE FBFR.BOOKING_FORM_ID = BOOKING_FORM_ID AND STATUS_ID = :STATUS_ID AND FIN_TRN_SET_OFF_ENT_ID < :FIN_TRN_SET_OFF_ENT_ID)");
			// AND FIN_TRN_SET_OFF_ENT_ID <:FIN_TRN_SET_OFF_ENT_ID 
		}
		query.append(" ORDER BY FBFR.FIN_BOK_FRM_RCPT_ID");
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("BOOKING_FORM_ID", transactionEntryDetailsInfo.getBookingFormId(), Types.BIGINT);
		namedParameters.addValue("FIN_TRN_SET_OFF_ENT_ID", transactionEntryDetailsInfo.getTransactionSetOffEntryId(), Types.BIGINT);
		
		
		List<FinBookingFormReceiptsPojo> receiptList = (List<FinBookingFormReceiptsPojo>) getData(
				query.toString(), namedParameters, FinBookingFormReceiptsPojo.class);
		return receiptList;
	}
	
	@Override
	public Long copyTransactionApproveRejectDetails(FinTransactionApprStatPojo apprStatPojo,EmployeeFinancialTransactionServiceInfo transactionServiceInfoNot) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.copyTransactionApproveRejectDetails() *****");		
		String query = SqlQuery.QRY_TO_INSERT_TRANSACTION_APPROVAL_REJECT_DETAILS;
 		Long result = commonMethodToInsertData(query, apprStatPojo, "FIN_TRN_APPR_STAT_ID");
		apprStatPojo.setPrevFinTrnApprStatId(apprStatPojo.getFinTrnApprStatId());
		apprStatPojo.setFinTrnApprStatId(result);
		
		/*INSERT INTO FIN_TRANSACTION_APPR_STAT (FIN_TRN_APPR_STAT_ID,FIN_TRN_SET_OFF_ENT_ID ,ACTION_TYPE,EMP_ID,COMMENTS ,CREATED_DATE) 
        SELECT FIN_TRANSACTION_APPR_REJECT_STAT_SEQ.NEXTVAL,:NEW_ID,ACTION_TYPE ,EMP_ID ,COMMENTS  ,CREATED_DATE FROM FIN_TRANSACTION_APPR_STAT 
        WHERE FIN_TRN_SET_OFF_ENT_ID = :PREV_ID;*/
		
		return result;
	}
	
	@Override
	public int copyTransactionChangedDetails(FinTransactionApprStatPojo apprStatPojo) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.copyTransactionChangedDetails() *****");
		String query = SqlQuery.QRY_TO_COPY_FIN_TRANSACTION_CHANGED_DTLS_RECORDS;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("NEW_FIN_TRN_APPR_STAT_ID",apprStatPojo.getFinTrnApprStatId(), Types.BIGINT);
		namedParameters.addValue("PREV_FIN_TRN_APPR_STAT_ID",apprStatPojo.getPrevFinTrnApprStatId(), Types.BIGINT);
		int result = commonMethodToUpdateData(query, namedParameters);
		return result;
	}
	
	@Override
	public int copyTransactionSetOffApprovaldetails(EmployeeFinancialTransactionServiceInfo clonedTransactionServiceInfo,
																EmployeeFinancialTransactionServiceInfo transactionServiceInfo) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.copyTransactionSetOffApprovaldetails() *****");
		String query = SqlQuery.QRY_TO_COPY_FIN_TRANSACTION_SET_OFF_APPROVAL_RECORDS;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("PREV_FIN_TRN_SET_OFF_ENT_ID",clonedTransactionServiceInfo.getTransactionSetOffEntryId(), Types.BIGINT);
		namedParameters.addValue("NEW_FIN_TRN_SET_OFF_ENT_ID", transactionServiceInfo.getTransactionSetOffEntryId(),Types.BIGINT);
		int result = commonMethodToUpdateData(query, namedParameters);
		return result;
	}

	@Override
	public int copyTransactionSetOffApprovaldetailsFrmUpload(
			EmployeeFinancialTransactionServiceInfo transactionServiceInfo) {
		String query = SqlQuery.QRY_TO_COPY_FIN_TRANSACTION_SET_OFF_APPROVAL_RECORDS;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("PREV_FIN_TRN_SET_OFF_ENT_ID",transactionServiceInfo.getPrevTransactionSetOffEntryId(), Types.BIGINT);
		namedParameters.addValue("NEW_FIN_TRN_SET_OFF_ENT_ID", transactionServiceInfo.getTransactionSetOffEntryId(),Types.BIGINT);
		int result = commonMethodToUpdateData(query, namedParameters);
		return result;
	}
	
	@Override
	public int revertExcessAmountOfPayment(FinBookingFormExcessAmountPojo excessAmountPojo) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.revertExcessAmountOfPayment() *****");
		String query = SqlQuery.QRY_TO_REVERT_FIN_BOOKING_FORM_EXCESS_AMT_OF_PAYMENT;
		int result = commonMethodToUpdateData(query, excessAmountPojo);
		return result;
	}
	
	@Override
	public int revertFinBookingFormAccountsAmountOfPayment(FinBookingFormAccountsPojo pojo) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.revertFinBookingFormAccountsAmountOfPayment() *****");
		String query = SqlQuery.QRY_TO_REVERT_FIN_BOOKING_FORM_ACCOUNTS_PAID_AMT_OF_PAYMENT;
		int result = commonMethodToUpdateData(query, pojo);
		return result;
	}
	
	@Override
	public int revertFinBookingFormAccountsStatementAmountOfPayment(FinBookingFormAccountsStatementPojo accStatementPojo) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.revertFinBookingFormAccountsStatementAmountOfPayment() *****");
		String query = SqlQuery.QRY_TO_REVERT_FIN_BOOKING_FORM_ACCOUNTS_STATEMENT_PAID_AMT_OF_PAYMENT;
		int result = commonMethodToUpdateData(query, accStatementPojo);
		return result;
	}
	
	@Override
	public int revertBookingFormAccountAmount(FinBookingFormAccountsPojo pojo) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.revertTransactionAmount() *****");
		String query = SqlQuery.QRY_TO_REVERT_FIN_BOOKING_FORM_ACCOUNTS_PAID_AMT;
		int result = commonMethodToUpdateData(query, pojo);
		return result;
	}

	@Override
	public int revertBookingFormAccountSummaryAmount(FinBookingFormAccountSummaryPojo pojo) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.revertBookingFormAccountSummaryAmount() *****");
		String query = SqlQuery.QRY_TO_REVERT_FIN_BOOKING_FORM_ACCOUNTS_SUMMARY;
		int result = commonMethodToUpdateData(query, pojo);
		return result;
	}

	@Override
	public int inActiveAccountStatementRecord(FinTransactionEntryDetailsPojo entryDetailsPojo) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.inActiveAccountStatementRecord() *****");
		String query = SqlQuery.QRY_TO_UPDATE_FIN_BOOKING_FORM_ACCOUNTS_STATEMENT.toString();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("ACTIVE_STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.INACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("BOOKING_FORM_ID", entryDetailsPojo.getBookingFormId(), Types.BIGINT);
		namedParameters.addValue("FIN_TRN_SET_OFF_ENT_ID", entryDetailsPojo.getTransactionSetOffEntryId(),Types.BIGINT);
		int result = nmdPJdbcTemplate.update(query, namedParameters);
		return result;
	}

	@Override
	public int inActiveReceiptRecord(FinTransactionEntryDetailsPojo entryDetailsPojo) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.inActiveReceiptRecord() *****");
		String query = SqlQuery.QRY_TO_UPDATE_FIN_BOOKING_FORM_RECEIPTS_STATUS.toString();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("ACTIVE_STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.INACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("BOOKING_FORM_ID", entryDetailsPojo.getBookingFormId(), Types.BIGINT);
		namedParameters.addValue("FIN_TRN_SET_OFF_ENT_ID", entryDetailsPojo.getTransactionSetOffEntryId(),Types.BIGINT);
		int result = nmdPJdbcTemplate.update(query, namedParameters);
		return result;
	}
	
	@Override
	public int inActiveBookingFormAccountPayment(FinBookingFormAccountPaymentDetailsPojo paymentRefundDetailsPojo) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.inActiveBookingFormAccountPayment() *****");
		//updating status for refund amount tables
		String query = SqlQuery.QRY_TO_UPDATE_FIN_BOOKING_FORM_ACCOUNT_PAYMENT_STATUS.toString();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("ACTIVE_STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.INACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("FIN_TRN_SET_OFF_ENT_ID", paymentRefundDetailsPojo.getTransactionSetOffEntryId(),Types.BIGINT);
		int result = nmdPJdbcTemplate.update(query, namedParameters);
		return result;
	}

	@Override
	public List<Map<String, Object>> getMilstonePaidOrNotPaidDetails(List<Long> projectMileStoneIds, Long[] bookingIds) {
		//log.info("***** control inside the EmployeeFinancialServiceDaoImpl.getMilstonePaidOrNotPaidDetails() *****");
		String query = FinancialQuerys.QRY_TO_GET_FIN_BOOKING_FORM_ACCOUNTS_PAID_NON_PAID_DATA.toString();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("PROJECT_MILESTONE_IDS", projectMileStoneIds, Types.BIGINT);
		namedParameters.addValue("BOOKING_FORM_IDS",Arrays.asList(bookingIds), Types.BIGINT);
		namedParameters.addValue("STATUS_ID",Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("PAYMENT_STATUS",Arrays.asList(Status.INPROGRESS.getStatus()), Types.BIGINT);
		return nmdPJdbcTemplate.queryForList(query, namedParameters);
	}
	
	@Override
	public Long updateMilstonePaidOrNotPaidStatus(Map<String, Object> accountDetails) {
		log.info("***** control inside the EmployeeFinancialServiceDaoImpl.updateMilstonePaidOrNotPaidStatus() *****");
		String query = SqlQuery.QRY_TO_UPDATE_FIN_BOOKING_FORM_ACCOUNTS_PAID_NON_PAID_DATA.toString();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FIN_BOOKING_FORM_ACCOUNTS_ID",Util.isEmptyObject(accountDetails.get("FIN_BOOKING_FORM_ACCOUNTS_ID"))?0l:accountDetails.get("FIN_BOOKING_FORM_ACCOUNTS_ID").toString(), Types.BIGINT);
		namedParameters.addValue("PAYMENT_STATUS",Status.PAID.getStatus(), Types.BIGINT);
		int result = nmdPJdbcTemplate.update(query, namedParameters);
		return (long) result;
	}
	
	@Override
	public List<FinBookingFormLglCostDtlsPojo> getLegalCostDetails( FinancialProjectMileStonePojo pojo) {
		String query = SqlQuery.QRY_TO_GET_FIN_BOOKING_FORM_LEGAL_COST;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FIN_BOK_FRM_LEGAL_COST_ID", pojo.getTypeId(), Types.BIGINT);
		namedParameters.addValue("TYPE", MetadataId.LEGAL_COST.getId(), Types.BIGINT);
		
		List<FinBookingFormLglCostDtlsPojo> pojos = (List<FinBookingFormLglCostDtlsPojo>) getData(
				query.toString(), namedParameters, FinBookingFormLglCostDtlsPojo.class);
		return pojos;
	}

	@Override
	public List<FinBookingFormModiCostDtlsPojo> getModificationCostDetails(FinancialProjectMileStonePojo pojo) {
		//log.info("***** EmployeeFinancialServiceDaoImpl.getModificationCostDetails() *****");
		String query = SqlQuery.QRY_TO_GET_FIN_BOOKING_FORM_MODI_COST_DTLS;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FIN_BOK_FRM_MODI_COST_ID", pojo.getTypeId(), Types.BIGINT);
		namedParameters.addValue("TYPE", MetadataId.MODIFICATION_COST.getId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		List<FinBookingFormModiCostDtlsPojo> pojos = (List<FinBookingFormModiCostDtlsPojo>) getData(
				query.toString(), namedParameters, FinBookingFormModiCostDtlsPojo.class);
		return pojos;
	}
	
	@Override//Maintenance charges
	public List<FinBookingFormMaintenanceDtlsPojo> loadMaintenance_ChargesDetails(FinancialProjectMileStonePojo pojo) {
		String query = FinancialQuerys.QRY_TO_GET_MAINTENANANCE_CHARGES_DETAILS;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FIN_BOK_FRM_MAINTENANCE_ID", pojo.getTypeId(), Types.BIGINT);
		List<FinBookingFormMaintenanceDtlsPojo> pojos = getData(query.toString(), namedParameters, FinBookingFormMaintenanceDtlsPojo.class);
		for (FinBookingFormMaintenanceDtlsPojo finBookingFormMaintenanceDtlsPojo : pojos) {
			finBookingFormMaintenanceDtlsPojo.setMaintenanceTaxPojos(loadMaintenance_ChargesTAXDetails(finBookingFormMaintenanceDtlsPojo));
		}
		return pojos;
	}
	
	@Override//Maintenance charges tax
	public List<FinBokFrmMaintenanceTaxPojo> loadMaintenance_ChargesTAXDetails(FinBookingFormMaintenanceDtlsPojo pojo) {
		String query = FinancialQuerys.QRY_TO_GET_MAINTENANANCE_CHARGES_TAX_DETAILS;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FIN_BOK_FORM_MAINTENANCE_DTLS_ID", pojo.getFinBokFormMaintenanceDtlsId(), Types.BIGINT);
		List<FinBokFrmMaintenanceTaxPojo> pojos = getData(query.toString(), namedParameters, FinBokFrmMaintenanceTaxPojo.class);
		return pojos;
	}

	@Override//Flar Khata charges
	public List<FinBookingFormFlatKhataDtlsPojo> loadFlatKhataDetails(FinancialProjectMileStonePojo pojo) {
		String query = FinancialQuerys.QRY_TO_GET_FLAT_KHATA_CHARGES_DETAILS;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FIN_BOK_FRM_FLAT_KHATA_ID", pojo.getTypeId(), Types.BIGINT);
		List<FinBookingFormFlatKhataDtlsPojo> pojos = getData(query.toString(), namedParameters, FinBookingFormFlatKhataDtlsPojo.class);
		for (FinBookingFormFlatKhataDtlsPojo finBookingFormMaintenanceDtlsPojo : pojos) {
			finBookingFormMaintenanceDtlsPojo.setFlatKhataTAXDetailsPojo(loadFlatKhataTAXDetails(finBookingFormMaintenanceDtlsPojo));
		}
		return pojos;
	}
	
	@Override//Flar Khata charges tax
	public List<FinBokFrmFlatKhataTaxPojo> loadFlatKhataTAXDetails(FinBookingFormFlatKhataDtlsPojo pojo) {
		String query = FinancialQuerys.QRY_TO_GET_FLAT_KHATA_CHARGES_TAX_DETAILS;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FIN_BOK_FORM_FLAT_KHATA_DTLS_ID", pojo.getFinBokFormFlatKhataDetailsId(), Types.BIGINT);
		List<FinBokFrmFlatKhataTaxPojo> pojos = getData(query.toString(), namedParameters, FinBokFrmFlatKhataTaxPojo.class);
		return pojos;
	}
	
	@Override
	public List<FinBookingFormModiCostPojo> getModificationCost(FinancialProjectMileStonePojo pojo) {
		//log.info("***** EmployeeFinancialServiceDaoImpl.getModificationCostDetails() *****");
		String query = SqlQuery.QRY_TO_GET_FIN_BOOKING_FORM_MODI_COST;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FIN_BOK_FRM_MODI_COST_ID", pojo.getTypeId(), Types.BIGINT);
		namedParameters.addValue("TYPE", MetadataId.MODIFICATION_COST.getId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		List<FinBookingFormModiCostPojo> modificationPojos = getData(query.toString(), namedParameters, FinBookingFormModiCostPojo.class);
		return modificationPojos;
	}

	@Override
	public List<Map<String, Object>> getFinBookingFormAccountType(FinancialProjectMileStoneInfo finProjDemandNoteInfo) {
		//log.info(" ***** EmployeeFinancialServiceDaoImpl.getFinBookingFormAccountType() *****");
		String query = SqlQuery.QRY_TO_GET_TYPE_FROM_BOOKING_FORM_ACCOUNTS_TABLE;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FIN_BOOKING_FORM_ACCOUNTS_ID",finProjDemandNoteInfo.getFinBookingFormAccountsId(), Types.BIGINT);
		List<Map<String, Object>> list = nmdPJdbcTemplate.queryForList(query, namedParameters);
		return list;
	}
	
	@Override
	public List<Map<String, Object>> getFinBookingFormAccountByTypeAndID(FinBookingFormAccountsInfo finBookingFormAccountsPojo) {
		String query = SqlQuery.QRY_TO_GET_TYPE_FROM_BOOKING_FORM_ACCOUNTS_BY_ID;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FIN_BOOKING_FORM_ACCOUNTS_ID",finBookingFormAccountsPojo.getFinBookingFormAccountsId(), Types.BIGINT);
		namedParameters.addValue("TYPE",finBookingFormAccountsPojo.getType(), Types.BIGINT);
		List<Map<String, Object>> list = nmdPJdbcTemplate.queryForList(query, namedParameters);
		return list;
	}
	
	@Override//not in use
	public void getExistingMileStoneDetailsForRegenerateDemandNote(FinancialProjectMileStoneInfo finProjDemandNoteInfo,
			CustomerPropertyDetailsInfo customerPropertyDetailsInfo) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.getExistingMileStoneDetailsForRegenerateDemandNote() *****");
		
	}
	
	@Override
	public Long savefinBookingFormTdsDetails(FinBookingFormTdsDetailsPojo pojo) {
		log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.savefinBookingFormTdsDetails() *****");
		String query = SqlQuery.QRY_TO_INSERT_FIN_BOOKING_FORM_TDS_DETAILS;
		Long pk = commonMethodToInsertData(query, pojo, "FIN_BOK_FRM_TDS_DTLS_ID");
		pojo.setFinBookingFormAccountsId(pk);
		return pk;
	}
	
	@Override
	public List<FinBookingFormTdsDetailsPojo> getMileStoneTDSDetails(FinancialProjectMileStonePojo pojo) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.getMileStoneTDSDetails() *****");
		String query=SqlQuery.QRY_TO_GET_FIN_BOOKING_FORM_TDS_DETAILS;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FIN_BOOKING_FORM_ACCOUNTS_ID", pojo.getFinBookingFormAccountsId()); 
		namedParameters.addValue("STATUS_ID", Arrays.asList(new Long[] {Status.ACTIVE.getStatus()}));
		
		List<FinBookingFormTdsDetailsPojo> demandNoteSchemeTaxDetailsList = (List<FinBookingFormTdsDetailsPojo>) getData(query.toString(), namedParameters, FinBookingFormTdsDetailsPojo.class);
		return demandNoteSchemeTaxDetailsList; 
	}
	
	@Override
	public int updateMileStoneTDSDetails(FinBookingFormTdsDetailsPojo pojo) {
		log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.updateMileStoneTDSDetails() ***** ");
		int result = nmdPJdbcTemplate.update(SqlQuery.QRY_TO_UPDATE_BOOKING_FORM_TDS_DETAILS, new BeanPropertySqlParameterSource(pojo));
		return result;
	}
	
//	@Override//not in use
//	public List<FinancialProjectMileStonePojo> getMileStoneDetailsFromBookingFormDemandNote(
//			FinBookingFormAccountsPojo finBookingFormAccountsPojo1) {
//		 log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.getMileStoneDetailsFromBookingFormDemandNote() *****");
//			String query = new StringBuilder("SELECT ").append(SqlQuery.QRY_TO_GET_MILESTONE_DETAILS).append(",FBFMSTM.AMOUNT,FBFMSTM.TOTAL_AMOUNT")
//					.append(" FROM FIN_BOOKING_FORM_MILESTONES FBFM,")
//					.append(" FIN_PROJECT_MILESTONES FPM,")
//					.append(" PERCENTAGES PERCENT,")
//					.append(" FIN_BOOKING_FORM_DEMAND_NOTE FBFDN,")
//					.append(" FIN_BOK_FRM_MST_SCH_TAX_MAP FBFMSTM")
//					.append(" WHERE FPM.PROJECT_MILESTONE_ID  = FBFM.PROJECT_MILESTONE_ID")
//					.append(" AND FBFMSTM.FIN_BOK_FOM_DMD_NOTE_ID=FBFDN.FIN_BOK_FOM_DMD_NOTE_ID")
//					.append(" AND PERCENT.PERCENTAGE_ID=FPM.PERCENTAGE_ID ")
//					.append(" AND FBFDN.FIN_BOK_FOM_DMD_NOTE_ID = FBFM.FIN_BOK_FOM_DMD_NOTE_ID and FBFM.FIN_BOK_FOM_DMD_NOTE_ID=:FIN_BOK_FOM_DMD_NOTE_ID")
//					.append("").toString();
//			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
//			namedParameters.addValue("FIN_BOK_FOM_DMD_NOTE_ID", finBookingFormAccountsPojo1.getTypeId(),Types.BIGINT);
//			
//			List<List<FinancialProjectMileStonePojo>> listOfFinancialServicePojo = nmdPJdbcTemplate.query(query.toString(),
//					namedParameters, new ExtractDataFromResultSet<FinancialProjectMileStonePojo>(FinancialProjectMileStonePojo.class) {});
//	 
//			if (listOfFinancialServicePojo.isEmpty()) {
//				listOfFinancialServicePojo.add(new ArrayList<FinancialProjectMileStonePojo>());
//			}
//			// here we have nested list object so we want only index zero object, as all data coming in first index of list object
//			return listOfFinancialServicePojo.get(0);
//	}
	
	@Override
	public List<FinBookingFormAccountSummaryPojo> getFlatBookingDetails(CustomerPropertyDetailsInfo customerPropertyDetailsInfo) {
		//log.info("***** The Control is inside the  EmployeeFinancialServiceDaoImpl.getFlatBookingDetails() ***** ");
		String query = SqlQuery.QRY_TO_GET_BOOKING_FORM_ACCOUNT_SUMMARY;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("BOOKING_FORM_ID", customerPropertyDetailsInfo.getFlatBookingId(), Types.BIGINT);
		namedParameters.addValue("TYPE", customerPropertyDetailsInfo.getType(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		
		List<FinBookingFormAccountSummaryPojo> listOfFinancialServicePojo = (List<FinBookingFormAccountSummaryPojo>) getData(query,namedParameters,FinBookingFormAccountSummaryPojo.class);
		return listOfFinancialServicePojo;
	}	
	
	@Override
	public int updateBookingFormAccountSummary(FinBookingFormAccountSummaryPojo accSummaryPojo) {
		log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.updateBookingFormAccountSummary() ***** ");
		StringBuffer query = new StringBuffer(SqlQuery.QRY_TO_UPDATE_BOOKING_FORM_ACCOUNT_SUMMARY);
		//accSummaryPojo.getType().equals(MetadataId.REFUNDABLE_ADVANCE.getId()) ||
		if(Util.isNotEmptyObject(accSummaryPojo.getType()) && accSummaryPojo.getType().equals(MetadataId.FLAT_CANCELLATION.getId())) {
					query.append(",PAID_AMOUNT = NVL(PAID_AMOUNT,0) + :payableAmount");
				}
				
				query.append(" WHERE BOOKING_FORM_ID = :bookingFormId")
				  .append(" AND TYPE = :type")
				  .append(" AND STATUS_ID = :statusId")
			;
		
		return nmdPJdbcTemplate.update(query.toString(), new BeanPropertySqlParameterSource(accSummaryPojo));	
	}
	
	@Override
	public int updateBookingFormAccountSummaryOnlyPayAmount(FinBookingFormAccountSummaryPojo accSummaryPojo) {
		StringBuffer query = new StringBuffer(SqlQuery.QRY_TO_UPDATE_BOOKING_FORM_ACCOUNT_SUMMARY_PAYABLE_AMT);
				
		query.append(" WHERE BOOKING_FORM_ID = :bookingFormId")
				  .append(" AND TYPE = :type")
				  .append(" AND STATUS_ID = :statusId");
		
		return nmdPJdbcTemplate.update(query.toString(), new BeanPropertySqlParameterSource(accSummaryPojo));
	}
	
	@Override
	public int updateBookingFormAccountSummaryRefundAmount(FinBookingFormAccountSummaryPojo accSummaryPojo) {
		StringBuffer query = new StringBuffer(SqlQuery.QRY_TO_UPDATE_BOOKING_FORM_ACCOUNT_SUMMARY_REFUND);
		//accSummaryPojo.getType().equals(MetadataId.REFUNDABLE_ADVANCE.getId()) || code commented
				if(!(accSummaryPojo.getType().equals(MetadataId.FLAT_CANCELLATION.getId()))) {
					query.append(",BALANCE_AMOUNT = NVL(BALANCE_AMOUNT,0) + :refundAmount");
				}
				
				query.append(" WHERE BOOKING_FORM_ID = :bookingFormId")
				.append(" AND TYPE = :type");
				
		//log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.updateBookingFormAccountSummaryRefundAmount() ***** ");
		int result =  nmdPJdbcTemplate.update(query.toString(), new BeanPropertySqlParameterSource(accSummaryPojo));
		return result;	
	}
	
	@Override
	public int updatePaidAmountBookingFormAccountSummary(FinBookingFormAccountSummaryPojo accSummaryPojo) {
		//log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.updatePaidAmountBookingFormAccountSummary() ***** ");
		int result =  nmdPJdbcTemplate.update(SqlQuery.QRY_TO_UPDATE_PAID_AMT_BOOKING_FORM_ACCOUNT_SUMMARY, new BeanPropertySqlParameterSource(accSummaryPojo));
		return	result;
	}
	
	@Override
	public Long saveBookingFormAccountSummary(FinBookingFormAccountSummaryPojo accSummaryPojo) {
		//log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.saveBookingFormAccountSummary() ***** ");
		Long pk = commonMethodToInsertData(SqlQuery.QRY_TO_INSERT_BOOKING_FORM_ACCOUNT_SUMMARY, accSummaryPojo, "FIN_BOK_FRM_ACC_SMRY_ID");
		accSummaryPojo.setFinBokFrmAccountSummryId(pk);
		return pk;
	}
	
	@Override
	public Long saveFinBokFrmDemNteSchTaxMap(FinBokFrmDemNteSchTaxMapPojo finBokFrmDemNteSchTaxMapPojo) {
		//log.info("** The Control is inside the saveFinBokFrmDemNteSchTaxMap in EmployeeFinancialServiceDaoImpl **");
		String query = SqlQuery.QRY_TO_INSERT_FIN_BOK_FRM_MST_SCH_TAX_MAP_DATA;
		////log.debug("** Generated query is ***"+query);
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		nmdPJdbcTemplate.update(query, new BeanPropertySqlParameterSource(finBokFrmDemNteSchTaxMapPojo), keyHolder, new String[] {"FIN_BOK_FRM_MST_SCH_TAX_MAP_ID"});
		//log.debug("** one record is inserted and the generated primary Key is **"+keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	}
	
	@Override
	public Long saveFinBookingFormAccounts(FinBookingFormAccountsPojo finBookingFormAccountsPojo) {
		//log.info("***** The Control is inside the saveFinBookingFormAccounts in EmployeeFinancialServiceDaoImpl *****");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		Long pk = nmdPJdbcTemplate.queryForObject("SELECT FIN_BOOKING_FORM_ACCOUNTS_SEQ.NEXTVAL FROM DUAL",namedParameters, Long.class);
		finBookingFormAccountsPojo.setFinBookingFormAccountsId(pk);
		if(finBookingFormAccountsPojo.getType()!=null && finBookingFormAccountsPojo.getType().equals(MetadataId.MODIFICATION_COST.getId())) {
			String finBokAccInvoiceNo = String.format("%04d",finBookingFormAccountsPojo.getTypeId());
			finBookingFormAccountsPojo.setFinBokAccInvoiceNo("INV"+finBokAccInvoiceNo);			
		} else if(finBookingFormAccountsPojo.getType()!=null && finBookingFormAccountsPojo.getType().equals(MetadataId.LEGAL_COST.getId())) {
			String finBokAccInvoiceNo = String.format("%04d",finBookingFormAccountsPojo.getTypeId());
			finBookingFormAccountsPojo.setFinBokAccInvoiceNo("INV"+finBokAccInvoiceNo);
		} else {
			String finBokAccInvoiceNo = String.format("%04d",pk);
			finBookingFormAccountsPojo.setFinBokAccInvoiceNo("INV"+finBokAccInvoiceNo);
		}
		String query = SqlQuery.QRY_TO_INSERT_FIN_BOOKING_FORM_ACCOUNTS_DATA;
		pk = commonMethodToInsertData(query, finBookingFormAccountsPojo, "FIN_BOOKING_FORM_ACCOUNTS_ID");
		finBookingFormAccountsPojo.setFinBookingFormAccountsId(pk);
		return pk;
	}
	
	@Override
	public int updateFinBookingFormAccounts(FinBookingFormAccountsPojo finBookingFormAccountsPojo) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.updateFinBookingFormAccounts() *****");
		String query = SqlQuery.QRY_TO_UPDATE_FIN_BOOKING_FORM_ACCOUNTS_DATA;
		int result =  nmdPJdbcTemplate.update(query, new BeanPropertySqlParameterSource(finBookingFormAccountsPojo));
		return result;
	}
	
	@Override
	public int updateFinBookingFormAccountsPayAmount(FinBookingFormAccountsPojo accountsPojo) {
		String query = FinancialQuerys.QRY_TO_UPDATE_FIN_BOOKING_FORM_ACC_PAY_AMT;
		int result =  nmdPJdbcTemplate.update(query, new BeanPropertySqlParameterSource(accountsPojo));
		return result;
	}
	
	//@Override
	public int editFinBookingForm_Milestones_Details(FinBookingFormMilestonesPojo bookingFormMilestonesPojo) {
		String query = SqlQuery.QRY_TO_EDIT_FIN_BOOKING_FORM_MILESTONES;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("demandNoteDate", bookingFormMilestonesPojo.getDemandNoteDate(), Types.TIMESTAMP);
		namedParameters.addValue("modifiedBy", bookingFormMilestonesPojo.getCreatedBy(), Types.BIGINT);
		
		namedParameters.addValue("finBookingFormMilestonesId",bookingFormMilestonesPojo.getFinBookingFormMilestonesId(), Types.BIGINT);
		namedParameters.addValue("statusId", bookingFormMilestonesPojo.getStatusId(), Types.BIGINT);
		namedParameters.addValue("bookingFormId", bookingFormMilestonesPojo.getBookingFormId(), Types.BIGINT);
		
		int result = nmdPJdbcTemplate.update(query, namedParameters);
		return result;
	}

	//@Override
	public int editFinBookingForm_Accounts_Details(FinBookingFormAccountsPojo bookingFormAccountsPojo) {
		String query = SqlQuery.QRY_TO_EDIT_FIN_BOOKING_FORM_ACCOUNTS_DATA;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("mileStoneDueDate", bookingFormAccountsPojo.getMileStoneDueDate(), Types.TIMESTAMP);
		namedParameters.addValue("isInterestCalcCompleted", bookingFormAccountsPojo.getIsInterestCalcCompleted(), Types.BIGINT);
		
		namedParameters.addValue("modifiedBy", bookingFormAccountsPojo.getCreatedBy(), Types.BIGINT);

		namedParameters.addValue("finBookingFormAccountsId",bookingFormAccountsPojo.getFinBookingFormAccountsId(), Types.BIGINT);
		namedParameters.addValue("statusId", bookingFormAccountsPojo.getStatusId(), Types.BIGINT);
		namedParameters.addValue("bookingFormId", bookingFormAccountsPojo.getBookingFormId(), Types.BIGINT);
		namedParameters.addValue("type", bookingFormAccountsPojo.getType(), Types.BIGINT);
		
		int result = nmdPJdbcTemplate.update(query, namedParameters);
		return result;
	}
	
	@Override
	public int saveMilestoneChangedDetails(List<FinMSChangedDtlsPojo> listOfChangedDetails) {
		SqlParameterSource[] sqlParameterSource = new SqlParameterSource[listOfChangedDetails.size()];
		int i=0;
		for(FinMSChangedDtlsPojo changedDtlsPojo : listOfChangedDetails) {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("TYPE", changedDtlsPojo.getType(), Types.BIGINT);
			namedParameters.addValue("TYPE_ID", changedDtlsPojo.getTypeId(), Types.BIGINT);
			namedParameters.addValue("ACTUAL_VALUE", changedDtlsPojo.getActualValue(), Types.VARCHAR);
			namedParameters.addValue("CHANGED_VALUE", changedDtlsPojo.getChangedValue(), Types.VARCHAR);
			namedParameters.addValue("EMP_ID", changedDtlsPojo.getEmpId(), Types.VARCHAR);
			namedParameters.addValue("REMARKS", changedDtlsPojo.getRemarks(), Types.VARCHAR);
			namedParameters.addValue("COLUMN_ID", changedDtlsPojo.getColumnId(), Types.BIGINT);
			namedParameters.addValue("ACTION_TYPE", changedDtlsPojo.getActionType(), Types.BIGINT);
			
			sqlParameterSource[i++] = namedParameters;
		}
		String query = SqlQuery.QRY_TO_INSERT_FIN_MILESTONE_CHANGED_DETAILS;
		//log.debug("*** generated query is ***"+query);
		int result[]  = nmdPJdbcTemplate.batchUpdate(query, sqlParameterSource);
		return result.length;
	}
	
	@Override
	public int updateBookingFormAccountPaidAmount(FinBookingFormAccountsPojo pojo) {
		//log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.updateBookingFormAccountPaidAmount() ***** ");
		String query = SqlQuery.QRY_TO_UPDATE_FIN_BOOKING_FORM_ACCOUNTS_PAID_AMT;
		return nmdPJdbcTemplate.update(query, new BeanPropertySqlParameterSource(pojo));
	}
	
	@Override
	public int updateBookingFormAccountOnlyPaidAmount(FinBookingFormAccountsPojo pojo) {
		//log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.updateBookingFormAccountPaidAmount() ***** ");
		String query = SqlQuery.QRY_TO_UPDATE_FIN_BOOKING_FORM_ACCOUNTS_PAID_AMT1;
		return nmdPJdbcTemplate.update(query, new BeanPropertySqlParameterSource(pojo));
	}
	
	@Override
	public int updateBookingFormAccountTaxAmountPaid(FinancialProjectMileStonePojo pojo) {
		//log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.updateBookingFormAccountTaxAmountPaid() ***** ");
		String query = SqlQuery.QRY_TO_UPDATE_FIN_BOOKING_FORM_ACCOUNTS_PAID_TAX_AMT;
		int result =  nmdPJdbcTemplate.update(query, new BeanPropertySqlParameterSource(pojo));
		return result;
	}
	
	@Override
	public int revertBookingFormAccountTaxAmountPaid(FinancialProjectMileStonePojo pojo) {
		//log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.revertBookingFormAccountTaxAmountPaid() ***** ");
		String query = SqlQuery.QRY_TO_REVERT_FIN_BOOKING_FORM_ACCOUNTS_PAID_TAX_AMT;
		int result =  nmdPJdbcTemplate.update(query, new BeanPropertySqlParameterSource(pojo));
		return result;
	}
	
	@Override
	public int updateBookingFormAccountPaidDate(FinBookingFormAccountsPojo bookingFormAccountsPojo) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.updateBookingFormAccountPaidDate() *****");
		String query = SqlQuery.QRY_TO_UPDATE_FIN_BOOKING_FORM_ACCOUNTS_PAID_DATE;
		int result = commonMethodToUpdateData(query, bookingFormAccountsPojo);
		return result;
	}

	@Override
	public int updateBookingFormAccountInterestDetails(FinBookingFormAccountsPojo bookingFormAccountsPojo) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.updateBookingFormAccountInterestDetails() *****");
		String query = SqlQuery.QRY_TO_UPDATE_FIN_BOOKING_FORM_ACCOUNTS_INTEREST_DETAILS;
		int result = commonMethodToUpdateData(query, bookingFormAccountsPojo);
		return result;
	}
	
	@Override
	public int updateBookingFormAccountStatus(FinPenaltyPojo finPenaltyPojo,
			MetadataId metadataid) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.updateBookingFormAccountStatus() *****");
		String query = "";
		int result = 0;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		if (metadataid.getId().equals(MetadataId.FIN_PENALTY.getId())) {
			query = SqlQuery.QRY_TO_UPDATE_FIN_BOOKING_FORM_ACCOUNTS_STATUS_OF_PENALTY_RECORD;
			namedParameters.addValue("TYPE", metadataid.getId(), Types.BIGINT);
			namedParameters.addValue("TYPE_ID", finPenaltyPojo.getFinPenaltyId(), Types.BIGINT);
			namedParameters.addValue("MODIFIED_BY", finPenaltyPojo.getCreatedBy(), Types.BIGINT);
			//namedParameters.addValue("MODIFIED_BY", finPenaltyPojo.getCreatedBy(), Types.BIGINT);
			namedParameters.addValue("ACTIVE_STATUS_ID",finPenaltyPojo.getActiveStatusId(), Types.BIGINT);
			result = nmdPJdbcTemplate.update(query, namedParameters);
		}
		return result;
	}

	@Override
	public int updateFinPenaltyStatus(FinBookingFormAccountsPojo bookingFormAccountsPojo) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.updateFinPenaltyRecords() *****");
		String query = SqlQuery.QRY_TO_UPDATE_FIN_PENALTY_STATUS;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID", Status.INACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("ACTIVE_STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("FIN_BOOKING_FORM_ACCOUNTS_ID", bookingFormAccountsPojo.getFinBookingFormAccountsId(), Types.BIGINT);
		
		int result = commonMethodToUpdateData(query, namedParameters);
		return result;
	}
	
	/**
	 * used methods transaction time, and excess amount time
	 */
	@Override
	public int decreaseBookingFormAccountPaidAmount(FinBookingFormAccountsPojo pojo) {
		//log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.updateBookingFormAccountPaidAmount() ***** ");
		String query = SqlQuery.QRY_TO_DESCREASE_BOOKING_FORM_ACCOUNTS_PAID_AMT;
		int result =  nmdPJdbcTemplate.update(query, new BeanPropertySqlParameterSource(pojo));
		return result;
	}
	
	@Override
	public int decreaseExtraAdjustedPaidAmount(FinBookingFormAccountsPojo pojo) {
		//log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.decreaseExtraAdjustedPaidAmount() ***** ");
		String query = FinancialQuerys.QRY_TO_DESCREASE_EXTRA_PAID_AMT;
		int result =  nmdPJdbcTemplate.update(query, new BeanPropertySqlParameterSource(pojo));
		return result;
	}
	
	@Override
	public int decreaseBookingFormAccountStatementPaidAmount(FinBookingFormAccountsStatementPojo pojo, String condition) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.decreaseBookingFormAccountStatementPaidAmount() *****");
		String query = null;
		if(condition!=null && condition.equalsIgnoreCase("DecreasePaidAmount")) {
			query = SqlQuery.QRY_TO_DESCREASE_BOOKING_FORM_ACCOUNTS_STATEMENT_PAID_AMT1;
		} else {
			query = SqlQuery.QRY_TO_DESCREASE_BOOKING_FORM_ACCOUNTS_STATEMENT_PAID_AMT;
		}
		int result =  nmdPJdbcTemplate.update(query, new BeanPropertySqlParameterSource(pojo));
		return result;
	}
	
	@Override
	public Long saveBookingFormReceiptsDetials(FinBookingFormReceiptsPojo pojo) {
		 //log.info("***** The Control is inside the  EmployeeFinancialServiceDaoImpl.saveBookingFormReceiptsDetials() ***** ");
		String query = SqlQuery.QRY_TO_INSERT_FIN_BOOKING_FORM_RECEIPTS_DETAILS;
		Long pk = commonMethodToInsertData(query, pojo, "FIN_BOK_FRM_RCPT_ID");
		return pk;
	}
	
	@Override
	public int checkIsThisReceiptTransactionAlreadyAdjusted(EmployeeFinancialTransactionServiceInfo transactionServiceInfo, FinEnum transactionType) {
		String query = SqlQuery.QRY_TO_GET_FIN_BOOKING_FORM_RECEIPTS_ENTRY_COUNT;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource(); 
		namedParameters.addValue("FIN_TRN_SET_OFF_ENT_ID", transactionServiceInfo.getTransactionSetOffEntryId(), Types.BIGINT);
		namedParameters.addValue("BOOKING_FORM_ID", transactionServiceInfo.getBookingFormId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		int result = nmdPJdbcTemplate.queryForObject(query, namedParameters, Integer.class);
		return result;
	}
	
	@Override
	public int checkIsThisPaymentRefundTransactionAlreadyAdjusted(EmployeeFinancialTransactionServiceInfo transactionServiceInfo, FinEnum payment) {
		String query = SqlQuery.QRY_TO_GET_FIN_BOOKING_FORM_PAYMENT_ENTRY_COUNT;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource(); 
		namedParameters.addValue("FIN_TRN_SET_OFF_ENT_ID", transactionServiceInfo.getTransactionSetOffEntryId(), Types.BIGINT);
		namedParameters.addValue("BOOKING_FORM_ID", transactionServiceInfo.getBookingFormId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		int result = nmdPJdbcTemplate.queryForObject(query, namedParameters, Integer.class);
		return result;
	}
	
	@Override
	public Long saveBookingFormExcessAmountUsage(FinBookingFormExcessAmountUsagePojo pojo) {
		//System.out.println("***** The Control is inside the  EmployeeFinancialServiceDaoImpl.saveBookingFormExcessAmountUsage() ***** "); 
		String query = SqlQuery.QRY_TO_INSERT_BOOKING_FORM_EXCESS_AMOUNT_USAGE_DETAILS;
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		nmdPJdbcTemplate.update(query, new BeanPropertySqlParameterSource(pojo), keyHolder, new String[] {"FIN_BOK_FRM_EXS_AMT_USG_ID"});
		//log.info("** one record is inserted and the generated primary Key is **"+keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	}
	
	@Override
	public List<FinBookingFormExcessAmountPojo> getExcessAmountDetails(CustomerPropertyDetailsInfo customerPropertyDetailsInfo, List<Long> metaDateId) {
		//log.info("***** The Control is inside the  EmployeeFinancialServiceDaoImpl.getExtraPaidAmountOnMileStone() ***** ");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder query = null;
	 
			if(isNotEmptyObject(customerPropertyDetailsInfo.getCondition()) && (customerPropertyDetailsInfo.getCondition().equals("PaymentRefund") 
					|| customerPropertyDetailsInfo.getCondition().equalsIgnoreCase(MetadataId.MODIFICATION_COST.getName())
					|| customerPropertyDetailsInfo.getCondition().equalsIgnoreCase(MetadataId.FIN_BOOKING_FORM_EXCESS_AMOUNT.getId().toString())
					)) {
				query = new StringBuilder(SqlQuery.QRY_TO_GET_BOOKING_FORM_EXCESS_AMOUNT_DETAILS_BY_ID)
						  .append(" AND FBFEA.TYPE IN (:TYPE)")
						  .append(" AND FBFR.BOOKING_FORM_ID = :BOOKING_FORM_ID")
						  .append(" AND FBFR.STATUS_ID = :STATUS_ID");
				
					query.append(Util.isNotEmptyObject(customerPropertyDetailsInfo.getTypeId())?" AND FBFEA.FIN_BOK_FRM_EXS_AMT_ID = :FIN_BOK_FRM_EXS_AMT_ID ":"");
					query.append(Util.isNotEmptyObject(customerPropertyDetailsInfo.getFinBokAccInvoiceNo())?" AND FBFEA.FIN_BOK_ACC_INVOICE_NO = :FIN_BOK_ACC_INVOICE_NO ":"");
				
			if (Util.isNotEmptyObject(customerPropertyDetailsInfo.getTransactionSetOffEntryId())) {
				query.append(" AND FBFR.FIN_TRN_SET_OFF_ENT_ID = :FIN_TRN_SET_OFF_ENT_ID");
				namedParameters.addValue("FIN_TRN_SET_OFF_ENT_ID", customerPropertyDetailsInfo.getTransactionSetOffEntryId(), Types.BIGINT);
			}
			namedParameters.addValue("FIN_BOK_FRM_EXS_AMT_ID", customerPropertyDetailsInfo.getTypeId(), Types.BIGINT);
			namedParameters.addValue("FIN_BOK_ACC_INVOICE_NO", customerPropertyDetailsInfo.getFinBokAccInvoiceNo(), Types.VARCHAR);
		}

		namedParameters.addValue("BOOKING_FORM_ID", customerPropertyDetailsInfo.getFlatBookingId(), Types.BIGINT);
		namedParameters.addValue("TYPE", metaDateId, Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(),Types.BIGINT);
		
		List<FinBookingFormExcessAmountPojo> bookingFormExcessAmtList  = (List<FinBookingFormExcessAmountPojo>) getData(query.toString(), namedParameters, FinBookingFormExcessAmountPojo.class);
		return bookingFormExcessAmtList;
	}
	
	@Override
	public List<FinBookingFormExcessAmountUsagePojo> getExcessAmountUsage(FinBookingFormExcessAmountPojo excessAmountPojo, EmployeeFinancialTransactionServiceInfo serviceInfo) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.getExcessAmountUsage() *****");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder query =new StringBuilder("SELECT ")
				.append(SqlQuery.QRY_TO_GET_FIN_BOOKING_FORM_EXCESS_AMOUNT_USAGE)
				.append(" FROM  FIN_BOOKING_FORM_EXCESS_AMOUNT_USAGE FBFEAU")
				.append(" WHERE FBFEAU.FIN_BOK_FRM_EXS_AMT_ID = :FIN_BOK_FRM_EXS_AMT_ID ");
		
		namedParameters.addValue("FIN_BOK_FRM_EXS_AMT_ID", excessAmountPojo.getFinBookingFormExcessAmountId(), Types.BIGINT);

		
		List<FinBookingFormExcessAmountUsagePojo> bookingFormExcessAmtList = (List<FinBookingFormExcessAmountUsagePojo>) getData(query.toString(), namedParameters, FinBookingFormExcessAmountUsagePojo.class);
		return bookingFormExcessAmtList;
	}
	
	@Override
	public Long saveBookingFormExcessAmount(FinBookingFormExcessAmountPojo pojo) {
		log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.saveBookingFormExcessAmount() ***** ");
		String query = SqlQuery.QRY_TO_INSERT_BOOKING_FORM_EXCESS_AMOUNT_DETAILS;
		Long pk = commonMethodToInsertData(query, pojo, "FIN_BOK_FRM_EXS_AMT_ID");
		return pk;
	}

	@Override
	public Long updateAndReduceExcessAmount(FinBookingFormExcessAmountPojo excessAmountPojo) {
		log.info("***** The Control is inside the The Control is inside the EmployeeFinancialServiceDaoImpl.updateReduceExcessAmount() *****");
		String query = SqlQuery.QRY_TO_DECREASE_BOOKING_FORM_EXCESS_AMOUNT_DETAILS;
		int result  = nmdPJdbcTemplate.update(query, new BeanPropertySqlParameterSource(excessAmountPojo));
		return Long.valueOf(result);
	}
	
	@Override
	public Long updateAndIncreaseExcessAmount(FinBookingFormExcessAmountPojo excessAmountPojo) {
		log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.updateAndIncreaseExcessAmount() ***** ");
		String query  =SqlQuery.QRY_TO_INCREASE_BOOKING_FORM_EXCESS_AMOUNT_DETAILS;
		int result  = nmdPJdbcTemplate.update(query, new BeanPropertySqlParameterSource(excessAmountPojo));
		return Long.valueOf(result);
	}
	
	@Override
	public Long updateAndRevertExcessAmount(FinBookingFormExcessAmountPojo excessAmountPojo) {
		log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.updateAndIncreaseExcessAmount() ***** ");
		String query = SqlQuery.QRY_TO_REVERT_BOOKING_FORM_EXCESS_AMOUNT_DETAILS;
		int result  = nmdPJdbcTemplate.update(query, new BeanPropertySqlParameterSource(excessAmountPojo));
		return Long.valueOf(result);
	}
	
	@Override
	public List<FinBookingFormAccountsStatementPojo> getBookingFormAccountsStatementData(FinBookingFormAccountsPojo finBookingFormAccountsPojo) {
		//log.info("***** The Control is inside the  EmployeeFinancialServiceDaoImpl.getAccountStatementData() ***** ");
		String query = new StringBuilder("SELECT ").append(SqlQuery.QRY_TO_GET_FIN_BOOKING_FORM_ACCOUNTS_STATEMENT_DETAILS)
				.append(" FROM FIN_BOOKING_FORM_ACCOUNTS_STATEMENT FBFAS ")
				.append(" WHERE FBFAS.FIN_BOOKING_FORM_ACCOUNTS_ID=:FIN_BOOKING_FORM_ACCOUNTS_ID ")
				.append(" AND FBFAS.STATUS_ID = :STATUS_ID")
				.append(" ORDER BY FBFAS.FIN_BOK_FRM_ACC_SMT_ID ")
				.toString();
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FIN_BOOKING_FORM_ACCOUNTS_ID", finBookingFormAccountsPojo.getFinBookingFormAccountsId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		
		
		List<FinBookingFormAccountsStatementPojo> accountsStatementPojos = (List<FinBookingFormAccountsStatementPojo>) getData(query, namedParameters, FinBookingFormAccountsStatementPojo.class);
		//log.info( "***** EmployeeFinancialServiceDaoImpl.getBookingFormAccountsStatementData() method execution completed *****"+accountsStatementPojos);
		return accountsStatementPojos;
	}
	
	@Override
	public List<FinBookingFormAccountsStatementPojo> getBookingFormAccountsStatementAndReceiptData(
			FinBookingFormAccountsPojo bookingFormAccountsPojo, FinEnum dataOrder) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.getBookingFormAccountsStatementAndReceiptData() *****");
		StringBuilder query = new StringBuilder( SqlQuery.QRY_TO_GET_BOOKING_FORM_STATEMENT_AND_RECEIPT_DATA)
				.append(" AND FBFAS.FIN_BOOKING_FORM_ACCOUNTS_ID=:FIN_BOOKING_FORM_ACCOUNTS_ID ")
				.append(" AND FBFR.BOOKING_FORM_ID = :BOOKING_FORM_ID")
				.append(" AND FBFR.STATUS_ID = :STATUS_ID")
				.append(" AND FBFAS.STATUS_ID = :STATUS_ID")
				.append(" ORDER BY FBFAS.FIN_BOK_FRM_ACC_SMT_ID ")
				.append(dataOrder.getName());
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FIN_BOOKING_FORM_ACCOUNTS_ID", bookingFormAccountsPojo.getFinBookingFormAccountsId(), Types.BIGINT);
		namedParameters.addValue("BOOKING_FORM_ID", bookingFormAccountsPojo.getBookingFormId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		
		List<FinBookingFormAccountsStatementPojo> accountsStatementPojos = (List<FinBookingFormAccountsStatementPojo>) getData(query.toString(), namedParameters, FinBookingFormAccountsStatementPojo.class);
		//log.info( "***** EmployeeFinancialServiceDaoImpl.getBookingFormAccountsStatementAndReceiptData() method execution completed *****"+accountsStatementPojos);
		return accountsStatementPojos;
	}
	
	
	@Override
	public Long saveFinBookingFormAccountsStatement(FinBookingFormAccountsStatementPojo pojo) {
		log.info("** The Control is inside the saveFinBookingFormAccountsStatement in EmployeeFinancialServiceDaoImpl **");
		String query = SqlQuery.QRY_TO_INSERT_FIN_BOOKING_FORM_ACCOUNTS_STATEMENT_DATA;
		Long pk = commonMethodToInsertData(query, pojo, "FIN_BOK_FRM_ACC_SMT_ID");
		return pk;
	}
	
	@Override
	public Long isPenaltyDetailsExistInAccountTable(FinPenalityInfo finPenalityInfo) {
		Long finBookingFormAccId = 0L;
		try {
			StringBuilder query = new StringBuilder(SqlQuery.QRY_TO_GET_COUNT_FIN_BOOKING_FORM_ACCOUNTS_RECORD)
					.append(" AND TYPE_ID  IN (SELECT FIN_PENALTY_ID FROM FIN_PENALTY WHERE FIN_BOOKING_FORM_ACCOUNTS_ID = :FIN_BOOKING_FORM_ACCOUNTS_ID) ")// AND STATUS_ID = :STATUS_ID 
					//.append(finPenalityInfo.getCondition()!=null && finPenalityInfo.getCondition().equals("ReceiptEditDelete")?"":" AND TYPE_ID = :TYPE_ID ")
					;
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("BOOKING_FORM_ID", finPenalityInfo.getBookingFormId(),Types.BIGINT);
			namedParameters.addValue("TYPE", MetadataId.FIN_PENALTY.getId(),Types.BIGINT);
			namedParameters.addValue("TYPE_ID", finPenalityInfo.getFinPenaltyId(),Types.BIGINT);
			namedParameters.addValue("FIN_BOOKING_FORM_ACCOUNTS_ID", finPenalityInfo.getFinBookingFormAccountsId(),Types.BIGINT);
			namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(),Types.BIGINT);
			finBookingFormAccId = nmdPJdbcTemplate.queryForObject(query.toString(),namedParameters, Long.class);
		} catch (Exception e) {
			log.info("EmployeeFinancialServiceDaoImpl.isPenaltyDetailsExistInAccountTable() exception  "+e.getMessage());
		}
		return finBookingFormAccId;
	}
	
	@Override
	public List<FinPenaltyPojo> getFinPenaltyDetails(FinBookingFormAccountsPojo bookingFormAccountsPojo) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.getFinPenaltyDetails() *****");
		StringBuilder query = new StringBuilder("SELECT ")
				.append(SqlQuery.QRY_TO_GET_FIN_PENALTY_DATA)
				.append(" FROM  FIN_PENALTY FP ")
				.append(" WHERE FP.FIN_BOOKING_FORM_ACCOUNTS_ID = :FIN_BOOKING_FORM_ACCOUNTS_ID ")
				.append(" AND FP.STATUS_ID = :STATUS_ID ");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FIN_BOOKING_FORM_ACCOUNTS_ID", bookingFormAccountsPojo.getFinBookingFormAccountsId() , Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus() , Types.BIGINT);
		
		//List<FinPenaltyPojo> penaltyPojos = (List<FinPenaltyPojo>) getData(query.toString(), namedParameters, FinPenaltyPojo.class);
		return getData(query.toString(), namedParameters, FinPenaltyPojo.class);
	}
	
	/**
	 * used this method in service impl class multiple times
	 */
	@Override
	public List<FinBookingFormAccountsPojo> getFinPenaltyDetailsForInterestWaiver(FinancialProjectMileStoneInfo mileStoneInfo) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.getFinPenaltyDetailsForInterestWaiver() *****");
		//loading penalty id using milestone details id's
		StringBuilder query = 
				  new StringBuilder("SELECT ")
				  .append(SqlQuery.QRY_TO_GET_FIN_BOOKING_FORM_ACCOUNTS_DATA)
				  .append(",NVL(FBFA.INTEREST_WAIVER_AMT,0) AS INTEREST_WAIVER_AMT")
				  .append(" FROM FIN_BOOKING_FORM_ACCOUNTS FBFA,FIN_PENALTY FP ")
				  .append(" WHERE TYPE = :PENALTY_TYPE ")
				  .append(" AND FBFA.BOOKING_FORM_ID = :BOOKING_FORM_ID ")
				  .append(" AND FP.STATUS_ID = :STATUS_ID ")
				  .append(" AND FBFA.STATUS_ID = :STATUS_ID ")
				  .append(" AND FP.FIN_PENALTY_ID = FBFA.TYPE_ID ")
				  .append(" AND FP.FIN_BOOKING_FORM_ACCOUNTS_ID = (SELECT  FIN_BOOKING_FORM_ACCOUNTS_ID FROM FIN_BOOKING_FORM_ACCOUNTS WHERE BOOKING_FORM_ID = :BOOKING_FORM_ID AND TYPE= :MS_TYPE AND TYPE_ID = :MS_TYPE_ID AND STATUS_ID = :STATUS_ID)")
				  ;
				  
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("MS_TYPE_ID", mileStoneInfo.getFinBookingFormMilestonesId() , Types.BIGINT);
		//MS_TYPE_ID is milestone table Primary key which is stored in FIN_BOOKING_FORM_ACCOUNTS table in type_id column
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus() , Types.BIGINT);
		namedParameters.addValue("PENALTY_TYPE", MetadataId.FIN_PENALTY.getId() , Types.BIGINT);
		namedParameters.addValue("MS_TYPE", MetadataId.FIN_BOOKING_FORM_MILESTONES.getId() , Types.BIGINT);
		namedParameters.addValue("BOOKING_FORM_ID", mileStoneInfo.getBookingFormId(),Types.BIGINT);
		List<FinBookingFormAccountsPojo> finBookingFormAccountsPojoLists = (List<FinBookingFormAccountsPojo>) getData(query.toString(), namedParameters, FinBookingFormAccountsPojo.class);
		//log.debug("*** The Extracted Data From Result Set Object is ***"+finBookingFormAccountsPojoLists);
		return finBookingFormAccountsPojoLists;
	}
	
	@Override
	public List<FinBookingFormAccountsPojo> getFinMilestoneDetailsForInterestWaiver1(FinancialProjectMileStoneInfo mileStoneInfo) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.getFinMilestoneDetailsForInterestWaiver1() *****");
		//loading milestone details using penalty id's
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		
		StringBuilder query = new StringBuilder("SELECT ")
				  .append(SqlQuery.QRY_TO_GET_FIN_BOOKING_FORM_ACCOUNTS_DATA).append(" FROM FIN_BOOKING_FORM_ACCOUNTS FBFA,FIN_PENALTY FP ")
				  .append(" WHERE TYPE = :MS_TYPE ")
				  .append(" AND FBFA.BOOKING_FORM_ID = :BOOKING_FORM_ID ")
				  .append(" AND FP.STATUS_ID = :STATUS_ID ")
				  .append(" AND FBFA.STATUS_ID = :STATUS_ID ")
				  .append(" AND FP.FIN_BOOKING_FORM_ACCOUNTS_ID = FBFA.FIN_BOOKING_FORM_ACCOUNTS_ID ")
				  .append(" AND FP.FIN_PENALTY_ID IN (SELECT TYPE_ID FROM FIN_BOOKING_FORM_ACCOUNTS WHERE BOOKING_FORM_ID = :BOOKING_FORM_ID AND TYPE= :PENALTY_TYPE AND FIN_BOOKING_FORM_ACCOUNTS_ID IN (:FIN_BOOKING_FORM_ACCOUNTS_ID) AND STATUS_ID = :STATUS_ID)")
				  ;
				  
		namedParameters.addValue("FIN_BOOKING_FORM_ACCOUNTS_ID", mileStoneInfo.getCurrentprojectMileStoneIds(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus() , Types.BIGINT);
		namedParameters.addValue("PENALTY_TYPE", MetadataId.FIN_PENALTY.getId() , Types.BIGINT);
		namedParameters.addValue("MS_TYPE", MetadataId.FIN_BOOKING_FORM_MILESTONES.getId() , Types.BIGINT);
		namedParameters.addValue("BOOKING_FORM_ID", mileStoneInfo.getBookingFormId(),Types.BIGINT);
		List<FinBookingFormAccountsPojo> finBookingFormAccountsPojoLists = (List<FinBookingFormAccountsPojo>) getData(query.toString(), namedParameters, FinBookingFormAccountsPojo.class);
		return finBookingFormAccountsPojoLists;
	}
	
	@Override
	public int upadteFinBookingFormAccountRecords(FinPenalityInfo finPenalityInfoNotInUse, FinBookingFormAccountsPojo finBookingFormAccountsPojo) {
		log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.upadteFinBookingFormAccountRecords() ***** ");
		/*//System.out.println(finPenalityInfo);
		//System.out.println(finBookingFormAccountsPojo);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FIN_BOOKING_FORM_ACCOUNTS_ID", finPenalityInfo.getFinBookingFormAccountsId() , Types.BIGINT);
		namedParameters.addValue("TYPE", MetadataId.FIN_PENALTY.getId(), Types.BIGINT);
		namedParameters.addValue("TYPE_ID", finPenalityInfo.getFinPenaltyId(), Types.BIGINT);
		namedParameters.addValue("PAY_AMOUNT", finPenalityInfo.getPenaltyTotalAmount(), Types.DOUBLE);
		namedParameters.addValue("TAX_AMOUNT", finPenalityInfo.getPenaltyTaxAmount(), Types.DOUBLE);
		namedParameters.addValue("PRINCIPAL_AMOUNT", finPenalityInfo.getPenaltyAmount(), Types.DOUBLE);
		namedParameters.addValue("BALANCE_AMOUNT", finPenalityInfo.getPenaltyTotalAmount(), Types.DOUBLE);
		namedParameters.addValue("STATUS_ID", finPenalityInfo.getStatusId(), Types.BIGINT);*/
		//int count = nmdPJdbcTemplate.update(SqlQuery.QRY_TO_UPDATE_FIN_BOOKING_FORM_ACCOUNTS_PENALTY_RECORD, namedParameters);
		int count = commonMethodToUpdateData(SqlQuery.QRY_TO_UPDATE_FIN_BOOKING_FORM_ACCOUNTS_PENALTY_RECORD, finBookingFormAccountsPojo);
		return count;
	}
	
	@Override
	public int upadteFinBookingFormAccountPenaltyRecordTypeId(FinPenalityInfo finPenalityInfo) {
		log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.upadteFinBookingFormAccountPenaltyRecordTypeId() *****");
		
		String query = "";

		if(finPenalityInfo.getCondition()!=null && finPenalityInfo.getCondition().equals("ReceiptEditDelete")) {
			 query = SqlQuery.QRY_TO_UPDATE_FIN_BOOKING_FORM_ACCOUNTS_PENALTY_TYPE_ID;
		}else {
			 query = SqlQuery.QRY_TO_UPDATE_FIN_BOOKING_FORM_ACCOUNTS_PENALTY_TYPE_ID1;
		}
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FIN_BOOKING_FORM_ACCOUNTS_ID", finPenalityInfo.getFinBookingFormAccountsId() , Types.BIGINT);
		namedParameters.addValue("TYPE", MetadataId.FIN_PENALTY.getId(), Types.BIGINT);
		namedParameters.addValue("TYPE_ID", finPenalityInfo.getFinPenaltyId(), Types.BIGINT);
		/*namedParameters.addValue("PAY_AMOUNT", finPenalityInfo.getPenaltyTotalAmount(), Types.DOUBLE);
		namedParameters.addValue("TAX_AMOUNT", finPenalityInfo.getPenaltyTaxAmount(), Types.DOUBLE);
		namedParameters.addValue("PRINCIPAL_AMOUNT", finPenalityInfo.getPenaltyAmount(), Types.DOUBLE);
		namedParameters.addValue("BALANCE_AMOUNT", finPenalityInfo.getPenaltyTotalAmount(), Types.DOUBLE);*/
		namedParameters.addValue("STATUS_ID", finPenalityInfo.getStatusId(), Types.BIGINT);
		int result = nmdPJdbcTemplate.update(query,new BeanPropertySqlParameterSource(finPenalityInfo));
		return result;
	}
	
	@Override
	public Long savePenaltyData(FinPenaltyPojo finPenaltyPojo, FinancialProjectMileStonePojo finBookingFormAccountsPojo, 
			FinBookingFormAccountSummaryPojo accSummaryPojo) {
		log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.savePenaltyData() ***** ");
		Integer finPenaltyId=0;
		@SuppressWarnings("unused")
		int result = 0;
		//GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		if(Util.isNotEmptyObject(finBookingFormAccountsPojo.getPaidAmount())) {
				try {
					MapSqlParameterSource namedParameters = new MapSqlParameterSource();
					namedParameters.addValue("TYPE", MetadataId.FIN_BOOKING_FORM_MILESTONES.getId(), Types.BIGINT);
					namedParameters.addValue("TYPE_ID",finBookingFormAccountsPojo.getFinBookingFormMilestonesId(), Types.BIGINT);
					namedParameters.addValue("FIN_BOOKING_FORM_ACCOUNTS_ID",finBookingFormAccountsPojo.getFinBookingFormAccountsId(), Types.BIGINT);
					namedParameters.addValue("BOOKING_FORM_ID",finBookingFormAccountsPojo.getBookingFormId(), Types.BIGINT);
					namedParameters.addValue("STATUS_ID",Status.ACTIVE.getStatus(), Types.BIGINT);
					finPenaltyId = nmdPJdbcTemplate.queryForObject(FinancialQuerys.QRY_TO_GET_COUNT_FIN_PENALTY_RECORD,namedParameters,Integer.class);
				} catch (Exception e) {
					log.info("EmployeeFinancialServiceDaoImpl.savePenaltyData() exception  "+e.getMessage());
				}

				if(finPenaltyId!=null&&finPenaltyId!=0) {
					//finPenaltyPojo.setFinPenaltyId(Long.valueOf(finPenaltyId));
					//count = nmdPJdbcTemplate.update(SqlQuery.QRY_TO_DELETE_FIN_PENALTY_RECORD, new BeanPropertySqlParameterSource(finBookingFormAccountsPojo));
					result = nmdPJdbcTemplate.update(SqlQuery.QRY_TO_UPDATE_FIN_PENALTY_RECORD, new BeanPropertySqlParameterSource(finPenaltyPojo));
					StringBuffer query = new StringBuffer(SqlQuery.QRY_TO_UPDATE_BOOKING_FORM_ACCOUNT_SUMMARY);
							query.append(" WHERE BOOKING_FORM_ID = :bookingFormId")
							  	 .append(" AND TYPE = :type")
							  	 .append(" AND STATUS_ID = :statusId");
					result = nmdPJdbcTemplate.update(query.toString(), new BeanPropertySqlParameterSource(accSummaryPojo));
					return Long.valueOf(finPenaltyId);
				}	
		}

		String query = SqlQuery.QRY_TO_INSERT_FIN_PENALTY_DATA;
		
		Long penaltyKey = commonMethodToInsertData(query, finPenaltyPojo, "FIN_PENALTY_ID");		
		
		CustomerPropertyDetailsInfo info = new CustomerPropertyDetailsInfo();
		info.setFlatBookingId(accSummaryPojo.getBookingFormId());
		info.setType(accSummaryPojo.getType());
		List<FinBookingFormAccountSummaryPojo> listOfBookingAccountSummaryDetails = getFlatBookingDetails(info);
		if(!listOfBookingAccountSummaryDetails.isEmpty()) {
			StringBuffer queryForAccSummary = new StringBuffer(SqlQuery.QRY_TO_UPDATE_BOOKING_FORM_ACCOUNT_SUMMARY);
			queryForAccSummary.append(" WHERE BOOKING_FORM_ID = :bookingFormId")
			  	 .append(" AND TYPE = :type")
			  	 .append(" AND STATUS_ID = :statusId").toString();
			result = nmdPJdbcTemplate.update(queryForAccSummary.toString(), new BeanPropertySqlParameterSource(accSummaryPojo));
		} else {
			commonMethodToInsertData(SqlQuery.QRY_TO_INSERT_BOOKING_FORM_ACCOUNT_SUMMARY, accSummaryPojo, "FIN_BOK_FRM_ACC_SMRY_ID");
		}
		
 		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("IS_INTEREST_APPLICABLE", Status.YES.getStatus(),Types.BIGINT);
		namedParameters.addValue("FIN_BOOKING_FORM_ACCOUNTS_ID",finBookingFormAccountsPojo.getFinBookingFormAccountsId(),Types.BIGINT);
		namedParameters.addValue("TYPE", MetadataId.FIN_BOOKING_FORM_MILESTONES.getId(),Types.BIGINT);
		namedParameters.addValue("TYPE_ID",finBookingFormAccountsPojo.getFinBookingFormMilestonesId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID",Status.ACTIVE.getStatus(), Types.BIGINT);
		
		result = nmdPJdbcTemplate.update(SqlQuery.QRY_TO_UPDATE_FIN_BOOKING_FORM_ACCOUNTS, namedParameters);
		return penaltyKey;
	}
	
	@Override
	public List<DropDownPojo> getAllIncompletedEmpSitesList(EmployeeFinancialRequest employeeFinancialRequest) throws InSufficeientInputException {
		//log.info("*** The Control is inside the getAllIncompletedEmpSitesList in EmployeeFinancialServiceDaoImpl ***");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = null;
		if(employeeFinancialRequest.getActionUrl().equalsIgnoreCase("Sites_List")) {
			query = SqlQuery.QRY_TO_GET_ALL_EMP_ACCESSED_SITES_LIST;
			namedParameters.addValue("STATUS_ID", Status.COMPLETED.getStatus(),Types.BIGINT);
			namedParameters.addValue("SITE_IDS", employeeFinancialRequest.getSiteIds(),Types.BIGINT);
		}else if(employeeFinancialRequest.getActionUrl().equalsIgnoreCase("Blocks_List")) {
			query = SqlQuery.QRY_TO_GET_ALL_INCOMPLETED_BLOCKS_TO_FILL_MILESTONES;
			namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(),Types.BIGINT);
			namedParameters.addValue("SITE_ID", employeeFinancialRequest.getSiteId(),Types.BIGINT);
		}
		//log.debug("*** generated query and Sql Parameters Sources are ***"+query +"***"+namedParameters);
		List<List<DropDownPojo>> sitesLists = nmdPJdbcTemplate.query(query, namedParameters, 
				new ExtractDataFromResultSet<DropDownPojo>(DropDownPojo.class){});
		//log.debug("*** DropDownPojo all Accessed Sites List are ***" + sitesLists);
		if (sitesLists.isEmpty()) {
			sitesLists.add(new ArrayList<DropDownPojo>());
		}
		//here we have nested list object so we want only index zero object, as all data coming in first index of list object
		return sitesLists.get(0);		
	}

	@Override
	public Long savePenaltyStatisticsData(FinPenaltyStatisticsPojo finPenaltyStatisticsPojo,
			FinancialProjectMileStonePojo finBookingFormAccountsPojo__NotInUse) {
		log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.savePenaltyStatisticsData() ***** ");
		String query = SqlQuery.QRY_TO_INSERT_FIN_PENALTY_STATISTICS_DATA;
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		nmdPJdbcTemplate.update(query, new BeanPropertySqlParameterSource(finPenaltyStatisticsPojo), keyHolder, new String[] {"FIN_PNT_STAT_ID"});
		//log.info("** one record is inserted into and the generated primary Key is **"+keyHolder.getKey().longValue());
		Long finPenaltyStatisticsId = keyHolder.getKey().longValue();
		finPenaltyStatisticsPojo.setFinPenaltyStatisticsId(finPenaltyStatisticsId);
		return finPenaltyStatisticsId;
	}

	/**
	 * used this method for delete transaction also
	 */
	@Override
	public List<FinPenaltyStatisticsPojo> loadPenaltyStatisticsData(FinancialProjectMileStonePojo financialProjectMileStonePojo) {
		log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.savePenaltyStatisticsData() ***** ");
		String query = FinancialQuerys.QRY_TO_LOAD_PENALTY_STATISTICS_DATA;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();		
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(),Types.BIGINT);
		namedParameters.addValue("FIN_PENALTY_ID", financialProjectMileStonePojo.getTypeId(),Types.BIGINT);
		
		List<FinPenaltyStatisticsPojo> penaltyStatisticsPojo = (List<FinPenaltyStatisticsPojo>) getData(query, namedParameters, FinPenaltyStatisticsPojo.class);
		return penaltyStatisticsPojo;
	}
	
	@Override
	public List<FinPenaltyTaxMappingPojo> loadPenaltyTaxMappigData(FinPenaltyStatisticsPojo paidpenaltyStatisticsPojo) {
		String query = FinancialQuerys.QRY_TO_LOAD_FIN_PENALTY_TAX_MAPPING;
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();		
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(),Types.BIGINT);
		namedParameters.addValue("FIN_PNT_STAT_ID", paidpenaltyStatisticsPojo.getFinPenaltyStatisticsId(),Types.BIGINT);
		
		List<FinPenaltyTaxMappingPojo> penaltyStatisticsPojo = getData(query, namedParameters, FinPenaltyTaxMappingPojo.class);
		return penaltyStatisticsPojo;
	}
	
	@Override
	public int updatePenaltyStasticsPaidAmount(FinPenaltyStatisticsPojo penaltyAdjustmentInfo) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();		
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(),Types.BIGINT);
		namedParameters.addValue("FIN_PNT_STAT_ID", penaltyAdjustmentInfo.getFinPenaltyStatisticsId(),Types.BIGINT);
		
		namedParameters.addValue("PAID_TAX_AMOUNT",penaltyAdjustmentInfo.getPaidPenaltyTaxAmount(),Types.DOUBLE);
		namedParameters.addValue("PAID_AMOUNT", penaltyAdjustmentInfo.getPaidPenaltyAmount(),Types.DOUBLE);
		
		int result = nmdPJdbcTemplate.update(FinancialQuerys.QRY_TO_UPDATE_PENALTY_STATISTICS_PAID_DATA, namedParameters);
		return result;
	}
	
	/**
	 * Resting all the paid amount of penalty records
	 */
	@Override
	public int resetPenaltyStasticsPaidAmount(FinPenaltyStatisticsPojo penaltyAdjustmentInfo) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();		
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(),Types.BIGINT);
		namedParameters.addValue("FIN_PNT_STAT_ID", penaltyAdjustmentInfo.getFinPenaltyStatisticsId(),Types.BIGINT);
		
		namedParameters.addValue("PAID_TAX_AMOUNT",0.0,Types.DOUBLE);
		namedParameters.addValue("PAID_AMOUNT", 0.0,Types.DOUBLE);
		
		int result = nmdPJdbcTemplate.update(FinancialQuerys.RESET_PENALTY_STATISTICS_PAID_DATA, namedParameters);
		return result;
	}
	
	@Override
	public int updateOldPenaltyStasticsTotalAmt(FinPenaltyStatisticsPojo penaltyAdjustmentInfo) {

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();		
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(),Types.BIGINT);
		namedParameters.addValue("FIN_PNT_STAT_ID", penaltyAdjustmentInfo.getFinPenaltyStatisticsId(),Types.BIGINT);
		//namedParameters.addValue("MODIFIED_BY",penaltyAdjustmentInfo.getCreatedBy(),Types.BIGINT);
		namedParameters.addValue("TAX_AMOUNT",penaltyAdjustmentInfo.getPenaltyTaxAmount(),Types.DOUBLE);
		namedParameters.addValue("STAT_TOTAL_AMOUNT",  penaltyAdjustmentInfo.getStatTotalAmount(),Types.DOUBLE);
		
		int result = nmdPJdbcTemplate.update(FinancialQuerys.UPDATE_OLD_PENALTY_STATISTICS_PAID_DATA, namedParameters);
		return result;
	}

	@Override
	public int decreasePenaltyStasticsPaidAmount(FinPenaltyStatisticsPojo penaltyAdjustmentInfo) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();		
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(),Types.BIGINT);
		namedParameters.addValue("FIN_PNT_STAT_ID", penaltyAdjustmentInfo.getFinPenaltyStatisticsId(),Types.BIGINT);
		//namedParameters.addValue("MODIFIED_BY",penaltyAdjustmentInfo.getCreatedBy(),Types.BIGINT);
		
		namedParameters.addValue("PAID_TAX_AMOUNT",penaltyAdjustmentInfo.getPaidPenaltyTaxAmount(),Types.DOUBLE);
		namedParameters.addValue("PAID_AMOUNT", penaltyAdjustmentInfo.getPaidPenaltyAmount(),Types.DOUBLE);
		
		int result = nmdPJdbcTemplate.update(FinancialQuerys.QRY_TO_DESCREASE_PENALTY_STATISTICS_PAID_DATA, namedParameters);
		return result;		
	}
	
	@Override
	public int updatePenaltyTaxPaidAmount(FinPenaltyTaxMappingPojo penaltyAdjustmentInfo) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		//namedParameters.addValue("MODIFIED_BY",penaltyAdjustmentInfo.getCreatedBy(),Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(),Types.BIGINT);
		namedParameters.addValue("FIN_PEN_TAX_MAP_ID", penaltyAdjustmentInfo.getFinPenTaxMapId(),Types.BIGINT);
		namedParameters.addValue("TAX_PAID_AMOUNT", penaltyAdjustmentInfo.getTaxPaidPmount(),Types.DOUBLE);
		namedParameters.addValue("PAID_TOTAL_INTEREST_AMOUNT", penaltyAdjustmentInfo.getPaidTotalInterestAmount(),Types.DOUBLE);
		int result = nmdPJdbcTemplate.update(FinancialQuerys.QRY_TO_UPDATE_PENALTY_TAX_DATA, namedParameters);
		return result;
	}

	@Override
	public int resetPenaltyTaxPaidAmount(FinPenaltyTaxMappingPojo penaltyAdjustmentInfo) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		//namedParameters.addValue("MODIFIED_BY",penaltyAdjustmentInfo.getCreatedBy(),Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(),Types.BIGINT);
		namedParameters.addValue("FIN_PEN_TAX_MAP_ID", penaltyAdjustmentInfo.getFinPenTaxMapId(),Types.BIGINT);
		namedParameters.addValue("TAX_PAID_AMOUNT", 0.0,Types.DOUBLE);
		namedParameters.addValue("PAID_TOTAL_INTEREST_AMOUNT", 0.0,Types.DOUBLE);
		int result = nmdPJdbcTemplate.update(FinancialQuerys.RESET_PENALTY_TAX_DATA, namedParameters);
		return result;
	}
	
	@Override
	public int updateOldPenaltyTaxTotalAmt(FinPenaltyTaxMappingPojo penaltyAdjustmentInfo) {
		/*MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(),Types.BIGINT);
		namedParameters.addValue("FIN_PEN_TAX_MAP_ID", penaltyAdjustmentInfo.getFinPenTaxMapId(),Types.BIGINT);
		
		namedParameters.addValue("TAX_PAID_AMOUNT", 0.0,Types.DOUBLE);
		namedParameters.addValue("PAID_TOTAL_INTEREST_AMOUNT", 0.0,Types.DOUBLE);*/
		
		int result = commonMethodToUpdateData(FinancialQuerys.UPDATE_OLD_RECORDS_PENALTY_TAX_DATA, penaltyAdjustmentInfo);
		 //result = nmdPJdbcTemplate.update(FinancialQuerys.UPDATE_OLD_RECORDS_PENALTY_TAX_DATA, namedParameters);
		 return result;
	}
	
	@Override
	public int decreasePenaltyTaxPaidAmount(FinPenaltyTaxMappingPojo penaltyAdjustmentInfo) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(),Types.BIGINT);
		namedParameters.addValue("FIN_PEN_TAX_MAP_ID", penaltyAdjustmentInfo.getFinPenTaxMapId(),Types.BIGINT);
		namedParameters.addValue("TAX_PAID_AMOUNT", penaltyAdjustmentInfo.getTaxPaidPmount(),Types.DOUBLE);
		namedParameters.addValue("PAID_TOTAL_INTEREST_AMOUNT", penaltyAdjustmentInfo.getPaidTotalInterestAmount(),Types.DOUBLE);
		int result = nmdPJdbcTemplate.update(FinancialQuerys.QRY_TO_DECREASE_PENALTY_PAID_TAX_DATA, namedParameters);
		return result;
	}

	
	@Override
	public List<FinancialMileStoneClassifidesPojo> getAllAliasNamesForMileStone(EmployeeFinancialRequest employeeFinancialRequest) {
		//log.info("*** The Control is inside the getAllAliasNamesForMileStone in EmployeeFinancialServiceDaoImpl ***");
		String query = SqlQuery.QRY_TO_GET_ALL_ALIAS_NAMES_OF_SITES_FOR_MILESTONES;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("SITE_ID", employeeFinancialRequest.getSiteId(),Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(),Types.BIGINT);
		//log.debug("*** generated query and Sql Parameters Sources are ***"+query +"***"+namedParameters);
		List<List<FinancialMileStoneClassifidesPojo>> aliasNamesLists = nmdPJdbcTemplate.query(query, namedParameters, 
				new ExtractDataFromResultSet<FinancialMileStoneClassifidesPojo>(FinancialMileStoneClassifidesPojo.class){});
		//log.debug("*** FinancialMileStoneClassifidesPojo all Alias Names List are ***" + aliasNamesLists);
		if (aliasNamesLists.isEmpty()) {
			aliasNamesLists.add(new ArrayList<FinancialMileStoneClassifidesPojo>());
		}
		//here we have nested list object so we want only index zero object, as all data coming in first index of list object
		return aliasNamesLists.get(0);	
	}

	@Override
	public List<PercentagesPojo> getMileStonePercentages(EmployeeFinancialRequest employeeFinancialRequest) {
		//log.info("*** The Control is inside the getMileStonePercentages in EmployeeFinancialServiceDaoImpl ***");
		String query = SqlQuery.QRY_TO_GET_ALL_PERCENTAGES_FOR_MILESTONES;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(),Types.BIGINT);
		//log.debug("*** generated query and Sql Parameters Sources are ***"+query +"***"+namedParameters);
		
		
		List<PercentagesPojo> mileStonePercentagesLists = (List<PercentagesPojo>) getData(query, namedParameters, PercentagesPojo.class);
		
		/*List<List<PercentagesPojo>> mileStonePercentagesLists = nmdPJdbcTemplate.query(query, namedParameters, 
				new ExtractDataFromResultSet<PercentagesPojo>(PercentagesPojo.class){});
		log.debug("*** PercentagesPojo all milestone percentages List is ***" + mileStonePercentagesLists);
		if (mileStonePercentagesLists.isEmpty()) {
			mileStonePercentagesLists.add(new ArrayList<PercentagesPojo>());
		}*/
		//here we have nested list object so we want only index zero object, as all data coming in first index of list object
		return mileStonePercentagesLists;	
	}

	@Override
	public Long saveFinMileStoneClassifides(FinancialMileStoneClassifidesPojo financialMileStoneClassifidesPojo) {
		log.info("*** The Control is inside the saveFinMileStoneClassifides in EmployeeFinancialServiceDaoImpl ***");
		String query = SqlQuery.QRY_TO_INSERT_FIN_MILESTONE_CLASSIFIEDES;
		//log.debug("*** generated query is ***"+query);
		GeneratedKeyHolder keyHolder= new GeneratedKeyHolder();
		nmdPJdbcTemplate.update(query, new BeanPropertySqlParameterSource(financialMileStoneClassifidesPojo), keyHolder, new String[] {"FIN_MILESTONE_CLASSIFIDES_ID"});
		//log.debug("*** one record is inserted and the generated primary key is ***"+keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	}

	@Override
	public void saveFinancialMileStoneClassifideMappingBlocks(EmployeeFinancialRequest employeeFinancialRequest) {
		log.info("*** The Control is inside the saveFinancialMileStoneClassifideMappingBlocks in EmployeeFinancialServiceDaoImpl ***");
		String query = SqlQuery.QRY_TO_INSERT_FIN_MILESTONE_CLASSIFIEDES_MAPPING_BLOCKS;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		namedParameters.addValue("FIN_MILESTON_CLASSIFIDES_ID", employeeFinancialRequest.getFinMilestoneClassifidesId(),Types.BIGINT);
		namedParameters.addValue("CREATED_BY", employeeFinancialRequest.getEmpId(), Types.BIGINT);
		//log.debug("*** generated query and SqlParameterSource are ***"+query+"***"+namedParameters);
		for(Long blockId : employeeFinancialRequest.getBlockIds()) {
			namedParameters.addValue("BLOCK_ID", blockId, Types.BIGINT);
			nmdPJdbcTemplate.update(query, namedParameters, keyHolder, new String[] {"PRO_MIL_CLS_MAPPING_BLOCKS_ID"});	
			//log.debug("*** one record is inserted and the generated primary key is ***"+keyHolder.getKey().longValue());
		}
	}

	@Override
	public void saveFinancialProjectMileStoneClassifides(List<FinancialProjectMileStonePojo> financialProjectMileStonePojosList) {
		log.info("*** The Control is inside the saveFinancialProjectMileStoneClassifides in EmployeeFinancialServiceDaoImpl ***");
		SqlParameterSource[] sqlParameterSource = new SqlParameterSource[financialProjectMileStonePojosList.size()];
		int i=0;
		for(FinancialProjectMileStonePojo financialProjectMileStonePojo : financialProjectMileStonePojosList) {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("MILESTONE_NAME", financialProjectMileStonePojo.getMilestoneName(), Types.VARCHAR);
			namedParameters.addValue("FIN_MILESTONE_CLASSIFIDES_ID", financialProjectMileStonePojo.getFinMilestoneClassifidesId(), Types.BIGINT);
			namedParameters.addValue("PERCENTAGE_ID", financialProjectMileStonePojo.getPercentagesId(), Types.BIGINT);
			namedParameters.addValue("CREATED_BY", financialProjectMileStonePojo.getCreatedBy(), Types.BIGINT);
			namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
			namedParameters.addValue("MILE_STONE_NO", financialProjectMileStonePojo.getMileStoneNo(), Types.BIGINT);
			namedParameters.addValue("MILESTONE_DATE", financialProjectMileStonePojo.getMilestoneDate(), Types.TIMESTAMP);
			namedParameters.addValue("PROJECT_MS_STATUS_ID", Status.MS_INCOMPLETE.getStatus(), Types.BIGINT);
			sqlParameterSource[i++] = namedParameters;
		}
		String query = SqlQuery.QRY_TO_INSERT_FIN_PROJECT_MILESTONE_CLASSIFIEDES;
		//log.debug("*** generated query is ***"+query);
		nmdPJdbcTemplate.batchUpdate(query, sqlParameterSource);
	}
	
	@Override
	public List<SiteDetailsPojo> getRaisedMilestoneSites(EmployeeFinancialServiceInfo employeeFinancialServiceInfo) {
		//log.info("*** The Control is inside the getRaisedMilestoneSites in EmployeeFinancialServiceDaoImpl ***");
		StringBuilder query = new StringBuilder(SqlQuery.QRY_TO_GET_RAISED_MILESTONE_SITES);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("SITE_IDS", employeeFinancialServiceInfo.getSiteIds(), Types.BIGINT);
		//log.debug("*** generated query and SqlParameterSource are ***"+query+"***"+namedParameters);
		
		
		List<SiteDetailsPojo> siteDetailsPojoLists = (List<SiteDetailsPojo>) getData(query.toString(), namedParameters, SiteDetailsPojo.class);
		
		/*List<List<SiteDetailsPojo>> siteDetailsPojoLists = nmdPJdbcTemplate.query(query.toString(), namedParameters, 
				new ExtractDataFromResultSet<SiteDetailsPojo>(SiteDetailsPojo.class){});
		if(siteDetailsPojoLists.isEmpty()) {
			siteDetailsPojoLists.add(new ArrayList<SiteDetailsPojo>());
		}*/
		//log.debug("*** The Extracted Data From Result Set Object is ***"+siteDetailsPojoLists);
		return siteDetailsPojoLists;
	}

	@Override
	public List<SiteDetailsPojo> getActiveBlocksFlats(EmployeeFinancialServiceInfo employeeFinancialServiceInfo) {
		//log.info("*** The Control is inside the getActiveBlocksFlats in EmployeeFinancialServiceDaoImpl ***");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder query = new StringBuilder();
		/*boolean isSiteIdIsEmpty = (Util.isNotEmptyObject(employeeFinancialServiceInfo.getSiteIds()));
		boolean isBlockIdIsEmpty = Util.isNotEmptyObject(employeeFinancialServiceInfo.getBlockIds());
		boolean isFloorIdIsEmpty = Util.isNotEmptyObject(employeeFinancialServiceInfo.getFloorIds());
		boolean isFlatIDIsEmpty = Util.isNotEmptyObject(employeeFinancialServiceInfo.getFlatIds());*/
		
		if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getActionUrl()) && employeeFinancialServiceInfo.getActionUrl().equals("LoadBlockFloorFlatDetails11")) {

			query = new StringBuilder();
			query.append(SqlQuery.QRY_TO_GET_FLAT_FLAT_BOOKING_FLOOR_BLOCK_SITE_DETAILS);
			query.append(" WHERE B.STATUS_ID=:STATUS_ID AND F.STATUS_ID=:STATUS_ID  AND F.STATUS_ID=:STATUS_ID AND FD.STATUS_ID = :STATUS_ID ");
			if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getSiteIds())) {
				query.append(" AND S.SITE_ID IN (:SITE_ID) ");
				namedParameters.addValue("SITE_ID",employeeFinancialServiceInfo.getSiteIds());
			} else if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getSiteId())) {
				query.append(" AND S.SITE_ID IN (:SITE_ID) ");
				namedParameters.addValue("SITE_ID", employeeFinancialServiceInfo.getSiteId());
			}
			if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getBlockIds())) {
				query.append(" AND B.BLOCK_ID  IN (:BLOCK_ID) ");
				namedParameters.addValue("BLOCK_ID", employeeFinancialServiceInfo.getBlockIds());
			}
			if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getFloorIds())) {
				query.append(" AND FLD.FLOOR_ID IN (:FLOOR_ID) ");
				namedParameters.addValue("FLOOR_ID", employeeFinancialServiceInfo.getFloorIds(), Types.BIGINT);
			}
			if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getFlatIds())) {
				query.append(" AND FD.FLAT_ID IN (:FLAT_ID) ");
				namedParameters.addValue("FLAT_ID", employeeFinancialServiceInfo.getFlatIds(), Types.BIGINT);
			}
			
			query.append("  ORDER BY FD.FLAT_ID");
			
			namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);

		}else {
			if(Util.isNotEmptyObject(employeeFinancialServiceInfo) && Util.isNotEmptyObject(employeeFinancialServiceInfo.getSiteIds())
					&& Util.isEmptyObject(employeeFinancialServiceInfo.getCondition())) {
				query.append(SqlQuery.QRY_TO_GET_RAISED_MILESTONE_BLOCKS);
				query.append(" WHERE BD.SITE_ID =:SITE_ID AND B.STATUS_ID=:STATUS_ID ORDER BY BLOCK_NAME ");
				namedParameters.addValue("SITE_ID", employeeFinancialServiceInfo.getSiteIds().get(0), Types.BIGINT);
				namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
				
				if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getBlockIds())) {

					query = new StringBuilder();
					query.append(SqlQuery.QRY_TO_GET_FLAT_FLAT_BOOKING_FLOOR_BLOCK_SITE_DETAILS);
					query.append(" WHERE B.BLOCK_ID IN(:BLOCK_ID) AND B.STATUS_ID=:STATUS_ID AND F.STATUS_ID=:STATUS_ID ");
					if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getSiteIds())) {
						query.append(" AND S.SITE_ID IN (:SITE_ID) ");
						namedParameters.addValue("SITE_ID",employeeFinancialServiceInfo.getSiteIds());
					} else if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getSiteId())) {
						query.append(" AND S.SITE_ID IN (:SITE_ID) ");
						namedParameters.addValue("SITE_ID", employeeFinancialServiceInfo.getSiteId());
					}
					query.append("  ORDER BY BD.BLOCK_ID,FD.FLAT_ID");
					namedParameters.addValue("BLOCK_ID", employeeFinancialServiceInfo.getBlockIds(), Types.BIGINT);
					namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
					
				}
				
				if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getFloorIds())) {
					query = new StringBuilder();
					query.append(SqlQuery.QRY_TO_GET_FLAT_FLAT_BOOKING_FLOOR_BLOCK_SITE_DETAILS);
					query.append(" WHERE FLD.FLOOR_ID IN (:FLOOR_ID) AND B.STATUS_ID=:STATUS_ID AND F.STATUS_ID=:STATUS_ID ");
					
					if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getSiteIds())) {
						query.append(" AND S.SITE_ID IN (:SITE_ID) ");
						namedParameters.addValue("SITE_ID",employeeFinancialServiceInfo.getSiteIds());
					} else if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getSiteId())) {
						query.append(" AND S.SITE_ID IN (:SITE_ID) ");
						namedParameters.addValue("SITE_ID", employeeFinancialServiceInfo.getSiteId());
					}
					query.append("  ORDER BY BD.BLOCK_ID,FD.FLAT_ID");
					
					namedParameters.addValue("FLOOR_ID", employeeFinancialServiceInfo.getFloorIds(), Types.BIGINT);
					namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
				
				}
				
			}else if(Util.isNotEmptyObject(employeeFinancialServiceInfo) && Util.isNotEmptyObject(employeeFinancialServiceInfo.getSiteIds())
					&& Util.isNotEmptyObject(employeeFinancialServiceInfo.getCondition()) 
					&& employeeFinancialServiceInfo.getCondition().equalsIgnoreCase(MetadataId.FLAT_BOOKING.getName())){
				query.append(SqlQuery.QRY_TO_GET_FLAT_FLAT_BOOKING_FLOOR_BLOCK_SITE_DETAILS);
				query.append(" WHERE BD.SITE_ID =:SITE_ID AND B.STATUS_ID=:STATUS_ID AND F.STATUS_ID=:STATUS_ID ORDER BY BLOCK_NAME, FD.FLAT_ID ");
				namedParameters.addValue("SITE_ID", employeeFinancialServiceInfo.getSiteIds().get(0), Types.BIGINT);
				namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
			}else if(Util.isNotEmptyObject(employeeFinancialServiceInfo) && Util.isNotEmptyObject(employeeFinancialServiceInfo.getBlockIds())
					&& Util.isEmptyObject(employeeFinancialServiceInfo.getCondition())){
				query = new StringBuilder();
				query.append(SqlQuery.QRY_TO_GET_FLAT_FLAT_BOOKING_FLOOR_BLOCK_SITE_DETAILS);
				query.append(" WHERE B.BLOCK_ID IN (:BLOCK_ID) AND B.STATUS_ID=:STATUS_ID AND F.STATUS_ID=:STATUS_ID ");
				if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getSiteIds())) {
					query.append(" AND S.SITE_ID IN (:SITE_ID) ");
					namedParameters.addValue("SITE_ID",employeeFinancialServiceInfo.getSiteIds(), Types.BIGINT);
				} else if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getSiteId())) {
					query.append(" AND S.SITE_ID IN (:SITE_ID) ");
					namedParameters.addValue("SITE_ID", employeeFinancialServiceInfo.getSiteId(), Types.BIGINT);
				}
				query.append("  ORDER BY BD.BLOCK_ID,FD.FLAT_ID");
				namedParameters.addValue("BLOCK_ID", employeeFinancialServiceInfo.getBlockIds(), Types.BIGINT);
				namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
				
			}else if(Util.isNotEmptyObject(employeeFinancialServiceInfo) && Util.isNotEmptyObject(employeeFinancialServiceInfo.getBlockIds())
					&& Util.isNotEmptyObject(employeeFinancialServiceInfo.getCondition()) &&
					(employeeFinancialServiceInfo.getCondition().equalsIgnoreCase(MetadataId.LEGAL_COST.getName()) 
							|| employeeFinancialServiceInfo.getCondition().equalsIgnoreCase(MetadataId.MODIFICATION_COST.getName())
							|| employeeFinancialServiceInfo.getCondition().equalsIgnoreCase(MetadataId.CUSTOMER_LEDGER.getName()) )){
				query.append(SqlQuery.QRY_TO_GET_FLOOR_BLOCK_SITE_DETAILS);
				query.append(" WHERE B.BLOCK_ID  IN (:BLOCK_ID) AND B.STATUS_ID=:STATUS_ID AND FLD.STATUS_ID=:STATUS_ID ");
				
				if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getSiteIds())) {
					query.append(" AND S.SITE_ID IN (:SITE_ID) ");
					namedParameters.addValue("SITE_ID",employeeFinancialServiceInfo.getSiteIds(), Types.BIGINT);
				} else if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getSiteId())) {
					query.append(" AND S.SITE_ID IN (:SITE_ID) ");
					namedParameters.addValue("SITE_ID", employeeFinancialServiceInfo.getSiteId(), Types.BIGINT);
				}
				
				query.append("  ORDER BY BD.BLOCK_ID,FLD.FLOOR_ID");
				namedParameters.addValue("BLOCK_ID", employeeFinancialServiceInfo.getBlockIds(), Types.BIGINT);
				namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
				if( Util.isNotEmptyObject(employeeFinancialServiceInfo.getFloorIds())) {
					query = new StringBuilder();
					query.append(SqlQuery.QRY_TO_GET_FLAT_FLAT_BOOKING_FLOOR_BLOCK_SITE_DETAILS);
					query.append(" WHERE FLD.FLOOR_ID  IN (:FLOOR_ID) AND  B.BLOCK_ID  IN (:BLOCK_ID) AND B.STATUS_ID=:STATUS_ID AND F.STATUS_ID=:STATUS_ID ");
					
					if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getSiteIds())) {
						query.append(" AND S.SITE_ID IN (:SITE_ID) ");
						namedParameters.addValue("SITE_ID",employeeFinancialServiceInfo.getSiteIds(), Types.BIGINT);
					} else if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getSiteId())) {
						query.append(" AND S.SITE_ID IN (:SITE_ID) ");
						namedParameters.addValue("SITE_ID", employeeFinancialServiceInfo.getSiteId(), Types.BIGINT);
					}
					
					query.append("  ORDER BY BD.BLOCK_ID,FD.FLAT_ID ");
					namedParameters.addValue("BLOCK_ID", employeeFinancialServiceInfo.getBlockIds(), Types.BIGINT);
					namedParameters.addValue("FLOOR_ID", employeeFinancialServiceInfo.getFloorIds(), Types.BIGINT);
					namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
				}
			}else if(Util.isNotEmptyObject(employeeFinancialServiceInfo) && Util.isNotEmptyObject(employeeFinancialServiceInfo.getFloorIds())){
				query.append(SqlQuery.QRY_TO_GET_FLAT_FLAT_BOOKING_FLOOR_BLOCK_SITE_DETAILS);
				query.append(" WHERE FLD.FLOOR_ID IN (:FLOOR_ID) AND B.STATUS_ID=:STATUS_ID AND F.STATUS_ID=:STATUS_ID ");
				
				if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getSiteIds())) {
					query.append(" AND S.SITE_ID IN (:SITE_ID) ");
					namedParameters.addValue("SITE_ID",employeeFinancialServiceInfo.getSiteIds(), Types.BIGINT);
				} else if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getSiteId())) {
					query.append(" AND S.SITE_ID IN (:SITE_ID) ");
					namedParameters.addValue("SITE_ID", employeeFinancialServiceInfo.getSiteId(), Types.BIGINT);
				}
				
				query.append("  ORDER BY BD.BLOCK_ID,FLD.FLOOR_ID,FD.FLAT_ID ");
				namedParameters.addValue("FLOOR_ID", employeeFinancialServiceInfo.getFloorIds(), Types.BIGINT);
				namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
			}else if(Util.isNotEmptyObject(employeeFinancialServiceInfo) && Util.isNotEmptyObject(employeeFinancialServiceInfo.getFlatIds())) {
				if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getRequestUrl()) && employeeFinancialServiceInfo.getRequestUrl().equalsIgnoreCase("ViewAllData"))
				{
					query.append(SqlQuery.QRY_TO_GET_FLAT_FLAT_BOOKING_FLOOR_BLOCK_SITE_DETAILS_FOR_ALL_STATUS);
					query.append(" WHERE FD.FLAT_ID =:FLAT_ID AND B.STATUS_ID=:STATUS_ID AND F.STATUS_ID=:STATUS_ID ORDER BY FD.FLAT_ID ");
					namedParameters.addValue("FLAT_ID", employeeFinancialServiceInfo.getFlatIds().get(0), Types.BIGINT);
					namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
				}else
				{
					query.append(SqlQuery.QRY_TO_GET_FLAT_FLAT_BOOKING_FLOOR_BLOCK_SITE_DETAILS);
					query.append(" WHERE FD.FLAT_ID =:FLAT_ID AND B.STATUS_ID=:STATUS_ID AND F.STATUS_ID=:STATUS_ID ORDER BY FD.FLAT_ID ");
					namedParameters.addValue("FLAT_ID", employeeFinancialServiceInfo.getFlatIds().get(0), Types.BIGINT);
					namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
				}
				
			}
		}
		//log.debug("*** generated query and SqlParameterSource are ***"+query+"***"+namedParameters);
		
		List<SiteDetailsPojo> siteDetailsPojoLists = getData(query.toString(), namedParameters, SiteDetailsPojo.class);
		//log.debug("*** The Extracted Data From Result Set Object is ***"+siteDetailsPojoLists);
		return siteDetailsPojoLists;
	}

	@Override
	public List<FinBookingFormDemandNotePojo> getDemandNotes(EmployeeFinancialServiceInfo employeeFinancialServiceInfo) {
		log.info("*** The Control is inside the getDemandNotes in EmployeeFinancialServiceDaoImpl ***");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder query = new StringBuilder(SqlQuery.QRY_TO_GET_DEMAND_NOTES)
				.append(" WHERE FBFM.BOOKING_FORM_ID IN(:BOOKING_FORM_IDS) ");
		//query.append(" AND FBFD.STATUS_ID IN (:STATUS_IDS) ");
		if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getProjectMileStoneIds())) {
			query.append(" AND FBFM.PROJECT_MILESTONE_ID IN (:PROJECT_MILESTONE_ID) ");
			namedParameters.addValue("PROJECT_MILESTONE_ID", employeeFinancialServiceInfo.getProjectMileStoneIds(), Types.BIGINT);
		}
		
		//namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		/* For Getting Interest Letters */
		if("Interest_Letter".equalsIgnoreCase(employeeFinancialServiceInfo.getRequestUrl())) {
			query = new StringBuilder(SqlQuery.QRY_TO_GET_INTEREST_LETTER)
					.append(" WHERE FBFD.BOOKING_FORM_ID IN(:BOOKING_FORM_IDS) ");
			query.append(" AND FBFD.STATUS_ID IN (:STATUS_IDS) ");
			query.append(" AND FBFD.DEMAND_NOTE_TYPE =:DEMAND_NOTE_TYPE ");
			//query.append(" AND FBFM.STATUS_ID IN (:STATUS_IDS)");
			namedParameters.addValue("DEMAND_NOTE_TYPE", MetadataId.INTEREST_LETTER.getId());
		}else {
			query.append(" AND FBFM.STATUS_ID IN (:STATUS_IDS)");
			query.append(" AND FBFD.DEMAND_NOTE_TYPE IS NULL ");
		}
		if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getSiteId())) {
			query.append(" AND BD.SITE_ID =:SITE_ID ");
			namedParameters.addValue("SITE_ID", employeeFinancialServiceInfo.getSiteId(), Types.BIGINT);
		}
		/*if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getBlockIds())) {
			query.append(" AND BD.BLOCK_ID  IN (:BLOCK_IDS) ");
			namedParameters.addValue("BLOCK_IDS", employeeFinancialServiceInfo.getBlockIds(), Types.BIGINT);
		}*/
		
		
		query.append(" ORDER BY FBFD.DEMAND_NOTE_NO DESC");
		
		namedParameters.addValue("BOOKING_FORM_IDS", employeeFinancialServiceInfo.getBookingFormIds(), Types.BIGINT);
		namedParameters.addValue("STATUS_IDS", Arrays.asList(Status.ACTIVE.getStatus(), Status.REGENERATED_DEMAND_NOTE.getStatus()), Types.BIGINT);
		log.debug("*** generated getDemandNotes query and SqlParameterSource are ***"+query+"***"+namedParameters.getValues());
		
		
		List<FinBookingFormDemandNotePojo> finBookingFormDemandNotePojoLists = (List<FinBookingFormDemandNotePojo>) getData(query.toString(), namedParameters, FinBookingFormDemandNotePojo.class);
		/*if(Util.isEmptyObject(finBookingFormDemandNotePojoLists)) {
			//log.debug("*** The Extracted Data From Result Set Object is Empty ***"+finBookingFormDemandNotePojoLists);
			finBookingFormDemandNotePojoLists.add(new FinBookingFormDemandNotePojo());
		} */
		return finBookingFormDemandNotePojoLists;
	}

	private static final List<Long> listOfBookingStatus = Arrays.asList(Status.CANCEL.getStatus(),
			Status.PENDING.getStatus(),Status.SWAP.getStatus(),
			Status.AVAILABLE.getStatus(),Status.BLOCKED.getStatus(),
			Status.NOT_OPEN.getStatus(),Status.PRICE_UPDATE.getStatus(),
			Status.LEGAL_CASE.getStatus(),Status.PMAY_SCHEME_ELIGIBLE.getStatus()
			,Status.RETAINED.getStatus(),Status.ACTIVE.getStatus(),Status.INACTIVE.getStatus()
			,Status.CANCELLED_NOT_REFUNDED.getStatus(),Status.ASSIGNMENT.getStatus()
			);

	@Override
	public List<CustomerPropertyDetailsPojo> getCustomerPropertyDetails(EmployeeFinancialServiceInfo employeeFinancialServiceInfo) {
		//log.info("*** The Control is inside the getCustomerPropertyDetails in EmployeeFinancialServiceDaoImpl ***"+employeeFinancialServiceInfo.getBookingFormIds() );
		String condition = employeeFinancialServiceInfo.getCondition()==null?"":employeeFinancialServiceInfo.getCondition();
		
		StringBuilder query = new StringBuilder(SqlQuery.QRY_TO_GET_CUSTOMER_PROPERTY_DETAILS);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		if(condition.equals("saveBookingDetails")) {
			namedParameters.addValue("STATUS_ID", Arrays.asList(Status.PENDING.getStatus(),Status.ACTIVE.getStatus(),Status.CANCEL.getStatus(),Status.PENDING.getStatus(),Status.SWAP.getStatus(),
					Status.AVAILABLE.getStatus(),Status.BLOCKED.getStatus(),Status.NOT_OPEN.getStatus(),Status.PRICE_UPDATE.getStatus(),
					Status.LEGAL_CASE.getStatus(),Status.PMAY_SCHEME_ELIGIBLE.getStatus(),Status.RETAINED.getStatus()
					,Status.CANCELLED_NOT_REFUNDED.getStatus(),Status.ASSIGNMENT.getStatus(),Status.INACTIVE.getStatus(),Status.ASSIGNMENT.getStatus()), Types.BIGINT);
		} else  if(condition.equals(FinEnum.GET_INACTIVE_BOOKINGS.getName())) {
			namedParameters.addValue("STATUS_ID", employeeFinancialServiceInfo.getStatusIds(), Types.BIGINT);
		} else if(condition.equals(FinEnum.GET_ACTIVE_INACTIVE_FLATS.getName())) {
			namedParameters.addValue("STATUS_ID", employeeFinancialServiceInfo.getStatusIds(), Types.BIGINT);
		} else {
			namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		}
		//query.append(" JOIN FIN_BOOKING_FORM_DEMAND_NOTE FBFD ON(FBFD.BOOKING_FORM_ID=FB.FLAT_BOOK_ID) ");
		if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getActionUrl()) && employeeFinancialServiceInfo.getActionUrl().equalsIgnoreCase("View DemandNotes")) {
			query.append(" WHERE ");
			if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getSiteIds())) {
				query.append(" BD.SITE_ID IN(:SITE_IDS) AND ");
				namedParameters.addValue("SITE_IDS", employeeFinancialServiceInfo.getSiteIds(), Types.BIGINT);
			}
			
			if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getBlockIds())) {
				query.append(" BL.BLOCK_ID IN(:BLOCK_IDS) AND ");
				namedParameters.addValue("BLOCK_IDS", employeeFinancialServiceInfo.getBlockIds(), Types.BIGINT);
			}
			
			if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getFloorIds())) {
				query.append(" FL.FLOOR_ID IN(:FLOOR_IDs) AND ");
				namedParameters.addValue("FLOOR_IDs", employeeFinancialServiceInfo.getFloorIds(), Types.BIGINT);
			}
			
			if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getFlatIds())) {
				query.append(" FB.FLAT_ID IN(:FLAT_IDS) AND ");
				namedParameters.addValue("FLAT_IDS", employeeFinancialServiceInfo.getFlatIds(), Types.BIGINT);
			}
			
			if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getFlatNos())) {
				query.append(" F.FLAT_NO IN (:FLAT_NOS) AND ");
				namedParameters.addValue("FLAT_NOS", employeeFinancialServiceInfo.getFlatNos(), Types.VARCHAR);
			}
			
			/*if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getBookingFormIds())) {
				query.append(" FB.FLAT_BOOK_ID IN (:FLAT_BOOK_IDS) AND ");
				namedParameters.addValue("FLAT_BOOK_IDS", employeeFinancialServiceInfo.getBookingFormIds(), Types.BIGINT);
			}*/
			 if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getBookingFormId())) {
					query.append(" FB.FLAT_BOOK_ID IN (:FLAT_BOOK_IDS) AND ");
					namedParameters.addValue("FLAT_BOOK_IDS", Arrays.asList(employeeFinancialServiceInfo.getBookingFormId()), Types.BIGINT);
			 } 
			
		} else {
			query.append(" WHERE ");
			if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getSiteIds())) {
				//added this condition in CUG, without this condition it's loading another site flat details
				query.append(" BD.SITE_ID IN(:SITE_IDS) AND ");
				namedParameters.addValue("SITE_IDS", employeeFinancialServiceInfo.getSiteIds(), Types.BIGINT);
			} else if (Util.isNotEmptyObject(employeeFinancialServiceInfo.getSiteId())) {
				query.append(" BD.SITE_ID IN (:SITE_IDS) AND ");
				namedParameters.addValue("SITE_IDS", employeeFinancialServiceInfo.getSiteId(), Types.BIGINT);
			}
			if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getBlockIds())) {
				query.append(" BL.BLOCK_ID IN(:BLOCK_IDS) AND ");
				namedParameters.addValue("BLOCK_IDS", employeeFinancialServiceInfo.getBlockIds(), Types.BIGINT);
			} else if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getFlatIds())) {
				query.append(" FB.FLAT_ID IN(:FLAT_IDS) AND ");
				namedParameters.addValue("FLAT_IDS", employeeFinancialServiceInfo.getFlatIds(), Types.BIGINT);
			} else if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getBookingFormIds())) {
				query.append(" FB.FLAT_BOOK_ID IN (:FLAT_BOOK_IDS) AND ");
				namedParameters.addValue("FLAT_BOOK_IDS", employeeFinancialServiceInfo.getBookingFormIds(), Types.BIGINT);
			}  
			if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getBookingFormId())) {
				query.append(" FB.FLAT_BOOK_ID IN (:FLAT_BOOK_IDS) AND ");
				namedParameters.addValue("FLAT_BOOK_IDS", Arrays.asList(employeeFinancialServiceInfo.getBookingFormId()), Types.BIGINT);
			} 
		}
		/* for getting single record */
		/*if(condition.equals(FinEnum.GET_ACTIVE_INACTIVE_FLATS.getName())) {
			//no need status id selection
		}else {*/
		if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getRequestUrl()) && employeeFinancialServiceInfo.getRequestUrl().equalsIgnoreCase("Interest_Letter"))
		{
			namedParameters.addValue("STATUS_ID", listOfBookingStatus, Types.BIGINT);
			//query.append(" FB.FLAT_BOOK_ID=:FLAT_BOOKING_ID ");
			//namedParameters.addValue("FLAT_BOOKING_ID", employeeFinancialServiceInfo.getFlatBookingId() ,Types.BIGINT);
		}
		
			query.append(" FB.STATUS_ID IN (:STATUS_ID) ");
		
		//}
		//.append(" AND FBFD.STATUS_ID=:STATUS_ID AND ROWNUM=1 ");
		//log.debug("*** generated query and SqlParameterSource are ***"+query+"***\n"+namedParameters.getValues());
		namedParameters.addValue("AR_STATUS_ID",Status.ACTIVE.getStatus(), Types.BIGINT);
		List<CustomerPropertyDetailsPojo> customerPropertyDetailsPojoLists =  getData(query.toString(), namedParameters, CustomerPropertyDetailsPojo.class);
		
		return customerPropertyDetailsPojoLists;
	}

	@Override
	public List<FinClosingBalanceReportPojo> getClosingBalanceReportDetails(EmployeeFinancialServiceInfo employeeFinancialServiceInfo) {
		log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.getClosingBalanceReportDetails() *****");
		StringBuffer query = null;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		if(Util.isEmptyObject(employeeFinancialServiceInfo.getProjectMileStoneIds())) {
			query = new StringBuffer(SqlQuery.QRY_TO_GET_CUSTOMER_CLOSING_BALANCE_DETAILS);	
		} else {
			//load closing balance by mile stone id's
			query = new StringBuffer(SqlQuery.QRY_TO_GET_CUSTOMER_CLOSING_BALANCE_DETAILS_BY_MILESTONE);
			namedParameters.addValue("PROJECT_MILESTONE_IDs", employeeFinancialServiceInfo.getProjectMileStoneIds(), Types.BIGINT);
		}
		
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("TYPE", MetadataId.FIN_BOOKING_FORM_MILESTONES.getId(), Types.BIGINT);
		
		query.append(" WHERE ");
		if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getSiteIds())) {
			//added this condition in CUG, without this condition it's loading another site flat details
			query.append(" BD.SITE_ID IN(:SITE_IDS) AND ");
			namedParameters.addValue("SITE_IDS", employeeFinancialServiceInfo.getSiteIds(), Types.BIGINT);
		} else if (Util.isNotEmptyObject(employeeFinancialServiceInfo.getSiteId())) {
			query.append(" BD.SITE_ID IN (:SITE_IDS) AND ");
			namedParameters.addValue("SITE_IDS", employeeFinancialServiceInfo.getSiteId(), Types.BIGINT);
		}
		if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getBlockIds())) {
			query.append(" BL.BLOCK_ID IN(:BLOCK_IDS) AND ");
			namedParameters.addValue("BLOCK_IDS", employeeFinancialServiceInfo.getBlockIds(), Types.BIGINT);
		} else if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getFlatIds())) {
			query.append(" FB.FLAT_ID IN(:FLAT_IDS) AND ");
			namedParameters.addValue("FLAT_IDS", employeeFinancialServiceInfo.getFlatIds(), Types.BIGINT);
		} else if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getBookingFormIds())) {
			query.append(" FB.FLAT_BOOK_ID IN (:FLAT_BOOK_IDS) AND ");
			namedParameters.addValue("FLAT_BOOK_IDS", employeeFinancialServiceInfo.getBookingFormIds(), Types.BIGINT);
		} else  if(Util.isNotEmptyObject(employeeFinancialServiceInfo.getBookingFormId())) {
			query.append(" FB.FLAT_BOOK_ID IN (:FLAT_BOOK_IDS) AND ");
			namedParameters.addValue("FLAT_BOOK_IDS", Arrays.asList(employeeFinancialServiceInfo.getBookingFormId()), Types.BIGINT);
		} 
		query.append(" FB.STATUS_ID IN (:STATUS_ID) ")
		.append(" ORDER BY BL.BLOCK_ID,FB.FLAT_ID ")
		;
		
		log.info(" query "+query+" \n"+namedParameters.getValues());
		List<FinClosingBalanceReportPojo> customerPropertyDetailsPojoLists =  getData(query.toString(), namedParameters, FinClosingBalanceReportPojo.class);
		
		return customerPropertyDetailsPojoLists;
	}
	
	@Override
	public int insertFinBankData(FinBankPojo bankPojo) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.insertFinBankData() *****");
		String query = SqlQuery.QRY_TO_INSERT_FIN_BANK_DETAILS;
		Long pk = commonMethodToInsertData(query, bankPojo, "FIN_BANK_ID");
		bankPojo.setFinBankId(pk);
		return pk.intValue();
	}

	@Override
	public List<FinBankPojo> getFinBankData(EmployeeFinancialTransactionServiceInfo employeeFinancialTransactionInfo) {
		//log.info("*** The Control is inside the getFinBankData in EmployeeFinancialServiceDaoImpl ***");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		StringBuilder query = new StringBuilder(" SELECT ").append(SqlQuery.QRY_TO_GET_FIN_BANK)
				.append(" FROM FIN_BANK FB ");
		query.append(" WHERE FB.STATUS_ID = :STATUS_ID ");
		if (employeeFinancialTransactionInfo.getCondition() != null && employeeFinancialTransactionInfo.getCondition().equals(FinEnum.LOAD_BY_NAME.getName())) {
			query.append(" AND FB.BANK_NAME = :BANK_NAME ");
			namedParameters.addValue("BANK_NAME", employeeFinancialTransactionInfo.getBankName(), Types.VARCHAR);
		}
		query.append(" AND FB.BANK_NAME IS NOT NULL ");
		query.append(" ORDER BY FB.BANK_NAME ");
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		//log.debug("*** generated query and SqlParameterSource are ***" + query + "***" + namedParameters.getValues());
		List<List<FinBankPojo>> finBankPojoLists = nmdPJdbcTemplate.query(query.toString(), namedParameters,
				new ExtractDataFromResultSet<FinBankPojo>(FinBankPojo.class) {});
		if (finBankPojoLists.isEmpty()) {
			finBankPojoLists.add(new ArrayList<FinBankPojo>());
		}
		//log.debug("*** The Extracted Data From Result Set Object is ***"+finBankPojoLists);
		return finBankPojoLists.get(0);
	}

	@Override
	public List<FinTransactionModePojo> getFinTransactionModeData(EmployeeFinancialTransactionServiceInfo employeeFinancialTransactionInfo) {
		//log.info("*** The Control is inside the getFinTransactionModeData in EmployeeFinancialServiceDaoImpl ***");
		StringBuilder query = new StringBuilder(" SELECT ")
				.append(SqlQuery.QRY_TO_GET_FIN_TRANSACTION_MODE)
				.append(" FROM FIN_TRANSACTION_MODE FTM ")
				.append(" WHERE FTM.STATUS_ID=:STATUS_ID ");
				if(employeeFinancialTransactionInfo.getCondition().equalsIgnoreCase("fetchPaymentData")) {
					query.append(" AND FTM.TRANSACTION_MODE_ID NOT IN (5)");
				}
				query.append(" ORDER BY FTM.NAME ");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		//log.debug("*** generated query and SqlParameterSource are ***"+query+"***"+namedParameters);
		List<List<FinTransactionModePojo>> finTransactionModePojoLists = nmdPJdbcTemplate.query(query.toString(), namedParameters, 
				new ExtractDataFromResultSet<FinTransactionModePojo>(FinTransactionModePojo.class){});
		if(finTransactionModePojoLists.isEmpty()) {
			finTransactionModePojoLists.add(new ArrayList<FinTransactionModePojo>());
		}
		//log.debug("*** The Extracted Data From Result Set Object is ***"+finTransactionModePojoLists);
		return finTransactionModePojoLists.get(0);
	}

	@Override
	public List<FinTransactionTypePojo> getFinTransactionTyeData(EmployeeFinancialTransactionServiceInfo employeeFinancialTransactionInfo) {
		//log.info("*** The Control is inside the getFinTransactionTyeData in EmployeeFinancialServiceDaoImpl ***");
		StringBuilder query = new StringBuilder(SqlQuery.QRY_TO_GET_FIN_TRANSACTION_TYPE)
				.append(" FROM FIN_TRANSACTION_TYPE FTT ")
				.append(" WHERE FTT.STATUS_ID=:STATUS_ID ")
				.append(" ORDER BY FTT.NAME ");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		//log.debug("*** generated query and SqlParameterSource are ***"+query+"***"+namedParameters);
		List<List<FinTransactionTypePojo>> finTransactionModePojoLists = nmdPJdbcTemplate.query(query.toString(), namedParameters, 
				new ExtractDataFromResultSet<FinTransactionTypePojo>(FinTransactionTypePojo.class){});
		if(finTransactionModePojoLists.isEmpty()) {
			finTransactionModePojoLists.add(new ArrayList<FinTransactionTypePojo>());
		}
		//log.debug("*** The Extracted Data From Result Set Object is ***"+finTransactionModePojoLists);
		return finTransactionModePojoLists.get(0);
	}
	
	@Override
	public List<FinTransactionForPojo> getTransactionForDetails(EmployeeFinancialTransactionServiceInfo transactionInfo) {
		//log.info(" ***** EmployeeFinancialServiceDaoImpl.getTransactionForDetails() ***** ");
		StringBuilder query = new StringBuilder(SqlQuery.QRY_TO_GET_FIN_TRANSACTION_FOR)
				.append(" FROM FIN_TRANSACTION_FOR FTf ")
				.append(" WHERE FTF.STATUS_ID=:STATUS_ID ")
				.append(" ORDER BY FTF.NAME ");

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		
		
		List<FinTransactionForPojo> transactionForPojos  = (List<FinTransactionForPojo>) getData(query.toString(), namedParameters, FinTransactionForPojo.class);
		return transactionForPojos;
	}

	@Override
	public List<DropDownPojo> getSiteData(Site site) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.getSiteData() *****");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder query = new StringBuilder(SqlQuery.QRY_TO_GET_ALL_SITE_DETAILS);
		query.append(" WHERE S.STATUS_ID=:STATUS_ID ");
		if(Util.isNotEmptyObject(site.getName())) {
			query.append(" AND S.NAME = :SITE_NAME");
		}
		query.append(" ORDER BY S.NAME");
		namedParameters.addValue("STATUS_ID", Status.COMPLETED.getStatus());
		namedParameters.addValue("SITE_NAME", site.getName());
		
		
		List<DropDownPojo> sitesLists = (List<DropDownPojo>) getData(query.toString(), namedParameters, DropDownPojo.class);
		return sitesLists;
	}
	
	@Override
	public List<AddressPojo> getSiteAddressDetails(EmployeeFinancialServiceInfo employeeFinancialServiceInfo) {
		//log.info("*** The Control is inside the getSiteAddressDetails in EmployeeFinancialServiceDaoImpl ***");
		StringBuilder query = new StringBuilder(SqlQuery.QRY_TO_GET_ADDRESS)
				.append(" JOIN SITE ST ON(ST.ADDRESS_ID=ADDR.ADDRESS_ID) ")
				.append(" WHERE ST.SITE_ID IN(:SITE_ID) ");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("SITE_ID", employeeFinancialServiceInfo.getSiteIds(), Types.BIGINT);
		//log.debug("*** generated query and SqlParameterSource are ***"+query+"***"+namedParameters.getValues());
		
		
		List<AddressPojo> addressPojoLists = (List<AddressPojo>) getData(query.toString(), namedParameters, AddressPojo.class);
		return addressPojoLists;
	}

	@Override
	public List<FinProjectAccountPojo> getFinProjectAccountData(EmployeeFinancialTransactionServiceInfo employeeFinancialTransactionInfo, CustomerPropertyDetailsInfo customerPropertyDetailsInfo) {
		//log.info("*** The Control is inside the getFinProjectAccountData in EmployeeFinancialServiceDaoImpl ***");
		String condition = employeeFinancialTransactionInfo.getCondition()==null?"":employeeFinancialTransactionInfo.getCondition();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder query = new StringBuilder(" SELECT ")
				.append(SqlQuery.QRY_TO_GET_FIN_BANK).append(",")
				.append(SqlQuery.QRY_TO_GET_FIN_PROJECT__ACCOUNT).append(",")
				.append(SqlQuery.QRY_TO_GET_FIN_SITE_PROJ_ACC_MAPPING)
				.append(" ,ADDR.ADDRESS1 AS ADDRESS1 ")
				.append(" ,FSAM.FLATS_SALE_OWNERS_ID  ")
				.append(" ,FSO.FLAT_SALE_OWNER ")
				.append("  ,FPA.ACCOUNT_NO||'-'||FSO.FLAT_SALE_OWNER  ||(CASE WHEN  ACCOUNT_TYPE IS NOT NULL THEN '-' WHEN  ACCOUNT_TYPE IS NOT NULL THEN '-' END )|| ")
				.append("  CASE  ")
				.append("  WHEN FSAM.ACCOUNT_TYPE='35'  THEN 'Legal Charges' ")
				.append("  WHEN FSAM.ACCOUNT_TYPE='29' THEN 'Principal' ")
				.append("  WHEN FSAM.ACCOUNT_TYPE='142' THEN 'Principal With Demand note' ")
				.append("  WHEN FSAM.ACCOUNT_TYPE='27'  THEN 'Interest' ")
				.append("  WHEN FSAM.ACCOUNT_TYPE='102' THEN 'Khata Charges' ")
				.append("  WHEN FSAM.ACCOUNT_TYPE='103'  THEN 'Maintenance' ")
				.append("  WHEN FSAM.ACCOUNT_TYPE='104' THEN 'Corpus Fund' ")
				.append("  WHEN FSAM.ACCOUNT_TYPE='34' THEN 'Modification Charges' ")
				.append("  WHEN FSAM.ACCOUNT_TYPE='62'  THEN 'Refundable Advance'  ")
				.append("  WHEN FSAM.ACCOUNT_TYPE='32' THEN 'TDS'  ")
				.append("  WHEN FSAM.ACCOUNT_TYPE IS NULL THEN ''  ")
				.append("  END AS ACCOUNT_NO_WITH_SALEOWNER ")
				.append(" FROM FIN_SITE_PROJ_ACC_MAPPING FSAM ")
				.append(" JOIN FIN_PROJECT_ACCOUNTS FPA ")
				.append(" ON(FPA.FIN_PROJ_ACC_ID=FSAM.FIN_PROJ_ACC_ID) ")
				.append(" JOIN FIN_BANK FB ")
				.append(" ON(FB.FIN_BANK_ID=FPA.FIN_BANK_ID) ")
				.append(" FULL JOIN ADDRESS ADDR ")
				.append(" ON (ADDR.ADDRESS_ID=FPA.ADDRESS_ID) ")
				.append(" INNER JOIN FLATS_SALE_OWNERS FSO ON FSO.FLATS_SALE_OWNERS_ID=FSAM.FLATS_SALE_OWNERS_ID  ")
				.append(" WHERE FSAM.STATUS_ID=:STATUS_ID  ");
				query.append(" AND FSAM.SITE_ID IN(:SITE_ID) ");
				
			if (condition.equals("MODIFICATION_COST") || condition.equals("LEGAL_COST") ||  condition.equals("FIN_BOOKING_FORM_MILESTONES") ||  condition.equals("DEMAND_NOTE_ACCOUNT_NUMBER") ) {
				query.append(" AND FSAM.ACCOUNT_TYPE = :ACCOUNT_TYPE ");
				namedParameters.addValue("ACCOUNT_TYPE",EmployeeFinancialServiceImpl.getMetaDataId(condition), Types.VARCHAR);
			}/* else if (employeeFinancialTransactionInfo.getCondition().equals("LEGAL_COST")) {
				query.append(" AND FSAM.ACCOUNT_TYPE = :ACCOUNT_TYPE ");
			}*/
			
			if(customerPropertyDetailsInfo!=null && customerPropertyDetailsInfo.getFlatSaleOwnerId()!=null) {
				query.append(" AND FSAM.FLATS_SALE_OWNERS_ID = :FLATS_SALE_OWNERS_ID ");
				namedParameters.addValue("FLATS_SALE_OWNERS_ID",customerPropertyDetailsInfo.getFlatSaleOwnerId(), Types.BIGINT);
			}
			
			if(employeeFinancialTransactionInfo!=null && employeeFinancialTransactionInfo.getSiteAccountId()!=null) {
				query.append(" AND FSAM.FIN_SITE_PROJ_ACC_MAP_ID = :FIN_SITE_PROJ_ACC_MAP_ID ");
				namedParameters.addValue("FIN_SITE_PROJ_ACC_MAP_ID",employeeFinancialTransactionInfo.getSiteAccountId(), Types.BIGINT);
			}
			
			query.append(" ORDER BY FSAM.FIN_PROJ_ACC_ID ")
				.append(condition.equalsIgnoreCase("LoadAllAccountNo")?"":" FETCH FIRST 1 ROW ONLY");
						
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("SITE_ID", employeeFinancialTransactionInfo.getSiteIds(), Types.BIGINT);		
	
		List<FinProjectAccountPojo> finProjectAccountPojoLists = (List<FinProjectAccountPojo>) getData(query.toString(), namedParameters, FinProjectAccountPojo.class);
		return finProjectAccountPojoLists;
	}

	@Override
	public List<FinProjectAccountPojo> viewFinProjectAccountDataForInvoices(EmployeeFinancialTransactionServiceInfo employeeFinancialTransactionInfo, CustomerPropertyDetailsInfo customerPropertyDetailsInfo) {
		//log.info("*** The Control is inside the viewFinProjectAccountDataForInvoices in EmployeeFinancialServiceDaoImpl ***");
		String condition = employeeFinancialTransactionInfo.getCondition()==null?"":employeeFinancialTransactionInfo.getCondition();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder query = new StringBuilder(" SELECT ")
				.append(SqlQuery.QRY_TO_GET_FIN_BANK).append(",")
				.append(SqlQuery.QRY_TO_GET_FIN_PROJECT__ACCOUNT).append(",")
				.append(SqlQuery.QRY_TO_GET_FIN_SITE_PROJ_ACC_MAPPING)
				.append(" ,ADDR.ADDRESS1 AS ADDRESS1 ")
				.append(" ,FSAM.FLATS_SALE_OWNERS_ID  ")
				.append(" ,FSO.FLAT_SALE_OWNER ")
				.append("  ,FPA.ACCOUNT_NO||'-'||FSO.FLAT_SALE_OWNER  ||(CASE WHEN  ACCOUNT_TYPE IS NOT NULL THEN '-' WHEN  ACCOUNT_TYPE IS NOT NULL THEN '-' END )|| ")
				.append("  CASE  ")
				.append("  WHEN FSAM.ACCOUNT_TYPE='35'  THEN 'Legal Charges' ")
				.append("  WHEN FSAM.ACCOUNT_TYPE='29' THEN 'Principal' ")
				.append("  WHEN FSAM.ACCOUNT_TYPE='142' THEN 'Principal With Demand note' ")
				.append("  WHEN FSAM.ACCOUNT_TYPE='27'  THEN 'Interest' ")
				.append("  WHEN FSAM.ACCOUNT_TYPE='102' THEN 'Khata Charges' ")
				.append("  WHEN FSAM.ACCOUNT_TYPE='103'  THEN 'Maintenance' ")
				.append("  WHEN FSAM.ACCOUNT_TYPE='104' THEN 'Corpus Fund' ")
				.append("  WHEN FSAM.ACCOUNT_TYPE='34' THEN 'Modification Charges' ")
				.append("  WHEN FSAM.ACCOUNT_TYPE='62'  THEN 'Refundable Advance'  ")
				.append("  WHEN FSAM.ACCOUNT_TYPE='32' THEN 'TDS'  ")
				.append("  WHEN FSAM.ACCOUNT_TYPE IS NULL THEN ''  ")
				.append("  END AS ACCOUNT_NO_WITH_SALEOWNER ")
				.append(" FROM FIN_SITE_PROJ_ACC_MAPPING FSAM ")
				.append(" JOIN FIN_PROJECT_ACCOUNTS FPA ")
				.append(" ON(FPA.FIN_PROJ_ACC_ID=FSAM.FIN_PROJ_ACC_ID) ")
				.append(" JOIN FIN_BANK FB ")
				.append(" ON(FB.FIN_BANK_ID=FPA.FIN_BANK_ID) ")
				.append(" FULL JOIN ADDRESS ADDR ")
				.append(" ON (ADDR.ADDRESS_ID=FPA.ADDRESS_ID) ")
				.append(" INNER JOIN FLATS_SALE_OWNERS FSO ON FSO.FLATS_SALE_OWNERS_ID=FSAM.FLATS_SALE_OWNERS_ID  ")
				.append(" WHERE FSAM.STATUS_ID=:STATUS_ID  ");
				query.append(" AND FSAM.SITE_ID IN(:SITE_ID) ");
				
			if (condition.equals("MODIFICATION_COST") || condition.equals("LEGAL_COST") ||  condition.equals("FIN_BOOKING_FORM_MILESTONES") ) {
				query.append(" AND FSAM.ACCOUNT_TYPE = :ACCOUNT_TYPE ");
				namedParameters.addValue("ACCOUNT_TYPE",EmployeeFinancialServiceImpl.getMetaDataId(condition), Types.VARCHAR);
			}/* else if (employeeFinancialTransactionInfo.getCondition().equals("LEGAL_COST")) {
				query.append(" AND FSAM.ACCOUNT_TYPE = :ACCOUNT_TYPE ");
			}*/
			
			if(customerPropertyDetailsInfo!=null && customerPropertyDetailsInfo.getFlatSaleOwnerId()!=null) {
				query.append(" AND FSAM.FLATS_SALE_OWNERS_ID = :FLATS_SALE_OWNERS_ID ");
				namedParameters.addValue("FLATS_SALE_OWNERS_ID",customerPropertyDetailsInfo.getFlatSaleOwnerId(), Types.BIGINT);
			}
			
			if(employeeFinancialTransactionInfo!=null && employeeFinancialTransactionInfo.getSiteAccountId()!=null) {
				query.append(" AND FSAM.FIN_SITE_PROJ_ACC_MAP_ID = :FIN_SITE_PROJ_ACC_MAP_ID ");
				namedParameters.addValue("FIN_SITE_PROJ_ACC_MAP_ID",employeeFinancialTransactionInfo.getSiteAccountId(), Types.BIGINT);
			}
			
			query.append(" ORDER BY FSAM.FIN_PROJ_ACC_ID ")
				.append(condition.equalsIgnoreCase("LoadAllAccountNo")?"":" FETCH FIRST 1 ROW ONLY");
						
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("SITE_ID", employeeFinancialTransactionInfo.getSiteIds(), Types.BIGINT);		
	
		List<FinProjectAccountPojo> finProjectAccountPojoLists = (List<FinProjectAccountPojo>) getData(query.toString(), namedParameters, FinProjectAccountPojo.class);
		return finProjectAccountPojoLists;
	}

	
	@Override
	public Long loadSiteAccountNumberIdUsingNumber(FinancialDemandNoteMS_TRN_Info trn_Request,String condition) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder query = null;
		
		if("ApproveTransaction".equals(condition)) {
			 query = new StringBuilder("") .append("SELECT FSAM.FIN_SITE_PROJ_ACC_MAP_ID ")
						.append(" FROM FIN_SITE_PROJ_ACC_MAPPING FSAM ")
						.append(" JOIN FIN_PROJECT_ACCOUNTS FPA ON(FPA.FIN_PROJ_ACC_ID=FSAM.FIN_PROJ_ACC_ID) ")
						.append(" WHERE FPA.ACCOUNT_NO = :ACCOUNT_NO and FSAM.SITE_ID = :SITE_ID")
						.append(" AND FSAM.STATUS_ID = :STATUS_ID FETCH FIRST ROW ONLY");
			 				//AND FSAM.FLATS_SALE_OWNERS_ID = :FLATS_SALE_OWNERS_ID 
		} else if("uploadTransaction".equals(condition)) {
			 query = new StringBuilder("") .append("SELECT FSAM.FIN_SITE_PROJ_ACC_MAP_ID ")
						.append(" FROM FIN_SITE_PROJ_ACC_MAPPING FSAM ")
						.append(" JOIN FIN_PROJECT_ACCOUNTS FPA ON(FPA.FIN_PROJ_ACC_ID=FSAM.FIN_PROJ_ACC_ID) ")
						.append(" WHERE FPA.ACCOUNT_NO = :ACCOUNT_NO and FSAM.SITE_ID = :SITE_ID")
						.append(" AND FSAM.STATUS_ID = :STATUS_ID ")
						//.append(" AND FSAM.FLATS_SALE_OWNERS_ID = :FLATS_SALE_OWNERS_ID")
			 			.append(" FETCH FIRST ROW ONLY");
		}
		
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("SITE_ID", trn_Request.getSiteId(), Types.BIGINT);
		namedParameters.addValue("FLATS_SALE_OWNERS_ID", trn_Request.getCustomerPropertyDetailsInfo().getFlatSaleOwnerId(), Types.BIGINT);
		namedParameters.addValue("ACCOUNT_NO", trn_Request.getSiteBankAccountNumber(), Types.VARCHAR);

		try {
			return nmdPJdbcTemplate.queryForObject(query.toString(), namedParameters, Long.class);
		} catch (Exception ex) {
			log.info("Error Occured while loading account number : \n"+ex.getMessage());
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> loadListSiteAccountNumberIdUsingNumber(FinancialDemandNoteMS_TRN_Info trn_Request,
			String condition) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuilder query = null;
		
		if("ApproveTransaction".equals(condition)) {
			 query = new StringBuilder("") .append("SELECT FSAM.FIN_SITE_PROJ_ACC_MAP_ID,FSAM.SITE_ID ")
						.append(" FROM FIN_SITE_PROJ_ACC_MAPPING FSAM ")
						.append(" JOIN FIN_PROJECT_ACCOUNTS FPA ON(FPA.FIN_PROJ_ACC_ID=FSAM.FIN_PROJ_ACC_ID) ")
						.append(" WHERE FPA.ACCOUNT_NO = :ACCOUNT_NO and FSAM.SITE_ID = :SITE_ID")
						.append(" AND FSAM.FIN_SITE_PROJ_ACC_MAP_ID = :FIN_SITE_PROJ_ACC_MAP_ID ")
						.append(" AND FSAM.STATUS_ID = :STATUS_ID FETCH FIRST ROW ONLY");
			 				//AND FSAM.FLATS_SALE_OWNERS_ID = :FLATS_SALE_OWNERS_ID 
		}

		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("SITE_ID", trn_Request.getSiteId(), Types.BIGINT);
		//namedParameters.addValue("FLATS_SALE_OWNERS_ID", trn_Request.getCustomerPropertyDetailsInfo().getFlatSaleOwnerId(), Types.BIGINT);
		namedParameters.addValue("ACCOUNT_NO", trn_Request.getSiteBankAccountNumber(), Types.VARCHAR);
		namedParameters.addValue("FIN_SITE_PROJ_ACC_MAP_ID", trn_Request.getSiteAccountId(), Types.BIGINT);

		try {
			return nmdPJdbcTemplate.queryForList(query.toString(), namedParameters);
		} catch (Exception ex) {
			log.info("Error Occured while loading account number : \n"+ex.getMessage());
		}
		return null;
	}
	
	@Override
	public List<FinBookingFormAccountsPojo> getFinBookingFormAccountsData1(CustomerPropertyDetailsInfo customerPropertyDetailsInfo) {
		//log.info("*** The Control is inside the getFinBookingFormAccountsData in EmployeeFinancialServiceDaoImpl ***");
		StringBuilder query = new StringBuilder(" SELECT ")
				.append(SqlQuery.QRY_TO_GET_FIN_BOOKING_FORM_ACCOUNTS_DATA).append(",")
				.append("(NVL(FBFA.PAY_AMOUNT,0)-NVL(FBFA.PAID_AMOUNT,0)) AS PENDING_AMOUNT, ")
				.append(" FBFM.MS_STATUS_ID, ")
				.append(SqlQuery.QRY_TO_GET_FIN_PENALTY_DATA)
				.append(" FROM FIN_BOOKING_FORM_ACCOUNTS FBFA ")
				.append(" INNER JOIN FIN_BOOKING_FORM_MILESTONES FBFM ON FBFA.TYPE = 29 AND FBFA.TYPE_ID = FBFM.FIN_BOOKING_FORM_MILESTONES_ID AND FBFM.STATUS_ID = :STATUS_ID ")// AND FBFM.MS_STATUS_ID = :STATUS_ID 
				.append(" LEFT OUTER JOIN FIN_PENALTY FP ON(FP.FIN_BOOKING_FORM_ACCOUNTS_ID=FBFA.FIN_BOOKING_FORM_ACCOUNTS_ID AND FP.STATUS_ID = :STATUS_ID) ")
				.append(" WHERE FBFA.BOOKING_FORM_ID = :BOOKING_FORM_ID ")
				.append(" AND FBFA.PAYMENT_STATUS IN (:PAYMENT_STATUS) ")
				.append(" AND FBFA.STATUS_ID = :STATUS_ID")
				.append(" ORDER BY FBFA.CREATED_DATE DESC");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		
		namedParameters.addValue("TYPE",MetadataId.FIN_BOOKING_FORM_MILESTONES.getId(), Types.BIGINT);
		namedParameters.addValue("BOOKING_FORM_ID", customerPropertyDetailsInfo.getFlatBookingId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("PAYMENT_STATUS",Arrays.asList(new Long[] { Status.PAID.getStatus(),Status.PENDING.getStatus(),Status.INPROGRESS.getStatus()}), Types.BIGINT);
		//log.debug("*** generated query and SqlParameterSource are *** "+query+" ***");
		
		List<FinBookingFormAccountsPojo> finBookingFormAccountsPojoLists = (List<FinBookingFormAccountsPojo>) getData(query.toString(), namedParameters, FinBookingFormAccountsPojo.class);
		//log.debug("*** The Extracted Data From Result Set Object is ***"+finBookingFormAccountsPojoLists);
		return finBookingFormAccountsPojoLists;
	}

	/**
	 * Used this method in shift transaction
	 * used for NOC letter validation, and update gst old records also
	 * Note : if code changed need to check dependency code
	 */
	@Override
	public List<FinBookingFormAccountsPojo> getFinBookingFormAccountsData(FinBookingFormAccountsInfo bookingFormAccountsInfo ) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String condition = bookingFormAccountsInfo.getCondition() == null?"":bookingFormAccountsInfo.getCondition();
		boolean isThisLoadById = (condition.equals(FinEnum.LOAD_BY_ID.getName()));
		boolean isThisRequestFromNoc = (condition.equals("NOC Validation"));
		boolean updateOldPenaltyRecords = (condition.equals("UpdateOldPenaltyRecords"));
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.getFinBookingFormAccountsData() *****");
			StringBuilder query = null; /*new StringBuilder(" SELECT ");
					query.append(SqlQuery.QRY_TO_GET_FIN_BOOKING_FORM_ACCOUNTS_DATA).append(",NVL(FBFA.INTEREST_WAIVER_AMT,0) AS INTEREST_WAIVER_AMT ");
					query.append(" FROM FIN_BOOKING_FORM_ACCOUNTS FBFA ");
					query.append(" WHERE FBFA.STATUS_ID = :STATUS_ID ");
					query.append(" AND FBFA.PAYMENT_STATUS IN (:PAYMENT_STATUS) ");
					query.append(" AND FBFA.TYPE IN (:TYPE)");
					query.append(" AND FBFA.BOOKING_FORM_ID = :BOOKING_FORM_ID ");
			
			if(isThisLoadById) {
				query.append(" AND FBFA.TYPE_ID = :TYPE_ID ");
				namedParameters.addValue("TYPE_ID",bookingFormAccountsInfo.getTypeId(), Types.BIGINT);
			}
			
			if(isThisRequestFromNoc) {
				query.append(" ORDER BY FBFA.FIN_BOOKING_FORM_ACCOUNTS_ID ASC");
			} else {
				query.append(" ORDER BY FBFA.FIN_BOOKING_FORM_ACCOUNTS_ID DESC");
			}*/
			
			if (updateOldPenaltyRecords) {
				 query = new StringBuilder(" SELECT ");
					query.append(SqlQuery.QRY_TO_GET_FIN_BOOKING_FORM_ACCOUNTS_DATA).append(",NVL(FBFA.INTEREST_WAIVER_AMT,0) AS INTEREST_WAIVER_AMT ");
					query.append(" FROM FIN_BOOKING_FORM_ACCOUNTS FBFA ");
					query.append(" WHERE FBFA.STATUS_ID = :STATUS_ID ");
					query.append(" AND FBFA.PAYMENT_STATUS IN (:PAYMENT_STATUS) ");
					query.append(" AND FBFA.TYPE IN (:TYPE)");
					query.append(" ORDER BY FBFA.FIN_BOOKING_FORM_ACCOUNTS_ID ASC");
			} else {
				 query = new StringBuilder(" SELECT ");
					query.append(SqlQuery.QRY_TO_GET_FIN_BOOKING_FORM_ACCOUNTS_DATA).append(",NVL(FBFA.INTEREST_WAIVER_AMT,0) AS INTEREST_WAIVER_AMT ");
					query.append(" FROM FIN_BOOKING_FORM_ACCOUNTS FBFA ");
					query.append(" WHERE FBFA.STATUS_ID = :STATUS_ID ");
					query.append(" AND FBFA.PAYMENT_STATUS IN (:PAYMENT_STATUS) ");
					query.append(" AND FBFA.TYPE IN (:TYPE)");
					query.append(" AND FBFA.BOOKING_FORM_ID = :BOOKING_FORM_ID ");
			
				if(isThisLoadById) {
					query.append(" AND FBFA.TYPE_ID = :TYPE_ID ");
					namedParameters.addValue("TYPE_ID",bookingFormAccountsInfo.getTypeId(), Types.BIGINT);
				}
				
				if(isThisRequestFromNoc) {
					query.append(" ORDER BY FBFA.FIN_BOOKING_FORM_ACCOUNTS_ID ASC");
				} else {
					query.append(" ORDER BY FBFA.FIN_BOOKING_FORM_ACCOUNTS_ID DESC");
				}

			}
			
		namedParameters.addValue("BOOKING_FORM_ID",bookingFormAccountsInfo.getBookingFormId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID",bookingFormAccountsInfo.getStatusId() , Types.BIGINT);
		namedParameters.addValue("PAYMENT_STATUS", bookingFormAccountsInfo.getPaymentStatusList(), Types.BIGINT);
		namedParameters.addValue("TYPE", bookingFormAccountsInfo.getTypes(), Types.BIGINT);
		
		List<FinBookingFormAccountsPojo> bookingFormAccountsPojoLists = (List<FinBookingFormAccountsPojo>) getData( query.toString(), namedParameters, FinBookingFormAccountsPojo.class);
		//log.debug("*** The Extracted Data From Result Set Object is ***" + bookingFormAccountsPojoLists);
		return bookingFormAccountsPojoLists;
	}
	
	@Override
	public List<FinBookingFormAccountsPojo> getAccountsExcessAmountDetailsIfAny(
			FinBookingFormAccountsInfo bookingFormAccountsInfo, List<Long> processedBookingFormIds) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			StringBuilder query = new StringBuilder(" SELECT ")
					.append(SqlQuery.QRY_TO_GET_FIN_BOOKING_FORM_ACCOUNTS_DATA) 
					.append(" FROM FIN_BOOKING_FORM_ACCOUNTS FBFA ")
					.append(" WHERE FBFA.BOOKING_FORM_ID IN (:BOOKING_FORM_ID) ")
					.append(" AND FBFA.PAYMENT_STATUS IN (:PAYMENT_STATUS) ")
					.append(" AND FBFA.TYPE IN (:TYPE)")
					.append(" AND FBFA.STATUS_ID = :STATUS_ID")
					.append(" AND NVL(FBFA.PAID_AMOUNT,0) > NVL(FBFA.PAY_AMOUNT,0) ")
					.append(" ORDER BY FBFA.FIN_BOOKING_FORM_ACCOUNTS_ID DESC");
					
		namedParameters.addValue("BOOKING_FORM_ID",processedBookingFormIds, Types.BIGINT);
		namedParameters.addValue("STATUS_ID",bookingFormAccountsInfo.getStatusId() , Types.BIGINT);
		namedParameters.addValue("PAYMENT_STATUS", bookingFormAccountsInfo.getPaymentStatusList(), Types.BIGINT);
		namedParameters.addValue("TYPE", bookingFormAccountsInfo.getTypes(), Types.BIGINT);
		
		List<FinBookingFormAccountsPojo> bookingFormAccountsPojoLists = (List<FinBookingFormAccountsPojo>) getData( query.toString(), namedParameters, FinBookingFormAccountsPojo.class);
		return bookingFormAccountsPojoLists;
	}
	
	@Override
	public int updateBookingFormAccountStatementData(FinBookingFormAccountsPojo prevData, FinBookingFormAccountsPojo currData) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.updateBookingFormAccountStatementData() *****");
		StringBuilder query = SqlQuery.QRY_TO_UPDATE_FIN_BOOKING_FORM_ACCOUNTS_ACCOUNTS_DATA;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("CURR_FIN_BOOKING_FORM_ACCOUNTS_ID",currData.getFinBookingFormAccountsId(), Types.BIGINT);
		namedParameters.addValue("PREV_FIN_BOOKING_FORM_ACCOUNTS_ID",prevData.getFinBookingFormAccountsId(), Types.BIGINT);
		
		int result = nmdPJdbcTemplate.update(query.toString(), namedParameters);
		
		return result;
	}
	
	@Override
	public List<FinBookingFormAccountsPojo> getFlatPaidAmountDetails(BookingFormRequest bookingFormRequest, List<Long> types) {

		StringBuilder query = SqlQuery.QRY_TO_GET_FIN_BOOKING_FORM_ACCOUNTS_PAY_PAID_DATA;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("TYPEs", types , Types.BIGINT);
		namedParameters.addValue("BOOKING_FORM_ID",bookingFormRequest.getFlatBookingId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID",Status.ACTIVE.getStatus(), Types.BIGINT);
		
		List<FinBookingFormAccountsPojo> milestonePaidList = getData(query.toString(), namedParameters, FinBookingFormAccountsPojo.class);

		return milestonePaidList;
	}

	@Override
	public List<FinBookingFormAccountsPojo> getSumOfFlatPaidAmountDetails(CustomerPropertyDetailsInfo customerPropertyDetailsInfo, List<Long> types) {
		StringBuilder query = SqlQuery.QRY_TO_GET_SUM_BOOKING_FORM_ACCOUNTS_PAID;
		//query.append("");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("TYPEs", types , Types.BIGINT);
		namedParameters.addValue("BOOKING_FORM_ID",customerPropertyDetailsInfo.getFlatBookingId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID",Status.ACTIVE.getStatus(), Types.BIGINT);
		
		List<FinBookingFormAccountsPojo> milestonePaidList = getData(query.toString(), namedParameters, FinBookingFormAccountsPojo.class);
		return milestonePaidList;
	}
	
	@Override
	public List<FinBookingFormAccountsPojo> getSumOfFlatPaidAmountDetailsByInvoiveNo(CustomerPropertyDetailsInfo info, List<Long> types) {
		StringBuilder query = FinancialQuerys.QRY_TO_GET_SUM_BOOKING_FORM_ACCOUNTS_PAID_BY_INVOICE_NO;
		//query.append("");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("TYPEs", types , Types.BIGINT);
		namedParameters.addValue("BOOKING_FORM_ID",info.getFlatBookingId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID",Status.ACTIVE.getStatus(), Types.BIGINT);
		
		List<FinBookingFormAccountsPojo> milestonePaidList = getData(query.toString(), namedParameters, FinBookingFormAccountsPojo.class);
		return milestonePaidList;
	}
	
	@Override
	public List<FinBookingFormExcessAmountPojo> getSumOfFlatPaidExcessAmount(CustomerPropertyDetailsInfo customerPropertyDetailsInfo, List<Long> paymentTypes) {
		StringBuilder query = new StringBuilder()
				.append(" SELECT  FBFEA.TYPE,FBFR.BOOKING_FORM_ID,SUM(NVL(FBFEA.EXCESS_AMOUNT,0)) AS EXCESS_AMOUNT, ")
				.append(" SUM(NVL(FBFEA.USED_AMOUNT,0)) AS USED_AMOUNT,SUM(NVL(FBFEA.BALANCE_AMOUNT,0)) AS BALANCE_AMOUNT ")
				.append(",(SELECT MT.NAME FROM METADATA_TYPES MT WHERE FBFEA.TYPE = MT.METADATA_TYPES_ID ) AS METADATA_NAME")
				.append(" FROM FIN_BOOKING_FORM_EXCESS_AMOUNT FBFEA,FIN_BOOKING_FORM_RECEIPTS FBFR ")
				.append(" WHERE FBFR.FIN_BOK_FRM_RCPT_ID = FBFEA.FIN_BOK_FRM_RCPT_ID ")
				.append(" AND FBFEA.TYPE IN (:TYPEs)  ")
				.append(" AND FBFR.BOOKING_FORM_ID = :BOOKING_FORM_ID ")
				.append(" AND FBFR.STATUS_ID = :STATUS_ID ")
				.append(" GROUP BY FBFEA.TYPE ,FBFR.BOOKING_FORM_ID ");

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("TYPEs", paymentTypes , Types.BIGINT);
		namedParameters.addValue("BOOKING_FORM_ID",customerPropertyDetailsInfo.getFlatBookingId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID",Status.ACTIVE.getStatus(), Types.BIGINT);
		
		List<FinBookingFormExcessAmountPojo> milestonePaidList = getData(query.toString(), namedParameters, FinBookingFormExcessAmountPojo.class);

		return milestonePaidList;
	}
	
	@Override
	public List<FinBookingFormExcessAmountPojo> getSumOfFlatPaidExcessAmountByInvoiveNo(CustomerPropertyDetailsInfo info, List<Long> paymentTypes) {
		StringBuilder query = new StringBuilder()
				.append(" SELECT  FBFEA.TYPE,FBFR.BOOKING_FORM_ID,SUM(NVL(FBFEA.EXCESS_AMOUNT,0)) AS EXCESS_AMOUNT, ")
				.append(" SUM(NVL(FBFEA.USED_AMOUNT,0)) AS USED_AMOUNT,SUM(NVL(FBFEA.BALANCE_AMOUNT,0)) AS BALANCE_AMOUNT ")
				.append(",(SELECT MT.NAME FROM METADATA_TYPES MT WHERE FBFEA.TYPE = MT.METADATA_TYPES_ID ) AS METADATA_NAME")
				.append(",FBFEA.FIN_BOK_ACC_INVOICE_NO")
				.append(" FROM FIN_BOOKING_FORM_EXCESS_AMOUNT FBFEA,FIN_BOOKING_FORM_RECEIPTS FBFR ")
				.append(" WHERE FBFR.FIN_BOK_FRM_RCPT_ID = FBFEA.FIN_BOK_FRM_RCPT_ID ")
				.append(" AND FBFEA.TYPE IN (:TYPEs) ")
				.append(" AND FBFR.BOOKING_FORM_ID = :BOOKING_FORM_ID ")
				.append(" AND FBFR.STATUS_ID = :STATUS_ID ")
				.append(" GROUP BY FBFEA.TYPE,FBFEA.FIN_BOK_ACC_INVOICE_NO ,FBFR.BOOKING_FORM_ID ");

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("TYPEs", paymentTypes , Types.BIGINT);
		namedParameters.addValue("BOOKING_FORM_ID",info.getFlatBookingId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID",Status.ACTIVE.getStatus(), Types.BIGINT);
		
		List<FinBookingFormExcessAmountPojo> milestonePaidList = getData(query.toString(), namedParameters, FinBookingFormExcessAmountPojo.class);
		return milestonePaidList;
	}
	
	@Override
	public Long loadBlockCompletionPercent(BookingFormRequest bookingFormRequest) {
		long workCompletion = 0;
		StringBuilder query = SqlQuery.QRY_TO_GET_BLOCK_COMPLETION_PERCENTAGE_DATA;
		//StringBuilder query = SqlQuery.QRY_TO_GET_BLOCK_COMPLETION_PERCENTAGE_DATA_BY_SITE;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("BOOKING_FORM_ID",bookingFormRequest.getFlatBookingId(), Types.BIGINT);
		namedParameters.addValue("SITE_ID",bookingFormRequest.getSiteId(), Types.BIGINT);
		namedParameters.addValue("PROJECT_MS_STATUS_ID",Status.MS_COMPLETED.getStatus(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID",Status.ACTIVE.getStatus(), Types.BIGINT);
		try {
			workCompletion = nmdPJdbcTemplate.queryForObject(query.toString(), namedParameters, Long.class);	
		} catch (Exception e) {
			log.info(e, e);
		}
		
		return workCompletion;
	}
	
	@Override//not in 
	public List<FinBookingFormAccountsPojo> getCustomerInvoices(EmployeeFinancialTransactionServiceInfo transactionServiceInfo) {
		//log.info("*** The Control is inside the getCustomerInvoices in EmployeeFinancialServiceDaoImpl ***");
		StringBuilder query = SqlQuery.QRY_TO_GET_CUSTOMER_INVOICES;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("TYPEs", Arrays.asList(MetadataId.MODIFICATION_COST.getId(),MetadataId.LEGAL_COST.getId()), Types.BIGINT);
		namedParameters.addValue("BOOKING_FORM_ID",transactionServiceInfo.getBookingFormIds(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID",Status.ACTIVE.getStatus(), Types.BIGINT);
		
		List<FinBookingFormAccountsPojo> invoiceList = getData(query.toString(), namedParameters, FinBookingFormAccountsPojo.class);

		return invoiceList;
		
		/*SELECT FBFA.TYPE,FBFA.TYPE_ID,FBFA.PAY_AMOUNT,FBFA.PAID_AMOUNT,FBFA.BOOKING_FORM_ID,
		FBFA.FIN_BOK_ACC_INVOICE_NO,FBFA.CREATED_DATE,
		FBFLC.FIN_BOK_FRM_LEGAL_COST_ID,FBFMC.FIN_BOK_FRM_MODI_COST_ID,
		NVL(FBFLC.LEGAL_AMOUNT,FBFMC.BASIC_AMOUNT) AS BASIC_AMOUNT,
		NVL(FBFLC.TAX_AMOUNT,FBFMC.TAX_AMOUNT) AS TAX_AMOUNT,
		NVL(FBFLC.TOTAL_AMOUNT,FBFMC.TOTAL_AMOUNT) AS TOTAL_AMOUNT,
		NVL(FIDL1.DOC_LOCATION,FIDL2.DOC_LOCATION) AS DOC_LOCATION,
		NVL(FIDL1.DOC_NAME,FIDL2.DOC_NAME) AS DOC_NAME
		 FROM FIN_BOOKING_FORM_ACCOUNTS FBFA
		LEFT OUTER JOIN FIN_BOOKING_FORM_LEGAL_COST FBFLC 
		ON FBFA.TYPE_ID = FBFLC.FIN_BOK_FRM_LEGAL_COST_ID AND FBFA.TYPE = 35 AND FBFLC.STATUS_ID=6 
		LEFT OUTER JOIN FIN_INVOICE_DOCUMENT_LOCATION FIDL1 ON  FIDL1.TYPE = 35 AND  FIDL1.TYPE_ID = FBFLC.FIN_BOK_FRM_LEGAL_COST_ID 
		LEFT OUTER JOIN FIN_BOOKING_FORM_MODI_COST FBFMC
		ON FBFA.TYPE_ID = FBFMC.FIN_BOK_FRM_MODI_COST_ID AND FBFA.TYPE = 34  AND FBFMC.STATUS_ID=6 
		LEFT OUTER JOIN FIN_INVOICE_DOCUMENT_LOCATION FIDL2 ON  FIDL2.TYPE = 34 AND  FIDL2.TYPE_ID = FBFMC.FIN_BOK_FRM_MODI_COST_ID 
		WHERE FBFA.TYPE IN (34,35) AND FBFA.STATUS_ID=6
		AND FBFA.BOOKING_FORM_ID = 12
		ORDER BY FBFA.TYPE;*/	
	}
	
	@Override
	public List<FinTransactionEntryPojo> getMisPendingTransactions(EmployeeFinancialTransactionServiceInfo tansactionServiceInfo) {
		//log.info("*** The Control is inside the getMisPendingTransactions in EmployeeFinancialServiceDaoImpl ***");
		String condition = tansactionServiceInfo.getCondition()==null?"":tansactionServiceInfo.getCondition();
		String actionUrl = tansactionServiceInfo.getActionUrl()==null?"":tansactionServiceInfo.getActionUrl();
		boolean isConditionNotEmpty = Util.isNotEmptyObject(condition);
		boolean searchCondiftionOnCreatedDate = false;
		boolean isThisApproveTransactionReq = (condition.equalsIgnoreCase("approveTransaction"));
		boolean isThisCompletedTransaction = (condition.equalsIgnoreCase("loadCompletedTransaction"));
		boolean isViewCustomerData = (condition.equalsIgnoreCase("ViewCustomerData"));
		boolean getCustomerDataForBooking = (condition.equalsIgnoreCase("GetCustomerDataForBooking"));
		boolean isRequestedModifyTrnData = (condition.equalsIgnoreCase("modifyTransaction"));
		boolean isRequestedInterestWaiverData = (actionUrl.equalsIgnoreCase("Interest Waiver"));
		StringBuilder query = new StringBuilder(" SELECT ")
				.append(SqlQuery.QRY_TO_GET_FIN_TRANSACTION_ENTRY)
				.append(" ,(CUST.FIRST_NAME || ' ' || CUST.LAST_NAME) AS CUSTOMER_NAME ")
				.append(" ,FTM.NAME AS TRANSACTION_MODE_NAME ")
				.append(" ,FTT.TRANSACTION_TYPE_ID ")
				.append(" ,FTT.NAME AS TRANSACTION_TYPE_NAME ")
				.append(" ,INITCAP(S.STATUS) AS TRANSACTION_STATUS_NAME ")
				.append(" ,FTL.LEVEL_NAME AS PENDING_BY_LEVEL1")
				.append(" ,SI.NAME AS SITE_NAME")
				.append(" ,FLT.FLAT_NO ,FLT.FLAT_ID,FB.STATUS_ID AS FLAT_BOOKING_STATUS_ID")
				.append(",FIB.BANK_NAME ")
				.append(",NVL(FTC.CHEQUE_NO,FTO.REFERENCE_NO) AS CHEQUE_OR_REFERENCE_NO")
				//.append(",NVL(FTC.CHEQUE_DATE,FTO.TRANSACTION_DATE) AS CHEQUE_OR_ONLINE_DATE")
				.append(",FTE.RECEIVED_DATE AS CHEQUE_OR_ONLINE_DATE")
				.append(",FTC.CHEQUE_DEPOSITED_DATE,FTC.CHEQUE_BOUNCE_COMMENT,FTC.CLEARANCE_DATE,FTC.CHEQUE_HANDOVER_DATE,FTC.CHEQUE_BOUNCE_DATE")
				.append(",FTE.FIN_SITE_PROJ_ACC_MAP_ID AS FIN_PROJ_ACC_ID ,FPA.ACCOUNT_NO ")
				.append(",(SELECT LISTAGG(NVL((EMP.FIRST_NAME || ' ' || EMP.LAST_NAME),'N/A'), ',')   WITHIN GROUP (ORDER BY  FTESOLM1.FIN_TRAN_SET_OFF_LEVEL_ID)  AS EMP_NAME FROM FIN_TRN_EMP_SET_OFF_LEVEL_MAPPING FTESOLM1 JOIN EMPLOYEE EMP ")  
				.append(" ON(EMP.EMP_ID=FTESOLM1.EMP_ID)")
				.append(" WHERE FTESOLM1.FIN_TRAN_SET_OFF_LEVEL_ID = FTESOLM.FIN_TRAN_SET_OFF_LEVEL_ID AND FTESOLM1.STATUS_ID = :STATUS_ID)  AS PENDING_BY_LEVEL")
				.append(",(SELECT CONCAT(EMP.FIRST_NAME,' ' || EMP.LAST_NAME) FROM FIN_TRANSACTION_APPR_STAT FTAS JOIN EMPLOYEE EMP  ON(EMP.EMP_ID=FTAS.EMP_ID) WHERE FIN_TRN_APPR_STAT_ID = (SELECT MAX(FIN_TRN_APPR_STAT_ID) FROM FIN_TRANSACTION_APPR_STAT WHERE  FIN_TRN_SET_OFF_ENT_ID = FTSOE. FIN_TRN_SET_OFF_ENT_ID) ) AS LAST_APPR_BY ");
				if(isThisApproveTransactionReq || isViewCustomerData || isRequestedModifyTrnData) {
					query.append(", (SELECT  LISTAGG(CONCAT(NVL(FTED.LOCATION,0),'##'||FTED.DOC_NAME), '@@')   WITHIN GROUP (ORDER BY FTED.FIN_TRANSACTION_ENTRY_DOC_ID) FROM FIN_TRANSACTION_ENTRY_DOC FTED ");
					query.append("WHERE FTED.FIN_TRANSACTION_ENTRY_ID=FTE.FIN_TRANSACTION_ENTRY_ID AND FTED.STATUS_ID = 6) AS MULTIPLE_LOCATION");							 
					//namedParameters.addValue("DOC_TYPE", MetadataId.UPLOADED.getId(),Types.BIGINT);
					/*,( SELECT    LISTAGG(MT.NAME, ',')   WITHIN GROUP (ORDER BY FTSO.FIN_TRN_SET_OFF_ENT_ID)
							   FROM FIN_TRANSACTION_SET_OFF FTSO,
							  METADATA_TYPES MT
							   WHERE FTSOE.FIN_TRN_SET_OFF_ENT_ID = FTSO.FIN_TRN_SET_OFF_ENT_ID
							   AND MT.METADATA_TYPES_ID = FTSO.SET_OFF_TYPE
							   AND FTSO.STATUS_ID = 6) as PaymentSetOff*/
				 
				}
				if(isThisCompletedTransaction || isViewCustomerData) {
					query.append(", (SELECT  LISTAGG(CONCAT(NVL(FTED.LOCATION,0),'##'||FTED.DOC_NAME), '@@') WITHIN GROUP (ORDER BY FTED.FIN_TRANSACTION_ENTRY_DOC_ID) FROM FIN_TRANSACTION_ENTRY_DOC FTED  ");
					query.append("WHERE FTED.FIN_TRANSACTION_ENTRY_ID=FTE.FIN_TRANSACTION_ENTRY_ID AND FTED.DOC_TYPE = :DOC_TYPE AND FTED.STATUS_ID = 6) AS TRANSACTION_RECEIPT");
				}
				if(isThisApproveTransactionReq ||isViewCustomerData ||isRequestedModifyTrnData) {
					query.append(" ,(SELECT FTAS.CREATED_DATE FROM   fin_transaction_appr_stat FTAS  WHERE  fin_trn_appr_stat_id = (SELECT Max(fin_trn_appr_stat_id) FROM   fin_transaction_appr_stat WHERE fin_trn_set_off_ent_id =  FTSOE.fin_trn_set_off_ent_id)) AS LAST_APPROVED_DATE   ");
				}
				//.append(" ,ED.EMP_DESIGNATION AS PENDING_BY_DEPT")
				//.append(" ,DEPT.NAME AS TRANSACTION_PENDING_DEPT_NAME")
				//.append(" ,DEPT.DEPT_ID AS TRANSACTION_PENDING_DEPT_ID")
				query.append(" FROM FIN_TRANSACTION_ENTRY FTE ")
				.append(" LEFT OUTER JOIN FIN_SITE_PROJ_ACC_MAPPING FSPAM ")
				.append(" ON(FSPAM.FIN_SITE_PROJ_ACC_MAP_ID=FTE.FIN_SITE_PROJ_ACC_MAP_ID) ")
				.append(" LEFT OUTER JOIN  FIN_PROJECT_ACCOUNTS FPA ")
				.append(" ON(FPA.FIN_PROJ_ACC_ID=FSPAM.FIN_PROJ_ACC_ID) ")
				.append(" JOIN SITE SI ON SI.SITE_ID = FTE.SITE_ID ");
				if(isThisApproveTransactionReq) {
					//query.append(" FULL JOIN FIN_TRANSACTION_ENTRY_DOC FTED ")
				    //.append(" ON(FTED.FIN_TRANSACTION_ENTRY_ID=FTE.FIN_TRANSACTION_ENTRY_ID) ");
				}
				query.append(" LEFT OUTER JOIN FIN_TRANSACTION_CHEQUE FTC ON (FTC.FIN_TRANSACTION_ENTRY_ID = FTE.FIN_TRANSACTION_ENTRY_ID )")
		        .append(" LEFT OUTER JOIN FIN_TRANSACTION_ONLINE FTO  ON (FTO.FIN_TRANSACTION_ENTRY_ID = FTE.FIN_TRANSACTION_ENTRY_ID)")
		        .append(" LEFT OUTER JOIN FIN_BANK FIB ON FTE.FIN_BANK_ID = FIB.FIN_BANK_ID")
				.append(SqlQuery.QRY_TO_GET_LEVEL_NAME_BY_EMPID)
				.append(" JOIN FIN_TRANSACTION_SET_OFF FTSO ON FTSO.FIN_TRN_SET_OFF_ENT_ID = FTSOE. FIN_TRN_SET_OFF_ENT_ID")
				.append(" JOIN FLAT_BOOKING FB ON(FB.FLAT_BOOK_ID = FTE.BOOKING_FORM_ID ) ")//AND FB.STATUS_ID = :STATUS_ID
				.append(" JOIN FLAT FLT  ON (FB.FLAT_ID = FLT.FLAT_ID AND FLT.STATUS_ID = :STATUS_ID )")
				.append(" JOIN CUSTOMER CUST ON (CUST.CUST_ID = FB.CUST_ID) ")
				.append(" JOIN FIN_TRANSACTION_MODE FTM ON(FTM.TRANSACTION_MODE_ID=FTE.FIN_TRANSACTION_MODE_ID) ")
				.append(" JOIN FIN_TRANSACTION_TYPE FTT ON(FTT.TRANSACTION_TYPE_ID=FTE.FIN_TRANSACTION_TYPE_ID) ")
				.append(" FULL JOIN STATUS S ON (S.STATUS_ID = FTE.TRANSACTION_STATUS_ID) ")
				.append(" WHERE FTE.STATUS_ID=:STATUS_ID ")
				.append(" AND FTE.TRANSACTION_STATUS_ID IN (:TRANSACTION_STATUS_ID)");
			
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		//namedParameters.addValue("SITE_ID",tansactionServiceInfo.getSiteId(), Types.BIGINT);
		
		if (Util.isNotEmptyObject(tansactionServiceInfo.getActionUrl())) {
			if (tansactionServiceInfo.getActionUrl().equalsIgnoreCase("getUnclearedChequeDetails")) {
				//query.append(" AND (FTC.CLEARANCE_DATE IS NULL AND  FTC.CHEQUE_DEPOSITED_DATE IS NOT NULL)");
			}
		}
		
		if (Util.isNotEmptyObject(tansactionServiceInfo.getSiteIds())) {
			query.append(" AND FTE.SITE_ID IN (:SITE_ID) ");
			namedParameters.addValue("SITE_ID",tansactionServiceInfo.getSiteIds(), Types.BIGINT);
		}

		if (Util.isNotEmptyObject(tansactionServiceInfo.getTransactionTypeId())) {
			query.append(" AND FTE.FIN_TRANSACTION_TYPE_ID = :FIN_TRANSACTION_TYPE_ID ");
			namedParameters.addValue("FIN_TRANSACTION_TYPE_ID",tansactionServiceInfo.getTransactionTypeId(), Types.BIGINT);
		}
		if (Util.isNotEmptyObject(tansactionServiceInfo.getTransactionModeId())) {
			query.append(" AND FTE.FIN_TRANSACTION_MODE_ID  = :FIN_TRANSACTION_MODE_ID ");
			namedParameters.addValue("FIN_TRANSACTION_MODE_ID",tansactionServiceInfo.getTransactionModeId(), Types.BIGINT);
		}
		if (Util.isNotEmptyObject(tansactionServiceInfo.getSiteAccountIds())) {
			query.append(" AND FTE.FIN_SITE_PROJ_ACC_MAP_ID IN (:FIN_SITE_PROJ_ACC_MAP_ID) ");
			namedParameters.addValue("FIN_SITE_PROJ_ACC_MAP_ID", tansactionServiceInfo.getSiteAccountIds(),Types.BIGINT);
		}

		if (Util.isNotEmptyObject(tansactionServiceInfo.getSearchBySetOffTypes())) {
			query.append(" AND FTSO.SET_OFF_TYPE IN (:SER_SET_OFF_TYPE) ");
			namedParameters.addValue("SER_SET_OFF_TYPE", tansactionServiceInfo.getSearchBySetOffTypes(), Types.BIGINT);
		}
		if (isConditionNotEmpty) {
			if(isRequestedInterestWaiverData) {//is this is request from interest waiver
				/*query.append(" AND FTE.FIN_TRANSACTION_ENTRY_ID IN (SELECT FTSOE.FIN_TRANSACTION_ENTRY_ID ")
				  .append(" FROM FIN_TRANSACTION_SET_OFF FTSO,")
				  .append(" FIN_TRANSACTION_SET_OFF_ENTRY FTSOE ")
				  .append(" ")
				  .append(" WHERE FTSOE.FIN_TRN_SET_OFF_ENT_ID = FTSO.FIN_TRN_SET_OFF_ENT_ID")
				  .append(" ")
				  .append(" AND FTSOE.FIN_TRANSACTION_ENTRY_ID = FTE.FIN_TRANSACTION_ENTRY_ID")
				  .append(" AND FTSO.STATUS_ID = :STATUS_ID AND FTSO.SET_OFF_TYPE = :INTEREST_WAIVER ")
				  .append(" AND NVL(FTSO.SET_OFF_AMOUNT,0) != 0) ");
				namedParameters.addValue("INTEREST_WAIVER", MetadataId.INTEREST_WAIVER.getId(),Types.BIGINT);*/
				
				query.append(" AND FTE.FIN_TRANSACTION_TYPE_ID = :INTEREST_WAIVER_TYPE_ID ");
				if(Util.isNotEmptyObject(tansactionServiceInfo.getFromDate())&&Util.isNotEmptyObject(tansactionServiceInfo.getToDate())) {
					query.append(" AND TRUNC(FTE.PAYMENT_DATE) BETWEEN TRUNC(:FROM_DATE) AND TRUNC(:TO_DATE)");
				} else if (Util.isNotEmptyObject(tansactionServiceInfo.getFromDate())) {
					query.append(" AND TRUNC(FTE.PAYMENT_DATE) >= TRUNC(:FROM_DATE) ");
				} else if (Util.isNotEmptyObject(tansactionServiceInfo.getToDate())) {
					query.append(" AND TRUNC(FTE.PAYMENT_DATE) <= TRUNC(:TO_DATE)");
				}
				
				if (Util.isNotEmptyObject(tansactionServiceInfo.getBookingFormIds())) {
					query.append(" AND FTE.BOOKING_FORM_ID IN (:BOOKING_FORM_ID) ");
				}
				
				namedParameters.addValue("FROM_DATE", tansactionServiceInfo.getFromDate(),Types.DATE);
				namedParameters.addValue("TO_DATE", tansactionServiceInfo.getToDate(),Types.DATE);
				namedParameters.addValue("BOOKING_FORM_ID", tansactionServiceInfo.getBookingFormIds() ,Types.BIGINT);
				namedParameters.addValue("INTEREST_WAIVER_TYPE_ID",FinTransactionType.INTEREST_WAIVER.getId(), Types.BIGINT);
			} else if(isThisApproveTransactionReq){
				query.append(" AND FTE.FIN_TRANSACTION_TYPE_ID != :INTEREST_WAIVER_TYPE_ID ");
				namedParameters.addValue("INTEREST_WAIVER_TYPE_ID",FinTransactionType.INTEREST_WAIVER.getId(), Types.BIGINT);
			}
			
			if (tansactionServiceInfo.getCondition().equalsIgnoreCase("transactionStatus")) {
				searchCondiftionOnCreatedDate = false;
				//namedParameters.addValue("TRANSACTION_STATUS_ID", Arrays.asList(Status.NEW.getStatus(),Status.MODIFY.getStatus(),Status.PENDING.getStatus(),Status.APPROVED.getStatus(),Status.REJECTED.getStatus(),Status.UNCLEARED_CHEQUE.getStatus(),Status.CHEQUE_BOUNCED.getStatus()));
				namedParameters.addValue("TRANSACTION_STATUS_ID", Arrays.asList(Status.NEW.getStatus(),Status.MODIFY.getStatus(),Status.PENDING.getStatus(),Status.APPROVED.getStatus(),Status.UNCLEARED_CHEQUE.getStatus()));
			
				if(Util.isNotEmptyObject(tansactionServiceInfo.getTrnCreatedFromDate()) && Util.isNotEmptyObject(tansactionServiceInfo.getTrnCreatedTotoDate())) {
					query.append(" AND TRUNC(FTE.CREATED_DATE) BETWEEN TRUNC(:getTrnCreatedFromDate) AND TRUNC(:getTrnCreatedTotoDate)");
					searchCondiftionOnCreatedDate = true;
				} else if (Util.isNotEmptyObject(tansactionServiceInfo.getTrnCreatedFromDate())) {
					query.append(" AND TRUNC(FTE.CREATED_DATE) >= TRUNC(:getTrnCreatedFromDate) ");
					searchCondiftionOnCreatedDate = true;
				} else if (Util.isNotEmptyObject(tansactionServiceInfo.getTrnCreatedTotoDate())) {
					query.append(" AND TRUNC(FTE.CREATED_DATE) <= TRUNC(:getTrnCreatedTotoDate)");
					searchCondiftionOnCreatedDate = true;
				}

				namedParameters.addValue("getTrnCreatedFromDate", tansactionServiceInfo.getTrnCreatedFromDate(),Types.DATE);
				namedParameters.addValue("getTrnCreatedTotoDate", tansactionServiceInfo.getTrnCreatedTotoDate(),Types.DATE);
				
				if(!searchCondiftionOnCreatedDate) {//if search in on created date, then no need using received date wise
					if(Util.isNotEmptyObject(tansactionServiceInfo.getReceivedFromDate()) && Util.isNotEmptyObject(tansactionServiceInfo.getReceivedToDate())) {
						query.append(" AND TRUNC(FTE.RECEIVED_DATE) BETWEEN TRUNC(:getReceivedFromDate) AND TRUNC(:getReceivedToDate)");
					} else if (Util.isNotEmptyObject(tansactionServiceInfo.getReceivedFromDate())) {
						query.append(" AND TRUNC(FTE.RECEIVED_DATE) >= TRUNC(:getReceivedFromDate) ");
					} else if (Util.isNotEmptyObject(tansactionServiceInfo.getReceivedToDate())) {
						query.append(" AND TRUNC(FTE.RECEIVED_DATE) <= TRUNC(:getReceivedToDate)");
					}
					
					namedParameters.addValue("getReceivedFromDate", tansactionServiceInfo.getReceivedFromDate(),Types.DATE);
					namedParameters.addValue("getReceivedToDate", tansactionServiceInfo.getReceivedToDate(),Types.DATE);
				}
				
				if (Util.isNotEmptyObject(tansactionServiceInfo.getFlatIds())) {
					query.append(" AND FB.FLAT_ID IN (:FLAT_ID) ");
				}
				if (Util.isNotEmptyObject(tansactionServiceInfo.getBookingFormIds())) {
					query.append(" AND FTE.BOOKING_FORM_ID IN (:BOOKING_FORM_ID) ");
				}
				
				if(Util.isNotEmptyObject(tansactionServiceInfo.getPendingTrnByEmpId())) {
					query.append(" AND FTESOLM.EMP_ID = :TRN_PENDING_EMP_ID");
					query.append(" AND FTESOLM.STATUS_ID = :STATUS_ID");
					namedParameters.addValue("TRN_PENDING_EMP_ID", tansactionServiceInfo.getPendingTrnByEmpId(), Types.BIGINT);
				}
				
			} else if (isThisApproveTransactionReq) {
				query.append(" AND FTESOLM.EMP_ID = :EMP_ID");
				query.append(" AND FTESOLM.STATUS_ID = :STATUS_ID");
				namedParameters.addValue("EMP_ID", tansactionServiceInfo.getEmployeeId(), Types.BIGINT);
				if(Util.isNotEmptyObject(tansactionServiceInfo.getActionUrl()) && tansactionServiceInfo.getActionUrl().equalsIgnoreCase("getUnclearedChequeDetails")) {
					//query.append(" AND (FTC.CLEARANCE_DATE IS NULL AND  FTC.CHEQUE_DEPOSITED_DATE IS NOT NULL)");
					//namedParameters.addValue("TRANSACTION_STATUS_ID", new ArrayList<Long>(Arrays.asList(Status.UNCLEARED_CHEQUE.getStatus())));
					namedParameters.addValue("TRANSACTION_STATUS_ID", Arrays.asList(Status.UNCLEARED_CHEQUE.getStatus()));
				} else {
					namedParameters.addValue("TRANSACTION_STATUS_ID", Arrays.asList(Status.NEW.getStatus(),Status.APPROVED.getStatus(),Status.PENDING.getStatus()));
				}
			} else if(isRequestedModifyTrnData) {
				query.append(" AND FTESOLM.EMP_ID = :EMP_ID");
				query.append(" AND FTESOLM.STATUS_ID = :STATUS_ID");
				namedParameters.addValue("EMP_ID", tansactionServiceInfo.getEmployeeId(), Types.BIGINT);
				namedParameters.addValue("TRANSACTION_STATUS_ID", Arrays.asList(Status.MODIFY.getStatus()));
			} else if (isThisCompletedTransaction) {
			
				if(Util.isNotEmptyObject(tansactionServiceInfo.getFromDate())&&Util.isNotEmptyObject(tansactionServiceInfo.getToDate())) {
					query.append(" AND TRUNC(FTE.CREATED_DATE) BETWEEN TRUNC(:FROM_DATE) AND TRUNC(:TO_DATE)");
				} else if (Util.isNotEmptyObject(tansactionServiceInfo.getFromDate())) {
					query.append(" AND TRUNC(FTE.CREATED_DATE) >= TRUNC(:FROM_DATE) ");
				} else if (Util.isNotEmptyObject(tansactionServiceInfo.getToDate())) {
					query.append(" AND TRUNC(FTE.CREATED_DATE) <= TRUNC(:TO_DATE)");
				}
				
				if(Util.isNotEmptyObject(tansactionServiceInfo.getFlatIds())) {
					query.append(" AND FB.FLAT_ID IN (:FLAT_ID) ");
				} if (Util.isNotEmptyObject(tansactionServiceInfo.getBookingFormIds())) {
					query.append(" AND FTE.BOOKING_FORM_ID IN (:BOOKING_FORM_ID) ");
				}
				
				namedParameters.addValue("FLAT_ID", tansactionServiceInfo.getFlatIds() ,Types.BIGINT);
				namedParameters.addValue("BOOKING_FORM_ID", tansactionServiceInfo.getBookingFormIds() ,Types.BIGINT);
				namedParameters.addValue("DOC_TYPE", MetadataId.GENERATED.getId(),Types.BIGINT);
				namedParameters.addValue("FROM_DATE", tansactionServiceInfo.getFromDate(),Types.DATE);
				namedParameters.addValue("TO_DATE", tansactionServiceInfo.getToDate(),Types.DATE);
				namedParameters.addValue("TRANSACTION_STATUS_ID", Arrays.asList(Status.TRANSACTION_COMPLETED.getStatus()));
			} else if (isViewCustomerData) {
				query.append(" AND FTE.BOOKING_FORM_ID = :BOOKING_FORM_ID ");
				namedParameters.addValue("DOC_TYPE", MetadataId.GENERATED.getId(),Types.BIGINT);
				namedParameters.addValue("TRANSACTION_STATUS_ID", new ArrayList<Long>(Arrays.asList(Status.UNCLEARED_CHEQUE.getStatus(),Status.TRANSACTION_COMPLETED.getStatus(),Status.CHEQUE_BOUNCED.getStatus(),Status.NEW.getStatus(),Status.MODIFY.getStatus(),Status.APPROVED.getStatus(),Status.PENDING.getStatus())));
				namedParameters.addValue("BOOKING_FORM_ID", tansactionServiceInfo.getBookingFormId() ,Types.BIGINT);
			}  else if (getCustomerDataForBooking) {
				query.append(" AND FTE.BOOKING_FORM_ID = :BOOKING_FORM_ID ");
				
				query.append(" AND FTE.FIN_TRANSACTION_TYPE_ID = :FIN_TRANSACTION_TYPE_ID ");
				namedParameters.addValue("FIN_TRANSACTION_TYPE_ID",FinTransactionType.RECEIPT.getId(), Types.BIGINT);
				
				namedParameters.addValue("DOC_TYPE", MetadataId.GENERATED.getId(),Types.BIGINT);
				namedParameters.addValue("TRANSACTION_STATUS_ID",  Arrays.asList(Status.TRANSACTION_COMPLETED.getStatus()));
				namedParameters.addValue("BOOKING_FORM_ID", tansactionServiceInfo.getBookingFormId() ,Types.BIGINT);
			}
		}
		
		query.append(" ORDER BY  NVL(FTE.RECEIVED_DATE,FTE.PAYMENT_DATE),FTE.FIN_TRANSACTION_ENTRY_ID ");
		
		List<FinTransactionEntryPojo> finTransactionEntryPojoLists = getData(query.toString(), namedParameters, FinTransactionEntryPojo.class);
		
		if (isConditionNotEmpty) {
			if (isThisApproveTransactionReq || isViewCustomerData || isRequestedModifyTrnData) {
				String query1 = new StringBuffer(SqlQuery.QRY_TO_GET_FIN_TRANSACTION_ENTRY_PAYMENT_SET_OFF)
						.append(" AND FTSOE.FIN_TRANSACTION_ENTRY_ID = :FIN_TRANSACTION_ENTRY_ID")
						.toString();
				int index = 0;
				for (FinTransactionEntryPojo finTransactionEntryPojo : finTransactionEntryPojoLists) {
					if(finTransactionEntryPojo.getTransactionTypeId().equals(FinTransactionType.INTEREST_WAIVER.getId())) {
						finTransactionEntryPojo.setTransactionPaymentSetOff(FinTransactionType.INTEREST_WAIVER.getName());
						continue;
					}
					index = 0;
					namedParameters = new MapSqlParameterSource();
					StringBuffer data = new StringBuffer("");
					namedParameters.addValue("FIN_TRANSACTION_ENTRY_ID",finTransactionEntryPojo.getTransactionEntryId(), Types.BIGINT);
					namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
					List<FinTransactionSetOffPojo> transactionEntryPojoPaymentSetOffLists = getData(query1, namedParameters, FinTransactionSetOffPojo.class);
					for (index = 0; index < transactionEntryPojoPaymentSetOffLists.size(); index++) {
						FinTransactionSetOffPojo setOffPojo = transactionEntryPojoPaymentSetOffLists.get(index);
						
						//long setOffType = setOffPojo.getSetOffType();
						String setOffTypeName = setOffPojo.getSetOffTypeName();
						String payemntSetOff = EmployeeFinancialServiceImpl.getConvenienceNameFromMetaData(setOffTypeName);
						
						if(index==transactionEntryPojoPaymentSetOffLists.size()-1) {
							data.append(payemntSetOff);	
						}else {
							data.append(payemntSetOff).append(",");
						}
					}
					if(isViewCustomerData) {
						//if the count more the one show the default payment set off  
						if(index>1) {
							finTransactionEntryPojo.setTransactionPaymentSetOff(data.toString());
						} else {
							finTransactionEntryPojo.setTransactionPaymentSetOff(data.toString());
						}
					}else {
						finTransactionEntryPojo.setTransactionPaymentSetOff(data.toString());
					}
				}
			}
		}
		
		return finTransactionEntryPojoLists;
	}

	@Override
	public List<FinTransactionEntryPojo> getMisPendingTransactionsStatus(EmployeeFinancialTransactionServiceInfo tansactionServiceInfo) throws Exception {
		String condition = tansactionServiceInfo.getCondition()==null?"":tansactionServiceInfo.getCondition();
		String actionUrl = tansactionServiceInfo.getActionUrl()==null?"":tansactionServiceInfo.getActionUrl();
		boolean isConditionNotEmpty = Util.isNotEmptyObject(condition);
		boolean searchCondiftionOnCreatedDate = false;
		boolean isRequestedInterestWaiverData = (actionUrl.equalsIgnoreCase("Interest Waiver"));
		StringBuilder query = new StringBuilder(" SELECT ")
				.append(SqlQuery.QRY_TO_GET_FIN_TRANSACTION_ENTRY)
				.append(" ,(CUST.FIRST_NAME || ' ' || CUST.LAST_NAME) AS CUSTOMER_NAME ")
				.append(" ,FTM.NAME AS TRANSACTION_MODE_NAME,FTSO.SET_OFF_AMOUNT AS TRN_SET_OFF_AMOUNT ")
				.append(" ,FTT.TRANSACTION_TYPE_ID ")
				.append(" ,FTT.NAME AS TRANSACTION_TYPE_NAME ")
				.append(" ,INITCAP(S.STATUS) AS TRANSACTION_STATUS_NAME ")
				.append(" ,FTL.LEVEL_NAME AS PENDING_BY_LEVEL1")
				.append(" ,(SELECT MT.NAME FROM METADATA_TYPES MT WHERE MT.METADATA_TYPES_ID = FTSO.SET_OFF_TYPE  ) AS TRN_SET_OFF_NAME")
				.append(" ,SI.NAME AS SITE_NAME")
				.append(" ,FLT.FLAT_NO ,FLT.FLAT_ID,FB.STATUS_ID AS FLAT_BOOKING_STATUS_ID")
				.append(",FIB.BANK_NAME ")
				.append(",NVL(FTC.CHEQUE_NO,FTO.REFERENCE_NO) AS CHEQUE_OR_REFERENCE_NO")
				//.append(",NVL(FTC.CHEQUE_DATE,FTO.TRANSACTION_DATE) AS CHEQUE_OR_ONLINE_DATE")
				.append(",FTE.RECEIVED_DATE AS CHEQUE_OR_ONLINE_DATE")
				.append(",FTC.CHEQUE_DEPOSITED_DATE,FTC.CHEQUE_BOUNCE_COMMENT,FTC.CLEARANCE_DATE,FTC.CHEQUE_HANDOVER_DATE,FTC.CHEQUE_BOUNCE_DATE")
				.append(",FTE.FIN_SITE_PROJ_ACC_MAP_ID AS FIN_PROJ_ACC_ID,FPA.ACCOUNT_NO ")//,FPA.FIN_PROJ_ACC_ID
				.append(",(SELECT LISTAGG(NVL((EMP.FIRST_NAME || ' ' || EMP.LAST_NAME),'N/A'), ',')   WITHIN GROUP (ORDER BY  FTESOLM1.FIN_TRAN_SET_OFF_LEVEL_ID)  AS EMP_NAME FROM FIN_TRN_EMP_SET_OFF_LEVEL_MAPPING FTESOLM1 JOIN EMPLOYEE EMP ")  
				.append(" ON(EMP.EMP_ID=FTESOLM1.EMP_ID)")
				.append(" WHERE FTESOLM1.FIN_TRAN_SET_OFF_LEVEL_ID = FTESOLM.FIN_TRAN_SET_OFF_LEVEL_ID AND FTESOLM1.STATUS_ID = :STATUS_ID)  AS PENDING_BY_LEVEL")
				.append(",(SELECT CONCAT(EMP.FIRST_NAME,' ' || EMP.LAST_NAME) FROM FIN_TRANSACTION_APPR_STAT FTAS JOIN EMPLOYEE EMP  ON(EMP.EMP_ID=FTAS.EMP_ID) WHERE FIN_TRN_APPR_STAT_ID = (SELECT MAX(FIN_TRN_APPR_STAT_ID) FROM FIN_TRANSACTION_APPR_STAT WHERE  FIN_TRN_SET_OFF_ENT_ID = FTSOE. FIN_TRN_SET_OFF_ENT_ID) ) AS LAST_APPR_BY ");
				/*if(isThisApproveTransactionReq || isViewCustomerData || isRequestedModifyTrnData) {
					query.append(", (SELECT  LISTAGG(CONCAT(NVL(FTED.LOCATION,0),'##'||FTED.DOC_NAME), '@@')   WITHIN GROUP (ORDER BY FTED.FIN_TRANSACTION_ENTRY_DOC_ID) FROM FIN_TRANSACTION_ENTRY_DOC FTED ");
					query.append("WHERE FTED.FIN_TRANSACTION_ENTRY_ID=FTE.FIN_TRANSACTION_ENTRY_ID AND FTED.STATUS_ID = 6) AS MULTIPLE_LOCATION");							 
				}*/
		        if("transactionStatus".equalsIgnoreCase(tansactionServiceInfo.getCondition())) {
			        query.append(" ,(SELECT FTAS.CREATED_DATE FROM   fin_transaction_appr_stat FTAS  WHERE  fin_trn_appr_stat_id = (SELECT Max(fin_trn_appr_stat_id) FROM   fin_transaction_appr_stat WHERE fin_trn_set_off_ent_id =  FTSOE.fin_trn_set_off_ent_id)) AS LAST_APPROVED_DATE   ");
		        }
				query.append(" FROM FIN_TRANSACTION_ENTRY FTE ")
				.append(" LEFT OUTER JOIN FIN_SITE_PROJ_ACC_MAPPING FSPAM ")
				.append(" ON(FSPAM.FIN_SITE_PROJ_ACC_MAP_ID=FTE.FIN_SITE_PROJ_ACC_MAP_ID) ")
				.append(" LEFT OUTER JOIN  FIN_PROJECT_ACCOUNTS FPA ")
				.append(" ON(FPA.FIN_PROJ_ACC_ID=FSPAM.FIN_PROJ_ACC_ID) ")
				.append(" JOIN SITE SI ON SI.SITE_ID = FTE.SITE_ID ");
				query.append(" LEFT OUTER JOIN FIN_TRANSACTION_CHEQUE FTC ON (FTC.FIN_TRANSACTION_ENTRY_ID = FTE.FIN_TRANSACTION_ENTRY_ID )")
		        .append(" LEFT OUTER JOIN FIN_TRANSACTION_ONLINE FTO  ON (FTO.FIN_TRANSACTION_ENTRY_ID = FTE.FIN_TRANSACTION_ENTRY_ID)")
		        .append(" LEFT OUTER JOIN FIN_BANK FIB ON FTE.FIN_BANK_ID = FIB.FIN_BANK_ID")
				.append(SqlQuery.QRY_TO_GET_LEVEL_NAME_BY_EMPID);
				//if(Util.isNotEmptyObject(tansactionServiceInfo.getSearchBySetOffType())) {
					//if this value then we have to join FIN_TRANSACTION_SET_OFF this table, as we have select data based on payment set off type
					query.append(" JOIN FIN_TRANSACTION_SET_OFF FTSO ON FTSO.FIN_TRN_SET_OFF_ENT_ID = FTSOE. FIN_TRN_SET_OFF_ENT_ID");
				//}
				query.append(" JOIN FLAT_BOOKING FB ON(FB.FLAT_BOOK_ID = FTE.BOOKING_FORM_ID ) ")//AND FB.STATUS_ID = :STATUS_ID
				.append(" JOIN FLAT FLT  ON (FB.FLAT_ID = FLT.FLAT_ID AND FLT.STATUS_ID = :STATUS_ID )")
				.append(" JOIN CUSTOMER CUST ON (CUST.CUST_ID = FB.CUST_ID) ")
				.append(" JOIN FIN_TRANSACTION_MODE FTM ON(FTM.TRANSACTION_MODE_ID=FTE.FIN_TRANSACTION_MODE_ID) ")
				.append(" JOIN FIN_TRANSACTION_TYPE FTT ON(FTT.TRANSACTION_TYPE_ID=FTE.FIN_TRANSACTION_TYPE_ID) ")
				.append(" JOIN FLAT_DETAILS FD ON FD.FLAT_ID=FLT.FLAT_ID ")//bvr 1
				.append(" FULL JOIN STATUS S ON (S.STATUS_ID = FTE.TRANSACTION_STATUS_ID) ")
				.append(" WHERE FTE.STATUS_ID=:STATUS_ID ")
				.append(" AND FTE.TRANSACTION_STATUS_ID IN (:TRANSACTION_STATUS_ID)");
			
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		//namedParameters.addValue("SITE_ID",tansactionServiceInfo.getSiteId(), Types.BIGINT);
		
		if (Util.isNotEmptyObject(tansactionServiceInfo.getActionUrl())) {
			if (tansactionServiceInfo.getActionUrl().equalsIgnoreCase("getUnclearedChequeDetails")) {
				//query.append(" AND (FTC.CLEARANCE_DATE IS NULL AND  FTC.CHEQUE_DEPOSITED_DATE IS NOT NULL)");
			}
		}
		
		if (Util.isNotEmptyObject(tansactionServiceInfo.getSiteIds())) {
			query.append(" AND FTE.SITE_ID IN (:SITE_ID) ");
			namedParameters.addValue("SITE_ID",tansactionServiceInfo.getSiteIds(), Types.BIGINT);
		}
		
		if (Util.isNotEmptyObject(tansactionServiceInfo.getFlatSaleOwnerId())) {
			query.append("  AND FD.FLATS_SALE_OWNERS_ID=:FLATS_SALE_OWNERS_ID ");
			namedParameters.addValue("FLATS_SALE_OWNERS_ID",tansactionServiceInfo.getFlatSaleOwnerId(), Types.BIGINT);
		}

		if (Util.isNotEmptyObject(tansactionServiceInfo.getTransactionTypeId())) {
			query.append(" AND FTE.FIN_TRANSACTION_TYPE_ID = :FIN_TRANSACTION_TYPE_ID ");
			namedParameters.addValue("FIN_TRANSACTION_TYPE_ID",tansactionServiceInfo.getTransactionTypeId(), Types.BIGINT);
		}
		if (Util.isNotEmptyObject(tansactionServiceInfo.getTransactionModeId())) {
			query.append(" AND FTE.FIN_TRANSACTION_MODE_ID  = :FIN_TRANSACTION_MODE_ID ");
			namedParameters.addValue("FIN_TRANSACTION_MODE_ID",tansactionServiceInfo.getTransactionModeId(), Types.BIGINT);
		}
		
		if(Util.isNotEmptyObject(tansactionServiceInfo.getSiteAccountId()) && Util.isNotEmptyObject(tansactionServiceInfo.getSiteBankAccountNumber())) {
			query.append(" AND FTE.FIN_SITE_PROJ_ACC_MAP_ID = :FIN_SITE_PROJ_ACC_MAP_ID ");
			namedParameters.addValue("FIN_SITE_PROJ_ACC_MAP_ID",tansactionServiceInfo.getSiteAccountId(), Types.BIGINT);
		}
		  
		if (isConditionNotEmpty) {
			if(isRequestedInterestWaiverData) {//is this is request from interest waiver
				
				query.append(" AND FTE.FIN_TRANSACTION_TYPE_ID = :INTEREST_WAIVER_TYPE_ID ");
				if(Util.isNotEmptyObject(tansactionServiceInfo.getFromDate())&&Util.isNotEmptyObject(tansactionServiceInfo.getToDate())) {
					query.append(" AND TRUNC(FTE.PAYMENT_DATE) BETWEEN TRUNC(:FROM_DATE) AND TRUNC(:TO_DATE)");
				} else if (Util.isNotEmptyObject(tansactionServiceInfo.getFromDate())) {
					query.append(" AND TRUNC(FTE.PAYMENT_DATE) >= TRUNC(:FROM_DATE) ");
				} else if (Util.isNotEmptyObject(tansactionServiceInfo.getToDate())) {
					query.append(" AND TRUNC(FTE.PAYMENT_DATE) <= TRUNC(:TO_DATE)");
				}
				
				if (Util.isNotEmptyObject(tansactionServiceInfo.getBookingFormIds())) {
					query.append(" AND FTE.BOOKING_FORM_ID IN (:BOOKING_FORM_ID) ");
				}
				
				namedParameters.addValue("FROM_DATE", tansactionServiceInfo.getFromDate(),Types.DATE);
				namedParameters.addValue("TO_DATE", tansactionServiceInfo.getToDate(),Types.DATE);
				namedParameters.addValue("BOOKING_FORM_ID", tansactionServiceInfo.getBookingFormIds() ,Types.BIGINT);
				namedParameters.addValue("INTEREST_WAIVER_TYPE_ID",FinTransactionType.INTEREST_WAIVER.getId(), Types.BIGINT);
			}
			
			if (tansactionServiceInfo.getCondition().equalsIgnoreCase("transactionStatus")) {
				searchCondiftionOnCreatedDate = false;
				//namedParameters.addValue("TRANSACTION_STATUS_ID", Arrays.asList(Status.NEW.getStatus(),Status.MODIFY.getStatus(),Status.PENDING.getStatus(),Status.APPROVED.getStatus(),Status.REJECTED.getStatus(),Status.UNCLEARED_CHEQUE.getStatus(),Status.CHEQUE_BOUNCED.getStatus()));
				namedParameters.addValue("TRANSACTION_STATUS_ID", Arrays.asList(Status.NEW.getStatus(),Status.MODIFY.getStatus(),Status.PENDING.getStatus(),Status.APPROVED.getStatus(),Status.UNCLEARED_CHEQUE.getStatus()));
			
				if(Util.isNotEmptyObject(tansactionServiceInfo.getTrnCreatedFromDate()) && Util.isNotEmptyObject(tansactionServiceInfo.getTrnCreatedTotoDate())) {
					query.append(" AND TRUNC(FTE.CREATED_DATE) BETWEEN TRUNC(:getTrnCreatedFromDate) AND TRUNC(:getTrnCreatedTotoDate)");
					searchCondiftionOnCreatedDate = true;
				} else if (Util.isNotEmptyObject(tansactionServiceInfo.getTrnCreatedFromDate())) {
					query.append(" AND TRUNC(FTE.CREATED_DATE) >= TRUNC(:getTrnCreatedFromDate) ");
					searchCondiftionOnCreatedDate = true;
				} else if (Util.isNotEmptyObject(tansactionServiceInfo.getTrnCreatedTotoDate())) {
					query.append(" AND TRUNC(FTE.CREATED_DATE) <= TRUNC(:getTrnCreatedTotoDate)");
					searchCondiftionOnCreatedDate = true;
				}

				namedParameters.addValue("getTrnCreatedFromDate", tansactionServiceInfo.getTrnCreatedFromDate(),Types.DATE);
				namedParameters.addValue("getTrnCreatedTotoDate", tansactionServiceInfo.getTrnCreatedTotoDate(),Types.DATE);
				
				if(!searchCondiftionOnCreatedDate) {//if search in on created date, then no need using received date wise
					if(Util.isNotEmptyObject(tansactionServiceInfo.getReceivedFromDate()) && Util.isNotEmptyObject(tansactionServiceInfo.getReceivedToDate())) {
						query.append(" AND  TRUNC(NVL(FTE.RECEIVED_DATE,NVL(FTE.PAYMENT_DATE,FTE.CREATED_DATE))) BETWEEN TRUNC(:getReceivedFromDate) AND TRUNC(:getReceivedToDate)");
					} else if (Util.isNotEmptyObject(tansactionServiceInfo.getReceivedFromDate())) {
						query.append(" AND  TRUNC(NVL(FTE.RECEIVED_DATE,NVL(FTE.PAYMENT_DATE,FTE.CREATED_DATE))) >= TRUNC(:getReceivedFromDate) ");
					} else if (Util.isNotEmptyObject(tansactionServiceInfo.getReceivedToDate())) {
						query.append(" AND  TRUNC(NVL(FTE.RECEIVED_DATE,NVL(FTE.PAYMENT_DATE,FTE.CREATED_DATE))) <= TRUNC(:getReceivedToDate)");
					}
					
					namedParameters.addValue("getReceivedFromDate", tansactionServiceInfo.getReceivedFromDate(),Types.DATE);
					namedParameters.addValue("getReceivedToDate", tansactionServiceInfo.getReceivedToDate(),Types.DATE);
				}
				
				if (Util.isNotEmptyObject(tansactionServiceInfo.getFlatIds())) {
					query.append(" AND FB.FLAT_ID IN (:FLAT_ID) ");
				}
				if (Util.isNotEmptyObject(tansactionServiceInfo.getBookingFormIds())) {
					query.append(" AND FTE.BOOKING_FORM_ID IN (:BOOKING_FORM_ID) ");
				}
				
				if(Util.isNotEmptyObject(tansactionServiceInfo.getPendingTrnByEmpId())) {
					query.append(" AND FTESOLM.EMP_ID = :TRN_PENDING_EMP_ID");
					query.append(" AND FTESOLM.STATUS_ID = :STATUS_ID");
					namedParameters.addValue("TRN_PENDING_EMP_ID", tansactionServiceInfo.getPendingTrnByEmpId(), Types.BIGINT);
				}
				
				if(Util.isNotEmptyObject(tansactionServiceInfo.getSearchBySetOffType())) {
					query.append(" AND FTSO.SET_OFF_TYPE = :SER_SET_OFF_TYPE ");
					namedParameters.addValue("SER_SET_OFF_TYPE", tansactionServiceInfo.getSearchBySetOffType(), Types.BIGINT);
				}
				
			}
		}
		if(Util.isNotEmptyObject(tansactionServiceInfo.getTransactionEntryId())) {
			query.append(" AND FTE.FIN_TRANSACTION_ENTRY_ID IN  (:FIN_TRANSACTION_ENTRY_ID) ");
			namedParameters.addValue("FIN_TRANSACTION_ENTRY_ID", tansactionServiceInfo.getTransactionEntryId(), Types.BIGINT);
		}
		//By BVR
		if("PendingTransaction".equals(tansactionServiceInfo.getActionUrl())&&"reciept".equals(tansactionServiceInfo.getOperationType())) {
			query.append(" AND FTSO.set_off_type IN ( 29 ) ");
			query.append(" and FTE.FIN_TRANSACTION_TYPE_ID in (1) ");
			query.append(" and FTE.FIN_TRANSACTION_MODE_ID in (1,2) ");
		}else if("PendingTransaction".equals(tansactionServiceInfo.getActionUrl()) &&"payment".equals(tansactionServiceInfo.getOperationType())) {
			query.append(" AND FTSO.set_off_type IN ( 29 ) ");
			query.append(" and FTE.FIN_TRANSACTION_TYPE_ID in (2) ");
			query.append(" and FTE.FIN_TRANSACTION_MODE_ID in (1,2) ");
		}
		query.append(" ORDER BY  NVL(FTE.RECEIVED_DATE,FTE.PAYMENT_DATE),FTE.FIN_TRANSACTION_ENTRY_ID ");
		
		List<FinTransactionEntryPojo> finTransactionEntryPojoLists = getData(query.toString(), namedParameters, FinTransactionEntryPojo.class);
		
		if (isConditionNotEmpty) {
			for (FinTransactionEntryPojo finTransactionEntryPojo : finTransactionEntryPojoLists) {
				String payemntSetOff = EmployeeFinancialServiceImpl.getConvenienceNameFromMetaDataSalesForce(finTransactionEntryPojo.getTrnSetOffName(),finTransactionEntryPojo.getTransactionTypeId());
				finTransactionEntryPojo.setTransactionPaymentSetOff(payemntSetOff);
				if("Self".equals(finTransactionEntryPojo.getSourceOfFunds())) {
					finTransactionEntryPojo.setSourceOfFunds("Own Funds");
				}
			}
			if ("".equals("A")) {//if want paymnt set off then enable this condition
				String query1 = new StringBuffer(SqlQuery.QRY_TO_GET_FIN_TRANSACTION_ENTRY_PAYMENT_SET_OFF)
						.append(" AND FTSOE.FIN_TRANSACTION_ENTRY_ID = :FIN_TRANSACTION_ENTRY_ID")
						.toString();
				int index = 0;
				for (FinTransactionEntryPojo finTransactionEntryPojo : finTransactionEntryPojoLists) {
					if(finTransactionEntryPojo.getTransactionTypeId().equals(FinTransactionType.INTEREST_WAIVER.getId())) {
						finTransactionEntryPojo.setTransactionPaymentSetOff(FinTransactionType.INTEREST_WAIVER.getName());
						continue;
					}
					index = 0;
					namedParameters = new MapSqlParameterSource();
					StringBuffer data = new StringBuffer("");
					namedParameters.addValue("FIN_TRANSACTION_ENTRY_ID",finTransactionEntryPojo.getTransactionEntryId(), Types.BIGINT);
					namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
					List<FinTransactionSetOffPojo> transactionEntryPojoPaymentSetOffLists = getData(query1, namedParameters, FinTransactionSetOffPojo.class);
					for (index = 0; index < transactionEntryPojoPaymentSetOffLists.size(); index++) {
						FinTransactionSetOffPojo setOffPojo = transactionEntryPojoPaymentSetOffLists.get(index);
						
						//long setOffType = setOffPojo.getSetOffType();
						String setOffTypeName = setOffPojo.getSetOffTypeName();
						String payemntSetOff = EmployeeFinancialServiceImpl.getConvenienceNameFromMetaData(setOffTypeName);
						
						if(index==transactionEntryPojoPaymentSetOffLists.size()-1) {
							data.append(payemntSetOff);	
						}else {
							data.append(payemntSetOff).append(",");
						}
					}
					
					
					finTransactionEntryPojo.setTransactionPaymentSetOff(data.toString());
					
				}
			}
		}
		
		return finTransactionEntryPojoLists;
	}
	
	@Override
	public List<FinTransactionEntryPojo> getMisCompletedTransactions(EmployeeFinancialTransactionServiceInfo tansactionServiceInfo) throws Exception {
		//log.info("*** The Control is inside the getMisPendingTransactions in EmployeeFinancialServiceDaoImpl ***");
		String condition = tansactionServiceInfo.getCondition()==null?"":tansactionServiceInfo.getCondition();
		//String actionUrl = tansactionServiceInfo.getActionUrl()==null?"":tansactionServiceInfo.getActionUrl();
		boolean isConditionNotEmpty = Util.isNotEmptyObject(condition);
		boolean isThisApproveTransactionReq = (condition.equalsIgnoreCase("approveTransaction"));
		boolean isThisCompletedTransaction = (condition.equalsIgnoreCase("loadCompletedTransaction"));
		boolean loadCompletedTrnActiveBookingData = (condition.equalsIgnoreCase("loadCompletedTrnActiveBookingData"));
		boolean clearedCompletedTransaction = (condition.equalsIgnoreCase("clearedCompletedTransaction"));
		boolean isViewCustomerData = (condition.equalsIgnoreCase("ViewCustomerData"));
		//boolean getCustomerDataForBooking = (condition.equalsIgnoreCase("GetCustomerDataForBooking"));
		boolean isRequestedModifyTrnData = (condition.equalsIgnoreCase("modifyTransaction"));
		//boolean isRequestedInterestWaiverData = (actionUrl.equalsIgnoreCase("Interest Waiver"));
		StringBuilder query = new StringBuilder(" SELECT ")
				.append(SqlQuery.QRY_TO_GET_FIN_TRANSACTION_ENTRY);
				//if commenting this code open TRANSACTION_CLOSED_DATE in query QRY_TO_GET_FIN_TRANSACTION_ENTRY
				if(loadCompletedTrnActiveBookingData) {
					query.append(", ( SELECT B.CREATED_DATE ")
					.append(" FROM FIN_TRANSACTION_APPR_STAT B,  FIN_TRANSACTION_SET_OFF_ENTRY A, ")
					.append("  FIN_TRANSACTION_ENTRY C ")
					.append(" WHERE A.FIN_TRN_SET_OFF_ENT_ID = B.FIN_TRN_SET_OFF_ENT_ID ")
					.append(" AND C.FIN_TRANSACTION_ENTRY_ID = A.FIN_TRANSACTION_ENTRY_ID AND FTE.FIN_TRANSACTION_ENTRY_ID = A.FIN_TRANSACTION_ENTRY_ID ")
					.append(" and c.status_id = 6 and c.TRANSACTION_STATUS_ID in (37,51) ")
					.append(" and B.ACTION_TYPE in (37,51) ")
					.append(" and c.FIN_TRANSACTION_TYPE_ID in (1,2) ")
					.append(" and B.emp_id in (22,122,80,104,88) ")
					.append(" and trunc(b.CREATED_DATE)  between trunc(SYSDATE-1)  and trunc(SYSDATE-1) ")
					.append("  ) AS TRANSACTION_CLOSED_DATE ");
				} else {
					query.append(",FTE.TRANSACTION_CLOSED_DATE ");
				}
				query.append(" ,(CUST.FIRST_NAME || ' ' || CUST.LAST_NAME) AS CUSTOMER_NAME ")
				.append(" ,FTM.NAME AS TRANSACTION_MODE_NAME,FTSO.SET_OFF_AMOUNT AS TRN_SET_OFF_AMOUNT")
				.append(" ,FTT.TRANSACTION_TYPE_ID ")
				.append(" ,FTT.NAME AS TRANSACTION_TYPE_NAME ")
				.append(" ,INITCAP(S.STATUS) AS TRANSACTION_STATUS_NAME ")
				.append(" ,FTL.LEVEL_NAME AS PENDING_BY_LEVEL1")
				.append(" ,(SELECT MT.NAME FROM METADATA_TYPES MT WHERE MT.METADATA_TYPES_ID = FTSO.SET_OFF_TYPE  ) AS TRN_SET_OFF_NAME")
				.append(" ,SI.SALES_FORCE_SITE_NAME AS SITE_NAME")
				.append(" ,FLT.FLAT_NO ,FLT.FLAT_ID,FB.STATUS_ID AS FLAT_BOOKING_STATUS_ID")
				.append(",FB.SALESFORCE_BOOKING_ID AS SALESFORCE_BOOKING_ID,FIB.BANK_NAME,FB.SALESFORCE_TRANSACTION_ID,INITCAP(STS.STATUS) AS FB_STATUS_NAME ")
				.append(",NVL(FTC.CHEQUE_NO,FTO.REFERENCE_NO) AS CHEQUE_OR_REFERENCE_NO")
				//.append(",NVL(FTC.CHEQUE_DATE,FTO.TRANSACTION_DATE) AS CHEQUE_OR_ONLINE_DATE")
				.append(",FTE.RECEIVED_DATE AS CHEQUE_OR_ONLINE_DATE")
				.append(",FTC.CHEQUE_DEPOSITED_DATE,FTC.CHEQUE_BOUNCE_COMMENT,FTC.CLEARANCE_DATE,FTC.CHEQUE_HANDOVER_DATE,FTC.CHEQUE_BOUNCE_DATE")
				.append(",FTE.FIN_SITE_PROJ_ACC_MAP_ID AS FIN_PROJ_ACC_ID ,FPA.ACCOUNT_NO ")
				.append(",(SELECT LISTAGG(NVL((EMP.FIRST_NAME || ' ' || EMP.LAST_NAME),'N/A'), ',')   WITHIN GROUP (ORDER BY  FTESOLM1.FIN_TRAN_SET_OFF_LEVEL_ID)  AS EMP_NAME FROM FIN_TRN_EMP_SET_OFF_LEVEL_MAPPING FTESOLM1 JOIN EMPLOYEE EMP ")  
				.append(" ON(EMP.EMP_ID=FTESOLM1.EMP_ID)")
				.append(" WHERE FTESOLM1.FIN_TRAN_SET_OFF_LEVEL_ID = FTESOLM.FIN_TRAN_SET_OFF_LEVEL_ID AND FTESOLM1.STATUS_ID = :STATUS_ID)  AS PENDING_BY_LEVEL")
				.append(",(SELECT CONCAT(EMP.FIRST_NAME,' ' || EMP.LAST_NAME) FROM FIN_TRANSACTION_APPR_STAT FTAS JOIN EMPLOYEE EMP  ON(EMP.EMP_ID=FTAS.EMP_ID) WHERE FIN_TRN_APPR_STAT_ID = (SELECT MAX(FIN_TRN_APPR_STAT_ID) FROM FIN_TRANSACTION_APPR_STAT WHERE  FIN_TRN_SET_OFF_ENT_ID = FTSOE. FIN_TRN_SET_OFF_ENT_ID) ) AS LAST_APPR_BY ");
				if(isThisApproveTransactionReq || isViewCustomerData || isRequestedModifyTrnData) {
					query.append(", (SELECT  LISTAGG(CONCAT(NVL(FTED1.LOCATION,0),'##'||FTED.DOC_NAME), '@@')   WITHIN GROUP (ORDER BY FTED1.FIN_TRANSACTION_ENTRY_DOC_ID) FROM FIN_TRANSACTION_ENTRY_DOC FTED1 ");
					query.append("WHERE FTED1.FIN_TRANSACTION_ENTRY_ID=FTE.FIN_TRANSACTION_ENTRY_ID AND FTED1.STATUS_ID = 6) AS MULTIPLE_LOCATION");							 
				}
				if(isThisCompletedTransaction || isViewCustomerData || loadCompletedTrnActiveBookingData || clearedCompletedTransaction) {
					query.append(", (SELECT  LISTAGG(CONCAT(NVL(FTED1.LOCATION,0),'##'||FTED1.DOC_NAME), '@@') WITHIN GROUP (ORDER BY FTED1.FIN_TRANSACTION_ENTRY_DOC_ID) FROM FIN_TRANSACTION_ENTRY_DOC FTED1  ");
					query.append("WHERE FTED1.FIN_TRANSACTION_ENTRY_ID=FTE.FIN_TRANSACTION_ENTRY_ID  AND FTED1.RECEIPT_TYPE = FTSO.SET_OFF_TYPE  AND FTED1.DOC_TYPE = :DOC_TYPE AND FTED1.STATUS_ID = 6) AS TRANSACTION_RECEIPT");
				}
				if(isThisCompletedTransaction ||clearedCompletedTransaction ) {
					query.append(" ,(SELECT FTAS.CREATED_DATE FROM   fin_transaction_appr_stat FTAS  WHERE  fin_trn_appr_stat_id = (SELECT Max(fin_trn_appr_stat_id) FROM   fin_transaction_appr_stat WHERE fin_trn_set_off_ent_id =  FTSOE.fin_trn_set_off_ent_id)) AS LAST_APPROVED_DATE   ");
				}
				//.append(" ,ED.EMP_DESIGNATION AS PENDING_BY_DEPT")
				//.append(" ,DEPT.NAME AS TRANSACTION_PENDING_DEPT_NAME")
				//.append(" ,DEPT.DEPT_ID AS TRANSACTION_PENDING_DEPT_ID")
				query.append(" FROM FIN_TRANSACTION_ENTRY FTE ")

				.append(" LEFT OUTER JOIN FIN_SITE_PROJ_ACC_MAPPING FSPAM ")
				.append(" ON(FSPAM.FIN_SITE_PROJ_ACC_MAP_ID=FTE.FIN_SITE_PROJ_ACC_MAP_ID) ")
				.append(" LEFT OUTER JOIN  FIN_PROJECT_ACCOUNTS FPA ")
				.append(" ON(FPA.FIN_PROJ_ACC_ID=FSPAM.FIN_PROJ_ACC_ID) ")
				.append(" JOIN SITE SI ON SI.SITE_ID = FTE.SITE_ID ");
				query.append(" LEFT OUTER JOIN FIN_TRANSACTION_CHEQUE FTC ON (FTC.FIN_TRANSACTION_ENTRY_ID = FTE.FIN_TRANSACTION_ENTRY_ID )")
		        .append(" LEFT OUTER JOIN FIN_TRANSACTION_ONLINE FTO  ON (FTO.FIN_TRANSACTION_ENTRY_ID = FTE.FIN_TRANSACTION_ENTRY_ID)")
		        .append(" LEFT OUTER JOIN FIN_BANK FIB ON FTE.FIN_BANK_ID = FIB.FIN_BANK_ID")
				.append(SqlQuery.QRY_TO_GET_LEVEL_NAME_BY_EMPID)
				.append(" JOIN FIN_TRANSACTION_SET_OFF FTSO ON FTSO.FIN_TRN_SET_OFF_ENT_ID = FTSOE. FIN_TRN_SET_OFF_ENT_ID")
				.append(" LEFT OUTER JOIN FIN_TRANSACTION_ENTRY_DOC FTED ON FTED.FIN_TRANSACTION_ENTRY_ID=FTE.FIN_TRANSACTION_ENTRY_ID AND FTED.RECEIPT_TYPE = FTSO.SET_OFF_TYPE AND FTED.DOC_TYPE = :DOC_TYPE AND FTED.STATUS_ID = 6 ")
				.append(" JOIN FLAT_BOOKING FB ON(FB.FLAT_BOOK_ID = FTE.BOOKING_FORM_ID ) ")//AND FB.STATUS_ID = :STATUS_ID
				.append(" INNER JOIN FLAT_DETAILS FDT ON (FDT.FLAT_ID=FB.FLAT_ID) ")
				.append(" JOIN FLAT FLT  ON (FB.FLAT_ID = FLT.FLAT_ID AND FLT.STATUS_ID = :STATUS_ID )")
				.append(" JOIN CUSTOMER CUST ON (CUST.CUST_ID = FB.CUST_ID) ")
				.append(" JOIN FIN_TRANSACTION_MODE FTM ON(FTM.TRANSACTION_MODE_ID=FTE.FIN_TRANSACTION_MODE_ID) ")
				.append(" JOIN FIN_TRANSACTION_TYPE FTT ON(FTT.TRANSACTION_TYPE_ID=FTE.FIN_TRANSACTION_TYPE_ID) ")
				.append(" FULL JOIN STATUS S ON (S.STATUS_ID = FTE.TRANSACTION_STATUS_ID) ")
				.append(" FULL JOIN STATUS STS ON (STS.STATUS_ID = FB.STATUS_ID)  ")
				.append(" WHERE FTE.STATUS_ID=:STATUS_ID ")
				.append(" AND FTE.TRANSACTION_STATUS_ID IN (:TRANSACTION_STATUS_ID)");
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		//namedParameters.addValue("SITE_ID",tansactionServiceInfo.getSiteId(), Types.BIGINT);
		
		if (Util.isNotEmptyObject(tansactionServiceInfo.getActionUrl())) {
			if (tansactionServiceInfo.getActionUrl().equalsIgnoreCase("getUnclearedChequeDetails")) {
				//query.append(" AND (FTC.CLEARANCE_DATE IS NULL AND  FTC.CHEQUE_DEPOSITED_DATE IS NOT NULL)");
			}
		}
		
		if (Util.isNotEmptyObject(tansactionServiceInfo.getSiteIds())) {
			query.append(" AND FTE.SITE_ID IN (:SITE_ID) ");
			namedParameters.addValue("SITE_ID",tansactionServiceInfo.getSiteIds(), Types.BIGINT);
		}

		if (Util.isNotEmptyObject(tansactionServiceInfo.getTransactionTypeId())) {
			query.append(" AND FTE.FIN_TRANSACTION_TYPE_ID = :FIN_TRANSACTION_TYPE_ID ");
			namedParameters.addValue("FIN_TRANSACTION_TYPE_ID",tansactionServiceInfo.getTransactionTypeId(), Types.BIGINT);
		}
		if (Util.isNotEmptyObject(tansactionServiceInfo.getTransactionModeId())) {
			query.append(" AND FTE.FIN_TRANSACTION_MODE_ID  = :FIN_TRANSACTION_MODE_ID ");
			namedParameters.addValue("FIN_TRANSACTION_MODE_ID",tansactionServiceInfo.getTransactionModeId(), Types.BIGINT);
		}
		
		if(Util.isNotEmptyObject(tansactionServiceInfo.getSearchBySetOffType())) {
			query.append(" AND FTSO.SET_OFF_TYPE = :SER_SET_OFF_TYPE ");
			namedParameters.addValue("SER_SET_OFF_TYPE", tansactionServiceInfo.getSearchBySetOffType(), Types.BIGINT);
		} else if (clearedCompletedTransaction) {
			query.append(" AND FTSO.SET_OFF_TYPE = :SER_SET_OFF_TYPE ");
			namedParameters.addValue("SER_SET_OFF_TYPE", MetadataId.FIN_BOOKING_FORM_MILESTONES.getId(), Types.BIGINT);
		}
		
		if (isConditionNotEmpty) {
			
			if (tansactionServiceInfo.getCondition().equalsIgnoreCase("transactionStatus")) {
				namedParameters.addValue("TRANSACTION_STATUS_ID", Arrays.asList(Status.NEW.getStatus(),Status.MODIFY.getStatus(),Status.PENDING.getStatus(),Status.APPROVED.getStatus(),Status.REJECTED.getStatus(),Status.UNCLEARED_CHEQUE.getStatus(),Status.CHEQUE_BOUNCED.getStatus()));
			}/* else if (isThisApproveTransactionReq) {
				query.append(" AND FTESOLM.EMP_ID = :EMP_ID");
				query.append(" AND FTESOLM.STATUS_ID = :STATUS_ID");
				namedParameters.addValue("EMP_ID", tansactionServiceInfo.getEmployeeId(), Types.BIGINT);
				if(Util.isNotEmptyObject(tansactionServiceInfo.getActionUrl()) && tansactionServiceInfo.getActionUrl().equalsIgnoreCase("getUnclearedChequeDetails")) {
					namedParameters.addValue("TRANSACTION_STATUS_ID", Arrays.asList(Status.UNCLEARED_CHEQUE.getStatus()));
				} else {
					namedParameters.addValue("TRANSACTION_STATUS_ID", Arrays.asList(Status.NEW.getStatus(),Status.APPROVED.getStatus(),Status.PENDING.getStatus()));
				}
			} else if(isRequestedModifyTrnData) {
				query.append(" AND FTESOLM.EMP_ID = :EMP_ID");
				query.append(" AND FTESOLM.STATUS_ID = :STATUS_ID");
				namedParameters.addValue("EMP_ID", tansactionServiceInfo.getEmployeeId(), Types.BIGINT);
				namedParameters.addValue("TRANSACTION_STATUS_ID", Arrays.asList(Status.MODIFY.getStatus()));
			}*/ else if (isThisCompletedTransaction || loadCompletedTrnActiveBookingData || clearedCompletedTransaction) {
				int count = 0;
				if(Util.isNotEmptyObject(tansactionServiceInfo.getFromDate())&&Util.isNotEmptyObject(tansactionServiceInfo.getToDate())) {
					query.append(" AND TRUNC(FTE.TRANSACTION_CLOSED_DATE) BETWEEN TRUNC(:FROM_DATE) AND TRUNC(:TO_DATE)");
					count++;
				} else if (Util.isNotEmptyObject(tansactionServiceInfo.getFromDate())) {
					query.append(" AND TRUNC(FTE.TRANSACTION_CLOSED_DATE) >= TRUNC(:FROM_DATE) ");
					count++;
				} else if (Util.isNotEmptyObject(tansactionServiceInfo.getToDate())) {
					query.append(" AND TRUNC(FTE.TRANSACTION_CLOSED_DATE) <= TRUNC(:TO_DATE)");
					count++;
				}
				
				if(count==0) {
					//if in above condition from date or todate is entered then no need to execute this condition, bcoz data will load based on from date and toDate
					if(Util.isNotEmptyObject(tansactionServiceInfo.getClearanceFromDate())&&Util.isNotEmptyObject(tansactionServiceInfo.getClearanceToDate())) {
						//Note : if FTC.CLEARANCE_DATE is not empty means it is cheque transaction, or online transaction
						query.append(" AND TRUNC(NVL(FTC.CLEARANCE_DATE,NVL(FTE.RECEIVED_DATE,FTE.PAYMENT_DATE))) BETWEEN TRUNC(:getClearanceFromDate) AND TRUNC(:getClearanceToDate)");
					} else if (Util.isNotEmptyObject(tansactionServiceInfo.getClearanceFromDate())) {
						query.append(" AND TRUNC(NVL(FTC.CLEARANCE_DATE,NVL(FTE.RECEIVED_DATE,FTE.PAYMENT_DATE)))  >= TRUNC(:getClearanceFromDate) ");
					} else if (Util.isNotEmptyObject(tansactionServiceInfo.getClearanceToDate())) {
						query.append(" AND TRUNC(NVL(FTC.CLEARANCE_DATE,NVL(FTE.RECEIVED_DATE,FTE.PAYMENT_DATE)))  <= TRUNC(:getClearanceToDate)");
					}
					
					namedParameters.addValue("getClearanceFromDate", tansactionServiceInfo.getClearanceFromDate(),Types.DATE);
					namedParameters.addValue("getClearanceToDate", tansactionServiceInfo.getClearanceToDate(),Types.DATE);
				}
				
				if(Util.isNotEmptyObject(tansactionServiceInfo.getFlatIds())) {
					query.append(" AND FB.FLAT_ID IN (:FLAT_ID) ");
				} if (Util.isNotEmptyObject(tansactionServiceInfo.getBookingFormIds())) {
					query.append(" AND FTE.BOOKING_FORM_ID IN (:BOOKING_FORM_ID) ");
				}
				
				if(Util.isNotEmptyObject(tansactionServiceInfo.getFlatSaleOwnerId())) {
					query.append(" AND FDT.FLATS_SALE_OWNERS_ID = :FLATS_SALE_OWNERS_ID ");
					namedParameters.addValue("FLATS_SALE_OWNERS_ID", tansactionServiceInfo.getFlatSaleOwnerId(), Types.BIGINT);
				}
				if(clearedCompletedTransaction) {
					query.append("AND FB.STATUS_ID = :FLAT_BOOK_STATUS_ID");	
				}
				if(loadCompletedTrnActiveBookingData) {
					query.append("AND FB.STATUS_ID = :FLAT_BOOK_STATUS_ID");
				
					query.append(" AND FTE.FIN_TRANSACTION_ENTRY_ID IN ")
					.append(" ( SELECT  A.FIN_TRANSACTION_ENTRY_ID ")
					.append(" FROM FIN_TRANSACTION_APPR_STAT B,  FIN_TRANSACTION_SET_OFF_ENTRY A, ")
					.append("  FIN_TRANSACTION_ENTRY C ")
					.append(" WHERE A.FIN_TRN_SET_OFF_ENT_ID = B.FIN_TRN_SET_OFF_ENT_ID ")
					.append(" AND C.FIN_TRANSACTION_ENTRY_ID = A.FIN_TRANSACTION_ENTRY_ID ")
					.append(" and c.status_id = 6 and c.TRANSACTION_STATUS_ID in (37,51) ")
					.append(" and B.ACTION_TYPE in (37,51) ")
					.append(" and c.FIN_TRANSACTION_TYPE_ID in (1,2) ")
					.append(" and B.emp_id in (22,122,80,104,88) ")
					.append(" and trunc(b.CREATED_DATE)  between trunc(SYSDATE-1)  and trunc(SYSDATE-1) ")
					.append("  ) ");//ORDER BY A.FIN_TRANSACTION_ENTRY_ID
					
					//query.append(" and FTE.FIN_TRANSACTION_ENTRY_ID in (41529,41572,41621,41622,41623,41624,41921,41564,41960,41961,42098,42099,41912,42000)");
					//query.append(" and FTE.FIN_TRANSACTION_ENTRY_ID in (41529,41572,41621,41622,41623,41624,41912,41921,42000,42145,42194,42195,41960,41961,42241,42242)");
				}

				if(Util.isNotEmptyObject(tansactionServiceInfo.getFbStatusId())) {
					query.append(" AND FB.STATUS_ID IN  (:FB_STATUS_ID) ");
					namedParameters.addValue("FB_STATUS_ID", tansactionServiceInfo.getFbStatusId(), Types.BIGINT);
				}
				if(Util.isNotEmptyObject(tansactionServiceInfo.getTransactionEntryId())) {
					query.append(" AND FTE.FIN_TRANSACTION_ENTRY_ID IN  (:FIN_TRANSACTION_ENTRY_ID) ");
					namedParameters.addValue("FIN_TRANSACTION_ENTRY_ID", tansactionServiceInfo.getTransactionEntryId(), Types.BIGINT);
				}
				namedParameters.addValue("FLAT_BOOK_STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
				namedParameters.addValue("FLAT_ID", tansactionServiceInfo.getFlatIds() ,Types.BIGINT);
				namedParameters.addValue("BOOKING_FORM_ID", tansactionServiceInfo.getBookingFormIds() ,Types.BIGINT);
				namedParameters.addValue("DOC_TYPE", MetadataId.GENERATED.getId(),Types.BIGINT);
				namedParameters.addValue("FROM_DATE", tansactionServiceInfo.getFromDate(),Types.DATE);
				namedParameters.addValue("TO_DATE", tansactionServiceInfo.getToDate(),Types.DATE);
				namedParameters.addValue("TRANSACTION_STATUS_ID", Arrays.asList(Status.TRANSACTION_COMPLETED.getStatus(),Status.CHEQUE_BOUNCED.getStatus()));
				if(count==0) {//if count is zero means data loading based on cleared transaction date, so cheque bounced transaction not required, as they are not cleared
					namedParameters.addValue("TRANSACTION_STATUS_ID", Arrays.asList(Status.TRANSACTION_COMPLETED.getStatus()));	
				}
				if(loadCompletedTrnActiveBookingData) {//if this request from scheduler
					namedParameters.addValue("TRANSACTION_STATUS_ID", Arrays.asList(Status.TRANSACTION_COMPLETED.getStatus(),Status.CHEQUE_BOUNCED.getStatus()));	
				}
			}/* else if (isViewCustomerData) {
				query.append(" AND FTE.BOOKING_FORM_ID = :BOOKING_FORM_ID ");
				namedParameters.addValue("DOC_TYPE", MetadataId.GENERATED.getId(),Types.BIGINT);
				namedParameters.addValue("TRANSACTION_STATUS_ID", new ArrayList<Long>(Arrays.asList(Status.UNCLEARED_CHEQUE.getStatus(),Status.TRANSACTION_COMPLETED.getStatus(),Status.CHEQUE_BOUNCED.getStatus(),Status.NEW.getStatus(),Status.MODIFY.getStatus(),Status.APPROVED.getStatus(),Status.PENDING.getStatus())));
				namedParameters.addValue("BOOKING_FORM_ID", tansactionServiceInfo.getBookingFormId() ,Types.BIGINT);
			}  else if (getCustomerDataForBooking) {
				query.append(" AND FTE.BOOKING_FORM_ID = :BOOKING_FORM_ID ");
				
				query.append(" AND FTE.FIN_TRANSACTION_TYPE_ID = :FIN_TRANSACTION_TYPE_ID ");
				namedParameters.addValue("FIN_TRANSACTION_TYPE_ID",FinTransactionType.RECEIPT.getId(), Types.BIGINT);
				
				namedParameters.addValue("DOC_TYPE", MetadataId.GENERATED.getId(),Types.BIGINT);
				namedParameters.addValue("TRANSACTION_STATUS_ID",  Arrays.asList(Status.TRANSACTION_COMPLETED.getStatus()));
				namedParameters.addValue("BOOKING_FORM_ID", tansactionServiceInfo.getBookingFormId() ,Types.BIGINT);
			}*/
		}
		if("clearedCompletedTransaction".equals(tansactionServiceInfo.getCondition())&&"reciept".equals(tansactionServiceInfo.getOperationType())) {
			query.append(" and FTE.FIN_TRANSACTION_TYPE_ID in (1) ");
			query.append(" and FTE.FIN_TRANSACTION_MODE_ID in (1,2) ");
		}else if("clearedCompletedTransaction".equals(tansactionServiceInfo.getCondition()) &&"payment".equals(tansactionServiceInfo.getOperationType())) {
			query.append(" and FTE.FIN_TRANSACTION_TYPE_ID in (2) ");
			query.append(" and FTE.FIN_TRANSACTION_MODE_ID in (1,2) ");
		}
		query.append(" ORDER BY  NVL(FTE.RECEIVED_DATE,FTE.PAYMENT_DATE),FTE.FIN_TRANSACTION_ENTRY_ID,FTE.SITE_ID ");
		
		System.out.println(query.toString());
		
		List<FinTransactionEntryPojo> finTransactionEntryPojoLists = getData(query.toString(), namedParameters, FinTransactionEntryPojo.class);
		
		if (isConditionNotEmpty) {
			if(isThisCompletedTransaction || loadCompletedTrnActiveBookingData || clearedCompletedTransaction) {
				for (FinTransactionEntryPojo finTransactionEntryPojo : finTransactionEntryPojoLists) {
					String payemntSetOff = EmployeeFinancialServiceImpl.getConvenienceNameFromMetaDataSalesForce(finTransactionEntryPojo.getTrnSetOffName(),finTransactionEntryPojo.getTransactionTypeId());
					finTransactionEntryPojo.setTransactionPaymentSetOff(payemntSetOff);
					if("Self".equals(finTransactionEntryPojo.getSourceOfFunds())) {
						finTransactionEntryPojo.setSourceOfFunds("Own Funds");
					}
				}
			} else if (isThisApproveTransactionReq || isViewCustomerData || isRequestedModifyTrnData) {
				String query1 = new StringBuffer(SqlQuery.QRY_TO_GET_FIN_TRANSACTION_ENTRY_PAYMENT_SET_OFF)
						.append(" AND FTSOE.FIN_TRANSACTION_ENTRY_ID = :FIN_TRANSACTION_ENTRY_ID")
						.toString();
				int index = 0;
				for (FinTransactionEntryPojo finTransactionEntryPojo : finTransactionEntryPojoLists) {
					if(finTransactionEntryPojo.getTransactionTypeId().equals(FinTransactionType.INTEREST_WAIVER.getId())) {
						finTransactionEntryPojo.setTransactionPaymentSetOff(FinTransactionType.INTEREST_WAIVER.getName());
						continue;
					}
					index = 0;
					namedParameters = new MapSqlParameterSource();
					StringBuffer data = new StringBuffer("");
					namedParameters.addValue("FIN_TRANSACTION_ENTRY_ID",finTransactionEntryPojo.getTransactionEntryId(), Types.BIGINT);
					namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
					List<FinTransactionSetOffPojo> transactionEntryPojoPaymentSetOffLists = getData(query1, namedParameters, FinTransactionSetOffPojo.class);
					for (index = 0; index < transactionEntryPojoPaymentSetOffLists.size(); index++) {
						FinTransactionSetOffPojo setOffPojo = transactionEntryPojoPaymentSetOffLists.get(index);
						
						//long setOffType = setOffPojo.getSetOffType();
						String setOffTypeName = setOffPojo.getSetOffTypeName();
						String payemntSetOff = EmployeeFinancialServiceImpl.getConvenienceNameFromMetaData(setOffTypeName);
						
						if(index==transactionEntryPojoPaymentSetOffLists.size()-1) {
							data.append(payemntSetOff);	
						}else {
							data.append(payemntSetOff).append(",");
						}
					}
					if(isViewCustomerData) {
						//if the count more the one show the default payment set off  
						if(index>1) {
							finTransactionEntryPojo.setTransactionPaymentSetOff(data.toString());
						}else {
							finTransactionEntryPojo.setTransactionPaymentSetOff(data.toString());
						}
					} else {
						finTransactionEntryPojo.setTransactionPaymentSetOff(data.toString());
					}
				}
			}
		}
		
		return finTransactionEntryPojoLists;
		}
	
	@Override
	public List<FinancialDemandNoteMS_TRN_Pojo> loadOldBookingTransaction(FlatBookingPojo oldFlatBookingPojo) {
		String sqlQuery = FinancialQuerys.QRY_TO_LOAD_OLD_BOOKING_TRANSACTIONS;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		
		namedParameters.addValue("FB_STATUS_ID", oldFlatBookingPojo.getStatusId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("FLAT_NO", oldFlatBookingPojo.getFlatNo(), Types.VARCHAR);
		namedParameters.addValue("FLAT_BOOK_ID", oldFlatBookingPojo.getFlatBookingId(), Types.BIGINT);
		//namedParameters.addValue("FIN_TRANSACTION_TYPE_ID", oldFlatBookingPojo.getFlatBookingId(), Types.BIGINT);
		namedParameters.addValue("FIN_TRANSACTION_TYPE_ID", Arrays.asList(FinTransactionType.RECEIPT.getId(),FinTransactionType.PAYMENT.getId()));
		List<FinancialDemandNoteMS_TRN_Pojo> finTransactionEntryPojoLists = getData(sqlQuery.toString(), namedParameters, FinancialDemandNoteMS_TRN_Pojo.class);
		System.out.println(finTransactionEntryPojoLists);
		return finTransactionEntryPojoLists;
	}
	
	@Override
	public int inActiveOldBookingTransaction(FlatBookingPojo oldFlatBookingPojo) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("BOOKING_FORM_ID", oldFlatBookingPojo.getFlatBookingId(),Types.BIGINT);
		//namedParameters.addValue("INACTIVE_STATUS_ID", Status.INACTIVE.getStatus(), Types.BIGINT);
		String str1 = "update FIN_BOOKING_FORM_ACCOUNTS set status_id = 7,modified_date = sysdate,modified_by = 1000014  where  BOOKING_FORM_ID IN (:BOOKING_FORM_ID)";
		int result = nmdPJdbcTemplate.update(str1, namedParameters);
		String str2 = "update FIN_BOOKING_FORM_DEMAND_NOTE set status_id = 7,modified_date = sysdate,modified_by = 1000014   where  BOOKING_FORM_ID IN (:BOOKING_FORM_ID)";
		 result = nmdPJdbcTemplate.update(str2, namedParameters);
		String str3 = "update FIN_BOOKING_FORM_MILESTONES set status_id = 7,modified_date = sysdate,modified_by = 1000014   where  BOOKING_FORM_ID IN (:BOOKING_FORM_ID)";
		 result = nmdPJdbcTemplate.update(str3, namedParameters);
		String str4 = "update FIN_BOOKING_FORM_ACCOUNT_SUMMARY set PAID_AMOUNT= 0, BALANCE_AMOUNT= PAYABLE_AMOUNT, REFUND_AMOUNT= 0,modified_date = sysdate,modified_by = 1000014   where  BOOKING_FORM_ID IN (:BOOKING_FORM_ID) and status_id =6 ";
		result = nmdPJdbcTemplate.update(str4, namedParameters);
		String str5 = "update FIN_BOOKING_FORM_ACCOUNTS set PAID_AMOUNT = 0, PAID_DATE = null, PAYMENT_STATUS= 9,BALANCE_AMOUNT= PAY_AMOUNT,modified_date = sysdate,modified_by = 1000014   where  BOOKING_FORM_ID IN (:BOOKING_FORM_ID)";
		result = nmdPJdbcTemplate.update(str5, namedParameters);
		String str6 = "update FIN_BOOKING_FORM_RECEIPTS set status_id = 7,modified_date = sysdate,modified_by = 1000014   where  BOOKING_FORM_ID IN (:BOOKING_FORM_ID)";
		result = nmdPJdbcTemplate.update(str6, namedParameters);
		String str7 = "update FIN_TRANSACTION_ENTRY set status_id = 7,modified_date = sysdate,modified_by = 1000014   where  BOOKING_FORM_ID IN (:BOOKING_FORM_ID) ";
		result = nmdPJdbcTemplate.update(str7, namedParameters);
		
		return result;
	}
	
	@Override
	public Long getTransactionFirstLevelEmpId(EmployeeFinancialTransactionServiceInfo transactionServiceInfo) {
		
		String sqlQuery = FinancialQuerys.QRY_TO_TRANSACTIONS_LEVEL_ONE_EMP;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		
		namedParameters.addValue("FIN_TRANSACTION_TYPE_ID", transactionServiceInfo.getTransactionTypeId(), Types.BIGINT);
		namedParameters.addValue("FIN_TRANSACTION_MODE_ID", transactionServiceInfo.getTransactionModeId(), Types.BIGINT);
		namedParameters.addValue("SITE_ID", transactionServiceInfo.getSiteId(), Types.BIGINT);
		namedParameters.addValue("FIN_LEVEL_ID",1l, Types.BIGINT);
		
		long empId = 0;
		try {
			empId = nmdPJdbcTemplate.queryForObject(sqlQuery, namedParameters, Long.class);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return empId;
	}
	
	@SuppressWarnings("unused")
	private String getConvenienceNameFromMetaDataNotInUse(String paymentSetOffDetails) {
		if(paymentSetOffDetails.equalsIgnoreCase("Interest")) {
			return "Interest Amount";	
		} else if(paymentSetOffDetails.equalsIgnoreCase("Fin_Penalty")) {
			return "Interest Amount";
		} else if(paymentSetOffDetails.equalsIgnoreCase("FIN_BOOKING_FORM_MILESTONES")) {
			return "Principal Amount";
		} else if(paymentSetOffDetails.equalsIgnoreCase("Refundable_Advance")){
			return "Refundable Advance";
		} else if(paymentSetOffDetails.equalsIgnoreCase("Corpus_Fund")){
			return "Corpus Fund";
		} else if(paymentSetOffDetails.equalsIgnoreCase("Maintenance_Charge")){
			return "Maintenance Charge";
		} else if(paymentSetOffDetails.equalsIgnoreCase("Individual_Flat_Khata_bifurcation_and_other_charges")){
			return "Individual Flat Khata bifurcation and other charges";
		} else if(paymentSetOffDetails.equalsIgnoreCase("Flat_Cancellation")) {
			return "Flat Cancellation";
		} else {
			return MetadataId.valueOf(paymentSetOffDetails.toUpperCase()).getName();
		}
	}
	
	@Override
	public List<FinTransactionSetOffPojo> getTransactionPaymenSetOffTypes(FinTransactionEntryPojo finTransactionEntryPojo) {
		String query1 = new StringBuffer(SqlQuery.QRY_TO_GET_FIN_TRANSACTION_ENTRY_PAYMENT_SET_OFF)
				.append(" AND FTSOE.FIN_TRANSACTION_ENTRY_ID = :FIN_TRANSACTION_ENTRY_ID")
				.toString();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FIN_TRANSACTION_ENTRY_ID", finTransactionEntryPojo.getTransactionEntryId(),Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		List<FinTransactionSetOffPojo> transactionEntryPojoPaymentSetOffLists = getData(query1, namedParameters,FinTransactionSetOffPojo.class);
		return transactionEntryPojoPaymentSetOffLists;
	}

	@Override
	public List<Map<String, Object>> getTransactionDataByReceiptNo(EmployeeFinancialTransactionServiceInfo transactionServiceInfo, List<Long> ids) {
		StringBuilder query = new StringBuilder("SELECT ")
				///.append(SqlQuery.QRY_TO_GET_FIN_TRANSACTION_ENTRY)
				.append(" FTE.FIN_TRANSACTION_ENTRY_ID,FTE.SOURCE_OF_FUNDS,FTSOE.FIN_TRN_SET_OFF_ENT_ID,FTE.TRANSACTION_CLOSED_DATE ")
				.append(" FROM FIN_TRANSACTION_ENTRY FTE ")
				.append(" INNER JOIN FIN_TRANSACTION_SET_OFF_ENTRY FTSOE ON (FTE.FIN_TRANSACTION_ENTRY_ID =FTSOE.FIN_TRANSACTION_ENTRY_ID)")
				.append(" WHERE FTE.STATUS_ID IN (:STATUS_ID) ")
				.append(" AND FTE.TRANSACTION_RECEIPT_NO = :TRANSACTION_RECEIPT_NO")
				//.append(" AND FTE.BOOKING_FORM_ID = :BOOKING_FORM_ID")
				.append(" ORDER BY FTE.FIN_TRANSACTION_ENTRY_ID");
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID", ids, Types.BIGINT);
		namedParameters.addValue("TRANSACTION_RECEIPT_NO", transactionServiceInfo.getSalesFrTransactionReceiptNo(), Types.VARCHAR);
		namedParameters.addValue("BOOKING_FORM_ID", transactionServiceInfo.getBookingFormId(), Types.BIGINT);
		
		return nmdPJdbcTemplate.queryForList(query.toString(), namedParameters);
	}

	@Override
	public List<FinTransactionEntryDetailsPojo> getFinTransactionEntryDetails(EmployeeFinancialTransactionServiceInfo employeeFinancialTransactionInfo, CustomerPropertyDetailsInfo customerPropertyDetailsInfo) {
		//log.info("*** The Control is inside the getFinTransactionEntryDetails in EmployeeFinancialServiceDaoImpl ***");
		StringBuilder query = new StringBuilder();
		if(employeeFinancialTransactionInfo.getTransactionModeName().equalsIgnoreCase(FinTransactionMode.CHEQUE.getName())) {
			query = new StringBuilder(SqlQuery.QRY_TO_GET_MIS_RECEIPT_CHEQUE);			
		} else if(employeeFinancialTransactionInfo.getTransactionModeName().equalsIgnoreCase(FinTransactionMode.ONLINE.getName())) {
			query = new StringBuilder(SqlQuery.QRY_TO_GET_MIS_RECEIPT_ONLINE);
		} else  if(employeeFinancialTransactionInfo.getTransactionModeName().equalsIgnoreCase(FinTransactionMode.INTEREST_WAIVER.getName())) {
			query = new StringBuilder(SqlQuery.QRY_TO_GET_MIS_RECEIPT_CHEQUE);
		}else  if(employeeFinancialTransactionInfo.getTransactionModeName().equalsIgnoreCase(FinTransactionMode.WAIVED_OFF.getName())) {
			query = new StringBuilder(SqlQuery.QRY_TO_GET_MIS_RECEIPT_CHEQUE);
		}
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("FIN_TRANSACTION_ENTRY_ID", employeeFinancialTransactionInfo.getTransactionEntryId(), Types.BIGINT);
		namedParameters.addValue("FLAT_BOOK_ID", customerPropertyDetailsInfo.getFlatBookingId(), Types.BIGINT);
		log.info("*** The Control is inside the getFinTransactionEntryDetails in EmployeeFinancialServiceDaoImpl *** \n"+query);
		List<FinTransactionEntryDetailsPojo> finTransactionEntryDetailsPojosList = getData(query.toString(), namedParameters, FinTransactionEntryDetailsPojo.class);
		return finTransactionEntryDetailsPojosList;
	}

	@Override
	public List<FinTransactionEntryDocPojo> getFinTransactionEntryDocuments(FinTransactionEntryDetailsInfo finTransactionEntryDetailsInfo, MetadataId docType) {
		log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.getFinTransactionEntryDocuments() ****");
		String query = SqlQuery.QRY_TO_GET_TRANSACTION_ENTRY_DOC;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FIN_TRANSACTION_ENTRY_ID", finTransactionEntryDetailsInfo.getTransactionEntryId(), Types.BIGINT);
		namedParameters.addValue("DOC_TYPE",docType.getId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID",Status.ACTIVE.getStatus(), Types.BIGINT);
		
		List<FinTransactionEntryDocPojo> transactionEntryDocPojos  = (List<FinTransactionEntryDocPojo>) getData(query,namedParameters,FinTransactionEntryDocPojo.class);
		return transactionEntryDocPojos;
	}
	
	@Override
	public int inActiveUploadedAttachmetns(FileInfo fileInfo) {
		
		String query = SqlQuery.QRY_TO_UPDATE_TRANSACTION_ENTRY_DOC;
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FIN_TRANSACTION_ENTRY_DOC_ID", fileInfo.getId(), Types.BIGINT);
		namedParameters.addValue("DOC_TYPE",MetadataId.UPLOADED.getId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID",Status.INACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("ACTIVE_STATUS_ID",Status.ACTIVE.getStatus(), Types.BIGINT);
		 
		return nmdPJdbcTemplate.update(query, namedParameters);
	}
	
	@Override
	public List<FinTransactionSetOffPojo> getFinTransactionSetOffData(FinTransactionEntryDetailsInfo finTransactionEntryDetailsInfo) {
		log.info("***** The Control is inside the getFinTransactionSetOffData in EmployeeFinancialServiceDaoImpl ***");
		//boolean isTransactioForEmpty = Util.isEmptyObject(finTransactionEntryDetailsInfo.getTransactionFor())?true:false;
		
/*		if(finTransactionEntryDetailsInfo.getTransactionModeName().equalsIgnoreCase(MetadataId.CHEQUE.getName())) {
		}else if(finTransactionEntryDetailsInfo.getTransactionModeName().equalsIgnoreCase(MetadataId.ONLINE.getName())) {
		}*/
		StringBuilder query = new StringBuilder(SqlQuery.QRY_TO_GET_FIN_TRANSACTION_SET_OFF_BY_ENTRY_ID)
				.append(" WHERE FTSOE.FIN_TRANSACTION_ENTRY_ID=:FIN_TRANSACTION_ENTRY_ID ");
				//.append(isTransactioForEmpty?" AND FTSOAM.STATUS_ID = :STATUS_ID ":"");
				//.append(" AND FBFA.TYPE IN (:TYPE) ");
		query.append(" ORDER BY FTSO.FIN_TRN_SET_OFF_ID");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FIN_TRANSACTION_ENTRY_ID", finTransactionEntryDetailsInfo.getTransactionEntryId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID",Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("TYPE", new ArrayList<Long>(Arrays.asList(MetadataId.FIN_BOOKING_FORM_MILESTONES.getId(),MetadataId.PRINCIPAL_AMOUNT.getId(), MetadataId.TDS.getId(),
				MetadataId.MODIFICATION_COST.getId(), MetadataId.LEGAL_COST.getId(), MetadataId.FIN_PENALTY.getId()
				,MetadataId.MAINTENANCE_CHARGE.getId(),MetadataId.INDIVIDUAL_FLAT_KHATA_BIFURCATION_AND_OTHER_CHARGES.getId(),MetadataId.CORPUS_FUND.getId()
				)));
		
		List<FinTransactionSetOffPojo> finTransactionSetOffPojoLists1  = (List<FinTransactionSetOffPojo>) getData(query.toString(),namedParameters,FinTransactionSetOffPojo.class);
		
		return finTransactionSetOffPojoLists1;
	}

	@Override
	public List<FinTransactionCommentsPojo> getFinTransactionCommentsData(EmployeeFinancialTransactionServiceInfo employeeFinancialTransactionInfo) {
		//log.info("*** The Control is inside the getFinTransactionCommentsData in EmployeeFinancialServiceDaoImpl ***");
		StringBuilder query = new StringBuilder(SqlQuery.QRY_TO_GET_FIN_TRANSACTION_COMMENTS_BY_ENTRY_ID)
				.append(" WHERE FTCM.TYPE_ID=:TYPE_ID ");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("TYPE_ID", employeeFinancialTransactionInfo.getTypeId(), Types.BIGINT);
		
		
		List<FinTransactionCommentsPojo> finTransactionCommentsPojoLists = (List<FinTransactionCommentsPojo>) getData(query.toString(), namedParameters, FinTransactionCommentsPojo.class);
		
		/*List<List<FinTransactionCommentsPojo>> finTransactionCommentsPojoLists = nmdPJdbcTemplate.query(query.toString(), namedParameters, 
				new ExtractDataFromResultSet<FinTransactionCommentsPojo>(FinTransactionCommentsPojo.class){});
		if(finTransactionCommentsPojoLists.isEmpty()) {
			finTransactionCommentsPojoLists.add(new ArrayList<FinTransactionCommentsPojo>());
		}*/
		//log.debug("*** The Extracted Data From Result Set Object is ***");
		return finTransactionCommentsPojoLists;
	}

	@Override
	public List<FinBookingFormAccountsPojo> getFinBookingFormAccountsInvoices(CustomerPropertyDetailsInfo customerPropertyDetailsInfo) {
		log.info("*** The Control is inside the getFinBookingFormAccountsInvoices in EmployeeFinancialServiceDaoImpl ***");
		String condition = customerPropertyDetailsInfo.getCondition() == null ? "" : customerPropertyDetailsInfo.getCondition();
		StringBuilder query = new StringBuilder();
		/* Getting Legal Invoice and Modification Invoice in Customer App Side based on request */
		if("Customer_Invoices".equalsIgnoreCase(customerPropertyDetailsInfo.getActionUrl())) {
			query = new StringBuilder(SqlQuery.QRY_TO_GET_FIN_BOOKING_FORM_ACCOUNTS_CUSTOMER_INVOICES)
					.append(" AND FBFA.PAYMENT_STATUS IN (:PAYMENT_STATUS) ")
					.append(" ORDER BY FBFA.FIN_BOOKING_FORM_ACCOUNTS_ID DESC ");
		/* Getting Both Legal Invoice and Modification Invoice in Employee Portal Side */
		} else {
			query = new StringBuilder(SqlQuery.QRY_TO_GET_FIN_BOOKING_FORM_ACCOUNTS_INVOICES)
					.append(" AND FBFA.PAYMENT_STATUS IN (:PAYMENT_STATUS) ")
					.append(" ORDER BY FBFA.FIN_BOOKING_FORM_ACCOUNTS_ID DESC ");
		}
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.VARCHAR);
		namedParameters.addValue("BOOKING_FORM_ID", customerPropertyDetailsInfo.getFlatBookingId(), Types.BIGINT);
		namedParameters.addValue("TYPE", new ArrayList<Long>(Arrays.asList(MetadataId.MODIFICATION_COST.getId(), MetadataId.LEGAL_COST.getId())));
		if(condition.equals("getCustomerInvoices")) {
			namedParameters.addValue("PAYMENT_STATUS",Arrays.asList(new Long[] { Status.PENDING.getStatus(),Status.INPROGRESS.getStatus(),Status.PAID.getStatus()}), Types.BIGINT);
		} else if(condition.equals("CompletedTransaction")) {
			namedParameters.addValue("PAYMENT_STATUS",Arrays.asList(new Long[] { Status.PENDING.getStatus(),Status.INPROGRESS.getStatus(),Status.PAID.getStatus()}), Types.BIGINT);
		} else {
			namedParameters.addValue("PAYMENT_STATUS",Arrays.asList(new Long[] { Status.PENDING.getStatus(),Status.INPROGRESS.getStatus()}), Types.BIGINT);	
		}
		
		List<FinBookingFormAccountsPojo> finBookingFormAccountsInvoicesPojoList  = (List<FinBookingFormAccountsPojo>) getData(query.toString(),namedParameters,FinBookingFormAccountsPojo.class);
		return finBookingFormAccountsInvoicesPojoList;
	}

	@Override
	public List<CoApplicantPojo> getCoApplicantDetails(CustomerPropertyDetailsInfo customerPropertyDetailsInfo) {
		//log.info("*** The Control is inside the getCoApplicantDetails in EmployeeFinancialServiceDaoImpl ***");
		StringBuilder query = new StringBuilder(SqlQuery.QRY_TO_GET_CO_APPLICANT_BY_FLAT_BOOKING_ID);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.VARCHAR);
		namedParameters.addValue("BOOKING_FORM_ID", customerPropertyDetailsInfo.getFlatBookingId());
		if("All".equalsIgnoreCase(customerPropertyDetailsInfo.getActionUrl())) {
			namedParameters.addValue("TYPE", Arrays.asList(MetadataId.APPLICANT1.getId(),MetadataId.APPLICANT2.getId()));
		}else {
			namedParameters.addValue("TYPE", Arrays.asList(MetadataId.APPLICANT1.getId()));
		}
		
		List<CoApplicantPojo> coApplicantPojoList  = (List<CoApplicantPojo>) getData(query.toString(),namedParameters,CoApplicantPojo.class);
		return coApplicantPojoList;
	}

	@Override
	public Long savePaymentAnonymousEntry(FinAnonymousEntryPojo pojo) {
		log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.savePaymentAnonymousEntry() ***** ");
		String query = SqlQuery.QRY_TO_INSERT_ANONYMOUS_ENTRY_DETAILS;
	 	Long pk = commonMethodToInsertData(query, pojo,"FIN_ANMS_ENTRY_ID" ); 
		return pk;
	}
	
	@Override
	public int updateAnonymousEntryDetails(FinAnonymousEntryPojo anonymousEntryPojo) {
		String query = SqlQuery.QRY_TO_UPDATE_ANONYMOUS_ENTRY_DETAILS;
		int result = commonMethodToUpdateData(query, anonymousEntryPojo);
		return result;
	}
	
	@Override
	public int updateAnonymousEntryReferenceNoAmount(FinAnonymousEntryPojo anonymousEntryPojo) {
		String query = SqlQuery.QRY_TO_UPDATE_ANONYMOUS_ENTRY_REFERENCE;
		int result = commonMethodToUpdateData(query, anonymousEntryPojo);
		return result;
	}
	
	@Override
	public int activeSuspenseEntryData(FlatBookingPojo oldFlatBookingPojo) {
		String query = FinancialQuerys.QRY_TO_ACTIVE_SUSPENSE_ENTRY;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		
		//changing status id to active so we can use already inserted reference number 
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("SITE_ID", oldFlatBookingPojo.getSiteId(), Types.BIGINT);
		namedParameters.addValue("FLAT_BOOK_ID", oldFlatBookingPojo.getFlatBookingId(), Types.BIGINT);
		
		int result = nmdPJdbcTemplate.update(query, namedParameters);
		return result;
	}
	
	@Override
	public List<FinAnonymousEntryPojo> getAnonymousEntriesData(EmployeeFinancialTransactionServiceInfo serviceInfo, Long status) {
		//log.info("*** The Control is inside the getAnonymousEntriesData in EmployeeFinancialServiceDaoImpl ***");
		StringBuilder query = new StringBuilder(SqlQuery.QRY_TO_GET_FIN_ANONYMOUS_ENTRY_DATA);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		
		if(serviceInfo.getRequestUrl()!=null && serviceInfo.getRequestUrl().equals("LoadModifySuspenceEntry")) {
			namedParameters.addValue("STATUS_ID", Status.MODIFY.getStatus(), Types.BIGINT);	
		} else {
			namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		}
		
		if(Util.isNotEmptyObject(serviceInfo.getSiteIds())) {
			namedParameters.addValue("SITE_ID", serviceInfo.getSiteIds(), Types.BIGINT);	
		} else {
			namedParameters.addValue("SITE_ID", serviceInfo.getSiteId(), Types.BIGINT);
		}
		if (Util.isNotEmptyObject(serviceInfo.getFlatSaleOwnerId())) {
			query.append("  AND FSPAM.FLATS_SALE_OWNERS_ID=:FLATS_SALE_OWNERS_ID ");
			namedParameters.addValue("FLATS_SALE_OWNERS_ID",serviceInfo.getFlatSaleOwnerId(), Types.BIGINT);
		}
		
		if (Util.isNotEmptyObject(serviceInfo.getAnonymousEntryId())) {
			query.append("  AND FAEN.FIN_ANMS_ENTRY_ID=:FIN_ANMS_ENTRY_ID ");
			namedParameters.addValue("FIN_ANMS_ENTRY_ID",serviceInfo.getAnonymousEntryId(), Types.BIGINT);
		}
		
		if(Util.isNotEmptyObject(serviceInfo.getReceivedFromDate())&&Util.isNotEmptyObject(serviceInfo.getReceivedToDate())) {
			query.append(" AND TRUNC(NVL(FAEN.RECEIVED_DATE,FAEN.CREATED_DATE))    BETWEEN TRUNC(:getRecievedFromDate) AND TRUNC(:getRecievedToDate)");
		} else if (Util.isNotEmptyObject(serviceInfo.getReceivedFromDate())) {
			query.append(" AND TRUNC(NVL(FAEN.RECEIVED_DATE,FAEN.CREATED_DATE))   >= TRUNC(:getRecievedFromDate) ");
		} else if (Util.isNotEmptyObject(serviceInfo.getReceivedToDate())) {
			query.append(" AND TRUNC(NVL(FAEN.RECEIVED_DATE,FAEN.CREATED_DATE))    <= TRUNC(:getRecievedToDate)");
		}
		
		namedParameters.addValue("getRecievedFromDate", serviceInfo.getReceivedFromDate(),Types.DATE);
		namedParameters.addValue("getRecievedToDate", serviceInfo.getReceivedToDate(),Types.DATE);
		query.append(" ORDER BY FAEN.FIN_ANMS_ENTRY_ID");
		List<FinAnonymousEntryPojo> finAnonymousEntryPojoList  = (List<FinAnonymousEntryPojo>) getData(query.toString(),namedParameters,FinAnonymousEntryPojo.class);
		return finAnonymousEntryPojoList;
	}
	
	@Override
	public List<Map<String, Object>> getAnonymousEntriesDataForReport(EmployeeFinancialTransactionServiceInfo serviceInfo,
			Long status) {
		StringBuilder query = new StringBuilder(FinancialQuerys.QRY_TO_GET_FIN_ANONYMOUS_ENTRY_DATA_FOR_REPORT);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_IDs", Arrays.asList(Status.ACTIVE.getStatus(),Status.MODIFY.getStatus()) , Types.BIGINT);
		/*namedParameters.addValue("FROM_DATE", serviceInfo.getFromDate(),Types.DATE);
		namedParameters.addValue("TO_DATE", serviceInfo.getToDate(),Types.DATE);*/
		
		List<Map<String, Object>>  listOfDepartments = null;
		listOfDepartments = nmdPJdbcTemplate.queryForList(query.toString(), namedParameters);
		return listOfDepartments;
	}

	@Override
	public List<FinAnnyEntryDocPojo> getFinAnnyEntryDocData(FinAnonymousEntryInfo finAnonymousEntryInfo) {
		//log.info("*** The Control is inside the getFinAnnyEntryDocData in EmployeeFinancialServiceDaoImpl ***");
		StringBuilder query = new StringBuilder(SqlQuery.QRY_TO_GET_FIN_ANNY_ENTRY_DOC_DATA);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FIN_ANMS_ENTRY_ID", finAnonymousEntryInfo.getAnonymousEntryId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		
		List<FinAnnyEntryDocPojo> finAnnyEntryDocPojoList  = (List<FinAnnyEntryDocPojo>) getData(query.toString(),namedParameters,FinAnnyEntryDocPojo.class);
		return finAnnyEntryDocPojoList;
	}

	@Override
	public List<FinAnnyEntryCommentsPojo> getFinAnnyEntryCommentsData(EmployeeFinancialTransactionServiceInfo serviceInfo) {
		//log.info("*** The Control is inside the getFinAnnyEntryCommentsData in EmployeeFinancialServiceDaoImpl ***");
		StringBuilder query = new StringBuilder(SqlQuery.QRY_TO_GET_FIN_ANNY_ENTRY_COMMENTS_DATA)
				.append(" WHERE FAEC.TYPE_ID=:TYPE_ID ")
				.append(" AND FAEC.TYPE NOT IN (:TYPE)")
				;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("TYPE_ID", serviceInfo.getTypeId(), Types.BIGINT);
		
		if(serviceInfo.getRequestUrl()!=null && serviceInfo.getRequestUrl().equals("LoadModifySuspenceEntry")) {
			namedParameters.addValue("TYPE",0, Types.BIGINT);
		} else {
			namedParameters.addValue("TYPE",0, Types.BIGINT);
			//namedParameters.addValue("TYPE",MetadataId.CRM.getId(), Types.BIGINT);
		}
		
		List<FinAnnyEntryCommentsPojo> finAnnyEntryCommentsPojoList  = (List<FinAnnyEntryCommentsPojo>) getData(query.toString(),namedParameters,FinAnnyEntryCommentsPojo.class);
		return finAnnyEntryCommentsPojoList;
	}

	@Override
	public List<FinTransferModePojo> getFinTransferModeData(EmployeeFinancialTransactionServiceInfo employeeFinancialTransactionInfo) {
		//log.info("*** The Control is inside the getFinTransferModeData in EmployeeFinancialServiceDaoImpl ***");
		StringBuilder query = new StringBuilder(SqlQuery.QRY_TO_GET_FIN_TRANSFER_MODE_DATA);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		
		List<FinTransferModePojo> finTransferModePojoList  = (List<FinTransferModePojo>) getData(query.toString(),namedParameters,FinTransferModePojo.class);
		return finTransferModePojoList;
	}

	@Override
	public List<FinTransactionApprStatPojo> getFinTransactionApprStatData(EmployeeFinancialTransactionServiceInfo employeeFinancialTransactionInfo) {
		//log.info("*** The Control is inside the getFinTransactionApprStatData in EmployeeFinancialServiceDaoImpl ***");
		StringBuilder query = new StringBuilder(SqlQuery.QRY_TO_GET_FIN_TRANSACTION_APPR_STAT_DATA);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FIN_TRANSACTION_ENTRY_ID", employeeFinancialTransactionInfo.getTransactionEntryId(), Types.BIGINT);
		
		List<FinTransactionApprStatPojo> finTransactionApprStatPojoList  = (List<FinTransactionApprStatPojo>) getData(query.toString(),namedParameters,FinTransactionApprStatPojo.class);
		return finTransactionApprStatPojoList;
	}
	
	@Override
	public List<FinAnnyApproveStatPojo> getAnonymousApprStatData(EmployeeFinancialTransactionServiceInfo employeeFinancialTransactionInfo) {
		//log.info("*** The Control is inside the EmployeeFinancialServiceDaoImpl.getAnonymousApprStatData() ***");
		String query = SqlQuery.QRY_TO_GET_ANNYNOMOUS_APPR_STAT_DATA;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FIN_ANMS_ENTRY_ID", employeeFinancialTransactionInfo.getAnonymousEntryId(), Types.BIGINT);
		namedParameters.addValue("ACTION_TYPE", Status.MODIFY.getStatus(), Types.BIGINT);
		
		List<FinAnnyApproveStatPojo> finTransactionApprStatPojoList  = (List<FinAnnyApproveStatPojo>) getData(query.toString(),namedParameters,FinAnnyApproveStatPojo.class);
		return finTransactionApprStatPojoList;
	}
	
	@Override
	public FinBookingFormLegalCostPojo saveFinBookingFormLegalCostData(FinBookingFormLegalCostPojo finBookingFormLegalCostPojo) {
		log.info("*** The Control is inside the saveFinBookingFormLegalCostData in EmployeeFinancialServiceDaoImpl ***");
		String query = SqlQuery.QRY_TO_INSERT_INTO_FIN_BOOKING_FORM_LEGAL_COST;
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		nmdPJdbcTemplate.update(query, new BeanPropertySqlParameterSource(finBookingFormLegalCostPojo), keyHolder, new String[] {"FIN_BOK_FRM_LEGAL_COST_ID"});
		finBookingFormLegalCostPojo.setFinBokFrmLegalCostId(keyHolder.getKey().longValue());
		return finBookingFormLegalCostPojo;
	}

	@Override
	public List<FinBookingFormLglCostDtlsPojo> saveFinBookingFormLglCostDtlsData(List<FinBookingFormLglCostDtlsPojo> finBookingFormLglCostDtlsPojoList) {
		log.info("*** The Control is inside the saveFinBookingFormLglCostDtlsData in EmployeeFinancialServiceDaoImpl ***");
		String query = SqlQuery.QRY_TO_INSERT_INTO_FIN_BOOKING_FORM_LGL_COST_DTLS;
		for(FinBookingFormLglCostDtlsPojo finBookingFormLglCostDtlsPojo : finBookingFormLglCostDtlsPojoList) {
			GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
			nmdPJdbcTemplate.update(query, new BeanPropertySqlParameterSource(finBookingFormLglCostDtlsPojo), keyHolder, new String[] {"FIN_BOK_FRM_LEGAL_COST_ID"});
			finBookingFormLglCostDtlsPojo.setFinBokFrmLegalCostId(keyHolder.getKey().longValue());
		}
		return finBookingFormLglCostDtlsPojoList;
	}

	@Override
	public boolean isTypeExistedInFinBookingFormAccounts(CustomerPropertyDetailsInfo customerPropertyDetailsInfo,EmployeeFinancialTransactionServiceInfo employeeFinancialTransactionServiceInfo) {
		//log.info("*** The Control is inside the saveFinBookingFormLglCostDtlsData in EmployeeFinancialServiceDaoImpl ***");
		String query = SqlQuery.QRY_TO_GET_COUNT_FIN_BOOKING_FORM_ACCOUNTS;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("TYPE", employeeFinancialTransactionServiceInfo.getTypeId(), Types.BIGINT);
		namedParameters.addValue("BOOKING_FORM_ID", customerPropertyDetailsInfo.getFlatBookingId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		return nmdPJdbcTemplate.queryForObject(query, namedParameters, Boolean.class);
	}

	@Override
	public FinBokFrmLglCostTaxPojo saveFinBokFrmLglCostTaxData(FinBokFrmLglCostTaxPojo finBokFrmLglCostTaxPojo) {
		log.info("*** The Control is inside the saveFinBokFrmLglCostTaxData in EmployeeFinancialServiceDaoImpl ***");
		String query = SqlQuery.QRY_TO_INSERT_INTO_FIN_BOK_FRM_LGL_COST_TAX;
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		nmdPJdbcTemplate.update(query, new BeanPropertySqlParameterSource(finBokFrmLglCostTaxPojo), keyHolder, new String[] {"FIN_BOK_FRM_LGL_COST_TAX_ID"});
		finBokFrmLglCostTaxPojo.setFinBokFrmLglCostTaxId(keyHolder.getKey().longValue());
		return finBokFrmLglCostTaxPojo;
	}
	
	
	@Override
	public Long saveFlatCancelationCost(@NonNull FlatCancellationCostPojo pojo) {
		log.info("***** The control is inside the saveFlatCancelationCost in EmployeeFinancialServiceDaoImpl *****");
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		nmdPJdbcTemplate.update(SqlQuery.QRY_TO_INSERT_FIN_BKF_FLAT_CANCELATION_COST,
				new BeanPropertySqlParameterSource(pojo), keyHolder,
				new String[] { "ID" });
		//log.info("***** The primarykey generated for this record is ******" + keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	}
	
	
	@Override
	public Long saveFinBookingFormRefundableAdvance(@NonNull FinBookingFormRefundableAdvancePojo pojo) {
		log.info("***** The control is inside the saveFinBookingFormRefundableAdvance in EmployeeFinancialServiceDaoImpl *****");
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		nmdPJdbcTemplate.update(SqlQuery.QRY_TO_INSERT_FIN_BKNG_FORM_REFUNDABLE_ADV,
				new BeanPropertySqlParameterSource(pojo), keyHolder,
				new String[] { "ID" });
		//log.info("***** The primarykey generated for this record is ******" + keyHolder.getKey().longValue());
		return keyHolder.getKey().longValue();
	}

	@Override
	
	public List<FlatBookingPojo> getCustomerDetailsAndPendingAmounts(EmployeeFinancialServiceInfo employeeFinancialServiceInfo) {
		//log.info("***** The control is inside the getCustomerDetailsAndPendingAmounts in EmployeeFinancialServiceDaoImpl *****");
		String sqlQuery = SqlQuery.QRY_TO_GET_CUSTOMER_DETAILS_PENDING_AMOUNT;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("MILESTONE_IDS", employeeFinancialServiceInfo.getProjectMileStoneIds(), Types.BIGINT);
		namedParameters.addValue("BLOCK_IDS", employeeFinancialServiceInfo.getBlockIds()); 		
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		List<FlatBookingPojo> flatBookingPojoList = (List<FlatBookingPojo>) getData(sqlQuery, namedParameters, FlatBookingPojo.class);
		return flatBookingPojoList;
	}

	@Override
	public int saveInvoiceDocumentLocationForCharges(List<InvoiceDocumentLocationPojo> invoiceDocumentLocationPojoList) {
		log.info("***** The control is inside the saveInvoiceDocumentLocationForCharges in EmployeeFinancialServiceDaoImpl *****");
		int sqlParameterIndex = 0;
		String  sqlQuery = SqlQuery.QRY_TO_INSERT_INTO_INVOICE_DOCUMENT_LOCATION;
		SqlParameterSource sqlParameterSourceArray[] = new SqlParameterSource[invoiceDocumentLocationPojoList.size()];
		for(InvoiceDocumentLocationPojo invoiceDocumentLocationPojo : invoiceDocumentLocationPojoList) {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("type", invoiceDocumentLocationPojo.getType(), Types.BIGINT);
			namedParameters.addValue("typeId", invoiceDocumentLocationPojo.getTypeId(), Types.BIGINT);
			namedParameters.addValue("docName", invoiceDocumentLocationPojo.getDocName(), Types.VARCHAR);
			namedParameters.addValue("docLocation", invoiceDocumentLocationPojo.getDocLocation(), Types.VARCHAR);
			namedParameters.addValue("docPath", invoiceDocumentLocationPojo.getDocPath(), Types.VARCHAR);
			namedParameters.addValue("createdBy", invoiceDocumentLocationPojo.getCreatedBy(), Types.BIGINT);
			sqlParameterSourceArray[sqlParameterIndex++] = namedParameters;
		}
		int result[] = nmdPJdbcTemplate.batchUpdate(sqlQuery, sqlParameterSourceArray);
		return result.length;
	}

	@Override
	//This method is returning Excess Amount Details
	public List<FinBookingFormExcessAmountPojo> getExcessAmountDetailsForRefund(EmployeeFinancialTransactionServiceInfo employeeFinancialTransactionServiceInfo) {
		//log.info("***** The control is inside the getExcessAmountDetailsForRefund in EmployeeFinancialServiceDaoImpl *****");
		String sqlQuery = SqlQuery.QRT_TO_GET_EXCESS_AMOUNT_FOR_REFUND;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("BOOKING_FORM_ID", employeeFinancialTransactionServiceInfo.getBookingFormId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("TYPE_IDS", new ArrayList<Long>(Arrays.asList(MetadataId.LEGAL_COST.getId(), MetadataId.MODIFICATION_COST.getId())));
		List<FinBookingFormExcessAmountPojo> finBookingFormExcessAmountPojoList = getData(sqlQuery, namedParameters, FinBookingFormExcessAmountPojo.class);
		return finBookingFormExcessAmountPojoList;
	}
	
	@Override
	//This Method is returning Excess amount refund(returned amount to customer) details
	public List<FinBookingFormAccountPaymentDetailsPojo> getExcessAmountPaymentRefundDetails(FinBookingFormAccountPaymentPojo bookingFormAccountPaymentPojo) {
		//log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.getExcessAmountPaymentRefundDetails() *****");
		String sqlQuery = SqlQuery.QRT_TO_GET_EXCESS_AMOUNT_PAYMENT_REFUND;
		
		List<FinBookingFormAccountPaymentDetailsPojo>  excessAmountReturnDetails =  getData(sqlQuery, bookingFormAccountPaymentPojo, FinBookingFormAccountPaymentDetailsPojo.class);
		return excessAmountReturnDetails;
	}

	@Override
	public List<OfficeDtlsPojo> getOfficeDetailsBySite(EmployeeFinancialServiceInfo employeeFinancialServiceInfo) {
		//log.info("***** The control is inside the getOfficeDetailsBySite in EmployeeFinancialServiceDaoImpl *****");
		String sqlQuery = SqlQuery.QRY_TO_GET_OFFICE_DTLS_BY_SITE_ID;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("SITE_ID", employeeFinancialServiceInfo.getSiteId(), Types.BIGINT);
		List<OfficeDtlsPojo> officeDtlsPojoList =  getData(sqlQuery, namedParameters, OfficeDtlsPojo.class);
		return officeDtlsPojoList;
	}
	
	@Override
	public List<SitePojo> getSiteDetails(Site site) {
		StringBuilder query = new StringBuilder(SqlQuery.QRY_TO_GET_ALL_SITE_DETAILS)
				.append(" WHERE S.STATUS_ID IN (:STATUS_ID) ");
		if(site.getReferMessage()!=null && site.getReferMessage().equals("PORTAL_SITE_NAME")) {	
			query.append("  AND S.NAME IN (:NAME) ").toString();
		} else {
			query.append("  AND S.SALES_FORCE_SITE_NAME IN (:NAME) ").toString();
		}
		
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("NAME",site.getName(),Types.VARCHAR);
		namedParameters.addValue("STATUS_ID",Arrays.asList(1L,2L), Types.INTEGER);
		
		List<SitePojo> sitePojoLISTS =  getData(query.toString(), namedParameters, SitePojo.class);
		return sitePojoLISTS;
	}
	
	@Override
	public List<Map<String, Object>> getNonRefundFlats(EmployeeFinancialRequest employeeFinancialRequest) {
		log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.getNonRefundFlats() *****");
		List<Long> listOfBookingStatus = Arrays.asList(Status.SWAP.getStatus(), Status.PRICE_UPDATE.getStatus(), Status.CANCEL.getStatus(),
				Status.RETAINED.getStatus(), Status.INACTIVE.getStatus(), Status.PMAY_SCHEME_ELIGIBLE.getStatus()
				, Status.CANCELLED_NOT_REFUNDED.getStatus(),Status.ASSIGNMENT.getStatus()
				);

		StringBuilder sqlQuery = new StringBuilder(SqlQuery.QRY_TO_GET_NON_REFUND_FLATS1);
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("BOOKING_STATUS_ID", listOfBookingStatus, Types.BIGINT);
		namedParameters.addValue("TYPEs", Arrays.asList(MetadataId.FIN_BOOKING_FORM_MILESTONES.getId()), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Arrays.asList(Status.ACTIVE.getStatus()), Types.BIGINT);
		if(Util.isNotEmptyObject(employeeFinancialRequest.getSiteIds())) {
			sqlQuery.append("  AND S.SITE_ID IN (:SITE_IDs)");
			namedParameters.addValue("SITE_IDs", employeeFinancialRequest.getSiteIds() , Types.BIGINT);	
		}
		
		if(Util.isNotEmptyObject(employeeFinancialRequest.getBlockIds())) {
			sqlQuery.append(" AND BD.BLOCK_ID IN (:BLOCK_IDs)");
			namedParameters.addValue("BLOCK_IDs", employeeFinancialRequest.getBlockIds() , Types.BIGINT);	
		}
		List<Map<String, Object>> list = nmdPJdbcTemplate.queryForList(sqlQuery.toString(), namedParameters);
		log.info("***** Control inside the EmployeeFinancialServiceDaoImpl.getNonRefundFlats() *****\n" + list);
		return list;
	}



	@Override
	public List<CustomerPropertyDetailsPojo> getCustomerDetails(EmployeeFinancialServiceInfo employeeFinancialServiceInfo) {
		log.info("***** Control inside the getCustomerDetails in EmployeeFinancialServiceDaoImpl *****");
		String sqlQuery="";
		if (Util.isNotEmptyObject(employeeFinancialServiceInfo.getRequestUrl()) && employeeFinancialServiceInfo.getRequestUrl().equalsIgnoreCase("ViewAllData")) {
			sqlQuery = SqlQuery.QRY_TO_GET_CUSTOMER_DETAILS_FOR_CONSOLIDATED_RECEIPT_FB_WITHOUT_STATUS;
		} else {
			sqlQuery = SqlQuery.QRY_TO_GET_CUSTOMER_DETAILS_FOR_CONSOLIDATED_RECEIPT;
		}
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FLAT_BOOK_ID", employeeFinancialServiceInfo.getBookingFormId());
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus());
		List<CustomerPropertyDetailsPojo> customerPropertyDetailsPojoList =  getData(sqlQuery, namedParameters, CustomerPropertyDetailsPojo.class);
		return customerPropertyDetailsPojoList;
	}

	@Override
	public List<FinTransactionEntryDetailsPojo> getTransactionDetailsConsolidatedReceipt(FinTransactionEntryDetailsInfo transactionEntryDetailsInfo) {
		log.info("***** Control inside the getTransactionDetailsConsolidatedReceipt in EmployeeFinancialServiceDaoImpl *****");
		//String sqlQuery = SqlQuery.QRY_TO_GET_TRANSACTION_DETAILS_FOR_CONSOLIDATED_RECEIPT;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
/*		namedParameters.addValue("BOOKING_FORM_ID", employeeFinancialServiceInfo.getBookingFormId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus());
		namedParameters.addValue("TRANSACTION_STATUS_IDS", Arrays.asList(Status.TRANSACTION_COMPLETED.getStatus(), Status.CHEQUE_BOUNCED.getStatus()));
		List<FinTransactionEntryDetailsPojo> finTransactionEntryDetailsPojoList =  getData(sqlQuery, namedParameters, FinTransactionEntryDetailsPojo.class);*/

		boolean isCustomerLedger = (transactionEntryDetailsInfo.getCondition()!=null && transactionEntryDetailsInfo.getCondition().equals(MetadataId.CUSTOMER_LEDGER.getName()));
		boolean isPayementTrn =  (transactionEntryDetailsInfo.getCondition()!=null && transactionEntryDetailsInfo.getCondition().equals("LoadPaymentTrn"));
		boolean inCompleteTransaction =  (transactionEntryDetailsInfo.getCondition()!=null && transactionEntryDetailsInfo.getCondition().equals("LoadNonCompletedTransactions"));
		boolean loadExcessAmountReceiptDetailsList =  (transactionEntryDetailsInfo.getCondition()!=null && transactionEntryDetailsInfo.getCondition().equals("loadExcessAmountReceiptDetailsList"));
		
		StringBuffer query = null;
		
		if (isCustomerLedger) {
			query = new StringBuffer(SqlQuery.QRY_TO_GET_RECEIPT_DETAILS_FOR_CONSOLIDATED_RECEIPT);
			query.append(" AND FBA.TYPE IN (:TYPE) ");
			query.append(" ORDER BY FTE.RECEIVED_DATE,FTE.FIN_TRANSACTION_ENTRY_ID,FBAS.FIN_BOK_FRM_ACC_SMT_ID ");
		} else if (isPayementTrn) {
			query = new StringBuffer(SqlQuery.QRY_TO_GET_PAYMENT_DETAILS_FOR_CONSOLIDATED_RECEIPT);
			query.append(" AND FBFAPD.TYPE IN (:PAYMENT_REFUND_TYPE) ");
			query.append(" ORDER BY FTE.RECEIVED_DATE,FTE.FIN_TRANSACTION_ENTRY_ID,FBFAPD.FIN_BOK_FRM_ACC_PMT_DTLS_ID ");
			namedParameters.addValue("PAYMENT_REFUND_TYPE", Arrays.asList(MetadataId.FIN_BOOKING_FORM_ACCOUNTS.getId(),MetadataId.FIN_BOOKING_FORM_EXCESS_AMOUNT.getId()) , Types.BIGINT);

		} else if (inCompleteTransaction) {
			 query = new StringBuffer(SqlQuery.QRY_TO_GET_FIN_TRANSACTION__ENTRY_INCOMPLETE_TRN);
			 query.append(" AND FBA.TYPE IN (:TYPE) ");
			 query.append(" ORDER BY FTE.RECEIVED_DATE,FTE.FIN_TRANSACTION_ENTRY_ID ");
		} else if (loadExcessAmountReceiptDetailsList) {
			 query = new StringBuffer(SqlQuery.QRY_TO_GET_FIN_TRANSACTION_ENTRY_EXCESS_AMOUNT_TRN);
			 //query.append(" AND FBA.TYPE IN (:TYPE) ");
			 query.append(" ORDER BY FTE.RECEIVED_DATE,FTE.FIN_TRANSACTION_ENTRY_ID ");
		}

		if (isPayementTrn) {
		} else if (inCompleteTransaction) {
		} else if (isCustomerLedger) {
		}

		namedParameters.addValue("TYPE", Arrays.asList(MetadataId.FIN_BOOKING_FORM_MILESTONES.getId()), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("DOC_TYPE", MetadataId.GENERATED.getId(), Types.BIGINT);
		namedParameters.addValue("RECEIPT_TYPE", MetadataId.FIN_BOOKING_FORM_MILESTONES.getId(), Types.BIGINT);
		namedParameters.addValue("BOOKING_FORM_ID", transactionEntryDetailsInfo.getBookingFormId(), Types.BIGINT);
		namedParameters.addValue("TRANSACTION_STATUS_ID", Status.TRANSACTION_COMPLETED.getStatus(), Types.BIGINT);
		namedParameters.addValue("FIN_TRANSACTION_ENTRY_ID", transactionEntryDetailsInfo.getTransactionEntryId(), Types.BIGINT);

		 if (inCompleteTransaction) {
			 namedParameters.addValue("TRANSACTION_STATUS_ID", Status.CHEQUE_BOUNCED.getStatus(), Types.BIGINT);		
		 }
		
		List<FinTransactionEntryDetailsPojo> receiptDetails = getData(query.toString(), namedParameters,
				FinTransactionEntryDetailsPojo.class);

		return receiptDetails;
	}
	
	@Override
	public Long getApprovalNextLevelId(EmployeeFinancialTransactionServiceInfo transactionServiceInfo,Status status) {
		log.info("***** Control inside the getApprovalNextLevelId in EmployeeFinancialServiceDaoImpl *****");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		StringBuffer query = new StringBuffer(SqlQuery.QRY_TO_GET_NEXT_FIN_APPROVA_ID);
		if(Status.APPROVED.getStatus().equals(status.getStatus())) {
			query.append(" AND FSOAL.FIN_SET_OFF_LEVEL_ID = :FIN_SET_OFF_LEVEL_ID FETCH FIRST ROW ONLY");
			namedParameters.addValue("FIN_SET_OFF_LEVEL_ID", transactionServiceInfo.getFinsetOffAppLevelId(), Types.BIGINT);
		} else if(Status.CREATED.getStatus().equals(status.getStatus())) {
			query.append(" AND  FEMAPPING.EMP_ID=:EMP_ID AND FSOAL.ACTION_TYPE=:ACTION_TYPE  FETCH FIRST ROW ONLY ");
			namedParameters.addValue("EMP_ID", transactionServiceInfo.getEmployeeId(), Types.BIGINT);
			namedParameters.addValue("ACTION_TYPE", Status.APPROVED.getStatus(), Types.BIGINT);
		}
		
		namedParameters.addValue("TYPE", MetadataId.MODIFICATION_COST.getId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		Long approvalId =  nmdPJdbcTemplate.queryForObject(query.toString(), namedParameters, Long.class);
		return approvalId;
	}
	
	@Override
	public Long saveModifiactionInvocieAppRej(ModicationInvoiceAppRej modicationInvoiceAppRej) {
		log.info("***** The Control is inside the EmployeeFinancialServiceDaoImpl.saveModifiactionInvocieAppRejsaveModifiactionInvocieAppRej() ***** ");
		String query = SqlQuery.QRY_TO_INSERT_FIN_MODI_INVOICE_APPR_REJ;
	 	Long pk = commonMethodToInsertData(query, modicationInvoiceAppRej,"MODI_INVOICE_APPR_REJ_ID" ); 
	 	modicationInvoiceAppRej.setModificationInvoiceAppRejectId(pk);
		return pk;
	}
	
	@Override
	public List<FinModificationInvoicePojo> getPendingModificationInvoices( EmployeeFinancialRequest employeeFinancialPojo,List<Long> listOfApprovalId)
	{
		log.info("***** Control inside the getPendingModificationInvoices in EmployeeFinancialServiceDaoImpl *****");
		String condition = employeeFinancialPojo.getCondition()==null?"":employeeFinancialPojo.getCondition();
		StringBuffer query = null;
		query = new StringBuffer(SqlQuery.QRY_TO_GET_FIN_PENDING_MOD_INVOICES);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		query.append(" WHERE  FB.STATUS_ID=:STATUS_ID");
		if(Util.isNotEmptyObject(employeeFinancialPojo.getSiteIds())) {
			namedParameters.addValue("SITE_IDs", employeeFinancialPojo.getSiteIds(), Types.BIGINT);	
			query.append(" AND   S.SITE_ID IN (:SITE_IDs)  ");
		}
		 if(Util.isNotEmptyObject(employeeFinancialPojo.getBlockIds()))
		{
			namedParameters.addValue("BLOCK_IDs", employeeFinancialPojo.getBlockIds(), Types.BIGINT);	
			query.append(" AND   BD.BLOCK_ID IN (:BLOCK_IDs)  ");
			
		}if(Util.isNotEmptyObject(employeeFinancialPojo.getFlatIds()))
		{
			namedParameters.addValue("FLAT_IDs", employeeFinancialPojo.getFlatIds(), Types.BIGINT);	
			query.append(" AND   FB.FLAT_ID IN (:FLAT_IDs)  ");
			
		}
	    if(Util.isNotEmptyObject(employeeFinancialPojo.getFloorIds()))
		{
			namedParameters.addValue("FLOOR_IDs", employeeFinancialPojo.getFloorIds(), Types.BIGINT);	
			query.append(" AND   FL.FLOOR_ID IN (:FLOOR_IDs)  ");
			
		}
		query.append(" ) ");
		namedParameters.addValue("TYPE", MetadataId.MODIFICATION_COST.getId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("FIN_SET_OFF_APPR_LVL_ID",listOfApprovalId , Types.BIGINT);
		namedParameters.addValue("EMP_ID",employeeFinancialPojo.getEmpId() , Types.BIGINT);
		
		if(condition.equals("approveModificationInvoices")) {
			namedParameters.addValue("MODIFICATION_STATUS_ID",Status.PENDING.getStatus() , Types.BIGINT);	
		} else if(condition.equals("modificationInvoiceStatus")) {
			namedParameters.addValue("MODIFICATION_STATUS_ID", Arrays.asList(Status.PENDING.getStatus(),Status.REJECTED.getStatus()) , Types.BIGINT);
		} else {
			namedParameters.addValue("MODIFICATION_STATUS_ID",Status.TRANSACTION_COMPLETED.getStatus() , Types.BIGINT);
		}
		
		List<FinModificationInvoicePojo> finModificationInvoicePojoList = getData(query.toString(), namedParameters,
				FinModificationInvoicePojo.class);
		return finModificationInvoicePojoList;
	}

	@Override
	public List<Long> getNextApprovalLevelId(EmployeeFinancialRequest employeeFinancialRequest) {
		log.info("***** Control inside thegetApprovalLevelId in EmployeeFinancialServiceDaoImpl *****");
		String query = SqlQuery.QRY_TO_GET_FIN_APPROVA_ID;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("TYPE", MetadataId.MODIFICATION_COST.getId(), Types.BIGINT);
		namedParameters.addValue("EMP_ID", employeeFinancialRequest.getEmpId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		//Long approvalId = nmdPJdbcTemplate.queryForObject(query, namedParameters, Long.class);
		List<Long> listOfApprovalId = nmdPJdbcTemplate.queryForList(query, namedParameters,Long.class);
		return listOfApprovalId;
	}

	@Override
	public List<ModicationInvoiceAppRej> getModificationApprRejectDetails(EmployeeFinancialTransactionServiceInfo transactionServiceInfo) {
		String query = SqlQuery.QRY_TO_GET_FIN_MODIFICATION_APPR_DETAILS;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FIN_BOK_FRM_MODI_COST_ID", transactionServiceInfo.getFinBookingFormModiCostId(), Types.BIGINT);
		List<ModicationInvoiceAppRej> finModificationInvoicePojoList = getData(query.toString(), namedParameters,
				ModicationInvoiceAppRej.class);
		return finModificationInvoicePojoList;
	}
	
	@Override
	public List<ModicationInvoiceAppRej>  getNextModificationApprovalDetails(EmployeeFinancialTransactionServiceInfo transactionServiceInfo) {
		String query = SqlQuery.QRY_TO_GET_NEXT_FIN_MODIFICATION_APPROVAL_DETAILS;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FIN_SET_OFF_LEVEL_ID", transactionServiceInfo.getFinsetOffAppLevelId(), Types.BIGINT);
		namedParameters.addValue("TYPE", MetadataId.MODIFICATION_COST.getId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		List<ModicationInvoiceAppRej> finModificationInvoicePojoList = getData(query.toString(), namedParameters,ModicationInvoiceAppRej.class);
		
		return finModificationInvoicePojoList;
	}
	
	@Override
	public List<Map<String, Object>> getEmployeeDetailsUsingDeptAndRoll(Site site, Map<String, Object> map) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuffer query = null;
		query = new StringBuffer(FinancialQuerys.QRY_TO_GET_EMP_EMAILS_TO_FOR_RATE_OF_INTEREST);
		 if (site.getName()!=null && site.getName().equals("BookingForm")) {
				//name field used like condition
				query.append(" AND EL.EMP_ID = :EMP_ID ");
				namedParameters.addValue("EMP_ID", map.get("empId"), Types.BIGINT);
		} else if(map!=null) {
				query.append(" AND EDSM.ROLE_ID = ER.ROLE_ID ");
				query.append(" AND (EDSM.DEPT_ID IN (:DEPT_ID) OR EDSM.ROLE_ID IN (:ROLE_ID)) ");
				namedParameters.addValue("DEPT_ID", map.get("listOfDepartments"), Types.BIGINT);
				namedParameters.addValue("ROLE_ID", map.get("listOfRolles"), Types.BIGINT);
		}
				
		namedParameters.addValue("SITE_ID", site.getSiteId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		List<Map<String, Object>>  listOfDepartments = null;
		try {
			  listOfDepartments = nmdPJdbcTemplate.queryForList(query.toString(), namedParameters);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		log.info("***** Control inside the getEmployeeDetailsUsingDeptAndRoll in EmployeeFinancialServiceDaoImpl *****\n"+query+"\n"+namedParameters.getValues());
		return listOfDepartments;
	}

	@Override
	public List<EmployeeDetailsPojo> getEmployeeDetailsUsingDeptAndRollAndBlockId(
			CustomerPropertyDetailsPojo customerPropertyDetailsPojo, Map<String, Object> map) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuffer query = null;
		//query = new StringBuffer(FinancialQuerys.QRY_TO_GET_EMP_EMAILS_TO_FOR_RATE_OF_INTEREST);
		//query = new StringBuffer(SqlQueryTwo.QRY_TO_GET_EMPLOYEE_DETAILS);
		query = new StringBuffer(SqlQueryTwo.QRY_TO_GET_EMPLOYEE_DETAILS_FOR_MAIL);
		
		//query.append(" AND EDSM.ROLE_ID = ER.ROLE_ID ");
		if(Util.isNotEmptyObject(customerPropertyDetailsPojo.getBlockId())) {
			query.append(" AND EDSM.BLOCK_ID = :BLOCK_ID ");
		}
		namedParameters.addValue("DEPT_IDS", map.get("listOfDepartments"), Types.BIGINT);
		namedParameters.addValue("ROLE_ID", map.get("listOfRolles"), Types.BIGINT);
		
		namedParameters.addValue("SITE_ID", customerPropertyDetailsPojo.getSiteId(), Types.BIGINT);
		namedParameters.addValue("BLOCK_ID", customerPropertyDetailsPojo.getBlockId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		
		List<EmployeeDetailsPojo> listOfEmployee = getData(query.toString(), namedParameters,
				EmployeeDetailsPojo.class);
		
		log.info("***** Control inside the getEmployeeDetailsUsingDeptAndRoll in EmployeeFinancialServiceDaoImpl *****\n"+query+"\n"+namedParameters.getValues());
		return listOfEmployee;
	}

	@Override
	public List<Map<String, Object>> downloadGeneratedDemandNote(EmployeeFinancialRequest employeeFinancialRequest) {
		String query = FinancialQuerys.QRY_TO_GET_DEMAND_NOTE;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		List<Map<String, Object>>  listOfFormats = nmdPJdbcTemplate.queryForList(query.toString(), namedParameters);
		return listOfFormats;
	}
	
	@Override
	public List<FinancialProjectMileStonePojo> getFinancialDetails(EmployeeFinancialServiceInfo employeeFinancialRequest, Status active) {
		String query = "";
		query = new StringBuilder(SqlQuery.QRY_TO_GET_FINANCIAL_DETAILS).toString();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FLAT_BOOK_ID",employeeFinancialRequest.getBookingFormId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID",active.getStatus(), Types.BIGINT);
		namedParameters.addValue("SITE_ID",employeeFinancialRequest.getSiteId(), Types.BIGINT);

		List<FinancialProjectMileStonePojo>  listOfMilestones = getData(query, namedParameters, FinancialProjectMileStonePojo.class);
		
		return listOfMilestones;
	}
	
	/*@Override
	public List<FinModificationInvocieDetailsPojo> getPendingModificationInvoiceDetails( EmployeeFinancialRequest employeeFinancialPojo)
	{
		log.info("***** Control inside the getPendingModificationInvoiceDetails in EmployeeFinancialServiceDaoImpl *****");
		StringBuffer query = null;
		query = new StringBuffer(SqlQuery.QRY_TO_GET_FIN_PENDING_MOD_INVOICE_DETAILS);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		query.append(" WHERE FSOL.TYPE=:TYPE ");
		query.append(" AND FBFMC.STATUS_ID=:STATUS_ID ");
		if(Util.isNotEmptyObject(employeeFinancialPojo.getBookingFormId())) {
			namedParameters.addValue("BOOKING_FORM_ID", employeeFinancialPojo.getBookingFormId(), Types.BIGINT);	
			query.append(" AND FBFMC.BOOKING_FORM_ID=:BOOKING_FORM_ID ");
		}
		 if(employeeFinancialPojo.getFinBookingFormModiCostId()!=null && Util.isNotEmptyObject(employeeFinancialPojo.getFinBookingFormModiCostId()))
		{
			namedParameters.addValue("FIN_BOK_FRM_MODI_COST_ID", employeeFinancialPojo.getFinBookingFormModiCostId(), Types.BIGINT);	
			query.append(" AND FBFMC.FIN_BOK_FRM_MODI_COST_ID=:FIN_BOK_FRM_MODI_COST_ID  ");
			
		}
		if(Util.isNotEmptyObject(employeeFinancialPojo.getSiteId())) {
			namedParameters.addValue("SITE_ID", employeeFinancialPojo.getSiteId(), Types.BIGINT);	
			query.append(" AND   S.SITE_ID IN (:SITE_ID)  ");
		}
		namedParameters.addValue("TYPE", MetadataId.MODIFICATION_COST.getId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		List<FinModificationInvocieDetailsPojo> FinModicationInvocieDetailsPojoList = getData(query.toString(), namedParameters,
				FinModificationInvocieDetailsPojo.class);
		return FinModicationInvocieDetailsPojoList;
	}*/
	
	@Override
	public List<Map<String, Object>> loadDemandNoteFormats(EmployeeFinancialServiceInfo info) {
		String query = FinancialQuerys.QRY_TO_GET_DEMAND_NOTE_FORMATS;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		List<Map<String, Object>>  listOfFormats = nmdPJdbcTemplate.queryForList(query.toString(), namedParameters);
		return listOfFormats;
	}

	@Override
	public List<FinTransactionSetOffPojo> getInterestWaivedAndPaidDetails(EmployeeFinancialServiceInfo empFinSerInfo) {
		log.info("*** The Control is inside the getInterestWaivedAndPaidDetails in EmployeeFinancialServiceDaoImpl ***"+empFinSerInfo);
		StringBuilder sqlQuery = new StringBuilder(FinancialQuerys.QRY_TO_GET_INTEREST_WAIVED_AND_PAID_DETAILS);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		
		/* By Site Id */
		if(Util.isNotEmptyObject(empFinSerInfo.getSiteId())) {
			sqlQuery.append(" AND ST.SITE_ID=:SITE_ID ");
			namedParameters.addValue("SITE_ID", empFinSerInfo.getSiteId());
		}
		
		/* By Flat Id */
		if(Util.isNotEmptyObject(empFinSerInfo.getFlatId())) {
			sqlQuery.append(" AND FT.FLAT_ID=:FLAT_ID ");
			namedParameters.addValue("FLAT_ID", empFinSerInfo.getFlatId());
		}
		
		/* By Start Date */
		if(Util.isNotEmptyObject(empFinSerInfo.getStartDate())) {
			sqlQuery.append(" AND FTE.TRANSACTION_CLOSED_DATE>=TRUNC(:START_DATE) ");
			namedParameters.addValue("START_DATE", empFinSerInfo.getStartDate());
		}
		
		/* By End Date */
		if(Util.isNotEmptyObject(empFinSerInfo.getEndDate())) {
			sqlQuery.append(" AND TRUNC(FTE.TRANSACTION_CLOSED_DATE)<=:END_DATE");
			namedParameters.addValue("END_DATE", empFinSerInfo.getEndDate());
		}
		/* For Order */
		sqlQuery.append(" ORDER BY FTE.TRANSACTION_CLOSED_DATE DESC ");
		
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus());
		namedParameters.addValue("SET_OFF_FIN_PENALTY_TYPE", MetadataId.FIN_PENALTY.getId());
		namedParameters.addValue("SET_OFF_INTEREST_WAIVER_TYPE", MetadataId.INTEREST_WAIVER.getId());
		namedParameters.addValue("FIN_TRANSACTION_RECEIPT_TYPE_ID", FinTransactionType.RECEIPT.getId());
		namedParameters.addValue("FIN_TRANSACTION_INTEREST_WAIVER_TYPE_ID", FinTransactionType.INTEREST_WAIVER.getId());
		namedParameters.addValue("TRANSACTION_STATUS_IDS", Arrays.asList(Status.TRANSACTION_COMPLETED.getStatus()));
		namedParameters.addValue("SET_OFF_TYPE", Arrays.asList(MetadataId.FIN_PENALTY.getId(),MetadataId.INTEREST_WAIVER.getId()));
		namedParameters.addValue("FIN_TRANSACTION_TYPE_ID", Arrays.asList(FinTransactionType.RECEIPT.getId(),FinTransactionType.INTEREST_WAIVER.getId()));
		
		return getData(sqlQuery.toString(), namedParameters,FinTransactionSetOffPojo.class);
	}

	@Override
	public List<FinBookingFormAccountsPojo> getAllCustomersInvoices(EmployeeFinancialTransactionServiceInfo empFinTranSerInfo) {
		log.info("*** The Control is inside the getAllCustomersInvoices in EmployeeFinancialServiceDaoImpl ***"+empFinTranSerInfo);
		StringBuilder sqlQuery = new StringBuilder(FinancialQuerys.QRY_TO_GET_ALL_CUSTOMERS_INVOICES_DETAILS);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		/* Getting details based on Site Id */
		if(Util.isNotEmptyObject(empFinTranSerInfo.getSiteIds())) {
			sqlQuery.append(" AND ST.SITE_ID IN (:SITE_IDS) ");
			namedParameters.addValue("SITE_IDS", empFinTranSerInfo.getSiteIds());
		}
		/* Getting details based on Block Id */
		if(Util.isNotEmptyObject(empFinTranSerInfo.getBlockIds())) {
			sqlQuery.append(" AND BLD.BLOCK_ID IN (:BLOCK_IDS) ");
			namedParameters.addValue("BLOCK_IDS", empFinTranSerInfo.getBlockIds());
		}
		/* Getting details based on Floor Id */
		if(Util.isNotEmptyObject(empFinTranSerInfo.getFloorIds())) {
			sqlQuery.append(" AND FLD.FLOOR_ID IN (:FLOOR_IDS) ");
			namedParameters.addValue("FLOOR_IDS", empFinTranSerInfo.getFloorIds());
		}
		/* Getting details based on Flat Id */
		if(Util.isNotEmptyObject(empFinTranSerInfo.getFlatIds())) {
			sqlQuery.append(" AND FT.FLAT_ID IN (:FLAT_IDS) ");
			namedParameters.addValue("FLAT_IDS", empFinTranSerInfo.getFlatIds());
		}
		/* Getting details based on Invoice Type */
		if(Util.isNotEmptyObject(empFinTranSerInfo.getInvoiceType())) {
			namedParameters.addValue("TYPE", Arrays.asList(empFinTranSerInfo.getInvoiceType()));
		}else {
			if(Department.CRM_SALES.getId().equals(empFinTranSerInfo.getDeptId())) {
				namedParameters.addValue("TYPE", Arrays.asList(MetadataId.LEGAL_COST.getId()));
			}else if(Department.CRM_TECH.getId().equals(empFinTranSerInfo.getDeptId())) {
				namedParameters.addValue("TYPE", Arrays.asList(MetadataId.MODIFICATION_COST.getId()));
			}else {
				namedParameters.addValue("TYPE",  Arrays.asList(MetadataId.LEGAL_COST.getId(),MetadataId.MODIFICATION_COST.getId()));
			}
		}
		/* Getting details based on Invoice Created Date */
		if(Util.isNotEmptyObject(empFinTranSerInfo.getFromDate())) {
			sqlQuery.append(" AND FBFA.CREATED_DATE>=TRUNC(:FROM_DATE) ");
			namedParameters.addValue("FROM_DATE", empFinTranSerInfo.getFromDate());
		}
		if(Util.isNotEmptyObject(empFinTranSerInfo.getToDate())) {
			sqlQuery.append(" AND TRUNC(FBFA.CREATED_DATE)<=:TO_DATE ");
			namedParameters.addValue("TO_DATE", empFinTranSerInfo.getToDate());
		}
		sqlQuery.append(" ORDER BY FBFA.CREATED_DATE DESC ");
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.VARCHAR);
		namedParameters.addValue("PAYMENT_STATUS",Arrays.asList(new Long[] { Status.PENDING.getStatus(),Status.INPROGRESS.getStatus(),Status.PAID.getStatus()}));
		List<FinBookingFormAccountsPojo> finBookingFormAccountsInvoicesPojoList  = (List<FinBookingFormAccountsPojo>) getData(sqlQuery.toString(),namedParameters,FinBookingFormAccountsPojo.class);
		return finBookingFormAccountsInvoicesPojoList;
	}

	@Override
	public void checkEmployeeApprovalMappingAccess() {
		//String str = "select FIN_TRAN_SET_OFF_LEVEL_ID,FIN_LEVEL_ID,SITE_ID,FIN_TRANSACTION_TYPE_ID from FIN_TRANSACTION_SET_OFF_LEVEL where FIN_TRANSACTION_TYPE_ID IN (1) and FIN_LEVEL_ID IN (2) ORDER BY FIN_TRAN_SET_OFF_LEVEL_ID";
		String str = "SELECT FIN_TRAN_SET_OFF_LEVEL_ID,FIN_LEVEL_ID,SITE_ID,FIN_TRANSACTION_TYPE_ID FROM FIN_TRANSACTION_SET_OFF_LEVEL ";
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		
		List<Map<String, Object>> ListOfMappings = nmdPJdbcTemplate.queryForList(str, namedParameters);
		List<Map<String, Object>> ListOfMappingsMis = null;
		System.out.println(ListOfMappings);
		List<Long> EmployeeList = null;
		Long empId = 0l;
		for (Map<String, Object> map : ListOfMappings) {
			EmployeeList = new ArrayList<>();
			String FIN_TRAN_SET_OFF_LEVEL_ID = map.get("FIN_TRAN_SET_OFF_LEVEL_ID").toString();
			String FIN_LEVEL_ID = map.get("FIN_LEVEL_ID").toString();
			String SITE_ID = map.get("SITE_ID").toString();
			//String FIN_TRANSACTION_TYPE_ID = map.get("FIN_TRANSACTION_TYPE_ID").toString();
			
			 str = "SELECT * FROM FIN_TRN_EMP_SET_OFF_LEVEL_MAPPING WHERE STATUS_ID = 6 AND FIN_TRAN_SET_OFF_LEVEL_ID  = "+FIN_TRAN_SET_OFF_LEVEL_ID;
			 //str = "SELECT * FROM FIN_TRN_SET_OFF_APPR_TYPE WHERE FIN_TRAN_SET_OFF_LEVEL_ID  = "+FIN_TRAN_SET_OFF_LEVEL_ID+" AND SET_OFF_TYPE IN(102,103,104) ";
			 ListOfMappingsMis = nmdPJdbcTemplate.queryForList(str, namedParameters);
			 if(Util.isNotEmptyObject(ListOfMappingsMis)) {// && FIN_LEVEL_ID.equals("2") && FIN_TRANSACTION_TYPE_ID.equals("1")
	
				 for (Map<String, Object> setOffMap : ListOfMappingsMis) {
					 empId =  setOffMap.get("EMP_ID")==null?0l:Long.valueOf(setOffMap.get("EMP_ID").toString());
					 if(EmployeeList.contains(empId)) {
						 System.out.println(FIN_TRAN_SET_OFF_LEVEL_ID+" "+FIN_LEVEL_ID+" "+SITE_ID+" "+EmployeeList);
						 EmployeeList.add(empId);
						 throw new RuntimeException("Exception "+EmployeeList);
					 } else {
						 EmployeeList.add(empId);
					 }
				 }
				 System.out.println(FIN_TRAN_SET_OFF_LEVEL_ID+" "+FIN_LEVEL_ID+" "+SITE_ID+" "+EmployeeList);
				 
			 }	 
		}
	
	}
	@Override
	public void checkDuplicateApprovalMappingAccess() {
		//String str = "select FIN_TRAN_SET_OFF_LEVEL_ID,FIN_LEVEL_ID,SITE_ID,FIN_TRANSACTION_TYPE_ID from FIN_TRANSACTION_SET_OFF_LEVEL where FIN_TRANSACTION_TYPE_ID IN (1) and FIN_LEVEL_ID IN (2) ORDER BY FIN_TRAN_SET_OFF_LEVEL_ID";
		String str = "SELECT FIN_TRAN_SET_OFF_LEVEL_ID,FIN_LEVEL_ID,SITE_ID,FIN_TRANSACTION_TYPE_ID FROM FIN_TRANSACTION_SET_OFF_LEVEL WHERE FIN_LEVEL_ID IN (1,2) ";
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		
		List<Map<String, Object>> ListOfMappings = nmdPJdbcTemplate.queryForList(str, namedParameters);
		List<Map<String, Object>> ListOfMappingsMis = null;
		System.out.println(ListOfMappings);
		List<Long> setOffList = null;
		Long setOffType = 0l;
		for (Map<String, Object> map : ListOfMappings) {
			setOffList = new ArrayList<>();
			String FIN_TRAN_SET_OFF_LEVEL_ID = map.get("FIN_TRAN_SET_OFF_LEVEL_ID").toString();
			String FIN_LEVEL_ID = map.get("FIN_LEVEL_ID").toString();
			String SITE_ID = map.get("SITE_ID").toString();
			//String FIN_TRANSACTION_TYPE_ID = map.get("FIN_TRANSACTION_TYPE_ID").toString();
			
			 str = "SELECT * FROM FIN_TRN_SET_OFF_APPR_TYPE WHERE STATUS_ID = 6 AND FIN_TRAN_SET_OFF_LEVEL_ID  = "+FIN_TRAN_SET_OFF_LEVEL_ID;
			 //str = "SELECT * FROM FIN_TRN_SET_OFF_APPR_TYPE WHERE FIN_TRAN_SET_OFF_LEVEL_ID  = "+FIN_TRAN_SET_OFF_LEVEL_ID+" AND SET_OFF_TYPE IN(102,103,104) ";
			 ListOfMappingsMis = nmdPJdbcTemplate.queryForList(str, namedParameters);
			 if(Util.isNotEmptyObject(ListOfMappingsMis)) {// && FIN_LEVEL_ID.equals("2") && FIN_TRANSACTION_TYPE_ID.equals("1")
	
				 for (Map<String, Object> setOffMap : ListOfMappingsMis) {
					 setOffType =  setOffMap.get("SET_OFF_TYPE")==null?0l:Long.valueOf(setOffMap.get("SET_OFF_TYPE").toString());
					 if(setOffList.contains(setOffType)) {
						 System.out.println(FIN_TRAN_SET_OFF_LEVEL_ID+" "+FIN_LEVEL_ID+" "+SITE_ID+" "+setOffList);
						 setOffList.add(setOffType);
						 throw new RuntimeException("Exception "+setOffList);
					 } else {
						 setOffList.add(setOffType);
					 }
				 }
				 System.out.println(FIN_TRAN_SET_OFF_LEVEL_ID+" "+FIN_LEVEL_ID+" "+SITE_ID+" "+setOffList);
				 
			 }	 
		}
	}
	
	@Override
	public void insertMappingAccess() {
		//String str = "select FIN_TRAN_SET_OFF_LEVEL_ID,FIN_LEVEL_ID,SITE_ID,FIN_TRANSACTION_TYPE_ID from FIN_TRANSACTION_SET_OFF_LEVEL where FIN_TRANSACTION_TYPE_ID IN (1) and FIN_LEVEL_ID IN (2) ORDER BY FIN_TRAN_SET_OFF_LEVEL_ID";
		String str = "select FIN_TRAN_SET_OFF_LEVEL_ID,FIN_LEVEL_ID,SITE_ID,FIN_TRANSACTION_TYPE_ID from FIN_TRANSACTION_SET_OFF_LEVEL where FIN_TRANSACTION_TYPE_ID IN (2) and FIN_LEVEL_ID IN (1) ORDER BY FIN_TRAN_SET_OFF_LEVEL_ID";
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		
		List<Map<String, Object>> ListOfMappings = nmdPJdbcTemplate.queryForList(str, namedParameters);
		List<Map<String, Object>> ListOfMappingsMis = null;
		System.out.println(ListOfMappings);
		for (Map<String, Object> map : ListOfMappings) {
			String FIN_TRAN_SET_OFF_LEVEL_ID = map.get("FIN_TRAN_SET_OFF_LEVEL_ID").toString();
			String FIN_LEVEL_ID = map.get("FIN_LEVEL_ID").toString();
			String SITE_ID = map.get("SITE_ID").toString();
			//String FIN_TRANSACTION_TYPE_ID = map.get("FIN_TRANSACTION_TYPE_ID").toString();
			System.out.println(FIN_TRAN_SET_OFF_LEVEL_ID+" "+FIN_LEVEL_ID+" "+SITE_ID);
			 str = "SELECT * FROM FIN_TRN_SET_OFF_APPR_TYPE WHERE FIN_TRAN_SET_OFF_LEVEL_ID  = "+FIN_TRAN_SET_OFF_LEVEL_ID;
			 List<Map<String, Object>> ListOfMappingsExist  = nmdPJdbcTemplate.queryForList(str, namedParameters);
			 str = "SELECT * FROM FIN_TRN_SET_OFF_APPR_TYPE WHERE FIN_TRAN_SET_OFF_LEVEL_ID  = "+FIN_TRAN_SET_OFF_LEVEL_ID+" AND SET_OFF_TYPE IN (102,103,104) ";
			 ListOfMappingsMis = nmdPJdbcTemplate.queryForList(str, namedParameters);
			 if(Util.isEmptyObject(ListOfMappingsMis) && Util.isNotEmptyObject(ListOfMappingsExist)) {// && FIN_LEVEL_ID.equals("2") && FIN_TRANSACTION_TYPE_ID.equals("1")
				 /*nmdPJdbcTemplate.update("INSERT INTO FIN_TRN_SET_OFF_APPR_TYPE (FIN_TRN_SET_OFF_APPR_TYPE_ID, SET_OFF_TYPE, FIN_TRAN_SET_OFF_LEVEL_ID,STATUS_ID,CREATED_DATE) VALUES (FIN_TRN_SET_OFF_APPR_TYPE_SEQ.NEXTVAL, '33', '"+FIN_TRAN_SET_OFF_LEVEL_ID+"', '6',SYSDATE)", namedParameters);
				 nmdPJdbcTemplate.update("INSERT INTO FIN_TRN_SET_OFF_APPR_TYPE (FIN_TRN_SET_OFF_APPR_TYPE_ID, SET_OFF_TYPE, FIN_TRAN_SET_OFF_LEVEL_ID,STATUS_ID,CREATED_DATE) VALUES (FIN_TRN_SET_OFF_APPR_TYPE_SEQ.NEXTVAL, '29', '"+FIN_TRAN_SET_OFF_LEVEL_ID+"', '6',SYSDATE)", namedParameters);
				 //--INSERT INTO FIN_TRN_SET_OFF_APPR_TYPE (FIN_TRN_SET_OFF_APPR_TYPE_ID, SET_OFF_TYPE, FIN_TRAN_SET_OFF_LEVEL_ID,STATUS_ID,CREATED_DATE) VALUES (FIN_TRN_SET_OFF_APPR_TYPE_SEQ.NEXTVAL, '34', '"+FIN_TRAN_SET_OFF_LEVEL_ID+"', '6',SYSDATE);
				 nmdPJdbcTemplate.update(" INSERT INTO FIN_TRN_SET_OFF_APPR_TYPE (FIN_TRN_SET_OFF_APPR_TYPE_ID, SET_OFF_TYPE, FIN_TRAN_SET_OFF_LEVEL_ID,STATUS_ID,CREATED_DATE) VALUES (FIN_TRN_SET_OFF_APPR_TYPE_SEQ.NEXTVAL, '35', '"+FIN_TRAN_SET_OFF_LEVEL_ID+"', '6',SYSDATE)", namedParameters);
				 nmdPJdbcTemplate.update(" INSERT INTO FIN_TRN_SET_OFF_APPR_TYPE (FIN_TRN_SET_OFF_APPR_TYPE_ID, SET_OFF_TYPE, FIN_TRAN_SET_OFF_LEVEL_ID,STATUS_ID,CREATED_DATE) VALUES (FIN_TRN_SET_OFF_APPR_TYPE_SEQ.NEXTVAL, '62', '"+FIN_TRAN_SET_OFF_LEVEL_ID+"', '6',SYSDATE)", namedParameters);
				 nmdPJdbcTemplate.update(" INSERT INTO FIN_TRN_SET_OFF_APPR_TYPE (FIN_TRN_SET_OFF_APPR_TYPE_ID, SET_OFF_TYPE, FIN_TRAN_SET_OFF_LEVEL_ID,STATUS_ID,CREATED_DATE) VALUES (FIN_TRN_SET_OFF_APPR_TYPE_SEQ.NEXTVAL, '27', '"+FIN_TRAN_SET_OFF_LEVEL_ID+"', '6',SYSDATE)", namedParameters);*/
				 
				 nmdPJdbcTemplate.update(" INSERT INTO FIN_TRN_SET_OFF_APPR_TYPE (FIN_TRN_SET_OFF_APPR_TYPE_ID, SET_OFF_TYPE, FIN_TRAN_SET_OFF_LEVEL_ID,STATUS_ID,CREATED_DATE) VALUES (FIN_TRN_SET_OFF_APPR_TYPE_SEQ.NEXTVAL, '102', '"+FIN_TRAN_SET_OFF_LEVEL_ID+"', '6',SYSDATE)", namedParameters);
				 nmdPJdbcTemplate.update(" INSERT INTO FIN_TRN_SET_OFF_APPR_TYPE (FIN_TRN_SET_OFF_APPR_TYPE_ID, SET_OFF_TYPE, FIN_TRAN_SET_OFF_LEVEL_ID,STATUS_ID,CREATED_DATE) VALUES (FIN_TRN_SET_OFF_APPR_TYPE_SEQ.NEXTVAL, '103', '"+FIN_TRAN_SET_OFF_LEVEL_ID+"', '6',SYSDATE)", namedParameters);
				 nmdPJdbcTemplate.update(" INSERT INTO FIN_TRN_SET_OFF_APPR_TYPE (FIN_TRN_SET_OFF_APPR_TYPE_ID, SET_OFF_TYPE, FIN_TRAN_SET_OFF_LEVEL_ID,STATUS_ID,CREATED_DATE) VALUES (FIN_TRN_SET_OFF_APPR_TYPE_SEQ.NEXTVAL, '104', '"+FIN_TRAN_SET_OFF_LEVEL_ID+"', '6',SYSDATE)", namedParameters);
				 
				 
				 //break;
			 }	 
		}
	}
	
	
	@Override
	public FinBookingFormMaintenancePojo saveMaintenanceCharges(FinBookingFormMaintenancePojo bookingFormMaintenancePojo) {
		String query = FinancialQuerys.QRY_TO_INSERT_MAINTENANANCE_CHARGES;
		Long pk = commonMethodToInsertData(query, bookingFormMaintenancePojo,"FIN_BOK_FRM_MAINTENANCE_ID");
		bookingFormMaintenancePojo.setFinBokFrmMaintenanceId(pk);
		return bookingFormMaintenancePojo;
	}
	
	@Override
	public FinBookingFormMaintenanceDtlsPojo saveMaintenanceChargesDetails(FinBookingFormMaintenanceDtlsPojo bookingFormMaintenanceDtlsPojo) {
		String query = FinancialQuerys.QRY_TO_INSERT_MAINTENANANCE_CHARGES_DETAILS;
		Long pk = commonMethodToInsertData(query, bookingFormMaintenanceDtlsPojo,"FIN_BOK_FORM_MAINTENANCE_DTLS_ID");
		bookingFormMaintenanceDtlsPojo.setFinBokFormMaintenanceDtlsId(pk);
		return bookingFormMaintenanceDtlsPojo;
	}
	
	@Override
	public FinBokFrmMaintenanceTaxPojo saveMaintenanceChargesTaxData(FinBokFrmMaintenanceTaxPojo bokFrmMaintenanceTaxPojo) {
		String query = FinancialQuerys.QRY_TO_INSERT_MAINTENANANCE_CHARGES_TAX;
		Long pk = commonMethodToInsertData(query, bokFrmMaintenanceTaxPojo,"FIN_BOK_FRM_MAINTENANCE_TAX_ID");
		bokFrmMaintenanceTaxPojo.setFinBokFormMaintenanceDtlsId(pk);
		return bokFrmMaintenanceTaxPojo;
	}

	@Override
	public FinBookingFormFlatKhataPojo saveFlatKhata(FinBookingFormFlatKhataPojo bookingFormMaintenancePojo) {
		String query = FinancialQuerys.QRY_TO_INSERT_FLAT_KHATA_CHARGES;
		Long pk = commonMethodToInsertData(query, bookingFormMaintenancePojo,"FIN_BOK_FRM_FLAT_KHATA_ID");
		bookingFormMaintenancePojo.setFinBokFrmFlatKhataId(pk);
		return bookingFormMaintenancePojo;
	}
	
	@Override
	public FinBookingFormFlatKhataDtlsPojo saveFlatKhataDetails(
			FinBookingFormFlatKhataDtlsPojo bookingFormMaintenanceDtlsPojo) {
		String query = FinancialQuerys.QRY_TO_INSERT_FLAT_KHATA_CHARGES_DETAILS;
		Long pk = commonMethodToInsertData(query, bookingFormMaintenanceDtlsPojo,"FIN_BOK_FORM_FLAT_KHATA_DTLS_ID");
		bookingFormMaintenanceDtlsPojo.setFinBokFormFlatKhataDetailsId(pk);
		return bookingFormMaintenanceDtlsPojo;
	}
	
	@Override
	public FinBokFrmFlatKhataTaxPojo saveFlatKhataTaxData(FinBokFrmFlatKhataTaxPojo bokFrmMaintenanceTaxPojo) {
		String query = FinancialQuerys.QRY_TO_INSERT_FLAT_KHATA_CHARGES_TAX;
		Long pk = commonMethodToInsertData(query, bokFrmMaintenanceTaxPojo,"FIN_BOK_FRM_FLAT_KHATA_TAX_ID");
		bokFrmMaintenanceTaxPojo.setFinBokFrmFlatKhataTaxId(pk);
		return bokFrmMaintenanceTaxPojo;
	}
	
	@Override
	public FinBookingFormCorpusFundPojo saveCorpusFund(FinBookingFormCorpusFundPojo bookingFormMaintenancePojo) {
		String query = FinancialQuerys.QRY_TO_INSERT_CORPUS_FUND_CHARGES;
		Long pk = commonMethodToInsertData(query, bookingFormMaintenancePojo,"FIN_BOK_FRM_CORPUS_FUND_ID");
		bookingFormMaintenancePojo.setFinBokFrmCorpusFundId(pk);
		return bookingFormMaintenancePojo;
	}	
	
	@Override
	public List<Map<String, Object>> getClearedTransactionReport(EmployeeFinancialTransactionRequest tansactionServiceInfo) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuffer query = new StringBuffer(FinancialQuerys.QRY_TO_CLEARED_TRANSACTION_REPORT);
		
		if(Util.isNotEmptyObject(tansactionServiceInfo.getClearanceFromDate())&&Util.isNotEmptyObject(tansactionServiceInfo.getClearanceToDate())) {
			query.append(" AND TRUNC(NVL(FTC.CLEARANCE_DATE,NVL(FTE.RECEIVED_DATE,FTE.PAYMENT_DATE))) BETWEEN TRUNC(:getClearanceFromDate) AND TRUNC(:getClearanceToDate)");
		} else if (Util.isNotEmptyObject(tansactionServiceInfo.getClearanceFromDate())) {
			query.append(" AND TRUNC(NVL(FTC.CLEARANCE_DATE,NVL(FTE.RECEIVED_DATE,FTE.PAYMENT_DATE)))  >= TRUNC(:getClearanceFromDate) ");
		} else if (Util.isNotEmptyObject(tansactionServiceInfo.getClearanceToDate())) {
			query.append(" AND TRUNC(NVL(FTC.CLEARANCE_DATE,NVL(FTE.RECEIVED_DATE,FTE.PAYMENT_DATE)))  <= TRUNC(:getClearanceToDate)");
		}
		if(Util.isNotEmptyObject(tansactionServiceInfo.getSiteIds())) {
			query.append(" AND FTE.SITE_ID IN (:SITE_IDS)");
			namedParameters.addValue("SITE_IDS",tansactionServiceInfo.getSiteIds(), Types.BIGINT);
		}
		
		if(Util.isNotEmptyObject(tansactionServiceInfo.getStateId())) {
			query.append(" AND SI.STATE_ID IN (:STATE_IDs)");
			namedParameters.addValue("STATE_IDs",tansactionServiceInfo.getStateId(), Types.BIGINT);
		}
		
		
		if("clearedTransactionReport".equals(tansactionServiceInfo.getRequestUrl())&&"reciept".equals(tansactionServiceInfo.getOperationType())) {
			query.append(" AND FTSO.set_off_type IN ( 29 ) ");
			query.append(" and FTE.FIN_TRANSACTION_TYPE_ID in (1) ");
			query.append(" and FTE.FIN_TRANSACTION_MODE_ID in (1,2) ");
		}else if("clearedTransactionReport".equals(tansactionServiceInfo.getRequestUrl()) &&"payment".equals(tansactionServiceInfo.getOperationType())) {
			query.append(" AND FTSO.set_off_type IN ( 29 ) ");
			query.append(" and FTE.FIN_TRANSACTION_TYPE_ID in (2) ");
			query.append(" and FTE.FIN_TRANSACTION_MODE_ID in (1,2) ");
		}
		//query.append(" and trunc(NVL(FTC.CLEARANCE_DATE,NVL(FTE.RECEIVED_DATE,FTE.PAYMENT_DATE))) BETWEEN to_date('01-05-2022','dd-MM-yyyy') AND  to_date('27-05-2022','dd-MM-yyyy') ");
		
		query.append(" GROUP BY FTE.FIN_TRANSACTION_TYPE_ID,FTE.SITE_ID,SI.SALES_FORCE_SITE_NAME,FTE.SITE_ID,FSO.FLAT_SALE_OWNER,FSO.FLATS_SALE_OWNERS_ID ");
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.VARCHAR);
		namedParameters.addValue("TRANSACTION_STATUS_ID", Status.TRANSACTION_COMPLETED.getStatus(), Types.BIGINT);
		namedParameters.addValue("SET_OFF_TYPE",MetadataId.FIN_BOOKING_FORM_MILESTONES.getId(), Types.BIGINT);
		//MetadataId.FIN_BOOKING_FORM_MILESTONES.getId() THIS IS NOTHING BUT PRINCIPAL AMOUNT
		namedParameters.addValue("getClearanceFromDate", tansactionServiceInfo.getClearanceFromDate(),Types.DATE);
		namedParameters.addValue("getClearanceToDate", tansactionServiceInfo.getClearanceToDate(),Types.DATE);
		log.info("getClearedTransactionReport(query,namedParameters) query "+query+" \n"+namedParameters.getValues());
		List<Map<String, Object>> ListOfMappings = nmdPJdbcTemplate.queryForList(query.toString(), namedParameters);
		return ListOfMappings;
	}
	
	@Override
	public List<Map<String, Object>> getPendingTransactionReport(EmployeeFinancialTransactionRequest tansactionServiceInfo) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuffer query = new StringBuffer(FinancialQuerys.QRY_TO_CLEARED_TRANSACTION_REPORT);
		
		if(Util.isNotEmptyObject(tansactionServiceInfo.getReceivedFromDate())&&Util.isNotEmptyObject(tansactionServiceInfo.getReceivedToDate())) {
			query.append(" AND TRUNC(NVL(FTE.RECEIVED_DATE,NVL(FTE.PAYMENT_DATE,FTE.CREATED_DATE)))  BETWEEN TRUNC(:getRecievedFromDate) AND TRUNC(:getRecievedToDate)");
		} else if (Util.isNotEmptyObject(tansactionServiceInfo.getReceivedFromDate())) {
			query.append(" AND TRUNC(NVL(FTE.RECEIVED_DATE,NVL(FTE.PAYMENT_DATE,FTE.CREATED_DATE)))  >= TRUNC(:getRecievedFromDate) ");
		} else if (Util.isNotEmptyObject(tansactionServiceInfo.getReceivedToDate())) {
			query.append(" AND TRUNC(NVL(FTE.RECEIVED_DATE,NVL(FTE.PAYMENT_DATE,FTE.CREATED_DATE)))  <= TRUNC(:getRecievedToDate)");
		}
		if(Util.isNotEmptyObject(tansactionServiceInfo.getSiteIds())) {
			query.append(" AND FTE.SITE_ID IN (:SITE_IDS)");
			namedParameters.addValue("SITE_IDS",tansactionServiceInfo.getSiteIds(), Types.BIGINT);
		}
		if(Util.isNotEmptyObject(tansactionServiceInfo.getStateId())) {
			query.append(" AND SI.STATE_ID IN (:STATE_IDs)");
			namedParameters.addValue("STATE_IDs",tansactionServiceInfo.getStateId(), Types.BIGINT);
		}
		if("pendingTransactionReport".equals(tansactionServiceInfo.getRequestUrl())&&"reciept".equals(tansactionServiceInfo.getOperationType())) {
			query.append(" AND FTSO.set_off_type IN ( 29 ) ");
			query.append(" and FTE.FIN_TRANSACTION_TYPE_ID in (1) ");
			query.append(" and FTE.FIN_TRANSACTION_MODE_ID in (1,2) ");
		}else if("pendingTransactionReport".equals(tansactionServiceInfo.getRequestUrl()) &&"payment".equals(tansactionServiceInfo.getOperationType())) {
			query.append(" AND FTSO.set_off_type IN ( 29 ) ");
			query.append(" and FTE.FIN_TRANSACTION_TYPE_ID in (2) ");
			query.append(" and FTE.FIN_TRANSACTION_MODE_ID in (1,2) ");
		}
		//query.append(" and trunc(NVL(FTC.CLEARANCE_DATE,NVL(FTE.RECEIVED_DATE,FTE.PAYMENT_DATE))) BETWEEN to_date('01-05-2022','dd-MM-yyyy') AND  to_date('27-05-2022','dd-MM-yyyy') ");
		
		query.append(" GROUP BY FTE.FIN_TRANSACTION_TYPE_ID,FTE.SITE_ID,SI.SALES_FORCE_SITE_NAME,FTE.SITE_ID,FSO.FLAT_SALE_OWNER,FSO.FLATS_SALE_OWNERS_ID ");
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.VARCHAR);
		namedParameters.addValue("TRANSACTION_STATUS_ID", Arrays.asList(new Long[] { Status.NEW.getStatus(), Status.MODIFY.getStatus(),Status.PENDING.getStatus(),Status.APPROVED.getStatus(),Status.UNCLEARED_CHEQUE.getStatus() }), Types.BIGINT);
		namedParameters.addValue("SET_OFF_TYPE",MetadataId.FIN_BOOKING_FORM_MILESTONES.getId(), Types.BIGINT);
		//MetadataId.FIN_BOOKING_FORM_MILESTONES.getId() THIS IS NOTHING BUT PRINCIPAL AMOUNT
		namedParameters.addValue("getRecievedFromDate", tansactionServiceInfo.getReceivedFromDate(),Types.DATE);
		namedParameters.addValue("getRecievedToDate", tansactionServiceInfo.getReceivedToDate(),Types.DATE);
		log.info("getPendingTransactionReport(query,namedParameters) query "+query+" \n"+namedParameters.getValues());
		List<Map<String, Object>> ListOfMappings = nmdPJdbcTemplate.queryForList(query.toString(), namedParameters);
		return ListOfMappings;
	}
	
	@Override
	public List<FlatSaleOwnersByAccount> getFlatSaleOwnerByAccountId(EmployeeFinancialServiceInfo employeeFinancialRequest) {
		StringBuilder query = null;
		List<FlatSaleOwnersByAccount>  listOfSaleOwner=null;
		query = new StringBuilder(FinancialQuerys.QRY_TO_GET_FLAT_SALE_OWNERS_BY_ACCOUNT);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	//	namedParameters.addValue("FIN_PROJ_ACC_ID",employeeFinancialRequest.getSiteAccountId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("SITE_ID", employeeFinancialRequest.getSiteId(), Types.BIGINT);
		namedParameters.addValue("FIN_TRANSACTION_ENTRY_ID",employeeFinancialRequest.getTransactionEntryId(), Types.BIGINT);
		//List<Map<String, Object>> ListOfMappings = nmdPJdbcTemplate.queryForList(query.toString(), namedParameters);
	    listOfSaleOwner = getData(query.toString(), namedParameters, FlatSaleOwnersByAccount.class);
	    query=null;
		log.info(listOfSaleOwner);
		return listOfSaleOwner;
	}
	
	
	@Override
	public List<Map<String, Object>> getSuspnesEntryTransactionReport(EmployeeFinancialTransactionRequest tansactionServiceInfo) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuffer query = new StringBuffer(FinancialQuerys.QRY_TO_GET_FLAT_SALE_OWNERS_WISE_SUSPENSE_ENTRY_REPORT);
		
		if(Util.isNotEmptyObject(tansactionServiceInfo.getReceivedFromDate())&&Util.isNotEmptyObject(tansactionServiceInfo.getReceivedToDate())) {
			query.append(" AND TRUNC(NVL(FAE.RECEIVED_DATE,FAE.CREATED_DATE))    BETWEEN TRUNC(:getRecievedFromDate) AND TRUNC(:getRecievedToDate)");
		} else if (Util.isNotEmptyObject(tansactionServiceInfo.getReceivedFromDate())) {
			query.append(" AND TRUNC(NVL(FAE.RECEIVED_DATE,FAE.CREATED_DATE))   >= TRUNC(:getRecievedFromDate) ");
		} else if (Util.isNotEmptyObject(tansactionServiceInfo.getReceivedToDate())) {
			query.append(" AND TRUNC(NVL(FAE.RECEIVED_DATE,FAE.CREATED_DATE))    <= TRUNC(:getRecievedToDate)");
		}
		if(Util.isNotEmptyObject(tansactionServiceInfo.getSiteIds())) {
			query.append(" AND S.SITE_ID IN (:SITE_IDS)");
			namedParameters.addValue("SITE_IDS",tansactionServiceInfo.getSiteIds(), Types.BIGINT);
		}
		if(Util.isNotEmptyObject(tansactionServiceInfo.getStateId())) {
			query.append(" AND S.STATE_ID IN (:STATE_IDs)");
			namedParameters.addValue("STATE_IDs",tansactionServiceInfo.getStateId(), Types.BIGINT);
		}
		
		query.append(" GROUP BY   S.SALES_FORCE_SITE_NAME ,S.SITE_ID, FSO.FLAT_SALE_OWNER,FSO.FLATS_SALE_OWNERS_ID ");
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.VARCHAR);
		namedParameters.addValue("getRecievedFromDate", tansactionServiceInfo.getReceivedFromDate(),Types.DATE);
		namedParameters.addValue("getRecievedToDate", tansactionServiceInfo.getReceivedToDate(),Types.DATE);
		log.info("getSuspnesEntryTransactionReport(query,namedParameters) query "+query+" \n"+namedParameters.getValues());
		List<Map<String, Object>> ListOfMappings = nmdPJdbcTemplate.queryForList(query.toString(), namedParameters);
		return ListOfMappings;
	}
	
	@Override
	public List<FlatCostPojo> getFlatCost(BookingFormRequest bookingFormRequest) {
		// TODO Auto-generated method stub
		log.info("***** The control is inside the getFlatCost in EmployeeFinancialServiceImpl *****");
		String query = new StringBuilder(SqlQuery.QRY_TO_GET_FLAT_COST)
				.append(" WHERE  ")
				//.append(" AND STATUS_ID NOT IN  (:STATUS_ID) ")
				.append("  STATUS_ID IN  (:STATUS_ID) ")
				.append(" AND FLAT_BOOK_ID IN  (:FLAT_BOOK_ID) ")
	             .toString();
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("FLAT_BOOK_ID", bookingFormRequest.getFlatBookingId(),Types.BIGINT);
        namedParameters.addValue("FLAT_ID", bookingFormRequest.getFlatId(),Types.BIGINT);
        //namedParameters.addValue("STATUS_ID", Arrays.asList(Status.REJECTED.getStatus(),Status.INACTIVE.getStatus()), Types.BIGINT);
        namedParameters.addValue("STATUS_ID", Arrays.asList(Status.ACTIVE.getStatus(),Status.PENDING.getStatus()), Types.BIGINT);
        //changed query taking only active and pending flat cost details
        log.info("**** THE QRY_TO_GET_FLAT_COST IS *****"+query);
		
        List<List<FlatCostPojo>> FlatCostPojoLists = nmdPJdbcTemplate.query(query.toString(),namedParameters,
				new RowMapper<List<FlatCostPojo>>() {
					public List<FlatCostPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
						log.info("***** The ResultSet object is ****" + rs);
						final ResultSetMapper<FlatCostPojo> resultSetMapper = new ResultSetMapper<FlatCostPojo>();
						List<FlatCostPojo> FlatCostPojoLIST = null;
						try {
							FlatCostPojoLIST = resultSetMapper.mapRersultSetToObject(rs, FlatCostPojo.class);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
							log.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
							String msg = "The Exception is raised while mapping resultset object to Pojo";
							throw new ResultSetMappingException(msg,ex.getCause());
						}
						log.info("***** The FlatCostPojoLIST objects  is *****" + FlatCostPojoLIST);		
						return FlatCostPojoLIST;
					}
				});
		
        //logger.info("*** The FlatCostPojoLists is *****"+FlatCostPojoLists);
		if(FlatCostPojoLists.isEmpty()) {
			FlatCostPojoLists.add(new ArrayList<FlatCostPojo>());
		}
		log.info("**** The FlatCostPojoLists is ****"+FlatCostPojoLists.get(0));
		return FlatCostPojoLists.get(0);
	}
	
	
	@Override
	public List<FinancialDetailsPojo> getFinancialDetails(@NonNull CustomerInfo customerInfo,Status status) {
		log.info("***** The control is inside the getFinancialDetails in EmployeeFinancialServiceImpl *****");
		String query = new StringBuilder(FinancialQuerys.QRY_TO_GET_FINANCIAL_DETAILS).toString();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FLAT_BOOK_ID", customerInfo.getFlatBookingId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", status.getStatus(), Types.BIGINT);
		namedParameters.addValue("SITE_ID", customerInfo.getSiteId(), Types.BIGINT);
	   log.info("**** THE QRY_TO_GET_FINANCIAL_DETAILS IS *****"+query+"\n Params : "+namedParameters.getValues());

	    List<FinancialDetailsPojo> financialDetailsPojoLists=null;
		try {
			financialDetailsPojoLists = getData(query.toString(),namedParameters,FinancialDetailsPojo.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		log.debug("**** The financialDetailsPojoLists is ****"+financialDetailsPojoLists);
		namedParameters=null;
		query=null;
		return financialDetailsPojoLists;
 }
	@Override
	public List<CustomerPropertyDetailsPojo> getCustomerPropertyDetails(CustomerInfo customerInfo, Status status) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		
		StringBuilder querySB = new StringBuilder(FinancialQuerys.QRY_TO_GET_CUSTOMER_PROPERTY_DETAILS)
				.append(" WHERE FB.FLAT_BOOK_ID = :FLAT_BOOK_ID ");
				if(status!=null)
				{
					querySB.append(" AND  FB.STATUS_ID = :STATUS_ID");
					namedParameters.addValue("STATUS_ID",status.getStatus(),Types.BIGINT);
				}
	            
		namedParameters.addValue("FLAT_BOOK_ID",customerInfo.getFlatBookingId(), Types.BIGINT);
		//logger.info("**** THE QRY_TO_GET_CUSTOMER_PROPERTY_DETAILS IS *****"+query+"\nParams :"+namedParameters.getValues());
		 List<List<CustomerPropertyDetailsPojo>> customerPropertyDetailsPojoLists = nmdPJdbcTemplate.query(querySB.toString(),namedParameters,
					new RowMapper<List<CustomerPropertyDetailsPojo>>() {
						public List<CustomerPropertyDetailsPojo> mapRow(ResultSet rs, int arg1) throws SQLException {
							log.info("***** The ResultSet object is ****" + rs);
							final ResultSetMapper<CustomerPropertyDetailsPojo> resultSetMapper = new ResultSetMapper<CustomerPropertyDetailsPojo>();
							List<CustomerPropertyDetailsPojo> customerPropertyDetailsPojoLIST = null;
							try {
								customerPropertyDetailsPojoLIST = resultSetMapper.mapRersultSetToObject(rs, CustomerPropertyDetailsPojo.class);
							} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
								log.error("**** The Exception is raised while Mapping Resultset object to the PersistenDTO object ****");
								String msg = "The Exception is raised while mapping resultset object to Pojo";
								throw new ResultSetMappingException(msg,ex.getCause());
							}
							//logger.info("***** The customerPropertyDetailsPojoLIST objects  is *****" + customerPropertyDetailsPojoLIST);		
							return customerPropertyDetailsPojoLIST;
						}
					});
		
		if(customerPropertyDetailsPojoLists.isEmpty()) {
			customerPropertyDetailsPojoLists.add(new ArrayList<CustomerPropertyDetailsPojo>());
		}
		return customerPropertyDetailsPojoLists.get(0);
	}
	@Override
	public List<CustomerPropertyDetailsPojo> getCustomerAllActivePropertyDetails(CustomerInfo customerInfo, Status status) {
		log.info("***** The control is inside the getCustomerAllActivePropertyDetails in EmployeeFinancialServiceImpl *****");
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		
		StringBuilder querySB = new StringBuilder(FinancialQuerys.QRY_TO_GET_APP_REGISTERED_CUSTOMER_PROPERTY_DETAILS);
		querySB.append(" WHERE  FB.STATUS_ID = :STATUS_ID");
		namedParameters.addValue("STATUS_ID", status.getStatus(), Types.BIGINT);
		if(Util.isNotEmptyObject(customerInfo.getSiteId())) {
			querySB.append(" AND S.SITE_ID IN (:SITE_ID)");
			namedParameters.addValue("SITE_ID",customerInfo.getSiteId(), Types.BIGINT);
		}
		if (Util.isNotEmptyObject(customerInfo.getFlatBookingId())) {
			querySB.append(" AND FB.FLAT_BOOK_ID = :FLAT_BOOK_ID ");
			namedParameters.addValue("FLAT_BOOK_ID",customerInfo.getFlatBookingId(), Types.BIGINT);
//			querySB.append(" and  FB.STATUS_ID = :STATUS_ID");
//			namedParameters.addValue("STATUS_ID", status.getStatus(), Types.BIGINT);
		}
		namedParameters.addValue("AR_STATUS_ID",Status.ACTIVE.getStatus(), Types.BIGINT);
		log.info("**** THE QRY_TO_GET_CUSTOMER_PROPERTY_DETAILS IS *****"+querySB+"\nParams :"+namedParameters.getValues());
		 List<CustomerPropertyDetailsPojo> customerPropertyDetailsPojoLists=null;
		try {
			customerPropertyDetailsPojoLists = getData(querySB.toString(),namedParameters,CustomerPropertyDetailsPojo.class);
		} catch (Exception e) {
			log.error("exception :",e);
			e.printStackTrace();
		}
		namedParameters=null;
		querySB=null;
		 log.info("***** The control is inside the getCustomerAllActivePropertyDetails in EmployeeFinancialServiceImpl *****"+customerPropertyDetailsPojoLists);		
		return customerPropertyDetailsPojoLists;
	}
	
	
	@Override
	public List<FinClearedUncleareTXPojo> getClearedTransactionReportByAccountNo(EmployeeFinancialTransactionRequest tansactionServiceInfo) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuffer query = new StringBuffer(FinancialQuerys.QRY_TO_GET_CLEARED_TRANSACTIONS_REPORT);
		
		if(Util.isNotEmptyObject(tansactionServiceInfo.getClearanceFromDate())&&Util.isNotEmptyObject(tansactionServiceInfo.getClearanceToDate())) {
			query.append(" AND TRUNC(NVL(FTC.CLEARANCE_DATE,NVL(FTE.RECEIVED_DATE,FTE.PAYMENT_DATE))) BETWEEN TRUNC(:getClearanceFromDate) AND TRUNC(:getClearanceToDate)");
		} else if (Util.isNotEmptyObject(tansactionServiceInfo.getClearanceFromDate())) {
			query.append(" AND TRUNC(NVL(FTC.CLEARANCE_DATE,NVL(FTE.RECEIVED_DATE,FTE.PAYMENT_DATE)))  >= TRUNC(:getClearanceFromDate) ");
		} else if (Util.isNotEmptyObject(tansactionServiceInfo.getClearanceToDate())) {
			query.append(" AND TRUNC(NVL(FTC.CLEARANCE_DATE,NVL(FTE.RECEIVED_DATE,FTE.PAYMENT_DATE)))  <= TRUNC(:getClearanceToDate)");
		}
		if(Util.isNotEmptyObject(tansactionServiceInfo.getSiteIds())) {
			query.append(" AND FTE.SITE_ID IN (:SITE_IDS)");
			namedParameters.addValue("SITE_IDS",tansactionServiceInfo.getSiteIds(), Types.BIGINT);
		}
		if(Util.isNotEmptyObject(tansactionServiceInfo.getBankAccountNumber())) {
			query.append(" AND FPA.ACCOUNT_NO IN (:ACCOUNT_NO)");
			namedParameters.addValue("ACCOUNT_NO",tansactionServiceInfo.getBankAccountNumber(), Types.VARCHAR);
		}
		if(Util.isNotEmptyObject(tansactionServiceInfo.getBankAccountNumbers())) {
			query.append(" AND FPA.ACCOUNT_NO IN (:ACCOUNT_NOs)");
			namedParameters.addValue("ACCOUNT_NOs",tansactionServiceInfo.getBankAccountNumbers(), Types.VARCHAR);
		}
		query.append(" ORDER  BY TX_CLEARANCE_DATE DESC  ");
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.VARCHAR);
		namedParameters.addValue("TRANSACTION_STATUS_ID", Arrays.asList(new Long[] {  Status.TRANSACTION_COMPLETED.getStatus(),Status.NEW.getStatus(), Status.MODIFY.getStatus(),Status.PENDING.getStatus(),Status.APPROVED.getStatus(),Status.UNCLEARED_CHEQUE.getStatus() }), Types.BIGINT);
		namedParameters.addValue("SET_OFF_TYPE",MetadataId.FIN_BOOKING_FORM_MILESTONES.getId(), Types.BIGINT);
		//MetadataId.FIN_BOOKING_FORM_MILESTONES.getId() THIS IS NOTHING BUT PRINCIPAL AMOUNT
		namedParameters.addValue("getClearanceFromDate", tansactionServiceInfo.getClearanceFromDate(),Types.DATE);
		namedParameters.addValue("getClearanceToDate", tansactionServiceInfo.getClearanceToDate(),Types.DATE);
		log.info("getClearedTransactionReportByAccountNo(query,namedParameters) query "+query.toString()+" \n"+namedParameters.getValues());
		List<FinClearedUncleareTXPojo> ListOfMappings = getData(query.toString(), namedParameters, FinClearedUncleareTXPojo.class);
		return ListOfMappings;
	}
	
	@Override
	public List<FinClearedUncleareTXPojo> getPendingTransactionReportByAccountNo(EmployeeFinancialTransactionRequest tansactionServiceInfo) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuffer query = new StringBuffer(FinancialQuerys.QRY_TO_GET_PENDING_TRANSACTIONS_REPORT);
		
		if(Util.isNotEmptyObject(tansactionServiceInfo.getClearanceFromDate())&&Util.isNotEmptyObject(tansactionServiceInfo.getClearanceToDate())) {
			query.append(" AND TRUNC(NVL(FTE.RECEIVED_DATE,NVL(FTE.PAYMENT_DATE,FTE.CREATED_DATE)))  BETWEEN TRUNC(:getClearanceFromDate) AND TRUNC(:getClearanceToDate)");
		} else if (Util.isNotEmptyObject(tansactionServiceInfo.getClearanceFromDate())) {
			query.append(" AND TRUNC(NVL(FTE.RECEIVED_DATE,NVL(FTE.PAYMENT_DATE,FTE.CREATED_DATE)))   >= TRUNC(:getClearanceFromDate) ");
		} else if (Util.isNotEmptyObject(tansactionServiceInfo.getClearanceToDate())) {
			query.append(" AND TRUNC(NVL(FTE.RECEIVED_DATE,NVL(FTE.PAYMENT_DATE,FTE.CREATED_DATE)))  <= TRUNC(:getClearanceToDate)");
		}
		if(Util.isNotEmptyObject(tansactionServiceInfo.getSiteIds())) {
			query.append(" AND FTE.SITE_ID IN (:SITE_IDS)");
			namedParameters.addValue("SITE_IDS",tansactionServiceInfo.getSiteIds(), Types.BIGINT);
		}
		if(Util.isNotEmptyObject(tansactionServiceInfo.getBankAccountNumber())) {
			query.append(" AND FPA.ACCOUNT_NO IN (:ACCOUNT_NO)");
			namedParameters.addValue("ACCOUNT_NO",tansactionServiceInfo.getBankAccountNumber(), Types.VARCHAR);
		}
		if(Util.isNotEmptyObject(tansactionServiceInfo.getBankAccountNumbers())) {
			query.append(" AND FPA.ACCOUNT_NO IN (:ACCOUNT_NOs)");
			namedParameters.addValue("ACCOUNT_NOs",tansactionServiceInfo.getBankAccountNumbers(), Types.VARCHAR);
		}
		query.append(" ORDER  BY TX_CLEARANCE_DATE DESC ");
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.VARCHAR);
		namedParameters.addValue("TRANSACTION_STATUS_ID", Arrays.asList(new Long[] { Status.NEW.getStatus(), Status.MODIFY.getStatus(),Status.PENDING.getStatus(),Status.APPROVED.getStatus(),Status.UNCLEARED_CHEQUE.getStatus() }), Types.BIGINT);
		namedParameters.addValue("SET_OFF_TYPE",MetadataId.FIN_BOOKING_FORM_MILESTONES.getId(), Types.BIGINT);
		//MetadataId.FIN_BOOKING_FORM_MILESTONES.getId() THIS IS NOTHING BUT PRINCIPAL AMOUNT
		namedParameters.addValue("getClearanceFromDate", tansactionServiceInfo.getClearanceFromDate(),Types.DATE);
		namedParameters.addValue("getClearanceToDate", tansactionServiceInfo.getClearanceToDate(),Types.DATE);
		log.info("getClearedTransactionReportByAccountNo(query,namedParameters) query "+query+" \n"+namedParameters.getValues());
		List<FinClearedUncleareTXPojo> ListOfMappings = getData(query.toString(), namedParameters, FinClearedUncleareTXPojo.class);
		return ListOfMappings;
	}
	
	
	@Override
	public List<FinClearedUncleareTXPojo> getSuspnseEntriesTransactionReportByAccountNo(EmployeeFinancialTransactionRequest tansactionServiceInfo) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuffer query = new StringBuffer(FinancialQuerys.QRY_TO_GET_SUSPENSE_ENTRY_TRANSACTION_REPORT);
		
		if(Util.isNotEmptyObject(tansactionServiceInfo.getClearanceFromDate())&&Util.isNotEmptyObject(tansactionServiceInfo.getClearanceToDate())) {
			query.append(" AND TRUNC(NVL(FAE.RECEIVED_DATE,FAE.CREATED_DATE))  BETWEEN TRUNC(:getClearanceFromDate) AND TRUNC(:getClearanceToDate)");
		} else if (Util.isNotEmptyObject(tansactionServiceInfo.getClearanceFromDate())) {
			query.append(" AND TRUNC(NVL(FAE.RECEIVED_DATE,FAE.CREATED_DATE))   >= TRUNC(:getClearanceFromDate) ");
		} else if (Util.isNotEmptyObject(tansactionServiceInfo.getClearanceToDate())) {
			query.append(" AND TRUNC(NVL(FAE.RECEIVED_DATE,FAE.CREATED_DATE))   <= TRUNC(:getClearanceToDate)");
		}
		if(Util.isNotEmptyObject(tansactionServiceInfo.getSiteIds())) {
			query.append(" AND S.SITE_ID IN (:SITE_IDS)");
			namedParameters.addValue("SITE_IDS",tansactionServiceInfo.getSiteIds(), Types.BIGINT);
		}
		if(Util.isNotEmptyObject(tansactionServiceInfo.getBankAccountNumber())) {
			query.append(" AND FPA.ACCOUNT_NO IN (:ACCOUNT_NO)");
			namedParameters.addValue("ACCOUNT_NO",tansactionServiceInfo.getBankAccountNumber(), Types.VARCHAR);
		}
		if(Util.isNotEmptyObject(tansactionServiceInfo.getBankAccountNumbers())) {
			query.append(" AND FPA.ACCOUNT_NO IN (:ACCOUNT_NOs)");
			namedParameters.addValue("ACCOUNT_NOs",tansactionServiceInfo.getBankAccountNumbers(), Types.VARCHAR);
		}
		query.append(" ORDER  BY TX_CLEARANCE_DATE DESC ");
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.VARCHAR);
		namedParameters.addValue("FIN_ANN_STATUS_ID", Arrays.asList(new Long[] { Status.ACTIVE.getStatus(), Status.MODIFY.getStatus() }), Types.BIGINT);
		namedParameters.addValue("SET_OFF_TYPE",MetadataId.FIN_BOOKING_FORM_MILESTONES.getId(), Types.BIGINT);
		//MetadataId.FIN_BOOKING_FORM_MILESTONES.getId() THIS IS NOTHING BUT PRINCIPAL AMOUNT
		namedParameters.addValue("getClearanceFromDate", tansactionServiceInfo.getClearanceFromDate(),Types.DATE);
		namedParameters.addValue("getClearanceToDate", tansactionServiceInfo.getClearanceToDate(),Types.DATE);
		log.info("getClearedTransactionReportByAccountNo(query,namedParameters) query "+query+" \n"+namedParameters.getValues());
		List<FinClearedUncleareTXPojo> ListOfMappings = getData(query.toString(), namedParameters, FinClearedUncleareTXPojo.class);
		return ListOfMappings;
	}
	@Override
	public List<FinProjectAccountPojo> getAccountNumbers(EmployeeFinancialTransactionRequest tansactionServiceInfo) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuffer query = new StringBuffer(FinancialQuerys.QRY_TO_GET_ACCOUNT_NUMBERS);
			if(Util.isNotEmptyObject(tansactionServiceInfo.getSiteIds())) {
				query.append(" AND FSAM.SITE_ID IN (:SITE_IDS)");
				namedParameters.addValue("SITE_IDS",tansactionServiceInfo.getSiteIds(), Types.BIGINT);
			}		
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
	
		List<FinProjectAccountPojo> finProjectAccountPojoLists = (List<FinProjectAccountPojo>) getData(query.toString(), namedParameters, FinProjectAccountPojo.class);
		return finProjectAccountPojoLists;
	}
	
	
	@Override
	public List<StatusPojo> getBookingStatuses(EmployeeFinancialTransactionRequest tansactionServiceInfo) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuffer query = new StringBuffer(FinancialQuerys.QRY_TO_GET_BOOKING_STATUSES);
		List<StatusPojo> finProjectAccountPojoLists = (List<StatusPojo>) getData(query.toString(), namedParameters, StatusPojo.class);
		return finProjectAccountPojoLists;
	}
	

	@Override
	public List<StatePojo> getStates(EmployeeFinancialTransactionRequest tansactionServiceInfo) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		StringBuffer query = new StringBuffer(FinancialQuerys.QRY_TO_GET_STATES);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		List<StatePojo> finProjectAccountPojoLists = (List<StatePojo>) getData(query.toString(), namedParameters, StatePojo.class);
		return finProjectAccountPojoLists;
	}
	
	
	@Override
	public int updateFinTransactionBookingId(FlatBookingPojo oldFlatBookingPojo,FlatBookingPojo newFlatBookingPojo,FinancialDemandNoteMS_TRN_Pojo finTansaction) {
		String query = FinancialQuerys.QRY_TO_UPDATE_FIN_TRNSACTION_ENTRY_BOOKING_ID;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("SITE_ID", oldFlatBookingPojo.getSiteId(), Types.BIGINT);
		namedParameters.addValue("OLD_BOOKING_ID", oldFlatBookingPojo.getFlatBookingId(), Types.BIGINT);
		namedParameters.addValue("NEW_BOOKING_FORM_ID", newFlatBookingPojo.getFlatBookingId(), Types.BIGINT);
		namedParameters.addValue("FIN_TRANSACTION_ENTRY_ID",finTansaction.getTransactionEntryId(), Types.BIGINT);
		int result = nmdPJdbcTemplate.update(query, namedParameters);
		return result;
	}
	
	@Override
	public List<FinancialDemandNoteMS_TRN_Pojo> loadPendingOldBookingTransaction(FlatBookingPojo oldFlatBookingPojo) {
		String sqlQuery = FinancialQuerys.QRY_TO_LOAD_PENDING_OLD_BOOKING_TRANSACTIONS;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("FB_STATUS_ID", oldFlatBookingPojo.getStatusId(), Types.BIGINT);
		namedParameters.addValue("STATUS_ID", Status.ACTIVE.getStatus(), Types.BIGINT);
		namedParameters.addValue("FLAT_NO", oldFlatBookingPojo.getFlatNo(), Types.VARCHAR);
		namedParameters.addValue("FLAT_BOOK_ID", oldFlatBookingPojo.getFlatBookingId(), Types.BIGINT);
		//namedParameters.addValue("FIN_TRANSACTION_TYPE_ID", oldFlatBookingPojo.getFlatBookingId(), Types.BIGINT);
		namedParameters.addValue("FIN_TRANSACTION_TYPE_ID", Arrays.asList(FinTransactionType.RECEIPT.getId(),FinTransactionType.PAYMENT.getId()));
		List<FinancialDemandNoteMS_TRN_Pojo> finTransactionEntryPojoLists = getData(sqlQuery.toString(), namedParameters, FinancialDemandNoteMS_TRN_Pojo.class);
		System.out.println(finTransactionEntryPojoLists);
		return finTransactionEntryPojoLists;
	}
	
	
	@Override
	public int checkDuplicateTransactionOrNot(EmployeeFinancialTransactionRequest tansactionServiceInfo) {
		StringBuffer sqlQuery = new StringBuffer(FinancialQuerys.QRY_TO_CHECK_DUPLICATE_TRANSACTIONS);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		if(Util.isNotEmptyObject(tansactionServiceInfo.getTransactionAmount())) {
			sqlQuery.append(" WHERE FE.AMOUNT IN (:AMOUNT)");
			namedParameters.addValue("AMOUNT",tansactionServiceInfo.getTransactionAmount(), Types.DOUBLE);
		}
		if(Util.isNotEmptyObject(tansactionServiceInfo.getBankId())) {
			sqlQuery.append(" AND FE.FIN_BANK_ID IN (:FIN_BANK_ID)");
			namedParameters.addValue("FIN_BANK_ID",tansactionServiceInfo.getBankId(), Types.BIGINT);
		}
		if(Util.isNotEmptyObject(tansactionServiceInfo.getChequeNumber())) {
			sqlQuery.append(" AND FTC.CHEQUE_NO IN (:CHEQUE_NO)");
			namedParameters.addValue("CHEQUE_NO",tansactionServiceInfo.getChequeNumber().trim(), Types.VARCHAR);
		}
		if(Util.isNotEmptyObject(tansactionServiceInfo.getReferenceNo())) {
			sqlQuery.append(" AND FTO.REFERENCE_NO IN (:REFERENCE_NO)");
			namedParameters.addValue("REFERENCE_NO",tansactionServiceInfo.getReferenceNo().trim(), Types.VARCHAR);
		}
		if (Util.isNotEmptyObject(tansactionServiceInfo.getTransactionDate())) {
			String checDate = DateToWord.convertTimestampToString(tansactionServiceInfo.getTransactionDate());
			sqlQuery.append(" AND TRUNC(CHEQUE_DATE) =  TO_DATE(:CHEQUE_DATE, 'DD-MM-YYYY')  ");
			namedParameters.addValue("CHEQUE_DATE", checDate);
		}
		if(Util.isNotEmptyObject(tansactionServiceInfo.getTransactionReceiveDate())) {
			String checDate=DateToWord.convertTimestampToString(tansactionServiceInfo.getTransactionReceiveDate());
			sqlQuery.append(" AND TRUNC(FE.RECEIVED_DATE) =  TO_DATE(:RECEIVED_DATE, 'DD-MM-YYYY')  ");
			namedParameters.addValue("RECEIVED_DATE", checDate);
		}
		if (Util.isNotEmptyObject(tansactionServiceInfo.getBookingFormId())) {
			sqlQuery.append(" AND FE.BOOKING_FORM_ID = :FLAT_BOOK_ID ");
			namedParameters.addValue("FLAT_BOOK_ID",tansactionServiceInfo.getBookingFormId(), Types.BIGINT);
		}
		sqlQuery.append(" AND FE.TRANSACTION_STATUS_ID NOT IN (45,16,51) ");
		log.info("checkDuplicateTransactionOrNot(query,namedParameters) query "+sqlQuery+" \n"+namedParameters.getValues());
		int count = nmdPJdbcTemplate.queryForObject(sqlQuery.toString(), namedParameters,Integer.class);
		return count;
	}
	
	
	
	@Override
	public Long getLastTXFinTxsetOfAppLevelId(EmployeeFinancialTransactionServiceInfo transactionServiceInfo) {
		StringBuffer sqlQuery = new StringBuffer(FinancialQuerys.QRY_TO_GET_LAST_TX_FIN_TX_SET_OFF_APP_LEVEL_ID);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		if (Util.isNotEmptyObject(transactionServiceInfo.getPrevTransactionEntryId())) {
			namedParameters.addValue("FIN_TRANSACTION_ENTRY_ID",transactionServiceInfo.getPrevTransactionEntryId(), Types.BIGINT);
		}
		log.info("getLastTXFinTxsetOfAppLevelId(query,namedParameters) query "+sqlQuery+" \n"+namedParameters.getValues());
		Long count=null;
		try {
			count = nmdPJdbcTemplate.queryForObject(sqlQuery.toString(), namedParameters,Long.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
}
