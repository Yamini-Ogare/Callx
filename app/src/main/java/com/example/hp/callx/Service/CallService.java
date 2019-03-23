package com.example.hp.callx.Service;

import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;
import android.telecom.CallAudioState;
import android.telecom.InCallService;
import android.widget.Toast;

import com.example.hp.callx.Activities.CallActivity;
import com.example.hp.callx.Activities.DialerActivity;
import com.google.gson.Gson;

import static android.R.attr.phoneNumber;

/**
 * Created by hp on 23/03/2019.
 */

public class CallService extends InCallService {


    @Override
    public void onCallAdded(Call call) {
        super.onCallAdded(call);
        Toast.makeText(getApplicationContext(),"callAdded", Toast.LENGTH_LONG).show();

        Gson gson = new Gson();
        Intent intent1 = new Intent(getApplicationContext(), CallActivity.class);
        intent1.putExtra("tocall", gson.toJson(call));

        getApplicationContext().startActivity(intent1);


    }

    @Override
    public void onCallRemoved(Call call) {
        super.onCallRemoved(call);
    }
}
