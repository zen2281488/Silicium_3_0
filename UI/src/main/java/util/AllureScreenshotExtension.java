package util;

import io.qameta.allure.Attachment;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.*;

public class AllureScreenshotExtension implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext context) {
        if (context.getExecutionException().isPresent()) {
            WebDriver driver = WebDriverExtension.getStoredDriver(context);
            if (driver != null) {
                attachOnFailure(driver);
            }
        }
    }

    private void attachOnFailure(WebDriver driver) {
        try {
            screenshot(driver);
        } catch (UnhandledAlertException e) {
            try {
                Alert a = driver.switchTo().alert();
                alertText(a.getText());
                a.accept();
            } catch (Exception ignored) {}
            try { screenshot(driver); } catch (Exception ignored) {}
        } catch (WebDriverException e) {
            pageSource(driver.getPageSource());
        }
    }

    @Attachment(value = "Failure Screenshot", type = "image/png")
    private byte[] screenshot(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    @Attachment(value = "Alert text", type = "text/plain")
    private String alertText(String text) { return text; }

    @Attachment(value = "Page source on failure", type = "text/html", fileExtension = ".html")
    private String pageSource(String html) { return html; }
}
