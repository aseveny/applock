package com.app.lock.module.pwd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.lock.R;
import com.app.lock.base.BaseActivity;
import com.app.lock.base.AppConstants;
import com.app.lock.bean.LockStage;
import com.app.lock.module.main.MainActivity;
import com.app.lock.mvp.contract.GestureCreateContract;
import com.app.lock.mvp.p.GestureCreatePresenter;
import com.app.lock.service.LockService;
import com.app.lock.utils.LockPatternUtils;
import com.app.lock.utils.SpUtil;
import com.app.lock.utils.SystemBarHelper;
import com.app.lock.widget.LockPatternView;
import com.app.lock.widget.LockPatternViewPattern;

import java.util.List;

import static com.app.lock.bean.LockStage.NeedToConfirm;

/**
 *  Created by seven on 2017/12/19
 */

public class CreatePwdActivity extends BaseActivity implements View.OnClickListener,
        GestureCreateContract.View {
    private int count=0;
    private TextView mLockTip;
    private LockPatternView mLockPatternView;
   // private TextView mBtnReset;
    private Button reset_actoin;
    private Button mCancle;
    //图案锁相关
    private LockStage mUiStage = LockStage.Introduction;
    protected List<LockPatternView.Cell> mChosenPattern = null; //密码
    private LockPatternUtils mLockPatternUtils;
    private LockPatternViewPattern mPatternViewPattern;
    private GestureCreatePresenter mGestureCreatePresenter;
    //private RelativeLayout mTopLayout;


    @Override
    public int getLayoutId() {
        return R.layout.activity_create_pwd;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mCancle=(Button)findViewById(R.id.cancel_action);
        reset_actoin=(Button)findViewById(R.id.reset_actoin);
        mLockPatternView = (LockPatternView) findViewById(R.id.lock_pattern_view);
        mLockTip = (TextView) findViewById(R.id.lock_tip);
        //mBtnReset = (TextView) findViewById(R.id.btn_reset);
       // mTopLayout = (RelativeLayout) findViewById(R.id.top_layout);
        //mTopLayout.setPadding(0, SystemBarHelper.getStatusBarHeight(this),0,0);

    }

    @Override
    protected void initData() {
        mGestureCreatePresenter = new GestureCreatePresenter(this, this);
        initLockPatternView();
    }

    /**
     * 初始化锁屏控件
     */
    private void initLockPatternView() {
        mLockPatternUtils = new LockPatternUtils(this);
        mPatternViewPattern = new LockPatternViewPattern(mLockPatternView);
        mPatternViewPattern.setPatternListener(new LockPatternViewPattern.onPatternListener() {
            @Override
            public void onPatternDetected(List<LockPatternView.Cell> pattern) {
                mGestureCreatePresenter.onPatternDetected(pattern, mChosenPattern, mUiStage);

            }
        });
        mLockPatternView.setOnPatternListener(mPatternViewPattern);
        mLockPatternView.setTactileFeedbackEnabled(true);
        //confum.setClickable(true);
    }

    @Override
    protected void initAction() {
        //mBtnReset.setOnClickListener(this);
        mCancle.setOnClickListener(this);
        reset_actoin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reset_actoin:
                setStepOne();
                //NeedToConfirm
                break;
            case R.id.cancel_action:
                finish();
                break;
            //case R.id.reset_actoin:
            //    setStepOne();

        }
    }

    /**
     * 恢复到第一步
     */
    private void setStepOne() {
        mGestureCreatePresenter.updateStage(LockStage.Introduction);
        mLockTip.setText(getString(R.string.lock_recording_intro_header));
    }

    private void setContinue(){
        mGestureCreatePresenter.updateStage(LockStage.NeedToConfirm);
    }
    private void gotoLockMainActivity() {
        SpUtil.getInstance().putBoolean(AppConstants.LOCK_STATE, true); //开启应用锁开关
        startService(new Intent(this, LockService.class));
        SpUtil.getInstance().putBoolean(AppConstants.LOCK_IS_FIRST_LOCK, false);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    /**
     * 更新当前锁的状态
     */
    @Override
    public void updateUiStage(LockStage stage) {
        mUiStage = stage;
    }

    /**
     * 更新当前密码
     */
    @Override
    public void updateChosenPattern(List<LockPatternView.Cell> mChosenPattern) {
        this.mChosenPattern = mChosenPattern;
    }

    /**
     * 更新提示信息
     */
    @Override
    public void updateLockTip(String text, boolean isToast) {
        mLockTip.setText(text);
    }

    /**
     * 更新提示信息
     */
    @Override
    public void setHeaderMessage(int headerMessage) {
        mLockTip.setText(headerMessage);
    }

    /**
     * LockPatternView的一些配置
     */
    @Override
    public void lockPatternViewConfiguration(boolean patternEnabled, LockPatternView.DisplayMode displayMode) {
        if (patternEnabled) {
            mLockPatternView.enableInput();
        } else {
            mLockPatternView.disableInput();
        }
        mLockPatternView.setDisplayMode(displayMode);
    }

    /**
     * 初始化
     */
    @Override
    public void Introduction() {
        clearPattern();
    }

    @Override
    public void HelpScreen() {

    }

    /**
     * 路径太短
     */
    @Override
    public void ChoiceTooShort() {
        mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);  //路径太短
        mLockPatternView.removeCallbacks(mClearPatternRunnable);
        mLockPatternView.postDelayed(mClearPatternRunnable, 500);
    }

    private Runnable mClearPatternRunnable = new Runnable() {
        public void run() {
            mLockPatternView.clearPattern();
        }
    };

    /**
     * 画完第一步转到第二步
     */
    @Override
    public void moveToStatusTwo() {

    }

    /**
     * 清空控件路径
     */
    @Override
    public void clearPattern() {
        mLockPatternView.clearPattern();
    }

    /**
     * 第一次和第二次画得不一样
     */
    @Override
    public void ConfirmWrong() {
        mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);  //路径太短
        mLockPatternView.removeCallbacks(mClearPatternRunnable);
        mLockPatternView.postDelayed(mClearPatternRunnable, 500);
    }

    /**
     * 画成功了
     */
    @Override
    public void ChoiceConfirmed() {
        mLockPatternUtils.saveLockPattern(mChosenPattern); //保存密码
        clearPattern();
        gotoLockMainActivity();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGestureCreatePresenter.onDestroy();
    }
}
