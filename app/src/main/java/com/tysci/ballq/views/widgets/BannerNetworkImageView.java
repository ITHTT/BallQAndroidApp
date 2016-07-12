package com.tysci.ballq.views.widgets;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.tysci.ballq.R;
import com.tysci.ballq.modles.BallQBannerImageEntity;
import com.tysci.ballq.networks.GlideImageLoader;
import com.tysci.ballq.views.widgets.convenientbanner.holder.Holder;

/**
 * Created by HTT on 2016/7/7.
 */
public class BannerNetworkImageView implements Holder<BallQBannerImageEntity> {
    private ImageView imageView;

    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, BallQBannerImageEntity data) {
        imageView.setImageResource(R.mipmap.icon_ball_wrap_default_img);
        GlideImageLoader.loadImage(context,data.getPic_url(), R.mipmap.icon_ball_wrap_default_img,imageView);
    }
}
