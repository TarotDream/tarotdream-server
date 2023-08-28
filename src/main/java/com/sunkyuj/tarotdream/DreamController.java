package com.sunkyuj.tarotdream;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class DreamController {

    private final DreamService dreamService;

    @GetMapping("/")
    public String home(){
        return "hi";
    }

    @PostMapping("/dream")
    public DreamResponse generateDream(@RequestBody String dreamStory){
//        String dreamStory = "The mountain spirit suddenly appeared and gave me a peach. Please interpret this dream.";
        return dreamService.generate(dreamStory);
    }


}
