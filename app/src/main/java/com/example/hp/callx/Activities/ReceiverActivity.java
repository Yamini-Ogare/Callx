package com.example.hp.callx.Activities;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hp.callx.R;
import com.example.hp.callx.Service.CallService;

public class ReceiverActivity extends AppCompatActivity {

    TextView textView;
    Button hang, pick;
    String callto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);

        textView = (TextView) findViewById(R.id.callfrom);
        pick = (Button) findViewById(R.id.pick);
        hang = (Button) findViewById(R.id.hang);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
             callto = bundle.getString("callon");
            textView.setText(callto);
        }

    }


    public void hangup(View view) {

        endcall();


    }

    public void pickup(View view) {

        // when call is picked up
        Intent intent = new Intent();
        intent.setAction("com.example.hp.callx.CUSTOM_EVENT");
        intent.putExtra("Key", "pick");

        LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);

        Intent i = new Intent(ReceiverActivity.this,CallActivity.class);
        i.putExtra("callon",callto);
        startActivity(i);
        finish();




    }


    private void endcall() {

        Intent intent = new Intent();
        intent.setAction("com.example.hp.callx.CUSTOM_EVENT");
        intent.putExtra("Key", "stop");

        LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);

        finish();
    }

}
