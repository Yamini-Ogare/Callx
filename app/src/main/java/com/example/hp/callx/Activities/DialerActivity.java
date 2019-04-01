package com.example.hp.callx.Activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.telecom.TelecomManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hp.callx.R;
import com.example.hp.callx.Service.CallService;

import static android.telecom.TelecomManager.ACTION_CHANGE_DEFAULT_DIALER;
import static android.telecom.TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME;

public class DialerActivity extends AppCompatActivity {

    EditText editText ;
    Button button ;
    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialer);

        preferences = getSharedPreferences("mypref",Context.MODE_PRIVATE);


        if(preferences.getBoolean("oncall",false)){
            Intent intent1 = new Intent(DialerActivity.this,CallActivity.class);

            startActivity(intent1);

        }

        editText = (EditText) findViewById(R.id.phoneno);
        button   = (Button) findViewById(R.id.call);

        IntentFilter iffr= new IntentFilter("com.example.hp.callx.CUSTOM_EVENT");
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice , iffr);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                call();

            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        offerReplacingDefaultDialer() ;

    }


    private BroadcastReceiver onNotice= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Update your RecyclerView here using notifyItemInserted(position);
            Bundle extras = intent.getExtras();
            String status;

            if (extras != null) {
                status = extras.getString("Key");
                // and get whatever type user account id is
                if(status.equalsIgnoreCase("play")){

                    Toast.makeText(getApplicationContext(),"call added",Toast.LENGTH_LONG).show();
                  /*  Intent intent1 = new Intent(DialerActivity.this,CallActivity.class);
                //    intent.putExtra("ninja" , editText.getText().toString());
                    startActivity(intent1);*/
                }



            }
        }
    };

    // checking if our app is default caller app
    private void offerReplacingDefaultDialer() {
        TelecomManager telecommanager = (TelecomManager) getSystemService(TELECOM_SERVICE);


        if (telecommanager.getDefaultDialerPackage() != getApplicationContext().getPackageName()) {

            Intent intent= new Intent(ACTION_CHANGE_DEFAULT_DIALER);
            intent.putExtra(EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, getApplicationContext().getPackageName());
            startActivity(intent);
        }
    }


    // call function definition

    public void call() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 2);
        }
        else {
           // what to do in call

            Uri uri = Uri.parse("tel:"+editText.getText().toString());
            Intent intent = new Intent(Intent.ACTION_CALL, uri);

            startActivity(intent) ;

        }
    }

    // asking permission to make call

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if(requestCode==2){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                call();
            }
        }
    }




}
