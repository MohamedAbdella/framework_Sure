package com.sure.utilities;

import com.sure.base.DriverManager;
import com.sure.base.PageBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Upload extends PageBase {

    public Upload(DriverManager driverManager) {
        super(driverManager);
    }

//    public static void uploadFile(String fileName) throws AWTException, InterruptedException {
//        String filePath = System.getProperty("user.dir") + "/attachments/downloadedFiles/" + fileName;
//
//        // Use StringSelection to copy the file path to the clipboard
//        StringSelection selection = new StringSelection(filePath);
//        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
//
//        Robot robot = new Robot();
//        robot.setAutoDelay(500); // Delay to ensure clipboard operation
//
//        // Simulate CTRL + V to paste the file path
//        robot.keyPress(KeyEvent.VK_META); // Command key on macOS
//        robot.keyPress(KeyEvent.VK_V);
//        robot.keyRelease(KeyEvent.VK_V);
//        robot.keyRelease(KeyEvent.VK_META);
//
//        // Simulate Enter to confirm
//
//        robot.keyPress(KeyEvent.VK_ENTER);
//        robot.keyRelease(KeyEvent.VK_ENTER);
//
//        // Allow time for the file dialog to process
//        Thread.sleep(1000);
//    }

    public static void uploadFileUsingInput(WebDriver driver, String fileInputLocator, String filePath) {
        WebElement fileInput = driver.findElement(By.xpath(fileInputLocator));
        fileInput.sendKeys(filePath);
    }
}

