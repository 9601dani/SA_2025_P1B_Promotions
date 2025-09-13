package com.danimo.promotion;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@SpringBootApplication
public class PromotionApplication {

	@Value("${LOCATION_ROUTE_URI:http://localhost:8004}")
	private String locationRouteUri;

	@Value("${HOTEL_ROUTE_URI:http://localhost:8007}")
	private String hotelRouteUri;

	@Value("${RESTAURANT_ROUTE_URI:http://localhost:8005}")
	private String restaurantRouteUri;

	public static void main(String[] args) {
		SpringApplication.run(PromotionApplication.class, args);
	}

	@Bean("LocationRestApi")
	public RestClient restLocationClient() {
		return RestClient.builder()
				.baseUrl(locationRouteUri + "/v1/locations/check/")
				.build();
	}

	@Bean("RoomRestApi")
	public RestClient restRoomClient() {
		return RestClient.builder()
				.baseUrl(hotelRouteUri + "/v1/rooms/check/")
				.build();
	}

	@Bean("DishRestApi")
	public RestClient restDishClient() {
		return RestClient.builder()
				.baseUrl(restaurantRouteUri + "/v1/dishes/check/")
				.build();
	}
}
