package com.app.lock.module.lock;

import android.content.Intent;
import android.os.Bundle;
import android.view.TextureView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.lock.R;
import com.app.lock.base.AppConstants;
import com.app.lock.base.BaseActivity;
import com.app.lock.db.CommLockInfoManager;
import com.app.lock.module.main.MainActivity;
import com.app.lock.module.setting.LockSettingActivity;
import com.app.lock.utils.LockPatternUtils;
import com.app.lock.utils.SpUtil;
import com.app.lock.utils.SystemBarHelper;
import com.app.lock.widget.LockPatternView;
import com.app.lock.widget.LockPatternViewPattern;

import java.util.List;

/**
 * Created by seven on 2017/12/19
 */

public class GestureSelfUnlockActivity extends BaseActivity {

    private LockPatternView mLockPatternView;
    private LockPatternUtils mLockPatternUtils;
    private LockPatternViewPattern mPatternViewPattern;
    private int mFailedPatternAttemptsSinceLastTimeout = 0;
    private String actionFrom;//按返回键的操作
    private String pkgName; //解锁应用的包名
    private CommLockInfoManager mManager;
    private TextView wrong_passwd;


    @Override
    public int getLayoutId() {
        return R.layout.activity_gesture_self_unlock;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mLockPatternView = (LockPatternView) findViewById(R.id.unlock_lock_view);
        wrong_passwd = (TextView) findViewById(R.id.wrong_confirm);
    }

    @Override
    protected void initData() {
        mManager = new CommLockInfoManager(this);
        //获取解锁应用的包名
        pkgName = getIntent().getStringExtra(AppConstants.LOCK_PACKAGE_NAME);
        //获取按返回键的操作
        actionFrom = getIntent().getStringExtra(AppConstants.LOCK_FROM);

        initLockPatternView();


    }

    /**
     * 初始化解锁控件
     */
    private void initLockPatternView() {

        mLockPatternUtils = new LockPatternUtils(this);
        mPatternViewPattern = new LockPatternViewPattern(mLockPatternView);
        mPatternViewPattern.setPatternListener(new LockPatternViewPattern.onPatternListener() {
            @Override
            public void onPatternDetected(List <LockPatternView.Cell> pattern) {

                if (mLockPatternUtils.checkPattern(pattern)) { //解锁成功,更改数据库状态
                    mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Correct);
                    if (actionFrom.equals(AppConstants.LOCK_FROM_LOCK_MAIN_ACITVITY)) {
                        Intent intent = new Intent(GestureSelfUnlockActivity.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    } else if (actionFrom.equals(AppConstants.LOCK_FROM_FINISH)) {
                        mManager.unlockCommApplication(pkgName);
                        finish();
                    } else if (actionFrom.equals(AppConstants.LOCK_FROM_SETTING)) {
                        startActivity(new Intent(GestureSelfUnlockActivity.this, LockSettingActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    } else if (actionFrom.equals(AppConstants.LOCK_FROM_UNLOCK)) {
                        mManager.setIsUnLockThisApp(pkgName, true);
                        mManager.unlockCommApplication(pkgName);
                        sendBroadcast(new Intent(GestureUnlockActivity.FINISH_UNLOCK_THIS_APP));
                        finish();
                    }
                } else {
                    mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
                    wrong_passwd.setText(getResources().getString(R.string.wrong_confirm));
                    if (pattern.size() >= LockPatternUtils.MIN_PATTERN_REGISTER_FAIL) {
                        mFailedPatternAttemptsSinceLastTimeout++;
                        int retry = LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT - mFailedPatternAttemptsSinceLastTimeout;
                        if (retry >= 0) {
                        }
                    } else {
                    }
                    if (mFailedPatternAttemptsSinceLastTimeout >= 3) { //失败次数大于3次
                        if (SpUtil.getInstance().getBoolean(AppConstants.LOCK_AUTO_RECORD_PIC, false)) {

                        }
                    }
                    if (mFailedPatternAttemptsSinceLastTimeout >= LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT) { //失败次数大于阻止用户前的最大错误尝试次数

                    } else {
                        mLockPatternView.postDelayed(mClearPatternRunnable, 500);
                    }
                }
            }
        });
        mLockPatternView.setOnPatternListener(mPatternViewPattern);
        mLockPatternView.setTactileFeedbackEnabled(true);
    }

    private Runnable mClearPatternRunnable = new Runnable() {
        public void run() {
            mLockPatternView.clearPattern();
        }
    };

    @Override
    protected void initAction() {

    }


}
