package com.philipp_mandler.android.vtpl;

import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.philipp_mandler.android.vtpl.VtplListAdapter.RowType;

public class VtplListContentItem extends android.support.v4.app.ActivityCompat implements VtplListItem {

    private VtplEntry m_data;

    public VtplListContentItem(VtplEntry data) {
        m_data = data;
    }

    @Override
    public int getViewType() {
        return RowType.LIST_ITEM.ordinal();
    }


    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;

        if (convertView == null) {
            view = inflater.inflate(R.layout.list_item, null);
        } else {
            view = convertView;
        }

        TextView textClass = (TextView) view.findViewById(R.id.text_class);
        TextView textLesson = (TextView) view.findViewById(R.id.text_lesson);
        TextView textRoom = (TextView) view.findViewById(R.id.text_room);
        TextView textSupplyRoom = (TextView) view.findViewById(R.id.text_supplyRoom);
        TextView textSupplyTeacher = (TextView) view.findViewById(R.id.text_supplyTeacher);
        TextView textTeacher = (TextView) view.findViewById(R.id.text_teacher);

        //Meine Klasse
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(inflater.getContext());
        String classe;
        classe = prefs.getString("myClass", "leer");

        //Setze Daten
        textClass.setText(m_data.getSchoolClass());
        textLesson.setText(m_data.getLesson() + ". Stunde");
        textRoom.setText(m_data.getRoom());
        textSupplyRoom.setText(m_data.getSupplyRoom());
        textSupplyTeacher.setText(m_data.getSupplyTeacher());
        textTeacher.setText(m_data.getTeacher());


        //Entferne eventuelle Leerzeichen
        if (classe.contains(" ")) {
            classe = classe.replace(" ", "");
            prefs.edit().putString("myClass", classe).apply();
        }
        if (classe.equals("-")) {
            prefs.edit().remove("myClass").apply();
        }

        //Markieren

        //Wenn keine Klasse eingetragen wurde
        if (classe.equals("leer") || classe.equals("") || classe.isEmpty()) {


            if (Build.VERSION.SDK_INT >= 21) {
                view.setBackgroundResource(R.drawable.ripple);
            } else {
                view.setBackgroundResource(R.color.listContentBG);
            }


        }
        //Wenn doch Klasse eingetragen
        else {

            //Aktuelle Klassen enthält Klassennamen aus Preferences
            if (m_data.getSchoolClass().toLowerCase().contains(classe.toLowerCase())) {

                if (Build.VERSION.SDK_INT >= 21) {
                    view.setBackgroundResource(R.drawable.ripple_highlight);
                } else {
                    view.setBackgroundResource(R.color.highlight);
                }
            } else {
                if (Build.VERSION.SDK_INT >= 21) {
                    view.setBackgroundResource(R.drawable.ripple);
                } else {
                    view.setBackgroundResource(R.color.listContentBG);
                }
            }

        }

        //Markieren Ende

        return view;
    }

    @Override
    public VtplEntry getData() {
        return m_data;
    }

}
