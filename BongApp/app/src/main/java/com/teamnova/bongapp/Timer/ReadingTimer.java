package com.teamnova.bongapp.Timer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.teamnova.bongapp.FetchBooks.FetchMain;
import com.teamnova.bongapp.MainActivity;
import com.teamnova.bongapp.R;
import com.teamnova.bongapp.ReadBookList.ReadBookArchive;
import com.teamnova.bongapp.ReadBookList.ReadBookItem;
import com.teamnova.bongapp.ReadBookList.ReadBooksAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReadingTimer extends AppCompatActivity {

  final private static String TAG = "ReadingTimer.java";

  public static TextView textTimer,startText, pauseText, readStartText;
  public static String saveReadingTime;
  public static boolean isAgain;
  public static Button btnStart;
  public static Button btnPause;
  public static Button btnStop;
  public static String barcodeResult;
  public static TextView mTitleText, mAuthors, mPublisher;
  public static TextView mISBN;
  public static ImageView mThumbNail;
  public static int mPos;

  //인텐트로 전달할 내용
  public static String putTitle, putAuthor, putPublisher;


  // 상태값 정의
  final int READY = 0;
  final int RUNNING = 1;
  final int PAUSE = 2;
  final int STOP = 3;
  final int RESET = 4;

  //상태값 저장 변수
  int state = STOP; //초기상태

  //책 검색이 완료되었는가?
  public static boolean isFindBook;

  TimeThread thread;
  int mSec, sec, min, hour;

  //쓰레드로 부터 메세지를 받을 핸들러 객체
  Handler handler = new Handler() {
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case 0:
          mSec++; //mSec -> 0.1초
          if(mSec == 10){
            // mSec = 10 -> sec = 1
            mSec = 0;
            sec++;
          }
          if(sec > 59) {
            sec = 0;
            min++;
          }
          if(min > 59) {
            hour++;
            min = 0;
          }

          if( hour < 1 && min < 1 && state != STOP ){
            //0시 0분 일때 초만 나오게
            textTimer.setText(Integer.toString(sec)+"초 동안 독서 중");
          }else if( hour < 1 && state != STOP ){
            //0시 일때 분과 초 나오게
            textTimer.setText(Integer.toString(min)+"분 "+Integer.toString(sec)+"초 동안 독서 중");
          }else if( hour > 0 && state != STOP){
            //시간 분 초까지 나오게
            textTimer.setText(Integer.toString(hour)+"시간 "+Integer.toString(min)+"분 "+Integer.toString(sec)+"초 동안 독서 중");
          }
          Log.e(TAG, Integer.toString(hour)+"시간 "+Integer.toString(min)+"분 "+Integer.toString(sec)+"초 동안 독서 중");
          break;

        default:
          break;
      }
    }
  };

  public void resetTime() {
    sec = 0;
    min = 0;
    hour = 0;
  }



  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if(resultCode == Activity.RESULT_OK) {
      IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
      barcodeResult = scanResult.getContents();
      Log.d(TAG, "onActivityResult:" + barcodeResult);
      new FetchBook(mTitleText, mAuthors, mThumbNail).execute(barcodeResult);
      isFindBook = false;
    } else {
      super.onActivityResult(requestCode, resultCode, data);
    }
  }

  @Override
  public void onBackPressed() {
    //        super.onBackPressed();
    if(state == STOP) {
      Intent intent = new Intent(ReadingTimer.this, MainActivity.class);
      startActivity(intent);
      resetTime();
      ReadingTimer.this.finish();
    } else if(state != STOP) {
      Toast.makeText(this, "종료되지 않은 타이머가 존재합니다.", Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    //다른 액티비티 보일때 상태값 저장 -> SPF로??
    //TODO: 다른액티비티 보였다가 다시 돌아올 때 종료버튼 안눌리는 문제
  }

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_reading_timer);
    Log.e(TAG, "리딩타이머 시작");

    FetchMain fetchMain = (FetchMain) FetchMain.fetchMainActivity;
    fetchMain.finish();

    state = STOP; //초기상태
    isAgain = false;
    mPos = getIntent().getIntExtra("pos", -1);
    isAgain = getIntent().getBooleanExtra("isAgain", false);

    textTimer = (TextView) findViewById(R.id.readingTimer_textTimer);
    btnStart = (Button) findViewById(R.id.readingTimer_btnStart);
    btnPause = (Button) findViewById(R.id.readingTimer_btnPause);
    btnStop = (Button) findViewById(R.id.readingTimer_btnStop);
    mTitleText = (TextView) findViewById(R.id.readingTimer_book_title);
    mAuthors = (TextView) findViewById(R.id.readingTimer_book_authors);
    mThumbNail = (ImageView) findViewById(R.id.readingTimer_ivThumbnail);
    mPublisher = (TextView) findViewById(R.id.readingTimer_book_publisher);
    isFindBook = getIntent().getBooleanExtra("isFindBook", false);
    startText = (TextView) findViewById(R.id.textStart);
    pauseText = (TextView) findViewById(R.id.pauseText);
    readStartText = (TextView) findViewById(R.id.readStartText);

    //TODO: 관련영상쪽, 바코드검색 연동


    if(isAgain) {
      isFindBook = true;
      mTitleText.setText(ReadBookArchive.readBooksList.get(mPos).getSaveTitle());
      mAuthors.setText(ReadBookArchive.readBooksList.get(mPos).getSaveAuthor());
      mPublisher.setText(ReadBookArchive.readBooksList.get(mPos).getSavePublisher());
      //이전 시간 불러오기
      Glide.with(mThumbNail.getContext())
          .load(ReadBookArchive.readBooksList.get(mPos).getSaveThumbNail())
          .into(mThumbNail);
    } else {
      isFindBook = true;
      mTitleText.setText(getIntent().getStringExtra("title"));
      mPublisher.setText(getIntent().getStringExtra("authors"));
      mAuthors.setText(getIntent().getStringExtra("publisher"));
      Glide.with(mThumbNail.getContext())
          .load(getIntent().getStringExtra("imageUrl"))
          .into(mThumbNail);
    }


    //ReadBookArchive 전달할 내용
    putTitle = mTitleText.getText().toString();
    putAuthor= mAuthors.getText().toString();
    putPublisher = mPublisher.getText().toString();
    //추가사항


//    //타임저장리스트
//    ArrayList<TimerSaveItem> timerSaveItems = new TimerSaveItem(
//        null,
//        null,
//        null,
//        null,
//        null,
//        null,
//        null)

//    btnStop.setEnabled(false);
////    btnPause.setEnabled(false);

    //유튜브 관련영상
    findViewById(R.id.readingTimer_btnYoutube).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(state != STOP){
          Toast.makeText(ReadingTimer.this, "독서중!! 영상시청을 할 수 없습니다.", Toast.LENGTH_SHORT).show();
        } else if ( state == STOP ) {
          Intent intent = new Intent(ReadingTimer.this, BooksVideo.class);
          startActivity(intent);
          resetTime();
          ReadingTimer.this.finish();
        }
      }
    });

