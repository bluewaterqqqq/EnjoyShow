
package com.zmdx.enjoyshow.utils;

import android.content.Context;
import android.os.Build;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Proxy;

public class AndroidNetProxyCompat {

    private static Method sGetProxyMethod;

    static {
        try {
            Class<?>[] arrayOfClass = new Class[] {
                    Context.class, String.class
            };
            sGetProxyMethod = android.net.Proxy.class.getMethod("getProxy", arrayOfClass);
        } catch (NoSuchMethodException e) {
            sGetProxyMethod = null;
//            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    public static String getHost(Context context, String url) {
        if (Build.VERSION.SDK_INT < 16) {
            return android.net.Proxy.getHost(context);
        } else if (Build.VERSION.SDK_INT >= 17) {
            return android.net.Proxy.getDefaultHost();
        } else {
            Method getProxyMethod = sGetProxyMethod;
            if (getProxyMethod != null) {
                try {
                    Object[] arrayOfObject = new Object[] {
                            context, url
                    };
                    Proxy proxy = (Proxy) getProxyMethod.invoke(null, arrayOfObject);
                    if (proxy == null || proxy == Proxy.NO_PROXY) {
                        return null;
                    } else {
                        InetSocketAddress address = (InetSocketAddress) proxy.address();
                        return address.getHostName();
                    }
                } catch (IllegalAccessException e) {
                    // ignore this, will to the final
                } catch (InvocationTargetException e) {
                    // ignore this, will to the final
                }
            }
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    public static int getPort(Context context, String url) {
        if (Build.VERSION.SDK_INT < 16) {
            return android.net.Proxy.getPort(context);
        } else if (Build.VERSION.SDK_INT >= 17) {
            return android.net.Proxy.getDefaultPort();
        } else {
            Method getProxyMethod = sGetProxyMethod;
            if (getProxyMethod != null) {
                Object[] arrayOfObject = new Object[] {
                        context, url
                };
                try {
                    Proxy proxy = (Proxy) getProxyMethod.invoke(null, arrayOfObject);
                    if (proxy == null || proxy == Proxy.NO_PROXY) {
                        return -1;
                    } else {
                        InetSocketAddress address = (InetSocketAddress) proxy.address();
                        return address.getPort();
                    }
                } catch (IllegalAccessException e) {
                    // ignore this, will to the final
                } catch (InvocationTargetException e) {
                    // ignore this, will to the final
                }
            }
        }
        return -1;
    }
}
