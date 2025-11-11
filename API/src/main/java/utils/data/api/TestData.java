package utils.data.api;

import lombok.experimental.UtilityClass;
import models.api.Addition;
import models.api.Item;

import java.util.List;

@UtilityClass
public class TestData {

    public Item entity(Addition newLocalAddition, String title, Boolean verified, Integer important_number_1, Integer important_number_2,Integer important_number_3) {
        return Item.builder()
                .title(title)
                .verified(verified)
                .importantNumbers(List.of(
                        important_number_1,
                        important_number_2,
                        important_number_3
                ))
                .addition(newLocalAddition)
                .build();
    }

    public Addition addition(String additional_info,Integer additional_number) {
        return Addition.builder()
                .additionalInfo(additional_info)
                .additionalNumber(additional_number)
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

