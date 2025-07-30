package com.sure.base;

import com.sure.enums.ApiPath;
import com.sure.utilities.JsonFileManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import lombok.extern.log4j.Log4j2;
import java.util.Map;

@Log4j2
public class BaseApi {

    protected RequestSpecification spec;
    protected JsonFileManager jsonFileManagerBodyRequests;

    public BaseApi() {
        jsonFileManagerBodyRequests=new JsonFileManager();
        spec = RestAssured
                .given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .header("Connection", "keep-alive")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept", "*/*")
                .header("User-Agent", "PostmanRuntime/7.32.3");
    }

    public BaseApi setEndpoint(String endpoint) {
        spec.baseUri(endpoint);
        return this;
    }

    public BaseApi addPathParameters(Map<String, ?> pathParams) {
        if (pathParams != null && !pathParams.isEmpty()) {
            spec.pathParams(pathParams);
        }
        return this;
    }

    public BaseApi addQueryParameters(Map<String, ?> queryParams) {
        if (queryParams != null && !queryParams.isEmpty()) {
            spec.queryParams(queryParams);
        }
        return this;
    }

    public BaseApi addBody(Object body) {
        if (body != null) {
            spec.body(body);
        }
        return this;
    }

    public BaseApi addAuthToken(String token) {
        if (token != null && !token.isEmpty()) {
            spec.header("Authorization", "Bearer " + token);
        }
        return this;
    }

    public Response postRequest() {
        return spec
                .when()
                .post()
                .then()
                .log().all()
                .extract().response();
    }

    public Response putRequest() {
        return spec
                .when()
                .put()
                .then()
                .log().all()
                .extract().response();
    }

    public Response getRequest() {
        return spec
                .when()
                .get()
                .then()
                .log().all()
                .extract().response();
    }

    public Response deleteRequest() {
        return spec
                .when()
                .delete()
                .then()
                .log().all()
                .extract().response();
    }

}