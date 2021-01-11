# CH05 Date and Time

> `Date`在Java 1.0时难以使用，实例都是可变的，不能处理闰秒问题（官方秒钟偶尔插入1秒来保证计时与地球自转同步）等。它在Java 8得到修复并重新引入

[TOC]

## 5.1 时间线

`Instant`表示时间线上某一点，`Duration`表示两个Instanct之间的距离。这两个类的方法如下

> | 方法                                                 | 描述                                 |
> | ---------------------------------------------------- | ------------------------------------ |
> | plus，minus                                          | 增加/减少一段时间                    |
> | plusNanos/Millis/Seconds/Minutes/Hours/Days          | 增加/减少一段时间                    |
> | multipliedBy，dividedBy，negated（只能用于Duration） | 时间跨度乘以、除以一个long值，或取反 |

Instant和Durance都是不可变的，上面的方法都会返回一个新的实例

例子

> ```java
> Instant start = Instant.now();   // Instance表示时间轴上的一个点
> runAlgorithm();
> Instant end   = Instant.now();
> Duration timeElapsed = Duration.between(start, end); // 表示时间间隔
> System.out.printf("%d milliseconds\n", timeElapsed.toMillis() /*long*/);
> // 76 milliseconds
> ```
>
> ```java
> boolean overTenTimesFaster = timeElapsed.multipliedBy(10).minus(timeElapsed2).isNegative();
> ```
>
> 完整代码：[../code/ch5/sec01/Timeline.java](../code/ch5/sec01/Timeline.java)

## 5.2 本地日期: `LocalDate`

