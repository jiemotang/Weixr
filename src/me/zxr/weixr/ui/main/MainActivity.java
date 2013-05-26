package me.zxr.weixr.ui.main;


import me.zxr.weixr.R;
import me.zxr.weixr.bean.UserBean;
import me.zxr.weixr.dao.UserDao;
import android.os.Bundle;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.view.KeyEvent;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


public class MainActivity extends TabActivity {
	UserDao ud = new UserDao(this);
	public static final int SET_DATA = 0;
	
	private TabHost mTabHost;

	private RelativeLayout rl_main_menu;
	private ImageView tab_icon_index;
	private ImageView tab_icon_mail;
	private ImageView tab_icon_ad;
	private ImageView tab_icon_me;
	private ImageView tab_icon_more;
	private ListView lv_main_status;
	private String access_token;
	private ImageView tab_bg;
	
//	private HorizontalScrollView hsv_main;
	
	/*private View refreshView;
	
	private float lastY;
	// 拉动标记
	private boolean isDragging = false;
	// 是否可刷新标记
	private boolean isRefreshEnabled = true;
	// 在刷新中标记
	private boolean isRefreshing = false;*/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViews();
		
		
		SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
		
		String uid = sp.getString("uid", "");
		UserBean ub = ud.findUserById(uid);
		String name = ub.screen_name;
		access_token = ub.access_token;
		
		
		mTabHost = getTabHost();
		TabSpec tab1 = mTabHost.newTabSpec("tab1");
		tab_icon_index = new ImageView(this);
		tab_icon_index.setImageDrawable(getResources().getDrawable(R.drawable.home_tab_icon_1));
		tab1.setIndicator(tab_icon_index);
		tab1.setContent(new Intent(this, HomeActivity.class));
		mTabHost.addTab(tab1);
		
		TabSpec tab2 = mTabHost.newTabSpec("tab2");
		tab_icon_mail = new ImageView(this);
		tab_icon_mail.setImageDrawable(getResources().getDrawable(R.drawable.home_tab_icon_2));
		tab2.setIndicator(tab_icon_mail);
		tab2.setContent(new Intent(this, MailActivity.class));
		mTabHost.addTab(tab2);
		
		TabSpec tab3 = mTabHost.newTabSpec("tab3");
		tab_icon_ad = new ImageView(this);
		tab_icon_ad.setImageDrawable(getResources().getDrawable(R.drawable.home_tab_icon_3));
		tab3.setIndicator(tab_icon_ad);
		tab3.setContent(new Intent(this, AdActivity.class));
		mTabHost.addTab(tab3);
		
		TabSpec tab4 = mTabHost.newTabSpec("tab4");
		tab_icon_me = new ImageView(this);
		tab_icon_me.setImageDrawable(getResources().getDrawable(R.drawable.home_tab_icon_4));
		tab4.setIndicator(tab_icon_me);
		tab4.setContent(new Intent(this, MeActivity.class));
		mTabHost.addTab(tab4);
		
		TabSpec tab5 = mTabHost.newTabSpec("tab5");
		tab_icon_more = new ImageView(this);
		tab_icon_more.setImageDrawable(getResources().getDrawable(R.drawable.home_tab_icon_5));
		tab5.setIndicator(tab_icon_more);
		tab5.setContent(new Intent(this, MoreActivity.class));
		mTabHost.addTab(tab5);
		
		mTabHost.setOnTabChangedListener(new MyOnTabChangeListener());
	}
	
	float current_X = 0;
	private class MyOnTabChangeListener implements OnTabChangeListener{

		@Override
		public void onTabChanged(String tabId) {
			// TODO Auto-generated method stub
			TranslateAnimation animation = null;
			if(tabId.equals("tab1")){
				animation = new TranslateAnimation(current_X, 0, 0, 0);
				current_X = 0;
			}else if(tabId.equals("tab2")){
				int me_x = 95;
					animation = new TranslateAnimation(current_X, me_x, 0, 0);
				current_X = 95;
			}else if(tabId.equals("tab3")){
				int me_x = 190;
				animation = new TranslateAnimation(current_X, me_x, 0, 0);
				current_X = 190;
			}else if(tabId.equals("tab4")){
				int me_x = 286;
				animation = new TranslateAnimation(current_X, me_x, 0, 0);
				current_X = 286;
			}else if(tabId.equals("tab5")){
				int me_x = 383;
				animation = new TranslateAnimation(current_X, me_x, 0, 0);
				current_X = 383;
			}
			animation.setFillAfter(true);
			animation.setDuration(500);
			tab_bg.startAnimation(animation);
		}
		
	}
	
	long oneTime = 0;
	
	
	
@Override
public boolean dispatchKeyEvent(KeyEvent event) {
	// TODO Auto-generated method stub
	if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
		/*如果这一次按返回键时间减去上一次按返回键时间的间隔大于2000ms判定为失效的
		 * 所以再按返回键就会在此提示是否退出
		 */
		if( (System.currentTimeMillis() - oneTime ) > 2000 ||  (System.currentTimeMillis() - oneTime )<200 ){
			Toast.makeText(getApplicationContext(), "再次按返回键退出", 1).show();
			oneTime = System.currentTimeMillis();
		}else{//反之就退出
			System.out.println("进了");
				MainActivity.this.finish();
				System.exit(0);
		}
		return true;
	}
	return super.dispatchKeyEvent(event);
}

	/*@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			lastY = event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			int m = (int) (event.getY() - lastY);
			if(((m < 6) && (m > -1)) || (!isDragging )){
				doMovement(m);
				}
			//记录下此刻y坐标
			this.lastY = event.getY();
			break;
		case MotionEvent.ACTION_UP:
			fling();
			break;
		default:
			break;
		}
		
		return true;
		
	}*/
	
	
	
	
	/*private void fling() {
		// TODO Auto-generated method stub
		
	}

	private void doMovement(int moveY) {
		// TODO Auto-generated method stub
		LinearLayout.LayoutParams lp = (LayoutParams) refreshView.getLayoutParams();
		if(moveY > 0){
			//获取view的上边距
			float f1 =lp.topMargin;
			float f2 = moveY * 0.3F;
			int i = (int)(f1+f2);
			//修改上边距
			lp.topMargin = i;
			//修改后刷新
			refreshView.setLayoutParams(lp);
			refreshView.invalidate();
		//	invalidate();
			}
	}
*/
	private void findViews(){
		rl_main_menu = (RelativeLayout) findViewById(R.id.rl_main_menu);
		tab_bg = (ImageView) findViewById(R.id.tab_bg);
//		hsv_main = (HorizontalScrollView) findViewById(R.id.hsv_main);
	}
	
	
	
	
	
}
