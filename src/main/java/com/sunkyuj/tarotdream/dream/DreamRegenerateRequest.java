package com.sunkyuj.tarotdream.dream;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DreamRegenerateRequest {
//    private Long userId;
    private String engDreamTitle;
    private String recommendedTarotCard;
}
