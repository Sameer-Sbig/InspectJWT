package com.sbigeneral.LoginCapatchePoc.Controller;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.sbigeneral.LoginCapatchePoc.Entity.AgreementCodeEntity;
import com.sbigeneral.LoginCapatchePoc.Entity.CustomeIdGenerator;
import com.sbigeneral.LoginCapatchePoc.Entity.PinDetails;
import com.sbigeneral.LoginCapatchePoc.Entity.ResponseMessage;
import com.sbigeneral.LoginCapatchePoc.Entity.SearchbyPinDetails;
import com.sbigeneral.LoginCapatchePoc.Entity.UploadImage;
import com.sbigeneral.LoginCapatchePoc.Entity.User;
import com.sbigeneral.LoginCapatchePoc.Entity.UserDetails;
import com.sbigeneral.LoginCapatchePoc.Service.UserDetailsService;
import com.sbigeneral.LoginCapatchePoc.Service.UserService;
import com.sbigeneral.LoginCapatchePoc.Utill.CaptchaUtils;
import com.sbigeneral.LoginCapatchePoc.Utill.EmailSender;
import com.sbigeneral.LoginCapatchePoc.Utill.GCMUtilty;
import com.sbigeneral.LoginCapatchePoc.Utill.UserSessionService;
import com.sbigeneral.LoginCapatchePoc.model.ChangePasswordRequest;
import com.sbigeneral.LoginCapatchePoc.model.ExtraKmModel;
import com.sbigeneral.LoginCapatchePoc.model.UserModel;

import cn.apiclub.captcha.Captcha;

@Controller
@CrossOrigin(origins = {"http://localhost:4200", "https://ansappsuat.sbigen.in" ,"http://localhost:5173","http://172.18.115.105:7003/BMS"})
@PropertySource("classpath:log4j2.properties")
public class loginController {
	@Autowired
	private UserService userService;
	@Autowired
	private EmailSender sender;
	@Autowired
	private CustomeIdGenerator Idgenerator;
	@Autowired
	@Lazy
	private PasswordEncoder encoder;
	@Autowired
    private HttpSession httpSession;
	@Autowired
	private SessionRegistry sessionRegistry;
	@Autowired
	private UserSessionService userSessionService;
	@Autowired
	private GCMUtilty gcmUtility;
	private static RestTemplate restTemplate = new RestTemplate();

	@Autowired
	UserDetailsService userDetailsService;
	
	private static final Logger logger=LogManager.getLogger(loginController.class);
	

	private void setupCaptcha(User e) {
		Captcha captcha = CaptchaUtils.createCaptcha(200, 50);
		e.setHidden(captcha.getAnswer());
		e.setCaptcha("");
		e.setImage(CaptchaUtils.encodeBase64(captcha));
	}
	
	/*
	 * @RequestMapping(value = "/") public String redirect() { return
	 * "forward:/index.html"; }
	 */
	
