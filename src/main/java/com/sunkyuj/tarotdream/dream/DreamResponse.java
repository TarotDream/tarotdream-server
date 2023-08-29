package com.sunkyuj.tarotdream.dream;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
public class DreamResponse {
    private String gptResponse;
    private String imageUrl;
    private String title;
    private Timestamp created;
}
