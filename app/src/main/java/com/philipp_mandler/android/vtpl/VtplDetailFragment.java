package com.philipp_mandler.android.vtpl;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class VtplDetailFragment extends Fragment {
	
	private boolean m_created = false;
	
	VtplEntry m_data;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_detail, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		m_created = true;
		if(m_data != null)
			showData();
		super.onViewCreated(view, savedInstanceState);
	}
	
	public void setData(VtplEntry data) {
		m_data = data;
		if(m_created) {
			showData();
		}
	}
	
	private void showData() {
		((TextView)getActivity().findViewById(R.id.schoolClass)).setText(m_data.getSchoolClass());
		((TextView)getActivity().findViewById(R.id.date)).setText(m_data.getDate().getDay() + "." + m_data.getDate().getMonth() + "." + m_data.getDate().getYear());


		switch (m_data.getLesson())
		{
			case "1":
				((TextView) getActivity().findViewById(R.id.lesson)).setText(m_data.getLesson() + " (8:00 - 8:45 Uhr)");
				break;
			case "2":
				((TextView) getActivity().findViewById(R.id.lesson)).setText(m_data.getLesson() + " (8:45 - 9:30 Uhr)");
				break;
			case "3":
				((TextView) getActivity().findViewById(R.id.lesson)).setText(m_data.getLesson() + " (9:45 - 10:30 Uhr)");
				break;
			case "4":
				((TextView) getActivity().findViewById(R.id.lesson)).setText(m_data.getLesson() + " (10:45 - 11:15 Uhr)");
				break;
			case "5":
				((TextView) getActivity().findViewById(R.id.lesson)).setText(m_data.getLesson() + " (11:30 - 12:15 Uhr)");
				break;
			case "6":
				((TextView) getActivity().findViewById(R.id.lesson)).setText(m_data.getLesson() + " (12:15 - 13:00 Uhr)");
				break;
			case "7":
				((TextView) getActivity().findViewById(R.id.lesson)).setText(m_data.getLesson() + " (13:30 - 14:15 Uhr)");
				break;
			case "8":
				((TextView) getActivity().findViewById(R.id.lesson)).setText(m_data.getLesson() + " (14:15 - 15:00 Uhr)");
				break;
			case "9":
				((TextView) getActivity().findViewById(R.id.lesson)).setText(m_data.getLesson() + " (15:15 - 16:00 Uhr)");
				break;
			case "10":
				((TextView) getActivity().findViewById(R.id.lesson)).setText(m_data.getLesson() + " (16:00 - 16:45 Uhr)");
				break;
			case "11":
				((TextView) getActivity().findViewById(R.id.lesson)).setText(m_data.getLesson() + " (17:00 - 17:45 Uhr)");
				break;
			case "12":
				((TextView) getActivity().findViewById(R.id.lesson)).setText(m_data.getLesson() + " (17:45 - 18:30 Uhr)");
				break;
			default:
				((TextView) getActivity().findViewById(R.id.lesson)).setText(m_data.getLesson());
				break;
		}

		((TextView)getActivity().findViewById(R.id.teacher)).setText(m_data.getTeacher());
		((TextView)getActivity().findViewById(R.id.supplyTeacher)).setText(m_data.getSupplyTeacher());
		((TextView)getActivity().findViewById(R.id.room)).setText(m_data.getRoom());
		((TextView)getActivity().findViewById(R.id.supplyRoom)).setText(m_data.getSupplyRoom());
		((TextView)getActivity().findViewById(R.id.info)).setText(m_data.getInfo());
	}
}