	@GetMapping("/registration/{agreementCode}")
	public ResponseEntity<?> registerUser(@PathVariable String agreementCode) {
		ResponseEntity<?> resp=null;
		logger.info("Regsitation   details for agreementCode: {}",agreementCode);
		try
		{
			List<AgreementCodeEntity> list=null;
		list=userService.checkAgrementCodeExistOrNot(agreementCode);
		if(list.size()>0)
		{
			AgreementCodeEntity code=list.get(0);
			
			User user= new User();
			user.setUsername(code.getAgreementCode());
			user.setPassword("sbig@123");
			user.setAGREEMENT_ID(code.getAGREEMENT_ID());
			user.setMobileNo(code.getMobileNo());
			user.setEmail(code.getEmailId());
			
			userService.SaveEmployeDetails(user);
			sender.sendEmail(user.getEmail(),user.getUsername(),user.getPassword());
			logger.info("Registration Successfully and userName and password is sent to your given mailid",user);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseMessage("Registration Successfully and userName and password is sent to your given mailid"));
		
		}
		else {
			System.out.println("Agreement code does not exist in our database,Please communicate to the Ops team");
			logger.info("Agreement code does not exist in our database,Please communicate to the Ops team");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Agreement code does not exist in our database,Please communicate to the Ops team"));
			
			
		}
		
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("Internal server error occurred while Registration Page for username: {}",  e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("INTERNAL SERVER ERROR"));
			
		}
		
		
	}
	@PostMapping(value="/changePassword", consumes="application/x-www-form-urlencoded")
	public ResponseEntity<?> ChangePasswordForm(@RequestParam String username, @RequestParam String password) {
	    logger.info("changePassword details for Username: {}", username);
	    //logger.info("Content-Length: {}", request.getContentLength());
	    try {
	    String[] parts = password.split(":");
        if (parts.length == 3) {
            String password1 = parts[0];
            String iv = parts[1];
            String key = parts[2];
      
        String decryptedPassword = gcmUtility.decrypt(password1,key,iv);
        System.out.println(decryptedPassword);
        String decryptedPassword1 = decryptedPassword.replaceAll("^\"|\"$", "");
        System.out.println(decryptedPassword1);
	    
	        userService.UpdatePassword(username, decryptedPassword1);
	        logger.info("Password has been changed successfully");
	        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseMessage("Password has been changed successfully"));
	    } else {
            throw new AuthenticationServiceException("Invalid combined password format");
	    } 
	    } catch (Exception e) {
	        logger.error("Error changing password for Username: {}. Exception: {}", username, e.getMessage(), e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("INTERNAL SERVER ERROR"));
	    }
	}
 		
	
	  
	
	  
	  @PostMapping("/forgetpassword")
	    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
	        try {
	         
	            String agreementCode1 = request.get("agreementCode");
	            if (agreementCode1 != null) {
	                logger.info("Forget password request received for agreement code: {}", agreementCode1);
	                User user = userService.findByAgrementCode(agreementCode1);
	                if (user != null) {
	                    String emailId = user.getEmail();
	                    userService.savePasswordFlag(agreementCode1);
	                    sender.sendEmailPassword(emailId, user.getEncodePassword());
	                    logger.info("Password has been sent to the email associated with the provided agreement code");
	                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseMessage("Password has been sent to the email associated with the provided agreement code"));
	                } else {
	                    logger.info("User with the provided agreement code not found in the database.");
	                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage("User with the provided agreement code not found in the database."));
	                }
	            } else {
	                logger.info("No agreement code provided in the request.");
	                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("No agreement code provided in the request."));
	            }
	        } catch (Exception e) {
	            logger.error("Internal server error occurred while processing forget password request: {}", e);
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("Internal server error occurred"));
	        }
	    }
	  
	  
	@GetMapping("/fetchlogin/{username}")
    public ResponseEntity<?> fetchLoginDetails(@PathVariable String username) {
        logger.info("Fetching login details for username: {}", username);
        try {
            User user = userService.FindByUserName(username);
            if (user != null) {
                logger.info("Login details found for username: {}", username);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(user);
            } else {
                logger.info("User with username {} not found in the database", username);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseMessage("The username is not found in our database. Please provide a correct username."));
            }
        } catch (Exception e) {
            logger.error("Internal server error occurred while fetching login details for username: {}", username, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Internal server error occurred."));
        }
    }

	  

