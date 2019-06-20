package com.baidu.tts.ryxlib;

import android.content.Context;

import com.baidu.tts.client.TtsMode;
import com.baidu.tts.ryxlib.util.PreferenceUtil;

public class BaiduYuyinData {

    /**
     * 发布时请替换成自己申请的appId appKey 和 secretKey。注意如果需要离线合成功能,请在您申请的应用中填写包名。
     *
     */
    private String appId = "";

    private String appKey = "";

    private String secretKey = "";

    private static Context mContext;
    // TtsMode.MIX; 离在线融合，在线优先； TtsMode.ONLINE 纯在线； 没有纯离线
    protected TtsMode ttsMode = TtsMode.ONLINE;
    private static BaiduYuyinData baiduYuyinData;
    public boolean isdebug=false;

    public static BaiduYuyinData getInstance(Context context){
        mContext = context;
        if(baiduYuyinData==null){
            baiduYuyinData = new BaiduYuyinData();
        }
        return baiduYuyinData;
    }

    public String getAppId() {
        appId = PreferenceUtil.getInstance(mContext).getString("yuyin_appId","");
        return appId;
    }

    public String getAppKey() {
        appKey = PreferenceUtil.getInstance(mContext).getString("yuyin_appKey","");
        return appKey;
    }

    public String getSecretKey() {
        secretKey = PreferenceUtil.getInstance(mContext).getString("yuyin_secretKey","");
        return secretKey;
    }


    public void setAppId(String appId){
        PreferenceUtil.getInstance(mContext).saveString("yuyin_appId",appId);
    }

    public void setAppKey(String appKey) {
        PreferenceUtil.getInstance(mContext).saveString("yuyin_appKey",appKey);
    }

    public void setSecretKey(String secretKey) {
        PreferenceUtil.getInstance(mContext).saveString("yuyin_secretKey",secretKey);
    }
}
