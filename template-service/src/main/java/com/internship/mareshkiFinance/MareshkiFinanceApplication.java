package com.internship.mareshkiFinance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.internship.mareshkiFinance.repositories")
@EntityScan("com.internship.mareshkiFinance.Entities")
public class MareshkiFinanceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MareshkiFinanceApplication.class, args);
	}

}
