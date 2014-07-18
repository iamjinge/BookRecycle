package com.bingyan.bookrecycle.adapter;

import java.util.List;
import java.util.Map;

import com.bingyan.bookrecycle.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class BookGridAdapter extends BaseAdapter
{
//	private List<Bitmap> bData;
	private List<String> pData;
	private Context mContext = null;
    private LayoutInflater mLayoutInflater = null;
	
    private ImageView image;
//    private ImageCache imageCache;
	
	public BookGridAdapter(Context context, List<String> pData)
	{
//		this.bData = bData;
		this.pData = pData;
		this.mContext = context;
//		imageCache = new ImageCache(context);
		mLayoutInflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pData.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = null;
		
		if(convertView == null){
			v = mLayoutInflater.inflate(R.layout.grid_layout, null);
			
			ViewHolder holder = new ViewHolder();
			
			v.setTag(holder);
			
		}else{
			v = convertView;
		}
		
		ViewHolder holder = (ViewHolder) v.getTag();
		
		return v;
	}
	
	private static class ViewHolder{
	}
	
}
