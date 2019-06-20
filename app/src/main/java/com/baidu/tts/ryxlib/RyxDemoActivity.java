package com.baidu.tts.ryxlib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RyxDemoActivity extends AppCompatActivity {
    BaiduYuyinPlugin baiduYuyinPlugin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ryx_demo);

        BaiduYuyinConfig.init(this,"9954889",
                "ab5mxQ6y6z8F0cgG8aylvvqi","43b59e89027998409130319457986fee",BuildConfig.DEBUG);

        baiduYuyinPlugin= new BaiduYuyinPlugin();
        baiduYuyinPlugin.initialTts(RyxDemoActivity.this, new BaiduYuyinPlugin.SpeakFinishListener() {
            @Override
            public void successFinish(String id) {
                Toast.makeText(RyxDemoActivity.this,"id=="+id,Toast.LENGTH_SHORT).show();
            }
        });

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        baiduYuyinPlugin.speak("测试测试测试测试测试测试测试测试测试测试");
//                    }
//                });
//            }
//        }).start();

    }

    public void btnClick(View view) {
        final List<Pair<String, String>> texts1 = new ArrayList<Pair<String, String>>();
        for(int i=0;i<101;i++){
            texts1.add(new Pair<String, String>("09时27分，交易分润收益0.25元。", "201904031809746"));
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                baiduYuyinPlugin.batchSpeak(texts1);
            }
        }).start();


    }
}
