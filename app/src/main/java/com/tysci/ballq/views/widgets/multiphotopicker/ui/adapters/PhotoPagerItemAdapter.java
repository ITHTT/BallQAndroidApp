package com.tysci.ballq.views.widgets.multiphotopicker.ui.adapters;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.tysci.ballq.R;
import com.tysci.ballq.views.widgets.TouchImageView;
import com.tysci.ballq.views.widgets.multiphotopicker.entity.Photo;

import java.util.List;


/**
 * Created by HTT on 2016/4/16.
 */
public class PhotoPagerItemAdapter extends PagerAdapter {
    private List<Photo> photos=null;
    private Context context;

    public PhotoPagerItemAdapter(Context context,List<Photo>photos){
        this.context=context;
        this.photos=photos;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view= LayoutInflater.from(context).inflate(R.layout.layout_photo_pager_item,container,false);
        TouchImageView photoView= (TouchImageView) view.findViewById(R.id.iv_photo);
        Glide.with(context)
             .load(photos.get(position).getPath())
             .asBitmap()
             .override(800,800)
             .into(photoView);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
}
