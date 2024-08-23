package com.sbigeneral.LoginCapatchePoc.Controller;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbigeneral.LoginCapatchePoc.Service.ApiService;
import com.sbigeneral.LoginCapatchePoc.Service.Decrypt;
import com.sbigeneral.LoginCapatchePoc.Service.Encrypt;
import com.sbigeneral.LoginCapatchePoc.Utill.FormValidation;
import com.sbigeneral.LoginCapatchePoc.model.UserModel;

@RestController
@CrossOrigin(origins = { "http://localhost:4200", "https://ansappsuat.sbigen.in", "http://localhost:5173",
		"http://172.18.115.105:7003/InspectJWT" })
public class DemoController {

	@Autowired
	Decrypt decryptService;
	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	private Encrypt encrypt;

	private String loginPageImage;

	@Autowired
	private FormValidation validation;
		@Autowired
	private ApiService apiService;
	private static RestTemplate restTemplate = new RestTemplate();


	@PostConstruct
	public void init() {
		loginPageImage = apiService.getLoginImage();
	}
	@GetMapping("/breakin/ganpatiBappaMorya")
	public ResponseEntity<?> demo() {
		return ResponseEntity.ok("Ganpati Bappa Morya");
	}



	// @CrossOrigin
	// @PostMapping("/getByemployeeId")
	// public ResponseEntity<?> loginFunction(@RequestBody UserModel user) {
	// 	ResponseEntity<?> response = null;
	// 	try {
	// 		UserDetails userByEmployeeID = userDetailsService.getUserByEmployeeID(user.getEmployeeId());

	// 		System.out.println(userByEmployeeID);

	// 		Map<String, String> employee = new HashMap<String, String>();

	// 		employee.put("vendorCode", userByEmployeeID.getEmployeeId());
	// 		employee.put("name", userByEmployeeID.getName());
	// 		response = new ResponseEntity<Map<String, String>>(employee, HttpStatus.OK);

	// 	} catch (Exception e) {
	// 		// TODO: handle exception
	// 		System.out.println(e);
	// 		response = new ResponseEntity<String>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
	// 	}
	// 	return response;
	// }

}
