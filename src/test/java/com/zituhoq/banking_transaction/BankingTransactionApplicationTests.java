package com.zituhoq.banking_transaction;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class BankingTransactionApplicationTests {

	@Test
	void contextLoads() {
	}

}
