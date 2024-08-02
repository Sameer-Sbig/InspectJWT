package com.sbigeneral.LoginCapatchePoc.Controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sbigeneral.LoginCapatchePoc.Entity.ResponseMessage;
import com.sbigeneral.LoginCapatchePoc.Service.DashboardRenewalReportService;
import com.sbigeneral.LoginCapatchePoc.model.BirthdayWiseReportModel;
import com.sbigeneral.LoginCapatchePoc.model.BussinessSummarryReport;
import com.sbigeneral.LoginCapatchePoc.model.DashboardCurrentMonthReport;
import com.sbigeneral.LoginCapatchePoc.model.RenewalPolicyDetails;
import com.sbigeneral.LoginCapatchePoc.model.RenewalRmsModel;
import com.sbigeneral.LoginCapatchePoc.model.RenewalRmsReport;

@RestController
public class DashboardController {
	
	@Autowired
	private DashboardRenewalReportService dashboardService;
	private static final Logger logger=LogManager.getLogger(DashboardController.class);
	
	@GetMapping("/fetchdashboradMonthReport/{agreementCode}")
	public ResponseEntity<?> FetchRmsPolicydetails(@PathVariable String agreementCode,HttpServletRequest request )
	{
		 HttpSession session = request.getSession(false);
	
		   if (session == null || session.getAttribute("agreementCode") == null) {
		try {
			List<DashboardCurrentMonthReport> list;
			
			list= dashboardService.FetchCurrentMonthDashBoardReport(agreementCode);
			return ResponseEntity.status(HttpStatus.ACCEPTED)
					.body(list);
			
		}
		catch (Exception e) {
			e.printStackTrace();
			 logger.error("Internal server error occurred while fetching dashboard month  for renewalRmsModel: {}",  e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseMessage("INTERNAL SERVER ERROR"));
		}
		   }
		   return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseMessage("INTERNAL SERVER ERROR"));
	
		
	}
	@GetMapping("/fetchdashboradSummaryReport/{agreementCode}")
	public ResponseEntity<?> FetchdashboradSummarydetails(@PathVariable String agreementCode )
	{
		
		try {
			List<BussinessSummarryReport> list;
			
			list= dashboardService.FetchdashboradSummaryReport(agreementCode);
			return ResponseEntity.status(HttpStatus.ACCEPTED)
					.body(list);
			
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error("Internal server error occurred while fetching dashboard month  for renewalRmsModel: {}",  e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseMessage("INTERNAL SERVER ERROR"));
		}
	
		
	}
	@GetMapping("/fetchBirthdayWise/{agreementCode}")
	public ResponseEntity<?> FetchBirthdayWisedetails(@PathVariable String agreementCode )
	{
		
		try {
			List<BirthdayWiseReportModel> list;
			
			list= dashboardService.FetchBirthdayWiseReport(agreementCode);
			return ResponseEntity.status(HttpStatus.ACCEPTED)
					.body(list);
			
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error("Internal server error occurred while fetching dashboard month  for renewalRmsModel: {}",  e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseMessage("INTERNAL SERVER ERROR"));
		}
	
		
	}
	@PostMapping(value="/fetchPolicyDetails",consumes="application/x-www-form-urlencoded")
	public ResponseEntity<?> FetchPolicyDetails(@RequestParam String bykey,@RequestParam String byValue, @RequestParam String agent_code)
	{
		  logger.info("Fetching policy details for PolicyNumber: {}", byValue);
		
		try {
			List<RenewalRmsReport> list;
			
			list= dashboardService.FetchPolicyDetails(bykey,byValue,agent_code);
			if(list.size()>0)
			{
				 logger.info("Policy details {}  found in the database", list);
			return ResponseEntity.status(HttpStatus.ACCEPTED)
					.body(list);
			}
			else {
				 logger.info("policy details  {} not found in the database", list);
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ResponseMessage("Policy Details Not found in Our Databse"));
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error("Internal server error occurred while fetching Policy details  for renewalRmsModel: {}",  e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseMessage("INTERNAL SERVER ERROR"));
		}
		
	
		
	}
	
	

}
