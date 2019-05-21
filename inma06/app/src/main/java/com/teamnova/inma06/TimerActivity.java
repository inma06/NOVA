package com.teamnova.inma06;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.app.Activity;
import android.provider.Telephony;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.teamnova.nova.R;

public class TimerActivity extends Activity implements SensorEventListener {
  TextView myOutput;
  TextView myRec;
  Button myBtnStart;
  Button myBtnRec;

  final static int Init =0;
  final static int Run =1;
  final static int Pause =2;

  int cur_Status = Init; //현재의 상태를 저장할변수를 초기화함.
  int myCount=1;
  long myBaseTime;
  long myPauseTime;

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
    setContentView(R.layout.activity_timer);

    myOutput = (TextView) findViewById(R.id.time_out);
    myRec = (TextView) findViewById(R.id.record);
    myBtnStart = (Button) findViewById(R.id.btn_start);
    myBtnRec = (Button) findViewById(R.id.btn_rec);
    accZ_Tv = (TextView) findViewById(R.id.accZ_Tv);
    prox_Tv = (TextView) findViewById(R.id.prox_Tv);

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
  protected void onDestroy() {
    // TODO Auto-generated method stub
    super.onDestroy();
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

    if(isAcc && cur_Status != Run) {
      if(isProx){
        // 핸드폰을 책상에 뒤집어 놓았을 때 -> 타이머 시작
        myBaseTime = SystemClock.elapsedRealtime();
        System.out.println(myBaseTime);
        //myTimer이라는 핸들러를 빈 메세지를 보내서 호출
        myTimer.sendEmptyMessage(0);
        myBtnStart.setText("멈춤"); //버튼의 문자"시작"을 "멈춤"으로 변경
        myBtnRec.setEnabled(true); //기록버튼 활성
        cur_Status = Run; //현재상태를 런상태로 변경
        Toast.makeText(this, "핸드폰을 뒤집어 놓았다!", Toast.LENGTH_SHORT).show();
      } else if(cur_Status == Pause) {
        long now = SystemClock.elapsedRealtime();
        myTimer.sendEmptyMessage(0);
        myBaseTime += (now- myPauseTime);
        myBtnStart.setText("멈춤");
        myBtnRec.setText("기록");
        cur_Status = Run;
      }
    } else if(isProx == false) {
      // 핸드폰을 책상에서 들었을 때 -> 타이머 일시정지

      myTimer.removeMessages(0); //핸들러 메세지 제거
      myPauseTime = SystemClock.elapsedRealtime();
      myBtnStart.setText("시작");
      myBtnRec.setText("리셋");
      cur_Status = Pause;
    }


  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy) {

  }

  public void myOnClick(View v){
    switch(v.getId()){
      case R.id.btn_start: //시작버튼을 클릭했을때 현재 상태값에 따라 다른 동작을 할수있게끔 구현.
        switch(cur_Status){
          case Init:
            // 초기화 상태
            myBaseTime = SystemClock.elapsedRealtime();
            System.out.println(myBaseTime);
            //myTimer이라는 핸들러를 빈 메세지를 보내서 호출
            myTimer.sendEmptyMessage(0);
            myBtnStart.setText("멈춤"); //버튼의 문자"시작"을 "멈춤"으로 변경
            myBtnRec.setEnabled(true); //기록버튼 활성
            cur_Status = Run; //현재상태를 런상태로 변경
            break;
          case Run:
            // 실행중 상태
            myTimer.removeMessages(0); //핸들러 메세지 제거
            myPauseTime = SystemClock.elapsedRealtime();
            myBtnStart.setText("시작");
            myBtnRec.setText("리셋");
            cur_Status = Pause;
            break;
          case Pause:
            // 일시정지 상태
            long now = SystemClock.elapsedRealtime();
            myTimer.sendEmptyMessage(0);
            myBaseTime += (now- myPauseTime);
            myBtnStart.setText("멈춤");
            myBtnRec.setText("기록");
            cur_Status = Run;
            break;


        }
        break;
      case R.id.btn_rec:
        switch(cur_Status){
          case Run:

            String str = myRec.getText().toString();
            str +=  String.format("%d. %s\n",myCount,getTimeOut());
            myRec.setText(str);
            myCount++; //카운트 증가

            break;
          case Pause:
            //핸들러를 멈춤
            myTimer.removeMessages(0);

            myBtnStart.setText("시작");
            myBtnRec.setText("기록");
            myOutput.setText("00:00:00");

            cur_Status = Init;
            myCount = 1;
            myRec.setText("");
            myBtnRec.setEnabled(false);
            break;


        }
        break;

    }
  }

  Handler myTimer = new Handler(){
    public void handleMessage(Message msg){
      myOutput.setText(getTimeOut());

      //sendEmptyMessage 는 비어있는 메세지를 Handler 에게 전송하는겁니다.
      myTimer.sendEmptyMessage(0);
    }
  };
  //현재시간을 계속 구해서 출력하는 메소드
  String getTimeOut(){
    long now = SystemClock.elapsedRealtime(); //애플리케이션이 실행되고나서 실제로 경과된 시간(??)^^;
    long outTime = now - myBaseTime;
    String easy_outTime = String.format("%02d:%02d:%02d", outTime/1000 / 60, (outTime/1000)%60,(outTime%1000)/10);
    return easy_outTime;

  }


}