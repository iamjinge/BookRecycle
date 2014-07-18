package com.bingyan.bookrecycle;

import java.util.zip.Inflater;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
	private Spinner spinner=null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        RelativeLayout mainRe = new RelativeLayout(this);
        LinearLayout view = (LinearLayout) View.inflate(this, R.layout.title, null);
        LinearLayout main = (LinearLayout) View.inflate(this, R.layout.activity_main, null);
        
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
        
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
        		RelativeLayout.LayoutParams.MATCH_PARENT,
        		RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
//        params.
        mainRe.addView(view,params);
        mainRe.addView(main);
        setContentView(mainRe);
        
    }
}
