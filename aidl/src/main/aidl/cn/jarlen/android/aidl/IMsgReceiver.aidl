// IMsgReceiver.aidl
package cn.jarlen.android.aidl;

// Declare any non-default types here with import statements

import cn.jarlen.android.aidl.Message;

interface IMsgReceiver {
    void onReceiver(in Message msg);
}