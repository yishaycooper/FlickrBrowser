package com.example.android.flickrbrowser;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


public class BaseActivity extends AppCompatActivity {

    static final String FLICKR_QUERY = "FLICKR QUERY";
    static final String PHOTO_TRANSFER = "PHOTO TRANSFER";

    void activateToolbar(boolean enableHome) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {// if there is a toolbar we inflate the toolbar from the toolbar XML file
            Toolbar toolbar = findViewById(R.id.toolbar);
            if (toolbar != null) {
                setSupportActionBar(toolbar);
                actionBar = getSupportActionBar();// get a referenc to the neew actionbar
            }
        }

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(enableHome);// then enable/disable
        }
    }
}
