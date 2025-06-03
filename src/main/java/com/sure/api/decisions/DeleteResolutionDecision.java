package com.sure.api.decisions;

import com.sure.base.BaseApi;
import com.sure.enums.ApiPath;
import io.restassured.RestAssured;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class DeleteResolutionDecision extends BaseApi {

    public String deleteResolutionDecision(String accessToken, int resolutionDecisionId) {

        setUpRequestSpecification();
        setUpResponseSpecification();

        String message = RestAssured.given()
                .header("Authorization", "Bearer " + accessToken)

                .spec(requestSpec).log().all()
                .pathParam("resolutionDecisionId", resolutionDecisionId)
                .when()
                .baseUri(ApiPath.setBaseAPIPath())
                .body("{}")
                .delete(ApiPath.apiPath.ADD_ACTIONS.getValue() + "/{resolutionDecisionId}") // Use the path parameter
                .then().spec(responseSpec).log().all()
                .extract().path("result");
        log.info("The Message Is " + message);
        return message;
    }
}
