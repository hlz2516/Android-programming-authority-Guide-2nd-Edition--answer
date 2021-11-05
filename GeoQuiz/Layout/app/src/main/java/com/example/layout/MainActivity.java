package com.example.layout;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Environment;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;

import com.leon.lfilepickerlibrary.LFilePicker;

import java.io.File;
import java.util.List;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;

import static android.os.Environment.DIRECTORY_MUSIC;
import static android.os.Environment.getExternalStorageDirectory;

public class MainActivity extends Activity {

    private static int REQUESTCODE_FROM_ACTIVITY = 0;
    private static String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE},
                1);

        Button btnOpenFile = findViewById(R.id.btn_open_file);
        Button btnTest = findViewById(R.id.analysis);
        btnOpenFile.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        btnOpenFile.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                REQUESTCODE_FROM_ACTIVITY = 1000;
                String path = null;
                String externalStorageState = Environment.getExternalStorageState();
                if (externalStorageState.equals(Environment.MEDIA_MOUNTED)){
                    //sd卡已经安装，可以进行相关文件操作
                    File dir = new File(Environment.getExternalStorageDirectory(),"atestdir");
                    if (!dir.exists()){
                        dir.mkdirs();
                    }
                    path = dir.getAbsolutePath();
                    System.out.println(path);

                }
                new LFilePicker()
                        .withActivity(MainActivity.this)
                        .withRequestCode(REQUESTCODE_FROM_ACTIVITY)
                        .withStartPath(path)//指定初始显示路径
                        .start();
            }
        });

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("filepath:" + filePath);
                File file = new File(filePath);
                Encoder encoder = new Encoder();
                try {
                    MultimediaInfo info = encoder.getInfo(file);
                    int bit = info.getAudio().getBitRate();
                    System.out.println("bit:" + bit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESTCODE_FROM_ACTIVITY) {
                Button btnFileOpen = findViewById(R.id.btn_open_file);
                //如果是文件选择模式，需要获取选择的所有文件的路径集合
                //List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);//Constant.RESULT_INFO == "paths"
                List<String> list = data.getStringArrayListExtra("paths");
                //Toast.makeText(getApplicationContext(), "选中了" + list.size() + "个文件", Toast.LENGTH_SHORT).show();
                /*for (String name:list){
                    System.out.println("name:" + name);
                }*/
                if (list != null && list.size()!=0){
                    filePath = list.get(0).trim();
                    btnFileOpen.setText(filePath.substring(filePath.lastIndexOf("/")+1));
                }
                //如果是文件夹选择模式，需要获取选择的文件夹路径
                //String path = data.getStringExtra("path");
                //Toast.makeText(getApplicationContext(), "选中的路径为" + path, Toast.LENGTH_SHORT).show();
            }
        }
    }

}