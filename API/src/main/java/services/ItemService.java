package services;

import io.qameta.allure.Step;
import models.db.Item;
import utils.db.DbUtils;

import java.util.List;

public class ItemService {
    public ItemService() {
    }

    @Step("D?D���D1���,D, ���?���������%D���D_���?���,���O D���D_ ID: {id}")
    public Item findEntity(int id) {
        return DbUtils.findById(Item.class, id);
    }

    @Step("D�D_�.�?D�D�D�D�D,D� �?���%D�D_�?�,D, D� D`D\"")
    public void saveEntity(Item item) {
        DbUtils.save(item);
    }

    @Step("D���D'D���D���D,���,���O ���?���������%D���D_���?���,���O D,D��� D`D\"")
    public void deleteEntity(Item item) {
        DbUtils.delete(item);
    }

    @Step("DzD���D���D_D���D,���,���O ���?���������%D���D_���?���,���O D��� D`D\"")
    public void updateEntity(Item item) {
        DbUtils.update(item);
    }

    @Step("D?D���D1���,D, D������?D���. D���D_D������OD���D_D���D������,D���D���D���D1 D��� D`D\"")
    public List<Item> findAllEntities() {
        return DbUtils.findAll(Item.class);
    }
}
