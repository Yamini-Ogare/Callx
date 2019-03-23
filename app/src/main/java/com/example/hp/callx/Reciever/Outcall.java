package com.example.hp.callx.Reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.hp.callx.Activities.CallActivity;

/**
 * Created by hp on 23/03/2019.
 */

public class Outcall extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(Outcall.class.getSimpleName(), intent.toString());
        String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);

        Intent intent1 = new Intent(context, CallActivity.class);
        intent.putExtra("tocall", phoneNumber);

        context.startActivity(intent1);



    }
}
