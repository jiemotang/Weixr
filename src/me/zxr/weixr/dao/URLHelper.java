package me.zxr.weixr.dao;

public class URLHelper {
	
	//����
	public static final String APP_KEY = "849186383";
	//�ص�ҳ��
	public static final String REDIRECT_URI = "http://zxr.me";
	
	//Ȩ������
	public static final String SCOPE = "friendships_groups_read";
	
	//΢������URL
	public static final String BASE_WEIBO_URL = "https://api.weibo.com/2/";
	
	/* Oauth2*/
	public static final String BASE_OAUTH2_URL = "https://api.weibo.com/oauth2/";
	//������Ȩ
	public static final String OAUTH2_AUTHORIZE_URL = BASE_OAUTH2_URL+"authorize";
	//��Ȩ��ѯ
	public static final String OAUTH2_GET_TOKEN_INFO_URL = BASE_OAUTH2_URL+"get_token_info";
	
	
	//��ѯ�û���Ϣ
	public static final String SHOW_USER = BASE_WEIBO_URL+"users/show.json";
	
	//΢��
	public static final String STATUSES_FRIENDS_TIMELINE = BASE_WEIBO_URL+"statuses/friends_timeline.json";
	public static final String STATUSES_MENTIONS = BASE_WEIBO_URL+"statuses/mentions.json";
	public static final String STATUSES_USER_TIMELINE = BASE_WEIBO_URL + "statuses/user_timeline.json";
	public static final String UPDATE_STATUS = BASE_WEIBO_URL + "statuses/update.json";
	
	//����
	public static final String COMMENTS_MENTIONS = BASE_WEIBO_URL+"comments/mentions.json";
	public static final String COMMENTS_TO_ME = BASE_WEIBO_URL+"comments/to_me.json";

	//����
	public static final String EMOTIONS = BASE_WEIBO_URL+"emotions.json";
	
	//GPS
	public static final String NEARBY_POIS = BASE_WEIBO_URL + "place/nearby/pois.json";
}
