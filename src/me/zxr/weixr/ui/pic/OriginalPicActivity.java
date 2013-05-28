package me.zxr.weixr.ui.pic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import me.zxr.weixr.R;
import me.zxr.weixr.socket.SSLSocketFactoryEx;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class OriginalPicActivity extends Activity{
	private InputStream is;
	
	private static final int LOAD_PIC_OK = 0;
	
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case LOAD_PIC_OK:
				Toast.makeText(getApplicationContext(), "图片已保存至SDCard/weixr_pic目录下", 1).show();
				break;

			default:
				break;
			}
		}
		
	};
	private String path;
	private HttpClient httpClient;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_original_pic);
		ImageView iv_original_pic = (ImageView) findViewById(R.id.iv_original_pic);
		
		Intent intent = getIntent();
		path = intent.getStringExtra("path");
		
		
		try {
			httpClient = new SSLSocketFactoryEx(null).getNewHttpClient();
			HttpGet requst = new HttpGet(path);
			HttpResponse response = httpClient.execute(requst);
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode == 200){
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
				Bitmap bitmap = BitmapFactory.decodeStream(is);
				iv_original_pic.setImageBitmap(bitmap);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void save(View view){
		new Thread(){
			public void run() {
				File dir  = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/weixr_pic");
				if(!dir.exists()){
					dir.mkdirs();
				}
				File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/weixr_pic", System.currentTimeMillis()+".jpg");
				try {
					
					httpClient = new SSLSocketFactoryEx(null).getNewHttpClient();
					HttpGet requst = new HttpGet(path);
					HttpResponse response = httpClient.execute(requst);
					int statusCode = response.getStatusLine().getStatusCode();
					if(statusCode == 200){
						HttpEntity entity = response.getEntity();
						is = entity.getContent();
						
						OutputStream os = new FileOutputStream(file);
						int len = 0;
						byte[] buffer = new byte[1024];
						
						while((len = is.read(buffer)) != -1){
							os.write(buffer, 0, len);
						}
						os.close();
						is.close();
						Message msg = new Message();
						msg.what = LOAD_PIC_OK;
						mHandler.sendMessage(msg);
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("异常");
				}
			};
		}.start();

	}
}
