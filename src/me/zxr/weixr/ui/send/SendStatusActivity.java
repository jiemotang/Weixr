package me.zxr.weixr.ui.send;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weibo.sdk.android.WeiboParameters;

import me.zxr.weixr.R;
import me.zxr.weixr.bean.EmotionBean;
import me.zxr.weixr.dao.URLHelper;
import me.zxr.weixr.utils.HttpUtility;
import android.R.drawable;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class SendStatusActivity extends Activity{
	private String access_token;
	private String uid;
	private GridView gv_metions;
	private LinearLayout ll_metion;
	private EditText et_status_text;
	private List<Map<String, Object>> data;
	private TextView tv_surplus_word;
	private static final int LOAD_METIONS_OK = 0;

	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what){
			case LOAD_METIONS_OK:
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
		setContentView(R.layout.activity_ss_main);
		findViews();
		SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
		access_token = sp.getString("access_token", "");
		uid = sp.getString("uid", "");
		
		loadEmotions();
		et_status_text.setOnClickListener(new OnStatusTextClickListener());
		gv_metions.setOnItemClickListener(new onMetionsItemClickListener());
		
		et_status_text.setOnKeyListener(new MyOnKeyListener());
	}
	
	private class MyOnKeyListener implements OnKeyListener{

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			System.out.println("进来");
			tv_surplus_word.setText((140 - et_status_text.getText().length())+"");
			return false;
		}
		
	}
	
	
	private void loadEmotions(){
		getResources().getDrawable(R.drawable.aini);
		EmotionBean eb = new EmotionBean();
		data = eb.getEmotions();
		SimpleAdapter adapter = new SimpleAdapter(
				this,
				data,
				R.layout.metions_item,
				new String[]{"icon", "value"},
				new int[]{R.id.iv_metion, R.id.tv_metion_value});
		gv_metions.setAdapter(adapter);
	}
	
	boolean metion_open = false;
	
	
	
	public void metion_buton(View view){
		if(!metion_open){
			ll_metion.setVisibility(View.VISIBLE);
			metion_open = true;
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			if(!imm.isActive()){
				//如果键盘处于打开状态就关闭，如果键盘处于关闭状态就代开
				imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}else{
			metion_open = false;
			ll_metion.setVisibility(View.GONE);
		}
		
	}
	
	private class OnStatusTextClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(metion_open){//判断表情是否打开
				ll_metion.setVisibility(View.GONE);
				metion_open = false;
			}
		}
		
	}
	
	private class onMetionsItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Map<String, Object> map = data.get(position);
			Drawable drawable = getResources().getDrawable((Integer) map.get("icon"));
			drawable.setBounds(0, 0, 
                    drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
			ImageSpan imageSpan = new ImageSpan(drawable);
			String value = map.get("value")+"";
			SpannableString spannableString = new SpannableString(value+"");
			spannableString.setSpan(imageSpan, 0, value.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
			Editable editable = et_status_text.getEditableText();
			int selectionStart = et_status_text.getSelectionStart();
			 if(selectionStart<0){
					editable.append(spannableString);
				}else{
					editable.insert(selectionStart, spannableString);
				}
		}
		
	}
	
	/**
	 * 软键盘打开
	 * @param view
	 */
	public void keybord_open(View view){
		metion_open = false;
		ll_metion.setVisibility(View.GONE);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		//如果键盘处于打开状态就关闭，如果键盘处于关闭状态就代开
		imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	/**
	 * 回退
	 * @param view
	 */
	public void text_del(View view){
		String str = et_status_text.getText()+"";
		if(str != null && !str.equals("")){
			if(str.endsWith("]")){
				str = str.substring(0, str.lastIndexOf("["));
			}else{
				str = str.substring(0, str.length()-1);
			}
			et_status_text.setText(str);
		}
		et_status_text.setSelection(str.length());
		
	}
	
	public void huati_open(View view){
		
		String str = et_status_text.getText()+"##";
				et_status_text.setText(str);
		et_status_text.setSelection(str.length()-1);
	}
	
	public void back(View view){
		this.finish();
	}
	
	public void send(View view){
		String path = URLHelper.UPDATE_STATUS;
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		NameValuePair nvp1 = new BasicNameValuePair("access_token", access_token);
		NameValuePair nvp2 = new BasicNameValuePair("status", et_status_text.getText()+"");
		parameters.add(nvp1);
		parameters.add(nvp2);
		
		String result = HttpUtility.getmInstance().executeNormalTaskPost(path, parameters);
		System.out.println(access_token);
		System.out.println(et_status_text.getText()+"");
		System.out.println(result+"");
		Toast.makeText(this, "发送成功", 1).show();
		finish();
	}
	
	public void poi_open(View view){
		Intent intent = new Intent(this, SelectPoiActivity.class);
		startActivityForResult(intent, 101);
		
		
	}
	
	private void findViews(){
		gv_metions = (GridView) findViewById(R.id.gv_metions);
		ll_metion = (LinearLayout) findViewById(R.id.ll_metion);
		et_status_text = (EditText) findViewById(R.id.et_status_text);
		tv_surplus_word = (TextView) findViewById(R.id.tv_surplus_word);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		String title = data.getStringExtra("title");
		et_status_text.setText(et_status_text.getText()+"我在：#"+title+"#");
		et_status_text.setSelection(et_status_text.getText().length());
	}
	
	
}
