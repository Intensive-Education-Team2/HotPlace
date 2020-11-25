package com.posturn.hotplace;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceInfoFoldPhotoAdapter extends RecyclerView.Adapter<PlaceInfoFoldPhotoAdapter.PhotoViewHolder>{
    private String[] mDataSet;
    private Map<Integer, Boolean> mFoldStates = new HashMap<>();
    private Context mContext;

    public PlaceInfoFoldPhotoAdapter(String[] dataSet, Context context) {
        mDataSet = dataSet;
        mContext = context;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhotoViewHolder(new PlaceInfoFoldableLayout(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, int position) {
        final String path = "content://com.posturn.hotplace/demo-pictures/" + mDataSet[position];

        // Bind data
        Picasso.get().load(path).into(holder.mImageViewCover);
        Picasso.get().load(path).into(holder.mImageViewDetail);
        holder.mTextViewCover.setText(mDataSet[position].replace(".jpg", ""));

        // Bind state
        if (mFoldStates.containsKey(position)) {
            if (mFoldStates.get(position) == Boolean.TRUE) {
                if (!holder.mFoldableLayout.isFolded()) {
                    holder.mFoldableLayout.foldWithoutAnimation();
                }
            } else if (mFoldStates.get(position) == Boolean.FALSE) {
                if (holder.mFoldableLayout.isFolded()) {
                    holder.mFoldableLayout.unfoldWithoutAnimation();
                }
            }
        } else {
            holder.mFoldableLayout.foldWithoutAnimation();
        }

        holder.mButtonDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/jpg");
                Uri uri = Uri.parse(path);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                mContext.startActivity(Intent.createChooser(shareIntent, "Share image using"));
            }
        });

        holder.mFoldableLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.mFoldableLayout.isFolded()) {
                    holder.mFoldableLayout.unfoldWithAnimation();
                } else {
                    holder.mFoldableLayout.foldWithAnimation();
                }
            }
        });
        holder.mFoldableLayout.setFoldListener(new PlaceInfoFoldableLayout.FoldListener() {
            @Override
            public void onUnFoldStart() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.mFoldableLayout.setElevation(5);
                }
            }

            @Override
            public void onUnFoldEnd() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.mFoldableLayout.setElevation(0);
                }
                mFoldStates.put(holder.getAdapterPosition(), false);
            }

            @Override
            public void onFoldStart() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.mFoldableLayout.setElevation(5);
                }
            }

            @Override
            public void onFoldEnd() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.mFoldableLayout.setElevation(0);
                }
                mFoldStates.put(holder.getAdapterPosition(), true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }

    protected static class PhotoViewHolder extends RecyclerView.ViewHolder {

        protected PlaceInfoFoldableLayout mFoldableLayout;

        @BindView(R.id.imageview_cover)
        protected ImageView mImageViewCover;

        @BindView(R.id.imageview_detail)
        protected ImageView mImageViewDetail;

        @BindView(R.id.textview_cover)
        protected TextView mTextViewCover;

        @BindView(R.id.img_plus_button)
        protected Button mButtonDetail;

        /*@BindView(R.id.comment)
        protected Button mTextComment;

        @BindView(R.id.distance)
        protected Button mTextDistance;*/

        public PhotoViewHolder(PlaceInfoFoldableLayout foldableLayout) {
            super(foldableLayout);
            mFoldableLayout = foldableLayout;
            foldableLayout.setupViews(R.layout.placeinfo_fold_list_item_cover, R.layout.placeinfo_fold_list_item_detail, R.dimen.card_cover_height, itemView.getContext());
            ButterKnife.bind(this, foldableLayout);
        }
    }


}
