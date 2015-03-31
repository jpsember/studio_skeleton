package com.js.android;

import static com.js.android.AndroidTools.*;

import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import static com.js.basic.Tools.*;

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
    if (!testing()) {
      prepareSystemOut();
    }
    AppPreferences.prepare(this);
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

  private void prepareSystemOut() {
    if (sConsoleGreetingPrinted)
      return;
    sConsoleGreetingPrinted = true;

    // Print message about app starting. Print a bunch of newlines
    // to simulate clearing the console, and for convenience,
    // print the time of day so we can figure out if the
    // output is current or not.

    String strTime = "";
    {
      Calendar cal = Calendar.getInstance();
      java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
          "h:mm:ss", Locale.CANADA);
      strTime = sdf.format(cal.getTime());
    }
    for (int i = 0; i < 20; i++)
      pr("\n");
    pr("!!START!!--------------- Start of " + this.getClass().getSimpleName()
        + " ----- " + strTime + " -------------\n\n\n");
  }

  private boolean mLogging;
  private static boolean sConsoleGreetingPrinted;
}
