package com.sure.api.decisions;

import com.sure.base.BaseApi;
import com.sure.enums.ApiPath;
import com.sure.utilities.JsonFileManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.extern.log4j.Log4j2;

import java.util.Map;

@Log4j2
public class DeleteResolutionDecision {

    public String deleteResolutionDecision(String accessToken, int resolutionDecisionId) {

        JsonFileManager jsonFileManagerBodyRequests = new JsonFileManager();
        String endpoint=ApiPath.apiPath.ADD_ACTIONS.getValue() + "/{resolutionDecisionId}";

        Response response= new BaseApi()
                .setEndpoint(endpoint)
                .addAuthToken(accessToken)
                .addPathParameters(Map.of("resolutionDecisionId",resolutionDecisionId))
                .deleteRequest();
        String message=response.jsonPath().get("result");
        log.info("The Message Is " + message);
        return message;
    }
}
