package model.xyz;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

@Value
@Builder(toBuilder = true)
public class CustomerRow {
    String firstName;
    String lastName;
    String postCode;

    @Singular("accountNumber")
    List<String> accountNumbers;
    WebElement deleteButton;

    public void delete(WebDriverWait wait) {
        wait.until(ExpectedConditions.elementToBeClickable(deleteButton)).click();
    }
}