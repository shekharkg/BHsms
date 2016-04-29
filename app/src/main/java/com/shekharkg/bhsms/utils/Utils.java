package com.shekharkg.bhsms.utils;

import java.util.Calendar;

/**
 * Created by ShekharKG on 4/30/2016.
 */
public class Utils {

  public static Calendar calendar = Calendar.getInstance();


  /**
   * Get Date as String from timestamp
   */
  public static String getDate(long timeStampInMillis) {
    calendar.setTimeInMillis(timeStampInMillis);
    return getMonthAsString(calendar.get(Calendar.MONTH)) + " "
        + calendar.get(Calendar.DAY_OF_MONTH) + ", " + calendar.get(Calendar.YEAR);
  }

  /**
   * Get Month name from index
   */
  public static String getMonthAsString(int month) {
    String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    return months[month];
  }
}
