package me.zxr.weixr.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	private DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public DBHelper(Context context) {
		super(context, "weibo.db", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "create table user(_id integer primary key autoincrement,"
				+ "id text," + "idstr text," + "access_token text,"
				+ "screen_name text," + "name text," + "province integer,"
				+ "city integer," + "location text," + "description text,"
				+ "url text," + "profile_image_url text," + "domain text,"
				+ "gender text," + "followers_count integer,"
				+ "friends_count integer," + "statuses_count integer,"
				+ "favourites_count integer," + "created_at text,"
				+ "geo_enabled boolean," + "verified boolean,"
				+ "verified_type integer," + "remark text,"
				+ "follow_me boolean," + "online_status integer,"
				+ "bi_followers_count integer)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
