package com.feifei.webrtcaudioprocess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.feifei.webrtcaudioprocess.AudioEffect.AudioEffectUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private  static String TAG = "MainActivity";

    private final int GrantCode = 1;
    private Button ns, vad, agc, aec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissions();
    }

    private void requestPermissions(){
        List<String> permissionList = new ArrayList<>();

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!permissionList.isEmpty()){
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            requestPermissions(permissions, GrantCode);
        }else {
            grantPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case GrantCode :
                if (grantResults.length > 0){
                    for (int result : grantResults){
                        if (result!=PackageManager.PERMISSION_GRANTED){
                            Log.d(TAG, "必须同意所有权限！");
                            return;
                        }
                    }
                }
                grantPermission();
        }
    }

    private void grantPermission(){
        ns = findViewById(R.id.ns);
        ns.setOnClickListener(this);

        vad = findViewById(R.id.vad);
        vad.setOnClickListener(this);

        agc = findViewById(R.id.agc);
        agc.setOnClickListener(this);

        aec = findViewById(R.id.aec);
        aec.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String root = getExternalFilesDir("").getAbsolutePath();
        switch (v.getId()){
            case R.id.ns:
                ExecutorService testFileThreadExecutor = Executors.newSingleThreadExecutor();
                testFileThreadExecutor.execute(()->{
                    try {
                        /* 消噪前文件夹 */
                        File inFiles = new File(root + File.separator + "inFiles");
                        /* 消噪后文件夹 */
                        File outFiles = new File(root + File.separator + "outFiles");

                        AudioEffectUtils audioEffectUtils = new AudioEffectUtils();
                        audioEffectUtils.setNoiseSuppressionLevel(2);
                        audioEffectUtils.audioEffectInit(2, 16000);


                        int minBufferSize = AudioEffectUtils.getMinBufferSize(16000);

                        short[] datashort = new short[minBufferSize];
                        byte[] databyte = new byte[minBufferSize * 2];

                        for (File inFile : inFiles.listFiles()){
                            InputStream inputStream = new FileInputStream(inFile);
                            OutputStream outputStream = new FileOutputStream(outFiles.getAbsolutePath() + File.separator + inFile.getName());

                            int ret = 0;
                            while ((ret = inputStream.read(databyte)) > 0){
                                /*字节转化*/
                                ByteBuffer.wrap(databyte).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(datashort);

                                /*送入模型*/
                                audioEffectUtils.audioProcessStream(datashort);

                                /* 保存消噪效果 */
                                ByteBuffer.wrap(databyte).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().put(datashort);
                                outputStream.write(databyte);
                                outputStream.flush();
                            }
                        }

                        audioEffectUtils.audioEffectDestroy();

                    }catch (Exception e){

                    }
                });
                break;
            case R.id.vad:
                break;
            case R.id.agc:
                break;
            case R.id.aec:
                break;
            default:
                break;
        }
    }
}
