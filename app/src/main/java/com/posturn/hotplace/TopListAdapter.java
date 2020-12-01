package com.posturn.hotplace;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

class TopListAdapter extends BaseAdapter implements View.OnClickListener {

    private ArrayList<ObjectCount> objectCountsToday;
    private ArrayList<ObjectCount> objectCountsYesterday;
    private Context mContext;

    public TopListAdapter(Context mContext, ArrayList<ObjectCount> objectCountsToday, ArrayList<ObjectCount> objectCountsYesterday) {
        this.mContext = mContext;
        this.objectCountsToday = objectCountsToday;
        this.objectCountsYesterday = objectCountsYesterday;
    }

    public int getCount() {
        return objectCountsToday.size();
    }

    public Object getItem(int pos) {
        return objectCountsToday.get(pos);
    }


    @Override
    public long getItemId(int pos) {
        return pos;
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mContext = parent.getContext();

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.rank_list_shape, parent, false);
        }

        TextView placerank = convertView.findViewById(R.id.place_rank);
        TextView placename = convertView.findViewById(R.id.place_name);
        ImageView updownimg = convertView.findViewById(R.id.updown_img);
        TextView updownnum = convertView.findViewById(R.id.updown_num);

        final ObjectCount thisObjectCount = objectCountsToday.get(position);

        placerank.setText(Integer.toString(position + 1));
        placename.setText(thisObjectCount.getName());
        for (int i = 0; i < objectCountsToday.size(); i++) {
            if (thisObjectCount.getName().equals(objectCountsYesterday.get(i).getName())) {
                int updown = i - position;
                if (updown == 0) {
                    updownimg.setImageResource(R.drawable.notchange);
                    updownnum.setVisibility(View.INVISIBLE);
                    break;
                } else if (updown > 0) {
                    updownimg.setImageResource(R.drawable.up);
                    updownnum.setVisibility(View.VISIBLE);
                    updownnum.setText(Integer.toString(updown));
                    updownnum.setTextColor(R.color.colorRed);
                    break;
                } else {
                    updownimg.setImageResource(R.drawable.down);
                    updownnum.setVisibility(View.VISIBLE);
                    updownnum.setText(Integer.toString(Math.abs(updown)));
                    updownnum.setTextColor(R.color.colorBlue);
                    break;
                }
            }
        }



        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MarketMainActivity.class);
                intent.putExtra("placeName", thisObjectCount.getName());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    @Override
    public void onClick(View view) {

    }
}