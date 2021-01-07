import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.*;

class City {
   private String name;
   private String state;
   private int population;

   public City(String name, String state, int population) {
      this.name = name;
      this.state = state;
      this.population = population;
   }

   public String getName() { return name; }
   public String getState() { return state; }
   public int getPopulation() { return population; }
}

public class TestCh2Sec11 {
   // 工具函数，打印运行结果
   private final static String LOG_PREFIX = "\t// ";

   public static void display(String title, Object value) {
      System.out.println("");
      System.out.println(LOG_PREFIX + title);
      System.out.println(LOG_PREFIX + value.toString());
   }

   public static <K, V> void display(String title, Map<K,V> map) {
      System.out.println("");
      System.out.println(
         LOG_PREFIX + "[" + map.getClass().getName() + "]: " + title);
      System.out.println(
         LOG_PREFIX + "[" +
                  map.entrySet().stream()
                          .filter(kvEntry -> {return kvEntry.getKey().toString().length() > 0;})
                          .limit(5)
                          .map(Object::toString)
                          .collect(Collectors.joining(", "))
                  + ", ...]");
   }

   // 输入数据
   public static Stream<Locale> localeStream() {
      return Stream.of(Locale.getAvailableLocales());
   }

   public static Stream<City> cityStream() throws IOException {
      return
         Files.lines(
               Paths.get("./code/ch2/sec11/cities.txt"))
              .map(l -> l.split(", "))
              .map(a -> new City(a[0], a[1], Integer.parseInt(a[2]))
            );
   }

   public static void main(String[] args) throws IOException {

      // Collectors.groupingBy(classifier))
      Map<String, List<Locale>> countryToLocales
              = localeStream().collect(Collectors.groupingBy(Locale::getCountry));
      display("Collectors.groupingBy(Locale::getCountry))", countryToLocales);
      System.out.println(LOG_PREFIX + "Swiss locales: " + countryToLocales.get("CH"));
      // Collectors.groupingBy(Locale::getCountry))
      // [DE=[de_DE], PR=[es_PR], HK=[zh_HK], TW=[zh_TW], PT=[pt_PT], ...]
      // Swiss locales: [fr_CH, de_CH, it_CH]


      // Collectors.partitioningBy(predicate)
      Map<Boolean, List<Locale>> englishAndOtherLocales
              = localeStream().collect(
                      Collectors.partitioningBy(l -> l.getLanguage().equals("en")));
      display("English locales: ", englishAndOtherLocales.get(true));
      // English locales:
      // [en_US, en_SG, en_MT, en, en_PH, en_NZ, en_ZA, en_AU, en_IE, en_CA, en_IN, en_GB]


      // Collectors.groupingBy(classifier, downstream)
      // (1) Collectors.toSet()/toList()/toMap()/toConcurrentMap()
      Map<String, Set<Locale>> countryToLocaleSet
              = localeStream().collect(
                      Collectors.groupingBy(Locale::getCountry, toSet()));
      display("Collectors.groupingBy(Locale::getCountry, toSet())", countryToLocaleSet);
      // [java.util.HashMap]: Collectors.groupingBy(Locale::getCountry, toSet())
      // [DE=[de_DE], PR=[es_PR], HK=[zh_HK], TW=[zh_TW], PT=[pt_PT], ...]


      // (2) Collectors.counting()
      Map<String, Long> countryToLocaleCounts
              = localeStream().collect(
                      groupingBy(Locale::getCountry, counting()));
      display("Collectors.groupingBy(Locale::getCountry, Collectors.counting())", countryToLocaleCounts);
      // [java.util.HashMap]: Collectors.groupingBy(Locale::getCountry, Collectors.counting())
      // [DE=1, PR=1, HK=1, TW=1, PT=1, ...]


      Map<String, Integer> stateToCityPopulation
              = cityStream().collect(
                      groupingBy(City::getState, summingInt(City::getPopulation)));
      display("Collectors.groupingBy(City::getState, Collectors.summingInt(City::getPopulation))", stateToCityPopulation);
      // [java.util.HashMap]: Collectors.groupingBy(City::getState, Collectors.summingInt(City::getPopulation))
      // [DE=71292, HI=345610, TX=13748465, MA=2403297, MD=869891, ...]


      Map<String, Optional<String>> stateToLongestCityName
              = cityStream().collect(
                      groupingBy(
                              City::getState,
                              mapping(City::getName, maxBy(Comparator.comparing(String::length)))
                      ));
      display("mapping(City::getName, maxBy(Comparator.comparing(String::length)))", stateToLongestCityName);
      // [java.util.HashMap]: mapping(City::getName, maxBy(Comparator.comparing(String::length)))
      // [DE=Optional[Wilmington], HI=Optional[Honolulu], TX=Optional[North Richland Hills], MA=Optional[Springfield], MD=Optional[Gaithersburg], ...]


      Map<String, IntSummaryStatistics> stateToCityPopulationSummary
              = cityStream().collect(
                      groupingBy(
                              City::getState,
                              summarizingInt(City::getPopulation)
                      ));
      display(
              "summarizingInt(City::getPopulation) by example of NY",
              stateToCityPopulationSummary.get("NY"));
      // summarizingInt(City::getPopulation) by example of NY
      // IntSummaryStatistics{count=14, sum=9733274, min=49722, average=695233.857143, max=8336697}


      Map<String, Set<String>> countryToLanguages
              = localeStream().collect(
              groupingBy(
                      Locale::getDisplayCountry,
                      mapping(Locale::getDisplayLanguage, toSet())
              ));
      display("mapping(Locale::getDisplayLanguage, toSet())", countryToLanguages);
      // [java.util.HashMap]: mapping(Locale::getDisplayLanguage, toSet())
      // [泰国=[泰文], 巴西=[葡萄牙文], 塞尔维亚及黑山=[塞尔维亚文], 丹麦=[丹麦文], 塞尔维亚=[塞尔维亚文], ...]


      Map<String, String> stateToCityNames1
              = cityStream().collect(
              groupingBy(
                      City::getState,
                      mapping(City::getName, joining(", "))
              ));
      display("mapping(City::getName, joining(\", \") // by example of MD", stateToCityNames1.get("MD"));
      // mapping(City::getName, joining(", ") // by example of MD
      // Baltimore, Frederick, Rockville, Gaithersburg, Bowie


      Map<String, String> stateToCityNames2
              = cityStream().collect(
                      groupingBy(
                              City::getState,
                              reducing(
                                      "",
                                      City::getName,
                                      (s, t) -> s.length() == 0 ? t : s + ", " + t
                              )
                      ));
      display(
              "reducing(\"\", City::getName, (s, t) -> s.length() == 0 ? t : s + \", \" + t) // by example of MD",
              stateToCityNames2.get("MD"));
      // reducing("", City::getName, (s, t) -> s.length() == 0 ? t : s + ", " + t) // by example of MD
      // Baltimore, Frederick, Rockville, Gaithersburg, Bowie

   }
}


