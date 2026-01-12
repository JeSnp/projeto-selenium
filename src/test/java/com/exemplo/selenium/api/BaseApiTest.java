package com.exemplo.selenium.api;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class BaseApiTest {

    protected RequestSpecification request() {
        return RestAssured
                .given()
                .baseUri("https://google.com") 
                .basePath("/api")
                .contentType("application/json");
    }
}
