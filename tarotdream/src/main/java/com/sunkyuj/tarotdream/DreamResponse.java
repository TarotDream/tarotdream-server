package com.sunkyuj.tarotdream;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DreamResponse {
    private String gptResponse;
    private String imageUrl;
}
