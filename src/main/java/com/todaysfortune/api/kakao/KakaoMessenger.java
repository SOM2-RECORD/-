package com.todaysfortune.api.kakao;

import java.util.List;

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
	
	// List를 2분할하여 text 메세지로 카카오톡을 두 번 보내기
	public void sendTextMessageDefaultTwice(List<String> fortuneList, String url) {
    	try {
    		String accessToken = kakaoTokenManager.refreshAccessToken();
    		
    		int halfSize = fortuneList.size()/2;
    		for(int i=0; i<2; i++) {
    			String message = createListToString(fortuneList, i*halfSize, (i+1)*halfSize);
    			sendTextMessage(message, accessToken, url);
    		}
    	} catch(Exception e) {
    		logger.error("Failed to send fortune message.", e);
    	}
    }
	
	// 한 번에 text 메세지 전송하기
	public void sendTextMessageDefault(String message, String url) {
    	try {
    		String accessToken = kakaoTokenManager.refreshAccessToken();
    		sendTextMessage(message, accessToken, url);
    	
    	} catch(Exception e) {
    		logger.error("Failed to send fortune message.", e);
    	}
    }
	
	private void sendTextMessage(String message, String accessToken, String url) {
        try {
            String requestBody = createTemplateObject(message);
            HttpHeaders headers = createMessangerHttpHeaders(accessToken);
            
            ResponseEntity<String> responseEntity = sendRequest(requestBody, headers, url);
            if (responseEntity != null) {
                JSONObject jsonResponse = new JSONObject(responseEntity.getBody());
                checkResponse(jsonResponse);
            }
        } catch (Exception e) {
            logger.error("Failed to send fortune message.", e);
        }
    }
	
	private String createListToString(List<String> messageList, int start, int end) {
		StringBuilder message = new StringBuilder();
		for(int i=start; i<end; i++) {
			message.append(messageList.get(i));
		}
		return message.toString();
	}

    private String createTemplateObject(String message) {
        JSONObject templateObject = new JSONObject();
        templateObject.put("object_type", "text");
        templateObject.put("text", message);
        templateObject.put("link", setZodiacFortuneLink());
        return "template_object="+templateObject.toString();
    }
    
    private JSONObject setZodiacFortuneLink() {
    	JSONObject link = new JSONObject();
		link.put("web_url", TodayFortuneConfig.ZODIAC_FORTUNE_URL);
		link.put("mobile_web_url", TodayFortuneConfig.ZODIAC_FORTUNE_URL);
		return link;
    }
    
    private HttpHeaders createMessangerHttpHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", KakaoApiConfig.CONTENT_TYPE_URLENCODED_UTF);
        headers.add("Authorization", "Bearer " + accessToken);
        return headers;
    }
    
    private ResponseEntity<String> sendRequest(String requestBody, HttpHeaders headers, String url) {
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        } catch (Exception e) {
            logger.error("Failed to send request.", e);
            return null;
        }
    }
    
    private void checkResponse(JSONObject response) {
    	if((int)response.get("result_code") != 0) {
    		logger.info("Fail to send fortune message.");
    	}else {
    		logger.info("Success to send fortune message.");
    	}
    }
}