package utils;

import models.api.Addition;
import models.api.Item;
import models.db.AdditionDb;
import models.db.ItemDb;
import org.junit.jupiter.api.Assertions;
import services.ItemService;

import java.util.List;

public class AssertionsHelper {
    public static void assertEntityExists(int id, ItemService itemService, String message) {
        Assertions.assertNotNull(itemService.findEntity(id), message);
    }
    public static void assertItemsForIds(
            List<Integer> ids,
            List<Item> itemsFromApi,
            List<ItemDb> itemsFromDb
    ) {
        ids.forEach(id -> assertSingleItem(id, itemsFromApi, itemsFromDb));
    }

    public static void assertSingleItem(
            int id,
            List<Item> itemsFromApi,
            List<ItemDb> itemsFromDb
    ) {
        ItemDb dbItem = itemsFromDb.stream()
                .filter(i -> id == i.getId())
                .findFirst()
                .orElseThrow(() -> new AssertionError(
                        "Сущность с id = " + id + " не найдена в БД при проверке GET /getAll"
                ));

        Item apiItem = itemsFromApi.stream()
                .filter(i -> id == i.getId())
                .findFirst()
                .orElseThrow(() -> new AssertionError(
                        "Сущность с id = " + id + " не найдена в ответе API GET /getAll"
                ));

        Assertions.assertNotNull(
                dbItem.getAddition(),
                "Поле addition в БД равно null для сущности с id = " + id
        );
        Assertions.assertNotNull(
                apiItem.getAddition(),
                "Поле addition в ответе API равно null для сущности с id = " + id
        );

        Assertions.assertEquals(
                apiItem.getTitle(),
                dbItem.getTitle(),
                "Поле title в БД не совпадает со значением из API для сущности с id = " + id
        );
        Assertions.assertEquals(
                apiItem.getVerified(),
                dbItem.getVerified(),
                "Поле verified в БД не совпадает со значением из API для сущности с id = " + id
        );

        AdditionDb dbAddition = dbItem.getAddition();
        Addition apiAddition = apiItem.getAddition();

        Assertions.assertEquals(
                apiAddition.getAdditionalInfo(),
                dbAddition.getAdditionalInfo(),
                "Поле addition.additionalInfo в БД не совпадает со значением из API для сущности с id = " + id
        );
        Assertions.assertEquals(
                apiAddition.getAdditionalNumber(),
                dbAddition.getAdditionalNumber(),
                "Поле addition.additionalNumber в БД не совпадает со значением из API для сущности с id = " + id
        );
    }

    public static void assertEntityDeleted(int entityId, ItemService itemService) {
        boolean entityExists = itemService.findAllEntities().stream()
                .anyMatch(item -> entityId == item.getId());

        Assertions.assertFalse(
                entityExists,
                "Сущность с id = " + entityId + " не была удалена из БД"
        );
    }
}
