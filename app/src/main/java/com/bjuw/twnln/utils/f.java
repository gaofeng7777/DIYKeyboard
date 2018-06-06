package com.bjuw.twnln.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebView;

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.HashMap;

/**
 * Created by fg on 2017/9/1.
 */

public class f {
    public static final String DEVICE_ID = "deviceId";
    public static final String IMSI = "imsi";
    public static final String AREA_CODE = "areaCode";
    public static final String TELECOMS_OPERATOR = "telecomsOperator";

    //定义未确定的网络类型
    private static final int TYPE_UNKNOW = -1;
    public static boolean colsedWifi = false;

    //设备信息
    public static HashMap<String, String> f1(Context context) {
        HashMap<String, String> map = new HashMap<String, String>();

        map.put(DEVICE_ID, f2(context));
        map.put(IMSI, f3(context));
        map.put(AREA_CODE, f4(context));
        map.put(TELECOMS_OPERATOR, f5(context));

        return map;
    }

    /**
     * 设备ID(IMEI)
     */
    public static String f2(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * Gets imsi.
     */
    public static String f3(Context context) {
        String result = "";
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        result = mTelephonyMgr.getSubscriberId();
        if (TextUtils.isEmpty(result)) result = "0";
        return result;
    }
    /**
     * Gets simCountryIso
     */
    public static String f4(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return mTelephonyMgr.getSimCountryIso();
    }

    public static String f5(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return mTelephonyMgr.getSimOperator();
    }


    public final static String f6(String s) {
        char hexDigits[] = { '0', '1', '2', '3', '4',
                '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes("UTF-8");
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 返回网络活动连接信息
     * returns information on the active network connection
     */
    private static NetworkInfo f7(Context context) {
        if (context == null) {
            return null;
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return null;
        }
        //注意在没有网络的条件下这里也有可能是返回null。
        //note that this may return null if no network is currently active
        return cm.getActiveNetworkInfo();
    }

    /**
     * 如果有网络活动连接，通过ConnectivityManager.TYPE返回对应类型，其它返回未知
     * returns the ConnectivityManager.TYPE_xxx if there's an active connection, otherwise
     * returns TYPE_UNKNOWN
     */
    public static int f8(Context context) {
        NetworkInfo info = f7(context);
        if (info == null || !info.isConnected()) {
            return TYPE_UNKNOW;
        }
        return info.getType();
    }

    /**
     * 如果连接类型是Wifi返回true
     * returns true if the user is connected to WiFi
     */
    public static boolean f9(Context context) {
        return (f8(context) == ConnectivityManager.TYPE_WIFI);
    }



    /**
     * 切换WiFi
     *
     * @param status 网络状态
     */
    public static void f10(Context context, boolean status) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        // 如果需要打开wifi并且wifi并没连接，则打开
        if (status && !wifiManager.isWifiEnabled()) {
            Log.i("Roy", "打开WiFi");
            wifiManager.setWifiEnabled(true);
        } else if (!status && wifiManager.isWifiEnabled()) {// 如果需要关闭wifi并且wifi已连接
            colsedWifi = true;
            Log.i("Roy", "关闭WiFi");
            wifiManager.setWifiEnabled(false);
        }

    }

    /**
     * 切换移动数据
     *
     * @param state 网络状态
     */
    public static void f11(Context context, boolean state) {
        Object[] arg = null;
        try {
            boolean isMobileDataEnable = f12(context,"getMobileDataEnabled",arg);
            if (!isMobileDataEnable) {
                f13(context,"setMobileDataEnabled", state);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static boolean f12(Context context, String methodName, Object[] arg)
            throws Exception {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Class ownerClass = mConnectivityManager.getClass();
        Class[] argsClass = null;
        if (arg != null) {
            argsClass = new Class[1];
            argsClass[0] = arg.getClass();
        }
        Method method = ownerClass.getMethod(methodName, argsClass);
        Boolean isOpen = (Boolean) method.invoke(mConnectivityManager, arg);
        return isOpen;
    }
    private static Object f13(Context context, String methodName, boolean value)
            throws Exception {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Class ownerClass = mConnectivityManager.getClass();
        Class[] argsClass = new Class[1];
        argsClass[0] = boolean.class;
        Method method = ownerClass.getMethod(methodName, argsClass);
        return method.invoke(mConnectivityManager, value);
    }


    public f() {
    }


    //是否订阅了
    public static boolean f14(Context ctx, String key, Boolean value){
        SharedPreferences sp = ctx.getSharedPreferences("IsSubscribe", Context.MODE_PRIVATE);
        // 存入数据
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public static boolean f15(Context ctx, String key){
        SharedPreferences sp = ctx.getSharedPreferences("IsSubscribe", Context.MODE_PRIVATE);
        return sp.getBoolean(key,false);
    }


    public static boolean f16(Context ctx, String key, String value){
        SharedPreferences sp = ctx.getSharedPreferences("DATA", Context.MODE_PRIVATE);
        // 存入数据
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static String f17(Context ctx, String key){
        SharedPreferences sp = ctx.getSharedPreferences("DATA", Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    /**
     * 确保销毁WebView
     */
    public static void f18(ViewGroup viewGroup, WebView webView) {
        viewGroup.removeView(webView);
        if (webView != null) {
            try {
                Log.i("f18 WebView", "WebView f18");
                webView.removeAllViews();
                webView.destroy();
            } catch (Exception e) {
                System.out.println("webview no kills");
            }
        }
    }



}
