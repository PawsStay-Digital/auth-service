package com.pawsstay.auth_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"OWNER_SERVICE_URL=localhost:8081"
})
class AuthServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
