package com.ckj.customtimer;

import android.os.SystemClock;

/**
 * Created by chenkaijian on 16-8-1.
 */
public class Utils {
    public static long getTimeNow() {
        return SystemClock.elapsedRealtime();
    }

}
