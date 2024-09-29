package cn.jarlen.android.aidl.demo;

/**
 * @author jarlen
 * Create by 2024/9/29 14:34
 */
public class ThreadUtils {

    public static String getThreadPrint() {
        Thread thread = Thread.currentThread();
        return (thread.getName().equals("main") ? "当前线程是主线程" : "当前线程是子线程") + "线程名称为：" + thread.getName();
    }
}
