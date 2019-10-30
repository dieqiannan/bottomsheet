package com.dqn.bottomSheet;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;



/**
 * 网上搞来的
 */
public class FeedRootRecyclerView extends BetterRecyclerView{
  public FeedRootRecyclerView(Context context) {
    this(context, null);
  }
 
  public FeedRootRecyclerView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }
 
  public FeedRootRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }
 
  @Override
  public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    /* do nothing */
  }
}