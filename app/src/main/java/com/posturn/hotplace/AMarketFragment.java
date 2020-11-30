package com.posturn.hotplace;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

public class AMarketFragment extends Fragment {
    AMarketFragmentAdapter amkFragAdapter;
    ViewPager2 viewPager;

    AMarketFragment(String title_string, String placeName, String categoryName) {


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.a_market_fragment_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        amkFragAdapter = new AMarketFragmentAdapter(this);
        viewPager = view.findViewById(R.id.pager);
        viewPager.setAdapter(amkFragAdapter);
    }

}
