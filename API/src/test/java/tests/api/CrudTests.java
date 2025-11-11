package tests.api;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Description;
import models.api.Item;
import models.db.AdditionDb;
import models.db.ItemDb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.RestAssuredUtils;
import utils.data.api.TestData;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static utils.AssertionsHelper.assertEntityExists;
import static utils.AssertionsHelper.assertItemsForIds;
import static utils.AssertionsHelper.assertEntityDeleted;


@Epic("CRUD")
public class CrudTests extends BaseTest {


    @BeforeEach
    public void setUp() {
        newLocalAddition = TestData.addition("test_additional_info", 123);
        newLocalItem = TestData.entity(newLocalAddition, "test", true, 1, 2, 3);
    }

    @Feature("Создание сущности")
    @Test
    @DisplayName("T-007")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Тестирование создания сущности")
    @Issue("API-create-item-p")
    public void createEntityPTest() {
        String createdIdAsString = RestAssuredUtils.createItem(newLocalItem);
        int entityId = Integer.parseInt(createdIdAsString);

        ItemDb dbItem = itemService.findEntity(entityId);
        assertNotNull(dbItem, "Сущность не найдена в БД после создания");

        AdditionDb dbAddition = dbItem.getAddition();
        assertNotNull(dbAddition, "Поле addition в БД равно null после создания");

        assertEquals(
                entityId,
                dbItem.getId(),
                "ID сущности в БД не совпадает с ожидаемым после создания");
        assertEquals(
                newLocalItem.getTitle(),
                dbItem.getTitle(),
                "Поле title в БД не совпадает с тестовыми данными при создании");
        assertEquals(
                newLocalItem.getVerified(),
                dbItem.getVerified(),
                "Поле verified в БД не совпадает с тестовыми данными при создании");
        assertEquals(
                newLocalItem.getAddition().getAdditionalNumber(),
                dbAddition.getAdditionalNumber(),
                "Поле addition.additionalNumber в БД не совпадает с тестовыми данными при создании");
        assertEquals(
                newLocalItem.getAddition().getAdditionalInfo(),
                dbAddition.getAdditionalInfo(),
                "Поле addition.additionalInfo в БД не совпадает с тестовыми данными при создании");
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

        assertEntityExists(
                entityId,
                itemService,
                "Сущность не найдена в БД перед удалением"
        );

        RestAssuredUtils.deleteItem(entityId);

        assertEntityDeleted(entityId, itemService);
    }

