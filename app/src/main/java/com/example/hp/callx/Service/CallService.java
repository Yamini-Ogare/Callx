package com.example.hp.callx.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.telecom.Call;
import android.telecom.CallAudioState;
import android.telecom.InCallService;
import android.telecom.PhoneAccount;
import android.widget.Toast;

import com.example.hp.callx.Activities.CallActivity;
import com.example.hp.callx.Activities.DialerActivity;
import com.example.hp.callx.Activities.ReceiverActivity;
import com.google.gson.Gson;

import static android.R.attr.phoneNumber;

/**
 * Created by hp on 23/03/2019.
 */

public class CallService extends InCallService {

    Call call;
    SharedPreferences preferences;

    @Override
    public void onCallAdded(Call call) {
        super.onCallAdded(call);


        preferences = getSharedPreferences("mypref",Context.MODE_PRIVATE);
        this.call = call;



        call.registerCallback(new Call.Callback() {
            @Override
            public void onStateChanged(Call call, int state) {
                super.onStateChanged(call, state);

                SharedPreferences.Editor editor = preferences.edit();


                if (call.getState()==Call.STATE_ACTIVE || call.getState()==Call.STATE_DIALING){
                    editor.putBoolean("oncall",true);
                }else if(call.getState()==Call.STATE_DISCONNECTED) {
                    editor.putBoolean("oncall",false);
                    Intent intent = new Intent();
                    intent.setAction("com.example.hp.callx.CUSTOM_EVENT");
                    intent.putExtra("Key", "disconnect");

                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                }
                editor.apply();


                if(call.getState()==call.STATE_DIALING){

                    Intent intent = new Intent();
                    intent.setAction("com.example.hp.callx.CUSTOM_EVENT");
                    intent.putExtra("Key", "statedialing");

                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                }
                else if(call.getState()==call.STATE_ACTIVE){

                    Intent intent = new Intent();
                    intent.setAction("com.example.hp.callx.CUSTOM_EVENT");
                    intent.putExtra("Key", "stateactive");

                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                }
                else if(call.getState()==Call.STATE_RINGING){

                    Intent intent = new Intent();
                    intent.setAction("com.example.hp.callx.CUSTOM_EVENT");
                    intent.putExtra("Key", "stateringing");

                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                }


            }
        });

        if(call.getState()==2){

            String abc=  call.getDetails().getHandle().toString();
            String substr=abc.substring(abc.length() - 10);
            Intent i = new Intent();
            i.setClass(this, ReceiverActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("callon", substr);
            startActivity(i);

        }else {
            String abc=  call.getDetails().getHandle().toString();
            String substr=abc.substring(abc.length() - 10);
            Intent i = new Intent();
            i.setClass(this, CallActivity.class);
            i.putExtra("callon", substr);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);}


    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter iffr = new IntentFilter("com.example.hp.callx.CUSTOM_EVENT");
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, iffr);

    }

    @Override
    public void onCallRemoved(Call call) {
        super.onCallRemoved(call);
    }







    private BroadcastReceiver onNotice = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Update your RecyclerView here using notifyItemInserted(position);
            Bundle extras = intent.getExtras();
            String status;

            if (extras != null) {
                status = extras.getString("Key");
                // and get whatever type user account id is
                if (status.equalsIgnoreCase("stop")) {

                    Toast.makeText(getApplicationContext(), "call ended", Toast.LENGTH_LONG).show();
                    call.disconnect();

                } else if(status.equalsIgnoreCase("pick")) {

                    call.answer(0);


                } else if(status.equalsIgnoreCase("hold")) {

                    if(call.getState()==Call.STATE_HOLDING){

                        call.unhold();
                        Toast.makeText(getApplicationContext(), "On Call", Toast.LENGTH_LONG).show();
                    } else {

                        call.hold();
                        Toast.makeText(getApplicationContext(), "call on hold", Toast.LENGTH_LONG).show();
                    }


                }
            }
        }

    };

}
