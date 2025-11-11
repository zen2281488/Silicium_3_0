package models.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Addition {

    private Integer id;

    @JsonProperty("additional_info")
    private String additionalInfo;

    @JsonProperty("additional_number")
    private Integer additionalNumber;
}