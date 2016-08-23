package com.tysci.ballq.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.ybq.android.spinkit.SpinKitView;
import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseDialog;
import com.tysci.ballq.networks.HttpUrls;
import com.tysci.ballq.utils.ScreenUtil;
import com.tysci.ballq.views.widgets.TouchImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by LinDe
 * on 2016-08-17 0017.
 */
public final class ImageUrlBrowserDialog extends BaseDialog implements ViewPager.OnPageChangeListener
{
    @Bind(R.id.tv_image_index)
    protected TextView tvImageIndex;
    @Bind(R.id.image_view_pager)
    protected ViewPager mViewPager;

    private int currentImageIndex;
    private List<String> mUrls;
    private MyImagePagerAdapter mAdapter;

    public ImageUrlBrowserDialog(Context context)
    {
        super(context);
        mUrls = new ArrayList<>();
        mAdapter = new MyImagePagerAdapter();
        currentImageIndex = 0;
    }

    public ImageUrlBrowserDialog(Activity context)
    {
        super(context);
        mUrls = new ArrayList<>();
        mAdapter = new MyImagePagerAdapter();
        currentImageIndex = 0;
    }

    @Override
    protected int getContentView()
    {
        return R.layout.activity_image_browse;
    }

    @Override
    protected void initializing(Bundle savedInstanceState)
    {
        ViewGroup layout = (ViewGroup) this.findViewById(R.id.layout_content);
        ViewGroup.LayoutParams lp = layout.getLayoutParams();
        lp.width = ScreenUtil.getDisplayMetrics(getContext()).widthPixels;
        layout.setLayoutParams(lp);

        mViewPager.addOnPageChangeListener(this);
        mViewPager.setAdapter(mAdapter);
    }

    public void setCurrentImageIndex(int currentImageIndex)
    {
        this.currentImageIndex = currentImageIndex;
    }

    public void addUrl(String... urls)
    {
        for (String url : urls)
        {
            if (!TextUtils.isEmpty(url))
            {
                mUrls.add(HttpUrls.getImageUrl(url));
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    public void addUrl(List<String> urls)
    {
        for (String url : urls)
        {
            if (!TextUtils.isEmpty(url))
            {
                mUrls.add(HttpUrls.getImageUrl(url));
            }
        }
        mAdapter.notifyDataSetChanged();

    }

    public void clearUrls()
    {
        mUrls.clear();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void show()
    {
        if (mUrls != null && !mUrls.isEmpty())
        {
            super.show();

            if (currentImageIndex > mUrls.size() - 1 || currentImageIndex < 0)
            {
                currentImageIndex = 0;
            }
            mViewPager.setCurrentItem(currentImageIndex);

            setTextImageIndex(currentImageIndex);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {
    }

    @Override
    public void onPageSelected(int position)
    {
        setTextImageIndex(position);
    }

    private void setTextImageIndex(int position)
    {
        tvImageIndex.setText(String.valueOf(position + 1));
        tvImageIndex.append("/");
        tvImageIndex.append(mUrls == null ? "0" : String.valueOf(mUrls.size()));
        if (tvImageIndex.getText().toString().equals("1/1"))
        {
            tvImageIndex.setVisibility(View.GONE);
        }
        else
        {
            tvImageIndex.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state)
    {

    }

    private final class MyImagePagerAdapter extends PagerAdapter
    {
        private Bitmap mBitmap;

        @Override
        public int getCount()
        {
            return mUrls.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position)
        {
            final Context context = container.getContext();

            View view = LayoutInflater.from(context).inflate(R.layout.layout_image_viewpager_item, container, false);
            final TouchImageView photoView = (TouchImageView) view.findViewById(R.id.photo);
            final SpinKitView spinKitView = (SpinKitView) view.findViewById(R.id.progressBar);
            spinKitView.setVisibility(View.VISIBLE);
            Glide.with(container.getContext()).load(HttpUrls.getImageUrl(mUrls.get(position))).asBitmap().into(new SimpleTarget<Bitmap>()
            {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation)
                {
                    mBitmap = resource;
                    photoView.setImageBitmap(resource);
                    spinKitView.setVisibility(View.GONE);
                }
            });
            View.OnClickListener dismissListener = new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    dismiss();
                }
            };
            photoView.setOnClickListener(dismissListener);
            spinKitView.setOnClickListener(dismissListener);

            photoView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    saveBitmap(mBitmap);
                    return true;
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }
    }

    private void saveBitmap(Bitmap bitmap)
    {
        SaveBitmapDialog dialog = new SaveBitmapDialog(getOwnerActivity());
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.DialogFromBottom);
        window.setGravity(Gravity.BOTTOM);

        dialog.setBitmap(bitmap);

        dialog.show();
    }
}
