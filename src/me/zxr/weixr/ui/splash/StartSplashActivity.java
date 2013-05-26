package me.zxr.weixr.ui.splash;

import me.zxr.weixr.R;
import me.zxr.weixr.ui.login.LoginActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;

public class StartSplashActivity extends Activity{
	private static final int TIME_OVER = 0;
	Handler mHandler =  new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case TIME_OVER:
				Intent intent = new Intent(StartSplashActivity.this, LoginActivity.class);
				startActivity(intent);
				//跳过去后把这个activity关掉
				StartSplashActivity.this.finish();
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
		View view = new View(this);
		view.setBackgroundDrawable(getResources().getDrawable(R.drawable.default_start));
		setContentView(view);
		new Thread(){
			public void run() {
				SystemClock.sleep(1000);
				Message msg = new Message();
				msg.what = TIME_OVER;
				mHandler.sendMessage(msg);
			};
		}.start();
	}
}
