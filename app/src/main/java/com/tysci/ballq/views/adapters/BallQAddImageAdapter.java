package com.tysci.ballq.views.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tysci.ballq.R;
import com.tysci.ballq.views.widgets.multiphotopicker.ui.activitys.PhotoPickerActivity;

import java.util.List;

/**
 * Created by Administrator on 2016/7/14.
 */
public class BallQAddImageAdapter extends RecyclerView.Adapter<BallQAddImageAdapter.BallQAddImageViewHolder>{
    private List<String> imgUrls;

    private OnDeletePictureListener onDeletePictureListener;

    public BallQAddImageAdapter(List<String>list){
        imgUrls=list;
    }

    @Override
    public BallQAddImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_add_item,parent,false);
        return new BallQAddImageViewHolder(view);
    }

    public void setOnDeletePictureListener(OnDeletePictureListener listener){
        this.onDeletePictureListener=listener;
    }

    @Override
    public void onBindViewHolder(final BallQAddImageViewHolder holder, final int position) {
        String url=imgUrls.get(position);
        //ImageLoader.getInstance().displayImage(url, holder.ivPicture);
        if(position==imgUrls.size()-1){
            holder.ivPicture.setBackgroundResource(0);
            Glide.with(holder.itemView.getContext())
                    .load(url)
                    .asBitmap()
                    .placeholder(R.mipmap.icon_add_pictures)
                    .into(holder.ivPicture);
        }else{
            holder.ivPicture.setBackgroundResource(R.drawable.image_chose_selector);
            Glide.with(holder.itemView.getContext())
                    .load(url)
                    .asBitmap()
                    .into(holder.ivPicture);
        }

        if(position==imgUrls.size()-1){
            holder.ivDelete.setVisibility(View.GONE);
            holder.ivPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Activity activity= (Activity) holder.itemView.getContext();
                    int counts=10-imgUrls.size();
                    Intent intent=new Intent(activity, PhotoPickerActivity.class);
                    intent.putExtra("max_photo_counts", counts);
                    activity.startActivityForResult(intent, 0x0001);
                }
            });
        }else{
            holder.ivPicture.setOnClickListener(null);
            holder.ivDelete.setVisibility(View.VISIBLE);
        }

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgUrls.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                if(onDeletePictureListener!=null){
                    onDeletePictureListener.onDeletePicture(imgUrls.size()-1);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return imgUrls.size();
    }

    public static final class BallQAddImageViewHolder extends RecyclerView.ViewHolder{
        ImageView ivPicture;
        View ivDelete;

        public BallQAddImageViewHolder(View itemView) {
            super(itemView);
            ivPicture=(ImageView)itemView.findViewById(R.id.iv_picture);
            ivDelete=itemView.findViewById(R.id.iv_delete);
        }
    }

    public interface OnDeletePictureListener{
        void onDeletePicture(int counts);
    }
}
