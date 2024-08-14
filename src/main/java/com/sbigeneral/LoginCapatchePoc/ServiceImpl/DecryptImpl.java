package com.sbigeneral.LoginCapatchePoc.ServiceImpl;

import com.sbigeneral.LoginCapatchePoc.Service.Decrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import org.springframework.stereotype.Service;


@Service
public class DecryptImpl implements Decrypt{
	
	private static final String SECRET_KEY = "o2mgCRCUgWhoVYneW49ncFUwDnM3AqPYlCHgCsgD97O+uJw92LdzUtShK/EVw/M+\r\n"; 

	@Override
	public String decrypt(String encryptedData) throws Exception {
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);

        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);

        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);

	}

}
