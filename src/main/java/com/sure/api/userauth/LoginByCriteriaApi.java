package com.sure.api.userauth;


import com.sure.base.BaseApi;
import com.sure.enums.ApiPath;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class LoginByCriteriaApi extends BaseApi {
    public String loginByCriteriaApi(int accountId, String email, String password) {

        setUpRequestSpecification();
        setUpResponseSpecification();

        // Replace placeholders in the JSON body with actual values
//        String requestBody = jsonFileManagerBodyRequests.getJsonFileContent("testApiRequestsPath", "LoginByCriteriaApi.json")
//                .replace("{{Account_Id}}", String.valueOf(accountId))
//                .replace("{{Email}}", email)
//                .replace("{{Password}}", password);

        JsonPath responseJson = RestAssured.given()
                .spec(requestSpec).log().all()
                .when()
                .baseUri(ApiPath.setBaseAPIPath())
                .body("requestBody")
                .post(ApiPath.apiPath.LOGIN.getValue())
                .then().spec(responseSpec).log().all()
                .extract().jsonPath();

        String accessToken = responseJson.getString("result.token");
        log.info("Access Token: " + accessToken);
        return accessToken;
    }
}
