import java.lang.annotation.*;

// 标注为"可重复"，同时提供"容器注解"
@Repeatable(TestCases.class)
public @interface TestCase {
   String params();
   String expected();   
}
