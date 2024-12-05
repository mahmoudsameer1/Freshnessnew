package com.Freshnessnew.actiondriver;


import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.Freshnessnew.Base.Base;

public class Action extends Base {

	public void uploadFile(WebElement ele, String text) {

		ele.sendKeys(text);

	}

	public void click(WebDriver driver, WebElement ele) {

		Actions act = new Actions(driver);
		act.moveToElement(ele).click().build().perform();

	}

	public String getText(WebElement ele) {

		String text = ele.getText();
		return text;
	}

	public boolean findElement(WebElement ele, String text) {
		boolean flag = false;
		try {
			ele.isDisplayed();
			flag = true;
		} catch (Exception e) {
			// System.out.println("Location not found: "+locatorName);
			flag = false;
		} finally {
			if (flag) {
				System.out.println("Successfully Found element at");

			} else {
				System.out.println("Unable to locate element at");
			}
		}
		return flag;
	}

	public boolean typestring(WebElement ele, String text) {
		boolean flag = false;
		try {
			flag = ele.isDisplayed();
			ele.clear();
			ele.sendKeys(text);
			flag = true;
		} catch (Exception e) {
			System.out.println("Location Not found");
			flag = false;
		} finally {
			if (flag) {
				System.out.println("Successfully entered value");
			} else {
				System.out.println("Unable to enter value");
			}

		}
		return flag;
	}

	public boolean launchUrl(WebDriver driver, String url) {
		boolean flag = false;
		try {
			driver.navigate().to(url);
			flag = true;
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			if (flag) {
				System.out.println("Successfully launched \"" + url + "\"");
			} else {
				System.out.println("Failed to launch \"" + url + "\"");
			}
		}
	}

	public String getTitle(WebDriver driver) {
		boolean flag = false;

		String text = driver.getTitle();
		if (flag) {
			System.out.println("Title of the page is: \"" + text + "\"");
		}
		return text;
	}

	public boolean click1(WebElement locator, String locatorName) {
		boolean flag = false;
		try {
			locator.click();
			flag = true;
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			if (flag) {
				System.out.println("Able to click on \"" + locatorName + "\"");
			} else {
				System.out.println("Click Unable to click on \"" + locatorName + "\"");
			}
		}

	}

	public void implicitWait(WebDriver driver, int timeOut) {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	public void pageLoadTimeOut(WebDriver driver, int timeOut) {
		driver.manage().timeouts().pageLoadTimeout(timeOut, TimeUnit.SECONDS);
	}
}
