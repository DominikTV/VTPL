package com.philipp_mandler.android.vtpl;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.philipp_mandler.android.vtpl.VtplListAdapter.RowType;

import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SupplyPlanFragment extends ListFragment {

    private List<VtplListItem> m_vtplEntries = new ArrayList<VtplListItem>();
    private LayoutInflater m_inflater;
    private VtplListItem m_lastHeader;
    private View m_lastHeaderView;
    VtplDetailFragment m_detailFragment;

    private ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        m_vtplEntries = new ArrayList<VtplListItem>();

        VtplListAdapter adapter = new VtplListAdapter(inflater.getContext(), m_vtplEntries);
        setListAdapter(adapter);

        m_inflater = inflater;

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        getListView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //nothing
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int pos = firstVisibleItem;
                if (pos != 0) {
                    VtplListItem item;
                    while ((item = (VtplListItem) view.getItemAtPosition(pos)).getViewType() != RowType.HEADER_ITEM.ordinal()) {
                        pos--;
                    }

                    if (item != m_lastHeader) {
                        if (m_lastHeader != null) {
                            ((FrameLayout) getListView().getParent()).removeView(m_lastHeaderView);
                        }
                        m_lastHeader = item;
                        m_lastHeaderView = item.getView(m_inflater, null);
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        m_lastHeaderView.setLayoutParams(params);
                        ((FrameLayout) getListView().getParent()).addView(m_lastHeaderView);
                    }

                    if (m_lastHeader != null) {
                        Object nextHeader = view.getItemAtPosition(firstVisibleItem + 1);
                        if (nextHeader != null) {
                            if (((VtplListItem) nextHeader).getViewType() == RowType.HEADER_ITEM.ordinal()) {
                                if (m_lastHeaderView.getHeight() > view.getChildAt(1).getY())
                                    m_lastHeaderView.setTranslationY(view.getChildAt(1).getY() - view.getChildAt(1).getHeight());
                                else
                                    m_lastHeaderView.setTranslationY(0);
                            } else {
                                m_lastHeaderView.setTranslationY(0);
                            }
                        }
                    }
                }
            }
        });

        try {
            ObjectInputStream inputStream = new ObjectInputStream(getActivity().openFileInput("data.bin"));
            Object object = inputStream.readObject();
            if (object instanceof SaveData) {
                m_vtplEntries.clear();
                Date lastDay = null;
                for (VtplEntry dataEntry : ((SaveData) object).getData()) {
                    if (lastDay == null || !lastDay.equals(dataEntry.getDate())) {
                        m_vtplEntries.add(new VtplListHeaderItem(weekdayToString(dataEntry.getDate().getWeekday()) + ", " + dataEntry.getDate().getDay() + "." + dataEntry.getDate().getMonth() + "."));
                    }
                    lastDay = dataEntry.getDate();
                    m_vtplEntries.add(new VtplListContentItem(dataEntry));
                }
                VtplListAdapter adapter = new VtplListAdapter(m_inflater.getContext(), m_vtplEntries);
                setListAdapter(adapter);
            }
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!Data.updated) {
            GetVtplData dataRequest = new GetVtplData();
            dataRequest.execute("http://www.fricke-consult.de/php/MES_VertretungsplanL.php");
            Data.updated = true;
        }

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> av, View v, int position, long id) {

                if (isAdded()) {

                    try {

                        final String klasse = m_vtplEntries.get(position).getData().getSchoolClass();

                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        String classe;
                        classe = prefs.getString("myClass", "leer");


                        if (klasse.toLowerCase().contains(classe.toLowerCase())) {

                            entferneMarkierteKlasse();

                        } else if (!klasse.equals("-") && !klasse.equals(classe)) {


                            //Mehrere Klassen gefunden, die durch , getrennt sind
                            if (klasse.contains(",")) {

                                String[] klassen = klasse.split(",");

                                for (int i = 0; i < klassen.length; i++)
                                    klassen[i] = klassen[i].trim();

                                final String[] finalKlassen = klassen;

                                new AlertDialog.Builder(getActivity())
                                        .setTitle("Klasse auswählen")
                                        .setSingleChoiceItems(klassen, 0, null)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();

                                                markiereKlasse(finalKlassen[selectedPosition]);

                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        })
                                        .show();
                            }

                            //Nur eine Klasse gefunden
                            else {
                                markiereKlasse(klasse);
                            }

                        }
                    }catch (Exception ignored){
                        //Ignoriere, wenn Header lange angeklickt wird
                    }

                }

                return true;
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {

            GetVtplData dataRequest = new GetVtplData();
            dataRequest.execute("http://www.fricke-consult.de/php/MES_VertretungsplanL.php");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        if (l.getAdapter().getItemViewType(position) == RowType.LIST_ITEM.ordinal()) {
            if (getActivity().findViewById(R.id.main_single) == null) {
                m_detailFragment = new VtplDetailFragment();
                m_detailFragment.setData(m_vtplEntries.get(position).getData());
                FragmentTransaction trans = getFragmentManager().beginTransaction();
                trans.replace(R.id.detailviewPlaceholder, m_detailFragment);
                trans.commit();
            } else {
                m_detailFragment = new VtplDetailFragment();
                m_detailFragment.setData(m_vtplEntries.get(position).getData());
                FragmentTransaction trans = getFragmentManager().beginTransaction();
                trans.replace(R.id.detailviewPlaceholder, m_detailFragment);
                trans.setTransition(FragmentTransaction.TRANSIT_NONE);
                trans.commit();
                getActivity().findViewById(R.id.frameLayout3).setVisibility(View.VISIBLE);
            }
        }

        super.onListItemClick(l, v, position, id);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
    }


    private class GetVtplData extends AsyncTask<String, Void, Document> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            showProgressDialog();

        }

        @Override
        protected Document doInBackground(String... arg0) {

            try {
                //6sek Timeout
                return Jsoup.connect(arg0[0]).data("p", "vtpl").method(Method.POST).timeout(6000).execute().parse();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Document doc) {
            if (doc == null) {
                Toast.makeText(getActivity(), R.string.toast_plan_update_failed, Toast.LENGTH_LONG).show();

                //Menü
                dismissProgressDialog();
                return;
            }

            //Sind Daten OK und könen angezeigt werden?
            boolean DataOK = true;

            if (isAdded()) {

                try {
                    Elements elements = doc.select("tr");
                    if (elements != null) {

                        Log.i("Gefunden", elements.size() + " Elemente");

                        if (!elements.isEmpty()) {

                            List<VtplEntry> dataList = new ArrayList<VtplEntry>();

                            String lastSchoolClass = "-";
                            String lastLesson = "";
                            int i = 0;
                            for (Element element : elements) {
                                Elements tdElements = element.select("td");
                                Element data;

                                i++;
                                Log.i("Anzahl", "" + i);

                                VtplEntry entry = new VtplEntry();
                                // parse day
                                if (tdElements.size() >= 10 && (data = tdElements.get(0)) != null) {
                                    if (!data.html().contains("<b>")) {
                                        if (data.html().contains("&nbsp;")) {
                                            if (dataList.size() != 0)
                                                entry.setDate(dataList.get(dataList.size() - 1).getDate());
                                        } else {
                                            lastSchoolClass = "-";
                                            lastLesson = "";
                                            try {
                                                entry.setDate(new Date(data.text()));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        // parse lesson
                                        if ((data = tdElements.get(2)) != null) {
                                            if (data.html().contains("&nbsp;")) {
                                                entry.setLesson(lastLesson);
                                            } else {
                                                entry.setLesson(data.text());
                                                lastLesson = data.text();
                                            }
                                        }

                                        // parse teacher
                                        if ((data = tdElements.get(3)) != null) {
                                            entry.setTeacher(data.text());
                                        }

                                        // parse room
                                        if ((data = tdElements.get(4)) != null) {
                                            entry.setRoom(data.text());
                                        }

                                        // parse class
                                        if ((data = tdElements.get(5)) != null) {
                                            if (data.html().contains("&nbsp;"))
                                                entry.setSchoolClass(lastSchoolClass);
                                            else {
                                                entry.setSchoolClass(data.text());
                                                lastSchoolClass = data.text();
                                            }
                                        }

                                        // parse supply teacher
                                        if ((data = tdElements.get(6)) != null) {
                                            entry.setSupplyTeacher(data.text());
                                        }


                                        // parse supply room
                                        if ((data = tdElements.get(7)) != null) {
                                            entry.setSupplyRoom(data.text());
                                        }

                                        // parse attribute
                                        if ((data = tdElements.get(8)) != null) {
                                            entry.setAttribute(data.text());
                                        }

                                        // parse info
                                        if ((data = tdElements.get(9)) != null) {
                                            entry.setInfo(data.text());
                                        }

                                        dataList.add(entry);
                                    }

                                    m_vtplEntries.clear();

                                    Date lastDay = null;

                                    for (VtplEntry dataEntry : dataList) {
                                        if (lastDay == null || !lastDay.equals(dataEntry.getDate())) {

                                            if (dataEntry.getDate() != null) {
                                                m_vtplEntries.add(new VtplListHeaderItem(weekdayToString(dataEntry.getDate().getWeekday()) + ", " + dataEntry.getDate().getDay() + "." + dataEntry.getDate().getMonth() + "."));
                                            }
                                            else {
                                                DataOK = false;
                                                Log.e("Website Error", "Website wahrsch. nicht Erreichbar");
                                                Toast.makeText(getActivity(), R.string.toast_plan_update_failed_webdown, Toast.LENGTH_LONG).show();
                                            }
                                        }
                                        if (DataOK) {
                                            lastDay = dataEntry.getDate();
                                            m_vtplEntries.add(new VtplListContentItem(dataEntry));
                                        }
                                    }

                                    if (DataOK) {
                                        try {
                                            ObjectOutputStream outputStream = new ObjectOutputStream(getActivity().openFileOutput("data.bin", Context.MODE_PRIVATE));
                                            SaveData saveData = new SaveData(dataList);
                                            outputStream.writeObject(saveData);
                                            outputStream.close();
                                        } catch (Exception e) {
                                            e.printStackTrace();

                                        }
                                    }
                                }
                            }

                            if (DataOK) {
                                VtplListAdapter adapter = new VtplListAdapter(m_inflater.getContext(), m_vtplEntries);

                                setListAdapter(adapter);

                                Toast.makeText(getActivity(), R.string.toast_plan_updated, Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            if (isAdded()) {
                                Toast.makeText(getActivity(), R.string.toast_plan_update_failed_webdown, Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        if (isAdded()) {
                            Toast.makeText(getActivity(), R.string.toast_plan_update_failed_webdown, Toast.LENGTH_SHORT).show();
                        }
                    }

                    dismissProgressDialog();


                } catch (Throwable e) {
                    e.printStackTrace();

                    dismissProgressDialog();

                }

            } else {

                //Menü
                dismissProgressDialog();
            }

        }
    }

    private String weekdayToString(Weekday day) {
        String weekday;
        switch (day) {
            case Monday:
                weekday = getString(R.string.weekday_monday);
                break;
            case Tuesday:
                weekday = getString(R.string.weekday_tuesday);
                break;
            case Wednesday:
                weekday = getString(R.string.weekday_wednesday);
                break;
            case Thursday:
                weekday = getString(R.string.weekday_thursday);
                break;
            case Friday:
                weekday = getString(R.string.weekday_friday);
                break;
            case Saturday:
                weekday = getString(R.string.weekday_saturday);
                break;
            case Sunday:
                weekday = getString(R.string.weekday_sunday);
                break;
            default:
                weekday = getString(android.R.string.unknownName);
                break;
        }
        return weekday;
    }

    private void showProgressDialog() {
        if (dialog == null) {

            //Verhindere Screen Orientation Change und damit einen Nullpointer-Fehler
            int currentOrientation = getResources().getConfiguration().orientation;
            if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            } else {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            }

            //Dialog Builder
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage(getResources().getString(R.string.dialog_refresh) + "\n" + getResources().getString(R.string.dialog_please_wait));
            dialog.setProgress(0);
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);

        }

        dialog.show();


    }

    private void dismissProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        if (getActivity() != null) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }

    }

    private void markiereKlasse(final String klasse) {
        if (isAdded()) {

            new AlertDialog.Builder(getActivity())
                    .setTitle(klasse + " markieren?")
                    .setMessage("Markiert die Klasse \"" + klasse + "\" als \"Meine Klasse\".\nDies kann in den Einstellungen jederzeit geändert werden.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                            prefs.edit().putString("myClass", klasse).apply();

                            GetVtplData dataRequest = new GetVtplData();
                            dataRequest.execute("http://www.fricke-consult.de/php/MES_VertretungsplanL.php");

                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        }
    }

    private void entferneMarkierteKlasse() {
        if (isAdded()) {

            new AlertDialog.Builder(getActivity())
                    .setTitle("Markierung entfernen?")
                    .setMessage("Entferne deine markierte Klasse.\nDies kann in den Einstellungen jederzeit geändert werden.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                            prefs.edit().remove("myClass").apply();

                            GetVtplData dataRequest = new GetVtplData();
                            dataRequest.execute("http://www.fricke-consult.de/php/MES_VertretungsplanL.php");

                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        }
    }

    @Override
    public void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }

}
