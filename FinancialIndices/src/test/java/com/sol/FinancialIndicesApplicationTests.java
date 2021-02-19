package com.sol;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = { "test" })
class FinancialIndicesApplicationTests {

	@LocalServerPort
	int randomServerPort;
	
	@Test
	void contextLoads() {
	}

}
