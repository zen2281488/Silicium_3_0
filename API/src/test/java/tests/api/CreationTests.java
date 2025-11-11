package tests.api;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import utils.RestAssuredUtils;

@Epic("CRUD")
public class CreationTests extends BaseTest {

    @Feature("Создание сущности")
    @Test
    @DisplayName("T-007")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Тестирование создания сущности")
    @Issue("API-create-item-p")
    public void createEntityPTest() {
        var answerApiItem = RestAssuredUtils.createItem(newLocalItem);
        var entityId = Integer.parseInt(answerApiItem);
        var dbItem = itemService.findEntity(entityId);
        var dbAddition = dbItem.getAddition();

        Assertions.assertAll(
                () -> Assertions.assertNotNull(dbItem),
                () -> Assertions.assertNotNull(dbAddition),
                () -> Assertions.assertEquals(entityId, dbItem.getId()),
                () -> Assertions.assertEquals(newLocalItem.getTitle(), dbItem.getTitle()),
                () -> Assertions.assertEquals(newLocalItem.getVerified(), dbItem.getVerified()),
                () -> Assertions.assertEquals(newLocalItem.getAddition().getAdditionalNumber(), dbAddition.getAdditionalNumber()),
                () -> Assertions.assertEquals(newLocalItem.getAddition().getAdditionalInfo(), dbAddition.getAdditionalInfo())
        );
        itemService.deleteEntity(dbItem);
    }

    @Feature("Удаление сущности")
    @Test
    @DisplayName("T-008")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Тестирование удаления сущности")
    @Issue("API-delete-item-p")
    public void deleteEntityPTest() {

        var entityId = Integer.parseInt(RestAssuredUtils.createItem(newLocalItem));

        Assertions.assertNotNull(itemService.findEntity(entityId));

        RestAssuredUtils.deleteItem(entityId);

        Assertions.assertTrue(
                itemService.findAllEntities().stream().noneMatch(item -> item.getId().equals(entityId))
        );
    }
}
