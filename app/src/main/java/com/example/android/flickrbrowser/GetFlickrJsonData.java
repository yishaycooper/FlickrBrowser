package com.example.android.flickrbrowser;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.example.android.flickrbrowser.DownloadStatus.IDLE;

//can also  use final ints or interface interface instead of enum
enum DownloadStatus {IDLE, PROCESSING, NOT_INITIALISED, FAILED_RO_EMPTY, OK}

// no need to make your classes public unless your going to split the app into defferent packages
class GetFlickrJsonData extends AsyncTask<String, Void, List<Photo>> {

    private List<Photo> mPhotoList = null;
    // variables to store URL parameters
    private String mBaseURL;
    private String mLanguage;
    private boolean mMatchAll;

    private DownloadStatus mDownloadStatus = IDLE;

    private final OnDataAvailable mCallback;

    interface OnDataAvailable  {
        void onDataAvailable(List<Photo> data, DownloadStatus status);
    }

    public GetFlickrJsonData(OnDataAvailable callback, String baseURL, String language, boolean matchAll) {
        mCallback = callback;
        mBaseURL = baseURL;
        mLanguage = language;
        mMatchAll = matchAll;
    }

    @Override
    protected void onPostExecute(List<Photo> photos) {
        if(mCallback != null){
            mCallback.onDataAvailable(mPhotoList, DownloadStatus.OK);
        }
    }

    @Override
    protected List<Photo> doInBackground(String... params) {
        String destinationUri = createUri(params[0], mLanguage, mMatchAll);
        String rawData = getRawData(destinationUri);
        onDownloadComplete(rawData, mDownloadStatus);
        Log.d(TAG, "doInBackground: " + rawData);
        return mPhotoList;
    }

// this method builds up URL parameters i.e. afer ? https://api.flickr.com/services/feeds/photos_public.gne?tags=android&format=json&nojsoncallback=1
    private String createUri(String searchCritetia, String lang, boolean matchAll){
//        Uri uri = Uri.parse(mBaseURL);
//        Uri.Builder builder = uri.buildUpon();
//        builder.appendQueryParameter("tags", searchCritetia);
//        builder.appendQueryParameter("tagmode", matchAll ? "ALL" : "ANY");
//        builder.appendQueryParameter("lang", lang);
//        builder.appendQueryParameter("format", "json");
//        builder.appendQueryParameter("nojsoncallback", "1");
//        uri = builder.build();

// can also chain methods using builder design pattern
        return Uri.parse(mBaseURL).buildUpon()
                .appendQueryParameter("tags", searchCritetia)
                .appendQueryParameter("tagmode", matchAll ? "ALL" : "ANY")
                .appendQueryParameter("lang", lang)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback", "1")// needed for regular json format
                .build().toString();
    }

    public void onDownloadComplete(String data, DownloadStatus status){

        if(status == DownloadStatus.OK){
            mPhotoList = new ArrayList<>();

            try{
                JSONObject jsonData = new JSONObject(data);
                JSONArray itemsArray = jsonData.getJSONArray("items");// items in is the array of objects

                for(int i=0; i<itemsArray.length(); i++){
                    JSONObject jsonPhoto = itemsArray.getJSONObject(i);
                    // these have to be the same as defind in in the json feed
                    String title = jsonPhoto.getString("title");
                    String author = jsonPhoto.getString("author");
                    String authorId = jsonPhoto.getString("author_id");
                    String tags = jsonPhoto.getString("tags");
// see the link in the browser https://api.flickr.com/services/feeds/photos_public.gne?tags=android&format=json&nojsoncallback=1
                    JSONObject jsonMedia = jsonPhoto.getJSONObject("media");
                    String photoUrl = jsonMedia.getString("m");

                    String link = photoUrl.replaceFirst("_m.", "_b.");//see https://www.flickr.com/services/api/misc.urls.html

                    Photo photoObject = new Photo(title, author, authorId, link, tags, photoUrl);
                    mPhotoList.add(photoObject);
                }
            } catch(JSONException jsone){
                jsone.printStackTrace();
                Log.e(TAG, "onDownloadComplete: Error processing json data " + jsone.getMessage());
                mDownloadStatus = DownloadStatus.FAILED_RO_EMPTY;
            }
        }
    }

// this method is decoupled from the jsom parsing an can download a variety of formats
    private String getRawData(String stringsUrl) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        if(stringsUrl == null){
            mDownloadStatus = DownloadStatus.NOT_INITIALISED;
            return null;
        }
        try{
            mDownloadStatus = DownloadStatus.PROCESSING;
            URL url = new URL(stringsUrl);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int response = connection.getResponseCode();
            Log.d(TAG, "getRawData: The response code was " + response);

            StringBuilder result = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            for(String line = reader.readLine(); line != null; line = reader.readLine()){
                // when using read line the new line characters are stripped off so we nedd to append back
                result.append(line).append("\n");
            }

            mDownloadStatus = DownloadStatus.OK;
            return result.toString();
        }catch(MalformedURLException e){
            // log.e unlike log.e will be written to a log file
            Log.e(TAG, "getRawData: Invalid URL " + e.getMessage() );
        }catch (IOException e){
            Log.e(TAG, "getRawData: IO Exception reading data " + e.getMessage());
        }catch(SecurityException e){
            Log.e(TAG, "getRawData: Security Exception. Needs permission? " + e.getMessage() );
        }finally{ // the finally is acrually executed just before the method returns
            if(connection != null){
                connection.disconnect();
            }
            if(reader != null){
                try{
                    reader.close();
                }catch (IOException e){
                    Log.e(TAG, "getRawData: Error closing stream " + e.getMessage() );
                }
            }
        }
        mDownloadStatus = DownloadStatus.FAILED_RO_EMPTY;
        return null;
    }
}
