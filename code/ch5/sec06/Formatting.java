import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class Formatting {
   public static void main(String[] args) {

      ZonedDateTime apollo11launch = ZonedDateTime.of(1969, 7, 16, 9, 32, 0, 0,
         ZoneId.of("America/New_York"));


      // DateTimeFormatter.ISO_DATE_TIME.format(ZonedDateTime)
      String formatted = DateTimeFormatter.ISO_DATE_TIME.format(apollo11launch);
      System.out.println(formatted);
      // 1969-07-16T09:32:00-05:00[America/New_York]


      // DateTimeFormatter.ofLocalizedDateTime(FormatStyle).format(ZonedDateTime)
      // 使用系统默认的语言环境
      DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG);
      formatted = formatter.format(apollo11launch);
      System.out.println(formatted);
      // * July 16, 1969 9:32:00 AM EDT (作者电脑）
      // * 1969年7月16日 上午09时32分00秒（我的电脑）
      // DateTimeFormatter.withLocale(Locale).format(ZonedDateTime)

      // 指定语言环境 .withLocale(Locale)
      formatted = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL).withLocale(Locale.FRENCH).format(apollo11launch);
      System.out.println(formatted);
      // mercredi 16 juillet 1969 09 h 32 EDT

      // DateTimeFormatter.ofPattern("E yyyy-MM-dd HH:mm").format(ZonedDateTime)
      formatter = DateTimeFormatter.ofPattern("E yyyy-MM-dd HH:mm");
      formatted = formatter.format(apollo11launch);
      System.out.println(formatted);
      // 星期三 1969-07-16 09:32

      // LocalDate.parse("YYYY-MM-DD")
      LocalDate churchsBirthday = LocalDate.parse("1903-06-14");
      System.out.println("churchsBirthday: " + churchsBirthday);
      // churchsBirthday: 1903-06-14

      // ZonedDateTime.parse(String, DateTimeFormatter.ofPattern(String))
      apollo11launch =
         ZonedDateTime.parse("1969-07-16 03:32:00-0400",
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssxx"));
      System.out.println("apollo11launch: " + apollo11launch);
      // apollo11launch: 1969-07-16T03:32-04:00
   }
}
