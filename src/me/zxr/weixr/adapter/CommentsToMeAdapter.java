package me.zxr.weixr.adapter;

import java.sql.Date;
import java.text.DateFormat;
import java.util.List;

import me.zxr.weixr.R;
import me.zxr.weixr.bean.CommentBean;
import me.zxr.weixr.bean.CommentListBean;
import me.zxr.weixr.bean.WeiboBean;
import me.zxr.weixr.utils.HttpUtility;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;



public class CommentsToMeAdapter extends BaseAdapter{
	protected static final int SET_PROFILE_IMAGE = 0;
	private ImageView iv_comments_to_me_profile;
	private Context context;
	private LayoutInflater mInflater;
	private List<CommentBean> cbs;
	
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			switch (msg.what) {
			case SET_PROFILE_IMAGE:
				Object[] objs = (Object[]) msg.obj;
				iv_comments_to_me_profile = (ImageView) objs[0];
				Bitmap bitmap = (Bitmap) objs[1];
				iv_comments_to_me_profile.setImageBitmap(bitmap);
				break;
			default:
				break;
			}
		}
		
	};
	private TextView tv_comments_to_me_original_name;
	
	
	public CommentsToMeAdapter(Context context,
			List<CommentBean> cbs) {
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.cbs = cbs;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return cbs.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return cbs.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = mInflater.inflate(R.layout.comments_to_me_item, null);
		
		iv_comments_to_me_profile = (ImageView) view.findViewById(R.id.iv_comments_to_me_profile);
		TextView tv_comments_to_me_user_name = (TextView) view.findViewById(R.id.tv_comments_to_me_user_name);
		TextView tv_comments_to_me_time = (TextView) view.findViewById(R.id.tv_comments_to_me_time);
		TextView tv_comments_to_me_text = (TextView) view.findViewById(R.id.tv_comments_to_me_text);
		TextView tv_comments_to_me_original_text = (TextView) view.findViewById(R.id.tv_comments_to_me_original_text);
		tv_comments_to_me_original_name = (TextView) view.findViewById(R.id.tv_comments_to_me_original_name);
		String space = "";
		for(int i=0;i<(tv_comments_to_me_original_name+"：").length()-3*3;i++){
			space+=" ";
		}
		CommentBean cb = cbs.get(position);
		String user_name = cb.user.screen_name;
		
		
		String time = cb.created_at;
		String date = DateFormat.getInstance().format(Date.parse(time));
		
		//String name  = null;
		String original_text = null;
		String original_name = null;
		if(cb.reply_comment != null){
			
			original_name = "@"+cb.reply_comment.user.screen_name+":";
			original_text = cb.reply_comment.text;
			
		}else{
			original_name = "@"+cb.status.user.screen_name+":";
			original_text = cb.status.text;
		}
		String text = cb.text;
	
		
		final String profile_url = cb.user.profile_image_url;
		
		if(profile_url != null){//判断图片路径是否为空
			new Thread(){
				public void run() {
					//把图片的后缀作为文件名
					String fileName = profile_url.substring(profile_url.lastIndexOf("cn/")+3);
					fileName = fileName.replace("/", "_")+".jpg";
					
					//先从本地拿，如果没拿到再把这个图片加载到本地
					Bitmap bitmap = HttpUtility.getmInstance().getPic(fileName);
						if(bitmap == null){
							HttpUtility.getmInstance().loadPic(profile_url, fileName);
							bitmap = HttpUtility.getmInstance().getPic(fileName);
						}
						//加载完后发信息给handler
						Message msg = new Message();
						msg.what = SET_PROFILE_IMAGE;
						Object[] objs = new Object[]{iv_comments_to_me_profile, bitmap};
						msg.obj = objs;
						mHandler.sendMessage(msg);
					
				};
			}.start();
		}
		
		//把信息设置进去
		tv_comments_to_me_user_name.setText(user_name);
		tv_comments_to_me_time.setText(date);
		tv_comments_to_me_text.setText(text);
		tv_comments_to_me_original_name.setText(original_name);
		tv_comments_to_me_original_text.setText(space+original_text);
		
		return view;
	}

}
