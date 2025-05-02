package com.gresham.bulk.upload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.gresham.bulk.upload.service")
public class BulkUploadApplication {

	public static void main(String[] args) {
		SpringApplication.run(BulkUploadApplication.class, args);
	}

}
