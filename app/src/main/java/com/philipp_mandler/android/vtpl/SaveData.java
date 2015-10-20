package com.philipp_mandler.android.vtpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SaveData implements Serializable {

	private static final long serialVersionUID = -2367216842732549436L;
	
	private List<VtplEntry> m_data;
	
	public SaveData() {
		m_data = new ArrayList<VtplEntry>();
	}
	
	public SaveData(List<VtplEntry> data) {
		m_data = data;
	}
	
	public List<VtplEntry> getData() {
		return m_data;
	}
	
	public void setData(List<VtplEntry> data) {
		m_data = data;
	}
	
	public void addEntry(VtplEntry entry) {
		m_data.add(entry);
	}
	
	public void clear() {
		m_data.clear();
	}

}
