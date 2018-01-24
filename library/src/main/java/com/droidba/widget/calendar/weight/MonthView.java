package com.droidba.widget.calendar.weight;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.droidba.widget.calendar.bean.DateBean;
import com.droidba.widget.calendar.callback.ISelect;

import java.util.ArrayList;

public abstract class MonthView<T extends DateBean> extends ViewGroup {
  private static final int ROW = 6;
  private static final int COLUMN = 7;
  private Context mContext;
  private ISelect mSelectListener;
  private boolean isRadio;
  private ArrayList<T> mList;

  public MonthView(@NonNull Context context) {
    this(context, null);
  }

  public MonthView(@NonNull Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public MonthView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    mContext = context;
    if (setBackColor() != null) {
      setBackgroundColor(setBackColor());
    }
  }

  public void setData(ArrayList<T> list) {
    mList = list;
    for (int i = 0; i < Math.max(list.size(), getChildCount()); i++) {
      View view;
      if (i < getChildCount()) {
        view = getChildAt(i);
      } else {
        view = LayoutInflater.from(mContext).inflate(setDaysRes(), null);
        addView(view, i);
      }
      if (i < list.size()) {
        final T dateBean = list.get(i);
        view.setVisibility(VISIBLE);
        if (mSelectListener.getSelectedTag().contains(dateBean.getDate())) {
          dateBean.setSelected(true);
        } else {
          dateBean.setSelected(false);
        }
        setData(dateBean, view);
        if (dateBean.getState() == DateBean.STATE_CURRENT) {
          view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
              if (mSelectListener.getSelectedTag().contains(dateBean.getDate())) {
                mSelectListener.getSelectedTag().remove(dateBean.getDate());
              } else {
                if (isRadio) {
                  mSelectListener.getSelectedTag().clear();
                }
                mSelectListener.onSelect(dateBean);
                mSelectListener.getSelectedTag().add(dateBean.getDate());
              }
              mSelectListener.refresh();
            }
          });
        } else {
          view.setOnClickListener(null);
        }
      } else {
        view.setVisibility(GONE);
      }
    }
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
    int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

    int itemWidth = widthSpecSize / COLUMN;

    //计算日历的最大高度
    if (heightSpecSize > itemWidth * ROW) {
      heightSpecSize = itemWidth * ROW;
    }

    setMeasuredDimension(widthSpecSize, heightSpecSize);

    int itemHeight = heightSpecSize / ROW;

    int itemSize = Math.min(itemWidth, itemHeight);
    for (int i = 0; i < getChildCount(); i++) {
      View childView = getChildAt(i);
      childView.measure(MeasureSpec.makeMeasureSpec(itemSize, MeasureSpec.EXACTLY),
          MeasureSpec.makeMeasureSpec(itemSize, MeasureSpec.EXACTLY));
    }
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    if (getChildCount() == 0) {
      return;
    }
    View childView = getChildAt(0);
    int itemWidth = childView.getMeasuredWidth();
    int itemHeight = childView.getMeasuredHeight();
    //计算列间距
    int dx = (getMeasuredWidth() - itemWidth * COLUMN) / (COLUMN * 2);

    //当显示五行时扩大行间距
    int dy = 0;
    int visibleChildCount = 0;
    for (int i = 0; i < getChildCount(); i++) {
      if (getChildAt(i).getVisibility() == VISIBLE) {
        visibleChildCount++;
      }
    }
    if (visibleChildCount == 35) {
      dy = itemHeight / 5;
    }

    for (int i = 0; i < getChildCount(); i++) {
      View view = getChildAt(i);

      int left = i % COLUMN * itemWidth + ((2 * (i % COLUMN) + 1)) * dx;
      int top = i / COLUMN * (itemHeight + dy);
      int right = left + itemWidth;
      int bottom = top + itemHeight;
      view.layout(left, top, right, bottom);
    }
  }

  public void refresh() {
    if (mList != null) {
      setData(mList);
    }
  }

  public void setSelectListener(ISelect selectListener) {
    this.mSelectListener = selectListener;
  }

  public void setRadio(boolean radio) {
    isRadio = radio;
  }

  public abstract Integer setBackColor();

  public abstract Integer setDaysRes();

  public abstract void setData(T monthBean, View view);
}
