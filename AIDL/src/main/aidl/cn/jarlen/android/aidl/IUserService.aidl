
package cn.jarlen.android.aidl;

// Declare any non-default types here with import statements
import cn.jarlen.android.aidl.User;

import cn.jarlen.android.aidl.ILoginCallBack;

interface IUserService {

    void onLogin(String name,String pwd,ILoginCallBack cb);

    User getUser(String name);
}