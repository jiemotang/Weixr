package me.zxr.weixr.adapter;

import java.io.File;
import java.io.InputStream;
import java.sql.Date;
import java.text.DateFormat;
import java.util.Iterator;
import java.util.List;

import me.zxr.weixr.R;
import me.zxr.weixr.bean.WeiboBean;
import me.zxr.weixr.utils.HttpUtility;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class StatusesAdapter extends BaseAdapter{
	private List<WeiboBean> data;
	private Context context;
	private LayoutInflater mInflater;
	
	private static final int SET_PROFILE_IMAGE = 0;
	private static final int SET_STATUSES_IMAGE = 1;
	private static final int SET_STATUSES_REPOST_IMAGE = 2;
	
	public StatusesAdapter(List<WeiboBean> data, Context context) {
		super();
		this.data = data;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			switch (msg.what) {
			case SET_PROFILE_IMAGE:
				Object[] objs = (Object[]) msg.obj;
				iv_statuses_profile_image = (ImageView) objs[0];
				Bitmap bitmap = (Bitmap) objs[1];
					iv_statuses_profile_image.setImageBitmap(bitmap);
				break;
			case SET_STATUSES_IMAGE:
				Object[] objs2 = (Object[]) msg.obj;
				iv_statuses_image = (ImageView) objs2[0];
				Bitmap bitmap2 = (Bitmap) objs2[1];
					iv_statuses_image.setImageBitmap(bitmap2);
				
				break;
			case SET_STATUSES_REPOST_IMAGE:
				Object[] objs3 = (Object[]) msg.obj;
				iv_statuses_repost_image = (ImageView) objs3[0];
				Bitmap bitmap3 = (Bitmap) objs3[1];
					iv_statuses_repost_image.setImageBitmap(bitmap3);
				break;
			default:
				break;
			}
		}
		
	};
	private ImageView iv_statuses_profile_image;
	private TextView tv_statuses_user_name;
	private TextView tv_statuses_time;
	private TextView tv_statuses_text;
	private TextView tv_statuses_source;
	private TextView tv_statuses_comment;
	private ImageView iv_statuses_image;
	private TextView tv_statuses_repost_text;
	private TextView tv_statuses_repost_comment;
	private ImageView iv_statuses_repost_image;
	private TextView tv_statuses_repost_name;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = null;
		WeiboBean wb = data.get(position);
		
		final String path = wb.user.profile_image_url;
		String name = wb.user.screen_name;
		String time = wb.created_at;
		String date = DateFormat.getInstance().format(Date.parse(time));
		String text = wb.text;
		String source = wb.source;
		source = source.substring(0, source.indexOf("</a>"));
		source = "来自 "+source.substring(source.lastIndexOf(">")+1);
		String comment = "转发("+wb.reposts_count+") | 评论("+wb.comments_count+")";
		
		if(wb.retweeted_status == null){//判断是否是原创的微博，等于null是原创，反之则是转发
			view = mInflater.inflate(R.layout.statuses_item, null);
			iv_statuses_profile_image = (ImageView) view.findViewById(R.id.iv_statuses_profile_image);
			tv_statuses_user_name = (TextView) view.findViewById(R.id.tv_statuses_user_name);
			tv_statuses_time = (TextView) view.findViewById(R.id.tv_statuses_time);
			tv_statuses_text = (TextView) view.findViewById(R.id.tv_statuses_text);
			tv_statuses_source = (TextView) view.findViewById(R.id.tv_statuses_source);
			tv_statuses_comment = (TextView) view.findViewById(R.id.tv_statuses_comment);
			iv_statuses_image = (ImageView) view.findViewById(R.id.iv_statuses_image);
			
			final String image_url = wb.thumbnail_pic;
			String[] pics = new String[]{wb.bmiddle_pic, wb.original_pic};
			if(path != null){
				new Thread(){
					public void run() {
						String fileName = path.substring(path.lastIndexOf("cn/")+3);
						fileName = fileName.replace("/", "_")+".jpg";
						Bitmap bitmap = HttpUtility.getmInstance().getPic(fileName);
							if(bitmap == null){
								HttpUtility.getmInstance().loadPic(path, fileName);
								bitmap = HttpUtility.getmInstance().getPic(fileName);
							}
							Message msg = new Message();
							msg.what = SET_PROFILE_IMAGE;
							Object[] objs = new Object[]{iv_statuses_profile_image, bitmap};
							msg.obj = objs;
							mHandler.sendMessage(msg);
						
					};
				}.start();
			}else{
				iv_statuses_profile_image.setVisibility(View.GONE);
			}
			iv_statuses_profile_image.setTag(wb.user.id);
			tv_statuses_user_name.setText(name);
			tv_statuses_user_name.setTag(wb.user.id);
			tv_statuses_time.setText(date);
			
			
			int start = 0;
			int end = 0;
			if(text.indexOf("[") != -1 && text.indexOf("]") != -1){
				System.out.println(text);
			}
			tv_statuses_text.setText(text);
			tv_statuses_source.setText(source);
			tv_statuses_comment.setText(comment);
			
			
			if(image_url != null){
				
				iv_statuses_image.setTag(pics);
				new Thread(){
					public void run() {
						String fileName = image_url.substring(image_url.lastIndexOf("/")+1);
						Bitmap bitmap = HttpUtility.getmInstance().getPic(fileName);
						if(bitmap == null){
							HttpUtility.getmInstance().loadPic(image_url, fileName);
							bitmap = HttpUtility.getmInstance().getPic(fileName);
						}
						Message msg = new Message();
						msg.what = SET_STATUSES_IMAGE;
						Object[] objs = new Object[]{iv_statuses_image, bitmap};
						msg.obj = objs;
						mHandler.sendMessage(msg);
					};
				}.start();
			}else{
				iv_statuses_image.setVisibility(View.GONE);
			}
			//iv_statuses_image.setImageBitmap(HttpUtility.getmInstance().getBitMap(image_url));
		}else{
			view = mInflater.inflate(R.layout.repost_statuses_item, null);
			final ImageView iv_statuses_profile_image = (ImageView) view.findViewById(R.id.iv_statuses_profile_image);
			tv_statuses_user_name = (TextView) view.findViewById(R.id.tv_statuses_user_name);
			tv_statuses_time = (TextView) view.findViewById(R.id.tv_statuses_time);
			tv_statuses_text = (TextView) view.findViewById(R.id.tv_statuses_text);
			tv_statuses_source = (TextView) view.findViewById(R.id.tv_statuses_source);
			tv_statuses_comment = (TextView) view.findViewById(R.id.tv_statuses_comment);
			tv_statuses_repost_text = (TextView) view.findViewById(R.id.tv_statuses_repost_text);
			tv_statuses_repost_comment = (TextView) view.findViewById(R.id.tv_statuses_repost_comment);
			iv_statuses_repost_image = (ImageView) view.findViewById(R.id.iv_statuses_repost_image);
			tv_statuses_repost_name = (TextView) view.findViewById(R.id.tv_statuses_repost_name);
			
				String repost_name = wb.retweeted_status.user.screen_name;
				String repost_text = wb.retweeted_status.text;
				String repost_comment = "转发("+wb.retweeted_status.reposts_count+") | 评论("+wb.retweeted_status.comments_count+")";
				final String repost_image_url = wb.retweeted_status.thumbnail_pic;
				String[] repost_pics = new String[]{wb.retweeted_status.bmiddle_pic, wb.retweeted_status.original_pic};
				
				if(path != null){
					new Thread(){
						public void run() {
							String fileName = path.substring(path.lastIndexOf("cn/")+3);
							fileName = fileName.replace("/", "_")+".jpg";
							Bitmap bitmap = HttpUtility.getmInstance().getPic(fileName);
							if(bitmap == null){
								HttpUtility.getmInstance().loadPic(path, fileName);
								bitmap = HttpUtility.getmInstance().getPic(fileName);
							}
							Message msg = new Message();
							msg.what = SET_PROFILE_IMAGE;
							Object[] objs = new Object[]{iv_statuses_profile_image, bitmap};
							msg.obj = objs;
							mHandler.sendMessage(msg);
						};
					}.start();
				}else{
					iv_statuses_profile_image.setVisibility(View.GONE);
				}
			//iv_statuses_profile_image.setImageBitmap(HttpUtility.getmInstance().getBitMap(path));
			iv_statuses_repost_image.setTag(wb.user.id);
			tv_statuses_user_name.setText(name);
			tv_statuses_user_name.setTag(wb.user.id);
			tv_statuses_time.setText(date);
			tv_statuses_text.setText(text);
			tv_statuses_source.setText(source);
			tv_statuses_comment.setText(comment);
			tv_statuses_repost_name.setText("@"+repost_name+"：");
			tv_statuses_repost_name.setTag(wb.retweeted_status.user.id);
			String space = "";
			for(int i=0;i<("@"+repost_name+"：").length()*4;i++){
				space+=" ";
			}
			tv_statuses_repost_text.setText(space+repost_text);
			tv_statuses_repost_comment.setText(repost_comment);
			
			
			if(repost_image_url != null){
				iv_statuses_repost_image.setTag(repost_pics);
				new Thread(){
					public void run() {
						String fileName = repost_image_url.substring(repost_image_url.lastIndexOf("/")+1);
						Bitmap bitmap = HttpUtility.getmInstance().getPic(fileName);
						if(bitmap == null){
							HttpUtility.getmInstance().loadPic(repost_image_url, fileName);
							bitmap = HttpUtility.getmInstance().getPic(fileName);
						}
						Message msg = new Message();
						msg.what = SET_STATUSES_REPOST_IMAGE;
						Object[] objs = new Object[]{iv_statuses_repost_image, bitmap};
						msg.obj = objs;
						mHandler.sendMessage(msg);
					};
				}.start();
			}else{
				iv_statuses_repost_image.setVisibility(View.GONE);
			}
			//iv_statuses_repost_image.setImageBitmap(HttpUtility.getmInstance().getBitMap(repost_image_url));
			
		}
		return view;
	}
	
}
