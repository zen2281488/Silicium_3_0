package tests.api;

import models.api.Addition;
import models.api.Item;

import org.junit.jupiter.api.BeforeEach;
import services.AdditionService;
import services.ItemService;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import utils.data.api.TestData;


public abstract class BaseTest {

    protected Item newLocalItem;
    protected Addition newLocalAddition;
    protected ItemService itemService;
    protected AdditionService additionService;

    static {
        RestAssured.baseURI = "http://93.113.171.2:8080/api/";
        RestAssured.defaultParser = Parser.JSON;
    }

    @BeforeEach
    public void beforeEach() {
        newLocalAddition = TestData.addition();
        newLocalItem = TestData.entity(newLocalAddition);

        itemService = new ItemService();
        additionService = new AdditionService();
    }
}
