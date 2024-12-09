package cn.jarlen.android.aidl.util;

import android.util.Log;

public class AIDLLog {

    public static final String TAG = "AIDL2024";

    public static void w(String msg) {
        Log.w(TAG, "[" + Utils.getThreadPrint() + "]" + msg);
    }

    public static void e(String msg) {
        Log.e(TAG, "[" + Utils.getThreadPrint() + "]" + msg);
    }
}
