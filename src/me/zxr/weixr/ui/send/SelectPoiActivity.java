package me.zxr.weixr.ui.send;

import java.util.List;

import com.google.gson.Gson;
import com.weibo.sdk.android.WeiboParameters;

import me.zxr.weixr.R;
import me.zxr.weixr.adapter.PoiAdapter;
import me.zxr.weixr.bean.PoiBean;
import me.zxr.weixr.bean.PoiListBean;
import me.zxr.weixr.dao.URLHelper;
import me.zxr.weixr.utils.HttpUtility;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SelectPoiActivity extends Activity{
	private String access_token;
	private ListView lv_pois;
	private List<PoiBean> data;
	
	private static final int LOAD_LOCATION_OK = 0;
	
	Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case LOAD_LOCATION_OK:
				data = (List<PoiBean>) msg.obj;
				PoiAdapter adapter = new PoiAdapter(SelectPoiActivity.this, data);
				lv_pois.setAdapter(adapter);
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
		setContentView(R.layout.activity_sp);
		findViews();
		loadLocation();
		lv_pois.setOnItemClickListener(new MyOnItemClickListener());
	}
	
	private void loadLocation(){
		new Thread(){
			public void run() {
				Criteria criteria = new Criteria();
				criteria.setAltitudeRequired(false);
				criteria.setBearingRequired(false);
				criteria.setCostAllowed(true);
				criteria.setPowerRequirement(Criteria.POWER_LOW);
				//拿到位置管理器
				LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				//拿到GSP_Provider
				String provider = lm.getBestProvider(criteria, true);
				//通过provider拿到位置
				Location location = lm.getLastKnownLocation(provider);
				float lat =(float) location.getLatitude();
				float lon = (float) location.getLongitude();
				
				SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
				access_token = sp.getString("access_token", "");
				
				String path = URLHelper.NEARBY_POIS;
				WeiboParameters parameters = new WeiboParameters();
				parameters.add("access_token", access_token);
				parameters.add("lat", lat+"");
				parameters.add("long", lon+"");
				
				String result = HttpUtility.getmInstance().executeNormalTask("GET", path, parameters);
				Gson gson = new Gson();
				PoiListBean plb = gson.fromJson(result, PoiListBean.class);
				Message msg = new Message();
				msg.what = LOAD_LOCATION_OK;
				msg.obj = plb.getPois();
				mHandler.sendMessage(msg);
			};
		}.start();
	}
	
	private class MyOnItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			PoiBean pb = data.get(position);
			Intent data = new Intent();
			data.putExtra("title", pb.title);
			setResult(101, data);
			finish();
		}
		
	}
	
	private void findViews(){
		lv_pois = (ListView) findViewById(R.id.lv_pois);
	}
	
}
