package page;

import io.qameta.allure.Step;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class FormFieldsPage extends BasePage<FormFieldsPage> {

    public FormFieldsPage(WebDriver browser) {
        super(browser);
    }

    @FindBy(xpath = "//*[@id=\"name-input\"]")
    private WebElement nameInput;

    @FindBy(css = "input[type=\"password\"]")
    private WebElement passwordInput;

    @FindBy(css = "input[name='fav_drink']")
    private List<WebElement> favDrinkCheckboxes;

    @FindBy(css = "input[name='fav_color']")
    private List<WebElement> colorRadios;

    @FindBy(id="automation")
    private WebElement automationSelect;

    @FindBy(id="email")
    private WebElement emailInput;

    @FindBy(id="message")
    private WebElement messageInput;

    @FindBy(css="#feedbackForm ul li")
    private List<WebElement> automationToolsBullets;

    @FindBy(id="submit-btn")
    private WebElement submitButton;

    @Step("Выбрать чекбокс напитка: {value}")
    public FormFieldsPage clickDrinksCheckbox(String value) {
        clickCheckbox(favDrinkCheckboxes, value);
        return this;
    }

    @Step("Выбрать радио-опцию цвета: {value}")
    public FormFieldsPage clickColorRadio(String value) {
        clickCheckbox(colorRadios, value);
        return this;
    }

    @Step("Выбрать в селекторе Automation: {text}")
    public FormFieldsPage selectAutomation(String text) {
        select(automationSelect, text);
        return this;
    }

    @Step("Получить самый длинный инструмент из блока Automation tools")
    public String getLongestToolName() {
        return util.TestUtils.longestToolName(automationToolsBullets);
    }

    @Step("Получить количество инструментов в блоке Automation tools")
    public int getToolsCount() {
        return util.TestUtils.toolsCount(automationToolsBullets);
    }

    @Step("Заполнить поле Message: {text}")
    public FormFieldsPage fillMessageInput(String text) {
        fillElement(messageInput, text);
        return this;
    }

    @Step("Заполнить поле Name: {name}")
    public FormFieldsPage fillNameInput(String name) {
        fillElement(nameInput, name);
        return this;
    }

    @Step("Заполнить поле Password")
    public FormFieldsPage fillPasswordInput(String password) {
        fillElement(passwordInput, password);
        return this;
    }

    @Step("Заполнить поле Email: {email}")
    public FormFieldsPage fillEmailInput(String email) {
        fillElement(emailInput, email);
        return this;
    }

    @Step("Нажать кнопку Submit")
    public FormFieldsPage clickSubmitButton() {
        clickElement(submitButton);
        return this;
    }

    @Step("Прочитать текст alert")
    public String getAlertText() {
        return waitAlert().getText();
    }

    @Step("Ожидать нативную валидацию поля Name (:invalid)")
    public boolean isNameInputNativeInvalid() {
        return waitNativeInvalid(nameInput);
    }


    protected boolean waitNativeInvalid(WebElement input) {
        try {
            wait.until(d -> (Boolean) ((JavascriptExecutor) d)
                    .executeScript("return arguments[0].matches(':invalid')", input));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
}
