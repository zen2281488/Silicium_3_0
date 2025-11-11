package services;

import io.qameta.allure.Step;
import models.db.ItemDb;
import utils.db.DbUtils;

import java.util.List;

public class ItemService {

    public ItemService() {
    }

    @Step("Поиск сущности в БД по id: {id}")
    public ItemDb findEntity(int id) {
        return DbUtils.findById(ItemDb.class, id);
    }

    @Step("Сохранение сущности в БД")
    public void saveEntity(ItemDb item) {
        DbUtils.save(item);
    }

    @Step("Удаление сущности из БД")
    public void deleteEntity(ItemDb item) {
        DbUtils.delete(item);
    }

    @Step("Обновление сущности в БД")
    public void updateEntity(ItemDb item) {
        DbUtils.update(item);
    }

    @Step("Получение списка всех сущностей из БД")
    public List<ItemDb> findAllEntities() {
        return DbUtils.findAll(ItemDb.class);
    }
}
