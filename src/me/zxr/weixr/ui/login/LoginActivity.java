package me.zxr.weixr.ui.login;

import com.google.gson.Gson;
import com.weibo.sdk.android.WeiboParameters;
import com.weibo.sdk.android.util.Utility;

import me.zxr.weixr.R;
import me.zxr.weixr.bean.TokenInfoBean;
import me.zxr.weixr.bean.UserBean;
import me.zxr.weixr.dao.URLHelper;
import me.zxr.weixr.dao.UserDao;
import me.zxr.weixr.ui.main.MainActivity;
import me.zxr.weixr.utils.HttpUtility;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class LoginActivity extends Activity{
	UserDao ud = new UserDao(this);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		WebView wv_login = (WebView) findViewById(R.id.wv_login);
		WebSettings settings = wv_login.getSettings();
		//允许加载javascript
		settings.setJavaScriptEnabled(true);
		
		SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
		String uid = sp.getString("uid", "");
		String access_token = sp.getString("access_token", "");
		boolean flag = true;
		if(uid.equals("") && access_token.equals("")){//首选项中是否保存有用户信息，如果没有就进入授权界面
			flag = false;
		}else{//如果有，则查询授权剩余时间
			String token_info_url = URLHelper.OAUTH2_GET_TOKEN_INFO_URL;
			WeiboParameters parameters = new WeiboParameters();
			parameters.add("access_token", access_token);
			try {
				String result = HttpUtility.getmInstance().executeNormalTask("POST", token_info_url, parameters);
				Gson gson = new Gson();
				TokenInfoBean tib = gson.fromJson(result, TokenInfoBean.class);
				if(Integer.parseInt(tib.expire_in)>0){//如果有授权剩余时间则直接进入主界面
					Intent intent = new Intent(LoginActivity.this, MainActivity.class);
					LoginActivity.this.startActivity(intent);
					LoginActivity.this.finish();
				}else{//如果授权剩余时间到了就再次进入授权界面
					flag = false;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Toast.makeText(getApplicationContext(), "网络无法连接", Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
		}
		
		if(!flag){
			WeiboParameters parameters = new WeiboParameters();
			parameters.add("response_type", "token");
			parameters.add("client_id", URLHelper.APP_KEY);
			parameters.add("redirect_uri", URLHelper.REDIRECT_URI);
			parameters.add("display", "mobile");
			parameters.add("scope", URLHelper.SCOPE);
			String url = URLHelper.OAUTH2_AUTHORIZE_URL+"?"+Utility.encodeUrl(parameters);
			wv_login.loadUrl(url);
			wv_login.setWebViewClient(new LoginWebViewClient());
		}
	}
	
	private class LoginWebViewClient extends WebViewClient{
		
	@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				return super.shouldOverrideUrlLoading(view, url);
			}
		
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
			if(url.startsWith(URLHelper.REDIRECT_URI)){
				Bundle values = Utility.parseUrl(url);
				WeiboParameters parameters = new WeiboParameters();
				parameters.add("access_token", values.getString("access_token"));
				parameters.add("uid", values.getString("uid"));
				if(ud.isEmpty(values.getString("uid"))){
					UserBean ub = null;
					try {
						String result = HttpUtility.getmInstance().executeNormalTask("GET", URLHelper.SHOW_USER, parameters);
						Gson gson = new Gson();
						ub = gson.fromJson(result, UserBean.class);
						ub.access_token = values.getString("access_token");
						ud.addUser(ub);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Toast.makeText(getApplicationContext(), "网络无法连接", Toast.LENGTH_LONG).show();
						e.printStackTrace();
					}
					
				}
				
				SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
				Editor editor = sp.edit();
				editor.putString("uid", values.getString("uid"));
				editor.putString("access_token", values.getString("access_token"));
				editor.commit();
				view.stopLoading();
				Intent intent = new Intent(LoginActivity.this, MainActivity.class);
				LoginActivity.this.startActivity(intent);
				LoginActivity.this.finish();
			}
		}
	}
}
