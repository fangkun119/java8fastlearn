import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Locales {
    public static void main(String[] args) {
        // List中包含两个LanguageRange，分别表示"说德语"，以及"在瑞士"
        List<Locale.LanguageRange> priorityRangeList
                = Stream.of("de", "*-CH")
                        .map(Locale.LanguageRange::new)
                        .collect(Collectors.toList());

        // 用两个LanguageRange进行筛选，返回所有匹配的Local
        List<Locale> matches
                = Locale.filter(
                        priorityRangeList,
                        Arrays.asList(Locale.getAvailableLocales()));
        System.out.println(matches);
        // 输出
        // [de_IT, de_CH, de_BE, de, de_LU, de_DE, de_LI, de_AT, pt_CH, gsw_CH, fr_CH, rm_CH, it_CH, wae_CH, en_CH]

        // 用两个LanguageRange进行筛选，返回匹配度最高的Local
        Locale bestMatch = Locale.lookup(
                priorityRangeList,
                Arrays.asList(Locale.getAvailableLocales()));
        System.out.println(bestMatch);
        // 输出
        // de
    }
}
