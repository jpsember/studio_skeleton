package com.js.askeleton;

import android.test.InstrumentationTestCase;

import com.js.android.AppPreferences;

public class AppPreferencesTest extends InstrumentationTestCase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    AppPreferences.prepare(this.getInstrumentation().getContext());
  }

  public void testPreferencesDefaultValue() {
    String value = AppPreferences.getString("a", "alpha");
    assertEquals("alpha", value);
    AppPreferences.putString("a", "beta");
    assertEquals("beta", AppPreferences.getString("a", null));
  }


  public void testGetUniqueIdentifier() {
    int previousValue = -1;
    for (int i = 0; i < 10; i++) {
      int value = AppPreferences.getUniqueIdentifier("a");
      if (i != 0)
        assertEquals(previousValue + 1, value);
      previousValue = value;
    }
  }

}