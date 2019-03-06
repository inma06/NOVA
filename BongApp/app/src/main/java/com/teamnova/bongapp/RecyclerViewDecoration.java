package com.teamnova.bongapp;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RecyclerViewDecoration extends RecyclerView.ItemDecoration {
  private final int divHeight;
  private final int divWidth;

  public RecyclerViewDecoration(int divHeight, int divWidth) {
    this.divHeight = divHeight;
    this.divWidth = divWidth;
  }

  @Override
  public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
    super.getItemOffsets(outRect, view, parent, state);
    outRect.top = divHeight;
    outRect.right = divWidth;
  }
}
