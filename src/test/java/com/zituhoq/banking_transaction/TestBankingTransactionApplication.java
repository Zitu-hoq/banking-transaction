package com.zituhoq.banking_transaction;

import org.springframework.boot.SpringApplication;

public class TestBankingTransactionApplication {

	public static void main(String[] args) {
		SpringApplication.from(BankingTransactionApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
