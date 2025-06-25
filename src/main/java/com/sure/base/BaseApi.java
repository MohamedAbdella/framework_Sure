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

    /**
     * Constructs a new {@code BaseApi} instance.
     * <p>
     * The class represents a common base for API related utilities. During
     * construction it prepares the {@link JsonFileManager} that will be used
     * by any subclass to read JSON request bodies.
     */
    public BaseApi() {
        jsonFileManagerBodyRequests = new JsonFileManager();
    }

    /**
     * Builds the default {@link RequestSpecification} used by REST-assured.
     * <ul>
     *   <li>Creates a specification with common HTTP headers.</li>
     *   <li>The specification is stored in a static field so tests can reuse it.</li>
     * </ul>
     */
    public static void setUpRequestSpecification() {
        requestSpec = new RequestSpecBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Connection", "keep-alive")
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .addHeader("Accept", "*/*")
                .addHeader("User-Agent", "PostmanRuntime/7.32.3")
                .build();
    }

    /**
     * Builds the default {@link ResponseSpecification} used by REST-assured.
     * <p>
     * The specification verifies that a request returns either HTTP 200 or 201
     * and that the response body is JSON.
     */
    public static void setUpResponseSpecification() {
        responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(anyOf(is(200), is(201)))
                .expectHeader("Content-Type", "application/json")
                .expectContentType(ContentType.JSON)
                .build();
    }

}
