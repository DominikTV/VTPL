package com.philipp_mandler.android.vtpl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public class VtplListAdapter extends ArrayAdapter<VtplListItem> {
	
	private LayoutInflater m_inflater;
	private List<VtplListItem> m_items;
	
	public enum RowType {
		LIST_ITEM, HEADER_ITEM
	}

	public VtplListAdapter(Context context, List<VtplListItem> items) {
		super(context, 0, items);
		m_items = items;
		m_inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getViewTypeCount() {
		return RowType.values().length;
	}
	
	@Override
	public int getItemViewType(int position) {
		return m_items.get(position).getViewType();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {


		return m_items.get(position).getView(m_inflater, convertView);
	}

}
