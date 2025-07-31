package com.sure.api.userauth;


import com.sure.base.BaseApi;
import com.sure.enums.ApiPath;
import com.sure.utilities.JsonFileManager;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class LoginByCriteriaApi{
    public String loginByCriteriaApi(int accountId, String email, String password) {

       String endPoint=ApiPath.apiPath.LOGIN.getValue();
       JsonFileManager jsonFileManagerBodyRequests = new JsonFileManager();
        // Replace placeholders in the JSON body with actual values
        String requestBody = jsonFileManagerBodyRequests.getJsonFileContent("testApiRequestsPath", "LoginByCriteriaApi.json")
                .replace("{{Account_Id}}", String.valueOf(accountId))
                .replace("{{Email}}", email)
                .replace("{{Password}}", password);

        Response response= new BaseApi().setEndpoint(endPoint)
                .addBody(requestBody)
                .postRequest();

        String accessToken = response.jsonPath().getString("result.token");
        log.info("Access Token: " + accessToken);
        return accessToken;
    }
}
