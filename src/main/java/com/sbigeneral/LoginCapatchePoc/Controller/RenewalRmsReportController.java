package com.sbigeneral.LoginCapatchePoc.Controller;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.sbigeneral.LoginCapatchePoc.Entity.ResponseMessage;
import com.sbigeneral.LoginCapatchePoc.Service.RenewalRmsReportService;
import com.sbigeneral.LoginCapatchePoc.model.RenewalRmsModel;
import com.sbigeneral.LoginCapatchePoc.model.RenewalRmsReport;

@Controller
@PropertySource("classpath:ProductType.properties")
public class RenewalRmsReportController {
	
	
	@Value("${rms.SmeproductType}")
	private String SmeProductType;
	@Value("${rms.HealthproductType}")
	private String HealthProductType;
	@Value("${rms.MotorproductType}")
	private String MotorProductType;
	
	@Autowired
	private RenewalRmsReportService renewalRmsReportService;
	private static final Logger logger=LogManager.getLogger(RenewalRmsReportController.class);

	@GetMapping("/productType/{lob}")
	public ResponseEntity<?> GetDisplayproductType(@PathVariable String lob) {
		 logger.info("Get display productType : {}", lob);
		String[] values;
		String json = null;
		try {
			if (lob.equalsIgnoreCase("Health")) {
				values = HealthProductType.split("\\|");

			} else if (lob.equalsIgnoreCase("Sme")) {
				values = SmeProductType.split("\\|");

			} else {
				values = MotorProductType.split("\\|");

			}
			ObjectMapper mapper = new ObjectMapper();
			json = mapper.writeValueAsString(values);
			// return null;
			logger.info("display Product type Details{}", json);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(json);
		} catch (Exception e) {
			e.printStackTrace();
			 logger.error("Internal server error occurred while display  product type details: {}", json, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseMessage("INTERNAL SERVER ERROR"));

		}

	}
	
	@PostMapping("/fetchReport")
	public ResponseEntity<?> FetchRmsPolicydetails(@RequestBody RenewalRmsModel renewalRmsModel,HttpServletRequest request)
	{
		 HttpSession session = request.getSession(false);
			
		   if (session == null || session.getAttribute("agreementCode") == null) {
		logger.info("Fetching Renewal report details for renewalRmsModel: {}", renewalRmsModel);
		try {
			List<RenewalRmsReport> list;
			
			list= renewalRmsReportService.fetchRenewalrmsReport(renewalRmsModel);
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
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ResponseMessage("INTERNAL SERVER ERROR"));
	
		
	}
	/*
	 * @PostMapping("/generateRenewalNote")
	 * 
	 * @ResponseBody public ResponseEntity<byte[]> generateRenewalNote(@RequestBody
	 * String searchParams, HttpServletResponse response) throws Exception { String
	 * message = null; JSONParser jParser = new JSONParser(); JSONObject inJson =
	 * new JSONObject(); JSONObject resJson = new JSONObject(); byte[] pdfContent =
	 * null; HttpHeaders headers = new HttpHeaders(); try { inJson = (JSONObject)
	 * jParser.parse(searchParams); String renewalNoticeNo = (String)
	 * inJson.get("searchValue"); String policyNo = (String)
	 * inJson.get("searchPolicy"); String option = (String)
	 * inJson.get("searchType"); String userId = (String)
	 * inJson.get("agrementcode"); resJson =
	 * renewalRmsReportService.downloadRenewalNotice(renewalNoticeNo,policyNo,option
	 * ,response,userId); String pdfFilePath =
	 * resJson.get("local.uploadfilepath").toString();
	 * 
	 * // Load the PDF file into a byte array File pdfFile = new File(pdfFilePath);
	 * pdfContent = FileUtils.readFileToByteArray(pdfFile);
	 * 
	 * 
	 * headers.setContentType(MediaType.APPLICATION_PDF);
	 * headers.setContentDispositionFormData("filename", pdfFile.getName());
	 * headers.setCacheControl("must-revalidate, post-check=0, pre-check=0"); }
	 * catch (Exception e) { // TODO: handle exception e.printStackTrace();
	 * resJson.put("Description", e.getMessage()); } return new
	 * ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
	 * 
	 * 
	 * }
	 */
	
	//@PostMapping("/generateRenewalNote")
	@ResponseBody
	public JSONObject generateRenewalNote(@RequestBody String searchParams, HttpServletResponse response)
			throws Exception {
		logger.info("Generate  Renewal report details for renewalRmsModel: {}", searchParams);
		String message = null;
		JSONParser jParser = new JSONParser();
		JSONObject inJson = new JSONObject();
		JSONObject resJson = new JSONObject();

		try {
			inJson = (JSONObject) jParser.parse(searchParams);
            String renewalNoticeNo = (String) inJson.get("searchValue");
            String policyNo = (String) inJson.get("searchPolicy");
            String option = (String) inJson.get("searchType");
            String userId = (String) inJson.get("agrementcode");
            resJson = renewalRmsReportService.downloadRenewalNotice(renewalNoticeNo,policyNo,option,response,userId);
			/*
			 * response.setContentType("application/pdf");
			 * response.setHeader("Content-Disposition",
			 * "attachment;filename="+policyNo+".pdf");
			 */
            logger.info("Generate  Renewal report details for resJson: {}", resJson);
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			 logger.error("Internal server error occurred while generate the Renewal Note  for renewalRmsModel: {}", resJson, e);
			resJson.put("Description", e.getMessage());
		}
		System.out.println("report response"+resJson);
		return resJson;

		
	}
	
	@PostMapping("/generateRenewalNote")
	@ResponseBody
	public JSONObject getPDF(@RequestBody String searchParams, Errors errors, Model model)
			throws JsonProcessingException {
		logger.info("Search data" + searchParams);
		//System.out.println("Search data" + searchParams);

		JSONParser jParser = new JSONParser();
		JSONObject resJsonObj = new JSONObject();
		JSONObject inJson = null;
		String base64pdfStr = null;
		//ServiceFactory serviceFactory = new ServiceFactory();
		JSONObject jObj = null;

		try {

			//PdfDownloadService pdfDownloadService = serviceFactory.getService("CommonSearchEngine");

			inJson = (JSONObject) jParser.parse(searchParams);
//			String pdfType = (String) inJson.get("pdfType");
//			String pdfValue = (String) inJson.get("pdfValue");
//			String resJson = null;
			jObj = renewalRmsReportService.getPDF(inJson);
			System.out.println("resJson" + jObj.toJSONString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println("base64pdfStr" + base64pdfStr);

		return jObj;
	}
	@PostMapping("/sendRenewalSms")
	@ResponseBody
	public JSONObject sendRenewalSms(@RequestBody String searchParams, Model model,HttpSession session,HttpServletRequest request,HttpServletResponse response)
			throws Exception {
		String message = null;
		JSONParser jParser = new JSONParser();
		JSONObject inJson = new JSONObject();
		JSONObject resJson = new JSONObject();
		logger.info("Send  Renewal Sms");
		try {
		/*String userId = (String) session.getAttribute("user");
		if (userId != null) {*/
			inJson = (JSONObject) jParser.parse(searchParams);
            String mobileNo = (String) inJson.get("mobileNo");
            String policyNo = (String) inJson.get("policyNo");
            resJson = renewalRmsReportService.sendRenewalSms(mobileNo,policyNo,"73831");
			/* } */
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			 logger.error("Internal server error occurred while generate the Sending Renewal SMS renewalRmsModel: {}", resJson, e);
			resJson.put("message", e.getMessage());
		}

		return resJson;
	}
}
