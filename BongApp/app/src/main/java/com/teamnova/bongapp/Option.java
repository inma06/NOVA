package com.teamnova.bongapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Option extends AppCompatActivity {


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        Option.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        Button btn = findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Option.this, PasswordSet.class);
                startActivity(intent);
                Option.this.finish();
            }
        });
        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences spfLockScreen = getSharedPreferences("LOCK_SCREEN_DATA", MODE_PRIVATE);
                SharedPreferences.Editor editor = spfLockScreen.edit();
                editor.putBoolean("LOCK_SCREEN_ISLOCK", false);
                editor.commit();
                Toast.makeText(Option.this, "잠금이 해제 되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Option.this, MainActivity.class);
                startActivity(intent);
                Option.this.finish();
            }
        });
    }
}
