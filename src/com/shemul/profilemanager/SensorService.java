package com.shemul.profilemanager;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


public class SensorService extends Service implements SensorEventListener {
	
    private Sensor _accelerometerSensor;
    private Sensor _lightSensor;
    private Sensor _proximitySensor;
    private SensorManager _sensorManager;
    private AudioManager _audioManager;
    private int _stage;
    private boolean _isDarkCheck = false;
    private boolean _isTopSideUp = false;
    private boolean _isNormalMode = true;
    
    @Override
    public void onDestroy() {
    	// TODO Auto-generated method stub
        super.onDestroy();
        Toast.makeText(getBaseContext(), "Service Stopped.", Toast.LENGTH_SHORT).show();
        _sensorManager.unregisterListener(this);
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	// TODO Auto-generated method stub
        Toast.makeText(getBaseContext(), "Service Started", Toast.LENGTH_SHORT).show();

        _sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        _accelerometerSensor = _sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        _proximitySensor = _sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        _lightSensor = _sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        _sensorManager.registerListener(this, _accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        _sensorManager.registerListener(this, _proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        _sensorManager.registerListener(this, _lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        _audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        return START_STICKY;
    }
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	
    private void profileChange(int stage)
    {
        if(this._stage == stage)
        {
            return;
        }
        else
        {
            this._stage = stage;
            switch(this._stage)
            {
                case 1:
                    _audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

                    Log.d("Condition", "Loud Sound");
                    //Toast.makeText(ProfileService.this, "Loud Sound", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    _audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);

                    Log.d("Condition", "Vibration");
                    //Toast.makeText(ProfileService.this, "Vibration", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    _audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    Log.d("Condition", "Silent");
                    //Toast.makeText(ProfileService.this, "Silent", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
    
    

	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub

        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] < 5) {
                _isDarkCheck = true;
            } else {
                _isDarkCheck = false;
            }
        }


        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

                if (event.values[2] < -5) {
                    if (_isDarkCheck == true) {
                        profileChange(3);
                        //profileChange(2);
                    }else {
                        _isDarkCheck = false;
                    }
                }

            if (event.values[1] > 8) {
                if (_isDarkCheck) {

                    profileChange(2);
                    //profileChange(2);
                }
                else {
                    profileChange(1);
                }
            }

            if (event.values[1] < -8) {
                if (_isDarkCheck) {
                    profileChange(2);
                    //profileChange(2);
                }
                else {
                    profileChange(1);
                }
            }


            if (event.values[2] >= -1) {
                    if (event.values[1] > -8 && event.values[1] < 8) {
                        profileChange(1);
                    }

            }



        }
        

		
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
