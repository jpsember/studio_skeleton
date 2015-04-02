package com.js.android;

import static com.js.android.AndroidTools.*;

import android.app.Activity;
import android.os.Bundle;

import static com.js.basic.Tools.*;

@Deprecated
public abstract class MyActivity extends Activity {

  public void setLogging(boolean f) {
    doNothingAndroid();
    mLogging = f;
  }

  protected void log(Object message) {
    if (mLogging) {
      StringBuilder sb = new StringBuilder("===> ");
      sb.append(nameOf(this));
      sb.append(" : ");
      tab(sb, 30);
      sb.append(message);
      pr(sb);
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    log("onCreate savedInstanceState=" + nameOf(savedInstanceState));
    super.onCreate(savedInstanceState);
  }

  @Override
  protected void onResume() {
    log("onResume");
    super.onResume();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    log("onSaveInstanceState outState=" + nameOf(outState));
    super.onSaveInstanceState(outState);
  }

  @Override
  protected void onPause() {
    log("onPause");
    super.onPause();
  }

  @Override
  protected void onDestroy() {
    log("onDestroy");
    super.onDestroy();
  }


  private boolean mLogging;
}
