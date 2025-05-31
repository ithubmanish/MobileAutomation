package Utilities;

import Runner.TestRunner;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.*;
import java.net.URL;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;

import io.cucumber.java.Scenario;


public class KeywordUtils {
    static String testCaseDescription;
    static String imagePath;
    static String pathForLogger;

    public static void cleanDirectory() {
        try {
            String filePath = System.getProperty("user.dir") + File.separator + ConfigReader.getValue("screenshotPath");
            if (new File(filePath).exists()) {
                FileUtils.cleanDirectory(new File(filePath));
            }
            filePath = System.getProperty("user.dir") + File.separator + "Reports" + File.separator + "HTMLReports";
            if (new File(filePath).exists()) {
                FileUtils.cleanDirectory(new File(filePath));
            }
        } catch (Throwable e) {

        }
    }

    public static void extentReportInitialization() {
        try {
            TestRunner.extent = new ExtentReports(System.getProperty("user.dir") + ConfigReader.getValue("extentReportPath"), true);
            TestRunner.extent.loadConfig(new File(System.getProperty("user.dir") + ConfigReader.getValue("extentConfigFile")));
            LogUtils.infoLog(TestRunner.class, "\n\n+===========================================================================================================+");
            LogUtils.infoLog(TestRunner.class, " Suite started" + " at " + new Date());
            LogUtils.infoLog(TestRunner.class, "\n\n+===========================================================================================================+");
        } catch (Throwable e) {
            e.getMessage();
            e.getCause();
        }
    }

    public static void startServer() {
        try {
            DriverUtils.service = new AppiumServiceBuilder()
                    .usingPort(4723)
//                    .usingAnyFreePort()
                    .build();
            DriverUtils.service.start();
            System.out.println("Appium server started at: " + DriverUtils.service.getUrl());
            DriverUtils.driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), KeywordUtils.setCapabilities());
            System.out.println("Android Application started at: " + DriverUtils.service.getUrl());
            Thread.sleep(5000);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void stopServer() {
        try {
            DriverUtils.service.stop();
            System.out.println("Android Application stopped at: " + DriverUtils.service.getUrl());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    public static Capabilities setCapabilities() throws IOException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        ExcelUtilities.fetchData("DeviceDetail","ConfigureApp");
        capabilities.setCapability("deviceName", String.valueOf(ExcelUtilities.excelData.get("DeviceName")));
        capabilities.setCapability("platformName", String.valueOf(ExcelUtilities.excelData.get("PlatformName")));
        capabilities.setCapability("platformVersion", String.valueOf(ExcelUtilities.excelData.get("PlatformVersion")));
        capabilities.setCapability("automationName", String.valueOf(ExcelUtilities.excelData.get("AutomationName")));
        capabilities.setCapability("appPackage", String.valueOf(ExcelUtilities.excelData.get("AppPackage")));
        capabilities.setCapability("appActivity", String.valueOf(ExcelUtilities.excelData.get("AppActivity")));
        return capabilities;
    }

    public static void extentReportClosure() {
        try {
            LogUtils.infoLog(TestRunner.class, "Suite Finished at: " + new Date());
            LogUtils.infoLog(TestRunner.class, "===========================================================");
            TestRunner.extent.flush();
        } catch (Throwable e) {

        }
    }


    public static byte[] takeScreenshot(String screenshotFilePath) {
        try {
            byte[] screenshot = ((TakesScreenshot) DriverUtils.getDriver()).getScreenshotAs(OutputType.BYTES);
            FileOutputStream fileOuputStream = new FileOutputStream(screenshotFilePath);
            fileOuputStream.write(screenshot);
            fileOuputStream.close();
            return screenshot;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void testCaseDescriptionStart(Scenario scenario) {
        try {
            testCaseDescription = scenario.getName().split("_")[1];
        } catch (Exception e) {
            testCaseDescription = scenario.getName();
        }
        TestRunner.logger = TestRunner.extent.startTest(testCaseDescription);
        try {
            Collection<String> tagsList = scenario.getSourceTagNames();
            if (tagsList.size() > 0) {
                for (String tag : tagsList
                ) {
                    TestRunner.logger.assignCategory(tag);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        LogUtils.infoLog("",
                "\n+----------------------------------------------------------------------------------------------------------------------------+");
        LogUtils.infoLog("", "Mobile Tests Started: " + scenario.getName());
        LogUtils.infoLog("Mobile Test Environment",
                "Mobile Test is executed in OS:  " + System.getProperty("udid"));
    }

    public static void endScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            try {
                String scFileName = "ScreenShot_" + System.currentTimeMillis();
                String screenshotFilePath = ConfigReader.getValue("screenshotPath") + "\\" + scFileName + ".png";
                imagePath = HTMLReportUtil.testFailTakeScreenshot(screenshotFilePath);
                InputStream is = new FileInputStream(imagePath);
                byte[] imageBytes = IOUtils.toByteArray(is);
                Thread.sleep(4000);
                String base64 = Base64.getEncoder().encodeToString(imageBytes);
                TestRunner.logger.log(LogStatus.FAIL, HTMLReportUtil.failStringRedColor("Failed at point: " + pathForLogger + TestRunner.failMsg));
                byte[] screenshot = TakeScreenshot.takeScreenshot(imagePath);
                scenario.attach(screenshot, "image/png", "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            LogUtils.infoLog("TestEnded", "Closing the application");
        }
        DriverUtils.driver.quit();
        TestRunner.extent.endTest(TestRunner.logger);
    }


    public static void openReport() {
        String htmlReport = System.getProperty("user.dir") + "\\" + ConfigReader.getValue("extentReportPath");
        try {
            Runtime.getRuntime().exec("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe \"" + htmlReport);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static boolean click(By locator, String logStep) {
        DriverUtils.driver.findElement(locator).click();
        return true;
    }

    public static boolean isElementPresent(By locator, String logStep) {
        WebElement element = DriverUtils.driver.findElement(locator);
        if (element.isDisplayed()) {
            TestRunner.logger.log(LogStatus.PASS, "Element is Present " + logStep);
        }
        return true;
    }

}
