package com.example.android.flickrbrowser;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.SearchView;

public class SearchActivity extends BaseActivity {

    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // uses the method in BaseActivity to enable toolbar home button
        activateToolbar(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

// get searchmanager to associate the searchabe configuration in the manifest
// and provides access to the system search services
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
// get a reference to the searchview widget that's embedded in the search menu item in the toolbar
// findItem() gets the menu item and getActionView() gets the searchview widget
        mSearchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
// pass in component name in this case SearchActyvity, to get searchable info from searchable.xml
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
// searchable info is then set into searchable widget to configure it
        mSearchView.setSearchableInfo(searchableInfo);
// set iconified to false so you don't have to press a nother search icon in SearchActivity to start typing
        mSearchView.setIconified(false);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
// called when prsing enter on external keyboard ro submit on android device
            @Override
            public boolean onQueryTextSubmit(String query) {
                // here we use getApplicationContext() and not this because data will be retrieved from a separate activity that is MainActivity
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                // edit() puts the preference into a writable state then we can call putString()
                // because both MainActivity and SearchActivity are subclasses of BaseActivity they can both use FLICKR_QUERY defined in BaseActivity
                // the data is stored when we call apply()
                sharedPreferences.edit().putString(FLICKR_QUERY, query).apply();
                mSearchView.clearFocus();// this is needed when using an external keyboard so the enter dey won't send an event to MainActivity
                                         // as well as SearchActivity and return right back to SearchActivity
                finish();// close and return to the activity that launched it
                return true;// return true to indicate that we are dealing with the event and not android operating system
            }
// this call back is for when text changes reading a character at a time
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // this interface is for going back to MainActivity when closing the search field
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                finish();
                return false;
            }
        });
        return true;
    }
}
