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
    private RecyclerView recyclerView;
    private MarketFragmentAdapter adapter;
    private ArrayList<MarketObject> list = new ArrayList<>();
    private MarketObject mkObject;

    public String market_name;
    public String place;
    public String category;
    public String comment;
    public String distance;
    public String imgcoverfrag;
    public String detail_uri;


    MarketFragment(String title_string) {
        this.title_string = title_string;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.market_fragment_post, container, false);
        view.setClickable(true);

        list.clear();
        /*list.add(new MarketObject("블랭크", "잠실","res","361m",
                "잠실,잠실본동",
                "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fblank_jamsil_img.jpg?alt=media&token=7414e8c8-e738-4fb5-a447-8b7b8dbeb9d1",
                "https://m.store.naver.com/places/detail?id=1409709820"));*/
        /*list.add(new MarketObject("연탄부락", "잠실","res","452m",
                "연탄불에 구워먹는 두툼한 돼지고기",
                "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/market%2F%EC%9E%A0%EC%8B%A4%2Fyeontan_jamsil_img.jpg?alt=media&token=a2ad2f92-c5cc-4e30-8407-2fe60f667d9d",
                "https://m.store.naver.com/places/detail?id=1390330587"));*/
        getFireBaseObject();

        TextView title = view.findViewById(R.id.title);
        title.setText(title_string);

        adapter = new MarketFragmentAdapter(getContext(), list);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void getFireBaseObject(){
        db_market.collection("Market/잠실/res").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){
                        Map map = document.getData();
                        market_name = map.get("marketName").toString();
                        place = map.get("place").toString();
                        category = map.get("category").toString();
                        distance = map.get("distance").toString();
                        comment = map.get("comment").toString();
                        imgcoverfrag = map.get("imgCover").toString();
                        detail_uri = map.get("detailUri").toString();
                        mkObject = new MarketObject(market_name,place,category,distance,comment, imgcoverfrag,detail_uri);
                        list.add(mkObject);
                        Log.v("value",mkObject.getMarketName());
                        Log.v("value",mkObject.getPlace());
                        Log.v("value",mkObject.getCategory());
                        Log.v("value",mkObject.getDistance());
                        Log.v("value",mkObject.getComment());
                        Log.v("value",mkObject.getImgCover());
                        Log.v("value",mkObject.getDetailUri());
                        Log.v("value",""+list.size());
                    }
                }
            }
        });

    }

}
