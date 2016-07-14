package com.tysci.ballq.views.widgets.multiphotopicker.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tysci.ballq.R;
import com.tysci.ballq.views.widgets.SmoothCheckBox;
import com.tysci.ballq.views.widgets.multiphotopicker.entity.PhotoDirectory;

import java.util.List;

/**
 * Created by HTT on 2016/4/13.
 */
public class PhotoDirectoryItemAdapter extends BaseAdapter{
    private List<PhotoDirectory> photoDirectoryList;
    private Context context;
    private OnItemClickListener onItemClickListener=null;
    private int currentCheckedItem=0;


    public PhotoDirectoryItemAdapter(Context context,List<PhotoDirectory>photoDirectories){
        this.context=context;
        this.photoDirectoryList=photoDirectories;
    }
    @Override
    public int getCount() {
        return photoDirectoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return photoDirectoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        PhotoDirectoryItemViewHolder holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.layout_photo_directory_item,parent,false);
            holder=new PhotoDirectoryItemViewHolder();
            holder.ivDirectoryCoverPhoto=(ImageView)convertView.findViewById(R.id.iv_directory_cover_photo);
            holder.tvDirectoryName=(TextView)convertView.findViewById(R.id.tv_directory_name);
            holder.tvDIrectoryPhotoCounts=(TextView)convertView.findViewById(R.id.tv_directory_photo_counts);
            holder.photoCheckBox=(SmoothCheckBox)convertView.findViewById(R.id.directory_checkbox);
            convertView.setTag(holder);
        }else{
            holder= (PhotoDirectoryItemViewHolder) convertView.getTag();
        }
        PhotoDirectory directory=photoDirectoryList.get(position);
        Glide.with(context)
                .load(directory.getDirectoryPath())
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy( DiskCacheStrategy.NONE )
                .thumbnail(0.1f)
                .into(holder.ivDirectoryCoverPhoto);
        holder.tvDirectoryName.setText(directory.getDirectoryName());
        holder.tvDIrectoryPhotoCounts.setText(directory.getPhotos().size()+"");
        if(currentCheckedItem==position){
            holder.photoCheckBox.setChecked(true);
        }else{
            holder.photoCheckBox.setChecked(false);
        }
        final View finalConvertView = convertView;
        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClick(finalConvertView,position);
                    currentCheckedItem=position;
                    notifyDataSetChanged();
                }
            }
        };
        convertView.setOnClickListener(onClickListener);
        holder.photoCheckBox.setOnClickListener(onClickListener);
        return convertView;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener=listener;
    }

    public static final class PhotoDirectoryItemViewHolder{
        ImageView ivDirectoryCoverPhoto;
        TextView tvDirectoryName;
        TextView tvDIrectoryPhotoCounts;
        SmoothCheckBox photoCheckBox;
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
}
