package com.philipp_mandler.android.vtpl;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.philipp_mandler.android.vtpl.VtplListAdapter.RowType;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class VtplListHeaderItem implements VtplListItem {

	String m_title;
	
	public VtplListHeaderItem(String title) {
		m_title = title;
	}
	
	public int getViewType() {
		return RowType.HEADER_ITEM.ordinal();
	}

	public View getView(LayoutInflater inflater, View convertView) {
		View view;
		if(convertView == null) {
			view = inflater.inflate(R.layout.list_header, null);
		}
		else {
			view = convertView;
		}
		
		TextView textTitle = (TextView)view.findViewById(R.id.list_header_title);


		//g = gestern
		GregorianCalendar g = new GregorianCalendar();
		g.add(Calendar.DATE, -1);

        //h = Heute
        GregorianCalendar h = new GregorianCalendar();

        //m = Morgen
        GregorianCalendar m = new GregorianCalendar();
        m.add(Calendar.DATE, 1);


        //Formatiere Datum zu String in passenden Format
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d.M.", Locale.GERMAN);

		//Formatiere Gestern
		String formattedDateG = sdf.format(g.getTime());

        //Formatiere Heute
        String formattedDateH = sdf.format(h.getTime());

        //Formatiere Morgen
        String formattedDateM = sdf.format(m.getTime());

        //Setze "(Heute)"
        if (m_title.contains(formattedDateH) && !m_title.contains("(Heute)")) {
            textTitle.setText(m_title + " (Heute)");
        }
        //Setze "(Morgen)"
        else if (m_title.contains(formattedDateM) && !m_title.contains("(Morgen)") && !m_title.contains("(Gestern)")) {
            textTitle.setText(m_title + " (Morgen)");
        }
		//Setze "(Gestern)"
		else if (m_title.contains(formattedDateG) && !m_title.contains("(Gestern)") && !m_title.contains("(Morgen)")) {
			textTitle.setText(m_title + " (Gestern)");
		}
        else { textTitle.setText(m_title); }



        //Log
        //Log.i("H", formattedDateH);
        //Log.i("M", formattedDateM);

        //Return
		return view;
	}
	
	@Override
	public VtplEntry getData() {
		return null;
	}

}
