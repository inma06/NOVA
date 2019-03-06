package com.teamnova.bongapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class PasswordSet extends AppCompatActivity {

    EditText et;
    String savePW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_set);

        et = (EditText)findViewById(R.id.passWordEdit);


        et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
                    savePW = et.getText().toString();
                    Intent intent = new Intent(PasswordSet.this, Lockscreen.class);
                    startActivity(intent);
                    PasswordSet.this.finish();
                    return true;
                }
                return false;
            }
        });


        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);

    }

    @Override
    protected void onPause() {
        super.onPause();
        // Activity 가 종료되기 전에 저장한다
        // SharedPreferences 에 설정값(특별히 기억해야할 사용자 값)을 저장하기
        SharedPreferences spfLockScreen = getSharedPreferences("LOCK_SCREEN_DATA", MODE_PRIVATE);
        SharedPreferences.Editor editor = spfLockScreen.edit();
        editor.putBoolean("LOCK_SCREEN_ISLOCK", true);
        savePW = et.getText().toString(); // 사용자가 입력한 값
        editor.putString("LOCK_SCREEN_PW", savePW); // 입력
        editor.commit(); // 파일에 최종 반영함
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Activity 가 종료되기 전에 저장한다
        // SharedPreferences 에 설정값(특별히 기억해야할 사용자 값)을 저장하기
        SharedPreferences spfLockScreen = getSharedPreferences("LOCK_SCREEN_DATA", MODE_PRIVATE);
        SharedPreferences.Editor editor = spfLockScreen.edit();
        editor.putBoolean("isLock", true);
        savePW = et.getText().toString(); // 사용자가 입력한 값
        editor.putString("LOCK_SCREEN_PW", savePW); // 입력
        editor.commit(); // 파일에 최종 반영함
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Activity 가 종료되기 전에 저장한다
        // SharedPreferences 에 설정값(특별히 기억해야할 사용자 값)을 저장하기
        SharedPreferences spfLockScreen = getSharedPreferences("LOCK_SCREEN_DATA", MODE_PRIVATE);
        SharedPreferences.Editor editor = spfLockScreen.edit();
        editor.putBoolean("isLock", true);
        savePW = et.getText().toString(); // 사용자가 입력한 값
        editor.putString("LOCK_SCREEN_PW", savePW); // 입력
        editor.commit(); // 파일에 최종 반영함
    }
}
