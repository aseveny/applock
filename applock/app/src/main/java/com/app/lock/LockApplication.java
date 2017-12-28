package com.app.lock;

import com.app.lock.module.lock.GestureUnlockActivity;
import com.app.lock.base.BaseActivity;
import com.app.lock.utils.SpUtil;

import org.litepal.LitePalApplication;

import java.util.ArrayList;
import java.util.List;

/**
 *  Created by seven on 2017/12/19
 */

public class LockApplication extends LitePalApplication {

    private static LockApplication application;
    private static List<BaseActivity> activityList; //acticity管理

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        SpUtil.getInstance().init(application);
        activityList = new ArrayList<>();
    }

    public static LockApplication getInstance() {
        return application;
    }

    public void doForCreate(BaseActivity activity) {
        activityList.add(activity);
    }

    public void doForFinish(BaseActivity activity) {
        activityList.remove(activity);
    }

    public void clearAllActivity() {
        try {
            for (BaseActivity activity : activityList) {
                if (activity != null && !clearAllWhiteList(activity))
                    activity.clear();
            }
            activityList.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean clearAllWhiteList(BaseActivity activity) {
        return activity instanceof GestureUnlockActivity;
    }



}
