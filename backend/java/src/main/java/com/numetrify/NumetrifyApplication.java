package com.numetrify;

import org.mariuszgromada.math.mxparser.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NumetrifyApplication {

	public static void main(String[] args) {
		License.iConfirmNonCommercialUse("numetrify");
		SpringApplication.run(NumetrifyApplication.class, args);
	}

}
