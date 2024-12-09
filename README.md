# 利用android IPC AIDL技术，实现客户端与服务端通讯。

## 概述

AIDL相关使用，主要分为三个部分

- bindService
- 接口调用
- unbindService

### bindService

```java
	private IMsgService mUserService;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AIDLLog.w("Client->onServiceConnected");
            mUserService = IMsgService.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            AIDLLog.w("Client->onServiceDisconnected");
        }

        @Override
        public void onBindingDied(ComponentName name) {
            /**
             * <p>当与该连接的绑定死亡时会调用此方法。</p>
             * 这意味着接口将永远不会收到另一个连接。应用程序需要取消绑定并重新绑定该连接以重新激活它。</p>
             * 例如，如果托管服务的应用程序已更新，则可能会发生这种情况。</p>
             */
            AIDLLog.w("Client->onBindingDied");
        }

        @Override
        public void onNullBinding(ComponentName name) {
            AIDLLog.w("Client->onNullBinding");
            /**
             * <p>当服务的 onBind() 方法返回 null 时会调用此方法。这表明尝试绑定的服务连接将永远不会变得可用。</p>
             * <p>应用程序必须仍然调用 Context.unbindService(ServiceConnection) 来释放与此 ServiceConnection 相关的跟踪资源;</p>
             * <p></>即使在 Context.bindService() 调用后此回调被触发。</p>
             */
            unBindMessageService();
        }
    };
```

```java
 Intent bindIntent = new Intent();
 bindIntent.setClassName("cn.jarlen.android.aidl.server", "cn.jarlen.android.aidl.server.MessageService");
 bindService(bindIntent, connection, BIND_AUTO_CREATE);
```

### unbindService

```java
 unbindService(connection);
```

## Android AIDL调用方法活动周期

![](https://gitee.com/jarlen/AndroidAIDL/raw/master/images/Activity.png)

## Android AIDL调用方法线程切换

![](https://gitee.com/jarlen/AndroidAIDL/raw/master/images/ThreadChange.png)

> * AIDL 接口业务处理隶属单独的Binder线程池
> * 不管调用方在主线程还是子线程，在通过AIDL接口调用时，AIDL接口接收并处理数据均在Binder线程
> * 结果返回到调用方侧后又切换为原有的线程
> * AIDL接口调用是一个同步处理过程
