package page.xyzbank;

import io.qameta.allure.Step;
import model.xyz.CustomerRow;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import page.BasePage;
import util.TestUtils;

import java.util.*;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

public class ManagerPage extends BasePage<ManagerPage> {
    public ManagerPage(WebDriver browser) {
        super(browser);
    }

    @FindBy(css = "[ng-click='addCust()']")
    private WebElement addCustomerButton;
    @FindBy(css = "[ng-click='openAccount()']")
    private WebElement openAccountButton;

    @FindBy(css = "[ng-click='showCust()']")
    private WebElement customersButton;
    @FindBy(css = "[ng-model='fName']")
    private WebElement firstNameInput;
    @FindBy(css = "[ng-model='lName']")
    private WebElement lastNameInput;
    @FindBy(css = "[ng-model='postCd']")
    private WebElement postCodeInput;
    @FindBy(css = "[type='submit']")
    private WebElement addCustomerSubmitButton;

    @FindBy(css = "[ng-model='searchCustomer']")
    private WebElement searchCustomerInput;

    @FindBy(id = "userSelect")
    private WebElement customerSelector;
    @FindBy(id = "currency")
    private WebElement currencySelector;
    @FindBy(css = "table")
    private WebElement customersTable;
    @FindBy(css = "thead tr td:nth-child(1) a")
    private WebElement firstNameHeaderLink;
    @FindBy(css = "thead tr td:nth-child(2) a")
    private WebElement lastNameHeaderLink;
    @FindBy(css = "thead tr td:nth-child(3) a")
    private WebElement postCodeHeaderLink;

    @Step("Кликнуть по кнопке Add Customer")
    public ManagerPage clickAddCustomerButton() {
        clickElement(addCustomerButton);
        return this;
    }

    @Step("Кликнуть по кнопке Open Account")
    public ManagerPage clickOpenAccountButton() {
        clickElement(openAccountButton);
        return this;
    }

    @Step("Кликнуть по кнопке Customers")
    public ManagerPage clickCustomersButton() {
        clickElement(customersButton);
        return this;
    }

    @Step("Заполнить поле First Name: {text}")
    public ManagerPage fillFirstNameInput(String text) {
        fillElement(firstNameInput, text);
        return this;
    }

    @Step("Заполнить поле Last Name: {text}")
    public ManagerPage fillLastNameInput(String text) {
        fillElement(lastNameInput, text);
        return this;
    }

    @Step("Заполнить поле Post Code: {value}")
    public ManagerPage fillPostCodeInput(long value) {
        fillElement(postCodeInput, value);
        return this;
    }

    @Step("Кликнуть по кнопке Add Customer")
    public ManagerPage clickAddCustomerSubmitButton() {
        clickElement(addCustomerSubmitButton);
        return this;
    }

    @Step("Прочитать текст alert")
    public String getAlertText() {
        return acceptAlertAndGetText();
    }

    @Step("Выбрать клиента в селекторе Customer")
    public ManagerPage selectCustomer(String text) {
        select(customerSelector, text);
        return this;
    }

    @Step("Проверить, что клиент '{text}' отсутствует в селекторе Customer")
    public boolean isCustomerNotPresent(String text) {
        return !isStrPresent(customerSelector, text);
    }

    @Step("Отсортировать First Name по возрастанию")
    public ManagerPage sortFirstNameAsc() {
        return sortHeaderUntil(1, firstNameHeaderLink, true);
    }

    @Step("Отсортировать First Name по убыванию")
    public ManagerPage sortFirstNameDesc() {
        return sortHeaderUntil(1, firstNameHeaderLink, false);
    }

    public boolean isFirstNameSortedAsc() {
        return isColumnSorted(1, true);
    }

    public boolean isFirstNameSortedDesc() {
        return isColumnSorted(1, false);
    }

    private ManagerPage sortHeaderUntil(int colIndex, WebElement headerLink, boolean asc) {
        wait.until(visibilityOf(customersTable));

        for (int i = 0; i < 3; i++) {
            if (isColumnSorted(colIndex, asc)) return this;
            String before = customersTable.getText();
            wait.until(elementToBeClickable(headerLink)).click();
            wait.until(d -> !customersTable.getText().equals(before));
        }

        return this;
    }

    private boolean isColumnSorted(int colIndex, boolean asc) {
        var values = readColumn(colIndex);
        var expected = new ArrayList<>(values);
        expected.sort(String::compareToIgnoreCase);
        if (!asc) Collections.reverse(expected);
        return values.equals(expected);
    }

    private List<String> readColumn(int colIndex) {
        return customersTable.findElements(org.openqa.selenium.By.cssSelector("tbody tr"))
                .stream()
                .map(tr -> tr.findElement(org.openqa.selenium.By.cssSelector("td:nth-child(" + colIndex + ")"))
                        .getText().trim())
                .toList();
    }

    @Step("Прочитать строки таблицы Customers")
    public List<CustomerRow> customerRows() {
        wait.until(visibilityOf(customersTable));
        return TestUtils.parseCustomerRows(customersTable);
    }

    @Step("Найти строку клиента: {firstName} {lastName}")
    public CustomerRow findCustomerRow(String firstName, String lastName) {
        return TestUtils.parseCustomerRow(customerRows(), firstName, lastName);
    }

    @Step("Убедится что клиент {firstName} {lastName} существует")
    public boolean isCustomerExist(String firstName, String lastName) {
        try {
            TestUtils.parseCustomerRow(customerRows(), firstName, lastName);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
    @Step("Удалить клиента: {firstName} {lastName}")
    public ManagerPage deleteCustomer(String firstName, String lastName) {
        var row = findCustomerRow(firstName, lastName);
        row.delete(wait);
        wait.until(driver ->
                customersTable.getText().lines().noneMatch(line ->
                        line.contains(firstName) && line.contains(lastName)
                )
        );
        return this;
    }

    @Step("Найти клиента с длиной First Name, ближайшей к среднему")
    public CustomerRow findCustomerByFirstNameLenClosestToMean() {
        return TestUtils.closestByFirstNameLenToMean(customerRows())
                .orElseThrow(() -> new NoSuchElementException("Таблица Customers пуста"));
    }

}
