package com.numetrify;

import org.mariuszgromada.math.mxparser.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class for the Numetrify application.
 * This class bootstraps the Spring Boot application.
 */
@SpringBootApplication
public class NumetrifyApplication {

	/**
	 * The main method for the Numetrify application.
	 * Confirms non-commercial use of the mXparser library and runs the Spring Boot application.
	 *
	 * @param args command-line arguments
	 *
	 * Example usage:
	 * <pre>
	 * {@code
	 * public static void main(String[] args) {
	 *     NumetrifyApplication.main(args);
	 * }
	 * </pre>
	 */
	public static void main(String[] args) {
		License.iConfirmNonCommercialUse("numetrify");
		SpringApplication.run(NumetrifyApplication.class, args);
	}

}