package com.Freshnessnew.Base;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class Base {
    
    public static  WebDriver driver;
    public Logger logger;
    public Properties prop;
    
    @BeforeClass
    @Parameters({"browser"})
    public void launchApp(String br) throws IOException {

        // Loading config.properties file
        FileReader file = new FileReader("./src/test/resources/config.properties");
        prop = new Properties();
        prop.load(file);

        logger = LogManager.getLogger(this.getClass());

        switch (br.toLowerCase()) {
            case "chrome":
                // Set up Chrome options for headless mode
                ChromeOptions options = new ChromeOptions();
				/*
				 * options.addArguments("--headless");
				 * options.addArguments("--window-size=1920x1080");
				 * options.addArguments("--disable-gpu");
				 */
                driver = new ChromeDriver(options);
                break;

            case "edge":
                driver = new EdgeDriver();
                break;

            case "firefox":
                driver = new FirefoxDriver();
                break;

            default:
                System.out.println("Invalid browser name");
                return;
        }

        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        driver.get(prop.getProperty("URL"));
    }
    
    @AfterClass
    public void tearDown() {
        driver.quit();
    }

    // Method to take a screenshot and save it to a file
    public String takeScreenshot(String testName) {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String screenshotPath = Paths.get(System.getProperty("user.dir"), "allure-results//screenshots", testName + "_" + timestamp + ".png").toString();
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            File destination = new File(screenshotPath);
            FileHandler.copy(source, destination);
            logger.info("Screenshot captured: " + screenshotPath);
        } catch (IOException e) {
            logger.error("Failed to capture screenshot", e);
        }
        return screenshotPath;
    }
}
