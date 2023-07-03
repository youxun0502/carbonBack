package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan("com.liu.filter")
public class Group5Application {

	public static void main(String[] args) {
		SpringApplication.run(Group5Application.class, args);
		
	}

}
