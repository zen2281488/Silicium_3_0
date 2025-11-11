package tests.api;

import models.api.Addition;
import models.api.Item;

import org.junit.jupiter.api.BeforeEach;
import services.ItemService;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;

public abstract class BaseTest {

    protected Item newLocalItem;
    protected Addition newLocalAddition;
    protected ItemService itemService;

    static {
        RestAssured.baseURI = System.getProperty("API_BASE_URI");
        RestAssured.defaultParser = Parser.JSON;
    }

    @BeforeEach
    public void beforeEach() {
        itemService = new ItemService();
    }
}
