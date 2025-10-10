package anui;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Step;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import page.FormFieldsPage;

import static util.ConfProperties.getProperty;

public class FormFieldsTest extends BaseTest {
    private FormFieldsPage formFieldsPage;

    @BeforeEach
    @Step("Создание экземпляров страниц")
    public void before() {
        formFieldsPage = new FormFieldsPage(driver);
    }

    @Feature("Form Fields")
    @Description("Тестирование работоспособности базовой функциональности формы Form Fields.")
    @Severity(value = SeverityLevel.CRITICAL)
    @Test
    @Issue("PracticeAutomation-UI-FormFieldsSmoke")
    @DisplayName("T-001")
    public void fillFormPTest() {
        driver.get(getProperty("baseUrl")+"form-fields/");
        formFieldsPage
                .fillNameInput("Testovlev Test Testovich")
                .fillPasswordInput("qwertyTest123")
                .clickDrinksCheckbox("Milk")
                .clickDrinksCheckbox("Coffee")
                .clickColorRadio("Yellow")
                .selectAutomation("Yes")
                .fillEmailInput("test@test.ru")
                .fillMessageInput(formFieldsPage.getToolsCount()+" "+formFieldsPage.getLongestToolName())
                .clickSubmitButton();
        Assertions.assertEquals("Message received!",formFieldsPage.getAlertText());
    }

    @Feature("Form Fields")
    @Description("Тестирование работоспособности функциональности формы Form Fields при заполнении исключительно обязательного поля Name.")
    @Severity(value = SeverityLevel.CRITICAL)
    @Test
    @Issue("PracticeAutomation-UI-FormFieldsFillNameP")
    @DisplayName("T-002")
    public void fillNamePTest() {
        driver.get(getProperty("baseUrl")+"form-fields/");
        formFieldsPage
                .fillNameInput("Testovlev Test Testovich")
                .clickSubmitButton();
        Assertions.assertEquals("Message received!",formFieldsPage.getAlertText());
    }

    @Feature("Form Fields")
    @Description("Тестирование блокирования функционала и подсветки нативной валидаци поля Nameи при его пропуске и полном заполнении других полей.")
    @Severity(value = SeverityLevel.CRITICAL)
    @Test
    @Issue("PracticeAutomation-UI-FormFieldsFillNameN")
    @DisplayName("T-003")
    public void fillNameNTest() {
        driver.get(getProperty("baseUrl")+"form-fields/");
        formFieldsPage
                .fillPasswordInput("qwertyTest123")
                .clickDrinksCheckbox("Milk")
                .clickDrinksCheckbox("Coffee")
                .clickColorRadio("Yellow")
                .selectAutomation("Yes")
                .fillEmailInput("test@test.ru")
                .fillMessageInput(formFieldsPage.getToolsCount()+" "+formFieldsPage.getLongestToolName())
                .clickSubmitButton();
        Assertions.assertTrue(formFieldsPage.isNameInputNativeInvalid(),"Пустое поле 'Name' не стало :invalid после нажатия на Submit.");
    }
}
