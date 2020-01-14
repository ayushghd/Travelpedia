package com.ganador.travelpedia.PlaceSearch;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.ganador.travelpedia.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PlaceDetails extends AppCompatActivity {

    private String city;
    private String type;
    private String title;
    private String Description;
    private ProgressDialog dialog;
    private List<PlaceParcelClass> allplace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.show();
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        Intent intent = getIntent();

        dialog = new ProgressDialog(this);
        title = intent.getExtras().getString("Title");
        type = intent.getExtras().getString("Category");
        city = intent.getExtras().getString("City");
        Description = intent.getExtras().getString("Description");
        if(city.equals("") || city.equals("Emergency"))
            toolbar.setTitle(title);
        else
            toolbar.setTitle(title + " in " + city);
        toolbar.setPopupTheme(R.style.AppTheme_PopupOverlay_Dark);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?");
        if(city.equals(""))
            sb.append("query=" + type);
        else {
            sb.append("query=" + type);
            sb.append("+in+" + city);
        }
        sb.append("&language=en");
        sb.append("&key=AIzaSyBetBer1jCeuflwEc_EnbxUnFiWFabxJZg");
        Log.e("type",type);
        Log.e("url123",sb.toString());
        String final_url = sb.toString();
        if(city.equals("Emergency"))
            final_url = type;
        PlacesTask placesTask = new PlacesTask();
        if(isNetworkAvailable(getApplicationContext())) {
            placesTask.execute(final_url);
        }
        else{
            Toast.makeText(PlaceDetails.this,"No Internet Connection",Toast.LENGTH_LONG).show();
        }

    }
    public static boolean isNetworkAvailable(Context context)
    {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection connection = null;
        try{
            URL url = new URL(strUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.connect();
            iStream = connection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d(" url", e.toString());
        }finally{
            iStream.close();
            connection.disconnect();
        }
        return data;
    }
    private class PlacesTask extends AsyncTask<String, Integer, String> {

        String data = null;
        @Override
        protected String doInBackground(String... url) {
            try{
                Log.e("llllllll","llllllll");
                Log.d("URL:",url[0]);
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }
        @Override
        protected void onPostExecute(String result){
            dialog.dismiss();
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);

        }

    }
    private PlaceParcelClass[] getPlaces(JSONArray jPlaces){
        int placesCount = jPlaces.length();
        PlaceParcelClass[] places = new PlaceParcelClass[placesCount];
        for(int i=0; i<placesCount;i++){
            try {
                places[i] = getPlace((JSONObject)jPlaces.get(i));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return places;
    }
    private class ParserTask extends AsyncTask<String, Integer, PlaceParcelClass[]> {

        JSONObject jObject;
        @Override
        protected PlaceParcelClass[] doInBackground(String... jsonData) {


            PlaceParcelClass[] places = null;
            try{
                jObject = new JSONObject(jsonData[0]);
                places = parse(jObject);

            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return places;
        }
        @Override
        protected void onPostExecute(PlaceParcelClass[] places){

            allplace = new ArrayList<>();
            for(int i=0;i< places.length  ;i++){
                allplace.add(places[i]);

            }
            RecyclerView myrv =  findViewById(R.id.recyclerview_id);
            PlaceRecyclerView myAdapter = new PlaceRecyclerView(getApplicationContext(),allplace);
            myrv.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
            myrv.setAdapter(myAdapter);
        }

    }
    public PlaceParcelClass[] parse(JSONObject jObject){

        JSONArray jPlaces = null;
        try {
            jPlaces = jObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getPlaces(jPlaces);
    }

    private PlaceParcelClass getPlace(JSONObject jPlace){

        PlaceParcelClass place = new PlaceParcelClass();

        try {
            if(!jPlace.isNull("name")){
                place.mPlaceName = jPlace.getString("name");
            }
            if(!jPlace.isNull("formatted_address")){
                place.mVicinity = jPlace.getString("formatted_address");
            }
            if(!jPlace.isNull("rating")){
                place.rating = jPlace.getDouble("rating");
            }

            if(!jPlace.isNull("photos")){
                JSONArray photos = jPlace.getJSONArray("photos");
                place.mPhotos = new PhotoParcelClass[photos.length()];
                for(int i=0;i<photos.length();i++){
                    place.mPhotos[i] = new PhotoParcelClass();
                    place.mPhotos[i].mWidth = ((JSONObject)photos.get(i)).getInt("width");
                    place.mPhotos[i].mHeight = ((JSONObject)photos.get(i)).getInt("height");
                    place.mPhotos[i].mPhotoReference = ((JSONObject)photos.get(i)).getString("photo_reference");
                    JSONArray attributions = ((JSONObject)photos.get(i)).getJSONArray("html_attributions");
                    place.mPhotos[i].mPhotoHelperClasses = new PhotoHelperClass[attributions.length()];
                    for(int j=0;j<attributions.length();j++){
                        place.mPhotos[i].mPhotoHelperClasses[j] = new PhotoHelperClass();
                        place.mPhotos[i].mPhotoHelperClasses[j].mHtmlAttribution = attributions.getString(j);
                    }
                }
            }

            place.mLat = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lat");
            place.mLng = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lng");



        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("EXCEPTION", e.toString());
        }
        return place;
    }
}
