package com.sbigeneral.LoginCapatchePoc.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sbigeneral.LoginCapatchePoc.model.BirthdayWiseReportModel;
import com.sbigeneral.LoginCapatchePoc.model.BussinessSummarryReport;
import com.sbigeneral.LoginCapatchePoc.model.DashboardCurrentMonthReport;
import com.sbigeneral.LoginCapatchePoc.model.RenewalPolicyDetails;
import com.sbigeneral.LoginCapatchePoc.model.RenewalRmsReport;

public interface DashboardRenewalReportService {
	
	
	public List<DashboardCurrentMonthReport> FetchCurrentMonthDashBoardReport(String agrement);
	public List<BussinessSummarryReport> FetchdashboradSummaryReport(String agrement);
	public List<BirthdayWiseReportModel> FetchBirthdayWiseReport(String agrement);
	public List<RenewalRmsReport> FetchPolicyDetails(String byKey,String byValue,String agent_code);
	

}
