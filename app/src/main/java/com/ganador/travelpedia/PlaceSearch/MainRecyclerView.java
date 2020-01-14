package com.ganador.travelpedia.PlaceSearch;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ganador.travelpedia.R;
import com.ganador.travelpedia.Weather.WeatherActivity;

import java.util.List;

public class MainRecyclerView extends RecyclerView.Adapter<MainRecyclerView.MyViewHolder> {

    private Context mContext ;
    private List<NearByPlace> mData ;
    private String mCity = null;

    public MainRecyclerView(Context mContext, List<NearByPlace> mData, String city) {
        this.mContext = mContext;
        this.mData = mData;
        this.mCity = city;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardveiw_place,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.placeTitle.setText(mData.get(position).getTitle());
        holder.placeThumbnail.setImageResource(mData.get(position).getThumbnail());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position ==0){
                    Intent intent = new Intent(mContext,WeatherActivity.class);
                    intent.putExtra("City",mCity);
                    mContext.startActivity(intent);
                }
                else {
                    Intent intent = new Intent(mContext, PlaceDetails.class);
                    intent.putExtra("Title", mData.get(position).getTitle());
                    intent.putExtra("City", mCity);
                    intent.putExtra("Category", mData.get(position).getCategory());
                    intent.putExtra("Description", mData.get(position).getDescription());
                    intent.putExtra("Thumbnail", mData.get(position).getThumbnail());
                    mContext.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView placeTitle;
        ImageView placeThumbnail;
        CardView cardView ;

        public MyViewHolder(View itemView) {
            super(itemView);

            placeTitle =  itemView.findViewById(R.id.placeTitle_id) ;
            placeThumbnail =  itemView.findViewById(R.id.placeImg_id);
            cardView =  itemView.findViewById(R.id.cardview_id);


        }
    }

}
