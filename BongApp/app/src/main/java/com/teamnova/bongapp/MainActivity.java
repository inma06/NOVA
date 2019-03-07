package com.teamnova.bongapp;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.teamnova.bongapp.FetchBooks.FetchMain;
import com.teamnova.bongapp.ReadBookList.ReadBookArchive;
import com.teamnova.bongapp.ReadBookList.ReadBookItem;
import com.teamnova.bongapp.Timer.ReadingTimer;


public class MainActivity extends AppCompatActivity {

  protected static Bundle extras;
  protected static Bitmap imageBitmap;
  private static final int CAMERA_CODE = 1;
  protected static ImageView mainThumbNail;
  private BackPressCloseHandler backPressCloseHandler;

  @Override
  public void onBackPressed() {
//        super.onBackPressed();

    backPressCloseHandler.onBackPressed();
    ReadBookArchive.readBooksList.clear();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == CAMERA_CODE && resultCode == RESULT_OK) {
      Glide.with(this)
          .load(data)
          .into(mainThumbNail);
//      imageBitmap = (Bitmap) extras.get("data");
    }
  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    backPressCloseHandler = new BackPressCloseHandler(this);

    mainThumbNail = (ImageView) findViewById(R.id.mainThumbNail);
    mainThumbNail.setImageBitmap(imageBitmap);
    Glide.with(this)
        .load(R.drawable.image_1)
        .into(mainThumbNail);
    GradientDrawable drawable = (GradientDrawable) getBaseContext().getDrawable(R.drawable.background_rounding);
    mainThumbNail.setBackground(drawable);
    mainThumbNail.setClipToOutline(true);


    //메인화면이 시작될때 쉐어드에서 데이터 불러오기 ( ReadBookArchive )
    ReadBookArchive.readBookSpf = getSharedPreferences("readBookArchive", MODE_PRIVATE);

    String THUMBNAIL, TITLE,
        AUTHOR, PUBLISHER, LAST_TIME,
        TOTAL_TIME, LAST_PAGE, TOTAL_PAGE;
    int READ_BOOK_ITEM_COUNT = ReadBookArchive.readBookSpf.getInt("ITEM_COUNT", 0);

    try {
      /*
       * ==========// 아이템 중복 생성 방지 //=============
       *
       * 어플이 처음 시작된 상태면
       * SharedPreferences 에 저장된 ITEM_COUNT 값이 현재 item 의 size 보다 크다.
       * 만약 아니라면
       * 1. 어플이 종료되지 않은 것
       * 2. 아이템을 아무것도 추가하지 않은 상태인 것
       * -> 때문에 추가로 아이템을 만들지 않는다.
       * */
      Log.e( "현재 아이템 갯수", Integer.toString( ReadBookArchive.readBooksList.size() ) );
      if(READ_BOOK_ITEM_COUNT > ReadBookArchive.readBooksList.size() ) {
        for(int i = 0; i < READ_BOOK_ITEM_COUNT; i++) {

          THUMBNAIL = ReadBookArchive.readBookSpf
              .getString("ITEM_POSITION_" + Integer.toString(i) +
                  "_THUMBNAIL", ""); // 썸네일
          TITLE = ReadBookArchive.readBookSpf
              .getString("ITEM_POSITION_" + Integer.toString(i) +
                  "_TITLE",""); // 책제목
          AUTHOR = ReadBookArchive.readBookSpf
              .getString("ITEM_POSITION_" + Integer.toString(i) +
                  "_AUTHOR",""); // 저자
          PUBLISHER = ReadBookArchive.readBookSpf
              .getString("ITEM_POSITION_" + Integer.toString(i) +
                  "_PUBLISHER", ""); // 출판사
          LAST_TIME = ReadBookArchive.readBookSpf
              .getString("ITEM_POSITION_" + Integer.toString(i) +
                  "_LAST_TIME", ""); // 마지막으로 읽은 시간
          TOTAL_TIME = ReadBookArchive.readBookSpf
              .getString("ITEM_POSITION_" + Integer.toString(i) +
                  "_TOTAL_TIME", ""); // 누적 독서 시간
          LAST_PAGE = ReadBookArchive.readBookSpf
              .getString("ITEM_POSITION_" + Integer.toString(i) +
                  "_LAST_PAGE", ""); // 마지막으로 읽은 페이지
          TOTAL_PAGE = ReadBookArchive.readBookSpf.
              getString("ITEM_POSITION_" + Integer.toString(i) +
                  "_TOTAL_PAGE", ""); // 책의 총 페이지

          ReadBookArchive.readBooksList
              .add(new ReadBookItem(
                  THUMBNAIL, TITLE,
                  AUTHOR, PUBLISHER,
                  LAST_TIME, TOTAL_TIME,
                  LAST_PAGE, TOTAL_PAGE));
          Log.e("i -> ", Integer.toString(i) );
          Log.e("파일명 ->", THUMBNAIL);
        }
      }
    }catch(Exception e){
      e.printStackTrace();
    }

