package com.sure.utilities;

import com.sure.base.DriverManager;
import com.sure.base.PageBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Simple page object providing a utility for uploading files via input fields.
 */
public final class Upload extends PageBase {

    /**
     * Standard constructor linking the page to the driver manager.
     */
    public Upload(DriverManager driverManager) {
        super(driverManager);
    }


    /**
     * Sends the provided file path to a standard file input element.
     */
    public static void uploadFileUsingInput(WebDriver driver, String fileInputLocator, String filePath) {
        WebElement fileInput = driver.findElement(By.xpath(fileInputLocator));
        fileInput.sendKeys(filePath);
    }
}

