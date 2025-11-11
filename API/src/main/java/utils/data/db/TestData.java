package utils.data.db;

import lombok.experimental.UtilityClass;
import models.db.Addition;
import models.db.Item;
import utils.properties.ConfProperties;

import java.util.List;

@UtilityClass
public class TestData {

    public Item entity() {
        Item item = new Item();
        item.setTitle(ConfProperties.string("title"));
        item.setVerified(ConfProperties.bool("verified"));
        item.setImportantNumbers(List.of(
                ConfProperties.integer("important_number_1"),
                ConfProperties.integer("important_number_2"),
                ConfProperties.integer("important_number_3")
        ));
        return item;
    }

    public Addition addition() {
        Addition addition = new Addition();
        addition.setAdditionalInfo(ConfProperties.string("additional_info"));
        addition.setAdditionalNumber(ConfProperties.integer("additional_number"));
        return addition;
    }
}
