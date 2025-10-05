package page;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class BasePage<T extends BasePage<T>> {
    protected WebDriver browser;
    protected WebDriverWait wait;
    protected Alert alert;

    public BasePage(WebDriver browser) {
        this.browser = browser;
        this.wait = new WebDriverWait(browser, Duration.ofSeconds(15));
        PageFactory.initElements(browser, this);
    }

    protected T clickElement(WebElement element){
        wait.until(visibilityOf(element));
        wait.until(elementToBeClickable(element));
        element.click();
        return (T) this;
    }

    protected T fillElement(WebElement element,Integer num){
        wait.until(visibilityOf(element));
        element.clear();
        element.sendKeys(Integer.toString(num));
        return (T) this;
    }

    protected T fillElement(WebElement element,String string){
        wait.until(visibilityOf(element));
        element.clear();
        element.sendKeys(string);
        return (T) this;
    }

    protected T clickCheckbox(List<WebElement> checkboxes, String value) {
        WebElement cb = checkboxes.stream()
                .filter(e -> value.equals(e.getAttribute("value")))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("checkbox value=" + value + " not found"));
        wait.until(elementToBeClickable(cb)).click();
        return (T) this;
    }

    protected T select(WebElement selector,String text) {
        wait.until(visibilityOf(selector));
        new Select(selector).selectByVisibleText(text);
        return (T) this;
    }
    protected Alert waitAlert() {
        return wait.until(alertIsPresent());
    }
}