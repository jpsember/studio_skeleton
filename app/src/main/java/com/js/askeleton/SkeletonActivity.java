package com.js.askeleton;

import static com.js.basic.Tools.*;

import com.js.android.AppPreferences;
import com.js.android.MyActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

public class SkeletonActivity extends MyActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    startApp(this);
    super.onCreate(savedInstanceState);
    View view = prepareView();
    setContentView(view);
  }


  private View prepareView() {
    AppPreferences.prepare(this);

    View view = new View(this);
    {
      int colorIndex = AppPreferences.getUniqueIdentifier("color");
      int[] colors = {Color.GREEN, Color.BLUE, Color.MAGENTA, Color.DKGRAY};
      view.setBackgroundColor(colors[colorIndex % colors.length]);
    }
    return view;
  }
}
