package me.zxr.weixr.bean;

/**
 * Î¢²©
 * @author Zoe
 *
 */
public class WeiboBean {
	public int _id;
	public String id;
	public String created_at;
	public String mid;
	public String idstr;
	public String text;
	public String source;
	public boolean favorited;
	public boolean truncated;
	public String in_reply_to_status_id;
	public String in_reply_to_user_id;
	public String in_reply_to_screen_name;
	public String thumbnail_pic;
	public String bmiddle_pic;
	public String original_pic;
	public GeoBean geo;
	public UserBean user;
	public WeiboBean retweeted_status;
	public int reposts_count;
	public int comments_count;
	public int attitudes_count;
	
	
}
