package com.droidba.widget.calendar.bean;

public class DateBean {
  public static final int STATE_BEFORE = 0;
  public static final int STATE_CURRENT = 1;
  public static final int STATE_AFTER = 2;
  private Integer year;
  private Integer month;
  private Integer solarDay;
  private int state;
  private boolean isSelected;
  private boolean isToday;

  public boolean isSelected() {
    return isSelected;
  }

  public void setSelected(boolean selected) {
    isSelected = selected;
  }

  public boolean isToday() {
    return isToday;
  }

  public void setToday(boolean today) {
    isToday = today;
  }

  public int getState() {
    return state;
  }

  public void setState(int state) {
    this.state = state;
  }

  public String getDate() {
    return year + "." + month + "." + solarDay;
  }

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public Integer getMonth() {
    return month;
  }

  public void setMonth(Integer month) {
    this.month = month;
  }

  public Integer getSolarDay() {
    return solarDay;
  }

  public void setSolarDay(Integer solarDay) {
    this.solarDay = solarDay;
  }
}
