package me.zxr.weixr.ui.splash;

import java.util.ArrayList;
import java.util.List;

import me.zxr.weixr.R;
import me.zxr.weixr.adapter.MyPageAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

public class SplashActivity extends Activity{
	private ViewPager vp_splash;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		vp_splash = (ViewPager) findViewById(R.id.vp_splash);
		
		List<View> views = new ArrayList<View>();
		View view1 = new View(this);
		view1.setBackgroundDrawable(getResources().getDrawable(R.drawable.nf_01));
		View view2 = new View(this);
		view2.setBackgroundDrawable(getResources().getDrawable(R.drawable.nf_02));
		View view3 = getLayoutInflater().inflate(R.layout.splash_inlet, null);
		views.add(view1);
		views.add(view2);
		views.add(view3);
		
		MyPageAdapter myPageAdapter = new MyPageAdapter(views);
		vp_splash.setAdapter(myPageAdapter);
	}
}