/*    // 책검색 버튼 -> 검색 전용 액티비티 연결
    findViewById(R.id.readingTimer_btnSearchBook).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(ReadingTimer.this, SearchBook_backup.class);
        startActivity(intent);
        ReadingTimer.this.finish();
      }
    });*/


    /*//책 검색 -> 제목으로
    findViewById(R.id.readingTimer_btnFindTitile).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //책이 검색되어 있을 때만 타이머가 가동될 수 있게
        isFindBook = false;
        //종료 상태 일때만 책검색이 가능하게
        if(state != STOP){
          Toast.makeText(ReadingTimer.this, "독서중 입니다. 독서를 종료해 주세요.", Toast.LENGTH_SHORT).show();
        } else if( state == STOP ){
          //이전 검색결과 초기화
          mTitleText.setText("");
          mAuthors.setText("");
          Glide.with(mThumbNail.getContext())
              .load("")
              .into(mThumbNail);
          //다이얼로그
          AlertDialog.Builder ad = new AlertDialog.Builder(ReadingTimer.this);
          ad.setTitle("책 검색");
          ad.setMessage("제목을 입력하세요.");
          final EditText et = new EditText(ReadingTimer.this);
          ad.setView(et);
          //확인버튼
          ad.setPositiveButton("검색", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              new FetchBook(mTitleText, mAuthors, mThumbNail).execute(et.getText().toString());
              dialog.dismiss();
            }
          });
          //취소버튼
          ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              dialog.dismiss();
            }
          });
          //다이얼로그 띄우기
          ad.show();
        }
      }
    });

    //책 검색 -> 바코드 인식
    findViewById(R.id.readingTimer_btnFindCode).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        isFindBook = false;
        if(state != STOP) {
          Toast.makeText(ReadingTimer.this, "독서중 입니다. 독서를 종료해 주세요.", Toast.LENGTH_SHORT).show();
        } else if( state == STOP ){
          mTitleText.setText("");
          mAuthors.setText("");
          Glide.with(mThumbNail.getContext())
              .load("")
              .into(mThumbNail);
          IntentIntegrator integrator = new IntentIntegrator(ReadingTimer.this);
          integrator.setBeepEnabled(true);
          integrator.setCaptureActivity(CustomScanner.class);
          integrator.initiateScan();
        }
      }
    });*/


    //시작 버튼
    btnStart.setOnClickListener(new View.OnClickListener() {
      //TODO: 비행기모드 활성화 -> 안내멘트 출력 선택옵션으로 처리.
      @Override
      public void onClick(View v) {
        // 버튼 비활성화 처리 우선 주석처리
        //TODO: 버튼 비활성화 <-> 활성화 하려면 상태를 저장해야함.( 해당 액티비티가 종료되어도 로그아웃 되기 전까지 )

        // 시작버튼을 누르면 다시 시작버튼 못누르게
        // 시작 버튼비활성화
        // 일시정지 버튼 활성화
        // 종료버튼 활성화
//        btnPause.setEnabled(true);
//        btnStop.setEnabled(true);
//        btnStart.setEnabled(false);
        if(state == RUNNING || state == PAUSE){
          Toast.makeText(ReadingTimer.this, "이미 독서중입니다.", Toast.LENGTH_SHORT).show();
        }else if(isFindBook) {
          // 시간 초기화
          resetTime();
          state = RUNNING;

          try {
            //쓰레드 시작
            thread = new TimeThread(handler);
            thread.start();
          } catch (Exception e) {
            e.printStackTrace();
            //익셉션 발생시 쓰레드 정지
            thread.stopForever();
            //쓰레드 객체 재생성
            thread = new TimeThread(handler);
            //쓰레드를 시작
            thread.start();
          }
        } else {
          Toast.makeText(ReadingTimer.this, "읽을 책이 준비되지 않았습니다.", Toast.LENGTH_SHORT).show();
        }
      }
    });

    //일시 중지 && 재시작 버튼
    btnPause.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(state == RUNNING) {
          //TODO: 휴식할 때는 비행기모드 비활성화 -> 핸드폰 모든 통신 가능하게
          //일시정지 시키기
          //쓰레드 일시정지
          thread.pauseNResume(true);
          //버튼 내용 바꾸기
          btnPause.setBackgroundResource(R.drawable.noun_start_69466);
          pauseText.setText("다시읽기");

          //상태값 바꾸기
          state = PAUSE;
        } else if(state == PAUSE) {
          //TODO: 휴식 끝나고 다시 읽을때 비행기모드 활성화
          //재시작 하기
          thread.pauseNResume(false);
          //버튼 내용 바꾸기
          pauseText.setText("잠시휴식");
          btnPause.setBackgroundResource(R.drawable.noun_pause_1948185);
          state = RUNNING;
        }
      }
    });

    //종료 버튼 눌렀을 때
    btnStop.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //종료버튼 누르면 시작버튼 누를 수 있게 활성화
