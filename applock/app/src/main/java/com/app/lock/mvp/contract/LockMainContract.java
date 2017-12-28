package com.app.lock.mvp.contract;

import android.content.Context;

import com.app.lock.base.BasePresenter;
import com.app.lock.base.BaseView;
import com.app.lock.bean.CommLockInfo;
import com.app.lock.mvp.p.LockMainPresenter;

import java.util.List;

/**
 *  Created by seven on 2017/12/19
 */

public interface LockMainContract {
    interface View extends BaseView<Presenter> {

        void loadAppInfoSuccess(List<CommLockInfo> list);
    }

    interface Presenter extends BasePresenter {
        void loadAppInfo(Context context);

        void searchAppInfo(String search, LockMainPresenter.ISearchResultListener listener);

        void onDestroy();
    }
}
