package Runner;

import Utilities.KeywordUtils;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import org.apache.logging.log4j.LogManager;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

@CucumberOptions(features = "src/test/resources/Feature", glue = {"StepDefinitions", "Hooks"},
        plugin = {"pretty", "html:target/cucumber-html-report", "json:target/cucumber.json"},
        tags = "@Case1", monochrome = true)
public class TestRunner extends AbstractTestNGCucumberTests {
    public static ExtentReports extent;
    public static ExtentTest logger;
    public static String failMsg;
    public static org.apache.logging.log4j.Logger log = LogManager.getLogger();

    @BeforeSuite
    public void before() {
        KeywordUtils.cleanDirectory();
        KeywordUtils.extentReportInitialization();
    }
    @AfterSuite
    public void after() {
        KeywordUtils.extentReportClosure();
        KeywordUtils.openReport();
    }
}
