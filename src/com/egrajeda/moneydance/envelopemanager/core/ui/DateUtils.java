package com.egrajeda.moneydance.envelopemanager.core.ui;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {
  public static Date create(int year, int month, int day) {
    return new GregorianCalendar(year, month - 1, day).getTime();
  }
}
