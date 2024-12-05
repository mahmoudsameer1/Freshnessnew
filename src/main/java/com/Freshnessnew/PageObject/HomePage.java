package com.Freshnessnew.PageObject;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.Freshnessnew.actiondriver.Action;
import com.Freshnessnew.Base.Base;

public class HomePage extends Base{
	
	public HomePage() {
		 PageFactory.initElements(driver, this);
	}
	
	Action action = new Action();
	
	@FindBy(css = "a[href='/add'] > svg")
	private WebElement add_button;
	
	@FindBy(css = ".sc-gxYJeL")
	private WebElement search;
	
    @FindBy(xpath = "//div[@class='sc-kuWgmH brWvPg mt-4 cursor-pointer']")
	private List<WebElement> product_titles;
	
	@FindBy(xpath = "//div[@class='sc-kuWgmH brWvPg mt-4 cursor-pointer']")
	private WebElement product_title;
	
	@FindBy(xpath = ".//button[@class='sc-crozmw sc-jJTsDX jrDktB TnOul flex justify-center items-center h-9 w-9 transition ease-in-out delay-150 duration-300']")
	private List<WebElement> delete_product;


	public void createNewProductButton() {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));		
		action.click(driver, add_button);
	}
	
	public boolean searchCreatedProduct(String textTitle) throws InterruptedException {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    wait.until(driver -> {
	        List<String> titles = product_titles.stream().map(WebElement::getText).collect(Collectors.toList());
	        return titles.contains(textTitle);
	    });
	    boolean found = false;
	    for (int i = 0; i < product_titles.size(); i++) {
	        String result = product_titles.get(i).getText();
	        if (result.equals(textTitle)) {
	            found = true;
	            break;
	        }
	    }
	    return found;
	}
	
	public String searchForProduct(String actualProductTitle) throws InterruptedException {
		action.typestring(search, actualProductTitle);
		Thread.sleep(5000);
		String expectedTitle = action.getText(product_title);
		return expectedTitle;
	}
	
	public boolean searchProducts(String searchProductsTitle) throws InterruptedException {
	    action.typestring(search,searchProductsTitle);
	    Thread.sleep(5000);
	    boolean searchTermFound = false;
	    for (int i = 0; i < product_titles.size(); i++) {
	        String result = product_titles.get(i).getText();
	        if (result.contains(searchProductsTitle)) {
	            searchTermFound = true;
	        } else {
	            searchTermFound = false;
	            break;
	        }
	    }
	    return searchTermFound;
	}
    
	public int[] deleteproduct(String editedTitle) throws InterruptedException {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    wait.until(driver -> {
	        List<String> titles = product_titles.stream().map(WebElement::getText).collect(Collectors.toList());
	        return titles.contains(editedTitle);
	    });
	    int beforeDeletionCount = product_titles.size();
	    WebElement lastDeleteButton = delete_product.get(delete_product.size() - 1);
	    action.click(driver, lastDeleteButton);
	    Thread.sleep(3000);
	    int afterDeletionCount = product_titles.size();
	    return new int[]{beforeDeletionCount, afterDeletionCount};
	}

}
