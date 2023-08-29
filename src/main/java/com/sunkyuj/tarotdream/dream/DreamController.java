package com.sunkyuj.tarotdream.dream;

import lombok.RequiredArgsConstructor;
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
    public DreamResponse generateDream(@RequestBody DreamRequest dreamStory){
//        String dreamStory = "The mountain spirit suddenly appeared and gave me a peach. Please interpret this dream.";
        Dream generatedDream = dreamService.generate(dreamStory);
        return DreamResponse.builder()
                .gptResponse(null)
                .imageUrl(generatedDream.getImageUrl())
                .title(generatedDream.getTitle())
                .created(generatedDream.getCreated())
                .build();
    }


}
