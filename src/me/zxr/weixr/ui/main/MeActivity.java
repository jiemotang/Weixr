package me.zxr.weixr.ui.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import com.google.gson.Gson;
import com.weibo.sdk.android.WeiboParameters;

import me.zxr.weixr.R;
import me.zxr.weixr.adapter.MyPageAdapter;
import me.zxr.weixr.adapter.StatusesAdapter;
import me.zxr.weixr.bean.CommentListBean;
import me.zxr.weixr.bean.UserBean;
import me.zxr.weixr.bean.WeiboBean;
import me.zxr.weixr.bean.WeiboListBean;
import me.zxr.weixr.dao.URLHelper;
import me.zxr.weixr.utils.HttpUtility;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MeActivity extends Activity{
	private static final int SET_PROFILE_IMAGE = 0;
	private static final int LOAD_ME_STATUS_OK = 1;
	private ListView lv_me;
	private ImageView iv_me_profile_image;
	private TextView tv_me_gender_location;
	private TextView tv_me_name;
	private TextView tv_me_friends_count;
	private TextView tv_me_followers_count;
	private TextView tv_me_favourites_count;
	private TextView tv_me_status_count;
	private String access_token;
	private String uid;

	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			switch (msg.what) {
			case SET_PROFILE_IMAGE:
				Bitmap bitmap = (Bitmap) msg.obj;
				iv_me_profile_image.setImageBitmap(bitmap);
				break;
			case LOAD_ME_STATUS_OK:
				StatusesAdapter statusesAdapter = new StatusesAdapter((List<WeiboBean>) msg.obj, MeActivity.this);
				lv_me.setAdapter(statusesAdapter);
				break;
			default:
				break;
			}
		}
	};
	
	@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_me);
			findViews();
			loadMeInfo();//加载个人信息
			loadMeStatus();//加载自己发的微博
		}
	
	public void loadMeInfo(){
		SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
		access_token = sp.getString("access_token", "");
		uid = sp.getString("uid", "");
		String path = URLHelper.SHOW_USER;
		WeiboParameters parameters = new WeiboParameters();
		parameters.add("access_token", access_token);
		parameters.add("uid", uid);
		String result = HttpUtility.getmInstance().executeNormalTask("GET", path, parameters);
		UserBean ub = new Gson().fromJson(result, UserBean.class);
		final String profile_image_url = ub.profile_image_url;
		String name = ub.screen_name;
		String location = ub.location;
		String gender = ub.gender;
		int followers_count = ub.followers_count;
		int friends_count = ub.friends_count;
		int statuses_count = ub.statuses_count;
		int favourites_count = ub.favourites_count;
		
		
		if(profile_image_url != null){//判断图片路径是否为空
			new Thread(){
				public void run() {
					//把图片的后缀作为文件名
					String fileName = profile_image_url.substring(profile_image_url.lastIndexOf("cn/")+3);
					fileName = fileName.replace("/", "_")+".jpg";
					
					//先从本地拿，如果没拿到再把这个图片加载到本地
					Bitmap bitmap = HttpUtility.getmInstance().getPic(fileName);
						if(bitmap == null){
							HttpUtility.getmInstance().loadPic(profile_image_url, fileName);
							bitmap = HttpUtility.getmInstance().getPic(fileName);
						}
						//加载完后发信息给handler
						Message msg = new Message();
						msg.what = SET_PROFILE_IMAGE;
						msg.obj = bitmap;
						mHandler.sendMessage(msg);
					
				};
			}.start();
		}
		
	tv_me_name.setText(name);
	String sex = gender.equals("m") ? "男" : "女";
	tv_me_gender_location.setText(sex+" "+location);
	tv_me_friends_count.setText(friends_count+"");
	tv_me_followers_count.setText(followers_count+"");
	tv_me_favourites_count.setText(favourites_count+"");
	tv_me_status_count.setText("共 "+statuses_count+" 条微博");
	}
	
	private void loadMeStatus(){
		new Thread(){
			public void run() {
				String path = URLHelper.STATUSES_USER_TIMELINE;
				WeiboParameters parameters = new WeiboParameters();
				parameters.add("access_token", access_token);
				parameters.add("uid", uid);
				String result = HttpUtility.getmInstance().executeNormalTask("GET", path, parameters);
				System.out.println(result+"dsad");
				WeiboListBean wlb = new Gson().fromJson(result, WeiboListBean.class);
				Message msg = new Message();
				msg.what = LOAD_ME_STATUS_OK;
				msg.obj = wlb.getStatuses();
				mHandler.sendMessage(msg);
			}
		}.start();
	}
	 
	private void findViews(){
		lv_me = (ListView) findViewById(R.id.lv_me);
		 iv_me_profile_image = (ImageView) findViewById(R.id.iv_me_profile_image);
		 tv_me_name = (TextView) findViewById(R.id.tv_me_name);
		 tv_me_gender_location = (TextView) findViewById(R.id.tv_me_gender_location);
		 tv_me_friends_count = (TextView) findViewById(R.id.tv_me_friends_count);
		 tv_me_followers_count = (TextView) findViewById(R.id.tv_me_followers_count);
		 tv_me_favourites_count = (TextView) findViewById(R.id.tv_me_favourites_count);
		 tv_me_status_count = (TextView) findViewById(R.id.tv_me_status_count);
	 }
}
