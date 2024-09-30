package cn.jarlen.android.aidl.demo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static String getThreadPrint() {
        Thread thread = Thread.currentThread();
        return (thread.getName().equals("main") ? "当前线程是主线程" : "当前线程是子线程") + "线程名称为：" + thread.getName();
    }

    public static boolean isMainThread() {
        return Thread.currentThread().getName().equals("main");
    }

    public static String convertTime2String(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMDD nn HH:mm:ss");
        Date date = new Date(timestamp);
        return sdf.format(date);
    }
}
