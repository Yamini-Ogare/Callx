package com.example.hp.callx.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hp.callx.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CallActivity extends AppCompatActivity {

    TextView textView ;
    Button button;
    Call call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        textView = (TextView) findViewById(R.id.callto);
        button = (Button) findViewById(R.id.endcall);
        Bundle bundle = getIntent().getExtras();

        Gson gson = new Gson();

         call= gson.fromJson( bundle.getString("tocall"), new TypeToken<Call>() {
        }.getType());

       // textView.setText(bundle.getString("tocall"));



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                endcall();

            }
        });
    }

    private void endcall() {
         call.disconnect();
        finish();

    }
}
