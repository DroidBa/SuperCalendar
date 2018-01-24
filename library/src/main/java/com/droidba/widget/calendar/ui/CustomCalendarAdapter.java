package com.droidba.widget.calendar.ui;

import android.content.Context;

import com.droidba.widget.calendar.bean.DateBean;
import com.droidba.widget.calendar.weight.CalendarAdapter;
import org.joda.time.DateTime;

public class CustomCalendarAdapter extends CalendarAdapter<CustomMonthView, DateBean> {
  public CustomCalendarAdapter(boolean isRadio) {
    super(isRadio);
  }

  @Override
  public CustomMonthView setMonthView(Context context) {
    return new CustomMonthView(context);
  }

  @Override
  public DateBean setMonthBean() {
    return new DateBean();
  }

  @Override
  public DateTime getMinTime() {
    return new DateTime(2013, 1, 1, 0, 0);
  }

  @Override
  public DateTime getMaxTime() {
    return new DateTime(2019, 1, 31, 0, 0);
  }
}
