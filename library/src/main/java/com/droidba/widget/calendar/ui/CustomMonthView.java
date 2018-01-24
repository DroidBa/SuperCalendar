package com.droidba.widget.calendar.ui;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.droidba.widget.calendar.R;
import com.droidba.widget.calendar.bean.DateBean;
import com.droidba.widget.calendar.weight.MonthView;

public class CustomMonthView extends MonthView {

  public CustomMonthView(@NonNull Context context) {
    super(context);
  }

  public CustomMonthView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public CustomMonthView(@NonNull Context context, @Nullable AttributeSet attrs,
      int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  public Integer setBackColor() {
    return Color.WHITE;
  }

  @Override
  public Integer setDaysRes() {
    return R.layout.item_moth_example;
  }

  @Override
  public void setData(DateBean dateBean, View view) {
    Context context = view.getContext();
    TextView tv_solarDay = (TextView) view.findViewById(R.id.tv_solarDay);
    tv_solarDay.setText(dateBean.getSolarDay() + "");
    switch (dateBean.getState()) {
      case DateBean.STATE_BEFORE:
        if (dateBean.isSelected()) {
          tv_solarDay.setTextColor(Color.GRAY);
          view.setBackground(context.getResources()
              .getDrawable(R.drawable.shape_oval_transparent_stroke_red_example));
        } else {
          if (dateBean.isToday()) {
            tv_solarDay.setTextColor(Color.WHITE);
            view.setBackground(
                context.getResources().getDrawable(R.drawable.shape_oval_yellow_example));
          } else {
            tv_solarDay.setTextColor(Color.GRAY);
            view.setBackground(null);
          }
        }
        break;
      case DateBean.STATE_CURRENT:
        if (dateBean.isSelected()) {
          tv_solarDay.setTextColor(Color.WHITE);
          view.setBackground(context.getResources().getDrawable(R.drawable.shape_oval_red_example));
        } else {
          if (dateBean.isToday()) {
            tv_solarDay.setTextColor(Color.WHITE);
            view.setBackground(
                context.getResources().getDrawable(R.drawable.shape_oval_orange_example));
          } else {
            tv_solarDay.setTextColor(Color.BLACK);
            view.setBackground(null);
          }
        }
        break;
      case DateBean.STATE_AFTER:
        if (dateBean.isSelected()) {
          tv_solarDay.setTextColor(Color.GRAY);
          view.setBackground(context.getResources()
              .getDrawable(R.drawable.shape_oval_transparent_stroke_red_example));
        } else {
          if (dateBean.isToday()) {
            tv_solarDay.setTextColor(Color.WHITE);
            view.setBackground(
                context.getResources().getDrawable(R.drawable.shape_oval_yellow_example));
          } else {
            tv_solarDay.setTextColor(Color.GRAY);
            view.setBackground(null);
          }
        }
        break;
    }
  }
}
