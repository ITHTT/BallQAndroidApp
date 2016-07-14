package com.tysci.ballq.views.widgets;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.tysci.ballq.R;
import com.tysci.ballq.utils.CommonUtils;

import java.util.ArrayList;


public class PromotedActionsLibrary {

    Context context;

    FrameLayout frameLayout;

    ImageView mainImageButton;

    //RotateAnimation rotateOpenAnimation;

    //RotateAnimation rotateCloseAnimation;

    ArrayList<View> promotedActions;

    ObjectAnimator objectAnimator[];

    private int px;

    private static final int ANIMATION_TIME = 100;

    private boolean isMenuOpened;

    public void setup(Context activityContext, FrameLayout layout) {
        context = activityContext;
        promotedActions = new ArrayList<View>();
        frameLayout = layout;
        px = (int) context.getResources().getDimension(R.dimen.dim56dp) + CommonUtils.dip2px(context, 10);
//        openRotation();
//        closeRotation();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void addMainItem(int res) {

       ImageView button=new ImageView(context);

        button.setImageResource(res);

        button.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View view) {

                if (isMenuOpened) {
                    closePromotedActions().start();
                    isMenuOpened = false;
                } else {
                    isMenuOpened = true;
                    openPromotedActions().start();
                }
            }
        });

        frameLayout.addView(button);

        mainImageButton = button;

        return;
    }

    public View addItem(int id,int res, View.OnClickListener onClickListener) {

        ImageView button = (ImageView) LayoutInflater.from(context).inflate(R.layout.layout_menu_action_button, frameLayout, false);

        button.setId(id);
        button.setImageResource(res);

        button.setOnClickListener(onClickListener);
        promotedActions.add(button);
        frameLayout.addView(button);

        return button;
    }

    /**
     * Set close animation for promoted actions
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public AnimatorSet closePromotedActions() {

        if (objectAnimator == null){
            objectAnimatorSetup();
        }

        AnimatorSet animation = new AnimatorSet();

        for (int i = 0; i < promotedActions.size(); i++) {

            objectAnimator[i] = setCloseAnimation(promotedActions.get(i), i+1);
        }

        if (objectAnimator.length == 0) {
            objectAnimator = null;
        }

        animation.playTogether(objectAnimator);
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                //mainImageButton.startAnimation(rotateCloseAnimation);
                mainImageButton.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mainImageButton.setClickable(true);
                hidePromotedActions();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                mainImageButton.setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {}
        });

        return animation;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public AnimatorSet openPromotedActions() {

        if (objectAnimator == null){
            objectAnimatorSetup();
        }

        AnimatorSet animation = new AnimatorSet();

        for (int i = 0; i < promotedActions.size(); i++) {

            objectAnimator[i] = setOpenAnimation(promotedActions.get(i), i+1);
        }

        if (objectAnimator.length == 0) {
            objectAnimator = null;
        }

        animation.playTogether(objectAnimator);
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
               // mainImageButton.startAnimation(rotateOpenAnimation);
                //mainImageButton.setClickable(false);
                showPromotedActions();
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //mainImageButton.setClickable(true);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                //mainImageButton.setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {}
        });


        return animation;
    }

    private void objectAnimatorSetup() {

        objectAnimator = new ObjectAnimator[promotedActions.size()];
    }


    /**
     * Set close animation for single button
     *
     * @param promotedAction
     * @param position
     * @return objectAnimator
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private ObjectAnimator setCloseAnimation(View promotedAction, int position) {

        ObjectAnimator objectAnimator;

        if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            objectAnimator = ObjectAnimator.ofFloat(promotedAction, View.TRANSLATION_Y, px * (promotedActions.size() - position), 0f);
            objectAnimator.setRepeatCount(0);
            objectAnimator.setDuration(ANIMATION_TIME * (promotedActions.size() - position));

        } else {

            objectAnimator = ObjectAnimator.ofFloat(promotedAction, View.TRANSLATION_X, px * (promotedActions.size() - position), 0f);
            objectAnimator.setRepeatCount(0);
            objectAnimator.setDuration(ANIMATION_TIME * (promotedActions.size() - position));
        }

        return objectAnimator;
    }

    /**
     * Set open animation for single button
     *
     * @param promotedAction
     * @param position
     * @return objectAnimator
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private ObjectAnimator setOpenAnimation(View promotedAction, int position) {

        ObjectAnimator objectAnimator;

        if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            objectAnimator = ObjectAnimator.ofFloat(promotedAction, View.TRANSLATION_Y, -100f, px * (promotedActions.size() - position));
            objectAnimator.setRepeatCount(0);
            //objectAnimator.setStartDelay((promotedActions.size() - position)*100);
            objectAnimator.setDuration(ANIMATION_TIME * (promotedActions.size() - position));
        } else {
            objectAnimator = ObjectAnimator.ofFloat(promotedAction, View.TRANSLATION_X, 0f, px * (promotedActions.size() - position));
            objectAnimator.setRepeatCount(0);
           // objectAnimator.setStartDelay((promotedActions.size() - position)*100);
            objectAnimator.setDuration(ANIMATION_TIME * (promotedActions.size() - position));
        }
        return objectAnimator;
    }

    private void hidePromotedActions() {

        for (int i = 0; i < promotedActions.size(); i++) {
            promotedActions.get(i).setVisibility(View.GONE);
        }
    }

    private void showPromotedActions() {

        for (int i = 0; i < promotedActions.size(); i++) {
            promotedActions.get(i).setVisibility(View.VISIBLE);
        }
    }

//    private void openRotation() {
//
//        rotateOpenAnimation = new RotateAnimation(0, 45, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
//                0.5f);
//        rotateOpenAnimation.setFillAfter(true);
//        rotateOpenAnimation.setFillEnabled(true);
//        rotateOpenAnimation.setDuration(ANIMATION_TIME);
//    }
//
//    private void closeRotation() {
//
//        rotateCloseAnimation = new RotateAnimation(45, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        rotateCloseAnimation.setFillAfter(true);
//        rotateCloseAnimation.setFillEnabled(true);
//        rotateCloseAnimation.setDuration(ANIMATION_TIME);
//    }
}
