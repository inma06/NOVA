package com.teamnova.bongapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class UserArrayList extends AppCompatActivity {

  String userID;
  String userPW;
  String userEmail;

  public String getUserID() {
    return userID;
  }

  public String getUserPW() {
    return userPW;
  }

  public String getUserEmail() {
    return userEmail;
  }

  public UserArrayList(String userID, String userPW, String userEmail) {
    this.userID = userID;
    this.userPW = userPW;
    this.userEmail = userEmail;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_array_list);
  }



}
