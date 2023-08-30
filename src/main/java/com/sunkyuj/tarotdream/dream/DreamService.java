package com.sunkyuj.tarotdream.dream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class DreamService {

    private final DreamRepository dreamRepository;
    private final String flaskUrl = "http://43.201.23.0:5000";


    public DreamResponse generate(DreamRequest dreamStory) throws IOException {

        HttpURLConnection conn = getHttpURLConnection(dreamStory, "/dream/generate");
        // 서버로부터 데이터 읽어오기
        StringBuilder sb = getStringBuilder(conn);
//            HashMap<String, Object> responseMap = new ObjectMapper().readValue(sb.toString(), HashMap.class);
//        ObjectMapper objectMapper = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        ModelGenerateResponse modelGenerateResponse = new ObjectMapper().readValue(sb.toString(), ModelGenerateResponse.class);

        DreamResponse dreamResponse = DreamResponse.builder()
                .dreamTitle(modelGenerateResponse.getDreamTitle())
                .engDreamTitle(modelGenerateResponse.getEngDreamTitle())
                .imageUrl(modelGenerateResponse.getImageUrl())
                .possibleMeanings(modelGenerateResponse.getPossibleMeanings())
                .recommendedTarotCard(modelGenerateResponse.getRecommendedTarotCard())
                .created(new Timestamp(System.currentTimeMillis()))
                .build();

        Dream dream = dreamResponse.toEntity();
        dreamRepository.save(dream);
        return dreamResponse;

    }

    public DreamResponse regenerate(DreamRequest dreamStory) throws IOException {
        HttpURLConnection conn = getHttpURLConnection(dreamStory, "/dream/regenerate");
        // 서버로부터 데이터 읽어오기
        StringBuilder sb = getStringBuilder(conn);
//            HashMap<String, Object> responseMap = new ObjectMapper().readValue(sb.toString(), HashMap.class);
        ModelRegenerateResponse modelResponse = new ObjectMapper().readValue(sb.toString(), ModelRegenerateResponse.class);

        DreamResponse dreamResponse = DreamResponse.builder()
                .dreamTitle(modelResponse.getDreamTitle())
                .engDreamTitle(modelResponse.getEngDreamTitle())
                .imageUrl(modelResponse.getImageUrl())
                .possibleMeanings(modelResponse.getPossibleMeanings())
                .recommendedTarotCard(modelResponse.getRecommendedTarotCard())
                .created(new Timestamp(System.currentTimeMillis()))
                .build();

        Dream dream = dreamResponse.toEntity();
        dreamRepository.save(dream);
        return dreamResponse;
    }

    private StringBuilder getStringBuilder(HttpURLConnection conn) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = br.readLine()) != null) { // 읽을 수 있을 때 까지 반복
            sb.append(line);
        }
        return sb;
    }

    private HttpURLConnection getHttpURLConnection(DreamRequest dreamStory, String path) throws IOException {
        URL url = new URL(flaskUrl+path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST"); // http 메서드
        conn.setRequestProperty("Content-Type", "application/json"); // header Content-Type 정보
        conn.setDoInput(true); // 서버에 전달할 값이 있다면 true
        conn.setDoOutput(true); // 서버로부터 받는 값이 있다면 true

        // 서버에 데이터 전달
        JSONObject obj = new JSONObject();
        obj.put("utterance", dreamStory.getDreamStory());

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        bw.write(obj.toString()); // 버퍼에 담기
        bw.flush(); // 버퍼에 담긴 데이터 전달
        bw.close();
        return conn;
    }



}
