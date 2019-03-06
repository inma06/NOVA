package com.teamnova.bongapp.Timer;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {
  private static final String TAG = NetworkUtils.class.getSimpleName();
  private static final String QUERY_PARAM = "q";
  private static final String MAX_RESULTS = "maxResults"; // 최대 결과값 한계치
  private static final String PRINT_TYPE = "printType"; // 프린트 타입

  static String getBookInfo(String queryString) {
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    String bookJSONString = null;
    String BOOK_BASE_URL = "https://www.googleapis.com/books/v1/volumes?";
    String ALADIN_ISBN_URL = "https://www.aladin.co.kr/shop/wproduct.aspx?";

    try {
      //만약 query 가 13자 or 10자이면 ISBN으로 판단함. ( 병법 추후 수정요망 PLEASE FIX IT )
      Uri builtUri = Uri.parse(BOOK_BASE_URL).buildUpon()
//          .appendQueryParameter(QUERY_PARAM, "isbn:" + queryString)
          .appendQueryParameter(QUERY_PARAM, queryString)
          // Build up your query URI, limiting results to 10 items and printed books.
          .appendQueryParameter(MAX_RESULTS, "10")
          .appendQueryParameter(PRINT_TYPE, "books")
          .build();
      URL requestURL = new URL(builtUri.toString());

      urlConnection = (HttpURLConnection) requestURL.openConnection();
      urlConnection.setRequestMethod("GET");
      urlConnection.connect();
      //Read the response using an inputStream and a StringBuffer, then convert it to a String
      InputStream inputStream = urlConnection.getInputStream();
      StringBuffer buffer = new StringBuffer();
      if(inputStream == null) {
        return null;
      }
      reader = new BufferedReader(new InputStreamReader(inputStream));
      String line;
      while ((line = reader.readLine()) != null) {
        buffer.append(line + "\n");
      }
      if(buffer.length() == 0) {
        //Stream was empty. No point in parsing.
        return null;
      }
      bookJSONString = buffer.toString();
      Log.d(TAG, "getBookInfo: " + bookJSONString);

    } catch ( Exception e) {
      e.printStackTrace();
      return null;

    } finally {
      if(urlConnection != null) {
        urlConnection.disconnect();
      }
      if(reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      Log.d(TAG, "---->" + bookJSONString);
      return  bookJSONString;
    }

  }
}
