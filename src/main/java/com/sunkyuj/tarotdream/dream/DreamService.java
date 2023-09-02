package com.sunkyuj.tarotdream.dream;

import com.fasterxml.jackson.databind.ObjectMapper;
//import com.sunkyuj.tarotdream.common.S3Uploader;
import com.sunkyuj.tarotdream.common.CustomMultipartFile;
import com.sunkyuj.tarotdream.common.S3Uploader;
import com.sunkyuj.tarotdream.dream.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.List;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
@Slf4j
public class DreamService {

    private final DreamRepository dreamRepository;
    private final S3Uploader s3Uploader;
    @Value("${url.flask-server-url}")
    private String flaskUrl;

    public List<Dream> findDreams(){
        return dreamRepository.findAll();
    }

    public Dream findOne(Long dreamId) {
        return dreamRepository.findById(dreamId);
    }
    public Dream generate(DreamGenerateRequest dreamGenerateRequest) throws IOException {

        JSONObject obj = new JSONObject();
        obj.put("utterance", dreamGenerateRequest.getDreamStory());

        // 서버에 데이터 전달
        HttpURLConnection conn = getHttpURLConnection(obj, "/dream/generate");
        // 서버로부터 데이터 읽어오기
        ModelGenerateResponse modelGenerateResponse = getModelResponse(conn, ModelGenerateResponse.class);

        // TODO: Dream 생성 시 created, update를 클래스 내부에서 만들도록 변경
        Timestamp curTime = new Timestamp(System.currentTimeMillis());
        Dream dream = Dream.builder()
                .dreamTitle(modelGenerateResponse.getDreamTitle())
                .engDreamTitle(modelGenerateResponse.getEngDreamTitle())
                .possibleMeanings(modelGenerateResponse.getPossibleMeanings())
                .recommendedTarotCard(modelGenerateResponse.getRecommendedTarotCard())
                .created(curTime)
                .updated(curTime)
                .build();
        dreamRepository.save(dream);

        Long dreamId = dream.getDreamId();
        // 읽은 파일은 S3 업로드하기 위해 MultipartFile 객체로 변환한다
        MultipartFile dalleImage = downloadDalleImage(modelGenerateResponse.getImageUrl(), dreamId);
        String imageS3Url = s3Uploader.upload(dalleImage, "dalle-images");
        dream.setImageUrl(imageS3Url);
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

        MultipartFile dalleImage = downloadDalleImage(modelRegenerateResponse.getImageUrl(), dream.getDreamId());
        String imageS3Url = s3Uploader.upload(dalleImage, "dalle-images");

        dream.setImageUrl(imageS3Url);
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

    private CustomMultipartFile downloadDalleImage(String dalleImageUrl, Long dreamId) throws IOException {
        // Dalle 이미지 다운
        URL url = new URL(dalleImageUrl);
        InputStream in = new BufferedInputStream(url.openStream());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[102400];
        int n = 0;
        while (-1!=(n=in.read(buf)))
        {
            out.write(buf, 0, n);
        }
        out.close();
        in.close();
        byte[] dalleImage = out.toByteArray();

        return new CustomMultipartFile(dalleImage,"image"+dreamId, "image"+dreamId+".jpeg", "jpeg", dalleImage.length);

    }


}
