package com.tysci.ballq.activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.modles.BallQNoteContentEntity;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.views.widgets.TouchImageView;

import java.util.List;

import butterknife.Bind;

/**
 * Created by HTT on 2016/7/13.
 */
public class BallQImageBrowseActivity extends BaseActivity implements ViewPager.OnPageChangeListener{
    @Bind(R.id.tv_image_index)
    protected TextView tvImageIndex;
    @Bind(R.id.image_view_pager)
    protected ViewPager viewPager;

    private int currentIndex;
    private List<BallQNoteContentEntity> contents;
    private ImagePagerAdapter adapter=null;
    @Override
    protected int getContentViewId() {
        return R.layout.activity_image_browse;
    }

    @Override
    protected void initViews() {
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void getIntentData(Intent intent) {
        currentIndex=intent.getIntExtra("index", 0);
        contents=intent.getParcelableArrayListExtra("images");
        if(contents!=null) {
            adapter = new ImagePagerAdapter(contents);
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(currentIndex);
            tvImageIndex.setText((currentIndex + 1) + "/" + contents.size());
        }
    }

    @Override
    protected boolean isCanceledEventBus() {
        return true;
    }

    @Override
    protected void saveInstanceState(Bundle outState) {

    }

    @Override
    protected void handleInstanceState(Bundle savedInstanceState) {

    }

    @Override
    protected void onViewClick(View view) {

    }

    @Override
    protected void notifyEvent(String action) {

    }

    @Override
    protected void notifyEvent(String action, Bundle data) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentIndex=position;
        tvImageIndex.setText((currentIndex + 1) + "/" + contents.size());

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public static final class ImagePagerAdapter extends PagerAdapter {
        private List<BallQNoteContentEntity> pictures;

        public ImagePagerAdapter(List<BallQNoteContentEntity> pictures){
            this.pictures=pictures;
        }

        @Override
        public int getCount() {
            return pictures.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(container.getContext()).inflate(R.layout.layout_image_viewpager_item, container, false);
            final TouchImageView photoView = (TouchImageView) view.findViewById(R.id.photo);
            BallQNoteContentEntity info=pictures.get(position);
            final ProgressBar progressBar=(ProgressBar)view.findViewById(R.id.progressBar);
            String url=pictures.get(position).getOriginal();
            if(TextUtils.isEmpty(url)){
                url=pictures.get(position).getContent();
            }
            Glide.with(container.getContext()).load(HttpUrls.getImageUrl(url)).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    photoView.setImageBitmap(resource);
                    progressBar.setVisibility(View.GONE);
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
