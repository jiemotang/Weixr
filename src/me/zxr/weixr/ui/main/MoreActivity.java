package me.zxr.weixr.ui.main;

import me.zxr.weixr.R;
import me.zxr.weixr.bean.UserBean;
import me.zxr.weixr.dao.UserDao;
import me.zxr.weixr.ui.login.AccountActivity;
import me.zxr.weixr.ui.login.LoginActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MoreActivity extends Activity{
	 private LinearLayout ll_more_account;
	private String uid;
	private TextView tv_more_userName;
	private Builder builder;
	private AlertDialog alertDialog;

	@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_more);
			findViews();
			ll_more_account.setOnClickListener(new AccountOnClickListener());
			
			SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
			uid = sp.getString("uid", "");
			UserBean ub = new UserDao(this).findUserById(uid);
			String userName = ub.screen_name;
			tv_more_userName.setText(userName);
		}
	 
	private class AccountOnClickListener implements OnClickListener{
	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(MoreActivity.this, AccountActivity.class);
			startActivity(intent);
		}
	}
	
	public void logOut(View view){
		builder = new Builder(this);
		builder.setView(getLayoutInflater().inflate(R.layout.log_out_dialog, null));
		alertDialog = builder.show();
	}
	
	public void confirmLogOut(View view){
		 SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
		 Editor editor = sp.edit();
		 editor.remove("access_token");
		 editor.remove("uid");
		 editor.commit();
		 Intent intent = new Intent(this, LoginActivity.class);
		 startActivity(intent);
		 this.finish();
	}
	
	public void cancel(View view){
		alertDialog.dismiss();
	}
	
	public void exit(View view){
		builder = new Builder(this);
		builder.setView(getLayoutInflater().inflate(R.layout.exit_dialog, null));
		alertDialog = builder.show();
		
	}
	
	public void confirmExit(View view){
		finish();
	}
	
	 private void findViews(){
		 ll_more_account = (LinearLayout) findViewById(R.id.ll_more_account);
		 tv_more_userName = (TextView) findViewById(R.id.tv_more_userName);
	 }
}
