package com.app.lock.module.splash;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ImageView;

import com.app.lock.R;
import com.app.lock.module.lock.GestureSelfUnlockActivity;
import com.app.lock.base.BaseActivity;
import com.app.lock.base.AppConstants;
import com.app.lock.module.pwd.CreatePwdActivity;
import com.app.lock.service.LoadAppListService;
import com.app.lock.service.LockService;
import com.app.lock.utils.AppUtils;
import com.app.lock.utils.LockUtil;
import com.app.lock.utils.PermissionUtils;
import com.app.lock.utils.SpUtil;
import com.app.lock.utils.ToastUtil;
import com.app.lock.widget.DialogPermission;

/**
 *  Created by seven on 2017/12/19
 */

public class SplashActivity extends BaseActivity {
    private String[] permissionArray = new String[]{
            Manifest.permission.PACKAGE_USAGE_STATS,
            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.RECEIVE_BOOT_COMPLETED,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
            Manifest.permission.CAMERA
    };
    //private ImageView mImgSplash;
    private ObjectAnimator animator;
    private int RESULT_ACTION_USAGE_ACCESS_SETTINGS = 1;
    private int RESULT_ACTION_MANAGE_OVERLAY_PERMISSION = 2;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        AppUtils.hideStatusBar(getWindow(), true);
        //mImgSplash = (ImageView) findViewById(R.id.img_splash);
    }

    @Override
    protected void initData() {
        startService(new Intent(this, LoadAppListService.class));
        if (SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE, false)) {
            startService(new Intent(this, LockService.class));
        }

        boolean isFirstLock = SpUtil.getInstance().getBoolean(AppConstants.LOCK_IS_FIRST_LOCK, true);
        if (isFirstLock) { //如果第一次
            showDialog();
        } else {
            Intent intent = new Intent(SplashActivity.this, GestureSelfUnlockActivity.class);
            intent.putExtra(AppConstants.LOCK_PACKAGE_NAME, AppConstants.APP_PACKAGE_NAME); //传自己的包名
            intent.putExtra(AppConstants.LOCK_FROM, AppConstants.LOCK_FROM_LOCK_MAIN_ACITVITY);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
   /*     animator = ObjectAnimator.ofFloat(mImgSplash, "alpha", 0.5f, 1);
        animator.setDuration(1500);
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

            }
        });*/
    }

    /**
     * 弹出dialog
     */
    private void showDialog() {
        //如果没有获得查看使用情况权限和手机存在查看使用情况这个界面
        if (!LockUtil.isStatAccessPermissionSet(SplashActivity.this) && LockUtil.isNoOption(SplashActivity.this)) {
           DialogPermission dialog = new DialogPermission(SplashActivity.this);
            dialog.show();
            dialog.setOnClickListener(new DialogPermission.onClickListener() {
                @Override
                public void onClick() {
                    Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                    startActivityForResult(intent, RESULT_ACTION_USAGE_ACCESS_SETTINGS);
                }
            });
            //PermissionUtils.checkPermissionArray(SplashActivity.this, permissionArray, 2);
            //gotoCreatePwdActivity();
        } else {
            gotoCreatePwdActivity();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_ACTION_USAGE_ACCESS_SETTINGS) {
            if (LockUtil.isStatAccessPermissionSet(SplashActivity.this)) {
                gotoCreatePwdActivity();
            } else {
                ToastUtil.showToast("no permission");
                finish();
            }
        }
    }

    private void gotoCreatePwdActivity() {
        Intent intent = new Intent(SplashActivity.this, CreatePwdActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void initAction() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        animator = null;
    }
}
