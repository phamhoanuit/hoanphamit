package com.example.calendarcustom.ICustom;

/**
 * Created by rafal.manka on 18/09/2017
 */

public interface MonthScrollListener {
    boolean shouldAddNextMonth(int lastDisplayedYear, int lastDisplayedMonth);

    boolean shouldAddPreviousMonth(int firstDisplayedYear, int firstDisplayedMonth);
}
