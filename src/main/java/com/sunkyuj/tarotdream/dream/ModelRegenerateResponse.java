package com.sunkyuj.tarotdream.dream;

import lombok.Getter;

import java.util.List;

@Getter
public class ModelRegenerateResponse {
    private String dreamTitle;
    private String engDreamTitle;
    private String imageUrl;
    private List<String> possibleMeanings;
    private String recommendedTarotCard;
    private int status;
}
