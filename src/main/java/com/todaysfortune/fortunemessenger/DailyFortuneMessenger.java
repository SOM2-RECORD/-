package com.todaysfortune.fortunemessenger;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.todaysfortune.api.kakao.KakaoApiConfig;
import com.todaysfortune.api.kakao.KakaoMessenger;
import com.todaysfortune.jsoup.ZodiacFortuneScraper;

@Service
public class DailyFortuneMessenger {
	
	@Autowired
	private KakaoMessenger messenger;
	
	@Autowired
	private ZodiacFortuneScraper scraper;
	
	// cron 초 분 시 일 월 요일 - *:매번
	@Scheduled(cron = "0 15 21 * * *")
	public void sendDailyZodiacFortune() {
		// 오늘의 띠별 운세 받아오기
		List<String> fortuneList = scraper.scrapeZodiacFortune();
		// 카카오톡으로 보내기
		messenger.sendTextMessageDefaultTwice(fortuneList, KakaoApiConfig.MESSAGE_TO_ME_URL);
	}

}
