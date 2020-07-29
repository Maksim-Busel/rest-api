package com.epam.esm;

import com.epam.esm.config.SpringConfig;
import com.epam.esm.config.WebSecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class RestApplication {

	public static void main(String[] args) {
		SpringApplication.run(new Class<?>[]{RestApplication.class, SpringConfig.class, WebSecurityConfig.class}, args);
	}
}
