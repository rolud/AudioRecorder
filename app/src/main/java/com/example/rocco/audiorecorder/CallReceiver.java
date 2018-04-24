package com.example.rocco.audiorecorder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallReceiver extends BroadcastReceiver{

    AudioRecorder ar = new AudioRecorder();

    public void onReceive(final Context context, Intent intent) {
        //Log.w("DEBUG", "DEBUG");


        final TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        //PSListener listener = new PSListener();
        PhoneStateListener listener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                Log.w("PhoneListener", state + " incoming n: " + incomingNumber);
                switch (state) {
                    case 0: // call finished
                        AudioRecorderService.resumeRecording(context.getApplicationContext());
                        break;
                    case 1: // incoming call
                        AudioRecorderService.pauseRecording(context.getApplicationContext());
                        break;
                    case 2: // outcoming call
                        AudioRecorderService.pauseRecording(context.getApplicationContext());
                        break;
                }
            }
        };


        manager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);


    }

}
