package com.philipp_mandler.android.vtpl;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class Notenrechner extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText numInput;
    private TextView erg;
    private TextView ergText;

    private Double prozent;

    //private Button b_bg;
    //private Button b_bf_fs;
    //private Button b_fos;

    private TextInputLayout prozentWertLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notenrechner);

        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        numInput = (EditText) findViewById(R.id.numInput);
        erg = (TextView) findViewById(R.id.ergebnis);
        ergText = (TextView) findViewById(R.id.ergebnisText);

        //b_bg = (Button) findViewById(R.id.bg);
        //b_bf_fs = (Button) findViewById(R.id.bf_fs);
        //b_fos = (Button) findViewById(R.id.fos);

        prozentWertLayout = (TextInputLayout) findViewById(R.id.numInput_layout);


    }

    public void buttonBG(View view) {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        // BF

        if (isValid()) {
            //---

            if (prozent >= 96) { ergText.setText(R.string.noten_ergebnis_punkte); erg.setText( 15 + " Punkte"); }
            if (prozent >= 91 && prozent <= 95.9) { ergText.setText(R.string.noten_ergebnis_punkte); erg.setText( 14 + " Punkte"); }
            if (prozent >= 86 && prozent <= 90.9) { ergText.setText(R.string.noten_ergebnis_punkte); erg.setText( 13 + " Punkte"); }
            if (prozent >= 81 && prozent <= 85.9) { ergText.setText(R.string.noten_ergebnis_punkte); erg.setText( 12 + " Punkte"); }
            if (prozent >= 76 && prozent <= 80.9) { ergText.setText(R.string.noten_ergebnis_punkte); erg.setText( 11 + " Punkte"); }
            if (prozent >= 71 && prozent <= 75.9) { ergText.setText(R.string.noten_ergebnis_punkte); erg.setText( 10 + " Punkte"); }
            if (prozent >= 66 && prozent <= 70.9) { ergText.setText(R.string.noten_ergebnis_punkte); erg.setText( 9 + " Punkte"); }
            if (prozent >= 61 && prozent <= 65.9) { ergText.setText(R.string.noten_ergebnis_punkte); erg.setText( 8 + " Punkte"); }
            if (prozent >= 56 && prozent <= 60.9) { ergText.setText(R.string.noten_ergebnis_punkte); erg.setText( 7 + " Punkte"); }
            if (prozent >= 51 && prozent <= 55.9) { ergText.setText(R.string.noten_ergebnis_punkte); erg.setText( 6 + " Punkte"); }
            if (prozent >= 46 && prozent <= 50.9) { ergText.setText(R.string.noten_ergebnis_punkte); erg.setText( 5 + " Punkte"); }
            if (prozent >= 41 && prozent <= 45.9) { ergText.setText(R.string.noten_ergebnis_punkte); erg.setText( 4 + " Punkte"); }
            if (prozent >= 34 && prozent <= 40.9) { ergText.setText(R.string.noten_ergebnis_punkte); erg.setText( 3 + " Punkte"); }
            if (prozent >= 27 && prozent <= 33.9) { ergText.setText(R.string.noten_ergebnis_punkte); erg.setText( 2 + " Punkte"); }
            if (prozent >= 20 && prozent <= 26.9) { ergText.setText(R.string.noten_ergebnis_punkte); erg.setText( 1 + " Punkt"); }
            if (prozent >= 0  && prozent <= 19.9) { ergText.setText(R.string.noten_ergebnis_punkte); erg.setText( 0 + " Punkte"); }

            //---
        }

    }

    public void buttpnBSFS(View view) {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        // BSFS

        if (isValid()) {
            //---

            if (prozent >= 93) { ergText.setText(R.string.noten_ergebnis_noten); erg.setText( 1 + ""); }
            if (prozent >= 91 && prozent <= 92.9) { ergText.setText(R.string.noten_ergebnis_noten); erg.setText( 1 + "-"); }
            if (prozent >= 88 && prozent <= 90.9) { ergText.setText(R.string.noten_ergebnis_noten); erg.setText( 2 + "+"); }
            if (prozent >= 83 && prozent <= 87.9) { ergText.setText(R.string.noten_ergebnis_noten); erg.setText( 2 + ""); }
            if (prozent >= 78 && prozent <= 82.9) { ergText.setText(R.string.noten_ergebnis_noten); erg.setText( 2 + "-"); }
            if (prozent >= 73 && prozent <= 77.9) { ergText.setText(R.string.noten_ergebnis_noten); erg.setText( 3 + "+"); }
            if (prozent >= 68 && prozent <= 72.9) { ergText.setText(R.string.noten_ergebnis_noten); erg.setText( 3 + ""); }
            if (prozent >= 63 && prozent <= 67.9) { ergText.setText(R.string.noten_ergebnis_noten); erg.setText( 3 + "-"); }
            if (prozent >= 58 && prozent <= 62.9) { ergText.setText(R.string.noten_ergebnis_noten); erg.setText( 4 + "+"); }
            if (prozent >= 53 && prozent <= 57.9) { ergText.setText(R.string.noten_ergebnis_noten); erg.setText( 4 + ""); }
            if (prozent >= 48 && prozent <= 52.9) { ergText.setText(R.string.noten_ergebnis_noten); erg.setText( 4 + "-"); }
            if (prozent >= 41 && prozent <= 47.9) { ergText.setText(R.string.noten_ergebnis_noten); erg.setText( 5 + "+"); }
            if (prozent >= 34 && prozent <= 40.9) { ergText.setText(R.string.noten_ergebnis_noten); erg.setText( 5 + ""); }
            if (prozent >= 27 && prozent <= 33.9) { ergText.setText(R.string.noten_ergebnis_noten); erg.setText( 5 + "-"); }
            if (prozent >= 20 && prozent <= 26.9) { ergText.setText(R.string.noten_ergebnis_noten); erg.setText( 6 + "+"); }
            if (prozent >= 0  && prozent <= 19.9) { ergText.setText(R.string.noten_ergebnis_noten); erg.setText( 6 + "");}

            //---
        }

    }

    public void buttonFOS(View view) {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        // FOS

        if (isValid()) {
            //---

            if (prozent >= 91) { ergText.setText(R.string.noten_ergebnis_noten); erg.setText( 1 + ""); }
            if (prozent >= 86 && prozent <= 90.9) { ergText.setText(R.string.noten_ergebnis_noten); erg.setText( 1 + "-"); }
            if (prozent >= 81 && prozent <= 85.9) { ergText.setText(R.string.noten_ergebnis_noten); erg.setText( 2 + "+"); }
            if (prozent >= 76 && prozent <= 80.9) { ergText.setText(R.string.noten_ergebnis_noten); erg.setText( 2 + ""); }
            if (prozent >= 71 && prozent <= 75.9) { ergText.setText(R.string.noten_ergebnis_noten); erg.setText( 2 + "-"); }
            if (prozent >= 67 && prozent <= 70.9) { ergText.setText(R.string.noten_ergebnis_noten); erg.setText( 3 + "+"); }
            if (prozent >= 62 && prozent <= 66.9) { ergText.setText(R.string.noten_ergebnis_noten); erg.setText( 3 + ""); }
            if (prozent >= 58 && prozent <= 61.9) { ergText.setText(R.string.noten_ergebnis_noten); erg.setText( 3 + "-"); }
            if (prozent >= 54 && prozent <= 57.9) { ergText.setText(R.string.noten_ergebnis_noten); erg.setText( 4 + "+"); }
            if (prozent >= 50 && prozent <= 53.9) { ergText.setText(R.string.noten_ergebnis_noten); erg.setText( 4 + ""); }
            if (prozent >= 46 && prozent <= 49.9) { ergText.setText(R.string.noten_ergebnis_noten); erg.setText( 4 + "-"); }
            if (prozent >= 38 && prozent <= 45.9) { ergText.setText(R.string.noten_ergebnis_noten); erg.setText( 5 + "+"); }
            if (prozent >= 29 && prozent <= 37.9) { ergText.setText(R.string.noten_ergebnis_noten); erg.setText( 5 + ""); }
            if (prozent >= 20 && prozent <= 28.9) { ergText.setText(R.string.noten_ergebnis_noten); erg.setText( 5 + "-"); }
            if (prozent >= 14 && prozent <= 19.9) { ergText.setText(R.string.noten_ergebnis_noten); erg.setText( 6 + "+"); }
            if (prozent >= 0  && prozent <= 13.9) { ergText.setText(R.string.noten_ergebnis_noten); erg.setText( 6 + "");}

            //---
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notenrechner, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_info) {

            new AlertDialog.Builder(this)
                    .setTitle(R.string.noten_info)
                    .setMessage(R.string.noten_info_text)
                    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //Nichts, nur Dialog schließen
                        }
                    })
                    .show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isValid() {
        if (numInput.getText().toString().trim().length() > 0) {

            prozentWertLayout.setErrorEnabled(false);

            prozent = Double.valueOf(numInput.getText().toString());

            if (prozent > 100) { prozent = 100.0; }
            else { prozent = round1(prozent); }

            numInput.setText( prozent.toString());

            ergText.setVisibility(View.VISIBLE);
            erg.setVisibility(View.VISIBLE);

            //Log.i("Prozent", prozent.toString());

            return true;

        } else {
            //Toast.makeText(Notenrechner.this, "Bitte deinen Prozentsatz eintragen!", Toast.LENGTH_SHORT).show();
            prozentWertLayout.setErrorEnabled(true);
            prozentWertLayout.setError("Bitte deinen Prozentsatz eintragen!");

            return false;
        }
    }

    private static double round1(double d) {
        return Math.floor(d * 1e1) / 1e1;
    }

}
