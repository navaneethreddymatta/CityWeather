package com.example.navanee.cityweather;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by navanee on 20-10-2016.
 */

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder>{
    private Context mContext;
    private List<Weather> mDataSet;
    DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
    DecimalFormat df = new DecimalFormat("#.##");

    public DetailAdapter(Context context, List<Weather> list){
        mContext = context;
        mDataSet = list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.detail_layout,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Weather item = mDataSet.get(position);
        holder.timeView.setText(dateFormat.format(item.getTimeStamp()));
        holder.tempView.setText("" + df.format(item.getTemperature()) + (char) 0x00B0 + "C");
        holder.condView.setText(item.getCondition());
        holder.pressureView.setText(String.valueOf(df.format(item.getPressure())) + "hPa");
        holder.humidityView.setText(String.valueOf(item.getHumidity()) + "%");
        holder.windView.setText(String.valueOf(df.format(item.getWindSpeed())) + "mps" + ", " + item.getWindAngle() + (char) 0x00B0 + item.getWindDir());
        Picasso.with(mContext).load("http://openweathermap.org/img/w/"+item.getIcon_url()+".png").into(holder.imgView);
        //holder.imgView.setText("date");
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView timeView, tempView, condView, pressureView, humidityView, windView;
        ImageView imgView;
        RelativeLayout lLayout;
        public ViewHolder(View v){
            super(v);
            timeView = (TextView) v.findViewById(R.id.det_time);
            tempView = (TextView) v.findViewById(R.id.det_temp);
            condView = (TextView) v.findViewById(R.id.det_condition);
            pressureView = (TextView) v.findViewById(R.id.det_pressure);
            humidityView = (TextView) v.findViewById(R.id.det_humidity);
            windView = (TextView) v.findViewById(R.id.det_wind);
            imgView = (ImageView) v.findViewById(R.id.det_img);
            lLayout = (RelativeLayout) v.findViewById(R.id.landingLayout);
        }
    }
}
