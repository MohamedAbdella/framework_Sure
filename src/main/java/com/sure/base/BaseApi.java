package com.sure.base;

import com.sure.utilities.JsonFileManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import lombok.extern.log4j.Log4j2;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

@Log4j2
public class BaseApi {

    protected static RequestSpecification requestSpec;
    protected static ResponseSpecification responseSpec;
    protected JsonFileManager jsonFileManagerBodyRequests;

    public BaseApi() {
        jsonFileManagerBodyRequests = new JsonFileManager();
    }

    public static void setUpRequestSpecification() {
        requestSpec = new RequestSpecBuilder().
                addHeader("Content-Type", "application/json").
                addHeader("Connection", "keep-alive").
                addHeader("Accept-Encoding", "gzip, deflate, br").
                addHeader("Accept", "*/*").
                addHeader("User-Agent", "PostmanRuntime/7.32.3").
                build();
    }

    public static void setUpResponseSpecification() {
        responseSpec = new ResponseSpecBuilder().
                expectStatusCode(anyOf(is(200), is(201))).
                expectHeader("Content-Type", "application/json").
                expectContentType(ContentType.JSON).
                build();
    }

}
