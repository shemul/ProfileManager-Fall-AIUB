package com.shemul.profilemanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProfileManager_fallActivity extends Activity {
	private Button _stopButton;
    public static TextView t;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_main);
        _stopButton = (Button) findViewById(R.id.stopButtonID);
        t = (TextView) findViewById(R.id.textView);
        startService(new Intent(getBaseContext(), SensorService.class));


        _stopButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				stopService(new Intent(getBaseContext(), SensorService.class));
				
			}    
        });
    }
}