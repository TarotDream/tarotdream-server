package com.sunkyuj.tarotdream.dream;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DreamRequest {
    private Long userId;
    private String dreamStory;
}
