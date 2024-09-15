package com.encora.sample.nimbus.security.auth;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SecurityApplicationTests {

	@Test
	void contextLoads() throws NoSuchAlgorithmException {

		KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA512");
		keyGen.init(512);
		SecretKey secretKey = keyGen.generateKey();

		String base64EncodedSecretKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());

		Assertions.assertNotNull(base64EncodedSecretKey);

	}

}
