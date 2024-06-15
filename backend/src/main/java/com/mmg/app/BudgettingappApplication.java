package com.mmg.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BudgettingappApplication {

	public static void main(String[] args) {
		SpringApplication.run(BudgettingappApplication.class, args);

		System.out.println("\n\n\n***************************");
		System.out.println("Server running.");
		System.out.println("***************************\n\n\n");
	}

}