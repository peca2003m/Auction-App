package com.auction.core_service;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuctionApiTest {

    private static final String BASE_URL = "http://localhost:8082";
    private static String token;

    @BeforeAll
    static void setup() {
        token = given()
                .contentType(ContentType.URLENC)
                .formParam("grant_type", "password")
                .formParam("client_id", "auction-client")
                .formParam("client_secret", "cT48D31AS5SJpqFSqBWa6XZerUyKiWOl")
                .formParam("username", "pm123")
                .formParam("password", "pm123")
                .when()
                .post("http://localhost:8080/realms/auction/protocol/openid-connect/token")
                .then()
                .statusCode(200)
                .extract()
                .path("access_token");

        RestAssured.baseURI = BASE_URL;
        RestAssured.basePath = "/api";
    }

    @Test
    void shouldGetAuctions() {
        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when()
                .get("/auctions")
                .then()
                .statusCode(200);
    }
}