package com.example.rocco.audiorecorder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AudioRecorder extends AppCompatActivity {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final int READ = 201;
    private static final int WRITE = 203;
//    private static final int PHONE_STATE = 205;
//    private static final int CAMERA = 206;
    private static final int IGNORE_BATTERY_OPTIMIZATION = 210;


    private Button recordButton;
    private Button stopButton;


    // request permission
    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            /*Manifest.permission.READ_PHONE_STATE,*/
            /*Manifest.permission.CAMERA,*/
            Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS};

    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
            case WRITE:
                permissionToRecordAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                break;
            case READ:
                permissionToRecordAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                break;
//            case PHONE_STATE:
//                permissionToRecordAccepted = grantResults[3] == PackageManager.PERMISSION_GRANTED;
//                break;
//            case CAMERA:
//                permissionToRecordAccepted = grantResults[4] == PackageManager.PERMISSION_GRANTED;
//                break;
            case IGNORE_BATTERY_OPTIMIZATION:
                permissionToRecordAccepted = grantResults[5] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted)
            finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_recorder);


        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        ActivityCompat.requestPermissions(this, permissions, WRITE);
        ActivityCompat.requestPermissions(this, permissions, READ);
//        ActivityCompat.requestPermissions(this, permissions, PHONE_STATE);
//        ActivityCompat.requestPermissions(this, permissions, CAMERA);
        ActivityCompat.requestPermissions(this, permissions, IGNORE_BATTERY_OPTIMIZATION);

        recordButton = findViewById(R.id.buttonRecord);
        stopButton = findViewById(R.id.buttonStop);


        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioRecorderService.startRecording(AudioRecorder.this);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioRecorderService.stopRecording(AudioRecorder.this);
            }
        });

    }

}