    @Feature("Получение сущности по id")
    @Test
    @DisplayName("T-009")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Тестирование получения сущности по id через API")
    @Issue("API-id-get-item-p")
    public void idGetEntityApiPTest() {
        String createdIdAsString = RestAssuredUtils.createItem(newLocalItem);
        int entityId = Integer.parseInt(createdIdAsString);

        ItemDb dbItemBefore = itemService.findEntity(entityId);
        assertNotNull(dbItemBefore, "Сущность не найдена в БД перед GET по id");

        Item apiItem = RestAssuredUtils.idGetItem(entityId);

        assertEquals(
                entityId,
                apiItem.getId(),
                "ID сущности в ответе API не совпадает с ожидаемым"
        );
        assertEquals(
                newLocalItem.getTitle(),
                apiItem.getTitle(),
                "Некорректное значение поля title в ответе API"
        );
        assertEquals(
                newLocalItem.getVerified(),
                apiItem.getVerified(),
                "Некорректное значение поля verified в ответе API"
        );
        assertNotNull(
                apiItem.getAddition(),
                "Поле addition в ответе API равно null"
        );
        assertEquals(
                newLocalItem.getAddition().getAdditionalNumber(),
                apiItem.getAddition().getAdditionalNumber(),
                "Некорректное значение addition.additionalNumber в ответе API"
        );
        assertEquals(
                newLocalItem.getAddition().getAdditionalInfo(),
                apiItem.getAddition().getAdditionalInfo(),
                "Некорректное значение addition.additionalInfo в ответе API"
        );

        itemService.deleteEntity(dbItemBefore);
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

        assertEntityExists(firstEntityId, itemService,
                "Первая сущность не найдена в БД перед GET /getAll");
        assertEntityExists(secondEntityId, itemService,
                "Вторая сущность не найдена в БД перед GET /getAll");

        List<Item> itemsFromApi = RestAssuredUtils.getAll();
        List<ItemDb> itemsFromDb = itemService.findAllEntities();

        assertItemsForIds(
                List.of(firstEntityId, secondEntityId),
                itemsFromApi,
                itemsFromDb
        );

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

        ItemDb originalDbItem = itemService.findEntity(entityId);
        assertNotNull(originalDbItem, "Сущность не найдена в БД перед PATCH");

        Item updatedItem = TestData.patchedEntity(newLocalItem);

        RestAssuredUtils.patchItem(entityId, updatedItem);

        ItemDb patchedDbItem = itemService.findEntity(entityId);

        assertNotNull(
                patchedDbItem,
                "Сущность не найдена в БД после PATCH");
        assertNotNull(
                patchedDbItem.getAddition(),
                "Поле addition в БД равно null после PATCH");
        assertEquals(
                entityId,
                patchedDbItem.getId(),
                "ID сущности в БД не совпадает с ожидаемым после PATCH");
        assertEquals(
                updatedItem.getTitle(),
                patchedDbItem.getTitle(),
                "Поле title в БД не совпадает с ожидаемым (обновлённым)");
        assertEquals(
                updatedItem.getVerified(),
                patchedDbItem.getVerified(),
                "Поле verified в БД не совпадает с ожидаемым (обновлённым)");
        assertEquals(
                updatedItem.getAddition().getAdditionalInfo(),
                patchedDbItem.getAddition().getAdditionalInfo(),
                "Поле addition.additionalInfo в БД не совпадает с ожидаемым (обновлённым)");
        assertEquals(
                updatedItem.getAddition().getAdditionalNumber(),
                patchedDbItem.getAddition().getAdditionalNumber(),
                "Поле addition.additionalNumber в БД не совпадает с ожидаемым (обновлённым)");
        Item apiAfterPatch = RestAssuredUtils.idGetItem(entityId);

        assertEquals(
                updatedItem.getTitle(),
                apiAfterPatch.getTitle(),
                "Поле title в ответе API не совпадает с ожидаемым (обновлённым)");
        assertEquals(
                updatedItem.getVerified(),
                apiAfterPatch.getVerified(),
                "Поле verified в ответе API не совпадает с ожидаемым (обновлённым)");
        assertEquals(
                updatedItem.getAddition().getAdditionalInfo(),
                apiAfterPatch.getAddition().getAdditionalInfo(),
                "Поле addition.additionalInfo в ответе API не совпадает с ожидаемым (обновлённым)");
        assertEquals(
                updatedItem.getAddition().getAdditionalNumber(),
                apiAfterPatch.getAddition().getAdditionalNumber(),
                "Поле addition.additionalNumber в ответе API не совпадает с ожидаемым (обновлённым)");

        itemService.deleteEntity(patchedDbItem);
    }

    @Feature("Создание сущности")
    @Test
    @DisplayName("T-012")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка, что после создания сущность сохраняется в БД")
    @Issue("API-create-item-db-p")
    public void createEntityPersistedInDbPTest() {
        String createdIdAsString = RestAssuredUtils.createItem(newLocalItem);
        int entityId = Integer.parseInt(createdIdAsString);

        ItemDb dbItem = itemService.findEntity(entityId);

        assertNotNull(
                dbItem,
                "Сущность не была сохранена в БД"
        );
        assertNotNull(
                dbItem.getAddition(),
                "Поле addition в БД равно null после сохранения"
        );
        assertEquals(
                entityId,
                dbItem.getId(),
                "ID сущности в БД не совпадает с ожидаемым"
        );
        assertEquals(
                newLocalItem.getTitle(),
                dbItem.getTitle(),
                "Поле title в БД не совпадает с ожидаемым значением"
        );
        assertEquals(
                newLocalItem.getVerified(),
                dbItem.getVerified(),
                "Поле verified в БД не совпадает с ожидаемым значением"
        );
        assertEquals(
                newLocalItem.getAddition().getAdditionalNumber(),
                dbItem.getAddition().getAdditionalNumber(),
                "Поле addition.additionalNumber в БД не совпадает с ожидаемым значением"
        );
        assertEquals(
                newLocalItem.getAddition().getAdditionalInfo(),
                dbItem.getAddition().getAdditionalInfo(),
                "Поле addition.additionalInfo в БД не совпадает с ожидаемым значением"
        );

        itemService.deleteEntity(dbItem);
    }

}
