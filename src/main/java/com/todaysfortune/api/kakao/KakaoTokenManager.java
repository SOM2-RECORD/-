package com.todaysfortune.api.kakao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.todaysfortune.confidentials.KakaoApiConfigSecret;

@Service
public class KakaoTokenManager {
	
	private static final String REST_API_KEY = KakaoApiConfigSecret.getRestApiKey();
	private static final String REFRESH_TOKEN_FILE_PATH = KakaoApiConfigSecret.getRefreshTokenFilePath();
	
	// 리프레시 토큰으로 액세스 토큰 갱신
	public String refreshAccessToken() {
		HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", KakaoApiConfig.CONTENT_TYPE_URLENCODED_UTF);
        
        String refreshToken = readRefreshTokenFromFile();
        
        String requestBody = "grant_type=refresh_token" +
                             "&client_id=" + REST_API_KEY +
                             "&refresh_token=" + refreshToken;
        
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(KakaoApiConfig.TOKEN_REQUEST_URL, HttpMethod.POST, requestEntity, String.class);

        JSONObject jsonResponse = new JSONObject(responseEntity.getBody());
        
        // 리프레시 토큰이 갱신되었을 경우 새로운 리프레시 토큰으로 파일 저장
        if(jsonResponse.has("refresh_token")) {
        	saveRefreshTokenToFile(jsonResponse.getString("refresh_token"));
        }
        
        // 새로운 액세스 토큰 반환
        return jsonResponse.getString("access_token");
	}
	
    // 파일에서 리프레시 토큰 읽어오기
    private String readRefreshTokenFromFile() {
        StringBuilder refreshToken = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(REFRESH_TOKEN_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                refreshToken.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return refreshToken.toString();
    }
    
    // 리프레시 토큰을 파일에 저장
    private void saveRefreshTokenToFile(String refreshToken) {
    	try (PrintWriter writer = new PrintWriter(new FileWriter(REFRESH_TOKEN_FILE_PATH))) {
    		writer.println(refreshToken);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
}
