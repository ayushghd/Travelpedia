package com.ganador.travelpedia.Weather;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.ganador.travelpedia.PlaceSearch.PlaceDetails;
import com.ganador.travelpedia.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class WeatherActivity extends AppCompatActivity {

    private List<Weather> longTermWeather = new ArrayList<>();
    TextView Temperature;
    TextView Description;
    TextView Icon;
    Toolbar toolbar;
    Typeface weatherFont;
    private ProgressDialog dialog;
    private PlaceDetails placeDetails;
    String city = "Allahabad,IN";
    String OPEN_WEATHER_MAP_API = "051a00ac576ef1c0b5d47ccdead73616";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar_Dark);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scrolling);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        toolbar.setPopupTheme(R.style.AppTheme_PopupOverlay_Dark);
        placeDetails = new PlaceDetails();
        Intent intent = getIntent();
        city = intent.getExtras().getString("City");
        Temperature =  findViewById(R.id.temperature);
        Description =  findViewById(R.id.description);
        Icon =  findViewById(R.id._icon);
        weatherFont = Typeface.createFromAsset(this.getAssets(), "fonts/weathericons-regular-webfont.ttf");
        Icon.setTypeface(weatherFont);

        if (isNetworkAvailable(getApplicationContext())) {
            dialog.show();
            DownloadWeather task = new DownloadWeather();
            task.execute(city);
            DownloadLongTermWeather task2 = new DownloadLongTermWeather();
            task2.execute(city);
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
        }

    }
   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_weather, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            if (isNetworkAvailable(getApplicationContext())) {
               Intent intent = getIntent();
               finish();
               startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(),"Network Is Not Available", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    class DownloadLongTermWeather extends AsyncTask< String, Void, String > {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }
        protected String doInBackground(String...args) {
            try {
                String xml = placeDetails.downloadUrl("http://api.openweathermap.org/data/2.5/forecast?q=" + args[0] +
                        "&units=metric&appid=" + OPEN_WEATHER_MAP_API);
                return xml;
            }
            catch (Exception e){
                return null;
            }
        }
        @Override
        protected void onPostExecute(String xml) {
            int i;
            try {

                JSONObject json = new JSONObject(xml);
                if (json != null) {
                    Log.e("aboveloop","aboveloop");
                    longTermWeather = new ArrayList<>();
                    JSONArray list = json.getJSONArray("list");
                    for (i = 0; i < list.length(); i++) {
                        Weather weather = new Weather();

                        JSONObject listItem = list.getJSONObject(i);
                        JSONObject main = listItem.getJSONObject("main");
                        Log.e("belowloop","belowloop");
                        weather.setDate(listItem.getString("dt"));
                        Log.e("middddd",weather.mdate.toString());
                        weather.mtemperature = main.getString("temp");
                        weather.mdescription = listItem.optJSONArray("weather").getJSONObject(0).getString("description");
                        final String idString = listItem.optJSONArray("weather").getJSONObject(0).getString("id");
                        weather.mid = idString;
                        Log.e("belowmid","belowmid");
                        // longTermWeather.add(weather);
                        final String dateMsString = listItem.getString("dt") + "000";
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(Long.parseLong(dateMsString));
                        weather.micon = setWeatherIcon(Integer.parseInt(idString), cal.get(Calendar.HOUR_OF_DAY));

                        longTermWeather.add(weather);
                    }
                    Log.e("size",longTermWeather.size()+"");
                    RecyclerView myrv = (RecyclerView) findViewById(R.id.recyclerview_id1);
                    WeatherRecyclerViewAdapter myAdapter = new WeatherRecyclerViewAdapter(getApplicationContext(),longTermWeather);
                    myrv.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
                    myrv.setAdapter(myAdapter);
                    Log.e("done","done");
                }} catch (Exception e) {
                Log.e("erorr",e.toString());
                Toast.makeText(getApplicationContext(), "Error in city name", Toast.LENGTH_SHORT).show();
            }

        }
    }
    class DownloadWeather extends AsyncTask< String, Void, String > {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        protected String doInBackground(String...args) {
            try{
               String xml = placeDetails.downloadUrl("http://api.openweathermap.org/data/2.5/weather?q=" + args[0] +
                    "&units=metric&appid=" + OPEN_WEATHER_MAP_API);
               return xml;
            }
            catch (Exception e){
               return null;
            }
        }
        @Override
        protected void onPostExecute(String xml) {
            try {
                JSONObject json = new JSONObject(xml);
                if (json != null) {
                    JSONObject main = json.getJSONObject("main");
                    toolbar.setTitle(json.getString("name").toUpperCase(Locale.US) + ", " + json.getJSONObject("sys").getString("country"));
                    Temperature.setText(main.getString("temp") + "°");
                    Description.setText(json.optJSONArray("weather").getJSONObject(0).getString("description"));
                    final String idString = json.optJSONArray("weather").getJSONObject(0).getString("id");
                    final String dateMsString = json.getString("dt") + "000";
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(Long.parseLong(dateMsString));
                    Icon.setText(Html.fromHtml(setWeatherIcon(Integer.parseInt(idString), cal.get(Calendar.HOUR_OF_DAY))));
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Error, Check City", Toast.LENGTH_SHORT).show();
            }
            if (dialog.isShowing())
                dialog.dismiss();
        }

    }


    public static boolean isNetworkAvailable(Context context)
    {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    public static String setWeatherIcon(int Id, int hourOfDay) {
        String icon = "";
        if (Id == 800) {
            if (hourOfDay >= 7 && hourOfDay < 20) {
                icon = "&#xf00d;";
            } else {
                icon = "&#xf02e;";
            }
        } else {
            Id/=100;
            switch (Id) {
                case 2 : icon = "&#xf01e;";
                    break;
                case 3 : icon = "&#xf01c;";
                    break;
                case 5 : icon = "&#xf019;";
                    break;
                case 6 : icon = "&#xf01b;";
                    break;
                case 7 : icon = "&#xf014;";
                    break;
                case 8 : icon = "&#xf013;";
                    break;

            }
        }
        return icon;
    }
}