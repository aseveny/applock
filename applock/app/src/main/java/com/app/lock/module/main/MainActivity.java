package com.app.lock.module.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.lock.R;
import com.app.lock.base.BaseActivity;
import com.app.lock.bean.CommLockInfo;
import com.app.lock.module.setting.LockSettingActivity;
import com.app.lock.mvp.contract.LockMainContract;
import com.app.lock.mvp.p.LockMainPresenter;
import com.app.lock.utils.SystemBarHelper;
import com.app.lock.widget.DialogSearch;

import java.util.ArrayList;
import java.util.List;

/**
 *  Created by seven on 2017/12/19
 */

public class MainActivity extends BaseActivity implements LockMainContract.View, View.OnClickListener {

    private RelativeLayout mTopLayout;
    private ImageView mBtnSetting;
    private TextView mEditSearch;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private CommentPagerAdapter mPagerAdapter;
    private LockMainPresenter mLockMainPresenter;
    private DialogSearch mDialogSearch;
    private ImageView mBack;

    private List<String> titles ;
    private List<Fragment> fragmentList ;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mBack=(ImageView)findViewById(R.id.btn_back) ;
        mBtnSetting = (ImageView) findViewById(R.id.btn_setting);
        mEditSearch = (TextView) findViewById(R.id.edit_search);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mTopLayout = (RelativeLayout) findViewById(R.id.top_layout);
        mTopLayout.setPadding(0, SystemBarHelper.getStatusBarHeight(this), 0, 0);

        mLockMainPresenter = new LockMainPresenter(this, this);
        mLockMainPresenter.loadAppInfo(this);
    }

    @Override
    protected void initData() {
        mDialogSearch = new DialogSearch(this);
    }

    @Override
    protected void initAction() {
        mBtnSetting.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mEditSearch.setOnClickListener(this);
        mDialogSearch.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                mLockMainPresenter.loadAppInfo(MainActivity.this);
            }
        });
    }

    @Override
    public void loadAppInfoSuccess(List<CommLockInfo> list) {
        int sysNum = 0;
        int userNum = 0;
        for (CommLockInfo info : list) {
            if (info.isSysApp()) {
                sysNum++;
            } else {
                userNum++;
            }
        }
        titles = new ArrayList<>();
        titles.add("Recommended" + " (" + sysNum + ")");
        titles.add("installed" + " (" + userNum + ")");
        SysAppFragment sysAppFragment = SysAppFragment.newInstance(list);
        UserAppFragment userAppFragment = UserAppFragment.newInstance(list);
        fragmentList = new ArrayList<>();
        fragmentList.add(sysAppFragment);
        fragmentList.add(userAppFragment);
        mPagerAdapter = new CommentPagerAdapter(getSupportFragmentManager(), fragmentList, titles);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_setting:
                startActivity(new Intent(this, LockSettingActivity.class));
                break;
            case R.id.edit_search:
                mDialogSearch.show();
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }

    public class CommentPagerAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> fragmentList = new ArrayList<>();
        private List<String> titles = new ArrayList<>();


        public CommentPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titles) {
            super(fm);
            this.fragmentList = fragmentList;
            this.titles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        @Override
        public int getCount() {
            return titles.size();
        }
    }

}
