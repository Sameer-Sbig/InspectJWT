package com.sbigeneral.LoginCapatchePoc.Service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.sbigeneral.LoginCapatchePoc.model.DocSearchRequest;
import com.sbigeneral.LoginCapatchePoc.model.DocSearchResponse;
import com.sbigeneral.LoginCapatchePoc.model.RenewalRmsModel;
import com.sbigeneral.LoginCapatchePoc.model.RenewalRmsReport;

public interface RenewalRmsReportService {
	
	public List<RenewalRmsReport> fetchRenewalrmsReport(RenewalRmsModel renewalRmsModel);
	public JSONObject downloadRenewalNotice(String renewalNoticeNo,String policyNo,String option, HttpServletResponse response,String userId) ;
	public DocSearchResponse policySearchCall(DocSearchRequest parameters);	
	public JSONObject sendRenewalSms(String mobileNo,String policyNo,String userId);
	JSONObject getPDF(JSONObject policyNumber);
}
