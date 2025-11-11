package services;

import io.qameta.allure.Step;
import models.db.Item;
import utils.db.DbUtils;

import java.util.List;

public class ItemService {

    public ItemService() {
    }

    @Step("Поиск сущности в БД по id: {id}")
    public Item findEntity(int id) {
        return DbUtils.findById(Item.class, id);
    }

    @Step("Сохранение сущности в БД")
    public void saveEntity(Item item) {
        DbUtils.save(item);
    }

    @Step("Удаление сущности из БД")
    public void deleteEntity(Item item) {
        DbUtils.delete(item);
    }

    @Step("Обновление сущности в БД")
    public void updateEntity(Item item) {
        DbUtils.update(item);
    }

    @Step("Получение списка всех сущностей из БД")
    public List<Item> findAllEntities() {
        return DbUtils.findAll(Item.class);
    }
}
