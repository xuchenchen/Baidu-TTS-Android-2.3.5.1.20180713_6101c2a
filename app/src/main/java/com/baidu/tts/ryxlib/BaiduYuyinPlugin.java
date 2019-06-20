package com.baidu.tts.ryxlib;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import android.widget.Button;

import com.baidu.tts.chainofresponsibility.logger.LoggerProxy;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.ryxlib.control.InitConfig;
import com.baidu.tts.ryxlib.control.MySyntherizer;
import com.baidu.tts.ryxlib.control.NonBlockSyntherizer;
import com.baidu.tts.ryxlib.listener.UiMessageListener;
import com.baidu.tts.ryxlib.util.AutoCheck;
import com.baidu.tts.ryxlib.util.OfflineResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.baidu.tts.ryxlib.MainHandlerConstant.INIT_SUCCESS;
import static com.baidu.tts.ryxlib.MainHandlerConstant.PRINT;
import static com.baidu.tts.ryxlib.MainHandlerConstant.SPEECHFINISH;
import static com.baidu.tts.ryxlib.MainHandlerConstant.UI_CHANGE_INPUT_TEXT_SELECTION;
import static com.baidu.tts.ryxlib.MainHandlerConstant.UI_CHANGE_SYNTHES_TEXT_SELECTION;

public class BaiduYuyinPlugin {

    public interface SpeakFinishListener{
         void successFinish(String id);
    }

    SpeakFinishListener speakFinishListener;

    // 主控制类，所有合成控制方法从这个类开始
    protected MySyntherizer synthesizer;
    protected String offlineVoice = OfflineResource.VOICE_MALE;


    public void initialTts(Context context,SpeakFinishListener speakFinishListener) {
        this.speakFinishListener=speakFinishListener;
        BaiduYuyinData baiduYuyinData = BaiduYuyinData.getInstance(context);
        LoggerProxy.printable(baiduYuyinData.isdebug); // 日志打印在logcat中
        // 设置初始化参数
        // 此处可以改为 含有您业务逻辑的SpeechSynthesizerListener的实现类
        SpeechSynthesizerListener listener = new UiMessageListener(mainHandler);

        Map<String, String> params = getParams(context);

        // appId appKey secretKey 网站上您申请的应用获取。注意使用离线合成功能的话，需要应用中填写您app的包名。包名在build.gradle中获取。
        InitConfig initConfig = new InitConfig(baiduYuyinData.getAppId(), baiduYuyinData.getAppKey(),
                baiduYuyinData.getSecretKey(), baiduYuyinData.ttsMode, params, listener);

        // 如果您集成中出错，请将下面一段代码放在和demo中相同的位置，并复制InitConfig 和 AutoCheck到您的项目中
        // 上线时请删除AutoCheck的调用
//        AutoCheck.getInstance(context).check(initConfig, new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                if (msg.what == 100) {
//                    AutoCheck autoCheck = (AutoCheck) msg.obj;
//                    synchronized (autoCheck) {
//                        String message = autoCheck.obtainDebugMessage();
//                        // Log.w("AutoCheckMessage", message);
//                    }
//                }
//            }
//
//        });
        synthesizer = new NonBlockSyntherizer(context, initConfig, mainHandler); // 此处可以改为MySyntherizer 了解调用过程
    }

    Handler  mainHandler = new Handler() {
        /*
         * @param msg
         */
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INIT_SUCCESS:

                    break;
                case SPEECHFINISH:
                    if(speakFinishListener!=null){
                        speakFinishListener.successFinish(msg.obj.toString());
                    }
                    break;
                default:
                    break;
            }
        }

    };

    /**
     * 合成的参数，可以初始化时填写，也可以在合成前设置。
     *
     * @return
     */
    protected Map<String, String> getParams(Context context) {
        Map<String, String> params = new HashMap<String, String>();
        // 以下参数均为选填
        // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
        params.put(SpeechSynthesizer.PARAM_SPEAKER, "0");
        // 设置合成的音量，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_VOLUME, "9");
        // 设置合成的语速，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_SPEED, "5");
        // 设置合成的语调，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_PITCH, "5");

        params.put(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
        // 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
        // MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线

        // 离线资源文件， 从assets目录中复制到临时目录，需要在initTTs方法前完成
        OfflineResource offlineResource = createOfflineResource(context,offlineVoice);
        // 声学模型文件路径 (离线引擎使用), 请确认下面两个文件存在
        params.put(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, offlineResource.getTextFilename());
        params.put(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE,
                offlineResource.getModelFilename());
        return params;
    }

    protected OfflineResource createOfflineResource(Context context, String voiceType) {
        OfflineResource offlineResource = null;
        try {
            offlineResource = new OfflineResource(context, voiceType);
        } catch (IOException e) {
            // IO 错误自行处理
            e.printStackTrace();
            LoggerProxy.v("baiduyuyin","【error】:copy files from assets failed." + e.getMessage());
        }
        return offlineResource;
    }

    /**
     * speak 实际上是调用 synthesize后，获取音频流，然后播放。
     * 获取音频流的方式见SaveFileActivity及FileSaveListener
     * 需要合成的文本text的长度不能超过1024个GBK字节。
     */
    public int speak(String text) {
        // 合成前可以修改参数：
        // Map<String, String> params = getParams();
        // synthesizer.setParams(params);
        int result = synthesizer.speak(text);
        if (result != 0) {
            LoggerProxy.v("baiduyuyin","error code :" + result + " method:speak , 错误码文档:http://bdyuyin.baidu.com/docs/tts/122 ");
        }
        return result;
    }

    /**
     * 批量播放
     */
    public int batchSpeak(List<Pair<String, String>> texts) {

        return synthesizer.batchSpeak(texts);
    }

    public void stop(){
        synthesizer.stop();
    }

    public void onDestroy() {
        synthesizer.release();
        LoggerProxy.v("baiduyuyin","释放资源成功");
    }
}