    //메인 썸네일 클릭시 전체화면
    findViewById(R.id.mainThumbNail).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, Fullscreen.class);
        if(extras == null){
          intent.putExtra("mainThumb", R.drawable.image_1);
        } else {
          intent.putExtra("mainThumb", extras.toString());
        }
        startActivity(intent);
        MainActivity.this.finish();
      }
    });

    //메인 - > 책 검색 액티비티 연결
    findViewById(R.id.btnTimer).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, FetchMain.class);
        startActivity(intent);
        MainActivity.this.finish();
      }
    });

/*    //메인 -> ReadBookArchive (내 서재) 연결
    findViewById(R.id.btnTimer).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, ReadBookArchive.class);
        startActivity(intent);
        MainActivity.this.finish();
      }
    });*/

    //메인 카메라버튼
    findViewById(R.id.cameraBtn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
          Log.e("123213222", "카메라되냐" );
          startActivityForResult(takePictureIntent, CAMERA_CODE);
        }
      }
    });

    //메인 Option 버튼
    findViewById(R.id.optionBtn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, Option.class);
        startActivity(intent);
        MainActivity.this.finish();
      }

    });

    // ReadBookArchive 내 서재 버튼
    findViewById(R.id.btnMyLib).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, ReadBookArchive.class);
        startActivity(intent);
        MainActivity.this.finish();
      }
    });

    //메인 Archive 버튼
    findViewById(R.id.galleryBtn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        //SharedPreferences 값 불러오기 ( Archive )
        com.teamnova.bongapp.Archive.spf = getSharedPreferences("archiveData", MODE_PRIVATE);

        String image;
        String text;
        String time;
        String imagePath;


        int count = com.teamnova.bongapp.Archive.spf.getInt("ITEM_COUNT", 0);
        try {
          /*
           * ==========// 아이템 중복 생성 방지 //=============
           *
           * 어플이 처음 시작된 상태면
           * SharedPreferences 에 저장된 ITEM_COUNT 값이 현재 item 의 size 보다 크다.
           * 만약 아니라면
           * 1. 어플이 종료되지 않은 것
           * 2. 아이템을 아무것도 추가하지 않은 상태인 것
           * -> 때문에 추가로 아이템을 만들지 않는다.
           * */
          Log.e("아이템 갯수", Integer.toString(com.teamnova.bongapp.Archive.items.size()));
          if(count > com.teamnova.bongapp.Archive.items.size()) {
            for(int i = 0; i < count; i++) {
              image = com.teamnova.bongapp.Archive.spf.getString("ITEM_POSITION_" + Integer.toString(i) + "_IMAGE", "");
              text = com.teamnova.bongapp.Archive.spf.getString("ITEM_POSITION_" + Integer.toString(i) + "_TEXT","");
              time = com.teamnova.bongapp.Archive.spf.getString("ITEM_POSITION_" + Integer.toString(i) +"_TIME","");
              imagePath = com.teamnova.bongapp.Archive.spf.getString("ITEM_POSITION_" + Integer.toString(i) + "_PATH", "");
              com.teamnova.bongapp.Archive.items.add(i, new Item(image, text, time, image));
              Log.e("i -> ", Integer.toString(i) );
              Log.e("파일명 ->", image);
            }
          }
        }catch(Exception e){
          e.printStackTrace();
        }
        //Archive 액티비티 열기
        Intent intent = new Intent(MainActivity.this, com.teamnova.bongapp.Archive.class);
        startActivity(intent);
        //Main 액티비티 닫기
        MainActivity.this.finish();
      }
    });
  }

  @Override
  protected void onDestroy() {
//        ActivityCompat.finishAffinity(this);
//        this.getDelegate().onDestroy();


    Log.e("app", "onDestroy: ");
    super.onDestroy();

//   Toast.makeText(this, "어플리케이션 정상 종료", Toast.LENGTH_SHORT).show();

  }
  @Override
  protected void onRestart() {
    super.onRestart();
    //액티비티 가장 위로
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//        String link = "market://details?id=";
//        if(isLock == false){
//            if ((int) (Math.random() * 5) + 1 == 1) {
//                Toast.makeText(this, "평가부탁드립니다", Toast.LENGTH_SHORT).show();
////                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link + getPackageName())));
//            }
//        }else if(isLock){
//            Intent intent2 = new Intent(MainActivity.this, Lockscreen.class);
//            startActivity(intent2);
//        }else startActivity(intent);
//

  }
}
