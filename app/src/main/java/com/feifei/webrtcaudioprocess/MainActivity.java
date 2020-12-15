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

import java.util.ArrayList;
import java.util.List;

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
        switch (v.getId()){
            case R.id.ns:
                AudioEffectUtils audioEffectUtils = new AudioEffectUtils();
                audioEffectUtils.setNoiseSuppressionLevel(2);
                audioEffectUtils.audioEffectInit(2, 16000);
                audioEffectUtils.audioProcessStream(null);
                audioEffectUtils.audioEffectDestroy();
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
