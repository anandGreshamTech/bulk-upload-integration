package com.gresham.bulk.upload;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static com.gresham.bulk.upload.ExistingBeneficiaryTest.getLoginToBankGenericParam;
import static com.gresham.bulk.upload.ExistingBeneficiaryTest.setBaseURI;
import static io.restassured.RestAssured.given;

public class SessionCookieExample {
    public static void main(String[] args) {
        RestAssured.useRelaxedHTTPSValidation();
        setBaseURI("sora");
        // Example login endpoint
        Response response = given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("User-Agent", "Mozilla/5.0")
                .header("Origin", "https://sora.clareti.cash")
                .header("Referer", "https://sora.clareti.cash/login")
                .log().all()
                .queryParams(getLoginToBankGenericParam())
                .when()
                .get("https://sora.clareti.cash/login").then()
                .statusCode(200) // or whatever you expect
                .extract().response();
                

        // Extract session cookie (name depends on the app, often "JSESSIONID")
//        Cookie sessionCookie = loginResponse.getDetailedCookie("JSESSIONID");
        String tokenCookie = response.getCookie("token");
        System.out.println("Token from cookie: " + tokenCookie);
    }
}
