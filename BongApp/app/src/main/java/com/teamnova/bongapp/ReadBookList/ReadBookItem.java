package com.teamnova.bongapp.ReadBookList;

import android.os.Parcel;
import android.os.Parcelable;

import com.teamnova.bongapp.FetchBooks.book;

public class ReadBookItem implements Parcelable {

  private String saveThumbNail,
      saveTitle, saveAuthor, savePublisher,
      saveLastTime,saveTotalTime,saveLastPage, saveTotalPage;


  public ReadBookItem(String saveThumbNail, String saveTitle,
                      String saveAuthor, String savePublisher,
                      String saveLastTime, String saveTotalTime,
                      String saveLastPage, String saveTotalPage) {

    this.saveThumbNail = saveThumbNail;
    this.saveTitle = saveTitle;
    this.saveAuthor = saveAuthor;
    this.savePublisher = savePublisher;
    this.saveLastTime = saveLastTime;
    this.saveTotalTime = saveTotalTime;
    this.saveLastPage = saveLastPage;
    this.saveTotalPage = saveTotalPage;


  }

  private ReadBookItem(Parcel in) {

  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<ReadBookItem> CREATOR = new Creator<ReadBookItem>() {
    @Override
    public ReadBookItem createFromParcel(Parcel in) {
      return new ReadBookItem(in);
    }

    @Override
    public ReadBookItem[] newArray(int size) {
      return new ReadBookItem[size];
    }
  };

  public String getSaveThumbNail() {
    return saveThumbNail;
  }

  public String getSaveTitle() {
    return saveTitle;
  }

  public String getSaveAuthor() {
    return saveAuthor;
  }

  public String getSavePublisher() {
    return savePublisher;
  }

  public String getSaveLastTime() {
    return saveLastTime;
  }

  public String getSaveTotalTime() {
    return saveTotalTime;
  }

  public String getSaveLastPage() {
    return saveLastPage;
  }

  public String getSaveTotalPage() {
    return saveTotalPage;
  }


  @Override
  public void writeToParcel(Parcel parcel, int i) {

  }
}
