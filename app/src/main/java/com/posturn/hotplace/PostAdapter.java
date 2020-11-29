package com.posturn.hotplace;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private ArrayList<ObjectPost> objectPosts = null;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userImg;
        TextView userName;
        TextView placeName;
        ImageView boardMenu;
        ImageView contentImg;
        ImageView like;
        TextView likeNum;
        TextView content;

        ViewHolder(View itemView) {
            super(itemView);

            // 뷰 객체에 대한 참조. (hold strong reference)
            userImg=itemView.findViewById(R.id.imageViewUserImg);
            userName=itemView.findViewById(R.id.textViewUserName);
            placeName=itemView.findViewById(R.id.placename);
            boardMenu=itemView.findViewById(R.id.boardmenu);
            contentImg=itemView.findViewById(R.id.contentimg);
            like=itemView.findViewById(R.id.like);
            likeNum=itemView.findViewById(R.id.likenum);
            content=itemView.findViewById(R.id.content);
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    PostAdapter(ArrayList<ObjectPost> postlist) {
        objectPosts = postlist;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.post_list_shape, parent, false);
        PostAdapter.ViewHolder vh = new PostAdapter.ViewHolder(view);

        return vh;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(PostAdapter.ViewHolder holder, int position) {
        Picasso.get().load(objectPosts.get(position).getUserimg()).into(holder.userImg);
        holder.userName.setText(objectPosts.get(position).getUsername());
        holder.placeName.setText(objectPosts.get(position).getPlace());
        Picasso.get().load(objectPosts.get(position).getImg()).into(holder.contentImg);
        holder.likeNum.setText(Integer.toString(objectPosts.get(position).getHot()));
        holder.content.setText(objectPosts.get(position).getContent());

    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return objectPosts.size();
    }
}
