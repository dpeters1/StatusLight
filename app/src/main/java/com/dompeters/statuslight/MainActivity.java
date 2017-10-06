package com.dompeters.statuslight;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import belka.us.androidtoggleswitch.widgets.ToggleSwitch;

public class MainActivity extends AppCompatActivity {

    final String IP_KEY = "ip";
    final String LED_KEY = "led";
    final String BRT_KEY = "brt";
    Button notifAccess;
    Button ipOkay;
    ToggleSwitch ledModes;
    SeekBar brightnessSlider;
    EditText ipAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ledModes = (ToggleSwitch) findViewById(R.id.ledModes);
        notifAccess = (Button) findViewById(R.id.notifAccess);
        ipOkay = (Button) findViewById(R.id.ipButton);
        ipAddress = (EditText) findViewById(R.id.ipAddr);
        brightnessSlider = (SeekBar) findViewById(R.id.brightnessSeekbar);
        notifAccess.setVisibility(View.VISIBLE);

        final Intent notifSettings = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        notifAccess.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(notifSettings);
            }
        });
        ipOkay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dataSingleton.getInstance().ipAddr = ipAddress.getText().toString();
            }
        });
        ledModes.setOnToggleSwitchChangeListener(new ToggleSwitch.OnToggleSwitchChangeListener(){

            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
                dataSingleton.getInstance().ledMode = position;
                Log.d("toggle", String.valueOf(position));
                sendRequest(dataSingleton.getInstance().ipAddr, position, dataSingleton.getInstance().maxBrightness, MainActivity.this);
            }
        });
        brightnessSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                dataSingleton.getInstance().maxBrightness = progressValue;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekbar) {
                sendRequest(dataSingleton.getInstance().ipAddr, dataSingleton.getInstance().ledMode, dataSingleton.getInstance().maxBrightness, MainActivity.this);
            }

        });

        SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        dataSingleton.getInstance().ipAddr = sharedPref.getString(IP_KEY, "192.168.0.1");
        dataSingleton.getInstance().ledMode = sharedPref.getInt(LED_KEY, 2);
        dataSingleton.getInstance().maxBrightness = sharedPref.getInt(BRT_KEY, 100);

        ledModes.setCheckedTogglePosition(dataSingleton.getInstance().ledMode);
        ipAddress.setText(dataSingleton.getInstance().ipAddr);
        sendRequest(dataSingleton.getInstance().ipAddr, dataSingleton.getInstance().ledMode, dataSingleton.getInstance().maxBrightness, MainActivity.this);
    }

    @Override
    protected void onStop(){
        SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(IP_KEY, dataSingleton.getInstance().ipAddr);
        editor.putInt(LED_KEY, dataSingleton.getInstance().ledMode);
        editor.putInt(BRT_KEY, dataSingleton.getInstance().maxBrightness);
        editor.commit();

        super.onStop();
    }

    public static void sendRequest(String host, int ledMode, int brightness, Context ctx) {
        RequestQueue queue = Volley.newRequestQueue(ctx);
        String url = String.format(Locale.US, "http://%s/?ledMode=%d&maxBrightness=%d", host, ledMode, brightness);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, null, null) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Accept", "application/xhtml+xml");

                return params;
            }
        };
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
