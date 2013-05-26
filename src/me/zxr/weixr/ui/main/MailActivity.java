package me.zxr.weixr.ui.main;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.weibo.sdk.android.WeiboParameters;

import me.zxr.weixr.R;
import me.zxr.weixr.adapter.CommentsToMeAdapter;
import me.zxr.weixr.adapter.MyPageAdapter;
import me.zxr.weixr.adapter.StatusesAdapter;
import me.zxr.weixr.bean.CommentBean;
import me.zxr.weixr.bean.CommentListBean;
import me.zxr.weixr.bean.WeiboBean;
import me.zxr.weixr.bean.WeiboListBean;
import me.zxr.weixr.dao.URLHelper;
import me.zxr.weixr.utils.HttpUtility;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class MailActivity extends Activity{
	
	private ViewPager vp_mail;
	 private ListView lv_status_mentions;
	 private String access_token;
	 
	 private static final int LOAD_STATUS_MENTIONS_OK = 0;
	 private static final int LOAD_COMMENTS_TO_ME_OK = 1;
	 
	 Handler mHandler = new Handler(){

			private ListView lv_mail_comments_to_me;

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				
				switch (msg.what) {
				case LOAD_STATUS_MENTIONS_OK:
					List<WeiboBean> wbs = (List<WeiboBean>) msg.obj;
					StatusesAdapter mStatusesAdapter = new StatusesAdapter(wbs, MailActivity.this);
					lv_status_mentions = (ListView) findViewById(R.id.lv_status_mentions);
					lv_status_mentions.setAdapter(mStatusesAdapter);
					break;
				case LOAD_COMMENTS_TO_ME_OK:
					List<CommentBean> cbs = (List<CommentBean>) msg.obj;
					CommentsToMeAdapter mCommentsToMeAdapter = new CommentsToMeAdapter(MailActivity.this, cbs);
					lv_mail_comments_to_me = (ListView) findViewById(R.id.lv_mail_comments_to_me);
					lv_mail_comments_to_me.setAdapter(mCommentsToMeAdapter);
					break;
				default:
					break;
				}
			}
			 
		 };
	private TextView tv_mail_tab_me;
	private TextView tv_mail_tab_comment;
	private TextView tv_mail_tab_private_mail;
		 
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mail);
		
		findViews();
		List<View> views = new ArrayList<View>();
		View view1 = getLayoutInflater().inflate(R.layout.mail_status_mentions, null);
		views.add(view1);
		View view2 = getLayoutInflater().inflate(R.layout.mail_comments_to_me, null);
		views.add(view2);
		
		MyPageAdapter myPageAdapter = new MyPageAdapter(views);
		vp_mail.setAdapter(myPageAdapter);
		
		vp_mail.setOnPageChangeListener(new MyOnPageChangeListener());
		
		//加载关于我的新微博
		loadStatusMentions();
	}
	 
	 private class MyOnPageChangeListener implements OnPageChangeListener{

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageSelected(int page) {
				// TODO Auto-generated method stub
				switch (page) {
				case 0:
					tv_mail_tab_me.setTextColor(getResources().getColor(R.color.normal_text));
					tv_mail_tab_comment.setTextColor(getResources().getColor(android.R.color.darker_gray));
					tv_mail_tab_private_mail.setTextColor(getResources().getColor(android.R.color.darker_gray));
					loadStatusMentions();
					break;
				case 1:
					tv_mail_tab_me.setTextColor(getResources().getColor(android.R.color.darker_gray));
					tv_mail_tab_comment.setTextColor(getResources().getColor(R.color.normal_text));
					tv_mail_tab_private_mail.setTextColor(getResources().getColor(android.R.color.darker_gray));
					loadCommentsToMe();
					break;
				default:
					break;
				}
			}
			
		}
		
		private void loadStatusMentions(){
			new Thread(){
				public void run() {
					SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
					access_token = sp.getString("access_token", "");
					if(!access_token.equals("")){
						WeiboParameters parameters = new WeiboParameters();
						parameters.add("access_token", access_token);
						String path = URLHelper.STATUSES_MENTIONS;
							String result = HttpUtility.getmInstance().executeNormalTask("GET", path, parameters);
							Gson gson = new Gson();
							WeiboListBean cbs = gson.fromJson(result, WeiboListBean.class);
							Message msg = new Message();
							msg.what = LOAD_STATUS_MENTIONS_OK;
							msg.obj = cbs.getStatuses();
							mHandler.sendMessage(msg);
					}
				};
			}.start();
		}
	
		private void loadCommentsToMe(){
			new Thread(){
				public void run() {
					WeiboParameters parameters = new WeiboParameters();
					parameters.add("access_token", access_token);
					String path = URLHelper.COMMENTS_TO_ME;
					String result = HttpUtility.getmInstance().executeNormalTask("GET", path, parameters);
					Gson gson = new Gson();
					CommentListBean clb = gson.fromJson(result, CommentListBean.class);
					Message msg = new Message();
					msg.what = LOAD_COMMENTS_TO_ME_OK;
					msg.obj = clb.getComments();
					mHandler.sendMessage(msg);
				};
			}.start();
		}
		
		 private void findViews(){
			 vp_mail = (ViewPager) findViewById(R.id.vp_mail);
			 tv_mail_tab_me = (TextView) findViewById(R.id.tv_mail_tab_me);
			 tv_mail_tab_comment = (TextView) findViewById(R.id.tv_mail_tab_comment);
			 tv_mail_tab_private_mail = (TextView) findViewById(R.id.tv_mail_tab_private_mail);
		 }
}
