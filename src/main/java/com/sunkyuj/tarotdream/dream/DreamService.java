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

        // Dalle 이미지 다운
        URL url = new URL(modelGenerateResponse.getImageUrl());
        InputStream in = new BufferedInputStream(url.openStream());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024000];
        int n = 0;
        while (-1!=(n=in.read(buf)))
        {
            out.write(buf, 0, n);
        }
        out.close();
        in.close();
        byte[] response = out.toByteArray();
        System.out.println("response = " + response);
        // 읽은 파일은 S3 업로드하기 위해 MultipartFile 객체로 변환한다
        MultipartFile multipartFile = new CustomMultipartFile(response,"image", "image.jpeg", "jpeg", response.length);
        System.out.println("multipartFile. = " + multipartFile.getOriginalFilename());
//        BufferedImage image = ImageIO.read(url);
        String imageS3Url = s3Uploader.upload(multipartFile, "dalle-images");
//        String imageS3Url = s3Uploader.upload(image, "dalle-images");

        Timestamp curTime = new Timestamp(System.currentTimeMillis());

        Dream dream = Dream.builder()
                .dreamTitle(modelGenerateResponse.getDreamTitle())
                .engDreamTitle(modelGenerateResponse.getEngDreamTitle())
                .imageUrl(imageS3Url)
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

    private BufferedImage imageToBufferedImage(Image im) {
        BufferedImage bi = new BufferedImage
                (im.getWidth(null),im.getHeight(null),BufferedImage.TYPE_INT_RGB);
        Graphics bg = bi.getGraphics();
        bg.drawImage(im, 0, 0, null);
        bg.dispose();
        return bi;
    }

    private MultipartFile convertBufferedImageToMultipartFile(BufferedImage image) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpeg", out);
        } catch (IOException e) {
            log.error("IO Error", e);
            return null;
        }
        byte[] bytes = out.toByteArray();
        return new CustomMultipartFile(bytes, "image", "image.jpeg", "jpeg", bytes.length);
    }


}
