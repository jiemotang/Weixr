package me.zxr.weixr.dao;

import com.sina.weibo.sdk.log.Log;

import me.zxr.weixr.bean.UserBean;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class UserDao {
	private Context context;
	
	public UserDao(Context context) {
		super();
		this.context = context;
	}

	public boolean isEmpty(String id){
		DBHelper helper = new DBHelper(context);
		SQLiteDatabase db = helper.getReadableDatabase();
		boolean isEmpty =  !db.query("user", UserBean.PROJECTION, "id=?", new String[]{id},null, null, null).moveToFirst();
		return isEmpty;
	}
	
	public void addUser(UserBean ub){
		DBHelper helper = new DBHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("id", ub.id);
			values.put("idstr", ub.idstr);
			values.put("access_token", ub.access_token);
			values.put("screen_name", ub.screen_name);
			values.put("name", ub.name);
			values.put("province", ub.province);
			values.put("city", ub.city);
			values.put("location", ub.location);
			values.put("description", ub.description);
			values.put("url", ub.url);
			values.put("profile_image_url", ub.profile_image_url);
			values.put("domain", ub.domain);
			values.put("gender", ub.gender);
			values.put("followers_count", ub.followers_count);
			values.put("friends_count", ub.friends_count);
			values.put("statuses_count", ub.statuses_count);
			values.put("favourites_count", ub.favourites_count);
			values.put("created_at", ub.created_at);
			values.put("geo_enabled", ub.geo_enabled);
			values.put("verified", ub.verified);
			values.put("verified_type", ub.verified_type);
			values.put("remark", ub.remark);
			values.put("follow_me", ub.follow_me);
			values.put("online_status", ub.online_status);
			values.put("bi_followers_count", ub.bi_followers_count);
			db.insert("user", "_id", values);
	}
	
	public UserBean findUserById(String uid){
		DBHelper helper = new DBHelper(context);
		SQLiteDatabase db = helper.getReadableDatabase();
		UserBean ub = new UserBean();
		Cursor c = db.query("user", UserBean.PROJECTION, "id=?", new String[]{uid}, null, null, null);
		if(c.moveToFirst()){
			ub.id = c.getString(UserBean.ID_INDEX);
			ub.idstr = c.getString(UserBean.IDSTR_INDEX);
			ub.access_token = c.getString(UserBean.ACCESS_TOKEN_INDEX);
			ub.screen_name = c.getString(UserBean.SCREEN_NAME_INDEX);
			ub.name = c.getString(UserBean.NAME_INDEX);
			ub.province  = c.getInt(UserBean.PROVINCE_INDEX);
			ub.city = c.getInt(UserBean.CITY_INDEX);
			ub.location = c.getString(UserBean.LOCATION_INDEX);
			ub.description = c.getString(UserBean.DESCRIPTION_INDEX);
			ub.url = c.getString(UserBean.URL_INDEX);
			ub.profile_image_url = c.getString(UserBean.PROFILE_IMAGE_URL_INDEX);
			ub.domain = c.getString(UserBean.DOMAIN_INDEX);
			ub.gender = c.getString(UserBean.GENDER_INDEX);
			ub.followers_count = c.getInt(UserBean.FOLLOWERS_COUNT_INDEX);
			ub.friends_count = c.getInt(UserBean.FRIENDS_COUNT_INDEX);
			ub.statuses_count = c.getInt(UserBean.STATUSES_COUNT_INDEX);
			ub.favourites_count = c.getInt(UserBean.FAVOURITES_COUNT_INDEX);
			ub.created_at = c.getString(UserBean.CREATED_AT_INDEX);
			ub.verified_type = c.getInt(UserBean.VERIFIED_TYPE_INDEX);
			ub.remark = c.getString(UserBean.REMARK_INDEX);
			ub.online_status = c.getInt(UserBean.ONLINE_STATUS_INDEX);
			ub.bi_followers_count = c.getInt(UserBean.BI_FOLLOWERS_COUNT_INDEX);
			
		}
		return ub;
	}
}
