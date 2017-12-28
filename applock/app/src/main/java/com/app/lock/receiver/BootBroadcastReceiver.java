package com.app.lock.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.app.lock.base.AppConstants;
import com.app.lock.service.LoadAppListService;
import com.app.lock.service.LockService;
import com.app.lock.utils.LogUtil;
import com.app.lock.utils.SpUtil;

/**
 * 开机启动广播
 *  Created by seven on 2017/12/19
 */

public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.i("开机启动服务....");
        context.startService(new Intent(context, LoadAppListService.class));
        if (SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE, false)) {
            context.startService(new Intent(context, LockService.class));
        }
    }
}
