package com.teamnova.bongapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class Archive extends AppCompatActivity {
  private static int count;
  public static ArrayList<Item> items = new ArrayList();
  public static RecyclerView mRecyclerView;
  public static LinearLayoutManager mLayoutManager;
  public static RecyclerViewAdapter mAdapter;
  public static SharedPreferences spf;
  public static SharedPreferences.Editor editor;
  public static int archiveItemCount;
  private static String TAG = "아카이브";
//  private String IMAGE_KEY = RecyclerViewAdapter.mItems.get() + "IMAGE_KEY";
//  private String MAIN_TEXT_KEY = AddItemBackupsrc.mTimeStamp + "MAIN_TEXT_KEY";
//  private String TIME_STAMP_KEY = AddItemBackupsrc.mTimeStamp + "TIME_STAMP_KEY";
//  private String ITEM_POSITION_KEY = AddItemBackupsrc.mTimeStamp +"ITEM_POSITION_KEY";
//  private String ITEM_COUNT_KEY = "ITEM_COUNT_KEY";



  //뒤로가기 눌렀을때 메인액티비티로 이동
  @Override
  public void onBackPressed() {
    super.onBackPressed();
    Intent intent = new Intent(Archive.this, MainActivity.class);
    startActivity(intent);
    Archive.this.finish();
  }

  @Override
  protected void onPause() {
    super.onPause();
    //RecyclerView 내용 저장
    //onPause -> 다른 액티비가 전면에 보일때 호출되는 생명주기
    spf = getSharedPreferences("archiveData", MODE_PRIVATE);
    editor = spf.edit();

    /*
     *
     *
     * */
    //쉐어드에 데이터 초기화
    editor.clear();
    for(int i = 0; i < mAdapter.getItemCount(); i++) {

      //sample) key -> ITEM_POSITION_2_TIME
      // 쉐어드에 데이터 put
      editor.putString("ITEM_POSITION_"+Integer.toString(i)+"_IMAGE", mAdapter.mItems.get(i).imagePath); //이미지 경로
      editor.putString("ITEM_POSITION_"+Integer.toString(i)+"_TEXT", mAdapter.mItems.get(i).textView); //본문
      editor.putString("ITEM_POSITION_"+Integer.toString(i)+"_TIME", mAdapter.mItems.get(i).dateText); //작성일
      Log.e(TAG, Integer.toString(i) );
      Log.e(TAG, Integer.toString(mAdapter.getItemCount()) );

    }

    //아이템의 총 갯수
    editor.putInt("ITEM_COUNT", mAdapter.getItemCount());
    editor.commit();

    Log.e(TAG, "onPause: 커밋완료");
    Archive.this.finish();

  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_archive);
    mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
    mRecyclerView.setHasFixedSize(true);
    mRecyclerView.setVerticalScrollBarEnabled(true);
    mLayoutManager = new LinearLayoutManager(this);



    //화면 수직으로 보이게하기
    mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//      RecyclerViewAdapter.mItems.add(0,new Item(null,null,null));
//      String str123 = BitMapToString(mAdapter.mItems.get(0).thumbNail);
//      Log.e("비트맵->문자열", str123.substring(0,5));

    findViewById(R.id.addItemBtn).setOnClickListener(new Button.OnClickListener() {
      @Override
      public void onClick(View v) {
        //추가하기 누르면 추가하기 액티비티 실행되게
        Intent intent = new Intent(Archive.this, AddItem.class);
        startActivity(intent);
        Archive.this.finish();
      }
    });

    // LinearLayout으로 설정
    mRecyclerView.setLayoutManager(mLayoutManager);
    // Decoration 설정
    mRecyclerView.addItemDecoration(new RecyclerViewDecoration(LinearLayoutManager.VERTICAL, LinearLayoutManager.HORIZONTAL));
    // Adapter 생성
    mAdapter = new RecyclerViewAdapter(items);
//      Archive.items.add(spfPosition, new Item(spfImage, spfMainText, spfTimeStamp));
    mRecyclerView.setAdapter(mAdapter);
  }

}
