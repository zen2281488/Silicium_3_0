package tests.api;

import io.qameta.allure.*;
import models.api.Item;
import org.junit.jupiter.api.*;
import utils.RestAssuredUtils;
import utils.data.api.TestData;

import java.util.List;

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
        var answerApiItem = RestAssuredUtils.createItem(newLocalItem);
        var entityId = Integer.parseInt(answerApiItem);


        Assertions.assertNotNull(itemService.findEntity(entityId));

        RestAssuredUtils.deleteItem(entityId);

        Assertions.assertTrue(
                itemService.findAllEntities().stream().noneMatch(item -> item.getId().equals(entityId))
        );
    }

    @Feature("Получение сущности")
    @Test
    @DisplayName("T-009")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Тестирование получения сущности по айди")
    @Issue("API-id-get-item-p")
    public void idGetEntityPTest() {
        var answerApiItem = RestAssuredUtils.createItem(newLocalItem);
        var entityId = Integer.parseInt(answerApiItem);
        Assertions.assertNotNull(itemService.findEntity(entityId));

        Item item = RestAssuredUtils.idGetItem(entityId);

        var dbItem = itemService.findEntity(item.getId());

        Assertions.assertAll(
                () -> Assertions.assertNotNull(dbItem),
                () -> Assertions.assertNotNull(dbItem.getAddition()),
                () -> Assertions.assertEquals(entityId, dbItem.getId()),
                () -> Assertions.assertEquals(item.getTitle(), dbItem.getTitle()),
                () -> Assertions.assertEquals(item.getVerified(), dbItem.getVerified()),
                () -> Assertions.assertEquals(item.getAddition().getAdditionalNumber(), dbItem.getAddition().getAdditionalNumber()),
                () -> Assertions.assertEquals(item.getAddition().getAdditionalInfo(), dbItem.getAddition().getAdditionalInfo())
        );
        itemService.deleteEntity(dbItem);

    }

    @Feature("Получение всех сущностей")
    @Test
    @DisplayName("T-010")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Тестирование получения всех сущностей")
    @Issue("API-getAll-item-p")
    public void getAllEntityPTest() {
        var firstAnswerApiItem = RestAssuredUtils.createItem(newLocalItem);
        var firstEntityId = Integer.parseInt(firstAnswerApiItem);
        var secondAnswerApiItem = RestAssuredUtils.createItem(newLocalItem);
        var secondEntityId = Integer.parseInt(secondAnswerApiItem);

        Assertions.assertNotNull(itemService.findEntity(firstEntityId));
        Assertions.assertNotNull(itemService.findEntity(secondEntityId));

        List<models.api.Item> itemsFromApi = RestAssuredUtils.getAll();
        List<models.db.Item>  itemsFromDb  = itemService.findAllEntities();

        List<Integer> createdIds = List.of(firstEntityId, secondEntityId);

        createdIds.forEach(id -> {
            models.db.Item dbItem = itemsFromDb.stream()
                    .filter(i -> i.getId().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new AssertionError(
                            "В БД нет сущности с id = " + id
                    ));

            models.api.Item apiItem = itemsFromApi.stream()
                    .filter(i -> i.getId().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new AssertionError(
                            "В ответе API нет сущности с id = " + id
                    ));

            Assertions.assertAll(
                    () -> Assertions.assertEquals(dbItem.getId(), apiItem.getId(), "id"),
                    () -> Assertions.assertEquals(dbItem.getTitle(), apiItem.getTitle(), "title"),
                    () -> Assertions.assertEquals(dbItem.getVerified(), apiItem.getVerified(), "verified"),
                    () -> Assertions.assertNotNull(dbItem.getAddition(), "addition в БД не должен быть null"),
                    () -> Assertions.assertNotNull(apiItem.getAddition(), "addition в API не должен быть null"),
                    () -> Assertions.assertEquals(
                            dbItem.getAddition().getAdditionalInfo(),
                            apiItem.getAddition().getAdditionalInfo(),
                            "additionalInfo"
                    ),
                    () -> Assertions.assertEquals(
                            dbItem.getAddition().getAdditionalNumber(),
                            apiItem.getAddition().getAdditionalNumber(),
                            "additionalNumber"
                    )
            );
        });

        itemService.deleteEntity(itemService.findEntity(firstEntityId));
        itemService.deleteEntity(itemService.findEntity(secondEntityId));
    }

    @Feature("Обновление сущности")
    @Test
    @DisplayName("T-011")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Тестирование обновления сущности и её дополнений")
    @Issue("API-patch-item-p")
    public void patchEntityPTest() {
        var answerApiItem = RestAssuredUtils.createItem(newLocalItem);
        var entityId = Integer.parseInt(answerApiItem);

        Assertions.assertNotNull(
                itemService.findEntity(entityId),
                "Сущность не создалась в БД"
        );

        Item updatedItem = TestData.patchedEntity(newLocalItem);

        RestAssuredUtils.patchItem(entityId, updatedItem);

        var patchedDbItem = itemService.findEntity(entityId);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(patchedDbItem, "После PATCH сущность отсутствует в БД"),
                () -> Assertions.assertNotNull(patchedDbItem.getAddition(), "После PATCH addition в БД null"),
                () -> Assertions.assertEquals(entityId, patchedDbItem.getId(), "id"),
                () -> Assertions.assertEquals(
                        updatedItem.getTitle(),
                        patchedDbItem.getTitle(),
                        "title"
                ),
                () -> Assertions.assertEquals(
                        updatedItem.getVerified(),
                        patchedDbItem.getVerified(),
                        "verified"
                ),
                () -> Assertions.assertEquals(
                        updatedItem.getAddition().getAdditionalInfo(),
                        patchedDbItem.getAddition().getAdditionalInfo(),
                        "additionalInfo"
                ),
                () -> Assertions.assertEquals(
                        updatedItem.getAddition().getAdditionalNumber(),
                        patchedDbItem.getAddition().getAdditionalNumber(),
                        "additionalNumber"
                )
        );

        Item apiAfterPatch = RestAssuredUtils.idGetItem(entityId);

        Assertions.assertAll(
                () -> Assertions.assertEquals(
                        updatedItem.getTitle(),
                        apiAfterPatch.getTitle(),
                        "API title"
                ),
                () -> Assertions.assertEquals(
                        updatedItem.getVerified(),
                        apiAfterPatch.getVerified(),
                        "API verified"
                ),
                () -> Assertions.assertEquals(
                        updatedItem.getAddition().getAdditionalInfo(),
                        apiAfterPatch.getAddition().getAdditionalInfo(),
                        "API additionalInfo"
                ),
                () -> Assertions.assertEquals(
                        updatedItem.getAddition().getAdditionalNumber(),
                        apiAfterPatch.getAddition().getAdditionalNumber(),
                        "API additionalNumber"
                )
        );

        itemService.deleteEntity(patchedDbItem);
    }


}
