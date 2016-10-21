package com.example.navanee.cityweather;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by navanee on 20-10-2016.
 */

public class FavCityAdapter extends RecyclerView.Adapter<FavCityAdapter.ViewHolder> {
    private Context mContext;
    private List<FavouriteCity> mDataSet;
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
    DecimalFormat df = new DecimalFormat("#.##");
    DatabaseDataManager dm;
    ConnectFavourites cf;

    public FavCityAdapter(Context context, List<FavouriteCity> list,ConnectFavourites cFav){
        mContext = context;
        mDataSet = list;
        cf = cFav;
        dm = new DatabaseDataManager(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.favcity_row_layout,parent,false);
        FavCityAdapter.ViewHolder vh = new FavCityAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int cellPosition) {
        FavouriteCity favItem = mDataSet.get(cellPosition);
        holder.cityNameView.setText(favItem.getCityName() + "," + favItem.getCountry());
        holder.tempValView.setText("" + df.format(Double.parseDouble(favItem.getTemperature())) + (char) 0x00B0 + "C");
        holder.dateValView.setText("Updated on: " + favItem.getDate());
        holder.thisCity = favItem;
        if(favItem.getFavorite() == 1) {
            holder.imgBtn.setImageResource(R.drawable.star_gold);
        } else {
            holder.imgBtn.setImageResource(R.drawable.star_gray);
        }
        holder.landing1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavouriteCity favItem1 = mDataSet.get(cellPosition);
                Intent intent = new Intent(mContext, CityDetail.class);
                intent.putExtra("city",favItem1.getCityName());
                intent.putExtra("country",favItem1.getCountry());
                mContext.startActivity(intent);
            }
        });
        holder.landing2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavouriteCity favItem1 = mDataSet.get(cellPosition);
                Intent intent = new Intent(mContext, CityDetail.class);
                intent.putExtra("city",favItem1.getCityName());
                intent.putExtra("country",favItem1.getCountry());
                mContext.startActivity(intent);
            }
        });
        holder.imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavouriteCity favItem1 = mDataSet.get(cellPosition);
                if(favItem1.getFavorite() == 0) {
                    favItem1.setFavorite(1);
                    ((ImageButton)v.findViewById(R.id.makeFavBtn)).setImageResource(R.drawable.star_gold);
                } else if(favItem1.getFavorite() == 1){
                    favItem1.setFavorite(0);
                    ((ImageButton)v.findViewById(R.id.makeFavBtn)).setImageResource(R.drawable.star_gray);
                }
                Log.d("demo","City getting updating");
                dm.updateCity(favItem1);
                cf.setSelectedRow();
            }
        });

        holder.landing1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                FavouriteCity favItem1 = mDataSet.get(cellPosition);
                dm.deleteCity(favItem1);
                Toast.makeText(mContext, "City Deleted", Toast.LENGTH_SHORT).show();
                cf.setSelectedRow();
                return true;
            }
        });

        holder.landing2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                FavouriteCity favItem1 = mDataSet.get(cellPosition);
                dm.deleteCity(favItem1);
                Toast.makeText(mContext, "City Deleted", Toast.LENGTH_SHORT).show();
                cf.setSelectedRow();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView cityNameView, tempValView, dateValView;
        LinearLayout landing1, landing2, mainLanding;
        ImageButton imgBtn;
        FavouriteCity thisCity;
        DatabaseDataManager dm;

        public ViewHolder(View v){
            super(v);
            dm = new DatabaseDataManager(v.getContext());
            cityNameView = (TextView) v.findViewById(R.id.favCityName);
            tempValView = (TextView) v.findViewById(R.id.favCityTemp);
            dateValView = (TextView) v.findViewById(R.id.favCityDate);
            imgBtn = (ImageButton) v.findViewById(R.id.makeFavBtn);
            landing1 = (LinearLayout) v.findViewById(R.id.landingCell1);
            landing2 = (LinearLayout) v.findViewById(R.id.landingCell2);
            mainLanding = (LinearLayout) v.findViewById(R.id.landingCell);
        }
    }

    interface ConnectFavourites {
        void setSelectedRow();
    }
}
