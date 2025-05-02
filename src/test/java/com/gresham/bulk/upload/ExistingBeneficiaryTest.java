package com.gresham.bulk.upload;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;


public class ExistingBeneficiaryTest {

    
    public static void main(String[] args) {
     
//        assertThat(loginToBank("sora")).isTrue();
//        https://sora.clareti.cash/graphql

//        setBaseURI("sora");
        ValidatableResponse token = given().log().all()
                .queryParams(getLoginToBankGenericParam())
                .when()
                .get("https://sora.clareti.cash/login")
                .then().log().cookies()
                .cookie("token");
        System.out.println(token.toString());
        String query = "{ \"query\": \"{ bank { customers { edges { node { parties(filter:{partyType:TRUST}) { edges { node { ... on Party { shortId } roles } } } } } } } }\" }";

        given().contentType(ContentType.JSON).body(query)
                .when().log().all().post("https://sora.clareti.cash/graphql")
                .then().log().all();
    }
    
    
public String getBeneficiaryQueryForCustomer(String customer,String type) {
        
        return """
                {
                	bank {
                		customers (filter:{fullNameExact:"%s"}){
                			edges {
                				node {
                					parties(filter:{partyType:%s}) {
                						edges {
                							node {
                								... on Party {
                									shortId
                								}
                								roles
                							}
                						}
                					}
                				}
                			}
                		}
                	}
                }
                
                """.formatted(customer,type);
}    
    public boolean loginToBank(String environment) {
        setBaseURI(environment);
        Response response = given()
                .queryParams(getLoginToBankGenericParam())
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract().response();

        return response.statusCode()==200;
    }
    
    public void setBaseURI(String environment) {
       String.format("https://%s.clareti.cash/login",environment);
    }
    public static Map<String, Object> getLoginToBankGenericParam(){
        Map<String, Object> params = new HashMap<>();
        params.put("type", "BANK");
        params.put("read", "READ");
        params.put("write", "WRITE");
        params.put("beta", "BETA");
        params.put("api-only", "API_ONLY");
        return params;
    }
}
