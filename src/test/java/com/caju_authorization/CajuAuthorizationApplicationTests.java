package com.caju_authorization;

import static org.assertj.core.api.Assertions.assertThat;

import com.caju_authorization.controller.TransactionController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CajuAuthorizationApplicationTests {

	@Autowired
	private TransactionController transactionController;

	@Test
	void contextLoads() throws Exception {
		assertThat(transactionController).isNotNull();
	}

}
