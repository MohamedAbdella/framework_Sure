package com.sure.api.decisions;

import com.github.javafaker.Faker;
import com.sure.base.BaseApi;
import com.sure.enums.ApiPath;
import com.sure.enums.VotingType;
import com.sure.utilities.JsonFileManager;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class AddResolutionDecision {
    @Step("User Can Add New Meeting")
    public JsonPath addResolutionDecisions(String accessToken, VotingType.votingType votingType) {
        JsonFileManager jsonFileManagerBodyRequests = new JsonFileManager();
        String decisionResolutionTitle = new Faker().name().title();

        String requestBody = jsonFileManagerBodyRequests.getJsonFileContent("testApiRequestsPath", "AddResolutionDecision.json")
                .replace("{{Voting_Type}}", String.valueOf(votingType.getValue()))
                .replace("{{title}}", decisionResolutionTitle);
        String endPoint = ApiPath.apiPath.ADD_ACTIONS.getValue();

        return new BaseApi()
                .setEndpoint(endPoint)
                .addAuthToken(accessToken)
                .addBody(requestBody).postRequest().jsonPath();

    }

    @Step("Get Resolution Decision Id From Response")
    public int getResolutionDecisionId(JsonPath jsonPath) {
        int id = jsonPath.getInt("result.id");
        log.info("The Meeting Id is " + id);
        return id;
    }

    @Step("Get The Message From Response")
    public String getResolutionDecisionCreatedMessage(JsonPath jsonPath) {
        String message = jsonPath.getString("Message");
        log.info("The Meeting Message is " + message);
        return message;
    }
}
