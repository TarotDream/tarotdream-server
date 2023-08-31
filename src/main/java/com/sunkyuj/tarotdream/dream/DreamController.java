package com.sunkyuj.tarotdream.dream;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.sunkyuj.tarotdream.utils.ApiResult;
import com.sunkyuj.tarotdream.utils.ApiUtils;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/dream")
@RequiredArgsConstructor
public class DreamController {

    private final DreamService dreamService;

    @Operation(summary = "전체 타로몽 카드 조회", description = "모든 타로몽 카드를 조회한다 ", tags = { "Dream" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = DreamResponse.class))))
    })
    @GetMapping("/")
    public ApiResult<List<DreamResponse>> getDreams() {
        List<Dream> dreams = dreamService.findDreams();
        List<DreamResponse> dreamResponseList = new ArrayList<>();
        for (Dream dream : dreams) {
            DreamResponse dreamResponse = DreamResponse.builder()
                    .dreamId(dream.getDreamId())
                    .dreamTitle(dream.getDreamTitle())
                    .engDreamTitle(dream.getEngDreamTitle())
                    .imageUrl(dream.getImageUrl())
                    .possibleMeanings(dream.getPossibleMeanings())
                    .recommendedTarotCard(dream.getRecommendedTarotCard())
                    .created(dream.getCreated())
                    .build();
            dreamResponseList.add(dreamResponse);
        }
        return ApiUtils.success(dreamResponseList);
    }

    @Operation(summary = "단일 타로몽 카드 조회", description = "타로몽 카드 하나를 조회한다", tags = { "Dream" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = DreamResponse.class)))
    })
    @GetMapping("/{dreamId}")
    public ApiResult<DreamResponse> getPost(@PathVariable("dreamId") Long dreamId) {
        Dream dream = dreamService.findOne(dreamId);
        DreamResponse dreamResponse = DreamResponse.builder()
                .dreamId(dream.getDreamId())
                .dreamTitle(dream.getDreamTitle())
                .engDreamTitle(dream.getEngDreamTitle())
                .imageUrl(dream.getImageUrl())
                .possibleMeanings(dream.getPossibleMeanings())
                .recommendedTarotCard(dream.getRecommendedTarotCard())
                .created(dream.getCreated())
                .build();
        return ApiUtils.success(dreamResponse);
    }

    @Operation(summary = "타로카드 생성", description = "사용자가 꿈을 입력하면, 해몽과 꿈 이미지가 생성된다.", tags = { "Dream" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = DreamResponse.class)))
    })
    @PostMapping("/generate")
    public ApiResult<DreamResponse> generateDream(@RequestBody DreamGenerateRequest dreamGenerateRequest) throws IOException {
//        String dreamStory = "The mountain spirit suddenly appeared and gave me a peach. Please interpret this dream.";
        Dream generatedDream = dreamService.generate(dreamGenerateRequest);
        DreamResponse dreamResponse = DreamResponse.builder()
                .dreamId(generatedDream.getDreamId())
                .dreamTitle(generatedDream.getDreamTitle())
                .engDreamTitle(generatedDream.getEngDreamTitle())
                .imageUrl(generatedDream.getImageUrl())
                .possibleMeanings(generatedDream.getPossibleMeanings())
                .recommendedTarotCard(generatedDream.getRecommendedTarotCard())
                .created(generatedDream.getCreated())
                .build();
        return ApiUtils.success(dreamResponse);
    }

    @Operation(summary = "타로카드 이미지 재생성", description = "사용자의 꿈에 대한 꿈 이미지가 재생성된다.", tags = { "Dream" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = DreamResponse.class)))
    })
    @PostMapping("/regenerate")
    public ApiResult<DreamResponse> regenerateDream(@RequestBody DreamRegenerateRequest dreamRegenerateRequest) throws IOException {
        Dream regeneratedDream = dreamService.regenerate(dreamRegenerateRequest);
        DreamResponse dreamResponse = DreamResponse.builder()
                .dreamId(regeneratedDream.getDreamId())
                .dreamTitle(regeneratedDream.getDreamTitle())
                .engDreamTitle(regeneratedDream.getEngDreamTitle())
                .imageUrl(regeneratedDream.getImageUrl())
                .possibleMeanings(regeneratedDream.getPossibleMeanings())
                .recommendedTarotCard(regeneratedDream.getRecommendedTarotCard())
                .created(regeneratedDream.getCreated())
                .build();
        return ApiUtils.success(dreamResponse);
    }



}
