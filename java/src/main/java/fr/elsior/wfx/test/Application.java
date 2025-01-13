package fr.elsior.wfx.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * Author: Elimane
 */
@SpringBootApplication
@EnableWebFlux
public class Application {
	public static void main(String... args) {
		SpringApplication.run(Application.class, args);
	}
}
