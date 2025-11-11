package tests.api;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Description;
import models.api.Item;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
        String createdIdAsString = RestAssuredUtils.createItem(newLocalItem);
        int entityId = Integer.parseInt(createdIdAsString);

        var dbItem = itemService.findEntity(entityId);
        Assertions.assertNotNull(dbItem, "Сущность не найдена в БД после создания");

        var dbAddition = dbItem.getAddition();
        Assertions.assertNotNull(dbAddition, "Поле addition в БД равно null после создания");

        Assertions.assertAll(
                () -> Assertions.assertEquals(
                        entityId,
                        dbItem.getId(),
                        "ID сущности в БД не совпадает с ожидаемым после создания"
                ),
                () -> Assertions.assertEquals(
                        newLocalItem.getTitle(),
                        dbItem.getTitle(),
                        "Поле title в БД не совпадает с тестовыми данными при создании"
                ),
                () -> Assertions.assertEquals(
                        newLocalItem.getVerified(),
                        dbItem.getVerified(),
                        "Поле verified в БД не совпадает с тестовыми данными при создании"
                ),
                () -> Assertions.assertEquals(
                        newLocalItem.getAddition().getAdditionalNumber(),
                        dbAddition.getAdditionalNumber(),
                        "Поле addition.additionalNumber в БД не совпадает с тестовыми данными при создании"
                ),
                () -> Assertions.assertEquals(
                        newLocalItem.getAddition().getAdditionalInfo(),
                        dbAddition.getAdditionalInfo(),
                        "Поле addition.additionalInfo в БД не совпадает с тестовыми данными при создании"
                )
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
        String createdIdAsString = RestAssuredUtils.createItem(newLocalItem);
        int entityId = Integer.parseInt(createdIdAsString);

        Assertions.assertNotNull(
                itemService.findEntity(entityId),
                "Сущность не найдена в БД перед удалением"
        );

        RestAssuredUtils.deleteItem(entityId);

        boolean entityExists = itemService.findAllEntities().stream()
                .anyMatch(item -> entityId == item.getId());

        Assertions.assertFalse(
                entityExists,
                "Сущность с id = " + entityId + " не была удалена из БД"
        );
    }

    @Feature("Получение сущности по id")
    @Test
    @DisplayName("T-009")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Тестирование получения сущности по id")
    @Issue("API-id-get-item-p")
    public void idGetEntityPTest() {
        String createdIdAsString = RestAssuredUtils.createItem(newLocalItem);
        int entityId = Integer.parseInt(createdIdAsString);

        var dbItemBefore = itemService.findEntity(entityId);
        Assertions.assertNotNull(dbItemBefore, "Сущность не найдена в БД перед GET по id");

        Item apiItem = RestAssuredUtils.idGetItem(entityId);

        var dbItem = itemService.findEntity(apiItem.getId());

        Assertions.assertAll(
                () -> Assertions.assertNotNull(
                        dbItem,
                        "Сущность не найдена в БД после GET по id"
                ),
                () -> Assertions.assertNotNull(
                        dbItem.getAddition(),
                        "Поле addition в БД равно null после GET по id"
                ),
                () -> Assertions.assertEquals(
                        entityId,
                        apiItem.getId(),
                        "ID сущности в ответе API не совпадает с ожидаемым"
                ),
                () -> Assertions.assertEquals(
                        entityId,
                        dbItem.getId(),
                        "ID сущности в БД не совпадает с ожидаемым"
                ),
                () -> Assertions.assertEquals(
                        apiItem.getTitle(),
                        dbItem.getTitle(),
                        "Поле title в БД не совпадает со значением из API"
                ),
                () -> Assertions.assertEquals(
                        apiItem.getVerified(),
                        dbItem.getVerified(),
                        "Поле verified в БД не совпадает со значением из API"
                ),
                () -> Assertions.assertEquals(
                        apiItem.getAddition().getAdditionalNumber(),
                        dbItem.getAddition().getAdditionalNumber(),
                        "Поле addition.additionalNumber в БД не совпадает со значением из API"
                ),
                () -> Assertions.assertEquals(
                        apiItem.getAddition().getAdditionalInfo(),
                        dbItem.getAddition().getAdditionalInfo(),
                        "Поле addition.additionalInfo в БД не совпадает со значением из API"
                )
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
        String firstCreatedIdAsString = RestAssuredUtils.createItem(newLocalItem);
        int firstEntityId = Integer.parseInt(firstCreatedIdAsString);

        String secondCreatedIdAsString = RestAssuredUtils.createItem(newLocalItem);
        int secondEntityId = Integer.parseInt(secondCreatedIdAsString);

        Assertions.assertNotNull(
                itemService.findEntity(firstEntityId),
                "Первая сущность не найдена в БД перед GET /getAll"
        );
        Assertions.assertNotNull(
                itemService.findEntity(secondEntityId),
                "Вторая сущность не найдена в БД перед GET /getAll"
        );

        List<models.api.Item> itemsFromApi = RestAssuredUtils.getAll();
        List<models.db.Item> itemsFromDb = itemService.findAllEntities();

        List<Integer> createdIds = List.of(firstEntityId, secondEntityId);

        createdIds.forEach(id -> {
            var dbItem = itemsFromDb.stream()
                    .filter(i -> id == i.getId())
                    .findFirst()
                    .orElseThrow(() -> new AssertionError(
                            "Сущность с id = " + id + " не найдена в БД при проверке GET /getAll"
                    ));

            var apiItem = itemsFromApi.stream()
                    .filter(i -> id == i.getId())
                    .findFirst()
                    .orElseThrow(() -> new AssertionError(
                            "Сущность с id = " + id + " не найдена в ответе API GET /getAll"
                    ));

            Assertions.assertAll(
                    () -> Assertions.assertNotNull(
                            dbItem.getAddition(),
                            "Поле addition в БД равно null для сущности с id = " + id
                    ),
                    () -> Assertions.assertNotNull(
                            apiItem.getAddition(),
                            "Поле addition в ответе API равно null для сущности с id = " + id
                    ),
                    () -> Assertions.assertEquals(
                            apiItem.getTitle(),
                            dbItem.getTitle(),
                            "Поле title в БД не совпадает со значением из API для сущности с id = " + id
                    ),
                    () -> Assertions.assertEquals(
                            apiItem.getVerified(),
                            dbItem.getVerified(),
                            "Поле verified в БД не совпадает со значением из API для сущности с id = " + id
                    ),
                    () -> Assertions.assertEquals(
                            apiItem.getAddition().getAdditionalInfo(),
                            dbItem.getAddition().getAdditionalInfo(),
                            "Поле addition.additionalInfo в БД не совпадает со значением из API для сущности с id = " + id
                    ),
                    () -> Assertions.assertEquals(
                            apiItem.getAddition().getAdditionalNumber(),
                            dbItem.getAddition().getAdditionalNumber(),
                            "Поле addition.additionalNumber в БД не совпадает со значением из API для сущности с id = " + id
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
        String createdIdAsString = RestAssuredUtils.createItem(newLocalItem);
        int entityId = Integer.parseInt(createdIdAsString);

        var originalDbItem = itemService.findEntity(entityId);
        Assertions.assertNotNull(originalDbItem, "Сущность не найдена в БД перед PATCH");

        Item updatedItem = TestData.patchedEntity(newLocalItem);

        RestAssuredUtils.patchItem(entityId, updatedItem);

        var patchedDbItem = itemService.findEntity(entityId);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(
                        patchedDbItem,
                        "Сущность не найдена в БД после PATCH"
                ),
                () -> Assertions.assertNotNull(
                        patchedDbItem.getAddition(),
                        "Поле addition в БД равно null после PATCH"
                ),
                () -> Assertions.assertEquals(
                        entityId,
                        patchedDbItem.getId(),
                        "ID сущности в БД не совпадает с ожидаемым после PATCH"
                ),
                () -> Assertions.assertEquals(
                        updatedItem.getTitle(),
                        patchedDbItem.getTitle(),
                        "Поле title в БД не совпадает с ожидаемым (обновлённым)"
                ),
                () -> Assertions.assertEquals(
                        updatedItem.getVerified(),
                        patchedDbItem.getVerified(),
                        "Поле verified в БД не совпадает с ожидаемым (обновлённым)"
                ),
                () -> Assertions.assertEquals(
                        updatedItem.getAddition().getAdditionalInfo(),
                        patchedDbItem.getAddition().getAdditionalInfo(),
                        "Поле addition.additionalInfo в БД не совпадает с ожидаемым (обновлённым)"
                ),
                () -> Assertions.assertEquals(
                        updatedItem.getAddition().getAdditionalNumber(),
                        patchedDbItem.getAddition().getAdditionalNumber(),
                        "Поле addition.additionalNumber в БД не совпадает с ожидаемым (обновлённым)"
                )
        );

        Item apiAfterPatch = RestAssuredUtils.idGetItem(entityId);

        Assertions.assertAll(
                () -> Assertions.assertEquals(
                        updatedItem.getTitle(),
                        apiAfterPatch.getTitle(),
                        "Поле title в ответе API не совпадает с ожидаемым (обновлённым)"
                ),
                () -> Assertions.assertEquals(
                        updatedItem.getVerified(),
                        apiAfterPatch.getVerified(),
                        "Поле verified в ответе API не совпадает с ожидаемым (обновлённым)"
                ),
                () -> Assertions.assertEquals(
                        updatedItem.getAddition().getAdditionalInfo(),
                        apiAfterPatch.getAddition().getAdditionalInfo(),
                        "Поле addition.additionalInfo в ответе API не совпадает с ожидаемым (обновлённым)"
                ),
                () -> Assertions.assertEquals(
                        updatedItem.getAddition().getAdditionalNumber(),
                        apiAfterPatch.getAddition().getAdditionalNumber(),
                        "Поле addition.additionalNumber в ответе API не совпадает с ожидаемым (обновлённым)"
                )
        );

        itemService.deleteEntity(patchedDbItem);
    }
}