//        btnStart.setEnabled(true);  버튼 활성화 상태 저장 전까지 주석처리
        //종료버튼 다시 누르지 못하게 비활성화
//        btnStop.setEnabled(false);
        //일시정지 버튼 비활성화
//        btnPause.setEnabled(false);
        if(state != STOP) {
          //TODO: 독서 시간 저장기능 만들기
          // 쓰레드 정지 하기
          thread.stopForever();
          saveReadingTime = Integer.toString(hour) +
              "h " + Integer.toString(min) + "m " + Integer.toString(sec) + "s";
          resetTime();
          //다이얼로그 띄우기
          AlertDialog.Builder ad = new AlertDialog.Builder(ReadingTimer.this);
          ad.setTitle("독서 정보");
          final TextView et = new TextView(ReadingTimer.this);
          et.setText(mTitleText.getText().toString() + "\n\n" + "" +
              mAuthors.getText().toString() + "\n\n" +
              "총 독서량(시간) : " + saveReadingTime);

          ad.setView(et);
          //확인버튼
          ad.setPositiveButton("저장", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              String timeStamp = new SimpleDateFormat(("yy년MM월dd일")).format(new Date());
              if(isAgain){
                Log.e(TAG, "수정입니다" );
                String thumb = ReadBookArchive.readBooksList.get(mPos).getSaveThumbNail();
                Log.e(TAG, thumb);
                Intent intent = new Intent(ReadingTimer.this, ReadBookArchive.class);
                ReadBookArchive.readBooksList.set(mPos, new ReadBookItem(
                    thumb,mTitleText.getText().toString(),
                    mAuthors.getText().toString(),
                    mPublisher.getText().toString(),
                    timeStamp,
                    saveReadingTime,
                    "testLastPage",
                    "testTotalPage"));
                intent.putExtra("isModifyItem", true);
               /* intent.putExtra("thumbNail", thumb)
                    .putExtra("title", mTitleText.getText().toString())
                    .putExtra("author", mAuthors.getText().toString())
                    .putExtra("publisher", mPublisher.getText().toString())
                    .putExtra("lastTime", timeStamp)
                    .putExtra("totalTime", saveReadingTime)
                    .putExtra("lastPage", "testLastPage")
                    .putExtra("totalPage", "testTotalPage")
                    .putExtra("isModifyItem", true)
                    .putExtra("itemPos", mPos);*/
                startActivity(intent);
              } else {
                Log.e(TAG, "제발되라123" );
                String thumb = getIntent().getStringExtra("imageUrl");
//                Log.e(TAG, thumb);
                Intent intent = new Intent(ReadingTimer.this, ReadBookArchive.class);
/*                intent.putExtra("thumbNail", thumb)
                    .putExtra("title", mTitleText.getText().toString())
                    .putExtra("author", mAuthors.getText().toString())
                    .putExtra("publisher", mPublisher.getText().toString())
                    .putExtra("lastTime", timeStamp)
                    .putExtra("totalTime", saveReadingTime)
                    .putExtra("lastPage", "testLastPage")
                    .putExtra("totalPage", "testTotalPage")
                    .putExtra("isModifyItem", false);*/
                ReadBookArchive.readBooksList.add(0,new ReadBookItem(thumb, "데미안",
                    "헤르만 헤세",
                    "브라운힐",
                    timeStamp,
                    saveReadingTime,
                    null,
                    null));
                intent.putExtra("isModifyItem", false);
                startActivity(intent);
              }
              Toast.makeText(ReadingTimer.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
              dialog.dismiss();
              ReadingTimer.this.finish();
            }
          });
          //취소버튼
          ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              dialog.dismiss();
            }
          });
          //다이얼로그 띄우기
          ad.show();
          textTimer.setText("독서를 종료합니다.");
          mTitleText.setText("");
          mAuthors.setText("");
          Glide.with(mThumbNail.getContext())
              .load("")
              .into(mThumbNail);
          //버튼 내용 바꾸기
//          btnStop.setText("리셋");
          //상태값 바꾸기
          state = STOP;
        }
//        else if(state == STOP) {
//          //STOP 상태에서 리셋버튼을 누르면 시간관련된 값을 초기화 시켜준다.
//          /*TODO: 쓰레드 바로 정지 기능
//          * 정지 버튼 누를시 쓰레드가 대기상태로갔다가 종료됨으로 약간의 딜레이가 발생
//          * -> 책읽는 시간을 체크하는 기능 상 크리티컬한 문제점이 아님
//          * -> 만약 달리기를 할때와 같은 세밀한 타임워치라면 문제가 되지만, 그렇지 않음
//          * -> 딜레이 최대 약 1초는 감안한다. -> 시간남으면 처리해도됨.
//          * */
//          resetTime();
//          //시작할수 있도록 상태값을 바꿔준다.
//          state = READY;
//        }
      }
    });
  }
}
