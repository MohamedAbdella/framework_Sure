package com.sure.tests.apistests;

import com.sure.api.userauth.LoginByCriteriaApi;
import com.sure.base.BaseApi;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.Test;

@Log4j2
public class LoginApiTest extends BaseApi {
    String token;

    @Test(priority = 1, description = "Verify That The User Is Logged In Successfully From Api And Get The Access Token")
    public void login() {
        token = new LoginByCriteriaApi().loginByCriteriaApi(94, "yabdelmenaam@sure.com.sa", "123456");
        log.info("Tokes is {}:", token);
    }

}
