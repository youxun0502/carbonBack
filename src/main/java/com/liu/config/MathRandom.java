package com.liu.config;

import java.util.Random;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MathRandom {

	@Bean
	public Random random() {
		return new Random();
	}
}
