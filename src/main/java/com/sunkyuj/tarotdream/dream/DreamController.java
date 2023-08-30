package com.sunkyuj.tarotdream.dream;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/dream")
@RequiredArgsConstructor
public class DreamController {

    private final DreamService dreamService;

    @PostMapping("/generate")
    public DreamResponse generateDream(@RequestBody DreamGenerateRequest dreamGenerateRequest) throws IOException {
//        String dreamStory = "The mountain spirit suddenly appeared and gave me a peach. Please interpret this dream.";
        return dreamService.generate(dreamGenerateRequest);
    }
    @PostMapping("/regenerate")
    public DreamResponse regenerateDream(@RequestBody DreamRegenerateRequest dreamRegenerateRequest) throws IOException {
//        String dreamStory = "The mountain spirit suddenly appeared and gave me a peach. Please interpret this dream.";
        return dreamService.regenerate(dreamRegenerateRequest);
    }


}
