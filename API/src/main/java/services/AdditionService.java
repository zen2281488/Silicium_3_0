package services;

import io.qameta.allure.Step;
import models.db.Addition;
import utils.db.DbUtils;

import java.util.List;

public class AdditionService {
    public AdditionService() {
    }

    @Step("Найти дополнительную информацию по ID: {id}")
    public Addition findAddition(int id) {
        return DbUtils.findById(Addition.class, id);
    }

    @Step("Сохранить дополнительную информацию о сущности в БД")
    public void saveAddition(Addition addition) {
        DbUtils.save(addition);
    }

    @Step("Удалить дополнительную информацию из БД")
    public void deleteAddition(Addition addition) {
        DbUtils.delete(addition);
    }

    @Step("Обновить дополнительную информацию в БД")
    public void updateAddition(Addition addition) {
        DbUtils.update(addition);
    }

    @Step("Найти всю дополнительную информацию в БД")
    public List<Addition> findAllAdditions() {
        return DbUtils.findAll(Addition.class);
    }
}
