import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

public class DateAdjusters {
    public static void main(String[] args) {
        // 基准时间：6月1日
        // 矫正规则：某一天为起点，第1个星期2
        // 计算结果：6月第1个星期2
        LocalDate firstTuesday = LocalDate.of(2014, 6, 1).with(
                TemporalAdjusters.nextOrSame(DayOfWeek.TUESDAY));
        System.out.println("firstTuesday: " + firstTuesday);
        // firstTuesday: 2014-06-03

        // 基准时间：今天
        // 矫正规则：下一个工作日
        // 计算结果：从今天开始计算得出的下一个工作日
        LocalDate today = LocalDate.of(2013, 11, 9); // Saturday
        TemporalAdjuster NEXT_WORKDAY = w -> {
            LocalDate result = (LocalDate) w;
            do {
                result = result.plusDays(1);
            } while (result.getDayOfWeek().getValue() >= 6);
            return result;
        };
        LocalDate backToWork = today.with(NEXT_WORKDAY);
        System.out.println("backToWork: " + backToWork);
        // backToWork: 2013-11-11

        // 基准时间：今天
        // 矫正规则：下一个工作日
        // 计算结果：从今天开始计算得出的下一个工作日
        TemporalAdjuster NEXT_WORKDAY2 = TemporalAdjusters.ofDateAdjuster(w -> {
            LocalDate result = w; // No cast
            do {
                result = result.plusDays(1);
            } while (result.getDayOfWeek().getValue() >= 6);
            return result;
        });
        backToWork = today.with(NEXT_WORKDAY2);
        System.out.println("backToWork: " + backToWork);
        // backToWork: 2013-11-11
    }
}
