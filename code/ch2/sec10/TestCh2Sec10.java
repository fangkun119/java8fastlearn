import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

class Person {
   private int id;
   private String name;

   public Person(int id, String name) { this.id = id; this.name = name; }
   public int getId() { return id; }
   public String getName() { return name; }
   public String toString() { return getClass().getName() + 
         "[id=" + id + ",name=" + name + "]"; 
   }
}

public class TestCh2Sec10 {
   // 工具函数，打印运行结果
   private final static String PREFIX = "\t// ";
   public static void display(String title, Object value) {
      System.out.println("");
      System.out.println(PREFIX + title);
      System.out.println(PREFIX + value.toString());
   }


   // 测试数据
   public static Stream<Person> personStream() {
      return Stream.of(
         new Person(1001, "Peter"),
         new Person(1002, "Paul"),
         new Person(1003, "Mary"));
   } 

   public static void main(String[] args) throws IOException {
      // Collectors.toMap(keyMapper, valueMapper)

      // (1) key和value都是item的方法返回值
      Map<Integer, String> idToName = personStream().collect(Collectors.toMap(Person::getId, Person::getName));
      display("Collectors.toMap(Person::getId, Person::getName)", idToName);
      // Collectors.toMap(Person::getId, Person::getName)
      // {1001=Peter, 1002=Paul, 1003=Mary}

      // (2）value是item本身
      Map<Integer, Person> idToPerson1 = personStream().collect(Collectors.toMap(Person::getId, Function.identity()));
      display("Collectors.toMap(Person::getId, Function.identity())", idToPerson1);
      // Collectors.toMap(Person::getId, Function.identity())
      // {1001=Person[id=1001,name=Peter], 1002=Person[id=1002,name=Paul], 1003=Person[id=1003,name=Mary]}

      // Collection.toMap(keyMapper, valueMapper, mergeFunction)
      Map<String, String> languageNames = Stream.of(Locale.getAvailableLocales()).collect(
         Collectors.toMap(
            Locale::getDisplayLanguage, 
            Locale::getDisplayLanguage, 
            (existingValue, newValue) -> existingValue));
      display("(existingValue, newValue) -> existingValue", languageNames);
      // (existingValue, newValue) -> existingValue
      // {土耳其文=土耳其文, =, 意大利文=意大利文, 冰岛文=冰岛文, 印地文=印地文, ...}

      Map<String, Set<String>> countryLanguageSets = Stream.of(Locale.getAvailableLocales()).collect(
         Collectors.toMap(
            Locale::getDisplayCountry,
            l -> Collections.singleton(l.getDisplayLanguage()),
            (a, b) -> { // union of a and b
               Set<String> r = new HashSet<>(a); 
               r.addAll(b);
               return r; }));
      display("(a, b) -> {Set<String> r = new HashSet<>(a); r.addAll(b); return r;}", countryLanguageSets);
      // (a, b) -> {Set<String> r = new HashSet<>(a); r.addAll(b); return r;}
      // {新加坡=[中文, 英文], 澳大利亚=[英文], 印度尼西亚=[印度尼西亚文], 科威特=[阿拉伯文], 菲律宾=[英文], ..., =[, 土耳其文, 意大利文, 冰岛文, 印地文, ...]}

      // Collection.toMap(keyMapper, valueMapper, mergeFunction, mapSupplier)
      Map<Integer, Person> idToPerson2  = personStream().collect(
              Collectors.toMap(
                      Person::getId,
                      Function.identity(),
                      (existingValue, newValue) -> { throw new IllegalStateException(); },
                      TreeMap::new));
      display("TreeMap::new", idToPerson2);
      // TreeMap::new
      // {1001=Person[id=1001,name=Peter], 1002=Person[id=1002,name=Paul], 1003=Person[id=1003,name=Mary]}
   }
}


