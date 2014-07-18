package com.bingyan.bookrecycle.ui;

import com.bingyan.bookrecycle.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class HomeFragment extends Fragment {

	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
		View view = inflater.inflate(R.layout.main_page, null);
//		GridView bookGrid = (GridView)view.findViewById(R.id.home_grid);
//		bookGrid.setAdapter();
		
		return view;
	}
	
}
