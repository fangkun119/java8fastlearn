import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

class Person {
    private String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getFirstName() {
        return name.substring(0, name.indexOf(' '));
    }

    public String getMiddleName() {
        int space1 = name.indexOf(" ");
        int space2 = name.lastIndexOf(" ");
        if (space1 == space2) return null;
        else return name.substring(space1 + 1, space2);
    }

    public String getLastName() {
        return name.substring(name.lastIndexOf(' ') + 1);
    }

    public String toString() {
        return name;
    }
}

class TestData {
    public static Person[] personArray = {
            new Person("George Washington"),
            new Person("John Adams"),
            new Person("Thomas Jefferson"),
            new Person("James Madison"),
            new Person("James Monroe"),
            new Person("John Quincy Adams"),
            new Person("Andrew Jackson"),
            new Person("Martin VanBuren"),
            new Person("William Henry Harrison"),
            new Person("John Tyler"),
            new Person("James Knox Polk"),
            new Person("Zachary Taylor"),
            new Person("Millard Fillmore"),
            new Person("Franklin Pierce"),
            new Person("James Buchanan"),
            new Person("Abraham Lincoln"),
            new Person("Andrew Johnson"),
            new Person("Ulysses S. Grant"),
            new Person("Rutherford Birchard Hayes"),
            new Person("James Abram Garfield"),
            new Person("Grover Cleveland"),
            new Person("Benjamin Harrison"),
            new Person("Grover Cleveland"),
            new Person("William McKinley"),
            new Person("Theodore Roosevelt"),
            new Person("William Howard Taft"),
            new Person("Woodrow Wilson"),
            new Person("Warren Gamaliel Harding"),
            new Person("Calvin Coolidge"),
            new Person("Herbert Hoover"),
            new Person("Franklin Delano Roosevelt"),
            new Person("Harry S. Truman"),
            new Person("Dwight David Eisenhower"),
            new Person("John Fitzgerald Kennedy"),
            new Person("Lyndon Baines Johnson"),
            new Person("Richard Mulhouse Nixon"),
            new Person("Gerald Ford"),
            new Person("James Earl Carter"),
            new Person("Ronald Reagan"),
            new Person("George Herbert Walker Bush"),
            new Person("William Jefferson Clinton"),
            new Person("George Walker Bush"),
            new Person("Barack Hussein Obama"),
    };
}

/**
 * 比较器：
 * 1. Comparator.comparing 静态方法提供的比较器，接收一个“Key Extractor”，返回一个Comparator对象
 * 2. Comparator.thenComparing与Comparator.comparing结合使用，来进行多级比较
 * 3. Comparator.comparing可接收一个key比较器（下面的lambda语句），指定如何比较被“Key Extractor”取出来的key
 * 4. Comparator.comparingInt/Long/Double方法，用来避免int/long/double的装箱、拆箱
 * 5. nullsFirst(naturalOrder())或nullsLast(naturalOrder())来让null值排在最前或最后，而不是抛异常
 * 6. reversed()用来颠倒任何一个比较器，例如natrualOrder().reversed()等价于reverseOrder()
 */
public class ComparatorsTest {
    private static final String NEW_LOG_PREFIX = "\n\t// ";

    private static <T> void display(T[] array, int maxNum) {
        String content = Stream.of(array).limit(maxNum).map(T::toString).collect(Collectors.joining(","));
        System.out.println(NEW_LOG_PREFIX + "[" + content + "...]");
    }

    public static void main(String[] args) {
        // 基于字段比较
        // Comparator.comparing(Function<? super T, ? extends U> keyExtractor)
        Arrays.sort(
                TestData.personArray,
                Comparator.comparing(Person::getName)
        );
        display(TestData.personArray, 5);
        // [Abraham Lincoln,Andrew Jackson,Andrew Johnson,Barack Hussein Obama,Benjamin Harrison...]


        // 多级比较
        // Comparator.comparing(Function<? super T, ? extends U> keyExtractor)
        // Comparator.thenComparing(Function<? super T, ? extends U> keyExtractor)
        Arrays.sort(
                TestData.personArray,
                Comparator
                    .comparing(Person::getLastName)
                    .thenComparing(Person::getFirstName)
        );
        display(TestData.personArray, 5);
        // [John Adams,John Quincy Adams,James Buchanan,George Herbert Walker Bush,George Walker Bush...]


        // 基于字段及排序规则来比较
        // Comparator.comparing(
        //            Function<? super T, ? extends U> keyExtractor,
        //            Comparator<? super U> keyComparator)
        Arrays.sort(
                TestData.personArray,
                Comparator.comparing(
                    Person::getName,
                    (s, t) -> Integer.compare(s.length(), t.length())
                )
        );
        display(TestData.personArray, 5);
        // [John Adams,John Tyler,Gerald Ford,James Monroe,James Madison...]


        // 用来避免int/long/double的装箱拆箱的比较器
        // Comparator.comparingInt(ToIntFunction<? super T> keyExtractor)
        // Comparator.comparingLong(ToIntFunction<? super T> keyExtractor)
        // Comparator.comparingDouble(ToIntFunction<? super T> keyExtractor)
        Arrays.sort(
                TestData.personArray,
                Comparator.comparingInt(p -> p.getName().length())
        );
        display(TestData.personArray, 5);
        // [John Adams,John Tyler,Gerald Ford,James Monroe,James Madison...]


        // 兼容null值的比较，让null值排在最前或最后，而不是抛异常
        // Comparator.nullsFirst(Comparator<? super T> comparator)
        // nullsLast(Comparator<? super T> comparator)
        //
        // Comparator.自然顺序
        // Comparator.naturalOrder()
        Arrays.sort(
                TestData.personArray,
                Comparator.comparing(
                        Person::getMiddleName,
                        nullsFirst(naturalOrder())
                )
        );
        display(TestData.personArray, 5);
        // [John Adams,John Tyler,Gerald Ford,James Monroe,James Madison...]


        // 倒序排序
        // Comparator.reversed()
        Arrays.sort(
                TestData.personArray,
                Comparator.comparing(
                    Person::getMiddleName,
                    // Comparator<T>.natrualOrder().reversed()等价于Comparator<T>.reverseOrder()
                    nullsFirst(Comparator.<String>naturalOrder().reversed())
                )
        );
        display(TestData.personArray, 5);
        // [John Adams,John Tyler,Gerald Ford,James Monroe,James Madison...]
    }
}
