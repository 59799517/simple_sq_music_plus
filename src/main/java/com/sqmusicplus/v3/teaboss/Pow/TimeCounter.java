package com.sqmusicplus.v3.teaboss.Pow;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeCounter
{
  private static String TimeMacth_Default = "yyyy-MM-dd HH:mm:ss.SSS";

  private static String TimeMacth_Date = "yyyy-MM-dd";

  private static String TimeMacth_DateTime = "yyyy-MM-dd HH:mm:ss";

  private static String TimeMacth_DateTimeSSS = "yyyy-MM-dd HH:mm:ss.SSS";

  public static String getTimeMacth(String input) {
    String timemacth = TimeMacth_Default;
    if (input.length() == 19) {
      timemacth = TimeMacth_DateTime;
    } else if (input.length() == 10) {
      timemacth = TimeMacth_Date;
    } else {
      timemacth = TimeMacth_DateTimeSSS;
    }
    return timemacth;
  }
  public String getStrDatetime(Date date) {
    DateFormat df = new SimpleDateFormat(TimeMacth_DateTime);
    return df.format(date);
  }
  public Date getDate(String input) {
    DateFormat df = new SimpleDateFormat(getTimeMacth(input));
    try {
      return df.parse(input);
    } catch (ParseException e) {
      e.printStackTrace();
      return null;
    }
  }
  public Date offset(Date date, int field, int amount) {
    GregorianCalendar grecal = new GregorianCalendar();
    grecal.setTime(date);
    grecal.add(field, amount);
    return grecal.getTime();
  }

  public long getRemainMs(Date start, Date end) {
    return end.getTime() - start.getTime();
  }


  public String getToJson(Date date) {
    return getToJson(new Date(), date);
  }

  public String getToJson(Date start, Date end) {
    GregorianCalendar grecal = new GregorianCalendar();
    String json = "{re:'" + getRemainMs(start, end) + "'," + "y:'" + grecal.get(1) + "'," + "M:'" + (grecal.get(2) + 1) + "'," + "d:'" + grecal.get(5) + "'," + "h:'" + grecal.get(11) + "'," + "m:'" + grecal.get(12) + "'," + "s:'" + grecal.get(13) + "'," + "S:'" + grecal.get(14) + "'," + "W:'" + (grecal.get(7) - 1) + "'" + "}";

    return json;
  }




































  public static void main(String[] args) {
    System.out.println((new TimeCounter()).getDate("2020-10-02"));
  }
}
