package com.sure.api.decisions;

import com.sure.base.BaseApi;
import com.sure.enums.ApiPath;
import io.restassured.RestAssured;
import lombok.extern.log4j.Log4j2;

import java.util.Map;

@Log4j2
public class DeleteResolutionDecision extends BaseApi {

    public String deleteResolutionDecision(String accessToken, int resolutionDecisionId) {


        String endpoint=ApiPath.apiPath.ADD_ACTIONS.getValue() + "/{resolutionDecisionId}";
        String message= deleteRequest(accessToken,endpoint, Map.of("resolutionDecisionId",resolutionDecisionId),null)
                .then().extract().jsonPath().get("result");

        log.info("The Message Is " + message);
        return message;
    }
}
