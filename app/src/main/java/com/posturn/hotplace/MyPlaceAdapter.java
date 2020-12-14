package com.posturn.hotplace;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyPlaceAdapter extends RecyclerView.Adapter<MyPlaceAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> myplacelist;
    private ObjectPlace op;
    private String token;

    SharedPreferences pref;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public double latitude;
    public double longitude;

    public MyPlaceAdapter(Context context, ArrayList<String> myplacelist,String token){
        this.context = context;
        this.myplacelist = myplacelist;
        this.token = token;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        protected ImageView my_place_image;
        protected TextView my_place_name;
        protected TextView my_place_nickname;
        protected TextView my_place_distance;
        protected CardView my_fav_card;
        protected ImageView my_place_star;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            my_place_image = itemView.findViewById(R.id.my_place_image);
            my_place_name = itemView.findViewById(R.id.my_place_name);
            my_place_nickname = itemView.findViewById(R.id.my_place_nickname);
            my_place_distance = itemView.findViewById(R.id.my_place_distance);
            my_fav_card = itemView.findViewById(R.id.my_fav_card);
            my_place_star = itemView.findViewById(R.id.my_place_star);
        }
    }

    @NonNull
    @Override
    public MyPlaceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.myplace_item, null);

        //강남 경도 위도
        latitude = 37.4985;
        //latitude = gpsTracker.getLatitude();
        longitude = 127.0299;
        //longitude = gpsTracker.getLongitude();

        return new MyPlaceAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPlaceAdapter.ViewHolder holder, int position) {

        holder.my_place_name.setText(myplacelist.get(position).toString());
        db.collection("HotPlace").document(myplacelist.get(position).toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                double lat=0;
                double lon=0;
                if(task.isSuccessful()){
                    DocumentSnapshot ds = task.getResult();
                    Picasso.get().load(ds.getString("img")).into(holder.my_place_image);
                    holder.my_place_nickname.setText(ds.getString("tag"));
                    lat = ds.getDouble("lat");
                    lon = ds.getDouble("lon");
                    holder.my_place_distance.setText(getDistance(latitude,longitude,lat,lon)+"km");
                }
            }
        });


        holder.my_place_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MarketMainActivity.class);
                intent.putExtra("placeName",holder.my_place_name.getText());
                context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        holder.my_place_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MarketMainActivity.class);
                intent.putExtra("placeName",holder.my_place_name.getText());
                context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        holder.my_place_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("1111111111",position+"");
                ObjectMyplace obmyplace = new ObjectMyplace();
                db.collection("MyPlace").document(token).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot ds = task.getResult();
                            obmyplace.myplacelist = (ArrayList) ds.get("myplacelist");
                            myplacelist = obmyplace.myplacelist;
                            myplacelist.remove(position);
                            obmyplace.myplacelist = myplacelist;
                            Toast.makeText( context, "My 플레이스에서 삭제되었습니다.", Toast.LENGTH_SHORT ).show();

                            db.collection("MyPlace").document(token).set(obmyplace);

                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, obmyplace.myplacelist.size());
                        }
                    }
                });
            }
        });

    }
    @Override
    public int getItemCount() {
        return myplacelist.size();
    }

    private String getDistance(double myLat, double myLon, double targetLat, double targetLon) {
        double distance;

        Location locationA = new Location("point A");
        locationA.setLatitude(myLat);
        locationA.setLongitude(myLon);

        Location locationB = new Location("point B");
        locationB.setLatitude(targetLat);
        locationB.setLongitude(targetLon);

        distance = locationA.distanceTo(locationB) /1000.0;

        return String.format("%.2f",distance);
    }

}
