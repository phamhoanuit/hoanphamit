package com.example.calendarcustom.ICustom;

import com.example.calendarcustom.config.State;

/**
 * Created by rafal.manka on 18/09/2017
 */
public interface DateWatcher {
    @State
    int getStateForDate(int year, int month, int day);
}
