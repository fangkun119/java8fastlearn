import java.time.*;

public class LocalTimes {
   public static void main(String[] args) {
      LocalTime rightNow = LocalTime.now();
      System.out.println("rightNow: " + rightNow);
      // rightNow: 20:31:15.776

      LocalTime bedtime =  LocalTime.of(22, 30, 0); // 等价于 LocalTime.of(22, 30);
      System.out.println("bedtime: " + bedtime);
      // bedtime: 22:30
      
      LocalTime wakeup = bedtime.plusHours(8); // 会按照一天24小时循环
      System.out.println("wakeup: " + wakeup);
      // wakeup: 06:30
   }
}
