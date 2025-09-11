package com.danimo.promotion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@SpringBootApplication
public class PromotionApplication {

	public static void main(String[] args) {
		SpringApplication.run(PromotionApplication.class, args);
	}

	@Bean("LocationRestApi")
	public RestClient restLocationClient() {
		return RestClient.builder()
				.baseUrl("http://localhost:8000/v1/locations/check/")
				.build();
	}
	@Bean("RoomRestApi")
	public RestClient restRoomClient() {
		return RestClient.builder()
				.baseUrl("http://localhost:8000/v1/rooms/check/")
				.build();
	}
	@Bean("DishRestApi")
	public RestClient restDishClient() {
		return RestClient.builder()
				.baseUrl("http://localhost:8000/v1/dishes/check/")
				.build();
	}
}
