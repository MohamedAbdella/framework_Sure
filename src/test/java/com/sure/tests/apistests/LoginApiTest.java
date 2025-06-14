package com.sure.tests.apistests;

import com.sure.api.userauth.LoginByCriteriaApi;
import com.sure.base.BaseApi;
import com.sure.utilities.JsonFileManager;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.Test;
import java.io.FileNotFoundException;


@Log4j2
public class LoginApiTest extends BaseApi {
    String token;
    private final JsonFileManager jsonFileManagerLoginTestData = new JsonFileManager();

    @Test(priority = 1, description = "Verify That The User Is Logged In Successfully From Api And Get The Access Token")
    public void login() throws FileNotFoundException {
        jsonFileManagerLoginTestData.getJsonFilePath("testDataFolderPath", "Login.json");
        String email = jsonFileManagerLoginTestData.getTestData("Users.BoardSecretary");
        String password = jsonFileManagerLoginTestData.getTestData("Users.Password");
        token = new LoginByCriteriaApi().loginByCriteriaApi(94, email, password);
        log.info("Tokes is {}:", token);
    }

}
