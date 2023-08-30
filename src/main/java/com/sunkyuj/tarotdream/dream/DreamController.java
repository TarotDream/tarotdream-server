package com.sunkyuj.tarotdream.dream;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/dream")
@RequiredArgsConstructor
public class DreamController {

    private final DreamService dreamService;

    @Operation(summary = "타로카드 생성", description = "사용자가 꿈을 입력하면, 해몽과 꿈 이미지가 생성된다.", tags = { "Dream" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = DreamResponse.class))))
    })
    @PostMapping("/generate")
    public DreamResponse generateDream(@RequestBody DreamGenerateRequest dreamGenerateRequest) throws IOException {
//        String dreamStory = "The mountain spirit suddenly appeared and gave me a peach. Please interpret this dream.";
        return dreamService.generate(dreamGenerateRequest);
    }

    @Operation(summary = "타로카드 이미지 재생성", description = "사용자의 꿈에 대한 꿈 이미지가 재생성된다.", tags = { "Dream" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = DreamResponse.class))))
    })
    @PostMapping("/regenerate")
    public DreamResponse regenerateDream(@RequestBody DreamRegenerateRequest dreamRegenerateRequest) throws IOException {
        return dreamService.regenerate(dreamRegenerateRequest);
    }


}
