package models.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
    private Integer id;
    private String title;
    private Boolean verified;
    @JsonProperty("important_numbers")
    private List<Integer> importantNumbers;

    @JsonProperty("addition")
    private Addition addition;
}
