package com.sunkyuj.tarotdream;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

@Service
public class DreamService {

    public DreamResponse generate(String dreamStory) {

        try {
            String flaskUrl = "http://localhost:5000";
            URL url = new URL(flaskUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

            conn.setRequestMethod("POST"); // http 메서드
            conn.setRequestProperty("Content-Type", "application/json"); // header Content-Type 정보
            conn.setDoInput(true); // 서버에 전달할 값이 있다면 true
            conn.setDoOutput(true); // 서버로부터 받는 값이 있다면 true

            // 서버에 데이터 전달
            JSONObject obj = new JSONObject();
            obj.put("utterance", dreamStory);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            bw.write(obj.toString()); // 버퍼에 담기
            bw.flush(); // 버퍼에 담긴 데이터 전달
            bw.close();

            // 서버로부터 데이터 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();

            String line = null;
            while((line = br.readLine()) != null) { // 읽을 수 있을 때 까지 반복
                sb.append(line);
            }
            HashMap<String, Object> responseMap = new ObjectMapper().readValue(sb.toString(), HashMap.class);
            return DreamResponse.builder()
                    .gptResponse((String) responseMap.get("data"))
                    .imageUrl("")
//                    .gptResponse((String) responseMap.get("gpt_response"))
//                    .imageUrl((String) responseMap.get("image_url"))
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
