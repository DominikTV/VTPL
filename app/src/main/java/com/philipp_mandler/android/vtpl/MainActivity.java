package com.philipp_mandler.android.vtpl;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Arrow designed by Jardson Ara?jo from The Noun Project

    //Static String
    private final String FIRST_TIME_LAUNCH = "FirstTimeLaunch";
    private final String FIRST_TIME_LAUNCH_Main = "FirstTimeLaunchMain";
    //URLs
    private String newsLink = "https://dev.dominiktv.net/vtpl/news/news.json";
    //private String killSwitchUrl = "https://dev.dominiktv.net/vtpl/killswitch/update.json";
    private Toolbar toolbar;

    //News
    private String newsTextHead = "";
    private String newsTextText = "";
    private String newsUrl = "";

    //DBTE
    private boolean doubleBackToExitPressedOnce;

    //NavDraw
    private NavigationView navigationView;
    private DrawerLayout drawer;

    public MainActivity() {
        Log.i("Acctivity", "Activity konstructor");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        //Toolbar
        setupToolbar();

        //Drawer
        setupNavDrawer();

        //Stuff
        //killSwitch();
        news();
    }

    private void setupNavDrawer() {

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle(R.string.app_title);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        //killSwitch();
        news();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_notenrechner) {
            //Notenrechner
            Intent intent = new Intent(this, Notenrechner.class);
            startActivity(intent);

        } else if (id == R.id.nav_ferien) {
            //Ferien
            Intent intent = new Intent(this, Ferien.class);
            startActivity(intent);

        } else if (id == R.id.nav_settings) {
            //Einstellungen
            Intent intent = new Intent(this, PreferenceActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_website) {
            //Source Code
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("http://goo.gl/gAklCJ"));
            startActivity(i);

        } else if (id == R.id.nav_cafeteria) {
            //Source Code
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("http://www.mes-ks.net/schueler-eltern/cafeteria/"));
            startActivity(i);

        } else if (id == R.id.nav_bug) {
            //Bug report
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://github.com/DominikTV/VTPL/issues"));
            startActivity(i);

        } else if (id == R.id.nav_news) {
            //Nachricht

            //Zeige URL und Text
            if (newsUrl != null && !newsUrl.isEmpty()) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(newsTextHead)
                        .setMessage(newsTextText)
                        .setPositiveButton(R.string.news_OpenWebsite, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Open Website
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(newsUrl));
                                startActivity(i);

                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Close
                            }
                        })
                        .show();
            }

            //Zeige Text
            else {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(newsTextHead)
                        .setMessage(newsTextText)
                        .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Close
                            }
                        })
                        .show();

            }

            //---

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
    */

    @Override
    public void onBackPressed() {

        if (findViewById(R.id.frameLayout3) != null && findViewById(R.id.frameLayout3).getVisibility() == View.VISIBLE) {
            findViewById(R.id.frameLayout3).setVisibility(View.INVISIBLE);

        } else if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            final Boolean doubletoexit = prefs.getBoolean("doubletoexit", false);

            if (doubletoexit) {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, R.string.home_toast_dbte, Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            } else {
                super.onBackPressed();
            }

        }
    }

