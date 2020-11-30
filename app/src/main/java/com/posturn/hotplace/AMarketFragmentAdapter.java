package com.posturn.hotplace;


import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AMarketFragmentAdapter extends RecyclerView.Adapter<MarketFragmentAdapter.Holder> {
    private ArrayList<MarketObject> list = new ArrayList<>();
    private Context context;
    private String placeName;
    ArrayList<Fragment> fragments = new ArrayList<>();

    public AMarketFragmentAdapter(Fragment fragment) {
        fragments.add(new MarketFragment("# 실시간 전체 점포 정보","해방촌","all"));
        fragments.add(new MarketFragment("# 실시간 음식 점포 정보","해방촌","res"));
        fragments.add(new MarketFragment("# 실시간 카페 점포 정보","해방촌","cafe"));
        fragments.add(new MarketFragment("# 실시간 술집 점포 정보","해방촌","bar"));
    }


    @NonNull
    @Override
    public MarketFragmentAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MarketFragmentAdapter.Holder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
