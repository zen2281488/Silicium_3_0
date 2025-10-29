package xyzbank;

import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import page.xyzbank.ManagerPage;
import test.BaseTest;
import util.TestUtils;

import static util.ConfProperties.getProperty;

@Epic("XYZ Bank")
public class ManagerUserTest extends BaseTest {
    private ManagerPage managerPage;
    @BeforeEach
    @Step("Создание экземпляров страниц")
    public void before() {
        managerPage = new ManagerPage(driver);
    }

    @Feature("Manager")
    @Description("Тестирование работоспособности базовой функциональности создания нового клиента банка")
    @Severity(value = SeverityLevel.CRITICAL)
    @Test
    @Issue("XYZBank-UI-CreateCustomerUserP")
    @DisplayName("T-004")
    public void createCustomerUserPTest() {
        driver.get(getProperty("xyzBaseUrl")+"manager");
        var postCode = TestUtils.getRandomPostCodeNumber();
        var firstName = TestUtils.nameFromPostCode(postCode);
        var lastName = "Testovlev";

        managerPage.clickAddCustomerButton()
                .fillFirstNameInput(firstName)
                .fillLastNameInput(lastName)
                .fillPostCodeInput(postCode)
                .clickAddCustomerSubmitButton();

        Assertions.assertTrue(
                managerPage.getAlertText().contains("Customer added successfully with customer id :"),"Некорректный текст alert");

        managerPage.clickOpenAccountButton()
                .selectCustomer(firstName+" "+lastName)
                .clickCustomersButton()
                .findCustomerRow(firstName,lastName);
    }

    @Feature("Manager")
    @Description("Тестирование работоспособности сортировки списка клиентов банка по First Name")
    @Severity(value = SeverityLevel.NORMAL)
    @Test
    @Issue("XYZBank-UI-SortTableP")
    @DisplayName("T-005")
    public void sortTablePTest() {
        driver.get(getProperty("xyzBaseUrl")+"manager");
        managerPage.clickCustomersButton().sortFirstNameAsc();
        Assertions.assertTrue(managerPage.isFirstNameSortedAsc(),"Некорректно работает asc сортировка по First Name клиентов в таблице клиентов");
        managerPage.clickCustomersButton().sortFirstNameDesc();
        Assertions.assertTrue(managerPage.isFirstNameSortedDesc(),"Некорректно работает desc сортировка по First Name клиентов в таблице клиентов");

    }

    @Feature("Manager")
    @Description("Тестирование работоспособности удаления клиента банка")
    @Severity(value = SeverityLevel.CRITICAL)
    @Test
    @Issue("XYZBank-UI-DeleteCustomerUserP")
    @DisplayName("T-006")
    public void deleteCustomerPTest() {
        driver.get(getProperty("xyzBaseUrl")+"manager");

        managerPage
                .clickCustomersButton()
                .deleteCustomer("Neville","Longbottom").clickOpenAccountButton();
        Assertions.assertTrue(!managerPage.isCustomerPresent("Neville Longbottom"),"Клиент не был удален");

    }
}
