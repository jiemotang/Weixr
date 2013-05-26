package me.zxr.weixr.bean;

public class UserBean {
	public int _id;
	public String id;
	public String idstr;
	public String access_token;
	public String screen_name;
	public String name;
	public int province;
	public int city;
	public String location;
	public String description;
	public String url;
	public String profile_image_url;
	public String domain;
	public String gender;
	public int followers_count;
	public int friends_count;
	public int statuses_count;
	public int favourites_count;
	public String created_at;
	public boolean geo_enabled;
	public boolean verified;
	public int verified_type;
	public String remark;
	public boolean follow_me;
	public int online_status;
	public int bi_followers_count;
	
	public static final String[] PROJECTION = new String[]{
		"_id",
		"id",
		"idstr",
		"access_token",
		"screen_name",
		"name",
		"province",
		"city",
		"location",
		"description",
		"url",
		"profile_image_url",
		"domain",
		"gender",
		"followers_count",
		"friends_count",
		"statuses_count",
		"favourites_count",
		"created_at",
		"geo_enabled",
		"verified",
		"verified_type",
		"remark",
		"follow_me",
		"online_status",
		"bi_followers_count"
	};
	
	public static final int _ID_INDEX = 0;
	public static final int ID_INDEX = 1;
	public static final int IDSTR_INDEX = 2;
	public static final int ACCESS_TOKEN_INDEX = 3;
	public static final int SCREEN_NAME_INDEX = 4;
	public static final int NAME_INDEX = 5;
	public static final int PROVINCE_INDEX = 6;
	public static final int CITY_INDEX = 7;
	public static final int LOCATION_INDEX = 8;
	public static final int DESCRIPTION_INDEX = 9;
	public static final int URL_INDEX = 10;
	public static final int PROFILE_IMAGE_URL_INDEX = 11;
	public static final int DOMAIN_INDEX = 12;
	public static final int GENDER_INDEX = 13;
	public static final int FOLLOWERS_COUNT_INDEX = 14;
	public static final int FRIENDS_COUNT_INDEX = 15;
	public static final int STATUSES_COUNT_INDEX = 16;
	public static final int FAVOURITES_COUNT_INDEX = 17;
	public static final int CREATED_AT_INDEX = 18;
	public static final int GEO_ENABLED_INDEX = 19;
	public static final int VERIFIED_INDEX = 20;
	public static final int VERIFIED_TYPE_INDEX = 21;
	public static final int REMARK_INDEX = 22;
	public static final int FOLLOW_ME_INDEX = 23;
	public static final int ONLINE_STATUS_INDEX = 24;
	public static final int BI_FOLLOWERS_COUNT_INDEX = 25;
}
