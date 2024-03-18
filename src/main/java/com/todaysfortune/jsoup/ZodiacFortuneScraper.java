package com.todaysfortune.jsoup;

import java.io.IOException;

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
	
	public StringBuilder scrapeZodiacFortune(){
        
		try {
            String url = TodayFortuneConfig.ZODIAC_FORTUNE_URL;

            Document document = Jsoup.connect(url).get();

            if (document != null) {
                Element fortuneInfo = document.selectFirst("#card");
                
                if (fortuneInfo != null) {
                    Elements fortuneInfoAll = fortuneInfo.select(".txt_box");
                    StringBuilder fortuneText = new  StringBuilder();
                    
                    for (Element info : fortuneInfoAll) {
                    	fortuneText.append(info.text()).append("\n\n");
                    }
                    return fortuneText;
                    
                } else {
                    logger.info("Fortune information not found.");
                }
            } else {
            	logger.info("Failed to retrieve fortune information.");
            }
            
            return null;
            
        } catch (IOException e) {
        	logger.error("An error occurred while scraping the fortune information: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
