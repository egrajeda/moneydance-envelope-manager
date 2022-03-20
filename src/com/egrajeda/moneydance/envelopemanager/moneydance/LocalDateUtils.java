package com.egrajeda.moneydance.envelopemanager.moneydance;

import com.infinitekind.moneydance.model.DateRange;
import com.infinitekind.util.DateUtil;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class LocalDateUtils {
  public static DateRange createDateRange(LocalDate start, LocalDate end) {
    return new DateRange(convertLocalDateToDate(start), convertLocalDateToDate(end));
  }

  public static int convertLocalDateToInt(LocalDate localDate) {
    return DateUtil.convertDateToInt(convertLocalDateToDate(localDate));
  }

  public static int nowAsInt() {
    return convertLocalDateToInt(LocalDate.now());
  }

  private static Date convertLocalDateToDate(LocalDate localDate) {
    return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
  }
}
