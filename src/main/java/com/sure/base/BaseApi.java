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
        jsonFileManagerBodyRequests=new JsonFileManager();
        RestAssured.baseURI = ApiPath.setBaseAPIPath();
    }

    public Response postRequestWithoutAuth(String endPoint,Object payload) {
        return getBaseRequestWithoutAuth()
                .body(payload)
                .when()
                .post(endPoint)
                .then()
                .log().all()
                .extract().response();
    }
    public Response postRequestWithAuth(String accessToken, Object payload, String endPoint,Map<String, ?> pathParams, Map<String, ?> queryParams) {
        return getBaseRequestWithAuth(accessToken,pathParams, queryParams)
                .body(payload)
                .when()
                .post(endPoint)
                .then()
                .log().all()
                .extract().response();
    }

    public Response putRequest(String accessToken, Object payload, String endPoint,Map<String, ?> pathParams, Map<String, ?> queryParams) {
        return getBaseRequestWithAuth(accessToken,pathParams, queryParams)
                .body(payload)
                .when()
                .put(endPoint)
                .then()
                .log().all()
                .extract().response();
    }

    public Response getRequest(String accessToken, String endPoint,Map<String, ?> pathParams, Map<String, ?> queryParams) {
        return getBaseRequestWithAuth(accessToken,pathParams, queryParams)
                .when()
                .get(endPoint)
                .then()
                .log().all()
                .extract().response();
    }
    public Response deleteRequest(String accessToken, String endPoint,Map<String, ?> pathParams, Map<String, ?> queryParams) {
        return getBaseRequestWithAuth(accessToken,pathParams, queryParams)
                .when()
                .delete(endPoint)
                .then()
                .log().all()
                .extract().response();
    }

    /**
     * Builds the default {@link RequestSpecification} used by REST-assured.
     * <ul>
     *   <li>Creates a specification with common HTTP headers.</li>
     *   <li>The specification is stored in a static field so tests can reuse it.</li>
     * </ul>
     */
    private static RequestSpecification getBaseRequestWithAuth(String accessToken, Map<String, ?> pathParams, Map<String, ?> queryParams) {

        RequestSpecification spec = getBaseRequestWithoutAuth()
                .header("Authorization", "Bearer " + accessToken);

        if (pathParams != null && !pathParams.isEmpty()) {
            spec.pathParams(pathParams);
        }

        if (queryParams != null && !queryParams.isEmpty()) {
            spec.queryParams(queryParams);
        }

        return spec;
    }

    /**
     * Builds the default {@link ResponseSpecification} used by REST-assured.
     * <p>
     * The specification verifies that a request returns either HTTP 200 or 201
     * and that the response body is JSON.
     */
    private static RequestSpecification getBaseRequestWithoutAuth() {
        return RestAssured
                .given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .header("Connection", "keep-alive")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept", "*/*")
                .header("User-Agent", "PostmanRuntime/7.32.3");
    }

}
