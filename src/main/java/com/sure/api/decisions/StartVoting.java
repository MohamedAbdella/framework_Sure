package com.sure.api.decisions;

import com.sure.base.BaseApi;
import com.sure.enums.ApiPath;
import com.sure.utilities.JsonFileManager;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import lombok.extern.log4j.Log4j2;

import java.util.Map;

@Log4j2
public class StartVoting {

    public String startVoting(String accessToken, int decisionId) {
        JsonFileManager jsonFileManagerBodyRequests = new JsonFileManager();
        String endPoint = ApiPath.apiPath.START_VOTING.getValue()+ "/{decisionId}";
        String requestBody = jsonFileManagerBodyRequests.getJsonFileContent("testApiRequestsPath", "Voting.json")
                .replace("{{Id}}", String.valueOf(decisionId));

        JsonPath responseJson=new BaseApi().addAuthToken(accessToken)
                .addBody(requestBody)
                .setEndpoint(endPoint)
                .addPathParameters(Map.of("decisionId", decisionId))
                .postRequest()
                .jsonPath();

        String message = responseJson.getString("Message");

        log.info("The Message Is " + message);
        return message;
    }
}
