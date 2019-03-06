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
import android.widget.Toast;

public class Lockscreen extends AppCompatActivity {


    EditText et;
    String loadPW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lockscreen);

        et = (EditText)findViewById(R.id.passWordEdit);

        SharedPreferences spfLockScreen = getSharedPreferences("LOCK_SCREEN_DATA", MODE_PRIVATE);
        loadPW = spfLockScreen.getString("LOCK_SCREEN_PW", ""); // 키값으로 꺼냄

        et.requestFocus();
        et.setOnKeyListener(new View.OnKeyListener() {
            //엔터를 입력하면?
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( (event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER ) {
                    if( loadPW.equals(et.getText().toString()) ) {
                        Intent intent = new Intent(Lockscreen.this, MainActivity.class);
                        Toast.makeText(Lockscreen.this, "비밀번호 일치", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        Lockscreen.this.finish();
                    }else Toast.makeText(Lockscreen.this, "비밀번호를 확인해 주세요"+loadPW+"//"+et.getText().toString(), Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);


    }
}
