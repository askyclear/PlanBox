package com.example.korea.planner.util;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;

/**
 * Created by korea on 2017-05-01.
 */

public class WakeLockUtil {
    private static PowerManager.WakeLock mCpuWakeLock;
    private static KeyguardManager.KeyguardLock keyguardLock;
    private static boolean isScrennLock;

    static void acquireCpuWakeLock(Context context) {
        if (mCpuWakeLock != null) {
            return;
        }
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mCpuWakeLock = pm.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK, "ALARM");
        mCpuWakeLock.acquire();
    }

    static void releaseCpuLock() {
        if (mCpuWakeLock != null) {
            mCpuWakeLock.release();
            mCpuWakeLock = null;
        }
    }
}
