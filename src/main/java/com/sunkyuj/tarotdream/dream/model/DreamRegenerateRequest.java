package com.sunkyuj.tarotdream.dream.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DreamRegenerateRequest {
    private Long dreamId;
    private String engDreamTitle;
    private String recommendedTarotCard;
}
