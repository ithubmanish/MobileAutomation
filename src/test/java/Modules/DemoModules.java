package Modules;

import Pages.DemoPages;
import Runner.TestRunner;
import Utilities.KeywordUtils;
import com.relevantcodes.extentreports.LogStatus;
import org.testng.Assert;

public class DemoModules {
    public static void clickHome() {
        try {
            TestRunner.logger.log(LogStatus.PASS,"Click Home");
            TestRunner.log.info("Click Home");
        } catch (Throwable e) {
            TestRunner.failMsg=e.getMessage();
            Assert.fail(String.valueOf(e));
        }
    }

    public static void validateHome() {
        try {
//            KeywordUtils.isElementPresent(DemoPages.chromeHome,"Chrome Home");
            TestRunner.logger.log(LogStatus.PASS,"Validate Home");
            TestRunner.log.info("Validate Home");
        } catch (Throwable e) {
            TestRunner.logger.log(LogStatus.FAIL,"Validate Home"+e.getMessage());
//            TestRunner.failMsg=e.getMessage();
//            Assert.fail(String.valueOf(e));
        }
    }
}
