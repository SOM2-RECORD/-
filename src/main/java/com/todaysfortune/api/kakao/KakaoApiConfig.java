package com.todaysfortune.api.kakao;

public class KakaoApiConfig {
	
	public static final String TOKEN_REQUEST_URL = "https://kauth.kakao.com/oauth/token";
	
	public static final String MESSAGE_TO_ME_URL = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
	public static final String MESSAGE_TO_OPEN_URL = "https://kapi.kakao.com/v1/api/talk/chats";
	
	public static final String CONTENT_TYPE_URLENCODED = "application/x-www-form-urlencoded;";
	public static final String CONTENT_TYPE_URLENCODED_UTF = "application/x-www-form-urlencoded;charset=UTF-8";
}
