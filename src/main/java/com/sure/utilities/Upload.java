package com.sure.utilities;

import com.sure.base.DriverManager;
import com.sure.base.PageBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public final class Upload extends PageBase {

    public Upload(DriverManager driverManager) {
        super(driverManager);
    }


    public static void uploadFileUsingInput(WebDriver driver, String fileInputLocator, String filePath) {
        WebElement fileInput = driver.findElement(By.xpath(fileInputLocator));
        fileInput.sendKeys(filePath);
    }
}

