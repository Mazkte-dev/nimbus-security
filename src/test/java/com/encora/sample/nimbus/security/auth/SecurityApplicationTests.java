package com.encora.sample.nimbus.security.auth;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SecurityApplicationTests {

	@Test
	void contextLoads() throws NoSuchAlgorithmException {
		// Generate a secret key
		KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA512");
		keyGen.init(512); // Key size
		SecretKey secretKey = keyGen.generateKey();

		// Encode the secret key to Base64
		String base64EncodedSecretKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());

		// Print the Base64 encoded secret key
		System.out.println("Base64 Encoded Secret Key: " + base64EncodedSecretKey);

	}

}
