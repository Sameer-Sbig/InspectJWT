package com.sbigeneral.LoginCapatchePoc.Controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
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
import com.sbigeneral.LoginCapatchePoc.Service.ApiService;
import com.sbigeneral.LoginCapatchePoc.Service.UserDetailsService;
import com.sbigeneral.LoginCapatchePoc.Service.UserService;
import com.sbigeneral.LoginCapatchePoc.Utill.CaptchaUtils;
import com.sbigeneral.LoginCapatchePoc.Utill.EmailSender;
import com.sbigeneral.LoginCapatchePoc.Utill.GCMUtilty;
import com.sbigeneral.LoginCapatchePoc.Utill.UserSessionService;
import com.sbigeneral.LoginCapatchePoc.config.WebLogicConfig;
import com.sbigeneral.LoginCapatchePoc.model.ChangePasswordRequest;
import com.sbigeneral.LoginCapatchePoc.model.ExtraKmModel;
import com.sbigeneral.LoginCapatchePoc.model.UserModel;

import cn.apiclub.captcha.Captcha;
import sun.net.www.protocol.https.Handler;

@Controller
@CrossOrigin(origins = { "http://localhost:4200", "https://ansappsuat.sbigen.in", "http://localhost:5173",
		"http://172.18.115.105:7003/BMS" })
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
	@Autowired
	private ApiService apiService;
	private static RestTemplate restTemplate = new RestTemplate();

	@Autowired
	UserDetailsService userDetailsService;

	private static final Logger logger = LogManager.getLogger(loginController.class);

	private final Path imagePath = Paths.get("/src/main/resources/static/image");

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
		ResponseEntity<?> resp = null;
		logger.info("Regsitation   details for agreementCode: {}", agreementCode);
		try {
			List<AgreementCodeEntity> list = null;
			list = userService.checkAgrementCodeExistOrNot(agreementCode);
			if (list.size() > 0) {
				AgreementCodeEntity code = list.get(0);

				User user = new User();
				user.setUsername(code.getAgreementCode());
				user.setPassword("sbig@123");
				user.setAGREEMENT_ID(code.getAGREEMENT_ID());
				user.setMobileNo(code.getMobileNo());
				user.setEmail(code.getEmailId());

				userService.SaveEmployeDetails(user);
				sender.sendEmail(user.getEmail(), user.getUsername(), user.getPassword());
				logger.info("Registration Successfully and userName and password is sent to your given mailid", user);
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseMessage(
						"Registration Successfully and userName and password is sent to your given mailid"));

			} else {
				System.out.println("Agreement code does not exist in our database,Please communicate to the Ops team");
				logger.info("Agreement code does not exist in our database,Please communicate to the Ops team");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(
						"Agreement code does not exist in our database,Please communicate to the Ops team"));

			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Internal server error occurred while Registration Page for username: {}", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseMessage("INTERNAL SERVER ERROR"));

		}

	}

	@PostMapping(value = "/changePassword", consumes = "application/x-www-form-urlencoded")
	public ResponseEntity<?> ChangePasswordForm(@RequestParam String username, @RequestParam String password) {
		logger.info("changePassword details for Username: {}", username);
		// logger.info("Content-Length: {}", request.getContentLength());
		try {
			String[] parts = password.split(":");
			if (parts.length == 3) {
				String password1 = parts[0];
				String iv = parts[1];
				String key = parts[2];

				String decryptedPassword = gcmUtility.decrypt(password1, key, iv);
				System.out.println(decryptedPassword);
				String decryptedPassword1 = decryptedPassword.replaceAll("^\"|\"$", "");
				System.out.println(decryptedPassword1);

				userService.UpdatePassword(username, decryptedPassword1);
				logger.info("Password has been changed successfully");
				return ResponseEntity.status(HttpStatus.ACCEPTED)
						.body(new ResponseMessage("Password has been changed successfully"));
			} else {
				throw new AuthenticationServiceException("Invalid combined password format");
			}
		} catch (Exception e) {
			logger.error("Error changing password for Username: {}. Exception: {}", username, e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseMessage("INTERNAL SERVER ERROR"));
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
					return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseMessage(
							"Password has been sent to the email associated with the provided agreement code"));
				} else {
					logger.info("User with the provided agreement code not found in the database.");
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
							new ResponseMessage("User with the provided agreement code not found in the database."));
				}
			} else {
				logger.info("No agreement code provided in the request.");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ResponseMessage("No agreement code provided in the request."));
			}
		} catch (Exception e) {
			logger.error("Internal server error occurred while processing forget password request: {}", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseMessage("Internal server error occurred"));
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
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(
						"The username is not found in our database. Please provide a correct username."));
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
		HttpSession session = request.getSession(false);
		if (session != null) {
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
		return ResponseEntity.ok(
				httpSession.getAttribute("username") != null && httpSession.getAttribute("username").equals(username));
	}

	private void updateSessionRegistry(HttpSession session) { // Remove invalidated session from the session registry
		sessionRegistry.getAllSessions(session, false).forEach(SessionInformation::expireNow);
	}

	/// ***************************************Sameer Code
	/// ************************************////////////////////////////////////

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

