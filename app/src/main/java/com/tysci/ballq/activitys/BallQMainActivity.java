package com.tysci.ballq.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.tysci.ballq.R;
import com.tysci.ballq.base.BaseActivity;
import com.tysci.ballq.fragments.BallQFindFragment;
import com.tysci.ballq.fragments.BallQIndexPageFragment;
import com.tysci.ballq.fragments.BallQMatchFragment;
import com.tysci.ballq.fragments.BallQPersonalFragment;
import com.tysci.ballq.fragments.BallQTipOffFragment;
import com.tysci.ballq.views.widgets.MainBottomMenuView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by HTT on 2016/7/12.
 */
public class BallQMainActivity extends BaseActivity {
    @Bind(R.id.menu_home)
    protected MainBottomMenuView menuHome;
    @Bind(R.id.menu_match)
    protected MainBottomMenuView menuMatch;
    @Bind(R.id.menu_tip_off)
    protected MainBottomMenuView menuTipOff;
    @Bind(R.id.menu_find)
    protected MainBottomMenuView menuFind;
    @Bind(R.id.menu_my)
    protected MainBottomMenuView menuMy;

    protected BallQIndexPageFragment homePageFragment;
    protected BallQMatchFragment matchFragment;
    protected BallQTipOffFragment tipOffFragment;
    protected BallQFindFragment findFragment;
    protected BallQPersonalFragment personalFragment;

    private int currentTab;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_ballq_main;
    }

    @Override
    protected void initViews() {
        setSelectedTab(R.id.menu_home);
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void getIntentData(Intent intent) {

    }

    public void setSelectedTab(int id) {
        if (id == R.id.menu_home) {
            menuHome.setMenuChecked(true);
            menuMatch.setMenuChecked(false);
            menuTipOff.setMenuChecked(false);
            menuFind.setMenuChecked(false);
            menuMy.setMenuChecked(false);
        } else if (id == R.id.menu_match) {
            menuHome.setMenuChecked(false);
            menuMatch.setMenuChecked(true);
            menuTipOff.setMenuChecked(false);
            menuFind.setMenuChecked(false);
            menuMy.setMenuChecked(false);
        } else if (id == R.id.menu_tip_off) {
            menuHome.setMenuChecked(false);
            menuMatch.setMenuChecked(false);
            menuTipOff.setMenuChecked(true);
            menuFind.setMenuChecked(false);
            menuMy.setMenuChecked(false);
        } else if (id == R.id.menu_find) {
            menuHome.setMenuChecked(false);
            menuMatch.setMenuChecked(false);
            menuTipOff.setMenuChecked(false);
            menuFind.setMenuChecked(true);
            menuMy.setMenuChecked(false);
        } else if (id == R.id.menu_my) {
            menuHome.setMenuChecked(false);
            menuMatch.setMenuChecked(false);
            menuTipOff.setMenuChecked(false);
            menuFind.setMenuChecked(false);
            menuMy.setMenuChecked(true);
        }
        setTabPager(id);
    }


    private void hideFragments(int tab, FragmentTransaction transaction) {
        if (tab == R.id.menu_home) {
            if (homePageFragment != null) {
                transaction.hide(homePageFragment);
            }
        } else if (tab == R.id.menu_match) {
            if (matchFragment != null) {
                transaction.hide(matchFragment);
            }
        } else if (tab == R.id.menu_tip_off) {
            if (tipOffFragment != null) {
                transaction.hide(tipOffFragment);
            }
        } else if (tab == R.id.menu_find) {
            if (findFragment != null) {
                transaction.hide(findFragment);
            }
        } else if (tab == R.id.menu_my) {
            if (personalFragment != null) {
                transaction.hide(personalFragment);
            }
        }
    }

    private void setTabPager(int tab) {
        FragmentTransaction transaction = null;
        if (tab != currentTab) {
            //setSelectedTab(tab);
            transaction = this.getSupportFragmentManager().beginTransaction();
            hideFragments(currentTab, transaction);
        } else {
            return;
        }
        switch (tab) {
            case R.id.menu_home:
                if (homePageFragment == null) {
                    homePageFragment = new BallQIndexPageFragment();
                    transaction.add(R.id.layout_container, homePageFragment);
                } else {
                    transaction.show(homePageFragment);
                }
                break;
            case R.id.menu_match:
                if (matchFragment == null) {
                    matchFragment = new BallQMatchFragment();
                    transaction.add(R.id.layout_container, matchFragment);
                } else {
                    transaction.show(matchFragment);
                }
                break;
            case R.id.menu_tip_off:
                if (tipOffFragment == null) {
                    tipOffFragment = new BallQTipOffFragment();
                    // matchFragment.setUrl(url);
                    transaction.add(R.id.layout_container, tipOffFragment);
                } else {
                    transaction.show(tipOffFragment);
                }
                break;
            case R.id.menu_find:
                if (findFragment == null) {
                    findFragment = new BallQFindFragment();
                    transaction.add(R.id.layout_container, findFragment);
                } else {
                    transaction.show(findFragment);
                }
                break;
            case R.id.menu_my:
                if (personalFragment == null) {
                    personalFragment = new BallQPersonalFragment();
                    transaction.add(R.id.layout_container, personalFragment);
                } else {
                    transaction.show(personalFragment);
                }
                break;
        }
        currentTab = tab;
        transaction.commitAllowingStateLoss();
    }

    @OnClick({R.id.menu_home, R.id.menu_match, R.id.menu_tip_off, R.id.menu_find, R.id.menu_my})
    protected void onClickMenuItem(View view) {
        setSelectedTab(view.getId());
    }


    @Override
    protected boolean isCanceledEventBus() {
        return false;
    }

    @Override
    protected void saveInstanceState(Bundle outState) {

    }

    @Override
    protected void handleInstanceState(Bundle outState) {

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
}
