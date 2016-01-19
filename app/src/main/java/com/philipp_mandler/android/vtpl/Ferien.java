package com.philipp_mandler.android.vtpl;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class Ferien extends AppCompatActivity {

    private static final String TAG = Ferien.class.getSimpleName();
    private Toolbar toolbar;
    // json object response url
    private String urlJsonObj = "https://api.smartnoob.de/ferien/v1/ferien/?bundesland=he&jahr=";
    //private String urlDominikTVAPI_FerienHe = "http://dominiktv.net/vtpl/ferien/ferien_he.json";
    private String urlJsonObj2 = "https://api.smartnoob.de/ferien/v1/ferien/?bundesland=he&jahr=";
    // Progress dialog
    private ProgressDialog pDialog;


    // temporary string to show the parsed response
    private String jsonResponse;

    //ListView
    private ListView list;
    private ArrayList<HashMap<String, String>> ferienlist = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ferien);

        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //JodaTimeAndroid.init(this);

        ferienlist = new ArrayList<>();


        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getResources().getString(R.string.ferien_get_data) + "\n" + getResources().getString(R.string.dialog_please_wait));
        pDialog.setCancelable(false);


        //Strings erstellen
        Calendar c = Calendar.getInstance();
        int jahr1 = c.get(Calendar.YEAR);
        int jahr2 = jahr1 + 1;

        urlJsonObj = urlJsonObj + jahr1;

        urlJsonObj2 = urlJsonObj2 + jahr2;

        makeJsonObjectRequest();

        //putDataInListView();

    }

    private void makeJsonObjectRequest() {

        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(urlJsonObj,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {
                            // Getting JSON Array from URL

                            if (response.getInt("error") == 0) {

                                JSONArray daten = response.getJSONArray("daten");
                                for (int i = 0; i < daten.length(); i++) {

                                    jsonResponse = "";

                                    JSONObject c = daten.getJSONObject(i);

                                    long Beginn;
                                    long Ende;

                                    String title = c.getString("title");
                                    Beginn = c.getLong("beginn");
                                    Ende = c.getLong("ende");

                                    //Hessen loeschen
                                    if (title.contains("Hessen")) {
                                        title = title.replace("Hessen", "");
                                    }

                                    long dB = Beginn * 1000;// its need to be in milisecond
                                    java.util.Date dfB = new java.util.Date(dB);
                                    String SB = new SimpleDateFormat("EE, dd.MM.yyyy", Locale.GERMAN).format(dfB);

                                    long dE = Ende * 1000;// its need to be in milisecond
                                    java.util.Date dfE = new java.util.Date(dE);
                                    String SE = new SimpleDateFormat("EE, dd.MM.yyyy", Locale.GERMAN).format(dfE);


                                    java.util.Date now = new java.util.Date();

                                    jsonResponse = SB + " bis " + SE;

                                    if (dfE.before(now)) {
                                        //Toast.makeText(Ferien.this, title + "After", Toast.LENGTH_LONG).show();
                                        title = "Vorbei: " + title;

                                    } else if (dfB.before(now) && dfE.after(now)) {

                                        jsonResponse += " (Aktuell)";

                                    } else {
                                        long oneDay = 1000 * 60 * 60 * 24;
                                        long delta = ((now.getTime() - dfB.getTime()) / oneDay) * -1;

                                        if (delta == 1) {
                                            jsonResponse += " - Noch " + delta + " Tag";
                                        } else {
                                            jsonResponse += " - Noch " + delta + " Tage";
                                        }


                                    }


                                    // Adding value HashMap key => value
                                    HashMap<String, String> map = new HashMap<>();
                                    map.put("title", title);
                                    map.put("zeitraum", jsonResponse);
                                    ferienlist.add(map);
                                    list = (ListView) findViewById(R.id.list);
                                    ListAdapter adapter = new SimpleAdapter(Ferien.this, ferienlist,
                                            R.layout.list_ferien,
                                            new String[]{"title", "zeitraum"}, new int[]{
                                            R.id.name, R.id.zeit});
                                    list.setAdapter(adapter);


                                }
                            } else {
                                setFehlerView();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            setFehlerView();
                            hidepDialog();

                        }
                        //hidepDialog();
                        makeJsonObjectRequest2();

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                setFehlerView();

                // hide the progress dialog
                hidepDialog();
            }
        });


        // Adding request to request queue
        MyController.getInstance().addToRequestQueue(jsonObjReq);


    }

    private void makeJsonObjectRequest2() {


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(urlJsonObj2,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {
                            // Getting JSON Array from URL

                            if (response.getInt("error") == 0) {

                                jsonResponse = "";

                                JSONArray daten = response.getJSONArray("daten");
                                for (int i = 0; i < daten.length(); i++) {

                                    jsonResponse = "";

                                    JSONObject c = daten.getJSONObject(i);

                                    String title = c.getString("title");
                                    long Beginn = c.getLong("beginn");
                                    long Ende = c.getLong("ende");

                                    //Hessen löschen
                                    if (title.contains("Hessen")) {
                                        title = title.replace("Hessen", "");
                                    }

                                    //Log.i("Beginn", "" + Beginn);
                                    //Log.i("Ende", "" + Ende);

                                    //DateTime dt = new DateTime(Beginn + 1000);
                                    //DateTime dt = new DateTime((long)Beginn*1000);
                                    //dt.setTime((long)unix_time*1000);

                                    //Log.i("Info", "" + dt.dayOfMonth().getAsText() + dt.dayOfMonth().getAsShortText() + dt.monthOfYear().getAsShortText() + dt.year().getAsShortText());

                                    long dB = Beginn * 1000;// its need to be in milisecond
                                    java.util.Date dfB = new java.util.Date(dB);
                                    String SB = new SimpleDateFormat("EE, dd.MM.yyyy", Locale.GERMAN).format(dfB);

                                    long dE = Ende * 1000;// its need to be in milisecond
                                    java.util.Date dfE = new java.util.Date(dE);
                                    String SE = new SimpleDateFormat("EE, dd.MM.yyyy", Locale.GERMAN).format(dfE);

                                    java.util.Date now = new java.util.Date();


                                    jsonResponse = SB + " bis " + SE;

                                    if (dfE.before(now)) {
                                        //Toast.makeText(Ferien.this, title + "After", Toast.LENGTH_LONG).show();
                                        title = "Vorbei: " + title;

                                    } else {
                                        long oneDay = 1000 * 60 * 60 * 24;
                                        long delta = (now.getTime() - dfB.getTime()) / oneDay;

                                        jsonResponse = jsonResponse + " - Noch " + delta * -1 + " Tage";
                                    }


                                    // Adding value HashMap key => value
                                    HashMap<String, String> map = new HashMap<>();
                                    map.put("title", title);
                                    map.put("zeitraum", jsonResponse);
                                    ferienlist.add(map);
                                    list = (ListView) findViewById(R.id.list);
                                    ListAdapter adapter = new SimpleAdapter(Ferien.this, ferienlist,
                                            R.layout.list_ferien,
                                            new String[]{"title", "zeitraum"}, new int[]{
                                            R.id.name, R.id.zeit});
                                    list.setAdapter(adapter);

                                }
                            } else {
                                setFehlerView();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            setFehlerView();

                        }
                        hidepDialog();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                setFehlerView();

                // hide the progress dialog
                hidepDialog();
            }
        });

        // Adding request to request queue
        MyController.getInstance().addToRequestQueue(jsonObjReq);

    }


    private void putDataInListView() {

        //Daten
        String[] titel = new String[] {
                "Osterferien 2015",
                "Von 30.03. bis 12.04."
        };


        String[] zeitraum = new String[] {
                "Bright Mode",
                "Normal Mode"
        };

        //ListView
        list = (ListView) findViewById(R.id.list);

        for (int i = 0; i < 10; i++) {


            // Adding value HashMap key => value
            HashMap<String, String> map = new HashMap<>();
            map.put("title", "title");
            map.put("zeitraum", "Test" + i);
            ferienlist.add(map);


        }

        ListAdapter adapter = new SimpleAdapter(Ferien.this, ferienlist,
                R.layout.list_ferien,
                new String[]{"title", "zeitraum"}, new int[]{
                R.id.name, R.id.zeit});
        list.setAdapter(adapter);

    }

    private void showpDialog() {
        if (!pDialog.isShowing()) {
            pDialog.show();
        }
    }

    private void hidepDialog() {
        if (pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    private void setFehlerView() {


        // Adding value HashMap key => value
        HashMap<String, String> map = new HashMap<>();
        map.put("title", getResources().getString(R.string.ferien_error_head));
        map.put("zeitraum", getResources().getString(R.string.ferien_error_text));
        ferienlist.add(map);
        list = (ListView) findViewById(R.id.list);
        ListAdapter adapter = new SimpleAdapter(Ferien.this, ferienlist,
                R.layout.list_ferien,
                new String[]{"title", "zeitraum"}, new int[]{
                R.id.name, R.id.zeit});
        list.setAdapter(adapter);

        Toast.makeText(Ferien.this, "Ein Fehler ist aufgetreten", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ferien, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_apiby) {


            new AlertDialog.Builder(this)
                    .setTitle(R.string.ferien_api_by_head)
                    .setMessage(R.string.ferien_api_by_text)
                    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .show();
            return true;
        }

        if (id == R.id.action_refresh) {
            ferienlist.clear();
            makeJsonObjectRequest();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
