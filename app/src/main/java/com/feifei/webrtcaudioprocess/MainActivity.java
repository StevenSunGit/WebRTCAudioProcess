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
import java.io.FileNotFoundException;
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
    private Button resample, hpf, ns, vad, agc, aec, aecm;

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
        resample = findViewById(R.id.resample);
        resample.setOnClickListener(this);

        hpf = findViewById(R.id.hpf);
        hpf.setOnClickListener(this);

        ns = findViewById(R.id.ns);
        ns.setOnClickListener(this);

        vad = findViewById(R.id.vad);
        vad.setOnClickListener(this);

        agc = findViewById(R.id.agc);
        agc.setOnClickListener(this);

        aec = findViewById(R.id.aec);
        aec.setOnClickListener(this);

        aecm = findViewById(R.id.aecm);
        aecm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String root = getExternalFilesDir("").getAbsolutePath();
        switch (v.getId()){
            case R.id.resample:
                break;
            case R.id.hpf:
                ExecutorService hpfExecutor = Executors.newSingleThreadExecutor();
                hpfExecutor.execute(()->{
                    try {
                        /* hpf前文件夹 */
                        File inFiles = new File(root + File.separator + "inFiles");
                        inFiles.mkdirs();
                        /* hpf后文件夹 */
                        File outFiles = new File(root + File.separator + "outFiles");
                        outFiles.mkdirs();
                        AudioEffectUtils audioEffectUtils = new AudioEffectUtils();
                        audioEffectUtils.audioEffectInit(2, 16000);
                        audioEffectUtils.setHighPassFilterParameter(true);

                        int minBufferSize = AudioEffectUtils.get10msBufferInByte(16000, 16);
                        short[] datashort = new short[minBufferSize/2];
                        byte[] databyte = new byte[minBufferSize];

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
                        Log.d(TAG, "finish hpf test");
                    }catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case R.id.ns:
                ExecutorService nsExecutor = Executors.newSingleThreadExecutor();
                nsExecutor.execute(()->{
                    try {
                        /* ns前文件夹 */
                        File inFiles = new File(root + File.separator + "inFiles");
                        inFiles.mkdirs();
                        /* ns后文件夹 */
                        File outFiles = new File(root + File.separator + "outFiles");
                        outFiles.mkdirs();

                        AudioEffectUtils audioEffectUtils = new AudioEffectUtils();
                        audioEffectUtils.audioEffectInit(2, 16000);
                        audioEffectUtils.setNoiseSuppressionParameter(2);

                        int minBufferSize = AudioEffectUtils.get10msBufferInByte(16000, 16);
                        short[] datashort = new short[minBufferSize/2];
                        byte[] databyte = new byte[minBufferSize];

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
                        Log.d(TAG, "finish ns test");

                    }catch (Exception e){
                        Log.e(TAG, e.toString());
                    }
                });
                break;
            case R.id.vad:
                ExecutorService vadExecutor = Executors.newSingleThreadExecutor();
                vadExecutor.execute(()->{
                    try {
                        /* ns前文件夹 */
                        File inFiles = new File(root + File.separator + "inFiles");
                        inFiles.mkdirs();
                        /* ns后文件夹 */
                        File outFiles = new File(root + File.separator + "outFiles");
                        outFiles.mkdirs();

                        AudioEffectUtils audioEffectUtils = new AudioEffectUtils();
                        audioEffectUtils.audioEffectInit(2, 16000);
                        audioEffectUtils.setVoiceDetectionLikeLiHood(0);

                        int minBufferSize = AudioEffectUtils.get10msBufferInByte(16000, 16);
                        short[] datashort = new short[minBufferSize/2];
                        byte[] databyte = new byte[minBufferSize];

                        for (File inFile : inFiles.listFiles()){
                            InputStream inputStream = new FileInputStream(inFile);

                            int ret = 0;
                            while ((ret = inputStream.read(databyte)) > 0){
                                /*字节转化*/
                                ByteBuffer.wrap(databyte).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(datashort);

                                /*送入模型*/
                                audioEffectUtils.audioProcessStream(datashort);
                                boolean isVoice = audioEffectUtils.audioHasVoice();
                                Log.d(TAG, "finish vad test: " + isVoice);
                            }
                        }

                    }catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case R.id.agc:
                break;
            case R.id.aec:
                break;
            case R.id.aecm:
                break;
            default:
                break;
        }
    }
}
