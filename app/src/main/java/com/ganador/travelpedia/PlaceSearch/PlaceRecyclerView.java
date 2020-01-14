package com.ganador.travelpedia.PlaceSearch;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ganador.travelpedia.R;

import java.util.List;

public class PlaceRecyclerView extends RecyclerView.Adapter<PlaceRecyclerView.MyViewHolder> {

    private Context mContext ;
    private PlaceDetails placeDetails;
    private Double mLat,mLng;
    private List<PlaceParcelClass> mData ;
    private String mCity = null;
    private String mUrl=null;
    public PlaceRecyclerView(Context mContext, List<PlaceParcelClass> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.place_list,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.placeName.setText(mData.get(position).mPlaceName.toString());
        holder.placeDescription.setText(mData.get(position).mVicinity.toString());
        holder.placeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUrl =null;
                PhotoParcelClass[] photos = mData.get(position).mPhotos;
                Log.e("available",photos.length+"");
                for(int i=0;i<photos.length;i++){
                    String photoReference = "photoreference="+photos[i].mPhotoReference;
                    mUrl = "&" + photoReference;
                    Log.e("BelowLoop",mUrl);
                }
                mLat = Double.parseDouble(mData.get(position).mLat);
                mLng = Double.parseDouble(mData.get(position).mLng);
                Intent intent = new Intent(mContext,PlaceFragment.class);
                intent.putExtra("placeName",mData.get(position).mPlaceName);
                intent.putExtra("placeDescription",mData.get(position).mVicinity);
                intent.putExtra("rating",mData.get(position).rating);
                intent.putExtra("url",mUrl);
                intent.putExtra("latitude",mLat);
                intent.putExtra("longitude",mLng);
                mContext.startActivity(intent);
            }
        });
        holder.ratingBar.setRating((float)mData.get(position).rating);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView placeName;
        public TextView placeDescription;
        public View lineView;
        public CardView placeList;
        public RatingBar ratingBar;
        public MyViewHolder(View view) {
            super(view);

            this.placeName =  view.findViewById(R.id.placeName);
            this.placeDescription = view.findViewById(R.id.placeDescription);
            this.lineView = view.findViewById(R.id.lineView1);
            this.placeList = view.findViewById(R.id.placelist);
            this.ratingBar = view.findViewById(R.id.rating_bar);
        }
    }

}
