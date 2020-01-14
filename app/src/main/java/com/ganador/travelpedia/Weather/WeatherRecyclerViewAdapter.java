package com.ganador.travelpedia.Weather;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ganador.travelpedia.R;

import java.util.List;

public class WeatherRecyclerViewAdapter extends RecyclerView.Adapter<WeatherRecyclerViewAdapter.MyViewHolder> {

    private Context mContext ;
    private List<Weather> mData ;
    private String mCity = null;
    public Typeface weatherFont;
    public WeatherRecyclerViewAdapter(Context mContext, List<Weather> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.list_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.itemDate.setText(mData.get(position).mdate.toString());
        holder.itemTemperature.setText(mData.get(position).mtemperature + "°");
        holder.itemDescription.setText(mData.get(position).mdescription);
        holder.itemIcon.setText(Html.fromHtml(mData.get(position).micon));
        weatherFont = Typeface.createFromAsset(mContext.getAssets(), "fonts/weathericons-regular-webfont.ttf");
        holder.itemIcon.setTypeface(weatherFont);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView itemDate;
        public TextView itemTemperature;
        public TextView itemDescription;
        public TextView itemIcon;
        public View lineView;
        public MyViewHolder(View view) {
            super(view);

            this.itemDate = view.findViewById(R.id.itemDate);
            this.itemTemperature = view.findViewById(R.id.itemTemperature);
            this.itemDescription = view.findViewById(R.id.itemDescription);
            this.itemIcon = view.findViewById(R.id.itemIcon);

            this.lineView = view.findViewById(R.id.lineView);


        }
    }

}
