package me.zxr.weixr.ui.main;

import java.util.List;

import com.google.gson.Gson;
import com.weibo.sdk.android.WeiboParameters;
import me.zxr.weixr.R;
import me.zxr.weixr.adapter.StatusesAdapter;
import me.zxr.weixr.bean.UserBean;
import me.zxr.weixr.bean.WeiboBean;
import me.zxr.weixr.bean.WeiboListBean;
import me.zxr.weixr.dao.URLHelper;
import me.zxr.weixr.dao.UserDao;
import me.zxr.weixr.ui.pic.OriginalPicActivity;
import me.zxr.weixr.ui.send.SendStatusActivity;
import me.zxr.weixr.utils.HttpUtility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class HomeActivity extends Activity {
	UserDao ud = new UserDao(this);
	
	public static final int SET_DATA = 0;
	private PopupWindow mPopupWindow;
	private TextView tv_main_userName;
	private RelativeLayout rl_main_menu;
	
	private String name;
	private String access_token;

	private List<WeiboBean> data;

	private StatusesAdapter adapter;

	Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case SET_DATA:
				adapter = new StatusesAdapter(data, HomeActivity.this);
				lv_main_status.setAdapter(adapter);
				break;

			default:
				break;
			}
		}
	};

	private ListView lv_main_status;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		findViews();//拿到要用的View
		
		SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
		
		String uid = sp.getString("uid", "");
		UserBean ub = ud.findUserById(uid);
		name = ub.screen_name;
		access_token = ub.access_token;
		tv_main_userName.setText(name);
		MyTask m = new MyTask();
		m.start();
		
		lv_main_status.setOnItemClickListener(new MyOnItemClickListener());
		
		rl_main_menu.setOnClickListener(new OnMenuClickListener());
	}
	
	private class OnMenuClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(mPopupWindow != null){
				mPopupWindow.dismiss();
				mPopupWindow = null;
			}else{
				mPopupWindow = new PopupWindow(getLayoutInflater().inflate(R.layout.main_pop, null), 335, 400);
				mPopupWindow.showAsDropDown(rl_main_menu, 0, 0);
			}
		}
		
	}
	
	private class MyOnItemClickListener implements OnItemClickListener{

		private TextView tv_statuses_repost_name;

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			
			tv_statuses_repost_name = (TextView) view.findViewById(R.id.tv_statuses_repost_name);
			if(tv_statuses_repost_name!=null){
				tv_statuses_repost_name.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
					}
				});
			}
			ImageView iv_statuses_repost_image = (ImageView) view.findViewById(R.id.iv_statuses_repost_image);
			if(iv_statuses_repost_image != null){
				String[] pics = (String[]) iv_statuses_repost_image.getTag();
				Intent intent = new Intent(HomeActivity.this, OriginalPicActivity.class);
				if(pics[1] != null && !pics[1].equals("")){
					intent.putExtra("path", pics[1]);
				}else{
					intent.putExtra("path", pics[0]);
				}
				startActivity(intent);
			}
		}
		
	}
	
	
	private class MyTask extends Thread{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			
			//根据access_token查最新的微博
			WeiboParameters parameters = new WeiboParameters();
			parameters.add("access_token", access_token);
			String statuses_friends_timeline = URLHelper.STATUSES_FRIENDS_TIMELINE;
			try {
				String result =HttpUtility.getmInstance().executeNormalTask("GET", statuses_friends_timeline, parameters);
				
				Gson gson = new Gson();
				WeiboListBean wbs = gson.fromJson(result, WeiboListBean.class);
				data = wbs.getStatuses();
				Message msg = new Message();
				msg.what = SET_DATA;
				mHandler.sendMessage(msg);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.i("iiii", "异常");
			}
		}
		
	};
	
	public void sendStatus(View view){
		Intent intent = new Intent(this, SendStatusActivity.class);
		startActivity(intent);
	}
	
	
	private void findViews(){
		rl_main_menu = (RelativeLayout) findViewById(R.id.rl_main_menu);
		lv_main_status = (ListView) findViewById(R.id.lv_main_status);
		tv_main_userName  = (TextView) findViewById(R.id.tv_main_userName);
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		MyTask m = new MyTask();
		m.start();
	}

}
