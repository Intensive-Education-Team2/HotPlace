package com.posturn.hotplace;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecommandSectionAdapter extends RecyclerView.Adapter<RecommandSectionAdapter.ViewHolder> {

    private ArrayList<ArrayList<ObjectPlace>> recommandPlaceSectionList;
    private Context context;


    public RecommandSectionAdapter(Context context, ArrayList<ArrayList<ObjectPlace>> recommandAllPlaceList){
        this.context = context;
        this.recommandPlaceSectionList = recommandAllPlaceList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected RecyclerView recyclerView;
        protected TextView sectionName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.sectionName = (TextView)itemView.findViewById(R.id.section_name);
            this.recyclerView = (RecyclerView)itemView.findViewById(R.id.recommand_section_recycler_view);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommand_recycler_section,null);

        return new RecommandSectionAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecommandPlaceAdapter rpAdapter = new RecommandPlaceAdapter(recommandPlaceSectionList.get(position));

        holder.sectionName.setText("급상승 핫플레이스");
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        holder.recyclerView.setAdapter(rpAdapter);
    }

    @Override
    public int getItemCount() {
        return recommandPlaceSectionList.size();
    }


}
