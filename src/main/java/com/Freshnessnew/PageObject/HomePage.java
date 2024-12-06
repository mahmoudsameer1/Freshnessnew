package com.Freshnessnew.PageObject;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.Freshnessnew.actiondriver.Action;
import com.Freshnessnew.Base.Base;

public class HomePage extends Base {

    public HomePage() {
        PageFactory.initElements(getDriver(), this); // Use getDriver() to initialize elements
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
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Use getDriver()
        action.click(getDriver(), add_button); // Use getDriver()
    }

    public boolean searchCreatedProduct(String textTitle) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10)); // Use getDriver()
        wait.until(driver -> {
            List<String> titles = product_titles.stream().map(WebElement::getText).collect(Collectors.toList());
            return titles.contains(textTitle);
        });
        boolean found = false;
        for (WebElement product : product_titles) {
            String result = product.getText();
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
        return action.getText(product_title);
    }

    public boolean searchProducts(String searchProductsTitle) throws InterruptedException {
        action.typestring(search, searchProductsTitle);
        Thread.sleep(5000);
        for (WebElement product : product_titles) {
            if (!product.getText().contains(searchProductsTitle)) {
                return false;
            }
        }
        return true;
    }

    public int[] deleteproduct(String editedTitle) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10)); // Use getDriver()
        wait.until(driver -> {
            List<String> titles = product_titles.stream().map(WebElement::getText).collect(Collectors.toList());
            return titles.contains(editedTitle);
        });
        int beforeDeletionCount = product_titles.size();
        WebElement lastDeleteButton = delete_product.get(delete_product.size() - 1);
        action.click(getDriver(), lastDeleteButton); // Use getDriver()
        Thread.sleep(3000);
        int afterDeletionCount = product_titles.size();
        return new int[] { beforeDeletionCount, afterDeletionCount };
    }
}
