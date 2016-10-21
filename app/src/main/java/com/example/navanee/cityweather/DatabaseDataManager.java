package com.example.navanee.cityweather;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

/**
 * Created by navanee on 20-10-2016.
 */

public class DatabaseDataManager {
    private Context mContext;
    private DatabaseOpenHelper dbOpenHelper;
    private SQLiteDatabase db;
    private CitiesDAO cityDAO;

    public DatabaseDataManager(Context mContext){
        this.mContext = mContext;
        dbOpenHelper = new DatabaseOpenHelper(this.mContext);
        db = dbOpenHelper.getWritableDatabase();
        cityDAO = new CitiesDAO(db);
    }

    public CitiesDAO getCitiesDAO(){
        return this.cityDAO;
    }

    public long saveCity(FavouriteCity city){
        return this.cityDAO.save(city);
    }

    public boolean updateCity(FavouriteCity city){
        Log.d("demo","City getting updating in DDM");
        return this.cityDAO.update(city);
    }

    public boolean deleteCity(FavouriteCity city){
        return this.cityDAO.delete(city);
    }

    public FavouriteCity getCity(long id){
        return this.cityDAO.get(id);
    }

    public List<FavouriteCity> getAllCities(){
        return this.cityDAO.getAll();
    }

    public void close(){
        if(db != null){
            db.close();
        }
    }
}
