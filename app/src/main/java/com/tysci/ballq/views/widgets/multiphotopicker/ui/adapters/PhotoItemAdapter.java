package com.tysci.ballq.views.widgets.multiphotopicker.ui.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tysci.ballq.R;
import com.tysci.ballq.utils.ToastUtil;
import com.tysci.ballq.views.widgets.multiphotopicker.entity.Photo;
import com.tysci.ballq.views.widgets.multiphotopicker.entity.PhotoDirectory;
import com.tysci.ballq.views.widgets.multiphotopicker.ui.activitys.PhotoPagerActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HTT on 2016/4/12.
 */
public class PhotoItemAdapter extends RecyclerView.Adapter<PhotoItemAdapter.PhotoItemViewHolder>{
    private List<Photo> photoList;
    private String photoDirectoryId;
    private String photoDirectoryName;
    /**缓存选中的图片*/
    private List<String>selectedPhotos=null;
    private OnSelectPhotoListener onSelectPhotoListener;
    private int maxSelectPhotos;

    public PhotoItemAdapter(PhotoDirectory photoDirectory,int maxSelectPhotos){
        this.photoDirectoryId=photoDirectory.getId();
        this.photoDirectoryName=photoDirectory.getDirectoryName();
        photoList=new ArrayList<>();
        photoList.addAll(photoDirectory.getPhotos());
        selectedPhotos=new ArrayList<>(9);
        this.maxSelectPhotos=maxSelectPhotos;
    }

    public void setPhotoDirectory(PhotoDirectory photoDirectory){
        this.photoDirectoryId=photoDirectory.getId();
        this.photoDirectoryName=photoDirectory.getDirectoryName();
        if(photoList!=null){
            if(photoList.size()>0){
                photoList.clear();
            }
            photoList.addAll(photoDirectory.getPhotos());
            notifyDataSetChanged();
        }
    }


    @Override
    public PhotoItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_photo_item,parent,false);
        return new PhotoItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PhotoItemViewHolder holder, int position) {
        final Photo photo=photoList.get(position);
        Glide.with(holder.itemView.getContext())
                .load(photo.getPath())
                .asBitmap()
                .thumbnail(0.1f)
                .override(480,480)
                .into(holder.ivPhoto);

        if(isSelectedPhoto(photo)){
            holder.ivPhotoPickerMark.setImageResource(R.mipmap.icon_photo_selected);
            holder.photoCoverView.setBackgroundResource(R.color.colorPhotoSelected);
        }else{
            holder.ivPhotoPickerMark.setImageResource(R.mipmap.icon_photo_unselected);
            holder.photoCoverView.setBackgroundResource(R.color.colorTransparent);
        }

        holder.ivPhotoPickerMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSelected=isSelectedPhoto(photo);
                if(isSelected){
                    selectedPhotos.remove(photo.getPath());
                    holder.ivPhotoPickerMark.setImageResource(R.mipmap.icon_photo_unselected);
                    holder.photoCoverView.setBackgroundResource(R.color.colorTransparent);
                }else{
                    if(selectedPhotos.size()<maxSelectPhotos) {
                        selectedPhotos.add(photo.getPath());
                        holder.ivPhotoPickerMark.setImageResource(R.mipmap.icon_photo_selected);
                        holder.photoCoverView.setBackgroundResource(R.color.colorPhotoSelected);
                    }else{
                        ToastUtil.show(v.getContext(),"您最多可选择" + maxSelectPhotos + "张图片");
                    }
                }
                if(onSelectPhotoListener!=null){
                    if(selectedPhotos.size()<=maxSelectPhotos) {
                        onSelectPhotoListener.onSelectedPhoto(selectedPhotos.size(), photo.getPath(), !isSelected);
                    }
                }
            }
        });

        setOnClickPhotoListener(holder,position);
    }

    public void setOnClickPhotoListener(final PhotoItemViewHolder holder, final int position){
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity= (Activity) holder.itemView.getContext();
                Intent intent=new Intent(activity, PhotoPagerActivity.class);
                intent.putExtra("position",position);
                intent.putParcelableArrayListExtra("photos", (ArrayList<? extends Parcelable>) photoList);
                intent.putStringArrayListExtra("selected_photos", (ArrayList<String>) selectedPhotos);
                intent.putExtra("max_select_photos",maxSelectPhotos);
                activity.startActivityForResult(intent,0x0001);
            }
        });
    }

    public void setOnSelectPhotoListener(OnSelectPhotoListener listener){
        this.onSelectPhotoListener=listener;
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    /**
     * 判断当前图片是否已经选择
     * @param photo
     * @return
     */
    private boolean isSelectedPhoto(Photo photo){
        int size=selectedPhotos.size();
        for(int i=0;i<size;i++){
            if(selectedPhotos.get(i).equals(photo.getPath())){
                return true;
            }
        }
        return false;
    }

    public void setSelectedPhotos(List<String>selectedPhotos){
        this.selectedPhotos=selectedPhotos;
        notifyDataSetChanged();
    }

    public List<String> getSelectedPhotos(){
        return selectedPhotos;
    }

    public static final class PhotoItemViewHolder extends ViewHolder {
        ImageView ivPhoto;
        ImageView ivPhotoPickerMark;
        View photoCoverView;
        public PhotoItemViewHolder(View itemView) {
            super(itemView);
            ivPhoto= (ImageView) itemView.findViewById(R.id.iv_photo);
            ivPhotoPickerMark=(ImageView)itemView.findViewById(R.id.iv_picker_mark);
            photoCoverView=itemView.findViewById(R.id.photo_cover_view);
        }
    }

    public interface OnSelectPhotoListener{
        void onSelectedPhoto(int selelctedPhotos, String photoPath, boolean isSelected);
    }

}
