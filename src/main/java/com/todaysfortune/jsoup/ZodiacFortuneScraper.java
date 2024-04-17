package com.todaysfortune.jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.todaysfortune.confidentials.TodayFortuneConfig;

@Service
public class ZodiacFortuneScraper {
	
	private static final Logger logger = LoggerFactory.getLogger(ZodiacFortuneScraper.class);
	
	/* StringBuilder에서 List로 변경한 이유
	 * Text 길이가 길어서 카카오톡으로 공유했을 때 일부 잘려서 보이지 않아서 두 번에 나눠서 보내기 위함 
	*/
	public List<String> scrapeZodiacFortune() {
	    int maxRetries = 3; // 최대 재시도 횟수 설정
	    int retryCount = 0;

	    while (retryCount < maxRetries) {
	        try {
	            return performScraping();
	            
            // IOException이 발생한 경우, 타임아웃으로 간주하고 재시도
	        } catch (IOException e) {
	            retryCount++;
	            if (retryCount >= maxRetries) {
	                logger.error("Failed to retrieve fortune information after " + maxRetries + " attempts: " + e.getMessage());
	                return null;
	            }
	            logger.warn("Retrying scraping operation, attempt " + retryCount);
	        }
	    }
	    return null;
	}
	
	private List<String> performScraping() throws IOException{
		String url = TodayFortuneConfig.ZODIAC_FORTUNE_URL;
	    Document document = Jsoup.connect(url).get();

	    if (document != null) {
	        Element fortuneInfo = document.selectFirst("#card");
	        if (fortuneInfo != null) {
	            Elements fortuneInfoAll = fortuneInfo.select(".txt_box");
	            List<String> fortuneList = new ArrayList<>();
	            for (Element info : fortuneInfoAll) {
	                fortuneList.add(info.text() + "\n\n");
	            }
	            return fortuneList;
	        } else {
	            logger.info("Fortune information not found.");
	            return null;
	        }
	    } else {
	        logger.info("Failed to retrieve fortune information.");
	        return null;
	    }
	}
}
