// ILoginCallBack.aidl
package cn.jarlen.android.aidl;

// Declare any non-default types here with import statements

import cn.jarlen.android.aidl.User;

interface ILoginCallBack {

    void onCallBack(int ret,in User user);
}