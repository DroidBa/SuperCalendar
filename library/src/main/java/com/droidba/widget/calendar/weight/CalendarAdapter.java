package com.droidba.widget.calendar.weight;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.droidba.widget.calendar.bean.DateBean;
import com.droidba.widget.calendar.callback.ISelectDate;
import com.droidba.widget.calendar.callback.ISelect;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedList;
import org.joda.time.DateTime;

public abstract class CalendarAdapter<V extends MonthView, T extends DateBean>
    extends PagerAdapter {
  private HashSet<V> mViews;
  private LinkedList<V> mCache;
  private HashSet<String> selectedTag;
  private int mYear;
  private int mMonth;
  private int mDay;
  private Calendar calendar;
  private ISelect mISelect;
  private boolean mIsRadio;
  private ISelectDate iSelectDate;
  private int mCurrentYear;
  private int mCurrentMonth;
  private int mCurrentDay;
  private int totleCount;
  private int targetPosition;
  private DateTime targetTime;

  public CalendarAdapter(boolean isRadio) {
    this.mIsRadio = isRadio;
    calendar = Calendar.getInstance();
    initData(new DateTime());
    mCurrentYear = mYear;
    mCurrentMonth = mMonth;
    mCurrentDay = mDay;
    mCache = new LinkedList<>();
    mViews = new HashSet<>();
    selectedTag = new HashSet<>();
    mISelect = new ISelect() {
      @Override
      public HashSet<String> getSelectedTag() {
        return selectedTag;
      }

      @Override
      public void refresh() {
        for (V v : mViews) {
          v.refresh();
        }
      }

      @Override
      public void onSelect(DateBean dateBean) {
        if (iSelectDate != null) {
          iSelectDate.onSelect(dateBean);
        }
      }
    };
  }

  private void initData(DateTime time) {
    targetTime = time == null ? new DateTime() : time;
    mYear = targetTime.getYear();
    mMonth = targetTime.getMonthOfYear();
    mDay = targetTime.getDayOfMonth();
    buildTodayPosition(targetTime);
  }

  public void buildTodayPosition(DateTime target) {
    DateTime time = new DateTime(getMinTime().toDate());
    int i = 0;
    totleCount = 0;
    while (time.isBefore(getMaxTime())) {
      totleCount++;
      if (time.getMonthOfYear() == target.getMonthOfYear() && target.getYear() == time.getYear()) {
        targetPosition = i;
      }
      i++;
      time = time.plusMonths(1);
    }
  }

  @Override
  public int getCount() {
    return totleCount;
  }

  @Override
  public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    int year;
    int month;
    int currentPosition = targetPosition;
    int offset = position - currentPosition;
    DateTime time = targetTime.plusMonths(offset);
    year = time.getYear();
    month = time.getMonthOfYear();

    V monthView;
    if (mCache.isEmpty()) {
      monthView = setMonthView(container.getContext());
      monthView.setSelectListener(mISelect);
      monthView.setRadio(mIsRadio);
      mViews.add(monthView);
    } else {
      monthView = mCache.removeFirst();
    }

    ArrayList<T> list = new ArrayList<>();
    addBeforeDays(year, month, list);
    addCurrentDays(year, month, list);
    addAfterDays(year, month, list);

    monthView.setData(list);
    container.addView(monthView);
    return monthView;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((MonthView) object);
    mCache.addLast((V) object);
  }

  private int getFirstDayOfWeek(int year, int month) {
    DateTime time = new DateTime(year, month, 1, 0, 0);
    return time.getDayOfWeek();
  }

  private int getDaysByYearMonth(int year, int month) {
    DateTime time = new DateTime(year, month, 1, 0, 0);
    return time.dayOfMonth().getMaximumValue();
  }

  private void addBeforeDays(int year, int month, ArrayList<T> list) {
    int dayOfWeek = getFirstDayOfWeek(year, month);
    if (dayOfWeek == 7) {
      dayOfWeek = 0;
    }

    int beforeMonth = month - 1;
    int beforeYear = year;
    if (beforeMonth < 1) {
      beforeMonth = 12;
      beforeYear = year - 1;
    }
    int beforeDays = getDaysByYearMonth(beforeYear, beforeMonth);
    T dateBean;
    for (int i = 0; i < dayOfWeek; i++) {
      dateBean = setMonthBean();
      dateBean.setYear(beforeYear);
      dateBean.setMonth(beforeMonth);
      dateBean.setSolarDay(beforeDays - dayOfWeek + 1 + i);
      dateBean.setState(DateBean.STATE_BEFORE);
      checkIsToday(dateBean);
      list.add(dateBean);
    }
  }

  public int getPositionOfMonth(DateTime target) {
    DateTime time = new DateTime(getMinTime().toDate());
    int i = 0;
    while (time.isBefore(getMaxTime())) {
      if (time.getMonthOfYear() == target.getMonthOfYear() && target.getYear() == time.getYear()) {
        break;
      }
      i++;
      time = time.plusMonths(1);
    }
    return i;
  }

  private void addCurrentDays(int year, int month, ArrayList<T> list) {
    int days = getDaysByYearMonth(year, month);
    T dateBean;
    for (int i = 0; i < days; i++) {
      dateBean = setMonthBean();
      dateBean.setSolarDay(i + 1);
      dateBean.setYear(year);
      dateBean.setMonth(month);
      dateBean.setState(DateBean.STATE_CURRENT);
      checkIsToday(dateBean);
      list.add(dateBean);
    }
  }

  private void addAfterDays(int year, int month, ArrayList<T> list) {
    int afterMonth = month + 1;
    int afterYear = year;
    if (afterMonth == 13) {
      afterMonth = 1;
      afterYear += 1;
    }
    int afterDays = list.size() % 7;
    if (afterDays != 0) {
      T dateBean;
      for (int i = 0; i < 7 - afterDays; i++) {
        dateBean = setMonthBean();
        dateBean.setYear(afterYear);
        dateBean.setMonth(afterMonth);
        dateBean.setSolarDay(i + 1);
        dateBean.setState(DateBean.STATE_AFTER);
        checkIsToday(dateBean);
        list.add(dateBean);
      }
    }
  }

  private void checkIsToday(T monthBean) {
    if (monthBean.getYear() == mCurrentYear
        && monthBean.getMonth() == mCurrentMonth
        && monthBean.getSolarDay() == mCurrentDay) {
      monthBean.setToday(true);
    } else {
      monthBean.setToday(false);
    }
  }

  public DateBean getCurrentDate(int position) {
    int year;
    int month;
    int currentPosition = targetPosition;
    int offset = position - currentPosition;
    DateTime time = targetTime.plusMonths(offset);
    year = time.getYear();
    month = time.getMonthOfYear();

    DateBean dateBean = new DateBean();
    dateBean.setYear(year);
    dateBean.setMonth(month);
    dateBean.setSolarDay(null);
    return dateBean;
  }

  public void setDate(DateTime dateTime) {
    initData(dateTime);
  }

  public HashSet<V> getViews() {
    return mViews;
  }

  public void setOnDateClickListener(ISelectDate iSelectDate) {
    this.iSelectDate = iSelectDate;
  }

  public abstract V setMonthView(Context context);

  public abstract T setMonthBean();

  public abstract DateTime getMinTime();

  public abstract DateTime getMaxTime();

  public int getTargetPosition() {
    return targetPosition;
  }
}
