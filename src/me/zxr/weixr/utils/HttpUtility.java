package me.zxr.weixr.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Map;

import me.zxr.weixr.socket.SSLSocketFactoryEx;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.sina.weibo.sdk.log.Log;
import com.weibo.sdk.android.WeiboParameters;
import com.weibo.sdk.android.util.Utility;

public class HttpUtility {
	private static HttpUtility mHttpUtility;
	
	
	
	public static HttpUtility getmInstance() {
		if(mHttpUtility == null){
			mHttpUtility = new HttpUtility();
		}
		return mHttpUtility;
	}

	public String executeNormalTask(String method, String path, WeiboParameters parameters){
		String result = null;
		HttpResponse response = null;
		path +=("?"+Utility.encodeUrl(parameters));
		try {
			//从SSL加密工厂拿到httpclient
			HttpClient httpClient = new SSLSocketFactoryEx(null).getNewHttpClient();
			if(method.toUpperCase().equals("GET")){//判断是GET请求
				HttpGet requst = new HttpGet(path);
				response = httpClient.execute(requst);
			}else if(method.toUpperCase().equals("POST")){//还是POST请求
				HttpPost request = new HttpPost(path);
				response = httpClient.execute(request);
			}
			
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode == 200){
				HttpEntity entity = response.getEntity();
				InputStream is = entity.getContent();
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while((len = is.read(buffer)) != -1){
					bos.write(buffer, 0, len);
				}
				result = bos.toString();
				bos.close();
				is.close();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	
	public void loadPic(String path, String name){
		try {
			HttpClient httpClient = new SSLSocketFactoryEx(null).getNewHttpClient();
				HttpGet requst = new HttpGet(path);
				HttpResponse response = httpClient.execute(requst);
				int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode == 200){
				HttpEntity entity = response.getEntity();
				InputStream is = entity.getContent();
				File dir = new File(Environment.getExternalStorageDirectory()+"/weixr/images");
				dir.mkdirs();
				File file = new File(Environment.getExternalStorageDirectory()+"/weixr/images", name);
				if(!file.exists()){
					OutputStream os = new FileOutputStream(file);
					int len = 0;
					byte[] buffer = new byte[1024];
					while((len = is.read(buffer)) != -1){
						os.write(buffer, 0, len);
					}
					os.close();
					is.close();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Bitmap getPic(String name){
		Bitmap bitmap = null;
		File file = new File(Environment.getExternalStorageDirectory()+"/weixr/images", name);
		try {
			InputStream is = new FileInputStream(file);
			bitmap = BitmapFactory.decodeStream(is);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;
	}
}
