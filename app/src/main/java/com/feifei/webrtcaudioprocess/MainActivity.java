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
import com.feifei.webrtcaudioprocess.AudioResample.AudioResampleUtils;

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
                ExecutorService resampleExecutor = Executors.newSingleThreadExecutor();
                resampleExecutor.execute(()->{
                    try {
                        /* resample前文件夹 */
                        File inFiles = new File(root + File.separator + "inFiles");
                        inFiles.mkdirs();
                        /* resample后文件夹 */
                        File outFiles = new File(root + File.separator + "outFiles");
                        outFiles.mkdirs();

                        AudioResampleUtils audioResampleUtils = new AudioResampleUtils();
                        audioResampleUtils.audioResampleInit(16000, 41100, 2);

                        int inMinBufferSize = AudioResampleUtils.get10msBufferInByte(16000, 16);
                        short[] inDatashort = new short[inMinBufferSize/2];
                        byte[] inDatabyte = new byte[inMinBufferSize];

                        int outMinBufferSize = AudioResampleUtils.get10msBufferInByte(41100, 16);
                        short[] outDatashort = new short[outMinBufferSize/2];
                        byte[] outDatabyte = new byte[outMinBufferSize];

                        for (File inFile : inFiles.listFiles()){
                            InputStream inputStream = new FileInputStream(inFile);
                            OutputStream outputStream = new FileOutputStream(outFiles.getAbsolutePath() + File.separator + inFile.getName());

                            int ret = 0;
                            while ((ret = inputStream.read(inDatabyte)) > 0){
                                /*字节转化*/
                                ByteBuffer.wrap(inDatabyte).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(inDatashort);

                                /*送入模型*/
                                audioResampleUtils.audioResamplePush(inDatashort, inMinBufferSize, outDatashort, outMinBufferSize);

                                /* 保存消噪效果 */
                                ByteBuffer.wrap(outDatabyte).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().put(outDatashort);
                                outputStream.write(outDatabyte);
                                outputStream.flush();
                            }
                        }

                        audioResampleUtils.audioResampleDestroy();
                        Log.d(TAG, "finish resample test");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
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
                        /* vad前文件夹 */
                        File inFiles = new File(root + File.separator + "inFiles");
                        inFiles.mkdirs();

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
                                Log.d(TAG, "vad test: " + isVoice);
                            }
                        }
                        audioEffectUtils.audioEffectDestroy();
                    }catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case R.id.agc:
                ExecutorService agcExecutor = Executors.newSingleThreadExecutor();
                agcExecutor.execute(()->{
                    try {
                        /* agc前文件夹 */
                        File inFiles = new File(root + File.separator + "inFiles");
                        inFiles.mkdirs();
                        /* agc后文件夹 */
                        File outFiles = new File(root + File.separator + "outFiles");
                        outFiles.mkdirs();

                        AudioEffectUtils audioEffectUtils = new AudioEffectUtils();
                        audioEffectUtils.audioEffectInit(2, 16000);
                        audioEffectUtils.setGainControlParameter(6, 9, 1);

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
                        Log.d(TAG, "finish agc test");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case R.id.aec:
                ExecutorService aecExecutor = Executors.newSingleThreadExecutor();
                aecExecutor.execute(()->{
                    try {
                        /* aec前文件夹 */
                        File inFiles = new File(root + File.separator + "inFiles");
                        inFiles.mkdirs();
                        /* aec后文件夹 */
                        File outFiles = new File(root + File.separator + "outFiles");
                        outFiles.mkdirs();

                        AudioEffectUtils audioEffectUtils = new AudioEffectUtils();
                        audioEffectUtils.audioEffectInit(2, 16000);
                        audioEffectUtils.setEchoCancellationLevel(2);

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
                                audioEffectUtils.audioProcessReverseStream(datashort);
                                audioEffectUtils.audioProcessStream(datashort);

                                /* 保存消噪效果 */
                                ByteBuffer.wrap(databyte).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().put(datashort);
                                outputStream.write(databyte);
                                outputStream.flush();
                            }
                        }
                        audioEffectUtils.audioEffectDestroy();
                        Log.d(TAG, "finish aec test");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case R.id.aecm:
                ExecutorService aecmExecutor = Executors.newSingleThreadExecutor();
                aecmExecutor.execute(()->{
                    try {
                        /* aecm前文件夹 */
                        File inFiles = new File(root + File.separator + "inFiles");
                        inFiles.mkdirs();
                        /* aecm后文件夹 */
                        File outFiles = new File(root + File.separator + "outFiles");
                        outFiles.mkdirs();

                        AudioEffectUtils audioEffectUtils = new AudioEffectUtils();
                        audioEffectUtils.audioEffectInit(2, 16000);
                        audioEffectUtils.setEchoCancellationMobileParameter(1);

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
                                audioEffectUtils.audioProcessReverseStream(datashort);
                                audioEffectUtils.audioProcessStream(datashort);

                                /* 保存消噪效果 */
                                ByteBuffer.wrap(databyte).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().put(datashort);
                                outputStream.write(databyte);
                                outputStream.flush();
                            }
                        }
                        audioEffectUtils.audioEffectDestroy();
                        Log.d(TAG, "finish aecm test");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            default:
                break;
        }
    }
}
