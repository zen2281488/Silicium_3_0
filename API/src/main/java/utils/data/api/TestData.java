package utils.data.api;

import lombok.experimental.UtilityClass;
import models.api.Addition;
import models.api.Item;
import utils.properties.ConfProperties;

import java.util.List;

@UtilityClass
public class TestData {

    public Item entity(Addition newLocalAddition) {
        return Item.builder()
                .title(ConfProperties.string("title"))
                .verified(ConfProperties.bool("verified"))
                .importantNumbers(List.of(
                        ConfProperties.integer("important_number_1"),
                        ConfProperties.integer("important_number_2"),
                        ConfProperties.integer("important_number_3")
                ))
                .addition(newLocalAddition)
                .build();
    }

    public Addition addition() {
        return Addition.builder()
                .additionalInfo(ConfProperties.string("additional_info"))
                .additionalNumber(ConfProperties.integer("additional_number"))
                .build();
    }

    public Addition patchedAddition(Addition original) {
        if (original == null) {
            return null;
        }

        return Addition.builder()
                .id(original.getId())
                .additionalInfo(original.getAdditionalInfo() + " (обновлено)")
                .additionalNumber(original.getAdditionalNumber())
                .build();
    }

    public Item patchedEntity(Item original) {
        if (original == null) {
            return null;
        }

        Addition patchedAddition = patchedAddition(original.getAddition());

        return Item.builder()
                .id(original.getId())
                .title(original.getTitle() + " (обновлено)")
                .verified(original.getVerified())
                .importantNumbers(original.getImportantNumbers())
                .addition(patchedAddition)
                .build();
    }
}

