package com.teamnova.inma06.Seat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.teamnova.nova.R;

import java.util.ArrayList;

public class SeatHomeActivity extends AppCompatActivity {

  private static String TAG = "recyclerview_example";

  private ArrayList<Dictionary> mArrayList;
  private CustomAdapter mAdapter;
  private RecyclerView mRecyclerView;
  private LinearLayoutManager mLinearLayoutManager;
  private int count = -1;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_seat_main);

    mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_main_list);
    mLinearLayoutManager = new LinearLayoutManager(this);
    mRecyclerView.setLayoutManager(mLinearLayoutManager);


    // MainActivity에서 RecyclerView의 데이터에 접근시 사용됩니다.
    mArrayList = new ArrayList<>();

    // RecyclerView를 위해 CustomAdapter를 사용합니다.
    mAdapter = new CustomAdapter( mArrayList);
    mRecyclerView.setAdapter(mAdapter);

    // RecyclerView의 줄(row) 사이에 수평선을 넣기위해 사용됩니다.
    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
        mLinearLayoutManager.getOrientation());
    mRecyclerView.addItemDecoration(dividerItemDecoration);

    Button buttonInsert = (Button)findViewById(R.id.button_main_insert);
    buttonInsert.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        count++;

        // Dictionary 생성자를 사용하여 ArrayList에 삽입할 데이터를 만듭니다.
        Dictionary dict = new Dictionary("1번",1,"사용 가능");

        //mArrayList.add(0, dict); //RecyclerView의 첫 줄에 삽입
        mArrayList.add(dict); // RecyclerView의 마지막 줄에 삽입

        mAdapter.notifyDataSetChanged(); //변경된 데이터를 화면에 반영
      }
    });

  }
}
