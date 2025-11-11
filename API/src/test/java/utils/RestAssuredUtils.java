package utils;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.experimental.UtilityClass;
import models.api.Item;

import java.util.List;

@UtilityClass
public class RestAssuredUtils {

    @Step("Формирование RestAssured спецификации")
    public static RequestSpecification getRestAssuredSpecification() {
        return RestAssured.given()
                .headers(
                        "Content-Type", ContentType.JSON,
                        "Accept", ContentType.JSON
                );
    }

    @Step("Создание сущности")
    public static String createItem(Item newItem) {
        return getRestAssuredSpecification()
                .body(newItem)
                .when()
                .post(RestAssured.baseURI + "create")
                .then()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.TEXT)
                .extract()
                .as(String.class);
    }

    @Step("Удаление сущности")
    public static void deleteItem(Integer id) {
        getRestAssuredSpecification()
                .when()
                .delete(RestAssured.baseURI + "delete/" + id)
                .then()
                .assertThat()
                .statusCode(204);
    }

    @Step("Получение сущности")
    public Item idGetItem(Integer id) {
        return getRestAssuredSpecification()
                .when()
                .get(RestAssured.baseURI + "get/" + id)
                .then()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .as(Item.class);
    }

    @Step("Получение списка сущностей")
    public static List<Item> getAll() {
        return getRestAssuredSpecification()
                .when()
                .get(RestAssured.baseURI + "getAll")
                .then()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .jsonPath()
                .getList("entity", Item.class);
    }


    @Step("Обновление сущности")
    public static void patchItem(int id, Item updatedItem) {
        getRestAssuredSpecification()
                .body(updatedItem)
                .when()
                .patch(RestAssured.baseURI + "patch/" + id)
                .then()
                .assertThat()
                .statusCode(204);
    }
}
