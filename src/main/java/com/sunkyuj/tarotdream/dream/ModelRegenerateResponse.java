package com.sunkyuj.tarotdream.dream;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ModelRegenerateResponse {
    private String dreamTitle;
    private String engDreamTitle;
    private String imageUrl;
    private List<String> possibleMeanings;
    private String recommendedTarotCard;
    private int status;
}
