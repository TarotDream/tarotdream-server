package com.sunkyuj.tarotdream.dream;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
@Slf4j
public class DreamService {

    private final DreamRepository dreamRepository;
    private final String flaskUrl = "http://43.201.23.0:5000";

    public List<Dream> findDreams(){
        return dreamRepository.findAll();
    }
    public Dream generate(DreamGenerateRequest dreamGenerateRequest) throws IOException {

        JSONObject obj = new JSONObject();
        obj.put("utterance", dreamGenerateRequest.getDreamStory());

        // 서버에 데이터 전달
        HttpURLConnection conn = getHttpURLConnection(obj, "/dream/generate");
        // 서버로부터 데이터 읽어오기
        ModelGenerateResponse modelGenerateResponse = getModelResponse(conn, ModelGenerateResponse.class);
        Timestamp curTime = new Timestamp(System.currentTimeMillis());

        Dream dream = Dream.builder()
                .dreamTitle(modelGenerateResponse.getDreamTitle())
                .engDreamTitle(modelGenerateResponse.getEngDreamTitle())
                .imageUrl(modelGenerateResponse.getImageUrl())
                .possibleMeanings(modelGenerateResponse.getPossibleMeanings())
                .recommendedTarotCard(modelGenerateResponse.getRecommendedTarotCard())
                .created(curTime)
                .updated(curTime)
                .build();

        dreamRepository.save(dream);
        return dream;

    }

    public Dream regenerate(DreamRegenerateRequest dreamRegenerateRequest) throws IOException {

        JSONObject obj = new JSONObject();
        obj.put("dream", dreamRegenerateRequest.getEngDreamTitle());
        obj.put("tarot_card", dreamRegenerateRequest.getRecommendedTarotCard());

        // 서버에 데이터 전달
        HttpURLConnection conn = getHttpURLConnection(obj, "/dream/regenerate");
        // 서버로부터 데이터 읽어오기
        ModelRegenerateResponse modelRegenerateResponse = getModelResponse(conn, ModelRegenerateResponse.class);

        Dream dream = dreamRepository.findById(dreamRegenerateRequest.getDreamId());
        dream.setImageUrl(modelRegenerateResponse.getImageUrl());
        dream.setUpdated(new Timestamp(System.currentTimeMillis()));

        dreamRepository.save(dream);
        return dream;
    }

    private <T> T getModelResponse(HttpURLConnection conn, Class<T> valueType) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = br.readLine()) != null) { // 읽을 수 있을 때 까지 반복
            sb.append(line);
        }
        return new ObjectMapper().readValue(sb.toString(), valueType);
    }


    private HttpURLConnection getHttpURLConnection(JSONObject jsonObject, String path) throws IOException {
        URL url = new URL(flaskUrl+path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST"); // http 메서드
        conn.setRequestProperty("Content-Type", "application/json"); // header Content-Type 정보
        conn.setDoInput(true); // 서버에 전달할 값이 있다면 true
        conn.setDoOutput(true); // 서버로부터 받는 값이 있다면 true

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        bw.write(jsonObject.toString()); // 버퍼에 담기
        bw.flush(); // 버퍼에 담긴 데이터 전달
        bw.close();
        return conn;
    }
}
