package com.droidba.widget.calendar.weight;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.droidba.widget.calendar.bean.DateBean;
import com.droidba.widget.calendar.callback.ISelectDate;

import org.joda.time.DateTime;

public abstract class CalendarView<T extends CalendarAdapter> extends ViewPager {
  private T adapter;
  private ISelectDate iSelectDate;

  public CalendarView(Context context) {
    this(context, null);
  }

  @Override
  public T getAdapter() {
    return adapter;
  }

  public CalendarView(Context context, AttributeSet attrs) {
    super(context, attrs);

    adapter = setCalendarAdapter();
    setAdapter(adapter);
    setCurrentItem(adapter.getTargetPosition(), false);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int calendarHeight;
    if (getAdapter() != null) {
      MonthView view = (MonthView) getChildAt(0);
      if (view != null) {
        calendarHeight = view.getMeasuredHeight();
        setMeasuredDimension(widthMeasureSpec,
            MeasureSpec.makeMeasureSpec(calendarHeight, MeasureSpec.EXACTLY));
      }
    }
  }

  public DateBean getDate(int position) {
    return adapter.getCurrentDate(position);
  }

  public void setDate(DateTime targetTime) {
    adapter = setCalendarAdapter();
    adapter.setDate(targetTime);
    adapter.setOnDateClickListener(iSelectDate);
    setAdapter(adapter);
    setCurrentItem(adapter.getTargetPosition(), true);
  }

  public void setOnDateClickListener(ISelectDate iSelectDate) {
    this.iSelectDate = iSelectDate;
    adapter.setOnDateClickListener(iSelectDate);
  }

  public abstract T setCalendarAdapter();
}
