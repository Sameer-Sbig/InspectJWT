package com.sbigeneral.LoginCapatchePoc.Service;

import org.springframework.stereotype.Service;

@Service
public interface Decrypt {
	
	
	 String decrypt(String encryptedData) throws Exception;

}
