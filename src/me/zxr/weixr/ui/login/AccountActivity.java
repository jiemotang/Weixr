package me.zxr.weixr.ui.login;

import java.util.List;

import me.zxr.weixr.R;
import me.zxr.weixr.adapter.AccountAdapter;
import me.zxr.weixr.bean.UserBean;
import me.zxr.weixr.dao.UserDao;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class AccountActivity extends Activity{
	 private ListView lv_account_user;
	private List<UserBean> ubs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_um_main);
		findViews();
		ubs = new UserDao(this).findUser();
		AccountAdapter adapter  = new AccountAdapter(ubs, this);
		lv_account_user.setAdapter(adapter);
		lv_account_user.setOnItemClickListener(new MyOnItemClickListener());
	}
	
	private class MyOnItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			UserBean ub = ubs.get(position);
			 SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
			 Editor editor = sp.edit();
			 editor.putString("access_token", ub.access_token);
			 editor.putString("uid", ub.id);
			 editor.commit();
			 
			 Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
			 startActivity(intent);
			 AccountActivity.this.finish();
		}
		
	}
	
	public void back(){
		finish();
	}
	
	public void edit(){
		
	}
	 
	 private void findViews(){
		 lv_account_user = (ListView) findViewById(R.id.lv_account_user);
	 }
	 
	 public void new_account(View view){
		 SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
		 Editor editor = sp.edit();
		 editor.remove("access_token");
		 editor.remove("uid");
		 editor.commit();
		 Intent intent = new Intent(this, LoginActivity.class);
		 startActivity(intent);
		 this.finish();
	 }
}
