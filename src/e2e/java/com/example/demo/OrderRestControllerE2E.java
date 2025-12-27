package com.example.demo;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

class OrderRestControllerE2E {

	private static final String BASE_URI = "http://localhost";
	private static final int PORT = 8080;
	private static final String ORDER_ENDPOINT = "/api/orders";

	@BeforeAll
	static void setup() {
		RestAssured.baseURI = BASE_URI;
		RestAssured.port = PORT;
	}

	@Test
	void test_FullCrudOrder() {
		int id = given().contentType(ContentType.JSON).body("""
				{
				  "orderDate": "2025-06-01"
				}
				""").when().post(ORDER_ENDPOINT + "/new").then().statusCode(200).contentType(ContentType.JSON)
				.body("id", notNullValue()).body("orderDate", equalTo("2025-06-01")).extract().path("id");

		given().when().get(ORDER_ENDPOINT + "/" + id).then().statusCode(200).body("orderDate", equalTo("2025-06-01"));
		given().contentType(ContentType.JSON).body("""
				{
				  "orderDate": "2025-07-01"
				}
				""").when().put(ORDER_ENDPOINT + "/" + id).then().statusCode(200).body("orderDate",
				equalTo("2025-07-01"));

		given().when().delete(ORDER_ENDPOINT + "/" + id).then().statusCode(200);
		given().when().get(ORDER_ENDPOINT + "/" + id).then().statusCode(404);
	}
}