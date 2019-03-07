package com.teamnova.bongapp.Timer;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.teamnova.bongapp.R;

public class SearchBook_backup extends AppCompatActivity {
 /*  *  책 검색 기능.
   *  Spinner(콤보박스)를 이용
   *    ->다양한 속성으로 검색 ( 제목, 저자, 출판사 ISBN ... 등 )
   *
   *  검색된 리턴값 중 "결과내 검색"Btn 활용.
   *    -> 원하는 책을 검색할 수 있다.
   *
   *  검색 리퀘스트 -> 리턴 완료 까지(+1초) ProgressBar...
   *  리턴된 값 -> CardView + RecyclerView 활용
   *
   *  Item 선택시 다이얼로그 "선택", "취소"
   *    -> "선택" -> ReadingTimer Activity 출력.
   *


  * 처음 검색해서 나온 리턴값을 ArrayList 에 담는다.
  * (title, authors, publisher, thumbnail, ISBN_13, ISBN10 ...)
  * 결과 내 재검색을 누르면 ( title, authors, publisher, ISBN -> ISBN13 || ISBN10 )
  * ArrayList 아이템중 선택한 속성값에 매칭되는 값이 존재하는 아이템만 출력한다.
  * 다시 결과 내 재검색을 누르면 위 과정을 반복한다.
  *
  * ----> 리사이클러뷰 검색 활용, SearchView 활용
  * 참조 - >https://pyeongho.github.io/edit_search
  *
  * */


  private static String TAG = "SearchBook_backup";
  private EditText etKeyword;
  private Spinner spinner;
  private String selectStr;

  //뒤로가기 눌렀을때 메인액티비티로 이동
  @Override
  public void onBackPressed() {
    super.onBackPressed();
    Intent intent = new Intent(SearchBook_backup.this, ReadingTimer.class);
    startActivity(intent);
    SearchBook_backup.this.finish();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search_book);
    //액티비티 화면 세로 고정
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    etKeyword = (EditText) findViewById(R.id.searchBook_etKeyword);
    spinner = (Spinner) findViewById(R.id.searchBook_spinner);

    String[] str = {"제목","저자","출판사","ISBN"};
    selectStr = new String();
    selectStr = "";

    //스피너 어댑터 생성
    ArrayAdapter<String> adapter = new ArrayAdapter<String >(this, R.layout.spinner_item, str);
    adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);

    //스피너 이벤트 리스너
    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(spinner.getSelectedItemPosition() > 0) {
          //선택된 항목
          Log.d(TAG, "선택된 항목: " + spinner.getSelectedItem().toString());
          //선택된 항목 String 값을 GoogleAPI 리턴값에 맞에 변환하여 변수에 담는다.
//          selectStr = spinner.getSelectedItem().toString();
          if(spinner.getSelectedItem().toString().equals("제목")) {
            selectStr = "title";
          } else if(spinner.getSelectedItem().toString().equals("저자")) {
            selectStr = "author";
          } else if(spinner.getSelectedItem().toString().equals("출판사")) {

          } else if(spinner.getSelectedItem().toString().equals("ISBN")) {
            selectStr = "isbn";
          }
        }
      }
      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });


    //검색 버튼
    findViewById(R.id.searchBook_btnSearch).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //검색어 et에 텍스트가 존재할 경우에만 동작
        //여기하다말았음
        if(etKeyword.getText().toString().length() != 0) {
          //결과내 검색 버튼 활성화
          findViewById(R.id.searchBook_btnResultInSearch).setEnabled(true);
//          new FetchBook(mTitleText, mAuthors, mThumbNail).execute(et.getText().toString());
        } else {
          Toast.makeText(SearchBook_backup.this, "검색어를 입력해 주세요", Toast.LENGTH_SHORT).show();
        }

      }
    });
    //결과내 검색 버튼
    findViewById(R.id.searchBook_btnResultInSearch).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(SearchBook_backup.this, "결과내 검색버튼 클릭됨.", Toast.LENGTH_SHORT).show();
      }
    });





  }
}
