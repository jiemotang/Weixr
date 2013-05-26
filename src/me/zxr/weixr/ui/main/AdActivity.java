package me.zxr.weixr.ui.main;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AdActivity extends Activity{
	 @Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			TextView tv = new TextView(this);
			tv.setText("ad");
			setContentView(tv);
		}
}
