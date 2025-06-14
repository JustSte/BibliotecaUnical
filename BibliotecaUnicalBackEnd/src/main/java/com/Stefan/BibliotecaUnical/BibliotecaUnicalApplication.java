package com.Stefan.BibliotecaUnical;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BibliotecaUnicalApplication {

	public static void main(String[] args) {
		SpringApplication.run(BibliotecaUnicalApplication.class, args);
	}

}
