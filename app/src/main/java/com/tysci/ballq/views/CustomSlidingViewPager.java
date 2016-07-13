package com.tysci.ballq.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseFragment;

import java.util.List;

/**
 * Created by LinDe on 2016-01-25.
 *
 * @see ViewPager
 */
@SuppressWarnings("unused")
public class CustomSlidingViewPager extends ViewPager {
    private boolean isCanScrollHorizontal;

    public CustomSlidingViewPager(Context context) {
        this(context, null);
    }

    public CustomSlidingViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        isCanScrollHorizontal = true;
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomSlidingViewPager);
            isCanScrollHorizontal = ta.getBoolean(R.styleable.CustomSlidingViewPager_can_scroll_horizontal, true);

            ta.recycle();
        }
//        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
//        {
//            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//            @Override
//            public void onGlobalLayout()
//            {
////                Log.e("TAG", String.valueOf(getWidth()));
////                Log.e("TAG", String.valueOf(getHeight()));
//                getViewTreeObserver().removeOnGlobalLayoutListener(this);
//            }
//        });
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return isCanScrollHorizontal && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return isCanScrollHorizontal && super.onInterceptTouchEvent(event);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
    }

//    /**
//     * @param canScrollHorizontal 允许左右滑动
//     */
//    public final void setCanScrollHorizontal(boolean canScrollHorizontal) {
//        this.isCanScrollHorizontal = canScrollHorizontal;
//    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
    }

    public void setAdapter(FragmentManager manager, final List<BaseFragment> list) {
        setAdapter(new FragmentStatePagerAdapter(manager) {
            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }

            @Override
            public int getCount() {
                return list.size();
            }
        });
    }
}
