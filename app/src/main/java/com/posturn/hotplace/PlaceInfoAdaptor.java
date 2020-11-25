package com.posturn.hotplace;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlaceInfoAdaptor extends RecyclerView.Adapter<PlaceInfoAdaptor.ViewHolder> {

    private ArrayList<PlaceInfoObject> mList;

    public  class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView_img;
        private TextView textView_title, textView_release, texView_director;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView_img = (ImageView) itemView.findViewById(R.id.placeinfo_img);
            textView_title = (TextView) itemView.findViewById(R.id.placeinfo_name);
            textView_release = (TextView) itemView.findViewById(R.id.placeinfo_distance);
            texView_director = (TextView) itemView.findViewById(R.id.placeinfo_more);
        }
    }

    //생성자
    public PlaceInfoAdaptor(ArrayList<PlaceInfoObject> list) {
        this.mList = list;
    }

    @NonNull
    @Override
    public PlaceInfoAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_info_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceInfoAdaptor.ViewHolder holder, int position) {

        holder.textView_title.setText(String.valueOf(mList.get(position).getTitle()));
        //다 해줬는데도 GlideApp 에러가 나면 rebuild project를 해주자.
        GlideApp.with(holder.itemView).load(mList.get(position).getImg_url())
                .override(300,400)
                .into(holder.imageView_img);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
