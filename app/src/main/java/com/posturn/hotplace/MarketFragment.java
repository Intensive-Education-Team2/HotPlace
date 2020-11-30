package com.posturn.hotplace;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Map;

public class MarketFragment extends Fragment {
    private String title_string;

    FirebaseFirestore db_market = FirebaseFirestore.getInstance();
    public RecyclerView recyclerView;
    public MarketFragmentAdapter adapter;
    public ArrayList<MarketObject> list = new ArrayList<>();
    public MarketObject mkObject;

    public String market_name;
    public String place;
    public String category;
    public String comment;
    public double lat;
    public double lon;
    public String imgcoverfrag;
    public String detail_uri;

    public String f_place;
    public String f_category;


    MarketFragment(String title_string, String placeName, String categoryName) {
        this.title_string = title_string;
        this.f_place = placeName;
        this.f_category = categoryName;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.market_fragment_post, container, false);
        view.setClickable(true);

        Log.v("value",this.f_category);
        Log.v("value",this.f_place);

        list.clear();

        getFireBaseObject();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        TextView title = view.findViewById(R.id.title);
        title.setText(title_string);

        return view;
    }

    public void getFireBaseObject(){
        ArrayList<String> connectPathList = new ArrayList<String>();

        if(f_category.equals("all")){
            connectPathList.add("market_test/"+f_place+"/res");
            connectPathList.add("market_test/"+f_place+"/cafe");
            connectPathList.add("market_test/"+f_place+"/bar");
        }else{
            connectPathList.add("market_test/"+f_place+"/"+f_category);
        }

        for(String connectPath : connectPathList) {
            db_market.collection(connectPath).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map map = document.getData();
                            market_name = map.get("marketName").toString();
                            place = map.get("place").toString();
                            category = map.get("category").toString();
                            lat = (Double)map.get("lat");
                            lon = (Double)map.get("lon");
                            comment = map.get("comment").toString();
                            imgcoverfrag = map.get("imgCover").toString();
                            detail_uri = map.get("detailUri").toString();
                            mkObject = new MarketObject(market_name, place, category, lat, lon, comment, imgcoverfrag, detail_uri);
                            list.add(mkObject);
                            Log.v("value", mkObject.getMarketName());
                            Log.v("value", mkObject.getPlace());
                            Log.v("value", mkObject.getCategory());
                            Log.v("value", mkObject.getComment());
                            Log.v("value", mkObject.getImgCover());
                            Log.v("value", mkObject.getDetailUri());
                            Log.v("value", "" + list.size());
                            for(MarketObject i : list){
                                Log.d("TestArray_InFirebase",i.market_name);
                            }
                        }
                    }
                    adapter = new MarketFragmentAdapter(getContext(), list);

                    recyclerView.setHasFixedSize(true);

                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(adapter);
                }


            });
        }


    }

}