/*
    private void killSwitch() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(killSwitchUrl,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("KS", response.toString());

                        try {
                            // Getting JSON Array from URL

                            if (response.getBoolean("updateavailable")) {

                                final String PlayStoreURL = response.getString("playstore");

                                if (response.getBoolean("updatekill")) {
                                    //Update da und alter VTPL abgeschaltet

                                    new AlertDialog.Builder(MainActivity.this)
                                            .setTitle(R.string.ks_update_kill_head)
                                            .setCancelable(false)
                                            .setIcon(R.drawable.ic_launcher)
                                            .setMessage(R.string.ks_update_kill_text)
                                            .setPositiveButton(R.string.ks_quellcode, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://goo.gl/gAklCJ"));
                                                    finish();
                                                    startActivity(browserIntent);
                                                }
                                            })
                                            .setNegativeButton(R.string.ks_update_newApp, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(PlayStoreURL));
                                                    finish();
                                                    startActivity(browserIntent);
                                                }
                                            })
                                            .show();


                                } else {

                                    //Update da, aber alter VTPL funktioniert noch

                                    if (!Data.later) {
                                        new AlertDialog.Builder(MainActivity.this)
                                                .setTitle(R.string.ks_update_available_head)
                                                .setCancelable(false)
                                                .setIcon(R.drawable.ic_launcher)
                                                .setMessage(R.string.ks_update_available_text)
                                                .setPositiveButton(R.string.ks_update_aviliable_later, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Data.later = true;
                                                        firstLaunch();
                                                    }
                                                })
                                                .setNegativeButton(R.string.ks_update_newApp, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(PlayStoreURL));

                                                        startActivity(browserIntent);
                                                    }
                                                })
                                                .show();
                                    }

                                }

                            }
                            firstLaunch();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            firstLaunch();

                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("KS", "Error: " + error.getMessage());
                firstLaunch();
            }
        });


        // Adding request to request queue
        MyController.getInstance().addToRequestQueue(jsonObjReq);
    }
    */

    private void news() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(newsLink,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("NEWS", response.toString());

                        try {
                            // Getting JSON Array from URL

                            //News verfügbar
                            if (response.getBoolean("newsAvailable")) {

                                newsTextHead = response.getString("newsHead");
                                newsTextText = response.getString("newsText");
                                newsUrl = response.getString("newsUrl");

                                setNewsIconVisible();

                                if (response.getBoolean("newsWichtig") && !Data.newsWichtigGelsesen) {

                                    Data.newsWichtigGelsesen = true;

                                    new AlertDialog.Builder(MainActivity.this)
                                            .setTitle(R.string.news_wichtig_head)
                                            .setMessage(R.string.news_wichtig_text)
                                            .setCancelable(false)
                                            .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //OK
                                                    drawer.openDrawer(GravityCompat.START);

                                                }
                                            })
                                            .show();

                                }

                            } else {
                                setNewsIconInvisible();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            setNewsIconInvisible();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("NEWS " + error.getMessage(), "Error: " + error.getMessage());
                setNewsIconInvisible();
            }
        });


        // Adding request to request queue
        MyController.getInstance().addToRequestQueue(jsonObjReq);

    }

    /*
    private void update() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(updateURL,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("NEWS", response.toString());

                        try {
                            // Getting JSON Array from URL

                            //News verfügbar
                            if (response.getBoolean("newsAvailable")) {


                                newsTextHead = response.getString("newsHead");
                                newsTextText = response.getString("newsText");
                                newsUrl = response.getString("newsUrl");

                                setNewsIconVisible();

                            } else {
                                setNewsIconInvisible();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            setNewsIconInvisible();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("NEWS " + error.getMessage(), "Error: " + error.getMessage());
                setNewsIconInvisible();
            }
        });


        // Adding request to request queue
        MyController.getInstance().addToRequestQueue(jsonObjReq);

    }
    */

    private void setNewsIconInvisible() {
        navigationView.getMenu().setGroupVisible(R.id.newsGroup, false);
    }

    private void setNewsIconVisible() {
        navigationView.getMenu().setGroupVisible(R.id.newsGroup, true);
    }

    private void firstLaunch() {

        //FirstLaunch
        SharedPreferences settings = getSharedPreferences(FIRST_TIME_LAUNCH, 0);

        if (settings.getBoolean(FIRST_TIME_LAUNCH_Main, true)) {
            //the app is being launched for first time, do something
            Log.i("Info", "First time launch ausgefuehrt");

            // First Time Aufgabe

            new AlertDialog.Builder(this)
                    .setTitle(R.string.first_head)
                    .setCancelable(false)
                    .setIcon(R.drawable.ic_launcher)
                    .setMessage(R.string.first_text)
                    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, R.string.first_show_later_again, Toast.LENGTH_LONG).show();
                        }
                    })
                    .show();

            // setzte SharedPrefernce auf false
            settings.edit().putBoolean(FIRST_TIME_LAUNCH_Main, false).apply();

        }
        //FirstLaunchEnde
    }

    public void onFrameLayout3Clicked(View view) {
        if (findViewById(R.id.frameLayout3).getVisibility() == View.VISIBLE)
            findViewById(R.id.frameLayout3).setVisibility(View.INVISIBLE);
    }

}
