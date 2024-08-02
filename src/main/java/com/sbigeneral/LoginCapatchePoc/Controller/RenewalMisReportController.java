package com.sbigeneral.LoginCapatchePoc.Controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sbigeneral.LoginCapatchePoc.Entity.ResponseMessage;
import com.sbigeneral.LoginCapatchePoc.Service.RenewalMisReportService;
import com.sbigeneral.LoginCapatchePoc.Service.RenewalRmsReportService;
import com.sbigeneral.LoginCapatchePoc.model.RenewalMisReport;
import com.sbigeneral.LoginCapatchePoc.model.RenewalRmsModel;
import com.sbigeneral.LoginCapatchePoc.model.RenewalRmsReport;

@RestController
@PropertySource("classpath:ProductType.properties")
public class RenewalMisReportController {
	
	@Value("${rms.SmeproductType}")
	private String SmeProductType;
	@Value("${rms.HealthproductType}")
	private String HealthProductType;
	@Value("${rms.MotorproductType}")
	private String MotorProductType;
	
	@Autowired
	private RenewalMisReportService renewalRmsReportService;
	private static final Logger logger=LogManager.getLogger(RenewalRmsReportController.class);
	
	@PostMapping("/fetchRenewalReport")
	public ResponseEntity<?> FetchRmsPolicydetails(@RequestBody RenewalRmsModel renewalRmsModel)
	{
		logger.info("Fetching Renewal report details for renewalRmsModel: {}", renewalRmsModel);
		try {
			List<RenewalMisReport> list;
			
			list= renewalRmsReportService.fetchRenewalMisReport(renewalRmsModel);
			logger.info("fetch Renewal  details found for renewalRmsModel: {}", renewalRmsModel);
			return ResponseEntity.status(HttpStatus.ACCEPTED)
					.body(list);
			
		}
		catch (Exception e) {
			e.printStackTrace();
			 logger.error("Internal server error occurred while fetching login details for renewalRmsModel: {}", renewalRmsModel, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseMessage("INTERNAL SERVER ERROR"));
		}
	
		
	}

}
