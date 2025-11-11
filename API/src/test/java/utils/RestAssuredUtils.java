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

    @Step("Формирование RestAssured спецификации с токеном")
    public static RequestSpecification getRestAssuredSpecification() {
        return RestAssured.given()
                .headers(
                        "Content-Type", ContentType.JSON,
                        "Accept", ContentType.JSON
                );
    }

    @Step("Создание сущности через API")
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
    @Step("Удаление сущности через API")
    public static String deleteItem(Integer id) {
        return getRestAssuredSpecification()
                .when()
                .delete(RestAssured.baseURI + "delete/"+id)
                .then()
                .assertThat()
                .statusCode(204)
                .contentType(ContentType.TEXT)
                .extract()
                .as(String.class);
    }
}
