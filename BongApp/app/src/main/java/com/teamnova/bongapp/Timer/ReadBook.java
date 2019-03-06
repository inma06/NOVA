package com.teamnova.bongapp.Timer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.teamnova.bongapp.R;

public class ReadBook extends AppCompatActivity {

  private static final String TAG = "ReadBook.java";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_read_book);

    findViewById(R.id.addRead).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent= new Intent(ReadBook.this, ReadingTimer.class);
        startActivity(intent);
        finish();
      }
    });




  }
}
