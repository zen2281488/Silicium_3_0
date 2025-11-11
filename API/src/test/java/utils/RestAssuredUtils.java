package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.experimental.UtilityClass;
import models.api.Item;


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

    @Step("Получение сущности")
    public Item getAll() {
        return getRestAssuredSpecification()
                .when()
                .get(RestAssured.baseURI + "get/")
                .then()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .as(Item.class);
    }
}
