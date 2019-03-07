package com.teamnova.bongapp.Timer;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.teamnova.bongapp.R;

import java.util.ArrayList;


public class YoutubeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

//  public static YouTubePlayerView youTubePlayerView;
//  public static YouTubePlayer.OnInitializedListener listener;
  public static class YoutubeViewHolder extends RecyclerView.ViewHolder {

    public static YouTubePlayerView youTubePlayerView;
    public static YouTubePlayer.OnInitializedListener listener;
    YoutubeViewHolder(View view){
      super(view);
      youTubePlayerView = view.findViewById(R.id.youtubeView);

      //리스너 등록부분
      listener = new YouTubePlayer.OnInitializedListener() {
        //초기화 성공시
        @Override
        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
          Log.e("되나안되나", "되라!!--2" );
        }
        @Override
        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        }
      };
    }
  }

  private ArrayList<YoutubeItem> youtubeItemArrayList;
  YoutubeAdapter(ArrayList<YoutubeItem> youtubeItemArrayList) {
    this.youtubeItemArrayList = youtubeItemArrayList;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_youtube_row, parent, false);
    return new YoutubeViewHolder(v);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
    YoutubeViewHolder youtubeViewHolder = (YoutubeViewHolder) holder;

    youtubeViewHolder.listener = new YouTubePlayer.OnInitializedListener() {
      @Override
      public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.cueVideo(youtubeItemArrayList.get(position).urlID);// i -> ms
      }

      @Override
      public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
      }
    };
    youtubeViewHolder.youTubePlayerView.initialize("AIzaSyBuDDiFDQRA0zh7LS-i0dQuLoc1i69qH7Y",youtubeViewHolder.listener);
    Log.e("되나안되나", "되라!!--1" );


    }

  @Override
  public int getItemCount() {
    return youtubeItemArrayList.size();
  }

}
