package me.zxr.weixr.ui.main;

import me.zxr.weixr.R;
import me.zxr.weixr.ui.login.AccountActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MoreActivity extends Activity{
	 private LinearLayout ll_more_account;

	@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_more);
			findViews();
			ll_more_account.setOnClickListener(new AccountOnClickListener());
		}
	 
	private class AccountOnClickListener implements OnClickListener{
	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(MoreActivity.this, AccountActivity.class);
			startActivity(intent);
		}
	}
	
	 private void findViews(){
		 ll_more_account = (LinearLayout) findViewById(R.id.ll_more_account);
	 }
}
