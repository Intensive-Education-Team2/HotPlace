package com.posturn.hotplace;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

public class MarketFragmentAdapter extends RecyclerView.Adapter<MarketFragmentAdapter.Holder> {
    private ArrayList<MarketObject> list = new ArrayList<>();
    private Context context;
    ImageView coverImageView;

    public double latitude;
    public double longitude;

    public MarketFragmentAdapter(Context context, ArrayList<MarketObject> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MarketFragmentAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.market_fragment_post_item, parent, false);
        GpsTracker gpsTracker = new GpsTracker(context);

        latitude = 37.497954;
        //latitude = gpsTracker.getLatitude();
        longitude = 127.027564;
        //longitude = gpsTracker.getLongitude();

        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }


    public class Holder extends RecyclerView.ViewHolder {
        protected TextView market_name;
        protected TextView comment;
        protected TextView distance;
        protected ImageView imgCover;
        //protected Button dbtn;

        public Holder(View view) {
            super(view);
            this.market_name = (TextView) view.findViewById(R.id.textview_cover_name);
            this.comment = (TextView) view.findViewById(R.id.market_comment);
            this.distance = (TextView) view.findViewById(R.id.market_distance);
            this.imgCover = (ImageView) view.findViewById(R.id.imageview_cover);
            //this.dbtn = (Button) view.findViewById(R.id.market_detail_button);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MarketFragmentAdapter.Holder holder, final int position) {
        holder.market_name.setText(list.get(position).market_name);
        holder.comment.setText(list.get(position).comment);

        holder.distance.setText(" "+getDistance(latitude,longitude,list.get(position).getLat(),list.get(position).getLat()));
        Picasso.get().load(list.get(position).imgcover).into(holder.imgCover);

        holder.imgCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(list.get(position).detail_uri));
                context.startActivity(browserIntent);
            }
        });
        holder.market_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(list.get(position).detail_uri));
                context.startActivity(browserIntent);
            }
        });

    }

    private double getDistance(double myLat, double myLon, double targetLat, double targetLon) {
        double distance;

        Location locationA = new Location("point A");
        locationA.setLatitude(myLat);
        locationA.setLongitude(myLon);

        Location locationB = new Location("point B");
        locationB.setLatitude(targetLat);
        locationB.setLongitude(targetLon);

        distance = locationA.distanceTo(locationB);

        return Math.round(distance / 1000 * 100) / 100.0;
    }


}
