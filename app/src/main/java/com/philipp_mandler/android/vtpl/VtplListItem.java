package com.philipp_mandler.android.vtpl;

import android.view.LayoutInflater;
import android.view.View;

public interface VtplListItem {
	int getViewType();
	View getView(LayoutInflater inflater, View convertView);
	VtplEntry getData();


}
