package com.feca.mface.ui;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.feca.mface.R;

import java.util.List;

/**
 * Created by ljh on 2017/9/28.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MomentsViewHolder> {
    private List<Moment> moments;
    private Context context;

    public RecyclerViewAdapter(List<Moment> moments,Context context) {
        this.moments = moments;
        this.context=context;
    }

    static class MomentsViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        ImageView moment_photo;
        TextView art_title;
        TextView art_intro;


        public MomentsViewHolder(final View itemView) {
            super(itemView);
            cardView= (CardView) itemView.findViewById(R.id.card_view);
            moment_photo= (ImageView) itemView.findViewById(R.id.moment_photo);
            art_title= (TextView) itemView.findViewById(R.id.art_title);
            art_intro= (TextView) itemView.findViewById(R.id.art_intro);

            //设置TextView背景为半透明
            art_title.setBackgroundColor(Color.argb(20, 0, 0, 0));

        }


    }

    @Override
    public MomentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.moment_list_item,parent,false);
        MomentsViewHolder momentsViewHolder=new MomentsViewHolder(v);
        return momentsViewHolder;
    }

    @Override
    public void onBindViewHolder(MomentsViewHolder holder, int position) {
        holder.moment_photo.setImageResource(moments.get(position).getPhotoId());
        holder.art_intro.setText(moments.get(position).getContent());
        holder.art_title.setText(moments.get(position).getTitle());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return moments.size();
    }
}
