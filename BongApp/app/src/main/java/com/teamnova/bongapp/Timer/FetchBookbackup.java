package com.teamnova.bongapp.Timer;

import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;


public class FetchBookbackup extends AsyncTask<String, Void, String> {

  private TextView mTitleText;
  private TextView mAuthors;
  private TextView mPublisher;
  private TextView mISBN;
  private ImageView mThumbNail;
  private static String TAG = "FetchBook";

  public FetchBookbackup(TextView mTitleText, TextView mAuthors, TextView mPublisher,
                         TextView mISBN, ImageView mThumbNail) {
    this.mTitleText = mTitleText;
    this.mAuthors = mAuthors;
    this.mPublisher = mPublisher;
    this.mISBN = mISBN;
    this.mThumbNail = mThumbNail;
  }

  @Override
  protected void onPostExecute(String s) {
    super.onPostExecute(s);
    try {
      JSONObject jsonObject = new JSONObject(s);
      JSONArray itemsArray = jsonObject.getJSONArray("items");

      for(int i = 0; i < itemsArray.length(); i++) {
        JSONObject book = itemsArray.getJSONObject(i);
        JSONObject volumeInfo = book.getJSONObject("volumeInfo");
        JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
        try {
          String title;
          String authors;
          String thumbNail;
          if(volumeInfo.isNull("imageLinks") == false) {
            if(imageLinks.isNull("thumbnail") == false) {
              thumbNail = imageLinks.getString("thumbnail");
              Glide.with(mThumbNail.getContext())
                  .load(thumbNail)
                  .into(ReadingTimer.mThumbNail);
            } else if(imageLinks.isNull("smallThumbnail") == false) {
              thumbNail = imageLinks.getString("smallThumbnail");
              Glide.with(mThumbNail.getContext())
                  .load(thumbNail)
                  .into(ReadingTimer.mThumbNail);
            } else {
              Glide.with(mThumbNail.getContext())
                  .load("")
                  .into(ReadingTimer.mThumbNail);
            }
          }
          //authors (작가) 정보가 존재할 때
          if(book.isNull("volumeInfo") == false) {
            if(volumeInfo.isNull("authors") == false) {
              authors = volumeInfo.getString("authors")
                  //앞뒤로 2글짜씩 잘라내기( Index[2] ~ Index[ 뒤에서 -2 ] 까지 보이게)
                  .substring(2, volumeInfo.getString("authors").length()-2);
              mAuthors.setText("저자 : " + authors);
            }
            //authors (작가) 정보가 존재하지 않을 때
            else {
              authors = "저자의 정보를 찾을 수 없습니다";
              mAuthors.setText(authors);
            }
            //제목이 존재할 때
            if(volumeInfo.isNull("title") == false) {
              title = volumeInfo.getString("title");
              mTitleText.setText("제목 : " + title);
              ReadingTimer.isFindBook = true;
            } else {
              title = "ISBN 정보가 존재 하지 않습니다.";
              mTitleText.setText(title);
              //TODO:제목으로 검색하기 추가
            }
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } catch (Exception e) {
      mTitleText.setText("책 제목을 찾을 수 없습니다.");
      mAuthors.setText("저자의 정보를 찾을 수 없습니다.");
      e.printStackTrace();
    }
  }

  @Override
  protected String doInBackground(String... strings) {
    return NetworkUtils.getBookInfo(strings[0]);
  }
}