package com.teamnova.bongapp.ReadBookList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;

import com.teamnova.bongapp.Archive;
import com.teamnova.bongapp.FetchBooks.FetchMain;
import com.teamnova.bongapp.Item;
import com.teamnova.bongapp.MainActivity;
import com.teamnova.bongapp.R;
import com.teamnova.bongapp.Timer.FetchBook;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReadBookArchive extends AppCompatActivity {

  private RecyclerView recyclerView;
  public ReadBooksAdapter adapter;
  public static ArrayList<ReadBookItem> readBooksList = new ArrayList<>();
  private Button btnNewBook;
  public static SharedPreferences readBookSpf;
  public static SharedPreferences.Editor editor;



  //뒤로가기 눌렀을때 메인액티비티로 이동
  @Override
  public void onBackPressed() {
    super.onBackPressed();
    Intent intent = new Intent(ReadBookArchive.this, MainActivity.class);
    startActivity(intent);
    ReadBookArchive.this.finish();
  }

  @Override
  protected void onPause() {
    super.onPause();
    //RecyclerView 내용 저장
    //onPause -> 다른 액티비가 전면에 보일때 호출되는 생명주기
    readBookSpf = getSharedPreferences("readBookArchive", MODE_PRIVATE);
    editor = readBookSpf.edit();
    /*
     *
     *
     * */
    //쉐어드 데이터 초기화
    editor.clear();
    for(int i = 0; i < recyclerView.getAdapter().getItemCount(); i++) {
      //sample) key -> ITEM_POSITION_2_TIME
      editor.putString("ITEM_POSITION_"+Integer.toString(i)+"_THUMBNAIL",
          readBooksList.get(i).getSaveThumbNail()); //썸네일
      editor.putString("ITEM_POSITION_"+Integer.toString(i)+"_TITLE",
          readBooksList.get(i).getSaveTitle()); //제목
      editor.putString("ITEM_POSITION_"+Integer.toString(i)+"_AUTHOR",
          readBooksList.get(i).getSaveAuthor()); //저자
      editor.putString("ITEM_POSITION_"+Integer.toString(i)+"_PUBLISHER",
          readBooksList.get(i).getSavePublisher()); //출판사
      editor.putString("ITEM_POSITION_"+Integer.toString(i)+"_LAST_TIME",
          readBooksList.get(i).getSaveLastTime()); //마지막 읽은시각
      editor.putString("ITEM_POSITION_"+Integer.toString(i)+"_TOTAL_TIME",
          readBooksList.get(i).getSaveTotalTime()); //총 독서시간
      editor.putString("ITEM_POSITION_"+Integer.toString(i)+"_LAST_PAGE",
          readBooksList.get(i).getSaveLastPage()); //마지막 읽은페이지 수
      editor.putString("ITEM_POSITION_"+Integer.toString(i)+"_TOTAL_PAGE",
          readBooksList.get(i).getSaveTotalPage()); //책의 총페이지 수
      Log.e("아이템 수 ->", Integer.toString(recyclerView.getAdapter().getItemCount()));
    }

    //아이템의 총 갯수
    editor.putInt("ITEM_COUNT", recyclerView.getAdapter().getItemCount());
    editor.commit();

    Log.e("ReadBookArchive", "onPause: 커밋완료");
    ReadBookArchive.this.finish();
  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_read_book_archive);



    recyclerView = (RecyclerView) findViewById(R.id.books_archive_recycler_view);

    adapter = new ReadBooksAdapter(this, readBooksList);
    adapter.notifyDataSetChanged();

    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
    recyclerView.setLayoutManager(mLayoutManager);
    recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setAdapter(adapter);


    //TODO: 리사이클러뷰 검색기능

    //TODO: 실제 데이터 연결해야함 Fix

    Intent intent = getIntent();
    String thumbNail = intent.getStringExtra("thumbNail");
    String title = intent.getStringExtra("title");
    String author = intent.getStringExtra("author");
    String publisher = intent.getStringExtra("publisher");
    String lastTime = intent.getStringExtra("lastTime");
    String totalTime = intent.getStringExtra("totalTime");
    String lastPage = intent.getStringExtra("lastPage");
    String totalPage = intent.getStringExtra("totalPage");
    boolean isModifyItem = intent.getBooleanExtra("isModifyItem", false);

    int itemPos = intent.getIntExtra("itemPos", -1);

    if(readBooksList.isEmpty() == false && isModifyItem == false) {
      // 북 리스트가 존재할 경우 && 수정시가 아닐 경우 ( 최초 작성 )
//      readBooksList.add(0, new ReadBookItem(
//          thumbNail,title,author,
//          publisher,lastTime,totalTime,
//          lastPage,totalPage));
      adapter.notifyDataSetChanged();
    } if(isModifyItem) {
      //수정시에
//      readBooksList.set(itemPos, new ReadBookItem(
//          thumbNail,title,author,
//          publisher,lastTime,totalTime,
//          lastPage,totalPage));
      adapter.notifyDataSetChanged();
    }else if(readBooksList.isEmpty()) {
      readBookSpf = getSharedPreferences("readBookArchive", MODE_PRIVATE);
      int item_count = readBookSpf.getInt("ITEM_COUNT",0);
      Log.e("쉐어드", "onCreate: 로그도나");
      for(int i = 0; i < item_count; i++) {
        Log.e("쉐어드", "onCreate: 로그도나");
        readBooksList.add(0, new ReadBookItem(readBookSpf.getString("ITEM_POSITION_"+Integer.toString(i)+"_THUMBNAIL", ""),
            readBookSpf.getString("ITEM_POSITION_"+Integer.toString(i)+"_TITLE", ""),
            readBookSpf.getString("ITEM_POSITION_"+Integer.toString(i)+"_AUTHOR", ""),
            readBookSpf.getString("ITEM_POSITION_"+Integer.toString(i)+"_PUBLISHER", ""),
            readBookSpf.getString("ITEM_POSITION_"+Integer.toString(i)+"_LAST_TIME", ""),
            readBookSpf.getString("ITEM_POSITION_"+Integer.toString(i)+"_TOTAL_TIME", ""),
            readBookSpf.getString("ITEM_POSITION_"+Integer.toString(i)+"_LAST_PAGE", ""),
            readBookSpf.getString("ITEM_POSITION_"+Integer.toString(i)+"_TOTAL_PAGE", "")));
      }
      adapter.notifyDataSetChanged();
    }


    //TODO: 쉐어드에 저장하고 불러오기 2018.12.25 PM8:35
  }

  public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int spacing;
    private boolean includeEdge;

    public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
      this.spanCount = spanCount;
      this.spacing = spacing;
      this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
      int position = parent.getChildAdapterPosition(view); // item position
      int column = position % spanCount; // item column

      // 한줄에 2개의 아이템
      if (includeEdge) {
        outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
        outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

        if (position < spanCount) { // top edge
          outRect.top = spacing;
        }
        outRect.bottom = spacing; // item bottom
      } else {
        outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
        outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
        if (position >= spanCount) {
          outRect.top = spacing; // item top
        }
      }
    }
  }

  private int dpToPx(int dp) {
    Resources r = getResources();
    return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
  }


}
