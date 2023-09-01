package com.sunkyuj.tarotdream.dream.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Builder
public class DreamResponse {
    private Long dreamId;
    private String dreamTitle;
    private String engDreamTitle;
    private String imageUrl;
    private List<String> possibleMeanings;
    private String recommendedTarotCard;
    private Timestamp created;
//    private int status;

    public Dream toEntity(){
        return Dream.builder()
                .dreamTitle(dreamTitle)
                .engDreamTitle(engDreamTitle)
                .imageUrl(imageUrl)
                .recommendedTarotCard(recommendedTarotCard)
                .created(created)
                .build();
    }
}
