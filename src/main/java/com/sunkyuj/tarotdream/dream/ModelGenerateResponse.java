package com.sunkyuj.tarotdream.dream;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.List;

@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ModelGenerateResponse {
    private String dreamTitle;
    private String engDreamTitle;
    private String imageUrl;
    private List<String> possibleMeanings;
    private String recommendedTarotCard;
    private int status;
}
