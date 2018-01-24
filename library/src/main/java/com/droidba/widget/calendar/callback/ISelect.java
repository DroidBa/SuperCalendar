package com.droidba.widget.calendar.callback;

import com.droidba.widget.calendar.bean.DateBean;

import java.util.HashSet;

public interface ISelect {
  HashSet<String> getSelectedTag();

  void refresh();

  void onSelect(DateBean dateBean);
}
