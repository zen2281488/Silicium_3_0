package xyzbank;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Issue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import page.xyzbank.ManagerPage;
import test.BaseTest;
import util.TestUtils;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static util.ConfProperties.getProperty;

@Epic("XYZ Bank")
public class ManagerUserTest extends BaseTest {
    private ManagerPage managerPage;

    @BeforeEach
    @Step("Создание экземпляров страниц")
    public void before() {
        managerPage = new ManagerPage(driver);
    }

    @BeforeEach
    @Step("Переход на страницу")
    public void setup(){
        driver.get(getProperty("xyzBaseUrl") + "manager");
    }

    @Feature("Manager")
    @Description("Тестирование работоспособности базовой функциональности создания нового клиента банка")
    @Severity(value = SeverityLevel.CRITICAL)
    @Test
    @Issue("XYZBank-UI-CreateCustomerUserP")
    @DisplayName("T-004")
    public void createCustomerUserPTest() {
        var postCode = TestUtils.getRandomPostCodeNumber();
        var firstName = TestUtils.nameFromPostCode(postCode);
        var lastName = "Testovlev";

        managerPage.clickAddCustomerButton()
                .fillFirstNameInput(firstName)
                .fillLastNameInput(lastName)
                .fillPostCodeInput(postCode)
                .clickAddCustomerSubmitButton();

        assertTrue(
                managerPage
                        .getAlertText()
                        .contains("Customer added successfully with customer id :"), "Некорректный текст alert");

        managerPage.clickOpenAccountButton()
                .selectCustomer(firstName + " " + lastName)
                .clickCustomersButton();
        assertTrue(managerPage
                .isCustomerExist(firstName, lastName)
                ,"Пользователь "+firstName+" "+lastName+" не был найден.");
    }

    @Feature("Manager")
    @Description("Тестирование работоспособности сортировки списка клиентов банка по First Name")
    @Severity(value = SeverityLevel.NORMAL)
    @Test
    @Issue("XYZBank-UI-SortTableP")
    @DisplayName("T-005")
    public void sortTablePTest() {
        managerPage.clickCustomersButton().sortFirstNameAsc();
        assertTrue(managerPage
                .isFirstNameSortedAsc()
                , "Некорректно работает asc сортировка по First Name клиентов в таблице клиентов");
        managerPage.clickCustomersButton().sortFirstNameDesc();
        assertTrue(managerPage
                .isFirstNameSortedDesc()
                , "Некорректно работает desc сортировка по First Name клиентов в таблице клиентов");
    }

    @Feature("Manager")
    @Description("Тестирование работоспособности удаления клиента банка")
    @Severity(value = SeverityLevel.CRITICAL)
    @Test
    @Issue("XYZBank-UI-DeleteCustomerUserP")
    @DisplayName("T-006")
    public void deleteCustomerPTest() {
        managerPage.clickCustomersButton();
        var customer = managerPage.findCustomerByFirstNameLenClosestToMean();
        managerPage
                .deleteCustomer(customer.getFirstName(), customer.getLastName())
                .clickOpenAccountButton();
        assertTrue(managerPage
                .isCustomerNotPresent(customer.getFirstName()+" "+customer.getLastName())
                , "Клиент не был удален");
    }
}
