package cn.jarlen.android.aidl.util;

public class Utils {

    public static String getThreadPrint() {
        Thread thread = Thread.currentThread();
        return thread.toString();
    }

    public static boolean isMainThread() {
        return Thread.currentThread().getName().equals("main");
    }
}
