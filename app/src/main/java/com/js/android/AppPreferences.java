package com.js.android;

import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Simpler interface to application preferences
 * 
 */
public class AppPreferences {

  /**
   * No objects are to be constructed
   */
  private AppPreferences() {
  }

  /**
   * This should be called once before any other methods
   * 
   * @param context
   *          application or activity context (treated differently in test mode)
   */
  synchronized static void prepare(Context context) {
    if (sPreferences != null)
      return;

    if (context instanceof Activity) {
      sPreferences = ((Activity) context).getPreferences(Context.MODE_PRIVATE);
    } else {
      sPreferences = context.getSharedPreferences("__test_preferences__",
          Context.MODE_PRIVATE);
    }
  }

  public static int getUniqueIdentifier(String key) {
    synchronized (sPreferences) {
      int value = sPreferences.getInt(key, 1000);
      sPreferences.edit().putInt(key, 1 + value).commit();
      return value;
    }
  }

  /**
   * Read string from app preferences
   * 
   * @param key
   * @param defaultValue
   * @return
   */
  public static String getString(String key, String defaultValue) {
    return sPreferences.getString(key, defaultValue);
  }

  /**
   * Store string
   * 
   * @param key
   * @param value
   */
  public static void putString(String key, String value) {
    sPreferences.edit().putString(key, value).commit();
  }

  public static void putInt(String key, int value) {
    sPreferences.edit().putInt(key, value).commit();
  }

  public static int getInt(String key, int defaultValue) {
    return sPreferences.getInt(key, defaultValue);
  }

  /**
   * Read boolean from app preferences
   * 
   * @param key
   * @param defaultValue
   * @return
   */
  public static boolean getBoolean(String key, boolean defaultValue) {
    return sPreferences.getBoolean(key, defaultValue);
  }

  /**
   * Store boolean
   * 
   * @param key
   * @param value
   */
  public static void putBoolean(String key, boolean value) {
    sPreferences.edit().putBoolean(key, value).commit();
  }

  /**
   * Store a series of string values (an optimization to allow a single commit
   * at the end)
   * 
   * @param map
   *          map with string values
   */
  public static void putStrings(Map<String, String> map) {
    SharedPreferences.Editor editor = sPreferences.edit();
    for (Map.Entry<String, String> entry : map.entrySet()) {
      editor.putString(entry.getKey(), entry.getValue());
    }
    editor.commit();
  }

  public static void removeKey(String preferencesKey) {
    sPreferences.edit().remove(preferencesKey).commit();
  }

  public static boolean toggle(String preferencesKey) {
    boolean val = !getBoolean(preferencesKey, false);
    putBoolean(preferencesKey, val);
    return val;
  }

  private static SharedPreferences sPreferences;

}
