package com.example.rocco.audiorecorder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AudioRecorder extends AppCompatActivity {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final int READ = 201;
    private static final int WRITE = 203;
    private static final int PHONE_STATE = 205;
    private static final int CAMERA = 206;

    //private MediaRecorder recorder;

    //  private boolean isRecording = false;

    private Button recordButton;
    private Button stopButton;

        //private String mFileName = null;


    // request permission
    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            /*Manifest.permission.CAMERA*/};

    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
            case WRITE:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
            case READ:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
            case PHONE_STATE:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
//            case CAMERA:
//                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                break;
        }
        if (!permissionToRecordAccepted)
            finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_recorder);

        // Record to the external cache directory for visibility


        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        ActivityCompat.requestPermissions(this, permissions, WRITE);
        ActivityCompat.requestPermissions(this, permissions, READ);
        ActivityCompat.requestPermissions(this, permissions, PHONE_STATE);
        //ActivityCompat.requestPermissions(this, permissions, CAMERA);

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

    @Override
    public void onStop() {
        super.onStop();
//        if (recorder != null) {
//            recorder.release();
//            recorder = null;
//        }

    }



}
