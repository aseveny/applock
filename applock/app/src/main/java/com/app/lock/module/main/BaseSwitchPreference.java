package com.app.lock.module.main;

import android.content.Context;
import android.os.Build;
import android.preference.SwitchPreference;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.app.lock.R;

/**
 * Created by Seven on 2017/12/21.
 */

public class BaseSwitchPreference extends SwitchPreference {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BaseSwitchPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public BaseSwitchPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BaseSwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseSwitchPreference(Context context) {
        super(context);
    }

/*    @Override
    public int getLayoutResource() {
        return R.layout.fragment_app_list;
    }*/

    @Override
    protected View onCreateView(ViewGroup parent) {
        return super.onCreateView(parent);
    }
}
