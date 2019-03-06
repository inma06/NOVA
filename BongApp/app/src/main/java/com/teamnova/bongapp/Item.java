package com.teamnova.bongapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


/*
 * CardView로 구성됨
 *
 * */
public class Item extends AppCompatActivity {
  //RecyclerView에 출력될 내용 요소들
//  Bitmap thumbNail;
  String thumbNail;
  String textView;
  String dateText;
  String imagePath;

  public String getImagePath() {
    return imagePath;
  }

  public String getThumbNail() {
    return thumbNail;
  }
/*  public Bitmap getThumbNail() {
    return thumbNail;
  }*/


  public String getTextView() {
    return textView;
  }

  public String getDateText() {
    return dateText;
  }

  public Item(String thumbNail, String textView, String dateText, String imagePath) {
    this.thumbNail = thumbNail;
    this.textView = textView;
    this.dateText = dateText;
    this.imagePath = imagePath;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_item);
  }
}