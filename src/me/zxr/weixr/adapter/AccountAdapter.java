package me.zxr.weixr.adapter;

import java.util.List;

import me.zxr.weixr.R;
import me.zxr.weixr.bean.UserBean;
import me.zxr.weixr.utils.HttpUtility;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AccountAdapter extends BaseAdapter{
	private List<UserBean> ubs;
	private Context context;
	private LayoutInflater mInflater;
	private ImageView iv_account_profile;	
	
	private static final int LOAD_PROFILE_OK = 0;
	
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case LOAD_PROFILE_OK:
				Object[] objs = (Object[]) msg.obj;
				Bitmap bitmap = (Bitmap) objs[1];
				iv_account_profile = (ImageView) objs[0];
				iv_account_profile.setImageBitmap(bitmap);
				break;

			default:
				break;
			}
		}
		
	};
	

	public AccountAdapter(List<UserBean> ubs, Context context) {
		super();
		this.ubs = ubs;
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ubs.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return ubs.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		SharedPreferences sp = context.getSharedPreferences("user", context.MODE_PRIVATE);
		String current_uid = sp.getString("uid", "");
		
		View view = mInflater.inflate(R.layout.ua_item, null);
		UserBean ub = ubs.get(position);
		
		iv_account_profile = (ImageView) view.findViewById(R.id.iv_account_profile);
		TextView tv_account_userName = (TextView) view.findViewById(R.id.tv_account_userName);
		ImageView iv_is_current_user = (ImageView) view.findViewById(R.id.iv_is_current_user);
		
		final String profile_url = ub.profile_image_url;
		System.out.println(profile_url);
		String name = ub.screen_name;
		String uid = ub.id;
		
		if(profile_url != null){
			new Thread(){
				public void run() {
					String fileName = profile_url.substring(profile_url.lastIndexOf("cn/")+3);
					fileName = fileName.replace("/", "_")+".jpg";
					Bitmap bitmap = HttpUtility.getmInstance().getPic(fileName);
						if(bitmap == null){
							HttpUtility.getmInstance().loadPic(profile_url, fileName);
							bitmap = HttpUtility.getmInstance().getPic(fileName);
						}
						Message msg = new Message();
						msg.what = LOAD_PROFILE_OK;
						Object[] objs = new Object[]{iv_account_profile, bitmap};
						msg.obj = objs;
						mHandler.sendMessage(msg);
				};
			}.start();
		}
		
		tv_account_userName.setText(name);
		if(current_uid.equals(uid)){
			iv_is_current_user.setVisibility(View.VISIBLE);
		}else{
			iv_is_current_user.setVisibility(View.GONE);
		}
		return view;
	}

}
