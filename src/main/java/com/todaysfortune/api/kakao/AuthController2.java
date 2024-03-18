package com.todaysfortune.api.kakao;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todaysfortune.jsoup.ZodiacFortuneScraper;

@RestController
public class AuthController2 {
	
	@Autowired
	private KakaoTokenManager manager;
	@Autowired
	private KakaoMessenger messenger;
	@Autowired
	private ZodiacFortuneScraper scraper;

	@GetMapping("/auth/tokentest/refresh")
	public void test() throws IOException {
		System.out.println("start");
		//manager.requestAccessToken();
		//manager.requestAuthorizationCode();
		String token = manager.refreshAccessToken();
		System.out.println("New Access Token : " + token);
	}
	
	//나에게 보내기
	@GetMapping("/kakaomessanger/test/self")
	public void sendDailyZodiacFortuneMyself() {
		//오늘의 띠별 운세 받아오기
		StringBuilder fortuneText = scraper.scrapeZodiacFortune();
		System.out.println("오늘의 운세: " + fortuneText);
		messenger.sendKakaotalkDefault(fortuneText.toString(), KakaoApiConfig.MESSAGE_TO_ME_URL);
		
	}
	
}
