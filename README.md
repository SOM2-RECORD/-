# 오늘의 운세(Play Today’s Fortune)
- 매일 아침 운세 사이트에서 오늘의 띠별 운세를 가져와 나에게 카카오톡 메시지로 보내주는 프로그램

## 사용 기술
- **Framework**    : SpringBoot3.2.3
- **Language**     : Java17
- **Build**        : Gradle
- **IDE**          : Eclipse
- **Web Scraping** : JSoup
- **Open API**     : Kakao Login API, Kakao Message API

## 스케줄링
- 스프링부트의 스케줄링 기능(@Scheduled)을 사용하여 특정 시간에 매일 카카오톡을 보내도록 설정
- 윈도우 작업 스케줄러를 사용해 메소드를 시작하도록 설정한 시간 5분 전 cmd를 열어 프로젝트가 실행시키는 작업 스케줄러 예약
 
