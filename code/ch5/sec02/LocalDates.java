import java.time.*;
import java.time.temporal.*;
import java.util.Calendar;

public class LocalDates {
   public static void main(String[] args) {
      // 当前本地日期：.now()
      LocalDate today = LocalDate.now(); // Today’s date
      System.out.println("today: " + today);
      // today: 2021-01-10

      // 指定本地日期：.of()
      LocalDate alonzosBirthday = LocalDate.of(1903, Month.JUNE, 14); // 等价于 LocalDate.of(1903, 6, 14);
      System.out.println("alonzosBirthday: " + alonzosBirthday);
      // alonzosBirthday: 1903-06-14

      // 日期加减： .plusDays(int), .plusMonths(int), .minusMonths(int)
      System.out.println(LocalDate.of(2014, 1, 1).plusDays(255)); // September 13, but in a leap year it would be September 12
      System.out.println(LocalDate.of(2016, 1, 31).plusMonths(1));
      System.out.println(LocalDate.of(2016, 3, 31).minusMonths(1));
      System.out.println(LocalDate.of(2016,1,31).plus(Period.ofYears(1)));
      // 2014-09-13
      // 2016-02-29
      // 2016-02-29
      // 2017-01-31

      // 两个日期之间的间隔： until
      LocalDate independenceDay = LocalDate.of(2014, Month.JULY, 4);
      LocalDate christmas = LocalDate.of(2014, Month.DECEMBER, 25);
      System.out.println("Until christmas: " + independenceDay.until(christmas));
      System.out.println("Until christmas: " + independenceDay.until(christmas, ChronoUnit.DAYS));
      // Until christmas: P5M21D 表示Period 5 Months 21 Days
      // Until christmas: 174

      // DaysOfWeek
      DayOfWeek startOfLastMillennium = LocalDate.of(1900, 1, 1).getDayOfWeek();
      System.out.println("startOfLastMillennium: " + startOfLastMillennium);
      System.out.println(startOfLastMillennium.getValue());
      System.out.println(DayOfWeek.SATURDAY.plus(3));
      // startOfLastMillennium: MONDAY
      // 1
      // TUESDAY
   }
}
