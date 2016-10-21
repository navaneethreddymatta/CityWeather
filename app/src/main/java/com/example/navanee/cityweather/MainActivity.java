package com.example.navanee.cityweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, FavCityAdapter.ConnectFavourites {

    TextView cityName, countryName;
    ListView favListView;
    Button submitButton;
    SharedPreferences.Editor editor;
    String favCitiesStr;
    Gson gson = new Gson();
    LinearLayout favListLayout, noFavListLayout;
    List<FavouriteCity> favList;
    SharedPreferences preferences;
    PreferenceChangeListener pListener = null;
    DatabaseDataManager dm;
    RecyclerView favCitiesView;
    RecyclerView.LayoutManager dLayoutManager;

    static String PREF_NAME = "favCities";
    static String PREF_KEY_NAME = "favCitiesList";
    static Type type = new TypeToken<List<FavouriteCity>>(){}.getType();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAllViews();
        dm = new DatabaseDataManager(this);
        if(isNetworkConnected()){
            submitButton.setOnClickListener(this);
        } else {
            Toast.makeText(this, R.string.noConnection, Toast.LENGTH_LONG).show();
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUI();
    }

    public void setAllViews() {
        cityName = (TextView) findViewById(R.id.cityName);
        countryName = (TextView) findViewById(R.id.countryName);
        submitButton = (Button) findViewById(R.id.submitBtn);
        favListLayout = (LinearLayout) findViewById(R.id.favListLayout);
        noFavListLayout = (LinearLayout) findViewById(R.id.noFavListLayout);
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni.isConnected();
    }

    @Override
    public void onClick(View v) {
        String city = cityName.getText().toString();
        String country = countryName.getText().toString();
        if(city.trim().equals("")) {
            Toast.makeText(this, R.string.toast_noCityName, Toast.LENGTH_LONG).show();
        }
        else if(country.trim().equals("")) {
            Toast.makeText(this, R.string.toast_noCountryName, Toast.LENGTH_LONG).show();
        }
        else {
            Intent intent = new Intent(this, CityDetail.class);
            intent.putExtra("city",city);
            intent.putExtra("country",country);
            startActivity(intent);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.settings) {
            Intent intent = new Intent(this, UserPreferenceActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu1,menu);
        return true;
    }

    @Override
    public void setSelectedRow() {
        loadUI();
    }

    private class PreferenceChangeListener implements SharedPreferences.OnSharedPreferenceChangeListener{
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Toast.makeText(MainActivity.this, "Preferences changed", Toast.LENGTH_LONG);
        }
    }

    public void loadUI() {
        favList =  dm.getAllCities();
        sortCities(favList);
        preferences = getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        editor = preferences.edit();
        favCitiesStr = preferences.getString(PREF_KEY_NAME,null);
        if(favCitiesStr == null) {
            favCitiesStr = "[]";
            editor.putString(PREF_KEY_NAME,favCitiesStr);
            editor.commit();
        }
        if(favList.size() == 0) {
            findViewById(R.id.favListLayout).setVisibility(View.GONE);
            findViewById(R.id.noFavListLayout).setVisibility(View.VISIBLE);
        } else {
            favCitiesView = (RecyclerView) findViewById(R.id.fCitiesView);
            dLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            favCitiesView.setLayoutManager(dLayoutManager);
            findViewById(R.id.noFavListLayout).setVisibility(View.GONE);
            findViewById(R.id.favListLayout).setVisibility(View.VISIBLE);
            FavCityAdapter adapter = new FavCityAdapter(MainActivity.this,favList,MainActivity.this);
            favCitiesView.setAdapter(adapter);
            //adapter.notifyDataSetChanged();
        }
    }

    public void sortCities (List<FavouriteCity> list){
        int index = 0;
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getFavorite() == 1){
                FavouriteCity temp = list.get(i);
                list.remove(i);
                list.add(index,temp);
                index++;
            }
        }
    }
}
