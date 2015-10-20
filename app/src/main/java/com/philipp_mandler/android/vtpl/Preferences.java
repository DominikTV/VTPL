package com.philipp_mandler.android.vtpl;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;

import it.gmariotti.changelibs.library.view.ChangeLogRecyclerView;



public class Preferences extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);


        final Preference welcomemessage = findPreference("welcomeMessage");
        welcomemessage.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {


                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.first_head)
                        .setMessage(R.string.first_text)
                        .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .setIcon(R.drawable.ic_launcher)
                        .show();


                return false;
            }
        });

        /*
        final Preference betatester = findPreference("betatester");
        betatester.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {


                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.betatester_head)
                        .setMessage(R.string.betatester_dialog_text)
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .setPositiveButton(R.string.betatester_dialog_googleplus, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/u/0/communities/110347292965575439919"));
                                startActivity(browserIntent);
                            }
                        })
                        .show();

                return false;
            }
        });
        */


        final Preference changelog = findPreference("version");
        changelog.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                LayoutInflater inflater = new LayoutInflater(getActivity()) {
                    @Override
                    public LayoutInflater cloneInContext(Context newContext) {
                        return null;
                    }
                };

                ChangeLogRecyclerView chgList = (ChangeLogRecyclerView) inflater.inflate(R.layout.changelog_dialog_changelog, null);

                new AlertDialog.Builder(getActivity())   //R.style.AppCompatAlertDialogStyle
                        .setTitle(R.string.changelog)
                        .setView(chgList)
                        .setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.dismiss();
                                    }
                                }
                        )
                        .show();

                return false;
            }
        });
    }

}