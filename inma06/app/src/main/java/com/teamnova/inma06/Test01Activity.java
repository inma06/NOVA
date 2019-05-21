package com.teamnova.inma06;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.teamnova.nova.R;

import java.util.Timer;

public class Test01Activity extends AppCompatActivity implements SensorEventListener {

  // 두 조건에 해당되면 책상에 핸드폰을 뒤집어 놓은 것으로 판단 한다.
  private static boolean isAcc = false; //false : 평상시, true : accZ가 -0.8 이하일 때
  private static boolean isProx = false; //false : 평상시, true : 근접센서값이 0일때


  private SensorManager sensorManager;
  private Sensor accelerSensor;
  private Sensor proximitySensor;
  private float accZ, prox;

  TextView accZ_Tv, prox_Tv;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_test01);

    accZ_Tv = (TextView)findViewById(R.id.accZ_Tv);
    prox_Tv = (TextView)findViewById(R.id.prox_Tv);

    sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
    accelerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
  }

  @Override
  protected void onResume() {
    super.onResume();
    sensorManager.registerListener(this, accelerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
  }

  @Override
  protected void onPause() {
    super.onPause();
    sensorManager.unregisterListener(this);
  }

  @Override
  public void onSensorChanged(SensorEvent event) {

    if(event.sensor == accelerSensor) {
      //가속도 센서
      accZ = event.values[2];
      accZ_Tv.setText("accZ : " + String.format("%.2f", accZ));
      if(accZ < -0.8) isAcc = true;
      else isAcc = false;
    }

    if(event.sensor == proximitySensor) {
      //근접 센서
      prox = event.values[0];
      prox_Tv.setText("prox : " + String.format("%.2f", prox));
      if(prox < 1.0) isProx = true;
      else isProx = false;
    }

    if(isAcc) {
      if(isProx){
        Toast.makeText(this, "핸드폰을 뒤집어 놓았다!", Toast.LENGTH_SHORT).show();
      }
    }


  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy) {

  }
}