//		@CrossOrigin
//		@PostMapping("/getReport")
//		public ResponseEntity<?> getReport(@RequestBody UserDetails user) {
//			logger.info("Fetching login details for PIN getReport method username: {}", user);
//
//			try {
//				// RestTemplate restTemplate = new RestTemplate();
//				// ResponseEntity<PinDetails[]> response = restTemplate.getForEntity(apiUrl,
//				// PinDetails[].class);
//				// PinDetails[] pinDetails = response.getBody();
//				// System.out.println(pinDetails);
//				// // Assuming you want to return the pinDetails array in the response
//				// return ResponseEntity.ok(pinDetails);
//
//				HttpHeaders headers = new HttpHeaders();
//				headers.setContentType(MediaType.APPLICATION_JSON);
//				Class<Map<String, List<PinDetails>>> responseType = (Class<Map<String, List<PinDetails>>>) (Class<?>) Map.class;
//
//				String employeeId = user.getEmployeeId();
//				String API_URL = "https://uat-dil.sbigen.in/services/PINModule/fetchPINDetails/v1";
//				String decision = "Extra KM Requested|Extra KM Approved|Case Recommend|Case Reject";
//				String apiUrl = API_URL + "?VendorCode=" + employeeId + "&Decision=" + decision;
//
//				System.out.println(apiUrl);
//
////				ResponseEntity<Map<String, List<PinDetails>>> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET,
////						null, responseType);
////
////				System.out.println(responseEntity.getBody());
////				logger.info("Response of getReport method in PIN details {}",responseEntity.getBody());
//				
//				
//				URL url = new URL(apiUrl);
//				HttpURLConnection con = (HttpURLConnection) url.openConnection();
//				con.setRequestMethod("GET");
//				//con.setRequestProperty("Content-Type", "application/json; utf-8");
//				con.setDoOutput(true);
//				OutputStream os = con.getOutputStream();
//				//os.write(POST_PARAMS.getBytes());
//				os.flush();
//				os.close();
//				// For POST only - END
//
//				int responseCode = con.getResponseCode();
//				if (responseCode == HttpURLConnection.HTTP_OK) {
//               BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//				String inputLine;
//				StringBuffer response = new StringBuffer();
//
//				while ((inputLine = in.readLine()) != null) {
//				response.append(inputLine);
//				}
//				in.close();
//
//				logger.debug("Response Body : " + response.toString());
//				String jsonStringResponse = response.toString();
//				System.out.println(jsonStringResponse);
////
////				return responseEntity.ok();
//				
//				}
//				return ResponseEntity.ok("");
//
//			} catch (Exception e) {
//				System.out.println("Error fetching report: " + e.getMessage());
//				logger.info("Error in PIN survey getReport {}", e.getMessage());
//				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch report");
//			}
//		}

	@CrossOrigin
	@PostMapping("/getReport")
	public ResponseEntity<?> getReport(@RequestBody UserDetails user) {
		logger.info("Fetching login details for PIN getReport method username: {}", user);

		String employeeId = user.getEmployeeId();
		
		ResponseEntity<String> response = apiService.getReport(employeeId);
		return response;
	}

	
	@PostMapping("/getByPinDetails")
	public ResponseEntity<?> getPinDetails(@RequestBody SearchbyPinDetails pinDetails) {
		System.out.println(pinDetails);
		String pinNumber = pinDetails.getPinNumber();

		ResponseEntity<Map<String, List<PinDetails>>> response =apiService.getByPinDetails(pinNumber);
		return response;
	}


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
		ResponseEntity<Map<String, String>> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST,
				requestEntity, responseType);
		try {

		
			System.out.println("Response Body : " + responseEntity.getBody());
			response = new ResponseEntity<Map<String, String>>(responseEntity.getBody(), HttpStatus.OK);
			logger.info("Response of postWithImage method in PIN details {}", response.getBody());

			if (responseEntity.getStatusCode().toString().contains("400")) {
				response = new ResponseEntity<Map<String, String>>(responseEntity.getBody(), HttpStatus.OK);
				logger.info("Response of postWithImage method in PIN details with error 400 due to Cognizant {}",
						response.getBody());
				
			}

		} catch (Exception e) {
			String[] errorLines = e.getMessage().split("<EOL>");
			for (String line : errorLines) {

				response = new ResponseEntity<>(responseEntity.getBody(), HttpStatus.ALREADY_REPORTED);
				logger.info("Response of postWithImage method in PIN details {}", e.getMessage());
				
				// response = new ResponseEntity<Map<String, String>>(errorResponseMap ,
				// HttpStatus.ALREADY_REPORTED);
			}
		}
		return response;
	}

	
	@PostMapping("/extraKmRequested")
	public ResponseEntity<?> extraKmRequestedMethod(@RequestBody ExtraKmModel entity) {
		// TODO: process POST request
		System.out.println(entity);
		ResponseEntity<?> response = null;
		String apiUrl = "https://uat-dil.sbigen.in/services/PINModule/ExtraKMRequest";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<ExtraKmModel> requestEntity = new HttpEntity<ExtraKmModel>(entity, headers);

		Class<Map<String, String>> responseType = (Class<Map<String, String>>) (Class<?>) Map.class;

		try {
			ResponseEntity<Map<String, String>> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST,
					requestEntity, responseType);
			if (responseEntity.getStatusCode().toString().contains("400")) {
				response = new ResponseEntity<Map<String, String>>(responseEntity.getBody(), HttpStatus.OK);
				logger.info("Response of Extra KM method in PIN details {}", response);

			}

		} catch (Exception e) {
			response = new ResponseEntity<String>(e.getMessage(), HttpStatus.ALREADY_REPORTED);
			logger.info("Response of Extra KM method in PIN details {}", response);
		}

		return response;
	}
	
	
	
	@GetMapping("/images")
	public ResponseEntity<Map<String, String>> getImage(){
		String loginPage ="";
		try {
			loginPage = new String(Files.readAllBytes(Paths.get("D:/SpringBootProjects/PSH/PSHBackend/src/main/resources/static/loginPage.txt")));
			System.out.println(loginPage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String logo = "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";
		
		Map<String, String> imageMap = new HashMap<String, String>();
		System.out.println(loginPage);
		imageMap.put("loginImage", loginPage);
		imageMap.put("sbigLogo" , logo);
		
		System.out.println(imageMap);
		
		
		return ResponseEntity.ok(imageMap);
		
	}

}
