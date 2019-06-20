package com.baidu.tts.ryxlib;

import android.content.Context;

public class BaiduYuyinConfig {


    public static void init(Context context, String appId, String appKey, String secretKey, boolean isDebug) {
        BaiduYuyinData baiduYuyinData = BaiduYuyinData.getInstance(context);
        baiduYuyinData.setAppId(appId);
        baiduYuyinData.setAppKey(appKey);
        baiduYuyinData.setSecretKey(secretKey);
        baiduYuyinData.isdebug = isDebug;
    }


}
