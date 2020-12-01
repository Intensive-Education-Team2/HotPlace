package com.posturn.hotplace;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecommandPlaceAdapter extends RecyclerView.Adapter<RecommandPlaceAdapter.ViewHolder> {

    private ArrayList<ObjectPlace> placeList;

    FirebaseFirestore db_re_place = FirebaseFirestore.getInstance();

    public RecommandPlaceAdapter(ArrayList<ObjectPlace> placelist){
        this.placeList = placelist;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        protected ImageView re_place_image;
        protected TextView re_place_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            re_place_image = itemView.findViewById(R.id.re_place_img);
            re_place_name = itemView.findViewById(R.id.re_place_name);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommand_recycler_item, null);

        return new RecommandPlaceAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.re_place_name.setText(placeList.get(position).getName());
        Picasso.get().load(placeList.get(position).getImg()).into(holder.re_place_image);
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }


}