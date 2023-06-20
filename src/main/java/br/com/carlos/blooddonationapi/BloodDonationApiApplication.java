package br.com.carlos.blooddonationapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "br.com.carlos.blooddonationapi.*")
public class BloodDonationApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BloodDonationApiApplication.class, args);
	}

}
