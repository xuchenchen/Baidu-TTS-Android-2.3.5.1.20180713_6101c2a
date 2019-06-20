package com.baidu.tts.ryxlib.listener;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.baidu.tts.client.SpeechError;

/**
 * 在 MessageListener的基础上，和UI配合。
 * Created by fujiayi on 2017/9/14.
 */

public class UiMessageListener extends MessageListener {

    private Handler mainHandler;

    private static final String TAG = "UiMessageListener";

    public UiMessageListener(Handler mainHandler) {
        super();
        this.mainHandler = mainHandler;
    }

    @Override
    public void onSynthesizeStart(String s) {

    }

    /**
     * 合成数据和进度的回调接口，分多次回调。
     * 注意：progress表示进度，与播放到哪个字无关
     * @param utteranceId
     * @param data 合成的音频数据。该音频数据是采样率为16K，2字节精度，单声道的pcm数据。
     * @param progress 文本按字符划分的进度，比如:你好啊 进度是0-3
     */
    @Override
    public void onSynthesizeDataArrived(String utteranceId, byte[] data, int progress) {
        // sendMessage("onSynthesizeDataArrived");
//        mainHandler.sendMessage(mainHandler.obtainMessage(UI_CHANGE_SYNTHES_TEXT_SELECTION, progress, 0));
    }

    @Override
    public void onSynthesizeFinish(String s) {

    }

    @Override
    public void onSpeechStart(String s) {

    }

    /**
     * 播放进度回调接口，分多次回调
     * 注意：progress表示进度，与播放到哪个字无关
     *
     * @param utteranceId
     * @param progress 文本按字符划分的进度，比如:你好啊 进度是0-3
     */
    @Override
    public void onSpeechProgressChanged(String utteranceId, int progress) {
        // sendMessage("onSpeechProgressChanged");
//        mainHandler.sendMessage(mainHandler.obtainMessage(UI_CHANGE_INPUT_TEXT_SELECTION, progress, 0));
    }


    @Override
    public void onSpeechFinish(String utteranceId) {
        sendMessage(SPEECHFINISH,utteranceId);
    }

    @Override
    public void onError(String s, SpeechError speechError) {
        sendMessage(SPEEERROR,s);
    }

    protected void sendMessage( int action,String message) {
        if (mainHandler != null) {
            Message msg = Message.obtain();
            msg.what = action;
            msg.obj = message;
            mainHandler.sendMessage(msg);
        }
    }
}
