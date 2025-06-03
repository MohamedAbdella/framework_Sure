package com.sure.api.decisions;

import com.sure.base.BaseApi;
import com.sure.enums.ApiPath;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class StartVoting extends BaseApi {

    public String startVoting(String accessToken, int decisionId) {

        setUpRequestSpecification();
        setUpResponseSpecification();

        String requestBody = jsonFileManagerBodyRequests.getJsonFileContent("testApiRequestsPath", "Voting.json")
                .replace("{{Id}}", String.valueOf(decisionId));


        JsonPath responseJson = RestAssured.given()
                .header("Authorization", "Bearer " + accessToken)

                .spec(requestSpec).log().all()
                .pathParam("decisionId", decisionId)
                .when()
                .baseUri(ApiPath.setBaseAPIPath())
                .body(requestBody)
                .post(ApiPath.apiPath.START_VOTING.getValue() + "/{decisionId}") // Use the path parameter
                .then().spec(responseSpec).log().all()
                .extract().jsonPath();
        String message = responseJson.getString("Message");

        log.info("The Message Is " + message);
        return message;
    }
}
