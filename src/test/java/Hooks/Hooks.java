package Hooks;

import Utilities.KeywordUtils;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {

    @Before("@MobileTests")
    public static void beforeMobileMethod(Scenario scenario) {
        KeywordUtils.testCaseDescriptionStart(scenario);
        KeywordUtils.startServer();
    }

    @After("@MobileTests")
    public static void afterMobileMethod(Scenario scenario) {
        KeywordUtils.endScenario(scenario);
        KeywordUtils.stopServer();
    }
}
