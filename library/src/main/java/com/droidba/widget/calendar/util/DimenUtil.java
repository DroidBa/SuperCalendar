package com.droidba.widget.calendar.util;

import android.content.Context;
import android.util.TypedValue;

public class DimenUtil {
  public static int dp2px(Context context, float dpVal) {
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal,
        context.getResources().getDisplayMetrics());
  }

  public static int getTextSize(Context context, int size) {
    return (int) (size * context.getResources().getDisplayMetrics().scaledDensity);
  }
}
