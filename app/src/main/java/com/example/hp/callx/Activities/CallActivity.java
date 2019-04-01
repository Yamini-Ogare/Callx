package com.example.hp.callx.Activities;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.callx.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CallActivity extends AppCompatActivity {

    TextView textView, state ;
    Button button, bmute, bhold, bspeaker, bblue;
    SharedPreferences preferences ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        preferences=getSharedPreferences("mypref",Context.MODE_PRIVATE);

        textView = (TextView) findViewById(R.id.callto);
        button = (Button) findViewById(R.id.endcall);
        bmute = (Button) findViewById(R.id.mute);
        bhold = (Button) findViewById(R.id.hold);
        bspeaker = (Button) findViewById(R.id.speaker) ;
        bblue = (Button) findViewById(R.id.blue) ;
        state = (TextView) findViewById(R.id.state);

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter.isEnabled()) {
            bblue.setText("Bluetooth is On");

        }
        else {
             bblue.setText("Bluetooth is Off");

        }



        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            String callto = bundle.getString("callon");
            textView.setText(callto);
        }

        IntentFilter iffr = new IntentFilter("com.example.hp.callx.CUSTOM_EVENT");
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, iffr);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                endcall();

            }
        });

        bmute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mute();

            }
        });


        if(preferences.getBoolean("hold",false)){
            bhold.setText("unhold");
        }

        if(preferences.getBoolean("speaker",false)){
            bspeaker.setText("Speaker off");
        }

        if(preferences.getBoolean("mute",false)){
            bmute.setText("unmute");
        }


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
                if (status.equalsIgnoreCase("disconnect")) {

                    endcall();

                }

                if (status.equalsIgnoreCase("statedialing")){

                    state.setText("Dialing...");
                }

                if (status.equalsIgnoreCase("stateactive")){

                    state.setText("On Call...");
                }
            }
        }

    };

    private void mute() {

        if(!preferences.getBoolean("mute",false)){
            bmute.setText("unmute");
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("mute",true);
            editor.apply();

        }else {
            bmute.setText("mute");
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("mute",false);
            editor.apply();


        }

        AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        if (audioManager.isMicrophoneMute() == false) {
            audioManager.setMicrophoneMute(true);

            Toast.makeText(getApplicationContext(), "call Muted", Toast.LENGTH_LONG).show();


        } else {
            audioManager.setMicrophoneMute(false);

            Toast.makeText(getApplicationContext(), "call unmuted", Toast.LENGTH_LONG).show();

        }


    }

    private void endcall() {

        Intent intent = new Intent();
        intent.setAction("com.example.hp.callx.CUSTOM_EVENT");
        intent.putExtra("Key", "stop");

        LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);

        finish();

    }

    @Override
    public void onBackPressed() {

    }


    public void speaker(View view) {

        if(!preferences.getBoolean("speaker",false)){
            bspeaker.setText("Speaker off");
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("speaker",true);
            editor.apply();

        }else {
            bspeaker.setText("Speaker");
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("speaker",false);
            editor.apply();


        }

        AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        if (audioManager.isSpeakerphoneOn() == false) {
            audioManager.setSpeakerphoneOn(true);

            Toast.makeText(getApplicationContext(), "call on speaker", Toast.LENGTH_LONG).show();


        } else {
            audioManager.setSpeakerphoneOn(false);

            Toast.makeText(getApplicationContext(), "Speaker off ", Toast.LENGTH_LONG).show();

        }


    }

    public void hold(View view) {

        if(!preferences.getBoolean("hold",false)){
            bhold.setText("unhold");
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("hold",true);
            editor.apply();

        }else {
            bhold.setText("hold");
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("hold",false);
            editor.apply();


        }

        Intent intent = new Intent();
        intent.setAction("com.example.hp.callx.CUSTOM_EVENT");
        intent.putExtra("Key", "hold");


        LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);

    }

    public void blue(View view) {


        bluetooth();

    }

    private void bluetooth() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, 5);
        }
        else {
            // what to do in bluetooth


            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.disable();
                bblue.setText("Bluetooth is off");

            }
            else {
                mBluetoothAdapter.enable();
                bblue.setText("Bluetooth is on");
                Intent intentOpenBluetoothSettings = new Intent();
                intentOpenBluetoothSettings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
                startActivity(intentOpenBluetoothSettings);

            }

        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 5 && ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH)
                == PackageManager.PERMISSION_GRANTED ){
            bluetooth();

        }

    }
}
