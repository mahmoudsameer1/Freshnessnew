package com.Freshnessnew.Base;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import browserstack.shaded.org.json.JSONArray;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.io.FileHandler;
import java.io.File;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Base {

    private static ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();
    public Logger logger;
    public Properties prop;

    // BrowserStack credentials
    public static final String BROWSERSTACK_USERNAME = System.getenv("BROWSERSTACK_USERNAME");
    public static final String BROWSERSTACK_ACCESS_KEY = System.getenv("BROWSERSTACK_ACCESS_KEY");
    public static final String BROWSERSTACK_URL = "https://" + BROWSERSTACK_USERNAME + ":" + BROWSERSTACK_ACCESS_KEY
                                                  + "@hub-cloud.browserstack.com/wd/hub";
    public static final String BROWSERSTACK_MEDIA_URL = "media://45138f377e8f375deb6a8f45c0c7d22c68295285";

    public static WebDriver getDriver() {
        return threadLocalDriver.get();
    }

    @BeforeClass
    @Parameters({ "browser", "useBrowserStack", "os", "osVersion" })
    public void launchApp(String browser, boolean useBrowserStack, String os, String osVersion) throws IOException {

        // Load config.properties file
        FileReader file = new FileReader("./src/test/resources/config.properties");
        prop = new Properties();
        prop.load(file);
        logger = LogManager.getLogger(this.getClass());
        WebDriver driver = null;

        if (useBrowserStack) {
            // BrowserStack setup
            DesiredCapabilities capabilities = new DesiredCapabilities();
            HashMap<String, Object> browserstackOptions = new HashMap<>();

            switch (browser.toLowerCase()) {
                case "chrome":
                    capabilities.setCapability("browserName", "Chrome");
                    capabilities.setCapability("browserVersion", "latest");
                    // Set Chrome to headless mode
                    capabilities.setCapability("goog:chromeOptions", new HashMap<String, Object>() {{
                        put("args", new String[]{"--headless", "--window-size=1920x1080", "--disable-gpu"});
                    }});
                    break;
                case "edge":
                    capabilities.setCapability("browserName", "Edge");
                    capabilities.setCapability("browserVersion", "latest");
                    // Set Edge to headless mode
                    capabilities.setCapability("ms:edgeOptions", new HashMap<String, Object>() {{
                        put("args", new String[]{"--headless", "--window-size=1920x1080", "--disable-gpu"});
                    }});
                    break;
                case "firefox":
                    capabilities.setCapability("browserName", "Firefox");
                    capabilities.setCapability("browserVersion", "latest");
                    // Set Firefox to headless mode
                    capabilities.setCapability("moz:firefoxOptions", new HashMap<String, Object>() {{
                        put("args", new String[]{"--headless", "--width=1920", "--height=1080"});
                    }});
                    break;
                case "safari":
                    capabilities.setCapability("browserName", "Safari");
                    capabilities.setCapability("browserVersion", "latest");
                    // Safari does not support headless mode in BrowserStack directly, so no need to set headless
                    break;
                default:
                    throw new IllegalArgumentException("Invalid browser name for BrowserStack.");
            }
            
            browserstackOptions.put("os", os);
            browserstackOptions.put("osVersion", osVersion);
            browserstackOptions.put("projectName", "Your Project Name");
            browserstackOptions.put("buildName", "Build Name");
            browserstackOptions.put("seleniumVersion", "4.27.0");
            browserstackOptions.put("uploadMedia", new JSONArray().put(BROWSERSTACK_MEDIA_URL));
            capabilities.setCapability("bstack:options", browserstackOptions);
            driver = new RemoteWebDriver(new URL(BROWSERSTACK_URL), capabilities);
        } else {
            // Local browser setup
            switch (browser.toLowerCase()) {
                case "chrome":
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--headless", "--window-size=1920x1080", "--disable-gpu");
                    driver = new ChromeDriver(chromeOptions);
                    break;
                case "edge":
                    EdgeOptions edgeOptions = new EdgeOptions();
                    edgeOptions.addArguments("--headless", "--window-size=1920x1080", "--disable-gpu");
                    driver = new EdgeDriver(edgeOptions);
                    break;
                case "firefox":
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.addArguments("--headless", "--width=1920", "--height=1080");
                    driver = new FirefoxDriver(firefoxOptions);
                    break;
                case "safari":
                    throw new UnsupportedOperationException("Safari is not supported for local execution.");
                default:
                    throw new IllegalArgumentException("Invalid browser name for local execution.");
            }
        }

        threadLocalDriver.set(driver);

        // Configure browser
        getDriver().manage().window().maximize();
        getDriver().manage().deleteAllCookies();
        getDriver().get(prop.getProperty("URL"));
    }
    
    // Method to take a screenshot and save it to a file
    public String takeScreenshot(String testName) {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String screenshotPath = Paths
                .get(System.getProperty("user.dir"), "allure-results/screenshots", testName + "_" + timestamp + ".png")
                .toString();
        try {
            TakesScreenshot ts = (TakesScreenshot) getDriver();
            File source = ts.getScreenshotAs(OutputType.FILE);
            File destination = new File(screenshotPath);
            FileHandler.copy(source, destination);
            logger.info("Screenshot captured: " + screenshotPath);
        } catch (IOException e) {
            logger.error("Failed to capture screenshot", e);
        }
        return screenshotPath;
    }

    @AfterClass
    public void tearDown() {
        if (getDriver() != null) {
            getDriver().quit();
            threadLocalDriver.remove();
        }
    }
    
}
