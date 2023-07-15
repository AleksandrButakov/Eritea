package ru.anbn.eritea;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String SENT_SMS = "SENT_SMS";
    String DELIVER_SMS = "DELIVER_SMS";

    Intent sent_intent = new Intent(SENT_SMS);
    Intent deliver_intent = new Intent(DELIVER_SMS);

    PendingIntent sent_pi, deliver_pi;

    EditText address;
    EditText text;

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sent_pi = PendingIntent.getBroadcast(MainActivity.this, 0, sent_intent, PendingIntent.FLAG_IMMUTABLE);
        deliver_pi = PendingIntent.getBroadcast(MainActivity.this, 0, deliver_intent, PendingIntent.FLAG_IMMUTABLE);

        address = (EditText) findViewById(R.id.address);
        text = (EditText) findViewById(R.id.text);

        button = (Button) findViewById(R.id.sendBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(address.getText().toString(), null, text.getText().toString(), sent_pi, deliver_pi);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(sentReceiver, new IntentFilter(SENT_SMS));
        registerReceiver(deliverReceiver, new IntentFilter(DELIVER_SMS));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(sentReceiver);
        unregisterReceiver(deliverReceiver);
    }

    BroadcastReceiver sentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(context, "Sent", Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(context, "Error send", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    BroadcastReceiver deliverReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                Toast.makeText(context, "Delivered", Toast.LENGTH_LONG).show();
                break;
                default:
                    Toast.makeText(context, "Delivery error", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

}