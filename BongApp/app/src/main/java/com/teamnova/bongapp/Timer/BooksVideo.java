package com.teamnova.bongapp.Timer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.teamnova.bongapp.FetchBooks.FetchMain;
import com.teamnova.bongapp.R;

import java.util.ArrayList;

public class BooksVideo extends YouTubeBaseActivity {

  private static String[] str = {"gnGcwAJur5U", "5F0TZrCIRyQ", "VTc45xsyWT8", "ApF9wNXI3fo","bPX1Kal3jWc", "mYGpPooIEBk"};
  private static int IDlen;
  private static final String TAG = BooksVideo.class.getSimpleName();
  private static ArrayList<YoutubeItem> youtubeItemArrayList;
  private static YoutubeAdapter youtubeAdapter;
//  private YouTubePlayerView youTubeView;
//  private YouTubePlayer.OnInitializedListener listener;

  private RecyclerView mRecyclerView;
  private RecyclerView.LayoutManager mLayoutManager;

  //뒤로가기 눌렀을때 메인액티비티로 이동
  @Override
  public void onBackPressed() {
    super.onBackPressed();
    Intent intent = new Intent(BooksVideo.this, FetchMain.class);
    startActivity(intent);
    BooksVideo.this.finish();
  }
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_test);

    youtubeItemArrayList = new ArrayList<>();
    IDlen = 0;


//    youTubeView = (YouTubePlayerView) findViewById(R.id.youtubeView);

    mRecyclerView = findViewById(R.id.recyclerView_YoutubeView);
    mRecyclerView.setHasFixedSize(true);
    mLayoutManager = new LinearLayoutManager(this);
    mRecyclerView.setLayoutManager(mLayoutManager);
    youtubeAdapter = new YoutubeAdapter(youtubeItemArrayList);
    youtubeItemArrayList.add(0, new YoutubeItem(str[IDlen]));
    mRecyclerView.setAdapter(youtubeAdapter);

    findViewById(R.id.test_exitBtn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(BooksVideo.this, FetchMain.class);
        startActivity(intent);
        BooksVideo.this.finish();
      }
    });


    //다음영상
    findViewById(R.id.test_addVideoBtn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(IDlen < str.length - 1) {
          youtubeItemArrayList.clear();
          youtubeItemArrayList.add(0, new YoutubeItem(str[IDlen+1]));
          IDlen++;
          if(IDlen == str.length - 1) {
            IDlen = 0;
          }
        }
        mRecyclerView.setAdapter(youtubeAdapter);
      }
    });
  }

}
//    youtubeItemArrayList.add(new YoutubeItem("QadSVWXF_ks"));
//
//    youtubeItemArrayList.add(new YoutubeItem("LFDDRTNLHxU"));
//    youtubeItemArrayList.add(new YoutubeItem("VTc45xsyWT8"));
//    youtubeItemArrayList.add(new YoutubeItem("ApF9wNXI3fo"));
//    youtubeItemArrayList.add(new YoutubeItem("wdJDokXJuq0"));
//    youtubeItemArrayList.add(new YoutubeItem("pWy51jOtO9Q"));





//    //리스너 등록부분
//    listener = new YouTubePlayer.OnInitializedListener() {
//      //초기화 성공시
//      @Override
//      public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
////        youTubePlayer.loadVideo("QadSVWXF_ks");//url의 맨 뒷부분 ID값
//        youTubePlayer.cueVideo("QadSVWXF_ks",5000); // i -> ms
//
//      }
//
//      @Override
//      public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
//
//      }
//    };
//    youTubeView.initialize("AIzaSyBuDDiFDQRA0zh7LS-i0dQuLoc1i69qH7Y",listener);
//TODO: 리사이클러뷰 동영상 리스트 구성

