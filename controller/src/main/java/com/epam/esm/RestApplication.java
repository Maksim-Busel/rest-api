package com.epam.esm;

import com.epam.esm.config.SpringConfig;
import com.epam.esm.config.WebSecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestApplication {

	public static void main(String[] args) {
		SpringApplication.run(new Class<?>[]{RestApplication.class, SpringConfig.class, WebSecurityConfig.class}, args);
	}
}
