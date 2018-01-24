package com.droidba.widget.calendar.ui;

import android.content.Context;
import android.util.AttributeSet;

import com.droidba.widget.calendar.weight.CalendarAdapter;
import com.droidba.widget.calendar.weight.CalendarView;

public class CustomCalendarView extends CalendarView {
  public CustomCalendarView(Context context) {
    super(context);
  }

  public CustomCalendarView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public CalendarAdapter setCalendarAdapter() {
    return new CustomCalendarAdapter(true);
  }
}
