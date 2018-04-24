package com.example.rocco.audiorecorder;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AudioRecorderService extends Service {

    private static final int NOTIFICATION_ID = 101;

    private MediaRecorder recorder;
    private enum Actions {
        START, STOP, PAUSE, RESUME
    }

    @Override
    public IBinder onBind(Intent intent) {
        // do not provide binding
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //recorder = new MediaRecorder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //handleStop();
        Toast.makeText(this, "Recorder Stopped", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //String fileName = intent.getStringExtra("FILENAME");
        //startRecording(fileName);
        if (intent != null) {
            final String action = intent.getAction();
            Log.w("AudioRecorderService", action);
            if (Actions.START.name().equals(action)) {
                handleStart();
            } else if (Actions.STOP.name().equals(action)) {
                handleStop();
            } else if (Actions.PAUSE.name().equals(action)) {
                handlePause();
            } else if (Actions.RESUME.name().equals(action)) {
                handleResume();
            }
        }

        return START_STICKY;
    }


    private void handleStart() {
        String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        //mFileName += "/audiorecordtest.3gp";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
        mFileName += "/audiorecordtest_" + formatter.format(new Date()) + ".mp3";

        recorder = new MediaRecorder();

        try {
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            recorder.setOutputFile(mFileName);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        } catch (IllegalStateException e) {
            Log.w("Recorder", "Setting resource failed: " + e);
        }

//        try {
//            recorder.prepare();   // inizializza
//        } catch (IllegalStateException e) {
//            Log.w("Recorder", "Prepare failed: " + e);
//        } catch (IOException e) {
//            Log.w("Recorder", "Prepare failed: " + e);
//        }

        try {
            recorder.prepare();   // inizializza
            recorder.start();
            Toast.makeText(this, "Recorder Started", Toast.LENGTH_SHORT).show();
        } catch (IllegalStateException e) {
            Log.w("Recorder", "Prepare/start failed: " + e);
            e.printStackTrace();
        } catch (RuntimeException e) {
            recorder.reset();
            recorder = null;
            //recorder.release();
            Log.w("Recorder", "Prepare/start failed: " + e);
            Toast.makeText(this, "The microphone is already used by another app.", Toast.LENGTH_SHORT).show();
            Log.w("Recorder", "Prepare/start failed: " + e);
        } catch (IOException e) {
            Log.w("Recorder", "The microphone is already used by another app.");
        }

        // ------ START FOREGROUND SERVICE
        Intent notificationIntent = new Intent(this, AudioRecorder.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new Notification.Builder(this)
                .setContentTitle("AudioRecorder")
                .setContentText("AudioRecorder")
                .setTicker("AudioRecorder")
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build();

        startForeground(NOTIFICATION_ID, notification);

//        try {
//            recorder.start();
//            Toast.makeText(this, "Recorder Started", Toast.LENGTH_SHORT).show();
//        } catch (RuntimeException e) {
//            Log.w("Recorder", "Start failed: " + e);
//            Toast.makeText(this, "The microphone is already used by another app.", Toast.LENGTH_SHORT).show();
//        }


    }

    private void handleStop() {
        try {
            recorder.stop();
            recorder.reset();
        } catch (IllegalStateException e) {
            Log.w("Recorder", "Stop/reset failed: " + e);
        }
        recorder.release();
        recorder = null;
        Toast.makeText(this, "Recorder Stopped", Toast.LENGTH_SHORT).show();
        stopForeground(true);
        stopSelf();
    }

    private void handlePause() {
        recorder.pause();
        Toast.makeText(this, "Recorder Paused", Toast.LENGTH_SHORT).show();
    }

    private void handleResume() {
        recorder.resume();
        Toast.makeText(this, "Recorder Resumed", Toast.LENGTH_SHORT).show();
    }

    public static void startRecording(Context context) {
        Intent intent = new Intent(context, AudioRecorderService.class);
        intent.setAction(Actions.START.name());
        context.startService(intent);
    }

    public static void stopRecording(Context context) {
        Intent intent = new Intent(context, AudioRecorderService.class);
        intent.setAction(Actions.STOP.name());
        //context.stopService(intent);
        context.startService(intent);
    }

    public static void pauseRecording(Context context) {
        Intent intent = new Intent(context, AudioRecorderService.class);
        intent.setAction(Actions.PAUSE.name());
        context.startService(intent);
    }

    public static void resumeRecording(Context context) {
        Intent intent = new Intent(context, AudioRecorderService.class);
        intent.setAction(Actions.RESUME .name());
        context.startService(intent);
    }


    // ----- AUDIO FOCUS
//    AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//    AudioAttributes mAudioAttributes = new AudioAttributes.Builder()
//            .setUsage(AudioAttributes.USAGE_MEDIA)
//            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
//            .build();
//    AudioFocusRequest mFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
//            .setAudioAttributes(mAudioAttributes)
//            .setAcceptsDelayedFocusGain(true)
//            .setOnAudioFocusChangeListener()
}
