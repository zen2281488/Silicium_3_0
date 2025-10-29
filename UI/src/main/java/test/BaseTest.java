package test;

import io.qameta.allure.Step;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import util.AllureScreenshotExtension;
import util.WebDriverExtension;

@ExtendWith({AllureScreenshotExtension.class, WebDriverExtension.class})
public class BaseTest {

    protected WebDriver driver;
    public boolean testFailed = false;

    @BeforeEach
    @Step("Инициализация вебдрайвера")
    public void baseBefore(WebDriver driver) {
        this.driver = driver;
    }


}