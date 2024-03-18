package com.todaysfortune.api.kakao;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.todaysfortune.confidentials.TodayFortuneConfig;

@Service
public class KakaoMessenger {
	
	private static final Logger logger = LoggerFactory.getLogger(KakaoMessenger.class);
	
	@Autowired
	private KakaoTokenManager kakaoTokenManager;
	
	public void sendKakaotalkDefault(String message, String url) {
    	try {
    		String accessToken = kakaoTokenManager.refreshAccessToken();
    		
    		JSONObject link = new JSONObject();
    		link.put("web_url", TodayFortuneConfig.ZODIAC_FORTUNE_URL);
    		link.put("mobile_web_url", TodayFortuneConfig.ZODIAC_FORTUNE_URL);
    		
    		String requestBody = createTemplateObejct(message, link);
            HttpHeaders headers = createMessangerHttpHeaders(accessToken);
    		
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

            RestTemplate restTemplate = new RestTemplate();
    		ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
    		
    		JSONObject jsonResponse = new JSONObject(responseEntity.getBody());
    		checkResponse(jsonResponse);
    	
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }

    private String createTemplateObejct(String message, JSONObject link) {
        JSONObject templateObject = new JSONObject();
        templateObject.put("object_type", "text");
        templateObject.put("text", message);
        templateObject.put("link", link);
        return "template_object="+templateObject.toString();
    }
    
    private HttpHeaders createMessangerHttpHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", KakaoApiConfig.CONTENT_TYPE_URLENCODED_UTF);
        headers.add("Authorization", "Bearer " + accessToken);
        return headers;
    }
    
    private void checkResponse(JSONObject response) {
    	if((int)response.get("result_code") != 0) {
    		logger.info("Fail to send fortune message.");
    	}else {
    		logger.info("Success to send fortune message.");
    	}
    }
}