@GetMapping("/logout/{agreementCode}")
public ResponseEntity<ResponseMessage> logout(@PathVariable String agreementCode, HttpServletRequest request,
		HttpServletResponse response) {
	HttpSession session=request.getSession(false);
	if(session!=null)
	{
		session.invalidate();
	}
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	if (auth != null) {

		userSessionService.logout(agreementCode);
	}
		request.getSession().invalidate();
		Cookie cookie = new Cookie("JSESSIONID", null); 
		cookie.setMaxAge(0); 
		cookie.setPath("/"); 
		response.addCookie(cookie);
		// Delete JSESSIONID cookie
		response.addCookie(new Cookie("JSESSIONID", null));
	
	return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseMessage("Logged out Successfully.."));

}
	 
	  @GetMapping("/invalidate-session/{username}")
	    public ResponseEntity<?> invalidateSession(@PathVariable String username) {
	        if (httpSession.getAttribute("username") != null && httpSession.getAttribute("username").equals(username)) {
	            httpSession.invalidate();
	            
	            return ResponseEntity.ok("Session invalidated");
	        }
	        return ResponseEntity.ok("No session found");
	    }

	    @GetMapping("/is-logged-in/{username}")
	    public ResponseEntity<Boolean> isLoggedIn(@PathVariable String username) {
	        return ResponseEntity.ok(httpSession.getAttribute("username") != null && httpSession.getAttribute("username").equals(username));
	    }
	    
	    
	    private void updateSessionRegistry(HttpSession session) { // Remove invalidated session from the session registry
	    	sessionRegistry.getAllSessions(session, false) .forEach(SessionInformation::expireNow); 
	    	}
	    
	   
	    /// ***************************************Sameer Code ************************************////////////////////////////////////
	    
	    
	    @CrossOrigin
	    @PostMapping("/getByemployeeId")
		public ResponseEntity<?> loginFunction(@RequestBody UserModel user) {
			ResponseEntity<?> response = null;
			try {
				UserDetails userByEmployeeID = userDetailsService.getUserByEmployeeID(user.getEmployeeId());

				System.out.println(userByEmployeeID);

				Map<String, String> employee = new HashMap<String, String>();

				employee.put("vendorCode", userByEmployeeID.getEmployeeId());
				employee.put("name", userByEmployeeID.getName());
				response = new ResponseEntity<Map<String, String>>(employee, HttpStatus.OK);

			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e);
				response = new ResponseEntity<String>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return response;
		}

		@CrossOrigin
		@PostMapping("/loginpage")
		public ResponseEntity<?> login(@RequestBody UserModel user) {
			logger.info("Fetching login details for PIN username: {}", user);
			
			ResponseEntity<?> response = null;
			try {
				UserDetails loggedInUser = userDetailsService.login(user);

				if (loggedInUser != null) {
					Map<String, String> employee = new HashMap<String, String>();
					employee.put("vendorCode", loggedInUser.getEmployeeId());
					employee.put("name", loggedInUser.getName());
					response = new ResponseEntity<Map<String, String>>(employee, HttpStatus.OK);
				} else {
					response = new ResponseEntity<String>("User does not exist", HttpStatus.NOT_FOUND);

				}

			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e);
				response = new ResponseEntity<String>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return response;

		}


		@CrossOrigin
		@PostMapping("/getReport")
		public ResponseEntity<?> getReport(@RequestBody UserDetails user) {
			logger.info("Fetching login details for PIN getReport method username: {}", user);

			try {
				// RestTemplate restTemplate = new RestTemplate();
				// ResponseEntity<PinDetails[]> response = restTemplate.getForEntity(apiUrl,
				// PinDetails[].class);
				// PinDetails[] pinDetails = response.getBody();
				// System.out.println(pinDetails);
				// // Assuming you want to return the pinDetails array in the response
				// return ResponseEntity.ok(pinDetails);

				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				Class<Map<String, List<PinDetails>>> responseType = (Class<Map<String, List<PinDetails>>>) (Class<?>) Map.class;

				String employeeId = user.getEmployeeId();
				String API_URL = "https://uat-dil.sbigen.in/services/PINModule/fetchPINDetails/v1";
				String decision = "Extra KM Requested|Extra KM Approved|Case Recommend|Case Reject";
				String apiUrl = API_URL + "?VendorCode=" + employeeId + "&Decision=" + decision;

				System.out.println(apiUrl);

				ResponseEntity<Map<String, List<PinDetails>>> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET,
						null, responseType);

				System.out.println(responseEntity.getBody());
				logger.info("Response of getReport method in PIN details {}",responseEntity.getBody());

				return responseEntity.ok(responseEntity.getBody());

			} catch (Exception e) {
				System.out.println("Error fetching report: " + e.getMessage());
				logger.info("Error in PIN survey getReport {}", e.getMessage());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch report");
			}
		}

		@CrossOrigin
		@PostMapping("/getByPinDetails")
		public ResponseEntity<?> getPinDetails(@RequestBody SearchbyPinDetails
				pinDetails) {
			System.out.println(pinDetails);
			String pinNumber = pinDetails.getPinNumber();
			logger.info("Response of getByPinDetails method in PIN details {}",pinDetails.getPinNumber());
			System.out.println(pinNumber);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			Class<Map<String, List<PinDetails>>> responseType = (Class<Map<String, List<PinDetails>>>) (Class<?>) Map.class;

			String apiUrl = "https://uat-dil.sbigen.in:443/services/PINModule/fetchPINDetails/v1?PINNumber=" + pinNumber;
			System.out.println(apiUrl);

			ResponseEntity<Map<String, List<PinDetails>>> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET,
					null, responseType);
			System.out.println(responseEntity.getBody());
			logger.info("Response of getByPinDetails method in PIN details {}",responseEntity.getBody());
			
			return responseEntity.ok(responseEntity.getBody());

		}

		@CrossOrigin
		@PostMapping("/postWithImage")
		public ResponseEntity<?> postWithImage(@RequestBody UploadImage obj) {
			// TODO: process POST request
			ResponseEntity<?> response = null;
			System.out.println(obj);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			String apiUrl = "https://uat-dil.sbigen.in/services/PINModule/updateCase";
			HttpEntity<UploadImage> requestEntity = new HttpEntity<UploadImage>(obj, headers);
			Class<Map<String, String>> responseType = (Class<Map<String, String>>) (Class<?>) Map.class;
			
			try {

				ResponseEntity<Map<String, String>> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST,
						requestEntity, responseType);
				System.out.println("Response Body : " + responseEntity.getBody());
					response =  new ResponseEntity<Map<String, String>>(responseEntity.getBody(), HttpStatus.OK);
					logger.info("Response of postWithImage method in PIN details {}",response.getBody());

				if (responseEntity.getStatusCode().toString().contains("400")) {
					response = new ResponseEntity<Map<String, String>>(responseEntity.getBody(), HttpStatus.OK);
					logger.info("Response of postWithImage method in PIN details with error 400 due to Cognizant {}",response.getBody());

				}

			} catch (Exception e) {
				String[] errorLines = e.getMessage().split("<EOL>");
				for (String line : errorLines) {
					
					response = new ResponseEntity<String>(e.getMessage(), HttpStatus.ALREADY_REPORTED);
					logger.info("Response of postWithImage method in PIN details {}",e.getMessage());

					// response = new ResponseEntity<Map<String, String>>(errorResponseMap ,
					// HttpStatus.ALREADY_REPORTED);
				}
			}
			return response;
		}

		@CrossOrigin
		@PostMapping("/extraKmRequested")
		public ResponseEntity<?> extraKmRequestedMethod(@RequestBody ExtraKmModel entity) {
			//TODO: process POST request
			System.out.println(entity);
			ResponseEntity<?> response = null;
			String apiUrl = "https://uat-dil.sbigen.in/services/PINModule/ExtraKMRequest";
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<ExtraKmModel> requestEntity = new HttpEntity<ExtraKmModel>(entity, headers);

			Class<Map<String, String>> responseType = (Class<Map<String, String>>) (Class<?>) Map.class;

			try{
				ResponseEntity<Map<String, String>> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST,
				requestEntity, responseType);
				if (responseEntity.getStatusCode().toString().contains("400")) {
					response = new ResponseEntity<Map<String, String>>(responseEntity.getBody(), HttpStatus.OK);
					logger.info("Response of Extra KM method in PIN details {}",response);

				}



			} catch (Exception e) {
				response = new ResponseEntity<String>(e.getMessage(), HttpStatus.ALREADY_REPORTED);
				logger.info("Response of Extra KM method in PIN details {}",response);
			}

			return response;
		}

	    

	}




