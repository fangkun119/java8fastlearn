import java.sql.*;
import java.text.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

public class Legacy {
   private static final String LOG_PREFIX = "\t// ";

   public static void main(String[] args) {
      System.out.println("\n" + LOG_PREFIX + "java.util.Date <-> Instant");
      System.out.println(LOG_PREFIX + new java.util.Date().toInstant());
      System.out.println(LOG_PREFIX + java.util.Date.from(Instant.now()));
      // java.util.Date <-> Instant
      // 2021-01-11T08:09:53.313Z
      // Mon Jan 11 16:09:53 CST 2021

      System.out.println("\n" + LOG_PREFIX + "GregorianCalendar <-> ZonedDateTime");
      System.out.println(LOG_PREFIX + new GregorianCalendar().toZonedDateTime());
      System.out.println(LOG_PREFIX + GregorianCalendar.from(ZonedDateTime.now()));
      // GregorianCalendar <-> ZonedDateTime
      // 2021-01-11T16:09:53.438+08:00[Asia/Shanghai]
      // java.util.GregorianCalendar[time=1610352593445,areFieldsSet=true,areAllFieldsSet=true,lenient=true,zone=sun.util.calendar.ZoneInfo[id="Asia/Shanghai",offset=28800000,dstSavings=0,useDaylight=false,transitions=19,lastRule=null],firstDayOfWeek=2,minimalDaysInFirstWeek=4,ERA=1,YEAR=2021,MONTH=0,WEEK_OF_YEAR=2,WEEK_OF_MONTH=2,DAY_OF_MONTH=11,DAY_OF_YEAR=11,DAY_OF_WEEK=2,DAY_OF_WEEK_IN_MONTH=2,AM_PM=1,HOUR=4,HOUR_OF_DAY=16,MINUTE=9,SECOND=53,MILLISECOND=445,ZONE_OFFSET=28800000,DST_OFFSET=0]

      Timestamp christmas = Timestamp.valueOf("2014-12-25 18:30:00");
      System.out.println("\n" + LOG_PREFIX + "java.sql.Timestamp <-> Instant");
      System.out.println(LOG_PREFIX + christmas.toInstant());
      System.out.println(LOG_PREFIX + Timestamp.from(Instant.now()));
      // java.sql.Timestamp <-> Instant
      // 2014-12-25T10:30:00Z
      // 2021-01-11 16:09:53.447

      System.out.println("\n" + LOG_PREFIX + "java.sql.Timestamp <-> LocalDateTime");
      System.out.println(LOG_PREFIX + christmas.toLocalDateTime());
      System.out.println(LOG_PREFIX + Timestamp.valueOf(LocalDateTime.now()));
      // java.sql.Timestamp <-> LocalDateTime
      // 2014-12-25T18:30
      // 2021-01-11 16:09:53.447

      System.out.println("\n" + LOG_PREFIX + "java.sql.Date <-> LocalDate");
      System.out.println(LOG_PREFIX + java.sql.Date.valueOf("2014-12-25").toLocalDate());
      System.out.println(LOG_PREFIX + java.sql.Date.valueOf(LocalDate.now()));
      // java.sql.Date <-> LocalDate
      // 2014-12-25
      // 2021-01-11

      System.out.println("\n" + LOG_PREFIX + "java.sql.Time <-> LocalTime");
      System.out.println(LOG_PREFIX + Time.valueOf("18:30:00").toLocalTime());
      System.out.println(LOG_PREFIX + Time.valueOf(LocalTime.now()));
      // java.sql.Time <-> LocalTime
      // 18:30
      // 16:09:53
      
      System.out.println("\n" + LOG_PREFIX + "DateTimeFormatter -> DateFormat");
      MessageFormat message = new MessageFormat("Today is {0}.");
      message.setFormat(0, DateTimeFormatter.ISO_WEEK_DATE.toFormat());
      System.out.println(LOG_PREFIX + message.format(new Object[] { LocalDate.now() }) );
      // DateTimeFormatter -> DateFormat
      // Today is 2021-W02-1.
   }
}
