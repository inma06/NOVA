package Seat;

import android.content.Intent;
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

  private static String TAG = "SeatHomeActivity";

  private ArrayList<Dictionary> mArrayList;
  private CustomAdapter mAdapter;
  private RecyclerView mRecyclerView;
  private LinearLayoutManager mLinearLayoutManager;
  private int count = -1;



  @Override
  protected void onResume() {
    super.onResume();

  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_seat_main);

    mRecyclerView = (RecyclerView) findViewById(R.id.seat_main_list);
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

    Intent intent = getIntent();
    String[] seatNumber = new String[55];
    String[] seatStatus = new String[55];
    for(int i = 0; i < 50; i++){
      seatNumber[i] = intent.getStringExtra("seatNumber"+ Integer.toString(i));
      seatStatus[i] = intent.getStringExtra("seatStatus"+ Integer.toString(i));
      count++;
      // Dictionary 생성자를 사용하여 ArrayList에 삽입할 데이터를 만듭니다.
      Dictionary dict = new Dictionary(seatNumber[i] + "번",Integer.parseInt(seatStatus[i]),"");
      mArrayList.add(dict); // RecyclerView의 마지막 줄에 삽입
    }
    mAdapter.notifyDataSetChanged(); //변경된 데이터를 화면에 반영

    Button buttonInsert = (Button)findViewById(R.id.button_main_insert);
    buttonInsert.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
      }
    });
  }
}
