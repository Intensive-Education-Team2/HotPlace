package com.posturn.hotplace;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MarketFragmentAdapter extends RecyclerView.Adapter<MarketFragmentAdapter.Holder> {
    private ArrayList<MarketObject> list = new ArrayList<>();
    private Context context;
    ImageView coverImageView;

    public MarketFragmentAdapter(Context context, ArrayList<MarketObject> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MarketFragmentAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ab_fragment_post_item, parent, false);
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


        public Holder(View view) {
            super(view);
            this.market_name = (TextView) view.findViewById(R.id.textview_cover_name);
            this.comment = (TextView) view.findViewById(R.id.market_comment);
            this.distance = (TextView) view.findViewById(R.id.market_distance);
            this.imgCover = (ImageView) view.findViewById(R.id.imageview_cover);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MarketFragmentAdapter.Holder holder, final int position) {
        holder.market_name.setText(list.get(position).market_name);
        holder.comment.setText(list.get(position).comment);
        holder.distance.setText(list.get(position).distance);
        Picasso.get().load(list.get(position).imgcover).into(holder.imgCover);

    }
}