> 本地日期是不带时区的时间，并且能够自动考虑夏令时等因素
>
> `LocalDate`表示本地日期，`Peroid`表示这两个日期之间的间隔。LocalDate的主要方法
>
> ![](https://raw.githubusercontent.com/kenfang119/pics/main/java8fastlearn/java8_localdate.jpg)
>
> 代码：[../code/ch5/sec02/LocalDates.java](../code/ch5/sec02/LocalDates.java)
>
> ```java
> // 当前本地日期：.now()
> LocalDate today = LocalDate.now(); // Today’s date
> System.out.println("today: " + today);
> // today: 2021-01-10
> 
> // 指定本地日期：.of()
> LocalDate alonzosBirthday = LocalDate.of(1903, Month.JUNE, 14); // 等价于 LocalDate.of(1903, 6, 14);
> System.out.println("alonzosBirthday: " + alonzosBirthday);
> // alonzosBirthday: 1903-06-14
> 
> // 日期加减： .plusDays(int), .plusMonths(int), .minusMonths(int)
> System.out.println(LocalDate.of(2014, 1, 1).plusDays(255)); // September 13, but in a leap year it would be September 12
> System.out.println(LocalDate.of(2016, 1, 31).plusMonths(1));
> System.out.println(LocalDate.of(2016, 3, 31).minusMonths(1));
> System.out.println(LocalDate.of(2016,1,31).plus(Period.ofYears(1)));
> // 2014-09-13
> // 2016-02-29
> // 2016-02-29
> // 2017-01-31
> 
> // 两个日期之间的间隔： until
> LocalDate independenceDay = LocalDate.of(2014, Month.JULY, 4);
> LocalDate christmas = LocalDate.of(2014, Month.DECEMBER, 25);
> System.out.println("Until christmas: " + independenceDay.until(christmas));
> System.out.println("Until christmas: " + independenceDay.until(christmas, ChronoUnit.DAYS));
> // Until christmas: P5M21D 表示Period 5 Months 21 Days
> // Until christmas: 174
> 
> // DaysOfWeek
> DayOfWeek startOfLastMillennium = LocalDate.of(1900, 1, 1).getDayOfWeek();
> System.out.println("startOfLastMillennium: " + startOfLastMillennium);
> System.out.println(startOfLastMillennium.getValue());
> System.out.println(DayOfWeek.SATURDAY.plus(3));
> // startOfLastMillennium: MONDAY
> // 1
> // TUESDAY
> ```

## 5.3 日期矫正器：`TemporalAdjuster`、`TemporalAdjusters`

> `日期矫正函数`与`LocalDate.with`方法结合，可以以`LocalDate对象所代表的时间`为基准，叠加一个`矫正函数`，得到另一个时间

(1) 使用`TemporalAdjusters`提供的矫正函数（静态函数）

> 例如：`TemporalAdjusters.nextOrSame(...)`
>
> ~~~java
> // 基准时间：6月1日
> // 矫正规则：某一天为起点，第1个星期2
> // 计算结果：6月第1个星期2，另外with方法会返回新的LocalDate对象，不会改变已有的
> LocalDate firstTuesday = LocalDate.of(2014, 6, 1).with(
>          TemporalAdjusters.nextOrSame(DayOfWeek.TUESDAY));
> System.out.println("firstTuesday: " + firstTuesday);
> ~~~
>
> TemporalAdjusters的主要方法
>
> ![](https://raw.githubusercontent.com/kenfang119/pics/main/java8fastlearn/java8_temporaladjusters.jpg)

(2) 实现`TemporalAdjuster`接口、编写自定义矫正函数

>~~~~java
>package java.time.temporal;
>import java.time.DateTimeException;
>
>@FunctionalInterface
>public interface TemporalAdjuster {
>    Temporal adjustInto(Temporal temporal);
>}
>~~~~
>
>该接口的参数类型是`Temporal`接口，但可以把它回LocalDate类型，以使用LocalDate的方法
>
>```java
>// 基准时间：今天
>// 矫正规则：下一个工作日
>// 计算结果：从今天开始计算得出的下一个工作日
>LocalDate today = LocalDate.of(2013, 11, 9); // Saturday
>TemporalAdjuster NEXT_WORKDAY = w -> {
>    // LocalDate实现了Temporal接口
>    // 将Temporal类型转回LocalDate、以使用LocalDate的API
>    LocalDate result = (LocalDate) w; 
>    do {
>        result = result.plusDays(1);
>    } while (result.getDayOfWeek().getValue() >= 6);
>    return result;
>};
>LocalDate backToWork = today.with(NEXT_WORKDAY);
>System.out.println("backToWork: " + backToWork);
>// backToWork: 2013-11-11
>```
>
>还可以使用`TemporalAdjusters.ofDateAdjuster`静态工厂方法来生成`TemporalAdjuster`。这个方法要求的Lambda表达式参数类型、返回值类型都是`LocalDate`，不需要类型转换
>
>```java
>// 基准时间：今天
>// 矫正规则：下一个工作日
>// 计算结果：从今天开始计算得出的下一个工作日
>TemporalAdjuster NEXT_WORKDAY2 = TemporalAdjusters.ofDateAdjuster(w -> {
>    LocalDate result = w; // No cast
>    do {
>        result = result.plusDays(1);
>    } while (result.getDayOfWeek().getValue() >= 6);
>    return result;
>});
>backToWork = today.with(NEXT_WORKDAY2);
>System.out.println("backToWork: " + backToWork);
>// backToWork: 2013-11-11
>```

代码地址：[../code/ch5/sec03/DateAdjusters.java](../code/ch5/sec03/DateAdjusters.java)

## 5.4 本地时间：`LocalTime`

> `LocalTime`表示一天中的某个时间
>
> 例子如下
>
> ```java
> LocalTime rightNow = LocalTime.now();
> System.out.println("rightNow: " + rightNow);
> // rightNow: 20:31:15.776
> 
> LocalTime bedtime =  LocalTime.of(22, 30, 0); // 等价于 LocalTime.of(22, 30);              
> System.out.println("bedtime: " + bedtime);
> // bedtime: 22:30
> 
> LocalTime wakeup = bedtime.plusHours(8); // 会按照一天24小时循环
> System.out.println("wakeup: " + wakeup);
> // wakeup: 06:30
> ```
>
> `LocalTime`的方法如下
>
> ![](https://raw.githubusercontent.com/kenfang119/pics/main/java8fastlearn/java8_localtime.jpg)
>
> ![](https://raw.githubusercontent.com/kenfang119/pics/main/java8fastlearn/java8_localtime2.jpg)

## 5.5  带时区的时间

> `LocalDateTime`表示一个日期和时间用来存储确定时区中的某个时间点，但是如果需要处理跨夏令时的计算、或者要处理的数据处于不同的时区，应当使用`ZonedDateTime`

所有可用时区

> ```java
> // 获取所有可用时区
> // ZoneId.getAvailableZoneIds()
> System.out.println(
>         ZoneId.getAvailableZoneIds());
> // [Asia/Aden, America/Cuiaba, Etc/GMT+9, Etc/GMT+8, ......]
> ```

创建ZonedDateTime

> ```java
> // 创建ZonedDateTime
> // ZonedDateTime.of(int, int, int, int, int, int, int, ZoneId.of(...))
> // ZonedDateTime.of(LocalDateTime, LocalDateTime, ZoneId.of(...))
> ZonedDateTime apollo11launch = ZonedDateTime.of(
>         1969, 7, 16,
>         9, 32, 0, 0,
>          ZoneId.of("America/New_York"));
> System.out.println("apollo11launch: " + apollo11launch);
> // apollo11launch: 1969-07-16T09:32-04:00[America/New_York]
> ```

ZonedDateTime与Instant相互转化

> ```java
> // ZonedDateTime与Instant互相转化
> // ZonedDateTime.toInstance()
> // instant.atZone(ZoneId.of(...))
> Instant instant = apollo11launch.toInstant();
> System.out.println("instant: " + instant);
> // instant: 1969-07-16T13:32:00Z
> ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("UTC"));
> System.out.println("zonedDateTime: " + zonedDateTime);
> // zonedDateTime: 1969-07-16T13:32Z[UTC]
> ```

ZonedDateTime的API（与LocalDateTime非常一致）：

> [https://kapeli.com/dash_share?docset_file=Java&docset_name=Java%20SE8&path=java/time/ZonedDateTime.html&platform=java&repo=Main&version=SE8](https://kapeli.com/dash_share?docset_file=Java&docset_name=Java%20SE8&path=java/time/ZonedDateTime.html&platform=java&repo=Main&version=SE8)

ZonedDateTime与LocalDateTime主要差别在于处理夏令时冬令时切换时

> 夏令时开始时
>
> ```java
> // 夏令时开始时会跳过一小时
> // 注意 2013-03-31 02:00 Europe/Berlin 进入夏令时
> // 因此 2013-03-31 02:30 Europe/Berlin 是一个不存在的时间点，用它来创建ZoneDateTime，系统会自动纠正
> // 输出 2013-03-31 03:30 Europe/Berlin
> ZonedDateTime skipped = ZonedDateTime.of(
>         LocalDate.of(2013, 3, 31),
>         LocalTime.of(2, 30),
>         ZoneId.of("Europe/Berlin"));
> System.out.println("skipped: " + skipped);
> // ZonedDateTime.of(LocalDate, LocalTime, ZoneId.of(...))
> // skipped: 2013-03-31T03:30+02:00[Europe/Berlin]
> ```
>
> 夏令时结束时
>
> ```java
> // 当夏令时结束时，时钟会回退一小时，此时有两个拥有相同本地时间点，类库会选择两者中较早的那个
> ZonedDateTime ambiguous = ZonedDateTime.of(
>         LocalDate.of(2013, 10, 27), // End of daylight savings time
>         LocalTime.of(2, 30),
>         ZoneId.of("Europe/Berlin"));
> System.out.println("ambiguous: " + ambiguous);
> // ZonedDateTime.of(LocalDate, LocalTime, ZoneId.of(...))
> // ambiguous: 2013-10-27T02:30+02:00[Europe/Berlin]
> 
> ZonedDateTime anHourLater = ambiguous.plusHours(1);
> System.out.println("anHourLater: " + anHourLater);
> // 输出: 2013-10-27T02:30+01:00[Europe/Berlin]
> // 从下面
> //    2013-10-27T03:30+02:00[Europe/Berlin]
> //    2013-10-27T02:30+01:00[Europe/Berlin]
> // 两个时间点中选择了最早的那个，时间点与一小时之前相同只是时区偏移量改变了
> ```

给ZonedDateTime增加时间偏移量时，要注意使用与`LocalDate`搭配的`Period`，而不要使用严格计算时间线距离与`Instant`搭配的`Duration`

> 例如下面错误的方法
>
> ```java
> ZonedDateTime meeting = ZonedDateTime.of(
>         LocalDate.of(2013, 10, 31),
>         LocalTime.of(14, 30),
>         ZoneId.of("America/Los_Angeles"));
> System.out.println("meeting: " + meeting);
> // meeting: 2013-10-31T14:30-07:00[America/Los_Angeles]
> 
> ZonedDateTime nextMeeting = meeting.plus(Duration.ofDays(7));
> System.out.println("nextMeeting: " + nextMeeting);
> // nextMeeting: 2013-11-07T13:30-08:00[America/Los_Angeles]
> // 遇到跨夏令时冬令时切换点时，会计算错误，输出的是13:30而不是14:30
> ```
>
> 正确的方法如下
>
> ```java
> nextMeeting = meeting.plus(Period.ofDays(7)); // OK
> System.out.println("nextMeeting: " + nextMeeting);
> // nextMeeting: 2013-11-07T14:30-08:00[America/Los_Angeles]
> ```

## 5.6 格式化解析：`DateTimeFormatter`

### 5.6.1 使用预定义格式

#### (1) 标准格式（机器可读）

`DateTimeFormatter.ISO_DATE_TIME.format(ZonedDateTime)`

例子

> ```java
> // DateTimeFormatter.ISO_DATE_TIME.format(ZonedDateTime)
> String formatted = DateTimeFormatter.ISO_DATE_TIME.format(apollo11launch);
> System.out.println(formatted);
> // 1969-07-16T09:32:00-05:00[America/New_York]
> ```

格式样式

> ![](https://raw.githubusercontent.com/kenfang119/pics/main/java8fastlearn/java8_datetime_formatter_predefine1.jpg)
>
> ![](https://raw.githubusercontent.com/kenfang119/pics/main/java8fastlearn/java8_datetime_formatter_predefine2.jpg)

#### (2) 语言环境相关格式（对人类阅读友好）

`DateTimeFormatter.ofLocalizedDateTime(FormatStyle).withLocale(Locale).format(ZonedDateTime)`

例子

> ```java
> // 使用系统默认语言环境：
> // DateTimeFormatter.ofLocalizedDateTime(FormatStyle).format(ZonedDateTime)
> DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG);
> formatted = formatter.format(apollo11launch);
> System.out.println(formatted);
> // July 16, 1969 9:32:00 AM EDT (作者电脑）
> // 1969年7月16日 上午09时32分00秒（我的电脑）
> 
> // 指定语言环境 .withLocale(Locale)
> formatted = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL).withLocale(Locale.FRENCH).format(apollo11launch);
> System.out.println(formatted);
> // mercredi 16 juillet 1969 09 h 32 EDT
> ```

格式样式

> ![](https://raw.githubusercontent.com/kenfang119/pics/main/java8fastlearn/java8_datatime_localizedformater.jpg)

#### (3) 指定输出模式

`DateTimeFormatter.ofPattern("E yyyy-MM-dd HH:mm").format(ZonedDateTime)`

例子

> ```java
> // DateTimeFormatter.ofPattern("E yyyy-MM-dd HH:mm").format(ZonedDateTime)
> formatter = DateTimeFormatter.ofPattern("E yyyy-MM-dd HH:mm");
> formatted = formatter.format(apollo11launch);
> System.out.println(formatted);
> // 星期三 1969-07-16 09:32
> ```

常用模式

> ![](https://raw.githubusercontent.com/kenfang119/pics/main/java8fastlearn/java8_dateformat_ofpattern_or_parse.jpg)

#### (4) 从字符串解析得到输出模式

`LocalDate.parse("YYYY-MM-DD")`

` ZonedDateTime.parse(String, DateTimeFormatter.ofPattern(String))`

例子

> ~~~java
> // LocalDate.parse("YYYY-MM-DD")
> LocalDate churchsBirthday = LocalDate.parse("1903-06-14");
> System.out.println("churchsBirthday: " + churchsBirthday);
> // churchsBirthday: 1903-06-14
> 
> // ZonedDateTime.parse(String, DateTimeFormatter.ofPattern(String))
> apollo11launch =
>    ZonedDateTime.parse("1969-07-16 03:32:00-0400",
>       DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssxx"));
> System.out.println("apollo11launch: " + apollo11launch);
> // apollo11launch: 1969-07-16T03:32-04:00
> ~~~

## 5.7 与老式API互操作

老式API

> * java.util.Date
> * java.util.GregorianCalendar
> * Java.sql.Date/Time/Timestamp

例子：

(1) `Instant`类似于老式的`java.util.Date`类，可以相互转换

> ~~~java
> System.out.println("\n" + LOG_PREFIX + "java.util.Date <-> Instant");
> System.out.println(LOG_PREFIX + new java.util.Date().toInstant());
> System.out.println(LOG_PREFIX + java.util.Date.from(Instant.now()));
> // java.util.Date <-> Instant
> // 2021-01-11T08:09:53.313Z
> // Mon Jan 11 16:09:53 CST 2021
> ~~~

(2) `ZonedDateTime`类似老式的`GregorianCalendar`，可以相互转换

> ~~~java
> System.out.println("\n" + LOG_PREFIX + "GregorianCalendar <-> ZonedDateTime");
> System.out.println(LOG_PREFIX + new GregorianCalendar().toZonedDateTime());
> System.out.println(LOG_PREFIX + GregorianCalendar.from(ZonedDateTime.now()));
> // GregorianCalendar <-> ZonedDateTime
> // 2021-01-11T16:09:53.438+08:00[Asia/Shanghai]
> // java.util.GregorianCalendar[time=1610352593445,areFieldsSet=true,areAllFieldsSet=true,lenient=true,zone=sun.util.calendar.ZoneInfo[id="Asia/Shanghai",offset=28800000,dstSavings=0,useDaylight=false,transitions=19,lastRule=null],firstDayOfWeek=2,minimalDaysInFirstWeek=4,ERA=1,YEAR=2021,MONTH=0,WEEK_OF_YEAR=2,WEEK_OF_MONTH=2,DAY_OF_MONTH=11,DAY_OF_YEAR=11,DAY_OF_WEEK=2,DAY_OF_WEEK_IN_MONTH=2,AM_PM=1,HOUR=4,HOUR_OF_DAY=16,MINUTE=9,SECOND=53,MILLISECOND=445,ZONE_OFFSET=28800000,DST_OFFSET=0]
> ~~~

(3) `Instant`、`LocalDateTime`与`java.sql.TimeStamp`之间的相互转换

> ```java
> Timestamp christmas = Timestamp.valueOf("2014-12-25 18:30:00");
> System.out.println("\n" + LOG_PREFIX + "java.sql.Timestamp <-> Instant");
> System.out.println(LOG_PREFIX + christmas.toInstant());
> System.out.println(LOG_PREFIX + Timestamp.from(Instant.now()));
> // java.sql.Timestamp <-> Instant
> // 2014-12-25T10:30:00Z
> // 2021-01-11 16:09:53.447
> 
> System.out.println("\n" + LOG_PREFIX + "java.sql.Timestamp <-> LocalDateTime");
> System.out.println(LOG_PREFIX + christmas.toLocalDateTime());
> System.out.println(LOG_PREFIX + Timestamp.valueOf(LocalDateTime.now()));
> // java.sql.Timestamp <-> LocalDateTime
> // 2014-12-25T18:30
> // 2021-01-11 16:09:53.447
> ```

(4) `LocalDate`、`LocalTime`与`java.sql.Date`、`java.sql.Time`之间的相互转换

> ~~~java
> System.out.println("\n" + LOG_PREFIX + "java.sql.Date <-> LocalDate");
> System.out.println(LOG_PREFIX + java.sql.Date.valueOf("2014-12-25").toLocalDate());
> System.out.println(LOG_PREFIX + java.sql.Date.valueOf(LocalDate.now()));
> // java.sql.Date <-> LocalDate
> // 2014-12-25
> // 2021-01-11
> 
> System.out.println("\n" + LOG_PREFIX + "java.sql.Time <-> LocalTime");
> System.out.println(LOG_PREFIX + Time.valueOf("18:30:00").toLocalTime());
> System.out.println(LOG_PREFIX + Time.valueOf(LocalTime.now()));
> // java.sql.Time <-> LocalTime
> // 18:30
> // 16:09:53
> ~~~

(5) 向老式的`java.text.Format`中传入新式的`DateTimeFormatter`对象，以新的方式格式化

> ~~~java
> System.out.println("\n" + LOG_PREFIX + "DateTimeFormatter -> DateFormat");
> MessageFormat message = new MessageFormat("Today is {0}.");
> message.setFormat(0, DateTimeFormatter.ISO_WEEK_DATE.toFormat());
> System.out.println(LOG_PREFIX + message.format(new Object[] { LocalDate.now() }) );
> // DateTimeFormatter -> DateFormat
> // Today is 2021-W02-1.
> ~~~

转换方法小结

> ![](https://raw.githubusercontent.com/kenfang119/pics/main/java8fastlearn/java8_datetime_format_convert1.jpg)
>
> ![](https://raw.githubusercontent.com/kenfang119/pics/main/java8fastlearn/java8_datetime_format_convert2.jpg)

