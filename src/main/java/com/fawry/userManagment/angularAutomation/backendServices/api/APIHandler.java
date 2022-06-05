package com.fawry.userManagment.angularAutomation.backendServices.api;

import io.restassured.RestAssured;
import io.restassured.RestAssured.*;
import io.restassured.matcher.RestAssuredMatchers.*;
import org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.*;

public class APIHandler {


    public static String getResponseBody(String url)
    {
        RestAssured.get();
        given().queryParam("CUSTOMER_ID","68195")
                .queryParam("PASSWORD","1234!")
                .queryParam("Account_No","1")
                .when().get(url).then().log()
                .body();
        return "";
    }

    public static void getResponseStatus(String url){
        int statusCode= given().queryParam("CUSTOMER_ID","68195")
                .queryParam("PASSWORD","1234!")
                .queryParam("Account_No","1") .when().get(url).getStatusCode();

        System.out.println("The response status is "+statusCode);

        given().when().get(url).then().assertThat().statusCode(200);
    }

    public static void getResponseHeaders(String url){
        System.out.println("The headers in the response "+
                get(url).then().extract()
                        .headers());
    }

    public static void getResponseTime(String url){
        System.out.println("The time taken to fetch the response "+get(url)
                .timeIn(TimeUnit.MILLISECONDS) + " milliseconds");
    }

    public static void getResponseContentType(String url){
        System.out.println("The content type of response "+
                get(url).then().extract()
                        .contentType());
    }

    public static String getSpecificPartOfResponseBody(String url, String extractedText){

        String extractedTxtValue = when().get(url).then().extract().path(extractedText) ;

        System.out.println("The extracted text from response is: " + extractedTxtValue);

        return extractedTxtValue;
    }

    public static void main(String args[]) {

        getResponseBody("http://demo.guru99.com/V4/sinkministatement.php?CUSTOMER_ID=68195&PASSWORD=1234!&Account_No=1");
        getResponseStatus("http://demo.guru99.com/V4/sinkministatement.php?CUSTOMER_ID=68195&PASSWORD=1234!&Account_No=1");
        String extractedText = getSpecificPartOfResponseBody("http://demo.guru99.com/V4/sinkministatement.php?CUSTOMER_ID=68195&PASSWORD=1234!&Account_No=1", "result.statements.AMOUNT");


    }
}
