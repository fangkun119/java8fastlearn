import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.Comparator;

/**
 * Lambda表达式的完整写法：
 * (ParamType1 param1, ParamType2 param2) -> {
 * code_1_with(param1, param2);
 * code_2_with(param1, param2);
 * }
 * Lambda表达式的简写：
 * 1. 只有一行代码时，代码块首尾的"{","}"可以被省略
 * 2. 参数类型可以被推导时，"ParamType1", "ParamType2"可以省略
 * 3. 没有参数时，可以只保留头部的一对小括号"()"，例如：() -> { for (int i = 0; i < 1000; ++i) doWork(); }
 * 4. 只有一个参数、且类型可推导时，头部的小括号"()"可以省略、例如：
 * EventHandler<ActionEvent> listener = event -> System.out.println("Thanks");
 * <p>
 * Lambda表达式的参数、可以添加与方法参数一样的注释或final修饰符，例如：
 * (final String name) -> {...}
 * (@NonNull String name) -> {...}
 * Lambda表达式的返回值类型可以被自动推导
 */

public class Lambdas {
    public static void main(String[] args) {
        // 以1.1中的`LengthComparator`为例，可以替换成如下的lambda表达式
        Comparator<String> comp = null;
        comp = (String first, String second) -> Integer.compare(first.length(), second.length());
        comp = (first, second) -> Integer.compare(first.length(), second.length());
        comp = (String first, String second) -> {
                    if (first.length() < second.length()) return -1;
                    else if (first.length() > second.length()) return 1;
                    else return 0;
                };

        // 以1.1中的Worker类为例，可以替换成形式如下的lambda表达式
        Runnable runner = () -> {
            for (int i = 0; i < 1000; i++) doWork();
        };

        // 以1.1中的`EventHandler<ActionEvent>的内部匿名子类`为例，可以替换成如下lambda表达式
        EventHandler<ActionEvent> listener = null;
        listener = e -> System.out.println(e.getTarget());
        listener = (e) -> System.out.println(e.getTarget());
        listener = (ActionEvent e) -> System.out.println(e.getTarget());
    }

    public static void doWork() {
    }
}
