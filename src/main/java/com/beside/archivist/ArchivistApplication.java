package com.beside.archivist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ArchivistApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArchivistApplication.class, args);
	}

}
