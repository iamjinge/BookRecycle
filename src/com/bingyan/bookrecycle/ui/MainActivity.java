package com.bingyan.bookrecycle.ui;

import com.bingyan.bookrecycle.R;
import com.bingyan.bookrecycle.R.layout;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		RelativeLayout main = new RelativeLayout(this);
		
		LinearLayout bottom = (LinearLayout)View.inflate(this, R.layout.bottom, null);
		LayoutParams bottomParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		bottomParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		
		HomeFragment homeFragmet = new HomeFragment();
		
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.add(homeFragmet, "home");
		ft.commit();
//		getFragmentManager().beginTransaction().add(main.getId(), homeFragmet);
		
		main.addView(bottom, bottomParams);
		setContentView(R.layout.activity_main);
	}
	
}
