import java.time.*;

public class ZonedTimes {
    public static void main(String[] args) {
        // 获取所有可用时区
        // ZoneId.getAvailableZoneIds()
        System.out.println(
                ZoneId.getAvailableZoneIds());
        // [Asia/Aden, America/Cuiaba, Etc/GMT+9, Etc/GMT+8, ......]


        // 创建ZonedDateTime
        // ZonedDateTime.of(int, int, int, int, int, int, int, ZoneId.of(...))
        // ZonedDateTime.of(LocalDateTime, LocalDateTime, ZoneId.of(...))
        ZonedDateTime apollo11launch = ZonedDateTime.of(
                1969, 7, 16,
                9, 32, 0, 0,
                ZoneId.of("America/New_York"));
        System.out.println("apollo11launch: " + apollo11launch);
        // apollo11launch: 1969-07-16T09:32-04:00[America/New_York]


        // ZonedDateTime与Instant互相转化
        // ZonedDateTime.toInstance()
        // instant.atZone(ZoneId.of(...))
        Instant instant = apollo11launch.toInstant();
        System.out.println("instant: " + instant);
        // instant: 1969-07-16T13:32:00Z
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("UTC"));
        System.out.println("zonedDateTime: " + zonedDateTime);
        // zonedDateTime: 1969-07-16T13:32Z[UTC]


        // 夏令时开始时会跳过一小时
        // 注意 2013-03-31 02:00 Europe/Berlin 进入夏令时
        // 因此 2013-03-31 02:30 Europe/Berlin 是一个不存在的时间点，用它来创建ZoneDateTime，系统会自动纠正
        // 输出 2013-03-31 03:30 Europe/Berlin
        ZonedDateTime skipped = ZonedDateTime.of(
                LocalDate.of(2013, 3, 31),
                LocalTime.of(2, 30),
                ZoneId.of("Europe/Berlin"));
        System.out.println("skipped: " + skipped);
        // ZonedDateTime.of(LocalDate, LocalTime, ZoneId.of(...))
        // skipped: 2013-03-31T03:30+02:00[Europe/Berlin]


        // 当夏令时结束时，时钟会回退一小时，此时有两个拥有相同本地时间点，类库会选择两者中较早的那个
        ZonedDateTime ambiguous = ZonedDateTime.of(
                LocalDate.of(2013, 10, 27), // End of daylight savings time
                LocalTime.of(2, 30),
                ZoneId.of("Europe/Berlin"));
        System.out.println("ambiguous: " + ambiguous);
        // ZonedDateTime.of(LocalDate, LocalTime, ZoneId.of(...))
        // ambiguous: 2013-10-27T02:30+02:00[Europe/Berlin]

        ZonedDateTime anHourLater = ambiguous.plusHours(1);
        System.out.println("anHourLater: " + anHourLater);
        // 输出: 2013-10-27T02:30+01:00[Europe/Berlin]
        // 从下面
        //    2013-10-27T03:30+02:00[Europe/Berlin]
        //    2013-10-27T02:30+01:00[Europe/Berlin]
        // 两个时间点中选择了最早的那个

        ZonedDateTime meeting = ZonedDateTime.of(
                LocalDate.of(2013, 10, 31),
                LocalTime.of(14, 30),
                ZoneId.of("America/Los_Angeles"));
        System.out.println("meeting: " + meeting);
        // meeting: 2013-10-31T14:30-07:00[America/Los_Angeles]

        ZonedDateTime nextMeeting = meeting.plus(Duration.ofDays(7));
        System.out.println("nextMeeting: " + nextMeeting);
        // nextMeeting: 2013-11-07T13:30-08:00[America/Los_Angeles]
        // 遇到跨夏令时冬令时切换点时，会计算错误，输出的是13:30而不是14:30

        nextMeeting = meeting.plus(Period.ofDays(7)); // OK
        System.out.println("nextMeeting: " + nextMeeting);
        // nextMeeting: 2013-11-07T14:30-08:00[America/Los_Angeles]
    }
}
